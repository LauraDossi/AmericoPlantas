import view.AppFrame;

public class Main {

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            AppFrame.getInstancia();
        });
    }
}