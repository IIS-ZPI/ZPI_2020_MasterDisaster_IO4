import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import zpi.category.Category;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class HTTPRequestStateTest {
	private final String IRRELEVANT_STATE_NAME = "/Alaska";
	private final String NON_EXISTING_STATE_NAME = "/TestStateName";

	@Mock
	private Context ctx;

	@BeforeClass
	public static void init(){
		HTTPRequestFactory.setdefaultBaseUrl();
	}

	@Test
	public void GET_toCheckAllStatesStatus() {
		Javalin app = HTTPRequestFactory.createApp().start(HTTPRequestFactory.PORT);
		HttpResponse response = HTTPRequestFactory.getResponse(HTTPRequestFactory.ALL_STATES_URL);
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.OK_STATUS);
		app.stop();
	}

	@Test
	public void GET_toCheckSingleStateExisting() {
		Javalin app = HTTPRequestFactory.createApp().start(HTTPRequestFactory.PORT);
		HttpResponse response = HTTPRequestFactory.getResponse(HTTPRequestFactory.SINGLE_STATE_URL + IRRELEVANT_STATE_NAME);
		String body = (String)response.getBody();
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.OK_STATUS);
		assertThat(body).contains(IRRELEVANT_STATE_NAME);
		app.stop();
	}

	@Test
	public void GET_toCheckSingleStateNonExisting() {
		Javalin app = HTTPRequestFactory.createApp().start(HTTPRequestFactory.PORT);
		HttpResponse response = HTTPRequestFactory.getResponse(HTTPRequestFactory.SINGLE_STATE_URL + NON_EXISTING_STATE_NAME);
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.OK_STATUS);
		assertThat(response.getBody()).isEqualTo(HTTPRequestFactory.NOT_FOUND_MESSAGE);
		app.stop();
	}

	//todo: categories, jakiś context ze zwracanymi taxami - usatw wszystkie kategorie
	@Test
	public void PUT_toCheckSingleStateExisting() throws JsonProcessingException {
		Javalin app = HTTPRequestFactory.createApp().start(HTTPRequestFactory.PORT);
		var values = Arrays.asList("0.5", "0.7", "0.12", "0.23", "0.4", "0.5");
//		ObjectMapper objectMapper = new ObjectMapper();
//		String bodyString = objectMapper.writeValueAsString(generateBody(values));
//		System.out.println(bodyString);

		HttpResponse response = Unirest.put(HTTPRequestFactory.SINGLE_STATE_URL + IRRELEVANT_STATE_NAME)
				.header("Content-Type", "application/json")
				.body(generateBody(values))
				.asString();
		String body = (String)response.getBody();
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.OK_STATUS);
		assertThat(body).contains("Edit taxes was successful!");
		app.stop();
	}

	@Test
	public void PUT_toCheckSingleStateNonExisting() {
		Javalin app = HTTPRequestFactory.createApp().start(HTTPRequestFactory.PORT);
		HttpResponse response = Unirest.put(HTTPRequestFactory.SINGLE_STATE_URL + NON_EXISTING_STATE_NAME).asString();
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.NOT_FOUND_STATUS);
		assertThat(response.getBody()).isEqualTo(HTTPRequestFactory.NOT_FOUND_MESSAGE);
		app.stop();
	}

	@Test
	public void PUT_toCheckIfAllCategoriesAreNotSet() {
		Javalin app = HTTPRequestFactory.createApp().start(HTTPRequestFactory.PORT);
		HttpResponse response = Unirest.put(HTTPRequestFactory.SINGLE_STATE_URL + IRRELEVANT_STATE_NAME).asString();
		String body = (String)response.getBody();
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.OK_STATUS);
		assertThat(body).contains("Check your input, edit taxes failed!");
		app.stop();
	}

	@Test
	public void PUT_toCheckIfTaxIsNotNumber() {
	}


	private Map<String, String> generateBody(List<String> taxValues){
		Map<String, String> body = new HashMap<>();
		int i = 0;
		for (var c : Category.values()) {
			body.put("tax_" + c.name(), taxValues.get(i));
			i++;
		}
		return body;
	}

}
