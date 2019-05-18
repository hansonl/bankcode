package com.weidou.tools;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.json.JSONObject;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;

public class XposedInit implements IXposedHookLoadPackage {

    private ClassLoader sdkWebViewClassLoader;
    private Object comTencentSmttSdkWebview;
    int i= 0;
     //stable version
    String js = "javascript:var script = document.createElement(\"script\");" +
            "script.type = \"text/javascript\";" +
            "function func(event) {" +
                //"document.getElementsByClassName(\"am-flexbox-item\")[0].classList.add(\"event_added_111\");" +
                "var el = document.getElementsByClassName(\"transfer-detail-viewd69fd0-protocol\")[0];" +

                "if (el.classList[1] == \"unchecked\") {" +
                    "el.click();" +
                    "setTimeout(function() { document.getElementsByClassName(\"am-button-primary\")[0].click();}, 100);" +
                "}" +


            "};" +

            "document.getElementsByClassName(\"am-flexbox-item\")[0].addEventListener(\"click\", func);";
//    String js = "javascript:var script = document.createElement(\"script\");" +
//            "script.type = \"text/javascript\";" +
//            "function func() {" +
//                //"document.getElementsByClassName(\"am-flexbox-item\")[0].classList.add(\"event_added_111\");" +
//                "var el = document.getElementsByClassName(\"transfer-detail-viewd69fd0-protocol\")[0];" +
//                "console.log(\"1111\");" +
//
//                "if (el.classList[1] == \"unchecked\") {" +
//                    "el.click();" +
//                    "setTimeout(function() { document.getElementsByClassName(\"am-button-primary\")[0].click();}, 100);" +
//                    "console.log(\"2222\");" +
//                "}" +
//
//            "};" +
//            "func();";
            //"document.getElementsByClassName(\"am-flexbox-item\")[0].addEventListener(\"click\", func);";


    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (isInjecter(this.getClass().getName())) {
            return;
        }
        if (!lpparam.isFirstApplication)return;
        if ("cn.com.cmbc.newmbank".equals(lpparam.processName)) {
            XposedBridge.log("检测到民生银行"+i+lpparam.processName);
            i++;


            //拿到页面加载的网页信息
            //attch进入了UsinglogManager里面的onWebPage方法---方法前https://mp.cmbc.com.cn:8223/CMBC_MPServer/page/setFiveBlessings/01.html
            //attch进入了UsinglogManager里面的onWebPage方法---方法前https://m1.cmbc.com.cn/CMBC_MBServer/new/app/mobile-bank/finance/home
            //attch进入了UsinglogManager里面的onWebPage方法---方法前https://m1.cmbc.com.cn/CMBC_MBServer/new/app/mobile-bank/finance/list
            findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    ClassLoader cl = ((Context) param.args[0]).getClassLoader();
                    Class<?> hookclass = null;
                    try {
                        hookclass = cl.loadClass("com.tesla.tmd.UsinglogManager");
                    } catch (Exception e) {
                        XposedBridge.log("attch寻找com.tesla.tmd.UsinglogManager" + e.toString());
                        return;
                    }
                    XposedBridge.log("attch寻找com.tesla.tmd.UsinglogManager成功");

                    try {
                        findAndHookMethod(hookclass
                                , "onWebPage"
                                , String.class
                                , Context.class

                                , new XC_MethodHook() {
                                    @Override
                                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                        super.beforeHookedMethod(param);
//                                        XposedBridge.log("attch进入了UsinglogManager里面的onWebPage方法---方法前" + param.args[0].toString());
//                                        XposedHelpers.callMethod(comTencentSmttSdkWebview
//                                                ,"loadUrl"
//                                                ,"javascript:document.onreadystatechange = function () {alert(document.readyState);}\n"
//                                        );
//                                        XposedBridge.log("TUsinglogManager里面的onWebPage方法前注入成功");
                                    }

                                    @Override
                                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                        super.afterHookedMethod(param);
                                        XposedBridge.log("attch进入了UsinglogManager里面的onWebPage方法---方法后");
                                    }
                                });
                    } catch (Exception throwable) {
                        throwable.printStackTrace();
                    }
                }
            });



            findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    ClassLoader cl = ((Context) param.args[0]).getClassLoader();
                    Class<?> hookclass = null;
                    try {
                        hookclass = cl.loadClass("com.tencent.smtt.sdk.WebView");
                    } catch (Exception e) {
                        XposedBridge.log("attch寻找com.tencent.smtt.sdk.WebView" + e.toString());
                        return;
                    }
                    XposedBridge.log("attch寻找com.tencent.smtt.sdk.WebView成功");

                    final Class<?> finalHookclass = hookclass;
                    try {
                        //可以成功替换loadUrl的方法
                        //通过此方法打开的debugx5.qq.com
                        findAndHookMethod(hookclass,
                                "loadUrl"
                                , String.class
                                , new XC_MethodHook() {

                                    @Override
                                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                        // 打开webContentsDebuggingEnabled

//                                param.args[0] = "http://debugx5.qq.com";

                                        XposedBridge.log("com.tencent.smtt.sdk.WebView更改loadUrl执行=");

                                    }
                                });
                        findAndHookMethod(hookclass,
                                "c"
                                , new XC_MethodHook() {

                                    @Override
                                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {


                                    }

                                    @Override
                                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                        //疯狂注入方式可行稳定 正常的流程下没问题 疯狂重复刷新列表页没问题 退出webview在进入也没问题 重启app没问题
                                        comTencentSmttSdkWebview = param.thisObject;
                                        XposedHelpers.callMethod(comTencentSmttSdkWebview
                                                ,"loadUrl"
                                                ,js);
                                        XposedBridge.log("注入成功");
                                    }
                                });

                        findAndHookMethod(hookclass,
                                "evaluateJavaScript"
                                , String.class
                                , hookclass.getClassLoader().loadClass("com.tencent.smtt.sdk.ValueCallBack")
                                , new XC_MethodHook() {

                                    @Override
                                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                                        XposedBridge.log("evaluateJavaScript方法前第一次参数为" + param.args[0].toString());

                                    }

                                    @Override
                                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                                        XposedHelpers.callMethod(param.thisObject
//                                                , "loadUrl"
//                                                , "document.getElementsByClassName(\"transfer-detail-viewd69fd0-protocol\")[0].click();\n"
//                                        );

                                        XposedBridge.log("evaluateJavaScript方法后");
                                    }
                                });
                    } catch (Exception throwable) {
                        throwable.printStackTrace();
                    }
                }
            });


            //尝试找到执行一次的方法
            findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    ClassLoader cl = ((Context) param.args[0]).getClassLoader();
                    Class<?> hookclass = null;
                    try {
                        hookclass = cl.loadClass("cn.com.cmbc.newmbank.views.TitleBarView");
                    } catch (Exception e) {
                        XposedBridge.log("attch寻找cn.com.cmbc.newmbank.views.TitleBarView" + e.toString());
                        return;
                    }
                    XposedBridge.log("attch寻找cn.com.cmbc.newmbank.views.TitleBarView成功");

                    final Class<?> finalHookclass = hookclass;
                    try {
                        findAndHookMethod(hookclass,
                                "setTitle"
                                , String.class
                                , new XC_MethodHook() {

                                    @Override
                                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                                    }

                                    @Override
                                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                                        XposedHelpers.callMethod(param.thisObject
//                                        ,"loadUrl"
//                                        ,"javascrpit:document.getElementsByClassName('transfer-detail-viewd69fd0-protocol')[0].click();"
//                                                        );
//                                        XposedBridge.log("执行cn.com.cmbc.newmbank.views.TitleBarView.setTitle(String)方法后");
//                                        XposedHelpers.callMethod(comTencentSmttSdkWebview
//                                        ,"loadUrl"
//                                        ,"javascript:document.getElementsByClassName(\"am-flexbox-item\")[0].addEventListener(\"click\", (event) => {alert(\"aaaa\");});\n"
//                                        );
//                                        XposedBridge.log("TitleBarView.setTitle(String)方法后注入成功");
                                    }
                                });
                    } catch (Exception throwable) {
                        throwable.printStackTrace();
                    }
                }
            });

            //尝试找到页面加载完的方法
            findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    ClassLoader cl = ((Context) param.args[0]).getClassLoader();
                    Class<?> hookclass = null;
                    try {
                        hookclass = cl.loadClass("cn.com.cmbc.newmbank.webkitjsimpl.WebKitEncryForCmbc");
                    } catch (Exception e) {
                        XposedBridge.log("attch寻找cn.com.cmbc.newmbank.webkitjsimpl.WebKitEncryForCmbc" + e.toString());
                        return;
                    }
                    XposedBridge.log("attch寻找cn.com.cmbc.newmbank.webkitjsimpl.WebKitEncryForCmbc成功");

                    final Class<?> finalHookclass = hookclass;
                    try {
                        findAndHookMethod(hookclass,
                                "executeJavaScript"
                                , String.class
                                ,String.class
                                , new XC_MethodHook() {

                                    @Override
                                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                        XposedBridge.log("执行cn.com.cmbc.newmbank.webkitjsimpl.WebKitEncryForCmbc.excuteJavaScript方法前参数1" +
                                                param.args[0].toString()+"参数2"+param.args[1].toString());

                                    }

                                    @Override
                                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                                        XposedHelpers.callMethod(param.thisObject
//                                        ,"loadUrl"
//                                        ,"javascrpit:document.getElementsByClassName('transfer-detail-viewd69fd0-protocol')[0].click();"
//                                                        );
//                                        XposedBridge.log("执行cn.com.cmbc.newmbank.webkitjsimpl.WebKitEncryForCmbc.excuteJavaScript方法后");
//                                        XposedHelpers.callMethod(comTencentSmttSdkWebview
//                                        ,"loadUrl"
//                                        ,"javascript:alert(\"hhhhhh\");");
//                                        XposedBridge.log("WebKitEncryForCmbc.excuteJavaScript方法后注入成功");
                                    }
                                });
                    } catch (Exception throwable) {
                        throwable.printStackTrace();
                    }
                }
            });

            //尝试通过shouldInterceptRequest抓取js返回
            findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    ClassLoader cl = ((Context) param.args[0]).getClassLoader();
                    Class<?> hookclass = null;
                    try {
                        hookclass = cl.loadClass("com.tencent.smtt.sdk.WebViewClient");
                    } catch (Exception e) {
                        XposedBridge.log("attch寻找com.tencent.smtt.sdk.WebViewClient" + e.toString());
                        return;
                    }
                    XposedBridge.log("attch寻找com.tencent.smtt.sdk.WebViewClient成功");

                    try {
                        findAndHookMethod(hookclass
                                , "onPageFinished"
                                , lpparam.classLoader.loadClass("com.tencent.smtt.sdk.WebView")
                                , String.class
                                , new XC_MethodHook() {

                                    @Override
                                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                        super.beforeHookedMethod(param);
                                        XposedBridge.log("x5webviewClient中onPageFinished执行前arg="+param.args[1]);
                                    }

                                    @Override
                                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                        super.afterHookedMethod(param);
                                        //打开webview onpageFinished后注入 正常的流程下没问题 疯狂重复刷新列表页没问题 退出webview在进入不能点击不能弹出对话框 重启app没问题
//                                        XposedHelpers.callMethod(comTencentSmttSdkWebview
//                                                ,"loadUrl"
//                                                ,js);
//                                        XposedBridge.log("注入成功");
                                        XposedBridge.log("x5webviewClient中onPageFinished执行后");
                                    }
                                });


