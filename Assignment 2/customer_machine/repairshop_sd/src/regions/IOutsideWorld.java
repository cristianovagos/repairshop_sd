package regions;

import entities.Customer;

public interface IOutsideWorld {

    /**
     * Operação backToWorkByBus (chamada pelo {@link Customer})<br>
     *
     * Aqui, o cliente vai fazer a sua vida normal, ficando à espera
     * de novidades por parte do {@link Manager}, que irá notificá-lo quando
     * o seu carro pessoal estiver pronto.<br>
     */
    public void backToWorkByBus();

    /**
     * Operação backToWorkByCar (chamada pelo {@link Customer})<br>
     *
     * Aqui, o cliente vai fazer a sua vida normal.
     * No caso de ter colocado a sua viatura para reparação na Oficina, fica à espera
     * de novidades por parte do {@link Manager}, que irá notificá-lo quando
     * o seu carro pessoal estiver pronto.<br>
     *
     * @param carRepaired indicação se a sua viatura já foi reparada
     */
    public void backToWorkByCar(boolean carRepaired);
}
