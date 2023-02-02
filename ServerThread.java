import java.io.*;
import java.net.*;

public class ServerThread extends Thread {
    ServerSocket server = null;
    Socket client = null;
    String stringaRicevuta = null;
    String stringaModifica = null;
    BufferedReader inDalClient;
    DataOutputStream outVersoClient;

    String numeriVincenti;
    String nomeGiocatore;
    

    public Socket attendi() {
        try {
            System.out.println("1 SERVER partito in esecuzione ...");
            server = new ServerSocket(6789);
            client = server.accept();
            server.close();
            inDalClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
            outVersoClient = new DataOutputStream(client.getOutputStream());
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Errore durante l'istanza del server !");
            System.exit(1);
        }
        return client;
    }

    public ServerThread(Socket socket, String numeriVincenti) {
        this.client = socket;
        this.numeriVincenti = numeriVincenti;
    }

    public void run() {
        try {
            comunica();
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public void comunica() throws Exception {
        inDalClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
        outVersoClient = new DataOutputStream(client.getOutputStream());


        stringaRicevuta = inDalClient.readLine();

        
        for (;;) {
            

            // -----------------------------------------------------------------------------------------------

            if (stringaRicevuta.trim() == "" || stringaRicevuta.trim() == null || stringaRicevuta.equals("FINE")) {

                System.out.println("Echo sul server in chiusura :" + stringaRicevuta);
                // clientConnessi = 0;
                break;

            }
            else {


                // outVersoClient.writeBytes(stringaRicevuta + " (ricevuta e ritrasmessa)" + '\n');
                System.out.println("6 Echo sul server :" + stringaRicevuta);
                nomeGiocatore = stringaRicevuta;
                // clientConnessi++;

                String numeriUsciti = "";
                //genero i 5 numeri che invio al client come stringa
                for (int i = 0; i < 5; i++) {
                    numeriUsciti += (Math.round((Math.random() * 10) % 9)); // divisore per dividere i vari numeri
                }

                System.out.println("Stringa client " + stringaRicevuta + ": " + numeriUsciti);
                outVersoClient.writeBytes(numeriUsciti + '\n');

                System.out.println("Numero utenti connessi: " + (super.activeCount() - 1)); // vedo quanti giocatori ci sono

                Boolean estrazione = false;

                do {
                    if((super.activeCount() - 1) >= 4) { // se ci sono 4 o più client, si estraggono i numeri vincenti


                        sleep(60000);
                        outVersoClient.writeBytes("ESTRAZIONE" + '\n'); // comunico l'inizio dell'estrazione

                    

                        System.out.println("Stringa client " + numeriVincenti);
                        outVersoClient.writeBytes(numeriVincenti + '\n');
                        estrazione = true;
                    }
                    sleep(1);
                } while (!estrazione);

                
                stringaRicevuta = inDalClient.readLine();
                System.out.println("Il client " + nomeGiocatore + " ha ben " + stringaRicevuta + " numeri uguali a quelli estratti");
                int numeriBuoni = Integer.parseInt(stringaRicevuta);

                float premio = (float)(numeriBuoni * 99.99);

                System.out.println("Il client " + nomeGiocatore + " ha vinto " + premio + "€");
                String premioStr = "";
                premioStr = Float.toString(premio) + "€";
                outVersoClient.writeBytes(premioStr + '\n');

                break;


            }
        }

        outVersoClient.close();
        inDalClient.close();
        System.out.println("9 Chiusura socket" + client);
        client.close();
    }

}
