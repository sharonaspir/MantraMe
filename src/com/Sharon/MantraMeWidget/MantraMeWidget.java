
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

import com.example.mantrame.R;


public class MantraMeWidget extends AppWidgetProvider {

	public static final int NUMBER_OF_STYLES = 6;
	public static int styleNumberUsed = -1;

	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		Log.w("33333333333", "onUpdate");

		final int N = appWidgetIds.length;
		super.onUpdate(context, appWidgetManager, appWidgetIds);


		UserProfile user = UserProfile.userProfileUsed;
		MantraGetter getter = new MantraGetter();
		getter.getAllMantrasFromServer();

		if (user != null){

			for (int i=0; i<N; i++) {
				int appWidgetId = appWidgetIds[i];
				RemoteViews view = new RemoteViews(context.getPackageName(), R.layout.widgetlayout);

				MantraGetter.next();
				Mantra mantra = MantraGetter.getCurrentMantra();

				if (mantra != null){
					view.setTextViewText(R.id.textViewMantraShown, mantra.man_str);
				}

				checkStyle(mantra,view);
				changeWidgetStyle(view);

				view.setTextViewText(R.id.textViewUserNameInWidget, user.name);

				// Adding on click event handler, to the app widget
				Intent intent = new Intent(context, Options.class);
				PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
				view.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);

				appWidgetManager.updateAppWidget(appWidgetId, view);					
			}
		}		
	}

	public static void checkStyle(Mantra mantra, RemoteViews view) {

		if (mantra.man_str.length() > 50){
			view.setTextViewTextSize(R.id.textViewMantraShown, TypedValue.COMPLEX_UNIT_SP, 12);
		}
		else{
			view.setTextViewTextSize(R.id.textViewMantraShown, TypedValue.COMPLEX_UNIT_SP, 16);
		}
	}

	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);		
		Toast.makeText(context, "Bye Bye Blue Sky", Toast.LENGTH_LONG).show();
	}

	public static void changeWidgetStyle(RemoteViews view){

		// LinearLayout ll = (LinearLayout)this.findViewById(R.id.widget_layout);

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
