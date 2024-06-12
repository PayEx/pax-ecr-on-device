package com.pax.ecr.app

import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val poiId: String?,
    val currencyCode: String?,
    val saleId: String?,
    val responseScreenEnabled: Boolean = false,
) {
    companion object {
        val DEFAULT = Config(null, null, "ECR1", false)
    }

    fun isValid() =
        poiId != null && currencyCode != null && saleId != null && currencyCode.length == 3 && currencyCode.all { it.isLetter() }
}
