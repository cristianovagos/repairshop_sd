package service;

import comm.*;
import model.CustomerState;
import service.proxy.ClientProxy;
import utils.Constants;

/**
 * Este tipo de dados define o interface ao Park, uma das regiões partilhadas do problema Repair Shop Activities,
 * que implementa o modelo cliente-servidor de tipo 2 (replicação do servidor).
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public class ParkInterface {
    /**
     *  Instanciação do interface ao Park.
     *  @param park Park
     */
    public ParkInterface(Park park) {
        this.park = park;
    }

    /**
     *  Park (representa o serviço a ser prestado)
     */
    private Park park;

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
            case PARK_GO_TO_REPAIR_SHOP_REQ:
                if ((inMessage.getIntegerParam1() < 0) || (inMessage.getIntegerParam1() > Constants.NUM_CUSTOMERS))
                    throw new MessageException("Id do cliente invalido!", inMessage);
                else
                    ((ClientProxy) Thread.currentThread()).setCustomerId(inMessage.getIntegerParam1());
                if ((inMessage.getIntegerParam2() < 0) || (
                        (inMessage.getIntegerParam2() > Constants.NUM_CUSTOMERS) &&
                        ((inMessage.getIntegerParam2() - 100) > Constants.NUM_REPLACEMENT_CARS)))
                    throw new MessageException("Id do carro do cliente invalido!", inMessage);
                else
                    ((ClientProxy) Thread.currentThread()).setCustomerCarId(inMessage.getIntegerParam2());
                break;
            case PARK_FIND_CAR_REQ:
                if ((inMessage.getIntegerParam1() < 0) || (inMessage.getIntegerParam1() > Constants.NUM_CUSTOMERS))
                    throw new MessageException("Id do cliente invalido!", inMessage);
                else
                    ((ClientProxy) Thread.currentThread()).setCustomerId(inMessage.getIntegerParam1());
                if ((inMessage.getIntegerParam2() < 0) || ((inMessage.getIntegerParam2() - 100) > Constants.NUM_REPLACEMENT_CARS))
                    throw new MessageException("Id do carro de substituicao invalido!", inMessage);
                break;
            case PARK_COLLECT_CAR_REQ:
                if ((inMessage.getIntegerParam1() < 0) || (inMessage.getIntegerParam1() > Constants.NUM_CUSTOMERS))
                    throw new MessageException("Id do cliente invalido!", inMessage);
                else
                    ((ClientProxy) Thread.currentThread()).setCustomerId(inMessage.getIntegerParam1());
                break;
            case PARK_GET_VEHICLE_REQ:
            case PARK_RETURN_VEHICLE_REQ:
                if ((inMessage.getIntegerParam1() < 0) || (inMessage.getIntegerParam1() > Constants.NUM_CUSTOMERS))
                    throw new MessageException("Id do carro do cliente invalido!", inMessage);
                else
                    ((ClientProxy) Thread.currentThread()).setCurrentCarFixingId(inMessage.getIntegerParam1());
                break;
            case NONE:
            default:
                throw new MessageException("Tipo de mensagem invalida!", inMessage);
        }

        /* seu processamento */

        switch (inMessage.getMessageType()) {
            case PARK_GO_TO_REPAIR_SHOP_REQ:
                park.goToRepairShop();
                CustomerState customerState1 = ((ClientProxy) Thread.currentThread()).getCustomerState();
                outMessage = new Message(MessageType.PARK_GO_TO_REPAIR_SHOP_RESP, customerState1);
                break;
            case PARK_FIND_CAR_REQ:
                park.findCar(inMessage.getIntegerParam2());
                int customerCarId1 = ((ClientProxy) Thread.currentThread()).getCustomerCarId();
                CustomerState customerState2 = ((ClientProxy) Thread.currentThread()).getCustomerState();
                outMessage = new Message(MessageType.PARK_FIND_CAR_RESP, customerState2, customerCarId1);
                break;
            case PARK_COLLECT_CAR_REQ:
                park.collectCar();
                int customerCarId2 = ((ClientProxy) Thread.currentThread()).getCustomerCarId();
                CustomerState customerState3 = ((ClientProxy) Thread.currentThread()).getCustomerState();
                outMessage = new Message(MessageType.PARK_COLLECT_CAR_RESP, customerState3, customerCarId2);
                break;
            case PARK_GET_VEHICLE_REQ:
                int partId = park.getVehicle();
                outMessage = new Message(MessageType.PARK_GET_VEHICLE_RESP, partId);
                break;
            case PARK_RETURN_VEHICLE_REQ:
                park.returnVehicle();
                outMessage = new Message(MessageType.PARK_RETURN_VEHICLE_RESP);
                break;
        }

        return (outMessage);
    }
}
