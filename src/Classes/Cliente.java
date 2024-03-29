package Classes;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class Cliente implements Runnable, Serializable
{

    private String ip;
    private String nome;
    private Integer porta;
    private String IPservidor;
    private static ServerSocket server;

    private TextField txmsg;
    private ListView<Object> lvmensagens;
    private ListView<Cliente> lvClientes;
    private ObservableList<Cliente> listaClientes;

    public Cliente()
    {
        this.listaClientes = FXCollections.observableArrayList();
    }

    public Cliente(String ip, String nome, String IPservidor)
    {
        this.ip = ip;
        this.nome = nome;
        this.IPservidor = IPservidor;
        this.listaClientes = FXCollections.observableArrayList();
    }

    /*
    public Cliente(String ip, String nome, String IPservidor, TextField txmsg, ListView<Mensagem> lvmensagens, ListView<Cliente> lvClientes)
    {
        this.ip = ip;
        this.nome = nome;
        this.IPservidor = IPservidor;
        this.txmsg = txmsg;
        this.lvmensagens = lvmensagens;
        this.lvClientes = lvClientes;
    }
     */
    public Cliente(String ip, String nome, Integer porta)
    {
        this.ip = ip;
        this.nome = nome;
        this.porta = porta;
        this.listaClientes = FXCollections.observableArrayList();
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

    public ListView<Cliente> getLvClientes()
    {
        return lvClientes;
    }

    public void setLvClientes(ListView<Cliente> lvClientes)
    {
        this.lvClientes = lvClientes;
        this.lvClientes.setItems(listaClientes);
    }

    public void setTxmsg(TextField txmsg)
    {
        this.txmsg = txmsg;
    }

    public void setLvmensagens(ListView<Object> lvmensagens)
    {
        this.lvmensagens = lvmensagens;
    }

    public ObservableList<Cliente> getListaClientes()
    {
        return listaClientes;
    }

    public void setListaClientes(ObservableList<Cliente> listaClientes)
    {
        this.listaClientes = listaClientes;
    }

    public boolean connect()
    {
        boolean flagcon = false;
        try
        {
            String con = "@connect#" + ip + "#" + nome;
            Socket cliente = new Socket(IPservidor, Servidor.getPort());
            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
            saida.flush();

            saida.writeObject(con);//new Cliente(InetAddress.getLocalHost().toString(), txNome.getText())

            ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
            String msg = (String) entrada.readObject();
            String[] parametros = msg.split("#");
            String[] Sub_parametros;
            int i;
            for (i = 1; i < parametros.length - 3; i += 3)
            {
                listaClientes.add(new Cliente(parametros[i], parametros[i + 1], Integer.parseInt(parametros[i + 2])));
            }
            if (lvClientes != null)
            {
                lvClientes.setItems(listaClientes);
            }
            System.out.println("mensagem recebida do servidor:" + msg);
            porta = Integer.parseInt(parametros[parametros.length - 1]);
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
        try
        {
            String con = "@disconnect#" + ip + "#" + nome + "#" + porta;
            Socket cliente = new Socket(IPservidor, Servidor.getPort());
            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
            saida.flush();
            saida.writeObject(con);
            saida.close();
            ObservableList<Cliente> oc = lvClientes.getItems();
            for (Cliente c : oc)
            {
                cliente = new Socket(c.getIp(), c.getPorta());
                saida = new ObjectOutputStream(cliente.getOutputStream());
                saida.flush();
                saida.writeObject(con);
                saida.close();
            }
        } catch (Exception ex)
        {
            System.out.println(ex.getCause());
        }
    }

    public void send(boolean all)
    {
        Mensagem m = new Mensagem(this, null, txmsg.getText());
        Cliente ci;
        if (all)
        {
            ObservableList<Cliente> oc = lvClientes.getItems();

            for (int i = 0; i < oc.size(); i++)
            {
                try
                {
                    ci = oc.get(i);
                    Socket cliente = new Socket(ci.getIp(), ci.getPorta());
                    ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
                    saida.flush();
                    saida.writeObject(m.toStringSocket());//new Cliente(InetAddress.getLocalHost().toString(), txNome.getText())
                    saida.close();
                    cliente.close();
                } catch (IOException ex)
                {
                    System.out.println(ex.getCause());
                }
            }
        } else
        {
            ci = lvClientes.getSelectionModel().getSelectedItem();
            if (ci != null)
            {
                try
                {
                    Socket cliente = new Socket(ci.getIp(), ci.getPorta());
                    ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
                    saida.flush();
                    saida.writeObject(m.toStringSocket());
                    saida.close();
                    cliente.close();
                } catch (IOException ex)
                {
                    System.out.println(ex.getCause());
                }
            }
        }
        lvmensagens.getItems().add("Você disse: "+txmsg.getText());
        txmsg.clear();
    }

    public void receve(Socket emissor, String[] resposta)
    {
        LocalDateTime parse = LocalDateTime.parse(resposta[resposta.length - 1]);

        ObservableList<Cliente> oc = lvClientes.getItems();
        Cliente cemissor = null;
        boolean achou = false;
        for (int i = 0; i < oc.size() && !achou; i++)
        {
            cemissor = oc.get(i);
            achou = cemissor.getIp().equals(resposta[1]);
        }
        Mensagem ms = new Mensagem(cemissor, this, resposta[2]);
        ms.setDt(parse);
        lvmensagens.getItems().add(ms);
    }

    @Override
    public String toString()
    {
        return "IP: " + ip + "\nNome: " + nome + "\nPort: " + porta;
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
                        System.out.println("Servidor/Cliente ouvindo a porta " + porta);
                        // o método accept() bloqueia a execução até que
                        // o servidor receba um pedido de conexão
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
                                case "@newUser":
                                    listaClientes.add(new Cliente(r[1], r[2], Integer.parseInt(r[3])));
                                    if (lvClientes != null)
                                    {
                                        lvClientes.setItems(listaClientes);
                                    }
                                    break;
                                case "@receive":
                                    //Cliente c = new Cliente(r[1], r[2], r[3]);
                                    //lvClientes.getItems().remove(c);
                                    break;
                                case "@send":
                                    receve(cliente, r);
                                    break;
                                case "@disconnect":
                                    Cliente c = new Cliente(r[1], r[2], r[3]);
                                    lvClientes.getItems().remove(c);
                                    break;
                            }
                        } else
                        {
                            System.out.println("Cliente Recebeu um Pacote que não foi entendido!!! = " + pacote.toString());
                        }
                        /*ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
                        saida.flush();
                        saida.writeObject("Hi");
                        saida.close();*/
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

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final Cliente other = (Cliente) obj;
        if (!Objects.equals(this.ip, other.ip))
        {
            return false;
        }
        return true;
    }

    public boolean testConnect()
    {
        boolean flagcon = false;
        try
        {
            String con = "@testConnect#" + ip + "#" + nome;
            Socket cliente = new Socket(IPservidor, Servidor.getPort());
            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
            saida.flush();

            saida.writeObject(con);//new Cliente(InetAddress.getLocalHost().toString(), txNome.getText())

            ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
            String msg = (String) entrada.readObject();
            System.out.println("mensagem recebida do servidor:" + msg);
            flagcon = msg.equals("@Ok");
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

}
