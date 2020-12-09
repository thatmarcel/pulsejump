package com.thatmarcel.jpmg.helpers.serial;

import arduino.Arduino;
import arduino.PortDropdownMenu;
import com.almasb.fxgl.dsl.FXGL;
import com.thatmarcel.jpmg.JPMGApp;
import com.thatmarcel.jpmg.helpers.config.Config;
import com.thatmarcel.jpmg.helpers.pulse.PulseAction;
import com.thatmarcel.jpmg.helpers.strings.Strings;
import com.thatmarcel.jpmg.helpers.ui.UIManager;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.*;
import java.awt.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class SerialCommunication {
    private static Arduino arduino;

    private static Stage dialog;
    private static JButton connectButton;
    private static PortDropdownMenu portList;
    private static JButton refreshButton;

    public static int lastBPM;

    private static ArrayList<Instant> pulseTimings;

    public static void start() {
        pulseTimings = new ArrayList<>();

        final SwingNode swingNode = new SwingNode();

        SwingUtilities.invokeLater(() -> {
            portList = new PortDropdownMenu();
            portList.refreshMenu();

            FlowLayout layout = new FlowLayout();
            layout.setHgap(16);
            layout.setVgap(16);

            JPanel panel = new JPanel();
            panel.setLayout(new GridBagLayout());

            JPanel secondPanel = new JPanel();
            secondPanel.setLayout(layout);

            refreshButton = new JButton(Strings.refresh);
            refreshButton.addActionListener(ev -> portList.refreshMenu());

            connectButton = new JButton(Strings.connect);
            connectButton.addActionListener(ev -> {
                if (portList.getSelectedItem() != null) {
                    connect(portList.getSelectedItem().toString());
                }
            });

            JLabel titleLabel = new JLabel(Strings.serialConnectionTitle);
            titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 24f));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;

            secondPanel.add(portList);
            secondPanel.add(refreshButton);

            panel.add(titleLabel, gbc);
            panel.add(secondPanel, gbc);
            panel.add(connectButton, gbc);

            swingNode.setContent(panel);

            FXGL.getGameTimer().runOnceAfter(() -> {
                StackPane pane = new StackPane();
                pane.getChildren().add(swingNode);
                dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(FXGL.getGameScene().getRoot().getScene().getWindow());
                Scene dialogScene = new Scene(pane, Config.Window.ConnectionPopup.width, Config.Window.ConnectionPopup.height);
                dialog.setScene(dialogScene);
                dialog.show();
            }, Duration.millis(10));
        });
    }

    private static void connect(String port) {
        SwingUtilities.invokeLater(() -> {
            connectButton.setEnabled(false);
            refreshButton.setEnabled(false);
            portList.setEnabled(false);
            connectButton.setText(Strings.connecting);

            new Thread(() -> {
                arduino = new Arduino(port, 9600);

                if (arduino.openConnection()) {
                    FXGL.getGameTimer().runOnceAfter(dialog::close, Duration.millis(10));
                    startPolling();
                } else {
                    SwingUtilities.invokeLater(() -> {
                        connectButton.setEnabled(true);
                        refreshButton.setEnabled(true);
                        portList.setEnabled(true);
                        connectButton.setText(Strings.connect);
                    });
                }
            }).start();
        });
    }

    private static void startPolling() {
        if (Config.SerialConnection.isTesting) {
            FXGL.getGameTimer().runAtInterval(() -> {
                handlePulse();
                FXGL.getGameTimer().runOnceAfter(UIManager.activeInstance::doHeartbeat, Duration.millis(10));
            }, Duration.seconds(0.6));
        } else {
            new Thread(() -> {
                while (JPMGApp.isRunning) {
                    arduino.waitForMessage();
                    handlePulse();
                    FXGL.getGameTimer().runOnceAfter(UIManager.activeInstance::doHeartbeat, Duration.millis(10));
                }
            }).start();
        }
    }

    private static void handlePulse() {
        pulseTimings.add(Instant.now());
        updateBPM();
    }

    private static void updateBPM() {
        if (pulseTimings.size() < 2) {
            return;
        }

        pulseTimings.removeIf(pulseTiming -> Instant.now().isAfter(pulseTiming.plus(java.time.Duration.ofSeconds(10))));
        long diffMillis = ChronoUnit.MILLIS.between(pulseTimings.get(0), Instant.now());
        double diffSeconds = (double) diffMillis / 1000;
        int bpm = (int) (pulseTimings.size() * (60 / diffSeconds));
        lastBPM = bpm;
        FXGL.getGameTimer().runOnceAfter(() -> UIManager.activeInstance.updateBPM(bpm), Duration.millis(10));
        PulseAction.lastBPM = bpm;
    }
}
