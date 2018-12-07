package Behaviours;

import Constants.Constants;
import Data.ServiceProviderData;
import Exceptions.negativeValueException;
import jade.lang.acl.ACLMessage;

import java.util.Date;
import java.util.Objects;


public class ServiceProviderInterface extends CommonTask {

    private Date openingHour = null;
    private Date closingHour = null;
    private int maximumNumberOfPlaces = 0;
    private String name = null;
    private String type = null;
    private String address = null;

    public void setServiceProviderData(ServiceProviderData data) throws negativeValueException {
        if (data.openingHour != null && openingHour != data.openingHour) {
            openingHour = data.openingHour;
        }
        if (data.closingHour != null && closingHour != data.closingHour) {
            closingHour = data.closingHour;
        }
        if (data.maximumNumberOfPlaces != 0 && maximumNumberOfPlaces != data.maximumNumberOfPlaces) {
            if (data.maximumNumberOfPlaces < 0) {
                throw new negativeValueException("maximumNumberOfPlaces");
            }
            maximumNumberOfPlaces = data.maximumNumberOfPlaces;
        }
        if (data.name != null && !Objects.equals(name, data.name)) {
            name = data.name;
        }
        if (data.type != null && !Objects.equals(type, data.type)) {
            type = data.type;
        }
        if (data.address != null && !Objects.equals(address, data.address)) {
            address = data.address;
        }

        ServiceProviderData.fromString(data.toString());
    }

    @Override
    public ACLMessage ProcessMessage(ACLMessage msg) {
        if (msg != null) {
            System.out.println("Message: " + msg.toString());
            String conversationId = msg.getConversationId();
            switch (conversationId) {
                case Constants.ServiceProviderInterfaceMessages.VERIFY_RESERVATION:
                    break;
                case Constants.ServiceProviderInterfaceMessages.SEND_SERVICE_DATA:
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
                case Constants.ServiceProviderInterfaceMessages.SEND_SERVICE_DATA:
                case Constants.ServiceProviderInterfaceMessages.VERIFY_RESERVATION:
                    return true;
                default:
                    return false;
            }
        }
        return false;
    }
}