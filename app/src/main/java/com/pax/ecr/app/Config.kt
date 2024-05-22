package com.pax.ecr.app

data class Config(
    val poiId: String?,
    val currencyCode: String?,
) {
    companion object {
        val DEFAULT = Config(null, null)
    }

    fun isValid() = poiId != null && currencyCode != null
}
