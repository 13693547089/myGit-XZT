package com.faujor.entity.bam;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 报价单附件
 * @author hql
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class QuoteAttr {
	
	private String id;
	
	private String quoteCode;
	
	private String attrFile;
	
	private String remark;
	
	private Integer attrIndex;
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getQuoteCode() {
		return quoteCode;
	}

	public void setQuoteCode(String quoteCode) {
		this.quoteCode = quoteCode;
	}

	public String getAttrFile() {
		return attrFile;
	}

	public void setAttrFile(String attrFile) {
		this.attrFile = attrFile;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	

	/**
	 * @return the attrIndex
	 */
	public Integer getAttrIndex() {
		return attrIndex;
	}

	/**
	 * @param attrIndex the attrIndex to set
	 */
	public void setAttrIndex(Integer attrIndex) {
		this.attrIndex = attrIndex;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "QuoteAttr [id=" + id + ", quoteCode=" + quoteCode + ", attrFile=" + attrFile + ", remark=" + remark
				+ ", attrIndex=" + attrIndex + "]";
	}

	
	

	

	
}
