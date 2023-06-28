package com.dxfeed.perftestapp.tools

import android.content.Context
import android.os.Process
import com.devexperts.logging.Logging
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class SystemUsageManager(private val context: Context) {
    private val logger = Logging.getLogging(SystemUsageManager::class.java)
    private val runtime = Runtime.getRuntime()
    private val numberOfCores = runtime.availableProcessors()
    fun memoryUsage(): Long {
        val usedMemInMB=(runtime.totalMemory() - runtime.freeMemory()) / 1048576L;
        return usedMemInMB
    }

    fun cpuUsage(): Double {
        return sampleCPU() / numberOfCores
    }

    private fun sampleCPU(): Double {
        val lMyProcessID = Process.myPid()
        try {
            var result = ""
            val p = Runtime.getRuntime().exec("top -o pid,%cpu -n 1 -p $lMyProcessID")
            val br = BufferedReader(InputStreamReader(p.getInputStream()))
            while (br.readLine().also { result = it } != null) {
                if (result.contains("$lMyProcessID")) {
                    println(result)
                    val info = result.trim { it <= ' ' }
                        .replace(" +".toRegex(), " ").split(" ".toRegex())
                        .dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                    return info.last().toDouble()
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return 0.0
    }
}