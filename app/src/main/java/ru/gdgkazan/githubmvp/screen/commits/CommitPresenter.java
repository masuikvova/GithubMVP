package ru.gdgkazan.githubmvp.screen.commits;

import ru.arturvasilov.rxloader.LifecycleHandler;
import ru.gdgkazan.githubmvp.R;
import ru.gdgkazan.githubmvp.repository.RepositoryProvider;

public class CommitPresenter {
    private CommitsView view;
    private LifecycleHandler lifecycleHandler;

    public CommitPresenter(CommitsView view, LifecycleHandler lifecycleHandler) {
        this.view = view;
        this.lifecycleHandler = lifecycleHandler;
    }

    public void init(String repo){
        RepositoryProvider.provideGithubRepository()
                .commits(repo)
                .doOnSubscribe(view::showLoading)
                .doAfterTerminate(view::hideLoading)
                .compose(lifecycleHandler.load(R.id.repositories_request))
                .subscribe(commits -> view.showCommits(commits),
                        throwable -> view.showError());
    }
}
