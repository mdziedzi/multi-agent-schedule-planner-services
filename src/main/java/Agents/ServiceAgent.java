package main.java.Agents;

import jade.core.behaviours.ParallelBehaviour;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import main.java.Behaviours.*;

import javax.naming.PartialResultException;

public class ServiceAgent extends GuiAgent {

    private ServiceAgentGUI serviceAgentGUI;

    @Override
    protected void setup() {
        // init GUI
        serviceAgentGUI = new ServiceAgentGUI(this);

        ParallelBehaviour parallelBehaviour = new ParallelBehaviour();

        parallelBehaviour.addSubBehaviour(new ServiceProviderInterface());
        parallelBehaviour.addSubBehaviour(new ServiceProviderScheduler());
        parallelBehaviour.addSubBehaviour(new ServiceProviderSecretary());

        addBehaviour(parallelBehaviour);
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
