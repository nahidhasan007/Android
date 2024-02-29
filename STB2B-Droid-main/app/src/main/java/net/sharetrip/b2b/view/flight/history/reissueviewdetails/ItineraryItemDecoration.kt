package net.sharetrip.b2b.view.flight.history.reissueviewdetails

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import net.sharetrip.b2b.R

class ItineraryItemDecoration :  RecyclerView.ItemDecoration(){
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val itemPosition = parent.getChildAdapterPosition(view)
        val startEndSpace = parent.context.resources.getDimension(R.dimen.spacing_normal).toInt()
        val zeroSpace = parent.context.resources.getDimension(R.dimen.spacing_zero).toInt()
        if(itemPosition!=0){
            outRect.top = zeroSpace
        }
        if(itemPosition == parent.adapter?.itemCount!!-1) {
            outRect.bottom = startEndSpace
        }
    }
}