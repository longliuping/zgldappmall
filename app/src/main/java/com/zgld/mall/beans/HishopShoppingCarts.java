package com.zgld.mall.beans;

import java.sql.Timestamp;

/**
 * HishopShoppingCarts entity. @author MyEclipse Persistence Tools
 */
public class HishopShoppingCarts extends AbstractHishopShoppingCarts implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public HishopShoppingCarts() {
	}
	//产品信息
	HishopProducts hishopProducts;
	public HishopProducts getHishopProducts() {
		return hishopProducts;
	}
	public void setHishopProducts(HishopProducts hishopProducts) {
		this.hishopProducts = hishopProducts;
	}
	
	
}
