package ChatRoom;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * ClassName: ChatRoom.ChatClientTest
 * Package: Network_Programming
 * Description: A client side file with multi-threading for chat room
 *
 * @Author Piggie
 * @Create 2/01/2024 6:10 pm
 * @Version 1.0
 */
public class ChatClientTest {
    public static void main(String[] args) throws Exception{
        int port = 8989;
        Socket socket = new Socket("127.0.0.1", port);
        Send send = new Send(socket);
        send.start();

        Receive receive = new Receive(socket);
        receive.start();
        send.join();
    }

}

class Send extends Thread {
    private Socket socket;
    private boolean setName;
    private String name;
    public Send(Socket socket) {
        super();
        this.socket = socket;
        this.setName = false;
    }

    @Override
    public void run() {
        OutputStream outputStream = null;
        PrintStream ps = null;

        try {
            outputStream = socket.getOutputStream();
            ps = new PrintStream(outputStream);
            if (this.setName == false) {
                System.out.print("Please input your name: ");
                this.name = new Scanner(System.in).nextLine();
                this.setName = true;
                ps.println(this.name);
                ps.flush();
            }
            while (true) {
                Scanner in = new Scanner(System.in);
                System.out.println("me: ");
                String str = in.nextLine();
                if (str.equals("bye")) {
                    break;
                }
                ps.println(str);
                ps.flush();
            }
            ps.close();
            if (outputStream != null) {
                outputStream.close();
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Receive extends Thread {
    private Socket socket;
    public Receive(Socket socket) {
        super();
        this.socket = socket;
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        PrintStream printStream = null;
        try {
            inputStream = socket.getInputStream();
            printStream = new PrintStream(System.out);
            Scanner scanner = new Scanner(inputStream);
            while (scanner.hasNextLine()) {
                String str = scanner.nextLine();
                printStream.println(str);
            }
            printStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

