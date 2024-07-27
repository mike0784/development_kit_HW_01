
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
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.io.BufferedReader;
public class ServerWindow extends JFrame {
    public static final int WIDTH = 400;
    public static final int HEIGHT = 300;
    public static final String LOG_PATH = "src/Log/log.txt";

    private static String BTN_TITLE_START;
    private static String BTN_TITLE_STOP;
    private static String TITLE;

    List<ClientGUI> clientGUIList;

    JButton btnStart, btnStop;
    JTextArea log;
    boolean work;

    public ServerWindow() throws IOException{
        clientGUIList = new ArrayList<>();
        readini();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setTitle(TITLE);
        setLocationRelativeTo(null);

        createPanel();

        setVisible(true);
    }

    private void readini() throws IOException
    {
        Properties props = new Properties();
        props.load(new FileInputStream(new File("src/config/configServer.ini")));
        this.TITLE = props.getProperty("TITLE");
        this.BTN_TITLE_START = props.getProperty("BTN_TITLE_START");
        this.BTN_TITLE_STOP = props.getProperty("BTN_TITLE_STOP");
    }

    public boolean connectUser(ClientGUI clientGUI){
        if (!work){
            return false;
        }
        clientGUIList.add(clientGUI);
        return true;
    }

    public String getLog() {
        return readLog();
    }

    public void disconnectUser(ClientGUI clientGUI){
        clientGUIList.remove(clientGUI);
        if (clientGUI != null){
            clientGUI.disconnectFromServer();
        }
    }

    public void message(String text){
        if (!work){
            return;
        }
        text += "";
        appendLog(text);
        answerAll(text);
        saveInLog(text);
    }

    private void answerAll(String text){
        for (ClientGUI clientGUI: clientGUIList){
            clientGUI.answer(text);
        }
    }

    private void saveInLog(String text){
        try (FileWriter writer = new FileWriter(LOG_PATH, true)){
            writer.write(text);
            writer.write("\n");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private String readLog(){
        String result = "";
        try{
            File file = new File(LOG_PATH);
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            String line = null; // = reader.readLine();
            while ((line = reader.readLine()) != null) {
                result += line + "\n";
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
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
                if (work){
                    appendLog("Сервер уже был запущен");
                } else {
                    work = true;
                    appendLog("Сервер запущен!");
                }
            }
        });

        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!work){
                    appendLog("Сервер уже был остановлен");
                } else {
                    work = false;
                    while (!clientGUIList.isEmpty()){
                        disconnectUser(clientGUIList.get(clientGUIList.size()-1));
                    }
                    appendLog("Сервер остановлен!");
                }
            }
        });

        panel.add(btnStart);
        panel.add(btnStop);
        return panel;
    }
}
