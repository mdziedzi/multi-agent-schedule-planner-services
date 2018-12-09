package Agents;

import Constants.Constants;
import Data.ServiceProvider.ServiceProviderData;
import jade.gui.GuiEvent;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ServiceAgentGUI extends JFrame {

    private final ServiceAgent serviceAgent;
    private JPanel serviceDataPanel;
    private JPanel reservationPanel;
    private JPanel mainPanel;
    private JTextField[] fields;
    JButton sendBtn;
    JButton verifyBtn;
    private String[] labels = {
            "Opening hour",
            "Closing hour",
            "Max nr of places",
            "Name",
            "Type",
            "Address"};

    private String[] defaults = {
            "10:20",
            "11:40",
            "1",
            "Nazwa",
            "Dziekanat",
            "EiTi"
    };

    public ServiceAgentGUI(ServiceAgent serviceAgent) {
        this.serviceAgent = serviceAgent;

        createGUI();

        setSize(400, 320);


    }

    private void createGUI() {

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        serviceDataPanel = new JPanel();

        serviceDataPanel.setBackground(new Color(58, 121, 123));
        serviceDataPanel.setLayout(new BorderLayout());

        reservationPanel = new JPanel();
        reservationPanel.setLayout(new BorderLayout());

        JPanel labelPanel = new JPanel(new GridLayout(labels.length, 1));
        JPanel fieldPanel = new JPanel(new GridLayout(labels.length, 1));
        serviceDataPanel.add(labelPanel, BorderLayout.WEST);
        serviceDataPanel.add(fieldPanel, BorderLayout.CENTER);
        fields = new JTextField[labels.length];

        for (int i = 0; i < labels.length; i += 1) {
            fields[i] = new JTextField();
            fields[i].setColumns(20);

            JLabel lab = new JLabel(labels[i], JLabel.RIGHT);
            lab.setLabelFor(fields[i]);


            labelPanel.add(lab);
            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
            p.add(fields[i]);
            fieldPanel.add(p);
            fields[i].setText(defaults[i]);
        }

        fields[0].setToolTipText("hh:mm");
        fields[1].setToolTipText("hh:mm");


        sendBtn = new JButton("Send");
        sendBtn.addActionListener(e -> {
            try {
                GuiEvent ge = new GuiEvent(ServiceAgentGUI.this,
                        Constants.ServiceAgentGuiMessages.SERVICE_PROVIDER_DATA);
                ge.addParameter(new ServiceProviderData(
                        parseDate(fields[0].getText()),
                        parseDate(fields[1].getText()),
                        Integer.valueOf(fields[2].getText()),
                        fields[3].getText(),
                        fields[4].getText(),
                        fields[5].getText()));
                serviceAgent.postGuiEvent(ge);
            } catch (ParseException e1) {
                JOptionPane.showMessageDialog(
                        this,
                        "Opening/closing hours should be in format hh:mm.",
                        "Wrong hour format",
                        JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException e1) {
                JOptionPane.showMessageDialog(
                        this,
                        "Max number of seats should be a number",
                        "Wrong seats nr format",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        sendBtn.setSize(300, 50);
        
        serviceDataPanel.add(sendBtn, BorderLayout.AFTER_LAST_LINE);

        mainPanel.add(serviceDataPanel);
        mainPanel.add(new JSeparator(JSeparator.HORIZONTAL));

        JPanel labelReservationPanel = new JPanel(new GridLayout(2, 1));
        JPanel fieldReservationPanel = new JPanel(new GridLayout(2, 1));
        reservationPanel.add(labelReservationPanel, BorderLayout.WEST);
        reservationPanel.add(fieldReservationPanel, BorderLayout.CENTER);

        labelReservationPanel.add(new JLabel(" "));
        fieldReservationPanel.add(new JLabel(" "));

        JTextField reservationTextField = new JTextField();
        reservationTextField.setColumns(20);

        JLabel reservationLabel = new JLabel("Reservation ID");
        reservationLabel.setLabelFor(reservationTextField);
        labelReservationPanel.add(reservationLabel);

        fieldReservationPanel.add(reservationTextField);

        verifyBtn = new JButton("Verify");
        verifyBtn.addActionListener(e -> {
            GuiEvent ge = new GuiEvent(ServiceAgentGUI.this,
                    Constants.ServiceAgentGuiMessages.RESERVATION_DATA);
            ge.addParameter(Integer.valueOf(reservationTextField.getText()));
            serviceAgent.postGuiEvent(ge);
        });
        reservationPanel.add(verifyBtn, BorderLayout.SOUTH);

        mainPanel.add(reservationPanel);

        this.add(mainPanel);

        setTitle(serviceAgent.getLocalName());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setVisible(true);
    }


    private Date parseDate(String date) throws ParseException {
        return new SimpleDateFormat("hh:mm").parse(date);
    }


    public void showReservationInfo() {
        JFrame reservationFrame = new JFrame("Reservation");
        JPanel panel = new JPanel();
        reservationFrame.setVisible(true);
    }
}
