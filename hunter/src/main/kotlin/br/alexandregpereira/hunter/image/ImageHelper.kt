/*
 * Hunter - DnD 5th edition monster compendium application
 * Copyright (C) 2021 Alexandre Gomes Pereira
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package br.alexandregpereira.hunter.image

import br.alexandregpereira.hunter.data.remote.model.ColorDto
import br.alexandregpereira.hunter.data.remote.model.MonsterDto
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import okhttp3.OkHttpClient
import okhttp3.Request
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.IOException
import java.io.InputStream
import java.lang.IllegalArgumentException
import java.util.LinkedList
import javax.imageio.ImageIO
import javax.imageio.ImageReader
import kotlin.math.roundToInt

@ExperimentalCoroutinesApi
fun MonsterDto.downloadImage(): Flow<MonsterDto?> = downloadImage(imageUrl).map { inputStream ->
    val imageData = getImageData(inputStream)
    print("Monster = ${this.name}; Color = ${imageData.lightBackgroundColor}; ")
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
fun downloadImage(imageUrl: String): Flow<InputStream> = flow {
    val client = OkHttpClient()

    val request: Request = Request.Builder()
        .url(imageUrl)
        .build()

    runCatching {
        client.newCall(request).execute()
    }.onSuccess { response ->
        if (response.isSuccessful) {
            println("request success: $imageUrl; ")
            emit(response.body()!!.byteStream())
        } else {
            println("request failed: $imageUrl")
            throw IOException()
        }
    }.onFailure { error ->
        println("request failed: $imageUrl: " + error.message)
        throw error
    }
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
    val bestIndex = (list.lastIndex * 0.7).roundToInt()
    val bestPixel = list[bestIndex].key
    val rgb = getRGBArr(bestPixel)
    val hsl = Color(rgb[0], rgb[1], rgb[2]).toHsl()
    val lightColor = floatArrayOf(
        hsl[0],
        hsl[1].coerceIn(0.8f, 1f),
        hsl[2].coerceIn(0.7f, 1f)
    ).fromHslToRgbColor()

    val darkColor = floatArrayOf(
        hsl[0],
        hsl[1].coerceIn(0.8f, 1f),
        hsl[2].coerceIn(0f, 0.3f)
    ).fromHslToRgbColor()

    return lightColor.getHexColor(isDark = false) to darkColor.getHexColor(isDark = true)
}

fun Color.toHsl(): FloatArray {
    // Get RGB values in the range 0 - 1
    val rgb = this.getRGBColorComponents( null )
    val r = rgb[0]
    val g = rgb[1]
    val b = rgb[2]

    // Minimum and Maximum RGB values are used in the HSL calculations
    val min = r.coerceAtMost(g.coerceAtMost(b))
    val max = r.coerceAtLeast(g.coerceAtLeast(b))

    // Calculate the Hue
    val h = when (max) {
        r -> (60 * (g - b) / (max - min) + 360) % 360
        g -> 60 * (b - r) / (max - min) + 120
        b -> 60 * (r - g) / (max - min) + 240
        else -> 0f
    }

    // Calculate the Luminance
    val l = (max + min) / 2

    // Calculate the Saturation
    val s = if (max == min) 0f else if (l <= .5f) (max - min) / (max + min) else (max - min) / (2 - max - min)
    return floatArrayOf(h, s, l)
}

fun FloatArray.fromHslToRgbColor(): Color {
    if (this[1] < 0f || this[1] > 1f) {
        val message = "Color parameter outside of expected range - Saturation"
        throw IllegalArgumentException(message)
    }
    if (this[2] < 0f || this[2] > 1f) {
        val message = "Color parameter outside of expected range - Luminance"
        throw IllegalArgumentException(message)
    }

    var _h = this[0]
    var _s = this[1] * 100
    var _l = this[2] * 100

    //  Formula needs all values between 0 - 1.
    _h %= 360.0f
    _h /= 360f
    _s /= 100f
    _l /= 100f
    val q = if (_l < 0.5) _l * (1 + _s) else _l + _s - _s * _l
    val p = 2 * _l - q
    val r = 0f.coerceAtLeast(hueToRGB(p, q, _h + 1.0f / 3.0f)).coerceAtMost(1f)
    val g = 0f.coerceAtLeast(hueToRGB(p, q, _h)).coerceAtMost(1f)
    val b = 0f.coerceAtLeast(hueToRGB(p, q, _h - 1.0f / 3.0f)).coerceAtMost(1f)
    return Color(r, g, b, 1f)
}

private fun hueToRGB(p: Float, q: Float, h: Float): Float {
    var _h = h
    if (_h < 0) _h += 1f
    if (_h > 1) _h -= 1f
    if (6 * _h < 1) {
        return p + (q - p) * 6 * _h
    }
    if (2 * _h < 1) {
        return q
    }
    return if (3 * _h < 2) {
        p + (q - p) * 6 * (2.0f / 3.0f - _h)
    } else p
}

private fun Color.getHexColor(isDark: Boolean): String {
    return "#" +
            Integer.toHexString(this.red).normalizeHex(isDark) +
            Integer.toHexString(this.green).normalizeHex(isDark) +
            Integer.toHexString(this.blue).normalizeHex(isDark)
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
