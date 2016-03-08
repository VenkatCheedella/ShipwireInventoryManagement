package com.venkat.inventory;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.venkat.datasource.Order;
import com.venkat.datasource.OrderGenerator;
import com.venkat.exceptions.InvalidProductInfoException;
import com.venkat.response.InventoryServiceResponse;
import com.venkat.test.InventoryAllocator;
import com.venkat.utility.RandomNumberGeneration;

class OrderProducer implements Runnable{

	private BlockingQueue<Order> orders;
	private boolean stopFlag;
	private int producerId;

	public OrderProducer(BlockingQueue<Order> orders, int producerId) {		
		this.orders = orders;
		this.stopFlag = false;
		this.producerId = producerId;
	}

	public void setStopFlag(){
		this.stopFlag =true;
	}
	
	@Override
	public void run() {
		Order order;
		int orderId=1;
		//order = OrderGenerator.getOrder(RandomNumberGeneration.getRandomNumber(6), orderId);
		while(true){					
			try {
				if(stopFlag)
					break;
				order = OrderGenerator.getOrder(RandomNumberGeneration.getRandomNumberInRange(1, 5), orderId);
				orders.put(order);										
				System.out.println("Request added from " + producerId);
				++orderId;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
	}

}


public class TestInventoryAllocator {

	public static void main(String[] args) {					
		try {			
			ExecutorService threadPool = Executors.newFixedThreadPool(3);
			BlockingQueue<Order> inputQueue = new ArrayBlockingQueue<>(10);				
			OrderProducer producer1 = new OrderProducer(inputQueue, 1);
			OrderProducer producer2 = new OrderProducer(inputQueue, 2);				
			InventoryAllocator orderConsumer = new InventoryAllocator(inputQueue, "src/inventoryinfo.txt");
			Future<Boolean> inventoryManagementStatus = threadPool.submit(orderConsumer);	
			threadPool.submit(producer1);
			threadPool.submit(producer2);
			if(inventoryManagementStatus.get()){
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
