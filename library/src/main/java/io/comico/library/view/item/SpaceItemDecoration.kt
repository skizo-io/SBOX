package io.comico.library.view.item

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager


class SpaceItemDecoration(
    var spacingHorizontal: Int = 0,
    var spacingIn: Int = 0,
    var spacingTop: Int = 0,
    var numberOfColumns: Int = 1
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        var spanIndex = 0
        if(numberOfColumns > 1 && view.layoutParams is StaggeredGridLayoutManager.LayoutParams)
            spanIndex = (view.layoutParams as StaggeredGridLayoutManager.LayoutParams).spanIndex

        outRect.left = if(spanIndex == 0) spacingHorizontal else spacingIn / 2
        outRect.right = if(spanIndex == numberOfColumns - 1) spacingHorizontal else spacingIn / 2

        if (position < numberOfColumns) {
            outRect.top = spacingTop
        }

        outRect.bottom = spacingIn
    }
}
