package com.TheJavaCooker.CookingWithJava;

import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Ingrediente;
import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Paso;
import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Utensilio;

import java.io.DataInputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Set;

public class Cliente {

    Socket socket;
    int puerto = 9000;
    String ip = "127.0.0.1";
    DataInputStream input;
    PrintStream output;

    String nombre;
    String tipo;
    String duracion;
    String nombre_creador;
    Set<Ingrediente> ingredientes;
    Set<Utensilio> utensilios;
    Set<Paso> pasos;

    public Cliente(String nombre,
            String tipo,
            String duracion,
            String nombre_creador,
            Set<Ingrediente> ingredientes,
            Set<Utensilio> utensilios,
            Set<Paso> pasos) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.duracion = duracion;
        this.nombre_creador = nombre_creador;
        this.ingredientes = ingredientes;
        this.utensilios = utensilios;
        this.pasos = pasos;
    }

    public void start() {
        try {
            socket = new Socket(ip, puerto);
            input = new DataInputStream(socket.getInputStream());

            String mensaje = nombre;
            output = new PrintStream(socket.getOutputStream());
            output.println(mensaje);

            byte[] pdf = null;
            int length = input.readInt();
            if(length>0) {
                pdf = new byte[length];
                input.readFully(pdf, 0, pdf.length);
            }

            input.close();
            output.close();
            socket.close();
        } catch(Exception e) {

        }
    }
}
