package view;

import controller.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RelatorioView extends JPanel {

    private JComboBox<String> cbTipoRelatorio;
    private JTextField txtDataInicio, txtDataFim;
    private JTextArea txtResumo;
    private JTable tabelaDetalhes;
    private DefaultTableModel model;
    private boolean isAdmin;
    private RelatorioController controller;

    public RelatorioView() {
        controller = new RelatorioController();
        isAdmin = LoginController.isAdministrador();
        setLayout(new BorderLayout());
        iniciarComponentes();
        controller.gerarRelatorio(this);
    }

    private void iniciarComponentes() {
        JPanel principal = new JPanel(new BorderLayout(20, 20));
        principal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        principal.setBackground(new Color(245, 245, 245));

        // ===== TOPO =====
        JPanel topo = new JPanel(new BorderLayout());
        topo.setBackground(new Color(34, 139, 34));
        topo.setPreferredSize(new Dimension(100, 70));

        JPanel topoEsquerdo = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 15));
        topoEsquerdo.setOpaque(false);
        JLabel lblTitulo = new JLabel("RELATÓRIOS");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        topoEsquerdo.add(lblTitulo);
        topo.add(topoEsquerdo, BorderLayout.WEST);

        JPanel topoDireito = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 15));
        topoDireito.setOpaque(false);
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

        // ===== CONTEÚDO =====
        JPanel conteudo = new JPanel(new BorderLayout(20, 20));
        conteudo.setOpaque(false);
        conteudo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // ===== MENU LATERAL =====
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
                    case "Relatórios" -> JOptionPane.showMessageDialog(this, "Você já está em Relatórios");
                    case "Fornecedor" -> Navegador.irPara(AppFrame.FORNECEDOR);
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

        // ===== ÁREA CENTRAL =====
        JPanel areaCentralWrapper = new JPanel(new BorderLayout());
        areaCentralWrapper.setOpaque(false);

        JPanel areaCentral = new JPanel();
        areaCentral.setLayout(new BoxLayout(areaCentral, BoxLayout.Y_AXIS));
        areaCentral.setOpaque(false);

        // Painel de controle
        JPanel painelControle = new JPanel(new GridBagLayout());
        painelControle.setBackground(Color.WHITE);
        painelControle.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        painelControle.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        painelControle.add(new JLabel("Tipo de Relatório:"), gbc);
        gbc.gridx = 1;
        cbTipoRelatorio = new JComboBox<>(new String[]{
                "Clientes", "Produtos", "Fornecedores", "Usuários", "Vendas", "Resumo geral"
        });
        painelControle.add(cbTipoRelatorio, gbc);

        gbc.gridx = 2;
        painelControle.add(new JLabel("Data de início:"), gbc);
        gbc.gridx = 3;
        txtDataInicio = new JTextField(10);
        txtDataInicio.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        painelControle.add(txtDataInicio, gbc);

        gbc.gridx = 4;
        painelControle.add(new JLabel("Data Fim:"), gbc);
        gbc.gridx = 5;
        txtDataFim = new JTextField(10);
        txtDataFim.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        painelControle.add(txtDataFim, gbc);

        gbc.gridx = 6;
        JButton btnGerar = criarBotaoVerde("GERAR RELATÓRIO");
        btnGerar.addActionListener(e -> controller.gerarRelatorio(this));
        painelControle.add(btnGerar, gbc);

        areaCentral.add(painelControle);
        areaCentral.add(Box.createRigidArea(new Dimension(0, 15)));

        // Painel resumo
        JPanel painelResumo = new JPanel(new BorderLayout());
        painelResumo.setBackground(Color.WHITE);
        painelResumo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        painelResumo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        JLabel lblResumo = new JLabel("RESUMO");
        lblResumo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        painelResumo.add(lblResumo, BorderLayout.NORTH);

        txtResumo = new JTextArea();
        txtResumo.setEditable(false);
        txtResumo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtResumo.setBackground(new Color(250, 250, 250));
        JScrollPane scrollResumo = new JScrollPane(txtResumo);
        scrollResumo.setPreferredSize(new Dimension(0, 150));
        painelResumo.add(scrollResumo, BorderLayout.CENTER);

        areaCentral.add(painelResumo);
        areaCentral.add(Box.createRigidArea(new Dimension(0, 15)));

        // Painel tabela
        JPanel painelTabela = new JPanel(new BorderLayout());
        painelTabela.setBackground(Color.WHITE);
        painelTabela.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        painelTabela.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));

        JLabel lblDetalhes = new JLabel("DETALHES");
        lblDetalhes.setFont(new Font("Segoe UI", Font.BOLD, 16));
        painelTabela.add(lblDetalhes, BorderLayout.NORTH);

        model = new DefaultTableModel();
        tabelaDetalhes = new JTable(model);
        tabelaDetalhes.setRowHeight(30);
        ModernUI.configureTableHeader(tabelaDetalhes);
        tabelaDetalhes.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabelaDetalhes.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        JScrollPane scrollTabela = new JScrollPane(tabelaDetalhes);
        scrollTabela.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollTabela.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        painelTabela.add(scrollTabela, BorderLayout.CENTER);

        areaCentral.add(painelTabela);
        areaCentral.add(Box.createRigidArea(new Dimension(0, 15)));

        // Painel salvar
        JPanel painelSalvar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        painelSalvar.setBackground(Color.WHITE);
        painelSalvar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        painelSalvar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        JButton btnSalvarTXT = criarBotaoVerde("SALVAR COMO TXT");
        btnSalvarTXT.addActionListener(e -> controller.salvarRelatorioTXT(this));
        painelSalvar.add(btnSalvarTXT);

        JButton btnSalvarBanco = criarBotaoVerde("SALVAR NO BANCO");
        btnSalvarBanco.addActionListener(e -> controller.salvarNoBanco(this));
        painelSalvar.add(btnSalvarBanco);

        areaCentral.add(painelSalvar);

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
        botao.setPreferredSize(new Dimension(180, 45));
        return botao;
    }

    public void exibirMensagem(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }

    public JComboBox<String> getCbTipoRelatorio() { return cbTipoRelatorio; }
    public JTextField getTxtDataInicio() { return txtDataInicio; }
    public JTextField getTxtDataFim() { return txtDataFim; }
    public JTextArea getTxtResumo() { return txtResumo; }
    public JTable getTabelaDetalhes() { return tabelaDetalhes; }
    public DefaultTableModel getModel() { return model; }
}
