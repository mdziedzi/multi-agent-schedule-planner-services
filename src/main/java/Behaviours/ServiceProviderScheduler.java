package Behaviours;

import Constants.*;

import Data.ReservationData;
import jade.lang.acl.ACLMessage;


import java.util.ArrayList;
import java.util.Date;

public class ServiceProviderScheduler extends ServiceProviderCommonBehaviour {

    private static final Date slotDuration = new Date(0, 0,0,0,15);

    private ArrayList<ArrayList<ReservationData>> reservations;

    public ServiceProviderScheduler(){
        reservations = null;
    }

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