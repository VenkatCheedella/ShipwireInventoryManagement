package com.venkat.datasource;

import java.util.List;

/**
 * 
 * @author venkat
 * Order is POJO to hold an order from stream. 
 * {"Header": 1, "Lines": {"Product": "A", "Quantity": "1"} -> Example of an order, 
 *  Here Lines are stored in a List
 *
 */
public class Order {
	
	private int id;
	private List<Line> listOfLines;	

	public Order(int id, List<Line> listOfLines) {		
		this.id = id;
		this.listOfLines = listOfLines;
	}

	public int getId() {
		return id;
	}

	public List<Line> getListOfLines() {
		return listOfLines;
	}
	
		
	
	
	public String convertLinesToString(){
		StringBuilder builder = new StringBuilder(listOfLines.size());
		for(Line line: listOfLines){
			builder.append("{ Product Type : " +  "\"" + line.getProductType() + 
					"\"" +  " , Qunatity : " + "\"" + line.getQunatity() + "\" }");
		}
		return builder.toString();
	}
	
	public String toString(){
		return "Header : "  + id + ", " + "Lines : " + convertLinesToString();
	}
}
