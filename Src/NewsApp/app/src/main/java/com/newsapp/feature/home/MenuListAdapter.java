package com.newsapp.feature.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.newsapp.R;
import com.newsapp.baseComponent.BaseListAdapter;
import com.newsapp.callback.OnItemClickListener;
import com.newsapp.constants.Country;
import com.newsapp.utils.ViewUtils;

import java.util.ArrayList;

public class MenuListAdapter extends BaseListAdapter<MenuListAdapter.MenuItemHolder> {

    private ArrayList<ViewUtils.Menu> menus;
    private Country selected = Country.INDIA;
    private OnItemClickListener onClickListener;

    MenuListAdapter(ArrayList<ViewUtils.Menu> menus, OnItemClickListener onClickListener) {
        this.menus = menus;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public MenuItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
        return new MenuItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MenuItemHolder holder = (MenuItemHolder) viewHolder;
        holder.title.setText(menus.get(position).getTitle());
        boolean isSelected = selected == menus.get(position).getType();
        holder.title.setBackgroundColor(holder.itemView.getResources().getColor(isSelected ? R.color.colorAccent
                : R.color.colorPrimary));
    }

    @Override
    public int getItemCount() {
        return menus.size();
    }

    void selectCountry(Country country) {
        selected = country;
        notifyDataSetChanged();
    }

    Country getSelected() {
        return selected;
    }

    class MenuItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title;
        MenuItemHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.menu_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onClickListener!=null)
                onClickListener.onItemClick(getAdapterPosition());
        }
    }
}
