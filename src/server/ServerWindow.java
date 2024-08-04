package server;

import client.ClientGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import java.io.*;
import java.io.FileInputStream;
import java.util.Properties;
import java.io.BufferedReader;
public class ServerWindow extends JFrame {
    public static final int WIDTH = 400;
    public static final int HEIGHT = 300;


    private static String BTN_TITLE_START;
    private static String BTN_TITLE_STOP;
    private static String TITLE;



    JButton btnStart, btnStop;
    JTextArea log;


    private Server server;

    public ServerWindow() throws IOException{

        readini();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setTitle(TITLE);
        setLocationRelativeTo(null);

        createPanel();

        setVisible(true);

        this.server = new Server(this);
    }

    private void readini() throws IOException
    {
        Properties props = new Properties();
        props.load(new FileInputStream(new File("src/config/configServer.ini")));
        this.TITLE = props.getProperty("TITLE");
        this.BTN_TITLE_START = props.getProperty("BTN_TITLE_START");
        this.BTN_TITLE_STOP = props.getProperty("BTN_TITLE_STOP");
    }

    public Server getServer()
    {
        return this.server;
    }



    /*public String getLog() {
        return readLog();
    }*/



    public void message(String text){
        appendLog(text);
    }

    private void appendLog(String text){
        log.append(text + "\n");
    }

    private void createPanel() {
        log = new JTextArea();
        add(log);
        add(createButtons(), BorderLayout.SOUTH);
    }

    private Component createButtons() {
        JPanel panel = new JPanel(new GridLayout(1, 2));
        btnStart = new JButton(BTN_TITLE_START);
        btnStop = new JButton(BTN_TITLE_STOP);

        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                server.setWork(true);
                appendLog("Сервер запущен!");
            }
        });

        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                server.setWork(false);
                appendLog("Сервер остановлен!");

            }
        });

        panel.add(btnStart);
        panel.add(btnStop);
        return panel;
    }
}