//                        findAndHookMethod(hookclass
//                                , "shouldOverrideUrlLoading"
//                                , lpparam.classLoader.loadClass("com.tencent.smtt.sdk.WebView")
//                                , String.class
//                                , new XC_MethodHook() {
//
//                                    @Override
//                                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                                        super.beforeHookedMethod(param);
//                                        XposedBridge.log("x5webviewClient中shouldOverrideUrlLoading执行前");
//                                    }
//
//                                    @Override
//                                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                                        super.afterHookedMethod(param);
//                                        XposedHelpers.callMethod(comTencentSmttSdkWebview
//                                                ,"loadUrl"
//                                                ,"javascript:document.getElementsByClassName(\"transfer-detail-viewd69fd0-protocol\")[0].click();\n");
//                                        XposedBridge.log("x5webviewClient中shouldOverrideUrlLoading返回值");
//                                    }
//                                });
                    } catch (Exception throwable) {
                        throwable.printStackTrace();
                    }
                }
            });

            //尝试通过shouldInterceptRequest抓取js返回

//            try {
//                Class<?> aClass = findClass("android.webkit.WebViewClient", lpparam.classLoader);
//                XposedBridge.log("找到webview");
//                findAndHookMethod(aClass
//                        , "onPageFinished"
//                        , lpparam.classLoader.loadClass("android.webkit.WebView")
//                        , String.class
//                        , new XC_MethodHook() {
//
//                            @Override
//                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                                super.beforeHookedMethod(param);
//                                XposedBridge.log("webviewClient中onPageFinished执行前");
//                            }
//
//                            @Override
//                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                                super.afterHookedMethod(param);
//                                XposedBridge.log("webviewClient中onPageFinished返回值" + param.getResult().toString());
//                            }
//                        });
//            } catch (Exception throwable) {
//                throwable.printStackTrace();
//            }



            //尝试找到执行一次的方法 找不到这个类
