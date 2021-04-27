package com.br.zup.keymanager.pix.query

import com.br.zup.ProtoQueryKeyResponse
import com.br.zup.keymanager.pix.AccountType
import com.br.zup.keymanager.pix.KeyType
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun ProtoQueryKeyResponse.toResponseDto(): QueryKeyResponse {
    return QueryKeyResponse(
        clientId = clientId,
        pixId = pixId,
        pixKey = PixKeyQueryResponse(
            type = KeyType.valueOf(pixKey.type.name),
            key = pixKey.key,
            account =AccountInfoQueryResponse(
                type = AccountType.valueOf(pixKey.account.type.name),
                institution = pixKey.account.institution,
                holderName = pixKey.account.holderName,
                cpfHolder = pixKey.account.cpfHolder,
                agency = pixKey.account.agency,
                number = pixKey.account.accountNumber
            ),
            createdAt = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(
                    pixKey.createdAt.seconds,
                    pixKey.createdAt.nanos.toLong()
                ),
                ZoneId.of("UTC")
            )
        )
    )
}