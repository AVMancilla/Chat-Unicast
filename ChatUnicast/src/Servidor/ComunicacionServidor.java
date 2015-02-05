package Servidor;

import BaseDeDatosDeUsuarios.BaseDeDatos;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ServerSocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
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
public class ComunicacionServidor {

    //public static int puerto = 8888;
    public static String direccion = "192.168.1.134"; //ordenador asus
    public final static int estadoIniciadoCorrectamente = 1;
    public final static int estadoErrorIniciarRed = 2;
    public final static int estadoApagadoCorrectamente = 4;
    public final static int estadoApagadoError = 3;
    public final static int estadoInicial = 0;
    public final static int borrarErrorHebra = 0;
    public final static int borrarErrorLogin = 1;
    public final static int tipoPublicar = 0;
    public final static int tipoListar = 1;
    private int estado;
    BaseDeDatos baseDeDatos;
    private SSLServerSocket socket;
    //private Socket socket1;
    private int puerto;
    //DataInputStream in;
    PrintWriter out;
    BufferedReader in;
    //DataOutputStream out;
    ArrayList<HebraSesionCliente> listaHebras;
    ArrayList<Usuario> listaUsuarios;
    ArrayList<Usuario> listaUsuariosProvisional;
    //HebraPrincipalServidor mainHebra;
    private boolean apagado = false;

    public ComunicacionServidor(int puerto) {
        estado = estadoInicial;
        this.puerto = puerto;
        listaHebras = new ArrayList<HebraSesionCliente>();
        listaUsuarios = new ArrayList<Usuario>();
        listaUsuariosProvisional = new ArrayList<Usuario>();
        baseDeDatos = new BaseDeDatos();
        // this.mainHebra=mainHebra;
    }

    public boolean getApagado() {
        return apagado;
    }

