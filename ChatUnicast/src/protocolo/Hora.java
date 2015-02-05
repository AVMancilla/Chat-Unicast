/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package protocolo;

import java.util.Calendar;

/**
 *
 * @author alex
 */
public class Hora {
    public static String getHora(){
        Calendar cal1=Calendar.getInstance();
        String hora=cal1.get(Calendar.HOUR)+":"+cal1.get(Calendar.MINUTE)+":"+cal1.get(Calendar.SECOND);
        return hora;
    }
    public static void main (String args[]){
    
    System.out.println(getHora());
    }
}
