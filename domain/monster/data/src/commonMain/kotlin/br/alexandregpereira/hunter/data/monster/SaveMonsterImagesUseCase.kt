package br.alexandregpereira.hunter.data.monster

import br.alexandregpereira.file.FileManager
import br.alexandregpereira.file.FileType
import br.alexandregpereira.hunter.domain.model.MonsterImage
import br.alexandregpereira.hunter.domain.model.isContentNull
import br.alexandregpereira.hunter.domain.repository.MonsterImageRepository
import br.alexandregpereira.hunter.domain.usecase.ResetMonsterImage
import br.alexandregpereira.hunter.domain.usecase.SaveMonsterImages

internal class SaveMonsterImagesUseCase(
    private val monsterImageRepository: MonsterImageRepository,
    private val fileManager: FileManager,
    private val resetMonsterImage: ResetMonsterImage,
) : SaveMonsterImages {

    override suspend fun invoke(vararg monsterImages: MonsterImage) {
        val monsterImagesToSave = monsterImages.filter { !it.isContentNull() }
        if (monsterImagesToSave.isNotEmpty()) {
            val monsterIndexes = monsterImagesToSave.map { it.monsterIndex }
            val localMonsterImages = monsterImageRepository.getLocalMonsterImages(monsterIndexes)
            monsterImageRepository.saveMonsterImages(monsterImagesToSave.toList())

            val monsterImagesByIndex = monsterImagesToSave.associateBy { it.monsterIndex }
            localMonsterImages.forEach { localMonsterImage ->
                val localFileName = localMonsterImage.imageUrl
                    ?.takeIf { it.startsWith("file://") }
                    ?.substringAfterLast("/")
                val fileName = monsterImagesByIndex[localMonsterImage.monsterIndex]?.imageUrl
                    ?.substringAfterLast("/")
                if (localFileName != null && (localFileName != fileName || fileName.startsWith("http"))) {
                    fileManager.deleteFileFromAppStorage(
                        fileName = localFileName,
                        fileType = FileType.IMAGE,
                    )
                }
            }
        }

        val monsterImageIndexesToReset = monsterImages.filter { it.isContentNull() }.map {
            it.monsterIndex
        }
        if (monsterImageIndexesToReset.isNotEmpty()) {
            resetMonsterImage(*monsterImageIndexesToReset.toTypedArray())
        }
    }
}
