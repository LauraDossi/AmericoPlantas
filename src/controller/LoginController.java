package controller;

import model.ServicoDeEmail;
import model.Usuario;
import javax.swing.*;
import java.awt.*;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import model.UsuarioDAO;
import view.AppFrame;
import view.LoginView;
import view.Navegador;

public class LoginController {

    private static List<Usuario> usuarios = new ArrayList<>();
    private static Usuario usuarioLogado = null;
    private LoginView view;
    private UsuarioDAO dao;

    private static Timer sessaoTimer;
    private static final int TIMEOUT_MINUTOS = 30;
    private static Component componenteRaiz;

    private static final String BACKUP_DIR = "backups";
    private static final int MAX_BACKUPS = 7;
    private static final File DATA_ULTIMO_BACKUP = new File(BACKUP_DIR, ".lastbackup");
    private static final String DB_NAME = "americoplantas";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Kw$3D6U5";

    private static final String[] POSSIVEIS_MYSQLDUMP = {
            "mysqldump",
            "C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysqldump.exe",
            "C:\\Program Files\\MySQL\\MySQL Server 5.7\\bin\\mysqldump.exe",
            "C:\\Program Files (x86)\\MySQL\\MySQL Server 5.7\\bin\\mysqldump.exe",
            "C:\\xampp\\mysql\\bin\\mysqldump.exe",
            "/usr/bin/mysqldump",
            "/usr/local/mysql/bin/mysqldump"
    };
    private static String caminhoMysqldump = null;

    public LoginController(LoginView view) {
        this.view = view;
        dao = new UsuarioDAO();
        localizarMysqldump();
    }

    private static void localizarMysqldump() {
        if (caminhoMysqldump != null) return;
        for (String path : POSSIVEIS_MYSQLDUMP) {
            try {
                Process p = Runtime.getRuntime().exec(path + " --version");
                if (p.waitFor() == 0) {
                    caminhoMysqldump = path;
                    System.out.println("mysqldump encontrado em: " + path);
                    return;
                }
            } catch (Exception ignored) {}
        }
        System.err.println("mysqldump não encontrado. Backup automático desabilitado.");
    }

    public void realizarLogin(LoginView view) {
        String login = view.getTxtLogin().getText().trim();
        String senha = new String(view.getTxtSenha().getPassword());

        if (login.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Digite o login!", "Atenção", JOptionPane.WARNING_MESSAGE);
            view.getTxtLogin().requestFocus();
            return;
        }
        if (senha.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Digite a senha!", "Atenção", JOptionPane.WARNING_MESSAGE);
            view.getTxtSenha().requestFocus();
            return;
        }

        if (autenticar()) {
            Navegador.irPara(AppFrame.MENU);
        }
    }

    public boolean toggleSenhaVisivel(JPasswordField txtSenha, JButton btnMostrarSenha, boolean senhaVisivel) {
        senhaVisivel = !senhaVisivel;
        if (senhaVisivel) {
            txtSenha.setEchoChar((char) 0);
            btnMostrarSenha.setText("Ocultar");
        } else {
            txtSenha.setEchoChar('*');
            btnMostrarSenha.setText("Mostrar");
        }
        return senhaVisivel;
    }

    public boolean autenticar() {
        String login = view.getTxtLogin().getText();
        String senha = new String(view.getTxtSenha().getPassword());

        try {
            Usuario u = dao.autenticar(login, senha);
            if (u != null) {
                view.exibirMensagem("Login realizado com sucesso!");
                usuarioLogado = u;
                iniciarSessao();
                if (isAdministrador()) {
                    verificarEExecutarBackup();
                }
                return true;
            } else {
                view.exibirMensagem("Usuário ou senha inválidos.");
                return false;
            }
        } catch (RuntimeException e) {
            view.exibirMensagem(e.getMessage());
            return false;
        }
    }

