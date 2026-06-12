package view;

import controller.LoginController;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

public class LoginView extends JPanel {

    private JTextField txtLogin;
    private JPasswordField txtSenha;
    private JButton btnEntrar;
    private JButton btnMostrarSenha;
    private JButton btnRecuperarSenha;
    private LoginController controller;
    private boolean senhaVisivel = false;

    public LoginView() {
        controller = new LoginController(this);
        setLayout(new GridLayout(1, 2));
        iniciarComponentes();
    }

    private void iniciarComponentes() {
        // ===== LADO ESQUERDO (BRANCO) =====
        JPanel painelEsquerdo = new JPanel();
        painelEsquerdo.setBackground(Color.WHITE);
        painelEsquerdo.setLayout(new BoxLayout(painelEsquerdo, BoxLayout.Y_AXIS));
        painelEsquerdo.setBorder(new EmptyBorder(60, 80, 60, 80));

        painelEsquerdo.add(Box.createVerticalGlue());

        try {
            java.net.URL imgURL = getClass().getResource("/img/logo.jpeg");
            ImageIcon iconLogo = imgURL != null ? new ImageIcon(imgURL) : new ImageIcon("src/img/logo.jpeg");
            if (iconLogo.getIconWidth() > 0) {
                // Redimensionamento de alta qualidade com Graphics2D
                int tamanho = 280;
                BufferedImage original = new BufferedImage(
                        iconLogo.getIconWidth(), iconLogo.getIconHeight(),
                        BufferedImage.TYPE_INT_ARGB
                );
                Graphics2D g2dOriginal = original.createGraphics();
                g2dOriginal.drawImage(iconLogo.getImage(), 0, 0, null);
                g2dOriginal.dispose();

                BufferedImage redimensionada = new BufferedImage(tamanho, tamanho, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = redimensionada.createGraphics();
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.drawImage(original, 0, 0, tamanho, tamanho, null);
                g2d.dispose();

                JLabel lblLogo = new JLabel(new ImageIcon(redimensionada));
                lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
                painelEsquerdo.add(lblLogo);
                painelEsquerdo.add(Box.createRigidArea(new Dimension(0, 20)));
            } else {
                adicionarLogoTexto(painelEsquerdo);
            }
        } catch (Exception e) {
            adicionarLogoTexto(painelEsquerdo);
        }

        JLabel lblTituloEmpresa = new JLabel("AMERICO PLANTAS MEDICINAIS");
        lblTituloEmpresa.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTituloEmpresa.setForeground(new Color(34, 139, 34));
        lblTituloEmpresa.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSistema = new JLabel("Sistema de Gestão");
        lblSistema.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        lblSistema.setForeground(new Color(100, 100, 100));
        lblSistema.setAlignmentX(Component.CENTER_ALIGNMENT);

        painelEsquerdo.add(lblTituloEmpresa);
        painelEsquerdo.add(Box.createRigidArea(new Dimension(0, 10)));
        painelEsquerdo.add(lblSistema);
        painelEsquerdo.add(Box.createVerticalGlue());

        // ===== LADO DIREITO (VERDE) =====
        JPanel painelDireito = new JPanel(new GridBagLayout());
        painelDireito.setBackground(new Color(34, 139, 34));

        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(600, 550));
        card.setBackground(Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                new EmptyBorder(50, 50, 50, 50)
        ));

