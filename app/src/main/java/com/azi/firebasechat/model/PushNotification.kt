package com.azi.firebasechat.model

data class PushNotification(
    var data: NotificationData,
    var to: String
)