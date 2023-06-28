package com.dxfeed.perftestapp.tools

import android.content.Context
import android.os.Process
import com.devexperts.logging.Logging
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


class SystemUsageManager(private val context: Context) {
    private val logger = Logging.getLogging(SystemUsageManager::class.java)
    private val numberOfCores = Runtime.getRuntime().availableProcessors()
    fun memoryUsage(): Long {
        val runtime = Runtime.getRuntime();
        val usedMemInMB=(runtime.totalMemory() - runtime.freeMemory()) / 1048576L;
        return usedMemInMB
    }

    fun cpuUsage(): Double {
        return sampleCPU() / numberOfCores
    }

    private fun sampleCPU(): Double {
        println("start")
        println("start 1")
        val lMyProcessID = Process.myPid()
        val rate = 0
        try {
            var Result: String
            val p = Runtime.getRuntime().exec("top -o pid,%cpu -n 1 -p $lMyProcessID")
            val br = BufferedReader(InputStreamReader(p.getInputStream()))
            while (br.readLine().also { Result = it } != null) {
                // replace "com.example.fs" by your application
                if (Result.contains("$lMyProcessID")) {
                    val info = Result.trim { it <= ' ' }
                        .replace(" +".toRegex(), " ").split(" ".toRegex())
                        .dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                    return info.last().toDouble()
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        println("finish")
        return rate.toDouble()
    }
}