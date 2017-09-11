package ru.gdgkazan.githubmvp.screen.commits;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.gdgkazan.githubmvp.R;
import ru.gdgkazan.githubmvp.content.Commit;


public class CommitHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.commitAuthor)
    TextView tvAuhor;
    @BindView(R.id.commitName)
    TextView tvName;
    @BindView(R.id.commitMessage)
    TextView tvMessage;

    public CommitHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(@NonNull Commit commit) {
        tvName.setText(commit.getRepoName());
        tvAuhor.setText(commit.getAuthor().getAuthorName());
        tvMessage.setText(commit.getMessage());
    }
}
