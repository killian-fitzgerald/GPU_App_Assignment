package ie.wit.gpu.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.wit.gpu.databinding.CardGpuBinding
import ie.wit.gpu.models.GPUModel


interface GPUListener {
    fun onGPUClick(gpu: GPUModel)
}

class GPUAdapter constructor(private var gpus: List<GPUModel>,
                                   private val listener: GPUListener) :
    RecyclerView.Adapter<GPUAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardGpuBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val gpu = gpus[holder.adapterPosition]
        holder.bind(gpu, listener)
    }

    override fun getItemCount(): Int = gpus.size

    class MainHolder(private val binding: CardGpuBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(gpu: GPUModel, listener: GPUListener) {
            binding.gpuTitle.text = gpu.title
            binding.description.text = gpu.description
            Picasso.get().load(gpu.image).resize(200,200).into(binding.imageIcon)
            binding.root.setOnClickListener {
                listener.onGPUClick(gpu)
            }
        }
    }

}