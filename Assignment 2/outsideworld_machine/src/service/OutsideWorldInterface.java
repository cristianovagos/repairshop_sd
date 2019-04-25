package service;

import comm.*;
import model.CustomerState;
import model.ManagerState;
import service.proxy.ClientProxy;
import utils.Constants;

/**
 * Este tipo de dados define o interface ao OutsideWorld, uma das regiões partilhadas do problema Repair Shop Activities,
 * que implementa o modelo cliente-servidor de tipo 2 (replicação do servidor).
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public class OutsideWorldInterface {
    /**
     *  Instanciação do interface ao OutsideWorld.
     *  @param outsideWorld OutsideWorld
     */
    public OutsideWorldInterface(OutsideWorld outsideWorld) {
        this.outsideWorld = outsideWorld;
    }

    /**
     *  OutsideWorld (representa o serviço a ser prestado)
     */
    private OutsideWorld outsideWorld;

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
            case OUTSIDE_WORLD_BACK_TO_WORK_BY_BUS_REQ:
                if ((inMessage.getIntegerParam1() < 0) || (inMessage.getIntegerParam1() > Constants.NUM_CUSTOMERS))
                    throw new MessageException("Id do cliente invalido!", inMessage);
                else
                    ((ClientProxy) Thread.currentThread()).setCustomerId(inMessage.getIntegerParam1());
                break;
            case OUTSIDE_WORLD_BACK_TO_WORK_BY_CAR_REQ:
                if ((inMessage.getIntegerParam1() < 0) || (inMessage.getIntegerParam1() > Constants.NUM_CUSTOMERS))
                    throw new MessageException("Id do cliente invalido!", inMessage);
                else
                    ((ClientProxy) Thread.currentThread()).setCustomerId(inMessage.getIntegerParam1());
                if ((inMessage.getBooleanParam1() == null))
                    throw new MessageException("Indicacao se o carro ja foi reparado invalida!", inMessage);
                break;
            case OUTSIDE_WORLD_PHONE_CUSTOMER_REQ:
                if ((inMessage.getIntegerParam1() < 0) || (inMessage.getIntegerParam1() > Constants.NUM_CUSTOMERS))
                    throw new MessageException("Id do cliente invalido!", inMessage);
                break;
            case NONE:
            default:
                throw new MessageException("Tipo de mensagem invalida!", inMessage);
        }

        /* seu processamento */

        switch (inMessage.getMessageType()) {
            case OUTSIDE_WORLD_BACK_TO_WORK_BY_BUS_REQ:
                outsideWorld.backToWorkByBus();
                CustomerState customerState1 = ((ClientProxy) Thread.currentThread()).getCustomerState();
                outMessage = new Message(MessageType.OUTSIDE_WORLD_BACK_TO_WORK_BY_BUS_RESP, customerState1);
                break;
            case OUTSIDE_WORLD_BACK_TO_WORK_BY_CAR_REQ:
                outsideWorld.backToWorkByCar(inMessage.getBooleanParam1());
                CustomerState customerState2 = ((ClientProxy) Thread.currentThread()).getCustomerState();
                outMessage = new Message(MessageType.OUTSIDE_WORLD_BACK_TO_WORK_BY_CAR_RESP, customerState2);
                break;
            case OUTSIDE_WORLD_PHONE_CUSTOMER_REQ:
                outsideWorld.phoneCustomer(inMessage.getIntegerParam1());
                ManagerState managerState = ((ClientProxy) Thread.currentThread()).getManagerState();
                outMessage = new Message(MessageType.OUTSIDE_WORLD_PHONE_CUSTOMER_RESP, managerState);
        }

        return (outMessage);
    }
}
