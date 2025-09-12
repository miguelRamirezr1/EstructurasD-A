package org.test7;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.ArrayList;
import java.util.List;

public class ComplexityAnalysis {
    private static final Logger logger = LogManager.getLogger(ComplexityAnalysis.class);

    public void analyzeComplexity(List<Quote> quotes) {
        logger.info("ANÁLISIS DE COMPLEJIDAD BIG O - ENCRIPTACIÓN");

        if (quotes == null || quotes.isEmpty()) {
            logger.error("[ANÁLISIS] No hay quotes disponibles del API");
            return;
        }

        logger.info("\n[ANÁLISIS] Ejecutando análisis con frases del API ZenQuotes...\n");
        logger.info("[ANÁLISIS] Total de frases disponibles: {}", quotes.size());

        // Ordenar quotes por longitud para análisis progresivo
        List<Quote> sortedQuotes = new ArrayList<>(quotes);
        sortedQuotes.sort((q1, q2) -> Integer.compare(q1.getQuote().length(), q2.getQuote().length()));

        // Seleccionar hasta 10 frases distribuidas por tamaño
        List<Quote> selectedQuotes = selectQuotesForAnalysis(sortedQuotes);
        List<MeasurementData> measurements = new ArrayList<>();

        logger.info("[ANÁLISIS] Frases seleccionadas para análisis: {}", selectedQuotes.size());
        logger.info("[ANÁLISIS] Rango de tamaños: {} a {} caracteres\n",
                selectedQuotes.getFirst().getQuote().length(),
                selectedQuotes.getLast().getQuote().length());

        // Procesar cada frase seleccionada
        for (int i = 0; i < selectedQuotes.size(); i++) {
            Quote quote = selectedQuotes.get(i);
            String message = quote.getQuote();

            logger.info("[ANÁLISIS] Procesando frase {} de {}", i+1, selectedQuotes.size());
            logger.info("  Autor: {}", quote.getAuthor());
            logger.info("  Longitud: {} caracteres", message.length());
            logger.info("  Palabras: {}", message.split(" ").length);

            MeasurementData data = measurePerformance(message, i+1);
            measurements.add(data);

            logger.info("  Tiempo total: {} ms", String.format("%.3f", data.totalTime / 1_000_000.0));
            logger.info("  Verificación: {}\n", data.isCorrect ? "✓ Correcta" : "✗ Error");
        }

        // Mostrar resumen de mediciones
        showMeasurementSummary(measurements);

        // Análisis teórico Big O
        performBigOAnalysis();

        // Verificación empírica de linealidad
        verifyLinearComplexity(measurements);
    }

    private List<Quote> selectQuotesForAnalysis(List<Quote> sortedQuotes) {
        List<Quote> selected = new ArrayList<>();
        int totalQuotes = sortedQuotes.size();
        int targetCount = Math.min(10, totalQuotes);

        if (totalQuotes <= 2) {
            return new ArrayList<>(sortedQuotes);
        }

        // Seleccionar quotes distribuidas uniformemente por índice
        double step = (double) totalQuotes / targetCount;
        for (int i = 0; i < targetCount; i++) {
            int index = (int) (i * step);
            selected.add(sortedQuotes.get(index));
        }

        return selected;
    }

    private MeasurementData measurePerformance(String message, int index) {
        int inputSize = message.length();
        int numWords = message.split(" ").length;

        Words words = new Words();
        Runtime runtime = Runtime.getRuntime();

        // Limpiar memoria para medición más precisa
        System.gc();
        try { Thread.sleep(30); } catch (InterruptedException e) {}

        long memBefore = runtime.totalMemory() - runtime.freeMemory();

        // Medir tiempo de encriptación
        long encryptStart = System.nanoTime();
        words.encryptMessage(message);
        long encryptEnd = System.nanoTime();
        long encryptTime = encryptEnd - encryptStart;

        // Medir tiempo de desencriptación
        long decryptStart = System.nanoTime();
        String decrypted = words.decryptMessage();
        long decryptEnd = System.nanoTime();
        long decryptTime = decryptEnd - decryptStart;

        long totalTime = encryptTime + decryptTime;
        long memAfter = runtime.totalMemory() - runtime.freeMemory();
        double memoryUsedMB = (memAfter - memBefore) / (1024.0 * 1024.0);

        // Verificar corrección
        boolean isCorrect = message.equals(decrypted);

        return new MeasurementData(inputSize, numWords, encryptTime, decryptTime, memoryUsedMB, isCorrect);
    }

    private void showMeasurementSummary(List<MeasurementData> measurements) {
        logger.info("\nRESUMEN DE MEDICIONES CON FRASES DEL API");

        // Estadísticas generales
        double avgEncryptTime = measurements.stream()
                .mapToLong(m -> m.encryptTime)
                .average().orElse(0) / 1_000_000.0;

        double avgDecryptTime = measurements.stream()
                .mapToLong(m -> m.decryptTime)
                .average().orElse(0) / 1_000_000.0;

        int minChars = measurements.stream()
                .mapToInt(m -> m.inputSize)
                .min().orElse(0);

        int maxChars = measurements.stream()
                .mapToInt(m -> m.inputSize)
                .max().orElse(0);

        long correctCount = measurements.stream()
                .filter(m -> m.isCorrect)
                .count();

        logger.info("Frases procesadas: {}", measurements.size());
        logger.info("Rango de caracteres: {} - {}", minChars, maxChars);
        logger.info("Tiempo promedio encriptación: {} ms", String.format("%.3f", avgEncryptTime));
        logger.info("Tiempo promedio desencriptación: {} ms", String.format("%.3f", avgDecryptTime));
        logger.info("Verificación correcta: {}/{}", correctCount, measurements.size());

        // Mostrar datos individuales sin tabla formateada
        logger.info("\nDatos por frase:");
        for (int i = 0; i < measurements.size(); i++) {
            MeasurementData data = measurements.get(i);
            logger.info("Frase {}: {} chars, {} palabras, {}ms encrypt, {}ms desenc, {}ms total",
                    i+1, data.inputSize, data.numWords,
                    data.encryptTime / 1_000_000.0,
                    data.decryptTime / 1_000_000.0,
                    data.totalTime / 1_000_000.0);
        }
    }

