package com.google.zxing.client.android;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;

public class MainActivity extends Activity {

	private ListView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mylist);
		Intent intent = getIntent();
		String message = intent.getStringExtra(CaptureActivity.EXTRA_MESSAGE);

		try {
			new MyTask().execute(message).get();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// 获取网络图片资源，返回类型是Bitmap，用于设置在ListView中
	public Bitmap getBitmap(String httpUrl) {
		Bitmap bmp = null;
		// ListView中获取网络图片
		try {
			URL url = new URL(httpUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			InputStream is = conn.getInputStream();
			bmp = BitmapFactory.decodeStream(is);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bmp;
	}

	class MyTask extends AsyncTask<String, Integer, List<Map<String, Object>>> {

		URL bitmap_url = null;
		URL bitmap_url2 = null;
		Bitmap bitmap = null;
		Bitmap bitmap2 = null;
		ImageView imview;
		String line = null;
		String url = "http://img1.douban.com/spic/s1747553.jpg";
		String url2 = "http://img3.douban.com/view/ark_article_cover/cut/public/1541407.jpg?v=1378364448.0";

		@Override
		protected List<Map<String, Object>> doInBackground(String... params) {

			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			Map<String, Object> map = new HashMap<String, Object>();

			try {
				bitmap_url = new URL(url);
				bitmap_url2 = new URL(url2);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

			try {
				HttpURLConnection conn = (HttpURLConnection) bitmap_url
						.openConnection();
				conn.setDoInput(true);
				conn.connect();
				InputStream is = conn.getInputStream();
				bitmap = BitmapFactory.decodeStream(is);

				HttpURLConnection conn2 = (HttpURLConnection) bitmap_url2
						.openConnection();
				conn2.setDoInput(true);
				conn2.connect();
				InputStream is2 = conn2.getInputStream();
				bitmap2 = BitmapFactory.decodeStream(is2);
				// http post
				NameValuePair pair1 = new BasicNameValuePair("name", params[0]);

				List<NameValuePair> pairList = new ArrayList<NameValuePair>();
				pairList.add(pair1);

				Log.i("http", params[0]);
				HttpEntity requestHttpEntity = new UrlEncodedFormEntity(
						pairList, "UTF-8");

				HttpPost httpPost = new HttpPost("http://192.168.1.126/t.php");

				// 将请求体内容加入请求中
				httpPost.setEntity(requestHttpEntity);

				HttpParams httpParameters = new BasicHttpParams();

				DefaultHttpClient httpClient = new DefaultHttpClient(
						httpParameters);
				Log.i("debug", "7");
				try {

					Log.i("code", httpPost.toString());

					Log.i("test", "xx");
					HttpResponse httpResponse = httpClient.execute(httpPost);
					Log.i("test", "xcxc");
					line = EntityUtils.toString(httpResponse.getEntity(),
							"UTF-8");

					// if (httpResponse.getStatusLine().getStatusCode() != 200)
					// {
					// return null;
					// }
					// HttpEntity httpEntity = httpResponse.getEntity();

				} catch (ConnectTimeoutException e) {
					line = "connect timeout";

				}

				try {
					//JSONObject jo = new JSONObject(line);
					
					
					 JSONObject result = new JSONObject(line);//转换为JSONObject
			            int num = result.length();
			            JSONArray nameList = result.getJSONArray("a");//获取JSONArray
			            int length = nameList.length();
			            String aa = "";
			            for(int i = 0; i < length; i++){//遍历JSONArray
			                Log.d("debugTest",Integer.toString(i));
			                JSONObject oj = nameList.getJSONObject(i);
			                line = aa + oj.getString("name");
			        		map = new HashMap<String, Object>();
							Log.i("weitao", params[0]);
							map.put("img", getBitmap(url2));
							map.put("title", line);
							map.put("info",
									"那一年，是听莫扎特、钓鲈鱼和家庭破裂的一年。说到家庭破裂，母亲怪自己当初没有找到好男人，父亲则认为当时是被狐狸精迷住了眼，失常的是母亲，但出问题的是父亲");
							list.add(map);
			                
			            }
				} catch (Exception e) {

				}
		
				/*
				 * map = new HashMap<String, Object>(); map.put("img",
				 * getBitmap(url2)); map.put("title", "jkdjfkdkfdjfk");
				 * map.put("info",
				 * "那一年，是听莫扎特、钓鲈鱼和家庭破裂的一年。说到家庭破裂，母亲怪自己当初没有找到好男人，父亲则认为当时是被狐狸精迷住了眼，失常的是母亲，但出问题的是父亲"
				 * ); list.add(map);
				 */
				/*
				 * 
				 * map.put("img", bitmap); map.put("title", "满月之夜白鲸现");
				 * map.put("info",
				 * "那一年，是听莫扎特、钓鲈鱼和家庭破裂的一年。说到家庭破裂，母亲怪自己当初没有找到好男人，父亲则认为当时是被狐狸精迷住了眼，失常的是母亲，但出问题的是父亲"
				 * ); list.add(map);
				 * 
				 * map.put("img", bitmap); map.put("title", "满月之夜白鲸现");
				 * map.put("info",
				 * "那一年，是听莫扎特、钓鲈鱼和家庭破裂的一年。说到家庭破裂，母亲怪自己当初没有找到好男人，父亲则认为当时是被狐狸精迷住了眼，失常的是母亲，但出问题的是父亲"
				 * ); list.add(map);
				 * 
				 * map.put("img", bitmap); map.put("title", "满月之夜白鲸现");
				 * map.put("info",
				 * "那一年，是听莫扎特、钓鲈鱼和家庭破裂的一年。说到家庭破裂，母亲怪自己当初没有找到好男人，父亲则认为当时是被狐狸精迷住了眼，失常的是母亲，但出问题的是父亲"
				 * ); list.add(map); map.put("img", bitmap); map.put("title",
				 * "满月之夜白鲸现"); map.put("info",
				 * "那一年，是听莫扎特、钓鲈鱼和家庭破裂的一年。说到家庭破裂，母亲怪自己当初没有找到好男人，父亲则认为当时是被狐狸精迷住了眼，失常的是母亲，但出问题的是父亲"
				 * ); list.add(map); map.put("img", bitmap); map.put("title",
				 * "满月之夜白鲸现"); map.put("info",
				 * "那一年，是听莫扎特、钓鲈鱼和家庭破裂的一年。说到家庭破裂，母亲怪自己当初没有找到好男人，父亲则认为当时是被狐狸精迷住了眼，失常的是母亲，但出问题的是父亲"
				 * ); list.add(map); map.put("img", bitmap); map.put("title",
				 * "满月之夜白鲸现"); map.put("info",
				 * "那一年，是听莫扎特、钓鲈鱼和家庭破裂的一年。说到家庭破裂，母亲怪自己当初没有找到好男人，父亲则认为当时是被狐狸精迷住了眼，失常的是母亲，但出问题的是父亲"
				 * ); list.add(map); map.put("img", bitmap); map.put("title",
				 * "满月之夜白鲸现"); map.put("info",
				 * "那一年，是听莫扎特、钓鲈鱼和家庭破裂的一年。说到家庭破裂，母亲怪自己当初没有找到好男人，父亲则认为当时是被狐狸精迷住了眼，失常的是母亲，但出问题的是父亲"
				 * ); list.add(map); map.put("img", bitmap); map.put("title",
				 * "满月之夜白鲸现"); map.put("info",
				 * "那一年，是听莫扎特、钓鲈鱼和家庭破裂的一年。说到家庭破裂，母亲怪自己当初没有找到好男人，父亲则认为当时是被狐狸精迷住了眼，失常的是母亲，但出问题的是父亲"
				 * ); list.add(map);
				 */

				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return list;
		}

		@Override
		protected void onPostExecute(List<Map<String, Object>> result) {
			imview = (ImageView) findViewById(R.id.imview);

			if (result != null) {

				lv = (ListView) findViewById(R.id.listview);
				SimpleAdapter adapter = new SimpleAdapter(MainActivity.this,
						result, R.layout.activity_main, new String[] { "img",
								"title", "info" }, new int[] { R.id.imview,
								R.id.title, R.id.info });
				adapter.setViewBinder(new ViewBinder() {
					public boolean setViewValue(View view, Object data,
							String textRepresentation) {
						// 判断是否为我们要处理的对象
						if (view instanceof ImageView && data instanceof Bitmap) {
							ImageView iv = (ImageView) view;
							iv.setImageBitmap((Bitmap) data);
							return true;
						} else
							return false;
					}
				});

				if (adapter != null) {
					lv.setAdapter(adapter);
				}
				/* imview.setImageBitmap(result); */
			} else {

				Log.i("bitmap", "b");
			}
			// ProgressBar update =
			// (ProgressBar)UpdateDialog.findViewById(R.id.horizontalProgressBar);

		}

	}

}
