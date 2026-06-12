package view;

import javax.swing.*;
import java.awt.*;

public class AppFrame extends JFrame {


    public static final String LOGIN = "login";
    public static final String MENU = "menu";
    public static final String CLIENTES = "clientes";
    public static final String PRODUTOS = "produtos";
    public static final String VENDAS = "vendas";
    public static final String RELATORIOS = "relatorios";
    public static final String FORNECEDOR = "fornecedor";
    public static final String USUARIOS = "usuarios";

    private static AppFrame instancia;

    public static AppFrame getInstancia() {
        if (instancia == null) {
            instancia = new AppFrame();
        }
        return instancia;
    }

    private final CardLayout cardLayout = new CardLayout();
    private final JPanel container  = new JPanel(cardLayout);
    private String telaAtual  = null;

    private AppFrame() {
        ModernUI.install();
        setTitle("Americo Plantas Medicinais");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1300, 750));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        container.setBackground(new Color(245, 245, 245));
        add(container);

        JPanel login = new LoginView();
        login.setName(LOGIN);
        container.add(login, LOGIN);
        ModernUI.styleTree(login);

        cardLayout.show(container, LOGIN);
        telaAtual = LOGIN;

        setVisible(true);
    }

    public void mostrar(String tela) {
        switch (tela) {
            case MENU -> registrar(MENU, new MenuPrincipalView());
            case VENDAS  -> registrar(VENDAS, new VendaView());
            case RELATORIOS -> registrar(RELATORIOS, new RelatorioView());
            case CLIENTES -> { if (!existe(CLIENTES)) registrar(CLIENTES, new ClienteView()); }
            case PRODUTOS -> { if (!existe(PRODUTOS)) registrar(PRODUTOS, new ProdutoView()); }
            case FORNECEDOR -> { if (!existe(FORNECEDOR)) registrar(FORNECEDOR, new FornecedorView()); }
            case USUARIOS -> registrar(USUARIOS, new UsuarioView());
            default -> {} // LOGIN já registrado
        }

        cardLayout.show(container, tela);
        telaAtual = tela;
        container.revalidate();
        container.repaint();
    }

    private boolean existe(String nome) {
        for (Component c : container.getComponents()) {
            if (nome.equals(c.getName())) return true;
        }
        return false;
    }

    private void registrar(String nome, JPanel painel) {
        // Remove o card antigo se existir
        for (Component c : container.getComponents()) {
            if (nome.equals(c.getName())) {
                container.remove(c);
                break;
            }
        }
        painel.setName(nome);
        container.add(painel, nome);
        ModernUI.styleTree(painel);
    }
}
