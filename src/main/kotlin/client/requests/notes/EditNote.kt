package client.requests.notes

import client.requests.BaseRequest

data class EditNote(
    val noteId: Int,
    val title: String,
    val text: String
) : BaseRequest()