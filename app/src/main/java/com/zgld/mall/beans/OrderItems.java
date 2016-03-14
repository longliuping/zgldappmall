package com.zgld.mall.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OrderItems implements Serializable {
	public String getUsername() {
		return Username;
	}

	public void setUsername(String username) {
		Username = username;
	}

	public String getQQ() {
		return QQ;
	}

	public void setQQ(String QQ) {
		this.QQ = QQ;
	}

	public String getWangwang() {
		return Wangwang;
	}

	public void setWangwang(String wangwang) {
		Wangwang = wangwang;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getShippingRegion() {
		return ShippingRegion;
	}

	public void setShippingRegion(String shippingRegion) {
		ShippingRegion = shippingRegion;
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

	public String getOrderId() {
		return OrderId;
	}

	public void setOrderId(String orderId) {
		OrderId = orderId;
	}

	public String getAdjustedFreight() {
		return AdjustedFreight;
	}

	public void setAdjustedFreight(String adjustedFreight) {
		AdjustedFreight = adjustedFreight;
	}

	public String getOrderTotal() {
		return OrderTotal;
	}

	public void setOrderTotal(String orderTotal) {
		OrderTotal = orderTotal;
	}

	public String getPayDate() {
		return PayDate;
	}

	public void setPayDate(String payDate) {
		PayDate = payDate;
	}

	public String getIsPayBalancePayment() {
		return IsPayBalancePayment;
	}

	public void setIsPayBalancePayment(String isPayBalancePayment) {
		IsPayBalancePayment = isPayBalancePayment;
	}

	String Username;
		String QQ;
		String Wangwang;
		String Address;
		String ShippingRegion;
		String ShipTo;
		String TelPhone;
		String CellPhone;
		String OrderId;
		String AdjustedFreight;
		String OrderTotal;
		String PayDate;
		String IsPayBalancePayment;


}
