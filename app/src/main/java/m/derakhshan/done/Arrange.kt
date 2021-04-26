package m.derakhshan.done

import android.util.Log

class Arrange {

    fun persianConverter(number: String?): String {
        var result = ""
        if (number.isNullOrEmpty() || number == "null")
            return ""
        for (i in number)
            result += when (i) {
                '0' -> '۰'
                '1' -> '۱'
                '2' -> '۲'
                '3' -> '۳'
                '4' -> '۴'
                '5' -> '۵'
                '6' -> '۶'
                '7' -> '۷'
                '8' -> '۸'
                '9' -> '۹'
                else -> i
            }

        return result
    }


    fun englishConvert(number: String?): String {
        if (number.isNullOrEmpty())
            return ""
        val result = StringBuilder()
        for (i in number.indices) {
            when (number[i]) {
                '۱' -> result.append("1")
                '۲' -> result.append("2")
                '۳' -> result.append("3")
                '۴' -> result.append("4")
                '۵' -> result.append("5")
                '۶' -> result.append("6")
                '۷' -> result.append("7")
                '۸' -> result.append("8")
                '۹' -> result.append("9")
                '۰' -> result.append("0")
                else -> result.append(number[i])
            }
        }
        return result.toString()
    }


    fun persianConcatenate(first: String? = "", middle: String? = "", end: String? = ""): String {
        return persianConverter(first) + persianConverter(middle) + persianConverter(end)
    }

    fun concatenate(first: String? = "", middle: String? = "", end: String? = ""): String {
        return (first) + (middle) + (end)
    }


    fun generateID(date: String, time: String): Int {
        val resultDate = date.split("/")
        val resultTime = time.split(":")
        if (resultTime[0].length < 2)
            "0${resultTime[0]}"
        return ("${(resultDate[0].toInt() % 100)}${resultDate[1]}${resultDate[2]}" +
                (if (resultTime[0].length < 2) "0" + resultTime[0] else resultTime[0]) +
                (if (resultTime[1].length < 2) "0" + resultTime[1] else resultTime[1]) +
                if (resultTime[2].length < 2) "0" + resultTime[2] else resultTime[2]).toInt()

    }


}