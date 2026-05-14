package domain;

public class Utente {
    private int id;
    private String username;
    private String password;
    private String ruolo;
    private ProfiloUtente profilo; // Relazione 1 a 1

    public Utente() {}

    public Utente(int id, String username, String password, String ruolo) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.ruolo = ruolo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRuolo() { return ruolo; }
    public void setRuolo(String ruolo) { this.ruolo = ruolo; }

    public ProfiloUtente getProfilo() { return profilo; }
    public void setProfilo(ProfiloUtente profilo) { this.profilo = profilo; }
}