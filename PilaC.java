package esercitazioneJava;

public class PilaC<E> implements PilaCI<E> {
    
    private NodoC<E> first;
    private NodoC<E> last;
    private int size;

    public int size() { return size; }

    public boolean isEmpty() { return size == 0; }

    public void push(E e) {
        NodoC<E> node = new NodoC<E>(e);
        if (size == 0) {
            first = node;
            last = node;
        } else {
            node.next = first;
            first = node;
        }
        size++;
    }

    public void add(E e) {
        NodoC<E> node = new NodoC<E>(e);
        if (size == 0) {
            first = node;
            last = node; 
        } else {
            last.next = node;
            last = node;
        }
        size++;
    }

    // TODO solo per test
    public String toString() {
        if (first == null) {
            return "[]";
        } else {
            return "[" + first + "]";
        }

    }

    @Override
    public void delete(int n) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void moveToTop(int n) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void moveToBottom(int n) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void clear() {
        // TODO Auto-generated method stub
        
    }

    
}
