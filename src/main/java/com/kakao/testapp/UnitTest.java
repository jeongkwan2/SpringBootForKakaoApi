package com.kakao.testapp;

import org.junit.runner.RunWith;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;

import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.http.MediaType;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@SpringBootConfiguration
@SpringBootApplication(scanBasePackages={"com.kakao.app","com.kakao.dto"})
@WebAppConfiguration
public class UnitTest extends SpringBootServletInitializer{

	@Autowired
	private WebApplicationContext ctx;
	
	private MockMvc mockMvc;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(ctx).build();
	}

	@Test
	public void MostAmount() throws Exception {
		System.out.println("**************************");
		System.out.println("********MostAmount********");
		System.out.println("**************************");
		mockMvc.perform(get("/MostAmount")).andExpect(status().isOk()).andDo(print());

	}

	@Test
	public void NoTradeCustomer() throws Exception {
		System.out.println("*******************************");
		System.out.println("********NoTradeCustomer********");
		System.out.println("*******************************");
		mockMvc.perform(get("/NoTradeCustomer")).andExpect(status().isOk()).andDo(print());
	}

	@Test
	public void YearAndBranchSumTradeAmt() throws Exception {
		System.out.println("****************************************");
		System.out.println("********YearAndBranchSumTradeAmt********");
		System.out.println("****************************************");
		mockMvc.perform(get("/YearAndBranchSumTradeAmt")).andExpect(status().isOk()).andDo(print());
	}

	
	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@SuppressWarnings("unchecked")
	@Test
	public void IntegratedBranch() throws Exception {
		System.out.println("*******************************");
		System.out.println("********IntegratedBranch********");
		System.out.println("*******************************");
		JSONObject obj = new JSONObject();
		obj.put("brName", "판교");
		
		ObjectMapper mapper = new ObjectMapper();
	    mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
	    ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
	    String requestJson=ow.writeValueAsString(obj);

	    mockMvc.perform(post("/IntegratedBranch").contentType(APPLICATION_JSON_UTF8)
	        .content(requestJson))
	        .andExpect(status().isOk()).andDo(print());
	}
	


	  public static String asJsonString(final Object obj) {
	    try {
	        return new ObjectMapper().writeValueAsString(obj);
	    } catch (Exception e) {
	        throw new RuntimeException(e);
	    }
	}

}
