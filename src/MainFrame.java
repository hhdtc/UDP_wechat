
//package FINAL_PROJECT.src;
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.regex.Pattern;
import java.awt.event.*;
import javax.swing.filechooser.*;

//import jdk.internal.platform.Container;

import java.io.*;
import java.net.*;
import java.nio.channels.FileLock;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.lang.*;
//import FINAL_PROJECT.src.*;

/**
 * the main frame of the UDP chat containing menu, tool bar, infomation panel,
 * controlling panel
 * 
 * It also includes the UDP transfer part.
 * @author Changdi Chen
 * @version 6.2
 */
public class MainFrame extends JFrame {
    // public static boolean flip = false;
    public static boolean needtranslating = false;
    public static boolean refreshbuffer = false;
    public static int translatingcounter = 0;
    public static boolean translating = false;
    public static boolean encryption = true;
    public static boolean translation = false;
    public static String source_language = "en";
    public static String target_language = "zh";
    public boolean systemworking = true;
    public ArrayList<String> displayname = new ArrayList<String>();
    public ArrayList<String> totalmessage = new ArrayList<String>();
    public ArrayList<String> translatingbuffer = new ArrayList<String>();
    public boolean issingle = true;
    public JButton switches = new JButton(" single ");
    public JScrollPane scrolldisplay;
    public JTextArea display;
    public JTextArea typeArea;
    public int chosenbutton;
    public String chosenname = "";
    public static DatagramSocket socket;
    public static String chosenip = "";
    public static JPanel TextPanel;
    public static JButton[] contactbutton;
    public static String[] buttonrelatedip;
    public static HashMap<String, Contactors> contactorslist = new HashMap<String, Contactors>();
    public static HashMap<String, ArrayList<String>> nametomessage = new HashMap<String, ArrayList<String>>();
    public static HashMap<String, String> iptoname = new HashMap<String, String>();
    public static HashMap<String, Scanner> iptoin = new HashMap<String, Scanner>();
    public static HashMap<String, PrintWriter> iptoout = new HashMap<String, PrintWriter>();
    public static HashMap<String, ArrayList<String>> iptomessage = new HashMap<String, ArrayList<String>>();
    public static HashMap<String, Boolean> iptonotice = new HashMap<String, Boolean>();
    public static String username = "";
    public static String status = "";
    public static boolean checkcomplete = false;
    public static boolean connected = false;
    public static int totalblock = 0;
    public static int AC;
    public static int BB;
    public static int DD;
    public static int sub;
    public static int PB;
    public static int ships[] = new int[] { 1, 1, 1, 1, 1 };
    public static int remainingships[] = new int[] { 1, 1, 1, 1, 1 };
    public static int time = 30;
    public static int remainingtime;
    public static int round = 0;
    public int fireloc[] = new int[] { -1, -1 };
    public static boolean attack = false;
    public static boolean setting = true;
    public static InputStream inStream;
    public static OutputStream outStream;
    public static Scanner in;
    public static PrintWriter out;
    public static int[][] checkgrid = new int[1][1];
    public static int[][] checkgridwithseperateships = new int[1][1];
    static int checkturn = 0;
    public Container mainContainer = getContentPane();
    public static final int DEFAULT_WIDTH = 300;
    public static final int DEFAULT_HEIGHT = 400;
    public JPanel grid = null;
    public JPanel info = null;
    public static Socket s;
    public static ServerSocket ss;
    public static int totalWinNum = 0;
    public EncrypDES encry;

