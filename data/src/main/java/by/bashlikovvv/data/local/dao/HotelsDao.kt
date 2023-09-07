package by.bashlikovvv.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import by.bashlikovvv.data.local.model.HotelsItemEntity
import by.bashlikovvv.data.local.contract.RoomContract.HotelsTable

@Dao
interface HotelsDao {

    @Query("SELECT * FROM ${HotelsTable.TABLE_NAME} WHERE ${HotelsTable.COLUMN_ID} = :id")
    fun selectHotelById(id: Long): HotelsItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHotels(itemEntities: List<HotelsItemEntity>)
}