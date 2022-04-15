package client.services

import client.methods.BaseImpl
import objects.BaseResponse
import client.requests.BaseRequest
import server.services.Server

object ApiService : BaseImpl {

    var ID = 1

    override fun execute(request: BaseRequest): BaseResponse {
        return Server.callBack(ID, request)
    }

}