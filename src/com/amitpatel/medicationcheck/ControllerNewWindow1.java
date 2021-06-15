package com.amitpatel.medicationcheck;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class ControllerNewWindow1 {
    @FXML
    private Button buttonDone;
    @FXML
    public Label medicineAdded;

    @FXML
    public void initialize()
    {
        medicineAdded.setText("Hello!" + "\n" +
                "This program is used to sort your medication list into the categories it belongs to." + "\n" +
                "Your primary care provider can customize the database. " + "\n" +
                "Please note that while the default database includes many medications, " +
                "it does not include every single medication available." + "\n" +
                "Furthermore, a medication may have many different brand names and while the database " +
                "has many of them, it does not every brand name." + "\n" +
                "Finally, takes note that a medication may have multiple uses that may not be reflected " +
                "in the database. " + "\n" +
                "Always discuss your medications and what they are used for with your " +
                "primary care provider." + "\n" +
                "This program offers a reference for your medications. " + "\n" +
                "You can bring the list to your primary care provider and discuss what it may be used for.");
    }

    public void helpFinish(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) buttonDone.getScene().getWindow();
        stage.close();
    }
}
