package jogo.appstate;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import jogo.gameobject.character.Player;
import jogo.persistence.HighscoreService;
import jogo.persistence.SaveData;
import jogo.persistence.SaveException;
import jogo.persistence.SaveService;

/**
 * AppState responsável por gerir operações de Save/Load e Highscores.
 * Processa inputs F5 (guardar), F9 (carregar), H (highscores).
 */
public class PersistenceAppState extends BaseAppState {

    private final InputAppState input;
    private final PlayerAppState playerAppState;
    private final HudAppState hud;
    private final Camera cam;

    /** Seed do mundo (para guardar e restaurar). */
    private long worldSeed = System.currentTimeMillis();

    /** Nome do ficheiro de save. */
    private static final String SAVE_FILENAME = "game.sav";

    public PersistenceAppState(InputAppState input, PlayerAppState playerAppState, HudAppState hud, Camera cam) {
        this.input = input;
        this.playerAppState = playerAppState;
        this.hud = hud;
        this.cam = cam;
    }

    /**
     * Define a seed do mundo (chamado pelo WorldAppState durante inicialização).
     */
    public void setWorldSeed(long seed) {
        this.worldSeed = seed;
    }

    @Override
    protected void initialize(Application app) {
        // Nada a inicializar
    }

    @Override
    public void update(float tpf) {
        // Processar pedido de Save (F5)
        if (input.consumeSaveRequested()) {
            performSave();
        }

        // Processar pedido de Load (F9)
        if (input.consumeLoadRequested()) {
            performLoad();
        }

        // Processar pedido de Highscores (H)
        if (input.consumeHighscoresRequested()) {
            showHighscores();
        }
    }

    private void performSave() {
        if (playerAppState == null || playerAppState.getPlayer() == null) {
            hud.showStatus("Erro: Player não disponível!", 2.0f);
            return;
        }

        Player player = playerAppState.getPlayer();
        Vector3f pos = cam.getLocation();

        try {
            SaveService.save(pos.x, pos.y, pos.z, player, worldSeed, SAVE_FILENAME);
            hud.showStatus("Jogo guardado! (F9 para carregar)", 3.0f);
        } catch (SaveException e) {
            System.err.println("[PersistenceAppState] Erro ao guardar: " + e.getMessage());
            hud.showStatus("Erro ao guardar: " + e.getMessage(), 3.0f);
        }
    }

    private void performLoad() {
        if (playerAppState == null || playerAppState.getPlayer() == null) {
            hud.showStatus("Erro: Player não disponível!", 2.0f);
            return;
        }

        if (!SaveService.saveExists(SAVE_FILENAME)) {
            hud.showStatus("Nenhum save encontrado!", 2.0f);
            return;
        }

        try {
            SaveData data = SaveService.load(SAVE_FILENAME);
            Player player = playerAppState.getPlayer();
            SaveService.applyToPlayer(data, player);
            playerAppState.teleportTo(data.getPlayerX(), data.getPlayerY(), data.getPlayerZ());
            hud.showStatus("Jogo carregado!", 3.0f);
        } catch (SaveException e) {
            System.err.println("[PersistenceAppState] Erro ao carregar: " + e.getMessage());
            hud.showStatus("Erro ao carregar: " + e.getMessage(), 3.0f);
        }
    }

    private void showHighscores() {
        Player player = playerAppState != null ? playerAppState.getPlayer() : null;
        StringBuilder sb = new StringBuilder();

        if (player != null) {
            sb.append("Score Atual: ").append(player.getScore())
                    .append(" (").append(player.getBlocksMined()).append(" blocos, ")
                    .append(player.getEnemiesDefeated()).append(" inimigos)\n\n");
        }

        var entries = HighscoreService.getAll();
        if (entries.isEmpty()) {
            sb.append("Top 10: (vazio)");
        } else {
            sb.append("=== TOP 10 ===\n");
            int rank = 1;
            for (var e : entries) {
                sb.append(rank++).append(". ").append(e.getPlayerName())
                        .append(": ").append(e.getScore()).append(" pts\n");
            }
        }

        hud.showStatus(sb.toString(), 5.0f);
    }

    /**
     * Submete a pontuação atual do jogador aos highscores.
     * Chamado quando o jogo termina ou jogador morre.
     * 
     * @param playerName nome do jogador
     * @return true se qualificou para top 10
     */
    public boolean submitScore(String playerName) {
        Player player = playerAppState != null ? playerAppState.getPlayer() : null;
        if (player == null)
            return false;

        boolean qualified = HighscoreService.addScore(playerName,
                player.getBlocksMined(), player.getEnemiesDefeated());

        if (qualified) {
            hud.showStatus("NOVO HIGHSCORE! Score: " + player.getScore(), 4.0f);
        }
        return qualified;
    }

    @Override
    protected void cleanup(Application app) {
    }

    @Override
    protected void onEnable() {
    }

    @Override
    protected void onDisable() {
    }
}