    /**
     * initialize frame
     */
    public MainFrame() {
        try {
            encry = new EncrypDES();
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        setTitle("MainFrame");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        // set up menu bar

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        panel = new JPanel();
        mainContainer.add(panel);
        JMenu menu = new JMenu("menu");
        menuBar.add(menu);

        JMenuItem linkItem = new JMenuItem("link");
        menu.add(linkItem);
        linkItem.addActionListener(new LinkListener());
        linkItem.setMnemonic(KeyEvent.VK_L);

        JMenuItem configItem = new JMenuItem("Configuration");
        menu.add(configItem);
        configItem.addActionListener(new ConfigListener());

        JMenuItem exitItem = new JMenuItem("Exit");
        menu.add(exitItem);
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (s != null) {
                    try {
                        s.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                if (ss != null) {
                    try {
                        ss.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                System.exit(0);
            }
        });

        exitItem.setMnemonic(KeyEvent.VK_E);

        // chooser.setFileView(new FileIconView(filter, new ImageIcon("palette.gif")));
        Action whiteAction = new ColorAction("white", new ImageIcon("../src/white-ball.gif"), Color.WHITE);
        Action blueAction = new ColorAction("Blue", new ImageIcon("../src/blue-ball.gif"), Color.BLUE);
        Action yellowAction = new ColorAction("Yellow", new ImageIcon("../src/yellow-ball.gif"), Color.YELLOW);
        Action redAction = new ColorAction("Red", new ImageIcon("../src/red-ball.gif"), Color.RED);
        Action configAction = new ConfigAction("config");
        Action exitAction = new AbstractAction("Exit", new ImageIcon("../src/exit.gif")) {
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        };
        exitAction.putValue(Action.SHORT_DESCRIPTION, "Exit");

        // tool bar

        JToolBar bar = new JToolBar();
        bar.add(whiteAction);
        bar.add(blueAction);
        bar.add(yellowAction);
        bar.add(redAction);
        bar.addSeparator();
        bar.add(configAction);
        bar.add(exitAction);
        mainContainer.add(bar, BorderLayout.NORTH);

        JPanel bottom = new JPanel();

        JButton startConnection = new JButton("host/join");
        startConnection.addActionListener(new PrepareListener());

        JButton findnearby = new JButton("find nearby user");
        findnearby.addActionListener(new FindNearbyListener());

        JButton sendbutton = new JButton("send");
        sendbutton.addActionListener(new sendmessageListener());
        // bottom.add(startConnection, BorderLayout.WEST);
        bottom.add(findnearby);
        bottom.add(sendbutton);
        mainContainer.add(bottom, BorderLayout.SOUTH);
        JOptionPane soc = new JOptionPane();
        String str = soc.showInputDialog(null, "user name:");
        username = str;
        if (info != null) {
            mainContainer.remove(info);
        }
        createinfoudp();
        Thread q = new StartServerReceivingudp();
        q.start();
        Thread tr = new Translating();
        tr.start();
        connected = true;
    }

    /**
     * file - configuration call the configuration panel
     */
    public class ConfigAction extends AbstractAction {
        public ConfigAction(String name) {
            putValue(Action.NAME, name);
        }

        public void actionPerformed(ActionEvent event) {
            new ConfigListener().actionPerformed(event);
        }
    }

    /**
     * deal with linking to another host
     */
    public class LinkListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            EventQueue.invokeLater(() -> {
                JOptionPane ip = new JOptionPane();
                String str = ip.showInputDialog(null, "the ip address of server:");
                if (!str.equals("")) {
                    UDPchat_GUI.ipAddress = str;
                }
                if (info != null) {
                    mainContainer.remove(info);
                }
                // createinfo();
                ;
            });
        }
    }

