package view;

import controller.*;
import model.Cliente;
import model.Produto;
import model.ProdutoDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.List;

public class MenuPrincipalView extends JPanel {

    private boolean isAdmin;
    private ClienteController clienteController;
    private ProdutoDAO dao;
    private FornecedorController fornecedorController;
    private VendaController vendaController;
    private UsuarioController usuarioController;
    private JLabel lblData;
    private JLabel lblHora;
    private JPanel painelNotificacoes;
    private JLabel lblAlertasCount;

    public MenuPrincipalView() {
        clienteController = new ClienteController();
        ProdutoController produtoController = new ProdutoController();
        dao = new ProdutoDAO();
        fornecedorController = new FornecedorController();
        vendaController = new VendaController();
        usuarioController = new UsuarioController();
        isAdmin = LoginController.isAdministrador();
        setLayout(new BorderLayout());
        iniciarComponentes();

        new javax.swing.Timer(1000, e -> atualizarDataHora()).start();
        new javax.swing.Timer(30000, e -> atualizarNotificacoes()).start();
    }

    private void atualizarDataHora() {
        if (lblData != null)
            lblData.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        if (lblHora != null)
            lblHora.setText(java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
    }

    private void atualizarNotificacoes() {
        carregarNotificacoes();
        atualizarAlertasCount();
    }

    private void atualizarAlertasCount() {
        List<Produto> produtos = dao.listar();
        int vencimentoProximo = 0, estoqueBaixo = 0;
        for (Produto p : produtos) {
            int d = calcularDiasVencimentoNumero(p.getDataValidade().toString());
            if (d >= 0 && d <= 30) vencimentoProximo++;
            if (p.getEstoque() <= 20) estoqueBaixo++;
        }
        int total = vencimentoProximo + estoqueBaixo;
        lblAlertasCount.setText(String.valueOf(total));
        lblAlertasCount.setForeground(total > 0 ? new Color(220, 53, 69) : new Color(46, 204, 113));
    }

    private int calcularDiasVencimentoNumero(String dataValidade) {
        if (dataValidade == null || dataValidade.isEmpty()) return 999;
        try {
            LocalDate validade = LocalDate.parse(dataValidade,
                    DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            return (int) ChronoUnit.DAYS.between(LocalDate.now(), validade);
        } catch (Exception e) { return 999; }
    }

    private void iniciarComponentes() {
        JPanel principal = new JPanel(new BorderLayout());
        principal.setBackground(new Color(240, 242, 245));

        // ===== TOPO =====
        JPanel topo = new JPanel(new BorderLayout());
        topo.setBackground(new Color(34, 139, 34));
        topo.setPreferredSize(new Dimension(100, 85));

        JPanel topoEsquerdo = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 20));
        topoEsquerdo.setOpaque(false);
        JLabel lblTitulo = new JLabel("AMERICO PLANTAS MEDICINAIS");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        topoEsquerdo.add(lblTitulo);
        topo.add(topoEsquerdo, BorderLayout.WEST);

        JPanel topoDireito = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 20));
        topoDireito.setOpaque(false);

        String tipoUsuario = LoginController.isAdministrador() ? "ADMINISTRADOR" : "OPERADOR";
        JLabel lblUsuario = new JLabel("Usuário: " + LoginController.getUsuarioLogado().getNome() + " (" + tipoUsuario + ")");
        lblUsuario.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblUsuario.setForeground(Color.WHITE);

        lblData = new JLabel();
        lblData.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblData.setForeground(Color.WHITE);

        lblHora = new JLabel();
        lblHora.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblHora.setForeground(Color.WHITE);

        topoDireito.add(lblUsuario);
        topoDireito.add(Box.createRigidArea(new Dimension(15, 0)));
        topoDireito.add(lblData);
        topoDireito.add(Box.createRigidArea(new Dimension(10, 0)));
        topoDireito.add(lblHora);
        topo.add(topoDireito, BorderLayout.EAST);

        principal.add(topo, BorderLayout.NORTH);

        // ===== CONTEÚDO =====
        JPanel conteudo = new JPanel(new BorderLayout(22, 15));
        conteudo.setOpaque(false);
        conteudo.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 24));

        // ===== MENU LATERAL =====
        JPanel menuLateral = new JPanel();
        menuLateral.setPreferredSize(new Dimension(260, 100));
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
                    case "Produtos" -> Navegador.irPara(AppFrame.PRODUTOS);
                    case "Registrar vendas" -> Navegador.irPara(AppFrame.VENDAS);
                    case "Relatórios" -> Navegador.irPara(AppFrame.RELATORIOS);
                    case "Fornecedor" -> Navegador.irPara(AppFrame.FORNECEDOR);
                    case "Usuários" -> Navegador.irParaUsuarios();
                }
            });
            menuLateral.add(btnMenu);
            menuLateral.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        menuLateral.add(Box.createVerticalGlue());

        JButton btnSair = criarBotaoMenuVermelho("Sair");
        btnSair.addActionListener(e -> Navegador.irPara(AppFrame.LOGIN));
        menuLateral.add(btnSair);

        // ===== ÁREA CENTRAL =====
        JPanel areaCentralWrapper = new JPanel(new BorderLayout());
        areaCentralWrapper.setOpaque(false);

        JPanel areaCentral = new JPanel();
        areaCentral.setLayout(new BoxLayout(areaCentral, BoxLayout.Y_AXIS));
        areaCentral.setOpaque(false);
        areaCentral.setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 0));

        // Título e alertas
        JPanel tituloPanel = new JPanel(new BorderLayout());
        tituloPanel.setOpaque(false);
        tituloPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        JLabel lblDashboard = new JLabel("DASHBOARD GERENCIAL");
        lblDashboard.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblDashboard.setForeground(new Color(34, 139, 34));

        JLabel lblSubtitulo = new JLabel("Visão geral do sistema de gestão da Americo Plantas Medicinais");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitulo.setForeground(new Color(87, 108, 96));

        JPanel textoDashboard = new JPanel();
        textoDashboard.setOpaque(false);
        textoDashboard.setLayout(new BoxLayout(textoDashboard, BoxLayout.Y_AXIS));
        textoDashboard.add(lblDashboard);
        textoDashboard.add(Box.createRigidArea(new Dimension(0, 6)));
        textoDashboard.add(lblSubtitulo);
        tituloPanel.add(textoDashboard, BorderLayout.WEST);

        JPanel alertaTopo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        alertaTopo.setOpaque(false);
        JLabel lblAlertasIcon = new JLabel("ALERTAS:");
        lblAlertasIcon.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblAlertasCount = new JLabel("0");
        lblAlertasCount.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblAlertasCount.setForeground(new Color(46, 204, 113));
        alertaTopo.add(lblAlertasIcon);
        alertaTopo.add(lblAlertasCount);
        tituloPanel.add(alertaTopo, BorderLayout.EAST);

        areaCentral.add(tituloPanel);
        areaCentral.add(Box.createRigidArea(new Dimension(0, 15)));

        // ===== CARDS =====
        JPanel cardsSuperiores = new JPanel(new GridLayout(1, 5, 15, 0));
        cardsSuperiores.setOpaque(false);
        cardsSuperiores.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        int totalClientes    = clienteController.listarClientes().size();
        int totalProdutos    = dao.listar().size();
        int totalFornecedores = fornecedorController.getTotalFornecedores();
        int totalUsuarios    = usuarioController.getTotalUsuarios();
        double totalVendas   = vendaController.getTotalVendas();

        cardsSuperiores.add(criarCard("CLIENTES",     String.valueOf(totalClientes),    "Total de clientes cadastrados",   new Color(52, 152, 219)));
        cardsSuperiores.add(criarCard("PRODUTOS",     String.valueOf(totalProdutos),    "Total de produtos no estoque",    new Color(46, 204, 113)));
        cardsSuperiores.add(criarCard("FORNECEDORES", String.valueOf(totalFornecedores),"Fornecedores ativos",             new Color(155, 89, 182)));
        cardsSuperiores.add(criarCard("USUÁRIOS",     String.valueOf(totalUsuarios),    "Usuários do sistema",             new Color(255, 159, 64)));
        cardsSuperiores.add(criarCard("VENDAS",       String.format("R$ %.2f", totalVendas), "Faturamento total",         new Color(241, 196, 15)));

        areaCentral.add(cardsSuperiores);
        areaCentral.add(Box.createRigidArea(new Dimension(0, 20)));

        // ===== NOTIFICAÇÕES =====
        JPanel painelNotificacoesWrapper = new JPanel(new BorderLayout());
        painelNotificacoesWrapper.setBackground(Color.WHITE);
        painelNotificacoesWrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        painelNotificacoesWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        JLabel lblNotifTitulo = new JLabel("CENTRO DE NOTIFICAÇÕES");
        lblNotifTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblNotifTitulo.setForeground(new Color(34, 139, 34));
        painelNotificacoesWrapper.add(lblNotifTitulo, BorderLayout.NORTH);

        painelNotificacoes = new JPanel();
        painelNotificacoes.setLayout(new BoxLayout(painelNotificacoes, BoxLayout.Y_AXIS));
        painelNotificacoes.setBackground(Color.WHITE);

        JScrollPane scrollNotif = new JScrollPane(painelNotificacoes);
        scrollNotif.setBorder(null);
        scrollNotif.setPreferredSize(new Dimension(0, 160));
        scrollNotif.getViewport().setBackground(Color.WHITE);
        painelNotificacoesWrapper.add(scrollNotif, BorderLayout.CENTER);

        areaCentral.add(painelNotificacoesWrapper);
        areaCentral.add(Box.createRigidArea(new Dimension(0, 20)));

        // ===== GRÁFICOS =====
        JPanel painelAnalise = criarGraficoAnaliseClientes();
        painelAnalise.setMaximumSize(new Dimension(Integer.MAX_VALUE, 400));
        areaCentral.add(painelAnalise);
        areaCentral.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel painelMaisVendidos = criarGraficoProdutosMaisVendidos();
        painelMaisVendidos.setMaximumSize(new Dimension(Integer.MAX_VALUE, 280));
        areaCentral.add(painelMaisVendidos);
        areaCentral.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel painelEstoque = criarGraficoEstoqueProdutos();
        painelEstoque.setMaximumSize(new Dimension(Integer.MAX_VALUE, 280));
        areaCentral.add(painelEstoque);
        areaCentral.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel painelMenosVendidos = criarGraficoProdutosMenosVendidos();
        painelMenosVendidos.setMaximumSize(new Dimension(Integer.MAX_VALUE, 280));
        areaCentral.add(painelMenosVendidos);
        areaCentral.add(Box.createRigidArea(new Dimension(0, 20)));

        JScrollPane scrollAreaCentral = new JScrollPane(areaCentral);
        scrollAreaCentral.setBorder(null);
        scrollAreaCentral.getViewport().setBackground(new Color(240, 242, 245));
        scrollAreaCentral.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        areaCentralWrapper.add(scrollAreaCentral, BorderLayout.CENTER);

        conteudo.add(menuLateral, BorderLayout.WEST);
        conteudo.add(areaCentralWrapper, BorderLayout.CENTER);
        principal.add(conteudo, BorderLayout.CENTER);

        add(principal, BorderLayout.CENTER);

        carregarNotificacoes();
        atualizarAlertasCount();
        atualizarDataHora();
    }

    // ===== BOTÕES =====
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

    private JButton criarBotaoMenuVermelho(String texto) {
        JButton botao = new JButton(texto);
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

    // ===== GRÁFICOS =====
    private JPanel criarGraficoAnaliseClientes() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(0, 380));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblTitulo = new JLabel("ANÁLISE DE CLIENTES");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(34, 139, 34));
        panel.add(lblTitulo, BorderLayout.NORTH);

        JPanel conteudoGrafico = new JPanel(new GridLayout(1, 2, 10, 0));
        conteudoGrafico.setOpaque(false);

        clienteController.listarClientes();
        int jovens = 0, adultos = 0, idosos = 0;

        for (Cliente c : clienteController.listarClientes()) {
            String dataNasc = c.getDataNascimento();

            if (dataNasc == null || dataNasc.trim().isEmpty()) continue;

            try {
                LocalDate nascimento = LocalDate.parse(dataNasc, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                int idade = (int) ChronoUnit.YEARS.between(nascimento, LocalDate.now());

                if (idade <= 30) jovens++;
                else if (idade <= 59) adultos++;
                else idosos++;

            } catch (Exception ignored) {}
        }

        JPanel idadePanel = new JPanel(new BorderLayout());
        idadePanel.setBorder(BorderFactory.createTitledBorder("Faixa etária"));

        IdadeBarraPanel idadeBarra = new IdadeBarraPanel(jovens, adultos, idosos);
        idadeBarra.setPreferredSize(new Dimension(300, 300));

        JPanel legendaIdade = new JPanel();
        legendaIdade.setLayout(new BoxLayout(legendaIdade, BoxLayout.Y_AXIS));
        legendaIdade.add(criarLegendaCor("18-30 anos", jovens,  new Color(46, 204, 113)));
        legendaIdade.add(criarLegendaCor("31-59 anos", adultos, new Color(52, 152, 219)));
        legendaIdade.add(criarLegendaCor("60+ anos", idosos,  new Color(155, 89, 182)));

        idadePanel.add(idadeBarra, BorderLayout.CENTER);
        idadePanel.add(legendaIdade, BorderLayout.EAST);

        conteudoGrafico.add(idadePanel);
        panel.add(conteudoGrafico, BorderLayout.CENTER);
        return panel;
    }

    private JPanel criarGraficoProdutosMaisVendidos() {
        JPanel panel = criarPainelGrafico("PRODUTOS MAIS VENDIDOS");
        List<Produto> produtos = dao.listar();
        Map<String, Integer> vendas = new HashMap<>();
        for (Produto p : produtos) vendas.put(p.getNome(), (int)(Math.random() * 100) + 10);

        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(vendas.entrySet());
        sorted.sort((a, b) -> b.getValue().compareTo(a.getValue()));
        List<Map.Entry<String, Integer>> top5 = sorted.stream().limit(5).toList();

        String[] colunas = {"Produto", "Quantidade Vendida", "%"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        int total = top5.stream().mapToInt(Map.Entry::getValue).sum();
        if (total == 0) total = 1;
        for (Map.Entry<String, Integer> e : top5)
            model.addRow(new Object[]{e.getKey(), e.getValue(), (e.getValue() * 100 / total) + "%"});

        JTable tabela = new JTable(model);
        tabela.setRowHeight(30);
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        ModernUI.configureTableHeader(tabela);
        panel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        return panel;
    }

    private JPanel criarGraficoEstoqueProdutos() {
        JPanel panel = criarPainelGrafico("ESTOQUE POR PRODUTO");
        List<Produto> produtos = dao.listar();
        String[] colunas = {"Produto", "Estoque Atual", "Status"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        for (Produto p : produtos) {
            String status = p.getEstoque() > 50 ? "OK" : (p.getEstoque() > 20 ? "Atenção" : "Crítico");
            model.addRow(new Object[]{p.getNome(), p.getEstoque(), status});
        }
        if (model.getRowCount() == 0) model.addRow(new Object[]{"Nenhum produto cadastrado", "-", "-"});
        JTable tabela = new JTable(model);
        tabela.setRowHeight(30);
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        ModernUI.configureTableHeader(tabela);
        panel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        return panel;
    }

    private JPanel criarGraficoProdutosMenosVendidos() {
        JPanel panel = criarPainelGrafico("PRODUTOS MENOS VENDIDOS");
        List<Produto> produtos = dao.listar();
        Map<String, Integer> vendas = new HashMap<>();
        for (Produto p : produtos) vendas.put(p.getNome(), (int)(Math.random() * 100) + 1);

        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(vendas.entrySet());
        sorted.sort(Comparator.comparing(Map.Entry::getValue));
        List<Map.Entry<String, Integer>> bottom5 = sorted.stream().limit(5).toList();

        String[] colunas = {"Produto", "Quantidade Vendida", "%"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0);
        int total = bottom5.stream().mapToInt(Map.Entry::getValue).sum();
        if (total == 0) total = 1;
        for (Map.Entry<String, Integer> e : bottom5)
            model.addRow(new Object[]{e.getKey(), e.getValue(), (e.getValue() * 100 / total) + "%"});

        JTable tabela = new JTable(model);
        tabela.setRowHeight(30);
        tabela.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        ModernUI.configureTableHeader(tabela);
        panel.add(new JScrollPane(tabela), BorderLayout.CENTER);
        return panel;
    }

    private JPanel criarPainelGrafico(String titulo) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(new Color(34, 139, 34));
        panel.add(lblTitulo, BorderLayout.NORTH);
        return panel;
    }

    // ===== NOTIFICAÇÕES =====
    private void carregarNotificacoes() {
        painelNotificacoes.removeAll();
        List<Produto> produtos = new ArrayList<>(dao.listar());
        produtos.sort((a, b) -> Integer.compare(
                calcularDiasVencimentoNumero(a.getDataValidade().toString()),
                calcularDiasVencimentoNumero(b.getDataValidade().toString())));

        boolean temAlerta = false;

        for (Produto p : produtos) {
            int dias = calcularDiasVencimentoNumero(p.getDataValidade().toString());
            if (dias >= 0 && dias <= 30) {
                temAlerta = true;
                Color cor = dias <= 7 ? new Color(220, 53, 69) : (dias <= 15 ? new Color(255, 159, 64) : new Color(241, 196, 15));
                String urgencia = dias <= 7 ? "CRÍTICO" : (dias <= 15 ? "URGENTE" : "ATENÇÃO");
                painelNotificacoes.add(criarAlerta("VENCIMENTO PRÓXIMO",
                        "Produto: " + p.getNome() + " | Validade: " + p.getDataValidade() + " | " + dias + " dias restantes",
                        urgencia + " - Produto vence em " + dias + " dias", cor));
                painelNotificacoes.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }

        for (Produto p : dao.listar()) {
            int est = p.getEstoque();
            if (est <= 20) {
                temAlerta = true;
                Color cor = est <= 5 ? new Color(220, 53, 69) : (est <= 10 ? new Color(255, 159, 64) : new Color(241, 196, 15));
                String nivel = est <= 5 ? "CRÍTICO" : (est <= 10 ? "URGENTE" : "ATENÇÃO");
                painelNotificacoes.add(criarAlerta("ESTOQUE BAIXO",
                        "Produto: " + p.getNome() + " | Estoque atual: " + est + " unidades",
                        nivel + " - Recomenda-se reabastecimento urgente", cor));
                painelNotificacoes.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }

        if (!temAlerta) {
            JPanel semAlertas = new JPanel(new FlowLayout(FlowLayout.CENTER));
            semAlertas.setBackground(new Color(240, 255, 240));
            semAlertas.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(46, 204, 113), 1),
                    BorderFactory.createEmptyBorder(20, 20, 20, 20)
            ));
            JLabel lbl = new JLabel("Nenhum alerta no momento. Todos os produtos estão dentro dos parâmetros.");
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lbl.setForeground(new Color(46, 204, 113));
            semAlertas.add(lbl);
            painelNotificacoes.add(semAlertas);
        }

        painelNotificacoes.revalidate();
        painelNotificacoes.repaint();
    }

    private JPanel criarAlerta(String titulo, String mensagem, String detalhe, Color cor) {
        JPanel alerta = new JPanel(new BorderLayout(10, 5));
        alerta.setBackground(Color.WHITE);
        alerta.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(cor, 2),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setForeground(cor);
        JLabel lblMsg = new JLabel(mensagem);
        lblMsg.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JLabel lblDetalhe = new JLabel(detalhe);
        lblDetalhe.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblDetalhe.setForeground(Color.GRAY);
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setOpaque(false);
        textPanel.add(lblMsg, BorderLayout.CENTER);
        textPanel.add(lblDetalhe, BorderLayout.SOUTH);
        alerta.add(lblTitulo, BorderLayout.NORTH);
        alerta.add(textPanel, BorderLayout.CENTER);
        return alerta;
    }

    private JPanel criarLegendaCor(String texto, int valor, Color cor) {
        JPanel legenda = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        legenda.setOpaque(false);
        JPanel corPanel = new JPanel();
        corPanel.setBackground(cor);
        corPanel.setPreferredSize(new Dimension(12, 12));
        JLabel lblTexto = new JLabel(texto + ": " + valor);
        lblTexto.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        legenda.add(corPanel);
        legenda.add(lblTexto);
        return legenda;
    }

    private JPanel criarCard(String titulo, String valor, String descricao, Color cor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setForeground(new Color(55, 77, 65));
        lblTitulo.setIcon(ModernUI.accentIcon(titulo, cor));
        lblTitulo.setIconTextGap(8);
        JLabel lblValor = new JLabel(valor);
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblValor.setForeground(cor);
        JLabel lblDesc = new JLabel(descricao);
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDesc.setForeground(new Color(104, 121, 111));
        card.add(lblTitulo, BorderLayout.NORTH);
        card.add(lblValor,  BorderLayout.CENTER);
        card.add(lblDesc,   BorderLayout.SOUTH);
        return card;
    }

    // ===== GRÁFICO DE BARRAS DE IDADE =====
    static class IdadeBarraPanel extends JPanel {
        private int jovens, adultos, idosos, maxValor;
        public IdadeBarraPanel(int jovens, int adultos, int idosos) {
            this.jovens = jovens; this.adultos = adultos; this.idosos = idosos;
            this.maxValor = Math.max(jovens, Math.max(adultos, idosos));
            if (maxValor == 0) maxValor = 1;
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int width = getWidth() - 60, startX = 50, startY = getHeight() - 40;
            int barWidth = (width - 40) / 3;
            String[] categorias = {"18-30", "31-59", "60+"};
            int[] valores = {jovens, adultos, idosos};
            Color[] cores = {new Color(46,204,113), new Color(52,152,219), new Color(155,89,182)};
            for (int i = 0; i < 3; i++) {
                int barHeight = (valores[i] * (startY - 50)) / maxValor;
                int x = startX + (i * (barWidth + 10)), y = startY - barHeight;
                g2d.setColor(cores[i]);
                g2d.fillRoundRect(x, y, barWidth, barHeight, 8, 8);
                g2d.setColor(Color.BLACK);
                g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                String vs = String.valueOf(valores[i]);
                g2d.drawString(vs, x + (barWidth - g2d.getFontMetrics().stringWidth(vs))/2, y - 5);
                g2d.drawString(categorias[i], x + (barWidth - g2d.getFontMetrics().stringWidth(categorias[i]))/2, startY + 15);
            }
            g2d.setColor(Color.BLACK);
            g2d.drawLine(startX-5, startY, startX-5, 40);
            g2d.drawLine(startX-5, startY, startX+width, startY);
            g2d.drawString("Quantidade", startX-30, startY/2);
        }
    }
}
