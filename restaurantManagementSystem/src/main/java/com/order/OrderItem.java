package com.order;
public class OrderItem {
	private int orderItemID;
    private int customerID;
    private int itemID;
    private int quantity;
	public OrderItem( int customerID, int itemID, int quantity) {
		this.customerID = customerID;
		this.itemID = itemID;
		this.quantity = quantity;
	}
	
	/*
	public int getOrderItemID() {
		return orderItemID;
	}
	public void setOrderItemID(int orderItemID) {
		this.orderItemID = orderItemID;
	}
	*/
	public int getCustomerID() {
		return customerID;
	}
	public void setCustomerID(int customerID) {
		this.customerID = customerID;
	}
	public int getItemID() {
		return itemID;
	}
	public void setItemID(int itemID) {
		this.itemID = itemID;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	@Override
	public String toString() {
		return "customerID=" + customerID + ", itemID=" + itemID
				+ ", quantity=" + quantity+"\n";
	}
		
	
}
