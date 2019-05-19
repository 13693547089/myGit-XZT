package com.faujor.entity.bam.psm;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 实体类：SaleFcstDetail
 * @tableName PS_SALE_FCST_DETAIL
 * @tableDesc 销售预测明细表
 * @author Vincent
 * @date 2018-06-04
 *
 */
public class SaleFcstDetail implements Serializable {
    private String id;
    // 序号（表中不存在，用于删除操作）
    private Integer sn;
    // 主表ID	
    private String mainId;
    // 成品物料编码
    private String matCode;
    // 物料名称
    private String matName;
    // 产品系列编码
    private String prodSeriesCode;
    // 产品系列
    private String prodSeries;
    // 大品项编码
    private String bigItemCode;
    // 大品项
    private String bigItemExpl;
    // 去年1月销售预测
    private BigDecimal saleForeQty1;
    // 去年2月销售预测
    private BigDecimal saleForeQty2;
    // 去年3月销售预测
    private BigDecimal saleForeQty3;
    // 去年4月销售预测
    private BigDecimal saleForeQty4;
    // 去年5月销售预测
    private BigDecimal saleForeQty5;
    // 去年6月销售预测
    private BigDecimal saleForeQty6;
    // 去年7月销售预测
    private BigDecimal saleForeQty7;
    // 去年8月销售预测
    private BigDecimal saleForeQty8;
    // 去年9月销售预测
    private BigDecimal saleForeQty9;
    // 去年10月销售预测
    private BigDecimal saleForeQty10;
    // 去年11月销售预测
    private BigDecimal saleForeQty11;
    // 去年12月销售预测
    private BigDecimal saleForeQty12;

    // 本年1月销售预测
    private BigDecimal saleFore1;
    // 本年2月销售预测
    private BigDecimal saleFore2;
    // 本年3月销售预测
    private BigDecimal saleFore3;
    // 本年4月销售预测
    private BigDecimal saleFore4;
    // 本年5月销售预测
    private BigDecimal saleFore5;
    // 本年6月销售预测
    private BigDecimal saleFore6;
    // 本年7月销售预测
    private BigDecimal saleFore7;
    // 本年8月销售预测
    private BigDecimal saleFore8;
    // 本年9月销售预测
    private BigDecimal saleFore9;
    // 本年10月销售预测
    private BigDecimal saleFore10;
    // 本年11月销售预测
    private BigDecimal saleFore11;
    // 本年12月销售预测
    private BigDecimal saleFore12;
    // 排名
    private String rank;
    // 销售预测总计1
    private BigDecimal sumSaleFore1;
    // 销售预测总计2
    private BigDecimal sumSaleFore2;
    // 销售预测总计3
    private BigDecimal sumSaleFore3;
    
    // 汇总行数（导出使用）
    private Integer sumnum;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getSn() {
		return sn;
	}

	public void setSn(Integer sn) {
		this.sn = sn;
	}

	public String getMainId() {
        return mainId;
    }

    public void setMainId(String mainId) {
        this.mainId = mainId;
    }

    public String getMatCode() {
        return matCode;
    }

    public void setMatCode(String matCode) {
        this.matCode = matCode;
    }

    public String getMatName() {
        return matName;
    }

    public void setMatName(String matName) {
        this.matName = matName;
    }

    public String getProdSeriesCode() {
        return prodSeriesCode;
    }

    public void setProdSeriesCode(String prodSeriesCode) {
        this.prodSeriesCode = prodSeriesCode;
    }

    public String getProdSeries() {
        return prodSeries;
    }

    public void setProdSeries(String prodSeries) {
        this.prodSeries = prodSeries;
    }

    public String getBigItemCode() {
        return bigItemCode;
    }

    public void setBigItemCode(String bigItemCode) {
        this.bigItemCode = bigItemCode;
    }

    public String getBigItemExpl() {
        return bigItemExpl;
    }

    public void setBigItemExpl(String bigItemExpl) {
        this.bigItemExpl = bigItemExpl;
    }

    public BigDecimal getSaleForeQty1() {
        return saleForeQty1;
    }

    public void setSaleForeQty1(BigDecimal saleForeQty1) {
        this.saleForeQty1 = saleForeQty1;
    }

    public BigDecimal getSaleForeQty2() {
        return saleForeQty2;
    }

    public void setSaleForeQty2(BigDecimal saleForeQty2) {
        this.saleForeQty2 = saleForeQty2;
    }

    public BigDecimal getSaleForeQty3() {
        return saleForeQty3;
    }

    public void setSaleForeQty3(BigDecimal saleForeQty3) {
        this.saleForeQty3 = saleForeQty3;
    }

    public BigDecimal getSaleForeQty4() {
        return saleForeQty4;
    }

    public void setSaleForeQty4(BigDecimal saleForeQty4) {
        this.saleForeQty4 = saleForeQty4;
    }

