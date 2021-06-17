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

package br.alexandregpereira.hunter.scripts

import br.alexandregpereira.hunter.data.remote.model.MonsterDto
import br.alexandregpereira.hunter.image.downloadImage
import br.alexandregpereira.hunter.image.getBufferedImage
import br.alexandregpereira.hunter.image.removeWhiteBackgroundColor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import java.io.File
import java.io.InputStream
import javax.imageio.ImageIO

@FlowPreview
@ExperimentalCoroutinesApi
suspend fun main() = start {
    val monsterIndexList = json.decodeFromString<List<MonsterDto>>(
        readJsonFile(JSON_FORMATTED_FILE_NAME)
    ).map {
        it.index
    }
    json.decodeFromString<List<WebImageData>>(readJsonFile(JSON_IMAGES_FILE_NAME))
        .filter {
            monsterIndexList.contains(it.name).not() && it.name.contains("variant").not()
        }
        .asSequence()
        .asFlow()
        .flatMapMerge { data ->
            downloadImage(data.url).map {
                saveImage(fileName = data.name, inputStream = it)
            }.catch {
                println("Error: $it")
            }
        }
        .collect()
}

private fun saveImage(fileName: String, inputStream: InputStream) {
    val bufferedImage = inputStream.getBufferedImage()?.removeWhiteBackgroundColor() ?: return
    println("File name: $fileName")
    val targetFile = File("images/$fileName.png")
    ImageIO.write(bufferedImage, "PNG", targetFile)
}
