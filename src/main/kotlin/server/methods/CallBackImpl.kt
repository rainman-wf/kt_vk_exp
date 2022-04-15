package server.methods

import objects.BaseResponse
import client.requests.BaseRequest

interface CallBackImpl {
    fun callBack (executorId: Int, request: BaseRequest) : BaseResponse
}