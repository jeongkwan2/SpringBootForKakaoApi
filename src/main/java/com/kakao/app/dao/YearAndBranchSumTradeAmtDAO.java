package com.kakao.app.dao;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class YearAndBranchSumTradeAmtDAO {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private static final String SQL = "SELECT \r\n" + 
			"      YEAR AS 'year',\r\n" + 
			"      branch_info.branch_name AS 'brName',\r\n" + 
			"      branch_info.branch_code AS 'brCode',\r\n" + 
			"      SUM(sumAmt) AS 'sumAmt'\r\n" + 
			"      \r\n" + 
			"          FROM \r\n" + 
			"               (SELECT \r\n" + 
			"                     LEFT(trade_date,4) AS 'year',\r\n" + 
			"                     trade_info.account_no AS 'acctNo',\r\n" + 
			"                     SUM(amount) AS 'sumAmt',\r\n" + 
			"                     account_info.branch_code\r\n" + 
			"               FROM trade_info INNER JOIN account_info ON trade_info.account_no =  account_info.account_no\r\n" + 
			"               WHERE cancel_yn = 'N' \r\n" + 
			"               GROUP BY LEFT(trade_date,4) , trade_info.account_no\r\n" + 
			"               ORDER BY LEFT(trade_date,4) DESC , sumAmt DESC) AS A\r\n" + 
			"               RIGHT JOIN branch_info ON branch_info.branch_code = A.branch_code\r\n" + 
			"         \r\n" + 
			"         GROUP BY YEAR ,branch_info.branch_code \r\n" + 
			"         ORDER BY YEAR ASC,sumAmt DESC";
	
	@SuppressWarnings("unchecked")
	public JSONArray isData() {
		
		JSONArray resultArr = new JSONArray();

		JSONArray branchArr18 = new JSONArray();
		JSONArray branchArr19 = new JSONArray();


		List<Map<String, Object>> rows = jdbcTemplate.queryForList(SQL);


		for (Map<String, Object> row : rows) {
			JSONObject branchObject18 = new JSONObject();
			JSONObject branchObject19 = new JSONObject();

			
			if (row.get("year").equals("2018"))
			{
				branchObject18.put("brName", row.get("brName"));
				branchObject18.put("brCode", row.get("brCode"));
				java.math.BigDecimal tempValue = (java.math.BigDecimal) row.get("sumAmt");
				Integer value = Integer.valueOf(tempValue.toString());
				branchObject18.put("sumAmt", value);
				branchArr18.add(branchObject18);
			}
			
			else if (row.get("year").equals("2019"))
			{
				branchObject19.put("brName", row.get("brName"));
				branchObject19.put("brCode", row.get("brCode"));
				java.math.BigDecimal tempValue = (java.math.BigDecimal) row.get("sumAmt");
				Integer value = Integer.valueOf(tempValue.toString());
				branchObject19.put("sumAmt", value);
				branchArr19.add(branchObject19);
			}
			
		}
		JSONObject yearsObject19 = new JSONObject();
		JSONObject yearsObject18 = new JSONObject();
		
		yearsObject18.put("data", branchArr18);
		yearsObject18.put("year",2018);
		
		yearsObject19.put("data", branchArr19);
		yearsObject19.put("year",2019);
		
		
		resultArr.add(yearsObject18);
		resultArr.add(yearsObject19);
		

		
		return resultArr;
		
	}
}