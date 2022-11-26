package esercitazioneJava2;

// Classe Punto per rendere più semplice l'implementazione di alcuni metodi nelle figure
public class Punto {
    /**
     * Coordinata x
     */
    double x;
    /**
     * Coordinata y
     */
    double y;

    /**
     * Costruttore date le due coordinate
     * 
     * @param xx coordinata x
     * @param yy coordinata y
     */
    Punto(double xx, double yy) {
        x = xx;
        y = yy;
    }

    /**
     * Ritorna il punto medio fra due punti dati
     * 
     * @param p1 primo punto
     * @param p2 secondo punto
     * @return punto medio
     */
    static Punto puntoMedio(Punto p1, Punto p2) {
        return new Punto((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
    }

    /**
     * Trasla il punto dei valori dati lungo x e lungo y
     * 
     * @param deltaX valore della traslazione lungo x
     * @param deltaY valore della traslazione lungo y
     */
    void trasla(double deltaX, double deltaY) {
        x += deltaX;
        y += deltaY;
    }

    /**
     * Ritorna la distanza da un punto dato
     * 
     * @param p altro punto
     * @return distanza
     */
    double distanza(Punto p) {
        return Math.sqrt(Math.pow((x - p.x), 2) + Math.pow((y - p.y), 2));
    }
}
