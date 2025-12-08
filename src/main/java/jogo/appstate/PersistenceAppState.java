package jogo.appstate;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import jogo.gameobject.character.Player;
import jogo.persistence.SaveData;
import jogo.persistence.SaveException;
import jogo.persistence.SaveService;

/**
 * AppState responsável por gerir operações de Save/Load.
 * Processa inputs F5 (guardar) e F9 (carregar) e coordena a persistência do
 * estado do jogo.
 */
public class PersistenceAppState extends BaseAppState {

    private final InputAppState input;
    private final PlayerAppState playerAppState;
    private final HudAppState hud;
    private final Camera cam;

    /** Seed do mundo (para guardar e restaurar). */
    private long worldSeed = System.currentTimeMillis(); // Seed por omissão

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

            // Aplicar dados ao jogador
            Player player = playerAppState.getPlayer();
            SaveService.applyToPlayer(data, player);

            // Teleportar jogador para posição guardada
            // Nota: Isto requer um método no PlayerAppState para teleportar
            playerAppState.teleportTo(data.getPlayerX(), data.getPlayerY(), data.getPlayerZ());

            hud.showStatus("Jogo carregado!", 3.0f);
        } catch (SaveException e) {
            System.err.println("[PersistenceAppState] Erro ao carregar: " + e.getMessage());
            hud.showStatus("Erro ao carregar: " + e.getMessage(), 3.0f);
        }
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
