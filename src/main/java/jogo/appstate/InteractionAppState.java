package jogo.appstate;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.collision.CollisionResults;
import jogo.engine.RenderIndex;
import jogo.engine.GameRegistry;
import jogo.gameobject.character.Player;
import jogo.gameobject.item.BreakableItem;
import jogo.gameobject.item.Item;
import jogo.gameobject.Wood;
import jogo.gameobject.GameObject;
import jogo.voxel.VoxelWorld;
import jogo.voxel.VoxelPalette;

public class InteractionAppState extends BaseAppState {

    private final Node rootNode;
    private final Camera cam;
    private final InputAppState input;
    private final RenderIndex renderIndex;
    private final GameRegistry registry;
    private final PlayerAppState playerAppState;
    private final WorldAppState world;
    private float reach = 5.5f;

    // State for progressive mining
    private jogo.voxel.VoxelWorld.Vector3i lastHitBlock = null;
    private int hitCount = 0;

    public InteractionAppState(Node rootNode, Camera cam, InputAppState input, RenderIndex renderIndex,
            WorldAppState world, GameRegistry registry, PlayerAppState playerAppState) {
        this.rootNode = rootNode;
        this.cam = cam;
        this.input = input;
        this.renderIndex = renderIndex;
        this.world = world;
        this.registry = registry;
        this.playerAppState = playerAppState;
    }

    @Override
    protected void initialize(Application app) {
    }

    @Override
    public void update(float tpf) {
        if (!input.isMouseCaptured())
            return;

        Vector3f origin = cam.getLocation();
        Vector3f dir = cam.getDirection().normalize();
        Ray ray = new Ray(origin, dir);
        ray.setLimit(reach);

        // 1) Interact (Generic Button: E) -> Pick up Items
        if (input.consumeInteractRequested()) {
            CollisionResults results = new CollisionResults();
            rootNode.collideWith(ray, results);
            if (results.size() > 0) {
                Spatial hit = results.getClosestCollision().getGeometry();
                GameObject obj = findRegistered(hit);
                if (obj instanceof Item item) {
                    Player player = playerAppState != null ? playerAppState.getPlayer() : null;
                    if (player != null) {
                        try {
                            int type = itemTypeFor(item);
                            player.getInventory().add(type, 1);
                            if (registry != null)
                                registry.remove(item);
                            System.out.println("Picked item: " + obj.getName());
                        } catch (RuntimeException e) {
                            System.out.println("Could not pick item: " + e.getMessage());
                        }
                    }
                    return;
                }
            }
        }

        // 2) Break (Left Click) -> Mine Voxels
        if (input.consumeBreakRequested()) {
            VoxelWorld vw = world != null ? world.getVoxelWorld() : null;
            if (vw != null) {
                vw.pickFirstSolid(cam, reach).ifPresent(hit -> {
                    VoxelWorld.Vector3i cell = hit.cell;
                    // Check if same block as before
                    if (lastHitBlock == null || cell.x != lastHitBlock.x || cell.y != lastHitBlock.y
                            || cell.z != lastHitBlock.z) {
                        lastHitBlock = cell;
                        hitCount = 0;
                    }
                    hitCount++;

                    byte id = vw.getBlock(cell.x, cell.y, cell.z);
                    int requiredHits = 1;
                    int dropType = -1;

                    if (id == VoxelPalette.LOG_ID) {
                        requiredHits = 4;
                        dropType = 200; // Wood Item
                    } else if (id == VoxelPalette.DIRT_ID) {
                        requiredHits = 3;
                        dropType = 300; // Dirt Item
                    } else if (id == VoxelPalette.STONE_ID) {
                        requiredHits = 6;
                    } else if (id == VoxelPalette.LEAVES_ID) {
                        requiredHits = 1;
                    }

                    System.out.println("Hit block ID " + id + ": " + hitCount + "/" + requiredHits);

                    if (hitCount >= requiredHits) {
                        if (vw.breakAt(cell.x, cell.y, cell.z)) {
                            vw.rebuildDirtyChunks(world.getPhysicsSpace());
                            playerAppState.refreshPhysics();

                            if (dropType != -1 && playerAppState != null) {
                                playerAppState.getPlayer().getInventory().add(dropType, 1);
                                System.out.println("Mined item type: " + dropType);
                            }
                            // Reset
                            hitCount = 0;
                            lastHitBlock = null;
                        }
                    }
                });
            }
        }
    }

    private GameObject findRegistered(Spatial s) {
        Spatial cur = s;
        while (cur != null) {
            GameObject obj = renderIndex.lookup(cur);
            if (obj != null)
                return obj;
            cur = cur.getParent();
        }
        return null;
    }

    private int itemTypeFor(Item item) {
        if (item instanceof BreakableItem)
            return 100;
        if (item instanceof Wood)
            return 200;
        if (item instanceof jogo.gameobject.Dirt)
            return 300;
        return 900;
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
