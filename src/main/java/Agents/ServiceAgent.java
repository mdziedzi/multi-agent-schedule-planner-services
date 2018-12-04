package Agents;

import Behaviours.ServiceProviderBasicBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;

public class ServiceAgent extends GuiAgent {

    private ServiceAgentGUI serviceAgentGUI;

    @Override
    protected void setup() {
        serviceAgentGUI = new ServiceAgentGUI(this);

        addBehaviour(new ServiceProviderBasicBehaviour());
    }

    @Override
    protected void takeDown() {
        System.out.println("Service-agent " + getAID().getName() + " terminating.");
    }

    @Override
    protected void onGuiEvent(GuiEvent guiEvent) {

    }
}
