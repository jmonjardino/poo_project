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
import jogo.appstate.PersistenceAppState;
import jogo.engine.GameRegistry;
import jogo.gameobject.Wood;
import jogo.gameobject.item.BreakableItem;
import jogo.engine.RenderIndex;
import jogo.gameobject.character.Enemy;
import jogo.gameobject.character.Ally;

/**
 * Entrada principal da aplicação.
 */
public class Jogo extends SimpleApplication {

    public static void main(String[] args) {
        Jogo app = new Jogo();
        app.setShowSettings(true); // mostrar diálogo de configurações
        AppSettings settings = new AppSettings(true);
        settings.setTitle("Test");
        settings.setWidth(1280);
        settings.setHeight(720);
        settings.setGammaCorrection(true); // ativar renderização com correção gama sRGB
        app.setSettings(settings);
        app.start();
    }

    private BulletAppState bulletAppState;

    @Override
    public void simpleInitApp() {
        // desativar flyCam, nós gerimos a câmara
        flyCam.setEnabled(false);
        inputManager.setCursorVisible(false);
        viewPort.setBackgroundColor(new ColorRGBA(0.6f, 0.75f, 1f, 1f)); // tipo céu

        assetManager.registerLocator("src/main/java/assets", com.jme3.asset.plugins.FileLocator.class);

        // Física
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        bulletAppState.setDebugEnabled(false); // desligar mais tarde
        PhysicsSpace physicsSpace = bulletAppState.getPhysicsSpace();

        // AppStates (a ordem importa: input -> world -> render -> interaction ->
        // player)
        // A ordem garante que o input está disponível, o mundo inicializado antes de
        // queries de spawn,
        // e render/interaction podem indexar objetos registados.
        InputAppState input = new InputAppState();
        stateManager.attach(input);

        // Registo do motor deve existir antes do mundo
        GameRegistry registry = new GameRegistry();

        WorldAppState world = new WorldAppState(rootNode, assetManager, physicsSpace, cam, input, registry);
        stateManager.attach(world);

        // Camadas de renderização do motor
        RenderIndex renderIndex = new RenderIndex();
        stateManager.attach(new RenderAppState(rootNode, assetManager, registry, renderIndex, world));

        // Objetos de demonstração movidos para baixo após anexar jogador

        PlayerAppState player = new PlayerAppState(rootNode, assetManager, cam, input, physicsSpace, world);
        stateManager.attach(player);
        InteractionAppState interaction = new InteractionAppState(rootNode, cam, input, renderIndex, world, registry,
                player);
        stateManager.attach(interaction);

        AIAppState ai = new AIAppState(player, world, registry);
        stateManager.attach(ai);

        // Colocar itens de demonstração APÓS anexar jogador usando uma referência de
        // spawn única.
        // Isto evita usar um spawn de recurso antes do mundo estar totalmente
        // inicializado
        // e mantém itens próximos da área inicial do jogador.

        // Spawn manual perto do centro do mundo (320x320)
        // Altura (ty) definida para 35 para cair em segurança no terreno (altura máx
        // ~24)
        int sx = 160;
        int sz = 160;
        int ty = 35;

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

        Enemy zombie = new Enemy("Zombie", 10, 0);
        zombie.setPosition(sx + 40, ty + 1, sz + 40);
        registry.add(zombie);

        Ally ally = new Ally("Ally", "Medic", 5, 10, 5.0); // assistPower=5, healPower=10, protectionPower=5
        ally.setPosition(sx - 40, ty + 1, sz - 40);
        registry.add(ally);



        // HUD
        HudAppState hud = new HudAppState(guiNode, assetManager, player);
        stateManager.attach(hud);
        CraftingAppState crafting = new CraftingAppState(input, player, hud);
        stateManager.attach(crafting);
        interaction.setCraftingAppState(crafting); // Ligar referência para verificação de menu

        // Persistência (F5 Save, F9 Load)
        PersistenceAppState persistence = new PersistenceAppState(input, player, hud, cam);
        stateManager.attach(persistence);
    }
}
