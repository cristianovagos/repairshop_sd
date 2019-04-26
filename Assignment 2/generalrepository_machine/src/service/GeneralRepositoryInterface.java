package service;

import comm.*;
import model.CustomerState;
import utils.Constants;

/**
 * Este tipo de dados define o interface ao GeneralRepository, uma das regiões partilhadas do problema Repair Shop Activities,
 * que implementa o modelo cliente-servidor de tipo 2 (replicação do servidor).
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public class GeneralRepositoryInterface {
    /**
     *  Instanciação do interface ao GeneralRepository.
     *  @param repository GeneralRepository
     */
    public GeneralRepositoryInterface(GeneralRepository repository) {
        this.repository = repository;
    }

    /**
     *  GeneralRepository (representa o serviço a ser prestado)
     */
    private GeneralRepository repository;

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
            case REPOSITORY_INITIALIZE_CUSTOMER_REQ:
                if ((inMessage.getIntegerParam1() < 0) || (inMessage.getIntegerParam1() > Constants.NUM_CUSTOMERS))
                    throw new MessageException("Id do cliente invalido!", inMessage);
                if (inMessage.getBooleanParam1() == null)
                    throw new MessageException("Indicacao se cliente requer viatura de substituicao invalido!", inMessage);
                break;
            case REPOSITORY_SET_CUSTOMER_STATE_REQ:
                if ((inMessage.getIntegerParam1() < 0) || (inMessage.getIntegerParam1() > Constants.NUM_CUSTOMERS))
                    throw new MessageException("Id do cliente invalido!", inMessage);
                if (inMessage.getCustomerState() == CustomerState.NONE)
                    throw new MessageException("Estado do cliente invalido!", inMessage);
                if (inMessage.getBooleanParam1() == null)
                    throw new MessageException("Indicacao de impressao no ficheiro de log invalido!", inMessage);
                break;
            case REPOSITORY_SET_CUSTOMER_CAR_REQ:
                if ((inMessage.getIntegerParam1() < 0) || (inMessage.getIntegerParam1() > Constants.NUM_CUSTOMERS))
                    throw new MessageException("Id do cliente invalido!", inMessage);
                if ((inMessage.getIntegerParam2() < -1) || (
                        (inMessage.getIntegerParam2() > Constants.NUM_CUSTOMERS) &&
                        (inMessage.getIntegerParam2() - 100 >= Constants.NUM_REPLACEMENT_CARS)))
                    throw new MessageException("Id do carro do cliente invalido!", inMessage);
                if (inMessage.getBooleanParam1() == null)
                    throw new MessageException("Indicacao de impressao no ficheiro de log invalido!", inMessage);
                break;
            case REPOSITORY_SET_MANAGER_STATE_REQ:
                if (inMessage.getBooleanParam1() == null)
                    throw new MessageException("Indicacao de impressao no ficheiro de log invalido!", inMessage);
                break;
            case REPOSITORY_SET_MECHANIC_STATE_REQ:
                if ((inMessage.getIntegerParam1() < 0) || (inMessage.getIntegerParam1() > Constants.NUM_MECHANICS))
                    throw new MessageException("Id do Mecanico invalido!", inMessage);
                if (inMessage.getBooleanParam1() == null)
                    throw new MessageException("Indicacao de impressao no ficheiro de log invalido!", inMessage);
                break;
            case REPOSITORY_CUSTOMER_CAR_ENTERS_PARK_REQ:
            case REPOSITORY_CUSTOMER_CAR_LEAVES_PARK_REQ:
            case REPOSITORY_REPLACEMENT_CAR_ENTERS_PARK_REQ:
            case REPOSITORY_REPLACEMENT_CAR_LEAVES_PARK_REQ:
            case REPOSITORY_MANAGER_REQUESTED_SERVICE_REQ:
                // nothing to do
                break;
            case REPOSITORY_SET_CUSTOMERS_IN_QUEUE_REQ:
                if (inMessage.getIntegerParam1() < 0)
                    throw new MessageException("Numero de clientes na fila invalido!", inMessage);
                if (inMessage.getBooleanParam1() == null)
                    throw new MessageException("Indicacao de impressao no ficheiro de log invalido!", inMessage);
                break;
            case REPOSITORY_SET_CUSTOMERS_IN_QUEUE_FOR_KEY_REQ:
                if (inMessage.getIntegerParam1() < 0)
                    throw new MessageException("Numero de clientes na fila invalido!", inMessage);
                break;
            case REPOSITORY_SET_TOTAL_REPAIRED_CARS_REQ:
                if (inMessage.getIntegerParam1() < 0 || inMessage.getIntegerParam1() > Constants.NUM_CUSTOMERS)
                    throw new MessageException("Numero total de carros reparados invalido!", inMessage);
                break;
            case REPOSITORY_SET_STOCK_PARTS_REQ:
                if (inMessage.getIntegerArrayParam().length < 1)
                    throw new MessageException("Array de pecas em stock invalido!", inMessage);
                if (inMessage.getBooleanParam1() == null)
                    throw new MessageException("Indicacao de impressao no ficheiro de log invalido!", inMessage);
                break;
            case REPOSITORY_ADD_MISSING_PART_REQ:
            case REPOSITORY_REMOVE_MISSING_PART_REQ:
                if (inMessage.getIntegerParam1() < 0 || inMessage.getIntegerParam1() > Constants.NUM_PARTS)
                    throw new MessageException("Indice de peca invalido!", inMessage);
                break;
            case REPOSITORY_SET_MISSING_PART_ALERT_REQ:
                if (inMessage.getIntegerParam1() < 0 || inMessage.getIntegerParam1() > Constants.NUM_PARTS)
                    throw new MessageException("Indice de peca invalido!", inMessage);
                if (inMessage.getBooleanParam1() == null)
                    throw new MessageException("Indicacao de peca em falta invalido!", inMessage);
                if (inMessage.getBooleanParam2() == null)
                    throw new MessageException("Indicacao de impressao no ficheiro de log invalido!", inMessage);
                break;
            case REPOSITORY_SET_SOLD_PARTS_REQ:
                if (inMessage.getIntegerArrayParam().length < 1)
                    throw new MessageException("Array de pecas vendidas invalido!", inMessage);
                break;
            case NONE:
            default:
                throw new MessageException("Tipo de mensagem invalida!", inMessage);
        }

        /* seu processamento */

        switch (inMessage.getMessageType()) {
            case REPOSITORY_INITIALIZE_CUSTOMER_REQ:
                repository.initializeCustomer(inMessage.getIntegerParam1(), inMessage.getBooleanParam1());
                outMessage = new Message(MessageType.REPOSITORY_INITIALIZE_CUSTOMER_RESP);
                break;
            case REPOSITORY_SET_CUSTOMER_STATE_REQ:
                repository.setCustomerState(inMessage.getIntegerParam1(), inMessage.getCustomerState(), inMessage.getBooleanParam1());
                outMessage = new Message(MessageType.REPOSITORY_SET_CUSTOMER_STATE_RESP);
                break;
            case REPOSITORY_SET_CUSTOMER_CAR_REQ:
                repository.setCustomerCar(inMessage.getIntegerParam1(), inMessage.getIntegerParam2(), inMessage.getBooleanParam1());
                outMessage = new Message(MessageType.REPOSITORY_SET_CUSTOMER_CAR_RESP);
                break;
            case REPOSITORY_SET_MANAGER_STATE_REQ:
                repository.setManagerState(inMessage.getManagerState(), inMessage.getBooleanParam1());
                outMessage = new Message(MessageType.REPOSITORY_SET_MANAGER_STATE_RESP);
                break;
            case REPOSITORY_SET_MECHANIC_STATE_REQ:
                repository.setMechanicState(inMessage.getIntegerParam1(), inMessage.getMechanicState(), inMessage.getBooleanParam1());
                outMessage = new Message(MessageType.REPOSITORY_SET_MECHANIC_STATE_RESP);
                break;
            case REPOSITORY_CUSTOMER_CAR_ENTERS_PARK_REQ:
                repository.customerCarEntersPark();
                outMessage = new Message(MessageType.REPOSITORY_CUSTOMER_CAR_ENTERS_PARK_RESP);
                break;
            case REPOSITORY_CUSTOMER_CAR_LEAVES_PARK_REQ:
                repository.customerCarLeavesPark();
                outMessage = new Message(MessageType.REPOSITORY_CUSTOMER_CAR_LEAVES_PARK_RESP);
                break;
            case REPOSITORY_REPLACEMENT_CAR_ENTERS_PARK_REQ:
                repository.replacementCarEntersPark();
                outMessage = new Message(MessageType.REPOSITORY_REPLACEMENT_CAR_ENTERS_PARK_RESP);
                break;
            case REPOSITORY_REPLACEMENT_CAR_LEAVES_PARK_REQ:
                repository.replacementCarLeavesPark();
                outMessage = new Message(MessageType.REPOSITORY_REPLACEMENT_CAR_LEAVES_PARK_RESP);
                break;
            case REPOSITORY_MANAGER_REQUESTED_SERVICE_REQ:
                repository.managerRequestedService();
                outMessage = new Message(MessageType.REPOSITORY_MANAGER_REQUESTED_SERVICE_RESP);
                break;
            case REPOSITORY_SET_CUSTOMERS_IN_QUEUE_REQ:
                repository.setCustomersInQueue(inMessage.getIntegerParam1(), inMessage.getBooleanParam1());
                outMessage = new Message(MessageType.REPOSITORY_SET_CUSTOMERS_IN_QUEUE_RESP);
                break;
            case REPOSITORY_SET_CUSTOMERS_IN_QUEUE_FOR_KEY_REQ:
                repository.setCustomersInQueueForKey(inMessage.getIntegerParam1());
                outMessage = new Message(MessageType.REPOSITORY_SET_CUSTOMERS_IN_QUEUE_FOR_KEY_RESP);
                break;
            case REPOSITORY_SET_TOTAL_REPAIRED_CARS_REQ:
                repository.setTotalRepairedCars(inMessage.getIntegerParam1());
                outMessage = new Message(MessageType.REPOSITORY_SET_TOTAL_REPAIRED_CARS_RESP);
                break;
            case REPOSITORY_SET_STOCK_PARTS_REQ:
                repository.setStockParts(inMessage.getIntegerArrayParam(), inMessage.getBooleanParam1());
                outMessage = new Message(MessageType.REPOSITORY_SET_STOCK_PARTS_RESP);
                break;
            case REPOSITORY_ADD_MISSING_PART_REQ:
                repository.addMissingPart(inMessage.getIntegerParam1());
                outMessage = new Message(MessageType.REPOSITORY_ADD_MISSING_PART_RESP);
                break;
            case REPOSITORY_REMOVE_MISSING_PART_REQ:
                repository.removeMissingPart(inMessage.getIntegerParam1());
                outMessage = new Message(MessageType.REPOSITORY_REMOVE_MISSING_PART_RESP);
                break;
            case REPOSITORY_SET_MISSING_PART_ALERT_REQ:
                repository.setPartMissingAlert(inMessage.getIntegerParam1(), inMessage.getBooleanParam1(), inMessage.getBooleanParam2());
                outMessage = new Message(MessageType.REPOSITORY_SET_MISSING_PART_ALERT_RESP);
                break;
            case REPOSITORY_SET_SOLD_PARTS_REQ:
                repository.setSoldParts(inMessage.getIntegerArrayParam());
                outMessage = new Message(MessageType.REPOSITORY_SET_SOLD_PARTS_RESP);
                break;
        }

        return (outMessage);
    }
}
