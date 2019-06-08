package com.kakao.app.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.kakao.dto.MostAmountDTO;

@Repository
public class MostAmountDAO {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private static final String SQL = "SELECT A.YEAR AS 'year' , A.name , A.acctNo AS 'acctNo' , MAX(A.sumAmt) AS 'sumAmt'\r\n" + 
				"FROM (\r\n" + 
				"SELECT \r\n" + 
				"      LEFT(trade_date,4) AS 'year',\r\n" + 
				"      account_info.account_name AS 'name',\r\n" + 
				"      trade_info.account_no AS 'acctNo',\r\n" + 
				"      SUM(amount)-SUM(fee) AS 'sumAmt' \r\n" + 
				"FROM trade_info INNER JOIN account_info ON trade_info.account_no =  account_info.account_no\r\n" + 
				"WHERE cancel_yn = 'N' \r\n" + 
				"GROUP BY LEFT(trade_date,4) , trade_info.account_no\r\n" + 
				"ORDER BY LEFT(trade_date,4) DESC , sumAmt DESC) AS A \r\n" + 
				"GROUP BY A.YEAR ORDER BY A.YEAR ASC ";
	
	public List<MostAmountDTO> isData() {
		
		List<MostAmountDTO> List = new ArrayList<MostAmountDTO>();

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(SQL);
		for (Map<String, Object> row : rows) {
			MostAmountDTO data = new MostAmountDTO();
			
			data.setYear(Integer.valueOf((String)row.get("year")));
			data.setName((String)row.get("name"));
			data.setAcctNo((String)row.get("acctNo"));
			
			java.math.BigDecimal tempValue = (java.math.BigDecimal) row.get("sumAmt");
			Integer value = Integer.valueOf(tempValue.toString());
			data.setsumAmt(value);
			List.add(data);
		}

		return List;
		
	}
}