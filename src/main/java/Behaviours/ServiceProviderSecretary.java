package main.java.Behaviours;

import main.java.Constants.Constants;

import jade.lang.acl.ACLMessage;

public class ServiceProviderSecretary extends ServiceProviderCommonBehaviour {

    @Override
    public void action() {
        ACLMessage msg = myAgent.blockingReceive();
        if (msg != null) {
            System.out.println("Message: " + msg.toString());
            String conversationId = msg.getConversationId();
            switch (conversationId) {
                case Constants.ServiceProviderSecretaryMessages.RECEIVE_RESERVATION_STATUS:
                    break;
                case Constants.ServiceProviderSecretaryMessages.RECEVICE_RESERVATION_REQUEST:
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
    public boolean done() {
        return false;
    }
}