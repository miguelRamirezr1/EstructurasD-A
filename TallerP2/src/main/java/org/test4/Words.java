package org.test4;

import java.util.ArrayDeque;

public class Words {
    private LinkedList encryptedList;
    private ArrayDeque<LinkedList> wordsDeque;
    private int oddCounter;

    public Words() {
        this.encryptedList = new LinkedList();
        this.wordsDeque = new ArrayDeque<>();
        this.oddCounter = 1;
    }

    public void encryptWord(String word) {
        encryptedList = new LinkedList();
        oddCounter = 1;
        System.out.println("Procesando palabra: " + word);

        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            int ascii = (int) c;
            int encrypted = ascii + oddCounter;

            System.out.println("    " + c + " (" + ascii + ") + " + oddCounter + " -> " + encrypted);

            encryptedList.insert(encrypted);
            oddCounter += 2;
        }

        System.out.println("nodos orden original: " + encryptedList);

        encryptedList.swapAdjacentNodes();
        System.out.println("nodos intercambiados: " + encryptedList);
        wordsDeque.add(encryptedList);
    }

    public void encryptMessage(String message) {
        wordsDeque.clear();

        System.out.println("Frase:" + message);
        System.out.println("Longitud: " + message.length() + " caracteres");

        String[] words = message.split(" ");
        System.out.println("Palabras a procesar: " + words.length);

        for (String word : words) {
            encryptWord(word);
        }
    }
}
