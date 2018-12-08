package Agents;

import Behaviours.*;
import Constants.Constants;
import Data.ServiceProviderData;
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
        System.out.println("Service-agent " + getAID().getName() + " terminating.");
    }

    @Override
    protected void onGuiEvent(GuiEvent guiEvent) {
        System.out.println(guiEvent.getParameter(0));
        ACLMessage msg = new ACLMessage();
        msg.setConversationId(Constants.ServiceProviderInterfaceMessages.SET_SERVICE_DATA);
        msg.setContent(ServiceProviderData.toString((ServiceProviderData) guiEvent.getParameter(0)));
    }
}
