/*
 * Copyright (C) 2026 Alexandre Gomes Pereira
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

package br.alexandregpereira.hunter.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue
import platform.UIKit.UIApplicationDidBecomeActiveNotification
import platform.UIKit.UIApplicationWillResignActiveNotification

@Composable
internal actual fun rememberLifecycleEvents(): Flow<LifecycleEvent> {
    return remember {
        val eventsFlow = MutableSharedFlow<LifecycleEvent>(
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_LATEST,
        )
        val notificationCenter = NSNotificationCenter.defaultCenter
        
        val didBecomeActiveObserver = notificationCenter.addObserverForName(
            name = UIApplicationDidBecomeActiveNotification,
            `object` = null,
            queue = NSOperationQueue.mainQueue
        ) {
            eventsFlow.tryEmit(LifecycleEvent.ON_RESUME)
        }
        
        val willResignActiveObserver = notificationCenter.addObserverForName(
            name = UIApplicationWillResignActiveNotification,
            `object` = null,
            queue = NSOperationQueue.mainQueue
        ) {
            eventsFlow.tryEmit(LifecycleEvent.ON_PAUSE)
        }
        
        // Clean up observers when leaving composition
        object {
            val flow = eventsFlow.asSharedFlow()
            
            fun cleanup() {
                notificationCenter.removeObserver(didBecomeActiveObserver)
                notificationCenter.removeObserver(willResignActiveObserver)
            }
        }
    }.let { wrapper ->
        DisposableEffect(Unit) {
            onDispose { wrapper.cleanup() }
        }
        wrapper.flow
    }
}
