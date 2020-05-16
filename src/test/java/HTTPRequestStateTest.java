import io.javalin.Javalin;
import io.javalin.http.Context;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.junit.Test;
import org.mockito.Mock;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class HTTPRequestStateTest {
	private final String IRRELEVANT_STATE_NAME = "Alaska";
	private final String NON_EXISTING_STATE_NAME = "TestStateName";

	@Mock
	private Context ctx;

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
		HttpResponse response = Unirest.get(HTTPRequestFactory.SINGLE_STATE_URL).queryString("name", IRRELEVANT_STATE_NAME).asString();
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.OK_STATUS);
		assertThat(response.getBody()).isEqualTo("");
		app.stop();
	}

	@Test
	public void GET_toCheckSingleStateNonExisting() {
		Javalin app = HTTPRequestFactory.createApp().start(HTTPRequestFactory.PORT);
		HttpResponse response = Unirest.get(HTTPRequestFactory.SINGLE_STATE_URL).queryString("name", NON_EXISTING_STATE_NAME).asString();
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.OK_STATUS);
		assertThat(response.getBody()).isEqualTo(HTTPRequestFactory.NOT_FOUND_MESSAGE);
		app.stop();
	}

	//todo: categories, jakiś context ze zwracanymi taxami - usatw wszystkie kategorie
	@Test
	public void PUT_toCheckSingleStateExisting() {
		Javalin app = HTTPRequestFactory.createApp().start(HTTPRequestFactory.PORT);
		HttpResponse response = Unirest.put(HTTPRequestFactory.SINGLE_STATE_URL).queryString("name", IRRELEVANT_STATE_NAME).asString();
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.OK_STATUS);
		assertThat(response.getBody()).isEqualTo("");
		app.stop();
	}

	@Test
	public void PUT_toCheckSingleStateNonExisting() {
		Javalin app = HTTPRequestFactory.createApp().start(HTTPRequestFactory.PORT);
		HttpResponse response = Unirest.put(HTTPRequestFactory.SINGLE_STATE_URL).queryString("name", NON_EXISTING_STATE_NAME).asString();
		assertThat(response.getStatus()).isEqualTo(HTTPRequestFactory.OK_STATUS);
		assertThat(response.getBody()).isEqualTo(HTTPRequestFactory.NOT_FOUND_MESSAGE);
		app.stop();
	}

	@Test
	public void PUT_toCheckIfNotAllCategoriesAreSet() {
	}

	@Test
	public void PUT_toCheckIfTaxIsNotNumber() {
	}

}
