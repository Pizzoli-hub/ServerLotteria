import java.net.*;

public class MultiServer {
    String numeriVincenti = "";
    public void start() {
        for (int i = 0; i < 5; i++) {
            numeriVincenti += (Math.round((Math.random() * 10) % 9));
        }
        try {
            ServerSocket serverSocket = new ServerSocket(6789);
            for (;;) {
                System.out.println("1 Server in attesa ...");
                Socket socket = serverSocket.accept();
                System.out.println("3 Server socket " + socket);
                ServerThread serverThread = new ServerThread(socket, numeriVincenti);
                serverThread.start();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Errore durante l'istanza del server");
            System.exit(1);
        }
    }
}
