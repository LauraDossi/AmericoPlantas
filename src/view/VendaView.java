package view;

import controller.*;
import model.Usuario;
import model.Produto;
import model.Cliente;
import model.Venda;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VendaView extends JPanel {

    private JComboBox<String> cbCliente;
    private JComboBox<String> cbVendedor;
    private JComboBox<String> cbFormaPagamento;
    private JTextField txtPesquisaItem;
    private JTextField txtItemCodigo;
    private JTextField txtItemDescricao;
    private JTextField txtItemQuantidade;
    private JTextField txtItemValorUnitario;
    private JTable tabelaItens;
    private DefaultTableModel modelItens;
    private VendaController controller;
    private ProdutoController produtoController;
    private ClienteController clienteController;
    private UsuarioController usuarioController;
    private List<Object[]> itensVenda;
    private double totalVenda = 0;
    private JLabel lblTotal;
    private boolean isAdmin;

    private JTable tabelaHistorico;
    private DefaultTableModel modelHistorico;
    private JLabel lblTotalHistorico;

    public VendaView() {
        controller = new VendaController();
        produtoController = new ProdutoController();
        usuarioController = new UsuarioController();
        clienteController = new ClienteController();
        itensVenda = new ArrayList<>();
        isAdmin = LoginController.isAdministrador();
        setLayout(new BorderLayout());
        iniciarComponentes();
    }

    private void iniciarComponentes() {
        JPanel principal = new JPanel(new BorderLayout(14, 14));
        principal.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        principal.setBackground(new Color(245, 245, 245));

        JPanel topo = new JPanel(new BorderLayout());
        topo.setBackground(new Color(34, 139, 34));
        topo.setPreferredSize(new Dimension(100, 80));

        JPanel topoEsquerdo = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        topoEsquerdo.setOpaque(false);
        JLabel lblTitulo = new JLabel("REGISTRAR VENDAS");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitulo.setForeground(Color.WHITE);
        topoEsquerdo.add(lblTitulo);
        topo.add(topoEsquerdo, BorderLayout.WEST);

        JPanel topoDireito = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        topoDireito.setOpaque(false);
        JButton btnVoltar = new JButton("VOLTAR");
        btnVoltar.setBackground(new Color(220, 53, 69));
        btnVoltar.setForeground(Color.WHITE);
        btnVoltar.setFocusPainted(false);
        btnVoltar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnVoltar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVoltar.setPreferredSize(new Dimension(150, 42));
        btnVoltar.addActionListener(e -> Navegador.irPara(AppFrame.MENU));
        topoDireito.add(btnVoltar);
        topo.add(topoDireito, BorderLayout.EAST);

        principal.add(topo, BorderLayout.NORTH);

        JPanel conteudo = new JPanel(new BorderLayout(14, 14));
        conteudo.setOpaque(false);
        conteudo.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JPanel menuLateral = new JPanel();
        menuLateral.setPreferredSize(new Dimension(240, 100));
        menuLateral.setBackground(Color.WHITE);
        menuLateral.setLayout(new BoxLayout(menuLateral, BoxLayout.Y_AXIS));
        menuLateral.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(20, 16, 20, 16)
        ));

        JLabel lblMenu = new JLabel("MENU PRINCIPAL");
        lblMenu.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblMenu.setForeground(new Color(34, 139, 34));
        lblMenu.setAlignmentX(Component.LEFT_ALIGNMENT);
        menuLateral.add(lblMenu);
        menuLateral.add(Box.createRigidArea(new Dimension(0, 18)));

        String[] menus = {"Clientes", "Produtos", "Registrar vendas", "Relatórios", "Fornecedor", "Usuários"};
        for (String menu : menus) {
            JButton btnMenu = criarBotaoMenuVerde(menu);
            btnMenu.addActionListener(e -> {
                switch (menu) {
                    case "Clientes" -> Navegador.irPara(AppFrame.CLIENTES);
                    case "Produtos" -> Navegador.irPara(AppFrame.PRODUTOS);
                    case "Registrar vendas" -> JOptionPane.showMessageDialog(this, "Você já está em Registrar vendas");
                    case "Relatórios" -> Navegador.irPara(AppFrame.RELATORIOS);
                    case "Fornecedor" -> Navegador.irPara(AppFrame.FORNECEDOR);
                    case "Usuários" -> Navegador.irParaUsuarios();
                }
            });
            menuLateral.add(btnMenu);
            menuLateral.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        menuLateral.add(Box.createVerticalGlue());
        JButton btnSair = criarBotaoMenuVermelho();
        btnSair.addActionListener(e -> Navegador.irPara(AppFrame.LOGIN));
        menuLateral.add(btnSair);

        JTabbedPane abas = new JTabbedPane();
        abas.setFont(new Font("Segoe UI", Font.BOLD, 15));
        abas.setBackground(new Color(245, 245, 245));
        abas.addTab("  Nova Venda  ", criarAbaNovaVenda());
        abas.addTab("  Histórico de Vendas  ", criarAbaHistorico());

        // Ao trocar para a aba de histórico, recarrega os dados
        abas.addChangeListener(e -> {
            if (abas.getSelectedIndex() == 1) {
                carregarHistorico();
            }
        });

        conteudo.add(menuLateral, BorderLayout.WEST);
        conteudo.add(abas, BorderLayout.CENTER);
        principal.add(conteudo, BorderLayout.CENTER);

        add(principal, BorderLayout.CENTER);

        txtItemCodigo.addActionListener(e -> {
            Produto p = controller.buscarProdutoPorCodigo(txtItemCodigo.getText());
            if (p != null) {
                txtItemDescricao.setText(p.getNome());
                txtItemValorUnitario.setText(String.valueOf(p.getPreco()));
                txtItemQuantidade.requestFocus();
            } else {
                JOptionPane.showMessageDialog(this, "Produto não encontrado!");
            }
        });
    }

    private JScrollPane criarAbaNovaVenda() {
        JPanel areaCentral = new ResponsivePanel();
        areaCentral.setLayout(new BoxLayout(areaCentral, BoxLayout.Y_AXIS));
        areaCentral.setBackground(new Color(245, 245, 245));
        areaCentral.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));

        JPanel painelSuperior = new JPanel(new GridBagLayout());
        painelSuperior.setBackground(Color.WHITE);
        painelSuperior.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(18, 22, 18, 22)
        ));
        painelSuperior.setMaximumSize(new Dimension(Integer.MAX_VALUE, 190));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.1;
        JLabel lblCliente = new JLabel("Cliente:");
        lblCliente.setFont(new Font("Segoe UI", Font.BOLD, 16));
        painelSuperior.add(lblCliente, gbc);

        gbc.gridx = 1; gbc.weightx = 0.4;
        cbCliente = new JComboBox<>();
        cbCliente.addItem("Selecione um cliente");
        controller.carregarClientes(cbCliente);
        cbCliente.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        cbCliente.setPreferredSize(new Dimension(220, 42));
        painelSuperior.add(cbCliente, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.1;
        JLabel lblVendedor = new JLabel("Vendedor:");
        lblVendedor.setFont(new Font("Segoe UI", Font.BOLD, 16));
        painelSuperior.add(lblVendedor, gbc);

        gbc.gridx = 1; gbc.weightx = 0.4;
        cbVendedor = new JComboBox<>();
        cbVendedor.addItem("Selecione");
        for (model.Usuario u : usuarioController.listarUsuarios()) {
            cbVendedor.addItem(u.getNome());
        }
        cbVendedor.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        cbVendedor.setPreferredSize(new Dimension(220, 42));
        painelSuperior.add(cbVendedor, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.1;
        JLabel lblFormaPagamento = new JLabel("Forma de Pagamento:");
        lblFormaPagamento.setFont(new Font("Segoe UI", Font.BOLD, 16));
        painelSuperior.add(lblFormaPagamento, gbc);

        gbc.gridx = 1; gbc.weightx = 0.4;
        cbFormaPagamento = new JComboBox<>(new String[]{"Selecione", "DINHEIRO", "PIX", "CARTÃO", "BOLETO"});
        cbFormaPagamento.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        cbFormaPagamento.setPreferredSize(new Dimension(220, 42));
        painelSuperior.add(cbFormaPagamento, gbc);

        areaCentral.add(painelSuperior);
        areaCentral.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel painelItens = new JPanel(new BorderLayout(12, 12));
        painelItens.setBackground(Color.WHITE);
        painelItens.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(18, 22, 18, 22)
        ));
        painelItens.setMaximumSize(new Dimension(Integer.MAX_VALUE, 620));

        JLabel lblRegistroVendas = new JLabel("REGISTRO DE VENDAS");
        lblRegistroVendas.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblRegistroVendas.setForeground(new Color(34, 139, 34));
        lblRegistroVendas.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));
        painelItens.add(lblRegistroVendas, BorderLayout.NORTH);

        JPanel painelPesquisaItem = new JPanel(new BorderLayout(10, 0));
        painelPesquisaItem.setOpaque(false);
        painelPesquisaItem.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));

        JLabel lblPesquisarItem = new JLabel("Pesquisar item:");
        lblPesquisarItem.setFont(new Font("Segoe UI", Font.BOLD, 14));
        painelPesquisaItem.add(lblPesquisarItem, BorderLayout.WEST);

        txtPesquisaItem = new JTextField();
        txtPesquisaItem.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtPesquisaItem.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        painelPesquisaItem.add(txtPesquisaItem, BorderLayout.CENTER);

        JButton btnPesquisarItem = criarBotaoVerde("Buscar");
        btnPesquisarItem.setPreferredSize(new Dimension(92, 42));
        btnPesquisarItem.addActionListener(e -> {
            Produto p = controller.pesquisarProduto(txtPesquisaItem.getText());
            if (p != null) {
                txtItemCodigo.setText(String.valueOf(p.getId()));
                txtItemDescricao.setText(p.getNome());
                txtItemValorUnitario.setText(String.valueOf(p.getPreco()));
                txtItemQuantidade.requestFocus();
            } else {
                JOptionPane.showMessageDialog(this, "Produto não encontrado!");
            }
        });
        painelPesquisaItem.add(btnPesquisarItem, BorderLayout.EAST);
        painelItens.add(painelPesquisaItem, BorderLayout.NORTH);

        String[] colunas = {"Código", "Descrição", "Quantidade", "Valor unitário", "Sub total"};
        modelItens = new DefaultTableModel(colunas, 0);
        tabelaItens = new JTable(modelItens);
        tabelaItens.setRowHeight(45);
        ModernUI.configureTableHeader(tabelaItens);
        tabelaItens.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabelaItens.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        tabelaItens.getColumnModel().getColumn(0).setPreferredWidth(70);
        tabelaItens.getColumnModel().getColumn(1).setPreferredWidth(240);
        tabelaItens.getColumnModel().getColumn(2).setPreferredWidth(90);
        tabelaItens.getColumnModel().getColumn(3).setPreferredWidth(110);
        tabelaItens.getColumnModel().getColumn(4).setPreferredWidth(110);
        tabelaItens.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
                return c;
            }
        });

        JScrollPane scrollTabela = new JScrollPane(tabelaItens);
        scrollTabela.setPreferredSize(new Dimension(0, 250));
        scrollTabela.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        painelItens.add(scrollTabela, BorderLayout.CENTER);

        JPanel painelAdicionar = new JPanel(new GridBagLayout());
        painelAdicionar.setOpaque(false);
        painelAdicionar.setBorder(BorderFactory.createEmptyBorder(14, 0, 6, 0));

        GridBagConstraints gbcItem = new GridBagConstraints();
        gbcItem.insets = new Insets(6, 5, 6, 5);
        gbcItem.fill = GridBagConstraints.HORIZONTAL;

        gbcItem.gridx = 0; gbcItem.gridy = 0; gbcItem.weightx = 0.08;
        JLabel lblCodigo = new JLabel("Código:");
        lblCodigo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        painelAdicionar.add(lblCodigo, gbcItem);

        gbcItem.gridx = 1; gbcItem.weightx = 0.18;
        txtItemCodigo = new JTextField(10);
        txtItemCodigo.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtItemCodigo.setPreferredSize(new Dimension(92, 42));
        txtItemCodigo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        painelAdicionar.add(txtItemCodigo, gbcItem);

        gbcItem.gridx = 2; gbcItem.weightx = 0.10;
        JLabel lblDescricao = new JLabel("Descrição:");
        lblDescricao.setFont(new Font("Segoe UI", Font.BOLD, 14));
        painelAdicionar.add(lblDescricao, gbcItem);

        gbcItem.gridx = 3; gbcItem.weightx = 0.64;
        txtItemDescricao = new JTextField(25);
        txtItemDescricao.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtItemDescricao.setPreferredSize(new Dimension(260, 42));
        txtItemDescricao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        painelAdicionar.add(txtItemDescricao, gbcItem);

        gbcItem.gridx = 0; gbcItem.gridy = 1; gbcItem.weightx = 0.08;
        JLabel lblQuantidade = new JLabel("Qtd:");
        lblQuantidade.setFont(new Font("Segoe UI", Font.BOLD, 14));
        painelAdicionar.add(lblQuantidade, gbcItem);

        gbcItem.gridx = 1; gbcItem.weightx = 0.18;
        txtItemQuantidade = new JTextField(8);
        txtItemQuantidade.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtItemQuantidade.setPreferredSize(new Dimension(78, 42));
        txtItemQuantidade.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        painelAdicionar.add(txtItemQuantidade, gbcItem);

        gbcItem.gridx = 2; gbcItem.weightx = 0.10;
        JLabel lblValorUnitario = new JLabel("Valor R$:");
        lblValorUnitario.setFont(new Font("Segoe UI", Font.BOLD, 14));
        painelAdicionar.add(lblValorUnitario, gbcItem);

        gbcItem.gridx = 3; gbcItem.weightx = 0.44;
        txtItemValorUnitario = new JTextField(10);
        txtItemValorUnitario.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtItemValorUnitario.setPreferredSize(new Dimension(120, 42));
        txtItemValorUnitario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)));
        painelAdicionar.add(txtItemValorUnitario, gbcItem);

        gbcItem.gridx = 4; gbcItem.weightx = 0;
        JButton btnAdicionar = criarBotaoVerde("Adicionar");
        btnAdicionar.setPreferredSize(new Dimension(112, 42));
        btnAdicionar.addActionListener(e -> {
            totalVenda = controller.adicionarItem(
                    txtItemCodigo.getText(), txtItemDescricao.getText(),
                    txtItemQuantidade.getText(), txtItemValorUnitario.getText(),
                    modelItens, itensVenda, totalVenda);
            lblTotal.setText("TOTAL: R$ " + String.format("%.2f", totalVenda));
            txtItemCodigo.setText("");
            txtItemDescricao.setText("");
            txtItemQuantidade.setText("");
            txtItemValorUnitario.setText("");
            txtPesquisaItem.setText("");
        });
        painelAdicionar.add(btnAdicionar, gbcItem);

        painelItens.add(painelAdicionar, BorderLayout.SOUTH);
        areaCentral.add(painelItens);
        areaCentral.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel painelInferior = new JPanel(new BorderLayout(15, 15));
        painelInferior.setOpaque(false);
        painelInferior.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        painelInferior.setMaximumSize(new Dimension(Integer.MAX_VALUE, 170));

        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.setOpaque(false);
        lblTotal = new JLabel("TOTAL: R$ 0,00");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTotal.setForeground(new Color(34, 139, 34));
        totalPanel.add(lblTotal);
        painelInferior.add(totalPanel, BorderLayout.NORTH);

        JPanel botoesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 8));
        botoesPanel.setOpaque(false);

        JButton btnSalvar = criarBotaoVerde("SALVAR VENDA");
        btnSalvar.setPreferredSize(new Dimension(148, 46));
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JButton btnExcluir = criarBotaoVermelho();
        btnExcluir.setPreferredSize(new Dimension(148, 46));
        btnExcluir.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JButton btnCancelar = criarBotaoVerde("CANCELAR");
        btnCancelar.setPreferredSize(new Dimension(128, 46));
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 16));

        btnSalvar.addActionListener(e -> {
            Cliente clienteSelecionado = clienteController.buscarPorNome((String) cbCliente.getSelectedItem());
            Usuario vendedorSelecionado = usuarioController.buscarPorNome((String) cbVendedor.getSelectedItem());
            boolean sucesso = controller.finalizarVenda(
                    clienteSelecionado, vendedorSelecionado,
                    (String) cbFormaPagamento.getSelectedItem(),
                    cbVendedor, itensVenda, totalVenda);
            if (sucesso) {
                totalVenda = 0;
                controller.limparVenda(modelItens, itensVenda, cbCliente, cbVendedor,
                        txtItemCodigo, txtItemDescricao, txtItemQuantidade,
                        txtItemValorUnitario, txtPesquisaItem, lblTotal);
            }
        });

        btnExcluir.addActionListener(e -> {
            totalVenda = controller.excluirItem(tabelaItens, modelItens, itensVenda, totalVenda);
            lblTotal.setText("TOTAL: R$ " + String.format("%.2f", totalVenda));
        });

        btnCancelar.addActionListener(e -> {
            totalVenda = 0;
            controller.limparVenda(modelItens, itensVenda, cbCliente, cbVendedor,
                    txtItemCodigo, txtItemDescricao, txtItemQuantidade,
                    txtItemValorUnitario, txtPesquisaItem, lblTotal);
        });

        botoesPanel.add(btnSalvar);
        botoesPanel.add(btnExcluir);
        botoesPanel.add(btnCancelar);
        painelInferior.add(botoesPanel, BorderLayout.SOUTH);

        areaCentral.add(painelInferior);

        JScrollPane scroll = new JScrollPane(areaCentral);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(new Color(245, 245, 245));
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        return scroll;
    }

    private JPanel criarAbaHistorico() {
        JPanel painel = new JPanel(new BorderLayout(0, 14));
        painel.setBackground(new Color(245, 245, 245));
        painel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        JLabel lblTitulo = new JLabel("HISTÓRICO DE VENDAS");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(34, 139, 34));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));

        JButton btnAtualizar = criarBotaoVerde("↻  Atualizar");
        btnAtualizar.setPreferredSize(new Dimension(130, 38));
        btnAtualizar.addActionListener(e -> carregarHistorico());

        JPanel topoHistorico = new JPanel(new BorderLayout());
        topoHistorico.setOpaque(false);
        topoHistorico.add(lblTitulo, BorderLayout.WEST);
        topoHistorico.add(btnAtualizar, BorderLayout.EAST);
        painel.add(topoHistorico, BorderLayout.NORTH);

        String[] colunas = {"ID", "Data", "Cliente", "Vendedor", "Forma de Pagamento", "Total"};
        modelHistorico = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaHistorico = new JTable(modelHistorico);
        tabelaHistorico.setRowHeight(38);
        tabelaHistorico.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        ModernUI.configureTableHeader(tabelaHistorico);
        tabelaHistorico.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        tabelaHistorico.getColumnModel().getColumn(0).setPreferredWidth(50);
        tabelaHistorico.getColumnModel().getColumn(1).setPreferredWidth(110);
        tabelaHistorico.getColumnModel().getColumn(2).setPreferredWidth(220);
        tabelaHistorico.getColumnModel().getColumn(3).setPreferredWidth(180);
        tabelaHistorico.getColumnModel().getColumn(4).setPreferredWidth(160);
        tabelaHistorico.getColumnModel().getColumn(5).setPreferredWidth(120);
        tabelaHistorico.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 250, 245));
                return c;
            }
        });

        JScrollPane scrollHistorico = new JScrollPane(tabelaHistorico);
        scrollHistorico.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        scrollHistorico.getViewport().setBackground(Color.WHITE);
        painel.add(scrollHistorico, BorderLayout.CENTER);

        JPanel rodape = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rodape.setBackground(Color.WHITE);
        rodape.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(10, 18, 10, 18)
        ));
        lblTotalHistorico = new JLabel("Total geral: R$ 0,00  |  0 venda(s)");
        lblTotalHistorico.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTotalHistorico.setForeground(new Color(34, 139, 34));
        rodape.add(lblTotalHistorico);
        painel.add(rodape, BorderLayout.SOUTH);

        carregarHistorico();
        return painel;
    }

    private void carregarHistorico() {
        if (modelHistorico == null) return;
        modelHistorico.setRowCount(0);

        List<Venda> vendas = controller.listarVendas();
        double totalGeral = 0;

        for (Venda v : vendas) {
            String nomeCliente = "—";
            Cliente c = clienteController.buscarPorId(v.getIdCliente());
            if (c != null) nomeCliente = c.getNome();

            String nomeVendedor = "—";
            Usuario u = usuarioController.buscarPorId(v.getIdUsuario());
            if (u != null) nomeVendedor = u.getNome();

            modelHistorico.addRow(new Object[]{
                    v.getId(), v.getData(), nomeCliente, nomeVendedor,
                    v.getFormaDePagamento(),
                    String.format("R$ %.2f", v.getTotal())
            });
            totalGeral += v.getTotal();
        }

        lblTotalHistorico.setText(String.format(
                "Total geral: R$ %.2f  |  %d venda(s)", totalGeral, vendas.size()));
    }

    private JButton criarBotaoMenuVerde(String texto) {
        JButton botao = new JButton(texto);
        botao.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        botao.setBackground(new Color(34, 139, 34));
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 15));
        botao.setHorizontalAlignment(SwingConstants.LEFT);
        botao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34, 139, 34), 1),
                BorderFactory.createEmptyBorder(12, 18, 12, 18)));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return botao;
    }

    private JButton criarBotaoMenuVermelho() {
        JButton botao = new JButton("Sair");
        botao.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        botao.setBackground(new Color(220, 53, 69));
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 15));
        botao.setHorizontalAlignment(SwingConstants.LEFT);
        botao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 53, 69), 1),
                BorderFactory.createEmptyBorder(12, 18, 12, 18)));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return botao;
    }

    private JButton criarBotaoVerde(String texto) {
        JButton botao = new JButton(texto);
        botao.setBackground(new Color(34, 139, 34));
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return botao;
    }

    private JButton criarBotaoVermelho() {
        JButton botao = new JButton("EXCLUIR ITEM");
        botao.setBackground(new Color(220, 53, 69));
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return botao;
    }

    public void exibirMensagem(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }

    private static class ResponsivePanel extends JPanel implements Scrollable {
        @Override public Dimension getPreferredScrollableViewportSize() { return getPreferredSize(); }
        @Override public int getScrollableUnitIncrement(Rectangle r, int o, int d) { return 16; }
        @Override public int getScrollableBlockIncrement(Rectangle r, int o, int d) { return 64; }
        @Override public boolean getScrollableTracksViewportWidth() { return true; }
        @Override public boolean getScrollableTracksViewportHeight() { return false; }
    }


    public JComboBox<String> getCbCliente() { return cbCliente; }
    public void setCbCliente(JComboBox<String> cbCliente) { this.cbCliente = cbCliente; }
    public JComboBox<String> getCbVendedor() { return cbVendedor; }
    public void setCbVendedor(JComboBox<String> cbVendedor) { this.cbVendedor = cbVendedor; }
    public JTextField getTxtPesquisaItem() { return txtPesquisaItem; }
    public void setTxtPesquisaItem(JTextField txtPesquisaItem) { this.txtPesquisaItem = txtPesquisaItem; }
    public JTextField getTxtItemCodigo() { return txtItemCodigo; }
    public void setTxtItemCodigo(JTextField txtItemCodigo) { this.txtItemCodigo = txtItemCodigo; }
    public JTextField getTxtItemDescricao() { return txtItemDescricao; }
    public void setTxtItemDescricao(JTextField txtItemDescricao) { this.txtItemDescricao = txtItemDescricao; }
    public JTextField getTxtItemQuantidade() { return txtItemQuantidade; }
    public void setTxtItemQuantidade(JTextField txtItemQuantidade) { this.txtItemQuantidade = txtItemQuantidade; }
    public JTextField getTxtItemValorUnitario() { return txtItemValorUnitario; }
    public void setTxtItemValorUnitario(JTextField txtItemValorUnitario) { this.txtItemValorUnitario = txtItemValorUnitario; }
    public JTable getTabelaItens() { return tabelaItens; }
    public void setTabelaItens(JTable tabelaItens) { this.tabelaItens = tabelaItens; }
    public DefaultTableModel getModelItens() { return modelItens; }
    public void setModelItens(DefaultTableModel modelItens) { this.modelItens = modelItens; }
    public VendaController getController() { return controller; }
    public void setController(VendaController controller) { this.controller = controller; }
    public ProdutoController getProdutoController() { return produtoController; }
    public void setProdutoController(ProdutoController produtoController) { this.produtoController = produtoController; }
    public ClienteController getClienteController() { return clienteController; }
    public void setClienteController(ClienteController clienteController) { this.clienteController = clienteController; }
    public List<Object[]> getItensVenda() { return itensVenda; }
    public void setItensVenda(List<Object[]> itensVenda) { this.itensVenda = itensVenda; }
    public double getTotalVenda() { return totalVenda; }
    public void setTotalVenda(double totalVenda) { this.totalVenda = totalVenda; }
    public JLabel getLblTotal() { return lblTotal; }
    public void setLblTotal(JLabel lblTotal) { this.lblTotal = lblTotal; }
    public boolean isAdmin() { return isAdmin; }
    public void setAdmin(boolean admin) { isAdmin = admin; }
}
