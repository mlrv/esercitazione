package esercitazioneJava2;

import java.awt.Color;
import java.text.NumberFormat;
import java.util.Scanner;

public class Ellisse extends Figura implements FiguraI{

    private Punto centro;
    private double raggioX;
    private double raggioY;
    private static final int NUMERODIVERTICI = 0;

    /**
     * Costruttore dato il centro e i due raggi. Assegna un colore casuale
     * 
     * @param c  centro
     * @param rX raggio orizzontale
     * @param rY raggio verticale
     */
    Ellisse(Punto c, double rX, double rY) {
        centro = c;
        raggioX = rX;
        raggioY = rY;
        colore = new Color((int)(Math.random() * 0x1000000));
    }

    /**
     * Costruttore dato il centro, i due raggi e il colore
     * 
     * @param c   colore
     * @param rX  raggio orizzonatle
     * @param rY  raggio verticale
     * @param col colore
     */
    Ellisse(Punto c, double rX, double rY, Color col) {
        this(c, rX, rY);
        colore = col;
    } 

    /* 
     * Overriding di toString() per avere un formato standard
     */
    public String toString() {
        // Il numberFormat è usato per scrivere i double con la virgola e non il punto,
        // poichè bufferedReader (usato nel caricamento da file) non può cambiare
        // formato di lettura come può scanner
        // Puoi ottimizzare anche questo come in Rettangolo
        NumberFormat nf = NumberFormat.getInstance();
        return "ELL " + nf.format(centro.x) + " " + nf.format(centro.y) + " " +
                nf.format(raggioX) + " " + nf.format(raggioY) + " COL " + colore.getRed() +
                " " + colore.getGreen() + " " + colore.getBlue();
    }

    /**
     * Ritorna un'ellisse da una stringa in formato standard. il colore viene
passata dopo che è già stato decodificato a monte dal metodo leggi nella
classe Figura
     * 
     * @param s stringa nel formato standard
     * @param col colore
     * @return ellisse da stringa in formato standard
     */
    static Ellisse leggi(String s, Color col) throws Exception {
        Scanner sc = new Scanner(s);
        double cx = sc.nextDouble();
        double cy = sc.nextDouble();
        double rx = sc.nextDouble();
        double ry = sc.nextDouble();
        if (sc.hasNext()) {
            // Se dopo che i quattro numeri sono stati letti ci sono altre stringhe, il
            // formato era errato
            sc.close();
            throw new Exception("Formato dell'ellisse  errato");
        }
        sc.close();
        return new Ellisse(new Punto(cx, cy), rx, ry, col);
    }

    // Per le funzione richieste dall'interfaccia FiguraI non scrivo un Javadoc in
    // quanto esiste già nell'interfaccia

    public int numeroVertici() { return NUMERODIVERTICI; }

    public double minX() { return centro.x - raggioX; }
    public double minY() { return centro.y - raggioY; }

    public double estensioneX() { return 2 * raggioX; }
    public double estensioneY() { return 2 * raggioY; }

    public double centroX() { return centro.x; }
    public double centroY() { return centro.y; }
    
    public boolean contiene(double x, double y) {
        return ((((x-centro.x)*(x-centro.x)) / ((raggioX)*(raggioX))) + 
                (((y-centro.y)*(y-centro.y)) / ((raggioY)*(raggioY)))) <= 1;
    }

    public double area() { return Math.PI * raggioX * raggioY; }

    public double contorno() {
        return 2 * Math.PI * Math.sqrt((raggioX*raggioX + raggioY*raggioY) / 2);
    }

    public void trasla(double deltaX, double deltaY) {
        // Uso il metodo trasla della classe Punto
        centro.trasla(deltaX, deltaY); 
    }
}
