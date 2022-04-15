package client.requests.notes

import client.requests.BaseRequest

data class NewNote(
    val title: String,
    val text: String
) : BaseRequest()
