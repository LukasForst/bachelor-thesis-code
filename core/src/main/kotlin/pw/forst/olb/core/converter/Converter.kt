package pw.forst.olb.core.converter

interface Converter<T : Any, V : Any> {
    fun convert(t: T): V

    fun convertBack(v: V): T
}
