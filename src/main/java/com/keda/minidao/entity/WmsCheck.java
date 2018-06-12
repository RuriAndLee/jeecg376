package com.keda.minidao.entity;

// Generated 2018-6-7 15:13:48 by Hibernate Tools 3.4.0.CR1

import java.util.Date;

/**
 * WmsCheck generated by hbm2java
 */
public class WmsCheck implements java.io.Serializable {

	private Integer id;
	private String createName;
	private String createBy;
	private Date createDate;
	private String updateName;
	private String updateBy;
	private Date updateDate;
	private String sysOrgCode;
	private String sysCompanyCode;
	private String bpmStatus;
	private String checktype;
	private String remarks;
	private String checkno;

	public WmsCheck() {
	}

	public WmsCheck(String createName, String createBy, Date createDate,
			String updateName, String updateBy, Date updateDate,
			String sysOrgCode, String sysCompanyCode, String bpmStatus,
			String checktype, String remarks, String checkno) {
		this.createName = createName;
		this.createBy = createBy;
		this.createDate = createDate;
		this.updateName = updateName;
		this.updateBy = updateBy;
		this.updateDate = updateDate;
		this.sysOrgCode = sysOrgCode;
		this.sysCompanyCode = sysCompanyCode;
		this.bpmStatus = bpmStatus;
		this.checktype = checktype;
		this.remarks = remarks;
		this.checkno = checkno;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCreateName() {
		return this.createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public String getCreateBy() {
		return this.createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUpdateName() {
		return this.updateName;
	}

	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}

	public String getUpdateBy() {
		return this.updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getSysOrgCode() {
		return this.sysOrgCode;
	}

	public void setSysOrgCode(String sysOrgCode) {
		this.sysOrgCode = sysOrgCode;
	}

	public String getSysCompanyCode() {
		return this.sysCompanyCode;
	}

	public void setSysCompanyCode(String sysCompanyCode) {
		this.sysCompanyCode = sysCompanyCode;
	}

	public String getBpmStatus() {
		return this.bpmStatus;
	}

	public void setBpmStatus(String bpmStatus) {
		this.bpmStatus = bpmStatus;
	}

	public String getChecktype() {
		return this.checktype;
	}

	public void setChecktype(String checktype) {
		this.checktype = checktype;
	}

	public String getRemarks() {
		return this.remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getCheckno() {
		return this.checkno;
	}

	public void setCheckno(String checkno) {
		this.checkno = checkno;
	}

}
