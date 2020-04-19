package com.darthvader11.slapp.ui.newpost


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.darthvader11.slapp.Objects.Post
import com.darthvader11.slapp.Objects.UserLocalStore
import com.darthvader11.slapp.R
import com.darthvader11.slapp.models.Feed
import com.darthvader11.slapp.models.feedSupplier
import com.darthvader11.slapp.server.GetPostCallback
import com.darthvader11.slapp.server.ServerRequest
import com.darthvader11.slapp.ui.feed.FeedFragment
import com.google.android.material.textfield.TextInputEditText

class NewpostFragment : Fragment(), View.OnClickListener {

    lateinit var post: Feed
    lateinit var title: TextInputEditText
    lateinit var description: TextInputEditText
    lateinit var location: TextInputEditText
    lateinit var author: String
    lateinit var genre: TextInputEditText
    lateinit var tags: TextInputEditText
    private val RESULT_LOAD_IMAGE = 1
    lateinit var imageToUpload: ImageView


    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_newpost, container, false)

        val uploadImage : Button = root.findViewById(R.id.uploadImageButton)
        uploadImage.setOnClickListener(this)
        val submit: Button = root.findViewById(R.id.btnSubmit)
        submit.setOnClickListener(this)

        imageToUpload = root.findViewById(R.id.uploadImage)

        val userbase = UserLocalStore(context!!)
        val user = userbase.getLoggedInUser()

         title= root.findViewById(R.id.inputTitle)
         author = user.username
         description = root.findViewById(R.id.inputDescription)
         location= root.findViewById(R.id.inputLocation)
        tags = root.findViewById(R.id.inputTags)
        genre = root.findViewById(R.id.inputGenre)


        return root

    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.uploadImageButton -> {
                val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE)

            }
            R.id.btnSubmit -> {

                var image: Bitmap = (imageToUpload.drawable.toBitmap())

                post = Feed(title.text.toString(), author , 0,
                    tags.text.toString(),
                    description.text.toString(),
                    image,
                    image,
                    location.text.toString(),
                    genre.text.toString()
                    )


                Log.v("newpost", title.text.toString())
                Log.v("newpost", description.text.toString())
                Log.v("newpost", location.text.toString())
                Log.v("newpost", "At least something?")
                Log.v("image", "After button pressed")

                var serverRequest = ServerRequest(context!!, R.layout.fragment_newpost)
                serverRequest.submitPost(post)


                Toast.makeText(context,"Post has been uploaded!", Toast.LENGTH_SHORT).show()


                val manager: FragmentManager? = fragmentManager
                val transaction: FragmentTransaction? = manager?.beginTransaction()
                transaction?.replace(R.id.nav_host_fragment, FeedFragment(), FeedFragment::class.java.simpleName)
                transaction?.addToBackStack(null)
                transaction?.commit()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null){
            var selectedImage: Uri = data.data!!
            imageToUpload.setImageURI(selectedImage)

        }


    }
}
















