package com.br.zup.keymanager.pix.register

import com.br.zup.ProtoAccountType
import com.br.zup.ProtoKeyType
import com.br.zup.ProtoRegisterKeyRequest
import com.br.zup.keymanager.shared.validations.ValidUUID
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size


data class RegisterKeyResponse(
   val pixId: String
)
