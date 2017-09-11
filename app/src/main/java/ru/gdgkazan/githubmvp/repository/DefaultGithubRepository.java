package ru.gdgkazan.githubmvp.repository;

import android.support.annotation.NonNull;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import ru.arturvasilov.rxloader.RxUtils;
import ru.gdgkazan.githubmvp.api.ApiFactory;
import ru.gdgkazan.githubmvp.content.Authorization;
import ru.gdgkazan.githubmvp.content.Commit;
import ru.gdgkazan.githubmvp.content.CommitResponse;
import ru.gdgkazan.githubmvp.content.Repository;
import ru.gdgkazan.githubmvp.utils.AuthorizationUtils;
import ru.gdgkazan.githubmvp.utils.PreferenceUtils;
import rx.Observable;

/**
 * @author Artur Vasilov
 */
public class DefaultGithubRepository implements GithubRepository {

    String user;

    @NonNull
    @Override
    public Observable<List<Repository>> repositories() {
        return ApiFactory.getGithubService()
                .repositories()
                .flatMap(repositories -> {
                    Realm.getDefaultInstance().executeTransaction(realm -> {
                        realm.delete(Repository.class);
                        realm.insert(repositories);
                    });
                    return Observable.just(repositories);
                })
                .onErrorResumeNext(throwable -> {
                    Realm realm = Realm.getDefaultInstance();
                    RealmResults<Repository> repositories = realm.where(Repository.class).findAll();
                    return Observable.just(realm.copyFromRealm(repositories));
                })
                .compose(RxUtils.async());
    }

    @NonNull
    public Observable<Authorization> auth(@NonNull String login, @NonNull String password) {
        String authorizationString = AuthorizationUtils.createAuthorizationString(login, password);
        return ApiFactory.getGithubService()
                .authorize(authorizationString, AuthorizationUtils.createAuthorizationParam())
                .flatMap(authorization -> {
                    PreferenceUtils.saveToken(authorization.getToken());
                    PreferenceUtils.saveUserName(login);
                    ApiFactory.recreate();
                    return Observable.just(authorization);
                })
                .compose(RxUtils.async());
    }

    @Override
    public Observable<List<Commit>> commits(@NonNull String repo) {
        PreferenceUtils.getUserName()
                .flatMap(user -> {
                    int index = user.indexOf("@");
                    return Observable.just(user.substring(0, index));
                })
                .subscribe(user -> this.user = user);

        return ApiFactory.getGithubService()
                .commits(user, repo)
                .flatMap(Observable::from)
                .map(CommitResponse::getCommit)
                .flatMap(commit->{
                    commit.setRepoName(repo);
                    return Observable.just(commit);})
                .toList()
                .flatMap(commits -> {
                    Realm.getDefaultInstance().executeTransaction(realm -> {
                        realm.delete(Commit.class);
                        realm.insert(commits);
                    });
                    return Observable.just(commits);
                })
                .onErrorResumeNext(throwable -> {
                    Realm realm = Realm.getDefaultInstance();
                    RealmResults<Commit> results = realm.where(Commit.class).findAll();
                    return Observable.just(realm.copyFromRealm(results));

                })
                .compose(RxUtils.async());
    }
}
