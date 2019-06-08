package com.kakao.app.dao;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class IntegratedBranchDAO {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	//private static final String CREATESQL = "CREATE TABLE tradedata.newtrade_info (\r\n" + 
	//		"	brName VARCHAR(255) NULL,\r\n" + 
	//		"	brCode VARCHAR(255) NULL,\r\n" + 
	//		"	sumAmt DECIMAL NULL)\r\n"; //DB TABLE은 생성 되어있음
	
	private static final String DELETESQL =		"DELETE FROM tradedata.newtrade_info"; //이전 테스트로 생성된 DB가 있어 사전에 TABLE 내용삭제
	
	private static final String INSERTSQL =		"INSERT INTO tradedata.newtrade_info SELECT * FROM\r\n" + 
			"   (SELECT \r\n" + 
			"         branch_info.branch_name AS 'brName',\r\n" + 
			"         branch_info.branch_code AS 'brCode',\r\n" + 
			"         SUM(sumAmt) AS 'sumAmt'\r\n" + 
			"         \r\n" + 
			"             FROM \r\n" + 
			"                  (SELECT \r\n" + 
			"                        trade_info.account_no AS 'acctNo',\r\n" + 
			"                        SUM(amount) AS 'sumAmt',\r\n" + 
			"                        account_info.branch_code\r\n" + 
			"                  FROM trade_info INNER JOIN account_info ON trade_info.account_no =  account_info.account_no\r\n" + 
			"                  WHERE cancel_yn = 'N' \r\n" + 
			"                  GROUP BY trade_info.account_no\r\n" + 
			"                  ORDER BY sumAmt DESC) AS A\r\n" + 
			"                  RIGHT JOIN branch_info ON branch_info.branch_code = A.branch_code\r\n" + 
			"            \r\n" + 
			"            GROUP BY branch_info.branch_code \r\n" + 
			"            ORDER BY branch_info.branch_code DESC) AS B"; //통폐합 하기전 지점별 거래금액 합계 저장
	
	private static final String UPDATESQL = "UPDATE tradedata.newtrade_info SET brName = '판교점',brCode ='A' WHERE  brName='분당점' AND brCode = 'B'";	// 이후 코드변경
	
	private static final String SELECTSQL =  "SELECT brName,brCode,SUM(sumAmt) AS sumAmt FROM tradedata.newtrade_info WHERE brCode =";
			
			

	
	@SuppressWarnings("unchecked")
	public JSONObject isData(JSONObject body) {

		JSONObject obj = new JSONObject(body);
		JSONObject resultobj = new JSONObject(body);
		
		String temp = "''";
		if (obj.get("brName") != null && obj.get("brName").toString().length() > 0 )
		{
			if(obj.get("brName").toString().contains("판교"))
				temp = "'A'";
			else if (obj.get("brName").toString().contains("강남"))
				temp = "'C'";
			else if (obj.get("brName").toString().contains("잠실"))
				temp = "'D'";
			else
				return DataIsNull();
		}
		else
			return DataIsNull();

		jdbcTemplate.update(DELETESQL);
		jdbcTemplate.update(INSERTSQL);
		jdbcTemplate.update(UPDATESQL);
		List<Map<String, Object>> rows = jdbcTemplate.queryForList(SELECTSQL+temp);
		
		for (Map<String, Object> row : rows) {

			resultobj.put("brName", (String)row.get("brName"));
			resultobj.put("brCode", (String)row.get("brCode"));
			
			java.math.BigDecimal tempValue = (java.math.BigDecimal) row.get("sumAmt");
			Integer value = Integer.valueOf(tempValue.toString());
			resultobj.put("sumAmt", value);
			

			
		}

		return resultobj;
		
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject DataIsNull() {
		JSONObject obj = new JSONObject();
		
		obj.put("code", "404");
		obj.put("메세지", "brCode not found error");
			
		return obj;
	}
	
}