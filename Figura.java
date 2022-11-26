package esercitazioneJava2;

import java.awt.Color;
import java.util.Scanner;

// Questa abstract class implementa i metodi che sono uguali per tutte e tre le figure
public abstract class Figura {
    protected Color colore;

    // Questi due metodi non sono usati da nessuna parte
    public Color colore() { return colore; }
    public void cambiaColore(Color nuovo) { colore = nuovo; }

    // Compie la prima parte di decodifica di una figura che è uguale per tutti,
    // ovvero la decodifica del colore. A seguire chiama il rispettivo metodo leggi
    // nella classe Rettangolo, Ellisse o Triangolo
    
    // Nome un po' strano per questa funzione, generalmente in Inglese quest'operazione si
    // chiama `parse`, non so.
    static public FiguraI leggi (String s) throws Exception {
        // Fai s.indexOf("COL") tre volte, not necessary - estrailo.
        if (s.indexOf(" COL ") == -1) {
            // Se non viene trovato "COL" il formato era sbagliato
            // Potrebbe essere una buona idea includere il formato corretto nel messaggio
            // di errore, cosi` un utente dell'applicazione sa esattamente cosa fare.
            throw new Exception("Formato della figura errato: manca il colore");
        }
        // Questo scanner decodifica il colore dai valori RGB
        Scanner scColore = new Scanner(s.substring(s.indexOf("COL ") + 4)); // Questo 4 volante cosa rappresenta?
        int red = scColore.nextInt();
        int green = scColore.nextInt();
        int blue = scColore.nextInt();
        Color col = new Color(red, green, blue);

        // Dalla stringa s vengono eliminati tutti i dati non più necessari: la parte
        // del colore e la prima parte che indica di che tipo di figura ci stiamo
        // occupando
        
        // c'e` motivo di modificare s qui? Non puoi passare allo scanner direttametne
        // s.substring(0, s.lastIndexOf(" COL ")).trim() ? 
        s = s.substring(0, s.lastIndexOf(" COL ")).trim();
        Scanner scFigura = new Scanner(s);
        String sigla = scFigura.next();
        scFigura.close();

        // Vedi se ha senso (e se funziona), cosi` hai molta meno duplicazione
        return Figura.tipoDiFiguraDaSigla(sigla).leggi(s.substring(4), col);
    }

    static private Figura tipoDiFiguraDaSigla(String s) throws Exception {
        switch (scFigura.next()) {
            case "RET":
                return Rettangolo;
            case "ELL":
                return Ellisse;
            case "TRI":
                return Triangolo;
            default:
                throw new Exception("Formato della figura errato: manca il tipo di figura");
        }
    }
}
