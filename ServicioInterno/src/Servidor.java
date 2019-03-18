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
        while(true) {
            try {
                serverSocket = new ServerSocket(puerto);
                socket = new Socket();
                socket = serverSocket.accept();

                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String mensaje = input.readLine();

                byte[] pdf = PDFCreator.createPDF(mensaje, "Paso 1", "Paso 2");

                output = new DataOutputStream(socket.getOutputStream());
                output.writeInt(pdf.length);
                output.write(pdf);

                System.out.println("Conection ended");

                socket.close();
            } catch (Exception e) {

            }
        }
    }
}
