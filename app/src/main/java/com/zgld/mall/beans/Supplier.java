package com.zgld.mall.beans;

/**
 * Supplier entity. @author MyEclipse Persistence Tools
 */
public class Supplier extends AbstractSupplier implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public Supplier() {
	}

	/** minimal constructor */
	public Supplier(String supplierName) {
		super(supplierName);
	}

	/** full constructor */
	public Supplier(Integer userId, String supplierName, String comName,
			String supplierLogoUrl, String supplierDescribe,
			String supplierUrl1, String supplierUrl2, String supplierUrl3,
			String supplierUrl4, String businessLicenseNo,
			String businessLicenseImgUrl, Integer isExamine, String extraStr1,
			String extraStr2, String extraStr3, Integer supplierOrder,
			Integer clickNum, Integer commentNum, String idCard,
			String idCardImgUrl, String linkMan, String linkPhone,
			Integer supplierRegionId, String qq, String shopMain,
			String shopAddress, String returnAddress, String bankOfDeposit,
			String bankOfDepositNo, Integer examineStuart, String remark) {
		super(userId, supplierName, comName, supplierLogoUrl, supplierDescribe,
				supplierUrl1, supplierUrl2, supplierUrl3, supplierUrl4,
				businessLicenseNo, businessLicenseImgUrl, isExamine, extraStr1,
				extraStr2, extraStr3, supplierOrder, clickNum, commentNum,
				idCard, idCardImgUrl, linkMan, linkPhone, supplierRegionId, qq,
				shopMain, shopAddress, returnAddress, bankOfDeposit,
				bankOfDepositNo, examineStuart, remark);
	}

}
