package com.yt.httpdemo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	private String TAG = "http";
	private EditText mNameText = null;
	private EditText mPasswordText = null;

	private Button getButton = null;
//	private Button postButton = null;

	
	private TextView new_view = null;
	
	private int mId=1;
	
	private NotificationCompat.Builder mBuilder=null;

	// 基本地址：服务器ip地址：端口号/Web项目逻辑地址+目标页面（Servlet）的url-pattern
	private String url = "http://10.0.2.2:80/login.php";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
	
	   onNewIntent(getIntent());
	 

		getButton = (Button) findViewById(R.id.submit_get);
		
		getButton.setOnClickListener(mGetClickListener);

	}
	
	public void onNewIntent(Intent intent)
	{
		 new_view = (TextView) findViewById(R.id.new_view);
		 
		
		Bundle extras =intent.getExtras();
		if(extras!=null)
		{
		new_view.setText((extras.getString("data1")));
		}
		
		
	}
	

	private OnClickListener mGetClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {

			String mt = null;
			try {
				Log.i("mt", "a");
				mt = new MyTask().execute("http://192.168.1.154/test.php")
						.get();
				if (mt != "") {
					Log.i("mt", mt);
				} else {

					Log.i("mt", "nothing");
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Log.i(TAG, "GET request");
			// 先获取用户名和年龄
			// String name = mNameText.getText().toString();
			// String age = mAgeText.getText().toString();

			// 使用GET方法发送请求,需要把参数加在URL后面，用？连接，参数之间用&分隔
			// String url = baseURL + "?username=" + name + "&age=" + age;

			// 生成请求对象
			/*
			 * HttpGet httpGet = new HttpGet(url); HttpClient httpClient = new
			 * DefaultHttpClient();
			 */
			// 发送请求
			// try
			// {
			// Log.i("http", "httpGet");
			// HttpResponse response = httpClient.execute(httpGet);
			// Log.i("http", "httpGet2");
			// 显示响应
			// showResponseResult(response);// 一个私有方法，将响应结果显示出来

			// }
			// catch (Exception e)
			// {
			// e.printStackTrace();
			// / }

		}
	};

	private OnClickListener mPostClickListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			Log.i(TAG, "POST request");
			// 先获取用户名和年龄
	        mNameText = (EditText) findViewById(R.id.username);
	        mPasswordText = (EditText) findViewById(R.id.password);

			String name = mNameText.getText().toString();
			String age = mPasswordText.getText().toString();

			Log.i("mt",name);
			Log.i("mt",age);
			NameValuePair pair1 = new BasicNameValuePair("username", name);
			NameValuePair pair2 = new BasicNameValuePair("age", age);

			List<NameValuePair> pairList = new ArrayList<NameValuePair>();
			pairList.add(pair1);
			pairList.add(pair2);
		

			try {
				HttpEntity requestHttpEntity = new UrlEncodedFormEntity(
						pairList);
				// URL使用基本URL即可，其中不需要加参数
				HttpPost httpPost = new HttpPost(url);
				// 将请求体内容加入请求中
				httpPost.setEntity(requestHttpEntity);
				// 需要客户端对象来发送请求
				HttpClient httpClient = new DefaultHttpClient();
				// 发送请求
				HttpResponse response = httpClient.execute(httpPost);
				// 显示响应
				showResponseResult(response);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	};

	/**
	 * 显示响应结果到命令行和TextView
	 * 
	 * @param response
	 */
	private void showResponseResult(HttpResponse response) {
		if (null == response) {
			return;
		}

		HttpEntity httpEntity = response.getEntity();
		try {
			InputStream inputStream = httpEntity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			String result = "";
			String line = "";
			while (null != (line = reader.readLine())) {
				result += line;

			}

			System.out.println(result);
			new_view.setText(result);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void createNotification()
	{
		Log.i("notification", "msg");
	 mBuilder =
		        new NotificationCompat.Builder(this)
		        .setSmallIcon(R.drawable.ic_launcher)
		        .setContentTitle("My notification")
		        .setContentText("Hello World!")
		        .setAutoCancel(true);

		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(this, MainActivity.class);

		// The stack builder object will contain an artificial back stack for the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		//TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// Adds the back stack for the Intent (but not the Intent itself)
		//stackBuilder.addParentStack(MainActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		//stackBuilder.addNextIntent(resultIntent);
		//PendingIntent resultPendingIntent =
		  //      stackBuilder.getPendingIntent(
		     //       0,
		      //      PendingIntent.FLAG_UPDATE_CURRENT
		       // );
		//mBuilder.setContentIntent(resultPendingIntent);
		int requestID = (int) System.currentTimeMillis();
		NotificationManager mNotificationManager =
		    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mBuilder.build().flags =mBuilder.build().FLAG_AUTO_CANCEL;
		// mId allows you to update the notification later on.
		 Intent intent = new Intent(this, MainActivity.class);  //Main.class即本Activity
			intent.putExtra("data1", "Hello Android");
		//intent.setAction("notification"+ requestID);
	
         //PendingIntent对象，用途：点击Notification通知后跳转到的Activity页面
         PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                         intent, PendingIntent.FLAG_UPDATE_CURRENT);
          
         //notification.defaults = Notification.DEFAULT_SOUND;
        
         intent.setData((Uri.parse("mystring"+requestID)));
         
		  	

			
         // 设置通知显示的参数
          mBuilder.setContentIntent(pendingIntent);
		mNotificationManager.notify(mId, mBuilder.build());
		
	}
	class MyTask extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... params) {
			String line=null;
			try {
				 mNameText = (EditText) findViewById(R.id.username);
			        mPasswordText = (EditText) findViewById(R.id.password);

					String name = mNameText.getText().toString();
					String password = mPasswordText.getText().toString();
	            NameValuePair pair1 = new BasicNameValuePair("login", name);
	            NameValuePair pair2 = new BasicNameValuePair("pwd", password);
	            NameValuePair pair3 = new BasicNameValuePair("sbt", "1");
	            
	            Log.i("mt",name);
			
	            List<NameValuePair> pairList = new ArrayList<NameValuePair>();
	            pairList.add(pair1);
	            pairList.add(pair2);

				Log.i("http", params[0]);
                HttpEntity requestHttpEntity = new UrlEncodedFormEntity(
                        pairList,"UTF-8");

				HttpPost httpPost = new HttpPost(params[0]);
				
				// 将请求体内容加入请求中
				httpPost.setEntity(requestHttpEntity);
				Log.i("debug","1");
				HttpParams httpParameters = new BasicHttpParams();
				Log.i("debug","2");
				// Set the timeout in milliseconds until a connection is
				// established.
				int timeoutConnection = 10000;
				Log.i("debug","3");
				HttpConnectionParams.setConnectionTimeout(httpParameters,
						timeoutConnection);
				Log.i("debug","4");
				// Set the default socket timeout (SO_TIMEOUT)
				// in milliseconds which is the timeout for waiting for data.
				int timeoutSocket = 10000;
				HttpConnectionParams
						.setSoTimeout(httpParameters, timeoutSocket);
				Log.i("debug","5");
				ResponseHandler<String> responseHandler = new BasicResponseHandler();
				Log.i("debug","6");
				DefaultHttpClient httpClient = new DefaultHttpClient(
						httpParameters);
				Log.i("debug","7");
				try {
					Log.i("code", httpPost.toString());
					
					HttpResponse httpResponse = httpClient.execute(httpPost);
					
					line=EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
							
					
					//if (httpResponse.getStatusLine().getStatusCode() != 200) {
					//	return null;
					//}
					//HttpEntity httpEntity = httpResponse.getEntity();
				
				} catch (ConnectTimeoutException e) {
					return "connect timeout";

				}

			} catch (UnsupportedEncodingException e) {
				line = "Can't connect to server1";
			} catch (MalformedURLException e) {
				line = "Can't connect to server2";
			} catch (NoHttpResponseException e)
			{
				 line="4";
				
			}
			catch (IOException e) {
				line = "Can't connect to server3";
			}
			return line ;
		}
		
		@Override
		protected void onPostExecute(String result) {
			 //ProgressBar update = (ProgressBar)UpdateDialog.findViewById(R.id.horizontalProgressBar);
           Log.i("mt","wt"+result);
           new_view = (TextView) findViewById(R.id.new_view);
           createNotification();
           
           
			if(result!=null)
			{
				
				
				SharedPreferences sharedPref =MainActivity.this.getPreferences(Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = sharedPref.edit();
			
				editor.putString(getString(R.string.save_data), result+"shared_prefs");
				editor.commit();
				SharedPreferences sharedPref2 = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
				String defaultValue = getResources().getString(R.string.save_data_default);
				String highScore = sharedPref2.getString(getString(R.string.save_data), defaultValue);
				Log.i("msg", highScore);
				new_view.setText(highScore);
				
			}
			else
			{
				new_view.setText("xxx");
					
				
			}
			super.onPostExecute(result);
		
		}

	}

}
