package domain;

public class ProfiloUtente {
    private int utenteId; 
    private String nome;
    private String cognome;
    private String indirizzo;

    // Costruttore vuoto
    public ProfiloUtente() {}

    // Costruttore con parametri
    public ProfiloUtente(int utenteId, String nome, String cognome, String indirizzo) {
        this.utenteId = utenteId;
        this.nome = nome;
        this.cognome = cognome;
        this.indirizzo = indirizzo;
    }

    // Getters e Setters
    public int getUtenteId() { return utenteId; }
    public void setUtenteId(int utenteId) { this.utenteId = utenteId; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCognome() { return cognome; }
    public void setCognome(String cognome) { this.cognome = cognome; }

    public String getIndirizzo() { return indirizzo; }
    public void setIndirizzo(String indirizzo) { this.indirizzo = indirizzo; }
}