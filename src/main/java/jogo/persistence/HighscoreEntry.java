package jogo.persistence;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa uma entrada de highscore com nome do jogador, pontuação e
 * timestamp.
 * Implementa Comparable para ordenação por pontuação descendente.
 */
public class HighscoreEntry implements Comparable<HighscoreEntry> {

    /** Nome do jogador. */
    private final String playerName;

    /** Pontuação total (blocos minerados + inimigos derrotados). */
    private final int score;

    /** Blocos minerados. */
    private final int blocksMined;

    /** Inimigos derrotados. */
    private final int enemiesDefeated;

    /** Timestamp quando o score foi registado. */
    private final LocalDateTime timestamp;

    /** Formato para serialização do timestamp. */
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Cria uma nova entrada de highscore.
     * 
     * @param playerName      nome do jogador
     * @param blocksMined     número de blocos minerados
     * @param enemiesDefeated número de inimigos derrotados
     */
    public HighscoreEntry(String playerName, int blocksMined, int enemiesDefeated) {
        this.playerName = playerName != null ? playerName : "Unknown";
        this.blocksMined = Math.max(0, blocksMined);
        this.enemiesDefeated = Math.max(0, enemiesDefeated);
        this.score = this.blocksMined + (this.enemiesDefeated * 10); // Inimigos valem mais
        this.timestamp = LocalDateTime.now();
    }

    /**
     * Construtor para deserialização com timestamp específico.
     */
    public HighscoreEntry(String playerName, int blocksMined, int enemiesDefeated, LocalDateTime timestamp) {
        this.playerName = playerName != null ? playerName : "Unknown";
        this.blocksMined = Math.max(0, blocksMined);
        this.enemiesDefeated = Math.max(0, enemiesDefeated);
        this.score = this.blocksMined + (this.enemiesDefeated * 10);
        this.timestamp = timestamp != null ? timestamp : LocalDateTime.now();
    }

    // Getters

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }

    public int getBlocksMined() {
        return blocksMined;
    }

    public int getEnemiesDefeated() {
        return enemiesDefeated;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Devolve o timestamp formatado como string para serialização.
     */
    public String getTimestampString() {
        return timestamp.format(FORMAT);
    }

    /**
     * Cria um HighscoreEntry a partir de uma string de timestamp.
     */
    public static LocalDateTime parseTimestamp(String timestampStr) {
        return LocalDateTime.parse(timestampStr, FORMAT);
    }

    /**
     * Ordenação por pontuação descendente (maior primeiro).
     */
    @Override
    public int compareTo(HighscoreEntry other) {
        // Ordem inversa para que maior pontuação venha primeiro
        return Integer.compare(other.score, this.score);
    }

    @Override
    public String toString() {
        return String.format("%s: %d pts (%d blocos, %d inimigos) - %s",
                playerName, score, blocksMined, enemiesDefeated,
                timestamp.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
    }

    /**
     * Serializa para formato de linha de ficheiro.
     */
    public String toFileLine() {
        return playerName + "|" + blocksMined + "|" + enemiesDefeated + "|" + getTimestampString();
    }

    /**
     * Deserializa de uma linha de ficheiro.
     */
    public static HighscoreEntry fromFileLine(String line) throws IllegalArgumentException {
        String[] parts = line.split("\\|");
        if (parts.length != 4) {
            throw new IllegalArgumentException("Formato de linha inválido: " + line);
        }
        String name = parts[0];
        int blocks = Integer.parseInt(parts[1]);
        int enemies = Integer.parseInt(parts[2]);
        LocalDateTime ts = parseTimestamp(parts[3]);
        return new HighscoreEntry(name, blocks, enemies, ts);
    }
}
