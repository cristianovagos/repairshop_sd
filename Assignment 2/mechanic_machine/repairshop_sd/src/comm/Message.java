package comm;

import model.CustomerState;
import model.ManagerState;
import model.ManagerTask;
import model.MechanicState;

import java.io.Serializable;
import java.util.Arrays;

/**
 *  Este tipo de dados define uma mensagem para ser enviada
 *
 *  @author Cristiano Vagos
 *  @author Miguel Brás
 */
public class Message implements Serializable {

    /**
     * Chave de serialização
     */
    private static final long serialVersionUID = 25042019L;

    /**
     * Tipo de mensagem
     */
    private MessageType messageType;

    /**
     * Parâmetro 1 do tipo Booleano
     */
    private Boolean booleanParam1 = null;

    /**
     * Parâmetro 2 do tipo Booleano
     */
    private Boolean booleanParam2 = null;

    /**
     * Estado interno do Manager
     */
    private ManagerState managerState;

    /**
     * Estado interno do Mecanico
     */
    private MechanicState mechanicState;

    /**
     * Estado interno do Cliente
     */
    private CustomerState customerState;

    /**
     * Tarefa do Manager
     */
    private ManagerTask managerTask;

    /**
     * Parâmetro 1 do tipo inteiro
     */
    private int integerParam1 = -1;

    /**
     * Parâmetro 2 do tipo inteiro
     */
    private int integerParam2 = -1;

    /**
     * Parâmetro 3 do tipo inteiro
     */
    private int integerParam3 = -1;

    /**
     * Parâmetro do tipo array de inteiros
     */
    private int[] integerArrayParam = {-1};

    /**
     * Instanciação de uma mensagem (forma 1)
     *
     * @param type tipo da mensagem
     */
    public Message(MessageType type) {
        messageType = type;
    }

    /**
     * Instanciação de uma mensagem (forma 2)
     *
     * @param type tipo da mensagem
     * @param mState estado do manager
     * @param nextTask próxima tarefa do manager
     */
    public Message(MessageType type, ManagerState mState, ManagerTask nextTask) {
        messageType = type;
        managerState = mState;
        managerTask = nextTask;
    }

    /**
     * Instanciação de uma mensagem (forma 3)
     *
     * @param type tipo da mensagem
     * @param bool valor boolean a passar na mensagem
     */
    public Message(MessageType type, Boolean bool) {
        messageType = type;
        booleanParam1 = bool;
    }

    /**
     * Instanciação de uma mensagem (forma 4)
     *
     * @param type tipo da mensagem
     * @param mState estado do manager a passar na mensagem
     * @param cState estado do customer a passar na mensagem
     */
    public Message(MessageType type, ManagerState mState, CustomerState cState) {
        messageType = type;
        managerState = mState;
        customerState = cState;
    }

    /**
     * Instanciação de uma mensagem (forma 5)
     *
     * @param type tipo da mensagem
     * @param mState estado do manager a passar na mensagem
     */
    public Message(MessageType type, ManagerState mState) {
        messageType = type;
        managerState = mState;
    }

    /**
     * Instanciação de uma mensagem (forma 6)
     *
     * @param type tipo da mensagem
     * @param integer inteiro a passar na mensagem
     */
    public Message(MessageType type, int integer) {
        messageType = type;
        integerParam1 = integer;
    }

    /**
     * Instanciação de uma mensagem (forma 7)
     *
     * @param type tipo da mensagem
     * @param integer1 primeiro inteiro a passar na mensagem
     * @param integer2 segundo inteiro a passar na mensagem
     */
    public Message(MessageType type, int integer1, int integer2) {
        messageType = type;
        integerParam1 = integer1;
        integerParam2 = integer2;
    }

    /**
     * Instanciação de uma mensagem (forma 8)
     *
     * @param type tipo da mensagem
     * @param mechState estado do mechanic a passar na mensagem
     */
    public Message(MessageType type, MechanicState mechState) {
        messageType = type;
        mechanicState = mechState;
    }

