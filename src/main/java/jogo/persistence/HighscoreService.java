package jogo.persistence;

import java.io.*;
import java.nio.file.*;
import java.util.List;

/**
 * Serviço para gestão de highscores: adicionar, guardar e carregar.
 */
public final class HighscoreService {

    /** Diretório onde os highscores são guardados. */
    private static final String SAVE_DIR = "saves";

    /** Nome do ficheiro de highscores. */
    private static final String HIGHSCORE_FILE = "highscores.txt";

    /** Fila de highscores em memória. */
    private static final HighscoreQueue queue = new HighscoreQueue();

    /** Indica se os highscores já foram carregados do disco. */
    private static boolean loaded = false;

    /** Construtor privado - classe utilitária. */
    private HighscoreService() {
    }

    /**
     * Adiciona uma nova entrada de highscore.
     * Guarda automaticamente para o disco se qualificar.
     * 
     * @param entry entrada a adicionar
     * @return true se a entrada foi adicionada ao top 10
     */
    public static boolean addScore(HighscoreEntry entry) {
        ensureLoaded();
        boolean added = queue.add(entry);
        if (added) {
            try {
                save();
                System.out.println("[HighscoreService] Nova entrada adicionada: " + entry);
            } catch (SaveException e) {
                System.err.println("[HighscoreService] Erro ao guardar highscores: " + e.getMessage());
            }
        }
        return added;
    }

    /**
     * Adiciona highscore com dados do jogador.
     * 
     * @param playerName      nome do jogador
     * @param blocksMined     blocos minerados
     * @param enemiesDefeated inimigos derrotados
     * @return true se qualificou para o top 10
     */
    public static boolean addScore(String playerName, int blocksMined, int enemiesDefeated) {
        return addScore(new HighscoreEntry(playerName, blocksMined, enemiesDefeated));
    }

    /**
     * Devolve a fila de highscores.
     */
    public static HighscoreQueue getQueue() {
        ensureLoaded();
        return queue;
    }

    /**
     * Devolve todas as entradas ordenadas.
     */
    public static List<HighscoreEntry> getAll() {
        ensureLoaded();
        return queue.getAll();
    }

    /**
     * Verifica se uma pontuação qualificaria para o top 10.
     */
    public static boolean wouldQualify(int score) {
        ensureLoaded();
        return queue.wouldQualify(score);
    }

    /**
     * Garante que os highscores foram carregados do disco.
     */
    private static void ensureLoaded() {
        if (!loaded) {
            try {
                load();
            } catch (SaveException e) {
                System.out.println("[HighscoreService] Sem ficheiro de highscores existente, começando vazio.");
            }
            loaded = true;
        }
    }

    /**
     * Guarda os highscores para o disco.
     */
    public static void save() throws SaveException {
        Path saveDir = Paths.get(SAVE_DIR);
        try {
            Files.createDirectories(saveDir);
        } catch (IOException e) {
            throw new SaveException("Não foi possível criar diretório de saves", e);
        }

        Path filePath = saveDir.resolve(HIGHSCORE_FILE);

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath.toFile()))) {
            writer.println("# IscteCraft Highscores");
            writer.println("# Formato: nome|blocos|inimigos|timestamp");
            for (HighscoreEntry entry : queue.getAll()) {
                writer.println(entry.toFileLine());
            }
            System.out.println("[HighscoreService] Highscores guardados em: " + filePath.toAbsolutePath());
        } catch (IOException e) {
            throw new SaveException("Erro ao guardar highscores: " + e.getMessage(), e);
        }
    }

    /**
     * Carrega os highscores do disco.
     */
    public static void load() throws SaveException {
        Path filePath = Paths.get(SAVE_DIR, HIGHSCORE_FILE);

        if (!Files.exists(filePath)) {
            throw new SaveException("Ficheiro de highscores não encontrado");
        }

        queue.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line;
            int lineNum = 0;
            while ((line = reader.readLine()) != null) {
                lineNum++;
                line = line.trim();

                // Ignorar comentários e linhas vazias
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                try {
                    HighscoreEntry entry = HighscoreEntry.fromFileLine(line);
                    queue.add(entry);
                } catch (Exception e) {
                    System.err.println("[HighscoreService] Aviso: linha " + lineNum + " inválida: " + line);
                }
            }
            System.out.println("[HighscoreService] Carregados " + queue.size() + " highscores");
        } catch (IOException e) {
            throw new SaveException("Erro ao ler highscores: " + e.getMessage(), e);
        }
    }

    /**
     * Devolve uma representação em string dos highscores para display.
     */
    public static String getDisplayString() {
        ensureLoaded();
        return queue.toString();
    }
}
