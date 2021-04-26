package com.br.zup.keymanager.pix.register

import com.br.zup.ProtoRegisterKeyResponse
import com.br.zup.ProtoRegisterKeyServiceGrpc
import com.br.zup.keymanager.pix.AccountType
import com.br.zup.keymanager.pix.KeyType
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
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class RegisterKeyControllerTest {

    @field:Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @field:Inject
    lateinit var grpcClient: ProtoRegisterKeyServiceGrpc.ProtoRegisterKeyServiceBlockingStub

    @Test
    internal fun `deve fazer chamado para registrar chave e retornar pixId quando os dados estiverem corretos`() {
        val pixId = UUID.randomUUID().toString()

        val request = RegisterKeyRequest(
            clientId = UUID.randomUUID().toString(),
            keyType = KeyType.EMAIL,
            key = "lucas@gmail.com",
            accountType = AccountType.CHECKING_ACCOUNT
        )

        Mockito.`when`(grpcClient.registerKey(request.toProtoRegisterKeyRequest()))
            .thenReturn(ProtoRegisterKeyResponse.newBuilder()
                .setPixId(pixId)
                .build())

        val response = httpClient.toBlocking().exchange(HttpRequest.POST("/api/key", request), RegisterKeyResponse::class.java)

        assertEquals(HttpStatus.CREATED, response.status)
        assertTrue(response.headers.contains("Location"))
        assertEquals(pixId, response.body().pixId)
    }

    @Test
    internal fun `deve retornar 422 quando uma chave ja existir no grpcServer`() {
        val pixId = UUID.randomUUID().toString()

        val request = RegisterKeyRequest(
            clientId = UUID.randomUUID().toString(),
            keyType = KeyType.EMAIL,
            key = "lucas@gmail.com",
            accountType = AccountType.CHECKING_ACCOUNT
        )

        Mockito.`when`(grpcClient.registerKey(request.toProtoRegisterKeyRequest()))
            .thenThrow(StatusRuntimeException(Status.ALREADY_EXISTS))

        val response = org.junit.jupiter.api.assertThrows<HttpClientResponseException> {
            httpClient.toBlocking().exchange(HttpRequest.POST("/api/key", request), RegisterKeyResponse::class.java)
        }

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, response.status)
    }

    @Test
    internal fun `deve retornar 400 quando algum dado for invalido`() {
        val request = RegisterKeyRequest(
            clientId = null,
            keyType = null,
            key = null,
            accountType = null
        )

        val response = org.junit.jupiter.api.assertThrows<HttpClientResponseException> {
            httpClient.toBlocking().exchange(HttpRequest.POST("/api/key", request), RegisterKeyResponse::class.java)
        }

        assertEquals(HttpStatus.BAD_REQUEST, response.status)
    }

    @Factory
    @Replaces(factory = KeyManagerGrpcFactory::class)
    internal class ClientRegister {
        @Singleton
        fun mockitoblockingStubRegister() = Mockito.mock(ProtoRegisterKeyServiceGrpc.ProtoRegisterKeyServiceBlockingStub::class.java)
    }
}