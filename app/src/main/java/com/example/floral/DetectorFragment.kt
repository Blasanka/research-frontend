package com.example.floral

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.floral.databinding.FragmentDetectorBinding
import com.example.floral.disease.Constants
import com.example.floral.disease.DiseasesService
import com.example.floral.disease.PredictionResultActivity
import com.example.floral.disease.ResultResponse
import com.google.gson.GsonBuilder
import com.kaopiz.kprogresshud.KProgressHUD
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

typealias CornersListener = () -> Unit

class DetectorFragment : Fragment() {
    private lateinit var diseaseData: ResultResponse
    private lateinit var capturedFile: File
    private var isOffline: Boolean = false

    private lateinit var binding: FragmentDetectorBinding

    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null

    private lateinit var safeContext: Context

    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService

    private var hud: KProgressHUD? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        binding = FragmentDetectorBinding.inflate(inflater, container, false)

        hud = KProgressHUD.create(activity)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait")
            .setCancellable(true);

        binding.selectPhotoFromGallery.setOnClickListener {
            if(ActivityCompat.checkSelfPermission(activity!!,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    2000);
            }
            else {
                val cameraIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                cameraIntent.setType("image/*");
                if (cameraIntent.resolveActivity(activity!!.getPackageManager()) != null) {
                    startActivityForResult(cameraIntent, Constants.GALLERY_IMAGE_ACTIVITY_REQUEST_CODE);
                }
            }
        }

        binding.selectPhotoFromGallery.setOnClickListener {
            if(ActivityCompat.checkSelfPermission(activity!!,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    2000);
            }
            else {
                val cameraIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                cameraIntent.setType("image/*");
                if (cameraIntent.resolveActivity(activity!!.getPackageManager()) != null) {
                    startActivityForResult(cameraIntent, Constants.GALLERY_IMAGE_ACTIVITY_REQUEST_CODE);
                }
            }
        }

        binding.takePictureWithCamera.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(
                intent,
                Constants.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE
            )
            takePhoto()
        }

        binding.submitImage.setOnClickListener {
            hud!!.show()

            val gson = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()
            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

            val diseaseService = retrofit.create(DiseasesService::class.java)
            val reqFile: RequestBody = RequestBody.create(MediaType.get("image/*"), capturedFile.readBytes())
            val body = MultipartBody.Part.createFormData("file", capturedFile.getName(), reqFile)
//            val name: RequestBody = RequestBody.create(MediaType.parse("text/plain"), "upload_test")
            // Fetch the national data
            diseaseService.detectDisease(body).enqueue(object: Callback<ResultResponse> {
                override fun onResponse(call: Call<ResultResponse>, response: Response<ResultResponse>) {
                    Log.i(Constants.TAG, "onResponse: $response")
                    val data = response.body()
                    if (data == null) {
                        Log.w(Constants.TAG, "Did not received a valid response body")
                        return
                    }
                    diseaseData = data
                    Log.i(Constants.TAG, "Update result view with predicted data")
                    Log.i(Constants.TAG, "${diseaseData.result}")
                    hud?.dismiss()
                    val intent = Intent(activity, PredictionResultActivity::class.java)
                    intent.putExtra("result", diseaseData)
                    startActivity(intent)
                }

                override fun onFailure(call: Call<ResultResponse>, t: Throwable) {
                    Log.e(Constants.TAG, "onFailure: $t")
                    hud?.dismiss();
                }
            })

        }
        return binding.root;
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(activity!!, Constants.REQUIRED_PERMISSIONS, Constants.REQUEST_CODE_PERMISSION)
        }

        outputDirectory = getOutputDirectory()

        cameraExecutor = Executors.newSingleThreadExecutor()
//        cameraExecutor = Executors.newCachedThreadPool()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        safeContext = context
    }

    private fun getStatusBarHeight(): Int {
        val resourceId = safeContext.resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            safeContext.resources.getDimensionPixelSize(resourceId)
        } else 0
    }

    private fun allPermissionsGranted() = Constants.REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(safeContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == Constants.REQUEST_CODE_PERMISSION) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(safeContext, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
//                finish()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun getOutputDirectory(): File {
        val mediaDir = activity?.externalMediaDirs?.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else activity?.filesDir!!
    }

    override fun onPause() {
        super.onPause()
        isOffline = true
    }

    override fun onResume() {
        super.onResume()
        isOffline = false
    }

    private fun takePhoto() {
        // Get a stable reference of the modifiable image capture use case
        val imageCapture = imageCapture ?: return

        // Create timestamped output file to hold the image
        val photoFile = File(outputDirectory, SimpleDateFormat(Constants.FILE_NAME_FORMAT, Locale.US).format(System.currentTimeMillis()) + ".jpg")

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        // Setup image capture listener which is triggered after photo has
        // been taken
        imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(safeContext), object : ImageCapture.OnImageSavedCallback {
            override fun onError(exc: ImageCaptureException) {
                Log.e(Constants.TAG, "Photo capture failed: ${exc.message}", exc)
            }

            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                val savedUri = Uri.fromFile(photoFile)
                val msg = "Photo capture succeeded: $savedUri"
                Toast.makeText(safeContext, msg, Toast.LENGTH_SHORT).show()
                Log.d(Constants.TAG, msg)
                capturedFile = photoFile
            }
        })
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(safeContext)

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            preview = Preview.Builder().build()

            imageCapture = ImageCapture.Builder().build()

//            imageAnalyzer = ImageAnalysis.Builder().build().apply {
//                setAnalyzer(Executors.newSingleThreadExecutor(), CornerAnalyzer {
//                    val bitmap = viewFinder.bitmap
//                    val img = Mat()
//                    Utils.bitmapToMat(bitmap, img)
//                    bitmap?.recycle()
//                    // Do image analysis here if you need bitmap
//                })
//            }
            // Select back camera
            val cameraSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                camera = cameraProvider.bindToLifecycle(this, cameraSelector, imageAnalyzer, preview, imageCapture)
                preview?.setSurfaceProvider(binding.viewFinder.surfaceProvider)
            } catch (exc: Exception) {
                Log.e(Constants.TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(safeContext))

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val bmp = data?.extras!!["data"] as Bitmap?
                val stream = ByteArrayOutputStream()
                bmp!!.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val byteArray: ByteArray = stream.toByteArray()

                // convert byte array to Bitmap
                val bitmap = BitmapFactory.decodeByteArray(
                    byteArray, 0,
                    byteArray.size
                )
                capturedFile = File(outputDirectory, SimpleDateFormat(Constants.FILE_NAME_FORMAT, Locale.US).format(System.currentTimeMillis()) + ".jpg")

                val os: OutputStream = BufferedOutputStream(FileOutputStream(capturedFile))
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
                os.close()

                binding.image.setImageBitmap(bitmap)
            }
        }

        if(resultCode == Activity.RESULT_OK) {
            if(requestCode == Constants.GALLERY_IMAGE_ACTIVITY_REQUEST_CODE){
                val returnUri = data?.data
                val bitmapImage = MediaStore.Images.Media.getBitmap(activity!!.getContentResolver(), returnUri);
                binding.image.setImageBitmap(bitmapImage);
                capturedFile = File(outputDirectory, SimpleDateFormat(Constants.FILE_NAME_FORMAT, Locale.US).format(System.currentTimeMillis()) + ".jpg")
                val os: OutputStream = BufferedOutputStream(FileOutputStream(capturedFile))
                bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, os)
                os.close()
            }
        }
    }

}