package com.ganjianping.lab.ak.shell

import com.ganjianping.lab.ak.features.httpclient.httpurlconnection.HttpURLConnectionRepository
import com.ganjianping.lab.ak.features.integration.firebase.firebaseModule
import com.ganjianping.lab.ak.features.others.deviceinfo.DeviceInfoRepository
import com.ganjianping.lab.ak.features.security.blockappduringcalls.BlockAppDuringCallsController
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val appModule = module {
    includes(firebaseModule)
    single { DeviceInfoRepository(androidContext()) }
    single { HttpURLConnectionRepository() }
    single { BlockAppDuringCallsController(androidContext()) }
}
