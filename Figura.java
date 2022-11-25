package esercitazioneJava2;

import java.awt.Color;
import java.util.Scanner;

// Questa abstract class implementa i metodi che sono uguali per tutte e tre le figure
public abstract class Figura {
    protected Color colore;

    public Color colore() { return colore; }
    public void cambiaColore(Color nuovo) { colore = nuovo; }

    // Compie la prima parte di decodifica di una figura che è uguale per tutti,
    // ovver la decodifica del colore. A seguire chiama il rispettivo metodo leggi
    // nella classe Rettangolo, Ellisse o Triangolo
    static public FiguraI leggi (String s) throws Exception {
        if (s.indexOf(" COL ") == -1) {
            // Se non viene trovato "COL" il formato era sbagliato
            throw new Exception("Formato della figura errato: manca il colore");
        }
        // Questo scanner decodifica il colore dai valori RGB
        Scanner scColore = new Scanner(s.substring(s.indexOf("COL ") + 4));
        int red = scColore.nextInt();
        int green = scColore.nextInt();
        int blue = scColore.nextInt();
        Color col = new Color(red, green, blue);;

        // Dalla stringa s vengono eliminati tutti i dati non più necessari: la parte
        // del colore e la prima parte che indica di che tipo di figura ci stiamo
        // occupando
        s = s.substring(0, s.lastIndexOf(" COL ")).trim();
        Scanner scFigura = new Scanner(s);
        switch (scFigura.next()) {
            case "RET":
            scFigura.close();
                return Rettangolo.leggi(s.substring(4), col);
            case "ELL":
            scFigura.close();
                return Ellisse.leggi(s.substring(4), col);
            case "TRI":
            scFigura.close();
                return Triangolo.leggi(s.substring(4), col);
            default:
            scFigura.close();
                throw new Exception("Formato della figura errato: manca il tipo di figura");
        }
    }
}
