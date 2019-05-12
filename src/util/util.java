/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author luis
 */
public class util
{
    public static String getIp_Processado(String ip)
    {
        return ip.split("/")[1];
    }
}
