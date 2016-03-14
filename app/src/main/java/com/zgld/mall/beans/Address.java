package com.zgld.mall.beans;

import java.io.Serializable;

public class Address implements Serializable {
	int RegionId;
	int ShippingId;
	int UserId;
	String ShipTo;
	String Address;
	String Zipcode;
	String TelPhone;
	String CellPhone;
	String ShippingRegion;

	public int getRegionId() {
		return RegionId;
	}

	public void setRegionId(int regionId) {
		RegionId = regionId;
	}

	public int getShippingId() {
		return ShippingId;
	}

	public void setShippingId(int shippingId) {
		ShippingId = shippingId;
	}

	public int getUserId() {
		return UserId;
	}

	public void setUserId(int userId) {
		UserId = userId;
	}

	public String getShipTo() {
		return ShipTo;
	}

	public void setShipTo(String shipTo) {
		ShipTo = shipTo;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getZipcode() {
		return Zipcode;
	}

	public void setZipcode(String zipcode) {
		Zipcode = zipcode;
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

	public String getShippingRegion() {
		return ShippingRegion;
	}

	public void setShippingRegion(String shippingRegion) {
		ShippingRegion = shippingRegion;
	}

}
