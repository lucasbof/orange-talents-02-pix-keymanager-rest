package com.br.zup.keymanager.pix.query

import com.br.zup.keymanager.pix.AccountType

data class AccountInfoQueryResponse(
    val type: AccountType,
    val institution: String,
    val holderName: String,
    val cpfHolder: String,
    val agency: String,
    val number: String
)
