package utils;

import java.io.Serializable;

/**
 * Classe utilitária Pair<br>
 *
 * Par de objetos genéricos, implementando a interface Serializable, para permitir comunicação.
 *
 * @param <L> tipo de dados 1
 * @param <R> tipo de dados 2
 */
public class Pair<L extends Serializable, R extends Serializable> implements Serializable {
    /**
     * ID de serialização
     */
    private static final long serialVersionUID = 16052019L;

    /**
     * Tipo de dados 1
     */
    private final L left;

    /**
     * Tipo de dados 2
     */
    private final R right;

    /**
     * Cria um objeto do tipo Pair.
     * @param left objeto com tipo de dados 1
     * @param right objeto com tipo de dados 2
     */
    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    /**
     * Obtém o objeto 1
     * @return o objeto 1
     */
    public L getLeft() {
        return left;
    }

    /**
     * Obtém o objeto 2
     * @return o objeto 2
     */
    public R getRight() {
        return right;
    }
}
