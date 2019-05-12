/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p2p_chat;

import Classes.Cliente;
import Classes.Mensagem;
import Classes.Servidor;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import util.util;

/**
 * FXML Controller class
 *
 * @author Aluno
 */
public class TelaServidorController implements Initializable
{

    private static Servidor server;
    private Thread th;
    @FXML
    private TextField txMensagemEnvi;
    @FXML
    private ListView<Mensagem> lvMensagens;
    @FXML
    private ListView<Cliente> lvClientes;
    @FXML
    private Label lblIpServidor;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        iniciaComponentes();

        levantaServidor();
    }

    @FXML
    private void evtEnviarMensagemTodos(MouseEvent event)
    {
        server.sendAll();
    }

    @FXML
    private void evtDisconectar(MouseEvent event)
    {
        server.DowServer();
        th.stop();
        new Alert(Alert.AlertType.INFORMATION, "Servidor Fechado", ButtonType.OK).show();
    }

    private void levantaServidor()
    {
        if (!th.isInterrupted())
        {
            th.start();
        }
    }

    private void iniciaComponentes()
    {
        try
        {
            lblIpServidor.setText(util.getIp_Processado(InetAddress.getLocalHost().toString()));
        } catch (UnknownHostException ex)
        {
            System.out.println(ex.getCause());
        }
        server = new Servidor(txMensagemEnvi, lvMensagens, lvClientes, lblIpServidor);
        th = new Thread(server);
    }

}
