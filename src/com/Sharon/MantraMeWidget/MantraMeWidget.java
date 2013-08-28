
package com.Sharon.MantraMeWidget;

import MantraMeClasses.Mantra;
import MantraMeClasses.MantraGetter;
import MantraMeClasses.UserProfile;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.Sharon.MantraMeWidget.AddNewUser.addNewUser;
import com.example.mantrame.R;


public class MantraMeWidget extends AppWidgetProvider {

	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		final int N = appWidgetIds.length;
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		Log.w("33333333333", "onUpdate");
		UserProfile user = UserProfile.userProfileUsed;
		MantraGetter getter = new MantraGetter(user, context);

		if (user != null){

			for (int i=0; i<N; i++) {
				int appWidgetId = appWidgetIds[i];
				RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.widgetlayout);

				Mantra mantra = getter.GetNewMantra();

				if (mantra != null){
					view.setTextViewText(R.id.textViewMantraShown, mantra.man_str);
				}
				
				view.setTextViewText(R.id.textViewUserNameInWidget, user.name);

				// Adding on click event handler, to the app widget
				Intent intent = new Intent(context, Options.class);
		        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
				view.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);
				
				appWidgetManager.updateAppWidget(appWidgetId, view);					
			}
		}
		
	}

	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);		
		Toast.makeText(context, "Bye Bye Blue Sky", Toast.LENGTH_LONG).show();
	}
}
