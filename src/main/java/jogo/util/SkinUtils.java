package jogo.util;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.texture.Texture;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.util.BufferUtils;

/**
 * Utility to create a Minecraft-style character model (Head, Body, Arms, Legs)
 * from a standard 64x64 or 64x32 skin texture.
 */
public class SkinUtils {

    // Texture size
    private static final int TEX_W = 64;
    private static final int TEX_H = 64;

    public static Node createCharacterModel(AssetManager assetManager, String skinPath) {
        Node node = new Node("CharacterModel");

        // Load texture
        Texture tex = assetManager.loadTexture(skinPath);
        tex.setMagFilter(Texture.MagFilter.Nearest);
        tex.setMinFilter(Texture.MinFilter.NearestNoMipMaps);

        Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
        mat.setTexture("DiffuseMap", tex);
        mat.setBoolean("UseMaterialColors", true);
        mat.setColor("Diffuse", ColorRGBA.White);
        mat.setColor("Specular", ColorRGBA.White.mult(0.01f));
        mat.setFloat("Shininess", 1f);

        // Setup transparency for Hat layer
        mat.getAdditionalRenderState().setBlendMode(com.jme3.material.RenderState.BlendMode.Alpha);
        mat.setFloat("AlphaDiscardThreshold", 0.5f);

        // Constants
        // 1 pixel = 0.055 units -> Total height ~1.8m
        float px = 0.055f;

        // Parts dimensions (w, h, d in pixels)
        // Leg: 4, 12, 4
        // Body: 8, 12, 4
        // Head: 8, 8, 8

        float legH = 12 * px;
        float bodyH = 12 * px;
        float headH = 8 * px;

        // POSITIONS (Y relative to parent, centered in part geometry)
        // Geometry creates centered mesh, so we offset center.

        // Legs sit on ground (0). Center = legH/2.
        float legY = legH / 2f;

        // Body sits on legs (legH). Center = legH + bodyH/2.
        float bodyY = legH + (bodyH / 2f);

        // Head sits on body (legH + bodyH). Center = legH + bodyH + headH/2.
        float headY = legH + bodyH + (headH / 2f);

        // Arms centered at shoulder height.
        // Shoulders are at (legH + bodyH). Arms hang down 12px.
        // Pivot is top of arm. Center is 6px down from top.
        // Center Y = (legH + bodyH) - (12*px / 2) = legH + bodyH - 6px.
        // = legH + 6px.
        // Let's verify: Body top is legH+12px. Center of body is legH+6px.
        // So arms center aligns with body center vertically if arms are same height.
        // Yes.
        float armY = bodyY;

        // HEAD (8x8x8) at (0, 0)
        Geometry head = createPart("Head", 8, 8, 8, 0, 0, mat, px, 0);
        head.setLocalTranslation(0, headY, 0);
        node.attachChild(head);

        // HEAD OVERLAY (HAT) (8x8x8) at (32, 0) - Scaled slightly up
        Geometry hat = createPart("Hat", 8, 8, 8, 32, 0, mat, px, 0.005f);
        hat.setLocalTranslation(0, headY, 0);
        node.attachChild(hat);

        // BODY (8x12x4) at (16, 16)
        Geometry body = createPart("Body", 8, 12, 4, 16, 16, mat, px, 0);
        body.setLocalTranslation(0, bodyY, 0);
        node.attachChild(body);

        // ARMS (4x12x4)
        // Body width = 8*px. Half = 4*px.
        // Arm width = 4*px. Half = 2*px.
        // Center Dist = 4*px + 2*px = 6*px.
        float armDist = 6 * px;

        // Right Arm at (40, 16)
        Geometry rightArm = createPart("RightArm", 4, 12, 4, 40, 16, mat, px, 0);
        rightArm.setLocalTranslation(armDist, armY, 0);
        node.attachChild(rightArm);

        // Left Arm at (32, 48)
        Geometry leftArm = createPart("LeftArm", 4, 12, 4, 32, 48, mat, px, 0);
        leftArm.setLocalTranslation(-armDist, armY, 0);
        node.attachChild(leftArm);

        // LEGS (4x12x4)
        // Leg width = 4*px. Half = 2*px.
        // Center Dist = 2*px.
        float legDist = 2 * px;

        // Right Leg at (0, 16)
        Geometry rightLeg = createPart("RightLeg", 4, 12, 4, 0, 16, mat, px, 0);
        rightLeg.setLocalTranslation(legDist, legY, 0);
        node.attachChild(rightLeg);

        // Left Leg at (16, 48)
        Geometry leftLeg = createPart("LeftLeg", 4, 12, 4, 16, 48, mat, px, 0);
        leftLeg.setLocalTranslation(-legDist, legY, 0);
        node.attachChild(leftLeg);

        return node;
    }

