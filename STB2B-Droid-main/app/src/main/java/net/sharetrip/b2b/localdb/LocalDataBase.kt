package net.sharetrip.b2b.localdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import net.sharetrip.b2b.util.Converters
import net.sharetrip.b2b.view.authentication.model.UserProfile
import net.sharetrip.b2b.view.flight.booking.model.Airport
import net.sharetrip.b2b.view.flight.booking.model.FlightSearch
import net.sharetrip.b2b.view.flight.booking.model.Passenger
import net.sharetrip.b2b.view.flight.history.reissue_change_date.searchairport.AirportDao
import net.sharetrip.b2b.view.more.model.QuickPassenger

@Database(
    entities = [UserProfile::class, FlightSearch::class, Passenger::class , QuickPassenger::class, Airport::class
    ], version = 3, exportSchema = true
)
@TypeConverters(Converters::class)
abstract class LocalDataBase : RoomDatabase() {

    abstract fun userProfileDao(): UserProfileDao

    abstract fun flightSearchDao(): FlightSearchDao

    abstract fun passengerDao(): PassengerDao

    abstract fun quickPassengerDao(): QuickPassengerDao

    abstract fun airportDao(): AirportDao

    companion object {
        @Volatile
        private var INSTANCE: LocalDataBase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE IF NOT EXISTS `QuickPassenger` (`id` INTEGER, `titleName` TEXT," +
                            "`firstName` TEXT, `lastName` TEXT,`gender` TEXT, `nationality` TEXT,`dateOfBirth` TEXT, `passportNumber` TEXT, `frequentFlyerNumber` TEXT, `passportExpireDate` TEXT, `seatPreference` TEXT,`mealPreference` TEXT," +
                            "`wheelChair` TEXT, `passportCopy` TEXT, `visaCopy` TEXT,`travellerType` TEXT, `email` TEXT,`mobileNumber` TEXT, PRIMARY KEY(`id`))"
                )
            }
        }

        private val MIGRATION_2_3 = object: Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS `airports` (
                        `id` INTEGER NOT NULL, 
                        `iata` TEXT NOT NULL, 
                        `name` TEXT NOT NULL, 
                        `city` TEXT NOT NULL, 
                        PRIMARY KEY(`id`)
                    );
                """.trimIndent())

                database.execSQL("""CREATE UNIQUE INDEX IF NOT EXISTS index_airports_name ON `airports`(`name`);""")
            }

        }

        fun getDataBase(context: Context): LocalDataBase {
            val tempInstance = INSTANCE
            if (tempInstance != null)
                return tempInstance

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalDataBase::class.java, "notifier_database"
                ).addMigrations(MIGRATION_1_2, MIGRATION_2_3).fallbackToDestructiveMigration().build()

                INSTANCE = instance
                return instance
            }
        }
    }
}
