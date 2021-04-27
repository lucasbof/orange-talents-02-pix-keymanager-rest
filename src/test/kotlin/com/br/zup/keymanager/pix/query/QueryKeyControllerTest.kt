package com.br.zup.keymanager.pix.query

import com.br.zup.*
import com.br.zup.keymanager.shared.grpc.KeyManagerGrpcFactory
import com.google.protobuf.Timestamp
import io.grpc.Status
import io.grpc.StatusRuntimeException
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
class QueryKeyControllerTest {

    @field:Inject
    @field:Client("/")
    lateinit var httpClient: HttpClient

    @field:Inject
    lateinit var grpcClient: ProtoQueryKeyServiceGrpc.ProtoQueryKeyServiceBlockingStub

    @Test
    internal fun `deve consultar chave quando ela existir e os dados estiverem corretos`() {

        val responseMock = ProtoQueryKeyResponse.newBuilder()
            .setPixId(UUID.randomUUID().toString())
            .setClientId(UUID.randomUUID().toString())
            .setPixKey(
                ProtoQueryKeyResponse.ProtoQueryPixKey.newBuilder()
                    .setKey("lucas@gmail.com")
                    .setType(ProtoKeyType.EMAIL)
                    .setCreatedAt(Timestamp.newBuilder()
                        .setNanos(87878788)
                        .setSeconds(4484848)
                        .build())
                    .setAccount(ProtoQueryKeyResponse.ProtoQueryPixKey.ProtoQueryAccountInfo.newBuilder()
                        .setType(ProtoAccountType.CHECKING_ACCOUNT)
                        .setInstitution("Banco")
                        .setHolderName("Lucas")
                        .setAgency("5148")
                        .setAccountNumber("892720")
                        .setCpfHolder("01496216040")
                        .build())
                    .build()
            )
            .build()

        Mockito.`when`(grpcClient.queryKey(ProtoQueryKeyRequest.newBuilder()
            .setPixId(ProtoQueryKeyRequest.ProtoPixIdClientId.newBuilder()
                .setPixId(responseMock.pixId)
                .setClientId(responseMock.clientId)
                .build())
            .build()))
            .thenReturn(responseMock)

        val response = httpClient.toBlocking().exchange("/api/key?pixid=${responseMock.pixId}&clientid=${responseMock.clientId}", QueryKeyResponse::class.java)

        assertEquals(HttpStatus.OK, response.status)
        assertEquals(responseMock.pixId, response.body()!!.pixId)
        assertEquals(responseMock.clientId, response.body()!!.clientId)
        assertEquals(responseMock.pixKey.key, response.body()?.pixKey?.key)
        assertEquals(responseMock.pixKey.type.name, response.body()?.pixKey?.type?.name)
        assertEquals(responseMock.pixKey.account.accountNumber, response.body()?.pixKey?.account?.number)
        assertEquals(responseMock.pixKey.account.agency, response.body()?.pixKey?.account?.agency)
        assertEquals(responseMock.pixKey.account.cpfHolder, response.body()?.pixKey?.account?.cpfHolder)
        assertEquals(responseMock.pixKey.account.holderName, response.body()?.pixKey?.account?.holderName)
        assertEquals(responseMock.pixKey.account.type.name, response.body()?.pixKey?.account?.type?.name)
        assertEquals(responseMock.pixKey.account.institution, response.body()?.pixKey?.account?.institution)
        assertNotNull(response.body()?.pixKey?.createdAt)

    }

    @Test
    internal fun `deve retornar 404 quando a chave nao for encontrada`() {
        val pixId = UUID.randomUUID().toString()
        val clientId = UUID.randomUUID().toString()

        Mockito.`when`(grpcClient.queryKey(ProtoQueryKeyRequest.newBuilder()
            .setPixId(ProtoQueryKeyRequest.ProtoPixIdClientId.newBuilder()
                .setPixId(pixId)
                .setClientId(clientId)
                .build())
            .build()))
            .thenThrow(StatusRuntimeException(Status.NOT_FOUND))

        val response = org.junit.jupiter.api.assertThrows<HttpClientResponseException> {
            httpClient.toBlocking().exchange("/api/key?pixid=${pixId}&clientid=${clientId}", QueryKeyResponse::class.java)
        }

        assertEquals(HttpStatus.NOT_FOUND, response.status)
    }

    @Test
    internal fun `deve retornar 400 quando algum dado for invalido`() {
        val response = org.junit.jupiter.api.assertThrows<HttpClientResponseException> {
            httpClient.toBlocking().exchange("/api/key?pixid=&clientid=", QueryKeyResponse::class.java)
        }

        assertEquals(HttpStatus.BAD_REQUEST, response.status)
    }

    @Factory
    @Replaces(factory = KeyManagerGrpcFactory::class)
    internal class ClientQuery {
        @Singleton
        fun mockitoblockingStubQuery() = Mockito.mock(ProtoQueryKeyServiceGrpc.ProtoQueryKeyServiceBlockingStub::class.java)
    }
}