package businesslogic;

public class ScontoNullo implements ScontoStrategy {
    @Override
    public double applicaSconto(double totaleIniziale) {
        return totaleIniziale; // Nessuno sconto
    }
}