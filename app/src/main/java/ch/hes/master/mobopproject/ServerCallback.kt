package ch.hes.master.mobopproject

import org.json.JSONObject

interface ServerCallback {
    fun onSuccess(result: JSONObject)
}