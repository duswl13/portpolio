package com.jyjportfolio.service.Impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.jyjportfolio.service.TistoryService;
import com.jyjportfolio.util.CommonUtils;

@Service
public class TistoryServiceImpl implements TistoryService {

	@Value("${tistory.client_id}")
	String clientId;

	@Value("${tistory.client_secret}")
	String clientSecret;

	@Value("${tistory.code}")
	String code;

	@Value("${tistory.access_token}")
	String accessToken;

	@Value("${tistory.blog_name}")
	String blogName;

	private static final Logger LOGGER = LoggerFactory.getLogger(TistoryServiceImpl.class);

	@Override
	public List<Map<String, Object>> getPosts() throws Exception {

		
		List<Integer> getPostIds = this.getPostIds();
		
		return this.getPostDetail(getPostIds);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Integer> getPostIds() throws Exception {

		String response = WebClient.create("https://www.tistory.com").get()
									.uri(uriBuilder -> uriBuilder.path("/apis/post/list")
									.queryParam("access_token", accessToken)
									.queryParam("output", "json").queryParam("blogName", blogName)
									.queryParam("page", 1).build())
									.retrieve()
									.bodyToMono(String.class).block();
		
		LOGGER.info("getPostIds -- response:" + response);

		JSONParser jsonParser = new JSONParser();

		Object obj = jsonParser.parse(response);

		JSONObject jsonObj = (JSONObject) obj;

		JSONArray posts = (JSONArray) ((JSONObject) ((JSONObject) jsonObj.get("tistory")).get("item")).get("posts");

		
		List<Integer> returnPostIds = new LinkedList<Integer>();
		
		if (posts != null && posts.size() > 0) {

			Iterator<JSONObject> iterator = posts.iterator();
			
			while (iterator.hasNext()) {
				
				JSONObject item = iterator.next();

				returnPostIds.add(Integer.valueOf((String) item.get("id")));
				
				if(returnPostIds.size() == 4)
					break;
			}
		}
		
		
		return returnPostIds;
	}

	@Override
	public List<Map<String, Object>> getPostDetail(List<Integer> postIds) throws Exception {
		
		LOGGER.info("getPostDetail -- postIds:" + postIds.toString());
		
		List<Map<String, Object>> returnPostDetail = new LinkedList<Map<String,Object>>();
		
		for(Integer id : postIds) {
		
			String response = WebClient.create("https://www.tistory.com").get()
					.uri(uriBuilder -> uriBuilder.path("/apis/post/read")
					.queryParam("access_token", accessToken)
					.queryParam("output", "json").queryParam("blogName", blogName)
					.queryParam("postId", id).build())
					.retrieve()
					.bodyToMono(String.class).block();
			
			LOGGER.info("getPostDetail -- response:" + response);
			
			JSONParser jsonParser = new JSONParser();

			Object obj = jsonParser.parse(response);

			JSONObject jsonObj = (JSONObject) obj;

			JSONObject posts = (JSONObject) ((JSONObject) jsonObj.get("tistory")).get("item");

			Map<String, Object> item = new HashMap<String, Object>();
			item.put("title", (String)posts.get("title"));
			item.put("postUrl", (String)posts.get("postUrl"));
			
			String content = (String)posts.get("content");
			
			String textWithoutTag = CommonUtils.getText(content);
			item.put("content", textWithoutTag);
			
			String imgStart = content.substring(content.indexOf("<img src='")+10);
			String imgPath = imgStart.substring(0, imgStart.indexOf("'"));
			item.put("img", imgPath);
			
			
			String dateStr = (String)posts.get("date");
		    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
		    Date date = formatter.parse(dateStr);
		    GregorianCalendar cal = new GregorianCalendar();
		    cal.setTime(date);
		    
		    item.put("month", cal.get(Calendar.MONTH)+1);
		    item.put("day", cal.get(Calendar.DAY_OF_MONTH));
		   
			returnPostDetail.add(item);
			
		}

		return returnPostDetail;
	}

}
