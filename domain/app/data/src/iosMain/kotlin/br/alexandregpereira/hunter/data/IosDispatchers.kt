/*
 * Copyright (C) 2024 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.data

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.StableRef
import kotlinx.cinterop.autoreleasepool
import kotlinx.cinterop.cstr
import kotlinx.cinterop.memScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Runnable
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_global_queue
import platform.darwin.dispatch_get_specific
import platform.darwin.dispatch_queue_set_specific
import platform.posix.QOS_CLASS_UTILITY
import platform.posix.qos_class_t
import kotlin.coroutines.CoroutineContext

object IosDispatchers {
    val IO: CoroutineDispatcher = NSQueueDispatcher(QOS_CLASS_UTILITY)
}

@OptIn(ExperimentalForeignApi::class)
class NSQueueDispatcher(private val qosClass: qos_class_t) : CoroutineDispatcher() {
    private val queue = dispatch_get_global_queue(qosClass.toLong(), 0.toULong())

    init {
        val key = "NSQueueDispatcher:$qosClass"
        memScoped {
            val keyCString = key.cstr.ptr
            dispatch_queue_set_specific(queue, keyCString, StableRef.create(key).asCPointer(), null)
        }
    }

    @OptIn(BetaInteropApi::class)
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        dispatch_async(queue) {
            autoreleasepool {
                block.run()
            }
        }
    }

    override fun isDispatchNeeded(context: CoroutineContext): Boolean {
        val key = "NSQueueDispatcher:$qosClass"
        return memScoped {
            val keyCString = key.cstr.ptr
            dispatch_get_specific(keyCString) == null
        }
    }
}
