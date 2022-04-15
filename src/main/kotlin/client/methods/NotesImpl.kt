package client.methods

import objects.Note
import client.requests.notes.*

interface NotesImpl {

    fun add(newNote: NewNote): Note

    fun get(getUserNotes: GetUserNotes): List<Note>

    fun getById(getUserNoteById: GetUserNoteById): Note

    fun edit(editNote: EditNote): Boolean

    fun delete(deleteNote: DeleteNote): Boolean

}