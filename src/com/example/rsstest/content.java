package com.example.rsstest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class content extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.content);
	
	Bundle bundle0311 =this.getIntent().getExtras();

    String sex = bundle0311.getString("url");
    TextView tv1 = (TextView)findViewById(R.id.Text);
    tv1.setText(sex);
    }
	
	}


