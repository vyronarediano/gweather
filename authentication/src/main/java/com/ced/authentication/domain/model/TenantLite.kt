package com.ced.authentication.domain.model

/**
 * @author Cedierick Vyron Arediano
 * @since 1.0.0
 */
class TenantLite(
    val id: String,
    val name: String
) {
    var active: Boolean = false

    var dataId: String? = null
}