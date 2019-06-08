package com.kakao.app.controller;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kakao.app.dao.MostAmountDAO;
import com.kakao.dto.MostAmountDTO;

import com.kakao.app.dao.NoTradeCustomerDAO;
import com.kakao.dto.NoTradeCustomerDTO;

import com.kakao.app.dao.YearAndBranchSumTradeAmtDAO;
import com.kakao.dto.YearAndBranchSumTradeAmtDTO;

import com.kakao.app.dao.IntegratedBranchDAO;

@RestController
public class KakaoApiController extends DefaultErrorAttributes {
	
	
	//1. 2018년, 2019년 각 연도별 합계 금액이 가장 많은 고객을 추출하는 API 개발.(단, 취소여부가
	//‘Y’ 거래는 취소된 거래임, 합계 금액은 거래금액에서 수수료를 차감한 금액임)
	@Autowired
	public MostAmountDAO MostAmountDAO;
	
	@RequestMapping("/MostAmount")
	public List<MostAmountDTO> MostAmout() {
		
		List<MostAmountDTO> jsonData = MostAmountDAO.isData();		 
		return jsonData;
	}
	
	//2. 2018년 또는 2019년에 거래가 없는 고객을 추출하는 API 개발.
	//(취소여부가 ‘Y’ 거래는 취소된 거래임)
	//입력 값은 아래와 같은 값을 사용.
	@Autowired
	public NoTradeCustomerDAO NoTradeCustomerDAO;
	
	@RequestMapping("/NoTradeCustomer")
	public List<NoTradeCustomerDTO> NoTradeCustomer() {
		
		List<NoTradeCustomerDTO> jsonData = NoTradeCustomerDAO.isData();		 
		
		return jsonData;
	}
	
	
	//3. 연도별 관리점 별 거래금액 합계를 구하고 합계금액이 큰 순서로 출력하는 API 개발.(
	//		취소여부가 ‘Y’ 거래는 취소된 거래임)
	@Autowired
	public YearAndBranchSumTradeAmtDAO YearAndBranchSumAmtDAO;
	
	@RequestMapping("/YearAndBranchSumTradeAmt")
	public List<YearAndBranchSumTradeAmtDTO> YearAndBranchSumAmt() {
		
		@SuppressWarnings("unchecked")
		List<YearAndBranchSumTradeAmtDTO> jsonData = YearAndBranchSumAmtDAO.isData();		 
		
		return jsonData;
	}
	
	//4. 분당점과 판교점을 통폐합하여 판교점으로 관리점 이관을 하였습니다. 지점명을 입력하면
	//해당지점의 거래금액 합계를 출력하는 API 개발( 취소여부가 ‘Y’ 거래는 취소된 거래임,)
	@Autowired
	public IntegratedBranchDAO IntegratedBranchDAO;
	@RequestMapping(value = "/IntegratedBranch" , method = RequestMethod.POST)
	@ResponseBody
	public JSONObject IntegratedBranch(@RequestBody String body ) {	
		System.out.println("body: "+body);
		JSONParser parser = new JSONParser();
		Object obj = null;
		try {
			obj = parser.parse( body );
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return DataIsNull();
		}
		JSONObject jsonObj = (JSONObject) obj;
		
		JSONObject jsonData = IntegratedBranchDAO.isData(jsonObj);
		
		
		return jsonData;
	} 
	
	@SuppressWarnings("unchecked")
	public JSONObject DataIsNull() {
		JSONArray resultArr = new JSONArray();
		JSONObject obj = new JSONObject();
		
		obj.put("code", "404");
		obj.put("메세지", "No JSON format");
		
		resultArr.add(obj);		
		return obj;
	}
}