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

    @FXML
    private TextField txmsg;
    @FXML
    private ListView<Mensagem> lvmensagens;
    @FXML
    private ListView<Cliente> lvClientes;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        CtrClienteP2P.instancia().connect();
    }

    @FXML
    private void evtEnviar(MouseEvent event) 
    {
        
    }
    
}
