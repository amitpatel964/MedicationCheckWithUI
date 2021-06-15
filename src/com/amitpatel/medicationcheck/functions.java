package com.amitpatel.medicationcheck;

import javafx.scene.control.Button;

import java.io.File;
import java.util.*;
import java.nio.file.Paths;

public class functions {
    // Creating list to hold patient's list of medications
    private final ArrayList<String> medlist;
    // Creating hashmap to store medicine database
    private final HashMap<String, ArrayList<String>> database;
    // Creating hashmap to store final list
    private Map<String, ArrayList<String>> toPrint;
    // To print out final Hashmap as an ArrayList
    private ArrayList<String> finalTableOfMedications;
    private ArrayList<String> finalTableOfCategories;

    static Scanner input = new Scanner(System.in);

    public functions()
    {
        medlist = new ArrayList<>();
        database = new HashMap<>();
        toPrint = new TreeMap<>();
    }

    // Imports a table made by this program or with the same formatting
    private void importTable(File tableName)
    {
//        try (Scanner scanner = new Scanner(tableName)) {
//            while (scanner.hasNextLine()) {
//                String nextLine = scanner.nextLine().toLowerCase();
//
//                // Splits the line to get a key and a string of medicine names
//                String[] line = nextLine.split(": ", 2);
//
//                // Splits string of medications into separate entries
//                String[] medications = line[1].split(", ");
//
//                // Removes [ from first entry
//                medications[0] = medications[0].substring(1);
//
//                // Index is saved for ease of readability
//                int index = medications.length-1;
//
//                // Removes ] from final entry
//                medications[index] = medications[index].substring(0, medications[index].length()-1);
//
//                // Begins adding to the medication list
//                toPrint.putIfAbsent(line[0], new ArrayList<>());
//
//                for (String medicine: medications)
//                {
//                    toPrint.get(line[0]).add(medicine.toLowerCase());
//                }
//
//            }
//        } catch (Exception e) {
//            System.out.println("Error: " + e.getMessage());
//        }
    }

