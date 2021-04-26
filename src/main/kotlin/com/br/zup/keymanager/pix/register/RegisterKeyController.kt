package com.br.zup.keymanager.pix.register

import com.br.zup.ProtoRegisterKeyServiceGrpc
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.uri.UriBuilder
import io.micronaut.validation.Validated
import javax.inject.Inject
import javax.validation.Valid

@Controller
@Validated
class RegisterKeyController(@Inject val grpcClient: ProtoRegisterKeyServiceGrpc.ProtoRegisterKeyServiceBlockingStub) {

    @Post("/api/key/register")
    fun registerKey(@Valid @Body request: RegisterKeyRequest): HttpResponse<Any> {
        val response = grpcClient.registerKey(request.toProtoRegisterKeyRequest())
        val uri = UriBuilder.of("/api/query/{id}")
            .expand(mutableMapOf("id" to response.pixId))
        return HttpResponse.created(RegisterKeyResponse(response.pixId), uri)
    }
}