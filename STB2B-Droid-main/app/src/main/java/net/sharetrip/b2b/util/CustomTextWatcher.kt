package net.sharetrip.b2b.util

import android.text.Editable
import android.text.TextWatcher

abstract class CustomTextWatcher:TextWatcher {
    override fun beforeTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(p0: Editable?) {}
}