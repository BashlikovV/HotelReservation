package by.bashlikovvv.domain.model

data class RoomItem(
    override val id: Long = 0L,
    val name: String = "",
    val price: Int = 0,
    val pricePer: String = "",
    val peculiarities: List<String> = emptyList(),
    val imageUrls: List<String> = emptyList()
) : Item(id) {

    fun isEmpty() = name.isEmpty()
}