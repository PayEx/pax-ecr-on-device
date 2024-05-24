package com.pax.ecr.app

import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val poiId: String?,
    val currencyCode: String?,
    val saleId: String?,
) {
    companion object {
        val DEFAULT = Config(null, null, "ECR1")
    }

    fun isValid() = poiId != null && currencyCode != null && saleId != null
}
