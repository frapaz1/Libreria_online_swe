package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import domain.Ordine;
import businesslogic.AcquistoController;

public class OrdiniView extends JDialog {
    
    public OrdiniView(JFrame parent, AcquistoController controller, int utenteId) {
        super(parent, "I Miei Ordini", true);
        setSize(700, 400);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        // Intestazioni della tabella
        String[] colonne = {"ID Ordine", "Data", "Totale", "Stato", "Tracking Spedizione"};
        DefaultTableModel tableModel = new DefaultTableModel(colonne, 0);
        JTable tabellaOrdini = new JTable(tableModel);

        // Recupero dati dal controller
        List<Ordine> ordini = controller.getStoricoOrdini(utenteId);

        for (Ordine o : ordini) {
            String tracking = (o.getSpedizione() != null) ? o.getSpedizione().getTrackingNumber() : "N/D";
            Object[] riga = {
                o.getId(),
                o.getDataOrdine(),
                String.format("%.2f€", o.getTotale()),
                o.getStato(),
                tracking
            };
            tableModel.addRow(riga);
        }

        add(new JScrollPane(tabellaOrdini), BorderLayout.CENTER);

        JButton btnChiudi = new JButton("Chiudi");
        btnChiudi.addActionListener(e -> dispose());
        JPanel pnlBottoni = new JPanel();
        pnlBottoni.add(btnChiudi);
        add(pnlBottoni, BorderLayout.SOUTH);
    }
}