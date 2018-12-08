package Behaviours;

import Constants.Constants;
import Data.ReservationData;
import Data.ServiceProviderData;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.Date;

public class ServiceProviderScheduler extends CommonTask {

    private static final int slotDuration = 15;

    private Date openingHour;
    private Date closingHour;
    private int maximumNumberOfPlaces;
    private ArrayList<ArrayList<ReservationData>> reservations;

    public ServiceProviderScheduler() {
        reservations = new ArrayList<>();
        openingHour = null;
        closingHour = null;
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
        ServiceProviderData serviceProviderData;
        serviceProviderData = ServiceProviderData.deserialize(msg.getContent());

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
        int openingMinutes = openingHour.getHours() * 60 + openingHour.getMinutes();
        int closingMinutes = closingHour.getHours() * 60 + openingHour.getMinutes();
        int newOpeningMinutes = serviceProviderData.openingHour.getHours() * 60 + serviceProviderData.openingHour.getMinutes();
        int newClosingMinutes = serviceProviderData.closingHour.getHours() * 60 + serviceProviderData.closingHour.getMinutes();
        if(openingMinutes < newOpeningMinutes){
            int slotsToDelete = (newOpeningMinutes - openingMinutes) / slotDuration;
            for(int y = 0; y < slotsToDelete; y++){
                for(int x = maximumNumberOfPlaces; x >= 0; x--) {
                    if(reservations.get(y).get(x) != null){
                        ACLMessage internalMsg = new ACLMessage();
                        internalMsg.setConversationId(Constants.ServiceProviderSecretaryMessages.CANCEL_RESERVATION);
                        internalMsg.setContent(ReservationData.serialize(reservations.get(y).get(x)));
                        SendMessageToOtherTask(internalMsg);
                    }
                }
                reservations.remove(y);
            }
        }
        if(openingMinutes > newOpeningMinutes){
            int slotsToInsert = (openingMinutes - newOpeningMinutes) / slotDuration;
            for(int y = 0; y < slotsToInsert; y++){
                reservations.add(0, new ArrayList<>(maximumNumberOfPlaces));
            }
        }
        if(closingMinutes > newClosingMinutes){
            int slotsToDelete = (closingMinutes - newClosingMinutes) / slotDuration;
            for(int y = 0; y < slotsToDelete; y++){
                for(int x = maximumNumberOfPlaces; x >= 0; x--) {
                    if(reservations.get(y).get(x) != null){
                        ACLMessage internalMsg = new ACLMessage();
                        internalMsg.setConversationId(Constants.ServiceProviderSecretaryMessages.CANCEL_RESERVATION);
                        internalMsg.setContent(ReservationData.serialize(reservations.get(y).get(x)));
                        SendMessageToOtherTask(internalMsg);
                    }
                }
                reservations.remove(y);
            }
        }
        if(closingMinutes < newClosingMinutes){
            int slotsToInsert = (newClosingMinutes - closingMinutes) / slotDuration;
            for(int y = 0; y < slotsToInsert; y++){
                reservations.add(0, new ArrayList<>(maximumNumberOfPlaces));
            }
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