/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author alex
 */
public class InputToFile {
    public static void InputStreamAFile(InputStream entrada){
 try{
   File f=new File("Archivo.txt");//Aqui le dan el nombre y/o con la ruta del archivo salida
   OutputStream salida=new FileOutputStream(f);
   byte[] buf =new byte[1024];//Actualizado me olvide del 1024
int len;
   while((len=entrada.read(buf))>0){
      salida.write(buf,0,len);
   }
   salida.close();
   entrada.close();
   System.out.println("Se realizo la conversion con exito");
  }catch(IOException e){
    System.out.println("Se produjo el error : "+e.toString());
  }
}
}
