package org.example.project

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform


@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.BINARY)
annotation class CommonParcelize


expect interface CommonParcelable
