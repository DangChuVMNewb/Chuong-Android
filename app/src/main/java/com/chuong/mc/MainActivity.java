package com.chuong.mc;

import android.Manifest;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.content.pm.ActivityInfo;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.activity.ComponentActivity;
import android.content.pm.PackageManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

import com.chuong.mc.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private long exit;
    private ProgressBar progressBar;
    private Server server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        progressBar = binding.progressBar;
        // -----------------------------
        // CẤU HÌNH WEBVIEW
        // -----------------------------
        WebView webView = binding.webview;
        webView.loadUrl("https://google.com");

        @SuppressLint("SetJavaScriptEnabled")
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setJavaScriptCanOpenWindowsAutomatically(true);
        ws.setSupportMultipleWindows(true);
        ws.setDomStorageEnabled(true);
        ws.setDatabaseEnabled(true);
        ws.setCacheMode(WebSettings.LOAD_DEFAULT);
        ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        ws.setMediaPlaybackRequiresUserGesture(false);
        ws.setLoadWithOverviewMode(true);
        ws.setAllowContentAccess(true);
        ws.setAllowFileAccess(true);
        ws.setAllowFileAccessFromFileURLs(true);
        ws.setUseWideViewPort(true);
        ws.setSupportZoom(true);
        ws.setBuiltInZoomControls(true);
        ws.setDisplayZoomControls(false);
        ws.setLoadsImagesAutomatically(true);
        ws.setDefaultTextEncodingName("utf-8");
        binding.webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        binding.webview.setInitialScale(1);

        // Bật User Agent
        ws.setUserAgentString(
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.6998.35/36 Safari/537.36");

        // ✅ Cho phép cookie và cookie bên thứ 3 (rất quan trọng cho Google)
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.setAcceptThirdPartyCookies(webView, true);

        // ✅ Cho phép WebView tự xử lý redirect (OAuth login)
        webView.setWebViewClient(
                new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        view.loadUrl(url);
                        return false;
                    }
                });
        // -----------------------------
        // XỬ LÝ VIDEO TOÀN MÀN HÌNH
        // -----------------------------
        webView.setWebChromeClient(
                new WebChromeClient() {
                    private View mCustomView;
                    private CustomViewCallback mCustomViewCallback;
                    private int mOriginalOrientation;
                    private int mOriginalSystemUiVisibility;

                    @Override
                    public Bitmap getDefaultVideoPoster() {
                        return null;
                    }

                    @Override
                    public void onShowCustomView(View view, CustomViewCallback callback) {
                        if (mCustomView != null) {
                            onHideCustomView();
                            return;
                        }

                        mCustomView = view;
                        mCustomViewCallback = callback;

                        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
                        mOriginalSystemUiVisibility = decor.getSystemUiVisibility();
                        mOriginalOrientation = getRequestedOrientation();

                        decor.addView(
                                mCustomView,
                                new FrameLayout.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        ViewGroup.LayoutParams.MATCH_PARENT));

                        hideSystemUI();
                        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                    }

                    @Override
                    public void onHideCustomView() {
                        if (mCustomView == null) return;

                        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
                        decor.removeView(mCustomView);
                        mCustomView = null;

                        showSystemUI();
                        setRequestedOrientation(mOriginalOrientation);

                        if (mCustomViewCallback != null) {
                            mCustomViewCallback.onCustomViewHidden();
                            mCustomViewCallback = null;
                        }
                    }

                    private void hideSystemUI() {
                        Window window = getWindow();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            window.setDecorFitsSystemWindows(false);
                            WindowInsetsController controller = window.getInsetsController();
                            if (controller != null) {
                                controller.hide(
                                        WindowInsets.Type.statusBars()
                                                | WindowInsets.Type.navigationBars());
                                controller.setSystemBarsBehavior(
                                        WindowInsetsController
                                                .BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
                            }
                        } else {
                            View decorView = window.getDecorView();
                            decorView.setSystemUiVisibility(
                                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                            | View.SYSTEM_UI_FLAG_FULLSCREEN);
                        }
                    }

                    private void showSystemUI() {
                        Window window = getWindow();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            window.setDecorFitsSystemWindows(true);
                            WindowInsetsController controller = window.getInsetsController();
                            if (controller != null) {
                                controller.show(
                                        WindowInsets.Type.statusBars()
                                                | WindowInsets.Type.navigationBars());
                            }
                        } else {
                            View decorView = window.getDecorView();
                            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                        }
                    }

                    @Override
                    public void onProgressChanged(WebView view, int newProgress) {
                        progressBar.setProgress(newProgress);
                        if (newProgress == 100) {
                            progressBar.setVisibility(View.GONE);
                        } else {
                            progressBar.setVisibility(View.VISIBLE);
                        }
                    }
                });
        binding.fabReload.setOnClickListener(
                v -> binding.webview.loadUrl(binding.webview.getUrl()));
        // checkPermissions();
        server = new Server(this);
        server.startServer();
    }

    @Override
    public void onBackPressed() {
        if (binding.webview.canGoBack()) {
            binding.webview.goBack();
        } else {
            if (exit + 2000 > System.currentTimeMillis()) {
                super.onBackPressed();
            } else {
                Toast.makeText(this, getString(R.string.onback), Toast.LENGTH_SHORT).show();
            }
            exit = System.currentTimeMillis();
        }
    }

    //    private void checkPermissions() {
    //      String[] permissions = {
    //            Manifest.permission.READ_MEDIA_AUDIO,
    //          Manifest.permission.READ_MEDIA_VIDEO,
    //          Manifest.permission.READ_MEDIA_IMAGES,
    //          Manifest.permission.POST_NOTIFICATIONS,
    //      };
    //      for (String perm : permissions) {
    //          if (checkSelfPermission(perm) != PackageManager.PERMISSION_GRANTED) {
    //              requestPermissions(permissions, 100);
    //              break;
    //          }
    //     }
    //  }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (server != null) {
            server.stop();
        }
    }
}
