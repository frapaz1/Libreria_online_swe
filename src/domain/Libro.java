package domain;

public class Libro {
    private int id;
    private String isbn; 
    private String titolo;
    private String autore;
    private String editore; 
    private double prezzo;
    private int quantitaMagazzino;

    public Libro(int id, String isbn, String titolo, String autore, String editore, double prezzo, int quantitaMagazzino) {
        this.id = id;
        this.isbn = isbn;
        this.titolo = titolo;
        this.autore = autore;
        this.editore = editore;
        this.prezzo = prezzo;
        this.quantitaMagazzino = quantitaMagazzino;
    }

    // Getter e Setter
    public int getId() { return id; }
    public String getIsbn() { return isbn; }
    public String getTitolo() { return titolo; }
    public String getAutore() { return autore; }
    public String getEditore() { return editore; }
    public double getPrezzo() { return prezzo; }
    public int getQuantitaMagazzino() { return quantitaMagazzino; }

    public void setQuantitaMagazzino(int quantitaMagazzino) {
        this.quantitaMagazzino = quantitaMagazzino;
    }
}