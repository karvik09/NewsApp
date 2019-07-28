package com.newsapp.feature.home;

import android.text.TextUtils;
import android.util.TimeUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.newsapp.R;
import com.newsapp.baseComponent.BaseListAdapter;
import com.newsapp.callback.OnItemClickListener;
import com.newsapp.constants.AppConstant;
import com.newsapp.db.entities.Article;
import com.newsapp.utils.TImeUtils;

import java.util.List;

public class ArticleListAdapter extends BaseListAdapter<RecyclerView.ViewHolder> {


    private static final int LOADER_ITEM_TYPE = 843;
    private static final int ARTICLE_ITEM_TYPE = 844;

    private boolean isLoading = false;
    private List<Article> mArticles;
    private OnItemClickListener itemClickListener;

    public ArticleListAdapter(OnItemClickListener onItemClickListener) {
        this.itemClickListener = onItemClickListener;
    }
    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1 && isLoading) {
            return LOADER_ITEM_TYPE;
        } else return ARTICLE_ITEM_TYPE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == LOADER_ITEM_TYPE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loader_item, parent, false);
            return new LoaderItemHolder(view);
        }else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_item, parent, false);
            return new ArticleItemHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ArticleItemHolder) {
            ArticleItemHolder holder = (ArticleItemHolder) viewHolder;
            Article article = mArticles.get(position);
            holder.title.setText(article.getTitle());
            holder.description.setText(article.getDescription());

            String timeText = TImeUtils.getArticleTime(holder.title.getContext(), article.getPublishedAt());
            String author = !TextUtils.isEmpty(article.getAuthor()) ? article.getAuthor() : AppConstant.NOT_AVAILABLE;

            String timeAndAuthor = String.format(holder.itemView.getResources().getString(R.string.time_author),
                    timeText, author);
            holder.time_nAuthor.setText(timeAndAuthor);

            Glide.with(holder.itemView.getContext())
                    .load(article.getUrlToImage())
                    .placeholder(R.drawable.image_placeholder)
                    .centerCrop()
                    .into(holder.articleImage);

        }
    }

    @Override
    public int getItemCount() {

        return articleCount() + (isLoading ? 1 : 0);
    }

    private int articleCount() {
        return mArticles == null ? 0 : mArticles.size();
    }

    public void addLoader(){
        if (!isLoading) {
            isLoading = true;
            notifyItemInserted(getItemCount()-1);
        }
    }

    public void removeLoader() {
        if (isLoading) {
            isLoading = false;
            notifyItemRemoved(getItemCount());
        }
    }

    class ArticleItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView articleImage;
        TextView title,time_nAuthor, description;

        ArticleItemHolder(@NonNull View itemView) {
            super(itemView);
            articleImage = itemView.findViewById(R.id.article_image);
            title = itemView.findViewById(R.id.title);
            time_nAuthor = itemView.findViewById(R.id.time_and_author);
            description = itemView.findViewById(R.id.description);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener!=null)
                itemClickListener.onItemClick(getAdapterPosition());
        }
    }

    class LoaderItemHolder extends RecyclerView.ViewHolder {

        LoaderItemHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void addArticles(List<Article> newList){
        isLoading = false;
        mArticles = newList;
        notifyDataSetChanged();
//        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtilCallback(newList));
//        diffResult.dispatchUpdatesTo(this);
//        mArticles = newList;
    }

    private class DiffUtilCallback extends DiffUtil.Callback {

        private List<Article> newArticles;
        DiffUtilCallback(List<Article> newList) {
            this.newArticles = newList;
        }

        @Override

        public int getOldListSize() {
            return mArticles == null ? 0 : mArticles.size();
        }

        @Override
        public int getNewListSize() {
            return newArticles==null?0:newArticles.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            Article newArticle = newArticles.get(newItemPosition);
            Article oldArticle = mArticles.get(oldItemPosition);
            return TextUtils.equals(newArticle.getTitle(),oldArticle.getTitle());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return true;
        }
    }
}
