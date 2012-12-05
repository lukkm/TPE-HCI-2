package apiClasses;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import android.content.Context;
import android.content.Intent;
import android.os.ResultReceiver;


public class ApiIntent extends Intent {

	private String baseURL = "http://eiffel.itba.edu.ar/hci/service2/";
	private String service;
	private String method;

	public ApiIntent(String method, String service, ResultReceiver receiver, 
			Context context){
		super(Intent.ACTION_SYNC, null, context, ApiService.class);
		this.service = service;
		this.method = method;
		this.putExtra("receiver", receiver);
	}

	public void setParams(List<NameValuePair> params) {
		String url = this.baseURL + this.service + ".groovy?method=" + this.method + "&";
		String paramString = URLEncodedUtils.format(params, "utf-8");
		url += paramString;
		this.putExtra("url", url);
	}

}
