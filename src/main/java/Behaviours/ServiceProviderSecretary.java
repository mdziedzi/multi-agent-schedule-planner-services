package Behaviours;

import Constants.Constants;
import Data.ServiceProvider.ReservationData;
import Data.Common.ReservationRequest;
import Data.ServiceProvider.ServiceProviderData;
import jade.lang.acl.ACLMessage;

public class ServiceProviderSecretary extends CommonTask {

    private ServiceProviderData serviceProviderData;

    public ServiceProviderSecretary() {
        serviceProviderData = new ServiceProviderData();
    }

    @Override
    public ACLMessage ProcessMessage(ACLMessage msg) {
        if (msg != null) {
            String conversationId = msg.getConversationId();
            switch (conversationId) {
                case Constants.ServiceProviderSecretaryMessages.RECEIVE_RESERVATION_STATUS:
                    return onReceiveReservationStatus(msg);
                case Constants.ServiceProviderSecretaryMessages.RECEIVE_RESERVATION_REQUEST:
                    return onReceiveReservationRequest(msg);
                case Constants.ServiceProviderSecretaryMessages.SEND_RESERVATION_RESPONSE:
                    return onSendReservationResponse(msg);
                case Constants.ServiceProviderSecretaryMessages.SEND_RESERVATION_TO_PROCESS:
                    return onSendReservationToProcess(msg);
                case Constants.ServiceProviderSecretaryMessages.SEND_SERVICE_DATA:
                    return onSendServiceData(msg);
                case Constants.ServiceProviderSecretaryMessages.RECEIVE_SERVICE_DATA:
                    return onReceiveServiceData(msg);
                case Constants.ServiceProviderSecretaryMessages.CANCEL_RESERVATION:
                    return onCancelReservation(msg);
                default:
                    return createNotUnderstoodMessage(msg);
            }
        }
        return new ACLMessage();
    }



    private ACLMessage onReceiveReservationStatus(ACLMessage msg){
        //TODO
        return null;
    }

    private ACLMessage onReceiveReservationRequest(ACLMessage msg){
        ReservationRequest reservationRequest = new ReservationRequest();
        reservationRequest = ReservationRequest.deserialize(msg.getContent());
        ReservationData reservationData = new ReservationData();
        reservationData.agentId = msg.getSender();
        reservationData.beginHour = reservationRequest.beginHour;
        reservationData.endHour = reservationRequest.endHour;

        ACLMessage internalMsg = new ACLMessage();
        internalMsg.setConversationId(Constants.ServiceProviderSchedulerMessages.RECEIVE_RESERVATION_TO_PROCESS);
        internalMsg.setContent(ReservationData.serialize(reservationData));
        SendMessageToOtherTask(internalMsg);

        return null;
    }

    private ACLMessage onSendReservationResponse(ACLMessage msg){
        //TODO
        return null;
    }

    private ACLMessage onSendReservationToProcess(ACLMessage msg){
        //TODO
        return null;
    }

    private ACLMessage onSendServiceData(ACLMessage msg){
        ACLMessage internalMsg = new ACLMessage();
        internalMsg.setConversationId(Constants.ServiceProviderInterfaceMessages.SEND_SERVICE_DATA);
        SendMessageToOtherTask(internalMsg);
        ACLMessage reply = msg.createReply();
        reply.setConversationId("TODO"); //TODO: set as Client constant
        reply.setContent(ServiceProviderData.serialize(serviceProviderData));
        return reply;
    }

    private ACLMessage onReceiveServiceData(ACLMessage msg){
        //TODO
        serviceProviderData = ServiceProviderData.deserialize(msg.getContent());
        return null;
    }

    private ACLMessage onCancelReservation(ACLMessage msg) {
        //TODO
        return null;
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
                case Constants.ServiceProviderSecretaryMessages.CANCEL_RESERVATION:
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }
}