    /**
     * it will call the configuration panel.
     */
    public class ConfigListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            EventQueue.invokeLater(() -> {
                var ConfigFrame = new ConfigFrame();
                ConfigFrame.setTitle("Configuration");
                ConfigFrame.setVisible(true);
            });

        }
    }

    /**
     * action to change background color
     */
    public class ColorAction extends AbstractAction {
        /**
         * action to change background color
         * 
         * @param name name of the action in tool bar
         * @param icon icon of the action in tool bar
         * @param c    color of the action
         */
        public ColorAction(String name, Icon icon, Color c) {
            putValue(Action.NAME, name);
            putValue(Action.SMALL_ICON, icon);
            putValue(Action.SHORT_DESCRIPTION, name + " background");
            putValue("Color", c);
        }

        /**
         * changing the background color
         */
        public void actionPerformed(ActionEvent event) {
            Color c = (Color) getValue("Color");
            if (TextPanel != null) {
                //TextPanel.setBackground(c);
                display.setBackground(c);
                typeArea.setBackground(c);
            } else {
                panel.setBackground(c);
            }
        }
    }

    /**
     * preparing, transferring settings from server to client. the first "handshake"
     * between two contactors.
     */

    public class MaintainingConnection extends Thread {
        private Scanner in;
        private PrintWriter out;
        private String ip;
        private Socket s;

        public MaintainingConnection(Scanner oin, PrintWriter oout, String oip, Socket os) {
            in = oin;
            out = oout;
            ip = oip;
            s = os;
        }

        public void run() {
            if (ip != "") {
                iptoin.put(ip, in);
                iptoout.put(ip, out);
                iptomessage.put(ip, new ArrayList<String>());
            }

            out.println(username);
            String remotename = in.nextLine();
            iptoname.put(ip, remotename);
            createinfoudp();
            try {
                while (true) {
                    String message = in.nextLine();
                    System.out.println(message);
                    ArrayList<String> tmp = iptomessage.get(ip);
                    tmp.add(message);
                    iptomessage.put(ip, tmp);
                    if (chosenip.equals(ip)) {
                        for (int i = 0; i < buttonrelatedip.length; i++) {
                            if (buttonrelatedip[i].equals(ip) || displayname.contains(ip)) {
                                System.out.println("update");
                                CreateTextboxesudp(i, displayname);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();

            } finally {
                try {
                    s.close();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        }
    }
    /**
     * all the network information of contactors
     */
    public class Contactors {
        private InetAddress address;
        private int port;
        public String name;
        /**
         * initailization
         * @param packet received handshake packet
         * @param message received messages.
         */
        public Contactors(DatagramPacket packet, String message) {
            address = packet.getAddress();
            port = packet.getPort();
            port = 8189;
            name = message.split("\\|")[1];
        }
        /**
         * send the message to opponent.
         * @param mes the message need to be sent.
         */
        public void sendmessage(String mes) {
            byte[] data2 = mes.getBytes();
            DatagramPacket packet2 = new DatagramPacket(data2, data2.length, address, port);
            try {
                socket.send(packet2);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    /**
     * A Thread which always detect is there new information need to be translated.
     */
    public class Translating extends Thread {
        public void run() {
            while (systemworking) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                //System.out.println("waiting");
                while ((translation && needtranslating) || refreshbuffer) {
                    translating = true;
                    System.out.println(translatingbuffer.size());
                    System.out.println(totalmessage.size());
                    while (translatingbuffer.size()< totalmessage.size()) {
                        System.out.println("doing translating, index: "+translatingcounter);
                        String tmp = "";
                        String msg[] = totalmessage.get(translatingcounter).split("\\|");
                        int msgsize = msg.length;
                        for (int i = 0; i < msgsize; i++) {
                            if (i < 3) {
                                tmp = tmp + msg[i];
                            } else {
                                try {
                                    tmp = tmp + Translator.translate(source_language, target_language, msg[i]);
                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }

                            if(i<msgsize-1){
                                tmp = tmp+"|";
                            }
                        }
                        translatingbuffer.add(tmp);
                        translatingcounter = translatingbuffer.size();
                        if(refreshbuffer == true){
                            translatingbuffer = new ArrayList<String>();
                            translatingcounter = 0;
                            refreshbuffer = false;
                        }   
                    }
                    if(refreshbuffer == true){
                        translatingbuffer = new ArrayList<String>();
                        translatingcounter = 0;
                        refreshbuffer = false;
                    }  

                    translating = false;
                    needtranslating = false;
                }
            }
        }
    }
    /**
     * UDP version
     * handle all the message received and the take actions.
     */
    public class StartServerReceivingudp extends Thread {
        public void run() {

            try {
                socket = new DatagramSocket(8189);
                byte[] data = new byte[1024];
                DatagramPacket packet = new DatagramPacket(data, data.length);
                while (true) {
                    System.out.println("waiting");
                    try {
                        socket.receive(packet);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    String message = new String(data, 0, packet.getLength());
                    if (encryption) {
                        try {
                            // System.out.println(message);
                            message = encry.decrypt(message);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    message = message.replace("\n", "");
                    System.out.println(message);
                    System.out.println(message.split("\\|")[0]);
                    if (message.split("\\|")[0].contentEquals("login")
                            || message.split("\\|")[0].contentEquals("ACK")) {
                        System.out.println("login request");
                        String name = message.split("\\|")[1];
                        if (!message.split("\\|")[0].contentEquals("ACK")) {
                            if (name.equals(username)) {
                                continue;
                            } else if (name.equals(username) || contactorslist.keySet().contains(name)) {
                                String m = "ACK|" + username;
                                if (encryption) {
                                    try {
                                        m = encry.encrypt(m);
                                    } catch (Exception e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                }
                                contactorslist.get(name).sendmessage(m);
                                continue;
                            }
                        }
                        Contactors newperson = new Contactors(packet, message);
                        if (!message.split("\\|")[0].contentEquals("ACK")) {
                            String m = "login|" + username;
                            if (encryption) {
                                try {
                                    m = encry.encrypt(m);
                                } catch (Exception e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                            newperson.sendmessage(m);
                        }
                        contactorslist.put(newperson.name, newperson);
                        nametomessage.put(newperson.name, new ArrayList<String>());
                        createinfoudp();
                    }
                    if (message.split("\\|")[0].contentEquals("message")) {
                        String name = message.split("\\|")[1];
                        System.out.println("message received");
                        // LocalDateTime a;
                        // Contactors newperson = new Contactors(packet,message);
                        // contactorslist.put(newperson.name,newperson);
                        ArrayList<String> tmp = nametomessage.get(name);
                        String msg = "";
                        for (int i = 2; i < message.split("\\|").length; i++) {
                            if (i > 2) {
                                msg = msg + "|";
                            }
                            msg = msg + message.split("\\|")[i];
                        }
                        tmp.add(message);
                        nametomessage.put(name, tmp);
                        totalmessage.add(message);
                        // if (name.equals(chosenname)) {
                        if (displayname.contains(name)) {
                            // CreateTextboxesudp(chosenbutton, chosenname);
                            if (!translation) {
                                display.append(name + "|" + msg + "\n");
                            } else {
                                try {
                                    display.append(name + "|"
                                            + Translator.translate(source_language, target_language, msg) + "\n");
                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }
                        needtranslating=true;
                        // createinfoudp();
                    }
                }
            } catch (SocketException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                // socket.close();
            }

        }
    }
    /**
     * TCP version
     * handle all the message received and the take actions.
     * (may be useful for further versions)
     */
    public class StartServerReceiving extends Thread {
        public void run() {
            try {
                // attack = true;
                // establish server socket
                ss = new ServerSocket(8189);
                DatagramSocket socket = new DatagramSocket(8800);
                byte[] data = new byte[1024];
                DatagramPacket packet = new DatagramPacket(data, data.length);
                // wait for client connection
                status = "waiting...";
                System.out.println(status);
                // if (info != null) {
                // mainContainer.remove(info);
                // }
                // createinfo();
                while (true) {
                    Socket s = ss.accept();
                    String ip = s.getRemoteSocketAddress().toString();
                    System.out.println(ip);
                    if (iptoin.containsKey(ip)) {
                        System.out.println("continue");
                        continue;
                    }
                    status = "preparing";
                    connected = true;
                    try {
                        inStream = s.getInputStream();
                        outStream = s.getOutputStream();

                        Scanner in = new Scanner(inStream);
                        PrintWriter out = new PrintWriter(outStream, true /* autoFlush */);
                        // echo client input
                        // boolean done = false;
                        Thread p = new MaintainingConnection(in, out, ip, s);
                        p.start();
                        /*
                         * while (!done && in.hasNextLine()) { String line = in.nextLine();
                         * out.println("Echo: " + line); if (line.trim().equals("BYE")) done = true; }
                         */
                    } finally {
                        // ss.close();
                        // s.close();
                        // s.close();
                        // System.out.println("initialize complete");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    s.close();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                try {
                    ss.close();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                connected = false;
            }
        }
    }

    /**
     * this action will call a new thread and start StartServerReceivingudp(). transfer settings
     * from server to client. the first handshake between two players
     */
    public class PrepareListener implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            if (connected == true) {
                return;
            }
            // Thread p = new Prepare();
            // p.start();
            Thread q = new StartServerReceivingudp();
            q.start();
            connected = true;

        }
    }
    /**
     * it enables active searching nearby users by broadcasting.
     */
    public class StartFinding extends Thread {
        public void run() {
            System.out.println("Start finding");
            String mes = "login|" + username;
            if (encryption) {
                try {
                    mes = encry.encrypt(mes);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            byte[] b = mes.getBytes();
            try {
                DatagramSocket findsocket = new DatagramSocket(8188);
                DatagramPacket dgPacket = new DatagramPacket(b, b.length, InetAddress.getByName("255.255.255.255"),
                        8189);
                findsocket.send(dgPacket);
                findsocket.close();
                mainContainer.validate();
                if(TextPanel!=null){
                    TextPanel.validate();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }
    /**
     * send the message in the text area to opponents.
     */
    public class SendAction extends AbstractAction {
        public void actionPerformed(ActionEvent event) {

            new sendmessageListener().actionPerformed(event);
        }
    }

    /**
     * send the message in the text area to opponents.
     */
    public class sendmessageListener implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            // if(connected==true){
            // return;
            // }
            // Thread p = new Prepare();
            // p.start();
            String message = "message|" + username + "|" + LocalDateTime.now() + "|" + typeArea.getText();
            ArrayList<String> tmp = nametomessage.get(chosenname);
            tmp.add(message);
            String tmper = "";
            for (String ss : displayname) {
                for (int i = 0; i < message.split("\\|").length; i++) {
                    if (i == 0) {
                        tmper = ss + "|";
                    } else if (i < message.split("\\|").length - 1) {
                        tmper = tmper + message.split("\\|")[i] + "|";
                    } else {
                        tmper = tmper + message.split("\\|")[i];
                    }
                }
            }
            totalmessage.add(tmper);
            needtranslating=true;
            nametomessage.put(chosenname, tmp);
            if (!translation) {
                display.append(username + "|" + LocalDateTime.now() + "|" + typeArea.getText() + "\n");
            } else {
                try {
                    display.append(username + "|" + LocalDateTime.now() + "|"
                            + Translator.translate(source_language, target_language, typeArea.getText()) + "\n");
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } // scrolldisplay.getVerticalScrollBar().addAdjustmentListener(new
              // AdjustmentListener() {
              // public void adjustmentValueChanged(AdjustmentEvent e) {
              // e.getAdjustable().setValue(e.getAdjustable().getMaximum());
              // }
              // });
            TextPanel.validate();
            typeArea.setText("");
            System.out.println(message);
            for (String ss : displayname) {
                String newmessage = "";
                if(encryption){
                    try {
                        newmessage = encry.encrypt(message);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    contactorslist.get(ss).sendmessage(newmessage);
                }else{
                    contactorslist.get(ss).sendmessage(message);
                }
            }
            mainContainer.validate();
        }
    }
    /**
     * This action calls StartFinding
     * it will actively search all nearby users by broadcasting.
     */
    public class FindNearbyListener implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            // if(connected==true){
            // return;
            // }
            // Thread p = new Prepare();
            // p.start();
            System.out.println("Start finding");
            Thread q = new StartFinding();
            q.start();
        }
    }

    /**
     * this action allows the cell button to switch between red and green
     * ( may be useful in future versions.)
     */
    public class MyFireButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (attack) {
                for (int row = 0; row < UDPchat_GUI.y; row++) {
                    for (int col = 0; col < UDPchat_GUI.x; col++) {
                        if (UDPchat_GUI.fireGrid[row][col] == e.getSource()) {
                            if (UDPchat_GUI.fireGrid[row][col].getBackground().equals(Color.BLUE)) {
                                UDPchat_GUI.fireGrid[row][col].setBackground(Color.BLACK);
                                if (fireloc[0] >= 0 && fireloc[1] >= 0) {
                                    UDPchat_GUI.fireGrid[fireloc[0]][fireloc[1]].setBackground(Color.BLUE);
                                }
                                fireloc[0] = row;
                                fireloc[1] = col;
                                /*
                                 * for (int i = 0; i < GOL_gui.y; i++) { for (int j = 0; j < GOL_gui.x; j++) {
                                 * if(!(i == row && j == col)){
                                 * GOL_gui.fireGrid[i][j].setBackground(Color.BLUE); } } }
                                 */
                            }
                        }
                        // here you have your row and column
                    }
                }
            }
        }
    }
    /**
     * it will recognize which contactors is chosen.
     * it will display all the message received by all selected contactors.
     */
    public class ContactButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // displayname = new ArrayList<String> ();
            if (issingle == true) {
                for (int i = 0; i < contactbutton.length; i++) {
                    displayname = new ArrayList<String>();
                    contactbutton[i].setBackground(Color.WHITE);
                }

            }
            for (int i = 0; i < contactbutton.length; i++) {
                if (contactbutton[i] == e.getSource()) {
                    // chosenip = buttonrelatedip[i];
                    System.out.println("button trigger: " + contactbutton[i].getText());
                    System.out.println("issingle: " + issingle);
                    if (contactbutton[i].getBackground().equals(Color.WHITE)
                            && !displayname.contains(contactbutton[i].getText())) {
                        System.out.println("white");
                        displayname.add(contactbutton[i].getText());
                        chosenbutton = i;
                        chosenname = contactbutton[i].getText();
                        contactbutton[i].setBackground(Color.GRAY);
                        CreateTextboxesudp(i, displayname);
                    } else if (contactbutton[i].getBackground().equals(Color.GRAY) && issingle == false) {
                        System.out.println("gray");
                        displayname.remove(contactbutton[i].getText());
                        chosenbutton = i;
                        chosenname = contactbutton[i].getText();
                        contactbutton[i].setBackground(Color.WHITE);
                        CreateTextboxesudp(i, displayname);
                    }

                }
            }
            mainContainer.validate();
        }
    }
    /**
     * a swich button which can switch between single mode and multiple mode
     */
    public class SwitchesListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            System.out.println("switch");
            if (issingle == true) {
                issingle = false;
                switches.setText("multiple");
            } else {
                issingle = true;
                switches.setText(" single ");
                boolean firstb = false;
                displayname = new ArrayList<String>();
                for (int i = 0; i < contactbutton.length; i++) {
                    if (contactbutton[i].getBackground().equals(Color.GRAY) && firstb == false) {
                        firstb = true;
                        displayname.add(contactbutton[i].getText());
                        CreateTextboxesudp(i, displayname);
                    } else {
                        contactbutton[i].setBackground(Color.WHITE);
                    }
                }
            }
            info.validate();
            mainContainer.validate();
        }
    }
    /**
     * UDP version.
     * creating the text area to display messages and areas to write.
     */
    public void CreateTextboxesudp(int i, ArrayList<String> name) {
        // contactbutton[i].setBackground(Color.GRAY);
        if (TextPanel != null) {
            mainContainer.remove(TextPanel);
        }
        TextPanel = new JPanel();
        // TextPanel.setSize(3*(int) mainContainer.getSize().getWidth()/5,
        // Integer.valueOf((int) mainContainer.getSize().getHeight()));
        TextPanel.setLayout(new GridLayout(2, 1, 10, 10));
        display = new JTextArea();
        // display.setColumns(10);
        // display.setRows(10);
        ArrayList<String> tmpstringarr = new ArrayList<String>();
        for (String t : name) {
            System.out.println(t);
            for (int j = 0; j < nametomessage.get(t).size(); j++) {
                // display.append(nametomessage.get(name).get(j)+"\n");
                tmpstringarr.add(nametomessage.get(t).get(j) + "\n");
            }
        }
        Collections.sort(tmpstringarr, Collections.reverseOrder());
        if(!translating && translation){
            System.out.println("using buffer"); 
            for (String j : translatingbuffer) {
                if (displayname.contains(j.split("\\|")[1]) || displayname.contains(j.split("\\|")[0])) {
                    System.out.println(j);
                    String tmp = "";
                    for (int k = 0; k < j.split("\\|").length; k++) {
                        if (k > 0) {
                            tmp = tmp+j.split("\\|")[k];
                            if(k<j.split("\\|").length-1){
                                tmp = tmp+("|");
                            }
                        }
    
                    }
                    tmp = tmp+"\n";
                    display.append(tmp);
                }
                
            }
        }else{
            System.out.println("translating now!");        
            for (String j : totalmessage) {
                if (displayname.contains(j.split("\\|")[1]) || displayname.contains(j.split("\\|")[0])) {
                    System.out.println(j);
                    String tmp = "";
                    for (int k = 0; k < j.split("\\|").length; k++) {
                        if (k > 0) {

                            


                            if (k > 2 && translation) {
                                try {
                                    tmp = tmp + Translator.translate(source_language, target_language, j.split("\\|")[k]);
                                } catch (IOException e1) {
                                    // TODO Auto-generated catch block
                                    e1.printStackTrace();
                                }
                            }else{
                                tmp = tmp+j.split("\\|")[k];
                            }
                            if(k<j.split("\\|").length-1){
                                tmp = tmp+("|");
                            }
                        }

                    }
                    tmp = tmp+"\n";
                    display.append(tmp);
                }
                
            }
        }

        
        JScrollPane scrolldisplay= new JScrollPane(display);
        scrolldisplay.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
            public void adjustmentValueChanged(AdjustmentEvent e) {  
                e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
            }
        });
        typeArea = new JTextArea();
        
        // typeArea.setColumns(10);
        // typeArea.setRows(10);
        JScrollPane scrollTypeArea= new JScrollPane(typeArea);
        TextPanel.add(scrolldisplay,BorderLayout.CENTER);
        TextPanel.add(scrollTypeArea,BorderLayout.SOUTH);
        mainContainer.add(TextPanel,BorderLayout.CENTER);
        mainContainer.validate();
    }
    /**
     * TCP version.
     * creating the text area to display messages and areas to write.
     * (might be useful in future versions)
     */
    public void CreateTextboxes( int i){
        contactbutton[i].setBackground(Color.GRAY);
        if (TextPanel!=null){
            mainContainer.remove(TextPanel);
        }
        TextPanel = new JPanel();
        //TextPanel.setSize(3*(int) mainContainer.getSize().getWidth()/5, Integer.valueOf((int) mainContainer.getSize().getHeight()));
        TextPanel.setLayout(new GridLayout(2, 1, 10, 10));
        JTextArea display = new JTextArea();
        // display.setColumns(10);
        // display.setRows(10);
        for (int j=0;j<iptomessage.get(buttonrelatedip[i]).size() ;j++){
            display.append(iptomessage.get(buttonrelatedip[i]).get(j)+"\n");
        }
        
        JScrollPane scrolldisplay= new JScrollPane(display);
        scrolldisplay.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
            public void adjustmentValueChanged(AdjustmentEvent e) {  
                e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
            }
        });
        JTextArea typeArea = new JTextArea();
        // typeArea.setColumns(10);
        // typeArea.setRows(10);
        JScrollPane scrollTypeArea= new JScrollPane(typeArea);
        TextPanel.add(scrolldisplay,BorderLayout.CENTER);
        TextPanel.add(scrollTypeArea,BorderLayout.SOUTH);
        mainContainer.add(TextPanel,BorderLayout.CENTER);
        mainContainer.validate();
    }
      /**
     * it can change the UI
     * @param plafName the name of UI
     */
    public void changeMainFrame(final String plafName){
        try {
            UIManager.setLookAndFeel(plafName);
            SwingUtilities.updateComponentTreeUI(MainFrame.this);
            UDPchat_GUI.Frame.validate();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * create a panel to display all the contactors
     */
    public void createinfoudp(){
        if (info!=null){
            mainContainer.remove(info);
            info = null;
        }
        info = new JPanel();
        info.setSize((int) mainContainer.getSize().getWidth()/5, Integer.valueOf((int) mainContainer.getSize().getHeight()));
        //c.setLayout(new GridLayout(5, 1, 10, 10));
        info.setLayout(new GridLayout(Math.max(7,1+contactorslist.size()), 1, 0, 0));
        switches = new JButton();
        if(issingle){
            switches.setText(" single ");
        }else{
            switches.setText("multiple");
        }
        switches.addActionListener(new SwitchesListener());
        info.add(switches);
        contactbutton = new JButton [contactorslist.keySet().size()];
        buttonrelatedip = new String[contactorslist.keySet().size()];
        int counter = 0;
        for(String i : contactorslist.keySet()){
            JButton tmp = new JButton(i);
            tmp.setBackground(Color.WHITE);
            if(displayname.contains(i)){
                tmp.setBackground(Color.GRAY);
            }
            
            System.out.println("add to contact: "+i);
            contactbutton[counter] = tmp;
            tmp.addActionListener(new ContactButtonListener());

            info.add(tmp);
            buttonrelatedip[counter] = i;
            counter = counter+1;
        }
        //mainContainer.add(info);
        System.out.println("add info");
        mainContainer.add(info,BorderLayout.WEST);
        mainContainer.validate();
        //mainContainer.add(info,BorderLayout.WEST);
    }

    public void createinfo(){

        if (info!=null){
            mainContainer.remove(info);
            info = null;
        }
        info = new JPanel();
        //info.setSize((int) mainContainer.getSize().getWidth()/5, Integer.valueOf((int) mainContainer.getSize().getHeight()));
        //c.setLayout(new GridLayout(5, 1, 10, 10));
        info.setLayout(new GridLayout(7, 1, 0, 0));

        contactbutton = new JButton [iptoname.keySet().size()];
        buttonrelatedip = new String[iptoname.keySet().size()];
        int counter = 0;
        for(String i : iptoname.keySet()){
            JButton tmp = new JButton(iptoname.get(i));
            tmp.setBackground(Color.WHITE);
            
            System.out.println("add to contact: "+i);
            contactbutton[counter] = tmp;
            tmp.addActionListener(new ContactButtonListener());

            info.add(tmp);
            buttonrelatedip[counter] = i;
            counter = counter+1;
        }
        //mainContainer.add(info);
        System.out.println("add info");
        mainContainer.add(info,BorderLayout.WEST);
        mainContainer.validate();
        //mainContainer.add(info,BorderLayout.WEST);
    }
    
    
    private JLabel label;
    public static JFileChooser chooser;
    private JPanel panel;
}