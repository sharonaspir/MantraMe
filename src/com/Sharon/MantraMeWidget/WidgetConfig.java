package com.Sharon.MantraMeWidget;

import com.example.mantrame.R;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.SeekBar;

public class WidgetConfig extends Activity implements OnClickListener{

	int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
	AppWidgetManager awm;
	private EditText editTextName;
	private EditText editTextEmail;
	private SeekBar seekBarEducation;
	private SeekBar seekBarNewAge;
	private SeekBar intrestSport;
	private SeekBar seekBarSport;
	private SeekBar seekBarHealth;
	private Context c;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.widgetconfig);

		// Set button listener
		Button configDoneButton = (Button)findViewById(R.id.buttonConfigDone);
		configDoneButton.setOnClickListener(this);

		// Set context
		c = WidgetConfig.this;

		// Set the info fields
		editTextName = (EditText) findViewById(R.id.editTextPersonName);
		editTextEmail= (EditText) findViewById(R.id.editTextEmailAddress);

		// Get and Set the widget id
		Intent i = getIntent();
		Bundle extras = i.getExtras();
		if (extras != null) {
			mAppWidgetId = extras.getInt(
					AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
		}

		// Set AppWidgetManager
		awm = AppWidgetManager.getInstance(c);
	}

	@Override
	public void onClick(View arg0) {		

		// Update widget info
		RemoteViews views = new RemoteViews(c.getPackageName(), R.layout.widgetlayout);
		views.setTextViewText(R.id.textViewUserNameInWidget, "bla bla");
		awm.updateAppWidget(mAppWidgetId, views);
		
		// Update the widget info
		Intent updateWidget = new Intent(getApplicationContext(), MantraMeWidget.class);
		updateWidget.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
		int[] ids = { mAppWidgetId };
		updateWidget.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
		sendBroadcast(updateWidget);
		
		// Set the activity result as Ok 
		Intent resultValue = new Intent();
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
		setResult(RESULT_OK, resultValue);
		
		finish();
	}


}
