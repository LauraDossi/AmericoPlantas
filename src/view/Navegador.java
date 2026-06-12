package view;

import controller.LoginController;
import javax.swing.JOptionPane;

public final class Navegador {

    private Navegador() {}

    public static void irPara(String tela) {
        AppFrame.getInstancia().mostrar(tela);
    }

    public static void irParaUsuarios() {
        if (LoginController.isAdministrador()) {
            irPara(AppFrame.USUARIOS);
        } else {
            JOptionPane.showMessageDialog(
                AppFrame.getInstancia(),
                "Acesso negado! Apenas administradores podem acessar Usuários."
            );
        }
    }
}
