package app.marees;

import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebPage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String newString;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                newString= null;
            } else {
                newString= extras.getString("STRING_I_NEED");
            }
        } else {
            newString= (String) savedInstanceState.getSerializable("STRING_I_NEED");
        }
        WebView w = new WebView(this);
        w.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                view.loadUrl("javascript:$('.insideheader-show').hide();");
            }
        });
        w.loadUrl(newString);
        setContentView(w);


    }
}
