package esercitazioneJava2;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;

public class Sistema implements SistemaI {
    // Utilizzo una linked list per contenere le figure. Tengo anche traccia
    // dell'ultimo nodo in modo da rendere più semplice l'implementazione di alcuni
    // metodi
    // magari fai chiarezza su cosa intendi con primo e ultimo, c'e` un ordine di qualche tipo?
    // primo e` la figura on top?
    private NodoF primo;
    private NodoF ultimo;
    private int numeroFigure;


    // Per i metodi dell'interfaccia SistemaI non scrivo un Javadoc in
    // quanto esiste già nell'interfaccia

    public int numeroFigure() {
        return numeroFigure;
    }

    public FiguraI[] figure() {
        FiguraI[] figure = new FiguraI[numeroFigure];
        NodoF cursore = primo;
        // Creo un array e lo riempo partendo dal fondo in modo da avere le figure
        // nell'ordine giusto
        for (int i = numeroFigure - 1; i >= 0; i--) {
            figure[i] = cursore.contenuto;
            cursore = cursore.successivo;
        }
        return figure;
    }

    // spostaSopra e spostaSotto sono *molto* simili, c'e` sicuramente
    // la possibilita` di estrarre la logica in comune
    public void spostaSopra(FiguraI f) {
        if (primo.contenuto == f) {
            // Se la figura è già in cima non devo fare niente
            return;
        }
        NodoF cursore = primo;
        while (cursore.successivo.contenuto != f) {
            // Uso un cursore per scorrere la lista finchè non trovo la figura nel nodo
            // successivo al cursore
            cursore = cursore.successivo;
        }
        // Usando un NodoF temporaneo metto in cima la figura cercata e "riaggancio" la
        // lista nel modo corretto
        NodoF temp = cursore.successivo.successivo;
        cursore.successivo.successivo = primo;
        primo = cursore.successivo;
        cursore.successivo = temp;
    }

    public void spostaSotto(FiguraI f) {
        if (ultimo.contenuto == f) {
            // Se la figura è già in fondo non devo fare niente
            return;
        }
        if (primo.contenuto == f) {
            // Se la figura è la prima il processo è diverso dal caso generale quindi lo
            // gestisco separatamente
            ultimo.successivo = primo;
            primo = primo.successivo;
            ultimo.successivo.successivo = null;
            ultimo = ultimo.successivo;
            return;
        }
        NodoF cursore = primo;
        while (cursore.successivo.contenuto != f) {
            // Uso un cursore per scorrere la lista finchè non trovo la figura nel nodo
            // successivo al cursore
            cursore = cursore.successivo;
        }
        // Usando un NodoF temporaneo metto in fondo la figura cercata e "riaggancio" la
        // lista nel modo corretto
        NodoF temp = cursore.successivo;
        cursore.successivo = cursore.successivo.successivo;
        ultimo.successivo = temp;
        ultimo = ultimo.successivo;
        ultimo.successivo = null;
    }

    public void elimina(FiguraI f) {
        // I casi in cui ci sia una sola figura o in cui la figura da eliminare sia la
        // prima vengono gestiti separatamente
        // Perche`? Cosa guadagni a fare cosi`?
        if (numeroFigure == 1) {
            svuota();
            return;
        }
        if (primo.contenuto == f) {
            primo = primo.successivo;
            numeroFigure--;
            return;
        }
        NodoF cursore = primo;
        while (cursore.successivo.contenuto != f) {
            // Uso un cursore per scorrere la lista finchè non trovo la figura nel nodo
            // successivo al cursore
            cursore = cursore.successivo;
        }
        // Aggancio il cursore alla figura successiva a quella da eliminare
        cursore.successivo = cursore.successivo.successivo;
        numeroFigure--;
    }

    public void svuota() {
        primo = ultimo = null;
        numeroFigure = 0;
    }

