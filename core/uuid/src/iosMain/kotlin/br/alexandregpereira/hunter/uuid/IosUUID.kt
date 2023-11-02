package br.alexandregpereira.hunter.uuid

import platform.Foundation.NSUUID

actual fun generateUUID(): String = NSUUID().UUIDString()
