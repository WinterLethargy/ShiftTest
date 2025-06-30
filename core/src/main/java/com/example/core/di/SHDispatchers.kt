package com.example.core.di

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME


@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val sfDispatcher: SHDispatchers)

enum class SHDispatchers {
    Default,
    IO,
}