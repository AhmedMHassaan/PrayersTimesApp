package com.ahmed.m.hassaan.prayerstimesapp.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BaseViewModel<DATA> : ViewModel() {

    val progressLiveData = MutableLiveData<Boolean>()
    val errorLiveDATA = MutableLiveData<String>()
    val dataLiveData = MutableLiveData<DATA>()


}