    public void iniciarRed() throws KeyStoreException {
        try {
            //File fichero = new File("src/cert/datos.txt"); 
           // System.out.println(fichero.getAbsolutePath());  
           // InputStream in=ClassLoader.getSystemResourceAsStream("src/cert");
         
            System.setProperty("javax.net.ssl.keyStore","src/cert/AlmacenSR");
            System.setProperty("javax.net.ssl.keyStorePassword","finisterre");
             SSLServerSocketFactory sslServerSocketFactory=(SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
             socket=(SSLServerSocket) sslServerSocketFactory.createServerSocket(puerto);
            // Gestor de claves, para acceder al certificado del servidor
            // La implementacio'n de referencia soporta so'lo llaves con formato X.509
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");

            //socket = new ServerSocket(puerto);
            System.out.println("Servidor rulando....");
            estado = estadoIniciadoCorrectamente;
        } catch (IOException ex) {
            Logger.getLogger(ComunicacionServidor.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("error al crear socket" + ex.getCause());
            estado = estadoErrorIniciarRed;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ComunicacionServidor.class.getName()).log(Level.SEVERE, null, ex);
       

        }
    }

    int getEstado() {
        return estado;
    }

    ServerSocket getServerSocket() {
        return socket;
    }

    public String recibirPeticion() {
        String mensaje = "";
        try {
            if (socket != null) {
                SSLSocket socketCliente = (SSLSocket) socket.accept();
                out = new PrintWriter(socketCliente.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
                Usuario nuevoUsuario = new Usuario();
                HebraSesionCliente nuevaHebra = new HebraSesionCliente(socketCliente, this, nuevoUsuario);//poco ortodoxo pero crea una hebra y se la asigna a nuevoUsuario
                //listaHebras.add( new HebraSesionCliente(socket1,this));//creamos hebra de servicio y la añadimos al arraylist
                // listaHebras.get(listaHebras.size()-1).start();//iniciamos la hebra que acabamos de crear

                nuevoUsuario.startHebra();
                listaUsuariosProvisional.add(nuevoUsuario);
                System.out.println("hebra cliente nuevo creada");
// in= new DataInputStream(socket1.getInputStream());
                // out=new DataOutputStream(socket1.getOutputStream());

            }

        } catch (IOException ex) {
            Logger.getLogger(ComunicacionServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mensaje;
    }

    public void apagarServidor() {
        try {
            cerrarSockets();
            socket.close();
            apagado = true;
            estado = estadoApagadoCorrectamente;
        } catch (IOException ex) {
            Logger.getLogger(ComunicacionServidor.class.getName()).log(Level.SEVERE, null, ex);
            estado = estadoApagadoError;
        }

    }

    /**
     * borra al usuario de la lista de provisionales y lo agrega a la lista de
     * definitivos
     */
    public synchronized void addUsuarioListaConectados(Usuario usuario) {
        listaUsuarios.add(usuario);
        borrarUsuarioCorrectoListaProvisional(usuario);
        for (int i = 0; i < listaUsuarios.size(); i++) {
            enviarMensajeCliente(i, MensajeProtocolo.nuevoUsuarioConectado(usuario.getLogin()),tipoPublicar);
            enviarMensajeCliente(i,MensajeProtocolo.listarUsuarios(listaToArrayString()),tipoListar);
        }
    }

    public synchronized void borrarUsuarioCorrectoListaProvisional(Usuario usuario) {

        listaUsuariosProvisional.remove(usuario);
        System.out.println("Usuario Borrado");

    }

    public synchronized void borrarUsuarioCorrectoListaUsuarios(Usuario usuario) {

        String login = usuario.getLogin();
        listaUsuarios.remove(usuario);

        for (int i = 0; i < listaUsuarios.size(); i++) {
            enviarMensajeCliente(i, MensajeProtocolo.peticionClienteDesconectado(login),tipoPublicar);
            enviarMensajeCliente(i,MensajeProtocolo.listarUsuarios(listaToArrayString()),tipoListar);
        }

        System.out.println("Usuario Borrado lista usuarios");

    }

    public synchronized void cerrarSesionUsuario(Usuario usuario) {
        borrarUsuarioCorrectoListaUsuarios(usuario);
        for (int i = 0; i < listaUsuarios.size(); i++) {
            enviarMensajeCliente(i, MensajeProtocolo.peticionClienteDesconectado(usuario.getLogin()),tipoPublicar);
            enviarMensajeCliente(i,MensajeProtocolo.listarUsuarios(listaToArrayString()),tipoListar);
        }
    }

    public synchronized int getPosicionUsuarioConectado(Usuario usuario, ArrayList<Usuario> lista) {
        int posicion = -1;
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getLogin().equals(usuario.getLogin()) && lista.get(i).getPassword().equals(usuario.getPassword())) {
                posicion = i;
                break;
            }
        }
        return posicion;
    }

    public synchronized int getPosicionUsuarioProvisional(Usuario usuario) {
        int posicion = -1;

        posicion = listaUsuariosProvisional.indexOf(usuario);

        return posicion;
    }

    public synchronized void borrarUsuarioIncorrectoListaProvisional(Usuario usuario, int tipo) {
        int posicion = getPosicionUsuarioProvisional(usuario);
        if (tipo == borrarErrorLogin) {
            usuario.getHebra().getOut().println(MensajeProtocolo.peticionLoginIncorrecta(MensajeProtocolo.peticionLoginIncorrecta("")));
            usuario.getHebra().getOut().flush();
            listaUsuariosProvisional.get(posicion).getHebra().cerrarSocket();
            listaUsuariosProvisional.get(posicion).getHebra().salir();
            listaUsuariosProvisional.get(posicion).borrarHebra();
            listaUsuariosProvisional.remove(usuario);
        }

    }

    public void cerrarSockets() {
        while (!listaUsuariosProvisional.isEmpty()) {
            listaUsuariosProvisional.get(0).cerrarSocketEnHebra();
            listaUsuariosProvisional.get(0).borrarHebra();
            listaUsuariosProvisional.remove(0);

        }
        while (!listaUsuarios.isEmpty()) {
            listaUsuarios.get(0).cerrarSocketEnHebra();
            listaUsuarios.get(0).borrarHebra();
            listaUsuarios.remove(0);

        }
    }

    /*
     public void gestionarPosibleNuevoUsuario(Socket socket,DataOutputStream o, DataOutputStream i){
     Usuario nuevoUsu=new Usuario(socket.getInetAddress(),socket.getPort());
     boolean encontrado=false;
     for (Usuario listaUsuario : listaUsuarios) {
     if (nuevoUsu.equals(listaUsuario)) {
     encontrado=true;
     break;
     }
     }
     if(encontrado==false){
     listaUsuarios.add(nuevoUsu);
     System.out.println("nuevo usuario aniadido");
     }
        
     }*/
    public synchronized void enviarMensajeCliente(int i, String mensaje, int tipo) {
        System.out.println("tratando de enviar mensaje a usuario: " + i);
        Usuario usuario;
        if (listaUsuarios.get(i) != null) {
            if (tipo == tipoPublicar) {
                usuario = listaUsuarios.get(i);
                HebraSesionCliente hebra = usuario.getHebra();
                String mensajeConCabecera;
                PrintWriter o = hebra.getOut();
        // o.println(); // enviar línea de texto

                mensajeConCabecera = MensajeProtocolo.publicarMensaje(mensaje);

                o.println(mensajeConCabecera); // enviar línea de texto
                o.flush();
                System.out.println("Enviado a cliente " + i + ": " + mensajeConCabecera);
                
            } else if (tipo == tipoListar) {
                usuario = listaUsuarios.get(i);
                HebraSesionCliente hebra = usuario.getHebra();
                String mensajeConCabecera;
                PrintWriter o = hebra.getOut();
        // o.println(); // enviar línea de texto
                String lista=listaToArrayString();
                mensajeConCabecera = MensajeProtocolo.listarUsuarios(lista);

                o.println(mensajeConCabecera); // enviar línea de texto
                o.flush();
                System.out.println("Enviado a cliente " + i + ": " + mensajeConCabecera);

            }

        }
    }

    int getSizeListaConectados() {
        return listaUsuarios.size();
    }

    public BaseDeDatos getBaseDeDatos() {
        return baseDeDatos;
    }

    private synchronized String listaToArrayString() {
        String lista="";
         for(int i=0;i<listaUsuarios.size();i++){
             if(i==0){
                lista=listaUsuarios.get(i).getLogin();
             }else{
                 lista=lista+" "+listaUsuarios.get(i).getLogin();
             }
         }
         return lista;
    }

}
