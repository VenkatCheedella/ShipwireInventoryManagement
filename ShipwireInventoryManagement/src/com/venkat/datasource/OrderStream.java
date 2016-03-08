package com.venkat.datasource;

/**
 * System can have more than a stream and each stream generates and places the orders in Blocking Queue(BQ). 
 * BQ is shared with InventoryAllocator. 
 */

import java.util.concurrent.BlockingQueue;

import com.venkat.utility.RandomNumberGeneration;

public class OrderStream implements Runnable{

	private BlockingQueue<Order> orders;				// To place the orders generated from stream
	private boolean stopFlag;
	private int producerId;

	public OrderStream(BlockingQueue<Order> orders, int producerId) {		
		this.orders = orders;
		this.stopFlag = false;
		this.producerId = producerId;
	}

	public void setStopFlag(){
		this.stopFlag =true;
	}
	
	
	public int getProducerId() {
		return producerId;
	}

	@Override
	public void run() {
		Order order;
		int orderId=1;		
		while(true){					
			try {
				if(stopFlag)
					break;
				order = OrderGenerator.getOrder(RandomNumberGeneration.getRandomNumberInRange(1, 5), orderId);
				orders.put(order);								// Orders are generated and placed in the queue								
				++orderId;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
	}

}
