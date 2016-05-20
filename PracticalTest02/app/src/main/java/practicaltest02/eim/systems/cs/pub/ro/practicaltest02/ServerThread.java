package practicaltest02.eim.systems.cs.pub.ro.practicaltest02;

import android.util.Log;

import org.apache.http.client.ClientProtocolException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by Ariel on 5/20/2016.
 */
public class ServerThread extends Thread {

    private int port = 0;
    private ServerSocket serverSocket = null;

    /*
    se va gestiona un obiect de tip Hashmap în care vor fi reținute
    informațiile cu privire la alarmele care au fost setate anterior
    */
    private HashMap<String, AlarmInformation> data = null;

    /*
    se va instanția un obiect de tip ServerSocket
    care va primi ca parametru portul indicat de utilizator
    */
    public ServerThread(int port) {
        this.port = port;
        try {
            this.serverSocket = new ServerSocket(port);
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
        this.data = new HashMap<>();
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void setServerSocker(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    public synchronized void setData(String client, AlarmInformation alarmInformation) {
        this.data.put(client, alarmInformation);
    }

    public synchronized HashMap<String, AlarmInformation> getData() {
        return data;
    }

    @Override
    public void run() {
        try {
            /*
            atâta vreme cât firul de execuție nu este întrerupt
                    (aplicația Android nu a fost distrusă)
                    */
            while (!Thread.currentThread().isInterrupted()) {
                /*
                sunt acceptate conexiuni de la clienți
                 */
                Log.i(Constants.TAG, "[SERVER] Waiting for a connection...");
                Socket socket = serverSocket.accept();

                /*
                comunicația fiind tratată pe un fir de execuție dedicat
                 */
                Log.i(Constants.TAG, "[SERVER] A connection request was received from " + socket.getInetAddress() + ":" + socket.getLocalPort());
                CommunicationThread communicationThread = new CommunicationThread(this, socket);
                communicationThread.start();
            }
        } catch (ClientProtocolException clientProtocolException) {
            Log.e(Constants.TAG, "An exception has occurred: " + clientProtocolException.getMessage());
            if (Constants.DEBUG) {
                clientProtocolException.printStackTrace();
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
    }

    public void stopThread() {
        if (serverSocket != null) {
            interrupt();
            try {
                serverSocket.close();
            } catch (IOException ioException) {
                Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }
        }
    }
}
