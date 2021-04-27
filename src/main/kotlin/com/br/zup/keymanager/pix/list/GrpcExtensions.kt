package com.br.zup.keymanager.pix.list

import com.br.zup.ProtoListKeyResponse
import com.br.zup.keymanager.pix.AccountType
import com.br.zup.keymanager.pix.KeyType
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun ProtoListKeyResponse.toResponseDTO(): List<ListKeyObj> {
    return keysList.map {
        ListKeyObj(
            pixId = it.pixId,
            clientId = it.clientId,
            keyType = KeyType.valueOf(it.keyType.name),
            key = it.key,
            accountType = AccountType.valueOf(it.accountType.name),
            createdAt =  LocalDateTime.ofInstant(
                Instant.ofEpochSecond(
                    it.createdAt.seconds,
                    it.createdAt.nanos.toLong()
                ),
                ZoneId.of("UTC")
            )
        )
    }
}