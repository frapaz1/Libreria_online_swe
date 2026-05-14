package domain;

public class Spedizione {
    private int id;
    private int ordineId; // Riferimento all'ordine spedito
    private String corriere;
    private String trackingNumber;
    private String indirizzoConsegna;

    public Spedizione() {}

    public Spedizione(int id, int ordineId, String corriere, String trackingNumber, String indirizzoConsegna) {
        this.id = id;
        this.ordineId = ordineId;
        this.corriere = corriere;
        this.trackingNumber = trackingNumber;
        this.indirizzoConsegna = indirizzoConsegna;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getOrdineId() { return ordineId; }
    public void setOrdineId(int ordineId) { this.ordineId = ordineId; }

    public String getCorriere() { return corriere; }
    public void setCorriere(String corriere) { this.corriere = corriere; }

    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }

    public String getIndirizzoConsegna() { return indirizzoConsegna; }
    public void setIndirizzoConsegna(String indirizzoConsegna) { this.indirizzoConsegna = indirizzoConsegna; }
}