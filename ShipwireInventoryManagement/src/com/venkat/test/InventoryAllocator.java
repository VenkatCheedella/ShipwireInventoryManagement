package com.venkat.test;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

import com.venkat.datasource.Order;
import com.venkat.exceptions.InvalidProductInfoException;
import com.venkat.inventory.Inventory;

public class InventoryAllocator implements Callable<Boolean>{

	private BlockingQueue<Order> ordersQueue;
	private Inventory inventory;	
	
	/**
	 * Inventory allocator has access the the blokcing queue where the source streams add the order
	 * requests
	 * @param ordersQueue  blocking queue where it holds the order requests
	 * @param inventoryStockInfo information of existing product stock information
	 * @throws IOException if unable to access the file
	 * @throws InvalidProductInfoException if inventoryStockInformation doesn't hold the valid prodcut information
	 */
	public InventoryAllocator(BlockingQueue<Order> ordersQueue, 
			String inventoryStockInfo) throws IOException, InvalidProductInfoException{
		this.ordersQueue = ordersQueue;
		inventory = new Inventory();
		inventory.loadInventory(inventoryStockInfo);
	}
	
	
	/**
	 * Inventory allocator works on a independent thread. It takes the order from the queue and dispatches 
	 * to the inventory.
	 */
	@Override
	public Boolean call() throws Exception {
		Order currentOrder = null;
		while(true){	
			if(inventory.isInventoryEmpty())
				return true;
			currentOrder  = ordersQueue.take();	
			inventory.provideServiceToOrder(currentOrder);																
		}		
	}
}
