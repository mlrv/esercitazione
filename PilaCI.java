package esercitazioneJava;

interface PilaCI<E> {

    // ritorna il numero di elementi
    public int size();

    // test se non ci sono elementi
    public boolean isEmpty();

    // aggiunge E in cima
    public void push(E e);

    // aggiunge elemento in fondo
    public void add(E e);

    // elimina l'elemento all'indice dato 
    public void delete(int n);

    // sposta ripettivamente in cima o in fondo l'elemento all'indice dato
    public void moveToTop(int n);
    public void moveToBottom(int n);

    // elimina tutta gli elementi
    public void clear();

}