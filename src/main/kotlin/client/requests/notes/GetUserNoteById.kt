package client.requests.notes

import client.requests.BaseRequest

data class GetUserNoteById(
    val ownerId: Int,
    val noteId: Int
) : BaseRequest()
