package Agents;

import Behaviours.*;
import Constants.Constants;
import Data.ServiceProvider.ServiceProviderData;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;

public class ServiceAgent extends GuiAgent {

    private ServiceAgentGUI serviceAgentGUI;
    private BasicBehaviour bb;

    @Override
    protected void setup() {
        serviceAgentGUI = new ServiceAgentGUI(this);

        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("service-provider");
        sd.setName("service-provider-default-name");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        ArrayList<CommonTask> tasks;

        tasks = new ArrayList<>();

        tasks.add(new ServiceProviderInterface());
        tasks.add(new ServiceProviderSecretary());
        tasks.add(new ServiceProviderScheduler());

        bb = new BasicBehaviour(tasks);

        for (CommonTask ct : tasks) {
            ct.SetBasicBehaviour(bb);
        }

        addBehaviour(bb);
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        System.out.println("Service-agent " + getAID().getName() + " terminating.");

    }

    @Override
    protected void onGuiEvent(GuiEvent guiEvent) {
        ACLMessage msg = new ACLMessage();
        if (guiEvent.getType() == Constants.ServiceAgentGuiMessages.SERVICE_PROVIDER_DATA) {
            msg.setConversationId(Constants.ServiceProviderInterfaceMessages.SET_SERVICE_DATA);
            msg.setContent(ServiceProviderData.serialize((ServiceProviderData) guiEvent.getParameter(0)));
            bb.SendMessageToTask(msg);
        } else if (guiEvent.getType() == Constants.ServiceAgentGuiMessages.RESERVATION_DATA) {
            msg.setConversationId(Constants.ServiceProviderInterfaceMessages.VERIFY_RESERVATION);
            msg.setContent(String.valueOf(guiEvent.getParameter(0)));
            String info = bb.SendMessageToTask(msg);
            serviceAgentGUI.showReservationInfo(info);
        }
    }
}
