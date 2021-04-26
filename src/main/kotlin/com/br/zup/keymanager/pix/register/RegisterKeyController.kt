package com.br.zup.keymanager.pix.register

import com.br.zup.ProtoRegisterKeyServiceGrpc
import com.br.zup.keymanager.pix.register.RegisterKeyRequest
import com.br.zup.keymanager.pix.register.RegisterKeyResponse
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.validation.Validated
import javax.inject.Inject
import javax.validation.Valid

@Controller
@Validated
class RegisterKeyController(@Inject val grpcClient: ProtoRegisterKeyServiceGrpc.ProtoRegisterKeyServiceBlockingStub) {

    @Post("/api/key/register")
    fun registerKey(@Valid @Body request: RegisterKeyRequest): HttpResponse<*> {
        val response = grpcClient.registerKey(request.toProtoRegisterKeyRequest())
        return HttpResponse.ok(RegisterKeyResponse(response.pixId))
    }
}