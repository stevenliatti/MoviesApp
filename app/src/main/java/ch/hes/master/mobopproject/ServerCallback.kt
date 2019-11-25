package ch.hes.master.mobopproject


interface ServerCallback<T> {
    fun onSuccess(result: T)
}