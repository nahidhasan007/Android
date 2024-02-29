package net.sharetrip.b2b.localdb

import androidx.room.*
import net.sharetrip.b2b.view.flight.booking.model.Passenger

@Dao
interface PassengerDao {
    @Query("SELECT * FROM passenger WHERE id = :passengerId")
    suspend fun getPassengerByID(passengerId: String): Passenger

    @Query("SELECT * FROM passenger")
    suspend fun getPassengerList(): List<Passenger>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePassenger(passenger: Passenger)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePassengerList(passengers: List<Passenger>)

    @Update
    suspend fun updatePassenger(passenger: Passenger)

    @Query("DELETE FROM passenger")
    suspend fun deleteAllPassenger()
}