    // Adding medications to ArrayList of user's medications via file
    public void medlistAddFromFile(File inputMedlist) {
        try (Scanner scanner = new Scanner(inputMedlist)) {
            while (scanner.hasNextLine()) {
                String nextLine = scanner.nextLine();
                medlist.add(nextLine.toLowerCase());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Adding database medications to hashmap of medications
    public void databaseAdd(String inputDatabase) {
        String[] temp;
        try (Scanner scanner = new Scanner(Paths.get(inputDatabase))) {
            while (scanner.hasNextLine()) {
                String nextLine2 = scanner.nextLine();
                String toLower = nextLine2.toLowerCase();
                temp = toLower.split(",");
                database.putIfAbsent(temp[0], new ArrayList<>());

                for (int i = 1; i < temp.length; i++) {
                    database.get(temp[0]).add(temp[i]);
                }
            }

            // Catch all category if a medication does not match a category
            database.putIfAbsent("other", new ArrayList<>());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Check medication list against database
    public void compare() {
        // Check to see if medication needs to go in the "Other section"
        ArrayList<String> checkList = new ArrayList<>();
        for (String s : medlist) {
            // for (HashMap.Entry<String, ArrayList<String>> type: database.entrySet())
//            boolean check = false;
//            int counter = 0;
            for (String type : database.keySet()) {
                checkList = database.get(type);
                if (checkList.contains(s)) {
                    toPrint.putIfAbsent(type, new ArrayList<>());
                    toPrint.get(type).add(s);
//                    check = true;
//                    counter++;
                }
            }

//            if (counter > 1) {
//                whichCategory(s);
//            }

            // If no matching categories are found, the medication is put in the "other" category
//            if (!check) {
//                whatIsMedicineUsedFor(s);
//            }
        }
    }

    // If medication is not the csv file, this method allows the user to put it in a category of their own
    public void whatIsMedicineUsedFor(String medicine)
    {
        System.out.println(medicine + " was not found in the database. Do you know what it is used for? " +
                "Type yes if you know and then enter the category. Otherwise, type no");

        while (true)
        {
            String answer = input.nextLine().toLowerCase();
            if (answer.equals("yes"))
            {
                System.out.println("What is " + medicine + " used for?");
                String category = input.nextLine().toLowerCase();

                toPrint.putIfAbsent(category, new ArrayList<>());
                toPrint.get(category).add(medicine);
                break;
            }
            else if (answer.equals("no"))
            {
                toPrint.putIfAbsent("other", new ArrayList<>());
                toPrint.get("other").add(medicine);
                break;
            }
            System.out.println("Please type yes or no");
        }
    }

    // If a medication is found in multiple categories, this allows them to choose which ones they are using
    // the medication for
    public void whichCategory(String medicine)
    {
        Controller test = new Controller();
        System.out.println(medicine + " has multiple uses. Do you what you are using the medicine for? Type yes for " +
                "a category when prompted if you are using it for that, no if not, or not sure if you are unsure");
        for (String name : toPrint.keySet())
        {
            ArrayList<String> checker = toPrint.get(name);
            if (checker.contains(medicine))
            {
                label:
                while (true)
                {
                    System.out.print(name + ": ");
                    String choice = input.nextLine();
                    switch (choice.toLowerCase()) {
                        case "yes":
                            break label;
                        case "no":
                            checker.remove(medicine);
                            break label;
                        case "not sure":
                            ListIterator<String> iterator = checker.listIterator();
                            while (iterator.hasNext()) {
                                String nextLine = iterator.next();
                                if (nextLine.equals(medicine)) {
                                    iterator.set(medicine + " (unsure)");
                                }
                            }
                            break label;
                    }
                }
            }
        }
    }

    // Removes any empty categories from the final table
    public void isItEmpty()
    {
        Iterator<Map.Entry<String, ArrayList<String>>> iter = toPrint.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, ArrayList<String>> entry = iter.next();
            if(entry.getValue().isEmpty()){
                iter.remove();
            }
        }
    }

    // Print out final list
    public void convert() {
        finalTableOfCategories = new ArrayList<>();
        finalTableOfMedications = new ArrayList<>();
        for (String name : toPrint.keySet()) {
            finalTableOfCategories.add(name);
            finalTableOfMedications.add(toPrint.get(name).toString());
        }
    }

    // Puts the table into a .txt file
    public void outputFile() {
//        PrintWriter writer = null;
//        try {
//            writer = new PrintWriter("sorted_list.txt");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        System.out.println(toPrint.isEmpty());
//        for (String name : toPrint.keySet())
//        {
//            assert writer != null;
//            writer.println(name + ": " + toPrint.get(name).toString());
//            /*
//            writer.print(name + ": [");
//            ArrayList<String> list = toPrint.get(name);
//            for (int i = 0; i < list.size(); i++)
//            {
//                writer.print(list.get(i) + ", ");
//            }
//            writer.println("]");
//            */
//        }
//        assert writer != null;
//        writer.close();

    }

    // Prints the commandLine method intro
    public void commandLineIntro()
    {
        System.out.println("Type change if you want to change the category a medication is in");
        System.out.println("Type remove if you want to remove a medication from a category");
        System.out.println("Type add if you want to add a medication to a category");
        System.out.println("Type edit if you want to edit a medication name");
        System.out.println("Type print if you want print the final list of medications");
        System.out.println("Type file if you want to put the final list into a .txt file");
        System.out.println("Type stop to exit the program");
        System.out.println("Type help or repeat to repeat these commands");
    }

    public ArrayList<String> getFinalTableOfCategories() {
        return finalTableOfCategories;
    }

    public ArrayList<String> getFinalTableOfMedications() {
        return finalTableOfMedications;
    }

    public Map<String, ArrayList<String>> getToPrint() {
        return toPrint;
    }

    public void setMedlist(ArrayList<String> input)
    {
        medlist.addAll(input);
    }

    public ArrayList<String> getMedlist() {
        return medlist;
    }

    public void setToPrint(Map<String, ArrayList<String>> currentMap)
    {
        toPrint = currentMap;
    }
}
