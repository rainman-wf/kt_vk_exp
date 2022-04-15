package client.methods

import client.requests.notes.*
import objects.Comment

interface CommentsImpl {

    fun createComment(newComment: NewComment): Comment

    fun getComments(getNoteComments: GetNoteComments): List<Comment>

    fun getCommentById(getNoteCommentById: GetNoteCommentById): Comment

    fun editComment(editComment: EditComment): Boolean

    fun deleteComment(deleteComment: DeleteComment): Boolean

    fun restoreComment(restoreComment: RestoreComment): Boolean

}