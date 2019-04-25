package regions;

import entities.Mechanic;
import utils.MemFIFO;

public interface IRepairArea {

    /**
     * Operação readThePaper (chamada pelo {@link Mechanic})<br>
     *
     * O Mecânico irá estar bloqueado até que lhe seja atribuído um serviço<br>
     *
     * @return estado do dia de trabalho
     */
    public  boolean readThePaper() ;

    /**
     * Operação startRepairProcedure (chamada pelo {@link Mechanic})<br>
     *
     * O Mecânico vai à lista de espera dos clientes para saber qual a viatura
     * que vai arranjar.<br>
     */
    public  void startRepairProcedure();

    /**
     * Operação getRequiredPart (chamada pelo {@link Mechanic})<br>
     *
     * O Mecânico irá verificar se a peça pretendida para o arranjo da viatura
     * está disponível.<br>
     *
     * @param partId peça pretendida
     * @return indicação se a peça pretendida existe em stock
     */
    public  boolean getRequiredPart(int partId);

    /**
     * Operação partAvailable (chamada pelo {@link Mechanic})<br>
     *
     * Obtém a peça para proceder ao arranjo.<br>
     *
     * @param partId a peça a obter
     */
    public  void partAvailable(int partId);

    /**
     * Operação resumeRepairProcedure (chamada pelo {@link Mechanic})<br>
     *
     * Recomeço da reparação, agora que o Mecânico tem a peça pretendida para
     * substituição.<br>
     */
    public  void resumeRepairProcedure();
}