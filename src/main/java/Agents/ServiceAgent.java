package Agents;

import Behaviours.*;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;

import java.util.ArrayList;

public class ServiceAgent extends GuiAgent {

    private ServiceAgentGUI serviceAgentGUI;

    @Override
    protected void setup() {
        serviceAgentGUI = new ServiceAgentGUI(this);

        ArrayList<CommonBehaviour> myBehaviours;

        myBehaviours = new ArrayList<>();

        myBehaviours.add(new ServiceProviderInterface());
        myBehaviours.add(new ServiceProviderSecretary());
        myBehaviours.add(new ServiceProviderScheduler());

        addBehaviour(new BasicBehaviour(myBehaviours));
    }

    @Override
    protected void takeDown() {
        System.out.println("Service-agent " + getAID().getName() + " terminating.");
    }

    @Override
    protected void onGuiEvent(GuiEvent guiEvent) {
        System.out.println(guiEvent.getParameter(0));
    }
}
