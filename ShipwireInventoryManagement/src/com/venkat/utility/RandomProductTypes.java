package com.venkat.utility;

/**
 * @author venkat
 * 
 * RandomProductTypes are generated for a given quantity and the list is dispatched to the client
 */

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.venkat.datasource.ProductTypes;

public class RandomProductTypes {

	public static List<ProductTypes> generateRandomProductTypes(int quantity){
		List<ProductTypes> prodTypes = new ArrayList<>();
		HashSet<Integer> generatedIntegers = new HashSet<>();	
				
		/**
		 * Here product types should be unique, so, the list shouldn't have duplicate product types
		 * Hence below approach of creating a random number between 0 and the size of the product list
		 */
		while(quantity != 0){
			int generatedRandNum = RandomNumberGeneration.getRandomNumberInRange(0, sizeOfTypesOfProductTypes()-1);
			if(!generatedIntegers.contains(generatedRandNum)){				
				prodTypes.add(ProductTypes.values()[generatedRandNum]);
				generatedIntegers.add(generatedRandNum);
				--quantity;
			}			
		}			
		return prodTypes;
	}
	
	/**
	 * 
	 * @return returns the size of product types
	 */
	private static int sizeOfTypesOfProductTypes(){
		ProductTypes[] types = ProductTypes.values();
		if(types != null)
			return (types.length > 0) ? types.length : 0;
		else
			return 0;
	}
}
