package org.test7;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;
import org.openjdk.jol.vm.VM;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;
import java.util.ArrayDeque;

public class PerformanceAnalyzer {
    private static final Logger logger = LogManager.getLogger(PerformanceAnalyzer.class);

    // OSHI components
    private SystemInfo systemInfo;
    private CentralProcessor processor;
    private GlobalMemory memory;
    private OperatingSystem os;
    private OSProcess procesoInicio;
    private OSProcess procesoFinal;
    private long[] ticksIniciales;

    // Métricas
    private long tiempoInicio;
    private long tiempoFinal;
    private long memoriaFisicaInicial;
    private long memoriaFisicaFinal;
    private double cpuInicial;
    private double cpuFinal;

    public PerformanceAnalyzer() {
        // Inicializar OSHI
        systemInfo = new SystemInfo();
        processor = systemInfo.getHardware().getProcessor();
        memory = systemInfo.getHardware().getMemory();
        os = systemInfo.getOperatingSystem();

        logger.info("[PERFORMANCE] Sistema inicializado: {}", os.toString());
        logger.info("[PERFORMANCE] Procesador: {} - {} cores físicos, {} lógicos",
                processor.getProcessorIdentifier().getName(),
                processor.getPhysicalProcessorCount(),
                processor.getLogicalProcessorCount());
    }

    public void iniciarMonitoreo(String proceso) {
        logger.info("\n INICIANDO MONITOREO: {}", proceso);

        // Información de JOL
        logger.info("[JOL] Información de la VM:");
        logger.info(VM.current().details());

        // Métricas iniciales
        tiempoInicio = System.nanoTime();
        memoriaFisicaInicial = memory.getTotal() - memory.getAvailable();
        ticksIniciales = processor.getSystemCpuLoadTicks();
        procesoInicio = os.getProcess(os.getProcessId());
        cpuInicial = processor.getSystemCpuLoadBetweenTicks(ticksIniciales) * 100;

        logger.info("[PERFORMANCE] Memoria física inicial: {} MB",
                bytesToMB(memoriaFisicaInicial));
        logger.info("[PERFORMANCE] CPU inicial del sistema: {}%", cpuInicial);
        logger.info("[PERFORMANCE] Hilos activos: {}", procesoInicio.getThreadCount());
    }

    public void finalizarMonitoreo(String proceso) {
        tiempoFinal = System.nanoTime();
        memoriaFisicaFinal = memory.getTotal() - memory.getAvailable();
        procesoFinal = os.getProcess(os.getProcessId());
        cpuFinal = processor.getSystemCpuLoadBetweenTicks(ticksIniciales) * 100;

        // Cálculo del incremento de CPU del proceso (como en CPUproceso.txt)
        double porcentajeCpuProceso = procesoFinal.getProcessCpuLoadBetweenTicks(procesoInicio) * 100.0;

        long tiempoTranscurrido = (tiempoFinal - tiempoInicio) / 1_000_000; // en ms
        long diferenciaMemoria = memoriaFisicaFinal - memoriaFisicaInicial;
        double diferenciaCpu = cpuFinal - cpuInicial;

        logger.info("\nRESULTADOS MONITOREO: {}", proceso);
        logger.info("[TIEMPO] Tiempo de ejecución: {} ms", tiempoTranscurrido);
        logger.info("[PERFORMANCE] Memoria física final: {} MB", bytesToMB(memoriaFisicaFinal));
        logger.info("[PERFORMANCE] Incremento memoria física: {} MB", bytesToMB(diferenciaMemoria));
        logger.info("[PERFORMANCE] CPU final del sistema: {}%", cpuFinal);
        logger.info("[PERFORMANCE] Incremento CPU del sistema: {}%", diferenciaCpu);
        logger.info("[PERFORMANCE] Incremento CPU del Proceso: {}%", porcentajeCpuProceso);
        logger.info("[PERFORMANCE] Hilos finales: {}", procesoFinal.getThreadCount());
        logger.info("[PERFORMANCE] Tiempo CPU del proceso: {} ms",
                procesoFinal.getKernelTime() + procesoFinal.getUserTime());
    }

    public void analizarObjetosConJOL(Words words) {
        logger.info("\nANÁLISIS JOL DE ESTRUCTURAS");

        // Análisis de la clase Words
        logger.info("[JOL] Layout de la clase Words:");
        logger.info(ClassLayout.parseInstance(words).toPrintable());

        // Análisis del ArrayDeque
        ArrayDeque<LinkedList> deque = words.getWordsDeque();
        if (!deque.isEmpty()) {
            logger.info("[JOL] Layout del ArrayDeque:");
            logger.info(ClassLayout.parseInstance(deque).toPrintable());

            // Análisis de una LinkedList
            LinkedList firstList = deque.peek();
            if (firstList != null) {
                logger.info("[JOL] Layout de LinkedList:");
                logger.info(ClassLayout.parseInstance(firstList).toPrintable());

                // Análisis de un Node
                Node head = firstList.getHead();
                if (head != null) {
                    logger.info("[JOL] Layout de Node:");
                    logger.info(ClassLayout.parseInstance(head).toPrintable());
                }
            }
        }

        // Análisis del grafo completo de objetos
        logger.info("\n[JOL] Análisis del grafo completo de objetos:");
        GraphLayout graph = GraphLayout.parseInstance(words);
        logger.info("Tamaño total: {} bytes ({} MB)",
                graph.totalSize(),
                graph.totalSize() / (1024.0 * 1024.0));
        logger.info("Número de objetos: {}", graph.totalCount());
        logger.info("\nDesglose por clase:");
        logger.info(graph.toFootprint());
    }

    private double bytesToMB(long bytes) {
        return bytes / (1024.0 * 1024.0);
    }
}
