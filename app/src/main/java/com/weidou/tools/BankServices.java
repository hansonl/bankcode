package com.weidou.tools;

import android.accessibilityservice.AccessibilityService;
import android.graphics.Rect;
import android.os.Looper;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import javax.crypto.AEADBadTagException;

public class BankServices extends AccessibilityService {

    private static android.os.Handler handler = new android.os.Handler(Looper.getMainLooper());

    private static final int CANT_FIND_ITEM = 1;
    private static final int FIRST_ITEM_CLICKED = 0;
    private String BANK_SERVICE_TAG = "BANK_SERVICE_TAG";
    private String BANK_PACKAGE_NAME = "cn.com.cmbc.newmbank";
    private String BANK_RESOURCE_ID_HEAD = "cn.com.cmbc.newmbank:id/";
    private String lastWindowContentChangeClassName;
    private String lastSecondWindowContentChangeClassName;
    private static int tabcount = -1;
    private static StringBuilder sb;
    private boolean isFirstClickConfirm = true;//避免交易确认对话框中多次点击确定位置
    private boolean isFirstProductClick = true;//避免从商品详情返回到列表中多次点击商品详情页中确定按钮
    private boolean isLogin = false;
    private String baifenbi;
    private boolean isJumpYHLCPage = false;
    private boolean isFirstTimeGoList = false;
    private AccessibilityNodeInfo child2;
    private Double rate;

    @Override
    public void onCreate() {
        super.onCreate();
        baifenbi = FileUtils.getFileFromSdcard("baifenbi");
        rate = Double.valueOf(baifenbi.substring(0, baifenbi.length() - 1));
    }


