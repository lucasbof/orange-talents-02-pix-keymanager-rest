package com.br.zup.keymanager.pix.query

import com.br.zup.ProtoQueryKeyResponse
import com.br.zup.keymanager.pix.AccountType
import com.br.zup.keymanager.pix.KeyType
import java.time.Instant
import java.time.LocalDateTime

data class QueryKeyResponse(
    val clientId: String?,
    val pixId: String?,
    val pixKey: PixKeyQueryResponse?,
)