package repository;

import comm.*;
import genclass.GenericIO;
import model.CustomerState;
import model.ManagerState;
import model.MechanicState;

/**
 * Classe GeneralRepository (Repositório Geral)<br>
 *
 * Esta classe é responsável pela comunicação com o servidor do serviço do Repositório Geral de Dados,
 * uma região partilhada do problema, feita através de passagem de mensagens, atuando
 * como um Stub para a classe real, sendo que são implementados os métodos do serviço
 * propriamente dito, através da sua interface.<br>
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public class GeneralRepository implements IGeneralRepository {
    /**
     *  Nome do sistema computacional onde está localizado o servidor.
     */
    private String serverHostName;

    /**
     *  Número do port de escuta do servidor.
     */
    private int serverPortNumb;

    /**
     *  Instanciação do stub.
     *
     *  @param hostName nome do sistema computacional onde está localizado o servidor
     *  @param port número do port de escuta do servidor
     */
    public GeneralRepository(String hostName, int port) {
        serverHostName = hostName;
        serverPortNumb = port;
    }

    /**
     * Operação initializeCustomer<br>
     *
     * Inicializa o estado de cada um dos Customers<br>
     *
     * @param index índice do cliente
     * @param requiresReplacement indicação se requer viatura de substituição
     */
    @Override
    public void initializeCustomer(int index, boolean requiresReplacement) {
        Message inMessage, outMessage;

        // criar mensagem
        outMessage = new Message(MessageType.REPOSITORY_INITIALIZE_CUSTOMER_REQ, index, requiresReplacement);
        inMessage = communicationWithServer(outMessage);    // enviar e receber mensagem de resposta

        // validar mensagem de resposta
        if(inMessage.getMessageType() != MessageType.REPOSITORY_INITIALIZE_CUSTOMER_RESP) {
            GenericIO.writelnString("Thread " + ( Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
    }

    /**
     * Operação setCustomerState<br>
     *
     * Altera o estado de um cliente em específico<br>
     *
     * @param index o índice do cliente em questão
     * @param newState o estado novo do cliente
     * @param print indicação se será impressa a linha de estado no ficheiro de logging
     */
    @Override
    public void setCustomerState(int index, CustomerState newState, boolean print) {
        Message inMessage, outMessage;

        // criar mensagem
        outMessage = new Message(MessageType.REPOSITORY_SET_CUSTOMER_STATE_REQ, index, newState, print);
        inMessage = communicationWithServer(outMessage);    // enviar e receber mensagem de resposta

        // validar mensagem de resposta
        if(inMessage.getMessageType() != MessageType.REPOSITORY_SET_CUSTOMER_STATE_RESP) {
            GenericIO.writelnString("Thread " + ( Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
    }

    /**
     * Operação setCustomerCar<br>
     *
     * Altera a viatura atual de um cliente em específico<br>
     *
     * @param index o índice do cliente em questão
     * @param carId o identificador da viatura
     *              (-1 se nenhuma, id do customer se a própria, ou 100..102 para viatura de substituição)
     * @param print indicação se será impressa a linha de estado no ficheiro de logging
     */
    @Override
    public void setCustomerCar(int index, int carId, boolean print) {
        Message inMessage, outMessage;

        // criar mensagem
        outMessage = new Message(MessageType.REPOSITORY_SET_CUSTOMER_CAR_REQ, index, carId, print);
        inMessage = communicationWithServer(outMessage);    // enviar e receber mensagem de resposta

        // validar mensagem de resposta
        if(inMessage.getMessageType() != MessageType.REPOSITORY_SET_CUSTOMER_CAR_RESP) {
            GenericIO.writelnString("Thread " + ( Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
    }

    /**
     * Operação setManagerState<br>
     *
     * Altera o estado do Manager<br>
     *
     * @param state estado do Manager
     * @param print indicação se será impressa a linha de estado no ficheiro de logging
     */
    @Override
    public void setManagerState(ManagerState state, boolean print) {
        Message inMessage, outMessage;

        // criar mensagem
        outMessage = new Message(MessageType.REPOSITORY_SET_MANAGER_STATE_REQ, state, print);
        inMessage = communicationWithServer(outMessage);    // enviar e receber mensagem de resposta

        // validar mensagem de resposta
        if(inMessage.getMessageType() != MessageType.REPOSITORY_SET_MANAGER_STATE_RESP) {
            GenericIO.writelnString("Thread " + ( Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
    }

    /**
     * Operação setMechanicState<br>
     *
     * Altera o estado de um mecânico em específico<br>
     *
     * @param index o índice do mecânico em questão
     * @param newState o estado novo do mecânico
     * @param print indicação se será impressa a linha de estado no ficheiro de logging
     */
    @Override
    public void setMechanicState(int index, MechanicState newState, boolean print) {
        Message inMessage, outMessage;

        // criar mensagem
        outMessage = new Message(MessageType.REPOSITORY_SET_MECHANIC_STATE_REQ, index, newState, print);
        inMessage = communicationWithServer(outMessage);    // enviar e receber mensagem de resposta

        // validar mensagem de resposta
        if(inMessage.getMessageType() != MessageType.REPOSITORY_SET_MECHANIC_STATE_RESP) {
            GenericIO.writelnString("Thread " + ( Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
    }

    /**
     * Operação customerCarEntersPark<br>
     *
     * Indica que uma viatura de um cliente entrou no park<br>
     */
    @Override
    public void customerCarEntersPark() {
        Message inMessage, outMessage;

        // criar mensagem
        outMessage = new Message(MessageType.REPOSITORY_CUSTOMER_CAR_ENTERS_PARK_REQ);
        inMessage = communicationWithServer(outMessage);    // enviar e receber mensagem de resposta

        // validar mensagem de resposta
        if(inMessage.getMessageType() != MessageType.REPOSITORY_CUSTOMER_CAR_ENTERS_PARK_RESP) {
            GenericIO.writelnString("Thread " + ( Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
    }

    /**
     * Operação customerCarLeavesPark<br>
     *
     * Indica que uma viatura de um cliente saiu do park<br>
     */
    @Override
    public void customerCarLeavesPark() {
        Message inMessage, outMessage;

        // criar mensagem
        outMessage = new Message(MessageType.REPOSITORY_CUSTOMER_CAR_LEAVES_PARK_REQ);
        inMessage = communicationWithServer(outMessage);    // enviar e receber mensagem de resposta

        // validar mensagem de resposta
        if(inMessage.getMessageType() != MessageType.REPOSITORY_CUSTOMER_CAR_LEAVES_PARK_RESP) {
            GenericIO.writelnString("Thread " + ( Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
    }

    /**
     * Operação replacementCarEntersPark<br>
     *
     * Indica que uma viatura de substituição entrou no park<br>
     */
    @Override
    public void replacementCarEntersPark() {
        Message inMessage, outMessage;

        // criar mensagem
        outMessage = new Message(MessageType.REPOSITORY_REPLACEMENT_CAR_ENTERS_PARK_REQ);
        inMessage = communicationWithServer(outMessage);    // enviar e receber mensagem de resposta

        // validar mensagem de resposta
        if(inMessage.getMessageType() != MessageType.REPOSITORY_REPLACEMENT_CAR_ENTERS_PARK_RESP) {
            GenericIO.writelnString("Thread " + ( Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
    }

    /**
     * Operação replacementCarLeavesPark<br>
     *
     * Indica que uma viatura de substituição saiu do park<br>
     */
    @Override
    public void replacementCarLeavesPark() {
        Message inMessage, outMessage;

        // criar mensagem
        outMessage = new Message(MessageType.REPOSITORY_REPLACEMENT_CAR_LEAVES_PARK_REQ);
        inMessage = communicationWithServer(outMessage);    // enviar e receber mensagem de resposta

        // validar mensagem de resposta
        if(inMessage.getMessageType() != MessageType.REPOSITORY_REPLACEMENT_CAR_LEAVES_PARK_RESP) {
            GenericIO.writelnString("Thread " + ( Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
    }

    /**
     * Operação setCustomersInQueue<br>
     *
     * Altera o valor dos clientes na fila no Lounge<br>
     *
     * @param customersInQueue número de clientes na fila no Lounge
     * @param print indicação se será impressa a linha de estado no ficheiro de logging
     */
    @Override
    public void setCustomersInQueue(int customersInQueue, boolean print) {
        Message inMessage, outMessage;

        // criar mensagem
        outMessage = new Message(MessageType.REPOSITORY_SET_CUSTOMERS_IN_QUEUE_REQ, customersInQueue, print);
        inMessage = communicationWithServer(outMessage);    // enviar e receber mensagem de resposta

        // validar mensagem de resposta
        if(inMessage.getMessageType() != MessageType.REPOSITORY_SET_CUSTOMERS_IN_QUEUE_RESP) {
            GenericIO.writelnString("Thread " + ( Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
    }

    /**
     * Operação setCustomersInQueueForKey<br>
     *
     * Altera o valor dos clientes na fila para obter uma chave para viatura de substituição no Lounge<br>
     *
     * @param customersInQueueForKey número de clientes na fila à espera de chave para viatura de substituição
     */
    @Override
    public void setCustomersInQueueForKey(int customersInQueueForKey) {
        Message inMessage, outMessage;

        // criar mensagem
        outMessage = new Message(MessageType.REPOSITORY_SET_CUSTOMERS_IN_QUEUE_FOR_KEY_REQ, customersInQueueForKey);
        inMessage = communicationWithServer(outMessage);    // enviar e receber mensagem de resposta

        // validar mensagem de resposta
        if(inMessage.getMessageType() != MessageType.REPOSITORY_SET_CUSTOMERS_IN_QUEUE_FOR_KEY_RESP) {
            GenericIO.writelnString("Thread " + ( Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
    }

    /**
     * Operação setTotalRepairedCars<br>
     *
     * Altera o valor do total de carros reparados<br>
     *
     * @param repairedCars total de carros reparados
     */
    @Override
    public void setTotalRepairedCars(int repairedCars) {
        Message inMessage, outMessage;

        // criar mensagem
        outMessage = new Message(MessageType.REPOSITORY_SET_TOTAL_REPAIRED_CARS_REQ, repairedCars);
        inMessage = communicationWithServer(outMessage);    // enviar e receber mensagem de resposta

        // validar mensagem de resposta
        if(inMessage.getMessageType() != MessageType.REPOSITORY_SET_TOTAL_REPAIRED_CARS_RESP) {
            GenericIO.writelnString("Thread " + ( Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
    }

    /**
     * Operação managerRequestedService<br>
     *
     * Incrementa o número de serviços pedidos pelo Manager na RepairArea<br>
     */
    @Override
    public void managerRequestedService() {
        Message inMessage, outMessage;

        // criar mensagem
        outMessage = new Message(MessageType.REPOSITORY_MANAGER_REQUESTED_SERVICE_REQ);
        inMessage = communicationWithServer(outMessage);    // enviar e receber mensagem de resposta

        // validar mensagem de resposta
        if(inMessage.getMessageType() != MessageType.REPOSITORY_MANAGER_REQUESTED_SERVICE_RESP) {
            GenericIO.writelnString("Thread " + ( Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
    }

    /**
     * Operação setStockParts<br>
     *
     * Altera o array referente ao número de peças em stock na RepairArea<br>
     *
     * @param stockParts lista com número de peças em stock
     */
    @Override
    public void setStockParts(int[] stockParts, boolean print) {
        Message inMessage, outMessage;

        // criar mensagem
        outMessage = new Message(MessageType.REPOSITORY_SET_STOCK_PARTS_REQ, stockParts, print);
        inMessage = communicationWithServer(outMessage);    // enviar e receber mensagem de resposta

        // validar mensagem de resposta
        if(inMessage.getMessageType() != MessageType.REPOSITORY_SET_STOCK_PARTS_RESP) {
            GenericIO.writelnString("Thread " + ( Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
    }

    /**
     * Operação addMissingPart<br>
     *
     * Marca uma dada peça como inexistente no stock da RepairArea<br>
     *
     * @param partIndex o índice da peça
     */
    @Override
    public void addMissingPart(int partIndex) {
        Message inMessage, outMessage;

        // criar mensagem
        outMessage = new Message(MessageType.REPOSITORY_ADD_MISSING_PART_REQ, partIndex);
        inMessage = communicationWithServer(outMessage);    // enviar e receber mensagem de resposta

        // validar mensagem de resposta
        if(inMessage.getMessageType() != MessageType.REPOSITORY_ADD_MISSING_PART_RESP) {
            GenericIO.writelnString("Thread " + ( Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
    }

    /**
     * Operação removeMissingPart<br>
     *
     * Marca uma dada peça como existente no stock da RepairArea<br>
     *
     * @param partIndex o índice da peça
     */
    @Override
    public void removeMissingPart(int partIndex) {
        Message inMessage, outMessage;

        // criar mensagem
        outMessage = new Message(MessageType.REPOSITORY_REMOVE_MISSING_PART_REQ, partIndex);
        inMessage = communicationWithServer(outMessage);    // enviar e receber mensagem de resposta

        // validar mensagem de resposta
        if(inMessage.getMessageType() != MessageType.REPOSITORY_REMOVE_MISSING_PART_RESP) {
            GenericIO.writelnString("Thread " + ( Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
    }

    /**
     * Operação setPartMissingAlert<br>
     *
     * Altera o valor de um dado índice do array de alerta de peças em falta<br>
     *
     * @param partIndex o índice da peça
     * @param value o novo valor
     * @param print indicação se será impressa a linha de estado no ficheiro de logging
     */
    @Override
    public void setPartMissingAlert(int partIndex, boolean value, boolean print) {
        Message inMessage, outMessage;

        // criar mensagem
        outMessage = new Message(MessageType.REPOSITORY_SET_MISSING_PART_ALERT_REQ, partIndex, value, print);
        inMessage = communicationWithServer(outMessage);    // enviar e receber mensagem de resposta

        // validar mensagem de resposta
        if(inMessage.getMessageType() != MessageType.REPOSITORY_SET_MISSING_PART_ALERT_RESP) {
            GenericIO.writelnString("Thread " + ( Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
    }

    /**
     * Operação setSoldParts<br>
     *
     * Altera o valor do array referente ao número de peças vendidas pela SupplierSite<br>
     *
     * @param soldParts array com o total de peças vendidas
     */
    @Override
    public void setSoldParts(int[] soldParts) {
        Message inMessage, outMessage;

        // criar mensagem
        outMessage = new Message(MessageType.REPOSITORY_SET_SOLD_PARTS_REQ, soldParts);
        inMessage = communicationWithServer(outMessage);    // enviar e receber mensagem de resposta

        // validar mensagem de resposta
        if(inMessage.getMessageType() != MessageType.REPOSITORY_SET_SOLD_PARTS_RESP) {
            GenericIO.writelnString("Thread " + ( Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
    }

    /**
     * Operação setCustomerCarRepaired<br>
     *
     * Marca um dado carro como reparado
     *
     * @param carIndex o índice do carro
     */
    @Override
    public void setCustomerCarRepaired(int carIndex) {
        Message inMessage, outMessage;

        // criar mensagem
        outMessage = new Message(MessageType.REPOSITORY_SET_CUSTOMER_CAR_REPAIRED_REQ, carIndex);
        inMessage = communicationWithServer(outMessage);    // enviar e receber mensagem de resposta

        // validar mensagem de resposta
        if(inMessage.getMessageType() != MessageType.REPOSITORY_SET_CUSTOMER_CAR_REPAIRED_RESP) {
            GenericIO.writelnString("Thread " + ( Thread.currentThread()).getName()+ ": Tipo inválido!");
            GenericIO.writelnString(inMessage.toString());
            System.exit(1);
        }
    }

    /**
     * Comunicação com o servidor do Repository.
     * Envia e recebe mensagem de resposta
     *
     * @param messageToSend mensagem a ser enviada para o servidor
     * @return mensagem de resposta vinda do servidor
     */
    private Message communicationWithServer(Message messageToSend) {
        ClientCom com = new ClientCom(serverHostName, serverPortNumb);
        Message fromServer;      //input
        Message fromUser;       //output

        //enquanto a ligação não estiver establecida
        //a thread vai "dormir" até establecer a ligação
        while(!com.open()) {
            try {
                Thread.currentThread ().sleep ((long) (10));
            }
            catch (InterruptedException e) {}
        }

        //Message to Send
        fromUser = messageToSend;

        //Send Message
        com.writeObject(fromUser);

        //receive message
        fromServer = (Message)com.readObject();

        //close communications
        com.close();

        //return object
        return fromServer;
    }
}
