package com.ced.authentication.data.mapper

import com.ced.authentication.data.entity.TenantEntity
import com.ced.authentication.domain.model.Tenant
import com.ced.authentication.domain.model.TenantLite

/**
 * @author Cedierick Vyron Arediano
 * @since 1.0.0
 */

fun Tenant.toEntity(): TenantEntity {
    val domain = this
    return TenantEntity().apply {
        active = domain.active
        data = domain.dataId
        id = domain.id
        name = domain.name
    }
}

fun TenantLite.toEntity(): TenantEntity {
    val domain = this
    return TenantEntity().apply {
        active = domain.active
        data = domain.dataId
        id = domain.id
        name = domain.name
    }
}

fun TenantEntity.toDomain(): Tenant {
    val entity = this
    check(entity.id != null) { "Tenant id is null" }
    check( entity.name != null ) { "Tenant name is null" }

    return Tenant(
        entity.id!!,
        entity.name!!
    ).apply {
        active = entity.active
        dataId = entity.data
    }
}

fun TenantEntity.toLite(): TenantLite {
    val entity = this
    check(entity.id != null) { "Tenant id is null" }
    check( entity.name != null ) { "Tenant name is null" }

    return TenantLite(
        entity.id!!,
        entity.name!!
    ).apply {
        active = entity.active
        dataId = entity.data
    }
}