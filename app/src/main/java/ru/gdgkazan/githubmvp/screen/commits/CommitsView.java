package ru.gdgkazan.githubmvp.screen.commits;

import android.support.annotation.NonNull;

import java.util.List;

import ru.gdgkazan.githubmvp.content.Commit;
import ru.gdgkazan.githubmvp.screen.general.LoadingView;


public interface CommitsView extends LoadingView {
    void showCommits(@NonNull List<Commit> commits);

    void showError();
}
