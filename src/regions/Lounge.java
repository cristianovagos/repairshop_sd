package regions;

import java.util.List;
import java.util.Queue;


/**
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public class Lounge {

    /**
     * Indicia o numero total de carros reparados no dia
     * @serialField nTotalRepairedCars
     */
    int nTotalRepairedCars;

    /**
     * Repositorio que armazena informação atual do sistema
     * @serialField repository
     */
    GeneralRepository repository;

  
  boolean isCurrentTaskCompleted;
    /**
   *  Cliente a ser atendido
   *
   *    @serialField nextCostumer
   */
    int nextCostumer;
  /**
   *  Costumer Queue
   *
   *    @serialField costumerQueue
   */
    Queue<Integer> costumerQueue;
    
   /**
   *  Missing Part
   *
   *    @serialField missingParts
   */
    int [] missingParts = {0,0,0};
    
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
  int costumerInQueue;

   /**
   *  Customer waiting for keys
   *
   *    @serialField nCostumerForKey
   */
  int nCostumerForKey;   

  /**
   *  Indica se há algum carro de substituição disponivel.
   *
   *    @serialField replacementCarAvailable
   */
  boolean replacementCarAvailable;

   /**
   *  Indica se há peças que necessitem de supply.
   *
   *    @serialField replacementCarAvailable
   */
  boolean ressuplyParts;

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
        nextCostumer = 0;
        isCurrentTaskCompleted = false;
        repository = _repository;
        replacementCarAvailable = true;
        ressuplyParts = false;
    }
    /**
     * Chamado pelo {@link entities.Manager})
     * Aqui o manager fica à espera da próxima tarefa.
     */
    public synchronized void getNextTask()
    {
        //TODO: UPDATE THE STATE
        //TODO: UPDATE REPOS

        // block on condition variable
        while (nRequests == 0) {
            try {
                wait();
            } catch (InterruptedException e) { }
        }
        nRequests--;
    }

    /**
     * Chamado pelo {@link entities.Manager})
     * Aqui o manager verifica qual a sua próxima tarefa a realizar.
     * Prioridade: 1º atribuir chave, 2º reabastecer stock, 3º atender cliente
     */
    public synchronized int appraiseSit()
    {
        //TODO: UPDATE THE STATE
        //TODO: UPDATE REPOS

        if(ressuplyParts)
        {
            return 1;
        }
        else if (nCostumerForKey >= 1 && replacementCarAvailable)
        {
            nCostumerForKey--;
            return 0;
        }
        else if(nRepairedCar > 0)
        {
            return 2;
        }
        return -1;
    }

   /**
   *  called by the Manager. 
   * Manda um sinal ao próximo cliente para ser atendido. 
   * devolve um int que é o id do cliente.
   */
    public synchronized int talkToCostumer()
    {
        int clientID = -1;
        if (costumerQueue.isEmpty())
            //you are not supposed to be here
            return clientID;

        clientID = costumerQueue.remove();
        nextCostumer = clientID;
        // change manager state
        // update repository

        // block on condition variable
        //waitForCarRepair[customerId] = false;
        notifyAll();
        return clientID;
    }
    
   /**
   * Chamado pelo manager.
   *  Regista o serviço do cliente
   * 
   * @param clientID identificação do cliente
   */
    public synchronized void registerService(int clientID)
    {
        ClientRegistry clientRegistry = new ClientRegistry(clientID, false);
        customerRegisters.add(clientRegistry);
        // change manager state
        // update repository

        // block on condition variable
        isCurrentTaskCompleted= true;
        notifyAll();
    }
    
    /**
     * Chamado pelo Manager
   *  Recebe pagamento por parte do cliente
   * 
   * @param clientID identificação do cliente
   */
    public synchronized void receivePayment()
    {
        isCurrentTaskCompleted = false;

        // update repository
        repository.setManagerState(ManagerState.ATTENDING_CUSTOMER);

        // block on condition variable
        while (!isCurrentTaskCompleted) {
            try {
                wait();
            } catch (InterruptedException e) { }
        }
    }
    
   /**
   *  Devolve a chave ao cliente
   * 
   * @param clientID identificação do cliente que recebe a chave
   * @param carKey identificador da chave que o cliente vai receber
   */
    public synchronized void handCarKey(int clientID, int carKey)
    {
        //TODO: CONTINUE HERE


    }
    
    /**
   *  Chamado pelo Customer. Cliente recebe a chave
   * 
   */
    public synchronized int getCarKey()
    {
        return 0;
    }
    
   /**
   *  Chamado pelo Customer. Cliente fala com o manager
   * 
   */
    public synchronized void talkWithManager()
    {
        
    }
    
   /**
   *  Chamado pelo Customer. Cliente entra na fila
   */
    public synchronized void queueIn(int clientID)
    {
        // update repository
        repository.setManagerState(CostumerState.RECEPTION);

        // block on condition variable
        while (clientID != nextCostumer) {
            try {
                wait();
            } catch (InterruptedException e) { }
        }
    }
    
   /**
   *  Chamado pelo Customer. Cliente paga pelo serviço
   */
    public synchronized void payForTheService()
    {
        
    }
    
  /**
   *  Chamado pelo mecanico. Para avisar que é necessário peças.
   * @param partRequired peça necessária para ressuply
   */
    public synchronized void letManagerKnow(int partRequired)
    {
        
    }
    
    
      /**
   *  Estrutura que armazena o client ID e o estado da viatura (false se viatura
   * encontrar-se avariada ou não presente, true se se encontrar reparada) 
   *
   *    @param id identificação do costumer
   *    @param repaired indica se o carro esta reparado
   */
    class ClientRegistry
    {
        public int id;
        public boolean repaired;

        /*public ClientRegistry(int _id)
        {
            this(_id, false);      
        }*/

        public ClientRegistry(int _id, boolean _repaired)
        {
            id = _id;
            repaired=_repaired;
        }
    }
}
