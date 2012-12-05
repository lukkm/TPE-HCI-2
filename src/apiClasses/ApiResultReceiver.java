package apiClasses;

import android.os.Handler;
import android.os.ResultReceiver;

public abstract class ApiResultReceiver extends ResultReceiver {

	public CallBack callback;

	public ApiResultReceiver(Handler handler) {
		super(handler);
	}

	public void setCallBack (CallBack callback) {
		this.callback = callback;
	};

}
