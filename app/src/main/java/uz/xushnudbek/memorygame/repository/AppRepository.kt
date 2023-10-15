package uz.xushnudbek.memorygame.repository

import android.util.Log
import uz.xushnudbek.memorygame.R
import uz.xushnudbek.memorygame.data.CardData
import uz.xushnudbek.memorygame.data.LevelEnum

class AppRepository private constructor() {
    companion object {
        private lateinit var instance: AppRepository
        fun getInstance(): AppRepository {
            if (!(::instance.isInitialized))
                instance = AppRepository()
            return instance
        }
    }

    private val ls = ArrayList<CardData>()

    init {
        ls.add(CardData(1, R.drawable.img_1))
        ls.add(CardData(2, R.drawable.img_2))
        ls.add(CardData(3, R.drawable.img_3))
        ls.add(CardData(4, R.drawable.img_4))
        ls.add(CardData(5, R.drawable.img_5))
        ls.add(CardData(6, R.drawable.img_6))
        ls.add(CardData(7, R.drawable.img_7))
        ls.add(CardData(8, R.drawable.img_8))
        ls.add(CardData(9, R.drawable.img_9))
        ls.add(CardData(10, R.drawable.img_10))
        ls.add(CardData(11, R.drawable.img_11))
        ls.add(CardData(12, R.drawable.img_12))
        ls.add(CardData(13, R.drawable.img_13))
        ls.add(CardData(14, R.drawable.img_14))
        ls.add(CardData(15, R.drawable.img_15))
        ls.add(CardData(16, R.drawable.img_16))
        ls.add(CardData(17, R.drawable.img_17))
        ls.add(CardData(18, R.drawable.img_18))
        ls.add(CardData(19, R.drawable.img_19))
        ls.add(CardData(20, R.drawable.img_20))
        ls.add(CardData(21, R.drawable.img_21))
        ls.add(CardData(22, R.drawable.img_22))
        ls.add(CardData(23, R.drawable.img_23))
        ls.add(CardData(24, R.drawable.img_24))
        ls.add(CardData(25, R.drawable.img_25))
        ls.add(CardData(26, R.drawable.img_26))
        ls.add(CardData(27, R.drawable.img_27))
        ls.add(CardData(28, R.drawable.img_28))
        ls.add(CardData(29, R.drawable.img_29))
        ls.add(CardData(30, R.drawable.img_30))
        ls.add(CardData(31, R.drawable.img_31))
        ls.add(CardData(32, R.drawable.img_32))
        ls.add(CardData(33, R.drawable.img_33))
        ls.add(CardData(34, R.drawable.img_34))
        ls.add(CardData(35, R.drawable.img_35))
        ls.add(CardData(36, R.drawable.img_36))
        ls.add(CardData(37, R.drawable.img_37))
        ls.add(CardData(38, R.drawable.img_38))
        ls.add(CardData(39, R.drawable.img_39))
        ls.add(CardData(40, R.drawable.img_40))
        ls.add(CardData(41, R.drawable.img_41))
        ls.add(CardData(42, R.drawable.img_42))
        ls.add(CardData(43, R.drawable.img_43))
        ls.add(CardData(44, R.drawable.img_44))
        ls.add(CardData(45, R.drawable.img_45))
        ls.add(CardData(46, R.drawable.img_46))
        ls.add(CardData(47, R.drawable.img_47))
        ls.add(CardData(48, R.drawable.img_48))
        ls.add(CardData(49, R.drawable.img_49))
        ls.add(CardData(50, R.drawable.img_50))
    }

    fun getCardDataByLevel(level: LevelEnum): List<CardData> {
        val count = level.horCount * level.verCount
        ls.shuffle()
        var result = ArrayList<CardData>(count)
        val l = ls.subList(0, count / 2)
        result.addAll(l)
        result.addAll(l)
        result.shuffle()
        for (i in result.indices) {
            Log.d("TTT", "id = ${result[i].id}")
        }
        return result
    }


}