import controller.MainController;
import controller.StorageController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        StorageController.init();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/main.fxml"));
        Parent root=loader.load();
        MainController loginController=loader.getController();
        loginController.init(stage);
        Scene scene=new Scene(root,800,600);
        stage.setScene(scene);
        stage.show();
    }
}
