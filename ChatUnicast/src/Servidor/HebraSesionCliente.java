package Servidor;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import protocolo.MensajeProtocolo;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alex
 */
public class HebraSesionCliente extends Thread {
    BufferedReader in;
    PrintWriter out;
    ComunicacionServidor comunicacion;
    Usuario usuario;
    Socket socket;
    int estadoSocket;
    int estadoProtocolo;
    public final static int estadoSocketRulando=1;
    public final static int estadoSocketCerrado=0;
    public final static int estadoProtocoloNoLogueado=0;
    public final static int estadoProtocoloLogueado=1;
    boolean salir=false;
    //String nombre;
    HebraSesionCliente(Socket socket,ComunicacionServidor comunicacion,Usuario usuario){
        try {
            this.socket=socket;
            out= new PrintWriter(socket.getOutputStream(), true);
            in= new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.comunicacion=comunicacion;
            this.usuario=usuario;
            usuario.setHebra(this);
        } catch (IOException ex) {
            Logger.getLogger(HebraSesionCliente.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Error al crear el socket en la hebra del cliente");
        }
               
    
    }
    @Override
    public void run(){
        String peticion;
        String peticionSinCabecera;
        int tipo;
        //boolean salir=false;
        try{
        do{
            
            peticion=leerPeticion();
            tipo=MensajeProtocolo.gestionarPeticion(peticion);
               System.out.println("estamos en el run de hebrasesioncliente: "+"peticion: "+peticion+ " tipo: "+tipo);
         
            switch(estadoProtocolo){
                case estadoProtocoloNoLogueado:
                    System.out.println();
                    boolean existe=false;
                    if(tipo==MensajeProtocolo.tipoLogin){
                        String log=MensajeProtocolo.getLogin(peticion);
                        String pass=MensajeProtocolo.getPassword(peticion);
                        existe=comunicacion.getBaseDeDatos().existeUsuario(log+pass);
                        usuario.setLogin(log);
                        usuario.setPassword(pass);
                         
                    }
                    if(existe==false){
                        System.out.println("mensajeIncorrecto, borrando hebra");
                        comunicacion.borrarUsuarioIncorrectoListaProvisional(usuario,ComunicacionServidor.borrarErrorLogin);
                    }else{
                        comunicacion.addUsuarioListaConectados(usuario);
                        estadoProtocolo=estadoProtocoloLogueado;
                    }
                   
                    
                    break;
                case estadoProtocoloLogueado:
                    switch(tipo){
                        case MensajeProtocolo.tipoMensaje:
                    
                        System.out.println("estadoProtocoloLogueado "+peticion);
                        peticionSinCabecera=MensajeProtocolo.quitarCabecera(peticion);
                        String peticionConOrigen=MensajeProtocolo.mensajeConUsuarioOrigen(usuario.getLogin(), peticionSinCabecera);
                        for(int i=0;i<comunicacion.getSizeListaConectados();i++){
                            comunicacion.enviarMensajeCliente(i,peticionConOrigen,ComunicacionServidor.tipoPublicar);
                
                            }
                        
                    break;
                        case MensajeProtocolo.tipoListar:
                            peticionSinCabecera=MensajeProtocolo.quitarCabecera(peticion);
                            comunicacion.enviarMensajeCliente(tipo, peticion, ComunicacionServidor.tipoListar);
                            break;
                    }
                    
                break;
        }
        }while(salir==false);
        }catch(Exception ex){
            System.err.println("ha habido un error");
            if(estadoProtocolo==estadoProtocoloNoLogueado){
                System.out.println("exception usuario no logueado ");
                 comunicacion.borrarUsuarioIncorrectoListaProvisional(usuario,ComunicacionServidor.borrarErrorHebra);
              }else if(estadoProtocolo==estadoProtocoloLogueado){
                  System.out.println("exception usaurio logueado");
                comunicacion.borrarUsuarioCorrectoListaUsuarios(usuario);
            }
        }
        
    }
    public String leerPeticion(){
        String mandar=null;
        try {
            if(!socket.isClosed() && in!=null){
                mandar = in.readLine();
            }
        } catch (IOException ex ) {
            Logger.getLogger(HebraSesionCliente.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("problema al leerPeticion en Hebra cliente");
        }catch(NullPointerException j){
            comunicacion.borrarUsuarioCorrectoListaProvisional(usuario);
        }
        return mandar;
    }
    PrintWriter getOut(){
        return out;
    }
    public void cerrarSocket(){
        try {
            socket.close();
            System.out.println("Socket del cliente cerrado correctamente");
            estadoSocket=estadoSocketCerrado;
        } catch (IOException ex) {
            
            Logger.getLogger(HebraSesionCliente.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Error al cerrar el socket del cliente");
            
        }
    }

    void salir() {
       salir=true;
    }
}
