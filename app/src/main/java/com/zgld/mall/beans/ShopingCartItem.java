package com.zgld.mall.beans;

import java.io.Serializable;

public class ShopingCartItem implements Serializable {
	String SupplierId;
	String AddTime;
	int UserId;
	String ProductId;
	String Quantity = "1";
	String ProductName;
	String ShortDescription;
	String ImageUrl1;
	String ThumbnailUrl220;
	int SaleCounts;
	int VistiCounts;
	String SalePrice;
	String MarketPrice;
	String putawayType;
	String TypeName;
	String BrandName;
	String SupplierName;
	String Postage;
	String OwnerUserId;
	int Stock;
	String Norms;
	int row_num;
	boolean checked;
	String remark;
	String CartsId;
	String ProductPutawayType;

	public String getSupplierId() {
		return SupplierId;
	}

	public void setSupplierId(String supplierId) {
		SupplierId = supplierId;
	}

	public String getAddTime() {
		return AddTime;
	}

	public void setAddTime(String addTime) {
		AddTime = addTime;
	}

	public int getUserId() {
		return UserId;
	}

	public void setUserId(int userId) {
		UserId = userId;
	}

	public String getProductId() {
		return ProductId;
	}

	public void setProductId(String productId) {
		ProductId = productId;
	}

	public String getQuantity() {
		return Quantity;
	}

	public void setQuantity(String quantity) {
		Quantity = quantity;
	}

	public String getProductName() {
		return ProductName;
	}

	public void setProductName(String productName) {
		ProductName = productName;
	}

	public String getShortDescription() {
		return ShortDescription;
	}

	public void setShortDescription(String shortDescription) {
		ShortDescription = shortDescription;
	}

	public int getSaleCounts() {
		return SaleCounts;
	}

	public void setSaleCounts(int saleCounts) {
		SaleCounts = saleCounts;
	}

	public int getVistiCounts() {
		return VistiCounts;
	}

	public void setVistiCounts(int vistiCounts) {
		VistiCounts = vistiCounts;
	}

	public String getSalePrice() {
		return SalePrice;
	}

	public void setSalePrice(String salePrice) {
		SalePrice = salePrice;
	}

	public String getMarketPrice() {
		return MarketPrice;
	}

	public void setMarketPrice(String marketPrice) {
		MarketPrice = marketPrice;
	}

	public String getPutawayType() {
		return putawayType;
	}

	public void setPutawayType(String putawayType) {
		this.putawayType = putawayType;
	}

	public String getTypeName() {
		return TypeName;
	}

	public void setTypeName(String typeName) {
		TypeName = typeName;
	}

	public String getBrandName() {
		return BrandName;
	}

	public void setBrandName(String brandName) {
		BrandName = brandName;
	}

	public String getSupplierName() {
		return SupplierName;
	}

	public void setSupplierName(String supplierName) {
		SupplierName = supplierName;
	}

	public int getRow_num() {
		return row_num;
	}

	public void setRow_num(int row_num) {
		this.row_num = row_num;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public String getThumbnailUrl220() {
		return ThumbnailUrl220;
	}

	public void setThumbnailUrl220(String thumbnailUrl220) {
		ThumbnailUrl220 = thumbnailUrl220;
	}

	public String getImageUrl1() {
		return ImageUrl1;
	}

	public void setImageUrl1(String imageUrl1) {
		ImageUrl1 = imageUrl1;
	}

	public int getStock() {
		return Stock;
	}

	public void setStock(int stock) {
		Stock = stock;
	}

	public String getNorms() {
		return Norms;
	}

	public void setNorms(String norms) {
		Norms = norms;
	}

	public String getPostage() {
		return Postage;
	}

	public void setPostage(String postage) {
		Postage = postage;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getOwnerUserId() {
		return OwnerUserId;
	}

	public void setOwnerUserId(String ownerUserId) {
		OwnerUserId = ownerUserId;
	}

	public String getCartsId() {
		return CartsId;
	}

	public void setCartsId(String cartsId) {
		CartsId = cartsId;
	}

	public String getProductPutawayType() {
		return ProductPutawayType;
	}

	public void setProductPutawayType(String productPutawayType) {
		ProductPutawayType = productPutawayType;
	}

}
