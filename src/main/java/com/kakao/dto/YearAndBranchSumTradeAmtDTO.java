package com.kakao.dto;

public class YearAndBranchSumTradeAmtDTO {
	
	private int year;
	private String brName;
	private String brCode;
	private Integer sumAmt;
	
	public YearAndBranchSumTradeAmtDTO() {
		
	}
	
	public YearAndBranchSumTradeAmtDTO(int year, String brName, String brCode, Integer sumAmt) {
		this.year = year;
		this.brName = brName;
		this.brCode = brCode;
		this.sumAmt = sumAmt;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getBrName() {
		return brName;
	}

	public void setBrName(String brName) {
		this.brName = brName;
	}
	
	public String getBrCode() {
		return brCode;
	}

	public void setBrCode(String brCode) {
		this.brCode = brCode;
	}
	
	public Integer getsumAmt() {
		return sumAmt;
	}

	public void setsumAmt(Integer sumAmt) {
		this.sumAmt = sumAmt;
	}
	
}
