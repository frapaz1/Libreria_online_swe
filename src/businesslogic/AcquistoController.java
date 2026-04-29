package businesslogic;

import domain.Libro;
import orm.LibroDAO;
import java.sql.Connection;

public class AcquistoController {
    private LibroDAO libroDAO; 

    public AcquistoController(Connection connection) {
        // Inizializza il DAO passandogli la connessione
        this.libroDAO = new LibroDAO(connection);
    }

    // Metodo che implementa il Basic Flow e l'Alternative Flow 
    public boolean aggiungiAlCarrello(int idLibro, int quantitaRichiesta) {
        Libro libro = libroDAO.getLibroById(idLibro); 
        
        if (libro == null) {
            System.out.println("Errore: Libro non trovato.");
            return false;
        }
        
        // Verifichiamo la disponibilità
        if (libro.getQuantitaMagazzino() >= quantitaRichiesta) {
            // Calcoliamo la nuova giacenza
            int nuovaGiacenza = libro.getQuantitaMagazzino() - quantitaRichiesta;
            
            // Il DAO salva la nuova giacenza sul Database!
            boolean aggiornato = libroDAO.updateQuantita(idLibro, nuovaGiacenza);
            
            if(aggiornato) {
                System.out.println("Acquisto completato! Nuova giacenza: " + nuovaGiacenza);
                return true; // Successo 
            } else {
                return false; // Errore nel salvataggio DB
            }
        } else {
            System.out.println("Errore: Quantità richiesta superiore alla giacenza.");
            return false; // Fallimento 
        }
    }

    public boolean rimuoviDalCarrello(Libro libro) {
        // Recuperiamo la giacenza attuale dal DB
        Libro libroDB = libroDAO.getLibroById(libro.getId());
        if (libroDB != null) {
            // Restituiamo una copia al magazzino
            int nuovaGiacenza = libroDB.getQuantitaMagazzino() + 1;
            return libroDAO.updateQuantita(libro.getId(), nuovaGiacenza);
        }
        return false;
    }

    // Metodo per il calcolo del totale utilizzando il Pattern Strategy
    public double calcolaTotaleOrdine(double totaleLordo, ScontoStrategy strategia) {
        // Se non viene passata una strategia, usiamo di default "Sconto Nullo"
        if (strategia == null) {
            strategia = new ScontoNullo();
        }
        
        double totaleFinale = strategia.applicaSconto(totaleLordo);
        System.out.println("Calcolo Totale: Lordo " + totaleLordo + "€ -> Scontato " + totaleFinale + "€");
        return totaleFinale;
    }
}