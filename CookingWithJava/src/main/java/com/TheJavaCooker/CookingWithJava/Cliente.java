package com.TheJavaCooker.CookingWithJava;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class Cliente {

    Socket socket;
    int puerto = 9000;
    String ip = "127.0.0.1";
    BufferedReader input, teclado;
    PrintStream output;

    public void start() {
        try {
            socket = new Socket(ip, puerto);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            teclado = new BufferedReader(new InputStreamReader(System.in));
            String mensaje = teclado.readLine();
            output = new PrintStream(socket.getOutputStream());

            output.println(mensaje);
            String msg = input.readLine();
            System.out.println(msg);

            input.close();
            output.close();
            teclado.close();
            socket.close();
        } catch(Exception e) {

        }
    }
}