        JLabel lblBemVindo = new JLabel("Bem-vindo");
        lblBemVindo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblBemVindo.setForeground(new Color(34, 139, 34));
        lblBemVindo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTexto = new JLabel("Faça login para acessar o sistema");
        lblTexto.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblTexto.setForeground(Color.GRAY);
        lblTexto.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel painelCampos = new JPanel(new GridBagLayout());
        painelCampos.setOpaque(false);
        painelCampos.setMaximumSize(new Dimension(550, 300));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.25;
        JLabel lblLoginLabel = new JLabel("LOGIN");
        lblLoginLabel.setIcon(ModernUI.fieldIcon("user"));
        lblLoginLabel.setIconTextGap(8);
        lblLoginLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblLoginLabel.setForeground(new Color(50, 50, 50));
        painelCampos.add(lblLoginLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 0.75;
        txtLogin = new JTextField();
        txtLogin.setPreferredSize(new Dimension(400, 55));
        txtLogin.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        txtLogin.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(12, 18, 12, 18)
        ));
        painelCampos.add(txtLogin, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.25;
        JLabel lblSenhaLabel = new JLabel("SENHA");
        lblSenhaLabel.setIcon(ModernUI.fieldIcon("lock"));
        lblSenhaLabel.setIconTextGap(8);
        lblSenhaLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblSenhaLabel.setForeground(new Color(50, 50, 50));
        painelCampos.add(lblSenhaLabel, gbc);

        gbc.gridx = 1; gbc.weightx = 0.75;
        JPanel painelSenha = new JPanel(new BorderLayout(10, 0));
        painelSenha.setOpaque(false);

        txtSenha = new JPasswordField();
        txtSenha.setPreferredSize(new Dimension(350, 55));
        txtSenha.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        txtSenha.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                BorderFactory.createEmptyBorder(12, 18, 12, 18)
        ));

        btnMostrarSenha = new JButton("Mostrar");
        btnMostrarSenha.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnMostrarSenha.setFocusPainted(false);
        btnMostrarSenha.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnMostrarSenha.setPreferredSize(new Dimension(90, 55));
        btnMostrarSenha.setBackground(new Color(245, 245, 245));
        btnMostrarSenha.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));
        btnMostrarSenha.addActionListener(e ->
                senhaVisivel = controller.toggleSenhaVisivel(txtSenha, btnMostrarSenha, senhaVisivel)
        );

        painelSenha.add(txtSenha, BorderLayout.CENTER);
        painelSenha.add(btnMostrarSenha, BorderLayout.EAST);
        painelCampos.add(painelSenha, gbc);


        btnRecuperarSenha = new JButton("Esqueci minha senha");
        btnRecuperarSenha.setBackground(new Color(34, 139, 34));
        btnRecuperarSenha.setForeground(Color.WHITE);
        btnRecuperarSenha.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnRecuperarSenha.setPreferredSize(new Dimension(500, 65));
        btnRecuperarSenha.setMaximumSize(new Dimension(500, 65));
        btnRecuperarSenha.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRecuperarSenha.setIcon(ModernUI.fieldIcon("lock"));
        btnRecuperarSenha.setFocusPainted(false);
        btnRecuperarSenha.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRecuperarSenha.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        btnRecuperarSenha.addActionListener(e -> controller.recuperarSenhaComDialogo(
                (JFrame) SwingUtilities.getWindowAncestor(this)
        ));

        btnEntrar = new JButton("ENTRAR");
        btnEntrar.setPreferredSize(new Dimension(500, 65));
        btnEntrar.setMaximumSize(new Dimension(500, 65));
        btnEntrar.setBackground(new Color(34, 139, 34));
        btnEntrar.setForeground(Color.WHITE);
        btnEntrar.setFont(new Font("Segoe UI", Font.BOLD, 20));
        btnEntrar.setFocusPainted(false);
        btnEntrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEntrar.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtLogin.addActionListener(e -> txtSenha.requestFocus());
        txtSenha.addActionListener(e -> controller.realizarLogin(this));
        btnEntrar.addActionListener(e -> controller.realizarLogin(this));

        card.add(lblBemVindo);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(lblTexto);
        card.add(Box.createRigidArea(new Dimension(0, 45)));
        card.add(painelCampos);
        card.add(Box.createRigidArea(new Dimension(0, 40)));
        card.add(btnRecuperarSenha);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(btnEntrar);

        painelDireito.add(card);

        add(painelEsquerdo);
        add(painelDireito);
    }

    private void adicionarLogoTexto(JPanel painel) {
        JLabel lblLogoTemp = new JLabel("AMERICO");
        lblLogoTemp.setFont(new Font("Segoe UI", Font.BOLD, 52));
        lblLogoTemp.setForeground(new Color(34, 139, 34));
        lblLogoTemp.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(lblLogoTemp);
        painel.add(Box.createRigidArea(new Dimension(0, 10)));
    }

    public void exibirMensagem(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    public JTextField getTxtLogin() { return txtLogin; }
    public JPasswordField getTxtSenha() { return txtSenha; }
    public JButton getBtnMostrarSenha() { return btnMostrarSenha; }
}