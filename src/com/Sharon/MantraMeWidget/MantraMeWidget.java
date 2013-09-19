
package com.Sharon.MantraMeWidget;

import java.util.Random;

import MantraMeClasses.Mantra;
import MantraMeClasses.MantraGetter;
import MantraMeClasses.UserProfile;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.util.TypedValue;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.Sharon.MantraMeWidget.R;

public class MantraMeWidget extends AppWidgetProvider {

	public static final int NUMBER_OF_STYLES = 6;
	public static int styleNumberUsed = -1;

	public void onUpdate(Context context, AppWidgetManager appWidgetManager,int[] appWidgetIds) {

		Log.w("MantraMeWidget", "onUpdate");

		final int N = appWidgetIds.length;
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		UserProfile user = UserProfile.getUser();

		for (int i=0; i<N; i++) {						
			int appWidgetId = appWidgetIds[i];			

			Log.w("MantraMeWidget", "i = " + i + " .appWidgetId = " + appWidgetId);

			RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.widgetlayout);

			MantraGetter.next(context);
			Mantra mantra = MantraGetter.getCurrentMantra(context);

			if (mantra != null){
				view.setTextViewText(R.id.textViewMantraShown, mantra.Description);
				view.setTextViewText(R.id.textViewAutorInWidget, mantra.Author);
			}

			checkStyle(mantra,view);
			changeWidgetStyle(view);

			if (user != null){
				view.setTextViewText(R.id.textViewUserNameInWidget, user.name);
				view.setFloat(R.id.textViewUserNameInWidget, "setTextSize", 10);
			}else{
				// set user name to empty, and resize to 2 size
				view.setTextViewText(R.id.textViewUserNameInWidget, "");
				view.setFloat(R.id.textViewUserNameInWidget, "setTextSize", 2);
			}

			// Adding on click event handler, to the app widget
			Intent intent = new Intent(context, Options.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
			view.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);

			appWidgetManager.updateAppWidget(appWidgetId, view);					
		}
	}

	public static void checkStyle(Mantra mantra, RemoteViews view) {

		if (mantra.Description.length() > 100){
			view.setTextViewTextSize(R.id.textViewMantraShown, TypedValue.COMPLEX_UNIT_SP, 13);
		}
		else if (mantra.Description.length() > 80){
			view.setTextViewTextSize(R.id.textViewMantraShown, TypedValue.COMPLEX_UNIT_SP, 15);
		}
		else{
			view.setTextViewTextSize(R.id.textViewMantraShown, TypedValue.COMPLEX_UNIT_SP, 20);
		}
	}

	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);		
		Toast.makeText(context, "Bye Bye Blue Sky", Toast.LENGTH_LONG).show();
	}

	public static void changeWidgetStyle(RemoteViews view){

		if (styleNumberUsed == -1){			
			Random r = new Random();
			int styleNumber = r.nextInt(NUMBER_OF_STYLES);
			setWidgetStyle(view, styleNumber);
			styleNumberUsed = styleNumber;
		}
		else{
			Random r = new Random();
			int styleNumber = styleNumberUsed;
			while (styleNumber == styleNumberUsed){
				styleNumber = r.nextInt(NUMBER_OF_STYLES);
			}
			setWidgetStyle(view, styleNumber);
			styleNumberUsed = styleNumber;
		}
	}

	public static void setWidgetStyle(RemoteViews view, int styleNumber) {

		switch (styleNumber){
		case 0 :
			view.setImageViewResource(R.id.backgroundImage, R.drawable.background0);
			break;
		case 1 :
			view.setImageViewResource(R.id.backgroundImage, R.drawable.background1);	
			break;
		case 2 :
			view.setImageViewResource(R.id.backgroundImage, R.drawable.background2);
			break;
		case 3 :
			view.setImageViewResource(R.id.backgroundImage, R.drawable.background3);
			break;
		case 4 :
			view.setImageViewResource(R.id.backgroundImage, R.drawable.background4);
			break;
		case 5 :
			view.setImageViewResource(R.id.backgroundImage, R.drawable.background5);
			break;
		default: 
			view.setImageViewResource(R.id.backgroundImage, R.drawable.background0);			
			break;
		}
	}
}
