package com.amitpatel.medicationcheck;

import javafx.beans.property.StringProperty;

import java.util.ArrayList;

public class MedicineTable {
    private String categories;
    private String medications;

    public MedicineTable(String categories, String medications) {
        this.categories = categories;
        this.medications = medications;
    }

    public MedicineTable() {
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public void setMedications(String medications) {
        this.medications = medications;
    }

    public String getCategories() {
        return categories;
    }

    public String getMedications() {
        return medications;
    }
}
