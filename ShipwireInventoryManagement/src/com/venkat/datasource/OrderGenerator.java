package com.venkat.datasource;

/**
 * Order is generated here, Number of lines and orderID is provided. With help of generateRandomProductTypes
 * from RandomProductTypes, it generates the number of types types. A list of lines are created. 
 * Lines are encapsulated in Order Object is dispatched
 */

import java.util.ArrayList;
import java.util.List;

import com.venkat.utility.RandomNumberGeneration;
import com.venkat.utility.RandomProductTypes;

public class OrderGenerator {
	
	public static Order getOrder(int numOfLines, int orderId){
		if(numOfLines > 6)
			numOfLines = 3;
		List<ProductTypes> productTypes = RandomProductTypes.generateRandomProductTypes(numOfLines);		// generates RandomProduct types.
		List<Line> lines = new ArrayList<>();
		for(int i=0; i< numOfLines; ++i){
			Line newLine = new Line(productTypes.get(i), RandomNumberGeneration.getRandomNumberInRange(1, 5));	// Random number is generated to specify the quantity of product
			lines.add(newLine);
		}
		return new Order(orderId, lines);
	}
}
