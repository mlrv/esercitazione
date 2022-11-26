package esercitazioneJava2;

import java.awt.Color;
import java.text.NumberFormat;
import java.util.Scanner;

public class Triangolo extends Figura implements FiguraI {
    private Punto verticeSx;
    private double base;
    private double altezza;
    private static final int NUMERODIVERTICI = 3;

    /**
     * Costruttore dato il veritce in basso a sinistra, la base e l'altezza. Assegna
     * un colore casuale
     * 
     * @param vSx veritce in basso a sinistra
     * @param b   base
     * @param h   altezza
     */
    Triangolo(Punto vSx, double b, double h) {
        verticeSx = vSx;
        base = b;
        altezza = h;
        colore = new Color((int)(Math.random() * 0x1000000));
    }

    /**
     * Cotruttore dato il vertice in basso a sinistra, la base, l'altezza e il
     * colore
     * 
     * @param vSx veritce in basso a sinistra
     * @param b   base
     * @param h   altezza
     * @param col colore
     */
    Triangolo(Punto vSx, double b, double h, Color col) {
        this(vSx, b, h);
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
        // Puoi ottimizzare anche questo come in Rettangolo
        return "TRI " + nf.format(verticeSx.x) + " " + nf.format(verticeSx.y) + " " +
                nf.format(base) + " " + nf.format(altezza) + " COL " + colore.getRed() +
                " " + colore.getGreen() + " " + colore.getBlue();
    }

    /**
     * Ritorna un triangolo da una stringa in formato standard. il colore viene
     * passata dopo che è già stato decodificato a monte dal metodo leggi nella
     * classe Figura
     * 
     * @param s   stringa nel formato standard
     * @param col colore
     * @return triangolo da stringa in formato standard
     */
    static Triangolo leggi(String s, Color col) throws Exception {
        Scanner sc = new Scanner(s);
        double vSx = sc.nextDouble();
        double vSy = sc.nextDouble();
        double b = sc.nextDouble();
        double h = sc.nextDouble();
        if (sc.hasNext()) {
            // Se dopo che i quattro numeri sono stati letti ci sono altre stringhe, il
            // formato era errato
            sc.close();
            throw new Exception("Formato del triangolo errato");
        }
        sc.close();
        return new Triangolo(new Punto(vSx, vSy), b, h, col);
    }

    // Per le funzione richieste dall'interfaccia FiguraI non scrivo un Javadoc in
    // quanto esiste già nell'interfaccia
    
    public int numeroVertici() { return NUMERODIVERTICI; }

    public double minX() { return verticeSx.x; }
    public double minY() { return verticeSx.y; }

    public double estensioneX() { return base; }
    public double estensioneY() { return altezza; }

    public double centroX() { return verticeSx.x + (base/2); }
    public double centroY() { return verticeSx.y + (altezza/3); }

    public boolean contiene(double x, double y) {
        if (x < verticeSx.x || x > verticeSx.x + base ||
            y < verticeSx.y || y > verticeSx.y + altezza) {
            return false;
        }
        return (Math.abs(x - (verticeSx.x + base/2))) <= 
                0.5 * (verticeSx.y + altezza - y) * (base/altezza);
    }

    public double area() { return base * altezza * 0.5; }

    public double contorno() { return base + 2 * Math.sqrt((base/2)*(base/2) + altezza*altezza); }

    public void trasla(double deltaX, double deltaY) {
        // Uso il metodo trasla della classe Punto
        verticeSx.trasla(deltaX, deltaY);
    }
}
