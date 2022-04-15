package client.methods

import objects.BaseResponse
import client.requests.BaseRequest

interface BaseImpl {
    fun execute (request: BaseRequest) : BaseResponse
}