import io.javalin.Javalin;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import org.junit.AfterClass;
import org.junit.Assert;
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
	private final String IRRELEVANT_STATE_NAME = "Alaska";
	private final String NON_EXISTING_STATE_NAME = "TestStateName";

	@Parameterized.Parameters
	public static Collection taxesValues() {
		return Arrays.asList(new Object[][] {
				{ HTTPRequestFactory.OK_STATUS, Arrays.asList("50.0", "70.0", "12.0", "23.0", "40.0", "50.0"), Arrays.asList("0.5", "0.7", "0.12", "0.23", "0.4", "0.5") },
				{ HTTPRequestFactory.OK_STATUS, Arrays.asList("3.0", "8.0", "12.0", "6.0", "4.0", "5.0"), Arrays.asList("0.03", "0.08", "0.12", "0.06", "0.04", "0.05") },
				{ HTTPRequestFactory.BAD_REQUEST_STATUS, Arrays.asList("50.0", "number", "12.0", "23.0", "number", "50.0"), Arrays.asList("0.5", "number", "0.12", "0.23", "number", "0.5") }
		});
	}

	@Parameterized.Parameter public int status;
	@Parameterized.Parameter(1) public List<String> values;
	@Parameterized.Parameter(2) public List<String> taxes;

	@BeforeClass
	public static void init(){
		HTTPRequestFactory.setDefaultBaseUrl();
		app = HTTPRequestFactory.createApp().start(HTTPRequestFactory.PORT);
	}

	@AfterClass
	public static void destruct(){
		app.stop();
	}

	private JSONArray generateBody(String stateName, List<String> valuesWithoutTax, List<String> taxValues){
		var arr = new JSONArray();
		arr.put(new JSONObject(Map.of("name", "stateName", "value", stateName)));

		if(valuesWithoutTax.isEmpty() || taxValues.isEmpty())
			return arr;
		int index= 0;
		//values
		for(var c : Category.values()){
			arr.put(new JSONObject(Map.of("name", "valueWithoutTax" + c.name(), "value", valuesWithoutTax.get(index++))));
		}
		index=0;
		//taxes
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

//	@Test
//	public void GET_toCheckSingleStateExisting() {
//		HttpResponse response = HTTPRequestFactory.getResponse(IRRELEVANT_STATE_URL);
//		String body = (String)response.getBody();
//		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.OK_STATUS);
//		assertThat(body).contains(IRRELEVANT_STATE_NAME);
//	}
//
//	@Test
//	public void GET_toCheckSingleStateNonExisting() {
//		HttpResponse response = HTTPRequestFactory.getResponse(NON_EXISTING_STATE_URL);
//		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.BAD_REQUEST_STATUS);
////		assertThat(response.getBody()).isEqualTo(HTTPRequestFactory.NOT_FOUND_MESSAGE);
//	}
//
	@Test
	public void PUT_toChangeContentOfExistingState() {
		var arr = generateBody(IRRELEVANT_STATE_NAME, values, taxes);
		HttpResponse response = HTTPRequestFactory.putResponse(HTTPRequestFactory.ALL_STATES_URL, arr);
		assertThat(response.getStatus()).isEqualTo(status);
	}

	@Test
	public void PUT_toChangeContentOfNonExistingState() {
		var nonExistingState = generateBody(NON_EXISTING_STATE_NAME, new ArrayList<>(), new ArrayList<>());
		HttpResponse response = HTTPRequestFactory.putResponse(HTTPRequestFactory.ALL_STATES_URL, nonExistingState);
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.NOT_FOUND_STATUS);
	}
}
