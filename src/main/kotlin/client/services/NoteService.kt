package client.services

import client.methods.CommentsImpl
import client.methods.NotesImpl
import client.requests.notes.*
import objects.*

object NoteService : NotesImpl, CommentsImpl {

    private val api = ApiService

    override fun add(newNote: NewNote): Note {
        return api.execute(newNote) as Note
    }

    override fun get(getUserNotes: GetUserNotes): List<Note> {
        return (api.execute(getUserNotes) as UserNoteList).notes
    }

    override fun getById(getUserNoteById: GetUserNoteById): Note {
        return api.execute(getUserNoteById) as Note
    }

    override fun edit(editNote: EditNote): Boolean {
        return (api.execute(editNote) as Success).isSuccess
    }

    override fun delete(deleteNote: DeleteNote): Boolean {
        return (api.execute(deleteNote) as Success).isSuccess
    }

    override fun createComment(newComment: NewComment): Comment {
        return api.execute(newComment) as Comment
    }

    override fun getComments(getNoteComments: GetNoteComments): List<Comment> {
        return (api.execute(getNoteComments) as NoteCommentList).comments
    }

    override fun getCommentById(getNoteCommentById: GetNoteCommentById): Comment {
        return api.execute(getNoteCommentById) as Comment
    }

    override fun editComment(editComment: EditComment): Boolean {
        return (api.execute(editComment) as Success).isSuccess
    }

    override fun deleteComment(deleteComment: DeleteComment): Boolean {
        return (api.execute(deleteComment) as Success).isSuccess
    }

    override fun restoreComment(restoreComment: RestoreComment): Boolean {
        return (api.execute(restoreComment) as Success).isSuccess
    }


}