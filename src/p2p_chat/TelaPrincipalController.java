/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p2p_chat;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Aluno
 */
public class TelaPrincipalController implements Initializable
{

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        
    }

    @FXML
    private void evtServidorSelecionado(MouseEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("TelaServidor.fxml"));
            Scene s = new Scene(root);
            P2P_Chat.getSTAGE().setScene(s);
        }
        catch(Exception ex)
        {
            System.out.println(ex.getCause());
        }
    }

    @FXML
    private void evtClienteSelecionado(MouseEvent event)
    {
        try
        {
            Parent root = FXMLLoader.load(getClass().getResource("TelaConfigConexao.fxml"));
            Scene s = new Scene(root);
            P2P_Chat.getSTAGE().setScene(s);
        }
        catch(Exception ex)
        {
            System.out.println(ex.getCause());
        }
        
    }

}
