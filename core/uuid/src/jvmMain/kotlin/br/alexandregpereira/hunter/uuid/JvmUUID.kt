package br.alexandregpereira.hunter.uuid

import java.util.UUID

actual fun generateUUID(): String = UUID.randomUUID().toString()
