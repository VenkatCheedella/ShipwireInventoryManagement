package com.venkat.inventory;

import java.io.IOException;

import com.venkat.datasource.Order;
import com.venkat.exceptions.InvalidProductInfoException;

public interface IInventory {
	public void provideServiceToOrder(Order order);
	public int reduceProductSizeFromInventoryForALine(String productId, int countOfItems);
	public boolean isInventoryEmpty();
	public void loadInventory(String inventoryInfo)throws IOException, InvalidProductInfoException;
}
