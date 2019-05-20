/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package p2p_chat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Aluno
 */
public class P2P_Chat extends Application
{
    private static Stage STAGE;
    @Override
    public void start(Stage stage) throws Exception
    {
        STAGE = stage;
        Parent root = FXMLLoader.load(getClass().getResource("TelaPrincipal.fxml"));

        Scene scene = new Scene(root);
        STAGE.initStyle(StageStyle.DECORATED);
        STAGE.setScene(scene);
        STAGE.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }

    public static Stage getSTAGE() 
    {
        return STAGE;
    }
    
}
