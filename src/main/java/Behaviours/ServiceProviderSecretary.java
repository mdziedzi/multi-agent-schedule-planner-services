package Behaviours;

import Constants.*;
import jade.lang.acl.ACLMessage;

public class ServiceProviderSecretary extends ServiceProviderCommonBehaviour {
    @Override
    public void action() {
        if (msg != null) {
            System.out.println("Message: " + msg.toString());
            String conversationId = msg.getConversationId();
            switch (conversationId) {
                case Constants.ServiceProviderSecretaryMessages.RECEIVE_RESERVATION_STATUS:
                    break;
                case Constants.ServiceProviderSecretaryMessages.RECEIVE_RESERVATION_REQUEST:
                    break;
                case Constants.ServiceProviderSecretaryMessages.SEND_RESERVATION_RESPONSE:
                    break;
                case Constants.ServiceProviderSecretaryMessages.SEND_RESERVATION_TO_PROCESS:
                    break;
                case Constants.ServiceProviderSecretaryMessages.SEND_SERVICE_DATA:
                    break;
                default:
                    myAgent.send(createNotUnderstoodMessage(msg));
                    break;
            }
        }
    }

    @Override
    public boolean isMessageRelevant(ACLMessage msg) {
        if(msg != null)
        {
            switch(msg.getConversationId()){
                case Constants.ServiceProviderSecretaryMessages.RECEIVE_RESERVATION_STATUS:
                case Constants.ServiceProviderSecretaryMessages.RECEIVE_RESERVATION_REQUEST:
                case Constants.ServiceProviderSecretaryMessages.SEND_RESERVATION_RESPONSE:
                case Constants.ServiceProviderSecretaryMessages.SEND_RESERVATION_TO_PROCESS:
                case Constants.ServiceProviderSecretaryMessages.SEND_SERVICE_DATA:
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }

    @Override
    public boolean done() {
        return false;
    }
}