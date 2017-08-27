package com.paijan.ui;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * Created by Maciej on 2016-04-29.
 */
public class RootTable extends TableView<Item> {
	private final BuySellComboBox componentsComboBox, productComboBox;
	private final CheckBox taxesCheckBox;
	private final ItemNodeFactory nodeFactory;

	public RootTable() {
		componentsComboBox = new BuySellComboBox();
		productComboBox = new BuySellComboBox();
		taxesCheckBox = new CheckBox("Include taxes");
		nodeFactory = new ItemNodeFactory(componentsComboBox.getIsBuyBinding(), productComboBox.getIsBuyBinding(), taxesCheckBox.selectedProperty());

		setFocusTraversable(false); //to disable row selection
		initColumns();
	}

	private void initColumns() {
		initColumnMaterial();
		initColumnComponents();
		initColumnProduct();
		initColumnProfit();
	}

	//------------------------------------------------------------ Init columns

	private void initColumnMaterial() {
		TableColumn<Item,Node> column = new TableColumn<>("Material");
		column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(nodeFactory.itemNode(param.getValue())));
		getColumns().add(column);
	}

	private void initColumnComponents() {
		TableColumn<Item, Object> column = new TableColumn<>("components");
		column.setGraphic(componentsComboBox);
		getColumns().add(column);

		TableColumn<Item, Node> columnIndividual = new TableColumn<>("Individual");
		columnIndividual.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(nodeFactory.forgeComponentsNode(param.getValue())));
		column.getColumns().add(columnIndividual);

		TableColumn<Item, Node> columnSum = new TableColumn<>("Sum");
		columnSum.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(nodeFactory.forgeSumNode(param.getValue())));
		column.getColumns().add(columnSum);
	}

	private void initColumnProduct() {
		TableColumn<Item, Node> column = new TableColumn<>("product");
		column.setGraphic(productComboBox);
		column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(nodeFactory.yieldNode(param.getValue())));
		getColumns().add(column);
	}

	private void initColumnProfit() {
		TableColumn<Item, Node> column = new TableColumn<>("Profit");
		column.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(nodeFactory.profitNode(param.getValue())));
		getColumns().add(column);
	}

	//------------------------------------------------------------

	private static class BuySellComboBox extends ComboBox<String> {
		public BuySellComboBox() {
			super(FXCollections.observableArrayList("Buy", "Sell"));
			getSelectionModel().select(0);
		}

		public BooleanBinding getIsBuyBinding() {
			return Bindings.equal(getSelectionModel().selectedItemProperty().asString(), "Buy");
		}
	}
	//------------------------------------------------------------
}
