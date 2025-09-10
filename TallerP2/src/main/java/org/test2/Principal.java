package org.test2;

public class Principal {

    public static void main(String[] args) {
        Words words = new Words();

        String message = "HOLA MUNDO";

        words.encryptMessage(message);

        words.showEncryptedWords();

    }
}
