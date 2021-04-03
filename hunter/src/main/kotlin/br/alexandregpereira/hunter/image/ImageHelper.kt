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

import br.alexandregpereira.hunter.data.remote.model.ColorDto
import br.alexandregpereira.hunter.data.remote.model.MonsterDto
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.IOException
import java.io.InputStream
import java.util.LinkedList
import javax.imageio.ImageIO
import javax.imageio.ImageReader
import kotlin.random.Random

@ExperimentalCoroutinesApi
fun MonsterDto.downloadImage(): Flow<MonsterDto?> = downloadImage(imageUrl).map { inputStream ->
    val imageData = getImageData(inputStream)
    print("Color = ${imageData.lightBackgroundColor}; ")
    println("isHorizontal = ${imageData.isHorizontalImage}")
    this@downloadImage.copy(
        backgroundColor = ColorDto(
            light = imageData.lightBackgroundColor,
            dark = imageData.darkBackgroundColor
        ),
        isHorizontalImage = imageData.isHorizontalImage
    )
}.catch {}

@ExperimentalCoroutinesApi
fun downloadImage(imageUrl: String): Flow<InputStream> = callbackFlow {
    val client = OkHttpClient()

    val request: Request = Request.Builder()
        .url(imageUrl)
        .build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            println("request failed: $imageUrl: " + e.message)
            channel.offer(null)
            channel.close()
        }

        @Throws(IOException::class)
        override fun onResponse(call: Call, response: Response) {
            if (response.isSuccessful) {
                println("request success: $imageUrl; ")
                channel.offer(
                    element = response.body()!!.byteStream()
                )
            } else {
                println("request failed: $imageUrl")
                channel.offer(element = null)
            }
            channel.close()
        }
    })

    awaitClose()
}

fun getImageData(inputStream: InputStream): ImageData {
    val image: BufferedImage = inputStream.getBufferedImage() ?: return ImageData()

    val colors = image.getMostCommonColours()
    return ImageData(
        lightBackgroundColor = colors.first,
        darkBackgroundColor = colors.second,
        image.isHorizontalImage()
    ).apply {
        inputStream.close()
    }
}

fun InputStream.getBufferedImage(): BufferedImage? {
    val imageInputStream = ImageIO.createImageInputStream(this)
    val iter: Iterator<*> = ImageIO.getImageReaders(imageInputStream)

    if (!iter.hasNext()) {
        return null
    }
    val imageReader: ImageReader = iter.next() as ImageReader
    imageReader.input = imageInputStream
    return imageReader.read(0)
}

fun BufferedImage.isHorizontalImage(): Boolean {
    return width >= height
}

fun BufferedImage.getMostCommonColours(): Pair<String, String> {
    val height = this.height
    val width = this.width

    val m: MutableMap<Int, Int> = HashMap()
    for (i in 0 until width) {
        for (j in 0 until height) {
            val rgb = this.getRGB(i, j)
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
    return getMostCommonColours(m)
}

fun getMostCommonColours(map: Map<Int, Int>): Pair<String, String> {
    val list: List<Map.Entry<Int, Int>> = LinkedList(map.entries).sortedBy { it.value }
    val bestPixel = list[list.size / 4].key
    val rgb = getRGBArr(bestPixel)
    val hsb = Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], null)
    val lightColor = Color(
        Color.HSBtoRGB(
            hsb[0],
            0.05f,
            1f
        )
    )
    val darkColor = Color(
        Color.HSBtoRGB(
            hsb[0],
            0.1f,
            0.18f
        )
    )
    return lightColor.getHexColor(isDark = false) to darkColor.getHexColor(isDark = true)
}

private fun Color.getHexColor(isDark: Boolean): String {
    return "#" +
            Integer.toHexString(this.red).normalizeHex(isDark) +
            Integer.toHexString(this.green).normalizeHex() +
            Integer.toHexString(this.blue).normalizeHex()
}

private fun String.normalizeHex(isDark: Boolean = false): String {
    return if (this.length == 1) {
        if (isDark) "0${this}" else "${this}0"
    } else this
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
    if (rgDiff > tolerance || rgDiff < -tolerance) {
        if (rbDiff > tolerance || rbDiff < -tolerance) {
            return false
        }
    }
    return true
}

fun isWhite(rgbArr: IntArray): Boolean {
    return rgbArr[0] in 240..255 && rgbArr[1] in 240..255 && rgbArr[2] in 240..255
}

fun BufferedImage.removeWhiteBackgroundColor(): BufferedImage {
    val color = Color(255, 255, 255, 255)
    var count = 0
    var countNot = 0
    val bi = BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB)
    for (x in 0 until this.width) {
        for (y in 0 until this.height) {
            val rgba = this.getRGB(x, y)
            val rgbArr = getRGBArr(rgba)
            if (isWhite(rgbArr)) {
                bi.setRGB(x, y, color.rgb and 0x00ffffff)
                count++
            } else {
                countNot++
                bi.setRGB(x, y, rgba)
            }
        }
    }
    return bi
}
