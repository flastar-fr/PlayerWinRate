package fr.flastar.programme;


import com.formdev.flatlaf.FlatDarculaLaf;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.*;


public class Graphicver extends JFrame {

    LogicManager logicExecution = new LogicManager();

    JTextArea textAreaRight;

    JTextArea textAreaLeft;

    /* Construction de l'interface graphique */
    public Graphicver() {
        super("Playerswinrate");
        this.setSize(1000,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Construction et injection de la barre de menu
        this.setJMenuBar(this.createMenuBar());

        // Construction et injection de la barre d'outils
        JPanel contentPane = (JPanel) getContentPane();
        contentPane.add(this.createToolBar(), BorderLayout.NORTH);

        // The content of the window
        textAreaLeft = new JTextArea(10, 30);
        textAreaLeft.setEditable(false);

        JScrollPane leftScrollPane = new JScrollPane(textAreaLeft);
        leftScrollPane.setPreferredSize(new Dimension(450, 0));

        textAreaRight = new JTextArea();
        textAreaRight.setEditable(false);
        JScrollPane rightScrollPane = new JScrollPane(textAreaRight);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftScrollPane, rightScrollPane);
        contentPane.add(splitPane);

    }


    /* Methode de construction de la barre de menu */
    private JMenuBar createMenuBar() {

        // La barre de menu à proprement parler
        JMenuBar menuBar = new JMenuBar();

        // Définition du menu déroulant "File" et de son contenu
        JMenu mnuFile = new JMenu( "File" );
        mnuFile.setMnemonic('F');


        JMenu languageSelection = new JMenu("Language");

        JRadioButtonMenuItem jrbEnglish = new JRadioButtonMenuItem("English");
        JRadioButtonMenuItem jrbFrench = new JRadioButtonMenuItem("Français");
        JRadioButtonMenuItem jrbJapanese = new JRadioButtonMenuItem("日本語");

        jrbEnglish.setSelected(true);

        languageSelection.add(jrbEnglish);
        languageSelection.add(jrbFrench);
        languageSelection.add(jrbJapanese);

        mnuFile.add(actLaunch);
        mnuFile.addSeparator();
        mnuFile.add(languageSelection);
        mnuFile.add(actSettings);
        mnuFile.addSeparator();
        mnuFile.add(actExit);

        menuBar.add(mnuFile);

        // Définition du menu déroulant "Help" et de son contenu
        JMenu mnuHelp = new JMenu("Help");
        mnuHelp.setMnemonic('H');
        JMenuItem mnContact = new JMenuItem("Contact creator");
        mnContact.addActionListener( (e) ->
                JOptionPane.showMessageDialog(this,
                        "Contact flastar on Discord by username flastar_ for further help",
                        "Contact creator",
                        JOptionPane.INFORMATION_MESSAGE));
        JMenuItem mnCredits = new JMenuItem("Credits");
        mnCredits.addActionListener( (e) ->
                JOptionPane.showMessageDialog(this,
                        """
                                Icon made by Flat Icons Design from www.flaticon.com
                                Icon made by Assia Benkerroum from www.flaticon.com
                                Icon made by feen from www.flaticon.com""",
                        "Credits",
                        JOptionPane.INFORMATION_MESSAGE));
        mnuHelp.add(mnContact);
        mnuHelp.add(mnCredits);

        menuBar.add(mnuHelp);

        return menuBar;
    }

    /* Methode de construction de la barre d'outils */
    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();

        toolBar.add(actLaunch);
        toolBar.addSeparator();
        toolBar.add(actExit);
        toolBar.addSeparator();

        return toolBar;
    }

    private final AbstractAction actSettings = new AbstractAction() {
        {
            putValue(Action.NAME, "File manager");
            putValue(Action.SMALL_ICON, new ImageIcon("icons/settings.png"));
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_F );
            putValue(Action.SHORT_DESCRIPTION, "File manager (CTRL+F)");
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK));
        }

        @Override public void actionPerformed( ActionEvent e ) {
            SettingsWindow settingsWindow = new SettingsWindow(logicExecution);
            settingsWindow.setSize(500, 150);
            settingsWindow.setLocationRelativeTo(getContentPane());
            settingsWindow.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            settingsWindow.setVisible(true);
        }
    };

    private final AbstractAction actLaunch = new AbstractAction() {
        {
            putValue(Action.NAME, "Launch app");
            putValue(Action.SMALL_ICON, new ImageIcon("icons/start.png"));
            putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S );
            putValue(Action.SHORT_DESCRIPTION, "Launch app (CTRL+S)");
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ArrayList<Player> players = null;
            try {
                players = logicExecution.initProgram();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            assert players != null;
            for (Player player : players) {
                String textFromLeft = textAreaLeft.getText();
                textFromLeft += player + "\n";
                textAreaLeft.setText(textFromLeft);
            }
            textAreaLeft.setText(textAreaLeft.getText() + "\n");

            int confirmValue = JOptionPane.showConfirmDialog(JOptionPane.getRootFrame(),
                    "Do you want to continue ?",
                    "Confirmation", JOptionPane.YES_NO_OPTION);

            if (confirmValue == JOptionPane.NO_OPTION) return;

            logicExecution.run();

            LinkedHashMap<String, Integer[]> playerMap = logicExecution.getPlayerMap();

            for (Map.Entry<String, Integer[]> entry : playerMap.entrySet()) {
                String textFromRight = textAreaRight.getText();
                String key = entry.getKey();
                Integer[] value = entry.getValue();
                double winRate = (double) value[0] / logicExecution.LOOP * 100;

                textFromRight += String.format("Player : %s, Winrate : %.3f%%, " +
                                "Game won : %,d, Player kills : %,d\n",
                        key, winRate, value[0], value[1]);

                textAreaRight.setText(textFromRight);
            }
            textAreaRight.setText(textAreaRight.getText() + "\n");

            confirmValue = JOptionPane.showConfirmDialog(JOptionPane.getRootFrame(),
                    "Do you want to save the result ?",
                    "Confirmation", JOptionPane.YES_NO_OPTION);

            if (confirmValue == JOptionPane.YES_OPTION) {
                try {
                    logicExecution.writeGameInfos();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        }
    };

    private final AbstractAction actExit = new AbstractAction() {
        {
            putValue( Action.NAME, "Exit" );
            putValue( Action.SMALL_ICON, new ImageIcon( "icons/exit.png" ) );
            putValue( Action.MNEMONIC_KEY, KeyEvent.VK_X );
            putValue( Action.SHORT_DESCRIPTION, "Exit (ALT+F4)" );
            putValue( Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_DOWN_MASK ) );
        }

        @Override public void actionPerformed( ActionEvent e ) {
            dispose();
        }
    };

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel( new FlatDarculaLaf() );
        Graphicver frame = new Graphicver();
        frame.setVisible(true);
    }
}