package comm;

/**
 * Enumerado MessageType<br>
 *
 * Identifica os tipos de mensagens a serem enviadas durante a execução do problema.
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public enum MessageType {

    /* LOUNGE */

    /**
     * Pedido Lounge appraiseSit
     */
    LOUNGE_APPRAISE_SIT_REQ,

    /**
     * Resposta Lounge appraiseSit
     */
    LOUNGE_APPRAISE_SIT_RESP,

    /**
     * Pedido Lounge getNextTask
     */
    LOUNGE_GET_NEXT_TASK_REQ,

    /**
     * Resposta Lounge getNextTask
     */
    LOUNGE_GET_NEXT_TASK_RESP,

    /**
     * Pedido Lounge talkToCustomer
     */
    LOUNGE_TALK_TO_CUSTOMER_REQ,

    /**
     * Resposta Lounge talkToCustomer
     */
    LOUNGE_TALK_TO_CUSTOMER_RESP,

    /**
     * Pedido Lounge handCarKey
     */
    LOUNGE_HAND_CAR_KEY_REQ,

    /**
     * Resposta Lounge handCarKey
     */
    LOUNGE_HAND_CAR_KEY_RESP,

    /**
     * Pedido Lounge receivePayment
     */
    LOUNGE_RECEIVE_PAYMENT_REQ,

    /**
     * Resposta Lounge receivePayment
     */
    LOUNGE_RECEIVE_PAYMENT_RESP,

    /**
     * Pedido Lounge repairConcluded
     */
    LOUNGE_REPAIR_CONCLUDED_REQ,

    /**
     * Resposta Lounge repairConcluded
     */
    LOUNGE_REPAIR_CONCLUDED_RESP,

    /**
     * Pedido Lounge queueIn
     */
    LOUNGE_QUEUE_IN_REQ,

    /**
     * Resposta Lounge queueIn
     */
    LOUNGE_QUEUE_IN_RESP,

    /**
     * Pedido Lounge talkWithManager
     */
    LOUNGE_TALK_WITH_MANAGER_REQ,

    /**
     * Resposta Lounge talkWithManager
     */
    LOUNGE_TALK_WITH_MANAGER_RESP,

    /**
     * Pedido Lounge collectKey
     */
    LOUNGE_COLLECT_KEY_REQ,

    /**
     * Resposta Lounge collectKey
     */
    LOUNGE_COLLECT_KEY_RESP,

    /**
     * Pedido Lounge payForTheService
     */
    LOUNGE_PAY_FOR_THE_SERVICE_REQ,

    /**
     * Resposta Lounge payForTheService
     */
    LOUNGE_PAY_FOR_THE_SERVICE_RESP,

    /**
     * Pedido Lounge letManagerKnow
     */
    LOUNGE_LET_MANAGER_KNOW_REQ,

    /**
     * Resposta Lounge letManagerKnow
     */
    LOUNGE_LET_MANAGER_KNOW_RESP,


    /* OUTSIDE WORLD */

    /**
     * Pedido OutsideWorld backToWorkByBus
     */
    OUTSIDE_WORLD_BACK_TO_WORK_BY_BUS_REQ,

    /**
     * Resposta OutsideWorld backToWorkByBus
     */
    OUTSIDE_WORLD_BACK_TO_WORK_BY_BUS_RESP,

    /**
     * Pedido OutsideWorld backToWorkByCar
     */
    OUTSIDE_WORLD_BACK_TO_WORK_BY_CAR_REQ,

    /**
     * Resposta OutsideWorld backToWorkByCar
     */
    OUTSIDE_WORLD_BACK_TO_WORK_BY_CAR_RESP,

    /**
     * Pedido OutsideWorld phoneCustomer
     */
    OUTSIDE_WORLD_PHONE_CUSTOMER_REQ,

    /**
     * Resposta OutsideWorld phoneCustomer
     */
    OUTSIDE_WORLD_PHONE_CUSTOMER_RESP,


    /* PARK */

    /**
     * Pedido Park goToRepairShop
     */
    PARK_GO_TO_REPAIR_SHOP_REQ,

    /**
     * Resposta Park goToRepairShop
     */
    PARK_GO_TO_REPAIR_SHOP_RESP,

    /**
     * Pedido Park findCar
     */
    PARK_FIND_CAR_REQ,

    /**
     * Resposta Park findCar
     */
    PARK_FIND_CAR_RESP,

    /**
     * Pedido Park collectCar
     */
    PARK_COLLECT_CAR_REQ,

    /**
     * Resposta Park collectCar
     */
    PARK_COLLECT_CAR_RESP,

    /**
     * Pedido Park getVehicle
     */
    PARK_GET_VEHICLE_REQ,

    /**
     * Resposta Park getVehicle
     */
    PARK_GET_VEHICLE_RESP,

    /**
     * Pedido Park returnVehicle
     */
    PARK_RETURN_VEHICLE_REQ,

    /**
     * Resposta Park returnVehicle
     */
    PARK_RETURN_VEHICLE_RESP,


    /* SUPPLIER SITE */

    /**
     * Pedido SupplierSite goToSupplier
     */
    SUPPLIER_SITE_GO_TO_SUPPLIER_REQ,

    /**
     * Resposta SupplierSite goToSupplier
     */
    SUPPLIER_SITE_GO_TO_SUPPLIER_RESP,


    /* REPAIR AREA */

    /**
     * Pedido RepairArea readThePaper
     */
    REPAIR_AREA_READ_THE_PAPER_REQ,

    /**
     * Resposta RepairArea readThePaper
     */
    REPAIR_AREA_READ_THE_PAPER_RESP,

    /**
     * Pedido RepairArea markEndOfDay
     */
    REPAIR_AREA_MARK_END_OF_DAY_REQ,

    /**
     * Resposta RepairArea markEndOfDay
     */
    REPAIR_AREA_MARK_END_OF_DAY_RESP,

    /**
     * Pedido RepairArea startRepairProcedure
     */
    REPAIR_AREA_START_REPAIR_PROCEDURE_REQ,

    /**
     * Resposta RepairArea startRepairProcedure
     */
    REPAIR_AREA_START_REPAIR_PROCEDURE_RESP,

    /**
     * Pedido RepairArea getRequiredPart
     */
    REPAIR_AREA_GET_REQUIRED_PART_REQ,

    /**
     * Resposta RepairArea getRequiredPart
     */
    REPAIR_AREA_GET_REQUIRED_PART_RESP,

    /**
     * Pedido RepairArea partAvailable
     */
    REPAIR_AREA_PART_AVAILABLE_REQ,

    /**
     * Resposta RepairArea partAvailable
     */
    REPAIR_AREA_PART_AVAILABLE_RESP,

    /**
     * Pedido RepairArea resumeRepairProcedure
     */
    REPAIR_AREA_RESUME_REPAIR_PROCEDURE_REQ,

    /**
     * Resposta RepairArea resumeRepairProcedure
     */
    REPAIR_AREA_RESUME_REPAIR_PROCEDURE_RESP,

    /**
     * Pedido RepairArea storePart
     */
    REPAIR_AREA_STORE_PART_REQ,

    /**
     * Resposta RepairArea storePart
     */
    REPAIR_AREA_STORE_PART_RESP,

    /**
     * Pedido RepairArea registerService
     */
    REPAIR_AREA_REGISTER_SERVICE_REQ,

    /**
     * Resposta RepairArea registerService
     */
    REPAIR_AREA_REGISTER_SERVICE_RESP,

    /**
     * Pedido RepairArea endOperation
     */
    REPAIR_AREA_END_OPERATION_REQ,

    /**
     * Resposta RepairArea endOperation
     */
    REPAIR_AREA_END_OPERATION_RESP,


    /* REPOSITORY */

    /**
     * Pedido GeneralRepository initializeCustomer
     */
    REPOSITORY_INITIALIZE_CUSTOMER_REQ,

    /**
     * Resposta GeneralRepository initializeCustomer
     */
    REPOSITORY_INITIALIZE_CUSTOMER_RESP,

    /**
     * Pedido GeneralRepository setCustomerState
     */
    REPOSITORY_SET_CUSTOMER_STATE_REQ,

    /**
     * Resposta GeneralRepository setCustomerState
     */
    REPOSITORY_SET_CUSTOMER_STATE_RESP,

    /**
     * Pedido GeneralRepository setCustomerCar
     */
    REPOSITORY_SET_CUSTOMER_CAR_REQ,

    /**
     * Resposta GeneralRepository setCustomerCar
     */
    REPOSITORY_SET_CUSTOMER_CAR_RESP,

    /**
     * Pedido GeneralRepository setManagerState
     */
    REPOSITORY_SET_MANAGER_STATE_REQ,

    /**
     * Resposta GeneralRepository setManagerState
     */
    REPOSITORY_SET_MANAGER_STATE_RESP,

    /**
     * Pedido GeneralRepository setMechanicState
     */
    REPOSITORY_SET_MECHANIC_STATE_REQ,

    /**
     * Resposta GeneralRepository setMechanicState
     */
    REPOSITORY_SET_MECHANIC_STATE_RESP,

    /**
     * Pedido GeneralRepository customerCarEntersPark
     */
    REPOSITORY_CUSTOMER_CAR_ENTERS_PARK_REQ,

    /**
     * Resposta GeneralRepository customerCarEntersPark
     */
    REPOSITORY_CUSTOMER_CAR_ENTERS_PARK_RESP,

    /**
     * Pedido GeneralRepository customerCarLeavesPark
     */
    REPOSITORY_CUSTOMER_CAR_LEAVES_PARK_REQ,

    /**
     * Resposta GeneralRepository customerCarLeavesPark
     */
    REPOSITORY_CUSTOMER_CAR_LEAVES_PARK_RESP,

    /**
     * Pedido GeneralRepository replacementCarEntersPark
     */
    REPOSITORY_REPLACEMENT_CAR_ENTERS_PARK_REQ,

    /**
     * Resposta GeneralRepository replacementCarEntersPark
     */
    REPOSITORY_REPLACEMENT_CAR_ENTERS_PARK_RESP,

    /**
     * Pedido GeneralRepository replacementCarLeavesPark
     */
    REPOSITORY_REPLACEMENT_CAR_LEAVES_PARK_REQ,

    /**
     * Resposta GeneralRepository replacementCarLeavesPark
     */
    REPOSITORY_REPLACEMENT_CAR_LEAVES_PARK_RESP,

    /**
     * Pedido GeneralRepository setCustomersInQueue
     */
    REPOSITORY_SET_CUSTOMERS_IN_QUEUE_REQ,

    /**
     * Resposta GeneralRepository setCustomersInQueue
     */
    REPOSITORY_SET_CUSTOMERS_IN_QUEUE_RESP,

    /**
     * Pedido GeneralRepository setCustomersInQueueForKey
     */
    REPOSITORY_SET_CUSTOMERS_IN_QUEUE_FOR_KEY_REQ,

    /**
     * Resposta GeneralRepository setCustomersInQueueForKey
     */
    REPOSITORY_SET_CUSTOMERS_IN_QUEUE_FOR_KEY_RESP,

    /**
     * Pedido GeneralRepository setTotalRepairedCars
     */
    REPOSITORY_SET_TOTAL_REPAIRED_CARS_REQ,

    /**
     * Resposta GeneralRepository setTotalRepairedCars
     */
    REPOSITORY_SET_TOTAL_REPAIRED_CARS_RESP,

    /**
     * Pedido GeneralRepository managerRequestedService
     */
    REPOSITORY_MANAGER_REQUESTED_SERVICE_REQ,

    /**
     * Resposta GeneralRepository managerRequestedService
     */
    REPOSITORY_MANAGER_REQUESTED_SERVICE_RESP,

    /**
     * Pedido GeneralRepository setStockParts
     */
    REPOSITORY_SET_STOCK_PARTS_REQ,

    /**
     * Resposta GeneralRepository setStockParts
     */
    REPOSITORY_SET_STOCK_PARTS_RESP,

    /**
     * Pedido GeneralRepository addMissingPart
     */
    REPOSITORY_ADD_MISSING_PART_REQ,

    /**
     * Resposta GeneralRepository addMissingPart
     */
    REPOSITORY_ADD_MISSING_PART_RESP,

    /**
     * Pedido GeneralRepository removeMissingPart
     */
    REPOSITORY_REMOVE_MISSING_PART_REQ,

    /**
     * Resposta GeneralRepository removeMissingPart
     */
    REPOSITORY_REMOVE_MISSING_PART_RESP,

    /**
     * Pedido GeneralRepository setMissingPartAlert
     */
    REPOSITORY_SET_MISSING_PART_ALERT_REQ,

    /**
     * Resposta GeneralRepository setMissingPartAlert
     */
    REPOSITORY_SET_MISSING_PART_ALERT_RESP,

    /**
     * Pedido GeneralRepository setSoldParts
     */
    REPOSITORY_SET_SOLD_PARTS_REQ,

    /**
     * Resposta GeneralRepository setSoldParts
     */
    REPOSITORY_SET_SOLD_PARTS_RESP,

    /**
     * Pedido GeneralRepository setCustomerCarRepaired
     */
    REPOSITORY_SET_CUSTOMER_CAR_REPAIRED_REQ,

    /**
     * Resposta GeneralRepository setCustomerCarRepaired
     */
    REPOSITORY_SET_CUSTOMER_CAR_REPAIRED_RESP,

    /**
     * Nenhuma
     */
    NONE
}
