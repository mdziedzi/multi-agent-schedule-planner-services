package Behaviours;

import Constants.Constants;
import Data.ReservationData;
import Data.ServiceProviderData;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.Date;

public class ServiceProviderScheduler extends CommonTask {

    private static final Date slotDuration = new Date(0, 0, 0, 0, 15);

    private Date beginHour;
    private Date endHour;
    private int maximumNumberOfPlaces;
    private ArrayList<ArrayList<ReservationData>> reservations;

    public ServiceProviderScheduler() {
        reservations = new ArrayList<>();
        beginHour = null;
        endHour = null;
        maximumNumberOfPlaces = 0;
    }

    @Override
    public ACLMessage ProcessMessage(ACLMessage msg) {
        if (msg != null) {
            String conversationId = msg.getConversationId();
            switch (conversationId) {
                case Constants.ServiceProviderSchedulerMessages.NOTIFY_CHANGES:
                    return onNotifyChanges(msg);
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

    private ACLMessage onNotifyChanges(ACLMessage msg) {
        //TODO
        return null;
    }

    private ACLMessage onReceiveReservationToProcess(ACLMessage msg) {
        //TODO
        return null;
    }

    private ACLMessage onReceiveServiceData(ACLMessage msg) {
        ServiceProviderData serviceProviderData = new ServiceProviderData();
        serviceProviderData = ServiceProviderData.deserialize(msg.getContent());
        ArrayList<ArrayList<ReservationData>> newReservations = new ArrayList<>();

        if (maximumNumberOfPlaces > serviceProviderData.maximumNumberOfPlaces) {
            for (int y = 0; y < reservations.size(); y++) {
                for(int x = maximumNumberOfPlaces;x >= serviceProviderData.maximumNumberOfPlaces; x--){
                    if(reservations.get(y).get(x) != null){
                        ACLMessage internalMsg = new ACLMessage();
                        internalMsg.setConversationId(Constants.ServiceProviderSecretaryMessages.CANCEL_RESERVATION);
                        internalMsg.setContent(ReservationData.serialize(reservations.get(y).get(x)));
                        SendMessageToOtherTask(internalMsg);
                        reservations.get(y).remove(x);
                    }
                }

            }
        }
        if(maximumNumberOfPlaces < serviceProviderData.maximumNumberOfPlaces){
            for(int y = 0; y < reservations.size(); y++){
                for(int x = maximumNumberOfPlaces; x < serviceProviderData.maximumNumberOfPlaces; x++){
                    reservations.get(y).add(null);
                }
            }
        }


        if (maximumNumberOfPlaces != serviceProviderData.maximumNumberOfPlaces ||
                beginHour != serviceProviderData.openingHour ||
                endHour != serviceProviderData.closingHour) {


        }
        return null;
    }

    private ACLMessage onSendReservationStatus(ACLMessage msg) {
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