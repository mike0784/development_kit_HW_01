package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import server.ServerWindow;
import client.ClientView;

public class ClientGUI extends JFrame implements ClientView {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    //private ServerWindow server;
    private boolean connected;
    private String name;
    private static String TITLE;
    private static String USER;
    private static String HOST;
    private static String PORT;
    private static String PASSWORD;
    private static String BTN_TITLE_LOGIN;
    private static String BTN_TITLE_SEND;
    private JTextArea log;
    private JTextField tfIPAddress, tfPort, tfLogin, tfMessage;
    private JPasswordField password;
    private JButton btnLogin, btnSend;
    private JPanel headerPanel;

    private Client client;

    public ClientGUI(ServerWindow server, String path) throws IOException{
        this.client = new Client(this, server.getServer());
        //this.server = server;
        readini(path);

        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setTitle(TITLE);
        setLocation(server.getX() - 500, server.getY());

        createPanel();

        setVisible(true);
    }

    private void readini(String path) throws IOException
    {
        Properties props = new Properties();
        props.load(new FileInputStream(new File(path)));
        this.TITLE = props.getProperty("TITLE");
        USER = props.getProperty("USER");
        HOST = props.getProperty("HOST");
        PORT = props.getProperty("PORT");
        PASSWORD = props.getProperty("PASSWORD");
        BTN_TITLE_LOGIN = props.getProperty("BTN_TITLE_LOGIN");
        BTN_TITLE_SEND = props.getProperty("BTN_TITLE_SEND");
    }

    public void answer(String text){
        appendLog(text);
    }

    private void connectToServer() {
        if (client.connectToServer(tfLogin.getText())){
            hideHeaderPanel(false);
        }
    }

    @Override
    public void sendMessage(String message) {
        appendLog(message);
    }

    public void disconnectFromServer() {
        hideHeaderPanel(true);
        client.disconnect();
    }

    private void hideHeaderPanel(boolean visible)
    {
        headerPanel.setVisible(visible);
    }

    public void sendMessage(){
        client.sendMessage(tfMessage.getText());
        tfMessage.setText("");
    }

    private void appendLog(String text){
        log.append(text + "\n");
    }

    private void createPanel() {
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createLog());
        add(createFooter(), BorderLayout.SOUTH);
    }

    private Component createHeaderPanel(){
        headerPanel = new JPanel(new GridLayout(2, 3));
        tfIPAddress = new JTextField(HOST);
        tfPort = new JTextField(PORT);
        tfLogin = new JTextField(USER);
        password = new JPasswordField(PASSWORD);
        btnLogin = new JButton(BTN_TITLE_LOGIN);
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectToServer();
            }
        });

        headerPanel.add(tfIPAddress);
        headerPanel.add(tfPort);
        headerPanel.add(new JPanel());
        headerPanel.add(tfLogin);
        headerPanel.add(password);
        headerPanel.add(btnLogin);

        return headerPanel;
    }

    private Component createLog(){
        log = new JTextArea();
        log.setEditable(false);
        return new JScrollPane(log);
    }

    private Component createFooter() {
        JPanel panel = new JPanel(new BorderLayout());
        tfMessage = new JTextField();
        tfMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n'){
                    sendMessage();
                }
            }
        });
        btnSend = new JButton(BTN_TITLE_SEND);
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
        panel.add(tfMessage);
        panel.add(btnSend, BorderLayout.EAST);
        return panel;
    }

    @Override
    protected void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING){
            disconnectFromServer();
        }
        super.processWindowEvent(e);
    }
}
