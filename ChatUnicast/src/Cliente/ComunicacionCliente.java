package Cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import protocolo.MensajeProtocolo;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alex
 * Se debe crear el objeto, despues llamar al metodo iniciarComunicacion y despues al enviarPeticiones
 * para cerrar el socket llamar al metodo cerrarConexion
 */
public class ComunicacionCliente {
    public static int estadoComunicacionExitosa=1;
    public static int estadoComunicacionError=0;
    String nombre;
    int puertoServidor;
    String direccionServidor;
    Socket socket;
    BufferedReader in;
    PrintWriter out;
    public ComunicacionCliente(String nombre,String direccionServ,int puertoServ){
        
        direccionServidor=direccionServ;
        puertoServidor=puertoServ;
        this.nombre=nombre;
        
        
    }

    ComunicacionCliente() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public int iniciarComunicacion(){
            
        try {
            String dirip; 
            System.setProperty("javax.net.ssl.trustStore","src/SSLCliente/AlmacenTrust");
               SocketFactory socketFactory = SSLSocketFactory.getDefault();

    // No es necesario que cambiemos nada más.
    // ¡Ahí está la gracia de utilizar fábricas!
            socket = socketFactory.createSocket(direccionServidor, puertoServidor);
             //socket=new Socket(direccionServidor,puertoServidor);
             out= new PrintWriter(socket.getOutputStream(), true);
             in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
             return estadoComunicacionExitosa;
             
                
        } catch (IOException ex) {
            Logger.getLogger(ComunicacionCliente.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Error al crear el socket en iniciarComunicacion() de ComunicacionCliente");
            return estadoComunicacionError;
        }

    }
    /**
     * El mensaje que se pase a este método no debe llevar cabeceras puestas
     * @param mensaje es el mensaje sin cabeceras
     */
    public void enviarMensajes(String mensaje){
       // boolean salir=false;
       // do{
          String mensajeConCabecera=MensajeProtocolo.mensaje(mensaje);
          out.println(mensajeConCabecera);
          out.flush();
          
       // }while(salir==false);
        
    
    }
    
    

    BufferedReader getIn() {
       return in;
    }

    PrintWriter getOut() {
       return out;
    }
    
    public Socket getSocket(){
        return socket;
    
    }
    
}
