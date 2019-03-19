package com.TheJavaCooker.CookingWithJava;

import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Ingrediente;
import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Paso;
import com.TheJavaCooker.CookingWithJava.DataBase.Entities.Utensilio;

import java.io.DataInputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Set;

public class InternalServiceCliente {

    private Socket socket;
    private final static int puerto = 9000;
    private final static String ip = "127.0.0.1";
    private DataInputStream input;
    private PrintStream output;

    private String nombre;
    private String tipo;
    private String duracion;
    private String nombre_creador;
    private Set<Ingrediente> ingredientes;
    private Set<Utensilio> utensilios;
    private Set<Paso> pasos;

    public InternalServiceCliente(String nombre,
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

    public byte[] obtenerPDF() {
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
            return pdf;
        } catch(Exception e) {
            return null;
        }
    }
}
