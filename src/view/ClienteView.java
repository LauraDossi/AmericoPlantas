package view;

import controller.ClienteController;
import controller.LoginController;
import model.Cliente;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ClienteView extends JPanel {

    private JTextField txtNome, txtCpfCnpj, txtTelefone, txtEmail, txtCep, txtDataNascimento, txtEndereco, txtNumero, txtPesquisa;
    private JTable tabela;
    private DefaultTableModel model;
    private ClienteController controller;
    private boolean isAdmin;

    public ClienteView() {
        controller = new ClienteController();
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

        JLabel lblTitulo = new JLabel("CLIENTE");
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
                    case "Clientes" -> JOptionPane.showMessageDialog(this, "Você já está em Clientes");
                    case "Produtos" -> Navegador.irPara(AppFrame.PRODUTOS);
                    case "Registrar vendas" -> Navegador.irPara(AppFrame.VENDAS);
                    case "Relatórios" -> Navegador.irPara(AppFrame.RELATORIOS);
                    case "Fornecedor" -> Navegador.irPara(AppFrame.FORNECEDOR);
                    case "Usuários" -> Navegador.irParaUsuarios();
                }
            });
            menuLateral.add(btnMenu);
            menuLateral.add(Box.createRigidArea(new Dimension(0, 12)));
        }

        menuLateral.add(Box.createVerticalGlue());

        JButton btnSair = criarBotaoMenuVermelho("Sair");
        btnSair.addActionListener(e -> Navegador.irPara(AppFrame.LOGIN));
        menuLateral.add(btnSair);

        JPanel areaCentralWrapper = new JPanel(new BorderLayout());
        areaCentralWrapper.setOpaque(false);

        JPanel areaCentral = new JPanel();
        areaCentral.setLayout(new BoxLayout(areaCentral, BoxLayout.Y_AXIS));
        areaCentral.setOpaque(false);
        areaCentral.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        JPanel formulario = new JPanel(new GridBagLayout());
        formulario.setBackground(Color.WHITE);
        formulario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        formulario.setMaximumSize(new Dimension(Integer.MAX_VALUE, 780));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        gbc.gridx = 0; gbc.gridy = 0;
        formulario.add(new JLabel("Nome *"), gbc);
        gbc.gridx = 1;
        txtNome = criarCampo();
        formulario.add(txtNome, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formulario.add(new JLabel("CPF"), gbc);
        gbc.gridx = 1;
        txtCpfCnpj = criarCampo();
        formulario.add(txtCpfCnpj, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formulario.add(new JLabel("CEP"), gbc);
        gbc.gridx = 1;
        txtCep = criarCampo();
        formulario.add(txtCep, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        formulario.add(new JLabel("Número"), gbc);
        gbc.gridx = 1;
        txtNumero = criarCampo();
        formulario.add(txtNumero, gbc);

        gbc.gridx = 0; gbc.gridy = 6;
        formulario.add(new JLabel("Email"), gbc);
        gbc.gridx = 1;
        txtEmail = criarCampo();
        formulario.add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy = 7;
        formulario.add(new JLabel("Data de Nascimento"), gbc);
        gbc.gridx = 1;
        txtDataNascimento = criarCampo();
        formulario.add(txtDataNascimento, gbc);

        gbc.gridx = 0; gbc.gridy = 8;
        formulario.add(new JLabel("Endereço"), gbc);
        gbc.gridx = 1;
        txtEndereco = criarCampo();
        formulario.add(txtEndereco, gbc);

        gbc.gridx = 0; gbc.gridy = 9;
        formulario.add(new JLabel("Telefone"), gbc);
        gbc.gridx = 1;
        txtTelefone = criarCampo();
        formulario.add(txtTelefone, gbc);

        JPanel botoesForm = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        botoesForm.setOpaque(false);

        JButton btnSalvar = criarBotaoVerde("SALVAR");
        JButton btnEditar = criarBotaoVerde("EDITAR");
        JButton btnExcluir = criarBotaoVermelho("EXCLUIR");
        JButton btnLimpar = criarBotaoVerde("LIMPAR");

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

        gbc.gridx = 0; gbc.gridy = 10;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(30, 10, 10, 10);
        formulario.add(botoesForm, gbc);



        JPanel tabelaPanel = new JPanel(new BorderLayout(10, 10));
        tabelaPanel.setBackground(Color.WHITE);
        tabelaPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        tabelaPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 520));

        JPanel pesquisaPanel = new JPanel(new BorderLayout(10, 10));
        pesquisaPanel.setOpaque(false);
        txtPesquisa = new JTextField();
        txtPesquisa.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPesquisa.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        JButton btnPesquisar = criarBotaoVerde("BUSCAR");
        pesquisaPanel.add(txtPesquisa, BorderLayout.CENTER);
        pesquisaPanel.add(btnPesquisar, BorderLayout.EAST);

        String[] colunas = {"ID", "Nome", "CPF/CNPJ", "Telefone", "Email", "CEP", "Endereço", "Número", "Data Nasc."};
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

        tabela.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
                }
                return c;
            }
        });

        JScrollPane scrollTabela = new JScrollPane(tabela);
        scrollTabela.setPreferredSize(new Dimension(0, 300));

        tabelaPanel.add(pesquisaPanel, BorderLayout.NORTH);
        tabelaPanel.add(scrollTabela, BorderLayout.CENTER);

        areaCentral.add(tabelaPanel);
        areaCentral.add(Box.createRigidArea(new Dimension(0, 20)));
        areaCentral.add(formulario);

        JScrollPane scrollAreaCentral = new JScrollPane(areaCentral);
        scrollAreaCentral.setBorder(null);
        scrollAreaCentral.getViewport().setBackground(new Color(245, 245, 245));
        scrollAreaCentral.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        areaCentralWrapper.add(scrollAreaCentral, BorderLayout.CENTER);

        conteudo.add(menuLateral, BorderLayout.WEST);
        conteudo.add(areaCentralWrapper, BorderLayout.CENTER);
        principal.add(conteudo, BorderLayout.CENTER);

        add(principal, BorderLayout.CENTER);

        btnSalvar.addActionListener(e -> controller.salvarCliente(this));
        btnExcluir.addActionListener(e -> controller.excluirCliente(this));
        btnEditar.addActionListener(e -> controller.editarCliente(this));
        btnPesquisar.addActionListener(e -> controller.pesquisarClientes(this));

        tabela.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                controller.carregarClienteSelecionado(this);
            }
        });

        btnLimpar.addActionListener(e -> limparCampos());

        controller.carregarTabela(this);
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

    private JButton criarBotaoMenuVermelho(String texto) {
        JButton botao = new JButton(texto);
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

    private JButton criarBotaoVerde(String texto) {
        JButton botao = new JButton(texto);
        botao.setBackground(new Color(34, 139, 34));
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.setPreferredSize(new Dimension(130, 45));
        return botao;
    }

    private JButton criarBotaoVermelho(String texto) {
        JButton botao = new JButton(texto);
        botao.setBackground(new Color(220, 53, 69));
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setFont(new Font("Segoe UI", Font.BOLD, 14));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.setPreferredSize(new Dimension(130, 45));
        return botao;
    }

    public void limparCampos() {
        txtNome.setText("");
        txtCpfCnpj.setText("");
        txtCep.setText("");
        txtNumero.setText("");
        txtEmail.setText("");
        txtDataNascimento.setText("");
        txtEndereco.setText("");
        txtTelefone.setText("");
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
    public JTextField getTxtDataNascimento() { return txtDataNascimento; }
    public JTextField getTxtEndereco() { return txtEndereco; }
    public JTextField getTxtTelefone() { return txtTelefone; }
    public JTextField getTxtPesquisa() { return txtPesquisa; }
    public JTable getTabela() { return tabela; }
    public DefaultTableModel getModel() { return model; }
    public boolean isAdmin() { return isAdmin; }
}
