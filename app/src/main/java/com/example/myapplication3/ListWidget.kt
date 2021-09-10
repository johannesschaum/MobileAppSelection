package com.example.myapplication3

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import android.widget.Toast


/**
 * Implementation of App Widget functionality.
 */
class ListWidget : AppWidgetProvider() {

    companion object {

        const val OPEN_APP = "openApp"
        const val APP_TO_OPEN = "app"
        const val ACTION_REROLL = "action.REROLL"
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray,
    ) {

        for (appWidgetId in appWidgetIds) {

            val serviceIntent = Intent(context, WidgetService::class.java)
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            serviceIntent.data = Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME))

            val clickIntent = Intent(context, ListWidget::class.java)
            clickIntent.action = OPEN_APP
            val clickPendingIntent = PendingIntent.getBroadcast(context,
                0, clickIntent, 0)


            val views = RemoteViews(context.packageName, R.layout.list_widget)

            views.setRemoteAdapter(R.id.widget_list_view, serviceIntent)
            views.setPendingIntentTemplate(R.id.widget_list_view, clickPendingIntent)

            views.setOnClickPendingIntent(
                R.id.button, getPendingSelfIntent(
                    context,
                    ACTION_REROLL
                )
            );

            print("LULW")


            appWidgetManager.updateAppWidget(appWidgetId,views)
        }
    }

    private fun getPendingSelfIntent(context: Context, action: String): PendingIntent? {
        val intent =
            Intent(context, javaClass) // An intent directed at the current class (the "self").
        intent.action = action
        return PendingIntent.getBroadcast(context, 0, intent, 0)
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (OPEN_APP == intent.action) {

            val packageName = intent.getStringExtra(APP_TO_OPEN)
            val launchIntent = context.packageManager.getLaunchIntentForPackage(packageName!!);
            context.startActivity(launchIntent);

        } else if (ACTION_REROLL.equals(intent.action)) {

            WidgetService.addApp()

            val appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID)

//            val appWidgetManager = AppWidgetManager.getInstance(context)
//            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,
//                R.id.example_widget_stack_view)


        }
        super.onReceive(context, intent)
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created

//        onUpdate(context)

    }

    private fun setRemoteAdapter(context: Context, views: RemoteViews) {
        views.setRemoteAdapter(
            R.id.widget_list_view,
            Intent(context, WidgetService::class.java)
        )
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private fun onUpdate(context: Context) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val thisAppWidgetComponentName = ComponentName(context.packageName, javaClass.name)
        val appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidgetComponentName)
        onUpdate(context, appWidgetManager, appWidgetIds)
    }


}


