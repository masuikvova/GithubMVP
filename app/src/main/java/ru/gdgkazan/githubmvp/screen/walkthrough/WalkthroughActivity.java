package ru.gdgkazan.githubmvp.screen.walkthrough;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ru.gdgkazan.githubmvp.R;
import ru.gdgkazan.githubmvp.content.Benefit;
import ru.gdgkazan.githubmvp.screen.auth.AuthActivity;
import ru.gdgkazan.githubmvp.utils.PreferenceUtils;
import ru.gdgkazan.githubmvp.widget.PageChangeViewPager;

/**
 * @author Artur Vasilov
 */
public class WalkthroughActivity extends AppCompatActivity implements
        PageChangeViewPager.PagerStateListener, WalkThroughView {


    @BindView(R.id.pager)
    PageChangeViewPager mPager;

    @BindView(R.id.btn_walkthrough)
    Button mActionButton;

    private WalkThroughPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkthrough);
        ButterKnife.bind(this);


        presenter = new WalkThroughPresenter(this);

        mPager.setAdapter(new WalkthroughAdapter(getFragmentManager(), presenter.getBenefits()));
        mPager.setOnPageChangedListener(this);

        mActionButton.setText(R.string.next_uppercase);

        if (PreferenceUtils.isWalkthroughPassed()) {
            startAuthActivity();
        }
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.btn_walkthrough)
    public void onActionButtonClick() {
        presenter.onActionClick();
    }

    @Override
    public void onPageChanged(int selectedPage, boolean fromUser) {
        presenter.onPageChange(selectedPage,fromUser);
    }

    @Override
    public void showBenefit(int index, boolean isLastBenefit) {
        mActionButton.setText(isLastBenefit ? R.string.finish_uppercase : R.string.next_uppercase);
        if (index == mPager.getCurrentItem()) {
            return;
        }
        mPager.smoothScrollNext(getResources().getInteger(android.R.integer.config_mediumAnimTime));
    }

    @Override
    public void startAuthActivity() {
        AuthActivity.start(this);
        finish();
    }
}
