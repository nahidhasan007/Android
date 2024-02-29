package net.sharetrip.b2b.localdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.sharetrip.b2b.view.flight.booking.model.FlightSearch

@Dao
interface FlightSearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFlightSearchQuery(flightSearch: FlightSearch)

    @Query("SELECT * FROM flight_search_table where trip_type = :tripType")
    suspend fun getFlight(tripType: String?): FlightSearch
}
