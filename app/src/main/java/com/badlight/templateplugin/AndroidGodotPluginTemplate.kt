package com.badlight.templateplugin

import android.telephony.SmsManager
import android.util.Log
import androidx.collection.ArraySet
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin
import org.godotengine.godot.plugin.SignalInfo
import java.util.Date


class AndroidGodotPluginTemplate(godot: Godot) : GodotPlugin(godot) {

    override fun getPluginName(): String {
        return "AndroidGodotPluginTemplate"
    }
    override fun getPluginMethods(): List<String> {
        return listOf(
            "getHelloWorldNative",
            "signalTest",
            "signalTest2",
            "sendSms"
        )
    }
    fun getHelloWorldNative(): String {
        return "Hello World from Android"
    }

    fun signalTest() {
        Thread {
            Log.d("signalTest", "called")
            Thread.sleep(3000)
            emitSignal("wake_up")
        }.start()
    }

    fun signalTest2() {
        Thread {
            runCatching {
                Log.d("signalTest2", "called")
                Thread.sleep(3000)
                val date = Date()
                emitSignal("wake_up2", date.toString())
            }.onFailure {
                Log.e("signalTest2", "error", it)
            }
        }.start()
    }

    fun sendSms(number: String, message: String): String? {
        return try{
            val smsManager = SmsManager.getDefault() as SmsManager
            smsManager.sendTextMessage(number, null, message, null, null)
            "Ok"
        }catch(exp: Exception){
            exp.message?.toString()
        }
    }

    override fun getPluginSignals():Set<SignalInfo> {
        val signals = ArraySet<SignalInfo>()
        signals.add(SignalInfo("wake_up"))
        val c = String::class.java
        signals.add(SignalInfo("wake_up2", c))
        return signals
    }
}
