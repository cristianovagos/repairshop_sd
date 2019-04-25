package service.proxy;

import model.MechanicState;

public interface IMechanicAtt {
    /**
     * Altera o estado interno do Mecanico
     * @param state novo estado do Mecanico
     */
    void setMechanicState(MechanicState state);

    /**
     * Obtém o estado interno do Mecanico
     * @return estado interno do Mecanico
     */
    MechanicState getMechanicState();

    /**
     * Altera o id do Mecanico
     * @param mechanicId novo id do Mecanico
     */
    void setMechanicId(int mechanicId);

    /**
     * Obtém o id do Mecanico
     * @return id do Mecanico
     */
    int getMechanicId();

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
