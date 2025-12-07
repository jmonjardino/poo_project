package jogo.voxel.blocks;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.texture.Texture2D;
import com.jme3.texture.Texture;
import jogo.voxel.VoxelBlockType;

public class LogBlockType extends VoxelBlockType {
    public LogBlockType() {
        super("log");
    }

    public float getHardness() {
        return 2.0f; // Harder than planks
    }

    @Override
    public Material getMaterial(AssetManager assetManager) {
        Texture2D tex = (Texture2D) assetManager.loadTexture("textures/blocks/log.png"); // Placeholder
        tex.setMagFilter(Texture.MagFilter.Nearest);
        tex.setMinFilter(Texture.MinFilter.NearestNoMipMaps);
        tex.setAnisotropicFilter(1);
        tex.setWrap(Texture.WrapMode.Repeat);
        Material m = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        m.setTexture("DiffuseMap", tex);
        m.setBoolean("UseMaterialColors", true);
        m.setColor("Diffuse", new ColorRGBA(0.6f, 0.4f, 0.2f, 1f)); // Darker brown tint
        m.setColor("Specular", ColorRGBA.White.mult(0.02f));
        m.setFloat("Shininess", 16f);
        return m;
    }
}
