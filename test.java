package esercitazioneJava;

public class test {
    public static void main(String [] arg) {
        PilaC<Integer> pila = new PilaC<>();

        System.out.println(pila + " " + pila.size());
        pila.add(3);
        pila.add(2);
        pila.add(5);
        System.out.println(pila + " " + pila.size());
        pila.push(0);
        pila.push(4);
        System.out.println(pila + " " + pila.size());


    }
}
