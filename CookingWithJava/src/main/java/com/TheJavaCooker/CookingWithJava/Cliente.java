package com.TheJavaCooker.CookingWithJava;

import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Cliente {

    Socket socket;
    int puerto = 9000;
    String ip = "127.0.0.1";
    DataInputStream input;
    PrintStream output;

    public void start() {
        try {
            socket = new Socket(ip, puerto);
            input = new DataInputStream(socket.getInputStream());

            String mensaje = "Hola";
            output = new PrintStream(socket.getOutputStream());
            output.println(mensaje);

            byte[] pdf = null;
            int length = input.readInt();
            if(length>0) {
                pdf = new byte[length];
                input.readFully(pdf, 0, pdf.length);
            }

            System.out.println(pdf);

            input.close();
            output.close();
            socket.close();
        } catch(Exception e) {

        }
    }
}