    private void performBigOAnalysis() {
        logger.info("ANÁLISIS TEÓRICO DE COMPLEJIDAD BIG O");

        logger.info("\nPROCESO DE ENCRIPTACIÓN");
        logger.info("Paso 1 - Conversión ASCII + incremento impar:");
        logger.info("  • Por cada carácter: O(1) operación");
        logger.info("  • Para n caracteres: O(n)");
        logger.info("  • Inserción en lista enlazada al final: O(m) donde m es longitud de palabra");

        logger.info("\nPaso 2 - Intercambio de nodos adyacentes:");
        logger.info("  • Recorrer lista de m nodos: O(m)");
        logger.info("  • Por cada palabra de longitud m: O(m)");

        logger.info("\nPaso 3 - Almacenamiento en ArrayDeque:");
        logger.info("  • Inserción en ArrayDeque: O(1) amortizado");

        logger.info("\nCOMPLEJIDAD TOTAL DE ENCRIPTACIÓN:");
        logger.info("  • Por palabra de longitud m: O(m) + O(m) + O(1) = O(m)");
        logger.info("  • Para w palabras con longitud promedio m: O(w * m)");
        logger.info("  • Donde n = w * m (total de caracteres): O(n)");
        logger.info("  ✅ COMPLEJIDAD ENCRIPTACIÓN: O(n) - LINEAL");

        logger.info("\nPROCESO DE DESENCRIPTACIÓN");
        logger.info("Paso 1 - Extracción del ArrayDeque:");
        logger.info("  • Iteración sobre w palabras: O(w)");

        logger.info("\nPaso 2 - Revertir intercambio:");
        logger.info("  • Por cada palabra de longitud m: O(m)");

        logger.info("\nPaso 3 - Restar números impares:");
        logger.info("  • Por cada carácter: O(1)");
        logger.info("  • Para m caracteres: O(m)");

        logger.info("\nCOMPLEJIDAD TOTAL DE DESENCRIPTACIÓN:");
        logger.info("  • Por palabra: O(m) + O(m) + O(m) = O(m)");
        logger.info("  • Para w palabras: O(w * m) = O(n)");
        logger.info("  ✅ COMPLEJIDAD DESENCRIPTACIÓN: O(n) - LINEAL");

        logger.info("COMPLEJIDAD TOTAL DEL ALGORITMO: O(n) - LINEAL");
    }

    private void verifyLinearComplexity(List<MeasurementData> measurements) {
        logger.info("\n VERIFICACIÓN EMPÍRICA DE COMPLEJIDAD LINEAL");

        if (measurements.size() < 2) {
            logger.warn("Insuficientes datos para verificación empírica");
            return;
        }

        logger.info("\nAnálisis de ratios (tiempo/tamaño debe ser ~constante para O(n)):");

        for (int i = 1; i < measurements.size(); i++) {
            MeasurementData prev = measurements.get(i-1);
            MeasurementData curr = measurements.get(i);

            if (prev.inputSize > 0 && prev.totalTime > 0) {
                double sizeRatio = (double) curr.inputSize / prev.inputSize;
                double timeRatio = (double) curr.totalTime / prev.totalTime;
                double efficiency = timeRatio / sizeRatio;

                logger.info("Comparación {} chars vs {} chars:",
                        prev.inputSize, curr.inputSize);
                logger.info("  - Incremento tamaño: {}x", String.format("%.2f", sizeRatio));
                logger.info("  - Incremento tiempo: {}x", String.format("%.2f", timeRatio));
                logger.info("  - Eficiencia: {} {}",
                        String.format("%.2f", efficiency),
                        (efficiency > 0.7 && efficiency < 1.3) ? "(✓ lineal)" : "(⚠ revisar)");
            }
        }

        logger.info("\nAnálisis completado usando exclusivamente frases del API ZenQuotes");
    }

    // Clase interna para datos de medición
    private static class MeasurementData {
        final int inputSize;
        final int numWords;
        final long encryptTime;
        final long decryptTime;
        final long totalTime;
        final double memoryUsedMB;
        final boolean isCorrect;

        MeasurementData(int inputSize, int numWords, long encryptTime,
                        long decryptTime, double memoryUsedMB, boolean isCorrect) {
            this.inputSize = inputSize;
            this.numWords = numWords;
            this.encryptTime = encryptTime;
            this.decryptTime = decryptTime;
            this.totalTime = encryptTime + decryptTime;
            this.memoryUsedMB = memoryUsedMB;
            this.isCorrect = isCorrect;
        }
    }
}