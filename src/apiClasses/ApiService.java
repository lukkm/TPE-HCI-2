package apiClasses;

import java.io.IOException;
import java.io.Serializable;
import java.net.SocketTimeoutException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;

public class ApiService extends IntentService{

	public ApiService() {
		super("ApiService");
	}

	private final String TAG = getClass().getSimpleName();

	public static final int STATUS_CONNECTION_ERROR = -1;
	public static final int STATUS_ERROR = -2;
	public static final int STATUS_ILLEGAL_ARGUMENT = -3;
	public static final int STATUS_OK = 0;

	@Override
	protected void onHandleIntent(Intent intent) {
		ResultReceiver receiver = intent.getParcelableExtra("receiver");
		final String url = intent.getStringExtra("url");

		final Bundle b = new Bundle();
		try {
			callMethod(url, receiver, b);
		} catch (SocketTimeoutException e) {
			Log.e(TAG, e.getMessage());
			receiver.send(STATUS_CONNECTION_ERROR, b);
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
			receiver.send(STATUS_ERROR, b);
		} catch (ClientProtocolException e) {
			Log.e(TAG, e.getMessage());
			receiver.send(STATUS_ERROR, b);
		} catch (IllegalArgumentException e) {
			Log.e(TAG, e.getMessage());
			receiver.send(STATUS_ILLEGAL_ARGUMENT, b);
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
			receiver.send(STATUS_ERROR, b);
		}

		this.stopSelf();
	}

	private void callMethod(String url, ResultReceiver receiver, 
			Bundle b) throws ClientProtocolException, IOException, JSONException {
		final DefaultHttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		final HttpResponse response = client.execute(get);
		if ( response.getStatusLine().getStatusCode() != 200 ) {
			throw new IllegalArgumentException(response.getStatusLine().toString());
		}
		final String jsonToParse = EntityUtils.toString(response.getEntity());

		b.putSerializable("return", (Serializable) jsonToParse);

		receiver.send(STATUS_OK, b);
	}

}
