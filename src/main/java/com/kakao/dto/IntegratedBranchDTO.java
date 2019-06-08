package com.kakao.dto;

public class IntegratedBranchDTO {
	
	private String brName;
	private String brCode;
	private Integer sumAmt;
	
	public IntegratedBranchDTO() {
		
	}
	
	public IntegratedBranchDTO(String brName, String brCode, Integer sumAmt) {
		this.brName = brName;
		this.brCode = brCode;
		this.sumAmt = sumAmt;
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
	
	public Integer getsumtradeAmt() {
		return sumAmt;
	}

	public void setsumtradeAmt(Integer sumtradeAmt) {
		this.sumAmt = sumtradeAmt;
	}
	
}
