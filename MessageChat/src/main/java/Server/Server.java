package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Server {
    private static final int port = 8052;
    private static ConcurrentLinkedDeque<ClientHandler> clients ;

    public static void main(String[] args)  {
        clients = new ConcurrentLinkedDeque<ClientHandler>() ;

        try {
            ServerSocket server = new ServerSocket(port);
            while(true){
                Socket socket = server.accept();
                myPrint("Соединение установлено");
                try {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            myPrint("Старт авторизации");
                            //
                            try {
                                ClientHandler handler = new ClientHandler(socket);

                                while (true) {
                                    handler.authorization();
                                    MessageDB messageDB = DB.chekAuthorization(handler.getNickName(), handler.getPassword());
                                    handler.sendMessage(messageDB.getMessageForClient());
                                    if (messageDB.getRez()){
                                        break;
                                    }
                                }
                                allClients(handler);
                                addClients(handler);
                                new Thread(handler).start();
                            } catch (Exception e) {
                                myPrint("Соединение разорвано");

                            }
                            //
                        }
                    }).start();

                } catch (Exception e) {
                    myPrint("Обрыв соединения");
                }
            }
        } catch (IOException e) {
            myPrint("Сервер остановлен");
        }


    }

    private static void allClients(ClientHandler handler) {
        for (ClientHandler client : clients) {
            try {
                handler.sendMessage(String.format("/addClient %s%n",client.getNickName()));
            } catch (IOException e) {
                myPrint("Соединение разорвано\n");
                removeClients(handler);
            }
        }
    }

    public synchronized static void myPrint(String message){
        Calendar data = new GregorianCalendar();
        DateFormat df = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss ");
        System.out.println( df.format(data.getTime()) + message);
    }

    public static void removeClients (ClientHandler client){
        clients.remove(client);
    }
    public static void massageUpdateNick(String message, String oldNick, ClientHandler handler){
        String[] str = message.split(" ");
        try {
            MessageDB messageDB = DB.updateNick(oldNick,str[1]);

                handler.sendMessage(messageDB.getMessageForClient());
        }
             catch (IOException e) {
                myPrint("Соединение разорвано\n");
                removeClients(handler);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            myPrint("Проблеиы с базой данных\n");
        }
    }

    public static void addClients (ClientHandler client){
        clients.add(client);
        broadCastMessage(String.format("/addClient К чату присоеденился %s%n",client.getNickName()));
    }


    public static void broadCastMessage(String message){
        for (ClientHandler client : clients) {
            try {
                client.sendMessage(message);
            } catch (Exception e) {
                myPrint("Соединение разорвано");
                removeClients(client);
            }
        }
    }

    public static void messageToNick (String message,String prefixMessage,ClientHandler handler) throws IOException {
        boolean findNick = false;
        try {
            int ind1 = message.indexOf(" ");
            int ind2 = message.indexOf(" ", ind1 + 1);
            String nick = message.substring(ind1 + 1, ind2);
            String str = message.substring(ind2 + 1);



            for (ClientHandler client : clients) {
                if (client.getNickName().equals(nick)){
                    findNick = true;
                    handler.sendMessage(String.format(" %s private message to %s: %s%n ",prefixMessage,nick,str));
                    client.sendMessage(String.format(" %s private message to %s: %s%n ",prefixMessage,nick,str));
                    break;
                }
            }
            if(!findNick){
                myPrint("Нет участника с таким именем!\n");
                handler.sendMessage(String.format(" %s Нет участника с таким именем : %s %n ",prefixMessage,nick));

            }
        } catch (StringIndexOutOfBoundsException e) {
            myPrint("Не верный формат сообщения\n");
            handler.sendMessage("Не верный формат сообщения "+message+"\n");
        }

    }


}
