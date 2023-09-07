package by.bashlikovvv.data.local.contract

object RoomContract {

    const val DATABASE_NAME = "hotel_reservation.room"

    object HotelsTable {
        const val TABLE_NAME = "hotels"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_ADDRESS = "address"
        const val COLUMN_MINIMAL_PRICE = "minimalPrice"
        const val COLUMN_PRICE_FOR_IT = "priceForIt"
        const val COLUMN_RATING = "rating"
        const val COLUMN_RATING_NAME = "ratingName"
        const val COLUMN_IMAGE_URLS = "imageUrls"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_PECULIARITIES = "peculiarities"
    }

    object RoomsTable {
        const val TABLE_NAME = "rooms"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_PRICE = "price"
        const val COLUMN_PRICE_PER = "pricePer"
        const val COLUMN_PECULIARITIES = "peculiarities"
        const val COLUMN_IMAGE_URLS = "imageUrls"
    }
}