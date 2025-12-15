package jogo.persistence;

import java.util.*;

/**
 * Fila de prioridade limitada para highscores.
 * Mantém apenas as N melhores pontuações ordenadas por score descendente.
 * Utiliza java.util.PriorityQueue internamente conforme requisito do projeto.
 */
public class HighscoreQueue {

    /** Número máximo de entradas a manter. */
    private static final int MAX_ENTRIES = 10;

    /** Fila de prioridade com ordenação natural (score descendente). */
    private final PriorityQueue<HighscoreEntry> queue;

    /**
     * Cria uma nova fila de highscores vazia.
     */
    public HighscoreQueue() {
        // Ordenação natural de HighscoreEntry é por score descendente
        this.queue = new PriorityQueue<>();
    }

    /**
     * Adiciona uma entrada à fila.
     * Se a fila já tiver MAX_ENTRIES, remove a pior se a nova for melhor.
     * 
     * @param entry entrada a adicionar
     * @return true se a entrada foi adicionada, false se não qualificou
     */
    public boolean add(HighscoreEntry entry) {
        if (entry == null)
            return false;

        if (queue.size() < MAX_ENTRIES) {
            queue.add(entry);
            return true;
        }

        // Fila cheia - verificar se nova entrada é melhor que a pior
        // Precisamos encontrar a pior entrada (maior valor compareTo = menor score)
        HighscoreEntry worst = findWorst();
        if (worst != null && entry.compareTo(worst) < 0) {
            // Nova entrada é melhor que a pior
            queue.remove(worst);
            queue.add(entry);
            return true;
        }

        return false;
    }

    /**
     * Encontra a entrada com pior pontuação.
     */
    private HighscoreEntry findWorst() {
        HighscoreEntry worst = null;
        for (HighscoreEntry e : queue) {
            if (worst == null || e.compareTo(worst) > 0) {
                worst = e;
            }
        }
        return worst;
    }

    /**
     * Devolve todas as entradas ordenadas por pontuação descendente.
     */
    public List<HighscoreEntry> getAll() {
        List<HighscoreEntry> list = new ArrayList<>(queue);
        Collections.sort(list); // Usa compareTo natural (descendente)
        return list;
    }

    /**
     * Devolve o número de entradas na fila.
     */
    public int size() {
        return queue.size();
    }

    /**
     * Verifica se a fila está vazia.
     */
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    /**
     * Limpa todas as entradas.
     */
    public void clear() {
        queue.clear();
    }

    /**
     * Devolve a melhor pontuação (topo do ranking).
     */
    public HighscoreEntry getBest() {
        if (queue.isEmpty())
            return null;
        return queue.peek();
    }

    /**
     * Verifica se uma pontuação qualificaria para o top 10.
     */
    public boolean wouldQualify(int score) {
        if (queue.size() < MAX_ENTRIES)
            return true;
        HighscoreEntry worst = findWorst();
        return worst != null && score > worst.getScore();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("=== HIGHSCORES ===\n");
        List<HighscoreEntry> sorted = getAll();
        int rank = 1;
        for (HighscoreEntry e : sorted) {
            sb.append(String.format("%2d. %s\n", rank++, e.toString()));
        }
        if (sorted.isEmpty()) {
            sb.append("(Sem pontuações registadas)\n");
        }
        return sb.toString();
    }
}
