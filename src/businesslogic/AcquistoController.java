package businesslogic;

import domain.Libro;
import domain.Ordine;
import domain.Utente;
import domain.Spedizione;
import orm.LibroDAO;
import orm.OrdineDAO; 
import java.sql.Connection;
import java.util.List;

public class AcquistoController {
    private LibroDAO libroDAO; 
    private OrdineDAO ordineDAO; 

    public AcquistoController(Connection connection) {
        // Inizializza i DAO passandogli la connessione
        this.libroDAO = new LibroDAO(connection);
        this.ordineDAO = new OrdineDAO(connection); 
    }

    // --- GESTIONE CARRELLO ---

    public boolean aggiungiAlCarrello(int idLibro, int quantitaRichiesta) {
        Libro libro = libroDAO.getLibroById(idLibro); 
        
        if (libro == null) {
            System.out.println("Errore: Libro non trovato.");
            return false;
        }
        
        // Verifichiamo la disponibilità in magazzino
        if (libro.getQuantitaMagazzino() >= quantitaRichiesta) {
            int nuovaGiacenza = libro.getQuantitaMagazzino() - quantitaRichiesta;
            
            // Il DAO aggiorna la giacenza sul Database
            boolean aggiornato = libroDAO.updateQuantita(idLibro, nuovaGiacenza);
            
            if(aggiornato) {
                System.out.println("Aggiunta riuscita! Nuova giacenza: " + nuovaGiacenza);
                return true; 
            } else {
                return false; 
            }
        } else {
            System.out.println("Errore: Quantità richiesta superiore alla giacenza.");
            return false; 
        }
    }

    public boolean rimuoviDalCarrello(Libro libro) {
        Libro libroDB = libroDAO.getLibroById(libro.getId());
        if (libroDB != null) {
            // Restituiamo il libro al magazzino
            int nuovaGiacenza = libroDB.getQuantitaMagazzino() + 1;
            return libroDAO.updateQuantita(libro.getId(), nuovaGiacenza);
        }
        return false;
    }

    // Metodo per il calcolo del totale utilizzando il Pattern Strategy
    public double calcolaTotaleOrdine(double totaleLordo, ScontoStrategy strategia) {
        if (strategia == null) {
            strategia = new ScontoNullo();
        }
        
        double totaleFinale = strategia.applicaSconto(totaleLordo);
        System.out.println("Calcolo Totale: Lordo " + totaleLordo + "€ -> Scontato " + totaleFinale + "€");
        return totaleFinale;
    }

    // --- GESTIONE PREFERITI (WISHLIST) ---

    public boolean aggiungiAPreferiti(int idLibro) {
        return libroDAO.aggiungiPreferito(idLibro);
    }

    public boolean rimuoviDaPreferiti(int idLibro) {
        return libroDAO.rimuoviPreferito(idLibro);
    }

    public List<Libro> getPreferiti() {
        return libroDAO.getLibriPreferiti();
    }
    
    public boolean isPreferito(int idLibro) {
        return libroDAO.isPreferito(idLibro);
    }

    // --- GESTIONE CHECKOUT E PERSISTENZA ORDINE ---
    public Ordine effettuaCheckout(List<Libro> carrello, Utente utenteLoggato, ScontoStrategy strategia, String indirizzoConsegna) {
        if (carrello == null || carrello.isEmpty() || utenteLoggato == null) {
            System.out.println("Errore: Carrello vuoto o utente non loggato.");
            return null;
        }

        // Calcolo del totale lordo basato sui prezzi attuali dei libri nel carrello
        double totaleLordo = 0;
        for (Libro libro : carrello) {
            totaleLordo += libro.getPrezzo();
        }

        // Creazione e configurazione dell'entità Ordine
        Ordine nuovoOrdine = new Ordine();
        nuovoOrdine.setUtenteId(utenteLoggato.getId());
        nuovoOrdine.setTotale(totaleLordo);
        
        // Applichiamo lo sconto 
        nuovoOrdine.applicaSconto(strategia); 
        nuovoOrdine.setStato("Creato");

        // Configurazione della Spedizione
        Spedizione spedizione = new Spedizione();
        spedizione.setIndirizzoConsegna(indirizzoConsegna);
        spedizione.setCorriere("Corriere Espresso"); 
        spedizione.setTrackingNumber("T-PENDING-" + System.currentTimeMillis()); // Tracking fittizio
        
        nuovoOrdine.setSpedizione(spedizione);

        //Salvataggio fisico su Database tramite OrdineDAO
        boolean successoSalvataggio = ordineDAO.salvaOrdineCompleto(nuovoOrdine, carrello);

        if (successoSalvataggio) {
            System.out.println("Checkout completato e salvato nel DB!");
            System.out.println("Ordine ID: " + nuovoOrdine.getId() + " per l'utente: " + utenteLoggato.getUsername());
            System.out.println("Importo finale pagato: " + nuovoOrdine.getTotale() + "€");
            return nuovoOrdine;
        } else {
            System.err.println("Errore critico: Impossibile salvare l'ordine nel database.");
            return null;
        }
    }
    public List<Ordine> getStoricoOrdini(int utenteId) {
        return ordineDAO.getOrdiniByUtente(utenteId);
    }
}