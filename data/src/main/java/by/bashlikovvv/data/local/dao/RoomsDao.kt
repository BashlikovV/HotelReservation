package by.bashlikovvv.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import by.bashlikovvv.data.local.contract.RoomContract.RoomsTable
import by.bashlikovvv.data.local.model.RoomsItemEntity

@Dao
interface RoomsDao {

    @Query("SELECT * FROM ${RoomsTable.TABLE_NAME}")
    fun selectRooms(): List<RoomsItemEntity?>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRooms(roomEntities: List<RoomsItemEntity>)
}