package com.br.zup.keymanager.pix.list

import com.br.zup.*
import com.br.zup.keymanager.pix.query.QueryKeyResponse
import com.br.zup.keymanager.shared.grpc.KeyManagerGrpcFactory
import com.google.protobuf.Timestamp
import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Replaces
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@MicronautTest
internal class ListKeysControllerTest {

    @field:Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @field:Inject
    lateinit var grpcClient: ProtoListKeyServiceGrpc.ProtoListKeyServiceBlockingStub

    companion object {
        val CLIENT_ID = UUID.randomUUID().toString()
    }

    @Test
    internal fun `deve trazer lista de chaves quando o clientId existir`() {

        Mockito.`when`(grpcClient.listKeys(ProtoListKeyRequest.newBuilder()
            .setClientId(CLIENT_ID)
            .build()))
            .thenReturn(ProtoListKeyResponse.newBuilder()
                .addAllKeys(getGrpcMockResponse())
                .build())

        val response = httpClient.toBlocking().exchange("/api/key/list?clientid=${CLIENT_ID}", List::class.java)

        with(response) {
            assertEquals(HttpStatus.OK, status)
            assertNotNull(body())
            assertEquals(2, body().size)
        }

    }

    @Test
    internal fun `deve trazer lista vazia quando um clientId nao tiver chave ou nao existir`() {
        Mockito.`when`(grpcClient.listKeys(ProtoListKeyRequest.newBuilder()
            .setClientId(CLIENT_ID)
            .build()))
            .thenReturn(ProtoListKeyResponse.newBuilder()
                .addAllKeys(listOf())
                .build())

        val response = httpClient.toBlocking().exchange("/api/key/list?clientid=${CLIENT_ID}", List::class.java)

        with(response) {
            assertEquals(HttpStatus.OK, status)
            assertNotNull(body())
            assertEquals(0, body().size)
        }
    }

    @Test
    internal fun `deve retornar 400 quando o clientId estiver invalido`() {
        val response = org.junit.jupiter.api.assertThrows<HttpClientResponseException> {
            httpClient.toBlocking().exchange("/api/key/list?clientid=", QueryKeyResponse::class.java)
        }

        assertEquals(HttpStatus.BAD_REQUEST, response.status)
    }

    private fun getGrpcMockResponse(): List<ProtoListKeyResponse.ProtoListObj> {

        val obj1 = ProtoListKeyResponse.ProtoListObj.newBuilder()
            .setKey(UUID.randomUUID().toString())
            .setPixId(UUID.randomUUID().toString())
            .setClientId(CLIENT_ID)
            .setKeyType(ProtoKeyType.RANDOM_KEY)
            .setAccountType(ProtoAccountType.CHECKING_ACCOUNT)
            .setCreatedAt(Timestamp.newBuilder()
                .setSeconds(94941971)
                .setNanos(940492))
            .build()

        val obj2 = ProtoListKeyResponse.ProtoListObj.newBuilder()
            .setKey("lucas@gmail.com")
            .setPixId(UUID.randomUUID().toString())
            .setClientId(CLIENT_ID)
            .setKeyType(ProtoKeyType.EMAIL)
            .setAccountType(ProtoAccountType.CHECKING_ACCOUNT)
            .setCreatedAt(Timestamp.newBuilder()
                .setSeconds(94941971)
                .setNanos(940492))
            .build()

        return listOf(obj1, obj2)
    }

    @Factory
    @Replaces(factory = KeyManagerGrpcFactory::class)
    internal class ClientList {
        @Singleton
        fun mockitoblockingStubList() = Mockito.mock(ProtoListKeyServiceGrpc.ProtoListKeyServiceBlockingStub::class.java)
    }
}