package Classes;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javax.net.ssl.SSLServerSocket;

public class Cliente implements Runnable, Serializable
{

    private String ip;
    private String nome;
    private Integer porta;
    private String IPservidor;
    private static ServerSocket server;
    
    private TextField txmsg;
    private ListView<Mensagem> lvmensagens;
    private ListView<Cliente> lvClientes;

    public Cliente()
    {
    }

    public Cliente(String ip, String nome, String IPservidor)
    {
        this.ip = ip;
        this.nome = nome;
        this.IPservidor = IPservidor;
    }

    public Cliente(String ip, String nome, String IPservidor, TextField txmsg, ListView<Mensagem> lvmensagens, ListView<Cliente> lvClientes)
    {
        this.ip = ip;
        this.nome = nome;
        this.IPservidor = IPservidor;
        this.txmsg = txmsg;
        this.lvmensagens = lvmensagens;
        this.lvClientes = lvClientes;
    }

    
    
    public Cliente(String ip, String nome, Integer porta)
    {
        this.ip = ip;
        this.nome = nome;
        this.porta = porta;
    }

    public Cliente(String ip)
    {
        this.ip = ip;
    }

    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public String getNome()
    {
        return nome;
    }

    public void setNome(String nome)
    {
        this.nome = nome;
    }

    public Integer getPorta()
    {
        return porta;
    }

    public void setPorta(Integer porta)
    {
        this.porta = porta;
    }

    public String getIPservidor()
    {
        return IPservidor;
    }

    public void setIPservidor(String IPservidor)
    {
        this.IPservidor = IPservidor;
    }

    public boolean connect()
    {
        boolean flagcon = false;
        try
        {
            String con = "@connect#"+ip+"#"+nome;
            Socket cliente = new Socket(IPservidor, Servidor.getPort());
            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
            saida.flush();
            
            saida.writeObject(con);//new Cliente(InetAddress.getLocalHost().toString(), txNome.getText())
            

            ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
            String msg = (String) entrada.readObject();
            String[] parametros = msg.split("#");
            String[] Sub_parametros;
            
            for (int i = 0; i < parametros.length-1; i++)
            {
                
            }
            System.out.println("mensagem recebida do servidor:" + msg);
            entrada.close();
            saida.close();
            System.out.println("Conexão encerrada");
            flagcon = true;
        } catch (Exception ex)
        {
            System.out.println(ex.getCause());
            flagcon = false;
        }
        return flagcon;
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

    @Override
    public String toString()
    {
        return "IP: "+ip+"\nNome: "+nome;
    }

    public boolean upServer()
    {
        boolean f = true;
        try
        {
            server = new ServerSocket(porta);
        } catch (IOException ex)
        {
            System.out.println(ex.getCause());
            f = false;
        }
        return f;
    }

    public boolean DowServer()
    {
        boolean flag = true;
        try
        {
            server.close();
            System.out.println("Shutting down Socket server!!");
        } catch (IOException ex)
        {
            flag = false;
        }
        return flag;
    }
    
    
    @Override
    public void run()
    {
        if (upServer())
        {
            if (server != null && server.isBound())
            {
                try
                {
                    Object pacote;
                    boolean flag = true;
                    while (flag)
                    {
                        // Instancia o ServerSocket ouvindo a porta 12345
                        //ServerSocket servidor = new ServerSocket(port);
                        System.out.println("Servidor/Cliente ouvindo a porta "+porta);
                        // o método accept() bloqueia a execução até que
                        // o servidor receba um pedido de conexão
                        Socket cliente = server.accept();
                        System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress());
                        ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
                        saida.flush();
                        saida.writeObject("Hi");
                        saida.close();
                        cliente.close();
                    }
                } catch (Exception ex)
                {
                    System.out.println(ex.getMessage());
                }
            } else
            {
                new Alert(Alert.AlertType.ERROR, "erro ao levantar o servidor", ButtonType.OK).show();
            }
        }
    }

}
