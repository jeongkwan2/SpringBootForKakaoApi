package com.kakao.dto;

public class MostAmountDTO {
	
	private int year;
	private String name;
	private String acctNo;
	private Integer sumAmt;
	
	
	
	public MostAmountDTO() {
		
	}
	
	public MostAmountDTO(int year, String name, String acctNo, Integer sumAmt) {
		this.year = year;
		this.name = name;
		this.acctNo = acctNo;
		this.sumAmt = sumAmt;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getAcctNo() {
		return acctNo;
	}

	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}

	public Integer getsumAmt() {
		return sumAmt;
	}

	public void setsumAmt(Integer sumAmt) {
		this.sumAmt = sumAmt;
	}
	
}
