package objects

data class UserNoteList(
    val notes: List<Note>
) : BaseResponse()