    //小米6
//    @Override
//    public void onAccessibilityEvent(AccessibilityEvent event) {
//        int eventType = event.getEventType();
//        String eventText = "";
//        Log.i(BANK_SERVICE_TAG, "==============Start====================");
////        FileUtils.saveContentToSdcard("queue_status", "1");
//        Log.i("BANK_SERVICE_TAG", eventType + "");
//        Log.i(BANK_SERVICE_TAG, "get_class_name——————" + event.getClassName().toString());
//        //1.点击首页银行理财进入银行理财页面   shell直接输入坐标点击
//        //2.银行理财界面判断登录状态          根据登录的文案
//        //  a.未登录状态 点击进入登录页面
//        //  b.用户登录页面输入用户密码回到银行理财页面
//        //3.登录状态下银行理财页面点击理财转让页面
//        //4.理财转账界面下点击筛选按钮刷新列表
//        //5.当前列表中第一个条目是否符合条件
//        //  a.符合条件进入第一个条目
//        //  b.不符合条件重复步骤4
//        //6.进入第一个商品详情页
//        //7.商品详情页中向下拉勾选同意并确认
//        //8.弹出确认对话框 点击确认
//        //9.进入输入密码界面输入密码
//        //10.输入正确密码走完一个流程将用来判断重复点击的标志重置
//        switch (eventType) {
//            case AccessibilityEvent.TYPE_VIEW_CLICKED:
//                eventText = "TYPE_VIEW_CLICKED";
//                break;
//            case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:
//                eventText = "TYPE_VIEW_LONG_CLICKED";
//                break;
//            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
//                eventText = "TYPE_NOTIFICATION_STATE_CHANGED";
//                break;
//            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
//                eventText = "TYPE_WINDOW_STATE_CHANGED";
//                if ((BANK_PACKAGE_NAME + ".activity.MainActivity").equals(event.getClassName().toString())) {// 1-进入首页
//                    // 1-点击首页的银行理财
//                    execShellCmd("input tap 500 700");
//                } else if ((BANK_PACKAGE_NAME + ".activity.WebViewActivity").equals(event.getClassName().toString())) {//2-银行理财界面 登录或直接进入理财转账
//                    //2a-没有登录就登录
//
//
//                    //3-已经登录的情况直接进入理财转账界面
//                    if (isNotFind(getRootInActiveWindow(), "理财转让")) {//3-判断页面title如果没有发现理财转让进入理财转让界面
//                        //3-直接进入理财转让(产品列表页)
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                execShellCmd("input tap 500 1300");//3-点击中间的理财转让图标
//                            }
//                        }, 600);
//                    }
//                } else if ((BANK_PACKAGE_NAME + ".activity.login.tradlogin.LoginActivity").equals(event.getClassName().toString())) {//登录界面
//
//                } else if ("cn.com.cmbc.safe.activity.PwdVerifyActivity".equals(event.getClassName().toString())) {//9-输入密码界面
//                    //10-重置
//                    isFirstClickConfirm = true;
//                    isFirstProductClick = true;
////                    坐标
////                    1(177,1258)
////                    2(534,1258)
////                    3(900,1258)
////                    4(177,1436)
////                    5(534,1436)
////                    6(900,1436)
////                    7(177,1623)
////                    8(534,1623)
////                    9(900,1623)
////                    0(534,1806)
//
//                    clickNum(0, 0);
//                    clickNum(1, 100);
//                    clickNum(2, 100);
//                    clickNum(3, 100);
//                    clickNum(4, 100);
//
//                }
//
//
//                break;
//            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
//                eventText = "TYPE_WINDOW_CONTENT_CHANGED";
//                //已经处在WebViewActivity中 在这种情况下判断
//                if (("android.webkit.WebView").equals(event.getClassName().toString())) {//3-处于理财转账页面并且页面刷新情况（从筛选页进来，首次进来）
////                    if (!isNotFind(getRootInActiveWindow(), "筛选")) {//4-进入搜索界面执行搜索流程
////                        execShellCmd("input tap 700 1800");
////                    }
//                } else if (("android.view.View").equals(lastWindowContentChangeClassName) && ("android.view.View").equals(lastSecondWindowContentChangeClassName)
//                        && ("android.view.View").equals(event.getClassName().toString())
//                        && isFirstProductClick) {//5a-商品列表加载数据完成
//                    clickFirstProduct(getRootInActiveWindow(), "6%");//5a-进入了商品详情
//                    Log.i(BANK_SERVICE_TAG, "进入了view情况" + lastWindowContentChangeClassName + "----当前classname----" + event.getClassName().toString());
////                    printPacketInfo(getRootInActiveWindow());
//                } else if (("android.widget.Button").equals(event.getClassName().toString())) {//8-找到对话框点击确认
//                    clickDialogConfirm(getRootInActiveWindow());
//                }
//                lastSecondWindowContentChangeClassName = lastWindowContentChangeClassName;
//                lastWindowContentChangeClassName = event.getClassName().toString();
//
//                break;
//            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
//                eventText = "TYPE_VIEW_SCROLLED";
//                if (("android.webkit.WebView").equals(event.getClassName().toString())
//                        && isFirstProductClick) {//7-商品详情页向下滚动完成
//
//                    isFirstProductClick = false;
//                    perforGlobalClick(500, 1100);//7-勾选同意
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            perforGlobalClick(500, 1800);//7-点击确定
//                        }
//                    }, 500);
//                }
//
//
//                break;
//        }
//        Log.i(BANK_SERVICE_TAG, eventText);
//    }


