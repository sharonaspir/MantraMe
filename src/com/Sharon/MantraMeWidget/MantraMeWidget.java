
package com.Sharon.MantraMeWidget;

import java.util.Random;

import com.example.mantrame.R;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;


public class MantraMeWidget extends AppWidgetProvider{

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		
		
		Log.w("11111111", "im in onUpdate");
		
		
		// TODO Auto-generated method stub
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		Random r = new Random();
		int randomInt = r.nextInt(1000);

		final int N = appWidgetIds.length;

		for (int i=0; i<N; i++) {
			int appWidgetId = appWidgetIds[i];

			RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.widgetlayout);
			view.setTextViewText(R.id.textViewMantraShown, "randomInt = " + randomInt + " !");
			appWidgetManager.updateAppWidget(appWidgetId, view);	
		}
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onDeleted(context, appWidgetIds);		

		Toast.makeText(context, "Bye Bye Blue Sky", Toast.LENGTH_LONG).show();
	}

}
