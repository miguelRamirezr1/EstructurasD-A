package org.test2;

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

        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            int ascii = (int) c;
            int encrypted = ascii + oddCounter;

            System.out.println(c + " (" + ascii + ") + " + oddCounter + " → " + encrypted);

            encryptedList.insert(encrypted);
            oddCounter += 2;
        }

        System.out.println("Lista después del Paso 1: " + encryptedList);

        encryptedList.swapAdjacentNodes();
        System.out.println("Lista después del Paso 2 (nodos intercambiados): " + encryptedList);

        wordsDeque.add(encryptedList);
        System.out.println("Palabra encriptada almacenada en ArrayDeque");
    }

    public void encryptMessage(String message) {
        String[] words = message.split(" ");
        for (String word : words) {
            encryptWord(word);
        }
        System.out.println("\n=== Proceso de encriptación completado ===");
        System.out.println("Total de palabras encriptadas: " + wordsDeque.size());
    }

    public void showEncryptedWords() {
        System.out.println("\nPalabras Encriptadas en ArrayDeque");
        int index = 1;
        for (LinkedList list : wordsDeque) {
            System.out.println("Palabra " + index + ": " + list);
            index++;
        }
    }
}
