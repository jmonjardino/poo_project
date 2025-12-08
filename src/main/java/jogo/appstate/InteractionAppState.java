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
import jogo.gameobject.item.Stick;
import jogo.gameobject.item.Axe;
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
    private CraftingAppState craftingAppState; // Referência opcional para verificar estado de crafting
    private float reach = 5.5f;

    // Estado para mineração progressiva
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

    /**
     * Definir referência ao CraftingAppState para verificar se menu está aberto.
     */
    public void setCraftingAppState(CraftingAppState craftingAppState) {
        this.craftingAppState = craftingAppState;
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

        // 0) Teclas 1-4: Selecionar slot OU fabricar (dependendo do estado do crafting)
        boolean craftingOpen = (craftingAppState != null && craftingAppState.isOpen());

        if (input.consumeCraftRecipe1Requested()) {
            if (craftingOpen) {
                craftingAppState.tryCraftByIndex(0);
            } else if (playerAppState != null && playerAppState.getPlayer() != null) {
                playerAppState.getPlayer().setSelectedSlot(0);
                System.out.println("Slot 1 selecionado");
            }
        }
        if (input.consumeCraftRecipe2Requested()) {
            if (craftingOpen) {
                craftingAppState.tryCraftByIndex(1);
            } else if (playerAppState != null && playerAppState.getPlayer() != null) {
                playerAppState.getPlayer().setSelectedSlot(1);
                System.out.println("Slot 2 selecionado");
            }
        }
        if (input.consumeCraftRecipe3Requested()) {
            if (craftingOpen) {
                craftingAppState.tryCraftByIndex(2);
            } else if (playerAppState != null && playerAppState.getPlayer() != null) {
                playerAppState.getPlayer().setSelectedSlot(2);
                System.out.println("Slot 3 selecionado");
            }
        }
        if (input.consumeCraftRecipe4Requested()) {
            if (craftingOpen) {
                craftingAppState.tryCraftByIndex(3);
            } else if (playerAppState != null && playerAppState.getPlayer() != null) {
                playerAppState.getPlayer().setSelectedSlot(3);
                System.out.println("Slot 4 selecionado");
            }
        }

        // 1) Interagir (Botão Genérico: E) -> Apanhar Itens
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

        // 2) Partir (Clique Esquerdo) -> Minar Vóxeis
        if (input.consumeBreakRequested()) {
            VoxelWorld vw = world != null ? world.getVoxelWorld() : null;
            if (vw != null) {
                vw.pickFirstSolid(cam, reach).ifPresent(hit -> {
                    VoxelWorld.Vector3i cell = hit.cell;
                    // Verificar se é o mesmo bloco de antes
                    if (lastHitBlock == null || cell.x != lastHitBlock.x || cell.y != lastHitBlock.y
                            || cell.z != lastHitBlock.z) {
                        lastHitBlock = cell;
                        hitCount = 0;
                    }
                    hitCount++;

                    byte id = vw.getBlock(cell.x, cell.y, cell.z);
                    int requiredHits = 1;
                    int dropType = -1;

                    // Verificar eficiência da ferramenta
                    Player p = playerAppState != null ? playerAppState.getPlayer() : null;
                    int heldItem = (p != null) ? p.getInventory().getItemTypeAt(p.getSelectedSlot()) : 0;
                    boolean isAxe = (heldItem == 400);

                    if (id == VoxelPalette.LOG_ID) {
                        requiredHits = isAxe ? 2 : 4;
                        dropType = 200; // Item Madeira
                    } else if (id == VoxelPalette.DIRT_ID) {
                        requiredHits = 3;
                        dropType = 300; // Item Terra
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
                            // Reiniciar
                            hitCount = 0;
                            lastHitBlock = null;
                        }
                    }
                });
            }
        }

        // 3) Colocar (Clique Direito) -> Colocar Vóxeis
        if (input.consumePlaceRequested()) {
            VoxelWorld vw = world != null ? world.getVoxelWorld() : null;
            Player p = playerAppState != null ? playerAppState.getPlayer() : null;
            if (vw != null && p != null) {
                int slot = p.getSelectedSlot();
                int type = p.getInventory().getItemTypeAt(slot);
                if (type > 0) {
                    byte blockToPlace = 0;
                    if (type == 300)
                        blockToPlace = VoxelPalette.DIRT_ID;
                    else if (type == 200)
                        blockToPlace = VoxelPalette.LOG_ID;
                    else if (type == 210)
                        blockToPlace = VoxelPalette.WOOD_ID;

                    if (blockToPlace > 0) {
                        final byte finalBlockToPlace = blockToPlace; // efetivamente final para lambda
                        vw.pickFirstSolid(cam, reach).ifPresent(hit -> {
                            // Colocar contra a face
                            int tx = hit.cell.x + (int) hit.normal.x;
                            int ty = hit.cell.y + (int) hit.normal.y;
                            int tz = hit.cell.z + (int) hit.normal.z;

                            // Garantir que o alvo está vazio (ar) antes de colocar
                            if (vw.getBlock(tx, ty, tz) == VoxelPalette.AIR_ID) {
                                vw.setBlock(tx, ty, tz, finalBlockToPlace);
                                vw.rebuildDirtyChunks(world.getPhysicsSpace());
                                playerAppState.refreshPhysics();
                                p.getInventory().removeAt(slot, 1);
                                System.out.println("Placed block " + finalBlockToPlace);
                            }
                        });
                    }
                }
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
        if (item instanceof Axe)
            return 400;
        if (item instanceof Stick)
            return 220;
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
