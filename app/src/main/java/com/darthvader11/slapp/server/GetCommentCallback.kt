package com.darthvader11.slapp.server

import org.json.JSONArray


interface GetCommentCallback {

    fun done(returnedCode: JSONArray)


}