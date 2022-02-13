package ie.wit.gpu.models

interface GPUStore {
    fun findAll(): List<GPUModel>
    fun create(gpu: GPUModel)
    fun update(gpu: GPUModel)
    fun delete(gpu: GPUModel)
}