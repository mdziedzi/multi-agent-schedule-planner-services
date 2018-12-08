package Behaviours;

import Constants.Constants;
import Data.ServiceProviderData;
import jade.lang.acl.ACLMessage;

public class ServiceProviderSecretary extends CommonTask {

    private ServiceProviderData serviceProviderData;

    public ServiceProviderSecretary() {
        this.serviceProviderData = new ServiceProviderData();
    }

    @Override
    public ACLMessage ProcessMessage(ACLMessage msg) {
        if (msg != null) {
            String conversationId = msg.getConversationId();
            switch (conversationId) {
                case Constants.ServiceProviderSecretaryMessages.RECEIVE_RESERVATION_STATUS:
                    //TODO
                    break;
                case Constants.ServiceProviderSecretaryMessages.RECEIVE_RESERVATION_REQUEST:
                    //TODO
                    break;
                case Constants.ServiceProviderSecretaryMessages.SEND_RESERVATION_RESPONSE:
                    //TODO
                    break;
                case Constants.ServiceProviderSecretaryMessages.SEND_RESERVATION_TO_PROCESS:
                    //TODO
                    break;
                case Constants.ServiceProviderSecretaryMessages.SEND_SERVICE_DATA:
                    ACLMessage internalMsg = new ACLMessage();
                    internalMsg.setConversationId(Constants.ServiceProviderInterfaceMessages.SEND_SERVICE_DATA);
                    basicBehaviour.SendMessageToTask(internalMsg);
                    ACLMessage reply = msg.createReply();
                    reply.setConversationId("TODO"); //TODO: set as Client constant
                    reply.setContent(ServiceProviderData.toString(serviceProviderData));
                    return reply;
                case Constants.ServiceProviderSecretaryMessages.RECEIVE_SERVICE_DATA:
                    serviceProviderData = ServiceProviderData.fromString(msg.getContent());
                    break;
                default:
                    return createNotUnderstoodMessage(msg);
            }
        }
        return new ACLMessage();
    }

    @Override
    public boolean isMessageRelevant(ACLMessage msg) {
        if (msg != null) {
            switch (msg.getConversationId()) {
                case Constants.ServiceProviderSecretaryMessages.RECEIVE_RESERVATION_STATUS:
                case Constants.ServiceProviderSecretaryMessages.RECEIVE_RESERVATION_REQUEST:
                case Constants.ServiceProviderSecretaryMessages.SEND_RESERVATION_RESPONSE:
                case Constants.ServiceProviderSecretaryMessages.SEND_RESERVATION_TO_PROCESS:
                case Constants.ServiceProviderSecretaryMessages.SEND_SERVICE_DATA:
                case Constants.ServiceProviderSecretaryMessages.RECEIVE_SERVICE_DATA:
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }
}