package view;

import controller.FornecedorController;
import controller.LoginController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FornecedorView extends JPanel {

    private JTextField txtNome, txtCpfCnpj, txtCep, txtNumero, txtEmail, txtEndereco, txtTelefone, txtPesquisa;
    private JTextArea txtTiposProdutos;
    private JTable tabela;
    private DefaultTableModel model;
    private FornecedorController controller;
    private JLabel lblTotal;
    private boolean isAdmin;

    public FornecedorView() {
        controller = new FornecedorController();
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

        JLabel lblTitulo = new JLabel("FORNECEDOR");
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
        menuLateral.setPreferredSize(new Dimension(280, 100));
        menuLateral.setBackground(Color.WHITE);
        menuLateral.setLayout(new BoxLayout(menuLateral, BoxLayout.Y_AXIS));
        menuLateral.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(25, 20, 25, 20)
        ));

        JLabel lblMenu = new JLabel("MENU PRINCIPAL");
        lblMenu.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblMenu.setForeground(new Color(34, 139, 34));
        lblMenu.setAlignmentX(Component.LEFT_ALIGNMENT);
        menuLateral.add(lblMenu);
        menuLateral.add(Box.createRigidArea(new Dimension(0, 25)));

        String[] menus = {"Clientes", "Produtos", "Registrar vendas", "Relatórios", "Fornecedor", "Usuários"};
        for (String menu : menus) {
            JButton btnMenu = criarBotaoMenuVerde(menu);
            btnMenu.addActionListener(e -> {
                switch (menu) {
                    case "Clientes" -> Navegador.irPara(AppFrame.CLIENTES);
                    case "Produtos" -> Navegador.irPara(AppFrame.PRODUTOS);
                    case "Registrar vendas" -> Navegador.irPara(AppFrame.VENDAS);
                    case "Relatórios" -> Navegador.irPara(AppFrame.RELATORIOS);
                    case "Fornecedor" -> JOptionPane.showMessageDialog(this, "Você já está em Fornecedores");
                    case "Usuários" -> Navegador.irParaUsuarios();
                }
            });
            menuLateral.add(btnMenu);
            menuLateral.add(Box.createRigidArea(new Dimension(0, 12)));
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
        areaCentral.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JPanel esquerdo = new JPanel(new BorderLayout(10, 10));
        esquerdo.setBackground(Color.WHITE);
        esquerdo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        esquerdo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        JLabel lblFornecedores = new JLabel("FORNECEDORES");
        lblFornecedores.setFont(new Font("Segoe UI", Font.BOLD, 18));
        esquerdo.add(lblFornecedores, BorderLayout.NORTH);

        JPanel painelPesquisa = new JPanel(new BorderLayout(10, 10));
        painelPesquisa.setOpaque(false);

        txtPesquisa = new JTextField();
        txtPesquisa.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPesquisa.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        JButton btnPesquisar = criarBotaoVerde();

        JPanel botoesPesquisa = new JPanel(new GridLayout(1, 1, 10, 10));
        botoesPesquisa.setOpaque(false);
        botoesPesquisa.add(btnPesquisar);

        painelPesquisa.add(txtPesquisa, BorderLayout.CENTER);
        painelPesquisa.add(btnPesquisar, BorderLayout.EAST);

        JPanel painelTotal = new JPanel(new FlowLayout(FlowLayout.LEFT));
        painelTotal.setOpaque(false);
        lblTotal = new JLabel("Total: " + controller.getTotalFornecedores() + " Fornecedor(es)");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotal.setForeground(new Color(34, 139, 34));
        painelTotal.add(lblTotal);

        String[] colunas = {"ID", "Nome", "CPF/CNPJ", "Telefone", "Email", "CEP", "Endereço", "Número", "Tipos de Produtos"};
        model = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabela = new JTable(model);
        tabela.setRowHeight(35);
        ModernUI.configureTableHeader(tabela);
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        tabela.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int i = 0; i < colunas.length; i++) {
            tabela.getColumnModel().getColumn(i).setPreferredWidth(150);
        }

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        JPanel painelBuscaTotal = new JPanel(new BorderLayout(10, 8));
        painelBuscaTotal.setOpaque(false);
        painelBuscaTotal.add(painelPesquisa, BorderLayout.CENTER);
        painelBuscaTotal.add(painelTotal, BorderLayout.SOUTH);

        esquerdo.add(painelBuscaTotal, BorderLayout.NORTH);
        esquerdo.add(scroll, BorderLayout.CENTER);

        areaCentral.add(esquerdo);
        areaCentral.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel direito = new JPanel(new BorderLayout());
        direito.setBackground(Color.WHITE);
        direito.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        direito.setMaximumSize(new Dimension(Integer.MAX_VALUE, 760));

        JLabel tituloForm = new JLabel("CADASTRO DE FORNECEDOR");
        tituloForm.setFont(new Font("Segoe UI", Font.BOLD, 22));
        tituloForm.setForeground(new Color(34, 139, 34));
        direito.add(tituloForm, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        formPanel.add(new JLabel("Nome *"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtNome = criarCampo();
        formPanel.add(txtNome, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        formPanel.add(new JLabel("CPF/CNPJ *"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtCpfCnpj = criarCampo();
        formPanel.add(txtCpfCnpj, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.3;
        formPanel.add(new JLabel("CEP *"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtCep = criarCampo();
        formPanel.add(txtCep, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.3;
        formPanel.add(new JLabel("Número *"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtNumero = criarCampo();
        formPanel.add(txtNumero, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.3;
        formPanel.add(new JLabel("Email *"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtEmail = criarCampo();
        formPanel.add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0.3;
        formPanel.add(new JLabel("Endereço *"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtEndereco = criarCampo();
        formPanel.add(txtEndereco, gbc);

        gbc.gridx = 0; gbc.gridy = 6; gbc.weightx = 0.3;
        formPanel.add(new JLabel("Telefone *"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtTelefone = criarCampo();
        formPanel.add(txtTelefone, gbc);

        gbc.gridx = 0; gbc.gridy = 7; gbc.weightx = 0.3;
        JLabel lblTiposProdutos = new JLabel("Tipos de Produtos:");
        lblTiposProdutos.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formPanel.add(lblTiposProdutos, gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtTiposProdutos = new JTextArea(3, 20);
        txtTiposProdutos.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtTiposProdutos.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        txtTiposProdutos.setLineWrap(true);
        txtTiposProdutos.setWrapStyleWord(true);
        JScrollPane scrollTipos = new JScrollPane(txtTiposProdutos);
        scrollTipos.setPreferredSize(new Dimension(280, 60));
        formPanel.add(scrollTipos, gbc);

        gbc.gridx = 0; gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel botoesForm = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        botoesForm.setOpaque(false);

        JButton btnSalvar = criarBotaoVerdeGrande("SALVAR");
        JButton btnEditar = criarBotaoVerdeGrande("EDITAR");
        JButton btnExcluir = criarBotaoVermelhoGrande();
        JButton btnLimpar = criarBotaoVerdeGrande("LIMPAR");

        if (!isAdmin) {
            btnSalvar.setEnabled(false);
            btnEditar.setEnabled(false);
            btnExcluir.setEnabled(false);
            btnSalvar.setToolTipText("Acesso negado - Apenas administradores");
            btnEditar.setToolTipText("Acesso negado - Apenas administradores");
            btnExcluir.setToolTipText("Acesso negado - Apenas administradores");
        }

        botoesForm.add(btnSalvar);
        botoesForm.add(btnEditar);
        botoesForm.add(btnExcluir);
        botoesForm.add(btnLimpar);

        formPanel.add(botoesForm, gbc);

        direito.add(formPanel, BorderLayout.CENTER);

        areaCentral.add(direito);

        JScrollPane scrollAreaCentral = new JScrollPane(areaCentral);
        scrollAreaCentral.setBorder(null);
        scrollAreaCentral.getViewport().setBackground(new Color(245, 245, 245));
        scrollAreaCentral.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollAreaCentral.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        areaCentralWrapper.add(scrollAreaCentral, BorderLayout.CENTER);

        conteudo.add(menuLateral, BorderLayout.WEST);
        conteudo.add(areaCentralWrapper, BorderLayout.CENTER);
        principal.add(conteudo, BorderLayout.CENTER);

        add(principal, BorderLayout.CENTER);

        btnLimpar.addActionListener(e -> limparCampos());
        btnSalvar.addActionListener(e -> controller.salvarFornecedor(this));
        btnExcluir.addActionListener(e -> controller.excluirFornecedor(this));
        btnEditar.addActionListener(e -> controller.editarFornecedor(this));
        btnPesquisar.addActionListener(e -> controller.pesquisarFornecedores(this));

        tabela.getSelectionModel().addListSelectionListener(
                e -> controller.carregarFornecedorSelecionado(this)
        );

        controller.carregarTabela(this);
        controller.atualizarTotal(this);
    }

    private JTextField criarCampo() {
        JTextField campo = new JTextField();
        campo.setPreferredSize(new Dimension(300, 40));
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return campo;
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
                BorderFactory.createEmptyBorder(12, 18, 12, 18)
        ));
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
                BorderFactory.createEmptyBorder(12, 18, 12, 18)
        ));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return botao;
    }

    private JButton criarBotaoVerde() {
        JButton botao = new JButton("PESQUISAR");
        botao.setBackground(new Color(34, 139, 34));
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.setPreferredSize(new Dimension(120, 40));
        return botao;
    }

    private JButton criarBotaoVerdeGrande(String texto) {
        JButton botao = new JButton(texto);
        botao.setBackground(new Color(34, 139, 34));
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.setPreferredSize(new Dimension(120, 45));
        return botao;
    }

    private JButton criarBotaoVermelhoGrande() {
        JButton botao = new JButton("EXCLUIR");
        botao.setBackground(new Color(220, 53, 69));
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.setPreferredSize(new Dimension(120, 45));
        return botao;
    }

    public void limparCampos() {
        txtNome.setText("");
        txtCpfCnpj.setText("");
        txtCep.setText("");
        txtNumero.setText("");
        txtEmail.setText("");
        txtEndereco.setText("");
        txtTelefone.setText("");
        txtTiposProdutos.setText("");
        txtPesquisa.setText("");
        txtNome.requestFocus();
    }

    public void exibirMensagem(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }

    public JTextField getTxtNome() { return txtNome; }
    public JTextField getTxtCpfCnpj() { return txtCpfCnpj; }
    public JTextField getTxtCep() { return txtCep; }
    public JTextField getTxtNumero() { return txtNumero; }
    public JTextField getTxtEmail() { return txtEmail; }
    public JTextField getTxtEndereco() { return txtEndereco; }
    public JTextField getTxtTelefone() { return txtTelefone; }
    public JTextField getTxtPesquisa()   { return txtPesquisa; }
    public JTextArea  getTxtTiposProdutos() { return txtTiposProdutos; }
    public JTable     getTabela() { return tabela; }
    public DefaultTableModel getModel() { return model; }
    public JLabel     getLblTotal() { return lblTotal; }
    public boolean    isAdmin() { return isAdmin; }
}
