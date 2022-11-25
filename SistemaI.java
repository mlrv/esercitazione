package esercitazioneJava2;

import java.awt.*;
import java.io.*;

public interface SistemaI {
    /**
     * Il sistema grafico usera' questo numero per dimensionare
     * la finestra di disegno. La dimensione sara' preferibilmente
     * DIMENSIONE x DIMENSIONE pixel con l'origine al centro.
     * Tuttavia potrebbe venire piu' grande per esigenze di layout,
     * come da prassi di Java swing.
     */
    int DIMENSIONE = 400;

    /**
     * Ritorna il numero di figure presenti.
     * 
     * @return numero di figure.
     */
    int numeroFigure();

    /**
     * Ritorna un array contenente tutte le figure presenti nel sistema
     * nell'ordine da quella piu' in basso a quella piu' in alto.
     * L'array puo' contenere proprio le stesse figure, come oggetti,
     * che sono presenti nel sistema, non delle copie.
     * 
     * @return array contenenta tutte le figure.
     */
    FiguraI[] figure();

    /**
     * Sposta la figura f sopra a tutte le altre.
     * 
     * @param f la figura da spostare
     *          (si assume che f sia una delle figure presenti nel sistema).
     */
    void spostaSopra(FiguraI f);

    /**
     * Sposta la figura f sotto a tutte le altre.
     * 
     * @param f la figura da spostare
     *          (si assume che f sia una delle figure presenti nel sistema).
     */
    void spostaSotto(FiguraI f);

    /**
     * Elimina la figura f dal sistema.
     * 
     * @param f la figura da eliminare.
     *          (si assume che f sia una delle figure presenti nel sistema).
     */
    void elimina(FiguraI f);

    /** Elimina tutte le figure presenti. */
    void svuota();

    /**
     * Ritorna la figura piu' in alto fra quelle che contengono
     * il punto di coordinate (x,y). Deve ritornare proprio la stessa
     * figura presente nel sistema come oggetto, non una sua copia.
     * Se nessuna figura contiene (x,y), ritorna null.
     * 
     * @param x ascissa del punto
     * @param y ordinata del punto
     * @return la figura piu' in alto tra quelle che contengono il punto (x,y)
     *         oppure null se nessuna figura contiene (x,y).
     */
    FiguraI figuraCheContiene(double x, double y);

    /**
     * Aggiunge come nuova figura un ellisse e lo ritorna,
     * la nuova figura viene collocata sopra a tutte le altre.
     * L'ellisse ha centro (cX,cY), raggio orizzontale rX e raggio
     * verticale rY.
     * Si garantisce che, nelle chiamate, rX e rY saranno positivi.
     * L'implementazione e' libera di decidere il colore dell'ellisse.
     * 
     * @param cX coordinata x del centro,
     * @param cY coordinata x del centro,
     * @param rX raggio orizzontale,
     * @param rY raggio verticale.
     * @return la nuova figura (ellisse) creata e aggiunta.
     */
    FiguraI aggiungiEllisse(double cX, double cY, double rX, double rY);

    /**
     * Aggiunge come nuova figura un rettangolo e lo ritorna,
     * la nuova figura viene collocata sopra a tutte le altre.
     * I due punti (x1,y1) e (x2,y2) definiscono una diagonale del
     * rettangolo.
     * Nelle chiamate non si garantisce che (x1,y1) sia l'angolo in basso
     * a sinistra, potrebbe essere uno qualsiasi dei quattro angoli.
     * Si garantisce (x1,y1) e (x2,y2) saranno due angoli opposti, cioe'
     * x1 diverso da x2 e y1 diverso da y2.
     * L'implementazione e' libera di decidere il colore del rettangolo.
     * 
     * @param x1 coordinata x del primo angolo,
     * @param y1 coordinata x del primo angolo,
     * @param x2 coordinata x del secondo angolo,
     * @param y2 coordinata x del secondo angolo.
     * @return la nuova figura (rettangolo) creata e aggiunta.
     */
    FiguraI aggiungiRettangolo(double x1, double y1, double x2, double y2);

    /**
     * Aggiunge come nuova figura un triangolo isoscele e lo ritorna,
     * la nuova figura viene collocata sopra a tutte le altre.
     * Il triangolo si estende in orizzontale da x1 a x2, yBase e'
     * l'ordinata della base e altezza e' l'altezza. Il triangolo si
     * intende con la punta in su, ovvero il vertice opposto alla base
     * ha ordinata yBase+altezza.
     * Non si garantisce che, nelle chiamate, x1 sia minore di x2,
     * potrebbe essere maggiore, si garantisce che i due valori siano diversi.
     * Il valore di altezza invece e' garantito positivo in ogni chiamata.
     * L'implementazione e' libera di decidere il colore del triangolo.
     * 
     * @param x1      coordinata x di uno dei due estremi della base,
     * @param x2      coordinata x dell'altro estremo della base,
     * @param yBase   coordinata y della base del triangolo,
     * @param altezza l'altezza del triangolo.
     * @return la nuova figura (triangolo) creata e aggiunta.
     */
    FiguraI aggiungiTriangolo(double x1, double x2, double yBase, double altezza);

    /**
     * Legge le figure nel file di nome dato e le carica nel sistema.
     * Il file deve contenere prima il numero totale di figure,
     * poi le figure nell'ordine dalla prima all'ultima disegnata.
     * Sintassi per rettangolo, triangolo, ellisse:
     * <BR>
     * RET xmin ymin xmax ymax COL red green blue
     * <BR>
     * TRI xmin ymin larghezza altezza COL red green blue
     * <BR>
     * ELL xcentro ycentro raggiox raggioy COL red green blue
     * <BR>
     * I tre valori red, green, blue, sono numeri reali tra 0 e 1.
     * 
     * @param nome il nome del file da cui leggere.
     * @throws Exception se non riesce a leggere (per esempio se il file
     *                   non esiste o non ha la sintassi richiesta).
     */
    void leggiDaFile(String nome) throws Exception;

    /**
     * Scrive tutte le figure presenti sul file di nome dato.
     * Il file viene scritto con la stessa sintassi specificata per la lettura,
     * andando a capo dopo il numero di figure e scrivendo ogni
     * figura su una riga.
     * 
     * @param nome il nome del file su cui scrivere.
     * @throws Exception se non riesce a scrivere.
     */
    void scriviSuFile(String nome) throws Exception;

    /**
     * Ritorna il titolo del sistema.
     * Potete metterci i vostri nomi, un nome di fantasia... come volete.
     * 
     * @return il titolo del sistema.
     */
    String titolo();

}
