package esercitazioneJava;

public class NodoC<E> implements NodoCI<E> {
    E content;
    NodoC<E> next;

    NodoC(E e) {
        content = e;
        next = null;
    }

    // TODO solo per test
    public String toString() {
        String s = "" + content;
        if (next != null) {
            s += "," + next;
        }
        return s;
    }
}
