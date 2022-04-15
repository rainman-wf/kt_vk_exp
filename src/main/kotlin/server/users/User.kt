package server.users

import objects.Note

data class User(
    val id: Int,
    val notes: MutableMap<Int, Note> = mutableMapOf()
)