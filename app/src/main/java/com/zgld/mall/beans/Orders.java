package com.zgld.mall.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;

public class Orders implements Serializable {
	private String OrderId;//订单号
	private int OrderStatus;//订单状态
	private String OrderDate;//订单日期
	private String PayDate;//购买日期

	public String getBuyerUserId() {
		return BuyerUserId;
	}

	public void setBuyerUserId(String buyerUserId) {
		BuyerUserId = buyerUserId;
	}

	public String getOrderId() {
		return OrderId;
	}

	public void setOrderId(String orderId) {
		OrderId = orderId;
	}

	public int getOrderStatus() {
		return OrderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		OrderStatus = orderStatus;
	}

	public String getOrderDate() {
		return OrderDate;
	}

	public void setOrderDate(String orderDate) {
		OrderDate = orderDate;
	}

	public String getPayDate() {
		return PayDate;
	}

	public void setPayDate(String payDate) {
		PayDate = payDate;
	}

	public String getShippingRegion() {
		return ShippingRegion;
	}

	public void setShippingRegion(String shippingRegion) {
		ShippingRegion = shippingRegion;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getShipTo() {
		return ShipTo;
	}

	public void setShipTo(String shipTo) {
		ShipTo = shipTo;
	}

	public String getTelPhone() {
		return TelPhone;
	}

	public void setTelPhone(String telPhone) {
		TelPhone = telPhone;
	}

	public String getCellPhone() {
		return CellPhone;
	}

	public void setCellPhone(String cellPhone) {
		CellPhone = cellPhone;
	}

	public String getAdjustedFreight() {
		return AdjustedFreight;
	}

	public void setAdjustedFreight(String adjustedFreight) {
		AdjustedFreight = adjustedFreight;
	}

	public int getRefundStatus() {
		return RefundStatus;
	}

	public void setRefundStatus(int refundStatus) {
		RefundStatus = refundStatus;
	}

	public String getOrderTotal() {
		return OrderTotal;
	}

	public void setOrderTotal(String orderTotal) {
		OrderTotal = orderTotal;
	}

	public String getAmount() {
		return Amount;
	}

	public void setAmount(String amount) {
		Amount = amount;
	}

	public String getPaymentTypeId() {
		return PaymentTypeId;
	}

	public void setPaymentTypeId(String paymentTypeId) {
		PaymentTypeId = paymentTypeId;
	}

	public String getPaymentType() {
		return PaymentType;
	}

	public void setPaymentType(String paymentType) {
		PaymentType = paymentType;
	}

	public String getUsername() {
		return Username;
	}

	public void setUsername(String username) {
		Username = username;
	}

	public String getOwnerUserId() {
		return OwnerUserId;
	}

	public void setOwnerUserId(String ownerUserId) {
		OwnerUserId = ownerUserId;
	}

	public String getSupplierName() {
		return SupplierName;
	}

	public void setSupplierName(String supplierName) {
		SupplierName = supplierName;
	}

	public String getIsPayBalancePayment() {
		return IsPayBalancePayment;
	}

	public void setIsPayBalancePayment(String isPayBalancePayment) {
		IsPayBalancePayment = isPayBalancePayment;
	}

	public String getSecondTimePayNo() {
		return SecondTimePayNo;
	}

	public void setSecondTimePayNo(String secondTimePayNo) {
		SecondTimePayNo = secondTimePayNo;
	}

	public List<com.zgld.mall.beans.OrderItems> getOrderItems() {
		return OrderItems;
	}

	public void setOrderItems(List<com.zgld.mall.beans.OrderItems> orderItems) {
		OrderItems = orderItems;
	}

	private String BuyerUserId;//购买者用户Id
	private String ShippingRegion;//购买者的地区
	private String Address;//详细地址
	private String ShipTo;//购买人的姓名
	private String TelPhone;//座机电话
	private String CellPhone;//手机号码
	private String AdjustedFreight;//
	private int RefundStatus;//预付状态
	private String OrderTotal;//
	private String Amount;//
	private String PaymentTypeId;
	private String PaymentType;
	private String Username;//用户名称(手机号码)
	private String OwnerUserId;//用户id
	private String SupplierName;//
	private String IsPayBalancePayment;
	private String SecondTimePayNo;
	List<OrderItems> OrderItems = new ArrayList<OrderItems>();

}