    private static Geometry createPart(String name, int w, int h, int d, int u, int v, Material mat, float pixelScale,
            float inflate) {
        // Size in meters (with slight inflation for overlays)
        float hw = ((w * pixelScale) / 2f) + inflate;
        float hh = ((h * pixelScale) / 2f) + inflate;
        float hd = ((d * pixelScale) / 2f) + inflate;

        Mesh mesh = new Mesh();

        // Vertices (8 corners, duplicates for face normals/UVs -> 24 vertices)
        Vector3f[] p = new Vector3f[] {
                new Vector3f(-hw, -hh, hd), new Vector3f(hw, -hh, hd), new Vector3f(hw, hh, hd),
                new Vector3f(-hw, hh, hd), // Front
                new Vector3f(hw, -hh, -hd), new Vector3f(-hw, -hh, -hd), new Vector3f(-hw, hh, -hd),
                new Vector3f(hw, hh, -hd), // Back
                new Vector3f(-hw, -hh, -hd), new Vector3f(-hw, -hh, hd), new Vector3f(-hw, hh, hd),
                new Vector3f(-hw, hh, -hd), // Left
                new Vector3f(hw, -hh, hd), new Vector3f(hw, -hh, -hd), new Vector3f(hw, hh, -hd),
                new Vector3f(hw, hh, hd), // Right
                new Vector3f(-hw, hh, hd), new Vector3f(hw, hh, hd), new Vector3f(hw, hh, -hd),
                new Vector3f(-hw, hh, -hd), // Top
                new Vector3f(-hw, -hh, -hd), new Vector3f(hw, -hh, -hd), new Vector3f(hw, -hh, hd),
                new Vector3f(-hw, -hh, hd) // Bottom
        };

        // Indices
        int[] indexes = {
                0, 1, 2, 2, 3, 0, // Front
                4, 5, 6, 6, 7, 4, // Back
                8, 9, 10, 10, 11, 8, // Left (Outer)
                12, 13, 14, 14, 15, 12, // Right (Outer)
                16, 17, 18, 18, 19, 16, // Top
                20, 21, 22, 22, 23, 20 // Bottom
        };

        // Normals (Flat shading)
        float[] normals = {
                0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1,
                0, 0, -1, 0, 0, -1, 0, 0, -1, 0, 0, -1,
                -1, 0, 0, -1, 0, 0, -1, 0, 0, -1, 0, 0,
                1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0,
                0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0,
                0, -1, 0, 0, -1, 0, 0, -1, 0, 0, -1, 0
        };

        mesh.setBuffer(VertexBuffer.Type.Position, 3, BufferUtils.createFloatBuffer(p));
        mesh.setBuffer(VertexBuffer.Type.Normal, 3, BufferUtils.createFloatBuffer(normals));
        mesh.setBuffer(VertexBuffer.Type.Index, 3, BufferUtils.createIntBuffer(indexes));

        // Calculate UVs per face
        float[] texCoords = new float[2 * 24];

        // Front Face (u+d, v+d, w, h) -> Indices 0-3
        mapFace(texCoords, 0, u + d, v + d, w, h);

        // Back Face (u+d+w+d, v+d, w, h) -> Indices 4-7
        mapFace(texCoords, 4, u + d + w + d, v + d, w, h);

        // Left Face (Outer Left logic: u+d+w, v+d) -> Indices 8-11
        mapFace(texCoords, 8, u + d + w, v + d, d, h);

        // Right Face (Outer Right logic: u, v+d) -> Indices 12-15
        mapFace(texCoords, 12, u, v + d, d, h);

        // Top Face (u+d, v, w, d) -> Indices 16-19
        mapFace(texCoords, 16, u + d, v, w, d);

        // Bottom Face (u+d+w, v, w, d) -> Indices 20-23
        mapFace(texCoords, 20, u + d + w, v, w, d);

        mesh.setBuffer(VertexBuffer.Type.TexCoord, 2, BufferUtils.createFloatBuffer(texCoords));
        mesh.updateBound();

        Geometry g = new Geometry(name, mesh);
        g.setMaterial(mat);
        return g;
    }

    private static void mapFace(float[] buff, int offsetVertex, int u, int v, int w, int h) {
        // Map UVs converting Top-Left origin (Texture) to Bottom-Left origin (OpenGL)
        float u1 = (float) u / TEX_W;
        float u2 = (float) (u + w) / TEX_W;
        float v_top = 1f - ((float) v / TEX_H);
        float v_bot = 1f - ((float) (v + h) / TEX_H);

        // Vertex 0 (BL)
        buff[offsetVertex * 2 + 0] = u1;
        buff[offsetVertex * 2 + 1] = v_bot;

        // Vertex 1 (BR)
        buff[offsetVertex * 2 + 2] = u2;
        buff[offsetVertex * 2 + 3] = v_bot;

        // Vertex 2 (TR)
        buff[offsetVertex * 2 + 4] = u2;
        buff[offsetVertex * 2 + 5] = v_top;

        // Vertex 3 (TL)
        buff[offsetVertex * 2 + 6] = u1;
        buff[offsetVertex * 2 + 7] = v_top;
    }
}
