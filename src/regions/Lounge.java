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
   *  Indica se a tarefa atual está completa
   *
   *    @serialField isCurrentTaskCompleted
   */
  int keyToHandle;    
   /**
   *  Indica se a tarefa atual está completa
   *
   *    @serialField isCurrentTaskCompleted
   */
    int [] replacementKeys = {101, 102, 103};
   /**
   *  Indica se a tarefa atual está completa
   *
   *    @serialField isCurrentTaskCompleted
   */
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
   List <ClientRegistry> customerRegisters;
   
    /**
   *  Customer Phone Queue 
   *
   *    @serialField customerPhoneQueue
   */
   Queue <Integer> customerPhoneQueue;
    
  /**
   *  Instanciação do Lounge.
   *
   *    @param barberId identificação do barbeiro
   *    @param customerId identificação do cliente
   *    @param bShop barbearia
   */
    public Lounge()
    {
        nextCostumer = 0;
        isCurrentTaskCompleted = false;
    }
    
   /**
   *  called by the Manager. 
   * Manda um sinal ao próximo cliente para ser atendido. 
   * devolve um int que é o id do cliente.
   */
    public synchronized int talkToCostumer()
    {
        int clientID = 0;
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
    public synchronized void receivePayment(int clientID)
    {
        isCurrentTaskCompleted = false;

        // change manager state

        // update repository

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
    public synchronized void queueIn()
    {
        
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
