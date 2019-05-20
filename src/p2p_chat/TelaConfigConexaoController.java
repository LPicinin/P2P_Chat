/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p2p_chat;

import Classes.Cliente;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import util.util;

/**
 * FXML Controller class
 *
 * @author luis
 */
public class TelaConfigConexaoController implements Initializable
{

    private static Cliente cliente;
    private boolean flagcon;

    @FXML
    private Button btTestConnect;
    @FXML
    private TextField txNome;
    @FXML
    private TextField txIPServer;
    @FXML
    private Button btConnect;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        flagcon = false;
        txIPServer.setText("192.168.56.1");
    }

    @FXML
    private void evtTesteConexao(MouseEvent event)
    {
        try
        {
            cliente = new Cliente(util.getIp_Processado(), txNome.getText(), txIPServer.getText());
            flagcon = (cliente.connect());
            Alert a;
            if(flagcon)
                a =  new Alert(Alert.AlertType.INFORMATION, "Teste de Conexão Foi Bem Sucedida", ButtonType.OK);
            else
                a = new Alert(Alert.AlertType.ERROR, "Teste de Conexão Falhou", ButtonType.OK);
            a.show();
            btConnect.setDisable(!flagcon);
            txIPServer.setDisable(flagcon);
            txNome.setDisable(flagcon);
            btTestConnect.setDisable(flagcon);
        } catch (Exception ex)
        {
            flagcon = false;
        }
        
        
    }

    @FXML
    private void evtConnect(MouseEvent event)
    {
        if (flagcon)
        {
            try
            {
                Parent root = FXMLLoader.load(getClass().getResource("TelaCliente.fxml"));
                Scene s = new Scene(root);
                P2P_Chat.getSTAGE().setScene(s);
            } catch (Exception ex)
            {
                System.out.println(ex.getCause());
            }
        }
    }

    public static Cliente getCliente()
    {
        return cliente;
    }

    

}
