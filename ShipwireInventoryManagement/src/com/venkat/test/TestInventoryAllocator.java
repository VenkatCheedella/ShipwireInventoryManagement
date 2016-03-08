package com.venkat.test;

/**
 * Test class to test whether end to end flow is working as expected. Inventory Allocator , each stream 
 * runs of an independent thread. Streams adds the orders to blocking queue. Inventory Allocator dispatches
 * the order to inventory for service
 * 
 *  
 * @author venkat cheedella
 **/

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import com.venkat.datasource.Order;
import com.venkat.datasource.OrderStream;
import com.venkat.exceptions.InvalidProductInfoException;
import com.venkat.response.InventoryServiceResponse;



public class TestInventoryAllocator {

	public static void main(String[] args) {
		try {			
			ExecutorService threadPool = Executors.newFixedThreadPool(3);			// Thread pool is generated to run threads for Inventory Management, Order streams
			BlockingQueue<Order> inputQueue = new ArrayBlockingQueue<>(10);			// Holds the orders, shared between the inventory allocator and object streams	
			OrderStream producer1 = new OrderStream(inputQueue, 1);
			OrderStream producer2 = new OrderStream(inputQueue, 2);				
			InventoryAllocator orderConsumer = new InventoryAllocator(inputQueue, "src/inventoryinfo.txt");
			Future<Boolean> inventoryManagementStatus = threadPool.submit(orderConsumer);	// If inventory is empty, inventory allocator stop providing the service
			threadPool.submit(producer1);											
			threadPool.submit(producer2);
			if(inventoryManagementStatus.get()){										// When the inventory allocator thread returns, a poison pill is triggered for order streams
				producer1.setStopFlag();
				producer2.setStopFlag();
				inputQueue.clear();								
			}
			InventoryServiceResponse inventoryServiceResponse = new InventoryServiceResponse();
			System.out.println(inventoryServiceResponse.toString());	
			threadPool.shutdown();														
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidProductInfoException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

	}

}