//            findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
//                @Override
//                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                    ClassLoader cl = ((Context) param.args[0]).getClassLoader();
//                    Class<?> hookclass = null;
//                    try {
//                        hookclass = cl.loadClass("com.tencent.smtt.webkit.b.a");
//                    } catch (Exception e) {
//                        XposedBridge.log("attch寻找com.tencent.smtt.webkit.b.a" + e.toString());
//                        return;
//                    }
//                    XposedBridge.log("attch寻找com.tencent.smtt.webkit.b.a成功");
//
//                    final Class<?> finalHookclass = hookclass;
//                    try {
//                        findAndHookMethod(hookclass,
//                                "e"
//                                , String.class
//                                , new XC_MethodHook() {
//
//                                    @Override
//                                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                                        XposedBridge.log("找到com.tencent.smtt.webkit.b.a成功参数"+param.args[0].toString());
//                                    }
//
//                                    @Override
//                                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                                    }
//                                });
//                    } catch (Exception throwable) {
//                        throwable.printStackTrace();
//                    }
//                }
//            });


            //获得各种Info的方法
            //attch进入了com.tesla.tmd.CommonUtil里面的saveInfoToFile方法---方法前activityInfo------{"session_id":"285b7b2194e9aa7a92e9b42d36a34d94","start_millis":"2019-05-16 10:58:17","end_millis":"2019-05-16 10:58:17","duration":"13","sd":"13","version":"4.4","activities":"HomePageFragment","appkey":"65c357c029f3aa85b763b7d5fa9f29ef","useridentifier":"2FAC378A41D7F16DBDECD36F11115368F8145F30","deviceid":"9b7aecd5fe8acd95ab789aa24b8d9e7e","lib_version":"1.0"}
            //attch进入了com.tesla.tmd.CommonUtil里面的saveInfoToFile方法---方法前eventInfo------{"time":"2019-05-16 11:04:09:818","version":"4.4","event_identifier":"homeHotAct","appkey":"65c357c029f3aa85b763b7d5fa9f29ef","activity":"HomePageFragment","label":"","acc":"1","attachment":"","useridentifier":"2FAC378A41D7F16DBDECD36F11115368F8145F30","deviceid":"9b7aecd5fe8acd95ab789aa24b8d9e7e","session_id":"9e0b153f8543c76995558825e04c2693","lib_version":"1.0","V_index":7,"V_product":"1000000102","V_ds":"ANDROID","V_req_id":"","V_rec_id":"","V_sid":"57361f148d396638b0fc169b5d898458","V_cid":"C95Item"}
