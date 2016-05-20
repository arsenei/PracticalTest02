package practicaltest02.eim.systems.cs.pub.ro.practicaltest02;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by Ariel on 5/20/2016.
 */
public class CommunicationThread  extends Thread {

    private ServerThread serverThread;
    private Socket socket;

    public CommunicationThread(ServerThread serverThread, Socket socket) {
        this.serverThread = serverThread;
        this.socket = socket;
    }

    @Override
    public void run() {
        if (socket != null) {

            try{

                 /*
                se obțin obiectele de tip BufferedReader și PrintWriter
                (prin apelul metodelor statice din clasa Utilities: getReader() și getWriter()),
                prin care se vor realiza operațiile de citire și scriere pe canalul de comunicație;
                */
                BufferedReader bufferedReader = Utilities.getReader(socket);
                PrintWriter printWriter = Utilities.getWriter(socket);

                if (bufferedReader != null && printWriter != null) {
                    Log.i(Constants.TAG, "[COMMUNICATION THREAD] Waiting for parameters from client (city / information type)!");
                    /*
                    se primesc de la client (prin citire de pe canalul de comunicație)
                    parametrii necesari pentru furnizarea informațiilor meteorologice
                     */
                    String command = bufferedReader.readLine();

                    /*
                    se obține o referință către obiectul gestionat de server
                    */
                    HashMap<String, AlarmInformation> data = serverThread.getData();

                    AlarmInformation alarmInformation = null;

                    if (command != null && !command.isEmpty()) {
                        printWriter.println(command);
                        printWriter.flush();
                    }
                }

        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[COMMUNICATION THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
    }
    }
}
