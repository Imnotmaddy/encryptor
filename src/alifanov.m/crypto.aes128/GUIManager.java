package alifanov.m.crypto.aes128;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import static javax.swing.JOptionPane.showMessageDialog;


public class GUIManager {

    private static String inputFileDirectory;
    private static String outputFileDirectory;
    private static int[][] key = new int[4][4];

    private static void addComponentsToPane(Container pane) {
        pane.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;
        constraints.weighty = 0.5;

        JButton decryptionButton = new JButton("Decryption");
        JButton encryptionButton = new JButton("Encryption");
        JButton submitKeyButton = new JButton("Submit Key");

        decryptionButton.setEnabled(false);
        encryptionButton.setEnabled(false);

        JButton fileChooser = new JButton("Choose File");

        JTextField inputKeyTextField = new JTextField();
        JTextField resultTextField = new JTextField();

        constraints.gridx = 0;
        constraints.gridy = 0;
        pane.add(decryptionButton, constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        pane.add(fileChooser, constraints);

        constraints.gridx = 2;
        constraints.gridy = 0;
        pane.add(encryptionButton, constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        pane.add(new JLabel("Input Key for Decryption/Encryption"), constraints);

        constraints.gridx = 1;
        constraints.gridy = 2;
        pane.add(inputKeyTextField, constraints);

        constraints.gridx = 2;
        constraints.gridy = 2;
        pane.add(submitKeyButton, constraints);

        constraints.gridx = 1;
        constraints.gridy = 3;
        pane.add(new JLabel("This is your result Key"), constraints);

        constraints.gridx = 1;
        constraints.gridy = 4;
        pane.add(resultTextField, constraints);


        fileChooser.addActionListener(e -> {
            JFileChooser fileopen = new JFileChooser();
            int ret = fileopen.showDialog(null, "Открыть файл");
            if (ret == JFileChooser.APPROVE_OPTION) {
                inputFileDirectory = fileopen.getCurrentDirectory()+"\\" + fileopen.getSelectedFile().getName();
                outputFileDirectory = fileopen.getCurrentDirectory().getAbsolutePath()+"\\" + "NEW_" + fileopen.getSelectedFile().getName();
                encryptionButton.setEnabled(true);
                decryptionButton.setEnabled(true);
            }
        });
        decryptionButton.addActionListener(e -> {
            try {
                if (inputKeyTextField.getText().length() < 16) {
                    showMessageDialog(null, "You need 16 symbol key");
                } else {
                    ThreadManager threadManager = new ThreadManager(inputFileDirectory, arrayToMatrix(inputKeyTextField.getText()));
                    threadManager.startDecryption(outputFileDirectory);
                    resultTextField.setText("Success");
                }
            } catch (IOException exception) {
                System.out.println("Exception in Decryption");
            }
        });

        encryptionButton.addActionListener(e -> {
            try {
                if (inputKeyTextField.getText().length() < 16) {
                    showMessageDialog(null, "You need 16 symbol key");
                } else {
                    ThreadManager threadManager = new ThreadManager(inputFileDirectory, arrayToMatrix(inputKeyTextField.getText()));
                    key = threadManager.startEncryption(outputFileDirectory);
                    resultTextField.setText(matrixToString());
                }
            } catch (IOException exc) {
                showMessageDialog(null, "Exception in Encryption");
            }
        });

    }

    private static int[][] arrayToMatrix(String str) {
        int k = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++, k++) {
                key[j][i] = str.charAt(k);
            }
        }
        return key;
    }

    private static String matrixToString() {
        int k = 0;
        String str = "";
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++, k++) {
                str += (char) key[j][i];
            }
        }
        return str;
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Encryptor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 300);
        frame.setResizable(false);

        addComponentsToPane(frame.getContentPane());
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}