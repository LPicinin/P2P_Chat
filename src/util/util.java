/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 *
 * @author luis
 */
public class util
{
    /*public static String getIp_Processado(String ip)
    {
        return ip.split("/")[1];
    }*/
    public static String getIp_Processado()
    {
        String systemipaddress = ""; 
        try
        { 
            URL url_name = new URL("http://bot.whatismyipaddress.com"); 
  
            BufferedReader sc = 
            new BufferedReader(new InputStreamReader(url_name.openStream())); 
  
            // reads system IPAddress 
            systemipaddress = sc.readLine().trim(); 
        } 
        catch (Exception e) 
        { 
            systemipaddress = "Cannot Execute Properly"; 
        } 
        System.out.println("Public IP Address: " + systemipaddress +"\n"); 
        return systemipaddress;
    }
}
