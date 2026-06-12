package view;

import controller.UsuarioController;
import controller.LoginController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class UsuarioView extends JPanel {

    private JTextField txtNome, txtCpf, txtDataNascimento, txtEndereco, txtCep, txtNumero, txtTelefone, txtEmail, txtLogin, txtPesquisa;
    private JPasswordField txtSenha;
    private JRadioButton rbAdministrador, rbOperador;
    private JTable tabela;
    private DefaultTableModel model;
    private UsuarioController controller;
    private JLabel lblTotal;
    private boolean isAdmin;

    public UsuarioView() {
        controller = new UsuarioController();
        isAdmin = LoginController.isAdministrador();
        setLayout(new BorderLayout());

        if (!isAdmin) {
            JOptionPane.showMessageDialog(null, "Acesso negado! Apenas administradores podem acessar Usuários.");
            JLabel lbl = new JLabel("Acesso negado", SwingConstants.CENTER);
            add(lbl, BorderLayout.CENTER);
            return;
        }

        iniciarComponentes();
    }

    private void iniciarComponentes() {
        JPanel principal = new JPanel(new BorderLayout(14, 14));
        principal.setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        principal.setBackground(new Color(245, 245, 245));

        JPanel topo = new JPanel(new BorderLayout());
        topo.setBackground(new Color(34, 139, 34));
        topo.setPreferredSize(new Dimension(100, 70));

        JPanel topoEsquerdo = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        topoEsquerdo.setOpaque(false);
        topoEsquerdo.setBackground(new Color(34, 139, 34));

        JLabel lblTitulo = new JLabel("USUÁRIOS");
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
        btnVoltar.setPreferredSize(new Dimension(150, 40));
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
                    case "Registrar vendas" -> Navegador.irPara(AppFrame.VENDAS);
                    case "Relatórios" -> Navegador.irPara(AppFrame.RELATORIOS);
                    case "Fornecedor" -> Navegador.irPara(AppFrame.FORNECEDOR);
                    case "Usuários" -> JOptionPane.showMessageDialog(this, "Você já está em Usuários");
                }
            });
            menuLateral.add(btnMenu);
            menuLateral.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        menuLateral.add(Box.createVerticalGlue());

        JButton btnSair = criarBotaoMenuVermelho();
        btnSair.addActionListener(e -> Navegador.irPara(AppFrame.LOGIN));
        menuLateral.add(btnSair);

        JPanel areaCentralWrapper = new JPanel(new BorderLayout());
        areaCentralWrapper.setOpaque(false);

        JPanel areaCentral = new ResponsivePanel();
        areaCentral.setLayout(new BoxLayout(areaCentral, BoxLayout.Y_AXIS));
        areaCentral.setOpaque(false);
        areaCentral.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JPanel esquerdo = new JPanel(new BorderLayout(10, 10));
        esquerdo.setBackground(Color.WHITE);
        esquerdo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        esquerdo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 350));

        JLabel lblUsuarios = new JLabel("USUÁRIOS");
        lblUsuarios.setFont(new Font("Segoe UI", Font.BOLD, 18));
        esquerdo.add(lblUsuarios, BorderLayout.NORTH);

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
        lblTotal = new JLabel("Total: " + controller.getTotalUsuarios() + " Usuário(s)");
        lblTotal.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTotal.setForeground(new Color(34, 139, 34));
        painelTotal.add(lblTotal);

        String[] colunas = {"ID", "Nome", "Login", "CPF", "Telefone", "Email", "Tipo", "Endereço", "Número", "CEP", "Data Nasc."};
        model = new DefaultTableModel(colunas, 0){
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabela = new JTable(model);
        tabela.setRowHeight(35);
        ModernUI.configureTableHeader(tabela);
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JScrollPane scroll = new JScrollPane(tabela);
        scroll.setPreferredSize(new Dimension(0, 250));

        JPanel painelBuscaTotal = new JPanel(new BorderLayout(10, 8));
        painelBuscaTotal.setOpaque(false);
        painelBuscaTotal.add(painelPesquisa, BorderLayout.CENTER);
        painelBuscaTotal.add(painelTotal, BorderLayout.SOUTH);

        esquerdo.add(painelBuscaTotal, BorderLayout.NORTH);
        esquerdo.add(scroll, BorderLayout.CENTER);

        JPanel direito = new JPanel(new BorderLayout());
        direito.setBackground(Color.WHITE);
        direito.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));
        direito.setMaximumSize(new Dimension(Integer.MAX_VALUE, 820));

        JLabel tituloForm = new JLabel("CADASTRO DE USUÁRIO");
        tituloForm.setFont(new Font("Segoe UI", Font.BOLD, 22));
        tituloForm.setForeground(new Color(34, 139, 34));
        tituloForm.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        direito.add(tituloForm, BorderLayout.NORTH);

        JPanel formPanel = new ResponsivePanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);

        JPanel camposPanel = new JPanel(new GridBagLayout());
        camposPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        camposPanel.add(new JLabel("Nome *"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtNome = criarCampo();
        camposPanel.add(txtNome, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        camposPanel.add(new JLabel("CPF *"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtCpf = criarCampo();
        camposPanel.add(txtCpf, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.3;
        camposPanel.add(new JLabel("Data de nascimento *"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtDataNascimento = criarCampo();
        camposPanel.add(txtDataNascimento, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.3;
        camposPanel.add(new JLabel("Endereço *"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtEndereco = criarCampo();
        camposPanel.add(txtEndereco, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.3;
        camposPanel.add(new JLabel("Número *"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtNumero = criarCampo();
        camposPanel.add(txtNumero, gbc);

        gbc.gridx = 0; gbc.gridy = 6; gbc.weightx = 0.3;
        camposPanel.add(new JLabel("CEP *"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtCep = criarCampo();
        camposPanel.add(txtCep, gbc);

        gbc.gridx = 0; gbc.gridy = 7; gbc.weightx = 0.3;
        camposPanel.add(new JLabel("Telefone *"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtTelefone = criarCampo();
        camposPanel.add(txtTelefone, gbc);

        gbc.gridx = 0; gbc.gridy = 8; gbc.weightx = 0.3;
        camposPanel.add(new JLabel("Email *"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtEmail = criarCampo();
        camposPanel.add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 9; gbc.weightx = 0.3;
        camposPanel.add(new JLabel("LOGIN *"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtLogin = criarCampo();
        camposPanel.add(txtLogin, gbc);

        gbc.gridx = 0; gbc.gridy = 10; gbc.weightx = 0.3;
        camposPanel.add(new JLabel("SENHA *"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        txtSenha = new JPasswordField();
        txtSenha.setPreferredSize(new Dimension(240, 40));
        txtSenha.setMinimumSize(new Dimension(140, 40));
        txtSenha.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtSenha.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        camposPanel.add(txtSenha, gbc);

        gbc.gridx = 0; gbc.gridy = 11; gbc.weightx = 0.3;
        JLabel lblTipo = new JLabel("TIPO DE USUÁRIO *");
        lblTipo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        camposPanel.add(lblTipo, gbc);

        gbc.gridx = 1; gbc.weightx = 0.7;
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        radioPanel.setOpaque(false);
        rbAdministrador = new JRadioButton("Administrador");
        rbOperador = new JRadioButton("Operador");
        ButtonGroup grupoTipo = new ButtonGroup();
        grupoTipo.add(rbAdministrador);
        grupoTipo.add(rbOperador);
        rbAdministrador.setSelected(true);
        radioPanel.add(rbAdministrador);
        radioPanel.add(rbOperador);
        camposPanel.add(radioPanel, gbc);

        formPanel.add(camposPanel);

        JPanel botoesForm = new JPanel(new GridLayout(2, 2, 12, 10));
        botoesForm.setOpaque(false);
        botoesForm.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));

        JButton btnSalvar = new JButton("SALVAR");
        JButton btnEditar = new JButton("EDITAR");
        JButton btnExcluir = new JButton("EXCLUIR");
        JButton btnLimpar = new JButton("LIMPAR");

        btnSalvar.setBackground(new Color(34, 139, 34));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFocusPainted(false);
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSalvar.setPreferredSize(new Dimension(112, 42));

        btnEditar.setBackground(new Color(34, 139, 34));
        btnEditar.setForeground(Color.WHITE);
        btnEditar.setFocusPainted(false);
        btnEditar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnEditar.setPreferredSize(new Dimension(112, 42));

        btnExcluir.setBackground(new Color(220, 53, 69));
        btnExcluir.setForeground(Color.WHITE);
        btnExcluir.setFocusPainted(false);
        btnExcluir.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnExcluir.setPreferredSize(new Dimension(112, 42));

        btnLimpar.setBackground(new Color(34, 139, 34));
        btnLimpar.setForeground(Color.WHITE);
        btnLimpar.setFocusPainted(false);
        btnLimpar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLimpar.setPreferredSize(new Dimension(112, 42));

        btnSalvar.addActionListener(e -> controller.salvarUsuario(this));
        btnEditar.addActionListener(e -> controller.editarUsuario(this));
        btnExcluir.addActionListener(e -> controller.excluirUsuario(this));
        btnLimpar.addActionListener(e -> limparCampos());

        botoesForm.add(btnSalvar);
        botoesForm.add(btnEditar);
        botoesForm.add(btnExcluir);
        botoesForm.add(btnLimpar);

        formPanel.add(botoesForm);

        JScrollPane scrollForm = new JScrollPane(formPanel);
        scrollForm.setBorder(null);
        scrollForm.getViewport().setBackground(Color.WHITE);
        scrollForm.setPreferredSize(new Dimension(0, 550));
        scrollForm.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollForm.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        direito.add(scrollForm, BorderLayout.CENTER);

        JPanel painelLados = new JPanel(new GridLayout(1, 2, 12, 0));
        painelLados.setOpaque(false);
        painelLados.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        areaCentral.add(esquerdo);
        areaCentral.add(Box.createRigidArea(new Dimension(0, 15)));
        areaCentral.add(direito);

        areaCentral.add(painelLados);

        JScrollPane scrollAreaCentral = new JScrollPane(areaCentral);
        scrollAreaCentral.setBorder(null);
        scrollAreaCentral.getViewport().setBackground(new Color(245, 245, 245));
        scrollAreaCentral.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollAreaCentral.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        areaCentralWrapper.add(scrollAreaCentral, BorderLayout.CENTER);

        conteudo.add(menuLateral, BorderLayout.WEST);
        conteudo.add(areaCentralWrapper, BorderLayout.CENTER);
        principal.add(conteudo, BorderLayout.CENTER);

        add(principal, BorderLayout.CENTER);

        btnPesquisar.addActionListener(e -> controller.pesquisarUsuarios(this));

        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                controller.carregarUsuarioSelecionado(this);
            }
        });

        controller.carregarTabela(this);
        controller.atualizarTotal(this);
    }

    private JTextField criarCampo() {
        JTextField campo = new JTextField();
        campo.setPreferredSize(new Dimension(240, 40));
        campo.setMinimumSize(new Dimension(140, 40));
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
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

    private JButton criarBotaoVerde() {
        JButton botao = new JButton("PESQUISAR");
        botao.setBackground(new Color(34, 139, 34));
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.setPreferredSize(new Dimension(110, 40));
        return botao;
    }

    private static class ResponsivePanel extends JPanel implements Scrollable {
        @Override
        public Dimension getPreferredScrollableViewportSize() { return getPreferredSize(); }
        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) { return 16; }
        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) { return 64; }
        @Override
        public boolean getScrollableTracksViewportWidth() { return true; }
        @Override
        public boolean getScrollableTracksViewportHeight() { return false; }
    }

    public void limparCampos() {
        txtNome.setText("");
        txtCpf.setText("");
        txtDataNascimento.setText("");
        txtEndereco.setText("");
        txtNumero.setText("");
        txtCep.setText("");
        txtTelefone.setText("");
        txtEmail.setText("");
        txtLogin.setText("");
        txtSenha.setText("");
        txtPesquisa.setText("");
        rbAdministrador.setSelected(true);
        txtNome.requestFocus();
    }

    public void exibirMensagem(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }

    public JTextField getTxtNome() { return txtNome; }
    public JTextField getTxtCpf() { return txtCpf; }
    public JTextField getTxtDataNascimento() { return txtDataNascimento; }
    public JTextField getTxtEndereco() { return txtEndereco; }
    public JTextField getTxtCep() { return txtCep; }
    public JTextField getTxtNumero() { return txtNumero; }
    public JTextField getTxtTelefone() { return txtTelefone; }
    public JTextField getTxtEmail() { return txtEmail; }
    public JTextField getTxtLogin() { return txtLogin; }
    public JTextField getTxtPesquisa() { return txtPesquisa; }
    public JPasswordField getTxtSenha() { return txtSenha; }
    public JTable getTabela() { return tabela; }
    public DefaultTableModel getModel() { return model; }
    public JLabel getLblTotal() { return lblTotal; }
    public boolean isAdministradorSelecionado(){ return rbAdministrador.isSelected(); }
    public JRadioButton getRbAdministrador() { return rbAdministrador; }
    public JRadioButton getRbOperador() { return rbOperador; }
}
