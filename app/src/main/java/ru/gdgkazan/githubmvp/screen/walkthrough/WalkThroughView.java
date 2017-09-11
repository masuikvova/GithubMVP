package ru.gdgkazan.githubmvp.screen.walkthrough;


public interface WalkThroughView {
    void startAuthActivity();

    void showBenefit(int index, boolean isLastBenefit);
}
