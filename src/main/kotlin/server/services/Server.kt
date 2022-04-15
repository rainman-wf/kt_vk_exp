package server.services

import client.exceptions.*
import client.requests.*
import client.requests.notes.*
import objects.*
import server.methods.CallBackImpl
import server.users.User
import java.util.*
import kotlin.math.min

object Server : CallBackImpl {

    private val users = mutableMapOf(
        1 to User(1),
        2 to User(2)
    )

    private var noteId = 0

    override fun callBack(executorId: Int, request: BaseRequest): BaseResponse {

        val executor = getUser(executorId)
        val executorNotes = executor.notes

        when (request) {
            is NewNote -> {

                val note = Note(noteId, executorId, request.title, request.text, Date().time)

                executorNotes[noteId++] = note

                return note
            }

            is GetUserNoteById -> return getNote(request.ownerId, request.noteId)

            is GetUserNotes -> {

                val sorted = executorNotes.values.sortedWith { o1, o2 -> o1.id.compareTo(o2.id) }

                val size = sorted.size
                val offset = request.offset
                val count = request.count
                val range = offset + count
                val endPos = min(range, size)

                if (offset >= size) throw NoteNotFound()

                val result = sorted.subList(offset, endPos)

                return UserNoteList(if (request.sort) result else result.reversed())
            }

            is EditNote -> {

                val note = executorNotes[request.noteId] ?: return Success(false)

                executorNotes[request.noteId] = note.copy(title = request.title, text = request.text)

                return Success(true)
            }
            is DeleteNote -> {

                executorNotes.remove(request.noteId) ?: return Success(false)

                return Success(true)
            }
            is NewComment -> {

                val targetNoteId = request.noteId
                val ownerId = request.ownerId
                val message = request.message

                val note = getNote(ownerId, targetNoteId)

                var commentId = note.commentsCount
                val comments = note.comments

                val newComment = Comment(commentId, executorId, targetNoteId, ownerId, Date().time, message)

                comments[commentId] = newComment

                val newNote = note.copy(commentsCount = ++commentId)

                getUser(ownerId).notes[targetNoteId] = newNote

                return newComment
            }

            is GetNoteComments -> {

                val targetNoteId = request.noteId
                val ownerId = request.ownerId

                val note = getNote(ownerId, targetNoteId)

                val sorted = note.comments.values.sortedWith { o1, o2 -> o1.id.compareTo(o2.id) }

                val size = sorted.size
                val offset = request.offset
                val count = request.count
                val range = offset + count
                val endPos = min(range, size)

                if (offset >= size) throw CommentNotFound()

                val result = sorted.subList(offset, endPos)

                return NoteCommentList(if (request.sort) result else result.reversed())
            }

            is GetNoteCommentById -> return getComment(request.ownerId, request.noteId, request.commentId)

            is EditComment -> {

                val comment = getComment(request.ownerId, request.noteId, request.commentId)
                if (comment.fromId != executorId) throw AccessDenied()
                val editedComment = comment.copy(message = request.message)
                getNote(request.ownerId, request.noteId).comments[comment.id] = editedComment
                return Success(true)
            }

            is DeleteComment -> {

                val deletedList = getNote(request.ownerId, request.noteId).deletedComments
                val comments = getNote(request.ownerId, request.noteId).comments

                if (deletedList.containsKey(request.commentId)) throw CommentAlreadyDeleted()

                val comment = getComment(request.ownerId, request.noteId, request.commentId)

                if (comment.fromId != executorId) throw AccessDenied()

                deletedList[comment.id] = comment
                comments.remove(comment.id)

                return Success (true)
            }

            is RestoreComment -> {

                val deletedList = getNote(request.ownerId, request.noteId).deletedComments
                val comments = getNote(request.ownerId, request.noteId).comments

                if (comments.containsKey(request.commentId)) throw CommentIsNotDeleted()

                val comment = getDeletedComment(request.ownerId, request.noteId, request.commentId)

                if (comment.fromId != executorId) throw AccessDenied()

                comments[comment.id] = comment
                deletedList.remove(comment.id)

                return Success (true)
            }
        }
        throw BadRequest()
    }

    private fun getNote(ownerId: Int, noteId: Int) = getUser(ownerId).notes[noteId] ?: throw NoteNotFound()

    private fun getUser(userId: Int) = users[userId] ?: throw InvalidUser()

    private fun getComment(ownerId: Int, noteId: Int, commentId: Int) =
        getNote(ownerId, noteId).comments[commentId] ?: throw CommentNotFound()

    private fun getDeletedComment(ownerId: Int, noteId: Int, commentId: Int) =
        getNote(ownerId, noteId).deletedComments[commentId] ?: throw CommentNotFound()
}