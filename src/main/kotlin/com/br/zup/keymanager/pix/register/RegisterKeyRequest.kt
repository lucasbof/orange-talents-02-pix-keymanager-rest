package com.br.zup.keymanager.pix.register

import com.br.zup.ProtoAccountType
import com.br.zup.ProtoKeyType
import com.br.zup.ProtoRegisterKeyRequest
import com.br.zup.keymanager.pix.AccountType
import com.br.zup.keymanager.pix.KeyType
import com.br.zup.keymanager.pix.ValidPixKey
import com.br.zup.keymanager.shared.validations.ValidUUID
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@ValidPixKey
@Introspected
data class RegisterKeyRequest(
    @field:ValidUUID
    @field:NotBlank
    val clientId: String?,
    @field:NotNull
    val keyType: KeyType?,
    @field:Size(max = 77)
    val key: String?,
    @field:NotNull
    val accountType: AccountType?
) {

    fun toProtoRegisterKeyRequest(): ProtoRegisterKeyRequest {
        return ProtoRegisterKeyRequest.newBuilder()
            .setClientId(clientId)
            .setKeyType(ProtoKeyType.valueOf(keyType!!.name))
            .setKeyValue(if(key.isNullOrBlank()) "" else key)
            .setAccountType(ProtoAccountType.valueOf(accountType!!.name))
            .build()
    }
}
