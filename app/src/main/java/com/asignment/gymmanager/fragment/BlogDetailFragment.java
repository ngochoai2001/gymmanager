package com.asignment.gymmanager.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

import com.asignment.gymmanager.activity.MainActivity;
import com.asignment.gymmanager.R;
import com.asignment.gymmanager.utils.ConstantUtils;
import com.asignment.gymmanager.utils.OnBackPressedListener;


public class BlogDetailFragment extends Fragment implements OnBackPressedListener {

    private String key;
    private WebView webView;

    public static BlogDetailFragment newInstance() {
        BlogDetailFragment fragment = new BlogDetailFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        MainActivity.stateMain = ConstantUtils.FRAGMENT_BLOG;
        ((MainActivity) getActivity()).updateTitle(MainActivity.page, MainActivity.stateMain);
        ((MainActivity) getActivity()).setOnBackPressedListener(this);
        ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_blog_detail, container, false);
        webView =v.findViewById(R.id.wv_blog);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        final AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                alertDialog.setTitle("Error");
                alertDialog.setMessage(description);
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                alertDialog.show();
            }
        });
        webView.loadUrl(key);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = this.getArguments();
        if (b != null) {
            key = b.getString("url");
        }
    }

    @Override
    public void doBack() {
        Fragment fragment = new HomeFragment().newInstance();
        getActivity().getFragmentManager().beginTransaction().replace(R.id.layout_main, fragment).commit();
//        MainActivity.stateMain = ConstantUtils.FRAGMENT_HOME;
//        ((MainActivity) getActivity()).updateTitle(MainActivity.page, MainActivity.stateMain);
    }

}
