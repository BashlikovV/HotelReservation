package by.bashlikovvv.domain.model

data class Description(
    val description: String = "",
    val peculiarities: List<String> = emptyList(),
    override val id: Long = 0L
) : Item(id)