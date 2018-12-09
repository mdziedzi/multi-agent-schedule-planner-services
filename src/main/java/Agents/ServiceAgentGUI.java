package Agents;

import Constants.Constants;
import com.github.lgooddatepicker.components.TimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;
import Data.ServiceProvider.ServiceProviderData;
import jade.gui.GuiEvent;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;

public class ServiceAgentGUI extends JFrame {

    private final ServiceAgent serviceAgent;
    private JPanel serviceDataPanel;
    private JPanel reservationPanel;
    private JPanel reservationInfoPanel;
    private JPanel mainPanel;
    private JTextField[] fields;
    JButton sendBtn;
    JButton verifyBtn;
    private String[] labels = {
            "Max nr of places",
            "Name",
            "Type",
            "Address",
            "Open from to"};

    private String[] defaults = {
            "1",
            "Nazwa",
            "Dziekanat",
            "EiTi"
    };

    public ServiceAgentGUI(ServiceAgent serviceAgent) {
        this.serviceAgent = serviceAgent;

        createGUI();

        setSize(450, 350);
    }

    private void createGUI() {

        mainPanel = new JPanel();

        serviceDataPanel = new JPanel();

        serviceDataPanel.setBackground(new Color(58, 121, 123));
        serviceDataPanel.setLayout(new BorderLayout());

        reservationPanel = new JPanel();
        reservationPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        JPanel labelPanel = new JPanel(new GridLayout(labels.length, 1));
        JPanel fieldPanel = new JPanel(new GridLayout(labels.length, 1));
        serviceDataPanel.add(labelPanel, BorderLayout.WEST);
        serviceDataPanel.add(fieldPanel, BorderLayout.CENTER);
        fields = new JTextField[labels.length - 1];

        TimePickerSettings timeSettings = new TimePickerSettings();
        timeSettings.use24HourClockFormat();
        timeSettings.initialTime = LocalTime.of(0, 0);
        timeSettings.generatePotentialMenuTimes(
                TimePickerSettings.TimeIncrement.FifteenMinutes,
                null,
                null);
        TimePicker openPicker = new TimePicker(timeSettings);
        timeSettings.initialTime = LocalTime.of(0, 15);
        TimePicker endPicker = new TimePicker(timeSettings);

        JLabel openLabel = new JLabel(labels[4], JLabel.RIGHT);
        openLabel.setLabelFor(openPicker);
        labelPanel.add(openLabel);

        JPanel pickersPanel = new JPanel();

        pickersPanel.add(openPicker);
        pickersPanel.add(endPicker);

        try {
            System.out.println(parseDate(openPicker.getText()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        fieldPanel.add(pickersPanel);

        for (int i = 0; i < labels.length - 1; i += 1) {
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

        sendBtn = new JButton("Send");
        sendBtn.addActionListener(e -> {
            try {
                GuiEvent ge = new GuiEvent(ServiceAgentGUI.this,
                        Constants.ServiceAgentGuiMessages.SERVICE_PROVIDER_DATA);
                ge.addParameter(new ServiceProviderData(
                        parseDate(openPicker.getText()),
                        parseDate(endPicker.getText()),
                        Integer.valueOf(fields[0].getText()),
                        fields[1].getText(),
                        fields[2].getText(),
                        fields[3].getText()));
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

        serviceDataPanel.add(sendBtn, BorderLayout.AFTER_LAST_LINE);

        mainPanel.add(serviceDataPanel);
        mainPanel.add(new JSeparator(JSeparator.HORIZONTAL));

        JTextField reservationTextField = new JTextField();
        reservationTextField.setColumns(20);

        JLabel reservationLabel = new JLabel("Reservation ID");
        reservationLabel.setLabelFor(reservationTextField);

        reservationPanel.add(reservationLabel);
        reservationPanel.add(reservationTextField);

        verifyBtn = new JButton("Verify");

        verifyBtn.addActionListener(e -> {
            if (!reservationTextField.getText().equals("")) {
                GuiEvent ge = new GuiEvent(ServiceAgentGUI.this,
                        Constants.ServiceAgentGuiMessages.RESERVATION_DATA);
                ge.addParameter(Integer.valueOf(reservationTextField.getText()));
                serviceAgent.postGuiEvent(ge);
            }
        });
        reservationPanel.add(verifyBtn);

        mainPanel.add(reservationPanel);

        reservationInfoPanel = new JPanel();

        mainPanel.add(reservationInfoPanel);


        this.add(mainPanel);

        setTitle(serviceAgent.getLocalName());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setVisible(true);
    }


    private Date parseDate(String date) throws ParseException {
        return new SimpleDateFormat("hh:mm").parse(date);
    }


    public void showReservationInfo(String info) {
        reservationInfoPanel.add(new JLabel(info));
        ServiceAgentGUI.this.revalidate();
    }
}