//            findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
//                @Override
//                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                    ClassLoader cl = ((Context) param.args[0]).getClassLoader();
//                    Class<?> hookclass = null;
//                    try {
//                        hookclass = cl.loadClass("com.tesla.tmd.CommonUtil");
//                    } catch (Exception e) {
//                        XposedBridge.log("attch寻找com.tesla.tmd.CommonUtil" + e.toString());
//                        return;
//                    }
//                    XposedBridge.log("attch寻找com.tesla.tmd.CommonUtil成功");
//
//                    try {
//                        findAndHookMethod(hookclass
//                                , "saveInfoToFile"
//                                , String.class
//                                , JSONObject.class
//                                , Context.class
//
//                                , new XC_MethodHook() {
//                                    @Override
//                                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                                        super.beforeHookedMethod(param);
//                                        XposedBridge.log("attch进入了com.tesla.tmd.CommonUtil里面的saveInfoToFile方法---方法前" + param.args[0].toString() +"------"+param.args[1].toString());
//                                    }
//
//                                    @Override
//                                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                                        super.afterHookedMethod(param);
//                                        XposedBridge.log("attch进入了com.tesla.tmd.CommonUtil里面的saveInfoToFile方法---方法后");
//                                    }
//                                });
//                    } catch (Exception throwable) {
//                        throwable.printStackTrace();
//                    }
//                }
//            });


