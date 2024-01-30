//This work done by Zhiyang Liu and Yanxiao He
package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.ImageFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;

public class MainController {
    public GridPane gridPane;
    public Button uploadButton;
    private Stage stage;

    public void init(Stage stage){
        this.stage=stage;
        updateGrid();
    }

    public void onUploadButtonClicked(ActionEvent actionEvent) {
        FileChooser fileChooser=new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("BMP Files", "*.bmp"),
//                new FileChooser.ExtensionFilter("JPEG Files", "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter("JPG Files","*.jpg"),
                new FileChooser.ExtensionFilter("PNG Files", "*.png"),
                new FileChooser.ExtensionFilter("All Files","*.*"));
        List<File> selectedFiles=fileChooser.showOpenMultipleDialog(stage);

        if(selectedFiles!=null){
            ArrayList<ImageFile> allImages=StorageController.getAllImages();
            for(File file:selectedFiles){
                String filename=file.getAbsolutePath();
                Image image=new Image(new File(filename).toURI().toString());
                ImageFile imageFile=new ImageFile(file.getName(),StorageController.getNextId(), (int) image.getWidth(), (int) image.getHeight(),"",file.getParent());
                allImages.add(imageFile);
            }
            StorageController.save(allImages);
            updateGrid();
        }
    }
    public void updateGrid(){
        gridPane.getChildren().clear();
        int row=0;
        int col=0;
        for(ImageFile imageFile:StorageController.getAllImages()){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/thumbnail.fxml"));
            try{
                VBox vBox=loader.load();
                ThumbnailController controller=loader.getController();
                controller.init(imageFile,stage,this);
                gridPane.add(vBox,col,row);
            }catch (IOException e){
                throw new RuntimeException(e);
            }
            col++;
            if(col==3){
                col=0;
                row++;
            }
        }
    }
}
