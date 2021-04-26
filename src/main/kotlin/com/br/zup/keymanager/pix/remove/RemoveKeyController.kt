package com.br.zup.keymanager.pix.remove

import com.br.zup.ProtoRemoveKeyRequest
import com.br.zup.ProtoRemoveKeyServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.validation.Validated
import javax.inject.Inject
import javax.validation.Valid

@Controller
@Validated
class RemoveKeyController(@Inject val grpcClient: ProtoRemoveKeyServiceGrpc.ProtoRemoveKeyServiceBlockingStub) {

    @Delete("/api/key")
    fun removeKey(@Valid @Body request: RemoveKeyRequest): HttpResponse<Any> {
        val response = grpcClient.removeKey(request.toProtoRemoveRequest())
        return HttpResponse.ok(RemoveKeyResponse(response.pixId, response.clientId))
    }
}