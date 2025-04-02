package fr.stockmate.java.utils;


import fr.stockmate.java.gui.GUI;

import javax.swing.*;

public class StockMate {
    public static void main(String[] args) {
        GUI gui = new GUI();
        SwingUtilities.invokeLater(() -> {
            gui.createAndShowGUI();
        });
    }
}
