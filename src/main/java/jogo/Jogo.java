package jogo;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.system.AppSettings;
import com.jme3.math.ColorRGBA;
import com.jme3.post.FilterPostProcessor;
import jogo.appstate.InputAppState;
import jogo.appstate.PlayerAppState;
import jogo.appstate.WorldAppState;
import jogo.appstate.HudAppState;
import jogo.appstate.RenderAppState;
import jogo.appstate.InteractionAppState;
import jogo.appstate.AIAppState;
import jogo.appstate.CraftingAppState;
import jogo.engine.GameRegistry;
import jogo.gameobject.Wood;
import jogo.gameobject.item.BreakableItem;
import jogo.engine.RenderIndex;
import jogo.gameobject.character.Enemy;
import jogo.gameobject.character.Ally;

/**
 * Main application entry.
 */
public class Jogo extends SimpleApplication {

    public static void main(String[] args) {
        Jogo app = new Jogo();
        app.setShowSettings(true); // show settings dialog
        AppSettings settings = new AppSettings(true);
        settings.setTitle("Test");
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setGammaCorrection(true); // enable sRGB gamma-correct rendering
        app.setSettings(settings);
        app.start();
    }

    private BulletAppState bulletAppState;

    @Override
    public void simpleInitApp() {
        // disable flyCam, we manage camera ourselves
        flyCam.setEnabled(false);
        inputManager.setCursorVisible(false);
        viewPort.setBackgroundColor(new ColorRGBA(0.6f, 0.75f, 1f, 1f)); // sky-like

        assetManager.registerLocator("src/main/java/assets", com.jme3.asset.plugins.FileLocator.class);

        // Physics
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        bulletAppState.setDebugEnabled(false); // toggle off later
        PhysicsSpace physicsSpace = bulletAppState.getPhysicsSpace();

        // AppStates (order matters a bit: input -> world -> render -> interaction ->
        // player)
        // Order ensures input is available, world is initialized before spawn queries,
        // and render/interaction can index registered objects.
        InputAppState input = new InputAppState();
        stateManager.attach(input);

        // Engine registry must exist before world
        GameRegistry registry = new GameRegistry();

        WorldAppState world = new WorldAppState(rootNode, assetManager, physicsSpace, cam, input, registry);
        stateManager.attach(world);

        // Engine render layers
        RenderIndex renderIndex = new RenderIndex();
        stateManager.attach(new RenderAppState(rootNode, assetManager, registry, renderIndex, world));

        // Demo objects moved below after player attached

        PlayerAppState player = new PlayerAppState(rootNode, assetManager, cam, input, physicsSpace, world);
        stateManager.attach(player);
        InteractionAppState interaction = new InteractionAppState(rootNode, cam, input, renderIndex, world, registry,
                player);
        stateManager.attach(interaction);

        AIAppState ai = new AIAppState(player, world, registry);
        stateManager.attach(ai);

        // Place demo items AFTER player attach using a single spawn reference.
        // This avoids using a fallback spawn before the world is fully initialized
        // and keeps items close to the player's starting area.

        var spawn = world.getRecommendedSpawnPosition();
        var vw = world.getVoxelWorld();
        int sx = (int) Math.floor(spawn.x);
        int sz = (int) Math.floor(spawn.z);
        int ty = vw != null ? vw.getTopSolidY(sx, sz) : (int) Math.floor(spawn.y);

        BreakableItem axe = new BreakableItem("Axe", 100, 100, "Axe", "Basic", 1.0);
        axe.setPosition(sx + 2, ty + 1, sz);
        registry.add(axe);

        Wood wood1 = new Wood("Test Wood", 100, "Oak");
        wood1.setPosition(sx - 2, ty + 1, sz);
        registry.add(wood1);

        Wood wood2 = new Wood("Test Wood", 100, "Oak");
        wood2.setPosition(sx + 1, ty + 1, sz + 1);
        registry.add(wood2);

        Wood wood3 = new Wood("Test Wood", 100, "Oak");
        wood3.setPosition(sx - 1, ty + 1, sz - 1);
        registry.add(wood3);

        Enemy enemy = new Enemy("Enemy");
        enemy.setPosition(sx, ty + 1, sz + 2);
        registry.add(enemy);

        Ally ally = new Ally("Ally", "Guard", 10);
        ally.setPosition(sx, ty + 1, sz - 2);
        registry.add(ally);

        // Post-processing: SSAO for subtle contact shadows
        try {
            FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
            Class<?> ssaoCls = Class.forName("com.jme3.post.ssao.SSAOFilter");
            Object ssao = ssaoCls.getConstructor(float.class, float.class, float.class, float.class)
                    .newInstance(2.1f, 0.6f, 0.5f, 0.02f); // radius, intensity, scale, bias
            // Add filter via reflection to avoid compile-time dependency
            java.lang.reflect.Method addFilter = FilterPostProcessor.class.getMethod("addFilter",
                    Class.forName("com.jme3.post.Filter"));
            addFilter.invoke(fpp, ssao);
            viewPort.addProcessor(fpp);
        } catch (Exception e) {
            System.out.println("SSAO not available (effects module missing?): " + e.getMessage());
        }

        // HUD
        HudAppState hud = new HudAppState(guiNode, assetManager, player);
        stateManager.attach(hud);
        stateManager.attach(new CraftingAppState(input, player, hud));
    }
}
