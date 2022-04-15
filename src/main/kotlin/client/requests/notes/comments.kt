package client.requests.notes

import client.requests.BaseRequest

data class NewComment(
    val noteId: Int,
    val ownerId: Int,
    val message: String
) : BaseRequest()

data class GetNoteComments(
    val noteId: Int,
    val ownerId: Int,
    val sort: Boolean = true,
    val offset: Int = 0,
    val count: Int = 20
) : BaseRequest()

data class GetNoteCommentById(
    val noteId: Int,
    val ownerId: Int,
    val commentId: Int
) : BaseRequest()

data class EditComment(
    val noteId: Int,
    val ownerId: Int,
    val commentId: Int,
    val message: String
) : BaseRequest()

data class DeleteComment(
    val ownerId: Int,
    val noteId: Int,
    val commentId: Int
) : BaseRequest()

data class RestoreComment(
    val ownerId: Int,
    val noteId: Int,
    val commentId: Int
) : BaseRequest()