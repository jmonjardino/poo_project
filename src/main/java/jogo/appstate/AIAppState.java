package jogo.appstate;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Vector3f;
import jogo.engine.GameRegistry;
import jogo.framework.math.Vec3;
import jogo.gameobject.GameObject;
import jogo.gameobject.capability.AIContext;
import jogo.gameobject.capability.HasAI;

/**
 * Ciclo de IA: constrói contexto por frame e invoca updateAI(ctx)
 * nos objetos registados com capacidades de IA.
 */
public class AIAppState extends BaseAppState {
    private final PlayerAppState playerAppState;
    private final WorldAppState gameWorldAppState;
    private final GameRegistry registry;

    /** Construtor com player, mundo e registo de objetos. */
    public AIAppState(PlayerAppState playerAppState, WorldAppState gameWorldAppState, GameRegistry registry) {
        this.playerAppState = playerAppState;
        this.gameWorldAppState = gameWorldAppState;
        this.registry = registry;
    }

    @Override
    protected void initialize(Application app) {
    }

    @Override
    /** Atualização por frame: cria AIContext e entrega ao HasAI.updateAI. */
    public void update(float tpf) {
        // Posição do jogador via câmara (lado engine) convertida para Vec3 (lado jogo)
        Vector3f camLoc = getApplication().getCamera().getLocation();
        Vec3 playerPos = new Vec3(camLoc.x, camLoc.y, camLoc.z);
        double baseSpeed = playerAppState != null ? playerAppState.getMoveSpeed() : 8.0;
        AIContext ctx = new AIContext(playerPos, baseSpeed, tpf, playerAppState.getPlayer(), registry.getAll());

        for (GameObject obj : registry.getAll()) {
            if (obj instanceof HasAI ai) {
                ai.updateAI(ctx);
            }
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