    public BigDecimal getSaleForeQty5() {
        return saleForeQty5;
    }

    public void setSaleForeQty5(BigDecimal saleForeQty5) {
        this.saleForeQty5 = saleForeQty5;
    }

    public BigDecimal getSaleForeQty6() {
        return saleForeQty6;
    }

    public void setSaleForeQty6(BigDecimal saleForeQty6) {
        this.saleForeQty6 = saleForeQty6;
    }

    public BigDecimal getSaleForeQty7() {
        return saleForeQty7;
    }

    public void setSaleForeQty7(BigDecimal saleForeQty7) {
        this.saleForeQty7 = saleForeQty7;
    }

    public BigDecimal getSaleForeQty8() {
        return saleForeQty8;
    }

    public void setSaleForeQty8(BigDecimal saleForeQty8) {
        this.saleForeQty8 = saleForeQty8;
    }

    public BigDecimal getSaleForeQty9() {
        return saleForeQty9;
    }

    public void setSaleForeQty9(BigDecimal saleForeQty9) {
        this.saleForeQty9 = saleForeQty9;
    }

    public BigDecimal getSaleForeQty10() {
        return saleForeQty10;
    }

    public void setSaleForeQty10(BigDecimal saleForeQty10) {
        this.saleForeQty10 = saleForeQty10;
    }

    public BigDecimal getSaleForeQty11() {
        return saleForeQty11;
    }

    public void setSaleForeQty11(BigDecimal saleForeQty11) {
        this.saleForeQty11 = saleForeQty11;
    }

    public BigDecimal getSaleForeQty12() {
        return saleForeQty12;
    }

    public void setSaleForeQty12(BigDecimal saleForeQty12) {
        this.saleForeQty12 = saleForeQty12;
    }

	public BigDecimal getSaleFore1() {
		return saleFore1;
	}

	public void setSaleFore1(BigDecimal saleFore1) {
		this.saleFore1 = saleFore1;
	}

	public BigDecimal getSaleFore2() {
		return saleFore2;
	}

	public void setSaleFore2(BigDecimal saleFore2) {
		this.saleFore2 = saleFore2;
	}

	public BigDecimal getSaleFore3() {
		return saleFore3;
	}

	public void setSaleFore3(BigDecimal saleFore3) {
		this.saleFore3 = saleFore3;
	}

	public BigDecimal getSaleFore4() {
		return saleFore4;
	}

	public void setSaleFore4(BigDecimal saleFore4) {
		this.saleFore4 = saleFore4;
	}

	public BigDecimal getSaleFore5() {
		return saleFore5;
	}

	public void setSaleFore5(BigDecimal saleFore5) {
		this.saleFore5 = saleFore5;
	}

	public BigDecimal getSaleFore6() {
		return saleFore6;
	}

	public void setSaleFore6(BigDecimal saleFore6) {
		this.saleFore6 = saleFore6;
	}

	public BigDecimal getSaleFore7() {
		return saleFore7;
	}

	public void setSaleFore7(BigDecimal saleFore7) {
		this.saleFore7 = saleFore7;
	}

	public BigDecimal getSaleFore8() {
		return saleFore8;
	}

	public void setSaleFore8(BigDecimal saleFore8) {
		this.saleFore8 = saleFore8;
	}

	public BigDecimal getSaleFore9() {
		return saleFore9;
	}

	public void setSaleFore9(BigDecimal saleFore9) {
		this.saleFore9 = saleFore9;
	}

	public BigDecimal getSaleFore10() {
		return saleFore10;
	}

	public void setSaleFore10(BigDecimal saleFore10) {
		this.saleFore10 = saleFore10;
	}

	public BigDecimal getSaleFore11() {
		return saleFore11;
	}

	public void setSaleFore11(BigDecimal saleFore11) {
		this.saleFore11 = saleFore11;
	}

	public BigDecimal getSaleFore12() {
		return saleFore12;
	}

	public void setSaleFore12(BigDecimal saleFore12) {
		this.saleFore12 = saleFore12;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public BigDecimal getSumSaleFore1() {
		return sumSaleFore1;
	}

	public void setSumSaleFore1(BigDecimal sumSaleFore1) {
		this.sumSaleFore1 = sumSaleFore1;
	}

	public BigDecimal getSumSaleFore2() {
		return sumSaleFore2;
	}

	public void setSumSaleFore2(BigDecimal sumSaleFore2) {
		this.sumSaleFore2 = sumSaleFore2;
	}

	public BigDecimal getSumSaleFore3() {
		return sumSaleFore3;
	}

	public void setSumSaleFore3(BigDecimal sumSaleFore3) {
		this.sumSaleFore3 = sumSaleFore3;
	}

	public Integer getSumnum() {
		return sumnum;
	}

	public void setSumnum(Integer sumnum) {
		this.sumnum = sumnum;
	}
}