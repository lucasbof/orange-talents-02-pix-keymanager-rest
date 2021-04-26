package com.br.zup.keymanager.pix.remove

import com.br.zup.ProtoRemoveKeyResponse
import com.br.zup.ProtoRemoveKeyServiceGrpc
import com.br.zup.keymanager.shared.grpc.KeyManagerGrpcFactory
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class RemoveKeyControllerTest {

    @field:Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @field:Inject
    lateinit var grpcClient: ProtoRemoveKeyServiceGrpc.ProtoRemoveKeyServiceBlockingStub

    @Test
    internal fun `deve remove chave caso o pixId e clientId existirem`() {

        val pixId = UUID.randomUUID().toString()
        val clientId = UUID.randomUUID().toString()
        val request = RemoveKeyRequest(
            clientId = clientId,
            pixId = pixId
        )

        Mockito.`when`(grpcClient.removeKey(request.toProtoRemoveRequest()))
            .thenReturn(
                ProtoRemoveKeyResponse.newBuilder()
                    .setPixId(pixId)
                    .setClientId(clientId)
                .build())

        val response = httpClient.toBlocking().exchange(HttpRequest.DELETE("/api/key", request), RemoveKeyResponse::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertEquals(pixId, response.body().pixId)
        assertEquals(clientId, response.body().clientId)
    }

    @Test
    internal fun `deve retornar 404 quando uma chave nao for encontrado pelo pixId e clientId`() {
        val pixId = UUID.randomUUID().toString()
        val clientId = UUID.randomUUID().toString()
        val request = RemoveKeyRequest(
            clientId = clientId,
            pixId = pixId
        )

        Mockito.`when`(grpcClient.removeKey(request.toProtoRemoveRequest()))
            .thenThrow(StatusRuntimeException(Status.NOT_FOUND))

        val response = org.junit.jupiter.api.assertThrows<HttpClientResponseException> {
            httpClient.toBlocking().exchange(HttpRequest.DELETE("/api/key", request), RemoveKeyResponse::class.java)
        }

        assertEquals(HttpStatus.NOT_FOUND, response.status)
    }

    @Test
    internal fun `deve retornar 400 quando algum dado for invalido`() {
        val request = RemoveKeyRequest(
            clientId = "",
            pixId = ""
        )

        val response = org.junit.jupiter.api.assertThrows<HttpClientResponseException> {
            httpClient.toBlocking().exchange(HttpRequest.DELETE("/api/key", request), RemoveKeyResponse::class.java)
        }

        assertEquals(HttpStatus.BAD_REQUEST, response.status)
    }

    @Factory
    @Replaces(factory = KeyManagerGrpcFactory::class)
    internal class ClientRemove {
        @Singleton
        fun mockitoblockingStubRemove() = Mockito.mock(ProtoRemoveKeyServiceGrpc.ProtoRemoveKeyServiceBlockingStub::class.java)
    }

}