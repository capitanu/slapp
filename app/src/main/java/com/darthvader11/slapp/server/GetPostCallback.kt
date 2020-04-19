package com.darthvader11.slapp.server


import android.graphics.Bitmap
import com.darthvader11.slapp.Objects.User

interface GetPostCallback {

    fun done(returnedImage: Bitmap?)


}