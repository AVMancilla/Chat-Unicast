package Servidor;


import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import protocolo.Seguridad;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author alex
 */
public class Usuario {
    
  // private BufferedReader in;
   // private PrintWriter out;
   // private Socket socket;
    private String login;
    private String password;
    private HebraSesionCliente hebra;
    
    Usuario(HebraSesionCliente nuevaHebra) {
        hebra=nuevaHebra;
        
       }
    public Usuario(){}
    public String getLogin(){
        return login;
    }
    public void setHebra(HebraSesionCliente hebra){
        this.hebra=hebra;
    }
    public HebraSesionCliente getHebra(){
    
        return hebra;
    }
    public void startHebra(){
        hebra.start();
    }
    
    public void cerrarSocketEnHebra(){
        hebra.cerrarSocket();
    }
    public void setLogin(String login){
        this.login=login;
    }
    public void setPassword(String password){
        this.password=password;
    }
    public String getPassword(){
    
        return password;
    }
    boolean comprobarCliente(String log,String pass){
        boolean resultado;
        String passAux=Seguridad.algoritmoDesencriptacion(pass);
        resultado = passAux.equals(password) && login.equals(log);
        return resultado;
    }
    
    /**
     * Este metodo aplica el algoritmo de recuperacion de clave a partir de un codigo
     * Hay que cambiar el algoritmo para implementar seguridad
     */
    String convertirPass(String pass){
          return pass;
    
    }

    void borrarHebra() {
        hebra=null;
    }
   /** InetAddress direccionIP;
    int puerto;
    String nombre;
    String password;
    
    public Usuario(InetAddress d, int p){
        direccionIP=d;
        puerto=p;
    }
    InetAddress getAddress(){
        return direccionIP;
    }
    int getPuerto(){
        return puerto;
    }
    boolean equals(Usuario usuario){
        boolean valor=false;
        if(usuario.getAddress().equals(direccionIP) && this.puerto==usuario.getPuerto())
            valor=true;
        return valor;
    } **/
}
