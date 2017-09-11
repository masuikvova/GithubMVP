package ru.gdgkazan.githubmvp.screen.walkthrough;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import ru.gdgkazan.githubmvp.content.Benefit;
import ru.gdgkazan.githubmvp.utils.PreferenceUtils;


public class WalkThroughPresenter {
    private static final int PAGES_COUNT = 3;
    private WalkThroughView view;
    private int mCurrentItem = 0;

    public WalkThroughPresenter(WalkThroughView view) {
        this.view = view;
    }


    public void onActionClick() {
        if (isLastBenefit()) {
            PreferenceUtils.saveWalkthroughPassed();
            view.startAuthActivity();
        } else {
            mCurrentItem++;
            view.showBenefit(mCurrentItem, isLastBenefit());
        }
    }

    private boolean isLastBenefit() {
        return mCurrentItem == PAGES_COUNT - 1;
    }

    public void onPageChange(int selectedPage, boolean fromUser) {
        if (fromUser) {
            mCurrentItem = selectedPage;
            view.showBenefit(mCurrentItem, isLastBenefit());
        }
    }

    @NonNull
    public List<Benefit> getBenefits() {
        return new ArrayList<Benefit>() {
            {
                add(Benefit.WORK_TOGETHER);
                add(Benefit.CODE_HISTORY);
                add(Benefit.PUBLISH_SOURCE);
            }
        };
    }
}
