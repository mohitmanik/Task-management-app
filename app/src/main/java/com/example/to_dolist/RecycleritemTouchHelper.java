package com.example.to_dolist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.appcompat.graphics.drawable.DrawableContainerCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.to_dolist.Adapter.TodoAdapter;

public class RecycleritemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private TodoAdapter adapter;

    public RecycleritemTouchHelper(TodoAdapter adapter){
        super(0,ItemTouchHelper.LEFT| ItemTouchHelper.RIGHT);
        this.adapter = adapter;

    }
  @Override

    public boolean onMove(RecyclerView recyclerView,RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target){
      return false;
  }

  @Override
    public void onSwiped(final RecyclerView.ViewHolder viewHolder,int direction){
        final int position = viewHolder.getAdapterPosition();
        if(direction == ItemTouchHelper.LEFT){
            AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());

            builder.setTitle("Delete Task");

            builder.setMessage("Are you sure you wnat to delete this Task ?");

            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.deletItem(position);
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.notifyItemChanged(viewHolder.getAbsoluteAdapterPosition());
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }

        else{
            adapter.editItem(position);
        }
  }

@Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView , RecyclerView.ViewHolder viewHolder ,float dx,float dy ,int actionState,boolean isCurrectlyActive ){
    super.onChildDraw(c,recyclerView,viewHolder,dx,dy,actionState,isCurrectlyActive);

    Drawable icon;

    ColorDrawable background;

     View itemView = viewHolder.itemView;
     int backgroundCornerOffset = 20;

     if(dx>0){
         icon = ContextCompat.getDrawable(adapter.getContext(),R.drawable.baseline_border_edit);
         background = new ColorDrawable((ContextCompat.getColor(adapter.getContext(),R.color.colorPrimaryDark)));


     }else{
         icon = ContextCompat.getDrawable(adapter.getContext(),R.drawable.baseline_auto_delete);
         background = new ColorDrawable(Color.RED);


    }

     int iconMargin = (itemView.getHeight()-icon.getIntrinsicHeight())/2;
     int iconTop = itemView.getTop()+(itemView.getHeight()- icon.getIntrinsicHeight())/2;
     int iconBottom = iconTop+icon.getIntrinsicHeight();

      if(dx>0){
          int iconleft = itemView.getLeft()+iconMargin;
          int iconright = itemView.getLeft()+iconMargin+icon.getIntrinsicHeight();
          icon.setBounds(iconleft,iconTop,iconright,iconBottom);

          background.setBounds(itemView.getLeft(),itemView.getTop(),itemView.getLeft()+((int)dx)+ backgroundCornerOffset,itemView.getBottom());

      }else if(dx<0){



              int iconleft = itemView.getRight()-iconMargin- icon.getIntrinsicWidth();
              int iconright = itemView.getRight()- iconMargin;
              icon.setBounds(iconleft,iconTop,iconright,iconBottom);

              background.setBounds(itemView.getRight()+((int)dx)-backgroundCornerOffset,itemView.getTop(),itemView.getRight(), itemView.getBottom());


      }else{
          background.setBounds(0,0,0,0);


      }

    background.draw(c);
      icon.draw(c);
}

}
