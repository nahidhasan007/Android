package net.sharetrip.b2b.localdb

import androidx.room.*
import net.sharetrip.b2b.view.more.model.QuickPassenger

@Dao
interface QuickPassengerDao {
    @Query("SELECT * FROM QuickPassenger WHERE id = :passengerId")
    suspend fun getQuickPassengerByID(passengerId: String): QuickPassenger

    @Query("SELECT * FROM QuickPassenger")
    suspend fun getQuickPassengerList(): List<QuickPassenger>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveQuickPassenger(passenger: QuickPassenger)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveQuickPassengerList(passengers: List<QuickPassenger>)

    @Update
    suspend fun updateQuickPassenger(passenger: QuickPassenger)

    @Query("DELETE FROM QuickPassenger")
    suspend fun deleteAllQuickPassenger()
}
