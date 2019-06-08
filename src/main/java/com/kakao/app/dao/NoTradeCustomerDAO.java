package com.kakao.app.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.kakao.dto.NoTradeCustomerDTO;

@Repository
public class NoTradeCustomerDAO {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private static final String SQL = "   SELECT * FROM \r\n" + 
			"            (SELECT \r\n" + 
			"                IFNULL (YEAR,2018) AS 'year',\r\n" + 
			"               account_name AS 'name',\r\n" + 
			"               account_no AS 'acctNo'\r\n" + 
			"               \r\n" + 
			"               FROM\r\n" + 
			"                  (SELECT \r\n" + 
			"                        LEFT(trade_date,4) AS 'year',\r\n" + 
			"                        account_info.account_name AS 'name',\r\n" + 
			"                        trade_info.account_no AS 'acctNo',\r\n" + 
			"                        SUM(amount)-SUM(fee) AS 'sumAmt'\r\n" + 
			"                  FROM trade_info INNER JOIN account_info ON trade_info.account_no =  account_info.account_no\r\n" + 
			"                  WHERE cancel_yn = 'N' AND trade_date LIKE '2018%'\r\n" + 
			"                  GROUP BY LEFT(trade_date,4) , trade_info.account_no\r\n" + 
			"                  ORDER BY LEFT(trade_date,4) DESC , sumAmt DESC) AS A\r\n" + 
			"               RIGHT JOIN account_info ON account_info.account_no = A.acctNo\r\n" + 
			"               WHERE A.acctNo IS NULL) AS B\r\n" + 
			"            \r\n" + 
			"      UNION (SELECT * FROM \r\n" + 
			"            \r\n" + 
			"            (SELECT \r\n" + 
			"                IFNULL (YEAR,2019) AS 'year',\r\n" + 
			"               account_name AS 'name',\r\n" + 
			"               account_no AS 'acctNo'\r\n" + 
			"               \r\n" + 
			"               FROM\r\n" + 
			"                  (SELECT \r\n" + 
			"                        LEFT(trade_date,4) AS 'year',\r\n" + 
			"                        account_info.account_name AS 'name',\r\n" + 
			"                        trade_info.account_no AS 'acctNo',\r\n" + 
			"                        SUM(amount)-SUM(fee) AS 'sumAmt'\r\n" + 
			"                  FROM trade_info INNER JOIN account_info ON trade_info.account_no =  account_info.account_no\r\n" + 
			"                  WHERE cancel_yn = 'N' AND trade_date LIKE '2019%'\r\n" + 
			"                  GROUP BY LEFT(trade_date,4) , trade_info.account_no\r\n" + 
			"                  ORDER BY LEFT(trade_date,4) DESC , sumAmt DESC) AS C\r\n" + 
			"               RIGHT JOIN account_info ON account_info.account_no = C.acctNo\r\n" + 
			"               WHERE C.acctNo IS NULL) AS D)";
	
	public List<NoTradeCustomerDTO> isData() {
		
		List<NoTradeCustomerDTO> List = new ArrayList<NoTradeCustomerDTO>();

		List<Map<String, Object>> rows = jdbcTemplate.queryForList(SQL);
		
		for (Map<String, Object> row : rows) {
			NoTradeCustomerDTO data = new NoTradeCustomerDTO();
			
			data.setYear(Integer.valueOf((String)row.get("year")));
			data.setName((String)row.get("name"));
			data.setAcctNo((String)row.get("acctNo"));

			List.add(data);
		}

		return List;
		
	}
}