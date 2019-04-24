package comm;

import entities.CustomerState;
import entities.ManagerState;
import entities.MechanicState;
import regions.ManagerTask;

import java.io.Serializable;
/**
        *  Este tipo de dados define uma mensagem para ser enviada
        *    @author Cristiano Vagos
        *    @author Miguel Brás
        */
public class Message implements Serializable {

    /**
     *  Chave de serialização
     *    @serialField serialVersionUID
     */

    private static final long serialVersionUID = 23050505L;

    private MessageType messageType;

    private boolean booleanParam = false;

    /*
     * Manager State
     */
    private ManagerState managerState;

    /*
     * Manager State
     */
    private MechanicState mechanicState;

    /*
     * Customer State
     */
    private CustomerState customerState;

    /*
     * Manager task
     */
    private ManagerTask managerTask;

    /*
     * Integer for param or returning values
     */
    private int integerParam1 = -1;

    /*
     * Integer for param or returning values
     */
    private int integerParam2 = -1;

    /*
     * Integer for param or returning values
     */
    private int integerParam3 = -1;

    /*
     * Array of integers
     */
    private int[] integerArrayParam = {-1};

    /**
     *  Instanciação de uma mensagem (forma 1).
     *
     *    @param type tipo da mensagem
     */
    public Message(MessageType type)
    {
        messageType = type;
    }

    /**
     *  Instanciação de uma mensagem (forma 2): resposta appraiseSit.
     *
     *      @param type tipo da mensagem
     *      @param mState estado do manager
     *      @param nextTask próxima tarefa do manager
     */
    public Message(MessageType type, ManagerState mState, ManagerTask nextTask)
    {
        messageType = type;
        managerState = mState;
        managerTask = nextTask;
    }

    /**
     *  Instanciação de uma mensagem (forma 3)
     *
     *      @param type tipo da mensagem
     *      @param bool valor boolean a passar na mensagem
     */
    public Message(MessageType type, boolean bool)
    {
        messageType = type;
        booleanParam = bool;
    }

    /**
     *  Instanciação de uma mensagem (forma 4): resposta talk to customer
     *
     *      @param type tipo da mensagem
     *      @param mState estado do manager
     *      @param cState estado do customer
     */
    public Message(MessageType type, ManagerState mState, CustomerState cState)
    {
        messageType = type;
        managerState = mState;
        customerState = cState;
    }

    /**
     *  Instanciação de uma mensagem (forma 5)
     *
     *      @param type tipo da mensagem
     *      @param mState estado do manager
     */
    public Message(MessageType type, ManagerState mState)
    {
        messageType = type;
        managerState = mState;
    }

    /**
     *  Instanciação de uma mensagem (forma 6)
     *
     *      @param type tipo da mensagem
     *      @param integer inteiro a passar na mensagem
     */
    public Message(MessageType type, int integer)
    {
        messageType = type;
        integerParam1 = integer;
    }

    /**
     *  Instanciação de uma mensagem (forma 7)
     *
     *      @param type tipo da mensagem
     *      @param integer1 primeiro inteiro a passar na mensagem
     *      @param integer2 segundo inteiro a passar na mensagem
     */
    public Message(MessageType type, int integer1, int integer2)
    {
        messageType = type;
        integerParam1 = integer1;
        integerParam2 = integer2;
    }

    /**
     *  Instanciação de uma mensagem (forma 8)
     *
     *      @param type tipo da mensagem
     *      @param mechState estado do mechanic
     */
    public Message(MessageType type, MechanicState mechState)
    {
        messageType = type;
        mechanicState = mechState;
    }

    /**
     *  Instanciação de uma mensagem (forma 9)
     *
     *      @param type tipo da mensagem
     *      @param cState estado do customer
     */
    public Message(MessageType type, CustomerState cState)
    {
        messageType = type;
        customerState = cState;
    }

    /**
     *  Instanciação de uma mensagem (forma 10)
     *
     *      @param type tipo da mensagem
     *      @param integer inteiro a passar
     *      @param bool boolean a passar
     */
    public Message(MessageType type, int integer, boolean bool)
    {
        messageType = type;
        integerParam1 = integer;
        booleanParam = bool;
    }

    /**
     *  Instanciação de uma mensagem (forma 11)
     *
     *      @param type tipo da mensagem
     *      @param cState customer State a passar
     *      @param integer integer a passar
     */
    public Message(MessageType type, CustomerState cState, int integer)
    {
        messageType = type;
        customerState = cState;
        integerParam1 = integer;
    }

    /**
     *  Instanciação de uma mensagem (forma 12)
     *
     *      @param type tipo da mensagem
     *      @param integerArray array de inteiros a passar
     *      @param mState estado do manager a passar
     */
    public Message(MessageType type, int [] integerArray, ManagerState mState)
    {
        messageType = type;
        integerArrayParam = integerArray ;
        managerState = mState;
    }

    /**
     *  Instanciação de uma mensagem (forma 13)
     *
     *      @param type tipo da mensagem
     *      @param mechState estado do mecanico a passar
     *      @param bool bool a passar
     */
    public Message(MessageType type, MechanicState mechState, boolean bool)
    {
        messageType = type;
        mechanicState = mechState;
        booleanParam = bool;
    }
    /**
     *  Instanciação de uma mensagem (forma 14)
     *
     *      @param type tipo da mensagem
     *      @param integer1 inteiro 1 a passar
     *      @param integer2 inteiro 2 a passar
     *      @param integer3 inteiro 3 a passar
     */
    public Message(MessageType type, int integer1, int integer2, int integer3)
    {
        messageType = type;
        integerParam1 =integer1;
        integerParam2 = integer2;
        integerParam3 = integer3;
    }

    /**
     *  Instanciação de uma mensagem (forma 15)
     *
     *      @param type tipo da mensagem
     *      @param integer1 inteiro 1 a passar
     *      @param mechState estado de mecanico a passar
     *      @param bool boolean a passar
     */
    public Message(MessageType type, MechanicState mechState, int integer1, boolean bool)
    {
        messageType = type;
        mechanicState = mechState;
        integerParam1 =integer1;
        booleanParam = bool;
    }

    /**
     *  Instanciação de uma mensagem (forma 16)
     *
     *      @param type tipo da mensagem
     *      @param integerArray array de inteiros a passar
     */
    public Message(MessageType type, int [] integerArray)
    {
        messageType = type;
        integerArrayParam = integerArray ;
    }

    /**
     *  Type of message
     *  @serialField messageType
     */
    public MessageType getMessageType() {
        return messageType;
    }

    /**
     * Generic boolean for param or returning values
     */
    public boolean isBooleanParam() {
        return booleanParam;
    }

    public ManagerState getManagerState() {
        return managerState;
    }

    public MechanicState getMechanicState() {
        return mechanicState;
    }

    public CustomerState getCustomerState() {
        return customerState;
    }

    public ManagerTask getManagerTask() {
        return managerTask;
    }

    public int getIntegerParam1() {
        return integerParam1;
    }

    public int getIntegerParam2() {
        return integerParam2;
    }

    public int getIntegerParam3() {
        return integerParam3;
    }

    public int[] getIntegerArrayParam() {
        return integerArrayParam;
    }
}