    public FiguraI figuraCheContiene(double x, double y) {
        NodoF cursore = primo;
        while (cursore != null) {
            // Uso un cursore per scorrere la lista chiamando il metodo contiene sul contenuto dei nodi
            if (cursore.contenuto.contiene(x, y)) {
                return cursore.contenuto;
            }
            cursore = cursore.successivo;
        }
        return null;
    }

    private void aggiungiFigura(FiguraI figura) {
        NodoF nuovo = new NodoF(figura);
        if (numeroFigure == 0) {
            // Poichè tengo traccia anche dell'ultimo nodo della linked list, devo gestire
            // separatamente il caso in cui la lista sia inizialmente vuota
            primo = nuovo;
            ultimo = nuovo;
        } else {
            nuovo.successivo = primo;
            primo = nuovo;
        }
        numeroFigure++;
    }

    // Questo metodo non è richiesto dall'interfaccia ma mi serve per caricare le
    // figure da file perchè se usassi aggiungiFigura che le aggiunge in cima
    // invertirei l'ordine delle figure
    // C'e` sicuramente la possibilita` di astrarre le parti comuni di
    // aggiungiFigura e aggiungiFiguraInFondo
    private void aggiungiFiguraInFondo(FiguraI figura) {
        NodoF nuovo = new NodoF(figura);
        if (numeroFigure == 0) {
            primo = nuovo;
            ultimo = nuovo;
        } else {
            ultimo.successivo = nuovo;
            ultimo = nuovo;
        }
        numeroFigure++;
    }

    // I tre metodi seguenti eseguono i dovuti controlli sui dati passati, creano la
    // figura richiesta e poi la passano ad aggiungiFigura che prende come parametro
    // una qualsiasi FiguraI

    public FiguraI aggiungiEllisse(double cX, double cY, double rX, double rY) {
        Ellisse ellisse = new Ellisse(new Punto(cX, cY), rX, rY);
        aggiungiFigura(ellisse);
        return ellisse;
    }

    public FiguraI aggiungiRettangolo(double x1, double y1, double x2, double y2) {
        double xB, yB, xA, yA;
        // uuuuuugh
        if (x1 < x2) {
            xB = x1;
            xA = x2;
        } else {
            xB = x2;
            xA = x1;
        }
        if (y1 < y2) {
            yB = y1;
            yA = y2;
        } else {
            yB = y2;
            yA = y1;
        }
        Rettangolo rettangolo = new Rettangolo(new Punto(xB, yB), new Punto(xA, yA));
        aggiungiFigura(rettangolo);
        return rettangolo;
    }

    public FiguraI aggiungiTriangolo(double x1, double x2, double yBase, double altezza) {
        double x = Math.min(x1, x2);
        Triangolo triangolo = new Triangolo(new Punto(x, yBase), Math.abs(x1 - x2), altezza);
        aggiungiFigura(triangolo);
        return triangolo;
    }

    public void leggiDaFile(String nome) throws Exception {
        // Elimino le figure presenti prima di aggiungere quelle lette dal file
        svuota();
        BufferedReader br = new BufferedReader(new FileReader(nome));    
        int num = Integer.parseInt(br.readLine());
        // Con un ciclo for aggiungo le figure in fondo usando il metodo leggi della
        // classe astratta Figura
        for (int i = 0; i < num; i++) {
            aggiungiFiguraInFondo(Figura.leggi(br.readLine()));
        }
        br.close();
    }

    public void scriviSuFile(String nome) throws Exception {
        PrintStream ps = new PrintStream (new FileOutputStream(nome));
        // Scrivo per rima cosa il numero di figure presenti
        ps.println(numeroFigure);
        // Tramite delegation stampo tutte le figure chimanado stampaTutti sul primo
        // nodo della linked list
        if (numeroFigure != 0) {
            ps.print(primo.stampaTutti());
            ps.close();
        }
    }

    public String titolo() {
        return "Levrero";
    }
    
}
