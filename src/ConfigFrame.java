
//package FINAL_PROJECT.src;
import java.awt.*;
import javax.swing.*;

//import jdk.javadoc.internal.tool.Main;

import java.io.*;
import java.awt.event.*;
//import FINAL_PROJECT.src.*;
/**
 * ConfigFrame is used to call the configuration panel
 * @author Changdi Chen
 * @version 3.0
 */
class ConfigFrame extends JFrame {
    public ConfigFrame() {
        setTitle("Configuration");
        setBounds(100, 100, 500, 500);
        Container c = getContentPane();
        c.setLayout(new GridLayout(4, 2, 10, 10));
        JButton b1 = new JButton("UI");
        b1.addActionListener(new UIListener());
        JButton b2 = new JButton("Translation setting");
        b2.addActionListener(new TranslationListener());
        // var pathAction = new PathAction();
        String[] languages = new String[] {"en","zh","fr","ja","es","de","kr","ru"};
        //String[] sourcelanges = new String[] {"auto","en","zh","fr","ja","es","de","kr","ru"};
        JComboBox b3 = new JComboBox<String>(languages);
        b3.addActionListener(new SelectSourceLanguageListener());
        b3.setSelectedItem(MainFrame.source_language);
        JComboBox b4 = new JComboBox<String>(languages);
        b4.setSelectedItem(MainFrame.target_language);
        b4.addActionListener(new SelectTargetLanguageListener());
        MainFrame.source_language = (String) b3.getSelectedItem();
        MainFrame.target_language = (String) b4.getSelectedItem();
        JButton b5 = new JButton("encryption setting setting");
        b5.addActionListener(new EncryptionListener());
        c.add(b1);
        c.add(b2);
        c.add(b5);
        c.add(new JLabel());
        c.add(new JLabel("select source language"));
        c.add(b3);
        c.add(new JLabel("select target language"));
        c.add(b4);

        setVisible(true);

    }
    private class SelectSourceLanguageListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            JComboBox<String> combo = (JComboBox<String>) event.getSource();
            MainFrame.source_language = (String) combo.getSelectedItem();
            if(MainFrame.target_language.equals(MainFrame.source_language)){
                MainFrame.refreshbuffer = false;
                MainFrame.needtranslating = false;
                return;
            }
            MainFrame.refreshbuffer = true;
            MainFrame.needtranslating = true;
            
            
        }
    }
    private class SelectTargetLanguageListener implements ActionListener{
        public void actionPerformed(ActionEvent event){
            JComboBox<String> combo = (JComboBox<String>) event.getSource();
            MainFrame.target_language = (String) combo.getSelectedItem();
            if(MainFrame.target_language.equals(MainFrame.source_language)){
                MainFrame.refreshbuffer = false;
                MainFrame.needtranslating = false;
                return;
            }
            MainFrame.refreshbuffer = true;
            MainFrame.needtranslating = true;
        }
    }

    /**
     * call the translation setting
     */
    private class TranslationListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            EventQueue.invokeLater(() -> {
                JOptionPane msgx = new JOptionPane();
                    String str = msgx.showInputDialog(null, "Enable translation (y/n)");
                    if( str.equals("N") || str.equals("n")){
                        MainFrame.translation = false;
                    }else if ( str.equals("Y") || str.equals("y")){
                        MainFrame.translation = true;
                        MainFrame.needtranslating = true;
                    }
            });

        }
    }

        /**
     * call the Encryption setting
     */
    private class EncryptionListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            EventQueue.invokeLater(() -> {
                JOptionPane msgx = new JOptionPane();
                    String str = msgx.showInputDialog(null, "Enable encryption (y/n)");
                    if( str.equals("N") || str.equals("n")){
                        MainFrame.encryption = false;
                    }else if ( str.equals("Y") || str.equals("y")){
                        MainFrame.encryption = true;
                    }
            });

        }
    }
    /**
     * call the UI setting panel
     */
    private class UIListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            EventQueue.invokeLater(() -> {
                uiframe = new UIFrame();
                // uiframe.setTitle("UI Configuration");
                // ConfigFrame.setSize(500, 500);
                uiframe.setVisible(true);
            });

        }
    }
    /**
     * setting the default grid
     */
    private class DefaultSizeListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            EventQueue.invokeLater(() -> {
                if(MainFrame.status.equals("")){
                    JOptionPane msgx = new JOptionPane();
                    String str = msgx.showInputDialog(null, "Please Enter the default x");
                    UDPchat_GUI.x = Integer.parseInt(str);
                    if(UDPchat_GUI.x<5){
                        UDPchat_GUI.x=5;
                    }
                    JOptionPane msgy = new JOptionPane();
                    str = msgy.showInputDialog(null, "Please Enter the default y");
                    UDPchat_GUI.y = Integer.parseInt(str);
                    if(UDPchat_GUI.y<5){
                        UDPchat_GUI.y=5;
                    }
                    UDPchat_GUI.Frame.validate();
                    if (UDPchat_GUI.Frame.info!=null){
                        UDPchat_GUI.Frame.mainContainer.remove(UDPchat_GUI.Frame.info);
                    }
                    //BB_gui.Frame.createinfo();
                }
            });

        }
    }
    /**
     * changing the number of ships
     */
    private class SetShipNumListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            EventQueue.invokeLater(() -> {
                if (MainFrame.status.equals("")){
                    JOptionPane ip = new JOptionPane();
                    String str = ip.showInputDialog(null, "Carrier(5 blocks): ");
                    try{
                        int n = Integer.valueOf(str);
                        if (n>=0){
                            MainFrame.ships[0] = n;
                            MainFrame.remainingships[0] = n;
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    str = ip.showInputDialog(null, "Battleship(4 blocks): ");
                    try{
                        int n = Integer.valueOf(str);
                        if (n>=0){
                            MainFrame.ships[1] = n;
                            MainFrame.remainingships[1] = n;
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    str = ip.showInputDialog(null, "Destroyer(3 blocks): ");
                    try{
                        int n = Integer.valueOf(str);
                        if (n>=0){
                            MainFrame.ships[2] = n;
                            MainFrame.remainingships[2] = n;
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    str = ip.showInputDialog(null, "Submarine(2 blocks): ");
                    try{
                        int n = Integer.valueOf(str);
                        if (n>=0){
                            MainFrame.ships[3] = n;
                            MainFrame.remainingships[3] = n;
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    str = ip.showInputDialog(null, "Patrol Boat(1 blocks): ");
                    try{
                        int n = Integer.valueOf(str);
                        if (n>=0){
                            MainFrame.ships[4] = n;
                            MainFrame.remainingships[4] = n;
                        }
                        if (MainFrame.ships[0]+MainFrame.ships[1]+MainFrame.ships[2]+MainFrame.ships[3] == 0){
                            MainFrame.ships[4] = 1;
                            MainFrame.remainingships[4] = 1;
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                    int sum = 0;
                    for (int i =0;i<5;i++){
                        sum = sum + MainFrame.ships[i]*(5-i);
                    }
                    if (sum>= UDPchat_GUI.x*UDPchat_GUI.y){
                        for (int i =0;i<5;i++){
                            MainFrame.ships[i] = 1;
                        }
                    }

                    if (UDPchat_GUI.Frame.info!=null){
                        UDPchat_GUI.Frame.mainContainer.remove(UDPchat_GUI.Frame.info);
                    }
                    //BB_gui.Frame.createinfo();
                }
            });

        }
    }
    /**
     * panel to change UI
     */
    class UIFrame extends JFrame {
        public UIFrame() {
            setTitle("UI Configuration");
            setSize(500, 500);
            buttonPanel = new JPanel();

            UIManager.LookAndFeelInfo[] infos = UIManager.getInstalledLookAndFeels();
            for (UIManager.LookAndFeelInfo info : infos) {
                makeButton(info.getName(), info.getClassName());
                add(buttonPanel);
            }

        }

        /**
         * Makes a button to change the pluggable look and feel.
         * 
         * @param name     the button name
         * @param plafName the name of the look and feel class
         */
        public void makeButton(String name, final String plafName) {
            // add button to panel

            JButton button = new JButton(name);
            buttonPanel.add(button);

            // set button action

            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    // button action: switch to the new look and feel
                    try {
                        UIManager.setLookAndFeel(plafName);
                        SwingUtilities.updateComponentTreeUI(UIFrame.this);
                        uiframe.validate();
                        ((MainFrame) UDPchat_GUI.Frame).changeMainFrame(plafName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }
    /**
     * set the default ip address to connect.
     */
    private class DefaultListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
                if (MainFrame.status.equals("")){
                JOptionPane ip = new JOptionPane();
                String str = ip.showInputDialog(null, "the ip address of server:");
                if (!str.equals("")) {
                    UDPchat_GUI.ipAddress = str;
                }
            }
        }
    }

     /**
     * set the time limit
     */
    private class TimeLimitListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
                if (MainFrame.status.equals("")){
                JOptionPane ip = new JOptionPane();
                String str = ip.showInputDialog(null, "time limit:");
                if (!str.equals("")) {
                    try{
                        UDPchat_GUI.Frame.time = Integer.valueOf(str);
                        if (UDPchat_GUI.Frame.time<3){
                            UDPchat_GUI.Frame.time =3;
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static JPanel buttonPanel;
    public static JFrame uiframe;
    public static JFrame defaultframe;
}