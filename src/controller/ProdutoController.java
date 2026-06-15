package controller;

import model.Produto;
import model.ProdutoDAO;
import model.LogAuditoria;
import model.LogAuditoriaDAO;
import view.ProdutoView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class ProdutoController {

    private final ProdutoDAO dao;
    private final LogAuditoriaDAO logDAO;
    DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public ProdutoController() {
        this.dao = new ProdutoDAO();
        this.logDAO = new LogAuditoriaDAO();
    }

    public void salvarProduto(ProdutoView view) {

        if (!view.isAdmin()) {
            view.exibirMensagem("Acesso negado! Apenas administradores podem cadastrar produtos.");
            return;
        }

        if (view.getTxtNome().getText().isEmpty()) {
            view.exibirMensagem("Preencha o nome do produto!");
            return;
        }

        try {
            int estoque = Validador.parseInteiroNaoNegativo(view.getTxtEstoque().getText(), "estoque");
            double preco = Validador.parseDecimalNaoNegativo(view.getTxtPreco().getText(), "preço");

            String dataFabricacaoTexto = view.getTxtDataFabricacao().getText().trim();
            String dataValidadeTexto = view.getTxtDataValidade().getText().trim();

            LocalDate dataFabricacao = null;
            LocalDate dataValidade = null;

            if (!dataFabricacaoTexto.isEmpty()) {
                dataFabricacao = LocalDate.parse(dataFabricacaoTexto, formato);
            }
            if (!dataValidadeTexto.isEmpty()) {
                dataValidade = LocalDate.parse(dataValidadeTexto, formato);
            }

            Produto produto = new Produto(
                    view.getTxtNome().getText(),
                    (String) view.getCbTipo().getSelectedItem(),
                    estoque,
                    preco,
                    (String) view.getCbStatus().getSelectedItem(),
                    dataFabricacao,
                    dataValidade
            );

            boolean cadastro = dao.inserir(produto);
            if (cadastro){
                view.exibirMensagem("Produto "+ view.getTxtNome().getText() + " cadastrado com sucesso!");
                atualizarTotal(view);
                view.limparCampos();
                carregarTabela(view);
            }else{
                view.exibirMensagem("Erro ao cadastrar produto...");
            }

        } catch (NumberFormatException e) {
            view.exibirMensagem("Estoque e preço inválidos!");

        } catch (Exception e) {
            view.exibirMensagem("Data inválida! Use DD/MM/AAAA");
        }
    }

    public void editarProduto(ProdutoView view) {

        if (!view.isAdmin()) {
            view.exibirMensagem("Acesso negado!");
            return;
        }

        int linha = view.getTabela().getSelectedRow();

        if (linha < 0) {
            view.exibirMensagem("Selecione um produto!");
            return;
        }

        int id = (int) view.getModel().getValueAt(linha, 0);

        try {
            int estoque = Validador.parseInteiroNaoNegativo(view.getTxtEstoque().getText(), "estoque");
            double preco = Validador.parseDecimalNaoNegativo(view.getTxtPreco().getText(), "preço");

            String dataFabricacaoTexto = view.getTxtDataFabricacao().getText().trim();
            String dataValidadeTexto = view.getTxtDataValidade().getText().trim();

            LocalDate dataFabricacao = null;
            LocalDate dataValidade = null;

            if (!dataFabricacaoTexto.isEmpty()) {
                dataFabricacao = LocalDate.parse(dataFabricacaoTexto, formato);
            }
            if (!dataValidadeTexto.isEmpty()) {
                dataValidade = LocalDate.parse(dataValidadeTexto, formato);
            }

            Produto produto = new Produto(
                    view.getTxtNome().getText(),
                    (String) view.getCbTipo().getSelectedItem(),
                    estoque,
                    preco,
                    (String) view.getCbStatus().getSelectedItem(),
                    dataFabricacao,
                    dataValidade
            );

            produto.setId(id);

            boolean editado = dao.editar(produto);

            if (editado) {
                view.exibirMensagem("Produto editado com sucesso!");
                carregarTabela(view);
                atualizarTotal(view);
                view.limparCampos();
            } else {
                view.exibirMensagem("Erro ao editar produto!");
            }

        } catch (NumberFormatException e) {
            view.exibirMensagem("Estoque e preço inválidos!");

        } catch (Exception e) {
            view.exibirMensagem("Data inválida! Use DD/MM/AAAA");
        }
    }

    public void pesquisarProdutos(ProdutoView view) {

        String termo = view.getTxtPesquisa().getText();
        String filtroTipo = (String) view.getCbFiltroTipo().getSelectedItem();

        DefaultTableModel model = view.getModel();
        model.setRowCount(0);

        List<Produto> produtos = dao.pesquisar(termo);

        for (Produto p : produtos) {

            assert filtroTipo != null;
            if (filtroTipo.equals("Todos") || p.getTipo().equals(filtroTipo)) {

                model.addRow(new Object[]{
                        p.getId(),
                        p.getNome(),
                        p.getTipo(),
                        p.getEstoque(),
                        String.format("R$ %.2f", p.getPreco()),
                        p.getStatus(),
                        p.getDataFabricacao() != null ? p.getDataFabricacao().format(formato) : "",
                        p.getDataValidade() != null
                                ? p.getDataValidade().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                : "",
                        calcularDiasVencimento(p.getDataValidade())
                });
            }
        }
    }

    public List<Produto> listarProduto() {
        return dao.listar();
    }

    public void excluirProduto(ProdutoView view) {

        if (!view.isAdmin()) {
            view.exibirMensagem("Acesso negado!");
            return;
        }

        int linha = view.getTabela().getSelectedRow();

        if (linha >= 0) {

            int confirm = JOptionPane.showConfirmDialog(
                    view,
                    "Deseja remover este produto?",
                    "Confirmar",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {

                int id = (int) view.getModel().getValueAt(linha, 0);
                String nomeProduto = (String) view.getModel().getValueAt(linha, 1);
                String tipoProduto = (String) view.getModel().getValueAt(linha, 2);
                int estoqueProduto = (int) view.getModel().getValueAt(linha, 3);

                dao.excluir(id);

                var usuario = controller.LoginController.getUsuarioLogado();
                if (usuario != null) {
                    LogAuditoria log = new LogAuditoria(
                            usuario.getId(),
                            usuario.getNome(),
                            "EXCLUIR",
                            "Produto",
                            id,
                            "Nome: " + nomeProduto + ", Tipo: " + tipoProduto + ", Estoque: " + estoqueProduto
                    );
                    logDAO.registrar(log);
                }

                view.exibirMensagem("Produto removido com sucesso!");

                carregarTabela(view);
                atualizarTotal(view);
                view.limparCampos();
            }

        } else {
            view.exibirMensagem("Selecione um produto!");
        }
    }

    public void carregarProdutoSelecionado(ProdutoView view) {
        int linha = view.getTabela().getSelectedRow();

        if (linha >= 0) {

            view.getTxtNome().setText(view.getModel().getValueAt(linha, 1).toString());
            view.getCbTipo().setSelectedItem(view.getModel().getValueAt(linha, 2).toString());
            view.getTxtEstoque().setText(view.getModel().getValueAt(linha, 3).toString());
            String preco = view.getModel().getValueAt(linha, 4)
                    .toString().replace("R$ ", "").replace(",", ".");
            view.getTxtPreco().setText(preco);
            view.getCbStatus().setSelectedItem(view.getModel().getValueAt(linha, 5).toString());
            view.getTxtDataFabricacao().setText(
                    view.getModel().getValueAt(linha, 6).toString()
            );
            view.getTxtDataValidade().setText(
                    view.getModel().getValueAt(linha, 7).toString()
            );
        }
    }

    public void carregarTabela(ProdutoView view) {

        verificarEAtualizarVencidos();
        DefaultTableModel model = view.getModel();
        model.setRowCount(0);

        List<Produto> produtos = dao.listar();

        for (Produto p : produtos) {
            model.addRow(new Object[]{
                    p.getId(),
                    p.getNome(),
                    p.getTipo(),
                    p.getEstoque(),
                    String.format("R$ %.2f", p.getPreco()),
                    p.getStatus(),
                    p.getDataFabricacao() != null
                            ? p.getDataFabricacao().format(formato) : "",
                    p.getDataValidade() != null
                            ? p.getDataValidade().format(formato) : "",
                    calcularDiasVencimento(p.getDataValidade())
            });
        }
    }

    public void atualizarTotal(ProdutoView view) {
        view.getLblTotalProdutos().setText("Total de produtos: " + dao.listar().size());
    }

    private String calcularDiasVencimento(LocalDate dataValidade) {
        if (dataValidade == null) {
            return "N/A";
        }

        LocalDate hoje = LocalDate.now();
        long dias = ChronoUnit.DAYS.between(hoje, dataValidade);

        if (dias < 0) return Math.abs(dias) + " dias (VENCIDO)";
        if (dias == 0) return "VENCE HOJE!";
        return dias + " dias";
    }

    public void verificarEAtualizarVencidos() {
        List<Produto> produtos = dao.listar();
        LocalDate hoje = LocalDate.now();

        for (Produto p : produtos) {
            boolean estaVencido = p.getDataValidade() != null
                    && p.getDataValidade().isBefore(hoje);
            boolean estaDisponivel = p.getStatus().equals("Disponível");

            if (estaVencido && estaDisponivel) {
                p.setStatus("Indisponível");
                dao.editar(p);
            }
        }
    }
}
