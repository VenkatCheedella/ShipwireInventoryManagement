package com.venkat.datasource;

import java.util.List;

public interface IOrder {
	public int getId();
	public List<Line> getListOfLines();
}
