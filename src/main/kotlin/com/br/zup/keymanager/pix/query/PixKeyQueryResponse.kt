package com.br.zup.keymanager.pix.query

import com.br.zup.keymanager.pix.KeyType
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class PixKeyQueryResponse(

    val type: KeyType?,
    val key: String?,
    val account: AccountInfoQueryResponse?,

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    val createdAt: LocalDateTime?
)
