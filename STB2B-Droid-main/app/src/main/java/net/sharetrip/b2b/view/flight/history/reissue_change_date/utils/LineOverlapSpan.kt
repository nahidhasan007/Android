package net.sharetrip.b2b.view.flight.history.reissue_change_date.utils

import android.graphics.Paint.FontMetricsInt
import android.text.style.LineHeightSpan

class LineOverlapSpan : LineHeightSpan {
    override fun chooseHeight(mText: CharSequence, mStart: Int, mEnd: Int,
                              mSpanStarTextView: Int, mLineHeight: Int,
                              mFontMetricsInt: FontMetricsInt) {
        mFontMetricsInt.bottom += mFontMetricsInt.top
        mFontMetricsInt.descent += mFontMetricsInt.top
    }
}
