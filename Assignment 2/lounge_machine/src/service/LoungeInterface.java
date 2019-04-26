package service;

import comm.*;
import model.CustomerState;
import model.ManagerState;
import model.ManagerTask;
import model.MechanicState;
import service.proxy.ClientProxy;
import utils.Constants;

/**
 * Este tipo de dados define o interface ao Lounge, uma das regiões partilhadas do problema Repair Shop Activities,
 * que implementa o modelo cliente-servidor de tipo 2 (replicação do servidor).
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public class LoungeInterface {
    /**
     *  Instanciação do interface ao Lounge.
     *  @param lounge Lounge
     */
    public LoungeInterface(Lounge lounge) {
        this.lounge = lounge;
    }

    /**
     *  Lounge (representa o serviço a ser prestado)
     */
    private Lounge lounge;

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
            case LOUNGE_GET_NEXT_TASK_REQ:
                if ((inMessage.getBooleanParam1() == null))
                    throw new MessageException("Indicacao de primeira execucao invalida!", inMessage);
                else
                    ((ClientProxy) Thread.currentThread()).setFirstRun(inMessage.getBooleanParam1());
                break;
            case LOUNGE_RECEIVE_PAYMENT_REQ:
            case LOUNGE_TALK_WITH_MANAGER_REQ:
            case LOUNGE_COLLECT_KEY_REQ:
            case LOUNGE_PAY_FOR_THE_SERVICE_REQ:
                if ((inMessage.getIntegerParam1() < 0) || (inMessage.getIntegerParam1() >= Constants.NUM_CUSTOMERS))
                    throw new MessageException("Id do cliente invalido!", inMessage);
                else
                    ((ClientProxy) Thread.currentThread()).setCustomerId(inMessage.getIntegerParam1());
                break;
            case LOUNGE_LET_MANAGER_KNOW_REQ:
                if ((inMessage.getIntegerParam1() < 0) || (inMessage.getIntegerParam1() >= Constants.NUM_PARTS))
                    throw new MessageException("Id da peca invalido!", inMessage);
                if ((inMessage.getIntegerParam2() < 0) || (inMessage.getIntegerParam2() >= Constants.NUM_MECHANICS))
                    throw new MessageException("Id do mecanico invalido!", inMessage);
                else
                    ((ClientProxy) Thread.currentThread()).setMechanicId(inMessage.getIntegerParam2());
                break;
            case LOUNGE_REPAIR_CONCLUDED_REQ:
                if ((inMessage.getIntegerParam1() < 0) || (inMessage.getIntegerParam1() >= Constants.NUM_CUSTOMERS))
                    throw new MessageException("Id do carro invalido!", inMessage);
                else
                    ((ClientProxy) Thread.currentThread()).setCurrentCarFixingId(inMessage.getIntegerParam1());
                if ((inMessage.getIntegerParam2() < 0) || (inMessage.getIntegerParam2() >= Constants.NUM_MECHANICS))
                    throw new MessageException("Id do mecanico invalido!", inMessage);
                else
                    ((ClientProxy) Thread.currentThread()).setMechanicId(inMessage.getIntegerParam2());
                break;
            case LOUNGE_QUEUE_IN_REQ:
                if ((inMessage.getIntegerParam1() < 0) || (inMessage.getIntegerParam1() >= Constants.NUM_CUSTOMERS))
                    throw new MessageException("Id do cliente invalido!", inMessage);
                else
                    ((ClientProxy) Thread.currentThread()).setCustomerId(inMessage.getIntegerParam1());
                if (inMessage.getBooleanParam1() == null)
                    throw new MessageException("Indicacao de conclusao de reparacao invalida!", inMessage);
                break;
            case LOUNGE_TALK_TO_CUSTOMER_REQ:
            case LOUNGE_APPRAISE_SIT_REQ:
            case LOUNGE_HAND_CAR_KEY_REQ:
                // nothing to do
                break;
            case NONE:
            default:
                throw new MessageException("Tipo de mensagem invalida!", inMessage);
        }

        /* seu processamento */

        switch (inMessage.getMessageType()) {
            case LOUNGE_APPRAISE_SIT_REQ:
                ManagerTask task = lounge.appraiseSit();                                // proxima tarefa do Manager
                ManagerState state = ((ClientProxy) Thread.currentThread()).getManagerState();
                if (task == ManagerTask.PHONE_CUSTOMER) {
                    int attendingCustomer = ((ClientProxy) Thread.currentThread()).getCurrentlyAttendingCustomer();
                    outMessage = new Message(MessageType.LOUNGE_APPRAISE_SIT_RESP, state, task, attendingCustomer);
                }
                else
                    outMessage = new Message(MessageType.LOUNGE_APPRAISE_SIT_RESP, state, task);
                break;
            case LOUNGE_GET_NEXT_TASK_REQ:
                boolean endOfDay = lounge.getNextTask();                                // proxima tarefa do Manager
                ManagerState state1 = ((ClientProxy) Thread.currentThread()).getManagerState();
                boolean firstRun = ((ClientProxy) Thread.currentThread()).getFirstRun();
                outMessage = new Message(MessageType.LOUNGE_GET_NEXT_TASK_RESP, state1, firstRun, endOfDay); // gerar resposta
                break;
            case LOUNGE_TALK_TO_CUSTOMER_REQ:
                CustomerState customerState = lounge.talkToCustomer();                  // Manager fala com o Cliente
                ManagerState state2 = ((ClientProxy) Thread.currentThread()).getManagerState();
                if (customerState == CustomerState.RECEPTION_PAYING || customerState == CustomerState.RECEPTION_REPAIR) {
                    int attendingCustomer = ((ClientProxy) Thread.currentThread()).getCurrentlyAttendingCustomer();
                    outMessage = new Message(MessageType.LOUNGE_TALK_TO_CUSTOMER_RESP, state2, customerState, attendingCustomer);
                }
                else
                    outMessage = new Message(MessageType.LOUNGE_TALK_TO_CUSTOMER_RESP, state2, customerState);// gerar resposta
                break;
            case LOUNGE_HAND_CAR_KEY_REQ:
                lounge.handCarKey();                                                    // Manager da chave ao Cliente
                ManagerState state3 = ((ClientProxy) Thread.currentThread()).getManagerState();
                outMessage = new Message(MessageType.LOUNGE_HAND_CAR_KEY_RESP, state3); // gerar resposta
                break;
            case LOUNGE_RECEIVE_PAYMENT_REQ:
                lounge.receivePayment(inMessage.getIntegerParam1());                       // Manager recebe pagamento
                ManagerState state4 = ((ClientProxy) Thread.currentThread()).getManagerState();
                outMessage = new Message(MessageType.LOUNGE_RECEIVE_PAYMENT_RESP, state4); // gerar resposta
                break;
            case LOUNGE_LET_MANAGER_KNOW_REQ:
                lounge.letManagerKnow(inMessage.getIntegerParam1());                           // Mecanico alerta o Manager de falta de pecas
                MechanicState mechanicState = ((ClientProxy) Thread.currentThread()).getMechanicState();
                outMessage = new Message(MessageType.LOUNGE_LET_MANAGER_KNOW_RESP, mechanicState); // gerar resposta
                break;
            case LOUNGE_REPAIR_CONCLUDED_REQ:
                lounge.repairConcluded();                                               // Mecanico acabou a reparacao
                MechanicState mechanicState2 = ((ClientProxy) Thread.currentThread()).getMechanicState();
                outMessage = new Message(MessageType.LOUNGE_REPAIR_CONCLUDED_RESP, mechanicState2);               // gerar resposta
                break;
            case LOUNGE_QUEUE_IN_REQ:
                lounge.queueIn(inMessage.getBooleanParam1());                         // Cliente esta na fila
                CustomerState customerState2 = ((ClientProxy) Thread.currentThread()).getCustomerState();
                outMessage = new Message(MessageType.LOUNGE_QUEUE_IN_RESP, customerState2); // gerar resposta
                break;
            case LOUNGE_TALK_WITH_MANAGER_REQ:
                lounge.talkWithManager();                                               // Cliente fala com o Manager
                int customerCar = ((ClientProxy) Thread.currentThread()).getCustomerCarId();
                CustomerState customerState3 = ((ClientProxy) Thread.currentThread()).getCustomerState();
                outMessage = new Message(MessageType.LOUNGE_TALK_WITH_MANAGER_RESP, customerState3, customerCar); // gerar resposta
                break;
            case LOUNGE_COLLECT_KEY_REQ:
                int key = lounge.collectKey();                                          // Cliente recolhe chave
                CustomerState customerState4 = ((ClientProxy) Thread.currentThread()).getCustomerState();
                outMessage = new Message(MessageType.LOUNGE_COLLECT_KEY_RESP, customerState4, key); // gerar resposta
                break;
            case LOUNGE_PAY_FOR_THE_SERVICE_REQ:
                lounge.payForTheService();                                              // Cliente paga pelo servico
                ManagerState state5 = ((ClientProxy) Thread.currentThread()).getManagerState();
                outMessage = new Message(MessageType.LOUNGE_PAY_FOR_THE_SERVICE_RESP, state5); // gerar resposta
                break;
        }

        return (outMessage);
    }
}
