package com.example.myapplication3

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.graphics.drawable.toBitmap
import com.example.myapplication3.ListWidget.Companion.APP_TO_OPEN



class WidgetService: RemoteViewsService() {




    companion object{

        val app1 = listOf<String>("Google Chrome", "com.android.chrome")
        val app2 = listOf<String>("Kalender", "com.google.android.calendar")
        val app3 = listOf<String>("YouTube", "com.google.android.youtube")

        val exampleData = mutableListOf(app1, app2, app3)


        public  fun addApp(){

            val app = mutableListOf<String>()


            app.add("GMail")
            app.add("com.google.android.gm")

            exampleData.add(app)

        }


    }



    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return ItemFactory(applicationContext, intent)
    }


    internal inner class ItemFactory(private val context: Context, intent: Intent) :
        RemoteViewsFactory {

        private val appWidgetId: Int
//        private var exampleData = arrayOf("one", "two", "three", "four",
//            "five", "six", "seven", "eight", "nine", "ten")

        init {
            appWidgetId = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID)
        }

        override fun onCreate() {

        }

        override fun onDataSetChanged() {
        }

        override fun onDestroy() {
        }

        override fun getCount(): Int {
            return exampleData.size
        }

        override fun getViewAt(position: Int): RemoteViews {
            val views = RemoteViews(context.packageName, R.layout.list_item)

            views.setTextViewText(R.id.app_name, exampleData[position][0])

            var icon = context.packageManager.getApplicationIcon(exampleData[position][1]).toBitmap()
            views.setImageViewBitmap(R.id.app_icon, icon)

            val intent = Intent()
            intent!!.putExtra(APP_TO_OPEN, exampleData[position][1])
            views.setOnClickFillInIntent(R.id.item, intent)

            return views
        }



        override fun getLoadingView(): RemoteViews? {
            return null
        }

        override fun getViewTypeCount(): Int {
            return 1
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun hasStableIds(): Boolean {
           return true
        }


    }
}