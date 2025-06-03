package ai.face.liva.sdk.appHelper.ext


fun<T> T.isNull(it:()->Unit):T?{
    if (this == null){
        it.invoke()
    }
    return this
}
