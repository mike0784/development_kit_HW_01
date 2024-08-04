package client;
import server.Server;

public class Client {
    private Server server;
    private boolean connected;
    private String name;
    private ClientView view;

    public Client(ClientView view, Server server)
    {
        this.view = view;
        this.server = server;
    }

    public boolean connectToServer(String name)
    {
        this.name = name;
        if (server.connectUser(this))
        {
            printText("Вы успешно подключились!\n");
            connected = true;
            String log = server.getHistory();
            if (log != null)
            {
                printText(log);
            }
            return true;
        }
        else {
            printText("Подключение не удалось\n");
            return false;
        }
    }

    public void disconnect() {
        if (connected) {
            connected = false;
            view.disconnectFromServer();
            server.disconnectUser(this);
            printText("Вы были отключены от сервера");
        }
    }

    public void sendMessage(String message)
    {
        if (connected) {
            if (!message.isEmpty()) {
                server.sendMessage(name + ": " + message);
            }
        }
        else {
            printText("Нет подключения к серверу");
        }
    }

    public void serverAnswer(String answer)
    {
        printText(answer);
    }

    private void printText(String text)
    {
        view.sendMessage(text);
    }
}
