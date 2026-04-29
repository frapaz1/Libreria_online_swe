package view;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import domain.Libro;
import businesslogic.AcquistoController;
import businesslogic.*; 

public class CarrelloView extends JDialog {
    private DefaultListModel<String> listModel;
    private JList<String> displayList;
    private List<Libro> carrello;
    private AcquistoController controller;
    private Runnable onUpdate; // Per aggiornare la Home quando chiudiamo
    private JCheckBox checkStudente; // Checkbox per attivare lo sconto

    public CarrelloView(JFrame parent, List<Libro> carrello, AcquistoController controller, Runnable onUpdate) {
        super(parent, "Il Tuo Carrello", true);
        this.carrello = carrello;
        this.controller = controller;
        this.onUpdate = onUpdate;

        setSize(400, 550);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));

        // Lista grafica
        listModel = new DefaultListModel<>();
        displayList = new JList<>(listModel);
        
        add(new JScrollPane(displayList), BorderLayout.CENTER);

        // Selezione dello sconto (Pattern Strategy)
        JPanel panelSconto = new JPanel();
        checkStudente = new JCheckBox("Applica Sconto Studente (10%)");
        checkStudente.addActionListener(e -> aggiornaListaUI()); // Ricalcola quando cambia la selezione
        panelSconto.add(checkStudente);
        add(panelSconto, BorderLayout.NORTH);

        // Pannello Azioni
        JPanel panelAzioni = new JPanel(new GridLayout(2, 1, 5, 5));
        
        JButton btnRimuovi = new JButton("Rimuovi Elemento Selezionato");
        btnRimuovi.addActionListener(e -> rimuoviElemento());
        
        JButton btnChiudi = new JButton("Torna allo Shopping");
        btnChiudi.addActionListener(e -> dispose());

        panelAzioni.add(btnRimuovi);
        panelAzioni.add(btnChiudi);
        add(panelAzioni, BorderLayout.SOUTH);

        // Inizializzazione della UI
        aggiornaListaUI();
    }

    private void aggiornaListaUI() {
        listModel.clear();
        double totaleLordo = 0;
        for (Libro l : carrello) {
            listModel.addElement(l.getTitolo() + " - €" + String.format("%.2f", l.getPrezzo()));
            totaleLordo += l.getPrezzo();
        }

        // Determiniamo la strategia di sconto
        ScontoStrategy strategia;
        if (checkStudente.isSelected()) {
            strategia = new ScontoFedelta();
        } else {
            strategia = new ScontoNullo();
        }

        // Il controller calcola il totale utilizzando la strategia scelta
        double totaleScontato = controller.calcolaTotaleOrdine(totaleLordo, strategia);

        setTitle("Carrello - Totale: €" + String.format("%.2f", totaleScontato));
    }

    private void rimuoviElemento() {
        int index = displayList.getSelectedIndex();
        if (index != -1) {
            Libro libroDaRimuovere = carrello.get(index);
            
            // Logica di Business: restituisci al DB
            if (controller.rimuoviDalCarrello(libroDaRimuovere)) {
                // Rimuovi dalla lista locale
                carrello.remove(index);
                aggiornaListaUI();
                onUpdate.run(); // Notifica la Home di ricaricare le giacenze
                JOptionPane.showMessageDialog(this, "Libro rimosso e tornato disponibile in magazzino.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleziona un libro da rimuovere.");
        }
    }
}