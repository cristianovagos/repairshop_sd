package regions;

import entities.*;
import genclass.GenericIO;
import genclass.TextFile;

import java.text.DecimalFormat;

/**
 * Classe GeneralRepository (Repositório Geral)<br>
 *
 * Esta classe é reponsável pela criação e posterior atualização de um ficheiro
 * de logging que irá permitir visualizar os diferentes estados e valores
 * decorrentes da execução do problema.<br>
 *
 * Assim que necessário e pedido pelas diferentes entidades e regiões partilhadas
 * de dados deste problema, será acrescentado ao ficheiro de logging os valores
 * mais recentes para posterior acompanhamento, que serão atualizados nas variáveis
 * e estruturas de dados que nada mais são de que "fotocópias" dos dados que estão
 * a ser alterados e atualizados nas diferentes entidades ativas e passivas do problema.<br>
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public class GeneralRepository {

    /**
     * Nome do ficheiro de Logging
     */
    private String FILE_NAME;

    /**
     * Número de Clientes ({@link Customer})
     */
    private final int N_CUSTOMERS;

    /**
     * Número de Mecânicos ({@link Mechanic})
     */
    private final int N_MECHANICS;

    /**
     * Número de tipos de peças disponíveis
     */
    private final int N_PARTS;

    /**
     * Formatação de números com dois números decimais
     */
    private final DecimalFormat FORMAT;

    /**
     * Estado para cada um dos clientes existentes
     * @see CustomerState
     */
    private CustomerState[] customersState;

    /**
     * Indicação se um dado cliente vai requerer viatura de substituição ou não
     */
    private boolean[] customersReqReplVehicle;

    /**
     * Viatura que cada cliente está a conduzir de momento<br>
     * <ul>
     *  <li>o id do cliente ({@link Customer})</li>
     *  <li>uma das viaturas de substituição</li>
     *  <li>
     *      <ul>
     *          <li>100, se viatura de substituição 0</li>
     *          <li>101, se viatura de substituição 1</li>
     *          <li>etc...</li>
     *      </ul>
     *  </li>
     *  <li>-1, se nenhuma</li>
     * </ul>
     */
    private int[] customersCars;

    /**
     * Indicação se a reparação da viatura do cliente foi concluída ou não
     */
    private boolean[] customersRepairConcluded;

    /**
     * Estado interno do Manager
     * @see ManagerState
     */
    private ManagerState managerState;

    /**
     * Estado interno de cada um dos Mecânicos
     * @see MechanicState
     */
    private MechanicState[] mechanicsState;

    /**
     * Número de viaturas de clientes no Parque de Estacionamento
     * {@link Park}
     */
    private int customerCarsInPark;

    /**
     * Número de viaturas de substituição no Parque de Estacionamento
     * {@link Park}
     */
    private int replacementCarsInPark;

    /**
     * Número de clientes na fila da Recepção ({@link Lounge})
     * para serem atendidos pelo Manager
     */
    private int customersInQueue;

    /**
     * Número de clientes na fila à espera de chave para viatura de substituição
     */
    private int customersInQueueForKey;

    /**
     * Número de viaturas reparadas
     */
    private int totalRepairedCars;

    /**
     * Número de serviços pedidos pelo Gerente ({@link Manager})
     */
    private int requestedServices;

    /**
     * Número de peças em stock na Área de Reparação ({@link RepairArea})
     */
    private int[] stockParts;

    /**
     * Número de clientes/viaturas à espera de peças para reparação
     */
    private int[] customersMissingParts;

    /**
     * Indicação se o Gerente ({@link Manager}) já foi alertado para a falta
     * de uma dada peça
     */
    private boolean[] partMissingAlert;

    /**
     * Número de peças vendidas pelo Fornecedor ({@link SupplierSite})
     */
    private int[] soldParts;

    /**
     * Construtor do Repositório<br>
     *
     * Aqui serão inicializadas as variáveis e estruturas de dados que serão
     * posteriormente atualizadas durante a execução do problema.<br>
     *
     * @param fileName nome do ficheiro de logging
     * @param nCustomers número de clientes
     * @param nMechanics número de mecânicos
     * @param nParts número de tipos de peças
     * @param nReplacementCars número de viaturas de substituição
     */
    public GeneralRepository(String fileName, int nCustomers, int nMechanics, int nParts, int nReplacementCars) {
        if ((fileName != null) && !("".equals(fileName)))
            this.FILE_NAME = fileName;

        this.N_CUSTOMERS = nCustomers;
        this.N_MECHANICS = nMechanics;
        this.N_PARTS = nParts;

        this.customersState = new CustomerState[N_CUSTOMERS];
        this.customersReqReplVehicle = new boolean[N_CUSTOMERS];
        this.customersCars = new int[N_CUSTOMERS];
        this.customersRepairConcluded = new boolean[N_CUSTOMERS];
        for (int i = 0; i < N_CUSTOMERS; i++) {
            this.customersState[i] = CustomerState.NORMAL_LIFE_WITH_CAR;
            this.customersReqReplVehicle[i] = false;
            this.customersCars[i] = i;
            this.customersRepairConcluded[i] = false;
        }

        this.mechanicsState = new MechanicState[N_MECHANICS];
        for (int i = 0; i < N_MECHANICS; i++)
            this.mechanicsState[i] = MechanicState.WAITING_FOR_WORK;

        this.customerCarsInPark = 0;
        this.replacementCarsInPark = nReplacementCars;

        this.customersInQueue = 0;
        this.customersInQueueForKey = 0;
        this.totalRepairedCars = 0;

        this.requestedServices = 0;
        this.stockParts = new int[N_PARTS];
        this.customersMissingParts = new int[N_PARTS];
        this.partMissingAlert = new boolean[N_PARTS];
        this.soldParts = new int[N_PARTS];

        for (int i = 0; i < N_PARTS; i++) {
            this.stockParts[i] = 0;
            this.customersMissingParts[i] = 0;
            this.partMissingAlert[i] = false;
            this.soldParts[i] = 0;
        }

        this.FORMAT = new DecimalFormat("00");

        printHeader();
    }

    /**
     * Operação printHeader<br>
     *
     * Aqui será escrito no ficheiro de logging o cabeçalho da tabela referente
     * a cada um dos valores que serão posteriormente escritos durante a execução
     * do problema.<br>
     *
     * O ficheiro de logging é criado de acordo com o nome que foi fornecido
     * no construtor, e irá ser escrito o cabeçalho da tabela.<br>
     */
    private void printHeader() {
        // initialize file writer
        TextFile log = new TextFile();

        // open file for writing
        if (!log.openForWriting (".", FILE_NAME)) {
            GenericIO.writelnString ("Failed to create file " + FILE_NAME + " !");
            System.exit(1);
        }

        log.writeFormString(37, "");
        log.writelnString("REPAIR SHOP ACTIVITIES - Description of the internal state of the problem\n");
        log.writeFormString(5, " MAN", "MECHANIC");
        log.writeFormString(64, "");
        log.writelnString("CUSTOMER");

        log.writeFormString(5, "Stat");
        log.writeFormString(4, "St0", "St1");

        for (int i = 0; i < N_CUSTOMERS; i++) {
            log.writeFormString(4, "S" + FORMAT.format(i), "C" + FORMAT.format(i), "P" + FORMAT.format(i), "R" + FORMAT.format(i));
            if(i != 0 && (i+1) % 10 == 0){
                log.writeString("\n ");
                log.writeFormString(12, "");
            }
        }

        log.writeFormString(3, "");
        log.writeString("LOUNGE");
        log.writeFormString(8, "");
        log.writeString("PARK");
        log.writeFormString(29, "");
        log.writeString("REPAIR AREA");
        log.writeFormString(43, "");
        log.writelnString("SUPPLIERS SITE");

        log.writeFormString(12, "");
        log.writeFormString(4, " InQ", " WtK", " NRV", "", "NCV", " NPV");
        log.writeFormString(6, "", " NSRQ   ");

        for (int i = 0; i < N_PARTS; i++) {
            log.writeFormString(4, "Prt" + i + " ", " NV" + i + " ", " S" + i);
        }

        log.writeFormString(23, "");
        log.writeFormString(6, " PP0", " PP1", " PP2");
        log.writelnString();

        // closing file
        if (!log.close()) {
            GenericIO.writelnString ("Failed to close file " + FILE_NAME + " !");
            System.exit (1);
        }
    }

    /**
     * Operação printStateLine<br>
     *
     * Aqui serão escritos os valores dos diferentes estados das entidades,
     * bem como os valores referentes às diferentes operações que serão executadas
     * durante a execução do problema.<br>
     *
     * O ficheiro de logging já foi previamente criado em {@link #printHeader()} e
     * agora os valores mais recentes serão colocados imediatamente abaixo do que
     * foi previamente escrito no ficheiro.<br>
     */
    private void printStateLine() {
        // initialize file writer
        TextFile log = new TextFile();

        // open file to append data
        if (!log.openForAppending (".", FILE_NAME)) {
            GenericIO.writelnString ("Failed to create file " + FILE_NAME + " !");
            System.exit(1);
        }

        // Manager state values
        log.writeFormString(5, managerState.state());

        // Mechanic state
        for (int i = 0; i < N_MECHANICS; i++)
            log.writeFormString(4, mechanicsState[i].state());

        /* Customer */
        for (int i = 0; i < N_CUSTOMERS; i++) {
            // Customer state
            log.writeFormString(4, customersState[i].state());

            // Vehicle driven by customer: own car (customer ID), replacement car (R0, etc), none "-"
            if(customersCars[i] == -1)
                log.writeFormString(4, " " + "--");
            else if (customersCars[i] >= 0 && customersCars[i] < N_CUSTOMERS)
                log.writeFormString(4, " " + FORMAT.format(customersCars[i]));
            else if (customersCars[i] == 100)
                log.writeFormString(4, " " + "R0");
            else if (customersCars[i] == 101)
                log.writeFormString(4, " " + "R1");
            else if (customersCars[i] == 102)
                log.writeFormString(4, " " + "R2");
            else
                // ERROR!!!
                log.writeFormString(4, " " + "!!");

            // Customer requires replacement vehicle (T or F)
            log.writeFormString(4, " " + (customersReqReplVehicle[i] ? "T" : "F"));
            
            // Customer vehicle has already been repaired (T or F)
            log.writeFormString(4, " " + (customersRepairConcluded[i] ? "T" : "F"));

            if(i != 0 && (i+1) % 10 == 0 && i < N_CUSTOMERS-1){
                log.writeString("\n ");
                log.writeFormString(12, "");
            }
        }

        /* Lounge */
        log.writelnString();
        log.writeFormString(13, "");
        // Number of customers in queue to talk with manager
        log.writeFormString(4, " " + FORMAT.format(customersInQueue));
        // Number of customers waiting for a replacement vehicle
        log.writeFormString(4, " " + FORMAT.format(customersInQueueForKey));
        // Number of cars that have already been repaired
        log.writeFormString(4, " " + FORMAT.format(totalRepairedCars));


        /* Park */
        log.writeFormString(4, "");
        // Number of customer vehicles currently parked
        log.writeFormString(4, FORMAT.format(customerCarsInPark));
        // Number of replacement vehicles currently parked
        log.writeFormString(4, " " + FORMAT.format(replacementCarsInPark));


        /* Repair Area */
        log.writeFormString(6, "");
        // Number of service requests made by the manager to the repair area
        log.writeFormString(6," " + FORMAT.format(requestedServices));
        log.writeString("  ");

        for (int i = 0; i < N_PARTS; i++) {
            // Number of parts of type i presently in storage at repair area
            log.writeFormString(4, FORMAT.format(stockParts[i]) + " ");
            // Number of customer vehicles waiting for part i
            log.writeFormString(4, "  " + FORMAT.format(customersMissingParts[i]) + "  ");
            // Flag signaling the manager has been adviced that part i is missing at repair area (T or F)
            log.writeFormString(4, " " + (partMissingAlert[i] ? "T" : "F") + " ");
        }

        /* Supplier Site */
        log.writeFormString(22, "");
        for (int i = 0; i < N_PARTS; i++) {
            // Number of parts of type i which have been purchased so far by the manager
            log.writeFormString(6, "  " + FORMAT.format(soldParts[i]));
        }

        log.writelnString("\n");

        // closing file
        if (!log.close()) {
            GenericIO.writelnString ("Failed to close file " + FILE_NAME + " !");
            System.exit (1);
        }
    }

    /**
     * Operação initializeCustomer<br>
     *
     * Inicializa o estado de cada um dos Customers<br>
     *
     * @param index índice do cliente
     * @param requiresReplacement indicação se requer viatura de substituição
     */
    public synchronized void initializeCustomer(int index, boolean requiresReplacement) {
        this.customersState[index] = CustomerState.NORMAL_LIFE_WITH_CAR;
        this.customersReqReplVehicle[index] = requiresReplacement;
        this.customersCars[index] = index;
        this.customersRepairConcluded[index] = false;

        // print state line
        printStateLine();
    }

    /**
     * Operação setCustomerState<br>
     *
     * Altera o estado de um cliente em específico<br>
     *
     * @param index o índice do cliente em questão
     * @param newState o estado novo do cliente
     */
    public synchronized void setCustomerState(int index, CustomerState newState) {
        this.customersState[index] = newState;

        // print state line
        printStateLine();
    }

    /**
     * Operação setCustomerCar<br>
     *
     * Altera a viatura atual de um cliente em específico<br>
     *
     * @param index o índice do cliente em questão
     * @param carId o identificador da viatura
     *              (-1 se nenhuma, id do customer se a própria, ou 100..102 para viatura de substituição)
     */
    public synchronized void setCustomerCar(int index, int carId) {
        this.customersCars[index] = carId;

        // print state line
        printStateLine();
    }

    /**
     * Operação setCustomerRepairConcluded<br>
     *
     * Altera o estado de conclusão da reparação da viatura<br>
     *
     * @param index o índice do cliente em questão
     * @param isRepairConcluded se a reparação foi concluída
     */
    public synchronized void setCustomerRepairConcluded(int index, boolean isRepairConcluded) {
        this.customersRepairConcluded[index] = isRepairConcluded;

        // print state line
        printStateLine();
    }

    /**
     * Operação setManagerState<br>
     *
     * Altera o estado do Manager<br>
     *
     * @param state estado do Manager
     */
    public synchronized void setManagerState(ManagerState state) {
        this.managerState = state;

        // print state line
        printStateLine();
    }

    /**
     * Operação setMechanicState<br>
     *
     * Altera o estado de um mecânico em específico<br>
     *
     * @param index o índice do mecânico em questão
     * @param newState o estado novo do mecânico
     */
    public synchronized void setMechanicState(int index, MechanicState newState) {
        this.mechanicsState[index] = newState;

        // print state line
        printStateLine();
    }

    /**
     * Operação customerCarEntersPark<br>
     *
     * Indica que uma viatura de um cliente entrou no park<br>
     */
    public synchronized void customerCarEntersPark() {
        this.customerCarsInPark++;

        // print state line
        printStateLine();
    }

    /**
     * Operação customerCarLeavesPark<br>
     *
     * Indica que uma viatura de um cliente saiu do park<br>
     */
    public synchronized void customerCarLeavesPark() {
        this.customerCarsInPark--;

        // print state line
        printStateLine();
    }

    /**
     * Operação replacementCarEntersPark<br>
     *
     * Indica que uma viatura de substituição entrou no park<br>
     */
    public synchronized void replacementCarEntersPark() {
        this.replacementCarsInPark++;

        // print state line
        printStateLine();
    }

    /**
     * Operação replacementCarLeavesPark<br>
     *
     * Indica que uma viatura de substituição saiu do park<br>
     */
    public synchronized void replacementCarLeavesPark() {
        this.replacementCarsInPark--;

        // print state line
        printStateLine();
    }

    /**
     * Operação setCustomersInQueue<br>
     *
     * Altera o valor dos clientes na fila no Lounge<br>
     *
     * @param customersInQueue número de clientes na fila no Lounge
     */
    public synchronized void setCustomersInQueue(int customersInQueue) {
        this.customersInQueue = customersInQueue;

        // print state line
        printStateLine();
    }

    /**
     * Operação setCustomersInQueueForKey<br>
     *
     * Altera o valor dos clientes na fila para obter uma chave para viatura de substituição no Lounge<br>
     *
     * @param customersInQueueForKey número de clientes na fila à espera de chave para viatura de substituição
     */
    public synchronized void setCustomersInQueueForKey(int customersInQueueForKey) {
        this.customersInQueueForKey = customersInQueueForKey;

        // print state line
        printStateLine();
    }

    /**
     * Operação setTotalRepairedCars<br>
     *
     * Altera o valor do total de carros reparados<br>
     *
     * @param repairedCars total de carros reparados
     */
    public synchronized void setTotalRepairedCars(int repairedCars) {
        this.totalRepairedCars = repairedCars;

        // print state line
        printStateLine();
    }

    /**
     * Operação managerRequestedService<br>
     *
     * Incrementa o número de serviços pedidos pelo ({@link Manager}) na ({@link RepairArea})<br>
     */
    public synchronized void managerRequestedService() {
        requestedServices++;

        // update state line
        printStateLine();
    }

    /**
     * Operação setStockParts<br>
     *
     * Altera o array referente ao número de peças em stock na ({@link RepairArea})<br>
     *
     * @param stockParts lista com número de peças em stock
     */
    public synchronized void setStockParts(int[] stockParts) {
        this.stockParts = stockParts;

        // update state line
        printStateLine();
    }

    /**
     * Operação addMissingPart<br>
     *
     * Marca uma dada peça como inexistente no stock da ({@link RepairArea})<br>
     *
     * @param partIndex o índice da peça
     */
    public synchronized void addMissingPart(int partIndex) {
        this.customersMissingParts[partIndex]++;

        // update state line
        printStateLine();
    }

    /**
     * Operação removeMissingPart<br>
     *
     * Marca uma dada peça como existente no stock da ({@link RepairArea})<br>
     *
     * @param partIndex o índice da peça
     */
    public synchronized void removeMissingPart(int partIndex) {
        if(this.customersMissingParts[partIndex] > 0) {
            this.customersMissingParts[partIndex]--;

            // update state line
            printStateLine();
        }
    }

    /**
     * Operação setMissingPartIndex<br>
     *
     * Altera o valor de um dado índice do array de peças em falta<br>
     *
     * @param partIndex o índice da peça
     * @param value o novo valor
     */
    public synchronized void setMissingPartIndex(int partIndex, int value) {
        this.customersMissingParts[partIndex] = value;

        // update state line
        printStateLine();
    }

    /**
     * Operação setPartMissingAlert<br>
     *
     * Altera o valor de um dado índice do array de alerta de peças em falta<br>
     *
     * @param partIndex o índice da peça
     * @param value o novo valor
     */
    public synchronized void setPartMissingAlert(int partIndex, boolean value) {
        this.partMissingAlert[partIndex] = value;

        // update state line
        printStateLine();
    }

    /**
     * Operação setSoldParts<br>
     *
     * Altera o valor do array referente ao número de peças vendidas pela ({@link SupplierSite})<br>
     *
     * @param soldParts array com o total de peças vendidas
     */
    public synchronized void setSoldParts(int[] soldParts) {
        this.soldParts = soldParts;

        // update state line
        printStateLine();
    }
}
