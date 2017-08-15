package com.artifex.mupdfdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.deya.acaide.R;
import com.deya.hospital.bizimp.MainBizImpl;
import com.deya.hospital.util.AbStrUtil;
import com.deya.hospital.util.AbViewUtil;
import com.deya.hospital.util.Constants;
import com.deya.hospital.util.MyHandler;
import com.deya.hospital.util.SharedPreferencesUtil;
import com.deya.hospital.util.ToastUtils;
import com.deya.hospital.util.Tools;
import com.deya.hospital.util.WebUrl;
import com.deya.hospital.vo.CommentVo;
import com.deya.hospital.widget.popu.PopCircleCommement;
import com.deya.hospital.workcircle.WebViewPDComentl2;
import com.deya.hospital.workcircle.knowledge.KnowLegePrivewActivity;
import com.deya.hospital.workcircle.knowledge.KnowledgeInfoSearchActivity;
import com.google.gson.Gson;
import com.im.sdk.dy.common.utils.ToastUtil;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

class ThreadPerTaskExecutor implements Executor {
    public void execute(Runnable r) {
        new Thread(r).start();
    }
}

public class
MuPDFActivity extends KnowledgeInfoSearchActivity implements OnClickListener {
    private String from = "";

    /* The core rendering instance */
    enum TopBarMode {
        Main, Search, Annot, Delete, More, Accept
    }

    ;

    enum AcceptMode {Highlight, Underline, StrikeOut, Ink, CopyText}

    ;

    private final int OUTLINE_REQUEST = 0;
    private final int PRINT_REQUEST = 1;
    private MuPDFCore core;
    private String mFileName;
    private MuPDFReaderView mDocView;
    private View mButtonsView;
    private boolean mButtonsVisible;
    private EditText mPasswordView;
    private TextView mFilenameView;
    private SeekBar mPageSlider;
    private int mPageSliderRes;
    private TextView mPageNumberView;
    private TextView mInfoView;
    private ImageButton mSearchButton;
    private ImageButton mReflowButton;
    private ImageButton mOutlineButton;
    private ImageButton mMoreButton;
    private TextView mAnnotTypeText;
    private ImageButton mAnnotButton;
    private ViewAnimator mTopBarSwitcher;
    private ImageButton mLinkButton;
    private TopBarMode mTopBarMode = TopBarMode.Main;
    private AcceptMode mAcceptMode;
    private ImageButton mSearchBack;
    private ImageButton mSearchFwd;
    private EditText mSearchText;
    private SearchTask mSearchTask;
    private AlertDialog.Builder mAlertBuilder;
    private boolean mLinkHighlight = false;
    private final Handler mHandler = new Handler();
    private boolean mAlertsActive = false;
    private boolean mReflow = false;
    private AsyncTask<Void, Void, MuPDFAlert> mAlertTask;
    private AlertDialog mAlertDialog;
    Button knowledgeBtn;

    public void createAlertWaiter() {
        mAlertsActive = true;
        // All mupdf library calls are performed on asynchronous tasks to avoid stalling
        // the UI. Some calls can lead to javascript-invoked requests to display an
        // alert dialog and collect a reply from the user. The task has to be blocked
        // until the user's reply is received. This method creates an asynchronous task,
        // the purpose of which is to wait of these requests and produce the dialog
        // in response, while leaving the core blocked. When the dialog receives the
        // user's response, it is sent to the core via replyToAlert, unblocking it.
        // Another alert-waiting task is then created to pick up the next alert.
        if (mAlertTask != null) {
            mAlertTask.cancel(true);
            mAlertTask = null;
        }
        if (mAlertDialog != null) {
            mAlertDialog.cancel();
            mAlertDialog = null;
        }
        mAlertTask = new AsyncTask<Void, Void, MuPDFAlert>() {

            @Override
            protected MuPDFAlert doInBackground(Void... arg0) {
                if (!mAlertsActive)
                    return null;

                return core.waitForAlert();
            }

            @Override
            protected void onPostExecute(final MuPDFAlert result) {
                // core.waitForAlert may return null when shutting down
                if (result == null)
                    return;
                final MuPDFAlert.ButtonPressed pressed[] = new MuPDFAlert.ButtonPressed[3];
                for (int i = 0; i < 3; i++)
                    pressed[i] = MuPDFAlert.ButtonPressed.None;
                DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        mAlertDialog = null;
                        if (mAlertsActive) {
                            int index = 0;
                            switch (which) {
                                case AlertDialog.BUTTON1:
                                    index = 0;
                                    break;
                                case AlertDialog.BUTTON2:
                                    index = 1;
                                    break;
                                case AlertDialog.BUTTON3:
                                    index = 2;
                                    break;
                            }
                            result.buttonPressed = pressed[index];
                            // Send the user's response to the core, so that it can
                            // continue processing.
                            core.replyToAlert(result);
                            // Create another alert-waiter to pick up the next alert.
                            createAlertWaiter();
                        }
                    }
                };
                mAlertDialog = mAlertBuilder.create();
                mAlertDialog.setTitle(result.title);
                mAlertDialog.setMessage(result.message);
                switch (result.iconType) {
                    case Error:
                        break;
                    case Warning:
                        break;
                    case Question:
                        break;
                    case Status:
                        break;
                }
                switch (result.buttonGroupType) {
                    case OkCancel:
                        mAlertDialog.setButton(AlertDialog.BUTTON2, getString(R.string.cancel), listener);
                        pressed[1] = MuPDFAlert.ButtonPressed.Cancel;
                    case Ok:
                        mAlertDialog.setButton(AlertDialog.BUTTON1, getString(R.string.okay), listener);
                        pressed[0] = MuPDFAlert.ButtonPressed.Ok;
                        break;
                    case YesNoCancel:
                        mAlertDialog.setButton(AlertDialog.BUTTON3, getString(R.string.cancel), listener);
                        pressed[2] = MuPDFAlert.ButtonPressed.Cancel;
                    case YesNo:
                        mAlertDialog.setButton(AlertDialog.BUTTON1, getString(R.string.yes), listener);
                        pressed[0] = MuPDFAlert.ButtonPressed.Yes;
                        mAlertDialog.setButton(AlertDialog.BUTTON2, getString(R.string.no), listener);
                        pressed[1] = MuPDFAlert.ButtonPressed.No;
                        break;
                }
                mAlertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    public void onCancel(DialogInterface dialog) {
                        mAlertDialog = null;
                        if (mAlertsActive) {
                            result.buttonPressed = MuPDFAlert.ButtonPressed.None;
                            core.replyToAlert(result);
                            createAlertWaiter();
                        }
                    }
                });

                mAlertDialog.show();
            }
        };

        mAlertTask.executeOnExecutor(new ThreadPerTaskExecutor());
    }

    public void destroyAlertWaiter() {
        mAlertsActive = false;
        if (mAlertDialog != null) {
            mAlertDialog.cancel();
            mAlertDialog = null;
        }
        if (mAlertTask != null) {
            mAlertTask.cancel(true);
            mAlertTask = null;
        }
    }

    private MuPDFCore openFile(String path) {
        int lastSlashPos = path.lastIndexOf('/');
        mFileName = new String(lastSlashPos == -1
                ? path
                : path.substring(lastSlashPos + 1));
        System.out.println("Trying to open " + path);
        try {
            core = new MuPDFCore(this, path);
            // New file: drop the old outline data
            OutlineActivityData.set(null);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
        return core;
    }

    private MuPDFCore openBuffer(byte buffer[]) {
        System.out.println("Trying to open byte buffer");
        try {
            core = new MuPDFCore(this, buffer);
            // New file: drop the old outline data
            OutlineActivityData.set(null);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
        return core;
    }

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title = getIntent().getStringExtra("title");

        url = getIntent().getStringExtra("urlweb");
        from = getIntent().getStringExtra("from");
        if (!AbStrUtil.isEmpty(url)) {
            articalId = url.substring(url.indexOf("id=") + 3, url.indexOf("&"));
        }
        if (!AbStrUtil.isEmpty(getIntent().getStringExtra("articleid"))) {
            articalId = getIntent().getStringExtra("articleid");
        }
        if (!AbStrUtil.isEmpty(getIntent().getStringExtra("pdfid"))) {
            url = WebUrl.WEB_ARTICAL + getIntent().getStringExtra("pdfid");
        } else if (getIntent().hasExtra("url")) {
            url = getIntent().getStringExtra("url");
        }
        mAlertBuilder = new AlertDialog.Builder(this);

        if (core == null) {
            core = (MuPDFCore) getLastNonConfigurationInstance();

            if (savedInstanceState != null && savedInstanceState.containsKey("FileName")) {
                mFileName = savedInstanceState.getString("FileName");
            }
        }
        if (core == null) {
            Intent intent = getIntent();
            byte buffer[] = null;
            if (Intent.ACTION_VIEW.equals(intent.getAction())) {
                Uri uri = intent.getData();
                if (uri.toString().startsWith("content://")) {
                    // Handle view requests from the Transformer Prime's file manager
                    // Hopefully other file managers will use this same scheme, if not
                    // using explicit paths.
                    Cursor cursor = getContentResolver().query(uri, new String[]{"_data"}, null, null, null);
                    if (cursor.moveToFirst()) {
                        String str = cursor.getString(0);
                        String reason = null;
                        if (str == null) {
                            try {
                                InputStream is = getContentResolver().openInputStream(uri);
                                int len = is.available();
                                buffer = new byte[len];
                                is.read(buffer, 0, len);
                                is.close();
                            } catch (OutOfMemoryError e) {
                                System.out.println("Out of memory during buffer reading");
                                reason = e.toString();
                            } catch (Exception e) {
                                reason = e.toString();
                            }
                            if (reason != null) {
                                buffer = null;
                                Resources res = getResources();
                                AlertDialog alert = mAlertBuilder.create();
                                setTitle(String.format(res.getString(R.string.cannot_open_document_Reason), reason));
                                alert.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.dismiss),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        });
                                alert.show();
                                return;
                            }
                        } else {
                            uri = Uri.parse(str);
                        }
                    }
                }
                if (buffer != null) {
                    core = openBuffer(buffer);
                } else {
                    core = openFile(Uri.decode(uri.getEncodedPath()));
                }
                SearchTaskResult.set(null);
                if (core != null && core.countPages() == 0)
                    core = null;
            }
            if (core != null && core.needsPassword()) {
                requestPassword(savedInstanceState);
                return;
            }
        }
        if (core == null) {
            AlertDialog alert = mAlertBuilder.create();
            alert.setTitle(R.string.cannot_open_document);
            alert.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.dismiss),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
            alert.show();
            return;
        }

        createUI(savedInstanceState);
    }

    public void requestPassword(final Bundle savedInstanceState) {
        mPasswordView = new EditText(this);
        mPasswordView.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
        mPasswordView.setTransformationMethod(new PasswordTransformationMethod());

        AlertDialog alert = mAlertBuilder.create();
        alert.setTitle(R.string.enter_password);
        alert.setView(mPasswordView);
        alert.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.okay),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (core.authenticatePassword(mPasswordView.getText().toString())) {
                            createUI(savedInstanceState);
                        } else {
                            requestPassword(savedInstanceState);
                        }
                    }
                });
        alert.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel),
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        alert.show();
    }

    public void createUI(Bundle savedInstanceState) {
        if (core == null)
            return;

        // Now create the UI.
        // First create the document view
        mDocView = new MuPDFReaderView(this) {
            @Override
            protected void onMoveToChild(int i) {
                if (core == null)
                    return;
                mPageNumberView.setText(String.format("%d / %d", i + 1,
                        core.countPages()));
                mPageSlider.setMax((core.countPages() - 1) * mPageSliderRes);
                mPageSlider.setProgress(i * mPageSliderRes);
                super.onMoveToChild(i);
            }

            @Override
            protected void onTapMainDocArea() {
                if (!mButtonsVisible) {
                    showButtons();
                } else {
                    if (mTopBarMode == TopBarMode.Main)
                        hideButtons();
                }
            }

            @Override
            protected void onDocMotion() {
                hideButtons();
            }

            @Override
            protected void onHit(Hit item) {
                switch (mTopBarMode) {
                    case Annot:
                        if (item == Hit.Annotation) {
                            showButtons();
                            mTopBarMode = TopBarMode.Delete;
                            mTopBarSwitcher.setDisplayedChild(mTopBarMode.ordinal());
                        }
                        break;
                    case Delete:
                        mTopBarMode = TopBarMode.Annot;
                        mTopBarSwitcher.setDisplayedChild(mTopBarMode.ordinal());
                        // fall through
                    default:
                        // Not in annotation editing mode, but the pageview will
                        // still select and highlight hit annotations, so
                        // deselect just in case.
                        MuPDFView pageView = (MuPDFView) mDocView.getDisplayedView();
                        if (pageView != null)
                            pageView.deselectAnnotation();
                        break;
                }
            }
        };
        mDocView.setAdapter(new MuPDFPageAdapter(this, core));

        mSearchTask = new SearchTask(this, core) {
            @Override
            protected void onTextFound(SearchTaskResult result) {
                SearchTaskResult.set(result);
                // Ask the ReaderView to move to the resulting page
                mDocView.setDisplayedViewIndex(result.pageNumber);
                // Make the ReaderView act on the change to SearchTaskResult
                // via overridden onChildSetup method.
                mDocView.resetupChildren();
            }
        };

        // Make the buttons overlay, and store all its
        // controls in variables
        makeButtonsView();

        // Set up the page slider
        int smax = Math.max(core.countPages() - 1, 1);
        mPageSliderRes = ((10 + smax - 1) / smax) * 2;

        // Set the file-name text
        mFilenameView.setText(mFileName);

        // Activate the seekbar
        mPageSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStopTrackingTouch(SeekBar seekBar) {
                mDocView.setDisplayedViewIndex((seekBar.getProgress() + mPageSliderRes / 2) / mPageSliderRes);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                updatePageNumView((progress + mPageSliderRes / 2) / mPageSliderRes);
            }
        });

        // Activate the search-preparing button
        mSearchButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                searchModeOn();
            }
        });

        // Activate the reflow button
        mReflowButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                toggleReflow();
            }
        });

        if (core.fileFormat().startsWith("PDF")) {
            mAnnotButton.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    mTopBarMode = TopBarMode.Annot;
                    mTopBarSwitcher.setDisplayedChild(mTopBarMode.ordinal());
                }
            });
        } else {
            mAnnotButton.setVisibility(View.GONE);
        }

        // Search invoking buttons are disabled while there is no text specified
        mSearchBack.setEnabled(false);
        mSearchFwd.setEnabled(false);
        mSearchBack.setColorFilter(Color.argb(255, 128, 128, 128));
        mSearchFwd.setColorFilter(Color.argb(255, 128, 128, 128));

        // React to interaction with the text widget
        mSearchText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                boolean haveText = s.toString().length() > 0;
                setButtonEnabled(mSearchBack, haveText);
                setButtonEnabled(mSearchFwd, haveText);

                // Remove any previous search results
                if (SearchTaskResult.get() != null && !mSearchText.getText().toString().equals(SearchTaskResult.get().txt)) {
                    SearchTaskResult.set(null);
                    mDocView.resetupChildren();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        });

        //React to Done button on keyboard
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                    search(1);
                return false;
            }
        });

        mSearchText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER)
                    search(1);
                return false;
            }
        });

        // Activate search invoking buttons
        mSearchBack.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                search(-1);
            }
        });
        mSearchFwd.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                search(1);
            }
        });

        mLinkButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                setLinkHighlight(!mLinkHighlight);
            }
        });

        if (core.hasOutline()) {
            mOutlineButton.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    OutlineItem outline[] = core.getOutline();
                    if (outline != null) {
                        OutlineActivityData.get().items = outline;
                        Intent intent = new Intent(MuPDFActivity.this, OutlineActivity.class);
                        startActivityForResult(intent, OUTLINE_REQUEST);
                    }
                }
            });
        } else {
            mOutlineButton.setVisibility(View.GONE);
        }

        // Reenstate last state if it was recorded
        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        mDocView.setDisplayedViewIndex(prefs.getInt("page" + mFileName, 0));

        if (savedInstanceState == null || !savedInstanceState.getBoolean("ButtonsHidden", false))
            showButtons();

        if (savedInstanceState != null && savedInstanceState.getBoolean("SearchMode", false))
            searchModeOn();

        if (savedInstanceState != null && savedInstanceState.getBoolean("ReflowMode", false))
            reflowModeSet(true);

        // Stick the document view and the buttons overlay into a parent view
        View view = getLayoutInflater().inflate(R.layout.magzine_pdfview, null);
        ll_bottom = (LinearLayout) view.findViewById(R.id.ll_bottom);
        ll_bottom.setVisibility(View.VISIBLE);
        view.findViewById(R.id.shareLay).setOnClickListener(this);
        if (!AbStrUtil.isEmpty(from) && from.equals("tabfragment")) {
            initBottomView(view);
            getArticleInfo();
        }
        knowledgeBtn = (Button) view.findViewById(R.id.knowledge_btn);
        knowledgeBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext, KnowLegePrivewActivity.class);
                intent.putExtra("article_id", getIntent().getStringExtra("article_id"));
                intent.putExtra("article_src", getIntent().getStringExtra("article_src"));
                startActivity(intent);
            }
        });
        RelativeLayout rl_back = (RelativeLayout) view.findViewById(R.id.rl_back);
        rl_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayout layout1 = (LinearLayout) view.findViewById(R.id.layout);
        layout1.addView(mDocView);
        //layout.addView(mButtonsView);
        getKnowledgeInfo(myHandler, getIntent().getStringExtra("article_id"), getIntent().getStringExtra("article_src"));
        setContentView(view);
    }

    private void setKnowLegeInfo(JSONObject jsonObject) {
        if (jsonObject.optString("result_id").equals("0")) {
            if (jsonObject.has("test")) {
                JSONObject job = jsonObject.optJSONObject("test");
                if (null == job) {
                    return;
                }
                if (job.optInt("conTest") > 0) {
                    knowledgeBtn.setVisibility(View.VISIBLE);

                }
            }
        } else {
            ToastUtil.showMessage(jsonObject.optString("result_msg"));
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case OUTLINE_REQUEST:
                if (resultCode >= 0)
                    mDocView.setDisplayedViewIndex(resultCode);
                break;
            case PRINT_REQUEST:
                if (resultCode == RESULT_CANCELED)
                    showInfo(getString(R.string.print_failed));
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public Object onRetainNonConfigurationInstance() {
        MuPDFCore mycore = core;
        core = null;
        return mycore;
    }

    private void showShare(String title, String url) {
        SHARE_MEDIA[] shareMedia = {SHARE_MEDIA.SMS, SHARE_MEDIA.SINA, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN_CIRCLE};
        // initCustomPlatforms(shareMedia);
        // showShareContent(shareMedia, shareTitle, shareContent, targetUrl);
        showShareDialog(getString(R.string.app_name), title, url);
    }

    protected static final int ADD_FAILE = 0x60089;
    protected static final int ADD_SUCESS = 0x60090;

    public void getAddScore(String id) {
        tools = new Tools(mcontext, Constants.AC);
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("aid", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomRequest(myHandler, MuPDFActivity.this, ADD_SUCESS,
                ADD_FAILE, job, "goods/actionGetIntegral");
    }


    private MyHandler myHandler = new MyHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            Activity activity = myHandler.mactivity.get();
            if (null != activity) {
                switch (msg.what) {
                    case ADD_SUCESS:
                        if (null != msg && null != msg.obj) {
                            try {
                                setAddRes(new JSONObject(msg.obj.toString()), activity);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case ADD_FAILE:
                        // ToastUtils.showToast(getActivity(), "");
                        break;
                    case COMMENT_SUCESS:
                        if (null != msg && null != msg.obj) {
                            try {
                                setCommentResult(new JSONObject(msg.obj.toString()));
                                SharedPreferencesUtil.clearCacheById(mcontext, articalId, "commetChache");
                            } catch (JSONException e5) {
                                e5.printStackTrace();
                            }
                        }
                        break;
                    case COMMENT_FAIL:
                        ToastUtils.showToast(MuPDFActivity.this, "亲，您的网络不顺畅哦！");
                        break;
                    case CLICK_LIKE_SUCESS:
                        if (null != msg && null != msg.obj) {
                            try {
                                setClickResult(new JSONObject(msg.obj.toString()));
                            } catch (JSONException e5) {
                                e5.printStackTrace();
                            }
                        }
                        break;
                    case CLICK_LIKE_FAIL:
                        break;
                    case GET_INFO_SUCESS:
                        if (null != msg && null != msg.obj) {
                            try {
                                setInfoResult(new JSONObject(msg.obj.toString()));
                            } catch (JSONException e5) {
                                e5.printStackTrace();
                            }
                        }
                        break;
                    case GET_INFO_FAIL:
                        break;
                    case CLICK_COLLECT_SUCESS:
                        if (null != msg && null != msg.obj) {
                            try {
                                setcollectResult(new JSONObject(msg.obj.toString()));
                            } catch (JSONException e5) {
                                e5.printStackTrace();
                            }
                        }
                        break;
                    case CLICK_COLLECT_FAIL:
                        break;
                    case KNOWLEDGEINFO_SUCESS:
                        if (null != msg && null != msg.obj) {
                            try {
                                dismissdialog();
                                setKnowLegeInfo(new JSONObject(msg.obj.toString()));
                            } catch (JSONException e5) {
                                e5.printStackTrace();
                            }
                        }
                        break;
                    case KNOWLEDGEINFO_FAIL:
                        dismissdialog();
                        ToastUtil.showMessage("亲，您的网咯不顺畅哦！");
                    default:
                        break;
                }
            }
        }
    };

    protected void setAddRes(JSONObject jsonObject, Activity activity) {
        if (jsonObject.optString("result_id").equals("0")) {
            int score = jsonObject.optInt("integral");
            String str = tools.getValue(Constants.INTEGRAL);
            if (null != str) {
                tools.putValue(Constants.INTEGRAL, Integer.parseInt(str) + score + "");
            } else {
                tools.putValue(Constants.INTEGRAL, score + "");
            }
            if (score > 0) {
                if (null != activity) {
                    showTipsDialog(score + "");
                }
            }

        }
    }


    private void reflowModeSet(boolean reflow) {
        mReflow = reflow;
        mDocView.setAdapter(mReflow ? new MuPDFReflowAdapter(this, core) : new MuPDFPageAdapter(this, core));
        mReflowButton.setColorFilter(mReflow ? Color.argb(0xFF, 172, 114, 37) : Color.argb(0xFF, 255, 255, 255));
        setButtonEnabled(mAnnotButton, !reflow);
        setButtonEnabled(mSearchButton, !reflow);
        if (reflow) setLinkHighlight(false);
        setButtonEnabled(mLinkButton, !reflow);
        setButtonEnabled(mMoreButton, !reflow);
        mDocView.refresh(mReflow);
    }

    private void toggleReflow() {
        reflowModeSet(!mReflow);
        showInfo(mReflow ? getString(R.string.entering_reflow_mode) : getString(R.string.leaving_reflow_mode));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (mFileName != null && mDocView != null) {
            outState.putString("FileName", mFileName);

            // Store current page in the prefs against the file name,
            // so that we can pick it up each time the file is loaded
            // Other info is needed only for screen-orientation change,
            // so it can go in the bundle
            SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = prefs.edit();
            edit.putInt("page" + mFileName, mDocView.getDisplayedViewIndex());
            edit.commit();
        }

        if (!mButtonsVisible)
            outState.putBoolean("ButtonsHidden", true);

        if (mTopBarMode == TopBarMode.Search)
            outState.putBoolean("SearchMode", true);

        if (mReflow)
            outState.putBoolean("ReflowMode", true);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mSearchTask != null)
            mSearchTask.stop();

        if (mFileName != null && mDocView != null) {
            SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = prefs.edit();
            edit.putInt("page" + mFileName, mDocView.getDisplayedViewIndex());
            edit.commit();
        }
    }

    public void onDestroy() {
        if (core != null)
            core.onDestroy();
        if (mAlertTask != null) {
            mAlertTask.cancel(true);
            mAlertTask = null;
        }
        core = null;
        super.onDestroy();
    }

    private void setButtonEnabled(ImageButton button, boolean enabled) {
        button.setEnabled(enabled);
        button.setColorFilter(enabled ? Color.argb(255, 255, 255, 255) : Color.argb(255, 128, 128, 128));
    }

    private void setLinkHighlight(boolean highlight) {
        mLinkHighlight = highlight;
        // LINK_COLOR tint
        mLinkButton.setColorFilter(highlight ? Color.argb(0xFF, 172, 114, 37) : Color.argb(0xFF, 255, 255, 255));
        // Inform pages of the change.
        mDocView.setLinksEnabled(highlight);
    }

    private void showButtons() {
        if (core == null)
            return;
        if (!mButtonsVisible) {
            mButtonsVisible = true;
            // Update page number text and slider
            int index = mDocView.getDisplayedViewIndex();
            updatePageNumView(index);
            mPageSlider.setMax((core.countPages() - 1) * mPageSliderRes);
            mPageSlider.setProgress(index * mPageSliderRes);
            if (mTopBarMode == TopBarMode.Search) {
                mSearchText.requestFocus();
                showKeyboard();
            }

            Animation anim = new TranslateAnimation(0, 0, -mTopBarSwitcher.getHeight(), 0);
            anim.setDuration(200);
            anim.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationStart(Animation animation) {
                    mTopBarSwitcher.setVisibility(View.VISIBLE);
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                }
            });
            mTopBarSwitcher.startAnimation(anim);

            anim = new TranslateAnimation(0, 0, mPageSlider.getHeight(), 0);
            anim.setDuration(200);
            anim.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationStart(Animation animation) {
                    mPageSlider.setVisibility(View.VISIBLE);
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    mPageNumberView.setVisibility(View.VISIBLE);
                }
            });
            mPageSlider.startAnimation(anim);
        }
    }

    private void hideButtons() {
        if (mButtonsVisible) {
            mButtonsVisible = false;
            hideKeyboard();

            Animation anim = new TranslateAnimation(0, 0, 0, -mTopBarSwitcher.getHeight());
            anim.setDuration(200);
            anim.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationStart(Animation animation) {
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    mTopBarSwitcher.setVisibility(View.INVISIBLE);
                }
            });
            mTopBarSwitcher.startAnimation(anim);

            anim = new TranslateAnimation(0, 0, 0, mPageSlider.getHeight());
            anim.setDuration(200);
            anim.setAnimationListener(new Animation.AnimationListener() {
                public void onAnimationStart(Animation animation) {
                    mPageNumberView.setVisibility(View.INVISIBLE);
                }

                public void onAnimationRepeat(Animation animation) {
                }

                public void onAnimationEnd(Animation animation) {
                    mPageSlider.setVisibility(View.INVISIBLE);
                }
            });
            mPageSlider.startAnimation(anim);
        }
    }

    private void searchModeOn() {
        if (mTopBarMode != TopBarMode.Search) {
            mTopBarMode = TopBarMode.Search;
            //Focus on EditTextWidget
            mSearchText.requestFocus();
            showKeyboard();
            mTopBarSwitcher.setDisplayedChild(mTopBarMode.ordinal());
        }
    }

    private void searchModeOff() {
        if (mTopBarMode == TopBarMode.Search) {
            mTopBarMode = TopBarMode.Main;
            hideKeyboard();
            mTopBarSwitcher.setDisplayedChild(mTopBarMode.ordinal());
            SearchTaskResult.set(null);
            // Make the ReaderView act on the change to mSearchTaskResult
            // via overridden onChildSetup method.
            mDocView.resetupChildren();
        }
    }

    private void updatePageNumView(int index) {
        if (core == null)
            return;
        mPageNumberView.setText(String.format("%d / %d", index + 1, core.countPages()));
    }

    private void printDoc() {
        if (!core.fileFormat().startsWith("PDF")) {
            showInfo(getString(R.string.format_currently_not_supported));
            return;
        }

        Intent myIntent = getIntent();
        Uri docUri = myIntent != null ? myIntent.getData() : null;

        if (docUri == null) {
            showInfo(getString(R.string.print_failed));
        }

        if (docUri.getScheme() == null)
            docUri = Uri.parse("file://" + docUri.toString());

//		Intent printIntent = new Intent(this, PrintDialogActivity.class);
//		printIntent.setDataAndType(docUri, "aplication/pdf");
//		printIntent.putExtra("title", mFileName);
//		startActivityForResult(printIntent, PRINT_REQUEST);
    }

    private void showInfo(String message) {
        //mInfoView.setText(message);

        int currentApiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentApiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            SafeAnimatorInflater safe = new SafeAnimatorInflater((Activity) this, R.animator.info, (View) mInfoView);
        } else {
            mInfoView.setVisibility(View.VISIBLE);
            mHandler.postDelayed(new Runnable() {
                public void run() {
                    mInfoView.setVisibility(View.INVISIBLE);
                }
            }, 500);
        }
    }

    //顶部视图
    private void makeButtonsView() {
        mButtonsView = getLayoutInflater().inflate(R.layout.buttons, null);
        mButtonsView.setVisibility(View.GONE);
        mFilenameView = (TextView) mButtonsView.findViewById(R.id.docNameText);
        mPageSlider = (SeekBar) mButtonsView.findViewById(R.id.pageSlider);
        mPageNumberView = (TextView) mButtonsView.findViewById(R.id.pageNumber);
        //mInfoView = (TextView)mButtonsView.findViewById(R.id.info);
        mSearchButton = (ImageButton) mButtonsView.findViewById(R.id.searchButton);
        mReflowButton = (ImageButton) mButtonsView.findViewById(R.id.reflowButton);
        mOutlineButton = (ImageButton) mButtonsView.findViewById(R.id.outlineButton);
        mAnnotButton = (ImageButton) mButtonsView.findViewById(R.id.editAnnotButton);
        mAnnotTypeText = (TextView) mButtonsView.findViewById(R.id.annotType);
        mTopBarSwitcher = (ViewAnimator) mButtonsView.findViewById(R.id.switcher);
        mSearchBack = (ImageButton) mButtonsView.findViewById(R.id.searchBack);
        mSearchFwd = (ImageButton) mButtonsView.findViewById(R.id.searchForward);
        mSearchText = (EditText) mButtonsView.findViewById(R.id.searchText);
        mLinkButton = (ImageButton) mButtonsView.findViewById(R.id.linkButton);
        mMoreButton = (ImageButton) mButtonsView.findViewById(R.id.moreButton);
        mTopBarSwitcher.setVisibility(View.INVISIBLE);
        mPageNumberView.setVisibility(View.INVISIBLE);
        //mInfoView.setVisibility(View.INVISIBLE);
        mPageSlider.setVisibility(View.INVISIBLE);
    }

    public void OnMoreButtonClick(View v) {
        mTopBarMode = TopBarMode.More;
        mTopBarSwitcher.setDisplayedChild(mTopBarMode.ordinal());
    }

    public void OnCancelMoreButtonClick(View v) {
        mTopBarMode = TopBarMode.Main;
        mTopBarSwitcher.setDisplayedChild(mTopBarMode.ordinal());
    }

    public void OnPrintButtonClick(View v) {
        printDoc();
    }

    public void OnCopyTextButtonClick(View v) {
        mTopBarMode = TopBarMode.Accept;
        mTopBarSwitcher.setDisplayedChild(mTopBarMode.ordinal());
        mAcceptMode = AcceptMode.CopyText;
        mDocView.setMode(MuPDFReaderView.Mode.Selecting);
        mAnnotTypeText.setText(getString(R.string.copy_text));
        showInfo(getString(R.string.select_text));
    }

    public void OnEditAnnotButtonClick(View v) {
        mTopBarMode = TopBarMode.Annot;
        mTopBarSwitcher.setDisplayedChild(mTopBarMode.ordinal());
    }

    public void OnCancelAnnotButtonClick(View v) {
        mTopBarMode = TopBarMode.More;
        mTopBarSwitcher.setDisplayedChild(mTopBarMode.ordinal());
    }

    public void OnHighlightButtonClick(View v) {
        mTopBarMode = TopBarMode.Accept;
        mTopBarSwitcher.setDisplayedChild(mTopBarMode.ordinal());
        mAcceptMode = AcceptMode.Highlight;
        mDocView.setMode(MuPDFReaderView.Mode.Selecting);
        mAnnotTypeText.setText(R.string.highlight);
        showInfo(getString(R.string.select_text));
    }

    public void OnUnderlineButtonClick(View v) {
        mTopBarMode = TopBarMode.Accept;
        mTopBarSwitcher.setDisplayedChild(mTopBarMode.ordinal());
        mAcceptMode = AcceptMode.Underline;
        mDocView.setMode(MuPDFReaderView.Mode.Selecting);
        mAnnotTypeText.setText(R.string.underline);
        showInfo(getString(R.string.select_text));
    }

    public void OnStrikeOutButtonClick(View v) {
        mTopBarMode = TopBarMode.Accept;
        mTopBarSwitcher.setDisplayedChild(mTopBarMode.ordinal());
        mAcceptMode = AcceptMode.StrikeOut;
        mDocView.setMode(MuPDFReaderView.Mode.Selecting);
        mAnnotTypeText.setText(R.string.strike_out);
        showInfo(getString(R.string.select_text));
    }

    public void OnInkButtonClick(View v) {
        mTopBarMode = TopBarMode.Accept;
        mTopBarSwitcher.setDisplayedChild(mTopBarMode.ordinal());
        mAcceptMode = AcceptMode.Ink;
        mDocView.setMode(MuPDFReaderView.Mode.Drawing);
        mAnnotTypeText.setText(R.string.ink);
        showInfo(getString(R.string.draw_annotation));
    }

    public void OnCancelAcceptButtonClick(View v) {
        MuPDFView pageView = (MuPDFView) mDocView.getDisplayedView();
        if (pageView != null) {
            pageView.deselectText();
            pageView.cancelDraw();
        }
        mDocView.setMode(MuPDFReaderView.Mode.Viewing);
        switch (mAcceptMode) {
            case CopyText:
                mTopBarMode = TopBarMode.More;
                break;
            default:
                mTopBarMode = TopBarMode.Annot;
                break;
        }
        mTopBarSwitcher.setDisplayedChild(mTopBarMode.ordinal());
    }

    public void OnAcceptButtonClick(View v) {
        MuPDFView pageView = (MuPDFView) mDocView.getDisplayedView();
        boolean success = false;
        switch (mAcceptMode) {
            case CopyText:
                if (pageView != null)
                    success = pageView.copySelection();
                mTopBarMode = TopBarMode.More;
                showInfo(success ? getString(R.string.copied_to_clipboard) : getString(R.string.no_text_selected));
                break;

            case Highlight:
                if (pageView != null)
                    success = pageView.markupSelection(Annotation.Type.HIGHLIGHT);
                mTopBarMode = TopBarMode.Annot;
                if (!success)
                    showInfo(getString(R.string.no_text_selected));
                break;

            case Underline:
                if (pageView != null)
                    success = pageView.markupSelection(Annotation.Type.UNDERLINE);
                mTopBarMode = TopBarMode.Annot;
                if (!success)
                    showInfo(getString(R.string.no_text_selected));
                break;

            case StrikeOut:
                if (pageView != null)
                    success = pageView.markupSelection(Annotation.Type.STRIKEOUT);
                mTopBarMode = TopBarMode.Annot;
                if (!success)
                    showInfo(getString(R.string.no_text_selected));
                break;

            case Ink:
                if (pageView != null)
                    success = pageView.saveDraw();
                mTopBarMode = TopBarMode.Annot;
                if (!success)
                    showInfo(getString(R.string.nothing_to_save));
                break;
        }
        mTopBarSwitcher.setDisplayedChild(mTopBarMode.ordinal());
        mDocView.setMode(MuPDFReaderView.Mode.Viewing);
    }

    public void OnCancelSearchButtonClick(View v) {
        searchModeOff();
    }

    public void OnDeleteButtonClick(View v) {
        MuPDFView pageView = (MuPDFView) mDocView.getDisplayedView();
        if (pageView != null)
            pageView.deleteSelectedAnnotation();
        mTopBarMode = TopBarMode.Annot;
        mTopBarSwitcher.setDisplayedChild(mTopBarMode.ordinal());
    }

    public void OnCancelDeleteButtonClick(View v) {
        MuPDFView pageView = (MuPDFView) mDocView.getDisplayedView();
        if (pageView != null)
            pageView.deselectAnnotation();
        mTopBarMode = TopBarMode.Annot;
        mTopBarSwitcher.setDisplayedChild(mTopBarMode.ordinal());
    }

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.showSoftInput(mSearchText, 0);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.hideSoftInputFromWindow(mSearchText.getWindowToken(), 0);
    }

    private void search(int direction) {
        hideKeyboard();
        int displayPage = mDocView.getDisplayedViewIndex();
        SearchTaskResult r = SearchTaskResult.get();
        int searchPage = r != null ? r.pageNumber : -1;
        mSearchTask.go(mSearchText.getText().toString(), direction, displayPage, searchPage);
    }

    @Override
    public boolean onSearchRequested() {
        if (mButtonsVisible && mTopBarMode == TopBarMode.Search) {
            hideButtons();
        } else {
            showButtons();
            searchModeOn();
        }
        return super.onSearchRequested();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mButtonsVisible && mTopBarMode != TopBarMode.Search) {
            hideButtons();
        } else {
            showButtons();
            searchModeOff();
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onStart() {
        if (core != null) {
            core.startAlerts();
            createAlertWaiter();
        }

        super.onStart();
    }

    @Override
    protected void onStop() {
        if (core != null) {
            destroyAlertWaiter();
            core.stopAlerts();
        }

        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if (core.hasChanges()) {
            DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (which == AlertDialog.BUTTON_POSITIVE)
                        core.save();

                    finish();
                }
            };
            AlertDialog alert = mAlertBuilder.create();
            alert.setTitle("MuPDF");
            alert.setMessage(getString(R.string.document_has_changes_save_them_));
            alert.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.yes), listener);
            alert.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.no), listener);
            alert.show();
        } else {
            super.onBackPressed();
        }
    }

    private TextView write_compment;
    private LinearLayout inputLay;
    private LinearLayout editorLay;
    private TextView submiText;
    String articalId = "";

    private String shareTitle = "";
    private String shareContent = "";
    TextView zan_num, commentNumTv;
    ImageView zanImg, shoucangImg;

    List<CommentVo> commetList = new ArrayList<CommentVo>();
    private EditText commentEdt;
    LinearLayout messageLay;
    LinearLayout ll_bottom;
    int headWebHight;
    boolean isFirst = true;

    String title = "";
    String url = "";

    private void initBottomView(View view) {

        view.findViewById(R.id.circlelay).setVisibility(View.VISIBLE);
        editorLay = (LinearLayout) view.findViewById(R.id.editorLay);
        write_compment = (TextView) view.findViewById(R.id.write_compment);
        write_compment.setOnClickListener(this);
        inputLay = (LinearLayout) view.findViewById(R.id.inputLay);
        inputLay.setOnClickListener(this);
        view.findViewById(R.id.framBg).setOnClickListener(this);

        submiText = (TextView) view.findViewById(R.id.submiText);
        submiText.setOnClickListener(this);
        commentEdt = (EditText) view.findViewById(R.id.commentEdt);
        zan_num = (TextView) view.findViewById(R.id.zan_num);
        zan_num.setOnClickListener(this);
        zanImg = (ImageView) view.findViewById(R.id.zanImg);
        zanImg.setOnClickListener(this);
        shoucangImg = (ImageView) view.findViewById(R.id.shoucangImg);
        shoucangImg.setOnClickListener(this);
        commentNumTv = (TextView) view.findViewById(R.id.commentNumTv);
        messageLay = (LinearLayout) view.findViewById(R.id.messageLay);
        messageLay.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.write_compment:
                showCommentEditorPop();
//				editorLay.setVisibility(View.GONE);
//				inputLay.setVisibility(View.VISIBLE);
//				AbViewUtil.openVirtualKeyboard(this,commentEdt);
                break;
            case R.id.inputLay:
                write_compment.setVisibility(View.VISIBLE);
                break;
//			case R.id.submiText:
//				if(commentEdt.getText().toString().trim().length()<=0){
//					ToastUtils.showToast(mcontext, "评论内容不能为空");
//					return;
//				}
//
//				editorLay.setVisibility(View.VISIBLE);
//				inputLay.setVisibility(View.GONE);
//				AbViewUtil.colseVirtualKeyboard(this);
//				doComment();
//				break;
            case R.id.framBg:

                editorLay.setVisibility(View.VISIBLE);
                inputLay.setVisibility(View.GONE);
                AbViewUtil.colseVirtualKeyboard(this);
                break;
            case R.id.zan_num:
                onClickLike();
                break;
            case R.id.zanImg:
                onClickLike();
                break;
            case R.id.shoucangImg:
                onCollection();
                break;
            case R.id.messageLay:
                Intent it = new Intent(mcontext, WebViewPDComentl2.class);
                it.putExtra("url", url);
                it.putExtra("id", articalId);
                startActivity(it);
                break;
            case R.id.shareLay:
                showShare();
                break;

            default:
                break;
        }

    }

    public static final int GET_INFO_SUCESS = 0x600045;
    public static final int GET_INFO_FAIL = 0x600046;
    public static final int COMMENT_SUCESS = 0x600039;
    public static final int COMMENT_FAIL = 0x600040;
    public static final int GET_COMMENTLIST_SUCESS = 0x600041;
    public static final int GET_COMMENTLIST_FAIL = 0x600042;
    public static final int CLICK_LIKE_SUCESS = 0x600043;
    public static final int CLICK_LIKE_FAIL = 0x600044;
    public static final int CLICK_COLLECT_SUCESS = 0x6000437;
    public static final int CLICK_COLLECT_FAIL = 0x600048;
    PopCircleCommement dialog;

    public void showCommentEditorPop() {
        dialog = new PopCircleCommement(mcontext, this, articalId, write_compment,
                new PopCircleCommement.OnPopuClick() {

                    @Override
                    public void enter(String text) {
                        doComment(text);

                    }

                    @Override
                    public void cancel() {
                        // TODO Auto-generated method stub

                    }
                });

    }

    /**
     * 点击收藏
     */
    private void onCollection() {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("article_id", articalId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomRequest(myHandler, this,
                CLICK_COLLECT_SUCESS, CLICK_COLLECT_FAIL, job,
                "workCircle/setCollect");
    }

    /**
     * 获取文章信息
     */
    public void getArticleInfo() {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("article_id", articalId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainBizImpl.getInstance().onComomRequest(myHandler, this,
                GET_INFO_SUCESS, GET_INFO_FAIL, job,
                "workCircle/getArticleInfo");
    }

    /**
     * 评论接口
     */
    public void doComment(String text) {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("article_id", articalId);
            job.put("content", text);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MainBizImpl.getInstance().onComomRequest(myHandler, this,
                COMMENT_SUCESS, COMMENT_FAIL, job, "workCircle/submitComment");
    }

    // 点赞接口
    public void onClickLike() {
        JSONObject job = new JSONObject();
        try {
            job.put("authent", tools.getValue(Constants.AUTHENT));
            job.put("article_id", articalId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MainBizImpl.getInstance()
                .onComomRequest(myHandler, this, CLICK_LIKE_SUCESS,
                        CLICK_LIKE_FAIL, job, "workCircle/clickLike");

    }

    protected void setCommentResult(JSONObject jsonObject) {
        ToastUtils.showToast(mcontext, jsonObject.optString("result_msg"));
        if (jsonObject.has("info")) {
            CommentVo cv = gson.fromJson(jsonObject.optString("info"), CommentVo.class);
            if (null != cv) {
                String numstr = commentNumTv.getText().toString();
                if (AbStrUtil.isEmpty(numstr)) {
                    numstr = "0";
                }
                showTipsDialog(jsonObject.optString("integral"));
                int num = Integer.parseInt(numstr) + 1;
                commentNumTv.setText(num + "");
                Intent it = new Intent(mcontext, WebViewPDComentl2.class);
                it.putExtra("url", url);
                it.putExtra("id", articalId);
                startActivity(it);
            }
        }
        commentEdt.setText("");

    }

    protected void setcollectResult(JSONObject jsonObject) {
        if (jsonObject.optString("result_id").equals("0")) {
            int isCollect = jsonObject.optInt("is_collection");
            shoucangImg
                    .setImageResource(isCollect == 1 ? R.drawable.shoucang_select
                            : R.drawable.shouchang_normal);
        }
        ToastUtils.showToast(mcontext, jsonObject.optString("result_msg"));
    }

    protected void setInfoResult(JSONObject result) {
        if (result.optString("result_id").equals("0")) {
            JSONObject jsonObject = result.optJSONObject("info");
            if (null == jsonObject) {
                return;
            }
            int like;
            if (AbStrUtil.isEmpty(jsonObject.optString("like_count"))) {
                like = 0;
            } else {
                like = jsonObject.optInt("like_count");
            }

            if (AbStrUtil.isEmpty(jsonObject.optString("title"))) {
                shareTitle = "";
            } else {
                shareTitle = jsonObject.optString("title");
            }
            zan_num.setText(like + "");
            int isCollect = jsonObject.optInt("is_collection");
            int isLike = jsonObject.optInt("is_like");
            zanImg.setImageResource(isLike == 1 ? R.drawable.zan_select
                    : R.drawable.zan_normal);
            shoucangImg
                    .setImageResource(isCollect == 1 ? R.drawable.shoucang_select
                            : R.drawable.shouchang_normal);
            int comment_num = jsonObject.optInt("comment_count");
            commentNumTv.setText("" + comment_num);
        }

    }

    Gson gson = new Gson();


    private void setClickResult(JSONObject jsonObject) {
        ToastUtils.showToast(mcontext, jsonObject.optString("result_msg"));
        if (jsonObject.has("is_like")) {
            String zanStr = zan_num.getText().toString();
            if (AbStrUtil.isEmpty(zanStr)) {
                zanStr = "0";
            }
            int num = Integer.parseInt(zanStr);
            if (!AbStrUtil.isEmpty(jsonObject.optString("is_like"))) {
                if (jsonObject.optInt("is_like") == 1) {
                    num = num + 1;
                    zanImg.setImageResource(R.drawable.zan_select);
                } else {
                    num = num - 1;
                    zanImg.setImageResource(R.drawable.zan_normal);
                }
                zan_num.setText(num + "");
            }
        }
    }

    private void showShare() {

        if (AbStrUtil.isEmpty(shareTitle)) {
            shareTitle = "工作圈";
            shareContent = "点击查看文章详情";
        } else {
            shareContent = shareTitle;
        }
        showShareDialog( shareTitle,
                shareContent,
                url);
    }
}
