package com.paijan.ui;
/**
 * Created by Maciej on 2016-04-17.
 */


import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {
	public static final Font FONT = new Font(14.0);
	public static final double COMPONENT_QUANTITY_WIDTH = 30.0;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		RootTable root = new RootTable();
		populate(root);
		stage.setScene(new Scene(root, 930,1000));
		stage.setResizable(true);
		stage.setTitle("Mystic Mats");
		stage.show();

		stage.widthProperty().addListener((observable, oldValue, newValue) -> System.out.println(newValue.doubleValue()));
	}

	public void populate(TableView<Item> table) {
		Item.refreshCategory("T6");
		Item.refreshCategory("T5");
		List<Item> t6 = Item.getCategory("T6");
		List<Item> other = Item.getCategory("OTHER");
		table.getItems().addAll(t6);
	}
}
