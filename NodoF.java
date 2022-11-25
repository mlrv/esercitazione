package esercitazioneJava2;

// Quaste classe serve per implementare una linked list che contiene le figure
public class NodoF {
    FiguraI contenuto;
    NodoF successivo;

    NodoF(FiguraI figura) {
        contenuto = figura;
        successivo = null;
    }

    // Stampa il contenuto di questo nodo e di tutti i segunenti tramite delegation
    public String stampaTutti() {
        String s = contenuto.toString();
        if (successivo != null) {
            s += "\n" + successivo.stampaTutti();
        }
        return s;
    }
}
