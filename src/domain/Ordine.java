package domain;

import businesslogic.ScontoStrategy;
import java.util.Date;

public class Ordine {
    private int id;
    private Date dataOrdine;
    private int utenteId;
    private double totale;
    private String stato;
    private Spedizione spedizione; // Relazione 1 a 1

    public Ordine() {
        this.dataOrdine = new Date();
        this.stato = "Creato";
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Date getDataOrdine() { return dataOrdine; }
    public void setDataOrdine(Date dataOrdine) { this.dataOrdine = dataOrdine; }

    public int getUtenteId() { return utenteId; }
    public void setUtenteId(int utenteId) { this.utenteId = utenteId; }

    public double getTotale() { return totale; }
    public void setTotale(double totale) { this.totale = totale; }

    public String getStato() { return stato; }
    public void setStato(String stato) { this.stato = stato; }

    public Spedizione getSpedizione() { return spedizione; }
    public void setSpedizione(Spedizione spedizione) { this.spedizione = spedizione; }

    // Logica di business interna al Modello
    public void applicaSconto(ScontoStrategy strategiaPromo) {
        if (strategiaPromo != null) {
            this.totale = strategiaPromo.applicaSconto(this.totale);
        }
    }
}