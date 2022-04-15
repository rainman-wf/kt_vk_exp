package objects

data class Comment(
    val id: Int,
    val fromId: Int,
    val noteId: Int,
    val ownerId: Int,
    val date: Long,
    val message: String
) : BaseResponse()