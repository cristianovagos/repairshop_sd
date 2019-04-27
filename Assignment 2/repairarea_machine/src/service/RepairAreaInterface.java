package service;

import comm.*;
import model.CustomerState;
import model.ManagerState;
import model.ManagerTask;
import model.MechanicState;
import service.proxy.ClientProxy;
import utils.Constants;

/**
 * Este tipo de dados define o interface ao RepairArea, uma das regiões partilhadas do problema Repair Shop Activities,
 * que implementa o modelo cliente-servidor de tipo 2 (replicação do servidor).
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public class RepairAreaInterface {
    /**
     *  Instanciação do interface ao RepairArea.
     *  @param repairArea RepairArea
     */
    public RepairAreaInterface(RepairArea repairArea) {
        this.repairArea = repairArea;
    }

    /**
     *  RepairArea (representa o serviço a ser prestado)
     */
    private RepairArea repairArea;

    /**
     *  Processamento das mensagens através da execução da tarefa correspondente.
     *  Geração de uma mensagem de resposta.
     *
     *  @param inMessage mensagem com o pedido
     *  @return mensagem de resposta
     *  @throws MessageException se a mensagem com o pedido for considerada inválida
     */
    public Message processAndReply(Message inMessage) throws MessageException {
        Message outMessage = null;                           // mensagem de resposta

        /* validação da mensagem recebida */
        switch (inMessage.getMessageType()) {
            case REPAIR_AREA_READ_THE_PAPER_REQ:
                if ((inMessage.getIntegerParam1() < 0) || (inMessage.getIntegerParam1() > Constants.NUM_MECHANICS))
                    throw new MessageException("Id do mecanico invalido!", inMessage);
                else
                    ((ClientProxy) Thread.currentThread()).setMechanicId(inMessage.getIntegerParam1());
                if ((inMessage.getBooleanParam1() == null))
                    throw new MessageException("Indicacao de primeira execucao invalida!", inMessage);
                else
                    ((ClientProxy) Thread.currentThread()).setFirstRun(inMessage.getBooleanParam1());
                break;
            case REPAIR_AREA_START_REPAIR_PROCEDURE_REQ:
            case REPAIR_AREA_RESUME_REPAIR_PROCEDURE_REQ:
                if ((inMessage.getIntegerParam1() < 0) || (inMessage.getIntegerParam1() > Constants.NUM_MECHANICS))
                    throw new MessageException("Id do mecanico invalido!", inMessage);
                else
                    ((ClientProxy) Thread.currentThread()).setMechanicId(inMessage.getIntegerParam1());
                break;
            case REPAIR_AREA_PART_AVAILABLE_REQ:
                if ((inMessage.getIntegerParam1() < 0) || (inMessage.getIntegerParam1() > Constants.NUM_MECHANICS))
                    throw new MessageException("Id do mecanico invalido!", inMessage);
                else
                    ((ClientProxy) Thread.currentThread()).setMechanicId(inMessage.getIntegerParam1());
                if ((inMessage.getIntegerParam2() < 0) || (inMessage.getIntegerParam2() >= Constants.NUM_PARTS))
                    throw new MessageException("Id da peca invalido!", inMessage);
                break;
            case REPAIR_AREA_GET_REQUIRED_PART_REQ:
                if ((inMessage.getIntegerParam1() < 0) || (inMessage.getIntegerParam1() > Constants.NUM_MECHANICS))
                    throw new MessageException("Id do mecanico invalido!", inMessage);
                else
                    ((ClientProxy) Thread.currentThread()).setMechanicId(inMessage.getIntegerParam1());
                if ((inMessage.getIntegerParam2() < 0) || (inMessage.getIntegerParam2() >= Constants.NUM_PARTS))
                    throw new MessageException("Id da peca invalido!", inMessage);
                if ((inMessage.getIntegerParam3() < 0) || (inMessage.getIntegerParam3() >= Constants.NUM_CUSTOMERS))
                    throw new MessageException("Id do carro invalido!", inMessage);
                else
                    ((ClientProxy) Thread.currentThread()).setCurrentCarFixingId(inMessage.getIntegerParam3());
                break;
            case REPAIR_AREA_STORE_PART_REQ:
                if ((inMessage.getIntegerArrayParam().length < 1))
                    throw new MessageException("Cesto de compras de pecas invalido!", inMessage);
                break;
            case REPAIR_AREA_REGISTER_SERVICE_REQ:
                if ((inMessage.getIntegerParam1() < 0) || (inMessage.getIntegerParam1() > Constants.NUM_CUSTOMERS))
                    throw new MessageException("Id do cliente invalido!", inMessage);
                break;
            case REPAIR_AREA_MARK_END_OF_DAY_REQ:
                // nothing to check
                break;
            case REPAIR_AREA_END_OPERATION_REQ:
                if (inMessage.getIntegerParam1() < 0 || inMessage.getIntegerParam1() > Constants.NUM_MECHANICS)
                    throw new MessageException("Id do mecanico invalido!", inMessage);
                break;
            case NONE:
            default:
                throw new MessageException("Tipo de mensagem invalida!", inMessage);
        }

        /* seu processamento */

        switch (inMessage.getMessageType()) {
            case REPAIR_AREA_READ_THE_PAPER_REQ:
                boolean endOfDay = repairArea.readThePaper();
                boolean firstRun = ((ClientProxy) Thread.currentThread()).getFirstRun();
                MechanicState mechanicState = ((ClientProxy) Thread.currentThread()).getMechanicState();
                outMessage = new Message(MessageType.REPAIR_AREA_READ_THE_PAPER_RESP, mechanicState, endOfDay, firstRun);
                break;
            case REPAIR_AREA_MARK_END_OF_DAY_REQ:
                repairArea.markEndOfDay();
                outMessage = new Message(MessageType.REPAIR_AREA_MARK_END_OF_DAY_RESP);
                break;
            case REPAIR_AREA_START_REPAIR_PROCEDURE_REQ:
                repairArea.startRepairProcedure();
                int currentCarFixingId = ((ClientProxy) Thread.currentThread()).getCurrentCarFixingId();
                MechanicState mechanicState2 = ((ClientProxy) Thread.currentThread()).getMechanicState();
                outMessage = new Message(MessageType.REPAIR_AREA_START_REPAIR_PROCEDURE_RESP, mechanicState2, currentCarFixingId);
                break;
            case REPAIR_AREA_GET_REQUIRED_PART_REQ:
                boolean partInStock = repairArea.getRequiredPart(inMessage.getIntegerParam2());
                MechanicState mechanicState3 = ((ClientProxy) Thread.currentThread()).getMechanicState();
                int currentCarFixingId2 = ((ClientProxy) Thread.currentThread()).getCurrentCarFixingId();
                outMessage = new Message(MessageType.REPAIR_AREA_GET_REQUIRED_PART_RESP, mechanicState3, currentCarFixingId2, partInStock);
                break;
            case REPAIR_AREA_PART_AVAILABLE_REQ:
                repairArea.partAvailable(inMessage.getIntegerParam2());
                MechanicState mechanicState4 = ((ClientProxy) Thread.currentThread()).getMechanicState();
                outMessage = new Message(MessageType.REPAIR_AREA_PART_AVAILABLE_RESP, mechanicState4);
                break;
            case REPAIR_AREA_RESUME_REPAIR_PROCEDURE_REQ:
                repairArea.resumeRepairProcedure();
                MechanicState mechanicState5 = ((ClientProxy) Thread.currentThread()).getMechanicState();
                outMessage = new Message(MessageType.REPAIR_AREA_RESUME_REPAIR_PROCEDURE_RESP, mechanicState5);
                break;
            case REPAIR_AREA_STORE_PART_REQ:
                repairArea.storePart(inMessage.getIntegerArrayParam());
                ManagerState managerState1 = ((ClientProxy) Thread.currentThread()).getManagerState();
                outMessage = new Message(MessageType.REPAIR_AREA_STORE_PART_RESP, managerState1);
                break;
            case REPAIR_AREA_REGISTER_SERVICE_REQ:
                repairArea.registerService(inMessage.getIntegerParam1());
                ManagerState managerState2 = ((ClientProxy) Thread.currentThread()).getManagerState();
                outMessage = new Message(MessageType.REPAIR_AREA_REGISTER_SERVICE_RESP, managerState2);
                break;
            case REPAIR_AREA_END_OPERATION_REQ:
                repairArea.endOperation(inMessage.getIntegerParam1());
                outMessage = new Message(MessageType.REPAIR_AREA_END_OPERATION_RESP);
                break;
        }

        return (outMessage);
    }
}
