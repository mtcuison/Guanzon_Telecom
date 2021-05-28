package org.rmj.ggc.samsung_knox.Etc;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

public class ClipBoardCopy {
    private static final String TAG = ClipBoardCopy.class.getSimpleName();

    private Context context;

    public ClipBoardCopy(Context context){
        this.context = context;
    }

    public void CopyTextClip(String Label, String Content){
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(Label, Content);
        clipboard.setPrimaryClip(clip);
    }
}
