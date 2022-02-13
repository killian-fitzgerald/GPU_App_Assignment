package ie.wit.gpu.activities


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import ie.wit.gpu.R
import ie.wit.gpu.adapters.GPUAdapter
import ie.wit.gpu.adapters.GPUListener
import ie.wit.gpu.databinding.ActivityGpuListBinding
import ie.wit.gpu.databinding.CardGpuBinding
import ie.wit.gpu.main.MainApp
import ie.wit.gpu.models.GPUModel

class GPUListActivity : AppCompatActivity(), GPUListener {

    lateinit var app: MainApp
    private lateinit var binding: ActivityGpuListBinding
    private lateinit var refreshIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGpuListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.title = title

        setSupportActionBar(binding.toolbar)

        app = application as MainApp

        val layoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = layoutManager
        loadGPUs()


        registerRefreshCallback()

        registerMapCallback()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_add -> {
                val launcherIntent = Intent(this, MainGPU::class.java)
                refreshIntentLauncher.launch(launcherIntent)
            }

            R.id.item_map -> {
                val launcherIntent = Intent(this, GPUMapsActivity::class.java)
                mapIntentLauncher.launch(launcherIntent)
            }


        }
        return super.onOptionsItemSelected(item)
    }



    override fun onGPUClick(gpu: GPUModel) {
        val launcherIntent = Intent(this, MainGPU::class.java)
        launcherIntent.putExtra("gpu_edit", gpu)
        refreshIntentLauncher.launch(launcherIntent)
    }

    private fun registerRefreshCallback() {
        refreshIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { loadGPUs() }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            {  }
    }

    private fun loadGPUs() {
        showGPUs(app.gpus.findAll())
    }

    fun showGPUs (gpus: List<GPUModel>) {
        binding.recyclerView.adapter = GPUAdapter(gpus, this)
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }




}

