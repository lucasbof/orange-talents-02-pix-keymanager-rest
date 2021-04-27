package com.br.zup.keymanager.shared.grpc

import com.br.zup.ProtoQueryKeyServiceGrpc
import com.br.zup.ProtoRegisterKeyServiceGrpc
import com.br.zup.ProtoRemoveKeyServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class KeyManagerGrpcFactory(@GrpcChannel("keyManager") val channel: ManagedChannel) {

    @Singleton
    fun registerKey() = ProtoRegisterKeyServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun removeKey() = ProtoRemoveKeyServiceGrpc.newBlockingStub(channel)

    @Singleton
    fun QueryKey() = ProtoQueryKeyServiceGrpc.newBlockingStub(channel)
}