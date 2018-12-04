package main.java.Behaviours;

import main.java.Constants.Constants;

import jade.lang.acl.ACLMessage;

public class ServiceProviderScheduler extends ServiceProviderCommonBehaviour {
    @Override
    public void action() {

        ACLMessage msg = myAgent.blockingReceive();
        if (msg != null) {
            System.out.println("Message: " + msg.toString());
            String conversationId = msg.getConversationId();
            switch (conversationId) {
                case Constants.ServiceProviderSchedulerMessages.NOTIFY_CHANGES:
                    break;
                case Constants.ServiceProviderSchedulerMessages.RECEIVE_RESERVATION_TO_PROCESS:
                    break;
                case Constants.ServiceProviderSchedulerMessages.RECEIVE_SERVICE_DATA:
                    break;
                case Constants.ServiceProviderSchedulerMessages.SEND_RESERVATION_STATUS:
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