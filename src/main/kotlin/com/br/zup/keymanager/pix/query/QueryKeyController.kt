package com.br.zup.keymanager.pix.query

import com.br.zup.ProtoQueryKeyRequest
import com.br.zup.ProtoQueryKeyServiceGrpc
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
class QueryKeyController(
    @Inject val grpcClient: ProtoQueryKeyServiceGrpc.ProtoQueryKeyServiceBlockingStub
    ) {

    @Get("/api/key")
    fun queryKey(
        @NotBlank
        @ValidUUID
        @QueryValue("pixid") pixId: String,

        @NotBlank
        @ValidUUID
        @QueryValue("clientid") clientId: String): HttpResponse<Any> {

        val response = grpcClient.queryKey(ProtoQueryKeyRequest.newBuilder()
            .setPixId(ProtoQueryKeyRequest.ProtoPixIdClientId.newBuilder()
                .setPixId(pixId)
                .setClientId(clientId)
                .build())
            .build())
        return HttpResponse.ok(response.toResponseDto())
        
    }
}