    /**
     * Instanciação de uma mensagem (forma 9)
     *
     * @param type tipo da mensagem
     * @param cState estado do customer a passar na mensagem
     */
    public Message(MessageType type, CustomerState cState) {
        messageType = type;
        customerState = cState;
    }

    /**
     * Instanciação de uma mensagem (forma 10)
     *
     * @param type tipo da mensagem
     * @param integer valor inteiro a passar na mensagem
     * @param bool valor booleano a passar na mensagem
     */
    public Message(MessageType type, int integer, Boolean bool) {
        messageType = type;
        integerParam1 = integer;
        booleanParam1 = bool;
    }

    /**
     * Instanciação de uma mensagem (forma 11)
     *
     * @param type tipo da mensagem
     * @param cState estado do cliente a passar na mensagem
     * @param integer valor inteiro a passar na mensagem
     */
    public Message(MessageType type, CustomerState cState, int integer) {
        messageType = type;
        customerState = cState;
        integerParam1 = integer;
    }

    /**
     * Instanciação de uma mensagem (forma 12)
     *
     * @param type tipo da mensagem
     * @param integerArray array de inteiros a passar na mensagem
     * @param mState estado do manager a passar na mensagem
     */
    public Message(MessageType type, int [] integerArray, ManagerState mState) {
        messageType = type;
        integerArrayParam = integerArray;
        managerState = mState;
    }

    /**
     * Instanciação de uma mensagem (forma 13)
     *
     * @param type tipo da mensagem
     * @param mechState estado do mecanico a passar na mensagem
     * @param bool valor booleano a passar na mensagem
     */
    public Message(MessageType type, MechanicState mechState, Boolean bool) {
        messageType = type;
        mechanicState = mechState;
        booleanParam1 = bool;
    }
    /**
     * Instanciação de uma mensagem (forma 14)
     *
     * @param type tipo da mensagem
     * @param integer1 valor inteiro a passar na mensagem
     * @param integer2 valor inteiro a passar na mensagem
     * @param integer3 valor inteiro a passar na mensagem
     */
    public Message(MessageType type, int integer1, int integer2, int integer3) {
        messageType = type;
        integerParam1 = integer1;
        integerParam2 = integer2;
        integerParam3 = integer3;
    }

    /**
     * Instanciação de uma mensagem (forma 15)
     *
     * @param type tipo da mensagem
     * @param integer1 valor inteiro a passar na mensagem
     * @param mechState estado do mecanico a passar na mensagem
     * @param bool valor booleano a passar na mensagem
     */
    public Message(MessageType type, MechanicState mechState, int integer1, Boolean bool) {
        messageType = type;
        mechanicState = mechState;
        integerParam1 = integer1;
        booleanParam1 = bool;
    }

    /**
     * Instanciação de uma mensagem (forma 16)
     *
     * @param type tipo da mensagem
     * @param integerArray array de inteiros a passar na mensagem
     */
    public Message(MessageType type, int[] integerArray) {
        messageType = type;
        integerArrayParam = integerArray;
    }

    /**
     * Instanciação de uma mensagem (forma 17)
     *
     * @param type tipo da mensagem
     * @param notendofday indicação de fim do dia de trabalho a passar na mensagem
     * @param firstrun indicação de primeira execução do programa a passar na mensagem
     */
    public Message(MessageType type, Boolean notendofday, Boolean firstrun) {
        messageType = type;
        booleanParam1 = notendofday;
        booleanParam2 = firstrun;
    }

    /**
     * Instanciação de uma mensagem (forma 18)
     *
     * @param type tipo da mensagem
     * @param mState estado do manager a passar na mensagem
     * @param firstrun indicação de primeira execução do programa a passar na mensagem
     */
    public Message(MessageType type, ManagerState mState, Boolean firstrun) {
        messageType = type;
        managerState = mState;
        booleanParam1 = firstrun;
    }

