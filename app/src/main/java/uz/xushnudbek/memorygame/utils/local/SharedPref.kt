package uz.xushnudbek.memorygame.utils.local

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

class SharedPref(context: Context) {
    private val pref: SharedPreferences = context.getSharedPreferences("my_shared", Context.MODE_PRIVATE)

    var sound: Boolean
        get() = pref.getBoolean("TOKEN", true)!!
        set(value) {
            pref.edit().putBoolean("TOKEN", value).apply()
        }

    var timer:Long
        get() = pref.getLong("TIMER", 0)
        set(value) {
            pref.edit().putLong("TIMER", value).apply()
        }

    fun clearData() {
        pref.edit().apply {
            //putBoolean("TOKEN", false)
            putLong("TIMER", 0)
        }.apply()

    }


}