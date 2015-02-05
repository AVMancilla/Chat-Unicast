/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import protocolo.MensajeProtocolo;

/**
 *
 * @author alex
 */
public class HebraRecibirMensajes extends Thread {
    
    BufferedReader in;
    PrintWriter out;
    VentanaCliente ventana;
    Socket socket;
    boolean salir=false;
    

    public HebraRecibirMensajes(ComunicacionCliente comunicacion, VentanaCliente ventana) {
        in = comunicacion.getIn();
        out = comunicacion.getOut();
        socket = comunicacion.getSocket();
        this.ventana = ventana;

    }

    @Override
    public void run() {

        String peticion;
        String peticionSinCabecera = "";
        try {
            while (salir==false) {
                peticion = in.readLine();
                if (peticion != null) {
                    System.out.println("Recibido del servidor: " + peticion);
                    
                    switch(MensajeProtocolo.gestionarPeticion(peticion)){
                        case MensajeProtocolo.tipoPublicar:
                        System.out.println("peticion del tipo publicar");
                        peticionSinCabecera = MensajeProtocolo.quitarCabecera(peticion);
                        ventana.publicarMensajesNuevos(peticionSinCabecera);
                            break;
                        case MensajeProtocolo.tipoError:
                            peticionSinCabecera=MensajeProtocolo.quitarCabecera(peticion);
                            ventana.publicarMensajesNuevos(peticion);
                            break;
                        case MensajeProtocolo.tipoListar:
                            peticionSinCabecera=MensajeProtocolo.quitarCabecera(peticion);
                            
                            String palabras[];
                            palabras=peticionSinCabecera.split(" ");
                            ventana.actualizarListaConectados(palabras);
                            break;
                    }
                } else {
                    System.out.println("Servidor ha cerrado socket");
                    
                    socket.close();
                    System.out.println("Socket cerrado");
                    salir=true;
                }
                System.out.println("iterando");
            }

        } catch (IOException ex) {
            Logger.getLogger(HebraRecibirMensajes.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Ha habido un error al leer la peticion del BufferedReader HebraRecibirMensajes");
        }
    }
    public void enviar(String mensaje){
        if(out!=null){
        out.println(mensaje);
        }else{
            System.err.println("No hay socket por el que enviar");
        }
    }
}
