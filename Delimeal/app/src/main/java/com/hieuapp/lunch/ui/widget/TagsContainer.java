package com.hieuapp.lunch.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by hieuapp on 23/04/2017.
 */

public class TagsContainer extends ViewGroup {
    private int lineHeight;
    public TagsContainer(Context context) {
        super(context);
    }

    public TagsContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TagsContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        final int width = r -1;
        int xpos = getPaddingLeft();
        int ypos = getPaddingTop();

        for(int i = 0; i < count; i++){
            final View child = getChildAt(i);
            if(child.getVisibility() != GONE){
                final int cwidth = child.getMeasuredWidth();
                final int cheight = child.getMeasuredHeight();
                final LayoutParams lp = child.getLayoutParams();
                if(xpos + cwidth > width){
                    xpos = getPaddingLeft();
                    ypos += lineHeight;
                }

                child.layout(xpos, ypos, xpos + cwidth +32, ypos + cheight +32);
                xpos += cwidth + lp.width;
            }
        }

    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(1, 1); // default of 1px spacing
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
        final int count =  getChildCount();
        int lineHeight = 0;

        int xpos = getPaddingLeft();
        int ypos = getPaddingTop();

        for(int i = 0; i < count; i++){
            final View child = getChildAt(i);
            if(child.getVisibility() != GONE){
                final LayoutParams lp = child.getLayoutParams();
                child.measure(
                        MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST),
                        MeasureSpec.makeMeasureSpec(height, MeasureSpec.UNSPECIFIED));
                final int childWidth = child.getMeasuredWidth();
                lineHeight = Math.max(lineHeight, child.getMeasuredHeight() + lp.height);

                if(xpos + childWidth > width){
                    xpos = getPaddingLeft();
                    ypos += lineHeight;
                }

                xpos += childWidth + lp.width;
            }
        }

        this.lineHeight = lineHeight;

        if(MeasureSpec.getMode(heightMeasureSpec)  == MeasureSpec.UNSPECIFIED){
            height = ypos + lineHeight;
        }else if(MeasureSpec.getMode(heightMeasureSpec)  == MeasureSpec.AT_MOST){
            if(ypos + lineHeight < height){
                height = ypos + lineHeight;
            }
        }

        setMeasuredDimension(width, height);
    }
}
