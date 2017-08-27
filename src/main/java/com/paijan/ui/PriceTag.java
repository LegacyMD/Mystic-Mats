package com.paijan.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.paijan.util.Paths;
import javafx.beans.binding.NumberExpression;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Created by Maciej on 2016-04-28.
 */
public class PriceTag extends HBox {
	private enum Currency {
		gold(Paths.img_currency + "gold.png"),
		silver(Paths.img_currency + "silver.png"),
		copper(Paths.img_currency + "copper.png");
		Currency(String path) {
			try {
				mImage = new Image(new FileInputStream(new File(path)));
			} catch (FileNotFoundException e) {
				mImage = null;
			}
		}

		private Image mImage;
		public Image getImage() {
			return mImage;
		}
		public static Currency ofOrdinal(int ordinal) {
			return Currency.values()[ordinal];
		}
	}

	private final NumberExpression mPriceBinding;
	private final ImageView[] mImageViews = new ImageView[3];
	private final Label[] mLabels = new Label[3];

	public PriceTag(NumberExpression binding) {
		mPriceBinding = binding;
		mPriceBinding.addListener((observable, oldValue, newValue) -> refresh());

		ObservableList<Node> children = getChildren();
		for (int i = 0; i < 3; i++) {
			mImageViews[i] = new ImageView(Currency.ofOrdinal(i).getImage());
			mLabels[i] = new Label("0");
			children.addAll(mImageViews[i], mLabels[i]);
		}

		refresh();
	}

	private void refresh() {
		ObservableList<Node> children = getChildren();
		children.clear();

		int price = Math.abs(mPriceBinding.getValue().intValue());

		int gold = (price/10000);
		int silver = (price/100)%100;
		int copper = price%100;

		if (gold > 0) {
			add(gold, Currency.gold);
			add(silver, Currency.silver);
		} else if(silver > 0) {
			add(silver, Currency.silver);
		}
		add(copper, Currency.copper);
	}

	private void add(int quantity, Currency currency) {
		Label label = mLabels[currency.ordinal()];
		label.textProperty().set(Integer.toString(quantity));
		ImageView imageView = mImageViews[currency.ordinal()];
		getChildren().addAll(label,imageView);
	}
}
