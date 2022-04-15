package client.services

import client.exceptions.*
import client.requests.BaseRequest
import objects.Note
import client.requests.notes.*
import objects.Comment
import org.junit.Test

import org.junit.Assert.*

class NoteServiceTest {

    private val service = NoteService
    private val api = ApiService

    @Test
    fun add() {
        api.ID = 1
        val note = service.add(NewNote("title", "text"))
        assertEquals(note, service.getById(GetUserNoteById(1, note.id)))
    }

    @Test (expected = NoteNotFound::class)
    fun getById () {
        api.ID = 1
        service.getById(GetUserNoteById(api.ID, 150))
    }
    @Test
    fun get() {
        api.ID = 1
        for (i in 0..10) service.add(NewNote("note #$i", "text of note #$i"))

        val offset = 2
        val count = 4

        val notes = service.get(GetUserNotes(1, offset = offset, count = count))

        val first = notes.first().id
        val expect = mutableListOf<Note>()
        for (index in 0 until count) expect.add(service.getById(GetUserNoteById(1, first + index)))

        assertEquals(expect, notes)
    }

    @Test
    fun get_reversed() {
        api.ID = 1
        for (i in 0..10) service.add(NewNote("note #$i", "text of note #$i"))

        val offset = 2
        val count = 4

        val notes = service.get(GetUserNotes(1, sort = false, offset = offset, count = count))

        val first = notes.first().id
        val expect = mutableListOf<Note>()
        for (index in 0 until count) expect.add(service.getById(GetUserNoteById(1, first - index)))

        assertEquals(expect, notes)
    }

    @Test (expected = NoteNotFound::class)
    fun get_offset_is_out() {
        api.ID = 1
        for (i in 0..10) service.add(NewNote("note #$i", "text of note #$i"))

        val offset = 50
        val count = 4

        service.get(GetUserNotes(1, sort = false, offset = offset, count = count))

    }

    @Test
    fun edit() {
        api.ID = 1
        val note = service.add(NewNote("title", "text"))
        val result = service.edit(EditNote(note.id, "new Title", "new Text"))
        assertTrue(result)
    }

    @Test (expected = InvalidUser::class)
    fun edit_not_valid_user() {

        api.ID = 3
        service.edit(EditNote(0, "new Title", "new Text"))

    }

    @Test
    fun edit_failed() {
        api.ID = 1
        service.add(NewNote("title", "text"))
        val result = service.edit(EditNote(150, "new Title", "new Text"))
        assertFalse(result)
    }

    @Test
    fun delete() {
        api.ID = 1
        val note = service.add(NewNote("title", "text"))
        val result = service.delete(DeleteNote(note.id))
        assertTrue(result)
    }

    @Test
    fun delete_failed() {
        api.ID = 1
        service.add(NewNote("title", "text"))
        val result = service.delete(DeleteNote(50))
        assertFalse(result)
    }

    @Test
    fun createComment() {
        api.ID = 1
        val note = service.add(NewNote("title", "text"))
        val comment = service.createComment(NewComment(note.id, api.ID, "new comment"))
        assertEquals(comment, service.getCommentById(GetNoteCommentById(note.id, api.ID, comment.id)))
    }

    @Test
    fun getComments() {
        api.ID = 1
        val note = service.add(NewNote("title", "text"))

        for (i in 0..10) service.createComment(NewComment(note.id, api.ID, "comment #$i"))

        val count = 3

        val comments = service.getComments(GetNoteComments(note.id, api.ID, count = count))

        val first = comments.first().id
        val expect = mutableListOf<Comment>()
        for (index in 0 until count) expect.add(service.getCommentById(GetNoteCommentById(note.id, api.ID, first + index)))

        assertEquals(expect, comments)

    }

    @Test
    fun getComments_reversed() {
        api.ID = 2
        val note = service.add(NewNote("title", "text"))

        for (i in 0..10) service.createComment(NewComment(note.id, api.ID, "comment #$i"))

        val count = 3

        val comments = service.getComments(GetNoteComments(note.id, api.ID, sort = false, count = count))

        val first = comments.first().id
        val expect = mutableListOf<Comment>()
        for (index in 0 until count)
            expect.add(service.getCommentById(GetNoteCommentById(note.id, api.ID, first - index)))

        assertEquals(expect, comments)

    }

