package controller;

import model.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import view.VendaView;

public class VendaController {

    private VendaDAO vendaDAO;
    private ProdutoController produtoController;
    private ClienteController clienteController;
    private ProdutoDAO produtoDAO;
    private VendaView view;

    public VendaController() {
        vendaDAO = new VendaDAO();
        produtoDAO = new ProdutoDAO();
        produtoController = new ProdutoController();
        clienteController = new ClienteController();
    }

    public void carregarClientes(JComboBox<String> cbCliente) {
        cbCliente.removeAllItems();
        cbCliente.addItem("Selecione um cliente");
        for (Cliente c : clienteController.listarClientes()) {
            cbCliente.addItem(c.getNome());
        }
    }

    public void setView(VendaView view) {
        this.view = view;
    }

    public Produto pesquisarProduto(String termo) {
        if (termo == null || termo.trim().isEmpty()) return null;
        for (Produto p : produtoDAO.listar()) {
            if (p.getNome().toLowerCase().contains(termo.toLowerCase())) return p;
        }
        return null;
    }

    public Produto buscarProdutoPorCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) return null;
        for (Produto p : produtoDAO.listar()) {
            if (String.valueOf(p.getId()).equals(codigo)) return p;
        }
        return null;
    }

    public double adicionarItem(
        String codigo, String descricao,
        String quantidadeTexto, String valorTexto,
        DefaultTableModel modelItens,
        List<Object[]> itensVenda, double totalAtual) {

        if (codigo.isEmpty() || descricao.isEmpty()) {
            view.exibirMensagem("Preencha o código e descrição do produto!");
            return totalAtual;
        }
    
        Produto produto = buscarProdutoPorCodigo(codigo);
        if (produto != null && "Indisponível".equals(produto.getStatus())) {
            view.exibirMensagem("O produto \"" + produto.getNome() + "\" está indisponível e não pode ser adicionado ao carrinho!");
            return totalAtual;
        }
    
        try {
            int quantidade = Validador.parseInteiroNaoNegativo(quantidadeTexto, "quantidade");
            double valorUnitario = Validador.parseDecimalNaoNegativo(valorTexto, "valor unitário");
            double subTotal = quantidade * valorUnitario;
    
            modelItens.addRow(new Object[]{
                    codigo, descricao, quantidade,
                    String.format("R$ %.2f", valorUnitario),
                    String.format("R$ %.2f", subTotal)
            });
    
            itensVenda.add(new Object[]{codigo, descricao, quantidade, valorUnitario, subTotal});
            return totalAtual + subTotal;
    
        } catch (Validador.ValidacaoException e) {
            view.exibirMensagem(e.getMessage());
            return totalAtual;
        }
    }

    public double excluirItem(
            JTable tabelaItens, DefaultTableModel modelItens,
            List<Object[]> itensVenda, double totalAtual) {

        int linha = tabelaItens.getSelectedRow();
        if (linha < 0) {
            view.exibirMensagem("Selecione um item!");
            return totalAtual;
        }

        double subTotal = Double.parseDouble(
                modelItens.getValueAt(linha, 4).toString()
                        .replace("R$ ", "").replace(",", ".")
        );

        modelItens.removeRow(linha);
        itensVenda.remove(linha);
        return totalAtual - subTotal;
    }

    public boolean finalizarVenda(
            Cliente clienteSelecionado,
            Usuario vendedorSelecionado,
            String formaPagamento,
            JComboBox<String> cbVendedor,
            List<Object[]> itensVenda,
            double totalVenda) {

        if (clienteSelecionado == null) {
            view.exibirMensagem("Selecione um cliente!");
            return false;
        }
        if (vendedorSelecionado == null) {
            view.exibirMensagem("Selecione um vendedor!");
            return false;
        }
        if (formaPagamento == null || formaPagamento.equals("Selecione")) {
            view.exibirMensagem("Selecione a forma de pagamento!");
            return false;
        }
        if (itensVenda.isEmpty()) {
            view.exibirMensagem("Adicione itens na venda!");
            return false;
        }

        for (Object[] item : itensVenda) {
            String codigoProduto = item[0].toString();
            int quantidade = (int) item[2];
            Produto produto = buscarProdutoPorCodigo(codigoProduto);
            if (produto == null) {
                view.exibirMensagem("Produto não encontrado: " + codigoProduto);
                return false;
            }
            if ("Indisponível".equals(produto.getStatus())) {
                view.exibirMensagem("O produto \"" + produto.getNome() + "\" está indisponível!");
                return false;
            }
            if (produto.getEstoque() < quantidade) {
                view.exibirMensagem("Estoque insuficiente para: " + produto.getNome() +
                        "\nEstoque disponível: " + produto.getEstoque());
                return false;
            }
        }

        Connection conn = null;
        try {

            conn = Conexao.getConnection();
            conn.setAutoCommit(false);

            Venda venda = new Venda();
            venda.setIdCliente(clienteSelecionado.getId());
            venda.setIdUsuario(vendedorSelecionado.getId());
            venda.setFormaDePagamento(formaPagamento);
            venda.setTotal(totalVenda);
            venda.setData(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            int idVenda = vendaDAO.inserirComConexao(conn, venda);
            if (idVenda <= 0) {
                throw new SQLException("Falha ao inserir venda.");
            }

            ItemVendaDAO itemDAO = new ItemVendaDAO();
            for (Object[] item : itensVenda) {
                String codigoProduto = item[0].toString();
                int quantidade = (int) item[2];
                double valorUnitario = (double) item[3];

                Produto produto = buscarProdutoPorCodigo(codigoProduto);
                ItemVenda itemVenda = new ItemVenda(produto, quantidade, valorUnitario);

                itemDAO.inserirComConexao(conn, itemVenda, idVenda);

                int novoEstoque = produto.getEstoque() - quantidade;
                produtoDAO.atualizarEstoqueComConexao(conn, produto.getId(), novoEstoque);
            }


            conn.commit();
            view.exibirMensagem("Venda registrada com sucesso!");
            return true;

        } catch (Exception e) {

            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            view.exibirMensagem("Erro ao registrar venda: " + e.getMessage());
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<Venda> listarVendas() {
        return vendaDAO.listar();
    }

    public List<Venda> listarPorPeriodo(String dataInicio, String dataFim) {
        return vendaDAO.listarPorPeriodo(dataInicio, dataFim);
    }

    public int getQuantidadeVendas() {
        return vendaDAO.listar().size();
    }

    public double getTotalVendas() {
        return vendaDAO.listar().stream().mapToDouble(Venda::getTotal).sum();
    }

    public void limparVenda(
            DefaultTableModel modelItens,
            List<Object[]> itensVenda,
            JComboBox<String> cbCliente,
            JComboBox<String> cbVendedor,
            JTextField txtItemCodigo,
            JTextField txtItemDescricao,
            JTextField txtItemQuantidade,
            JTextField txtItemValorUnitario,
            JTextField txtPesquisaItem,
            JLabel lblTotal) {

        modelItens.setRowCount(0);
        itensVenda.clear();
        cbCliente.setSelectedIndex(0);
        cbVendedor.setSelectedIndex(0);
        txtItemCodigo.setText("");
        txtItemDescricao.setText("");
        txtItemQuantidade.setText("");
        txtItemValorUnitario.setText("");
        txtPesquisaItem.setText("");
        lblTotal.setText("TOTAL: R$ 0,00");
    }
}
