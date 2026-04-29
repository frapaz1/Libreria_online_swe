package main;

import javax.swing.SwingUtilities;

public class MainTest {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new view.HomeView().setVisible(true);
        });
    }
}