package by.bashlikovvv.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import by.bashlikovvv.data.local.converters.ListTypeConverter
import by.bashlikovvv.data.local.dao.HotelsDao
import by.bashlikovvv.data.local.dao.RoomsDao
import by.bashlikovvv.data.local.model.HotelsItemEntity
import by.bashlikovvv.data.local.model.RoomsItemEntity

@Database(
    entities = [HotelsItemEntity::class, RoomsItemEntity::class],
    version = 1
)
@TypeConverters(ListTypeConverter::class)
abstract class HotelReservationDatabase : RoomDatabase() {
    abstract val hotelsDao: HotelsDao
    abstract val roomsDao: RoomsDao
}