package ie.wit.gpu.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import ie.wit.gpu.exists
import ie.wit.gpu.helpers.*
import ie.wit.gpu.read
import ie.wit.gpu.write
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "gpus.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<GPUModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class GPUJSONStore(private val context: Context) : GPUStore {

    var gpus = mutableListOf<GPUModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<GPUModel> {
        logAll()
        return gpus
    }

    override fun create(gpu: GPUModel) {
        gpu.id = generateRandomId()
        gpus.add(gpu)
        serialize()
    }



        override fun update(gpu: GPUModel) {
            val gpusList = findAll() as ArrayList<GPUModel>
            var foundGPU: GPUModel? = gpusList.find { p -> p.id == gpu.id }
            if (foundGPU != null) {
                foundGPU.title = gpu.title
                foundGPU.description = gpu.description
                foundGPU.image = gpu.image
                foundGPU.lat = gpu.lat
                foundGPU.lng = gpu.lng
                foundGPU.zoom = gpu.zoom
            }
            serialize()
        }


    override fun delete(gpu: GPUModel) {
        gpus.remove(gpu)
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(gpus, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        gpus = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        gpus.forEach { Timber.i("$it") }
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}