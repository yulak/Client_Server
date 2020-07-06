package HW_2_6.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    private final int PORT = 7777;
    static Socket socket;
    static ServerSocket server;
    private DataInputStream incoming;
    private DataOutputStream outcoming;
    private Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        new Server().run();
    }

    public void run(){
        try{
            server = new ServerSocket(PORT);
            System.out.println("Server on");

            socket = server.accept();
            System.out.println("User connected");

            incoming = new DataInputStream(socket.getInputStream());
            outcoming = new DataOutputStream(socket.getOutputStream());

            Thread serverTread = new Thread(()->{
               while (true){
                   clientMsg(scanner.nextLine());
               }
            });
            serverTread.setDaemon(true);
            serverTread.start();

            while (true){
                String text = incoming.readUTF();
                if (text.equals("/end")){
                    clientMsg("/end");
                    break;
                }
                System.out.println("User: " + text);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                incoming.close();
                outcoming.close();
                socket.close();
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Server off");
    }
    public void clientMsg(String text){
        try {
            outcoming.writeUTF(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
