package prontuario.drnubia.app;

import javax.swing.SwingUtilities;
import prontuario.drnubia.gui.TelaPrincipal;

public class Application {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new TelaPrincipal();
        });
    }
}