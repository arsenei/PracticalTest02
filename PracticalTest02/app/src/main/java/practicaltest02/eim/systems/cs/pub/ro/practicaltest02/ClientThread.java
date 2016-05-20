package practicaltest02.eim.systems.cs.pub.ro.practicaltest02;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Ariel on 5/20/2016.
 */
public class ClientThread extends Thread {

    private String address;
    private int port;
    private String command;
    private TextView statusTextView;

    private Socket socket;

    public ClientThread(
            String address,
            int port,
            String command,
            TextView statusTextView) {
        this.address = address;
        this.port = port;
        this.command = command;
        this.statusTextView = statusTextView;
    }

    @Override
    public void run() {
        try {
            /*
            deschiderea unui canal de comunicație folosind
            parametrii de conexiune la server (adresa Internet, port)
            */
            socket = new Socket(address, port);
            if (socket == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
            }

            /*
            obținerea unor obiecte de tip BufferedReader și PrintWriter
            (prin apelul metodelor statice din clasa Utilities:
            getReader() și getWriter()), prin care se vor realiza
            operațiile de citire și scriere pe canalul de comunicație
            */
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);

            if (bufferedReader != null && printWriter != null) {

                /*
                trimiterea la server (prin scriere pe canalul de comunicație)
                 a comenzii
                */
                printWriter.println(command);
                printWriter.flush();

                /*
                primirea de la server (prin citire de pa canalul de comunicație) a informatiei
                */
                String alarmInformation;

                while ((alarmInformation = bufferedReader.readLine()) != null) {
                    final String finalizedAlarmInformation = alarmInformation;
                    statusTextView.post(new Runnable() {
                        @Override
                        public void run() {
                            statusTextView.append(finalizedAlarmInformation + "\n");
                        }
                    });
                }

            } else {
                Log.e(Constants.TAG, "[CLIENT THREAD] BufferedReader / PrintWriter are null!");
            }

            /*
            inchiderea canalului de comunicatie
             */
            socket.close();
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
    }

}
