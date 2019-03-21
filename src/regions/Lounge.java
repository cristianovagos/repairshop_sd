package regions;

import java.util.Queue;

import entities.Customer;
import entities.CustomerState;
import entities.Manager;
import entities.ManagerState;
import entities.Mechanic;

/**
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public class Lounge {
    /*
    *   The current customer state
    */
    CustomerState currentCustomerState;

    /**
     * Indicia a chave que está a ser dada
     * @serialField carKeyToHandle
     */
    int carKeyToHandle;


    /**
     * Indicia o numero total de carros reparados no dia
     * @serialField nTotalRepairedCars
     */
    int nTotalRepairedCars;

    /**
     *  Keys for the Replacment Cars
     *
     *    @serialField missingParts
     */
    int [] replacementKeys = {100,101,102};

    /**
     * Current client wants replacment car
     * @serialField wantsReplacement
     */
    boolean wantsReplacement;

    /**
    *  number of Customer waiting for keys
    *
    *    @serialField nCostumerForKey
    */
    int nCustomerForKey;   

    /**
    *  Customer waiting for keys
    *
    *    @serialField clientsWaitingForKey
    */
    Queue <Integer> costumersWaitingForKey; 

    /**
     * Repositorio que armazena informação atual do sistema
     * @serialField repository
     */
    GeneralRepository repository;

      /**
     * Variáveis para interação entre manager e cliente
     * @serialField repository
     */
    boolean isCustomerTaskComplete;
    boolean isManagerTaskComplete;
    /**
   *  Cliente a ser atendido
   *
   *    @serialField nextCostumer
   */
    int nextCustomer;
  /**
   *  Costumer Queue
   *
   *    @serialField costumerQueue
   */
    Queue <Integer> costumerQueue;
    
   /**
   *  Missing Part
   *
   *    @serialField missingParts
   */
    boolean [] partsNeedRestocking = {false,false, false};
    
   /**
   *  Customer List Registry
   *
   *    @serialField customerRegistry
   */
   Queue <Integer> repairedCars;
   
   /**
   *  Number of Requests
   *
   *    @serialField nRequests
   */
  int nRequests;
   
  /**
   *  Customer Requests
   *
   *    @serialField costumerInQueue
   */
  int nCostumerInQueue;



  /**
   *  Indica a quantidade de carros de substituição disponivel.
   *
   *    @serialField replacementCarAvailable
   */
  int nReplacementCarAvailable;

   /**
   *  Indica se há peças que necessitem de supply.
   *
   *    @serialField replacementCarAvailable
   */
  boolean needsRessuplyParts;

   /**
   *  Indica o numero de carros reparados.
   *
   *    @serialField nRepairArea
   */
  int nRepairedCar;

  /**
   *  Instanciação do Lounge.
   *
   */
    public Lounge(GeneralRepository _repository)
    {
        nextCustomer = 0;
        isCustomerTaskComplete = isManagerTaskComplete = false;
        repository = _repository;
        nReplacementCarAvailable = 3;
        needsRessuplyParts = false;
        carKeyToHandle = -1;
        wantsReplacement = false;
        nCostumerInQueue = 0;
    }


    /**
     * Operação queueIn (chamada pelo {@link entities.Customer})
     *
     * Aqui, o cliente vai entrar na fila.
     *  {@link entities.Manager} avisa quando chegar a vez de o cliente ser atendido
     */
    public synchronized void queueIn()
    {     
        int customerId = ((Customer) Thread.currentThread()).getCustomerId();

        //change customer state
        costumerQueue.add(customerId);
        
        int currentCarID = ((Customer) Thread.currentThread()).getCarId();
        //if currentKey is equal to the costumer id, then customer arrives with its car and wants to repair it
        //else 
        ((Customer) Thread.currentThread()).setState(currentCarID == customerId ? CustomerState.RECEPTION_REPAIR : CustomerState.RECEPTION_PAYING);
        nCostumerInQueue++;
        //TODO: update repository

        //increase the number of requests and notify
        nRequests++;
        notifyAll();

        // block on condition variable
        while (customerId != nextCustomer) {
            try {
                wait();
            } catch (InterruptedException e) { }
        }
        nCostumerInQueue--;
    }

    /**
     * Operação collectKey (chamada pelo {@link entities.Customer})
     *
     * Aqui, o cliente vai receber a chave do carro dada pelo {@link entities.Manager}.
     * Devolve um int que representa a chave do carro.
     */
    public synchronized int collectKey() {
        int carKey = -1;

        int customerId = ((Customer) Thread.currentThread()).getCustomerId();

        // wait until manager searches for keys
        while (!isManagerTaskComplete) {
            try {
                wait();
            } catch (InterruptedException e) { }
        }

        if(carKeyToHandle != -1)
        {
            carKey = carKeyToHandle;
            carKeyToHandle = -1;

            isCustomerTaskComplete = true;
            notifyAll();

            return carKey;
        }
        //change customer state
        ((Customer) Thread.currentThread()).setState(CustomerState.WAITING_FOR_REPLACE_CAR);
        //TODO: UPDATE REPOSITORY

        costumersWaitingForKey.add(customerId);
        nCustomerForKey++;

        //increase the number of requests and notify
        nRequests++;
        notifyAll();

        // block on condition variable
        while ((nextCustomer != customerId) && (carKeyToHandle == -1)) {
            try {
                wait();
            } catch (InterruptedException e) { }
        }
        nCustomerForKey--;
        carKey = carKeyToHandle;
        carKeyToHandle = -1;

        
        isCustomerTaskComplete = true;
        notifyAll();

        return carKey;
    }

    /**
     * Operação talkWithmanager (chamada pelo {@link entities.Customer})
     *
     * Aqui, o cliente vai falar com {@link entities.Manager}.
     * O Cliente também avisa o manager se este pretende um  replacement car.
     */
    public synchronized void talkWithManager()
    {
        currentCustomerState = ((Customer)Thread.currentThread()).getCustomerState();

        wantsReplacement = ((Customer) Thread.currentThread()).getWantsReplacementCar();
        ((Customer) Thread.currentThread()).setState(CustomerState.RECEPTION_TALK_WITH_MANAGER);

        //TODO: UPDATE REPOSITORY

        if (wantsReplacement)
            nCustomerForKey++;
        
        isManagerTaskComplete = false;

        // block on condition variable
        while (!isManagerTaskComplete) {
            try {
                wait();
            } catch (InterruptedException e) { }
        }
        isManagerTaskComplete = false;
        int tempCarID = ((Customer) Thread.currentThread()).getCarId();
        if(( tempCarID != ((Customer) Thread.currentThread()).getCustomerId() )&&
            (tempCarID != -1))
        {
            nReplacementCarAvailable++;
            replacementKeys[100-tempCarID] = tempCarID;
        }
        ((Customer) Thread.currentThread()).setCarId(-1); //customer hands over the key

        //Customer hands key
        isCustomerTaskComplete = true;
        notifyAll();
    }


    /**
     * Operação payForTheService (chamada pelo {@link entities.Customer})
     *
     * Aqui, o cliente vai paga o serviço esperando pela confirmação do {@link entities.Manager}.
     * Após confirmação o {@link entities.Customer} vai receber a chave.
     */
	public synchronized void payForTheService() {
        
        int customerId = ((Customer) Thread.currentThread()).getCustomerId();
        //Customer pays
        isCustomerTaskComplete = true;
        notifyAll();

        ((Customer) Thread.currentThread()).setState(CustomerState.RECEPTION_PAYING);
        //TODO: UPDATE

        // block until manager confirms payment
        while (!isManagerTaskComplete) {
            try {
                wait();
            } catch (InterruptedException e) { }
        }
        isManagerTaskComplete = false;
        ((Customer) Thread.currentThread()).setCarId(carKeyToHandle);
	}

    /**
     * Operação getNexTask (chamada pelo {@link entities.Manager})
     * Aqui o manager fica à espera da próxima tarefa.
     * Se o dia chegar ao fim, o manager avisa os mecanicos que o dia acabou.
     */
    public synchronized boolean getNextTask()
    {
        //UPDATE THE STATE
        ((Manager) Thread.currentThread()).setState(ManagerState.CHECKING_WHAT_TO_DO);
        if(nTotalRepairedCars>=30)
        {
            return false;
        }

        //TODO: UPDATE REPOS

        // block on condition variable
        while (nRequests == 0) {
            try {
                wait();
            } catch (InterruptedException e) { }
        }
        nRequests--;
        return true;
    }

    /**
     * Operação appraiseSit chamada pelo {@link entities.Manager})
     * Aqui o manager verifica qual a sua próxima tarefa a realizar.
     * Prioridade: 1º reabastecer stock, 2º atribuir chave, 3º atender cliente
     */             
    public synchronized ManagerTask appraiseSit()
    {
        //TODO: UPDATE THE STATE
        ((Manager) Thread.currentThread()).setState(ManagerState.CHECKING_WHAT_TO_DO);

        //TODO: UPDATE REPOS

        if(needsRessuplyParts)
        {
            return ManagerTask.GET_PARTS;
        }
        else if (nCustomerForKey >= 1 && nReplacementCarAvailable>0)
        {
            //nCostumerForKey--;
            return ManagerTask.HAND_CAR_KEY;
        }
        else if(nRepairedCar > 0)
        {
            return ManagerTask.PHONE_CUSTOMER;
        }
        else if (nCostumerInQueue>0){
            return ManagerTask.TALK_CUSTOMER;
        }
        return ManagerTask.NONE;
    }

    /**
     * Operação talkToCustomer chamada pelo {@link entities.Manager})
     * Aqui o manager verifica qual o próximo cliente a chamar.
     * Devolve o próximo "costumer"
     */
    public synchronized  CustomerState talkToCustomer(ManagerTask task)
    {
        int nextC = -1;
        ((Manager) Thread.currentThread()).setState(ManagerState.ATTENDING_CUSTOMER);
        
        //TODO: UPDATE REPOS
        
        boolean clientWantsToRepair = false;
        if (task == ManagerTask.TALK_CUSTOMER)
        {
            nextC = costumerQueue.remove();
            clientWantsToRepair = true;
        }
        else if (task == ManagerTask.HAND_CAR_KEY)
        {
            nextC = costumersWaitingForKey.remove();
            clientWantsToRepair = false;
        }

        //avisa o customer que estão em fila
        nextCustomer = nextC;
        notifyAll();

        //TODO:
        //obter o estado do customer 
        if(clientWantsToRepair)
        {
            //Wait for customer to respond
            // block on condition variable
            while (!isCustomerTaskComplete) {
                try {
                    wait();
                } catch (InterruptedException e) { }
            }
            isCustomerTaskComplete = false;
            return currentCustomerState;
        }
        return null;
    } 
    
    /**
    * Operação receivePayment Chamada pelo {@link entities.Manager})
    *  Recebe pagamento por parte do cliente
    */
    public synchronized void receivePayment()
    {
        repository.setManagerState(ManagerState.ATTENDING_CUSTOMER);
        // TODO: update repository

        // wais for customer to pay
        while (!isCustomerTaskComplete) {
            try {
                wait();
            } catch (InterruptedException e) { }
        }
        isCustomerTaskComplete = false;

        //Manager confirms receivment of payment
        isManagerTaskComplete = true;
        notifyAll();
    }  
   /**
   *  Operação handCarKey Chamada pelo {@link entities.Manager})
   * Devolve a chave ao cliete
   * @param customer identificação do cliente que recebe a chave
   */
    public void handCarKey(boolean returningKey) {

        //TODO: UPDATE STATES;

        //TODO: UPDATE REPOS

        //Manager searches for keys
        if (returningKey) //Manager wants to return the car to its owner
        {
            carKeyToHandle = nextCustomer;
            nTotalRepairedCars++;
        }
        else
        {
            //client wants a replacement
            int temp = -1;
            if(nReplacementCarAvailable>0)
            {
                for (int i = 0; i <replacementKeys.length; i++)
                {
                    if(replacementKeys[i] != -1)
                    {
                        temp = replacementKeys[i];
                        replacementKeys[i] = -1;
                        nReplacementCarAvailable--;
                        break;
                    }
                }
            }
            carKeyToHandle = temp;            
        }

        //Manager signals that he found the key.
        isManagerTaskComplete = true;
        notifyAll();

        //waits until client receives the key
        while (!isCustomerTaskComplete) {
            try {
                wait();
            } catch (InterruptedException e) { }
        }
        isCustomerTaskComplete = false;
    }

    

  /**
   *  Operação letManagerKnow Chamada pelo {@link entities.Mechanic})
   * Notifica o manager que uma peça está em falta, se ainda não está registada
   * @param partRequired identificação da parte em falta
   */
    public synchronized void letManagerKnow(int partRequired)
    {
        //TODO UPDATE STATES

        //TODO UPDATE REPOSITORY
        if (!needsRessuplyParts)
        {
            partsNeedRestocking[partRequired] = true;
            needsRessuplyParts = true;
            nRequests++;
            notifyAll();
        }
    }
    /**
   *  Operação repairConcluded Chamada pelo {@link entities.Mechanic})
   * Notifica o manager que a reparação de um carro foi concluida.
   * @param carRepaired identificação do carro reparado
   */
    public void repairConcluded() {
        //TODO UPDATE STATE
        //TODO UPDATE REPOSITORY
        int repairedCarId = ((Mechanic)Thread.currentThread()).getCurrentCarFixingId();
        repairedCars.add(repairedCarId);
        nRepairedCar++;
        ((Mechanic)Thread.currentThread()).setCurrentCarFixingId(-1);
        notifyAll();

	}

    /**
     * Função auxiliar para obter o numero de um cliente.
     * Chamada pelo {@link entities.Manager})
     */
    public synchronized int getClientNumber()
    {
        return repairedCars.remove();
    }
    
    public synchronized Boolean wantReplacementCar()
    {
        return wantsReplacement;
    }

    public synchronized int getCurrentCustomerID()
    {
        return nextCustomer;
    }
}
