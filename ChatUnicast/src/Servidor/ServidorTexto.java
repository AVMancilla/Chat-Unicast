package Servidor;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alex
 */
public class ServidorTexto {
    
    HebraPrincipalServidor hebra;
    ComunicacionServidor com;
    ServidorTexto(){
        com=new ComunicacionServidor(8888);
        hebra=new HebraPrincipalServidor(com);
        hebra.start();
        System.out.println("vamos");
        if(com.getEstado()==ComunicacionServidor.estadoErrorIniciarRed){
        
            System.out.println("Error crear Server socket");
        }
        if(com.getEstado()==ComunicacionServidor.estadoIniciadoCorrectamente){
            System.out.println("Servidor funcionando");
        }
        
    }
    public static void main (String args[]){
    
        
        ServidorTexto server=new ServidorTexto();
        
    
    }
    
}
