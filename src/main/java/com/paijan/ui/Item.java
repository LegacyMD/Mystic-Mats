package com.paijan.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.paijan.util.Paths;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.binding.NumberBinding;
import javafx.beans.binding.NumberExpression;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * Created by Maciej on 2016-04-26.
 * Implementation of {@link com.paijan.core.Item} adding javaFX properties
 */
public class Item extends com.paijan.core.Item {
	//------------------------------------------------------------ Static
	private static Map<Integer,Item> map = new HashMap<>();
	static {
		try (Scanner scanner = new Scanner(new File(Paths.items))) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if(line.length()>0 && !line.startsWith("//")) {
					Item item = new Item(line);
					map.put(item.getId(), item);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void refreshCategory(String category) {
		for (Map.Entry<Integer,Item> entry : map.entrySet()) {
			Item item = entry.getValue();
			if (item.getCategory().equals(category)) item.refreshPrice();
		}
	}
	public static Item getItem(int id) {
		return map.get(id);
	}
	public static List<Item> getCategory(String category) {
		ArrayList<Item> result = new ArrayList<>();
		for (Map.Entry<Integer,Item> entry : map.entrySet()) {
			Item value = entry.getValue();
			if (value.getCategory().equals(category)) result.add(value);
		}
		return result;
	}

	//------------------------------------------------------------ Instance

	private final IntegerProperty mBuy = new SimpleIntegerProperty(0);
	private final IntegerProperty mSell = new SimpleIntegerProperty(0);
	public Item(String line) {
		super(line);
	}

	//------------------------------------------------------------ Accessors

	@Override
	public void setPrice(int buy, int sell) {
		mBuy.set(buy);
		mSell.set(sell);
	}

	public NumberExpression getPriceBinding(BooleanExpression isBuy) {
		return Bindings.when(isBuy).then(mBuy).otherwise(mSell);
	}

	public NumberExpression getForgePriceBinding(BooleanExpression isBuy) {
		Component[] components = getForgeInfo().components;
		NumberExpression result = new SimpleIntegerProperty(0);
		for(Component comp : components) {
			Item item = Item.getItem(comp.id);
			NumberBinding itemPrice = item.getPriceBinding(isBuy).multiply(comp.quantity);
			result = result.add(itemPrice);
		}
		return result;
	}

	//------------------------------------------------------------
}
