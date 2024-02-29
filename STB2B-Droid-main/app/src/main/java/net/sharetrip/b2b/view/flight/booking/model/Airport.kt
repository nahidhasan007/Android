package net.sharetrip.b2b.view.flight.booking.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "airports", indices = [Index(value = ["name"], unique = true)])
data class Airport (
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int = 0,
    @ColumnInfo(name = "iata") var iata: String,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "city") var city: String
)
 : Parcelable