    //小米8
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        String eventText = "";
        Log.i(BANK_SERVICE_TAG, "==============Start====================");
//        FileUtils.saveContentToSdcard("queue_status", "1");
        Log.i("BANK_SERVICE_TAG", eventType + "");
        String classeName = event.getClassName().toString();
        Log.i(BANK_SERVICE_TAG, "get_class_name——————" + classeName);
        //1.点击首页银行理财进入银行理财页面   shell直接输入坐标点击
        //2.银行理财界面判断登录状态          根据登录的文案
        //  a.未登录状态 点击进入登录页面
        //  b.用户登录页面输入用户密码回到银行理财页面
        //3.登录状态下银行理财页面点击理财转让页面
        //4.理财转账界面下点击筛选按钮刷新列表
        //5.当前列表中第一个条目是否符合条件
        //  a.符合条件进入第一个条目
        //  b.不符合条件重复步骤4
        //6.进入第一个商品详情页
        //7.商品详情页中向下拉勾选同意并确认
        //8.弹出确认对话框 点击确认
        //9.进入输入密码界面输入密码
        //10.输入正确密码走完一个流程将用来判断重复点击的标志重置
        switch (eventType) {
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                eventText = "TYPE_VIEW_CLICKED";
                break;
            case AccessibilityEvent.TYPE_VIEW_LONG_CLICKED:
                eventText = "TYPE_VIEW_LONG_CLICKED";
                break;
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                eventText = "TYPE_NOTIFICATION_STATE_CHANGED";
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                eventText = "TYPE_WINDOW_STATE_CHANGED";
                if ("cn.com.cmbc.safe.activity.PwdVerifyActivity".equals(classeName)) {//9-输入密码界面
                    //10-重置
                    isFirstClickConfirm = true;
                    isFirstProductClick = true;
//                    坐标小米6
//                    1(177,1258)
//                    2(534,1258)
//                    3(900,1258)
//                    4(177,1436)
//                    5(534,1436)
//                    6(900,1436)
//                    7(177,1623)
//                    8(534,1623)
//                    9(900,1623)
//                    0(534,1806)

                    //小米8
                    //4（177,1641）
                    perforGlobalClick(177, 1641);
                    postDelayClick(177, 1641, 50);
                    postDelayClick(177, 1641, 100);
                    postDelayClick(177, 1641, 150);
                    postDelayClick(177, 1641, 200);
                    postDelayClick(177, 1641, 250);
                    postDelayClick(177, 1641, 300);
                    postDelayClick(177, 1641, 350);
                    postDelayClick(177, 1641, 400);


                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            restartMinsheng();
                        }
                    }, 4000);
                } else if ((BANK_PACKAGE_NAME + ".activity.WelComeActivity").equals(classeName)) {

                    perforGlobalClick(900, 250);//跳过欢迎页面
                } else if ((BANK_PACKAGE_NAME + ".activity.MainActivity").equals(classeName)) {// 1-进入首页
                    //2a-进入首页直接登陆
                    if (isLogin) {
                        perforGlobalClick(500, 700);//进入银行理财界面
                        isJumpYHLCPage = false;
                    } else
                        perforGlobalClick(100, 150);
                } else if ((BANK_PACKAGE_NAME + ".activity.WebViewActivity").equals(classeName)) {//2-银行理财界面 登录或直接进入理财转账
                    //2a-没有登录就登录


                    //3-已经登录的情况直接进入理财转账界面
                    if (isNotFind(getRootInActiveWindow(), "理财转让")) {//3-判断页面title如果没有发现理财转让进入理财转让界面
                        if (!isNotFind(getRootInActiveWindow(), "我的账户")) {
                            perforGlobalClick(90, 180);
                        } else {
                            //3-直接进入理财转让(产品列表页)
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    execShellCmd("input tap 500 1300");//3-点击中间的理财转让图标
                                    isJumpYHLCPage = true;
                                }
                            }, 2500);
                        }
                    }
                } else if ((BANK_PACKAGE_NAME + ".activity.login.tradlogin.LoginActivity").equals(classeName)) {//登录界面
                    //小米8
                    // w(149,1465)
                    // c(414,1900)
                    // 123(92,2016)
                    // 1(177,1451)
                    // 2(534,1451)
                    // 3(900,1451)
                    // 4(177,1641)
                    // 5(534,1641)
                    // 6(900,1641)
                    // 7(177,1844)
                    // 8(534,1844)
                    // 9(900,1844)
                    // 0(534,2045)
                    // abc(177,2045)


                    // 密码wc123456
                    perforGlobalClick(300, 520);//2a-点击登录的输入密码框
                    postDelayClick(149, 1465, 600);
                    postDelayClick(414, 1900, 900);
                    postDelayClick(92, 2016, 1300);
                    postDelayClick(177, 1451, 1900);
                    postDelayClick(534, 1451, 1900 + 300);
                    postDelayClick(900, 1451, 1900 + 600);
                    postDelayClick(177, 1641, 1900 + 900);
                    postDelayClick(545, 1641, 1900 + 1200);
                    postDelayClick(900, 1641, 1900 + 1500);
                    postDelayClick(500, 900, 1900 + 2100);//2a-点击登录
                    isLogin = true;

                }


                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                eventText = "TYPE_WINDOW_CONTENT_CHANGED";

                if ((BANK_PACKAGE_NAME + ".activity.MainActivity").equals(classeName)) {//从登陆界面出来
                    // 1-点击首页的银行理财
                    execShellCmd("input tap 500 700");
                } else if (
                        ("android.view.View").equals(event.getClassName().toString())
                                || ("android.widget.FrameLayout").equals(event.getClassName().toString())
                                && isFirstProductClick) {
//                    Log.i(BANK_SERVICE_TAG, "进入了view情况" + lastWindowContentChangeClassName + "----当前classname----" + classeName);
                    clickFirstProduct(getRootInActiveWindow());//5a-进入了商品详情

                } else if (("android.app.Dialog").equals(classeName)) {//8-找到对话框点击确认
                    clickDialogConfirm(getRootInActiveWindow());
                }
                lastSecondWindowContentChangeClassName = lastWindowContentChangeClassName;
                lastWindowContentChangeClassName = classeName;

                break;
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                eventText = "TYPE_VIEW_SCROLLED";
                if (("android.webkit.WebView").equals(classeName)
                        && isFirstProductClick) {//7-商品详情页向下滚动完成

                    isFirstProductClick = false;
                    //小米6
//                    perforGlobalClick(500, 1100);//7-勾选同意
                    //小米8
//                    postDelayClick(500, 1300, 300);
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
////                            perforGlobalClick(500, 1800);//7-点击确定 小米6
//                            perforGlobalClick(500, 2000);//7-点击确定 小米8
//                            isFirstProductClick = true;
//                        }
//                    }, 500);
                }


                break;
        }
