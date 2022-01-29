package lmu.msp.frontend.helpers

import androidx.lifecycle.MutableLiveData

//just a collection of functions to help with liveData Obj
/**
 * clear mutable live data list
 *
 * @param T
 * @param liveData
 */
fun <T> liveDataListClear(liveData: MutableLiveData<MutableList<T>>) {
    liveData.postValue(mutableListOf())
}

/**
 * add a element to the mutable live data list
 *
 * @param T
 * @param element
 * @param liveData
 */
fun <T> liveDataListAddElement(element: T, liveData: MutableLiveData<MutableList<T>>) {
    val data = liveData.value ?: mutableListOf()
    data.add(element)
    liveData.postValue(data)
}

fun <T> liveDataRemoveElement(element: T, liveData: MutableLiveData<MutableList<T>>) {
    val data = liveData.value ?: mutableListOf()
    data.remove(element)
    liveData.postValue(data)
}

/**
 * add all elements to the mutable live data list
 *
 * @param T
 * @param E
 * @param list
 * @param liveData
 */
fun <T : List<E>, E> liveDataListAddElementList(list: T, liveData: MutableLiveData<MutableList<E>>) {
    val data = liveData.value ?: mutableListOf()
    data.addAll(list)
    liveData.postValue(data)
}
