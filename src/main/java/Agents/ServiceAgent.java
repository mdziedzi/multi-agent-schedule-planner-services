package main.java.Agents;

import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import main.java.Behaviours.*;

public class ServiceAgent extends GuiAgent {

    private ServiceAgentGUI serviceAgentGUI;

    @Override
    protected void setup() {
        // init GUI
        serviceAgentGUI = new ServiceAgentGUI(this);

        addBehaviour(new ServiceProviderInterface());
        addBehaviour(new ServiceProviderScheduler());
        addBehaviour(new ServiceProviderSecretary());
    }

    @Override
    protected void takeDown() {
        // Printout a dismissal message
        System.out.println("Service-agent " + getAID().getName() + " terminating.");
    }

    @Override
    protected void onGuiEvent(GuiEvent guiEvent) {

    }
}
