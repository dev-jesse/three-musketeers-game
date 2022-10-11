package assignment1;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;
import java.util.ArrayList;

public class ModeInputPanel extends GridPane {
    private final View view;
    private final Controller controller;

    /**
     * Constructs a new GridPane with the main menu of the game, to select a game mode and load saved boards
     * @param view
     */
    public ModeInputPanel(View view, Controller controller) {
        this.view = view;
        this.controller = controller;
        this.setAlignment(Pos.CENTER);
        this.setVgap(10);

        view.setMessageLabel("Main Menu");

        createModeButtons();
        createListView();
        createSkinView();
    }

    /**
     * Creates the Game mode buttons
     */
    private void createModeButtons(){
        for (ThreeMusketeers.GameMode mode: ThreeMusketeers.GameMode.values()) {
            Button button = new Button(mode.getGameModeLabel());
            button.setId(mode.getGameModeLabel().replaceAll(" ", "")); // DO NOT MODIFY ID

            // Default styles which can be modified
            button.setPrefSize(500, 100);
            button.setFont(new Font(20));
            button.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
            //button.setOnAction(e -> this.controller.setSkinSelect(mode));
            button.setOnAction(e -> this.controller.setGameMode(mode));
            	
            this.add(button, 0, this.getRowCount());
        }
    }

    /**
     * Creates the ListView to select a board to load
     */
    private void createListView(){
        Label selectBoardLabel = new Label(String.format("Current board: %s", view.model.getBoardFile().getName()));
        selectBoardLabel.setId("CurrentBoard"); // DO NOT MODIFY ID

        ListView<String> boardsList = new ListView<>();
        boardsList.setId("BoardsList");  // DO NOT MODIFY ID

        boardsList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        //System.out.println(getBoardFiles(boardsList));

        int starterIndex = getBoardFiles(boardsList);
        boardsList.getSelectionModel().select(starterIndex);

        Button selectBoardButton = new Button("Change board");
        selectBoardButton.setId("ChangeBoard"); // DO NOT MODIFY ID

        selectBoardButton.setOnAction(e -> selectBoard(selectBoardLabel, boardsList));

        VBox selectBoardBox = new VBox(10, selectBoardLabel, boardsList, selectBoardButton);

        // Default styles which can be modified

        boardsList.setPrefHeight(100);

        selectBoardLabel.setStyle("-fx-text-fill: #e8e6e3");
        selectBoardLabel.setFont(new Font(16));

        selectBoardButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        selectBoardButton.setPrefSize(200, 50);
        selectBoardButton.setFont(new Font(16));

        selectBoardBox.setAlignment(Pos.CENTER);

        this.add(selectBoardBox, 0, this.getRowCount());
    }

    /**
     * Gets all the text files from the boards directory and puts them into the given listView
     * @param listView ListView to update
     * @return the index in the listView of Starter.txt
     */
    private int getBoardFiles(ListView<String> listView) {
    	int starter = 0;
    	File directory = new File("boards");
    	File [] files = directory.listFiles();
    	for (int i = 0; i < files.length; i++) {
    		if (files[i].getName().equals("Starter.txt")) {
    			starter = i;
    		}
    		listView.getItems().add(files[i].getName());
    	}
        return starter;
    }

    /**
     * Loads the board file selected in the boardsList and updates the selectBoardLabel with the name of the new Board file
     * @param selectBoardLabel a message Label to update which board is currently selected
     * @param boardsList a ListView populated with boards to load
     */
    private void selectBoard(Label selectBoardLabel, ListView<String> boardsList) {
    	System.out.println("selectBoard");
    	File directory = new File("boards");
    	for (File file: directory.listFiles()) {
    		if (file.getName().equals(boardsList.getSelectionModel().getSelectedItem())){
    			this.controller.setCurrentBoardFile(file);
    			this.controller.loadBoard(file); 
    		}
    	}
    	//updating selectBoardLabel
    	selectBoardLabel.setText(String.format("Current board: %s", boardsList.getSelectionModel().getSelectedItem()));
    }
    
    private void createSkinView() {
    	Label selectSkinLabel = new Label(String.format("Current Skin: %s", "Default"));
        selectSkinLabel.setId("Current Skin");
        
        ListView<String> skinsList = new ListView<>();
        skinsList.setId("SkinsList");
        
        skinsList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        int starterIndex = getSkinFiles(skinsList);
        
        skinsList.getSelectionModel().select(starterIndex);
        
        Button selectSkinButton = new Button("Change Skin");
        selectSkinButton.setId("ChangeSkin");
        
        selectSkinButton.setOnAction(e -> selectSkin(selectSkinLabel, skinsList));
        
        VBox selectSkinBox = new VBox(10, selectSkinLabel, skinsList, selectSkinButton);
        
        skinsList.setPrefHeight(100);
        
        selectSkinLabel.setStyle("-fx-text-fill: #e8e6e3");
        selectSkinLabel.setFont(new Font(16));
        
        selectSkinButton.setStyle("-fx-background-color: #17871b; -fx-text-fill: white;");
        selectSkinButton.setPrefSize(200, 50);
        selectSkinButton.setFont(new Font(16));
        
        selectSkinBox.setAlignment(Pos.CENTER);
        
        this.add(selectSkinBox, 0, this.getRowCount());
    }
    
    private int getSkinFiles(ListView<String> listView) {
    	int starter = 0;
    	File directory = new File("images");
    	File [] files = directory.listFiles();
    	for (int i = 0; i < files.length; i++) {
    		if (files[i].getName().equals("Starter.txt")) {
    			starter = i;
    		}
    		listView.getItems().add(files[i].getName());
    	}
        return starter;
    }
    
    private void selectSkin(Label selectSkinLabel, ListView<String> skinsList) {
    	File images = new File("images");
    	for (File file: images.listFiles()) {
    		if (file.getName().equals(skinsList.getSelectionModel().getSelectedItem())) {
    			this.controller.loadSkin(file.toString());
    		}
    	}
    	selectSkinLabel.setText(String.format("Current Skin: %s", skinsList.getSelectionModel().getSelectedItem()));
    }
}
