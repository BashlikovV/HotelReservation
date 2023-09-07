package by.bashlikovvv.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import by.bashlikovvv.data.local.contract.RoomContract

@Entity(tableName = RoomContract.RoomsTable.TABLE_NAME)
data class RoomsItemEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = RoomContract.RoomsTable.COLUMN_ID) val id: Long,
    @ColumnInfo(name = RoomContract.RoomsTable.COLUMN_NAME) val name: String,
    @ColumnInfo(name = RoomContract.RoomsTable.COLUMN_PRICE) val price: Int,
    @ColumnInfo(name = RoomContract.RoomsTable.COLUMN_PRICE_PER) val pricePer: String,
    @ColumnInfo(name = RoomContract.RoomsTable.COLUMN_PECULIARITIES) val peculiarities: List<String>,
    @ColumnInfo(name = RoomContract.RoomsTable.COLUMN_IMAGE_URLS) val imageUrls: List<String>
)