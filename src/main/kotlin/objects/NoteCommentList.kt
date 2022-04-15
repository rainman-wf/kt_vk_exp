package objects

data class NoteCommentList(
    val comments: List<Comment>
) : BaseResponse()