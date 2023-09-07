package by.bashlikovvv.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import by.bashlikovvv.data.local.contract.RoomContract

@Entity(tableName = RoomContract.HotelsTable.TABLE_NAME)
data class HotelsItemEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = RoomContract.HotelsTable.COLUMN_ID) val id: Long,
    @ColumnInfo(name = RoomContract.HotelsTable.COLUMN_NAME) val name: String,
    @ColumnInfo(name = RoomContract.HotelsTable.COLUMN_ADDRESS) val address: String,
    @ColumnInfo(name = RoomContract.HotelsTable.COLUMN_MINIMAL_PRICE) val minimalPrice: Int,
    @ColumnInfo(name = RoomContract.HotelsTable.COLUMN_PRICE_FOR_IT) val priceForIt: String,
    @ColumnInfo(name = RoomContract.HotelsTable.COLUMN_RATING) val rating: Int,
    @ColumnInfo(name = RoomContract.HotelsTable.COLUMN_RATING_NAME) val ratingName: String,
    @ColumnInfo(name = RoomContract.HotelsTable.COLUMN_IMAGE_URLS) val imagesUrls: List<String>,
    @ColumnInfo(name = RoomContract.HotelsTable.COLUMN_DESCRIPTION) val description: String,
    @ColumnInfo(name = RoomContract.HotelsTable.COLUMN_PECULIARITIES) val peculiarities: List<String>
)