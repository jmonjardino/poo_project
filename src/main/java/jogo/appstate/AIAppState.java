package jogo.appstate;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Vector3f;
import jogo.engine.GameRegistry;
import jogo.framework.math.Vec3;
import jogo.gameobject.GameObject;
import jogo.gameobject.capability.AIContext;
import jogo.gameobject.capability.HasAI;

public class AIAppState extends BaseAppState {
    private final PlayerAppState playerAppState;
    private final WorldAppState gameWorldAppState;
    private final GameRegistry registry;

    public AIAppState(PlayerAppState playerAppState, WorldAppState gameWorldAppState, GameRegistry registry) {
        this.playerAppState = playerAppState;
        this.gameWorldAppState = gameWorldAppState;
        this.registry = registry;
    }

    @Override
    protected void initialize(Application app) { }

    @Override
    public void update(float tpf) {
        // Acquire player position from camera (engine side) and build a game-side context
        Vector3f camLoc = getApplication().getCamera().getLocation();
        Vec3 playerPos = new Vec3(camLoc.x, camLoc.y, camLoc.z);
        AIContext ctx = new AIContext(playerPos);

        for (GameObject obj : registry.getAll()) {
            if (obj instanceof HasAI ai) {
                ai.updateAI(ctx);
            }
        }
    }

    @Override
    protected void cleanup(Application app) { }

    @Override
    protected void onEnable() { }

    @Override
    protected void onDisable() { }
}
