import io.javalin.Javalin;
import kong.unirest.HttpResponse;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import zpi.category.Category;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(Parameterized.class)
public class HTTPRequestStateTest {
	private static Javalin app = null;
	private final String IRRELEVANT_STATE_NAME = "/Alaska";
	private final String NON_EXISTING_STATE_NAME = "/TestStateName";
	private final String IRRELEVANT_STATE_URL = HTTPRequestFactory.SINGLE_STATE_URL + IRRELEVANT_STATE_NAME;
	private final String NON_EXISTING_STATE_URL = HTTPRequestFactory.SINGLE_STATE_URL + NON_EXISTING_STATE_NAME;
	private static final String PUT_SUCCESS = "Edit taxes was successful!";
	private static final String PUT_FAIL = "Check your input, edit taxes failed!";

	@Parameterized.Parameters
	public static Collection taxesValues() {
		return Arrays.asList(new Object[][] {
				{ Arrays.asList("0.5", "0.7", "0.12", "0.23", "0.4", "0.5"), PUT_SUCCESS },
				{ Arrays.asList("0.03", "0.08", "0.12", "0.06", "0.04", "0.05"), PUT_SUCCESS },
				{ Arrays.asList("0.5", "number", "0.12", "0.23", "number", "0.5"), PUT_FAIL }
				//{ Arrays.asList("0.5", "0.08", "-0.12", "0.23", "0.09", "-0.5"), PUT_FAIL }
		});
	}

	@Parameterized.Parameter public List<String> taxes;
	@Parameterized.Parameter(1) public String returnMessage;

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
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.BAD_REQUEST_STATUS);
//		assertThat(response.getBody()).isEqualTo(HTTPRequestFactory.NOT_FOUND_MESSAGE);
	}

	@Test
	public void PUT_toChangeContentOfExistingState() {
		var arr = generateBody(taxes);

		HttpResponse response = HTTPRequestFactory.putResponse(IRRELEVANT_STATE_URL, arr);
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.OK_STATUS);
	}

	@Test
	public void PUT_toChangeContentOfNonExistingState() {
		HttpResponse response = HTTPRequestFactory.putResponse(NON_EXISTING_STATE_URL, new JSONArray());
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.BAD_REQUEST_STATUS);
//		assertThat(response.getBody()).isEqualTo(HTTPRequestFactory.NOT_FOUND_MESSAGE);
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
}
