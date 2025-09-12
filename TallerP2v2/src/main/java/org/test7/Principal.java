package org.test7;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;

public class Principal {
    private static final Logger logger = LogManager.getLogger(Principal.class);

    public static void main(String[] args) throws Exception {
        logger.info("[SISTEMA] INICIANDO APLICACIÓN");

        // Inicializar analizador de performance
        PerformanceAnalyzer perfAnalyzer = new PerformanceAnalyzer();

        // Iniciar monitoreo general
        perfAnalyzer.iniciarMonitoreo("APLICACIÓN COMPLETA");

        // Obtener quotes del API
        llamadoApi quoteService = new llamadoApi();
        List<Quote> quotes = quoteService.fetchQuotes();

        Quote firstQuote = quotes.getFirst();
        logger.info("[SISTEMA] Primera frase para análisis detallado:");
        logger.info("[SISTEMA] Autor: {}", firstQuote.getAuthor());
        logger.info("[SISTEMA] Texto: {}", firstQuote.getQuote());

        Words words = new Words();

        // FASE 1: Encriptación con monitoreo
        perfAnalyzer.iniciarMonitoreo("ENCRIPTACIÓN");
        words.encryptMessage(firstQuote.getQuote());
        perfAnalyzer.finalizarMonitoreo("ENCRIPTACIÓN");

        // Análisis JOL de las estructuras
        perfAnalyzer.analizarObjetosConJOL(words);

        // FASE 2: Desencriptación con monitoreo
        perfAnalyzer.iniciarMonitoreo("DESENCRIPTACIÓN");
        String mensajeDesencriptado = words.decryptMessage();
        perfAnalyzer.finalizarMonitoreo("DESENCRIPTACIÓN");

        // Verificar corrección
        if (firstQuote.getQuote().equals(mensajeDesencriptado)) {
            logger.info("[SISTEMA] Desencriptación exitosa - El mensaje coincide");
        } else {
            logger.error("[SISTEMA] Error en desencriptación - Los mensajes no coinciden");
        }

        // Finalizar monitoreo general
        perfAnalyzer.finalizarMonitoreo("APLICACIÓN COMPLETA");

        // FASE 3: Análisis de Complejidad Big O con múltiples frases del API
        logger.info("\n\n");
        ComplexityAnalysis complexityAnalysis = new ComplexityAnalysis();
        complexityAnalysis.analyzeComplexity(quotes);

        logger.info("\n[SISTEMA] APLICACIÓN FINALIZADA ");
    }
}