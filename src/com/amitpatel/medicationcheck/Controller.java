package com.amitpatel.medicationcheck;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Controller {
    @FXML
    public Label actionResult;
    @FXML
    public TextField medicineNameAddInput;
    @FXML
    public Button addButtonInput;
    @FXML
    private TextField medicineNameAdd;
    @FXML
    private TextField medicineNameRemove;
    @FXML
    private TextField medicineNameChange;
    @FXML
    private TextField categoryAdd;
    @FXML
    private TextField categoryRemove;
    @FXML
    private TextField categoryAddChange;
    @FXML
    private TextField categoryRemoveChange;
    @FXML
    private Button addButton;
    @FXML
    private Button removeButton;
    @FXML
    private Button changeButton;
    @FXML
    private TableView<MedicineTable> medicineTableListView = new TableView<>();
    @FXML
    private ObservableList<MedicineTable> bobTheBuilder;
    @FXML
    private String categoryAddText;
    @FXML
    private String categoryRemoveText;
    @FXML
    private String medicine;
    private Map<String, ArrayList<String>> toPrint;

    private boolean check = false;

    private ArrayList<String> medicineTable;
    private ArrayList<String> categoryTable;

    private ArrayList<String> tempTable = new ArrayList<>();

    @FXML
    public void initialize()
    {
        addButton.setDisable(true);
        removeButton.setDisable(true);
        changeButton.setDisable(true);
        addButtonInput.setDisable(true);
    }

    @FXML
    public void medicineButtonClick(ActionEvent click) throws IOException {
        functions function = new functions();

        if (click.getSource().equals(addButton))
        {
            actionResult.setText(medicineNameAdd.getText().toLowerCase() + " has been added to "
                    + categoryAdd.getText().toLowerCase());

            toPrint.putIfAbsent(categoryAddText.toLowerCase(), new ArrayList<>());
            toPrint.get(categoryAddText.toLowerCase()).add(medicine.toLowerCase());
            function.setToPrint(toPrint);

            medicineNameAdd.clear();
            categoryAdd.clear();
            addButton.setDisable(true);

            changeTable();
        }
        else if (click.getSource().equals(addButtonInput))
        {
            actionResult.setText(medicineNameAddInput.getText().toLowerCase() + " has been added to the list");

            tempTable.clear();

            function.setToPrint(toPrint);
            addToTable(medicine);
            CreateFromInput();

            medicineNameAddInput.clear();
            addButtonInput.setDisable(true);
        }
        else if (click.getSource().equals(removeButton))
        {
            actionResult.setText(medicineNameRemove.getText().toLowerCase() + " has been removed from "
                    + categoryRemove.getText().toLowerCase());

//            for (String name : toPrint.keySet()) {
//                System.out.println(name + ": " + toPrint.get(name).toString());
//            }

            toPrint.get(categoryRemoveText.toLowerCase()).remove(medicine.toLowerCase());

            function.isItEmpty();

            toPrint.entrySet().removeIf(entry -> entry.getValue().isEmpty());
            function.setToPrint(toPrint);
            function.isItEmpty();

            medicineNameRemove.clear();
            categoryRemove.clear();
            removeButton.setDisable(true);

            changeTable();
        }
        else if (click.getSource().equals(changeButton))
        {
            actionResult.setText(medicineNameChange.getText().toLowerCase() + " has been added to "
                    + categoryAddChange.getText().toLowerCase()
                    + " and removed from " + categoryRemoveChange.getText().toLowerCase());

            toPrint.putIfAbsent(categoryAddText.toLowerCase(), new ArrayList<>());
            toPrint.get(categoryAddText.toLowerCase()).add(medicine.toLowerCase());
            toPrint.get(categoryRemoveText.toLowerCase()).remove(medicine.toLowerCase());
            function.isItEmpty();
            function.setToPrint(toPrint);
            function.isItEmpty();

            medicineNameChange.clear();
            categoryAddChange.clear();
            categoryRemoveChange.clear();
            changeButton.setDisable(true);

            changeTable();
        }
    }

    private void changeTable() {
        medicineTable = new ArrayList<>();
        categoryTable = new ArrayList<>();
        for (String name : toPrint.keySet()) {
            categoryTable.add(name);
            medicineTable.add(toPrint.get(name).toString());
        }

        bobTheBuilder.clear();

        for (int i = 0; i < medicineTable.size(); i++)
        {
            bobTheBuilder.add(new MedicineTable(categoryTable.get(i),medicineTable.get(i)));
        }
    }

    @FXML
    public void handleKeyReleasedAdd()
    {
        String medicineAddString = medicineNameAdd.getText();
        String categoryAddString = categoryAdd.getText();

        boolean disableButtonAdd = medicineAddString.isEmpty()
                || medicineAddString.trim().isEmpty()
                || categoryAddString.isEmpty()
                || categoryAddString.trim().isEmpty();

        addButton.setDisable(disableButtonAdd);

        medicine = medicineAddString;
        categoryAddText = categoryAddString;
    }

    @FXML
    public void addMedicineFromInput()
    {
        String medicineAddString = medicineNameAddInput.getText();

        boolean disableButtonAdd = medicineAddString.isEmpty()
                || medicineAddString.trim().isEmpty();

        addButtonInput.setDisable(disableButtonAdd);

        medicine = medicineAddString;
    }

    @FXML public void CreateFromFile()
    {
        check = true;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        //fileChooser.showOpenDialog(null);
        File selectedFile = fileChooser.showOpenDialog(null);

        //Class with all of the functions needed
        functions function = new functions();

        //Adding database medications to hashmap of medications
        function.databaseAdd("meddatabase.csv");

        function.medlistAddFromFile(selectedFile);

        //Check medication list against database
        function.compare();

        //Check for empty ArrayLists
        function.isItEmpty();

        //Converts hashmap into arraylist to print
        function.convert();

        bobTheBuilder = medicineTableListView.getItems();

        toPrint = function.getToPrint();

        medicineTable = function.getFinalTableOfMedications();
        categoryTable = function.getFinalTableOfCategories();

        bobTheBuilder.clear();

        for (int i = 0; i < medicineTable.size(); i++)
        {
            bobTheBuilder.add(new MedicineTable(categoryTable.get(i),medicineTable.get(i)));
        }

    }

    @FXML
    public void handleKeyReleasedRemove()
    {
        String medicineRemoveString = medicineNameRemove.getText();
        String categoryRemoveString = categoryRemove.getText();

        boolean disableButtonRemove = medicineRemoveString.isEmpty()
                || medicineRemoveString.trim().isEmpty()
                || categoryRemoveString.isEmpty()
                || categoryRemoveString.trim().isEmpty();

        removeButton.setDisable(disableButtonRemove);

        medicine = medicineRemoveString;
        categoryRemoveText = categoryRemoveString;
    }

    @FXML
    public void handleKeyReleasedChange()
    {
        String medicineChangeString = medicineNameChange.getText();
        String categoryAddChangeString = categoryAddChange.getText();
        String categoryRemoveChangeString = categoryRemoveChange.getText();

        boolean disableButtonRemove = medicineChangeString.isEmpty()
                || medicineChangeString.trim().isEmpty()
                || categoryAddChangeString.isEmpty()
                || categoryAddChangeString.trim().isEmpty()
                || categoryRemoveChangeString.isEmpty()
                || categoryRemoveChangeString.trim().isEmpty();

        changeButton.setDisable(disableButtonRemove);

        medicine = medicineChangeString;
        categoryAddText = categoryAddChangeString;
        categoryRemoveText = categoryRemoveChangeString;
    }

    public void newWindow()
    {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("inputWindow.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1, 1000, 500));
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void CreateFromInput() throws IOException {
        //Class with all of the functions needed
        functions function = new functions();

        if (check)
        {
            function.setToPrint(toPrint);
        }

        function.setMedlist(tempTable);

        function.databaseAdd("meddatabase.csv");

        //Check medication list against database
        function.compare();

        //Check for empty ArrayLists
        function.isItEmpty();

        //Converts hashmap into arraylist to print
        function.convert();

        showTable(function);
    }

    public void addToTable(String input)
    {
        tempTable.add(input);
    }

    // Puts the table into a .txt file
    public void exportTablefunction(ActionEvent actionEvent)
    {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try {
                PrintWriter writer;
                writer = new PrintWriter(file);
                for (String name : toPrint.keySet())
                {
//                    assert writer != null;
                    writer.println(name + ": " + toPrint.get(name).toString());
                }
                writer.close();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    public void importTableFunction(ActionEvent actionEvent) {
        check = true;
        functions function = new functions();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Table File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        File selectedFile = fileChooser.showOpenDialog(null);

        Map<String, ArrayList<String>> tempMap = null;

        try (Scanner scanner = new Scanner(selectedFile)) {
            while (scanner.hasNextLine()) {
                String nextLine = scanner.nextLine().toLowerCase();

                // Splits the line to get a key and a string of medicine names
                String[] line = nextLine.split(": ", 2);

                // Splits string of medications into separate entries
                String[] medications = line[1].split(", ");

                // Removes [ from first entry
                medications[0] = medications[0].substring(1);

                // Index is saved for ease of readability
                int index = medications.length-1;

                // Removes ] from final entry
                medications[index] = medications[index].substring(0, medications[index].length()-1);

                // Begins adding to the medication list
                toPrint = function.getToPrint();
                toPrint.putIfAbsent(line[0], new ArrayList<>());

                for (String medicine: medications)
                {
                    toPrint.get(line[0]).add(medicine.toLowerCase());
                }

            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

        function.setToPrint(toPrint);

        function.convert();

        showTable(function);
    }

    private void showTable(functions function) {
        bobTheBuilder = medicineTableListView.getItems();

        medicineTable = function.getFinalTableOfMedications();
        categoryTable = function.getFinalTableOfCategories();

        bobTheBuilder.clear();

        for (int i = 0; i < medicineTable.size(); i++)
        {
            bobTheBuilder.add(new MedicineTable(categoryTable.get(i),medicineTable.get(i)));
        }
    }

}
