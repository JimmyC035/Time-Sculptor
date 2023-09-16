package com.example.timesculptor.data.source.source

import android.util.Log
import com.example.timesculptor.data.source.source.TimeSculptorRepository
import javax.inject.Inject

class Repo @Inject constructor() : TimeSculptorRepository {
    override fun hello() {
        super.hello()
        Log.i("jason","hello")
    }

}