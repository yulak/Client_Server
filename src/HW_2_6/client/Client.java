package HW_2_6.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private final int PORT = 7777;
    private final String IP_ADD = "Localhost";
    static Socket socket;
    private DataInputStream incoming;
    private DataOutputStream outcoming;
    private Scanner scanner;

    public static void main(String[] args) {
        new Client().run();
    }

    public void run() {
        try {
            socket = new Socket(IP_ADD, PORT);
            incoming = new DataInputStream(socket.getInputStream());
            outcoming = new DataOutputStream(socket.getOutputStream());
            scanner = new Scanner(System.in);

            System.out.println("User connected");

            Thread clientThread = new Thread(() -> {
                while (true) {
                    serverMsg(scanner.nextLine());
                }
            });
            clientThread.setDaemon(true);
            clientThread.start();

            while (true) {
                String text = incoming.readUTF();
                if (text.equals("/end")) {
                    break;
                }
                System.out.println("Server_Msg: " + text);
            }
            System.out.println("Server close");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            serverMsg("/end");
            try {
                incoming.close();
                outcoming.close();
                socket.close();
                scanner.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void serverMsg(String text) {
        try {
            outcoming.writeUTF(text);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

