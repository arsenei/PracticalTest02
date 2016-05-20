package practicaltest02.eim.systems.cs.pub.ro.practicaltest02;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest02MainActivity extends AppCompatActivity {

    // Server widgets
    private EditText serverPortEditText = null;
    private Button connectButton = null;

    // Client widgets
    private EditText clientAddressEditText = null;
    private EditText clientPortEditText = null;
    private EditText commandEditText = null;
    private Button sendCommandButton = null;
    private TextView statusTextView = null;

    private ServerThread serverThread = null;
    private ClientThread clientThread = null;

    private ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();

    private class ConnectButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String serverPort = serverPortEditText.getText().toString();
            if (serverPort == null || serverPort.isEmpty()) {
                Toast.makeText(
                        getApplicationContext(),
                        "Server port should be filled!",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() != null) {
                serverThread.start();
            } else {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not creat server thread!");
            }
        }

    }

    private GetStatusButtonClickListener getStatusButtonClickListener = new GetStatusButtonClickListener();

    private class GetStatusButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {

            String clientAddress = clientAddressEditText.getText().toString();
            String clientPort = clientPortEditText.getText().toString();

            /*
            verificare completarea câmpurilor text ce conțin
            parametrii de conexiune la server (adresă Internet, port)
            */
            if (clientAddress == null || clientAddress.isEmpty() ||
                    clientPort == null || clientPort.isEmpty()) {
                Toast.makeText(
                        getApplicationContext(),
                        "Client connection parameters should be filled!",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            /*
            existența unui fir de execuție corespunzător serverului
            care să ruleze la momentul respectiv;
            */
            if (serverThread == null || !serverThread.isAlive()) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] There is no server to connect to!");
                return;
            }

            /*
            completarea controalelor grafice ce conțin
            ce contine comanda
            */
            String command = commandEditText.getText().toString();
            if (command == null || command.isEmpty()) {
                Toast.makeText(
                        getApplicationContext(),
                        "Parameters from client (city / information type) should be filled!",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            statusTextView.setText(Constants.EMPTY_STRING);

            clientThread = new ClientThread(
                    clientAddress,
                    Integer.parseInt(clientPort),
                    command,
                    statusTextView
                 );
            clientThread.start();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        serverPortEditText = (EditText) findViewById(R.id.server_port_edit_text);
        connectButton = (Button) findViewById(R.id.connect_button);
        connectButton.setOnClickListener(connectButtonClickListener);

        clientAddressEditText = (EditText) findViewById(R.id.client_address_edit_text);
        clientPortEditText = (EditText) findViewById(R.id.client_port_edit_text);
        commandEditText = (EditText) findViewById(R.id.command);
        sendCommandButton = (Button) findViewById(R.id.command_button);
        sendCommandButton.setOnClickListener(getStatusButtonClickListener);
        statusTextView = (TextView) findViewById(R.id.status_text_view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_practical_test02_main, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }
}
