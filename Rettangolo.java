package esercitazioneJava2;

import java.awt.Color;
import java.text.NumberFormat;
import java.util.Scanner;

public class Rettangolo extends Figura implements FiguraI {
    private Punto verticeBasso;
    private Punto verticeAlto;
    private static final int NUMERODIVERTICI = 4;

    /**
     * Costruttore dato il vertice in basso a sinistra e quello in alto a destra.
     * Assegna un colore casuale
     * 
     * @param vB vertice in basso a sinistra
     * @param vA vertice in alto a destra
     */
    Rettangolo(Punto vB, Punto vA) {
        verticeBasso = vB;
        verticeAlto = vA;
        colore = new Color((int) (Math.random() * 0x1000000));
    }

    /**
     * Costruttore dato il vertice in basso a sinistra, quello in alto a destra, e
     * il colore
     * 
     * @param vB  vertice in basso a sinistra
     * @param vA  vertice in alto a destra
     * @param col colore
     */
    Rettangolo(Punto vB, Punto vA, Color col) {
        this(vB, vA);
        colore = col;
    }

    /* 
     * Overriding di toString() per avere un formato standard
     */
    public String toString() {
        // Il numberFormat è usato per scrivere i double con la virgola e non il punto,
        // poichè bufferedReader (usato nel caricamento da file) non può cambiare
        // formato di lettura come può scanner
        NumberFormat nf = NumberFormat.getInstance();

        String[] ss = new String[] {
            "RET",
            nf.format(verticeBasso.x),
            nf.format(verticeBasso.y),
            nf.format(verticeAlto.x),
            nf.format(verticeAlto.y),
            "COL",
            colore.getRed(),
            colore.getGreen(),
            colore.getBlue(),
        };

        return String.join(" ", ss);
    }

    /**
     * Ritorna un rettangolo da una stringa in formato standard. il colore viene
     * passato dopo che è già stato decodificato a monte dal metodo leggi nella
     * classe Figura
     * 
     * @param s   stringa nel formato standard
     * @param col colore
     * @return rettangolo da stringa in formato standard
     */
    // Il formato di `leggi` e` praticamente identico in tutte le figure,
    // c'e` la possibilita` di astrarlo in Figura?
    static Rettangolo leggi(String s, Color col) throws Exception {
        Scanner sc = new Scanner(s);
        double vBx = sc.nextDouble();
        double vBy = sc.nextDouble();
        double vAx = sc.nextDouble();
        double vAy = sc.nextDouble();
        if (sc.hasNext()) {
            // Se dopo che i quattro numeri sono stati letti ci sono altre stringhe, il
            // formato era errato
            sc.close();
            throw new Exception("Formato del rettangolo errato");
        }
        sc.close();
        return new Rettangolo(new Punto(vBx, vBy), new Punto(vAx, vAy), col);
    }

    // Per le funzione richieste dall'interfaccia FiguraI non scrivo un Javadoc in
    // quanto esiste già nell'interfaccia

    public int numeroVertici() { return NUMERODIVERTICI; }

    public double minX() { return verticeBasso.x; }
    public double minY() { return verticeBasso.y; }

    public double estensioneX() { return verticeAlto.x - verticeBasso.x; }
    public double estensioneY() { return verticeAlto.y - verticeBasso.y; }

    private Punto centro() { return Punto.puntoMedio(verticeBasso, verticeAlto); }
    public double centroX() { return centro().x; }
    public double centroY() { return centro().y; }

    public boolean contiene(double x, double y) {
        return (x >= verticeBasso.x && x <= verticeAlto.x &&
                y >= verticeBasso.y && y <= verticeAlto.y);
    }

    public double area() { return estensioneX() * estensioneY(); }

    public double contorno() { return 2 * (estensioneX() + estensioneY()); }

    public void trasla(double deltaX, double deltaY) {
        //Uso il metodo trasla della classe Punto
        verticeAlto.trasla(deltaX, deltaY);
        verticeBasso.trasla(deltaX, deltaY);
    }
}
