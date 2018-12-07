package Behaviours;

import Constants.Constants;
import Data.ReservationData;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.Date;

public class ServiceProviderScheduler extends CommonTask {

    private static final Date slotDuration = new Date(0, 0, 0, 0, 15);

    private ArrayList<ArrayList<ReservationData>> reservations;

    public ServiceProviderScheduler() {
        reservations = null;
    }

    @Override
    public ACLMessage ProcessMessage(ACLMessage msg) {
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
                    return createNotUnderstoodMessage(msg);
            }
        }
        return new ACLMessage();
    }

    @Override
    public boolean isMessageRelevant(ACLMessage msg) {
        if (msg != null) {
            switch (msg.getConversationId()) {
                case Constants.ServiceProviderSchedulerMessages.NOTIFY_CHANGES:
                case Constants.ServiceProviderSchedulerMessages.RECEIVE_RESERVATION_TO_PROCESS:
                case Constants.ServiceProviderSchedulerMessages.RECEIVE_SERVICE_DATA:
                case Constants.ServiceProviderSchedulerMessages.SEND_RESERVATION_STATUS:
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }
}