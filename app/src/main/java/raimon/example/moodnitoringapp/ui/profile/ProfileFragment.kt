package raimon.example.moodnitoringapp.ui.profile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import raimon.example.moodnitoringapp.R
import raimon.example.moodnitoringapp.databinding.FragmentProfileBinding
import java.io.ByteArrayOutputStream

class ProfileFragment : Fragment() {

    private var binding: FragmentProfileBinding? = null

    private var photoSelectedUri: Uri? = null
    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                photoSelectedUri = it.data?.data
                binding?.let {
                    Glide.with(this)
                        .load(photoSelectedUri)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .circleCrop()
                        .into(it.imgProfile)
                }

            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container,false)
        binding?.let {
            return it.root
        }
        // Inflate the layout for this fragment
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getProfile()
        configButton()
    }

    private fun configButton() {
        binding?.let { binding->
            binding.imgProfile.setOnClickListener {
                openGallery()
            }
            binding.btnUpdate.setOnClickListener {
                FirebaseAuth.getInstance().currentUser?.let { user->
                   if (photoSelectedUri == null){
                       updateUserProfile(user, Uri.parse(""))
                   }else{
                       uploadReduceImage(user)

                   }
                }
            }

        }
    }

    private fun uploadReduceImage(user: FirebaseUser) {
        val profileRef = FirebaseStorage.getInstance()
            .reference
            .child(user.uid)
            .child("profile")
            .child("my_photo")

        photoSelectedUri?.let { uri ->
            binding?.let { binding ->
                getBitMapFromUri(uri)?.let { bitmap->


                    val baos = ByteArrayOutputStream()

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos)
                    profileRef.putBytes(baos.toByteArray())
                        .addOnProgressListener {
                            val progress =
                                (100 * it.bytesTransferred / it.totalByteCount).toInt()
                            it.run {
                                binding.progressBar.progress = progress
                                binding.tvProgress.text = String.format("%s%%", progress)
                            }
                        }.addOnCompleteListener{
                            binding.progressBar.visibility = View.INVISIBLE
                            binding.tvProgress.text = ""
                        }
                        .addOnSuccessListener {
                            it.storage.downloadUrl.addOnSuccessListener { downloadUrl ->
                                updateUserProfile( user, downloadUrl)
                            }
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                activity,
                                "Error al subir imagen",
                                Toast.LENGTH_SHORT
                            )
                                .show()

                        }
                }
            }
        }
    }

    private fun getBitMapFromUri(uri: Uri): Bitmap? {
        activity?.let {
            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(it.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(it.contentResolver, uri)
            }
            return getResizeImage(bitmap, 356)

        }
        return null
    }
    private fun getResizeImage(image: Bitmap, maxSize: Int): Bitmap {
        var width = image.width
        var heigth = image.height
        if (width <= maxSize && heigth <= maxSize) return image

        val bitmapRatio = width.toFloat() / heigth.toFloat()
        if(bitmapRatio > 1){
            width = maxSize
            heigth = (width / bitmapRatio).toInt()
        }else{

            heigth = maxSize
            width = (heigth / bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, heigth, true)

    }

    private fun updateUserProfile(

        user: FirebaseUser,
        uri: Uri
    ) {
        val profileUpdate = UserProfileChangeRequest.Builder()
            .setPhotoUri(uri)
            .build()
        user.updateProfile(profileUpdate)
            .addOnSuccessListener {
                Toast.makeText(activity, "Usuario actualizado", Toast.LENGTH_SHORT).show()
                activity?.onBackPressed()
            }.addOnFailureListener {
                Toast.makeText(activity, "Error al actualizar el usuario ", Toast.LENGTH_SHORT).show()
            }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        resultLauncher.launch(intent)
    }

    private fun getProfile() {
        binding?.let { binding->
            FirebaseAuth.getInstance().currentUser?.let { user->
                binding.tvName.text = user.displayName

                Glide.with(this)
                    .load(user.photoUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .circleCrop()
                    .into(binding.imgProfile)

                setupActionBar()
            }
        }
    }


    private fun setupActionBar() {
        (activity as? AppCompatActivity)?.let {
            it.supportActionBar?.setDisplayHomeAsUpEnabled(false)
            it.supportActionBar?.title = "Perfil de usuario"
            //setHasOptionsMenu(true)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}