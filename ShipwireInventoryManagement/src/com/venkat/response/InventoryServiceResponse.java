package com.venkat.response;

/**
 * Service details of each order is stored in a order delivery information data store. When user requests the order service details, it 
 * returns in the string format of the service details of each order
 */

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.venkat.datasource.ProductTypes;

public class InventoryServiceResponse {
	private static Map<Integer, List<Map<String, Integer>>> orderDeliveryInformation;		// holds the orders for which service is provided. It holds requested lines, allocated lines and discarded lines
	static{
		orderDeliveryInformation = new LinkedHashMap<>();
	}
	
	/**
	 * puts the valid order for which service is provided
	 * @param orderId Unique reference of the id
	 * @param orderDeliveryEntry , entry of the service details of the order
	 */
	public static void addOrderDeliveryEntry(Integer orderId, List<Map<String, Integer>> orderDeliveryEntry){
		orderDeliveryInformation.put(orderId, orderDeliveryEntry);
	}
	
	/**
	 * Get the list of products that are present in the inventory
	 * @return list of product types
	 */
	private String[] getProductTypes(){
		ProductTypes[] prodTypes = ProductTypes.values();
		String[] products = new String[prodTypes.length];
		for(int i=0; i< prodTypes.length; ++i){
			products[i] = prodTypes[i].toString();
		}
		return products;
	}
	
	/**
	 * Each order service holds the requested lines, allocated lines and the discarded lines
	 * @param orderInfo service information of order
	 * @return string representation of the service information of an order
	 */
	public String retrieveOrderInfo(List<Map<String, Integer>> orderInfo){
		String[] listOfProducts = getProductTypes();
		StringBuilder orderInfoInString = new StringBuilder();
		for(Map<String, Integer> orderEntry : orderInfo){
			for(String product : listOfProducts){
				if(orderEntry.containsKey(product)){
					orderInfoInString.append(orderEntry.get(product).toString() + ",");
				}else{
					orderInfoInString.append("0,");
				}
			}
			orderInfoInString.append("::");
		}
		orderInfoInString.delete(orderInfoInString.length()-3, orderInfoInString.length());
		return orderInfoInString.toString();
	}
	
	/**
	 * Representing the service details of each order in string format
	 */
	public String toString(){
		Set<Integer> orderIds = orderDeliveryInformation.keySet();
		StringBuilder builder = new StringBuilder();	
		builder.append("\nOrder# " + " ReqOrder " + "    Alloted " + "   ExcessOrder \n");
		for(Integer orderId: orderIds){
			builder.append("   " + orderId.toString() + "   " + retrieveOrderInfo(orderDeliveryInformation.get(orderId)));			
			builder.append("\n");
		}
		return builder.toString();
	}
	
}
