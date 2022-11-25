package esercitazioneJava2;

import java.awt.*; // per Color

/**
 * Interfaccia che definisce quali metodi dovranno fornire
 * le classi che rappresentano le varie figure geometriche.
 */
public interface FiguraI {
    /**
     * Ritorna il numero di vertici per una figura poligonale,
     * zero per una figura circolare.
     * 
     * @return il numero di vertici.
     */
    int numeroVertici();

    /**
     * Ritorna la coordinata x minima della figura.
     * 
     * @return x minima della figura.
     */
    double minX();

    /**
     * Ritorna la coordinata y minima della figura.
     * 
     * @return y minima della figura.
     */
    double minY();

    /**
     * Ritorna l'estensione della figura nella direzione
     * dell'asse x (differenza tra x massima e x minima).
     * 
     * @return estensione della figura nella direzione x.
     */
    double estensioneX();

    /**
     * Ritorna l'estensione della figura nella direzione
     * dell'asse y (differenza tra y massima e y minima).
     * 
     * @return estensione della figura nella direzione y.
     */
    double estensioneY();

    /**
     * Ritorna la coordinata x del centro della figura.
     * Preferibilmente il baricentro, ma non e' strettamente
     * necessario (basta che sia un punto interno).
     * 
     * @return la x del centro.
     */
    double centroX();

    /**
     * Ritorna la coordinata y del centro della figura.
     * Preferibilmente il baricentro, ma non e' strettamente
     * necessario (basta che sia un punto interno).
     * 
     * @return la y del centro.
     */
    double centroY();

    /**
     * Ritorna vero se e solo se questa figura contiene
     * il punto (x,y) all'interno o sul contorno.
     * 
     * @param x ascissa del punto,
     * @param y ordinata del punto
     * @return true se la figura contiene (x,y), false altrimenti.
     */
    boolean contiene(double x, double y);

    /**
     * Ritorna l'area di questa figura.
     * 
     * @return l'area.
     */
    double area();

    /**
     * Ritorna la lunghezza del contorno di questa figura.
     * 
     * @return la lunghezza del contorno.
     */
    double contorno();

    /**
     * Trasla la figura del vettore (deltaX, deltaY).
     * 
     * @param deltaX componente x del vettore traslazione,
     * @param deltaY componente y del vettore traslazione.
     */
    void trasla(double deltaX, double deltaY);

    /**
     * Ritorna il colore della figura.
     * 
     * @return colore di questa figura.
     */
    Color colore();

    /**
     * Cambia il colore della figura mettendo il colore nuovo.
     * 
     * @param nuovo colore da mettere.
     */
    void cambiaColore(Color nuovo);
}
