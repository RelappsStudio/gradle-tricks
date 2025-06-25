package com.example.gradle_showcases

import android.app.AppOpsManager
import android.app.AsyncNotedAppOp
import android.app.SyncNotedAppOp
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import io.flutter.app.FlutterApplication

class Application : FlutterApplication() {

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun registerReceiver(receiver: BroadcastReceiver?, filter: IntentFilter?): Intent? {
        return if (Build.VERSION.SDK_INT >= 34 && applicationInfo.targetSdkVersion >= 34) {
            super.registerReceiver(receiver, filter, Context.RECEIVER_EXPORTED)
        } else {
            super.registerReceiver(receiver, filter, Context.RECEIVER_NOT_EXPORTED)
        }
    }
    
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            val appOpsCallback =
                    @RequiresApi(Build.VERSION_CODES.R)
                    object : AppOpsManager.OnOpNotedCallback() {
                        private fun logPrivateDataAccess(opCode: String, trace: String) {

                            Log.i(
                                    "COM.EXAMPLE.GRADLE_SHOWCASE",
                                    "Private data requested. " +
                                            "Operation: $opCode\nStack Trace:\n$trace"
                            )
                        }

                        @RequiresApi(Build.VERSION_CODES.R)
                        override fun onNoted(syncNotedAppOp: SyncNotedAppOp) {
                            logPrivateDataAccess(
                                    syncNotedAppOp.op,
                                    Throwable().stackTrace.toString()
                            )
                        }

                        @RequiresApi(Build.VERSION_CODES.R)
                        override fun onSelfNoted(syncNotedAppOp: SyncNotedAppOp) {
                            logPrivateDataAccess(
                                    syncNotedAppOp.op,
                                    Throwable().stackTrace.toString()
                            )
                        }

                        override fun onAsyncNoted(asyncNotedAppOp: AsyncNotedAppOp) {
                            logPrivateDataAccess(asyncNotedAppOp.op, asyncNotedAppOp.message)
                        }
                    }

            val appOpsManager = getSystemService(AppOpsManager::class.java) as AppOpsManager
            appOpsManager.setOnOpNotedCallback(mainExecutor, appOpsCallback)
        }
    }
}
