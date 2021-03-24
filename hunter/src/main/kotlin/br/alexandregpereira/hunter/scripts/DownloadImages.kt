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

package br.alexandregpereira.hunter.scripts

import br.alexandregpereira.hunter.dndapi.data.Monster
import br.alexandregpereira.hunter.image.downloadImage
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.serialization.decodeFromString
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

@FlowPreview
@ExperimentalCoroutinesApi
suspend fun main() = start {
    val monsterIndexList = json.decodeFromString<List<Monster>>(readJsonFile(JSON_FILE_NAME)).map {
        it.index
    }
    json.decodeFromString<List<WebImageData>>(readJsonFile(JSON_IMAGES_FILE_NAME))
        .filter { monsterIndexList.contains(it.name) }
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
    println("File name: $fileName")

    val out = ByteArrayOutputStream()
    val buf = ByteArray(1024)
    var n: Int
    while (-1 != inputStream.read(buf).also { n = it }) {
        out.write(buf, 0, n)
    }
    out.close()
    inputStream.close()
    val response: ByteArray = out.toByteArray()

    val targetFile = File("images/$fileName.png")
    val outStream: OutputStream = FileOutputStream(targetFile)
    outStream.write(response)
    outStream.close()
    inputStream.close()
}