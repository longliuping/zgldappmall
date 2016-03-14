package com.zgld.mall.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ShopingCar implements Serializable {
	String BrandName;
	String SupplierId;
	String SupplierName;
	String OwnerUserId;
	String shopingCar;
	List<ShopingCartItem> cartItems = new ArrayList<ShopingCartItem>();
	boolean checked;

	public String getBrandName() {
		return BrandName;
	}

	public void setBrandName(String brandName) {
		BrandName = brandName;
	}

	public String getSupplierId() {
		return SupplierId;
	}

	public void setSupplierId(String supplierId) {
		SupplierId = supplierId;
	}

	public String getSupplierName() {
		return SupplierName;
	}

	public void setSupplierName(String supplierName) {
		SupplierName = supplierName;
	}

	public String getOwnerUserId() {
		return OwnerUserId;
	}

	public void setOwnerUserId(String ownerUserId) {
		OwnerUserId = ownerUserId;
	}

	public List<ShopingCartItem> getCartItems() {
		return cartItems;
	}

	public void setCartItems(List<ShopingCartItem> cartItems) {
		this.cartItems = cartItems;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getShopingCar() {
		return shopingCar;
	}

	public void setShopingCar(String shopingCar) {
		this.shopingCar = shopingCar;
	}

}
