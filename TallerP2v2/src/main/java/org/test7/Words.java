package org.test7;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

// La clase Words es la clase obligatoria "Frase"
// que centraliza el flujo de los datos!!!

public class Words {
    private static final Logger logger = LogManager.getLogger(Words.class);
    private LinkedList encryptedList;
    private ArrayDeque<LinkedList> wordsDeque;
    private int oddCounter;
    private String textoOriginal;
    private String textoEncriptado;

    private long startTime;
    private long endTime;
    private Runtime runtime = Runtime.getRuntime();

    public Words() {
        this.encryptedList = new LinkedList();
        this.wordsDeque = new ArrayDeque<>();
        this.oddCounter = 1;
        logger.info("[SISTEMA] Instancia de Words creada");
    }

    public void encryptWord(String word) {
        long wordStartTime = System.nanoTime();
        logger.info("[SISTEMA] Procesando palabra: {}", word);

        encryptedList = new LinkedList();
        oddCounter = 1;

        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            int ascii = (int) c;
            int encrypted = ascii + oddCounter;

            logger.debug("[SISTEMA] {} ({}) + {} -> {}", c, ascii, oddCounter, encrypted);

            encryptedList.insert(encrypted);
            oddCounter += 2;
        }

        logger.info("[SISTEMA] Nodos orden original: {}", encryptedList);

        encryptedList.swapAdjacentNodes();
        logger.info("[SISTEMA] Nodos intercambiados: {}", encryptedList);

        wordsDeque.add(encryptedList);

        long wordEndTime = System.nanoTime();
        double wordTimeMs = (wordEndTime - wordStartTime) / 1_000_000.0;
        logger.info("[TIEMPO] Palabra '{}' encriptada en {} ms", word, wordTimeMs);
    }

    public void encryptMessage(String message) {
        if (message == null) {
            logger.error("[SISTEMA] El mensaje a encriptar es null");
            return;
        }

        startTime = System.nanoTime();
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        logger.info("[SISTEMA] Iniciando encriptación de mensaje completo");
        logger.info("[PERFORMANCE] Memoria antes de encriptar: {} bytes", memoryBefore);

        this.textoOriginal = message;
        wordsDeque.clear();

        logger.info("[SISTEMA] Frase: {}", message);
        logger.info("[SISTEMA] Longitud: {} caracteres", message.length());

        String[] words = message.split(" ");
        logger.info("[SISTEMA] Palabras a procesar: {}", words.length);

        for (String word : words) {
            encryptWord(word);
        }

        construirTextoEncriptado();

        endTime = System.nanoTime();
        double totalTimeMs = (endTime - startTime) / 1_000_000.0;
        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsed = memoryAfter - memoryBefore;

        logger.info("[TIEMPO] Encriptación completada en {} ms", totalTimeMs);
        logger.info("[PERFORMANCE] Memoria después de encriptar: {} bytes", memoryAfter);
        logger.info("[PERFORMANCE] Memoria utilizada: {} bytes", memoryUsed);
        logger.info("[SISTEMA] Encriptación de mensaje completada exitosamente");
    }

    private void construirTextoEncriptado() {
        StringBuilder sb = new StringBuilder();
        for (LinkedList list : wordsDeque) {
            sb.append(list.toString()).append(" | ");
        }
        if (sb.length() > 3) {
            sb.setLength(sb.length() - 3);
        }
        this.textoEncriptado = sb.toString();
        logger.debug("[SISTEMA] Texto encriptado construido: {}", textoEncriptado);
    }

    public String decryptMessage() {
        logger.info("[SISTEMA] INICIANDO PROCESO DE DESENCRIPTACIÓN");
        startTime = System.nanoTime();
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        logger.info("[PERFORMANCE] Memoria antes de desencriptar: {} bytes", memoryBefore);

        if (wordsDeque.isEmpty()) {
            logger.warn("[SISTEMA] No hay mensaje encriptado para desencriptar");
            return "";
        }

        logger.info("[SISTEMA] PROCESO DE DESENCRIPTACIÓN ==");
        logger.info("[SISTEMA] Texto encriptado: {}", textoEncriptado);

        List<String> palabrasDesencriptadas = new ArrayList<>();
        int wordIndex = 1;

        for (LinkedList listaEncriptada : wordsDeque) {
            logger.info("[SISTEMA] Desencriptando palabra {}", wordIndex);
            long wordStartTime = System.nanoTime();

            LinkedList listaCopia = copiarLista(listaEncriptada);

            logger.debug("[SISTEMA] Estado encriptado: {}", listaCopia);

            listaCopia.swapAdjacentNodes();
            logger.debug("[SISTEMA] Después de revertir intercambio: {}", listaCopia);

            String palabraDesencriptada = restarNumerosImpares(listaCopia);
            logger.info("[SISTEMA] Palabra desencriptada: \"{}\"", palabraDesencriptada);

            palabrasDesencriptadas.add(palabraDesencriptada);

            long wordEndTime = System.nanoTime();
            double wordTimeMs = (wordEndTime - wordStartTime) / 1_000_000.0;
            logger.info("[TIEMPO] Palabra {} desencriptada en {} ms", wordIndex, wordTimeMs);

            wordIndex++;
        }

        String mensajeDesencriptado = String.join(" ", palabrasDesencriptadas);

        endTime = System.nanoTime();
        double totalTimeMs = (endTime - startTime) / 1_000_000.0;
        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsed = memoryAfter - memoryBefore;

        logger.info("[SISTEMA] RESULTADO DE DESENCRIPTACIÓN");
        logger.info("[SISTEMA] Mensaje original guardado: \"{}\"", textoOriginal);
        logger.info("[SISTEMA] Mensaje desencriptado:     \"{}\"", mensajeDesencriptado);
        logger.info("[SISTEMA] ¿Coinciden? {}", (textoOriginal.equals(mensajeDesencriptado) ? "✓ SÍ" : "✗ NO"));

        logger.info("[TIEMPO] Desencriptación completada en {} ms", totalTimeMs);
        logger.info("[PERFORMANCE] Memoria después de desencriptar: {} bytes", memoryAfter);
        logger.info("[PERFORMANCE] Memoria utilizada en desencriptación: {} bytes", memoryUsed);
        logger.info("[SISTEMA] DESENCRIPTACIÓN COMPLETADA");

        return mensajeDesencriptado;
    }

    private LinkedList copiarLista(LinkedList original) {
        LinkedList copia = new LinkedList();
        Node current = original.getHead();
        while (current != null) {
            copia.insert(current.getData());
            current = current.getNext();
        }
        return copia;
    }

    private String restarNumerosImpares(LinkedList lista) {
        StringBuilder palabra = new StringBuilder();
        Node current = lista.getHead();
        int oddCounter = 1;

        while (current != null) {
            int valorEncriptado = current.getData();
            int valorOriginal = valorEncriptado - oddCounter;
            char caracterOriginal = (char) valorOriginal;

            logger.debug("[SISTEMA] Desencriptando: {} - {} = {} ('{}')",
                    valorEncriptado, oddCounter, valorOriginal, caracterOriginal);

            palabra.append(caracterOriginal);
            oddCounter += 2;
            current = current.getNext();
        }

        return palabra.toString();
    }

    public ArrayDeque<LinkedList> getWordsDeque() {
        return wordsDeque;
    }
}
