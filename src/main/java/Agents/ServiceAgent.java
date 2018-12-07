package Agents;

import Behaviours.*;
import Data.ServiceProviderData;
import Exceptions.negativeValueException;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;

import java.util.ArrayList;

public class ServiceAgent extends GuiAgent {

    private ServiceAgentGUI serviceAgentGUI;

    private ServiceProviderInterface serviceInterface;

    @Override
    protected void setup() {
        serviceAgentGUI = new ServiceAgentGUI(this);

        ArrayList<CommonTask> tasks;

        serviceInterface = new ServiceProviderInterface();

        tasks = new ArrayList<>();

        tasks.add(serviceInterface);
        tasks.add(new ServiceProviderSecretary());
        tasks.add(new ServiceProviderScheduler());

        BasicBehaviour bb = new BasicBehaviour(tasks);

        for(CommonTask ct : tasks){
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
        try {
            serviceInterface.setServiceProviderData((ServiceProviderData) guiEvent.getParameter(0));
        } catch (negativeValueException e) {
            e.printStackTrace(); //TODO
        }
    }
}
