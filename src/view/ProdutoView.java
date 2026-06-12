package view;

import controller.ProdutoController;
import controller.LoginController;
import model.ProdutoDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ProdutoView extends JPanel {

    private JTextField txtNome, txtEstoque, txtPreco, txtPesquisa;
    private JTextField txtDataFabricacao, txtDataValidade;
    private JComboBox<String> cbTipo, cbStatus, cbFiltroTipo;
    private JTable tabela;
    private DefaultTableModel model;
    private ProdutoController controller;
    private ProdutoDAO dao;
    private JLabel lblTotalProdutos;
    private boolean isAdmin;

    public ProdutoView() {
        controller = new ProdutoController();
        dao = new ProdutoDAO();
        isAdmin = LoginController.isAdministrador();
        setLayout(new BorderLayout());
        iniciarComponentes();
    }

    private void iniciarComponentes() {
        JPanel principal = new JPanel(new BorderLayout(20, 20));
        principal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        principal.setBackground(new Color(245, 245, 245));

        JPanel topo = new JPanel(new BorderLayout());
        topo.setBackground(new Color(34, 139, 34));
        topo.setPreferredSize(new Dimension(100, 70));

        JPanel topoEsquerdo = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        topoEsquerdo.setOpaque(false);
        topoEsquerdo.setBackground(new Color(34, 139, 34));

        JLabel lblTitulo = new JLabel("PRODUTOS");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        topoEsquerdo.add(lblTitulo);
        topo.add(topoEsquerdo, BorderLayout.WEST);

        JPanel topoDireito = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        topoDireito.setOpaque(false);
        topoDireito.setBackground(new Color(34, 139, 34));

        JButton btnVoltar = new JButton("VOLTAR");
        btnVoltar.setBackground(new Color(220, 53, 69));
        btnVoltar.setForeground(Color.WHITE);
        btnVoltar.setFocusPainted(false);
        btnVoltar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnVoltar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVoltar.setPreferredSize(new Dimension(220, 40));
        btnVoltar.addActionListener(e -> Navegador.irPara(AppFrame.MENU));
        topoDireito.add(btnVoltar);
        topo.add(topoDireito, BorderLayout.EAST);

        principal.add(topo, BorderLayout.NORTH);

        JPanel conteudo = new JPanel(new BorderLayout(20, 20));
        conteudo.setOpaque(false);
        conteudo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel menuLateral = new JPanel();
        menuLateral.setPreferredSize(new Dimension(250, 100));
        menuLateral.setBackground(Color.WHITE);
        menuLateral.setLayout(new BoxLayout(menuLateral, BoxLayout.Y_AXIS));
        menuLateral.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(20, 15, 20, 15)
        ));

        JLabel lblMenu = new JLabel("MENU PRINCIPAL");
        lblMenu.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblMenu.setForeground(new Color(34, 139, 34));
        lblMenu.setAlignmentX(Component.LEFT_ALIGNMENT);
        menuLateral.add(lblMenu);
        menuLateral.add(Box.createRigidArea(new Dimension(0, 20)));

        String[] menus = {"Clientes", "Produtos", "Registrar vendas", "Relatórios", "Fornecedor", "Usuários"};
        for (String menu : menus) {
            JButton btnMenu = criarBotaoMenuVerde(menu);
            btnMenu.addActionListener(e -> {
                switch (menu) {
                    case "Clientes" -> Navegador.irPara(AppFrame.CLIENTES);
                    case "Produtos" -> JOptionPane.showMessageDialog(this, "Você já está em Produtos");
                    case "Registrar vendas" -> Navegador.irPara(AppFrame.VENDAS);
                    case "Relatórios" -> Navegador.irPara(AppFrame.RELATORIOS);
                    case "Fornecedor" -> Navegador.irPara(AppFrame.FORNECEDOR);
                    case "Usuários" -> Navegador.irParaUsuarios();
                }
            });
            menuLateral.add(btnMenu);
            menuLateral.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        menuLateral.add(Box.createVerticalGlue());

        JButton btnSair = criarBotaoMenuVermelho();
        btnSair.addActionListener(e -> Navegador.irPara(AppFrame.LOGIN));
        menuLateral.add(btnSair);

        JPanel areaCentralWrapper = new JPanel(new BorderLayout());
        areaCentralWrapper.setOpaque(false);

        JPanel areaCentral = new JPanel();
        areaCentral.setLayout(new BoxLayout(areaCentral, BoxLayout.Y_AXIS));
        areaCentral.setOpaque(false);

        JPanel painelSuperior = new JPanel(new BorderLayout(15, 10));
        painelSuperior.setOpaque(false);
        painelSuperior.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));

        JPanel tituloPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        tituloPanel.setOpaque(false);
        JLabel lblProdutoTitulo = new JLabel("PRODUTOS");
        lblProdutoTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblProdutoTitulo.setForeground(new Color(34, 139, 34));
        tituloPanel.add(lblProdutoTitulo);
        painelSuperior.add(tituloPanel, BorderLayout.WEST);

        JPanel pesquisaPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pesquisaPanel.setOpaque(false);

        JLabel lblFiltroTipo = new JLabel("Filtrar por tipo:");
        lblFiltroTipo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        pesquisaPanel.add(lblFiltroTipo);

        cbFiltroTipo = new JComboBox<>(new String[]{"Todos", "Chá", "Medicinal", "Natural", "Ervas"});
        cbFiltroTipo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbFiltroTipo.setPreferredSize(new Dimension(170, 40));
        cbFiltroTipo.setMinimumSize(new Dimension(170, 40));
        cbFiltroTipo.addActionListener(e -> controller.pesquisarProdutos(this));
        pesquisaPanel.add(cbFiltroTipo);
        pesquisaPanel.add(Box.createRigidArea(new Dimension(15, 0)));

        txtPesquisa = new JTextField();
        txtPesquisa.setPreferredSize(new Dimension(200, 35));
        txtPesquisa.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPesquisa.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        JButton btnPesquisar = criarBotaoVerde("Pesquisar");
        btnPesquisar.addActionListener(e -> controller.pesquisarProdutos(this));

        JButton btnLimparCampoDePesquisa = criarBotaoVerde("Limpar");
        btnLimparCampoDePesquisa.addActionListener(e -> limparCampos());

        pesquisaPanel.add(txtPesquisa);
        pesquisaPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        pesquisaPanel.add(btnPesquisar);
        pesquisaPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        pesquisaPanel.add(btnLimparCampoDePesquisa);

        painelSuperior.add(pesquisaPanel, BorderLayout.EAST);
        areaCentral.add(painelSuperior);
        areaCentral.add(Box.createRigidArea(new Dimension(0, 15)));

        String[] colunas = {"Código", "Nome", "Tipo", "Estoque", "Preço", "Status", "Fabricação", "Validade", "Dias para vencer"};
        model = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabela = new JTable(model);
        tabela.setRowHeight(40);
        ModernUI.configureTableHeader(tabela);
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        tabela.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected && column == 8 && value != null) {
                    String diasStr = value.toString();
                    if (diasStr.contains("VENCIDO")) {
                        c.setBackground(new Color(255, 200, 200));
                        setForeground(new Color(180, 0, 0));
                    } else if (diasStr.contains("VENCE HOJE")) {
                        c.setBackground(new Color(255, 150, 150));
                        setForeground(new Color(180, 0, 0));
                    } else if (diasStr.contains("dias") && !diasStr.contains("VENCIDO")) {
                        try {
                            int dias = Integer.parseInt(diasStr.split(" ")[0]);
                            if (dias <= 30) {
                                c.setBackground(new Color(255, 220, 150));
                                setForeground(new Color(200, 100, 0));
                            } else if (dias <= 90) {
                                c.setBackground(new Color(255, 255, 200));
                                setForeground(new Color(100, 100, 0));
                            } else {
                                c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
                                setForeground(Color.BLACK);
                            }
                        } catch (NumberFormatException e) {
                            c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
                        }
                    } else {
                        c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
                        setForeground(Color.BLACK);
                    }
                } else if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
                    setForeground(Color.BLACK);
                }
                return c;
            }
        });

        JScrollPane scrollTabela = new JScrollPane(tabela);
        scrollTabela.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        scrollTabela.setPreferredSize(new Dimension(0, 250));
        areaCentral.add(scrollTabela);
        areaCentral.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel painelTotal = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelTotal.setOpaque(false);
        lblTotalProdutos = new JLabel("Total de Produtos: " + dao.listar().size());
        lblTotalProdutos.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotalProdutos.setForeground(new Color(34, 139, 34));
        painelTotal.add(lblTotalProdutos);
        areaCentral.add(painelTotal);
        areaCentral.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel painelFormulario = new JPanel(new BorderLayout());
        painelFormulario.setBackground(Color.WHITE);
        painelFormulario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));
        painelFormulario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 560));

        JLabel lblAdicionarTitulo = new JLabel("ADICIONAR PRODUTO");
        lblAdicionarTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblAdicionarTitulo.setForeground(new Color(34, 139, 34));
        lblAdicionarTitulo.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        painelFormulario.add(lblAdicionarTitulo, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.2;
        formPanel.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        txtNome = criarCampo();
        formPanel.add(txtNome, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.2;
        formPanel.add(new JLabel("Tipo:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        cbTipo = new JComboBox<>(new String[]{"Chá", "Medicinal", "Natural", "Ervas"});
        cbTipo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbTipo.setPreferredSize(new Dimension(300, 40));
        formPanel.add(cbTipo, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.2;
        formPanel.add(new JLabel("Estoque:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        txtEstoque = criarCampo();
        formPanel.add(txtEstoque, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.2;
        formPanel.add(new JLabel("Preço (R$):"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        txtPreco = criarCampo();
        formPanel.add(txtPreco, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.2;
        formPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        cbStatus = new JComboBox<>(new String[]{"Disponível", "Indisponível"});
        cbStatus.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbStatus.setPreferredSize(new Dimension(300, 40));
        formPanel.add(cbStatus, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0.2;
        formPanel.add(new JLabel("Data de fabricação:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        txtDataFabricacao = criarCampo();
        txtDataFabricacao.setToolTipText("Formato: DD/MM/AAAA");
        formPanel.add(txtDataFabricacao, gbc);

        gbc.gridx = 0; gbc.gridy = 6; gbc.weightx = 0.2;
        formPanel.add(new JLabel("Data Validade:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        txtDataValidade = criarCampo();
        txtDataValidade.setToolTipText("Formato: DD/MM/AAAA");
        formPanel.add(txtDataValidade, gbc);

        gbc.gridx = 0; gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel botoesForm = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        botoesForm.setOpaque(false);

        JButton btnSalvar = criarBotaoVerdeGrande("SALVAR");
        JButton btnEditar = criarBotaoVerdeGrande("EDITAR");
        JButton btnRemover = criarBotaoVermelhoGrande();
        JButton btnLimpar = criarBotaoVerdeGrande("LIMPAR");

        if (!isAdmin) {
            btnSalvar.setEnabled(false);
            btnEditar.setEnabled(false);
            btnRemover.setEnabled(false);
            btnSalvar.setToolTipText("Acesso negado - Apenas administradores");
            btnEditar.setToolTipText("Acesso negado - Apenas administradores");
            btnRemover.setToolTipText("Acesso negado - Apenas administradores");
        }

        btnSalvar.addActionListener(e -> controller.salvarProduto(this));
        btnEditar.addActionListener(e -> controller.editarProduto(this));
        btnRemover.addActionListener(e -> controller.excluirProduto(this));
        btnLimpar.addActionListener(e -> limparCampos());
        tabela.getSelectionModel().addListSelectionListener(
                e -> controller.carregarProdutoSelecionado(this));

        botoesForm.add(btnSalvar);
        botoesForm.add(btnEditar);
        botoesForm.add(btnRemover);
        botoesForm.add(btnLimpar);

        formPanel.add(botoesForm, gbc);
        painelFormulario.add(formPanel, BorderLayout.CENTER);
        areaCentral.add(painelFormulario);

        JScrollPane scrollAreaCentral = new JScrollPane(areaCentral);
        scrollAreaCentral.setBorder(null);
        scrollAreaCentral.getViewport().setBackground(new Color(245, 245, 245));
        scrollAreaCentral.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        areaCentralWrapper.add(scrollAreaCentral, BorderLayout.CENTER);

        conteudo.add(menuLateral, BorderLayout.WEST);
        conteudo.add(areaCentralWrapper, BorderLayout.CENTER);
        principal.add(conteudo, BorderLayout.CENTER);

        add(principal, BorderLayout.CENTER);

        controller.carregarTabela(this);
        controller.atualizarTotal(this);
    }

    private JTextField criarCampo() {
        JTextField campo = new JTextField();
        campo.setPreferredSize(new Dimension(350, 40));
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        return campo;
    }

    private JButton criarBotaoMenuVerde(String texto) {
        JButton botao = new JButton(texto);
        botao.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        botao.setBackground(new Color(34, 139, 34));
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botao.setHorizontalAlignment(SwingConstants.LEFT);
        botao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(34, 139, 34), 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return botao;
    }

    private JButton criarBotaoMenuVermelho() {
        JButton botao = new JButton("Sair");
        botao.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        botao.setBackground(new Color(220, 53, 69));
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botao.setHorizontalAlignment(SwingConstants.LEFT);
        botao.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 53, 69), 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
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
        botao.setPreferredSize(new Dimension(100, 38));
        return botao;
    }

    private JButton criarBotaoVerdeGrande(String texto) {
        JButton botao = new JButton(texto);
        botao.setBackground(new Color(34, 139, 34));
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 15));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.setPreferredSize(new Dimension(130, 45));
        return botao;
    }

    private JButton criarBotaoVermelhoGrande() {
        JButton botao = new JButton("REMOVER");
        botao.setBackground(new Color(220, 53, 69));
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 15));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.setPreferredSize(new Dimension(130, 45));
        return botao;
    }

    public void limparCampos() {
        txtNome.setText("");
        txtEstoque.setText("");
        txtPreco.setText("");
        txtDataFabricacao.setText("");
        txtDataValidade.setText("");
        txtPesquisa.setText("");
        cbTipo.setSelectedIndex(0);
        cbStatus.setSelectedIndex(0);
        cbFiltroTipo.setSelectedIndex(0);
        txtNome.requestFocus();
    }

    public void exibirMensagem(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }

    public JTextField getTxtNome() { return txtNome; }
    public JTextField getTxtEstoque() { return txtEstoque; }
    public JTextField getTxtPreco() { return txtPreco; }
    public JTextField getTxtPesquisa() { return txtPesquisa; }
    public JTextField getTxtDataFabricacao() { return txtDataFabricacao; }
    public JTextField getTxtDataValidade() { return txtDataValidade; }
    public JComboBox<String> getCbTipo() { return cbTipo; }
    public JComboBox<String> getCbStatus() { return cbStatus; }
    public JComboBox<String> getCbFiltroTipo() { return cbFiltroTipo; }
    public JTable getTabela() { return tabela; }
    public DefaultTableModel getModel() { return model; }
    public JLabel getLblTotalProdutos() { return lblTotalProdutos; }
    public boolean isAdmin() { return isAdmin; }
}
