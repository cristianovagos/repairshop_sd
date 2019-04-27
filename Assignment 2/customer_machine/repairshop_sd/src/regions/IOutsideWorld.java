package regions;

import entities.Customer;

/**
 * Interface IOutsideWorld (ligação ao Mundo Exterior)<br>
 *
 * Esta interface contém todos os métodos do serviço OutsideWorld, que
 * serão invocados assim que necessário.<br>
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public interface IOutsideWorld {

    /**
     * Operação backToWorkByBus (chamada pelo {@link Customer})<br>
     *
     * Aqui, o cliente vai fazer a sua vida normal, ficando à espera
     * de novidades por parte do Manager, que irá notificá-lo quando
     * o seu carro pessoal estiver pronto.<br>
     */
    void backToWorkByBus();

    /**
     * Operação backToWorkByCar (chamada pelo {@link Customer})<br>
     *
     * Aqui, o cliente vai fazer a sua vida normal.
     * No caso de ter colocado a sua viatura para reparação na Oficina, fica à espera
     * de novidades por parte do Manager, que irá notificá-lo quando
     * o seu carro pessoal estiver pronto.<br>
     *
     * @param carRepaired indicação se a sua viatura já foi reparada
     */
    void backToWorkByCar(boolean carRepaired);
}
