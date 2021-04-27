package com.br.zup.keymanager.pix.list

import com.br.zup.ProtoListKeyRequest
import com.br.zup.ProtoListKeyServiceGrpc
import com.br.zup.keymanager.shared.validations.ValidUUID
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue
import io.micronaut.validation.Validated
import javax.inject.Inject
import javax.validation.constraints.NotBlank

@Controller
@Validated
class ListKeysController(
    @Inject val grpcClient: ProtoListKeyServiceGrpc.ProtoListKeyServiceBlockingStub
) {

    @Get("/api/key/list")
    fun listKeys(
        @QueryValue("clientid")
        @NotBlank
        @ValidUUID
        clientId: String): HttpResponse<Any> {

        val response = grpcClient.listKeys(ProtoListKeyRequest.newBuilder()
            .setClientId(clientId)
            .build())

        return HttpResponse.ok(response.toResponseDTO())

    }
}