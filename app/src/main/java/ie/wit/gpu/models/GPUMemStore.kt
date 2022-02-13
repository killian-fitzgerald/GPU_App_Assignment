package ie.wit.gpu.models

import timber.log.Timber.i

var lastId = 0L

internal fun getId(): Long {
    return lastId++
}

class GPUMemStore : GPUStore {

    val gpus = ArrayList<GPUModel>()

    override fun findAll(): List<GPUModel> {
        return gpus
    }

    override fun create(gpu: GPUModel) {
        gpu.id = getId()
        gpus.add(gpu)
        logAll()
    }

    override fun update(gpu: GPUModel) {
        val foundGPU: GPUModel? = gpus.find { p -> p.id == gpu.id }
        if (foundGPU != null) {
            foundGPU.title = gpu.title
            foundGPU.description = gpu.description
            foundGPU.image = gpu.image
            foundGPU.lat = gpu.lat
            foundGPU.lng = gpu.lng
            foundGPU.zoom = gpu.zoom
            logAll()
        }


        }

    override fun delete(gpu: GPUModel) {
        gpus.remove(gpu)
    }

    private fun logAll() {
        gpus.forEach { i("$it") }
    }
}