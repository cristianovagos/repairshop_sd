package service;

import comm.*;
import model.ManagerState;
import service.proxy.ClientProxy;

/**
 * Este tipo de dados define o interface ao SupplierSite, uma das regiões partilhadas do problema Repair Shop Activities,
 * que implementa o modelo cliente-servidor de tipo 2 (replicação do servidor).
 *
 * @author Miguel Bras
 * @author Cristiano Vagos
 */
public class SupplierSiteInterface {
    /**
     *  Instanciação do interface ao SupplierSite.
     *  @param supplierSite SupplierSite
     */
    public SupplierSiteInterface(SupplierSite supplierSite) {
        this.supplierSite = supplierSite;
    }

    /**
     *  SupplierSite (representa o serviço a ser prestado)
     */
    private SupplierSite supplierSite;

    /**
     *  Processamento das mensagens através da execução da tarefa correspondente.
     *  Geração de uma mensagem de resposta.
     *
     *  @param inMessage mensagem com o pedido
     *  @return mensagem de resposta
     *  @throws MessageException se a mensagem com o pedido for considerada inválida
     */
    public Message processAndReply(Message inMessage) throws MessageException {
        Message outMessage = null;                           // mensagem de resposta

        /* validação da mensagem recebida */
        switch (inMessage.getMessageType()) {
            case SUPPLIER_SITE_GO_TO_SUPPLIER_REQ:
                // nothing to do
                break;
            case NONE:
            default:
                throw new MessageException("Tipo de mensagem invalida!", inMessage);
        }

        /* seu processamento */

        switch (inMessage.getMessageType()) {
            case SUPPLIER_SITE_GO_TO_SUPPLIER_REQ:
                int[] newParts = supplierSite.goToSupplier();
                ManagerState state = ((ClientProxy) Thread.currentThread()).getManagerState();
                outMessage = new Message(MessageType.SUPPLIER_SITE_GO_TO_SUPPLIER_RESP, state, newParts);
                break;
        }

        return (outMessage);
    }
}
