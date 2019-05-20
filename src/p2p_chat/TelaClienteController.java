/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p2p_chat;

import Classes.Cliente;
import Classes.Mensagem;
import Controladoras.CtrClienteP2P;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author Aluno
 */
public class TelaClienteController implements Initializable
{
    private static Cliente cliente;
    private Thread th;
    @FXML
    private TextField txmsg;
    @FXML
    private ListView<Mensagem> lvmensagens;
    @FXML
    private ListView<Cliente> lvClientes;
    @FXML
    private CheckBox chEnviarParaTodos;
    @FXML
    private Label lblClose;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        try
        {
            cliente = TelaConfigConexaoController.getCliente();
            cliente.setLvClientes(lvClientes);
            cliente.setLvmensagens(lvmensagens);
            cliente.setTxmsg(txmsg);
            P2P_Chat.getSTAGE().setTitle("Usuário: "+cliente.getNome());
            th = new Thread(cliente);
            th.start();
        }
        catch(Exception ex)
        {
            
        }
    }

    @FXML
    private void evtEnviar(MouseEvent event) 
    {
        cliente.send(chEnviarParaTodos.selectedProperty().get());
    }

    @FXML
    private void ClkEnviarParaTodos(MouseEvent event)
    {
        if(chEnviarParaTodos.selectedProperty().get())
        {
            System.out.println("selecionado");
            lvClientes.getSelectionModel().clearSelection();
            lvClientes.setDisable(true);
        }
        else
            lvClientes.setDisable(false);
    }

    @FXML
    private void evtClose(MouseEvent event) 
    {
        cliente.disconnect();
        th.stop();
        P2P_Chat.getSTAGE().close();
    }
    
}
