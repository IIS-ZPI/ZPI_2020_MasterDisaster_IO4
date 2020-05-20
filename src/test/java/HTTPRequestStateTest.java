import com.fasterxml.jackson.core.JsonProcessingException;
import io.javalin.Javalin;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import zpi.category.Category;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class HTTPRequestStateTest {
	private static Javalin app = null;
	private final String IRRELEVANT_STATE_NAME = "/Alaska";
	private final String NON_EXISTING_STATE_NAME = "/TestStateName";
	private final String IRRELEVANT_STATE_URL = HTTPRequestFactory.SINGLE_STATE_URL + IRRELEVANT_STATE_NAME;
	private final String NON_EXISTING_STATE_URL = HTTPRequestFactory.SINGLE_STATE_URL + NON_EXISTING_STATE_NAME;

	@BeforeClass
	public static void init(){
		HTTPRequestFactory.setDefaultBaseUrl();
		app = HTTPRequestFactory.createApp().start(HTTPRequestFactory.PORT);
	}

	@AfterClass
	public static void destruct(){
		app.stop();
	}

	private JSONArray generateBody(List<String> taxValues){
		var arr = new JSONArray();
		int index= 0;
		for(var c : Category.values()){
			arr.put(new JSONObject(Map.of("name", c.name(), "value", taxValues.get(index++))));
		}
		return arr;
	}

	@Test
	public void GET_toCheckAllStatesStatus() {
		HttpResponse response = HTTPRequestFactory.getResponse(HTTPRequestFactory.ALL_STATES_URL);
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.OK_STATUS);
	}

	@Test
	public void GET_toCheckSingleStateExisting() {
		HttpResponse response = HTTPRequestFactory.getResponse(IRRELEVANT_STATE_URL);
		String body = (String)response.getBody();
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.OK_STATUS);
		assertThat(body).contains(IRRELEVANT_STATE_NAME);
	}

	@Test
	public void GET_toCheckSingleStateNonExisting() {
		HttpResponse response = HTTPRequestFactory.getResponse(NON_EXISTING_STATE_URL);
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.OK_STATUS);
		assertThat(response.getBody()).isEqualTo(HTTPRequestFactory.NOT_FOUND_MESSAGE);
	}

	@Test
	public void PUT_toChangeContentOfExistingState() {
		var values = Arrays.asList("0.5", "0.7", "0.12", "0.23", "0.4", "0.5");
		var arr = generateBody(values);

		HttpResponse response = HTTPRequestFactory.putResponse(IRRELEVANT_STATE_URL, arr);
		String body = (String)response.getBody();
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.OK_STATUS);
		assertThat(body).contains("Edit taxes was successful!");
		values.forEach(value -> assertThat(body).contains(value));
	}

	@Test
	public void PUT_toChangeContentOfNonExistingState() {
		HttpResponse response = HTTPRequestFactory.putResponse(NON_EXISTING_STATE_URL, new JSONArray());
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.OK_STATUS);
		assertThat(response.getBody()).isEqualTo(HTTPRequestFactory.NOT_FOUND_MESSAGE);
	}

//	@Test
//	public void PUT_toChangeContentOfExistingState_NoBodySet() {
//		Javalin app = HTTPRequestFactory.createApp().start(HTTPRequestFactory.PORT);
//		HttpResponse response = Unirest.put(IRRELEVANT_STATE_URL).asString();
//		String body = (String)response.getBody();
//		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.OK_STATUS);
//		assertThat(body).contains("Check your input, edit taxes failed!");
//		app.stop();
//	}

//	@Test
//	public void PUT_toChangeContentOfExistingState_EmptyBody() {
//		HttpResponse response = Unirest.put(IRRELEVANT_STATE_URL).body(new JSONArray()).asString();
//		String body = (String)response.getBody();
//		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.OK_STATUS);
//		assertThat(body).contains("Check your input, edit taxes failed!");
//	}

	@Test
	public void PUT_toChangeContentOfExistingState_SomeTaxesSetAsDoubles() {
		var values = Arrays.asList("0.5", "number", "0.12", "0.23", "number", "0.5");
		var arr = generateBody(values);

		HttpResponse response = HTTPRequestFactory.putResponse(IRRELEVANT_STATE_URL, arr);
		String body = (String)response.getBody();
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.OK_STATUS);
		assertThat(body).contains("Check your input, edit taxes failed!");
	}
}
