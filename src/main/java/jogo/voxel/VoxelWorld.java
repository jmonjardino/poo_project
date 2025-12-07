package jogo.voxel;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.texture.Texture2D;
import jogo.util.ProcTextures;
import jogo.util.FastNoiseLite;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import jogo.util.Hit;

public class VoxelWorld {
    private final AssetManager assetManager;
    private final int sizeX, sizeY, sizeZ;
    private final VoxelPalette palette;

    private final Node node = new Node("VoxelWorld");
    private final Map<Byte, Geometry> geoms = new HashMap<>();
    private final Map<Byte, Material> materials = new HashMap<>();

    private boolean lit = true; // Sombreamento: Ligado por omissão
    private boolean wireframe = false; // Wireframe: Desligado por omissão
    private boolean culling = true; // Culling: Ligado por omissão
    private int groundHeight = 8; // nível base Y
    private int seed = 123456; // Semente por omissão

    private final int chunkSize = Chunk.SIZE;
    private final int chunkCountX, chunkCountY, chunkCountZ;
    private final Chunk[][][] chunks;

    public VoxelWorld(AssetManager assetManager, int sizeX, int sizeY, int sizeZ) {
        this.assetManager = assetManager;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
        this.palette = VoxelPalette.defaultPalette();
        this.chunkCountX = (int) Math.ceil(sizeX / (float) chunkSize);
        this.chunkCountY = (int) Math.ceil(sizeY / (float) chunkSize);
        this.chunkCountZ = (int) Math.ceil(sizeZ / (float) chunkSize);
        this.chunks = new Chunk[chunkCountX][chunkCountY][chunkCountZ];
        for (int cx = 0; cx < chunkCountX; cx++)
            for (int cy = 0; cy < chunkCountY; cy++)
                for (int cz = 0; cz < chunkCountZ; cz++)
                    chunks[cx][cy][cz] = new Chunk(cx, cy, cz);
        initMaterials();
    }

    // Auxiliar para obter chunk e coordenadas locais
    private Chunk getChunk(int x, int y, int z) {
        int cx = x / chunkSize;
        int cy = y / chunkSize;
        int cz = z / chunkSize;
        if (cx < 0 || cy < 0 || cz < 0 || cx >= chunkCountX || cy >= chunkCountY || cz >= chunkCountZ)
            return null;
        return chunks[cx][cy][cz];
    }

    private int lx(int x) {
        return x % chunkSize;
    }

    private int ly(int y) {
        return y % chunkSize;
    }

    private int lz(int z) {
        return z % chunkSize;
    }

    // Acesso a blocos
    public byte getBlock(int x, int y, int z) {
        Chunk c = getChunk(x, y, z);
        if (c == null)
            return VoxelPalette.AIR_ID;
        if (!inBounds(x, y, z))
            return VoxelPalette.AIR_ID;
        return c.get(lx(x), ly(y), lz(z));
    }

    public void setBlock(int x, int y, int z, byte id) {
        Chunk c = getChunk(x, y, z);
        if (c != null) {
            c.set(lx(x), ly(y), lz(z), id);
            c.markDirty();
            // Se na borda do chunk, marcar vizinho como sujo
            if (lx(x) == 0)
                markNeighborChunkDirty(x - 1, y, z);
            if (lx(x) == chunkSize - 1)
                markNeighborChunkDirty(x + 1, y, z);
            if (ly(y) == 0)
                markNeighborChunkDirty(x, y - 1, z);
            if (ly(y) == chunkSize - 1)
                markNeighborChunkDirty(x, y + 1, z);
            if (lz(z) == 0)
                markNeighborChunkDirty(x, y, z - 1);
            if (lz(z) == chunkSize - 1)
                markNeighborChunkDirty(x, y, z + 1);
        }
    }

    private void markNeighborChunkDirty(int x, int y, int z) {
        Chunk n = getChunk(x, y, z);
        if (n != null)
            n.markDirty();
    }

    public boolean breakAt(int x, int y, int z) {
        if (!inBounds(x, y, z))
            return false;
        setBlock(x, y, z, VoxelPalette.AIR_ID);
        return true;
    }

    public Node getNode() {
        return node;
    }

    public void generateLayers() {
        FastNoiseLite noise = new FastNoiseLite(seed);
        noise.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2);
        noise.SetFrequency(0.01f); // Ajustar para escala do terreno