//            findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
//                @Override
//                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                    ClassLoader cl = ((Context) param.args[0]).getClassLoader();
//                    Class<?> hookclass = null;
//                    try {
//                        hookclass = cl.loadClass("com.cmbc.firefly.webview.engine.FfWebViewClient");
//                    } catch (Exception e) {
//                        XposedBridge.log("attch寻找com.cmbc.firefly.webview.engine.FfWebViewClient" + e.toString());
//                        return;
//                    }
//                    XposedBridge.log("attch寻找com.cmbc.firefly.webview.engine.FfWebViewClient成功");
//
//                    try {
//                        findAndHookMethod(hookclass
//                                , "shouldInterceptRequest"
//                                , hookclass.getClassLoader().loadClass("com.tencent.smtt.sdk.WebView")
//                                , hookclass.getClassLoader().loadClass("com.tencent.smtt.export.external.interfaces.WebResourceRequest")
//
//                                , new XC_MethodHook() {
//                                    @Override
//                                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                                        super.beforeHookedMethod(param);
//                                        XposedBridge.log("attch进入了FfWebViewClient里面的shouldInterceptRequest方法---方法前" + param.args[1]);
//                                    }
//
//                                    @Override
//                                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                                        super.afterHookedMethod(param);
//                                        XposedBridge.log("attch进入了FfWebViewClient里面的shouldInterceptRequest方法---方法后" + param.getResult().toString());
//                                    }
//                                });
//                    } catch (Exception throwable) {
//                        throwable.printStackTrace();
//                    }
//                }
//            });

            //重新载入url的方法（没有测试完成）
            //cn.com.cmbc.newmbank.views.WebKitView.loadUrl(String,String) 不知道那个string是url
            //这个方法内部调用了com.tencent.smtt.sdk.WebView.loadUrl(String)
//            findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
//                @Override
//                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                    ClassLoader cl = ((Context) param.args[0]).getClassLoader();
//                    Class<?> hookclass = null;
//                    try {
//                        hookclass = cl.loadClass("cn.com.cmbc.newmbank.views.WebKitView");
//                    } catch (Exception e) {
//                        XposedBridge.log("attch寻找cn.com.cmbc.newmbank.views.WebKitView" + e.toString());
//                        return;
//                    }
//                    XposedBridge.log("attch寻找cn.com.cmbc.newmbank.views.WebKitView成功");
//
//                    final Class<?> finalHookclass = hookclass;
//                    try {
//                        findAndHookMethod(hookclass,
//                                "loadUrl"
//                                ,String.class
//                                , new XC_MethodHook() {
//
//                                    @Override protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                                        // 打开webContentsDebuggingEnabled
//
//                                        param.args[0] = "debugx5.qq.com";
//
//                                        XposedBridge.log("WebViewHook new WebView(): " );
//
//                                    } });
//                    } catch (Exception throwable) {
//                        throwable.printStackTrace();
//                    }
//                }
//            });


            //尝试通过shouldInterceptRequestFromNative抓取js返回值
//            try {
//
//                XposedHelpers.findAndHookMethod("android.webview.chromium.tencent.TencentWebViewContentsClientAdapter"
//                        , lpparam.classLoader
//                        , "shouldInterceptRequestFromNative"
//                        , String.class
//                        , String.class
//                        , String.class
//                        , String.class
//                        , new XC_MethodHook() {
//                            @Override
//                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                                super.beforeHookedMethod(param);
//                                XposedBridge.log("进入了AwContentsBackgroundThreadClient里面的shouldInterceptRequestFromNative方法---方法前" + param.args[1]);
//
//                            }
//
//                            @Override
//                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                                super.afterHookedMethod(param);
//                                XposedBridge.log("attch进入了AwContentsBackgroundThreadClient里面的shouldInterceptRequestFromNative方法---方法后" + param.getResult().toString());
//
//                            }
//                        });
//            } catch (Exception e) {
//                XposedBridge.log("寻找org.chromium.android_webview.AwContentsBackgroundThreadClient" + e.toString());
//
//            }

            //尝试通过shouldInterceptRequestFromNative抓取js返回值
