package objects

data class Note(
    val id: Int,
    val ownerId: Int,
    val title: String,
    val text: String,
    val date: Long,
    val comments: MutableMap<Int, Comment> = mutableMapOf(),
    val deletedComments: MutableMap<Int, Comment> = mutableMapOf(),
    val commentsCount: Int = 0
    ) : BaseResponse()