package com.darthvader11.slapp.server

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.darthvader11.slapp.Objects.Post
import com.darthvader11.slapp.Objects.User
import com.darthvader11.slapp.Objects.UserLocalStore
import com.darthvader11.slapp.Objects.user_id
import com.darthvader11.slapp.models.Comment
import com.darthvader11.slapp.models.Feed
import com.darthvader11.slapp.models.feedSupplier
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import javax.security.auth.callback.Callback

class ServerRequest() {

    lateinit var progressDialog: ProgressBar
    val CONNECTION_TIMEOUT: Int = 1000 * 80

    constructor(context: Context, layout: Int) : this(){
        var layout: ConstraintLayout = View.inflate(context,layout , null) as ConstraintLayout
        progressDialog = ProgressBar(context, null, android.R.attr.progressBarStyleLarge)
        var params: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(100, 100)
        params.addRule(RelativeLayout.CENTER_IN_PARENT)
        layout.addView(progressDialog, params)
    }


    fun storeUserDataInBackground(user: User, callback: GetUserCallback) {
        progressDialog.visibility = View.VISIBLE
        StoreUserDataAsyncTask(user, callback).execute()
    }

    fun fetchUserDataInBackground(user: User, callback: GetUserCallback) {
        progressDialog.visibility = View.VISIBLE
        FetchUserDataAsyncTask(user, callback).execute()
    }
    fun submitPost(post: Feed){
        progressDialog.visibility = View.VISIBLE
        SubmitPostAsyncTask(post).execute()
    }

    fun downloadImage(name: String, callback: GetPostCallback){
        progressDialog.visibility = View.VISIBLE
        DownloadImageAsyncTask(name, callback).execute()
    }

    fun updateAllFeed(feedCallback: GetFeedCallback){
        FetchAllPostsAsyncTask(feedCallback).execute()
    }

    fun updateLikes(likes: Int, post_id: Int){
        UpdateLikesAsyncTask(likes,post_id).execute()
    }

    fun checkLikes(user: String, post_id: Int, callback: GetLikeCallback){
        CheckLikesAsyncTask(user,post_id, callback).execute()
    }
    fun addComment(comment: Comment, post_id: Int){
        AddCommentAsyncTask(comment,post_id).execute()
    }

    fun fetchComments(post_id: Int, callback: GetCommentCallback){
        FetchCommentsAsyncTask(post_id,callback).execute()
    }

    private fun getUserIdHidden(username: String, userCallback: GetUserIdCallback){
        GetUserIdAsyncTask(username, userCallback).execute()
    }

    fun getUserId(context: Context) : Int{
        var userlogin = UserLocalStore(context)
        var username = userlogin.getLoggedInUser().username
        var userId: Int = 0
        getUserIdHidden(username, object : GetUserIdCallback{
            override fun done(returnedUser: Int) {
                userId = returnedUser
            }
        })
        return userId
    }







    inner class StoreUserDataAsyncTask(user: User, callback: GetUserCallback) : AsyncTask<Void, Void, Void>() {

        var user: User = user
        var userCallback: GetUserCallback = callback

        override fun doInBackground(vararg params: Void?): Void? {

            var url = URL("http://capitanu.tech/Register.php")
            var httpConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
            httpConnection.requestMethod = "POST"
            httpConnection.doOutput = true
            httpConnection.connectTimeout = CONNECTION_TIMEOUT

            Log.d("ServerDebug", "got to StoreUserDataAsyncTask.doInBackground()")


            var builder: Uri.Builder = Uri.Builder()
            builder.appendQueryParameter("username", user.username)
            builder.appendQueryParameter("email", user.email)
            builder.appendQueryParameter("password", user.password)


            var query: String = builder.build().encodedQuery as String

            var os: OutputStream = httpConnection.outputStream
            var bf = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
            bf.write(query)
            bf.flush()
            bf.close()

            Log.d("ServerDebug", httpConnection.responseCode.toString())

            var response = httpConnection.responseCode
            if(response != HttpURLConnection.HTTP_OK)
                throw Exception("THE RESPONSE WAS NOT GOOD!!")

            httpConnection.disconnect()

            return null
        }

        override fun onPostExecute(result: Void?) {

            userCallback.done(null)
            progressDialog.visibility = View.INVISIBLE

            super.onPostExecute(result)


        }
    }


