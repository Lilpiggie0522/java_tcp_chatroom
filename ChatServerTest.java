package ChatRoom;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * ClassName: ChatClientServer
 * Package: Network_Programming
 * Description: A server side file with multi-threading for chat room
 *
 * @Author Piggie
 * @Create 2/01/2024 6:38 pm
 * @Version 1.0
 */
public class ChatServerTest {
    static ArrayList<Socket> online = new ArrayList<>();
    public static void main(String[] args) throws Exception {
        int port = 8989;
        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            Socket socket = serverSocket.accept();
            online.add(socket);
            MessageHandler mh = new MessageHandler(socket);
            mh.start();
        }
    }
    static class MessageHandler extends Thread {
        private Socket socket;
        private String ip;
        private boolean setName;
        private String name;

        public MessageHandler(Socket socket) {
            super();
            this.socket = socket;
            this.setName = false;
        }

        public void run() {
            try {
                ip = socket.getInetAddress().getHostAddress();
                // sendToOther(ip+" is online");

                InputStream input = socket.getInputStream();
                InputStreamReader reader = new InputStreamReader(input);
                BufferedReader br = new BufferedReader(reader);

                String str;
                while((str = br.readLine())!=null){
                    if (!this.setName) {
                        this.setName = true;
                        this.name = str;
                        sendToOther(this.name + " is online", socket);
                    } else {
                        sendToOther(this.name+": "+str, socket);
                    }

                }

                sendToOther(this.name+" is offline", socket);
            } catch (IOException e) {
                sendToOther(this.name+" dropped", socket);
            } finally{
                online.remove(socket);
            }
        }
        public void sendToOther(String str, Socket socket) {
            for (Socket client : online) {
                if (client.getInetAddress().getHostAddress().equals(socket.getInetAddress().getHostAddress())) {
                    System.out.println("sending to myself");
                } else {
                    try {
                        OutputStream outputStream = client.getOutputStream();
                        PrintStream printStream = new PrintStream(outputStream);
                        printStream.println(str);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

            }
        }
    }


}


