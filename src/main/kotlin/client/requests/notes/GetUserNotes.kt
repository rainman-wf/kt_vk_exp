package client.requests.notes

import client.requests.BaseRequest

data class GetUserNotes(
    val ownerId: Int,
    val offset: Int = 0,
    val count: Int = 20,
    val sort: Boolean = true
) : BaseRequest()