    /**
     * Instanciação de uma mensagem (forma 19)
     *
     * @param type tipo da mensagem
     * @param index indice do cliente a passar na mensagem
     * @param cState novo estado do cliente a passar na mensagem
     * @param print indica se a operação deve atualizar o ficheiro de logging
     */
    public Message(MessageType type, int index, CustomerState cState, Boolean print) {
        messageType = type;
        integerParam1 = index;
        customerState = cState;
        booleanParam1 = print;
    }

    /**
     * Instanciação de uma mensagem (forma 20)
     *
     * @param type tipo da mensagem
     * @param index indice do cliente a passar na mensagem
     * @param carId novo id do carro do cliente a passar na mensagem
     * @param print indica se a operação deve atualizar o ficheiro de logging
     */
    public Message(MessageType type, int index, int carId, Boolean print) {
        messageType = type;
        integerParam1 = index;
        integerParam2 = carId;
        booleanParam1 = print;
    }

    /**
     * Instanciação de uma mensagem (forma 21)
     *
     * @param type tipo da mensagem
     * @param index indice do mecanico a passar na mensagem
     * @param mState novo estado do mecanico a passar na mensagem
     * @param print indica se a operação deve atualizar o ficheiro de logging
     */
    public Message(MessageType type, int index, MechanicState mState, Boolean print) {
        messageType = type;
        integerParam1 = index;
        mechanicState = mState;
        booleanParam1 = print;
    }

    /**
     * Instanciação de uma mensagem (forma 22)
     *
     * @param type tipo da mensagem
     * @param stockParts array de pecas em stock
     * @param print indica se a operação deve atualizar o ficheiro de logging
     */
    public Message(MessageType type, int[] stockParts, Boolean print) {
        messageType = type;
        integerArrayParam = stockParts;
        booleanParam1 = print;
    }

    /**
     * Instanciação de uma mensagem (forma 23)
     *
     * @param type tipo da mensagem
     * @param partIndex indice da peca a passar na mensagem
     * @param value novo valor da peca a passar na mensagem
     * @param print indica se a operação deve atualizar o ficheiro de logging
     */
    public Message(MessageType type, int partIndex, Boolean value, Boolean print) {
        messageType = type;
        integerParam1 = partIndex;
        booleanParam1 = value;
        booleanParam2 = print;
    }

    /**
     * Instanciação de uma mensagem (forma 24)
     *
     * @param type tipo da mensagem
     * @param state estado do manager a passar na mensagem
     * @param task tarefa do manager a passar na mensagem
     * @param index indice do cliente a ser atendido
     */
    public Message(MessageType type, ManagerState state, ManagerTask task, int index) {
        messageType = type;
        managerState = state;
        managerTask = task;
        integerParam1 = index;
    }

    /**
     * Instanciação de uma mensagem (forma 25)
     *
     * @param type tipo da mensagem
     * @param mState estado do manager a passar na mensagem
     * @param cState estado do cliente a passar na mensagem
     * @param index indice do cliente a ser atendido
     */
    public Message(MessageType type, ManagerState mState, CustomerState cState, int index) {
        messageType = type;
        managerState = mState;
        customerState = cState;
        integerParam1 = index;
    }

    /**
     * Instanciação de uma mensagem (forma 26)
     *
     * @param type tipo da mensagem
     * @param mState estado do mecanico a passar na mensagem
     * @param endOfDay indicacao se o dia de trabalho acabou
     * @param firstRun indicacao se é a primeira execucao do programa
     */
    public Message(MessageType type, MechanicState mState, Boolean endOfDay, Boolean firstRun) {
        messageType = type;
        mechanicState = mState;
        booleanParam1 = endOfDay;
        booleanParam2 = firstRun;
    }

    /**
     * Instanciação de uma mensagem (forma 27)
     *
     * @param type tipo da mensagem
     * @param mState estado do mecanico a passar na mensagem
     * @param currentCarFixingId id do carro a ser arranjado
     */
    public Message(MessageType type, MechanicState mState, int currentCarFixingId) {
        messageType = type;
        mechanicState = mState;
        integerParam1 = currentCarFixingId;
    }

