package com.darthvader11.slapp.server

import org.json.JSONArray


interface GetFeedCallback {

    fun done(returnedCode: JSONArray)


}