    inner class FetchUserDataAsyncTask(user: User, callback: GetUserCallback) : AsyncTask<Void, Void, User>() {

        var user: User = user
        var userCallback: GetUserCallback = callback


        override fun doInBackground(vararg params: Void?): User {


            var builder: Uri.Builder = Uri.Builder()
            builder.appendQueryParameter("email", user.email)
            builder.appendQueryParameter("password", user.password)

            var query: String = builder.build().encodedQuery as String

            var returnedUser: User


            var url: URL = URL("http://capitanu.tech/FetchUserData.php")
            var httpConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
            httpConnection.requestMethod = "POST"
            httpConnection.doOutput = true
            httpConnection.doInput = true
            httpConnection.connectTimeout = CONNECTION_TIMEOUT


            var os: OutputStream = httpConnection.outputStream
            var bf = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
            bf.write(query)
            bf.flush()
            bf.close()


            Log.d("ServerDebug",httpConnection.responseCode.toString())
            Log.d("ServerDebug",httpConnection.responseMessage.toString())

            var inputStream = httpConnection.inputStream
            var bufferedReader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
            var jObject: JSONObject = JSONObject(bufferedReader.readLine())

            Log.v("user_id", jObject.toString())
            if(jObject.length() != 0){
                Log.v("happened", "2")
                var username: String = jObject.getString("username")
                Log.v("TEST", jObject.toString())
                var user_id: Int = jObject.getInt("user_id")

                returnedUser = User(
                    username,
                    user.email,
                    user.password,
                    user_id
                )
            }
            else throw Exception("JOBJECT FAULT")

            return returnedUser
        }

        override fun onPostExecute(returnedUser: User) {

            userCallback.done(returnedUser)
            progressDialog.visibility = View.INVISIBLE


            super.onPostExecute(returnedUser)


        }

    }

    inner class SubmitPostAsyncTask(post: Feed) : AsyncTask<Void, Void, Void>() {

        var post: Feed = post


        override fun doInBackground(vararg params: Void?): Void? {


            var byteArrayOutputStream = ByteArrayOutputStream()
            post.postPic?.compress(Bitmap.CompressFormat.JPEG, 100 , byteArrayOutputStream)
            var encodedImage: String = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)

            var url = URL("http://capitanu.tech/UploadPost.php")
            var httpConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
            httpConnection.requestMethod = "POST"
            httpConnection.doOutput = true
            httpConnection.connectTimeout = CONNECTION_TIMEOUT * 10

            Log.d("ServerDebug", "got to SubmitPostDataAsyncTask.doInBackground()")


            var builder: Uri.Builder = Uri.Builder()
            builder.appendQueryParameter("Title", post.postTitle)
            builder.appendQueryParameter("Author", post.author)
            builder.appendQueryParameter("LikesCount", post.likesCount.toString())
            builder.appendQueryParameter("Tags", post.tags)
            builder.appendQueryParameter("Description", post.description)
            builder.appendQueryParameter("Genre", post.genre)
            builder.appendQueryParameter("Location", post.location)
            builder.appendQueryParameter("Picture", encodedImage)
            builder.appendQueryParameter("ProfilePicture", encodedImage)




            Log.v("Server", encodedImage)
            Log.v("Server", encodedImage.length.toString())


            var query: String = builder.build().encodedQuery as String

            Log.v("Post", query)

            var os: OutputStream = httpConnection.outputStream
            var bf = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
            bf.write(query)
            bf.flush()
            bf.close()

            Log.d("ServerDebug", httpConnection.responseCode.toString())

            var response = httpConnection.responseCode
            if (response != HttpURLConnection.HTTP_OK)
                throw Exception("THE RESPONSE WAS NOT GOOD!!")

            httpConnection.disconnect()

            return null
        }

