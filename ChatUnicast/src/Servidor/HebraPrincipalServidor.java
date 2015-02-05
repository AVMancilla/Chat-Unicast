package Servidor;

import java.security.KeyStoreException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alex
 */
public class HebraPrincipalServidor extends Thread{
    int estado=0;
    public static int estadoRedNoIniciada=0;
    public static int estadoRedIniciada=1;
    ComunicacionServidor comunicacion;
    
    HebraPrincipalServidor(ComunicacionServidor comunicacion){
        this.comunicacion=comunicacion;
        
    }
    
    @Override
    public void run(){
        try {
            //boolean salir=false;
            
            //if(estado==estadoRedNoIniciada){
            comunicacion.iniciarRed();
        } catch (KeyStoreException ex) {
            Logger.getLogger(HebraPrincipalServidor.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("ERROR CON KEYSTORE EXCEPTION");
        }
          //  estado=estadoRedIniciada;
            if(comunicacion.getServerSocket()!=null){
        do{
        comunicacion.recibirPeticion();
        //refrescarEstado(comunicacion.getEstado());
        }while(comunicacion.getApagado()==false);
        
        }
    }
    

    int getEstadoComunicacion(){
        return comunicacion.getEstado();
    }
    
    private void refrescarEstado(int estado) {
        switch(estado){
            case 0:
                System.out.println("Estado inicial");
                break;
            case 1:
                System.out.println("Servidor iniciado Correctamente");
                break;
            case 2:
                System.out.println("Error al iniciar Servidor");
                break;
            case 3:
                System.out.println("");
        }
        }
    
    
}
