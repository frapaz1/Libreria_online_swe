package main;

import javax.swing.SwingUtilities;
import view.CatalogoLibriView;

public class MainApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CatalogoLibriView frame = new CatalogoLibriView();
            frame.setVisible(true);
        });
    }
}