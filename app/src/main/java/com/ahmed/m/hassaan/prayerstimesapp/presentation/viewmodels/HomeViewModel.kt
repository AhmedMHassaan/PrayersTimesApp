package com.ahmed.m.hassaan.prayerstimesapp.presentation.viewmodels

import android.location.Location
import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmed.m.hassaan.prayerstimesapp.R
import com.ahmed.m.hassaan.prayerstimesapp.base.App
import com.ahmed.m.hassaan.prayerstimesapp.data.local.model.PrayerTimesLocal
import com.ahmed.m.hassaan.prayerstimesapp.data.local.repo.LocalPrayersRepository
import com.ahmed.m.hassaan.prayerstimesapp.data.local.repo.LocationRepository
import com.ahmed.m.hassaan.prayerstimesapp.data.local.shared_ref.AddressCacheSharedPref
import com.ahmed.m.hassaan.prayerstimesapp.data.network.responses.Designation
import com.ahmed.m.hassaan.prayerstimesapp.data.network.responses.Gregorian
import com.ahmed.m.hassaan.prayerstimesapp.data.network.responses.Hijri
import com.ahmed.m.hassaan.prayerstimesapp.data.network.responses.HijriMonth
import com.ahmed.m.hassaan.prayerstimesapp.data.network.responses.HijriWeekday
import com.ahmed.m.hassaan.prayerstimesapp.data.network.responses.Month
import com.ahmed.m.hassaan.prayerstimesapp.data.network.responses.PrayersTimes
import com.ahmed.m.hassaan.prayerstimesapp.data.network.responses.PrayersTimesResponse
import com.ahmed.m.hassaan.prayerstimesapp.data.network.responses.QiblaDirection
import com.ahmed.m.hassaan.prayerstimesapp.data.network.responses.ResponseDate
import com.ahmed.m.hassaan.prayerstimesapp.data.network.responses.ResponseTimingData
import com.ahmed.m.hassaan.prayerstimesapp.data.network.responses.Weekday
import com.ahmed.m.hassaan.prayerstimesapp.data.repository.PrayersTimesRepository
import com.ahmed.m.hassaan.prayerstimesapp.data.repository.QiblaDirectionsRepository
import com.example.easywaylocation.EasyWayLocation
import com.example.easywaylocation.GetLocationDetail
import com.example.easywaylocation.Listener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.DateTime
import org.joda.time.MutableDateTime
import org.joda.time.Seconds
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeViewModel : ViewModel() {

    val easyWayListener: Listener = object : Listener {
        override fun locationOn() {
            _locationPermissionCanceled.postValue(false)
        }

        override fun currentLocation(location: Location?) {
            location?.let { loc ->

                lastLat = loc.latitude
                lastLong = loc.longitude

                saveLocation(lastLat, lastLong)
                val getLocationDetail = GetLocationDetail({
                    _address.postValue("${it?.city}, ${it?.country}")
                    saveAddress("${it?.city}, ${it?.country}")
                    Log.d(
                        App.APP_TAG,
                        "HomeActivity - locationData:  full address is ${it?.full_address}"
                    )
                }, App.mACTIVITY)

                getLocationDetail.getAddress(loc.latitude, loc.longitude, "xyz")
                Log.d(
                    App.APP_TAG,
                    "HomeActivity - currentLocation:  location is $location"
                )
                loadPrayersTimes(
                    true,
                    loc.latitude,
                    loc.longitude
                )
                requestedDayIndex = 0
                stopFetchingLocation()

            } ?: kotlin.run {
                Log.d(App.APP_TAG, "HomeActivity - currentLocation:  Null Location")
            }
        }

        override fun locationCancelled() {
            _locationPermissionCanceled.postValue(true)
            _error.postValue("Location Permission Refused")
        }
    }

    private fun saveAddress(address: String) {
        LocationRepository.saveAddress(address)
    }

    private fun saveLocation(lastLat: Double, lastLong: Double) {
        LocationRepository.saveLocation(lastLat, lastLong)
    }

    private lateinit var easyWayLocation: EasyWayLocation

    private val prayersTimesRepos = PrayersTimesRepository

    private val _progress = MutableLiveData<Boolean>()
    val progressLiveData = _progress

    var lastLat: Double = 0.0
    var lastLong: Double = 0.0
//    private val _response = MutableLiveData<ApiResponse<PrayersTimesResponse>>()
//    val responseLiveData = _response


    private val _currentShownTimes = MutableLiveData<PrayersTimes>()
    val timesLiveData: LiveData<PrayersTimes> = _currentShownTimes

    private val _selectedDate = MutableLiveData<String>()
    val dateLivedata: LiveData<String> = _selectedDate

    private val _address = MutableLiveData<String>()
    val addressLiveData: LiveData<String> = _address


    private val _nextPrayerName = MutableLiveData<String>()
    val nextPrayerNameLiveData: LiveData<String> = _nextPrayerName

    private val _remainingTimeUntilAzan = MutableLiveData<String>()
    val remainingTimeUntilAzan: LiveData<String> = _remainingTimeUntilAzan

    private var nextPrayerNameWithItsTime: Pair<String, String> =
        Pair("", "") // to hold name of prayer and it's time

    private var orignalTimes: PrayersTimes? = null

    private var result: ResponseTimingData? = null

    private var timer: CountDownTimer? = null

    private val _error = MutableLiveData<String>()
    val errorLiveData: LiveData<String> = _error

    private val _locationPermissionCanceled = MutableLiveData<Boolean>()
    val locationPermissionCanceled: LiveData<Boolean> = _locationPermissionCanceled

    var prayersTimesResponse: PrayersTimesResponse? = null

    var requestedDayIndex = 0

    private val _qiblaDirection = MutableLiveData<QiblaDirection>()
    val qiblaDirectionsLivedata: LiveData<QiblaDirection> = _qiblaDirection


    init {
        loadPrayersFromCache()
        lastLat = getSavedLatitude()
        lastLong = getSavedLongitude()
        _address.postValue(getSavedAddress())

    }

    private fun getSavedAddress(): String {
        return LocationRepository.getAddress()
    }

    private fun getSavedLongitude(): Double {
        return LocationRepository.getSavedLongitude()
    }

    private fun getSavedLatitude(): Double {
        return LocationRepository.getSavedLatitude()
    }

    fun initEasyWayLocation(_easyWayLocation: EasyWayLocation) {
        this.easyWayLocation = _easyWayLocation
    }

    fun loadPrayersTimes(isConnectedToNetwork: Boolean, latitude: Double, longitude: Double) {
        stopFetchingLocation()
        if (isConnectedToNetwork) {
            val date: String = SimpleDateFormat("dd:mm:yyyy", Locale.ENGLISH).format(Date())
            loadFromApi(date, latitude, longitude)
        } else {
            loadPrayersFromCache()
        }
    }


    private fun loadFromApi(date: String, latitude: Double, longitude: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            _progress.postValue(true)

            prayersTimesRepos.getPrayersTimes(date, latitude, longitude)
                .also {
                    prayersTimesResponse = it.result
                    _progress.postValue(it.isLoading)
                    result = it.result?.data
                    orignalTimes = result?.timings

                    refreshingData(result?.timings, result?.date?.readable.toString())


                    orignalTimes?.copy()?.let { it1 -> cachePrayerTimes(it1, result?.date!!) }


                }


        }

    }

    private fun refreshingData(timings: PrayersTimes?, date: String) {
        Log.d(App.APP_TAG, "HomeViewModel - refreshingData:  times is $timings")
        timings?.let {
            nextPrayerNameWithItsTime =
                calculateNextPrayerWithItsTime(orignalTimes)
            val allSeconds = calculateTimeUntilAdanStart(
                nextPrayerNameWithItsTime.second,
                nextPrayerNameWithItsTime.first
            )
            Log.d(
                App.APP_TAG,
                "HomeViewModel - refreshingData:  nextPrayer is ${nextPrayerNameWithItsTime} and minutes is $allSeconds and datenow is ${DateTime.now()}"
            )
            if (requestedDayIndex == 0)
                startTimer(allSeconds)
            _currentShownTimes.postValue(timings?.copy()?.convertTo12())
            _selectedDate.postValue(date)
            _nextPrayerName.postValue(nextPrayerNameWithItsTime.first ?: "")
        }

    }

    private fun startTimer(allSeconds: Int) {

        Log.d(
            App.APP_TAG,
            "HomeViewModel - startTimer:  all minitues = $allSeconds and time will finished after ${allSeconds * 1000} milleseconds"
        )

        if (allSeconds <= 0) {
//            refreshingData(result)
            Log.d(
                App.APP_TAG,
                "HomeViewModel - startTimer:  returned because minutes is $allSeconds but timer is still run? ${timer}"
            )
            timer?.cancel()
            timer = null
            return
        }

        val secondsToTimer = if (allSeconds >= 86400) allSeconds - 86400 else allSeconds
        timer?.cancel()
        timer = null

        viewModelScope.launch(Dispatchers.Main) {
            timer = object : CountDownTimer(
                (secondsToTimer * 1000).toLong(),
                1000
            ) {
                override fun onTick(millisUntilFinished: Long) {
                    val seconds = calculateTimeUntilAdanStart(
                        nextPrayerNameWithItsTime.second,
                        nextPrayerNameWithItsTime.first
                    )
                    if (seconds / 1000 >= 86300) {
                        timer?.cancel()
                        timer = null
                        startTimer(60)
                        return
                    }


                    updateRemainingTime(((millisUntilFinished / 1000).toInt()))
                    Log.d(
                        App.APP_TAG,
                        "HomeViewModel - onTick:  tik $millisUntilFinished = ${millisUntilFinished / 1000} )"
                    )
                }

                override fun onFinish() {
                    Log.d(App.APP_TAG, "HomeViewModel - onTick:  finished")
                    timer?.cancel()
                    timer = null
                    refreshingData(orignalTimes, result?.date?.readable!!)

                }
            }
            timer?.start()
        }
    }

    private fun calculateTimeUntilAdanStart(endTime: String, prayerName: String): Int {

        val azanDate: DateTime = MutableDateTime.now().also {
            Log.d(
                App.APP_TAG,
                "HomeViewModel - calculateTimeUntilAdanStart:  end time is $endTime and after split = ${
                    endTime.split(":")
                }"
            )
            val minutes = endTime.split(":")[1].toInt()
            val hours = endTime.split(":")[0].toInt()
            it.setTime(hours, minutes, 0, 0)
        }.toDateTime()


        val allSeconds = calculateMinutesBetweenNowAndAzan(azanDate, prayerName)
        updateRemainingTime(allSeconds)
        return allSeconds
    }

    private fun updateRemainingTime(allSeconds: Int) {

        val hours = allSeconds / 3600

        val minutes = (allSeconds % 3600) / 60

        val seconds = ((allSeconds % 3600) % 60)


//        val seconds =((allSeconds % hours))

//        _remainingTimeUntilAzan.postValue(
//            "${App.mACTIVITY.getString(R.string.time_left)}\n $hours ${
//                App.mACTIVITY.getString(
//                    R.string.hours
//                )
//            } $minutes ${App.mACTIVITY.getString(R.string.minutes)} "
//        )

        if (hours >= 20)
            _remainingTimeUntilAzan.postValue(
                "0 ${App.mACTIVITY.getString(R.string.hours)} $minutes ${
                    App.mACTIVITY.getString(
                        R.string.minutes
                    )
                } $seconds ${App.mACTIVITY.getString(R.string.seconds)}"
            )
        else
            _remainingTimeUntilAzan.postValue(
                "$hours ${App.mACTIVITY.getString(R.string.hours)} $minutes ${
                    App.mACTIVITY.getString(
                        R.string.minutes
                    )
                } $seconds ${App.mACTIVITY.getString(R.string.seconds)}"
            )

    }

    private fun calculateMinutesBetweenNowAndAzan(
        azanDate: DateTime,
        prayerName: String = App.mACTIVITY.getString(R.string.Fajr)
    ): Int {

        if (prayerName == App.mACTIVITY.getString(R.string.Fajr))
            return Seconds.secondsBetween(
                DateTime.now(),
                azanDate.plusDays(1)
            ).seconds
        else
            return Seconds.secondsBetween(
                DateTime.now(),
                azanDate.plusDays(1)
            ).seconds
//            return minutes + 1  // +1 is because last minute is 00:59 second that show in lbl as 0h:0m
    }

    private fun calculateNextPrayerWithItsTime(timings: PrayersTimes?): Pair<String, String> {
        return timings?.let {


            Log.d(
                App.APP_TAG,
                "HomeViewModel - calculateNextPrayer:  timeNow is ${DateTime.now()} and Maghrib is ${
                    it.toJodaDateTime(it.Maghrib)
                }"
            )


            if (it.toJodaDateTime(it.Fajr).isAfterNow || it.toJodaDateTime(it.Fajr).isEqualNow) {
                return Pair(App.mACTIVITY.getString(R.string.Fajr), it.Fajr)
            }
            if (it.toJodaDateTime(it.Dhuhr).isAfterNow || it.toJodaDateTime(it.Dhuhr).isEqualNow)
                return Pair(App.mACTIVITY.getString(R.string.Duhr), it.Dhuhr)

            if (it.toJodaDateTime(it.Asr).isAfterNow || it.toJodaDateTime(it.Asr).isEqualNow)
                return Pair(
                    App.mACTIVITY.getString(R.string.Asr), it.Asr
                )

            if (it.toJodaDateTime(it.Maghrib).isAfterNow || it.toJodaDateTime(it.Maghrib).isEqualNow)
                return Pair(
                    App.mACTIVITY.getString(R.string.Maghrib), it.Maghrib
                )

            if (it.toJodaDateTime(it.Isha).isAfterNow || it.toJodaDateTime(it.Isha).isEqualNow)
                return Pair(
                    App.mACTIVITY.getString(R.string.Isha), it.Isha
                )


            return Pair(App.mACTIVITY.getString(R.string.Fajr), it.Fajr)

        } ?: Pair("null times", "")
    }


    fun startFetchingLocation() {
        easyWayLocation.startLocation()
    }

    fun stopFetchingLocation() {
        easyWayLocation.endUpdates()
    }

    fun locationPermissionResultSuccess(result: Int) {
        easyWayLocation.onActivityResult(result)
    }

    fun requestNextDayPrayersTimes() {
        requestedDayIndex++
        if (requestedDayIndex > 6) {
            _error.postValue("Max Value")
            return
        }

        val date = SimpleDateFormat("dd-mm-yyyy", Locale.ENGLISH).format(
            DateTime.now().plusDays(requestedDayIndex).toDate()
        )



        loadFromApi(
            date,
            lastLat,
            lastLong
        )


    }

    fun requestPreviousDayPrayersTimes() {
        requestedDayIndex--
        if (requestedDayIndex < 0) {
            _error.postValue("Min Value")
            return
        }

        val date = SimpleDateFormat("dd-mm-yyyy", Locale.ENGLISH).format(
            DateTime.now().plusDays(requestedDayIndex).toDate()
        )



        loadFromApi(
            date,
            lastLat,
            lastLong
        )

    }

    fun getQiblaDirections() {
        if (lastLat == 0.0 && lastLong == 0.0) {
            _error.postValue("Location unknown")
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            _progress.postValue(true)

            QiblaDirectionsRepository.getKaabaDirections(lastLat, lastLong)
                .also {

                    _progress.postValue(it.isLoading)
                    it.result?.let { qibla ->
                        _qiblaDirection.postValue(qibla.data)
                    }

                    it.errorMsg?.let { msg ->
                        _error.postValue(msg)
                    }


                }
        }
    }


    private fun cachePrayerTimes(times: PrayersTimes, date: ResponseDate) {
        viewModelScope.launch(Dispatchers.IO) {

            LocalPrayersRepository.insertTimes(
                PrayerTimesLocal(
                    times.Fajr,
                    times.Sunrise,
                    times.Dhuhr,
                    times.Asr,
                    times.Maghrib,
                    times.Isha,
                    date.readable
                )
            )
        }
    }


    private fun loadPrayersFromCache() {
        viewModelScope.launch(Dispatchers.IO) {
            _progress.postValue(true)
            val response = LocalPrayersRepository.loadPrayersTimes()
            response?.let {
                _currentShownTimes.postValue(response.toPrayerTime())
                _progress.postValue(false)

                Log.d(
                    App.APP_TAG,
                    "HomeViewModel - loadPrayersFromCache:  response cache is $it and after Converting is ${it.toPrayerTime()}"
                )
                orignalTimes = response.toPrayerTime()
                refreshingData(
                    orignalTimes,
                    response.date
                )
            }


        }
    }

}