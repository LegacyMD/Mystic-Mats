package com.paijan.ui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.paijan.core.Yield;
import com.paijan.util.Paths;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.binding.NumberBinding;
import javafx.beans.binding.NumberExpression;
import javafx.beans.binding.ObjectBinding;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

/**
 * Created by Maciej on 2016-05-02.
 */
public class ItemNodeFactory {
	private final BooleanExpression mBuyComponents, mBuyProduct, mTaxes;

	public ItemNodeFactory(BooleanExpression buyComponents, BooleanExpression buyProduct, BooleanExpression taxes) {
		mBuyComponents = buyComponents;
		mBuyProduct = buyProduct;
		mTaxes = taxes;
	}

	public Pane itemNode(Item item) {
		HBox result = new HBox();
		result.setSpacing(6.0);
		try {
			ImageView img = new ImageView(new Image(new FileInputStream(Paths.img_mats + item.getId() + ".png")));
			img.setFitHeight(Main.FONT.getSize()*2);
			img.setFitWidth(Main.FONT.getSize()*2);
			result.getChildren().add(img);
		} catch (FileNotFoundException e) { /*ok*/}
		Label label = new Label(item.getName());
		label.setFont(Main.FONT);
		result.setAlignment(Pos.CENTER_LEFT);
		result.getChildren().add(label);
		return result;
	}

	public VBox forgeComponentsNode(Item item) {
		VBox result = new VBox();
		ObservableList<Node> children = result.getChildren();
		for (com.paijan.core.Item.Component comp : item.getForgeInfo().components) {
			try {
				Item componentItem = Item.getItem(comp.id);
				Pane itemNode = itemNode(componentItem);
				children.add(itemNode);

				Label quantityLabel = new Label(Integer.toString(comp.quantity));
				quantityLabel.setFont(Main.FONT);
				quantityLabel.prefWidthProperty().set(Main.COMPONENT_QUANTITY_WIDTH);
				quantityLabel.setAlignment(Pos.CENTER_RIGHT);
				itemNode.getChildren().add(0, quantityLabel);

				PriceTag priceTag = new PriceTag(componentItem.getPriceBinding(mBuyComponents).multiply(comp.quantity));
				priceTag.setAlignment(Pos.CENTER);
				itemNode.getChildren().add(priceTag);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public Pane forgeSumNode(Item item) {
		PriceTag result = new PriceTag(item.getForgePriceBinding(mBuyComponents));
		result.setAlignment(Pos.CENTER);
		return result;
	}

	public Pane yieldNode(Item item) {
		Yield yield = item.getForgeInfo().yield;
		NumberExpression price = item.getPriceBinding(mBuyProduct);

		VBox result = new VBox();
		result.setAlignment(Pos.CENTER);
		result.getChildren().addAll(
				getYieldLabel("Minimum", yield.getMin(), price),
				getYieldLabel("Average", yield.getAvg(), price),
				getYieldLabel("Maximum", yield.getMax(), price)
		);
		return result;
	}
	private Pane getYieldLabel(String name, float qty, NumberExpression price) {
		Label firstLabel = new Label(String.format("%s: %s (", name, ((int)qty == qty) ? (int)qty : (Object) qty));
		Node node = new PriceTag(price.multiply(qty));
		Label lastLabel = new Label(")");
		return new HBox(firstLabel, node, lastLabel);
	}

	public Pane profitNode(Item item) {
		Yield yield = item.getForgeInfo().yield;
		NumberExpression product = item.getPriceBinding(mBuyProduct);
		NumberExpression forge = item.getForgePriceBinding(mBuyComponents);

		VBox result = new VBox();
		result.setAlignment(Pos.CENTER);
		result.getChildren().addAll(
				getProfitLabel("Minimum", yield.getMin(), product, forge),
				getProfitLabel("Average", yield.getAvg(), product, forge),
				getProfitLabel("Maximum", yield.getMax(), product, forge)
		);
		return result;
	}
	private Pane getProfitLabel(String name, float qty, NumberExpression priceProduct, NumberExpression priceForge) {
		Label label = new Label(name+": ");
		NumberBinding profit = priceProduct.multiply(qty).subtract(priceForge);
		ObjectBinding<Color> colorBinding = Bindings.when(profit.greaterThan(0)).then(Color.GREEN).otherwise(Color.RED);
		label.textFillProperty().bind(colorBinding);
		Node node = new PriceTag(profit);
		return new HBox(label, node);
	}
}
