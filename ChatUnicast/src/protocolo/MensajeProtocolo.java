/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package protocolo;

/**
 *
 * @author alex
 */
public class MensajeProtocolo {
    
    public final static int tipoMensaje=1;
    public final static int tipoSalir=2;
    public final static int tipoPublicar=3;
    public final static int tipoListar=4;
    public final static int tipoLogin=5;
    public final static int tipoError=6;
    
    /**
     * Lo manda el cliente
     */
    public static String mensaje(String mensaje){
        return "MENSAJE"+"#/#"+mensaje;
    }
    /**
     * Lo manda el cliente
     */
    public static String salir(){
        return "SALIR";
    }
    public static String publicarMensaje(String mensaje){
        
        return "PUBLICAR"+"#/#"+mensaje;
    
    }
    public static String listarUsuarios(String mensaje){
        return "LISTAR"+"#/#"+mensaje;
    }
    
    /**
     * Hay que pasarle el resultado de esto a publicarMensaje(
     */
    public static String nuevoUsuarioConectado(String login){
        return Hora.getHora()+" "+"Se ha conectado "+login+"\n";
    }
    public static String peticionLoginIncorrecta(String comentario){
        return "ERROR"+"#/#"+"Login incorrecto "+comentario;
    }
    
    /**
     * Añade el usuario que envia el mensaje al string
     */
    public static String mensajeConUsuarioOrigen(String login,String mensaje){
        return Hora.getHora()+" "+login+"  :>>  "+mensaje;
    }
    //public static String errorHebraUsuario(){
       // return "ERROR"+"#/#"+"ErrorSocket cerrar usuario";
    
    //}
    public static int gestionarPeticion(String peticion){
        String palabras[];
        int tipo=-1;
       // String mensaje;
        palabras=peticion.split("#/#");
        switch(palabras[0]){
            case "MENSAJE":
            tipo=tipoMensaje;
            break;
            case "SALIR":
                tipo=tipoSalir;
                break;
            case "LISTAR":
                tipo=tipoListar;
                break;
            case "PUBLICAR":
                tipo=tipoPublicar;
                break;
            case "LOGIN":
                tipo=tipoLogin;
                break;
            case "ERROR":
                tipo=tipoError;
                break;
            default:
                tipo=-1;
                break;
        } 
        return tipo;
    
    }
    
    public static String login(String login,String password){
        String definitivo;
        definitivo="LOGIN"+"#/#"+login+"#/#"+Seguridad.algoritmoEncriptacion(password);
    
        return definitivo;
    }
   
    /**
     * Devuelve el  login del mensaje de protocolo LOGIN
     */
    public static String getLogin(String peticion){
        String palabras[];
        palabras=peticion.split("#/#");
        return palabras[1];
    }
    public static String getPassword(String peticion){
        String palabras[];
        palabras=peticion.split("#/#");
        
        return Seguridad.algoritmoDesencriptacion(palabras[2]);
    }
    
    /**
     * hace split ("#/#") y devuelve palabras[1]
     * por ejemplo sirve para recibir mensaje, publicar, error
     * 
     */
    public static String quitarCabecera(String peticion) {
         String palabras[];
        palabras=peticion.split("#/#");
        return palabras[1];
    }

    public static String peticionClienteDesconectado(String login) {
       String mensaje=login+" se ha desconectado\n";
       return mensaje;
    }
    
    
}
