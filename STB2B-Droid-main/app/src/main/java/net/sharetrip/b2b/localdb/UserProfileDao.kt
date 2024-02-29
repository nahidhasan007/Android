package net.sharetrip.b2b.localdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import net.sharetrip.b2b.view.authentication.model.UserProfile

@Dao
interface UserProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUserProfile(user: UserProfile)

    @Query("SELECT * FROM userprofile")
    suspend fun getUserprofile(): UserProfile

    @Query("DELETE FROM userprofile")
    suspend fun deleteUser()
}