//        Log.i(BANK_SERVICE_TAG, eventText);
    }

    private void restartMinsheng() {
        execShellCmd("am force-stop cn.com.cmbc.newmbank");
        perforGlobalClick(450, 200);
        isLogin = false;
        isFirstTimeGoList = false;
    }

    @Override
    public void onInterrupt() {


    }

    public void postDelayClick(final int x, final int y, int time) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                perforGlobalClick(x, y);
            }
        }, time);
    }


    //数字按键 小米8
    public void clickNum(final int i, int time) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (i) {
                    case 0:
                        perforGlobalClick(534, 1806);
                        break;
                    case 1:
                        perforGlobalClick(177, 1258);
                        break;
                    case 2:
                        perforGlobalClick(534, 1258);
                        break;
                    case 3:
                        perforGlobalClick(900, 1258);
                        break;
                    case 4:
                        perforGlobalClick(177, 1436);
                        break;
                    case 5:
                        perforGlobalClick(534, 1436);
                        break;
                    case 6:
                        perforGlobalClick(900, 1436);
                        break;
                    case 7:
                        perforGlobalClick(177, 1623);
                        break;
                    case 8:
                        perforGlobalClick(534, 1623);
                        break;
                    case 9:
                        perforGlobalClick(900, 1623);
                        break;
                }
            }
        }, time);
    }

    public void printPacketInfo(AccessibilityNodeInfo root) {
        sb = new StringBuilder();
        tabcount = 0;
        int[] is = {};
        analysisPacketInfo(root, is);
//        Log.i(BANK_SERVICE_TAG, sb.toString());

    }


    //打印此时的界面状况,便于分析
    private static void analysisPacketInfo(AccessibilityNodeInfo info, int... ints) {
        if (info == null) {
            return;
        }
        if (tabcount > 0) {
            for (int i = 0; i < tabcount; i++) {
                sb.append("\t\t");
            }
        }
        if (ints != null && ints.length > 0) {
            StringBuilder s = new StringBuilder();
            for (int j = 0; j < ints.length; j++) {
                s.append(ints[j]).append(".");
            }
            sb.append(s).append(" ");
        }
        String name = info.getClassName().toString();
        String[] split = name.split("\\.");
        name = split[split.length - 1];
        if ("TextView".equals(name)) {
            CharSequence text = info.getText();
            sb.append("text:").append(text);
        } else if ("Button".equals(name)) {
            CharSequence text = info.getText();
            sb.append("Button:").append(text);
        } else {
            sb.append(name);
        }
        sb.append("\n");

        int count = info.getChildCount();
        if (count > 0) {
            tabcount++;
            int len = ints.length + 1;
            int[] newInts = Arrays.copyOf(ints, len);

            for (int i = 0; i < count; i++) {
                newInts[len - 1] = i;
                analysisPacketInfo(info.getChild(i), newInts);
            }
            tabcount--;
        }
    }


    /**
     * 交易确认对话框中点击确定
     */

    public void clickDialogConfirm(AccessibilityNodeInfo node) {

//        Log.i(BANK_SERVICE_TAG, "进入clickDialogConfirm");

        try {
            if ("android.webkit.WebView".equals(node.getClassName().toString())) {
                AccessibilityNodeInfo child;
                //小米6
//                try {
//                    child = node
//                            .getChild(0)
//                            .getChild(0)
//                            .getChild(2)
//                            .getChild(0)
//                            .getChild(1)
//                            .getChild(0)
//                            .getChild(0)
//                            .getChild(2)
//                            .getChild(2);
//                } catch (Exception e) {
//                    child = node
//                            .getChild(0)
//                            .getChild(0)
//                            .getChild(3)
//                            .getChild(0)
//                            .getChild(1)
//                            .getChild(0)
//                            .getChild(0)
//                            .getChild(2)
//                            .getChild(2);
//                }
                //小米8
                child = node
                        .getChild(0)
                        .getChild(1)
                        .getChild(0)
                        .getChild(0)
                        .getChild(0)
                        .getChild(2)
                        .getChild(1);

                if (child.getText() != null && child.getText().toString().equals("确定")) {
                    if (isFirstClickConfirm) {
//                        perforGlobalClick(700, 1400);//小米6
                        perforGlobalClick(700, 1600);//小米8
                        isFirstClickConfirm = false;
                    }
                }
            } else {
                for (int i = 0; i < node.getChildCount(); i++) {
                    if (node.getChild(i) != null) {
                        clickDialogConfirm(node.getChild(i));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
                        perforGlobalClick(700, 1600);//小米8
//            Log.i(BANK_SERVICE_TAG, "clickDialogConfirmt进入try-catch");
        }
    }


    /**
     * 当第一个条目的收益率大于或等于rate时 点击进入第一个条目
     * 默认4.24%
     */

    public void clickFirstProduct(AccessibilityNodeInfo node) {
        try {
//            if ("android.webkit.WebView".equals(node.getClassName().toString())) {
                AccessibilityNodeInfo child;


                //小米8
            child2 = node
                    .getChild(0)
                    .getChild(0)
                    .getChild(0)
                    .getChild(0)
                    .getChild(0)
                    .getChild(1)
                    .getChild(0);
                AccessibilityNodeInfo child1 = child2
                        .getChild(0)
                        .getChild(0)
                        .getChild(0)
                        .getChild(1);
                child = child1
                        .getChild(0)
                        .getChild(0)
                        .getChild(0)
                        .getChild(0)
                        .getChild(1)
                        .getChild(0);
//                Toast.makeText(getApplicationContext(),"找到相应view",Toast.LENGTH_LONG).show();


                CharSequence view = child.getText();
                if (view != null && view.toString().contains("%")) {
//                    Log.i(BANK_SERVICE_TAG, "clickFirstProduct----childtext-----" + view.toString() + "rate------" + rate);
                    String getRate = view.toString();
                    getRate = getRate.substring(0, getRate.length() - 1);
                    isFirstTimeGoList = true;
                    if (aMoreThanb(Double.valueOf(getRate), rate)) {//5a-收益率大于设定值符合条件点击第一个条目
//                        Toast.makeText(getApplicationContext(),"进入了判断比例界面",Toast.LENGTH_LONG).show();

//                        Log.i(BANK_SERVICE_TAG, "clickFirstProduct----" + rate + "-----大于页面第一个and-clicked");
                        perforGlobalClick(500, 600);//5a-点击第一个条目
                        handler.postDelayed(new Runnable() {//7-下拉操作之后勾选同意点击确认
                            @Override
                            public void run() {
                                perforGlobalSwipe(500, 1800, 500, 0, 100);//7-下拉操作
                            }
                        }, 1000);
                        //7-商品详情页向下滚动完成
                        postDelayClick(500, 1300, 1300);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
//                            perforGlobalClick(500, 1800);//7-点击确定 小米6
                                perforGlobalClick(500, 2000);//7-点击确定 小米8
                                isFirstProductClick = true;
                            }
                        }, 1600);
                    } else {//5b-返回标志重新筛选
//                        Log.i(BANK_SERVICE_TAG, "clickFirstProduct----" + rate + "-----小于页面第一个");
                        perforGlobalClick(1000, 160);//5b-点击筛选界面
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                perforGlobalClick(700, 2100);//4-筛选界面点击确定
                            }
                        }, 200);

                    }
                }
