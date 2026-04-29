package businesslogic;

public class ScontoFedelta implements ScontoStrategy {
    @Override
    public double applicaSconto(double totaleIniziale) {
        return totaleIniziale * 0.90; // Sconto del 10%
    }
}