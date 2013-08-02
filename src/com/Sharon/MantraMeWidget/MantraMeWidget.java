
package com.Sharon.MantraMeWidget;

import java.util.Random;

import com.example.mantrame.R;

import MantraMeClasses.Mantra;
import MantraMeClasses.MantraGetter;
import MantraMeClasses.UserProfile;
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

		super.onUpdate(context, appWidgetManager, appWidgetIds);

		Log.w("11111111", "im in onUpdate");

		UserProfile user = UserProfile.userProfileUsed;
		MantraGetter getter = new MantraGetter(user);

		if (user != null){
			final int N = appWidgetIds.length;

			for (int i=0; i<N; i++) {
				int appWidgetId = appWidgetIds[i];
				RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.widgetlayout);
				
				Mantra mantra = getter.GetNewMantra();
				
				view.setTextViewText(R.id.textViewMantraShown, mantra.man_str);
				view.setTextViewText(R.id.textViewUserNameInWidget, user.name);
				
				appWidgetManager.updateAppWidget(appWidgetId, view);
			}
		}
	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onDeleted(context, appWidgetIds);		

		Toast.makeText(context, "Bye Bye Blue Sky", Toast.LENGTH_LONG).show();
	}

}