//            }
//            else {
//                for (int i = 0; i < node.getChildCount(); i++) {
//                    if (node.getChild(i) != null) {
//                        clickFirstProduct(node.getChild(i), rate);
//                    }
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
//            Log.i(BANK_SERVICE_TAG, "clickFirstProduct进入try-catch");
//            Toast.makeText(getApplicationContext(),"进入第一个trycatch",Toast.LENGTH_LONG).show();

            try {
                String s = child2
                        .getChild(0)
                        .getChild(1)
                        .getChild(0)
                        .getChild(0).getText().toString();
                if (s.contains("com.csii.pe.core")) {
                    restartMinsheng();
//                    Toast.makeText(getApplicationContext(),"进入了判读黑框情况",Toast.LENGTH_LONG).show();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
                try {
                    String s = child2
                            .getChild(0)
                            .getChild(0)
                            .getChild(0)
                            .getChild(1)
                            .getChild(0).getClassName().toString();
                    if ("android.widget.Image".equals(s)
                            && isFirstTimeGoList) {
                        restartMinsheng();
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
//                Toast.makeText(getApplicationContext(),"进入了第二个trycatch",Toast.LENGTH_LONG).show();

            }
        }
    }


    /**
     * 递归查找当前窗口中的字符串
     *
     * @param node
     */
    public boolean findText(AccessibilityNodeInfo node, String s) {

        int windowId = node.getWindowId();
        String nodeText = node.getText() == null ? "null" : node.getText().toString();
        String nodeContentDescription = node.getContentDescription() == null ? "null" : node.getContentDescription().toString();
//        Log.i(BANK_SERVICE_TAG, "findText" + windowId + "------Node-text：" + nodeText + "------Node-contentDescription：" + nodeContentDescription);
        if (node.getChildCount() == 0) {
            if (node.getText() != null && s.equals(node.getText().toString())) {
//                Log.i(BANK_SERVICE_TAG, "findText------" + s + "------true");
                return true;
            }
        } else {
            for (int i = 0; i < node.getChildCount(); i++) {
                if (node.getChild(i) != null) {
                    findText(node.getChild(i), s);
                }
            }

        }
        return false;
    }


    /**
     * 根据id点击
     *
     * @param id
     */
    private void clickId(String id) {
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        List<AccessibilityNodeInfo> infos = rootNode.findAccessibilityNodeInfosByViewId(BANK_RESOURCE_ID_HEAD + id);
        rootNode.recycle();
        for (AccessibilityNodeInfo item : infos) {
            item.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//            Log.i(BANK_SERVICE_TAG, "clickId-----" + item.getText() + "");
        }
    }


    /**
     * 递归查找当前窗口中的id
     *
     * @param node
     * @param i
     * @return
     */
    public AccessibilityNodeInfo recycle(AccessibilityNodeInfo node, int i) {
        if (node.getChildCount() == 0) {
            if (node.getText() != null) {
                if (i == node.getWindowId()) {
                    return node;
                }
            }
        } else {
            for (int ii = 0; ii < node.getChildCount(); ii++) {
                if (node.getChild(i) != null) {
                    recycle(node.getChild(ii), i);
                }
            }
        }
        return node;
    }


    /**
     * 根据文字点击
     *
     * @param s
     */
    private void clickText(String s) {
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();
        AccessibilityNodeInfo node = recycle(rootNode, s);
//        Log.i(BANK_SERVICE_TAG, "clickText-----" + node.getText());
        node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        AccessibilityNodeInfo parent = node.getParent();
        while (parent != null) {
            if (parent.isClickable()) {
//                Log.i(BANK_SERVICE_TAG, "clickText-----parent" + (String) parent.getText());
                parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                break;
            }
            parent = parent.getParent();
        }
    }


    /**
     * 递归查找当前窗口中的字符串
     *
     * @param node
     */
    public AccessibilityNodeInfo recycle(AccessibilityNodeInfo node, String s) {

        int windowId = node.getWindowId();
        String nodeText = node.getText() == null ? "null" : node.getText().toString();
        String nodeContentDescription = node.getContentDescription() == null ? "null" : node.getContentDescription().toString();
//        Log.i(BANK_SERVICE_TAG, "递归查找窗口字符" + windowId + "------Node-text：" + nodeText + "------Node-contentDescription：" + nodeContentDescription);
        if (node.getChildCount() == 0) {
            if (node.getText() != null) {
                if (s.equals(node.getText().toString())) {
                    return node;
                }
            }
        } else {
            for (int i = 0; i < node.getChildCount(); i++) {
                if (node.getChild(i) != null) {
                    recycle(node.getChild(i), s);
                }
            }

        }
        return node;
    }

    /**
     * 2. * 执行shell命令
     * 3. *
     * 4. * @param cmd
     * 5.
     */
    private void execShellCmd(String cmd) {

        try {
            // 申请获取root权限，这一步很重要，不然会没有作用
            Process process = Runtime.getRuntime().exec("su");
            // 获取输出流
            OutputStream outputStream = process.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeBytes(cmd);
            dataOutputStream.flush();
            dataOutputStream.close();
            outputStream.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * 全局滑动操作
     *
     * @param x0
     * @param y0
     * @param x1
     * @param y1
     */
    public void perforGlobalSwipe(int x0, int y0, int x1, int y1, int time) {
//        Log.i(BANK_SERVICE_TAG, "shell swipe----" + x0 + ":" + y0 + "---" + x1 + ":" + y1 + ":" + time);
        execShellCmd("input swipe " + x0 + " " + y0 + " " + x1 + " " + y1 + " " + time);
    }

    /**
     * 点击操作
     *
     * @param x
     * @param y
     */
    public void perforGlobalClick(int x, int y) {
//        Log.i(BANK_SERVICE_TAG, "shell tap----" + x + ":" + y);
        execShellCmd("input tap " + x + " " + y);
    }


    private boolean isNotFind(AccessibilityNodeInfo rootNode, String txt) {
        List<AccessibilityNodeInfo> nodes;
        try {
            nodes = rootNode.findAccessibilityNodeInfosByText(txt);
//            Log.i(BANK_SERVICE_TAG, "isNotFind" + "------" + txt + "------" + String.valueOf(nodes == null || nodes.isEmpty()));

        } catch (Exception e) {
            return true;
        }
        return nodes == null || nodes.isEmpty();
    }

    private boolean isNotFindId(AccessibilityNodeInfo rootNode, String id) {
        List<AccessibilityNodeInfo> nodes = rootNode.findAccessibilityNodeInfosByViewId(BANK_RESOURCE_ID_HEAD + id);
//        Log.i(BANK_SERVICE_TAG, "isNotFindId" + "------" + id + "------" + String.valueOf(nodes == null || nodes.isEmpty()));
        return nodes == null || nodes.isEmpty();
    }

    public boolean isShaiXuanButtonNotVisible() {
        boolean visibleToUser;
        try {
            List<AccessibilityNodeInfo> accessibilityNodeInfosByViewId = getRootInActiveWindow().findAccessibilityNodeInfosByViewId(BANK_RESOURCE_ID_HEAD + "btn_right");
            visibleToUser = accessibilityNodeInfosByViewId.get(0).isVisibleToUser();
            return visibleToUser;
        } catch (Exception e) {
            return false;
        }
    }


    public static boolean aMoreThanb(double a, double b) {
        BigDecimal data1 = new BigDecimal(a);
        BigDecimal data2 = new BigDecimal(b);
        if (data1.compareTo(data2) < 0) {
            return false;
        }
        if (data1.compareTo(data2) == 0) {
            return true;
        }
        if (data1.compareTo(data2) > 0) {
            return true;
        }
        return false;
    }

}
