// src/main/java/jogo/voxel/blocks/WoodBlockType.java
package jogo.voxel.blocks;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Texture2D;
import jogo.util.ProcTextures;
import jogo.voxel.VoxelBlockType;

public class WoodBlockType extends VoxelBlockType {
    public WoodBlockType() {
        super("wood");
    }

    @Override
    public Material getMaterial(AssetManager assetManager) {
        Texture2D tex = ProcTextures.checker(128, 6,
                new ColorRGBA(0.55f, 0.38f, 0.24f, 1f),
                new ColorRGBA(0.42f, 0.29f, 0.18f, 1f));
        Material m = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        m.setTexture("DiffuseMap", tex);
        m.setBoolean("UseMaterialColors", true);
        m.setColor("Diffuse", ColorRGBA.White);
        m.setColor("Specular", ColorRGBA.White.mult(0.02f));
        m.setFloat("Shininess", 16f);
        return m;
    }
}