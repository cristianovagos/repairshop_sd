package service.proxy;

/**
 * Interface IMechanicAtt (atributos do Mechanic)<br>
 *
 * Esta interface contém todos os métodos do Mecanico, que
 * serão invocados assim que necessário, durante a troca de mensagens,
 * para manter os estados da entidade antes e após a troca de mensagens.<br>
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public interface IMechanicAtt {

    /**
     * Altera o id do carro que o Mecanico esta a arranjar de momento
     * @param carId id do carro
     */
    void setCurrentCarFixingId(int carId);

    /**
     * Obtém o id do carro que o Mecanico esta a arranjar de momento
     * @return id do carro
     */
    int getCurrentCarFixingId();
}
