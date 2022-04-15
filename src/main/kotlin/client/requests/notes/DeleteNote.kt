package client.requests.notes

import client.requests.BaseRequest

data class DeleteNote(
    val noteId: Int
) : BaseRequest()
