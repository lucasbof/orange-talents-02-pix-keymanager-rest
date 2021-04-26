package com.br.zup.keymanager.pix.remove

import com.br.zup.ProtoRemoveKeyRequest
import com.br.zup.keymanager.shared.validations.ValidUUID
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
data class RemoveKeyRequest(
    @field:NotBlank
    @field:ValidUUID
    val pixId: String?,

    @field:NotBlank
    @field:ValidUUID
    val clientId: String?
) {

    fun toProtoRemoveRequest(): ProtoRemoveKeyRequest {
        return ProtoRemoveKeyRequest.newBuilder()
            .setPixId(pixId)
            .setClientId(clientId)
            .build()
    }
}
