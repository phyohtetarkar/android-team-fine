package com.team.androidfine.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.team.androidfine.R;

public class SwipeDeleteCallback extends ItemTouchHelper.SimpleCallback {

    public interface OnSwipeDeleteListener {
        void onSwipeDelete(int position);
    }

    private final Paint paint = new Paint();
    private Bitmap icon;
    private Context context;
    private OnSwipeDeleteListener onSwipeDeleteListener;

    public void setOnSwipeDeleteListener(OnSwipeDeleteListener onSwipeDeleteListener) {
        this.onSwipeDeleteListener = onSwipeDeleteListener;
    }

    public SwipeDeleteCallback(Context context) {
        super(0, ItemTouchHelper.LEFT);
        this.context = context;
        paint.setColor(Color.parseColor("#f13a37"));
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if (onSwipeDeleteListener != null && viewHolder.itemView.isEnabled()) {
            onSwipeDeleteListener.onSwipeDelete(viewHolder.getAdapterPosition());
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View view = viewHolder.itemView;

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && view.isEnabled()) {
            float width = view.getHeight() / 3;
            float tX = dX;

            RectF background = new RectF(view.getRight() + tX, view.getTop() + 10, view.getRight(), view.getBottom() - 10);
            c.drawRoundRect(background, 15, 15, paint);

            RectF iconDest = new RectF(view.getRight() - 2 * width, view.getTop() + width,
                    view.getRight() - width, view.getBottom() - width);
            c.drawBitmap(getIcon(), null, iconDest, paint);

            super.onChildDraw(c, recyclerView, viewHolder, tX, dY, actionState, isCurrentlyActive);
        }
    }

    private Bitmap getIcon() {
        if (icon == null) {
            Drawable drawable;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                drawable = context.getResources().getDrawable(R.drawable.ic_delete_white, null);
            } else {
                drawable = context.getResources().getDrawable(R.drawable.ic_delete_white);
            }

            icon = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(icon);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        }
        return icon;
    }
}