    @Test (expected = CommentNotFound::class)
    fun getComments_offset_out() {
        api.ID = 2
        val note = service.add(NewNote("title", "text"))

        for (i in 0..10) service.createComment(NewComment(note.id, api.ID, "comment #$i"))

        val count = 3

        service.getComments(GetNoteComments(note.id, api.ID, offset = 15, sort = false, count = count))
    }

    @Test (expected = CommentNotFound::class)
    fun getCommentById_failed () {
        api.ID = 1
        val note = service.add(NewNote("title", "text"))
        service.getCommentById(GetNoteCommentById(note.id, api.ID, 150))
    }

    @Test
    fun editComment() {
        api.ID = 1
        val note = service.add(NewNote("title", "text"))
        val comment = service.createComment(NewComment(note.id, api.ID, "new comment"))
        val result = service.editComment(EditComment(note.id, api.ID, comment.id, "edit comment"))
        assertTrue(result)
    }

    @Test (expected = AccessDenied::class)
    fun editComment_access_denied() {
        api.ID = 1
        val note = service.add(NewNote("title", "text"))
        val comment = service.createComment(NewComment(note.id, api.ID, "new comment"))
        api.ID = 2
        service.editComment(EditComment(note.id, 1, comment.id, "edit comment"))
    }

    @Test
    fun deleteComment () {
        api.ID = 1
        val note = service.add(NewNote("title", "text"))
        val comment = service.createComment(NewComment(note.id, api.ID, "new comment"))
        val result = service.deleteComment(DeleteComment(api.ID, note.id, comment.id))
        assertTrue(result)
    }

    @Test (expected = CommentAlreadyDeleted::class)
    fun deleteComment_deleted () {
        api.ID = 1
        val note = service.add(NewNote("title", "text"))
        val comment = service.createComment(NewComment(note.id, api.ID, "new comment"))
        service.deleteComment(DeleteComment(api.ID, note.id, comment.id))
        service.deleteComment(DeleteComment(api.ID, note.id, comment.id))
    }

    @Test (expected = AccessDenied::class)
    fun deleteComment_access_denied() {
        api.ID = 1
        val note = service.add(NewNote("title", "text"))
        val comment = service.createComment(NewComment(note.id, api.ID, "new comment"))
        api.ID = 2
        service.deleteComment(DeleteComment(1, note.id, comment.id))
    }

    @Test
    fun restoreComment () {
        api.ID = 1
        val note = service.add(NewNote("title", "text"))
        val comment = service.createComment(NewComment(note.id, api.ID, "new comment"))
        service.deleteComment(DeleteComment(api.ID, note.id, comment.id))
        val result = service.restoreComment(RestoreComment(api.ID, note.id, comment.id))
        assertTrue(result)
    }

    @Test (expected = CommentIsNotDeleted::class)
    fun restoreComment_not_deleted () {
        api.ID = 1
        val note = service.add(NewNote("title", "text"))
        val comment = service.createComment(NewComment(note.id, api.ID, "new comment"))
        val result = service.restoreComment(RestoreComment(api.ID, note.id, comment.id))
        assertTrue(result)
    }

    @Test (expected = AccessDenied::class)
    fun restoreComment_access_denied () {
        api.ID = 1
        val note = service.add(NewNote("title", "text"))
        val comment = service.createComment(NewComment(note.id, api.ID, "new comment"))
        service.deleteComment(DeleteComment(api.ID, note.id, comment.id))
        api.ID = 2
        service.restoreComment(RestoreComment(1, note.id, comment.id))
    }

    @Test (expected = CommentNotFound::class)
    fun restoreComment_not_found () {
        api.ID = 1
        val note = service.add(NewNote("title", "text"))
        val comment = service.createComment(NewComment(note.id, api.ID, "new comment"))
        service.deleteComment(DeleteComment(api.ID, note.id, comment.id))
        service.restoreComment(RestoreComment(api.ID, note.id, 200))
    }

    @Test (expected = BadRequest::class)
    fun badRequest () {
        api.ID = 1
        api.execute(BaseRequest())
    }
}