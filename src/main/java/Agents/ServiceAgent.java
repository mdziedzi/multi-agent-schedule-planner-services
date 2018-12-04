package main.java.Agents;

import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;

public class ServiceAgent extends GuiAgent {

    private int arg0;
    private int arg1;
    private ServiceAgentGUI serviceAgentGUI;

    @Override
    protected void setup() {

        // set args
        Object[] args = getArguments();
        if (args != null) {
            if (args.length > 0) this.arg0 = (Integer) args[0];
            if (args.length > 1) this.arg1 = (Integer) args[1];
            System.out.println("Created ServiceAgent " + getAID().getName() + " with arg0 " + this.arg0
                    + " and arg1 " + this.arg1);
        }


        // init GUI
        serviceAgentGUI = new ServiceAgentGUI(this);

        // Register the parking service in the yellow pages
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("multi-agent-schedule-planner");
        sd.setName("service");
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }


        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                System.out.println("Agent " + getLocalName() + " say: hello");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void takeDown() {
        // Deregister from the yellow pages
        try {
            DFService.deregister(this);
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }

        // Printout a dismissal message
        System.out.println("Service-agent " + getAID().getName() + " terminating.");
    }

    @Override
    protected void onGuiEvent(GuiEvent guiEvent) {

    }
}
