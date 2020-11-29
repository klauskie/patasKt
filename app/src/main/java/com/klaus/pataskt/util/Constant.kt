package com.klaus.pataskt.util

class Constant {
    companion object {
        const val KEY_HAS_SEEN_LOGIN_SCREEN = "KEY_HAS_SEEN_LOGIN_SCREEN"
        const val KEY_MED_CODE = "KEY_MED_CODE"
        const val KEY_HAS_SEEN_CAMERA_INSTRUCTIONS = "KEY_HAS_SEEN_CAMERA_INSTRUCTIONS"
        const val KEY_SHARED_PREFERENCES = "com.klaus.pataskt.sharedpreferences"

        const val API_PHOTO_POST_URL = "https://patas-app.herokuapp.com/api/predict"
        const val API_LOGIN_URL = "https://patas-app.herokuapp.com/api/paciente/signin"
        const val API_GET_DOCTOR_URL = "https://patas-app.herokuapp.com/api/paciente/doctor"
        const val API_PHOTO_POST_REQUEST_FIELD = "foto"
        const val API_CODE_REQUEST_FIELD = "codigo"
        const val API_CATEGORY_RESPONSE_FIELD = "categoria"
        const val API_DATE_RESPONSE_FIELD = "fecha"

        const val KEY_API_ID = "id"
        const val KEY_API_NAME = "nombre"
        const val KEY_API_PHONE = "telefono"

        const val KEY_USER_ID = "id-user"
        const val KEY_USER_NAME = "nombre-user"
        const val KEY_USER_PHONE = "telefono-user"

        const val KEY_DOCTOR_ID = "id-doctor"
        const val KEY_DOCTOR_NAME = "nombre-doctor"
        const val KEY_DOCTOR_PHONE = "telefono-doctor"
        const val KEY_DOCTOR_EMAIL = "email"
        const val KEY_DOCTOR_HOSPITAL = "hospital"
    }
}