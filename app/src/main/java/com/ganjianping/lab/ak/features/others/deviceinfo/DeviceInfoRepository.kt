package com.ganjianping.lab.ak.features.others.deviceinfo

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import com.ganjianping.lab.ak.features.others.deviceinfo.InfoRow

class DeviceInfoRepository(private val context: Context) {
    fun read(): List<InfoRow> {
        val memoryInfo = ActivityManager.MemoryInfo()
        (context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).getMemoryInfo(memoryInfo)
        val displayMetrics = context.resources.displayMetrics

        return listOf(
            InfoRow("Android version", Build.VERSION.RELEASE),
            InfoRow("SDK level", Build.VERSION.SDK_INT.toString()),
            InfoRow("Codename", Build.VERSION.CODENAME),
            InfoRow("Screen", "${displayMetrics.widthPixels} × ${displayMetrics.heightPixels}"),
            InfoRow("Manufacturer", Build.MANUFACTURER),
            InfoRow("Model", Build.MODEL),
            InfoRow("Device", Build.DEVICE),
            InfoRow("Hardware", Build.HARDWARE),
            InfoRow("CPU cores", Runtime.getRuntime().availableProcessors().toString()),
            InfoRow("Memory", "${memoryInfo.totalMem / (1024 * 1024)} MB"),
            InfoRow("Supported ABIs", Build.SUPPORTED_ABIS.joinToString())
        )
    }
}
