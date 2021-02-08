//package FINAL_PROJECT.src;
import java.awt.*;
import java.io.File;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
//import FINAL_PROJECT.src.*;


/**
 * UDP_chat gui class
 * it contains many public values to modify
 * it also calls MainFrame
 * @author Changdi Chen
 * @version 2.0
 */
public class UDPchat_GUI{

    public static String ipAddress = "";
    public static Boolean isready;
    public static int starttic;
    public static int endtic;
    public static String defaultoutputname = "GOL_output";
    public static boolean isserver = false;
    public static JButton[][] buttonGrid = new JButton[1][1];
    public static JButton[][] fireGrid = new JButton[1][1];
    public static MainFrame Frame;
    public static int x = 10;
    public static int y = 10;
    public static String defaultloc = "..";
    public static int tics = 5;
    public static String fileName;
    public static int counter = -1;
    public static String[][][] allgrid;
    public static int [][][] allgridColors;
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            Frame = new MainFrame();
            Frame.setTitle("My Frame");
            Frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            Frame.setVisible(true);
            Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
            Frame.setSize((int) ScreenSize.getWidth() / 2, (int) ScreenSize.getHeight() / 2);

            // GridLayout MapLayout = new GridLayout();
        });
        System.out.println("App exited.");

    }


}


