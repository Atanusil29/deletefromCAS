package dlw.framework.exstream.main.deleteproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
public class DeleteProjects {
	private static final Logger LOGGER=LoggerFactory.getLogger(DeleteProjects.class);
	@Autowired
	private RestTemplate restTemplate;	
	@Autowired
    private Environment environment;
			@GetMapping ("/Index")
			String getTicket(Model model) throws JSONException {
				
				System.out.println("This is first call for showing the input page.");
				LOGGER.info("####Before submit:  Started getting the OTDS ticket ");
			       String otdsUrl = environment.getProperty("otds.url");
					LOGGER.info("Before submit: OTDS url is:"+otdsUrl);
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.APPLICATION_JSON);
					HashMap<String, String> map = new HashMap<>();
					String user =environment.getProperty("otds.serviceUser");
					String pwd =environment.getProperty("otds.userPwd");
					map.put("userName", user);
					map.put("password", pwd);
					HttpEntity<HashMap<String, String>> entity = new HttpEntity<HashMap<String, String>>(map,headers);
					ResponseEntity<String> response = restTemplate.exchange(otdsUrl, HttpMethod.POST, entity, String.class);
					JSONObject jsonObject = new JSONObject(response.getBody());
					LOGGER.info("Before submit: OTDS Access_Token:"+jsonObject.getString("ticket"));
					
					String baseUrl = environment.getProperty("exstream.serviceGateway");
					String findProjecturl = environment.getProperty("exstream.findproject");
					LOGGER.info("####Before submit: Exstream baseUrl:"+baseUrl);
					LOGGER.info("####Before submit: Exstream findprojecturl:"+findProjecturl);
					String urlvanproject = baseUrl+findProjecturl;
					LOGGER.info("####Before submit: Exstream to list projects:"+urlvanproject);
					HttpHeaders docHeaders = new HttpHeaders(); 
					docHeaders.setContentType(MediaType.APPLICATION_JSON);
					docHeaders.set("OTDSTicket", jsonObject.getString("ticket"));					
					HttpEntity<String> entityRes = new HttpEntity<String>("",docHeaders);
					ResponseEntity<String> responseResults = restTemplate.exchange(urlvanproject, HttpMethod.GET, entityRes, String.class);			
					LOGGER.info("####Before submit: Exstream find project body: "+responseResults.getBody().toString());
					
					final JSONObject obj = new JSONObject(responseResults.getBody().toString());
				    final org.json.JSONArray geodata = obj.getJSONArray("data");
				    final int n = geodata.length();
				    List<String> projectNames = new ArrayList<String>();
				    for (int i = 0; i < n; ++i) {
				      final JSONObject dataid = geodata.getJSONObject(i);
				      LOGGER.info(dataid.getString("679A17AC-6FF7-4AB3-B3FA-17379D1AE483"));
				      projectNames.add(dataid.getString("679A17AC-6FF7-4AB3-B3FA-17379D1AE483"));
				    }
				    LOGGER.info("####Before submit: list of projects: "+projectNames);
				    model.addAttribute("listProjects", projectNames);
				return "index";
			}
			
			@RequestMapping(value = "/DeletefromCAS/SubmitRequest", method = RequestMethod.POST)
			@ResponseBody
			public HttpStatus checkSubmit(@RequestBody RequestDTO RequestDTO) throws ParseException, JSONException{
		       
			       LOGGER.info("#### The retrieved CASID is: "+RequestDTO.getCasid());
			       LOGGER.info("#### Started getting the OTDS ticket ");
			       String otdsUrl = environment.getProperty("otds.url");
					LOGGER.info("OTDS url is:"+otdsUrl);
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(MediaType.APPLICATION_JSON);
					HashMap<String, String> map = new HashMap<>();
					String user =environment.getProperty("otds.serviceUser");
					String pwd =environment.getProperty("otds.userPwd");
					map.put("userName", user);
					map.put("password", pwd);
					HttpEntity<HashMap<String, String>> entity = new HttpEntity<HashMap<String, String>>(map,headers);
					ResponseEntity<String> response = restTemplate.exchange(otdsUrl, HttpMethod.POST, entity, String.class);
					JSONObject jsonObject = new JSONObject(response.getBody());
					LOGGER.info("OTDS Access_Token:"+jsonObject.getString("ticket"));
					
					
					String baseUrl = environment.getProperty("exstream.serviceGateway");
					String findProjecturl = environment.getProperty("exstream.findproject");
					LOGGER.info("####Exstream baseUrl:"+baseUrl);
					LOGGER.info("####Exstream findprojecturl:"+findProjecturl);
					String urlvanproject = baseUrl+findProjecturl+"&where_name="+RequestDTO.getCasid();
					LOGGER.info("####Exstream urlvanproject:"+urlvanproject);
					HttpHeaders docHeaders = new HttpHeaders(); 
					docHeaders.setContentType(MediaType.APPLICATION_JSON);
					docHeaders.set("OTDSTicket", jsonObject.getString("ticket"));					
					HttpEntity<String> entityRes = new HttpEntity<String>("",docHeaders);
					ResponseEntity<String> responseResults = restTemplate.exchange(urlvanproject, HttpMethod.GET, entityRes, String.class);			
					LOGGER.info("####Exstream find project body: "+responseResults.getBody().toString());
			        
			        
					final JSONObject obj = new JSONObject(responseResults.getBody().toString());
				    final org.json.JSONArray geodata = obj.getJSONArray("data");
				    final int n = geodata.length();
				    String casdataid= null;
				    for (int i = 0; i < n; ++i) {
				      final JSONObject dataid = geodata.getJSONObject(i);
				      LOGGER.info(dataid.getString("679A17AC-6FF7-4AB3-B3FA-17379D1AE483"));
				      LOGGER.info(dataid.getString("6B84E18B-F03C-350C-E040-007F0200026D"));
				      casdataid =dataid.getString("6B84E18B-F03C-350C-E040-007F0200026D");
				    }
				  //start deletetion
			        String delerePrepurl = baseUrl+environment.getProperty("exstream.delete")+"/"+casdataid+"?where_revision=-1";
			        LOGGER.info("### URL formed for deletetion :" +delerePrepurl);
			        HttpHeaders deleteProjectHeader = new HttpHeaders(); 
			        deleteProjectHeader.setContentType(MediaType.APPLICATION_JSON);
			        //deleteProjectHeader.add("where_revision", "-1");
			        deleteProjectHeader.set("OTDSTicket", jsonObject.getString("ticket"));
			        HttpEntity<String> entityDel = new HttpEntity<String>("",deleteProjectHeader);
					ResponseEntity<String> responseResultsDel = restTemplate.exchange(delerePrepurl, HttpMethod.DELETE, entityDel, String.class);			
					LOGGER.info("####Exstream project deleted: "+responseResultsDel.getBody().toString());
					//return responseResultsDel.getStatusCode();
					
			        
			        
					return responseResultsDel.getStatusCode();
			       
			   }
}
