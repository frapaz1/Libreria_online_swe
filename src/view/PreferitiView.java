package view;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import domain.Libro;
import businesslogic.AcquistoController;

public class PreferitiView extends JDialog {
    private DefaultListModel<String> listModel;
    private JList<String> displayList;
    private List<Libro> preferiti;
    private AcquistoController controller;

    public PreferitiView(JFrame parent, AcquistoController controller) {
        super(parent, "La tua Wishlist", true);
        this.controller = controller;

        setSize(400, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        listModel = new DefaultListModel<>();
        displayList = new JList<>(listModel);
        add(new JScrollPane(displayList), BorderLayout.CENTER);

        JPanel panelAzioni = new JPanel(new GridLayout(2, 1, 5, 5));
        
        JButton btnRimuovi = new JButton("Rimuovi dai Preferiti");
        btnRimuovi.addActionListener(e -> rimuoviElemento());
        
        JButton btnChiudi = new JButton("Chiudi Wishlist");
        btnChiudi.addActionListener(e -> dispose());

        panelAzioni.add(btnRimuovi);
        panelAzioni.add(btnChiudi);
        add(panelAzioni, BorderLayout.SOUTH);

        aggiornaLista();
    }

    private void aggiornaLista() {
        listModel.clear();
        // Chiediamo al controller la lista aggiornata dal DB
        preferiti = controller.getPreferiti();
        for (Libro l : preferiti) {
            listModel.addElement(l.getTitolo() + " - di " + l.getAutore());
        }
    }

    private void rimuoviElemento() {
        int index = displayList.getSelectedIndex();
        if (index != -1) {
            Libro libroDaRimuovere = preferiti.get(index);
            if (controller.rimuoviDaPreferiti(libroDaRimuovere.getId())) {
                aggiornaLista();
                JOptionPane.showMessageDialog(this, "Libro rimosso dai preferiti.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleziona un libro da rimuovere.");
        }
    }
}