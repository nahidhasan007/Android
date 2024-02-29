package net.sharetrip.b2b.view.flight.history.reissue_change_date.searchairport

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import net.sharetrip.b2b.view.flight.booking.model.Airport

@Dao
interface AirportDao {
    @Insert
    fun insert(airport: Airport)

    @Query("DELETE FROM airports")
    fun deleteAllAirports()

    @Query("SELECT * FROM airports ")
    suspend fun getAirports(): List<Airport>
}
