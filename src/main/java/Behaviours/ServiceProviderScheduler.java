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
            String conversationId = msg.getConversationId();
            switch (conversationId) {
                case Constants.ServiceProviderSchedulerMessages.NOTIFY_CHANGES:
                    return onNotyfiChanges(msg);
                case Constants.ServiceProviderSchedulerMessages.RECEIVE_RESERVATION_TO_PROCESS:
                    return onReceiveReservationToProcess(msg);
                case Constants.ServiceProviderSchedulerMessages.RECEIVE_SERVICE_DATA:
                    return onReceiveServiceData(msg);
                case Constants.ServiceProviderSchedulerMessages.SEND_RESERVATION_STATUS:
                    return onSendReservationStatus(msg);
                default:
                    return createNotUnderstoodMessage(msg);
            }
        }
        return new ACLMessage();
    }

    private ACLMessage onNotyfiChanges(ACLMessage msg){
        //TODO
        return null;
    }
    private ACLMessage onReceiveReservationToProcess(ACLMessage msg){
        //TODO
        return null;
    }
    private ACLMessage onReceiveServiceData(ACLMessage msg){
        //TODO
        return null;
    }
    private ACLMessage onSendReservationStatus(ACLMessage msg){
        //TODO
        return null;
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