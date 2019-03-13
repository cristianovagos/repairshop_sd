package regions;

import entities.CustomerState;
import entities.ManagerState;
import entities.MechanicState;
import utils.MemFIFO;

import java.util.List;

public class GeneralRepository {

    /**
     * Repository
     *
     * repositoryState              - estado interno do repositorio
     */
//    public int repositoryState;

    /**
     * ------------------------------------------
     * ENTIDADES
     * ------------------------------------------
     */

    /**
     * Customer
     *
     * customerState                - estado dos clientes
     * requireReplacementVehicle    - se cada cliente requer uma viatura de substituição
     * carState                     - estado das viaturas dos clientes
     */
    private CustomerState[] customerState;
    private boolean[] requireReplacementVehicle;
    private int[] carState;

    /**
     * Manager
     *
     * managerState                 - estado do Manager
     */
    private ManagerState managerState;

    /**
     * Mechanics
     *
     * mechanicsState               - estado dos mecânicos
     */
    private MechanicState[] mechanicsState;

    /**
     * ------------------------------------------
     * REGIÕES PARTILHADAS DE INFORMAÇÃO
     * ------------------------------------------
     */

    /**
     * Park
     *
     * customerCars                 - viaturas dos clientes
     * replacementCars              - viaturas de substituição
     */
    private List<Integer> customerCars;
    private int[] replacementCars;


    /**
     * Lounge
     *
     * customersQueue               - fila de espera de clientes
     * mechanicsQueue               - fila de espera de mecânicos
     */
    private MemFIFO customersQueue;
    private MemFIFO mechanicsQueue;

    /**
     * Repair Area
     *
     * customerFirstRepairQueue     - fila de espera de clientes à espera de reparação (primeira fila)
     * customerMissingPartQueue     - lista de clientes à espera de uma peça para reparação
     * taskDescription              - lista de tarefas a decorrer para cada mecânico (viatura a ser reparada, etc)
     * stockParts                   - número de peças em stock
     */
    private MemFIFO customerFirstRepairQueue;
    private List<List<Integer>> customerMissingPartQueue;
    private List<List<Integer>> taskDescription;
    private int[] stockParts;

    /**
     * Supplier Site
     *
     * soldParts                    - número de peças vendidas
     */
    private int[] soldParts;


    public CustomerState getCustomerState(int i) {
        return this.customerState[i];
    }

    public void setCustomerState(int i, CustomerState customerState) {
        this.customerState[i] = customerState;
    }

    public boolean requireReplacementVehicle(int i) {
        return requireReplacementVehicle[i];
    }


}
