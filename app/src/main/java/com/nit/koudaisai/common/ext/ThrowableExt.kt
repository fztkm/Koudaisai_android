package com.nit.koudaisai.common.ext

import com.google.firebase.auth.FirebaseAuthException

fun FirebaseAuthException.toJpString(): String {
    return when (this.errorCode) {
        "ERROR_EMAIL_ALREADY_IN_USE" ->
            "このメールアドレスは既に使用されています.別のメールアドレスを使用してください．"
        "ERROR_USER_NOT_FOUND" ->
            "メールアドレスもしくはパスワードが間違っています．"
        "ERROR_WRONG_PASSWORD" ->
            "メールアドレスもしくはパスワードが間違っています．"
        "ERROR_WRONG_EMAIL" ->
            "正しいメールアドレスを入力してください．"
        else ->
            "エラーが発生しました．${this.message}"
    }
}