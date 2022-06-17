package com.lws.permissionx

import android.Manifest
import android.annotation.TargetApi
import android.os.Build


@Suppress("DEPRECATION")
@TargetApi(Build.VERSION_CODES.Q)
val permissionMapOnQ = mapOf(
    Manifest.permission.READ_CALENDAR to Manifest.permission_group.CALENDAR,
    Manifest.permission.WRITE_CALENDAR to Manifest.permission_group.CALENDAR,
    Manifest.permission.READ_CALL_LOG to Manifest.permission_group.CALL_LOG,
    Manifest.permission.WRITE_CALL_LOG to Manifest.permission_group.CALL_LOG,
    Manifest.permission.PROCESS_OUTGOING_CALLS to Manifest.permission_group.CALL_LOG,
    Manifest.permission.CAMERA to Manifest.permission_group.CAMERA,
    Manifest.permission.READ_CONTACTS to Manifest.permission_group.CONTACTS,
    Manifest.permission.WRITE_CONTACTS to Manifest.permission_group.CONTACTS,
    Manifest.permission.GET_ACCOUNTS to Manifest.permission_group.CONTACTS,
    Manifest.permission.ACCESS_FINE_LOCATION to Manifest.permission_group.LOCATION,
    Manifest.permission.ACCESS_COARSE_LOCATION to Manifest.permission_group.LOCATION,
    Manifest.permission.ACCESS_BACKGROUND_LOCATION to Manifest.permission_group.LOCATION,
    Manifest.permission.RECORD_AUDIO to Manifest.permission_group.MICROPHONE,
    Manifest.permission.READ_PHONE_STATE to Manifest.permission_group.PHONE,
    Manifest.permission.READ_PHONE_NUMBERS to Manifest.permission_group.PHONE,
    Manifest.permission.CALL_PHONE to Manifest.permission_group.PHONE,
    Manifest.permission.ANSWER_PHONE_CALLS to Manifest.permission_group.PHONE,
    Manifest.permission.ADD_VOICEMAIL to Manifest.permission_group.PHONE,
    Manifest.permission.USE_SIP to Manifest.permission_group.PHONE,
    Manifest.permission.ACCEPT_HANDOVER to Manifest.permission_group.PHONE,
    Manifest.permission.BODY_SENSORS to Manifest.permission_group.SENSORS,
    Manifest.permission.ACTIVITY_RECOGNITION to Manifest.permission_group.ACTIVITY_RECOGNITION,
    Manifest.permission.SEND_SMS to Manifest.permission_group.SMS,
    Manifest.permission.RECEIVE_SMS to Manifest.permission_group.SMS,
    Manifest.permission.READ_SMS to Manifest.permission_group.SMS,
    Manifest.permission.RECEIVE_WAP_PUSH to Manifest.permission_group.SMS,
    Manifest.permission.RECEIVE_MMS to Manifest.permission_group.SMS,
    Manifest.permission.READ_EXTERNAL_STORAGE to Manifest.permission_group.STORAGE,
    Manifest.permission.WRITE_EXTERNAL_STORAGE to Manifest.permission_group.STORAGE,
    Manifest.permission.ACCESS_MEDIA_LOCATION to Manifest.permission_group.STORAGE,
)


@TargetApi(Build.VERSION_CODES.R)
val permissionMapOnR = permissionMapOnQ


@TargetApi(Build.VERSION_CODES.S)
val permissionMapOnS = mapOf(
    Manifest.permission.BLUETOOTH_SCAN to Manifest.permission_group.NEARBY_DEVICES,
    Manifest.permission.BLUETOOTH_ADVERTISE to Manifest.permission_group.NEARBY_DEVICES,
    Manifest.permission.BLUETOOTH_CONNECT to Manifest.permission_group.NEARBY_DEVICES,
).toMutableMap().apply {
    putAll(permissionMapOnR)
}.toMap()