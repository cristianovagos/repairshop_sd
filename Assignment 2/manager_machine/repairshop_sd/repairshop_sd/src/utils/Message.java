package utils;

import entities.CustomerState;
import entities.ManagerState;
import regions.ManagerTask;

import java.awt.*;
import java.io.Serializable;
/**
        *  Este tipo de dados define uma mensagem para ser enviada
        *    @author Cristiano Vagos
        *    @author Miguel Brás
        */
public class Message implements Serializable {

    private static final long serialVersionUID = 23050505L;

    /*
     *  Type of message
     */
    private MessageType messageType;

    /*
     * Generic boolean for param or returning values
     */
    private boolean genericBoolean = false;

    /*
     * Generic String for method name
     */
    private String methodName = "";

    /*
     * Manager State
     */
    private ManagerState managerState;

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
    private int genericInteger;

    /*
     * Array of integers
     */
    private int[] genericIntegerArray;





    public Message(MessageType type, String method)
    {
        messageType = type;
        methodName = method;
    }

    public Message(MessageType type, String method, Boolean param1)
    {
        messageType = type;
        methodName = method;
        genericBoolean = param1;
    }

    public Message(MessageType type, String method, int param1)
    {
        messageType = type;
        methodName = method;
        genericInteger = param1;
    }

    public Message(MessageType type, String method, int [] array)
    {
        messageType = type;
        methodName = method;
        genericIntegerArray = array;
    }


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public boolean getGenericBoolean() {
        return genericBoolean;
    }

    public String getMethodName() {
        return methodName;
    }

    public ManagerState getManagerState() {
        return managerState;
    }

    public CustomerState getCustomerState() {
        return customerState;
    }

    public ManagerTask getManagerTask() {
        return managerTask;
    }

    public int getGenericInteger() {
        return genericInteger;
    }

    public int[] getGenericIntegerArray() {
        return genericIntegerArray;
    }
}
