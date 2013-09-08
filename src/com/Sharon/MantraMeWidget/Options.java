package com.Sharon.MantraMeWidget;

import com.example.mantrame.R;

import MantraMeClasses.Mantra;
import MantraMeClasses.MantraGetter;
import android.os.Bundle;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;

public class Options extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_options);

		Mantra mantra = MantraGetter.getCurrentMantra();	
		updateMantraText(mantra);		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.options, menu);
		return true;
	}

	public void nextMantraClicked(View view) {	
		MantraGetter.next();
		Mantra mantra = MantraGetter.getCurrentMantra();	

		updateMantraText(mantra);
		
		Context context = this;
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widgetlayout);
		ComponentName thisWidget = new ComponentName(context, MantraMeWidget.class);
		remoteViews.setTextViewText(R.id.textViewMantraShown, mantra.man_str);
		
		MantraMeWidget.checkStyle(mantra, remoteViews);
		
		appWidgetManager.updateAppWidget(thisWidget, remoteViews);
		

		/*
		Intent intent = new Intent(this, MantraMeWidget.class);
		intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
		int ids[] = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), MantraMeWidget.class));
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,ids);
		sendBroadcast(intent);
		 */
	}

	public void refreshMantrasFromServer(View view) {	
		MantraGetter getter = new MantraGetter();
		getter.getAllMantrasFromServer();
	}

	public void okClicked(View view) {	
		finish();
	}
	
	public void nextStyleClicked(View view) {	
		RemoteViews remoteViews = new RemoteViews(this.getPackageName(), R.layout.widgetlayout);
		MantraMeWidget.changeWidgetStyle(remoteViews);
	}

	private void updateMantraText(Mantra mantra) {
		TextView mantraText = (TextView) findViewById(R.id.optionsTextViewMantraShown);		
		if (mantra != null)	mantraText.setText(mantra.man_str);
	}
	
	
}
