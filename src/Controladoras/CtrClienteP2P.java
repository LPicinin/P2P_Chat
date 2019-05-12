/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladoras;

import Classes.Cliente;

/**
 *
 * @author luis
 */
public class CtrClienteP2P
{

    private static CtrClienteP2P clienteP2P;
    private Cliente origem;
    private Thread pr_recebe;

    private CtrClienteP2P()
    {

    }
    public static CtrClienteP2P instancia()
    {
        if(clienteP2P == null)
            clienteP2P = new CtrClienteP2P();
        return clienteP2P;
    }

    public static CtrClienteP2P getClienteP2P()
    {
        return clienteP2P;
    }

    public static void setClienteP2P(CtrClienteP2P clienteP2P)
    {
        CtrClienteP2P.clienteP2P = clienteP2P;
    }

    public Cliente getOrigem()
    {
        return origem;
    }

    public void setOrigem(Cliente origem)
    {
        this.origem = origem;
    }
    
    
    
    public void connect()
    {
        
    }

    public void disconnect()
    {

    }

    public void send()
    {
        
    }

    public void receve()
    {
        
    }
}
