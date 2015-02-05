/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util;

import java.util.Random;
import java.util.Vector;

/**
 *
 * @author alex
 */
public class Varias {
    public static void movidas(){
       String curDir = System.getProperty("user.dir");//obtiene el directorio actual en el que estamos trabajando
          System.out.println("    ---------------       "+curDir);
    }
    
    public static void shuffle(Vector v){
        Random random = new Random(System.currentTimeMillis());
        Object[] array = new Object[v.size()];
        for(int i = 0; i < v.size(); i++){
            array[i] = v.elementAt(i);
        }
       
        int index;
        Object temp;
        for(int i = array.length - 1; i > 0; i--){
            index = random.nextInt(i + 1);
            temp = array[i];
            array[i] = array[index];
            array[index] = temp;
        }

        for(int i = 0; i < v.size(); i++){
            v.setElementAt(array[i], i);
        }
    }
    
    public String openFileToString(byte[] _bytes)
{
    String file_string = "";

    for(int i = 0; i < _bytes.length; i++)
    {
        file_string += (char)_bytes[i];
    }

    return file_string;    
}
}
