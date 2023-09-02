package by.bashlikovvv.hotelreservation.presentation.contract

import android.text.Editable
import android.text.TextWatcher

class PhoneTextViewListener(
    val onTextChanged: (before: String, old: String, new: String, after: String) -> Unit
) : TextWatcher {

    private var _ignore = false

    private var _after = ""

    private var _before = ""

    private var _new = ""

    private var _old = ""

    companion object {
        const val INITIAL_VALUE = "+7 (***) ***-**-**"
        const val ASTERISK = '*'
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        if (s == null) return
        _before = s.subSequence(0, start).toString()
        _old = s.subSequence(start, start+count).toString()
        _after = s.subSequence(start+count, s.length).toString()
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (s == null) return
        _new = s.subSequence(start, start+count).toString()
    }

    override fun afterTextChanged(s: Editable?) {
        if (s.isNullOrBlank()) {
            return
        }
        if (_ignore) return
        _ignore = true
        onTextChanged(_before, _old, _new, _after)
        _ignore = false
    }

}