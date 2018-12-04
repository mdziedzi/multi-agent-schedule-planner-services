package Behaviours;

import Constants.*;
import Data.ServiceProviderData;
import Exceptions.negativeValueException;
import Interfaces.ServiceProviderInterfaceInterface;
import jade.lang.acl.ACLMessage;

import java.util.Date;
import java.util.Objects;


public class ServiceProviderInterface extends ServiceProviderCommonBehaviour implements ServiceProviderInterfaceInterface {

    private Date openingHour = null;
    private Date closingHour = null;
    private int maximumNumberOfPlaces = 0;
    private String name = null;
    private String type = null;
    private String address = null;

    @Override
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
    }

    @Override
    public void action() {
        ACLMessage aclMessage = myAgent.blockingReceive();
        if (aclMessage != null) {
            System.out.println("Message: " + aclMessage.toString());
            String conversationId = aclMessage.getConversationId();
            switch (conversationId) {
                case Constants.ServiceProviderInterfaceMessages.VERIFY_RESERVATION:
                    break;
                case Constants.ServiceProviderInterfaceMessages.SEND_SERVICE_DATA:
                    ACLMessage msg = new ACLMessage();
                    msg.setConversationId(Constants.ServiceProviderSchedulerMessages.RECEIVE_SERVICE_DATA);

                    //msg.setContent();
                    myAgent.send(msg);
                    break;
                default:
                    myAgent.send(createNotUnderstoodMessage(aclMessage));
                    break;
            }
        }
    }

    public boolean done() {
        return false;
    }
}