    /**
     * Instanciação de uma mensagem (forma 28)
     *
     * @param type tipo da mensagem
     * @param mState estado do manager a passar na mensagem
     * @param newParts cesto de compras com novas pecas
     */
    public Message(MessageType type, ManagerState mState, int[] newParts) {
        messageType = type;
        managerState = mState;
        integerArrayParam = newParts;
    }

    /**
     * Instanciação de uma mensagem (forma 29)
     *
     * @param type tipo da mensagem
     * @param mState estado do manager a passar na mensagem
     * @param firstRun indicacao se e primeira execucao
     * @param endOfDay indicacao se o dia de trabalho terminou
     */
    public Message(MessageType type, ManagerState mState, Boolean firstRun, Boolean endOfDay) {
        messageType = type;
        managerState = mState;
        booleanParam1 = firstRun;
        booleanParam2 = endOfDay;
    }

    /**
     * Obtem tipo da mensagem
     * @return tipo da mensagem
     */
    public MessageType getMessageType() {
        return messageType;
    }

    /**
     * Obtem estado interno do Manager
     * @return estado interno do Manager
     */
    public ManagerState getManagerState() {
        return managerState;
    }

    /**
     * Obtém o estado interno do Mecanico
     * @return estado interno do Mecanico
     */
    public MechanicState getMechanicState() {
        return mechanicState;
    }

    /**
     * Obtém o estado interno do Cliente
     * @return estado interno do Cliente
     */
    public CustomerState getCustomerState() {
        return customerState;
    }

    /**
     * Obtém a tarefa a ser executada pelo Manager
     * @return tarefa a ser executada pelo Manager
     */
    public ManagerTask getManagerTask() {
        return managerTask;
    }

    /**
     * Obtém o parâmetro 1 do tipo inteiro
     * @return parâmetro inteiro 1
     */
    public int getIntegerParam1() {
        return integerParam1;
    }

    /**
     * Obtém o parâmetro 2 do tipo inteiro
     * @return parâmetro inteiro 2
     */
    public int getIntegerParam2() {
        return integerParam2;
    }

    /**
     * Obtém o parâmetro 3 do tipo inteiro
     * @return parâmetro inteiro 3
     */
    public int getIntegerParam3() {
        return integerParam3;
    }

    /**
     * Obtém o parâmetro do tipo array de inteiros
     * @return parâmetro do tipo array de inteiros
     */
    public int[] getIntegerArrayParam() {
        return integerArrayParam;
    }

    /**
     * Obtém o parâmetro 1 do tipo booleano
     * @return parâmetro 1 do tipo booleano
     */
    public Boolean getBooleanParam1() {
        return booleanParam1;
    }

    /**
     * Obtém o parâmetro 2 do tipo booleano
     * @return parâmetro 2 do tipo booleano
     */
    public Boolean getBooleanParam2() {
        return booleanParam2;
    }


    /**
     *  Impressão dos campos internos.
     *  Usada para fins de debugging.
     *
     *  @return string contendo, em linhas separadas, a concatenação da identificação de cada campo e valor respectivo
     */
    @Override
    public String toString() {
        return ("Message{" +
                "\nmessageType = " + messageType +
                "\nbooleanParam1 = " + booleanParam1 +
                "\nbooleanParam2 = " + booleanParam2 +
                "\nmanagerState = " + managerState +
                "\nmechanicState = " + mechanicState +
                "\ncustomerState = " + customerState +
                "\nmanagerTask = " + managerTask +
                "\nintegerParam1 = " + integerParam1 +
                "\nintegerParam2 = " + integerParam2 +
                "\nintegerParam3 = " + integerParam3 +
                "\nintegerArrayParam = " + Arrays.toString(integerArrayParam) +
                "\n}");
    }
}
