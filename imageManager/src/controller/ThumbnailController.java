//This work done by Zhiyang Liu and Yanxiao He
package controller;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.ImageFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ThumbnailController {
    public Label filenameLabel;
    public Label widthLabel;
    public Label heightLabel;
    public Label locationLabel;
    public ImageView imageView;
    private ImageFile imageFile;
    private Stage stage;
    private MainController mainController;
    private String magickExeFile;
    public void init(ImageFile imageFile,Stage stage,MainController mainController){
        this.imageFile=imageFile;
        this.stage=stage;
        this.mainController=mainController;
        imageView.setImage(new Image(new File(imageFile.getLocation()+"\\"+imageFile.getFilename()).toURI().toString()));
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        filenameLabel.setText("File Name: "+imageFile.getFilename());
        widthLabel.setText("Width: "+String.valueOf(imageFile.getWidth()));
        heightLabel.setText("Height: "+String.valueOf(imageFile.getHeight()));
        locationLabel.setText("Location: "+String.valueOf(imageFile.getLocation()));

    }

    public void onDeleteButtonClicked(ActionEvent actionEvent) {
        StorageController.deleteImageFile(this.imageFile.getId());
        mainController.updateGrid();
    }

    public void onTransformButtonClicked(ActionEvent actionEvent) {
        List<String> choices = Arrays.asList("BMP", "JPG", "PNG");

        ChoiceDialog<String> dialog = new ChoiceDialog<>("BMP", choices);
        dialog.setTitle("Selection");
        dialog.setHeaderText("Please select one");
        dialog.setContentText("Convert to:");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(selectedOption -> {
            if(magickExeFile==null){
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Executable Files", "*.exe"));
                fileChooser.setTitle("Please select your magick.exe"); //select your local magick.exe
                File selectedFile = fileChooser.showOpenDialog(stage);
                if (selectedFile == null) {
                    return;
                }
                magickExeFile=selectedFile.getAbsolutePath();
            }

            String newFilename=this.imageFile.getFilename().split("\\.")[0]+"."+selectedOption.toLowerCase();
            int exitCode=-1;
            try{
                ProcessBuilder processBuilder=new ProcessBuilder(magickExeFile,imageFile.getLocation()+"\\"+imageFile.getFilename(),"data\\"+newFilename);
                Process process = processBuilder.start();
                exitCode = process.waitFor();
            } catch (IOException | InterruptedException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Conversion failed!");
                alert.setContentText("Conversion failed!");
                alert.showAndWait();
                return;
            }
            if(exitCode!=0){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Conversion failed!");
                alert.setContentText("Conversion failed!");
                alert.showAndWait();
                return;
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Successful conversion! Do you want to save the converted image?");
            alert.setContentText("Choose your option.");
            ButtonType buttonTypeOK = new ButtonType("OK");
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);
            Optional<ButtonType> result2 = alert.showAndWait();
            if (!result2.isPresent() || result2.get() != buttonTypeOK) {
                return;
            }
            saveAs(newFilename);

        });
    }
    private void saveAs(String newFilename){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save As");
        fileChooser.setInitialFileName(newFilename);
        File selectedFile = fileChooser.showSaveDialog(stage);
        if (selectedFile == null) {
            System.out.println(1);
            return;
        }
        String saveTo=selectedFile.getAbsolutePath();
        try{
            Files.move(Path.of("data\\"+newFilename),Path.of(saveTo), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText("File saved successfully!");
        alert.setContentText("File saved successfully!");
        alert.showAndWait();
    }
}
