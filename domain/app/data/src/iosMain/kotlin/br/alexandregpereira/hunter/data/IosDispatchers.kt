/*
 * Copyright 2023 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