        for (int x = 0; x < sizeX; x++) {
            for (int z = 0; z < sizeZ; z++) {
                // Determinar altura: base 16 + variação +/- 8
                float noiseVal = noise.GetNoise(x, z);
                int height = (int) (16 + noiseVal * 8);

                for (int y = 0; y < sizeY; y++) {
                    byte id = VoxelPalette.AIR_ID;
                    if (y <= height) {
                        if (y == height) {
                            id = VoxelPalette.DIRT_ID; // Topo: Terra
                        } else if (y > height - 3) {
                            id = VoxelPalette.DIRT_ID; // 3 camadas de terra
                        } else {
                            id = VoxelPalette.STONE_ID; // Pedra abaixo
                        }
                    }
                    setBlock(x, y, z, id);
                }
                // Geração de árvores
                if (height < sizeY - 6 && x > 2 && x < sizeX - 2 && z > 2 && z < sizeZ - 2) {
                    // 1% de chance de uma árvore
                    if (Math.random() < 0.01) {
                        generateTree(x, height + 1, z);
                    }
                }
            }
        }
    }

    private void generateTree(int x, int y, int z) {
        // Tronco: 4 blocos para cima
        int trunkHeight = 4;
        for (int i = 0; i < trunkHeight; i++) {
            setBlock(x, y + i, z, VoxelPalette.LOG_ID);
        }

        // Folhas: 3x3 nas 2 camadas superiores do tronco, mais 1 no topo
        int leavesStart = y + 2;
        int leavesEnd = y + trunkHeight; // inclusive of top layer logic

        // Camada 1 (Larga): y + 2
        for (int dx = -2; dx <= 2; dx++) {
            for (int dz = -2; dz <= 2; dz++) {
                // Determinar forma (circular ou quadrada)
                if (Math.abs(dx) == 2 && Math.abs(dz) == 2)
                    continue; // Ignorar cantos para aspeto arredondado
                if (dx == 0 && dz == 0)
                    continue; // Tronco está aqui
                setBlock(x + dx, leavesStart, z + dz, VoxelPalette.LEAVES_ID);
                setBlock(x + dx, leavesStart + 1, z + dz, VoxelPalette.LEAVES_ID);
            }
        }

        // Camada Superior: y + 4 (acima do tronco)
        setBlock(x, y + trunkHeight, z, VoxelPalette.LEAVES_ID);
        setBlock(x + 1, y + trunkHeight, z, VoxelPalette.LEAVES_ID);
        setBlock(x - 1, y + trunkHeight, z, VoxelPalette.LEAVES_ID);
        setBlock(x, y + trunkHeight, z + 1, VoxelPalette.LEAVES_ID);
        setBlock(x, y + trunkHeight, z - 1, VoxelPalette.LEAVES_ID);
        // Adicionar um pouco mais de altura?
        setBlock(x, y + trunkHeight + 1, z, VoxelPalette.LEAVES_ID);
    }

    public int getTopSolidY(int x, int z) {
        if (x < 0 || z < 0 || x >= sizeX || z >= sizeZ)
            return -1;
        for (int y = sizeY - 1; y >= 0; y--) {
            if (palette.get(getBlock(x, y, z)).isSolid())
                return y;
        }
        return -1;
    }

    public Vector3f getRecommendedSpawn() {
        int cx = sizeX / 2;
        int cz = sizeZ / 2;
        int ty = getTopSolidY(cx, cz);
        if (ty < 0)
            ty = groundHeight;
        return new Vector3f(cx + 0.5f, ty + 3.0f, cz + 0.5f);
    }

    private void initMaterials() {
        // Material único para blocos de PEDRA
        Texture2D tex = ProcTextures.checker(128, 4, ColorRGBA.Gray, ColorRGBA.DarkGray);
        materials.put(VoxelPalette.STONE_ID, makeLitTex(tex, 0.08f, 16f));
    }

    private Material makeLitTex(Texture2D tex, float spec, float shininess) {
        Material m = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        m.setTexture("DiffuseMap", tex);
        m.setBoolean("UseMaterialColors", true);
        m.setColor("Diffuse", ColorRGBA.White);
        m.setColor("Specular", ColorRGBA.White.mult(spec));
        m.setFloat("Shininess", shininess);
        applyRenderFlags(m);
        return m;
    }

    private void applyRenderFlags(Material m) {
        m.getAdditionalRenderState()
                .setFaceCullMode(culling ? RenderState.FaceCullMode.Back : RenderState.FaceCullMode.Off);
        m.getAdditionalRenderState().setWireframe(wireframe);
    }

    public void buildMeshes() {
        node.detachAllChildren();
        for (int cx = 0; cx < chunkCountX; cx++) {
            for (int cy = 0; cy < chunkCountY; cy++) {
                for (int cz = 0; cz < chunkCountZ; cz++) {
                    Chunk chunk = chunks[cx][cy][cz];
                    chunk.buildMesh(assetManager, palette);
                    node.attachChild(chunk.getNode());
                }
            }
        }
    }

    public void buildPhysics(PhysicsSpace space) {
        // Construir corpos rígidos estáticos por chunk em vez de um corpo único
        if (space == null)
            return;
        for (int cx = 0; cx < chunkCountX; cx++) {
            for (int cy = 0; cy < chunkCountY; cy++) {
                for (int cz = 0; cz < chunkCountZ; cz++) {
                    Chunk chunk = chunks[cx][cy][cz];
                    chunk.updatePhysics(space);
                }
            }
        }
    }

    public Optional<Hit> pickFirstSolid(Camera cam, float maxDistance) {
        Vector3f origin = cam.getLocation();
        Vector3f dir = cam.getDirection().normalize();

        int x = (int) Math.floor(origin.x);
        int y = (int) Math.floor(origin.y);
        int z = (int) Math.floor(origin.z);

        float tMaxX, tMaxY, tMaxZ;
        float tDeltaX, tDeltaY, tDeltaZ;
        int stepX = dir.x > 0 ? 1 : -1;
        int stepY = dir.y > 0 ? 1 : -1;
        int stepZ = dir.z > 0 ? 1 : -1;

        float nextVoxelBoundaryX = x + (stepX > 0 ? 1 : 0);
        float nextVoxelBoundaryY = y + (stepY > 0 ? 1 : 0);
        float nextVoxelBoundaryZ = z + (stepZ > 0 ? 1 : 0);

        tMaxX = (dir.x != 0) ? (nextVoxelBoundaryX - origin.x) / dir.x : Float.POSITIVE_INFINITY;
        tMaxY = (dir.y != 0) ? (nextVoxelBoundaryY - origin.y) / dir.y : Float.POSITIVE_INFINITY;
        tMaxZ = (dir.z != 0) ? (nextVoxelBoundaryZ - origin.z) / dir.z : Float.POSITIVE_INFINITY;

        tDeltaX = (dir.x != 0) ? stepX / dir.x : Float.POSITIVE_INFINITY;
        tDeltaY = (dir.y != 0) ? stepY / dir.y : Float.POSITIVE_INFINITY;
        tDeltaZ = (dir.z != 0) ? stepZ / dir.z : Float.POSITIVE_INFINITY;

        float t = 0f;
        if (inBounds(x, y, z) && isSolid(x, y, z)) {
            return Optional.of(new Hit(new Vector3i(x, y, z), new Vector3f(0, 0, 0), 0f));
        }

        Vector3f lastNormal = new Vector3f(0, 0, 0);

        while (t <= maxDistance) {
            if (tMaxX < tMaxY) {
                if (tMaxX < tMaxZ) {
                    x += stepX;
                    t = tMaxX;
                    tMaxX += tDeltaX;
                    lastNormal.set(-stepX, 0, 0);
                } else {
                    z += stepZ;
                    t = tMaxZ;
                    tMaxZ += tDeltaZ;
                    lastNormal.set(0, 0, -stepZ);
                }
            } else {
                if (tMaxY < tMaxZ) {
                    y += stepY;
                    t = tMaxY;
                    tMaxY += tDeltaY;
                    lastNormal.set(0, -stepY, 0);
                } else {
                    z += stepZ;
                    t = tMaxZ;
                    tMaxZ += tDeltaZ;
                    lastNormal.set(0, 0, -stepZ);
                }
            }

            if (!inBounds(x, y, z)) {
                if (t > maxDistance)
                    break;
                continue;
            }
            if (isSolid(x, y, z)) {
                return Optional.of(new Hit(new Vector3i(x, y, z), lastNormal.clone(), t));
            }
        }
        return Optional.empty();
    }

    private boolean isSolid(int x, int y, int z) {
        if (!inBounds(x, y, z))
            return false;
        return palette.get(getBlock(x, y, z)) instanceof jogo.voxel.blocks.AirBlockType == false;
    }

    private boolean inBounds(int x, int y, int z) {
        return x >= 0 && y >= 0 && z >= 0 && x < sizeX && y < sizeY && z < sizeZ;
    }

    public void setLit(boolean lit) {
        if (this.lit == lit)
            return;
        this.lit = lit;
        for (var e : geoms.entrySet()) {
            Geometry g = e.getValue();
            var oldMat = g.getMaterial();
            com.jme3.texture.Texture tex = oldMat.getTextureParam("DiffuseMap") != null
                    ? oldMat.getTextureParam("DiffuseMap").getTextureValue()
                    : (oldMat.getTextureParam("ColorMap") != null ? oldMat.getTextureParam("ColorMap").getTextureValue()
                            : null);
            Material newMat;
            if (this.lit) {
                newMat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
                if (tex != null)
                    newMat.setTexture("DiffuseMap", tex);
                newMat.setBoolean("UseMaterialColors", true);
                newMat.setColor("Diffuse", ColorRGBA.White);
                newMat.setColor("Specular", ColorRGBA.White.mult(0.08f));
                newMat.setFloat("Shininess", 16f);
            } else {
                newMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
                if (tex != null)
                    newMat.setTexture("ColorMap", tex);
            }
            applyRenderFlags(newMat);
            g.setMaterial(newMat);
        }
    }

    public void setWireframe(boolean wireframe) {
        if (this.wireframe == wireframe)
            return;
        this.wireframe = wireframe;

        for (Geometry g : geoms.values())
            applyRenderFlags(g.getMaterial());
    }

    public void setCulling(boolean culling) {
        if (this.culling == culling)
            return;
        this.culling = culling;
        for (Geometry g : geoms.values())
            applyRenderFlags(g.getMaterial());
    }

    public boolean isLit() {
        return lit;
    }

    public boolean isWireframe() {
        return wireframe;
    }

    public boolean isCulling() {
        return culling;
    }

    public void toggleRenderDebug() {
        System.out.println("Toggled render debug");
        setLit(!isLit());
        setWireframe(!isWireframe());
        setCulling(!isCulling());
    }

    public int getGroundHeight() {
        return groundHeight;
    }

    public VoxelPalette getPalette() {
        return palette;
    }

    /**
     * Reconstrói malhas apenas para chunks sujos. Chame isto uma vez por frame no
     * loop de update.
     */
    public void rebuildDirtyChunks(PhysicsSpace physicsSpace) {
        int rebuilt = 0;
        for (int cx = 0; cx < chunkCountX; cx++) {
            for (int cy = 0; cy < chunkCountY; cy++) {
                for (int cz = 0; cz < chunkCountZ; cz++) {
                    Chunk chunk = chunks[cx][cy][cz];
                    if (chunk.isDirty()) {
                        System.out.println("Rebuilding chunk: " + cx + "," + cy + "," + cz);
                        chunk.buildMesh(assetManager, palette);
                        chunk.updatePhysics(physicsSpace);
                        chunk.clearDirty();
                        rebuilt++;
                    }
                }
            }
        }
        if (rebuilt > 0)
            System.out.println("Chunks rebuilt this frame: " + rebuilt);
        if (rebuilt > 0 && physicsSpace != null) {
            physicsSpace.update(0); // Forçar espaço de física a processar alterações
            System.out.println("Physics space forced update after chunk physics changes.");
        }
    }

    /**
     * Limpa a flag de sujo em todos os chunks. Chame após buildMeshes() inicial.
     */
    public void clearAllDirtyFlags() {
        for (int cx = 0; cx < chunkCountX; cx++)
            for (int cy = 0; cy < chunkCountY; cy++)
                for (int cz = 0; cz < chunkCountZ; cz++)
                    chunks[cx][cy][cz].clearDirty();
    }

    // int3 simples
    public static class Vector3i {
        public final int x, y, z;

        public Vector3i(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Vector3i(Vector3f vec3f) {
            this.x = (int) vec3f.x;
            this.y = (int) vec3f.y;
            this.z = (int) vec3f.z;
        }
    }
}
