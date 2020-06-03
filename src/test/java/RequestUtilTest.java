import io.javalin.http.Context;
import kong.unirest.json.JSONArray;
import org.json.JSONException;
import kong.unirest.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import static org.mockito.Mockito.*;
import static zpi.utils.RequestUtil.*;

@RunWith(Parameterized.class)
public class RequestUtilTest {
	@Parameterized.Parameters
	public static Collection stringValues() {
		return Arrays.asList(new String[][] {
				{ "TestKey", "TestValue" },
				{ "name", "John" },
				{ "age", "18" },
				{ "4321", "randomNumber" },
				{ "", "" }
		});
	}

	@Parameterized.Parameter public String value1;
	@Parameterized.Parameter(1) public String value2;

	private Context ctx = mock(Context.class);

	@Test(expected = JSONException.class)
	public void checkParseJSONStandardArrayWhenBodyIsWrong() {
		when(ctx.body()).thenReturn("[ThisIsAllWrong]");
		parseJSONStandardArrayToMap(ctx);
	}

	@Test
	public void checkParseJSONStandardArrayWhenBodyIsEmpty() {
		when(ctx.body()).thenReturn("[]");
		Map<String, String> result = parseJSONStandardArrayToMap(ctx);
		Assert.assertTrue(result.isEmpty());
		verify(ctx).body();
	}

	@Test
	public void checkParseJSONStandardArrayWhenBodyIsCorrect() {
		String correctBody = generateCorrectBody(value1, value2);
		when(ctx.body()).thenReturn(correctBody);
		Map<String, String> result = parseJSONStandardArrayToMap(ctx);
		Assert.assertFalse(result.isEmpty());
		Assert.assertTrue(isMapContainsRequiredParams(result, value1));
		Assert.assertEquals(value2, result.get(value1));
		verify(ctx).body();
	}

	private String generateCorrectBody(String value1, String value2){
		var arr = new JSONArray();
		arr.put(new JSONObject(Map.of("name", value1, "value", value2)));
		return arr.toString();
	}
}
