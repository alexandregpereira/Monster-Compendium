/*
 * Copyright (c) 2021 Alexandre Gomes Pereira
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.alexandregpereira.hunter.image

import br.alexandregpereira.hunter.data.remote.model.MonsterDto
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.awt.image.BufferedImage
import java.io.IOException
import java.io.InputStream
import java.util.LinkedList
import javax.imageio.ImageIO
import javax.imageio.ImageReader

@ExperimentalCoroutinesApi
fun MonsterDto.downloadImage(): Flow<MonsterDto?> = callbackFlow {
    val client = OkHttpClient()

    val request: Request = Request.Builder()
        .url(imageUrl)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            println("request failed: $index: " + e.message)
            channel.offer(null)
            channel.close()
        }

        @Throws(IOException::class)
        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                val color = getMostCommonColour(response.body()!!.byteStream())
                    ?: this@downloadImage.backgroundColor
                println("request success: $index; Color = $color")
                channel.offer(element = this@downloadImage.copy(backgroundColor = color))
            } else {
                println("request failed: $index")
                channel.offer(element = null)
            }
            channel.close()
        }
    })

    awaitClose()
}

fun getMostCommonColour(inputStream: InputStream): String? {
    val imageInputStream = ImageIO.createImageInputStream(inputStream)
    val iter: Iterator<*> = ImageIO.getImageReaders(imageInputStream)

    if (!iter.hasNext()) {
        return null
    }
    val imageReader: ImageReader = iter.next() as ImageReader
    imageReader.input = imageInputStream

    val image: BufferedImage = imageReader.read(0)

    val height = image.height
    val width = image.width

    val m: MutableMap<Int, Int> = HashMap()
    for (i in 0 until width) {
        for (j in 0 until height) {
            val rgb = image.getRGB(i, j)
            val rgbArr = getRGBArr(rgb)
            // Filter out grays....
            if (!isGray(rgbArr)) {
                var counter = m[rgb]
                if (counter == null) counter = 0
                counter++
                m[rgb] = counter
            }
        }
    }
    return getMostCommonColour(m)
}

fun getMostCommonColour(map: Map<Int, Int>): String {
    val list: List<Map.Entry<Int, Int>> = LinkedList(map.entries).sortedBy { it.value }
    val me = list[list.size - 1]
    val rgb = getRGBArr(me.key)
    return "#" +
            Integer.toHexString(rgb[0]).normalizeHex() +
            Integer.toHexString(rgb[1]).normalizeHex() +
            Integer.toHexString(rgb[2]).normalizeHex()
}

private fun String.normalizeHex(): String {
    return if (this.length == 1) "${this}0" else this
}

fun getRGBArr(pixel: Int): IntArray {
    val red = pixel shr 16 and 0xff
    val green = pixel shr 8 and 0xff
    val blue = pixel and 0xff
    return intArrayOf(red, green, blue)
}

fun isGray(rgbArr: IntArray): Boolean {
    val rgDiff = rgbArr[0] - rgbArr[1]
    val rbDiff = rgbArr[0] - rgbArr[2]
    // Filter out black, white and grays...... (tolerance within 10 pixels)
    val tolerance = 10
    if (rgDiff > tolerance || rgDiff < -tolerance) if (rbDiff > tolerance || rbDiff < -tolerance) {
        return false
    }
    return true
}