        override fun onPostExecute(result: Void?) {
            Log.v("Post", "Post has been submited")
            progressDialog.visibility = View.INVISIBLE
            super.onPostExecute(result)


        }
    }

    inner class DownloadImageAsyncTask(name: String, callback: GetPostCallback) : AsyncTask<Void, Void, Bitmap>() {

        var name: String = name
        var postCallback: GetPostCallback = callback


        override fun doInBackground(vararg params: Void?): Bitmap? {



            if(name.equals("null")){
                return null
            }
            var url = URL("http://capitanu.tech/pictures/31" + name + ".JPG")
            var httpConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
            httpConnection.connectTimeout = CONNECTION_TIMEOUT
            httpConnection.readTimeout = CONNECTION_TIMEOUT


            Log.v("ServerDebug", httpConnection.responseCode.toString())
            return BitmapFactory.decodeStream(httpConnection.getContent() as InputStream, null, null)


        }

        override fun onPostExecute(result: Bitmap?){
            progressDialog.visibility = View.INVISIBLE
            postCallback.done(result)
            super.onPostExecute(result)
        }
    }

    inner class FetchAllPostsAsyncTask(feedCallback: GetFeedCallback) : AsyncTask<Void, Void, JSONArray>() {

        var feedCallback: GetFeedCallback = feedCallback

        override fun doInBackground(vararg params: Void?): JSONArray {



            var url: URL = URL("http://capitanu.tech/FetchAllPosts.php")
            var httpConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
            httpConnection.requestMethod = "POST"
            httpConnection.doOutput = true
            httpConnection.doInput = true
            httpConnection.connectTimeout = CONNECTION_TIMEOUT


            //Log.d("ServerDebug responseCode",httpConnection.responseCode.toString())
            //Log.d("ServerDebug respondeMessage",httpConnection.responseMessage.toString())

            var inputStream = httpConnection.inputStream
            var bufferedReader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
            var jArray = JSONArray(bufferedReader.readLine())

            Log.v("JSONArray", jArray.toString())
            Log.v("JSONArray length", jArray.length().toString())


/*
            for(i in 0 until jArray.length()){
                Log.v("jobject",jArray[i].toString())
                var jObject: JSONObject = jArray[i] as JSONObject

                downloadImage(jObject.getString("Title"), object: GetPostCallback{
                    override fun done(returnedImage: Bitmap?) {
                        if(returnedImage != null) {
                            feedSupplier.feedContent.add(
                                Feed(
                                    jObject.getString("Title"),
                                    "@" + jObject.getString("Author"),
                                    jObject.getInt("LikesCount"),
                                    jObject.getString("Genre"),
                                    returnedImage
                                )
                            )
                            Log.v("shouldBeAfterFeed", feedSupplier.feedContent.size.toString())
                            Log.v("added", "one feed item has been added")
                        }
                    }
                })

                Log.v("download", jObject.getString("Title"))
            }
            Log.v("JARRAY", jArray.length().toString())*/
            httpConnection.disconnect()
            return jArray
        }

        override fun onPostExecute(result: JSONArray){
            progressDialog.visibility = View.INVISIBLE
            feedCallback.done(result)
            //Log.v("shouldBeAfterFeed", feedSupplier.feedContent.size.toString())
            Log.v("JSONArray", "Array has been fetched")
            super.onPostExecute(result)

        }

    }

   inner class UpdateLikesAsyncTask(likes: Int, post_id: Int) : AsyncTask<Void, Void, Void?>() {

       var likes: Int = likes
       var post_id: Int = post_id


        override fun doInBackground(vararg params: Void?): Void? {


            var builder: Uri.Builder = Uri.Builder()
            builder.appendQueryParameter("post_id", post_id.toString())
            builder.appendQueryParameter("likes", likes.toString())

            var query: String = builder.build().encodedQuery as String



            var url: URL = URL("http://capitanu.tech/UpdateLikes.php")
            var httpConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
            httpConnection.requestMethod = "POST"
            httpConnection.doOutput = true
            httpConnection.doInput = true
            httpConnection.connectTimeout = CONNECTION_TIMEOUT


            var os: OutputStream = httpConnection.outputStream
            var bf = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
            bf.write(query)
            bf.flush()
            bf.close()


            Log.d("ServerDebug",httpConnection.responseCode.toString())
            Log.d("ServerDebug",httpConnection.responseMessage.toString())
            httpConnection.disconnect()

            return null
        }

        override fun onPostExecute(returned: Void?) {
            super.onPostExecute(returned)
            Log.v("updatelikes", "Likes Updated")
        }
    }



    inner class CheckLikesAsyncTask(username: String, post_id: Int, callback: GetLikeCallback) : AsyncTask<Void, Void, Boolean>() {

        var username: String = username
        var post_id: Int = post_id
        var likeCallback: GetLikeCallback = callback


        override fun doInBackground(vararg params: Void?): Boolean {



            var url: URL = URL("http://capitanu.tech/CheckLikes.php")
            var httpConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
            httpConnection.requestMethod = "POST"
            httpConnection.doOutput = true
            httpConnection.doInput = true
            httpConnection.connectTimeout = 1000*15


            var builder: Uri.Builder = Uri.Builder()
            builder.appendQueryParameter("post_id", post_id.toString())
            builder.appendQueryParameter("user", username)
            var query: String = builder.build().encodedQuery as String

            var os: OutputStream = httpConnection.outputStream
            var bf = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
            bf.write(query)
            bf.flush()
            bf.close()



            Log.d("ServerDebug",httpConnection.responseCode.toString())
            Log.d("ServerDebug",httpConnection.responseMessage.toString())


            var inputStream = httpConnection.inputStream
            var bufferedReader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
            var jObject = JSONObject(bufferedReader.readLine())
            Log.v("TEEEST", jObject.getString("post_id"))
            Log.v("TEEEST", jObject.getString("user"))

            httpConnection.disconnect()

            if(jObject.getString("post_id").equals("NOT_FOUND") &&
                jObject.getString("user").equals("NOT_FOUND"))
                return true
            return false
        }

        override fun onPostExecute(returned: Boolean) {
            super.onPostExecute(returned)
            likeCallback.done(returned)
            Log.v("updatelikes", "Likes Updated")
        }
    }

    inner class AddCommentAsyncTask(comment: Comment, post_id: Int) : AsyncTask<Void, Void, Void?>() {

        var comment: Comment = comment
        var post_id: Int = post_id


        override fun doInBackground(vararg params: Void?): Void? {


            var builder: Uri.Builder = Uri.Builder()

            builder.appendQueryParameter("author", comment.author)
            builder.appendQueryParameter("comment", comment.comment)
            builder.appendQueryParameter("post_id", post_id.toString())

            Log.v("commentTesting", post_id.toString())
            Log.v("commentTesting", comment.author)
            Log.v("commentTesting", comment.comment)

            var query: String = builder.build().encodedQuery as String



            var url: URL = URL("http://capitanu.tech/AddComment.php")
            var httpConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
            httpConnection.requestMethod = "POST"
            httpConnection.doOutput = true
            httpConnection.doInput = true
            httpConnection.connectTimeout = CONNECTION_TIMEOUT


            Log.v("commentTesting", query)

            var os: OutputStream = httpConnection.outputStream
            var bf = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
            bf.write(query)
            bf.flush()
            bf.close()


            Log.d("ServerDebug",httpConnection.responseCode.toString())
            Log.d("ServerDebug",httpConnection.responseMessage.toString())

            httpConnection.disconnect()

            return null
        }

        override fun onPostExecute(returned: Void?) {
            super.onPostExecute(returned)
            Log.v("updatelikes", "Likes Updated")
        }
    }

    inner class FetchCommentsAsyncTask(post_id: Int, commentCallback: GetCommentCallback) : AsyncTask<Void, Void, JSONArray>() {

        var commentCallback: GetCommentCallback = commentCallback
        var post_id: Int = post_id

        override fun doInBackground(vararg params: Void?): JSONArray {


            var url: URL = URL("http://capitanu.tech/FetchComments.php")
            var httpConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
            httpConnection.requestMethod = "POST"
            httpConnection.doOutput = true
            httpConnection.doInput = true
            httpConnection.connectTimeout = CONNECTION_TIMEOUT

            var builder: Uri.Builder = Uri.Builder()
            builder.appendQueryParameter("post_id", post_id.toString())

            var query: String = builder.build().encodedQuery as String


            var os: OutputStream = httpConnection.outputStream
            var bf = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
            bf.write(query)
            bf.flush()
            bf.close()


            //Log.d("ServerDebug responseCode",httpConnection.responseCode.toString())
            //Log.d("ServerDebug respondeMessage",httpConnection.responseMessage.toString())

            var inputStream = httpConnection.inputStream
            var bufferedReader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
            var jArray = JSONArray(bufferedReader.readLine())

            Log.v("JSONArray", jArray.toString())
            Log.v("JSONArray length", jArray.length().toString())


/*
            for(i in 0 until jArray.length()){
                Log.v("jobject",jArray[i].toString())
                var jObject: JSONObject = jArray[i] as JSONObject

                downloadImage(jObject.getString("Title"), object: GetPostCallback{
                    override fun done(returnedImage: Bitmap?) {
                        if(returnedImage != null) {
                            feedSupplier.feedContent.add(
                                Feed(
                                    jObject.getString("Title"),
                                    "@" + jObject.getString("Author"),
                                    jObject.getInt("LikesCount"),
                                    jObject.getString("Genre"),
                                    returnedImage
                                )
                            )
                            Log.v("shouldBeAfterFeed", feedSupplier.feedContent.size.toString())
                            Log.v("added", "one feed item has been added")
                        }
                    }
                })

                Log.v("download", jObject.getString("Title"))
            }
            Log.v("JARRAY", jArray.length().toString())*/
            httpConnection.disconnect()
            return jArray
        }


        override fun onPostExecute(result: JSONArray) {
            progressDialog.visibility = View.INVISIBLE
            commentCallback.done(result)
            //Log.v("shouldBeAfterFeed", feedSupplier.feedContent.size.toString())
            Log.v("JSONArray", "Array has been fetched")
            super.onPostExecute(result)

        }
    }



    inner class GetUserIdAsyncTask(username: String, userCallback: GetUserIdCallback) : AsyncTask<Void, Void, Int>() {

        var username: String = username
        var userCallBack:GetUserIdCallback = userCallback
        override fun doInBackground(vararg params: Void?): Int {


            var url: URL = URL("http://capitanu.tech/GetUserId.php")
            var httpConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
            httpConnection.requestMethod = "POST"
            httpConnection.doOutput = true
            httpConnection.doInput = true
            httpConnection.connectTimeout = CONNECTION_TIMEOUT

            var builder: Uri.Builder = Uri.Builder()
            builder.appendQueryParameter("username", username)

            var query: String = builder.build().encodedQuery as String


            var os: OutputStream = httpConnection.outputStream
            var bf = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
            bf.write(query)
            bf.flush()
            bf.close()

            Log.v("TestUsername", username)


            //Log.d("ServerDebug responseCode",httpConnection.responseCode.toString())
            //Log.d("ServerDebug respondeMessage",httpConnection.responseMessage.toString())

            var inputStream = httpConnection.inputStream
            var bufferedReader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))


            var jArray = JSONArray(bufferedReader.readLine())
            Log.v("TEST", jArray.toString())
            var jObject = jArray.getJSONObject(0)

            httpConnection.disconnect()

            var user_id = jObject.getInt("user_id")


            return user_id
        }


        override fun onPostExecute(result: Int) {
            progressDialog.visibility = View.INVISIBLE
            userCallBack.done(result)
            super.onPostExecute(result)

        }
    }





}