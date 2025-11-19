package jogo.gameobject.capability;

public interface Guardian {

    /**
     * Devolve o raio de guarda do guardião.
     * Semântica: o centro da área é a posição lógica do implementador
     * (ex.: `GameObject.getPosition()`); o raio é não negativo.
     * Unidade: mesmo sistema de coordenadas da posição do jogo.
     * @return raio de guarda (>= 0)
     */
    float getGuardRadius();

    /**
     * Verifica se uma posição 3D está dentro da área de guarda do guardião.
     * Contrato: a distância Euclidiana entre a posição fornecida e a posição
     * lógica do implementador é comparada a {@link #getGuardRadius()}.
     * Coordenadas devem usar o mesmo sistema que a posição do jogo.
     * @param x coordenada x
     * @param y coordenada y
     * @param z coordenada z
     * @return true se a posição estiver dentro ou na fronteira do raio de guarda
     */
    boolean isWithinGuardArea(double x, double y, double z);
}
