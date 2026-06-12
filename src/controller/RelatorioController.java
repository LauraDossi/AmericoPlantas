package controller;

import model.*;
import view.RelatorioView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.ArrayList;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RelatorioController {

    private ClienteController clienteController;
    private ProdutoController produtoController;
    private ProdutoDAO dao;
    private VendaController vendaController;
    private FornecedorController fornecedorController;
    private UsuarioController usuarioController;
    private RelatorioDAO relatorioDAO = new RelatorioDAO();

    public RelatorioController() {
        clienteController = new ClienteController();
        produtoController = new ProdutoController();
        dao = new ProdutoDAO();
        vendaController = new VendaController();
        fornecedorController = new FornecedorController();
        usuarioController = new UsuarioController();
    }

    public void gerarRelatorio(RelatorioView view) {

        String tipo = (String) view.getCbTipoRelatorio().getSelectedItem();

        switch (tipo) {
            case "Clientes":
                gerarRelatorioClientes(view);
                break;

            case "Produtos":
                gerarRelatorioProdutos(view);
                break;

            case "Fornecedores":
                gerarRelatorioFornecedores(view);
                break;

            case "Usuários":
                gerarRelatorioUsuarios(view);
                break;

            case "Vendas":
                gerarRelatorioVendas(view);
                break;

            case "Resumo geral":
                gerarResumoGeral(view);
                break;
        }

        for (int i = 0;
             i < view.getModel().getColumnCount();
             i++) {

            view.getTabelaDetalhes()
                    .getColumnModel()
                    .getColumn(i)
                    .setPreferredWidth(150);
        }
    }

    private void gerarRelatorioClientes(RelatorioView view) {

        DefaultTableModel model = view.getModel();

        model.setColumnIdentifiers(new String[]{
                "ID", "Nome", "CPF", "Telefone", "Endereço", "Email"
        });

        model.setRowCount(0);

        int total = 0;

        for (Cliente c : clienteController.listarClientes()) {

            model.addRow(new Object[]{
                    c.getId(),
                    c.getNome(),
                    c.getCpfCnpj(),
                    c.getTelefone(),
                    c.getEndereco(),
                    c.getEmail()
            });

            total++;
        }

        StringBuilder resumo = new StringBuilder();

        resumo.append("RELATÓRIO DE CLIENTES\n");
        resumo.append("----------------------------------------\n");
        resumo.append("Total de clientes: ")
                .append(total)
                .append("\n");

        resumo.append("Clientes ativos: ")
                .append(total)
                .append("\n");

        resumo.append("\nPeríodo: ")
                .append(view.getTxtDataInicio().getText())
                .append(" a ")
                .append(view.getTxtDataFim().getText());

        view.getTxtResumo().setText(resumo.toString());
    }

    private void gerarRelatorioProdutos(RelatorioView view) {

        DefaultTableModel model = view.getModel();

        model.setColumnIdentifiers(new String[]{
                "ID", "Nome", "Tipo", "Estoque", "Preço", "Validade", "Status"
        });

        model.setRowCount(0);

        int totalEstoque = 0;
        double valorTotalEstoque = 0;

        String dataInicio = converterData(view.getTxtDataInicio().getText());
        String dataFim = converterData(view.getTxtDataFim().getText());

        ArrayList<Produto> produtos = dao.listarPorPeriodo(dataInicio, dataFim);

        for (Produto p : produtos) {

            model.addRow(new Object[]{
                    p.getId(),
                    p.getNome(),
                    p.getTipo(),
                    p.getEstoque(),
                    String.format("R$ %.2f", p.getPreco()),
                    p.getDataValidade().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    p.getStatus()
            });

            totalEstoque += p.getEstoque();
            valorTotalEstoque += p.getPreco() * p.getEstoque();
        }

        StringBuilder resumo = new StringBuilder();
        resumo.append("RELATÓRIO DE PRODUTOS\n");
        resumo.append("----------------------------------------\n");
        resumo.append("Período (validade): ")
                .append(view.getTxtDataInicio().getText())
                .append(" a ")
                .append(view.getTxtDataFim().getText())
                .append("\n");
        resumo.append("Total de produtos: ").append(produtos.size()).append("\n");
        resumo.append("Total em estoque: ").append(totalEstoque).append(" unidades\n");
        resumo.append("Valor total do estoque: R$ ")
                .append(String.format("%.2f", valorTotalEstoque));

        view.getTxtResumo().setText(resumo.toString());
    }

    private void gerarRelatorioFornecedores(RelatorioView view) {

        DefaultTableModel model = view.getModel();

        model.setColumnIdentifiers(new String[]{
                "ID", "Nome", "CNPJ/CPF", "Telefone", "Email", "Endereço"
        });

        model.setRowCount(0);

        for (Fornecedor f :
                fornecedorController.listarFornecedores()) {

            model.addRow(new Object[]{
                    f.getId(),
                    f.getNome(),
                    f.getCpfCnpj(),
                    f.getTelefone(),
                    f.getEmail(),
                    f.getEndereco()
            });
        }

        StringBuilder resumo = new StringBuilder();

        resumo.append("RELATÓRIO DE FORNECEDORES\n");
        resumo.append("----------------------------------------\n");

        resumo.append("Total de Fornecedores: ")
                .append(
                        fornecedorController.getTotalFornecedores()
                )
                .append("\n");

        resumo.append("\nPeriodo: ")
                .append(view.getTxtDataInicio().getText())
                .append(" a ")
                .append(view.getTxtDataFim().getText());

        view.getTxtResumo().setText(resumo.toString());
    }

    private void gerarRelatorioUsuarios(RelatorioView view) {

        DefaultTableModel model = view.getModel();

        model.setColumnIdentifiers(new String[]{
                "ID", "Nome", "CPF", "Telefone", "Email", "Tipo"
        });

        model.setRowCount(0);

        int administradores = 0;
        int operadores = 0;

        for (Usuario u :
                usuarioController.listarUsuarios()) {

            model.addRow(new Object[]{

                    u.getId(),
                    u.getNome(),
                    u.getCpfCnpj(),
                    u.getTelefone(),
                    u.getEmail(),
                    u.getTipoUsuario()
            });

            if (u.getTipoUsuario().equals("Administrador")) {
                administradores++;
            } else {
                operadores++;
            }
        }

        StringBuilder resumo = new StringBuilder();

        resumo.append("RELATÓRIO DE USUÁRIOS\n");
        resumo.append("----------------------------------------\n");

        resumo.append("Total de usuários: ")
                .append(usuarioController.getTotalUsuarios())
                .append("\n");

        resumo.append("Administradores: ")
                .append(administradores)
                .append("\n");

        resumo.append("Operadores: ")
                .append(operadores)
                .append("\n");

        resumo.append("\nPeríodo: ")
                .append(view.getTxtDataInicio().getText())
                .append(" a ")
                .append(view.getTxtDataFim().getText());

        view.getTxtResumo().setText(resumo.toString());
    }

    private void gerarRelatorioVendas(RelatorioView view) {

        DefaultTableModel model = view.getModel();

        model.setColumnIdentifiers(new String[]{
                "ID", "Cliente", "Vendedor", "Forma Pagamento", "Data", "Total"
        });

        model.setRowCount(0);

        double totalVendasPeriodo = 0;

        String dataInicio = converterData(view.getTxtDataInicio().getText());
        String dataFim = converterData(view.getTxtDataFim().getText());

        List<Venda> vendas = vendaController.listarPorPeriodo(dataInicio, dataFim);

        for (Venda v : vendas) {

            model.addRow(new Object[]{
                    v.getId(),
                    v.getNomeCliente(),
                    v.getNomeUsuario(),
                    v.getFormaDePagamento(),
                    v.getData(),
                    String.format("R$ %.2f", v.getTotal())
            });

            totalVendasPeriodo += v.getTotal();
        }

        StringBuilder resumo = new StringBuilder();
        resumo.append("RELATÓRIO DE VENDAS\n");
        resumo.append("----------------------------------------\n");
        resumo.append("Período: ")
                .append(view.getTxtDataInicio().getText())
                .append(" a ")
                .append(view.getTxtDataFim().getText())
                .append("\n");
        resumo.append("Total de vendas: ").append(vendas.size()).append("\n");
        resumo.append("Valor total vendido: R$ ")
                .append(String.format("%.2f", totalVendasPeriodo)).append("\n");
        resumo.append("Ticket médio: R$ ")
                .append(String.format("%.2f",
                        vendas.size() > 0 ? totalVendasPeriodo / vendas.size() : 0));

        view.getTxtResumo().setText(resumo.toString());
    }

    private void gerarResumoGeral(RelatorioView view) {

        DefaultTableModel model = view.getModel();

        model.setColumnIdentifiers(new String[]{
                "Indicador", "Quantidade", "Valor"
        });

        model.setRowCount(0);

        String dataInicio = converterData(view.getTxtDataInicio().getText());
        String dataFim = converterData(view.getTxtDataFim().getText());

        int totalClientes = clienteController.listarClientes().size();
        int totalProdutos = dao.listar().size();
        int totalEstoque = dao.listar().stream().mapToInt(Produto::getEstoque).sum();
        int totalFornecedores = fornecedorController.getTotalFornecedores();
        int totalUsuarios = usuarioController.getTotalUsuarios();

        List<Venda> vendasPeriodo = vendaController.listarPorPeriodo(dataInicio, dataFim);
        int totalVendas = vendasPeriodo.size();
        double valorTotalVendas = vendasPeriodo.stream().mapToDouble(Venda::getTotal).sum();

        double valorTotalEstoque = dao.listar().stream()
                .mapToDouble(p -> p.getPreco() * p.getEstoque()).sum();

        model.addRow(new Object[]{"Clientes", totalClientes, "-"});
        model.addRow(new Object[]{"Produtos", totalProdutos, "-"});
        model.addRow(new Object[]{"Unidades em estoque", totalEstoque,
                String.format("R$ %.2f", valorTotalEstoque)});
        model.addRow(new Object[]{"Fornecedores", totalFornecedores, "-"});
        model.addRow(new Object[]{"Usuários", totalUsuarios, "-"});
        model.addRow(new Object[]{"Vendas no período", totalVendas,
                String.format("R$ %.2f", valorTotalVendas)});

        StringBuilder resumo = new StringBuilder();
        resumo.append("RESUMO GERAL DO SISTEMA\n");
        resumo.append("----------------------------------------\n");
        resumo.append("Data de emissão: ")
                .append(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .append("\n");
        resumo.append("Período: ")
                .append(view.getTxtDataInicio().getText())
                .append(" a ")
                .append(view.getTxtDataFim().getText())
                .append("\n\n");
        resumo.append("Americo Plantas Medicinais - SISTEMA DE GESTÃO\n");
        resumo.append("----------------------------------------\n");
        resumo.append("Sistema operacional e funcionando\n");

        view.getTxtResumo().setText(resumo.toString());
    }

    public void salvarRelatorioTXT(RelatorioView view) {

        JFileChooser fileChooser = new JFileChooser();

        fileChooser.setSelectedFile(
                new File(
                        "relatório_" +
                                System.currentTimeMillis()
                                + ".txt"
                )
        );

        int resultado =
                fileChooser.showSaveDialog(view);

        if (resultado == JFileChooser.APPROVE_OPTION) {

            try (BufferedWriter writer =
                         new BufferedWriter(
                                 new FileWriter(
                                         fileChooser.getSelectedFile()
                                 )
                         )) {

                writer.write(
                        "RELATÓRIO GERADO EM: "
                                + LocalDate.now()
                                .format(
                                        DateTimeFormatter.ofPattern(
                                                "dd/MM/yyyy"
                                        )
                                )
                );

                writer.newLine();

                writer.write("-".repeat(60));

                writer.newLine();

                writer.write(
                        "TIPO DE RELATÓRIO: "
                                + view.getCbTipoRelatorio()
                                .getSelectedItem()
                );

                writer.newLine();

                writer.write(
                        "PERÍODO: "
                                + view.getTxtDataInicio().getText()
                                + " a "
                                + view.getTxtDataFim().getText()
                );

                writer.newLine();

                writer.write("-".repeat(60));

                writer.newLine();

                writer.newLine();

                writer.write("--- RESUMO ---");

                writer.newLine();

                writer.write(view.getTxtResumo().getText());

                writer.newLine();

                writer.newLine();

                writer.write("=== DETALHES ===");

                writer.newLine();

                for (int i = 0;
                     i < view.getModel().getColumnCount();
                     i++) {

                    writer.write(
                            view.getModel().getColumnName(i)
                                    + "\t"
                    );
                }

                writer.newLine();

                writer.write("-".repeat(60));

                writer.newLine();

                for (int i = 0;
                     i < view.getModel().getRowCount();
                     i++) {

                    for (int j = 0;
                         j < view.getModel().getColumnCount();
                         j++) {

                        writer.write(
                                view.getModel()
                                        .getValueAt(i, j) + "\t"
                        );
                    }

                    writer.newLine();
                }

                view.exibirMensagem("Relatório salvo com sucesso!");

            } catch (IOException ex) {
                view.exibirMensagem("Erro ao salvar arquivo: " + ex.getMessage());
            }
        }
    }

    public void salvarNoBanco(RelatorioView view) {
        String tipo = (String) view.getCbTipoRelatorio().getSelectedItem();
        String dataInicio = converterData(view.getTxtDataInicio().getText());
        String dataFim = converterData(view.getTxtDataFim().getText());

        StringBuilder conteudo = new StringBuilder();

        conteudo.append("=== RESUMO ===\n");
        conteudo.append(view.getTxtResumo().getText());
        conteudo.append("\n\n=== DETALHES ===\n");

        for (int i = 0; i < view.getModel().getColumnCount(); i++) {
            conteudo.append(view.getModel().getColumnName(i)).append("\t");
        }
        conteudo.append("\n").append("-".repeat(60)).append("\n");

        for (int i = 0; i < view.getModel().getRowCount(); i++) {
            for (int j = 0; j < view.getModel().getColumnCount(); j++) {
                conteudo.append(view.getModel().getValueAt(i, j)).append("\t");
            }
            conteudo.append("\n");
        }

        if (view.getModel().getRowCount() == 0) {
            view.exibirMensagem("Gere um relatório antes de salvar!");
            return;
        }

        boolean salvo = relatorioDAO.salvar(tipo, dataInicio, dataFim, conteudo.toString());

        if (salvo) {
            view.exibirMensagem("Relatório salvo no banco com sucesso!");
        } else {
            view.exibirMensagem("Erro ao salvar relatório!");
        }
    }

    private String converterData(String data) {
        try {
            LocalDate d = LocalDate.parse(data,
                    DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            return d.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }
    }
}