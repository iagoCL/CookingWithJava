package CookingWithJavaInternalService;

import CookingWithJavaInternalService.PDFCreator;
import CookingWithJavaInternalService.PersonalDebug;

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
            while (true) {
                socket = serverSocket.accept();
                PersonalDebug.imprimir("Conexión aceptada");

                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String mensaje = input.readLine();

                PersonalDebug.imprimir("Generando PDF: " + mensaje);
                byte[] pdf = PDFCreator.createPDF(mensaje, "Paso 1", "Paso 2");
                PersonalDebug.imprimir("PDF Generado: " + mensaje);
                output = new DataOutputStream(socket.getOutputStream());
                output.writeInt(pdf.length);
                output.write(pdf);

                PersonalDebug.imprimir("PDF Enviado");

                socket.close();
                PersonalDebug.imprimir("Conexion cerrada");
            }
        } catch (Exception e) {
            PersonalDebug.imprimir("ERROR de socket:" + e.toString());

        }
    }
}
