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
            if(server == null)
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
    public boolean resposta(Socket cliente, String resposta)
    {
        boolean flag = false;
        ObjectOutputStream saida = null;
        try
        {
            saida = new ObjectOutputStream(cliente.getOutputStream());
            saida.flush();
            saida.writeObject(resposta);
            saida.close();
            flag = true;
        } catch (Exception ex)
        {
            System.out.println(ex.getCause());
        }
        return flag;
    }
    public void accept(Socket cliente)
    {
        ObjectOutputStream saida = null;
        try
        {
            saida = new ObjectOutputStream(cliente.getOutputStream());
            saida.flush();
            StringBuilder acceptRes = new StringBuilder("@accept");
            Cliente c;
            for (int i = 0; i < lvClientes.getItems().size()-1; i++)
            {
                c = lvClientes.getItems().get(i);
                acceptRes.append("#").
                        append(c.getIp()).
                        append("#").
                        append(c.getNome()).
                        append("#").
                        append(c.getPorta());
            }
            acceptRes.append("#").append(lvClientes.getItems().get(lvClientes.getItems().size() - 1).getPorta());
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
                String con = "@newUser#" + uca.getIp() + "#" + uca.getNome() + "#" + uca.getPorta();
                Socket cliente = new Socket(ci.getIp(), ci.getPorta());
                ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
                saida.flush();
                saida.writeObject(con);
                saida.close();
                cliente.close();
            } catch (Exception ex)
            {
                System.out.println(ex.getCause());
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

                        if (pacote instanceof String)
                        {
                            String res = (String) (pacote);
                            System.out.println(res);
                            String[] r = res.split("#");
                            switch (r[0])
                            {
                                case "@testConnect":
                                    resposta(cliente, "@Ok");
                                    break;
                                case "@connect":
                                    lvClientes.getItems().add(new Cliente(r[1], r[2], getNextPort()));
                                    accept(cliente);
                                    newUser();
                                    break;
                                case "@disconnect":
                                    Cliente c = new Cliente(r[1], r[2], r[3]);
                                    lvClientes.getItems().remove(c);
                                    break;
                            }
                        }

                        cliente.close();
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

    private Integer getNextPort()
    {
        Integer p = 0;
        if (lvClientes.getItems().size() == 0)
        {
            p = Servidor.port + 1;
        }
        else
        {
            p = lvClientes.getItems().get(lvClientes.getItems().size() - 1).getPorta() + 1;
        }
        return p;
    }

}
