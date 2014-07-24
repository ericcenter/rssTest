package com.example.rsstest;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ListActivity {
	List headlines;
	List links;
	List content;
	
	private WebView m_WV;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

//		m_WV = (WebView) findViewById(R.id.webview);
//		WebSettings webSettings = m_WV.getSettings();
//		webSettings.setJavaScriptEnabled(true);

		 if (android.os.Build.VERSION.SDK_INT > 9) {
		 StrictMode.ThreadPolicy policy = new
		 StrictMode.ThreadPolicy.Builder()
		 .permitAll().build();
		 StrictMode.setThreadPolicy(policy);
		 }

		// Initializing instance variables
		headlines = new ArrayList();
		links = new ArrayList();
		 
		try {
		    URL url = new URL("http://www.appledaily.com.tw/rss/newcreate/kind/sec/type/5");
		 
		    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
		    factory.setNamespaceAware(false);
		    XmlPullParser xpp = factory.newPullParser();
		 
		        // We will get the XML from an input stream
		    xpp.setInput(getInputStream(url), "UTF_8");
		 
		        /* We will parse the XML content looking for the "<title>" tag which appears inside the "<item>" tag.
		         * However, we should take in consideration that the rss feed name also is enclosed in a "<title>" tag.
		         * As we know, every feed begins with these lines: "<channel><title>Feed_Name</title>...."
		         * so we should skip the "<title>" tag which is a child of "<channel>" tag,
		         * and take in consideration only "<title>" tag which is a child of "<item>"
		         *
		         * In order to achieve this, we will make use of a boolean variable.
		         */
		    boolean insideItem = false;
		 
		        // Returns the type of current event: START_TAG, END_TAG, etc..
		    int eventType = xpp.getEventType();
		    while (eventType != XmlPullParser.END_DOCUMENT) {
		        if (eventType == XmlPullParser.START_TAG) {
		 
		            if (xpp.getName().equalsIgnoreCase("item")) {
		                insideItem = true;
		            } else if (xpp.getName().equalsIgnoreCase("title")) {
		                if (insideItem)
//		                	Log.i("title",xpp.nextText());
		                    headlines.add(xpp.nextText()); //extract the headline
		            } else if (xpp.getName().equalsIgnoreCase("link")) {
		                if (insideItem)
//		                	Log.i("link", xpp.nextText());
		                    links.add(xpp.nextText()); //extract the link of article
		            } 
//		            else if (xpp.getName().equalsIgnoreCase("description")) {
//		                if (insideItem)
////		                	Log.i("content", xpp.nextText());
//		                    content.add(xpp.nextText());
//		            }
		        }else if(eventType==XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")){
		            insideItem=false;
		        }
		 
		        eventType = xpp.next(); //move to next element
		    }
		 
		} catch (MalformedURLException e) {
		    e.printStackTrace();
		} catch (XmlPullParserException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		 
		// Binding data
		ArrayAdapter adapter = new ArrayAdapter(this,
		        android.R.layout.simple_list_item_1, headlines);
		 
		setListAdapter(adapter);
	}

	private class rss extends Thread {
		public void run() {
			try {
				URL url = new URL("http://feeds.pcworld.com/pcworld/latestnews");

				XmlPullParserFactory factory = XmlPullParserFactory
						.newInstance();
				factory.setNamespaceAware(false);
				XmlPullParser xpp = factory.newPullParser();

				// We will get the XML from an input stream
				xpp.setInput(getInputStream(url), "UTF_8");

				/*
				 * We will parse the XML content looking for the "<title>" tag
				 * which appears inside the "<item>" tag. However, we should
				 * take in consideration that the rss feed name also is enclosed
				 * in a "<title>" tag. As we know, every feed begins with these
				 * lines: "<channel><title>Feed_Name</title>...." so we should
				 * skip the "<title>" tag which is a child of "<channel>" tag,
				 * and take in consideration only "<title>" tag which is a child
				 * of "<item>"
				 * 
				 * In order to achieve this, we will make use of a boolean
				 * variable.
				 */
				boolean insideItem = false;
				// Returns the type of current event: START_TAG, END_TAG, etc..
				int eventType = xpp.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					if (eventType == XmlPullParser.START_TAG) {

						if (xpp.getName().equalsIgnoreCase("item")) {
							insideItem = true;
						} else if (xpp.getName().equalsIgnoreCase("title")) {
							if (insideItem)
								headlines.add(xpp.nextText()); // extract the
																// headline
						} else if (xpp.getName().equalsIgnoreCase("link")) {
							if (insideItem)
								links.add(xpp.nextText()); // extract the link
															// of
															// article
						}
					} else if (eventType == XmlPullParser.END_TAG
							&& xpp.getName().equalsIgnoreCase("item")) {
						insideItem = false;
					}

					eventType = xpp.next(); // move to next element
				}

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public InputStream getInputStream(URL url) {
		try {
			return url.openConnection().getInputStream();
		} catch (IOException e) {
			return null;
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		Uri uri = Uri.parse(links.get(position).toString());

//		m_WV.loadUrl(uri.toString());
//	    m_WV.setWebViewClient(new WebViewClientImpl());

//		 Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//		 startActivity(intent);
		
		
//        setContentView(R.layout.content);

//	    TextView tv=(TextView)findViewById(R.id.Text); 
//		tv.setText(uri.toString());
//		setContentView(tv);
		
		
		   Intent intent = new Intent();
//		    intent.setClass(this, Page2.class);
		intent.setClass(MainActivity.this,content.class);
		
		
		 Bundle bundle = new Bundle();
         bundle.putString("url", uri.toString());
         intent.putExtras(bundle);
 		startActivity(intent);
		

	}

	private final class WebViewClientImpl extends WebViewClient
	  {
		public void WebViewClientImpl()
	    {
	    }

		@Override
	    public boolean shouldOverrideUrlLoading(WebView view, String url)
	    {return false;}
	  }}

