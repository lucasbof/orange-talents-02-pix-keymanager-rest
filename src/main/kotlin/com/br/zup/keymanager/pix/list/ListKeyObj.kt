package com.br.zup.keymanager.pix.list

import com.br.zup.keymanager.pix.AccountType
import com.br.zup.keymanager.pix.KeyType
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class ListKeyObj(
    val pixId: String?,
    val clientId: String?,
    val keyType: KeyType?,
    val key: String?,
    val accountType: AccountType,

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    val createdAt: LocalDateTime
)