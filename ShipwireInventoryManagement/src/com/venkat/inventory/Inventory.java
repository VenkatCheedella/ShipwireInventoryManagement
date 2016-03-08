package com.venkat.inventory;

/**
 * 
 * Inventory class has core functioning of the Inventory management. It loads the inventory information from
 * a source. It provides service to the order request. Based on the order request, the existing inventory
 *  size is modified.
 *  
 * @author venkat
 *
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.venkat.datasource.Line;
import com.venkat.datasource.Order;
import com.venkat.exceptions.InvalidProductInfoException;
import com.venkat.response.InventoryServiceResponse;


public class Inventory {
	
	private Map<String, Integer> existingItems;				// holds the inventory information
	private Set<Integer> serviceCompletedOrders;			// holds the orders Ids of the Orders where the service is provided successfully 
	
	public Inventory(){
		existingItems = new HashMap<>();	
		serviceCompletedOrders = new HashSet<>();
	}
	
	
	/**
	 * For an order from a stream, inventory allocates the products as request. It checks if the requested quantity 
	 * of product is present. If present, products are alloted to the order else, the line is discarded
	 * @param order order from stream
	 */
	public void provideServiceToOrder(Order order){		
		Map<String, Integer> requestedOrder = new HashMap<>();			// As output should have information of requested order
		Map<String, Integer> allotedOrder = new HashMap<>();			// alloted order and discarded order. Respective line information is stored
		Map<String, Integer> discardedOrder = new HashMap<>();			// in the respective data structures
		List<Line> currentOrderLines = order.getListOfLines();			
		if(serviceCompletedOrders.contains(order.getId()))
			return;
		serviceCompletedOrders.add(order.getId());
		for(Line currLine : currentOrderLines){
			String prodType = currLine.getProductType().toString();
			int quantity = currLine.getQunatity();
			int isAllocated = reduceProductSizeFromInventoryForALine(prodType, quantity);
			requestedOrder.put(prodType, quantity);
			if(isAllocated != 0){								// If line requested quantity of products are available ? If yes, alloted order map is filled
				allotedOrder.put(prodType, quantity);
				discardedOrder.put(prodType, 0);
			}else{
				allotedOrder.put(prodType, 0);
				discardedOrder.put(prodType, quantity);
			}
		}
		List<Map<String, Integer>> orderAllocationStatus = new ArrayList<>();
		orderAllocationStatus.add(requestedOrder);
		orderAllocationStatus.add(allotedOrder);
		orderAllocationStatus.add(discardedOrder);
		InventoryServiceResponse.addOrderDeliveryEntry(order.getId(), orderAllocationStatus);		// orderDelivery information holds the order service details
	}
	
	/**
	 * It checks whether requested quantity of products are present for a given line. If present is returns
	 * count of items requested else it returns 0
	 * @param productId product type
	 * @param countOfItems quantity of product
	 * @return
	 */
	public int reduceProductSizeFromInventoryForALine(String productId, int countOfItems){
		Integer existingProductsCount = existingItems.get(productId);
		if(existingProductsCount == null)
			return 0;
		if(existingProductsCount == 0){
			existingItems.remove(productId);
		}
		if(existingProductsCount < countOfItems)
			return 0;
		existingItems.replace(productId, existingProductsCount, existingProductsCount-countOfItems);
		return countOfItems;
	}
	
	/**
	 * Gives the size of the existing repository
	 * @return
	 */
	public int getExistingItemsSize() {
		return existingItems.size();
	}
	
	/**
	 * checks if repository is empty. If it is empty, it returns true
	 * @return
	 */
	public boolean isInventoryEmpty(){
		if(getExistingItemsSize() == 0)
			return true;
		return false;
	}

	/**
	 * It loads the inventory information
	 * @param inventoryInfo -> file name of inventory
	 * @throws IOException	-> if unable to open the file , this exception is thrown
	 * @throws InvalidProductInfoException -> If the file doesn't have product information in the expected format
	 */
	public void loadInventory(String inventoryInfo) throws IOException, InvalidProductInfoException{		
		File file = new File(inventoryInfo);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String readLine;
		try{
			while((readLine = reader.readLine()) != null){
				String[] productInfo = readLine.split("\\s");
				if(productInfo != null && productInfo.length==2){
					String productId = productInfo[0];
					Integer countOfProduct = Integer.valueOf(productInfo[1]);				
					if(productId != null && countOfProduct != null)
						existingItems.put(productId, countOfProduct);
				}
				else 
					throw new InvalidProductInfoException("ProductInfo input format is invalid");
			}
		}
		finally{
			reader.close();	
		}		
	}	
}