    public static void iniciarSessao() {
        if (componenteRaiz == null) {
            componenteRaiz = AppFrame.getInstancia();
        }
        resetarTimerSessao();

        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            @Override
            public void eventDispatched(AWTEvent event) {
                if (event instanceof MouseEvent || event instanceof KeyEvent) {
                    resetarTimerSessao();
                }
            }
        }, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.KEY_EVENT_MASK);
    }

    private static void resetarTimerSessao() {
        if (sessaoTimer != null) sessaoTimer.stop();
        sessaoTimer = new Timer(TIMEOUT_MINUTOS * 60 * 1000, e -> {
            if (componenteRaiz != null) {
                JOptionPane.showMessageDialog(componenteRaiz, "Sessão expirada por inatividade.");
            }
            setUsuarioLogado(null);
            AppFrame.getInstancia().mostrar(AppFrame.LOGIN);
        });
        sessaoTimer.setRepeats(false);
        sessaoTimer.start();
    }

    public static void pararSessao() {
        if (sessaoTimer != null) sessaoTimer.stop();
    }

    private static void verificarEExecutarBackup() {

        if (caminhoMysqldump == null) {
            JOptionPane.showMessageDialog(null,
                    "Backup automático desabilitado: mysqldump não encontrado.\n" +
                            "Verifique se o MySQL Client está instalado e o caminho está correto.",
                    "Backup", JOptionPane.WARNING_MESSAGE);
            return;
        }

        new File(BACKUP_DIR).mkdirs();

        LocalDate ultimoBackup = null;
        if (DATA_ULTIMO_BACKUP.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(DATA_ULTIMO_BACKUP))) {
                String line = br.readLine();
                if (line != null) ultimoBackup = LocalDate.parse(line);
            } catch (IOException ignored) {}
        }

        LocalDate hoje = LocalDate.now();
        if (ultimoBackup == null || !ultimoBackup.equals(hoje)) {
            new Thread(() -> {
                try {
                    realizarBackup();
                    try (FileWriter fw = new FileWriter(DATA_ULTIMO_BACKUP)) {
                        fw.write(hoje.toString());
                    }
                    JOptionPane.showMessageDialog(null,
                            "Backup automático realizado com sucesso!\nPasta: " + BACKUP_DIR,
                            "Backup", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null,
                            "Erro no backup automático: " + e.getMessage(),
                            "Backup", JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private static void realizarBackup() throws IOException, InterruptedException {
        String nomeArquivo = "backup_" + LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + ".sql";
        String caminhoCompleto = BACKUP_DIR + File.separator + nomeArquivo;

        String cmd = "\"" + caminhoMysqldump + "\" -h localhost -P 3306 -u " + DB_USER + " -p" + DB_PASSWORD + " " + DB_NAME +
                " --routines --triggers --single-transaction";

        Process process = Runtime.getRuntime().exec(cmd);
        try (InputStream is = process.getInputStream();
             FileOutputStream fos = new FileOutputStream(caminhoCompleto)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {

            try (InputStream es = process.getErrorStream();
                 BufferedReader br = new BufferedReader(new InputStreamReader(es))) {
                String line;
                StringBuilder sb = new StringBuilder();
                while ((line = br.readLine()) != null) sb.append(line).append("\n");
                throw new IOException("mysqldump falhou (código " + exitCode + "): " + sb);
            }
        }


        File[] backups = new File(BACKUP_DIR).listFiles((dir, name) -> name.endsWith(".sql"));
        if (backups != null && backups.length > MAX_BACKUPS) {
            Arrays.sort(backups, Comparator.comparingLong(File::lastModified).reversed());
            for (int i = MAX_BACKUPS; i < backups.length; i++) {
                backups[i].delete();
            }
        }
    }

    public void recuperarSenhaComDialogo(JFrame parent) {
        String login = JOptionPane.showInputDialog(parent, "Digite seu login:", "Recuperar senha", JOptionPane.QUESTION_MESSAGE);
        if (login == null) return;
        if (login.trim().isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Digite um login válido.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String email = dao.buscarEmailPorLogin(login.trim());
        if (email == null || email.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Login não encontrado.", "Atenção", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String senhaTemporaria = gerarSenhaAleatoria();
        ServicoDeEmail servicoDeEmail = new ServicoDeEmail();
        boolean enviado = servicoDeEmail.enviarNovaSenha(email, senhaTemporaria);
        if (!enviado) {
            JOptionPane.showMessageDialog(parent, "Erro ao enviar email.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        dao.alterarSenhaPorEmail(email, senhaTemporaria);
        JOptionPane.showMessageDialog(parent, "Uma senha temporária foi enviada para o email vinculado à conta.", "Senha enviada", JOptionPane.INFORMATION_MESSAGE);
    }

    private String gerarSenhaAleatoria() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            int idx = (int) (Math.random() * caracteres.length());
            sb.append(caracteres.charAt(idx));
        }
        return sb.toString();
    }

    public static Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public static void setUsuarioLogado(Usuario u) {
        usuarioLogado = u;
        if (u == null) pararSessao();
    }

    public static boolean isAdministrador() {
        return usuarioLogado != null && "Administrador".equalsIgnoreCase(usuarioLogado.getTipoUsuario());
    }

    public static boolean isOperador() {
        return usuarioLogado != null && usuarioLogado.getTipoUsuario().equalsIgnoreCase("Operador");
    }

    public static List<Usuario> getUsuarios() {
        return usuarios;
    }

    public static void adicionarUsuario(Usuario usuario) {
        usuarios.add(usuario);
    }
}