package server;

import client.Client;
import client.ClientGUI;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import file.WorkFile;

public class Server {

    boolean work;

    public static final String LOG_PATH = "src/Log/log.txt";

    List<Client> clientList;
    ServerWindow ui;

    WorkFile file;
    public Server(ServerWindow ui) {
        clientList = new ArrayList<>();
        file = new WorkFile(LOG_PATH);
        this.ui = ui;
    }

    public boolean connectUser(Client client){
        if (!work){
            return false;
        }
        clientList.add(client);
        return true;
    }

    public void setWork(boolean w)
    {
        work = w;
    }

    public void disconnectUser(Client client){
        clientList.remove(client);
        /*if (client != null){
            client.disconnectFromServer();
        }*/
    }

    public String getHistory() {
        return file.readLog();
    }

    public void sendMessage(String msg)
    {
        printText(msg);
        answerAll(msg);
    }

    private void printText(String text) {
        if (work) {
            ui.message(text);
            file.saveInLog(text);
        }
        else return;
    }

    private void answerAll(String text){
        for (Client client: clientList){
            client.serverAnswer(text);
        }
    }

}
