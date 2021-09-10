package com.example.myapplication3

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager as pm
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.RemoteViews
import androidx.core.graphics.drawable.toBitmap



/**
 * Implementation of App Widget functionality.
 */
class TestWidget : AppWidgetProvider() {

    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    val possibleApps = mapOf<String, String>(
        "Google" to "https://google.com",
        "Amazon" to "https://amazon.com",
        "Yahoo" to "https://yahoo.com",
        "YouTube" to "https://youtube.com",
        "Netflix" to "https://netflix.com",
        "Maps" to "https://www.google.de/maps"
    )

    val suggestedApps = mutableMapOf<String, String>()


    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {

            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context?, intent: Intent) {
        super.onReceive(context, intent)
        if (ACTION_REROLL.equals(intent.action)) {

            if (context != null) {

                suggestedApps.clear()

                val numberOfSuggestedApps = (1..3).random();

                for(i in 1..numberOfSuggestedApps){

                    val entry = possibleApps.entries.shuffled().first();

                    suggestedApps.put(entry.key,entry.value)
                }

                onUpdate(context)
            }
        }
    }

    private fun onUpdate(context: Context) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val thisAppWidgetComponentName = ComponentName(context.packageName, javaClass.name)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidgetComponentName)
        onUpdate(context, appWidgetManager, appWidgetIds)
    }

    private fun getPendingSelfIntent(context: Context, action: String): PendingIntent? {
        val intent =
            Intent(context, javaClass) // An intent directed at the current class (the "self").
        intent.action = action
        return PendingIntent.getBroadcast(context, 0, intent, 0)
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }


    companion object {
        const val SHARED_PREFS = "prefs"
        const val KEY_BUTTON_TEXT = "keyButtonText"
        const val ACTION_REROLL = "action.REROLL"
    }

    fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {

        val views = RemoteViews(context.packageName, R.layout.test_widget)

        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.data = Uri.parse("https://google.com")
        val pendingIntent1 = PendingIntent.getActivity(context, 0, intent, 0)
        views.setOnClickPendingIntent(R.id.button1, pendingIntent1)

//        val searchForRestaurants = Uri.parse("geo:0,0?q=restaurants")
//        val mapIntent = Intent(Intent.ACTION_VIEW, searchForRestaurants)
//        mapIntent.setPackage("com.google.android.apps.maps")
//
//        val pendingIntent2 = PendingIntent.getActivity(context, 0, mapIntent, 0)
//        views.setOnClickPendingIntent(R.id.button2, pendingIntent2)


        val youtubeIntent = context.packageManager.getLaunchIntentForPackage("com.google.android.youtube");
        val pendingIntent2 = PendingIntent.getActivity(context, 0, youtubeIntent, 0)
        views.setOnClickPendingIntent(R.id.button2, pendingIntent2)


        if (suggestedApps.isNotEmpty()) {

            val intent3 = Intent(Intent.ACTION_VIEW)
            intent3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent3.data = Uri.parse(suggestedApps.entries.first().value)
            print(suggestedApps.entries.first().value)
            views.setCharSequence(R.id.button3, "setText", suggestedApps.entries.first().key)

            val pendingIntent3 = PendingIntent.getActivity(context, 0, intent3, 0)
            views.setOnClickPendingIntent(R.id.button3, pendingIntent3)

            var icon = context.packageManager.getApplicationIcon("com.google.android.apps.maps").toBitmap()

            views.setImageViewBitmap(R.id.icon_test, icon)
            views.setOnClickPendingIntent(R.id.icon_test, pendingIntent2)


        }



        views.setOnClickPendingIntent(
            R.id.reroll_button, getPendingSelfIntent(
                context,
                TestWidget.ACTION_REROLL
            )
        );

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }


}



