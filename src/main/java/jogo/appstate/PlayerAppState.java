package jogo.appstate;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.PhysicsSpace;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.light.PointLight;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import jogo.gameobject.character.Player;

public class PlayerAppState extends BaseAppState {

    private final Node rootNode;
    private final AssetManager assetManager;
    private final Camera cam;
    private final InputAppState input;
    private final PhysicsSpace physicsSpace;
    private final WorldAppState world;

    private Node playerNode;
    private BetterCharacterControl characterControl;
    private Player player;

    // ângulos de visão
    private float yaw = 0f;
    private float pitch = 0f;

    // afinação
    private float moveSpeed = 8.0f; // m/s
    private float sprintMultiplier = 1.7f;
    private float mouseSensitivity = 30f; // graus por unidade analógica do rato
    private float eyeHeight = 1.7f;

    private Vector3f spawnPosition = new Vector3f(25.5f, 12f, 25.5f);
    private PointLight playerLight;

    public PlayerAppState(Node rootNode, AssetManager assetManager, Camera cam, InputAppState input,
            PhysicsSpace physicsSpace, WorldAppState world) {
        this.rootNode = rootNode;
        this.assetManager = assetManager;
        this.cam = cam;
        this.input = input;
        this.physicsSpace = physicsSpace;
        this.world = world;
        world.registerPlayerAppState(this);
    }

    @Override
    protected void initialize(Application app) {
        // consultar o mundo para spawn recomendado agora que deve estar inicializado
        if (world != null) {
            spawnPosition = world.getRecommendedSpawnPosition();
        }

        playerNode = new Node("Player");
        rootNode.attachChild(playerNode);

        // Entidade de jogador neutra em relação ao motor (sem visuais do motor aqui)
        player = new Player();

        // BetterCharacterControl(radius, height, mass)
        characterControl = new BetterCharacterControl(0.42f, 1.8f, 80f);
        characterControl.setGravity(new Vector3f(0, -24f, 0));
        characterControl.setJumpForce(new Vector3f(0, 400f, 0));
        playerNode.addControl(characterControl);
        physicsSpace.add(characterControl);

        // Fonte de luz local que segue a cabeça do jogador
        playerLight = new PointLight();
        playerLight.setColor(new com.jme3.math.ColorRGBA(0.6f, 0.55f, 0.5f, 1f));
        playerLight.setRadius(12f);
        rootNode.addLight(playerLight);

        // Renascer na localização recomendada
        respawn();

        // initialize camera
        cam.setFrustumPerspective(60f, (float) cam.getWidth() / cam.getHeight(), 0.05f, 500f);
        // inicializar câmara
        cam.setFrustumPerspective(60f, (float) cam.getWidth() / cam.getHeight(), 0.05f, 500f);
        // Olhar ligeiramente para baixo para que o chão seja visível imediatamente
        this.pitch = -0.35f;
        applyViewToCamera();
    }

    @Override
    public void update(float tpf) {
        // renascer a pedido
        if (input.consumeRespawnRequested()) {
            // atualizar spawn do mundo caso o terreno tenha mudado
            if (world != null)
                spawnPosition = world.getRecommendedSpawnPosition();
            respawn();
        }

        if (input.consumePrintCoordsRequested()) {
            Vector3f loc = cam.getLocation();
            System.out.println("Coords: " + loc.x + ", " + loc.y + ", " + loc.z);
        }

        // pausar controlos se o rato não estiver capturado
        if (!input.isMouseCaptured()) {
            characterControl.setWalkDirection(Vector3f.ZERO);
            // manter luz com o jogador mesmo quando pausado
            if (playerLight != null)
                playerLight.setPosition(playerNode.getWorldTranslation().add(0, eyeHeight, 0));
            applyViewToCamera();
            return;
        }

        // lidar com o olhar do rato
        Vector2f md = input.consumeMouseDelta();
        if (md.lengthSquared() != 0f) {
            float degX = md.x * mouseSensitivity;
            float degY = md.y * mouseSensitivity;
            yaw -= degX * FastMath.DEG_TO_RAD;
            pitch -= degY * FastMath.DEG_TO_RAD;
            pitch = FastMath.clamp(pitch, -FastMath.HALF_PI * 0.99f, FastMath.HALF_PI * 0.99f);
        }

        // input de movimento no plano XZ baseado no yaw da câmara
        Vector3f wish = input.getMovementXZ();
        Vector3f dir = Vector3f.ZERO;
        if (wish.lengthSquared() > 0f) {
            dir = computeWorldMove(wish).normalizeLocal();
        }
        float speed = moveSpeed * (input.isSprinting() ? sprintMultiplier : 1f);
        characterControl.setWalkDirection(dir.mult(speed));

        // saltar
        if (input.consumeJumpRequested() && characterControl.isOnGround()) {
            characterControl.jump();
        }

        // colocar câmara na altura dos olhos acima da localização física
        applyViewToCamera();

        // atualizar luz para seguir a cabeça
        if (playerLight != null)
            playerLight.setPosition(playerNode.getWorldTranslation().add(0, eyeHeight, 0));
    }

    private void respawn() {
        characterControl.setWalkDirection(Vector3f.ZERO);
        characterControl.warp(spawnPosition);
        // Reiniciar olhar
        this.pitch = -0.35f;
        applyViewToCamera();
    }

    private Vector3f computeWorldMove(Vector3f inputXZ) {
        // Build forward and left unit vectors from yaw
        float sinY = FastMath.sin(yaw);
        float cosY = FastMath.cos(yaw);
        Vector3f forward = new Vector3f(-sinY, 0, -cosY); // -Z when yaw=0
        Vector3f left = new Vector3f(-cosY, 0, sinY); // -X when yaw=0
        return left.mult(inputXZ.x).addLocal(forward.mult(inputXZ.z));
    }

    private void applyViewToCamera() {
        // Localização do mundo do personagem (spatial é sincronizado pelo controlo)
        Vector3f loc = playerNode.getWorldTranslation().add(0, eyeHeight, 0);
        cam.setLocation(loc);
        cam.setRotation(new com.jme3.math.Quaternion().fromAngles(pitch, yaw, 0f));
    }

    @Override
    protected void cleanup(Application app) {
        if (playerNode != null) {
            if (characterControl != null) {
                physicsSpace.remove(characterControl);
                playerNode.removeControl(characterControl);
                characterControl = null;
            }
            playerNode.removeFromParent();
            playerNode = null;
        }
        if (playerLight != null) {
            rootNode.removeLight(playerLight);
            playerLight = null;
        }
    }

    @Override
    protected void onEnable() {
    }

    @Override
    protected void onDisable() {
    }

    public void refreshPhysics() {
        if (characterControl != null) {
            physicsSpace.remove(characterControl);
            physicsSpace.add(characterControl);
        }
    }

    public Player getPlayer() {
        return player;
    }

    /** Velocidade base de caminhada (sem sprint), usada pela IA do Ally. */
    public float getMoveSpeed() {
        return moveSpeed;
    }
}
