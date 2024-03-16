package fr.flastar.programme;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class SettingsWindow extends JFrame {

    public SettingsWindow(LogicManager logicExecution) {
        setTitle("Playerswinrate - Settings");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new GridLayout(2, 2));
        this.setResizable(false);

        JPanel mainPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);

        // gérer les files
        JTextField txtFileInput = new JTextField(logicExecution.getInputFile());
        txtFileInput.setPreferredSize(new Dimension(350, 30));
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(txtFileInput, gbc);

        JButton btnFileInput = new JButton("Input");
        btnFileInput.addActionListener((e) -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int result = fileChooser.showDialog(SettingsWindow.this, "Select input file");

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                logicExecution.setInputFile(selectedFile.getAbsolutePath());
                txtFileInput.setText(logicExecution.getInputFile());
            }
        });

        gbc.gridx = 1;
        gbc.gridy = 0;
        mainPanel.add(btnFileInput, gbc);

        JTextField txtFileOutput = new JTextField(logicExecution.getOutputFile());
        txtFileOutput.setPreferredSize(new Dimension(350, 30));
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(txtFileOutput, gbc);

        JButton btnFileOutput = new JButton("Output");
        btnFileOutput.addActionListener((e) -> {
            JFileChooser folderChooser = new JFileChooser();
            folderChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = folderChooser.showDialog(SettingsWindow.this, "Select output folder");

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = folderChooser.getSelectedFile();
                logicExecution.setOutputFile(selectedFile.getAbsolutePath());
                txtFileOutput.setText(logicExecution.getOutputFile());
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(btnFileOutput, gbc);

        this.setLayout(new BorderLayout());
        this.add(mainPanel, BorderLayout.CENTER);

        this.pack();

        this.setVisible(true);
    }
}
