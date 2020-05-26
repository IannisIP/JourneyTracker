package journey.project.fragments

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ContentResolver
import android.content.Context
import android.widget.RemoteViews
import journey.project.R
import journey.project.data.SampleContentProvider

class JourneyTrackerWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
    }

    override fun onDisabled(context: Context) {
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    var last3TravelNotes : String? = null
    var counter = 0
    var cursor = context.contentResolver.query(SampleContentProvider.URI_TRAVELNOTES,null, null, null,"Date DESC")
    if(cursor?.moveToFirst()!!){
        do{
            last3TravelNotes += cursor.getString(1) +", "+ cursor.getString(3) +", "+  cursor.getString(5) + "\n"
            counter++
        }while (cursor.moveToNext() && counter <3 )
    }
    var notesCount = cursor.count

    val views = RemoteViews(context.packageName, R.layout.journey_tracker_widget)
    views.setTextViewText(R.id.textViewNumberOfNotes, notesCount.toString() + " Notes")
    views.setTextViewText(R.id.appwidget_text, if (notesCount == 0)  "Nothing to display..." else last3TravelNotes)
    appWidgetManager.updateAppWidget(appWidgetId, views)
}