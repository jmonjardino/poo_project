package jogo.appstate;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.texture.Texture2D;
import com.jme3.texture.Texture;
import jogo.engine.GameRegistry;
import jogo.engine.RenderIndex;
import jogo.voxel.VoxelWorld;
import jogo.framework.math.Vec3;
import jogo.gameobject.GameObject;
import jogo.gameobject.Wood;
import jogo.gameobject.character.Player;
import jogo.gameobject.character.Character;
import jogo.gameobject.character.Ally;
import jogo.gameobject.character.Enemy;
import jogo.gameobject.item.Item;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RenderAppState extends BaseAppState {

    private final Node rootNode;
    private final AssetManager assetManager;
    private final GameRegistry registry;
    private final RenderIndex renderIndex;
    private final WorldAppState worldAppState;

    private Node gameNode;
    private final Map<GameObject, Spatial> instances = new HashMap<>();

    public RenderAppState(Node rootNode, AssetManager assetManager, GameRegistry registry, RenderIndex renderIndex, WorldAppState worldAppState) {
        this.rootNode = rootNode;
        this.assetManager = assetManager;
        this.registry = registry;
        this.renderIndex = renderIndex;
        this.worldAppState = worldAppState;
    }

    @Override
    protected void initialize(Application app) {
        gameNode = new Node("GameObjects");
        rootNode.attachChild(gameNode);
    }

    @Override
    public void update(float tpf) {
        // Ensure each registered object has a spatial and sync position
        var current = registry.getAll();
        Set<GameObject> alive = new HashSet<>(current);

        for (GameObject obj : current) {
            Spatial s = instances.get(obj);
            if (s == null) {
                s = createSpatialFor(obj);
                if (s != null) {
                    gameNode.attachChild(s);
                    instances.put(obj, s);
                    renderIndex.register(s, obj);
                }
            }
            //Adicionar fisica as instancias de Ally e Enemy
            if (s != null) {
                Vec3 p = obj.getPosition();
                if (obj instanceof Ally || obj instanceof Enemy) {
                    VoxelWorld vw = worldAppState != null ? worldAppState.getVoxelWorld() : null;
                    if (vw != null) {
                        int topY = vw.getTopSolidY((int) p.x, (int) p.z);
                        if (topY >= 0) {
                            float ny = topY + 1f;
                            if (p.y != ny) {
                                obj.setPosition(p.x, ny + 0.5f, p.z);
                                p = obj.getPosition();
                            }
                        }
                    }
                }
                s.setLocalTranslation(new Vector3f(p.x, p.y, p.z));
            }
        }

        // Cleanup: remove spatials for objects no longer in registry
        var it = instances.entrySet().iterator();
        while (it.hasNext()) {
            var e = it.next();
            if (!alive.contains(e.getKey())) {
                Spatial s = e.getValue();
                renderIndex.unregister(s);
                if (s.getParent() != null) s.removeFromParent();
                it.remove();
            }
        }
    }

    private Spatial createSpatialFor(GameObject obj) {
        //TODO This could be set inside each GameObject!
        if (obj instanceof Player) {
            Geometry g = new Geometry(obj.getName(), new Cylinder(16, 16, 0.35f, 1.4f, true));
            g.setMaterial(colored(ColorRGBA.Green));
            return g;
        } else if (obj instanceof Enemy) {
            Geometry g = new Geometry(obj.getName(), new Cylinder(16, 16, 0.35f, 1.4f, true));
            g.setMaterial(colored(ColorRGBA.Red));
            return g;
        } else if (obj instanceof Ally) {
            Geometry g = new Geometry(obj.getName(), new Cylinder(16, 16, 0.35f, 1.4f, true));
            g.setMaterial(colored(ColorRGBA.Blue));
            return g;
        } else if (obj instanceof Wood) {
            Geometry g = new Geometry(obj.getName(), new Box(0.3f, 0.3f, 0.3f));
            Texture2D tex = (Texture2D) assetManager.loadTexture("textures/blocks/oak_planks.png");
            tex.setMagFilter(Texture.MagFilter.Nearest);
            tex.setMinFilter(Texture.MinFilter.NearestNoMipMaps);
            tex.setAnisotropicFilter(1);
            tex.setWrap(Texture.WrapMode.Repeat);
            Material m = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
            m.setTexture("DiffuseMap", tex);
            m.setBoolean("UseMaterialColors", true);
            m.setColor("Diffuse", ColorRGBA.White);
            m.setColor("Specular", ColorRGBA.White.mult(0.01f));
            m.setFloat("Shininess", 8f);
            g.setMaterial(m);
            return g;
        } else if (obj instanceof Item) {
            Geometry g = new Geometry(obj.getName(), new Box(0.3f, 0.3f, 0.3f));
            g.setMaterial(colored(ColorRGBA.White));
            return g;
        }
        return null;
    }

    private Material colored(ColorRGBA color) {
        Material m = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        m.setBoolean("UseMaterialColors", true);
        m.setColor("Diffuse", color.clone());
        m.setColor("Specular", ColorRGBA.White.mult(0.1f));
        m.setFloat("Shininess", 8f);
        return m;
    }

    @Override
    protected void cleanup(Application app) {
        if (gameNode != null) {
            gameNode.removeFromParent();
            gameNode = null;
        }
        instances.clear();
    }

    @Override
    protected void onEnable() { }

    @Override
    protected void onDisable() { }
}
