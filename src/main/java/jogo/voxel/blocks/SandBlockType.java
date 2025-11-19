package jogo.voxel.blocks;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Texture2D;
import jogo.util.ProcTextures;
import jogo.voxel.VoxelBlockType;

public class SandBlockType extends VoxelBlockType {
    public SandBlockType() {
        super("sand");
    }

    @Override
    public float getHardness() {
        return 0.3f;
    }

    @Override
    public Material getMaterial(AssetManager assetManager) {
        Texture2D tex = ProcTextures.checker(128, 6,
                new ColorRGBA(0.92f, 0.86f, 0.66f, 1f),
                new ColorRGBA(0.85f, 0.78f, 0.58f, 1f));
        Material m = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        m.setTexture("DiffuseMap", tex);
        m.setBoolean("UseMaterialColors", true);
        m.setColor("Diffuse", ColorRGBA.White);
        m.setColor("Specular", ColorRGBA.White.mult(0.0f));
        m.setFloat("Shininess", 2f);
        return m;
    }
}