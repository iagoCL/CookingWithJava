import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    ServerSocket serverSocket;
    Socket socket;
    int puerto = 9000;
    DataOutputStream output;
    BufferedReader input;

    // Métodos
    public void start() {
        try {
            serverSocket = new ServerSocket(puerto);
            socket = new Socket();
            socket = serverSocket.accept();

            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String mensaje = input.readLine();
            System.out.println(mensaje);
            output = new DataOutputStream(socket.getOutputStream());
            output.writeUTF("Conection ended");

            socket.close();
        } catch (Exception e) {

        }
    }
}
