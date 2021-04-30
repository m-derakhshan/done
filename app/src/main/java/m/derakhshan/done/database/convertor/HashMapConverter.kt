package m.derakhshan.done.database.convertor

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class HashMapConverter {

    @TypeConverter
    fun toHashMap(value: String): Map<String, String> =
        Gson().fromJson(value, object : TypeToken<Map<String, String>>() {}.type)

    @TypeConverter
    fun fromHashMap(value: Map<String, String>): String =
        Gson().toJson(value)


}