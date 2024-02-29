package net.sharetrip.b2b.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import net.sharetrip.b2b.view.flight.booking.model.Airport
import net.sharetrip.b2b.view.flight.booking.model.Flights
import net.sharetrip.b2b.view.flight.booking.model.TravellersInfo

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromTravellersInfo(travellersInfo: TravellersInfo): String {
        return gson.toJson(travellersInfo)
    }

    @TypeConverter
    fun toTravellersInfo(travellersInfo: String): TravellersInfo {
        return gson.fromJson(travellersInfo, TravellersInfo::class.java)
    }

    @TypeConverter
    fun fromAirport(airport: Airport): String {
        return gson.toJson(airport)
    }

    @TypeConverter
    fun toAirport(airport: String): Airport {
        return gson.fromJson(airport, Airport::class.java)
    }

/*    @TypeConverter
    fun listToString(value: ArrayList<String>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun stringToList(value: String): ArrayList<String> {
        val itemType = object : TypeToken<ArrayList<String>>() {}.type
        return gson.fromJson<ArrayList<String>>(value, itemType)
    }*/

    @TypeConverter
    fun stringToList(string: String): ArrayList<String> {
        val type = object : TypeToken<ArrayList<String>>() {}.type
        return Gson().fromJson(string, type)
    }

    @TypeConverter
    fun listToString(torrent: ArrayList<String>): String {
        val type = object : TypeToken<ArrayList<String>>() {}.type
        return Gson().toJson(torrent, type)
    }

    @TypeConverter
    fun fromFlights(flights: Flights?): String? {
        return gson.toJson(flights)
    }

    @TypeConverter
    fun toFlights(flights: String?): Flights? {
        return gson.fromJson(flights, Flights::class.java)
    }
}
