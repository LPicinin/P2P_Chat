package Classes;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class Servidor implements Runnable
{

    private static ServerSocket server;
    private static final Integer port = 8081;

    private TextField txMensagemEnvi;
    private ListView<Mensagem> lvMensagens;
    private ListView<Cliente> lvClientes;
    private Label lblIpServidor;
    private List<Cliente> clientes;

    public Servidor(TextField txMensagemEnvi, ListView<Mensagem> lvMensagens, ListView<Cliente> lvClientes, Label lblIpServidor)
    {
        this.txMensagemEnvi = txMensagemEnvi;
        this.lvMensagens = lvMensagens;
        this.lvClientes = lvClientes;
        this.lblIpServidor = lblIpServidor;
        clientes = new ArrayList<>();
    }

    public boolean upServer()
    {
        boolean f = true;
        try
        {
            server = new ServerSocket(port);
        } catch (Exception ex)
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

    public ServerSocket getServer()
    {
        return server;
    }

    public void setServer(ServerSocket server)
    {
        this.server = server;
    }

    public List<Cliente> getClientes()
    {
        return clientes;
    }

    public void setClientes(List<Cliente> clientes)
    {
        this.clientes = clientes;
    }

    public void accept(Socket cliente)
    {
        ObjectOutputStream saida = null;
        try
        {
            saida = new ObjectOutputStream(cliente.getOutputStream());
            saida.flush();
            StringBuilder acceptRes = new StringBuilder();
            Cliente c;
            for (int i = 0; i < lvClientes.getItems().size(); i++)
            {
                c = lvClientes.getItems().get(i);
                acceptRes.append("@accept#ipclient=").
                        append(c.getIp()).
                        append("#nome=").
                        append(c.getNome()).
                        append("#porta=").
                        append(c.getPorta());
            }   acceptRes.append("#count=").append(lvClientes.getItems().size());
            saida.writeObject(acceptRes.toString());
            saida.close();
        } catch (Exception ex)
        {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    public void newUser()
    {
        int size = lvClientes.getItems().size();
        Cliente uca = lvClientes.getItems().get(size - 1);
        for (int i = 0; i < size - 1; i++)
        {
            Cliente ci = lvClientes.getItems().get(i);
            try
            {
                String con = "#@newUser#" + uca.getIp() + "#" + uca.getNome() + "#" + uca.getPorta();
                Socket cliente = new Socket(ci.getIp(), ci.getPorta());
                ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
                saida.flush();
                saida.writeObject(con);//new Cliente(InetAddress.getLocalHost().toString(), txNome.getText())
                saida.close();
                cliente.close();
            } catch (Exception ex)
            {

            }
        }
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
                        System.out.println("Servidor ouvindo a porta " + port.toString());
                        //  bloqueia a execução até que o servidor receba um pedido de conexão
                        Socket cliente = server.accept();
                        System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress());
                        ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
                        pacote = entrada.readObject();
                        /*if(pacote instanceof Cliente)
                        {
                            System.out.println(((Cliente)(pacote)).toString());
                        }
                        else */

                        if (pacote instanceof String)
                        {
                            String res = (String) (pacote);
                            System.out.println(res);
                            String[] r = res.split("#");
                            switch (r[0])
                            {
                                case "@connect":
                                    lvClientes.getItems().add(new Cliente(r[1], r[2], lvClientes.getItems().size() + 8081));
                                    accept(cliente);
                                    //accept
                                    /*ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
                                    saida.flush();
                                    StringBuilder acceptRes = new StringBuilder();
                                    Cliente c;
                                    for (int i = 0; i < lvClientes.getItems().size(); i++)
                                    {
                                        c = lvClientes.getItems().get(i);
                                        acceptRes.append("@accept#ipclient=").
                                                append(c.getIp()).
                                                append("#nome=").
                                                append(c.getNome()).
                                                append("#porta=").
                                                append(c.getPorta());
                                    }
                                    acceptRes.append("#count=").append(lvClientes.getItems().size());
                                    saida.writeObject(acceptRes.toString());
                                    saida.close();
                                     */
                                    break;
                                case "@disconnect":

                                    break;
                            }

                        }

                        cliente.close();
                        newUser();
                    }
                } catch (Exception ex)
                {
                    System.out.println(ex.getMessage());
                }
            }
            else
            {
                new Alert(Alert.AlertType.ERROR, "erro ao levantar o servidor", ButtonType.OK).show();
            }
        }
    }

    public void sendAll()
    {
        if (server != null && server.isBound())
        {

        }
    }

    public static Integer getPort()
    {
        return port;
    }

}
