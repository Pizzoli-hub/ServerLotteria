import java.io.*;
import java.net.*;
import java.time.LocalDateTime;

public class ClientStr {
    String nomeServer = "localhost";
    int portaServer = 6789;
    Socket miosocket;
    BufferedReader tastiera;
    String stringaUtente;
    String stringaRicevutaDalServer;
    DataOutputStream outVersoServer;
    BufferedReader inDalServer;
    LocalDateTime dataSistema;

    String[] numeriTrovati; // La stringa con i numeri "scelti"
    String[] numeriVincenti; // Numeri vincenti da confrontare

    public Socket connetti() { // metodo per connettersi al server
        System.out.println("2 CLIENT partito in esecuzione ...");
        try {
            tastiera = new BufferedReader(new InputStreamReader(System.in));
            miosocket = new Socket(nomeServer, portaServer);
            outVersoServer = new DataOutputStream(miosocket.getOutputStream());
            inDalServer = new BufferedReader(new InputStreamReader(miosocket.getInputStream()));
        }
       catch (UnknownHostException e) {
            System.err.println("Host sconosciuto");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Errore durante la connessione!");
            System.exit(1);
        }
        return miosocket;
    }

    public void comunica() { // metodo per comunicare con il server
        // for (;;) {
            try {
                System.out.println("4 ... inserisci il nome da dare al server, non perderlo: ");
                stringaUtente = tastiera.readLine(); // scrivo il nome del client al server

                System.out.println("5 ... invio la stringa al server ed attendo i numeri ...");
                outVersoServer.writeBytes(stringaUtente + '\n'); // mando il nome

                if (stringaUtente.equals("FINE")) {
                    System.out.println("9 ... FINE COMUNICAZIONE, chiusura della comunicazione ... ");
                    miosocket.close(); // se la stringa è FINE il client non riceve numeri
                    // break;
                }

                stringaRicevutaDalServer = inDalServer.readLine(); // leggo i numeri

                System.out.println("8 ... risposta dal server, numeri che ho: " + '\n' + stringaRicevutaDalServer);

                numeriTrovati = stringaRicevutaDalServer.trim().split(""); // trasformo la stinga in un array

                // for (String a : numeriTrovati) {
                //     System.out.println(a);
                // }

                do {
                    stringaRicevutaDalServer = inDalServer.readLine(); //aspetto finché non inizia l'estrazione
                } while (!(stringaRicevutaDalServer.equals("ESTRAZIONE")));

                stringaRicevutaDalServer = inDalServer.readLine(); // leggo i numeri vincenti
                numeriVincenti = stringaRicevutaDalServer.trim().split(""); // trasformo una stringa in un array

                // for (String a : numeriVincenti) {
                //     System.out.println(a);
                // }

                int premio = 0; // il premio è inizializzto a 0

                for (String mio : numeriTrovati) { // controllo i numeri vincenti uguali
                    for (String vincente : numeriVincenti) {
                        if (mio.equals(vincente)) {
                            premio++;
                            break;
                        }
                    }
                }

                String premioStr = "";
                premioStr = Integer.toString(premio);
                outVersoServer.writeBytes(premioStr + '\n'); // mando la quantità di numeri uguali al server

                stringaRicevutaDalServer = inDalServer.readLine(); // aspetto quanti soldi ho vinto
                if(premio == 0) {
                    miosocket.close(); // se non ho vinto nulla, chiudo
                }
                System.out.println("Ho vinto: " + stringaRicevutaDalServer); //se ho vinto qualcosa lo scrivo

                miosocket.close(); // chiudo

                
            }
            catch (Exception e) { // se durante la comunicazione c'è un errore, lo mostro e mando via la 
            System.out.println(e.getMessage());
            System.out.println("Errore durante la comunicazione col server!");
            System.exit(1);
            }
        // }
    }
}