//            findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
//                @Override
//                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                    ClassLoader cl = ((Context) param.args[0]).getClassLoader();
//                    Class<?> hookclass = null;
//                    try {
//                        hookclass = cl.loadClass("org.chromium.android_webview.AwContentsBackgroundThreadClient");
//                    } catch (Exception e) {
//                        XposedBridge.log("attch寻找org.chromium.android_webview.AwContentsBackgroundThreadClient" + e.toString());
//                        return;
//                    }
//                    XposedBridge.log("attch寻找org.chromium.android_webview.AwContentsBackgroundThreadClient成功");
//
//                    try {
//                        XposedHelpers.findAndHookMethod(
//                                 hookclass
//                                , "shouldInterceptRequestFromNative"
//                                , String.class
//                                , String.class
//                                , String.class
//                                , String.class
//                                , new XC_MethodHook() {
//                                    @Override
//                                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                                        super.beforeHookedMethod(param);
//                                        XposedBridge.log("进入了AwContentsBackgroundThreadClient里面的shouldInterceptRequestFromNative方法---方法前" + param.args[1]);
//
//                                    }
//
//                                    @Override
//                                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                                        super.afterHookedMethod(param);
//                                        XposedBridge.log("attch进入了AwContentsBackgroundThreadClient里面的shouldInterceptRequestFromNative方法---方法后" + param.getResult().toString());
//
//                                    }
//                                });
//                    } catch (Exception throwable) {
//                        throwable.printStackTrace();
//                    }
//                }
//            });

            //打印ArrayList的toArray方法
//            findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
//                @Override
//                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                    ClassLoader cl = ((Context) param.args[0]).getClassLoader();
//                    Class<?> hookclass = null;
//                    try {
//                        hookclass = cl.loadClass("java.util.ArrayList");
//                    } catch (Exception e) {
//                        XposedBridge.log("attch寻找java.util.ArrayList" + e.toString());
//                        return;
//                    }
//                    XposedBridge.log("attch寻找java.util.ArrayList成功");
//
//                    try {
//                        XposedHelpers.findAndHookMethod(
//                                hookclass
//                                , "toArray"
//                                , new XC_MethodHook() {
//                                    @Override
//                                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                                        super.beforeHookedMethod(param);
//                                        XposedBridge.log("toArray---方法前" +param.thisObject.toString());
//
//                                    }
//
//                                    @Override
//                                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                                        super.afterHookedMethod(param);
//                                        XposedBridge.log("toArray---方法后" + param.getResult().toString());
//                                    }
//                                });
//                    } catch (Exception throwable) {
//                        throwable.printStackTrace();
//                    }
//                }
//            });

            //通过js设置actionbar的方法
//            XposedBridge.log("开始hook SysClientJsImpl里面的access方法");
//            XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
//                @Override
//                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                    ClassLoader cl = ((Context) param.args[0]).getClassLoader();
//                    Class<?> hookclass = null;
//                    try {
//                        hookclass = cl.loadClass("cn.com.cmbc.newmbank.webkitjsimpl.SysClientJsImpl");
//                    } catch (Exception e) {
//                        XposedBridge.log("寻找cn.com.cmbc.newmbank.webkitjsimpl.SysClientJsImpl" + e.toString());
//                        return;
//                    }
//                    XposedBridge.log("寻找cn.com.cmbc.newmbank.webkitjsimpl.SysClientJsImpl成功");
//
//                    try {
//                        XposedHelpers.findAndHookMethod(hookclass
//                                , "access$300"
//                                , hookclass
//                                , lpparam.classLoader.loadClass("org/json/JSONObject")
//                                , new XC_MethodHook() {
//                                    @Override
//                                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                                        super.beforeHookedMethod(param);
//                                        XposedBridge.log("进入了SysClientJsImpl里面的access方法---方法前"+param.args[1]);
//                                    }
//
//                                    @Override
//                                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                                        super.afterHookedMethod(param);
//                                        XposedBridge.log("进入了SysClientJsImpl里面的access方法---方法后");
//                                    }
//                                });
//                    } catch (Exception throwable) {
//                        throwable.printStackTrace();
//                    }
//                }
//            });

        }
    }


    /**
     *防止重复执行Hook代码
     * @param flag 判断标识,针对不同Hook代码分别进行判断
     * @return 是否已经注入Hook代码
     */
    private boolean isInjecter(String flag) {
        try {
            XposedBridge.log("进入isInjecter"+flag);
            if (TextUtils.isEmpty(flag)) return false;
            XposedBridge.log("text_is_not_empty");
            Field methodCacheField = XposedHelpers.class.getDeclaredField("methodCache");
            methodCacheField.setAccessible(true);
            HashMap<String, Method> methodCache = (HashMap<String, Method>) methodCacheField.get(null);
            Method method=XposedHelpers.findMethodBestMatch(Application.class,"onCreate");
            String key=String.format("%s#%s",flag,method.getName());
            if (methodCache.containsKey(key)) return true;
            methodCache.put(key, method);
            return false;
        } catch (Throwable e) {
            XposedBridge.log("");
            e.printStackTrace();
        }
        return false;
    }
}
