/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BaseDeDatosDeUsuarios;

import java.util.ArrayList;

/**
 *
 * @author alex
 */
public class BaseDeDatos {
    ArrayList<String> usuarios;
    
    public BaseDeDatos(){
        usuarios=new ArrayList<String>();
        usuarios.add("alexhola");
        usuarios.add("sergiodelicaillo");
        usuarios.add("motherwanter");
    }
    public boolean existeUsuario(String usu){
       boolean existe=false;
        for (String usuario : usuarios) {
            if (usuario.equals(usu)) {
                existe=true;
                break;
            }
        }
       return existe;
    }
}
