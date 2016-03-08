package com.venkat.datasource;

/**
 * 
 * @author venkat
 * Java class to hold Line information, it stores the ProductType and Quantity of productType
 * {"Product": "C", "Quantity": "4"} -> This information is stored as Line object
 */

public class Line {
	private ProductTypes productType;
	private int Qunatity ;
			
	public Line(ProductTypes productType, int qunatity) {		
		this.productType = productType;
		Qunatity = qunatity;
	}
	public ProductTypes getProductType() {
		return productType;
	}
	public int getQunatity() {
		return Qunatity;
	}	
}
