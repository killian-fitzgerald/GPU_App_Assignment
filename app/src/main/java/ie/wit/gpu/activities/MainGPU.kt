package ie.wit.gpu.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import ie.wit.gpu.R
import ie.wit.gpu.databinding.ActivityGpuBinding
import ie.wit.gpu.helpers.showImagePicker
import ie.wit.gpu.main.MainApp
import ie.wit.gpu.models.GPUModel
import ie.wit.gpu.models.Location
import timber.log.Timber
import timber.log.Timber.i

class MainGPU : AppCompatActivity() {
    private lateinit var binding: ActivityGpuBinding
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
//    var location = Location(52.245696, -7.139102, 15f)
    var gpu = GPUModel()

    lateinit var app: MainApp

    var edit = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        binding = ActivityGpuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbarAdd.title = title
        setSupportActionBar(binding.toolbarAdd)
        app = application as MainApp


        if (intent.hasExtra("gpu_edit")) {
            edit = true

            Picasso.get()
                .load(gpu.image)
//                .into(binding.gpuImage)

            if (gpu.image != Uri.EMPTY) {
                binding.chooseImage.setText(R.string.change_gpu_image)
            }

            gpu = intent.extras?.getParcelable("gpu_edit")!!
            binding.gpuTitle.setText(gpu.title)
            binding.description.setText(gpu.description)
            binding.btnAdd.setText(R.string.save_gpu)
        }


        binding.btnAdd.setOnClickListener() {
            gpu.title = binding.gpuTitle.text.toString()
            gpu.description = binding.description.text.toString()
            if (gpu.title.isEmpty()) {
                Snackbar.make(it,R.string.enter_GPU_title, Snackbar.LENGTH_LONG)
                    .show()
            } else {
                if (edit) {
                    app.gpus.update(gpu.copy())
                } else {
                    app.gpus.create(gpu.copy())
                }
            }
            setResult(RESULT_OK)
            finish()
        }

        binding.chooseImage.setOnClickListener {
            showImagePicker(imageIntentLauncher)

        }

        registerImagePickerCallback()

        registerMapCallback()

        binding.gpuLocation.setOnClickListener {
            val launcherIntent = Intent(this, MapActivity::class.java)
            mapIntentLauncher.launch(launcherIntent)

        }

        binding.gpuLocation.setOnClickListener {
            val location = Location(52.245696, -7.139102, 15f)
            if (gpu.zoom != 0f) {
                location.lat =  gpu.lat
                location.lng = gpu.lng
                location.zoom = gpu.zoom
            }
            val launcherIntent = Intent(this, MapActivity::class.java)
                .putExtra("location", location)
            mapIntentLauncher.launch(launcherIntent)
        }




        }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_gpu, menu)
        if (edit) menu.getItem(0).isVisible = true
        return super.onCreateOptionsMenu(menu)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_delete -> {
                app.gpus.delete(gpu)
                finish()
            }
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when(result.resultCode){
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Result ${result.data!!.data}")
                            gpu.image = result.data!!.data!!
                            Picasso.get()
                                .load(gpu.image)
//                                .into(binding.gpuImage)
                            binding.chooseImage.setText(R.string.change_gpu_image)
                        } //
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {
                            i("Got Location ${result.data.toString()}")
                            val location = result.data!!.extras?.getParcelable<Location>("location")!!
                            i("Location == $location")
                            gpu.lat = location.lat
                            gpu.lng = location.lng
                            gpu.zoom = location.zoom
                        }
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }



    }




