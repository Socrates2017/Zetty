/*! jEnel v1.0.0 | (c) Chen AnLian */

/**获取当前项目的路径*/
var urlRootContext = function () {
    var strPath = window.document.location.pathname;
    var postPath = strPath.substring(0, strPath.substr(1).indexOf('/') + 1);
    return postPath;
};

//js获取项目根路径，如： http://localhost:8083/uimcardprj
function getRootPath() {
    //获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp  
    var curWwwPath = window.document.location.href;
    //获取主机地址之后的目录，如： uimcardprj/share/meun.jsp  
    var pathName = window.document.location.pathname;
    var pos = curWwwPath.indexOf(pathName);
    //获取主机地址，如： http://localhost:8083  
    var localhostPaht = curWwwPath.substring(0, pos);
    //获取带"/"的项目名，如：/uimcardprj  
    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    return (localhostPaht + projectName);
}

/**
 * 自定义弹窗
 */
(function () {
    $.MsgBox = {
        Alert: function (title, msg) {
            GenerateHtml("alert", title, msg);
            btnOk();  //此方法没有回调函数，亦不会阻止进程执行下一步
            //btnNo();
        },
        Confirm: function (title, msg, callback) {
            GenerateHtml("confirm", title, msg);
            btnOk(callback);
            //btnNo();
        },
        Cancel: function (title, msg, callback) {
            GenerateHtml("cancel", title, msg);
            btnOk(callback);
            btnNo();
        },
        Cancel2: function (title, msg, callback) {
            GenerateHtml("cancel", title, msg);
            btnOk2(callback);
            btnNo();
        }
    }
    //生成Html
    var GenerateHtml = function (type, title, msg) {
        var _html = "";
        _html += '<div id="mb_box"></div><div id="mb_con"><span id="mb_tit">' + title + '</span>';
        _html += '<div id="mb_msg">' + msg + '</div><div id="mb_btnbox">';
        if (type == "alert") {
            _html += '<input id="mb_btn_ok" type="button" value="确定" />';
        }
        if (type == "confirm") {
            _html += '<input id="mb_btn_ok" type="button" value="确定" />';
        }
        if (type == "cancel") {
            _html += '<input id="mb_btn_ok" type="button" value="确定" />';
            _html += '<input id="mb_btn_no" type="button" value="取消" />';
        }
        _html += '</div></div>';
        //必须先将_html添加到body，再设置Css样式
        $("body").append(_html);
        //生成Css
        GenerateCss();
    }
    //生成Css
    var GenerateCss = function () {
        $("#mb_box").css({
            width: '100%', height: '100%', zIndex: '99999', position: 'fixed',
            filter: 'Alpha(opacity=60)', backgroundColor: 'black', top: '0', left: '0', opacity: '0.6'
        });
        $("#mb_con").css({
            zIndex: '999999', width: '300px', position: 'fixed',
            backgroundColor: 'White', borderRadius: '15px'
        });
        $("#mb_tit").css({
            display: 'none', fontSize: '14px', color: '#444', padding: '10px 15px',
            backgroundColor: '#DDD', borderRadius: '15px 15px 0 0',
            borderBottom: '3px solid #009BFE', fontWeight: 'bold'
        });
        $("#mb_msg").css({
            padding: '20px', lineHeight: '20px', textAlign: 'center',
            borderBottom: '1px dashed #DDD', fontSize: '13px'
        });
        $("#mb_ico").css({
            display: 'block', position: 'absolute', right: '10px', top: '9px',
            border: '1px solid Gray', width: '18px', height: '18px', textAlign: 'center',
            lineHeight: '16px', cursor: 'pointer', borderRadius: '12px', fontFamily: '微软雅黑'
        });
        $("#mb_btnbox").css({margin: '15px 0 10px 0', textAlign: 'center'});
        $("#mb_btn_ok,#mb_btn_no").css({width: '85px', height: '30px', color: 'white', border: 'none'});
        $("#mb_btn_ok").css({backgroundColor: '#168bbb'});
        $("#mb_btn_no").css({backgroundColor: 'gray', marginLeft: '20px'});
        //右上角关闭按钮hover样式
        $("#mb_ico").hover(function () {
            $(this).css({backgroundColor: 'Red', color: 'White'});
        }, function () {
            $(this).css({backgroundColor: '#DDD', color: 'black'});
        });
        var _widht = document.documentElement.clientWidth;  //屏幕宽
        var _height = document.documentElement.clientHeight; //屏幕高
        var boxWidth = $("#mb_con").width();
        var boxHeight = $("#mb_con").height();
        //让提示框居中
        $("#mb_con").css({top: (_height - boxHeight) / 2 + "px", left: (_widht - boxWidth) / 2 + "px"});
    }
    //确定按钮事件
    var btnOk = function (callback) {
        $("#mb_btn_ok").click(function () {
            $("#mb_box,#mb_con").remove();
            if (typeof (callback) == 'function') {
                callback();
            }
        });
    }

    var btnOk2 = function (callback) {
        $("#mb_btn_ok").click(function () {
            if (typeof (callback) == 'function') {
                callback();
            }
            $("#mb_box,#mb_con").remove();
        });
    }

    //取消按钮事件
    var btnNo = function () {
        $("#mb_btn_no,#mb_ico").click(function () {
            $("#mb_box,#mb_con").remove();
        });
    }
})();

/**刷新图标*/
function showReflesh() {
    var reflesh = function () {
        var refleshHtml = "";
        refleshHtml += '<div id="reflesh">';
        refleshHtml += '<img id="refleshImg" alt="" src="/static/img/reflesh.gif">';
        refleshHtml += '</div>';
        //必须先将_html添加到body，再设置Css样式
        $("body").append(refleshHtml);
        //生成Css
        GenerateCss();
    }
    var GenerateCss = function () {
        var _widht = document.documentElement.clientWidth;  //屏幕宽
        var _height = document.documentElement.clientHeight; //屏幕高
        $("#reflesh").css({zIndex: '999999', width: _widht / 5 + "px", position: 'fixed', borderRadius: '50%'});
        var boxWidth = $("#reflesh").width();
        var boxHeight = $("#reflesh").height();
        $("#refleshImg").css({zIndex: '999999', width: boxWidth + "px", position: 'fixed'});
        //让提示框居中
        $("#reflesh").css({top: (_height - boxHeight) / 2 + "px", left: (_widht - boxWidth) / 2 + "px"});
    }
    reflesh();
}

/**移除缓冲图标*/
function removeReflesh() {
    $("#reflesh").remove();
}

/**
 * 文本框根据输入内容自适应高度
 * @param                {HTMLElement}        输入框元素
 * @param                {Number}                设置光标与输入框保持的距离(默认0)
 * @param                {Number}                设置最大高度(可选)
 */
var autoTextarea = function (elem, extra, maxHeight) {
    extra = extra || 0;
    var isFirefox = !!document.getBoxObjectFor || 'mozInnerScreenX' in window,
        isOpera = !!window.opera && !!window.opera.toString().indexOf('Opera'),
        addEvent = function (type, callback) {
            elem.addEventListener ?
                elem.addEventListener(type, callback, false) :
                elem.attachEvent('on' + type, callback);
        },
        getStyle = elem.currentStyle ? function (name) {
            var val = elem.currentStyle[name];

            if (name === 'height' && val.search(/px/i) !== 1) {
                var rect = elem.getBoundingClientRect();
                return rect.bottom - rect.top -
                    parseFloat(getStyle('paddingTop')) -
                    parseFloat(getStyle('paddingBottom')) + 'px';
            }
            ;

            return val;
        } : function (name) {
            return getComputedStyle(elem, null)[name];
        },
        minHeight = parseFloat(getStyle('height'));


    elem.style.resize = 'none';

    var change = function () {
        var scrollTop, height,
            padding = 0,
            style = elem.style;

        if (elem._length === elem.value.length) return;
        elem._length = elem.value.length;

        if (!isFirefox && !isOpera) {
            padding = parseInt(getStyle('paddingTop')) + parseInt(getStyle('paddingBottom'));
        }
        ;
        scrollTop = document.body.scrollTop || document.documentElement.scrollTop;

        elem.style.height = minHeight + 'px';
        if (elem.scrollHeight > minHeight) {
            if (maxHeight && elem.scrollHeight > maxHeight) {
                height = maxHeight - padding;
                style.overflowY = 'auto';
            } else {
                height = elem.scrollHeight - padding;
                style.overflowY = 'hidden';
            }
            ;
            style.height = height + extra + 'px';
            scrollTop += parseInt(style.height) - elem.currHeight;
            document.body.scrollTop = scrollTop;
            document.documentElement.scrollTop = scrollTop;
            elem.currHeight = parseInt(style.height);
        }
        ;
    };

    addEvent('propertychange', change);
    addEvent('input', change);
    addEvent('focus', change);
    change();
};


/**
 * 获取formdata
 */
function getFormData() {
    var isNeedShim = ~navigator.userAgent.indexOf('Android')
        && ~navigator.vendor.indexOf('Google')
        && !~navigator.userAgent.indexOf('Chrome')
        && navigator.userAgent.match(/AppleWebKit\/(\d+)/).pop() <= 534;
    return isNeedShim ? new FormDataShim() : new FormData()
}

/**
 * @description文件上传
 * @param file 上传的文件
 * @param URL 调用接口
 * @param successCallback 回调函数，其中参数data为返回数据
 *
 */
function upload(file, URL, successCallback) {
    var formdata = getFormData();
    formdata.append('file', file);
    $.ajax({
        type: "post",
        url: URL,
        async: true,
        contentType: false, //这个一定要写
        processData: false, //这个也一定要写，不然会报错
        data: formdata,
        dataType: 'text', //返回类型，有json，text，HTML。这里并没有jsonp格式，所以别妄想能用jsonp做跨域了。
        success: function (data) {
            successCallback(data);
        },
        error: function (XMLHttpRequest, textStatus, errorThrown,
                         data) {
            alert(errorThrown);
        }
    });
}


/**
 * 利用promise属性进行上传，可以实现异步函数按队列进行
 * @param file 上传的文件
 * @param URL 调用的接口
 * @param successData，必须为数组，用以存储ajax返回的数据
 * @param i successData数组的索引 如successData[i]
 * @example
 * var uploadImgPath=new Array();
 function reg() {
		showReflesh();		
		var images = $('#upload-liceimg').get(0).files[0];
		var images2 = $('#upload-bankimg').get(0).files[0];
		var p1 = uploadInPromise(images, urlRootContext() + "/html2base/user/reg/fileupload?fileKind=liceimg",uploadImgPath,0);	
		var p2 = uploadInPromise(images2, urlRootContext() + "/html2base/user/reg/fileupload?fileKind=bankimg",uploadImgPath,1);	
		// 同时执行p1和p2，并在它们都完成后执行then:
		//单个p的时候，用p1.then(function..)
		Promise.all([p1, p2]).then(function () { 
			reg2();
		}).catch(function () { 
			alert("升级失败");
		});
	}
 * */
function uploadInPromise(file, URL, successData, i) {
    return new Promise(function (resolve, reject) {
        var formdata = getFormData();
        formdata.append('file', file);
        $.ajax({
            type: "post",
            url: URL,
            async: true,
            contentType: false, //这个一定要写
            processData: false, //这个也一定要写，不然会报错
            data: formdata,
            dataType: 'text', //返回类型，有json，text，HTML。这里并没有jsonp格式，所以别妄想能用jsonp做跨域了。
            success: function (data) {
                successData[i] = data;
                resolve();
            },
            error: function (XMLHttpRequest, textStatus, errorThrown,
                             data) {
                alert(errorThrown);
            }
        });
    });
}

/**
 * 判断对象是否为空
 * @param obj
 * @returns
 */
function isEmpty(obj) {
    if (null == obj || "" == obj || "null" == obj) {
        return true;
    }
    return false;
}

// 对Date的扩展，将 Date 转化为指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
// 例子：
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1,                 //月份
        "d+": this.getDate(),                    //日
        "h+": this.getHours(),                   //小时
        "m+": this.getMinutes(),                 //分
        "s+": this.getSeconds(),                 //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds()             //毫秒
    };
    if (/(y+)/.test(fmt))
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
        if (new RegExp("(" + k + ")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

var EventUtil = {
    addHandler: function (element, type, handler) {
        if (element.addEventListener)
            element.addEventListener(type, handler, false);
        else if (element.attachEvent)
            element.attachEvent("on" + type, handler);
        else
            element["on" + type] = handler;
    },
    removeHandler: function (element, type, handler) {
        if (element.removeEventListener)
            element.removeEventListener(type, handler, false);
        else if (element.detachEvent)
            element.detachEvent("on" + type, handler);
        else
            element["on" + type] = handler;
    },
    /**
     * 监听触摸的方向
     * @param target            要绑定监听的目标元素
     * @param isPreventDefault  是否屏蔽掉触摸滑动的默认行为（例如页面的上下滚动，缩放等）
     * @param upCallback        向上滑动的监听回调（若不关心，可以不传，或传false）
     * @param rightCallback     向右滑动的监听回调（若不关心，可以不传，或传false）
     * @param downCallback      向下滑动的监听回调（若不关心，可以不传，或传false）
     * @param leftCallback      向左滑动的监听回调（若不关心，可以不传，或传false）
     */
    listenTouchDirection: function (target, isPreventDefault, upCallback, rightCallback, downCallback, leftCallback) {
        this.addHandler(target, "touchstart", handleTouchEvent);
        this.addHandler(target, "touchend", handleTouchEvent);
        this.addHandler(target, "touchmove", handleTouchEvent);
        var startX;
        var startY;

        function handleTouchEvent(event) {
            switch (event.type) {
                case "touchstart":
                    startX = event.touches[0].pageX;
                    startY = event.touches[0].pageY;
                    break;
                case "touchend":
                    var spanX = event.changedTouches[0].pageX - startX;
                    var spanY = event.changedTouches[0].pageY - startY;

                    if (Math.abs(spanX) > Math.abs(spanY)) {      //认定为水平方向滑动
                        if (spanX > 30) {         //向右
                            if (rightCallback)
                                rightCallback();
                        } else if (spanX < -30) { //向左
                            if (leftCallback)
                                leftCallback();
                        }
                    } else {                                    //认定为垂直方向滑动
                        if (spanY > 30) {         //向下
                            if (downCallback)
                                downCallback();
                        } else if (spanY < -30) {//向上
                            if (upCallback)
                                upCallback();
                        }
                    }

                    break;
                case "touchmove":
                    //阻止默认行为
                    if (isPreventDefault)
                        event.preventDefault();
                    break;
            }
        }
    }
};

/**
 * 判断字符串是否以某字符串结尾
 * console.log(caseNum.endWith("YD"));
 * @param s
 * @returns {boolean}
 */
String.prototype.endWith = function (s) {
    var d = this.length - s.length;
    return (d >= 0 && this.lastIndexOf(s) == d)
}

/**
 * 设置cookie
 * @param name
 * @param value
 */
function setCookie(name, value) {
    var Days = 30;
    var exp = new Date();
    exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
    document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString();
}

function getCookie(name) {
    var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
    if (arr = document.cookie.match(reg))
        return unescape(arr[2]);
    else
        return null;
}

function delCookie(name) {
    var exp = new Date();
    exp.setTime(exp.getTime() - 1);
    var cval = getCookie(name);
    if (cval != null)
        document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString();
}

//使用示例
//setCookie("name", "hayden");
//alert(getCookie("name"));
//如果需要设定自定义过期时间
//那么把上面的setCookie　函数换成下面两个函数就ok;
//程序代码
function setCookie(name, value, time) {
    var strsec = getsec(time);
    var exp = new Date();
    exp.setTime(exp.getTime() + strsec * 1);
    document.cookie = name + "=" + escape(value) + ";expires=" + exp.toGMTString();
}

function getsec(str) {
    var str1 = str.substring(1, str.length) * 1;
    var str2 = str.substring(0, 1);
    if (str2 == "s") {
        return str1 * 1000;
    } else if (str2 == "h") {
        return str1 * 60 * 60 * 1000;
    } else if (str2 == "d") {
        return str1 * 24 * 60 * 60 * 1000;
    }
}

/**
 * 判断对象是否为空
 */
(function ($) {
    $.isBlank = function (obj) {
        return (!obj || $.trim(obj) === "");
    };
})(jQuery);


function webLog(duration) {
    var uri = location.pathname;
    $.ajax({
        url: "/log/duration?duration=" + duration + "&uri=" + uri,
        type: "GET",
        datatype: "json",
        async: true,
        success: function (result) {

        },
        error: function () {

        }
    });
}

/**
 * 记录页面用户访问时长
 * 页面关闭时触发
 */
(function () {
    var startTime = Math.ceil(new Date().getTime() / 1000);//单位秒
    var getDuration = function () {
        var endTime = Math.ceil(new Date().getTime() / 1000);
        return endTime - startTime;
    };

    window.onbeforeunload = function (e) {
        var duration = getDuration();
        webLog(duration);
    };
})();

/**
 * 获取用户位置
 */
function initLocationProcedure() {
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(displayAndWatch, locError, {
            enableHighAccuracy: true,
            timeout: 60000,
            maximumAge: 60000
        });
    } else {
        //alert("Your phone does not support the Geolocation API");
    }
}

function locError(error) {
    // the current position could not be located
    //alert("The current position could not be found!");
}

function displayAndWatch(position) {
    // set current position
    setUserLocation(position);
    // watch position
    //watchCurrentPosition();
}

function setUserLocation(position) {
    // marker for userLocation
    positionLog(position.coords.latitude, position.coords.longitude, position.coords.altitude, position.coords.accuracy,
        position.coords.altitudeAccuracy, position.coords.heading, position.coords.speed, position.coords.timestamp);
}

function watchCurrentPosition() {
    var positionTimer = navigator.geolocation.watchPosition(function (position) {
        positionLog(position.coords.latitude, position.coords.longitude, position.coords.altitude, position.coords.accuracy,
            position.coords.altitudeAccuracy, position.coords.heading, position.coords.speed, position.coords.timestamp);
    });
}

/**
 * 发送用户位置到后端
 * @param latitude
 * @param longitude
 * @param altitude
 * @param accuracy
 * @param altitudeAccuracy
 * @param heading
 * @param speed
 * @param timestamp
 */
function positionLog(latitude, longitude, altitude, accuracy, altitudeAccuracy, heading, speed, timestamp) {

    var uri = location.pathname;
    var dataObj = {};
    dataObj.latitude = latitude;
    dataObj.longitude = longitude;

    dataObj.altitude = altitude;
    dataObj.accuracy = accuracy;
    dataObj.altitudeAccuracy = altitudeAccuracy;
    dataObj.heading = heading;
    dataObj.speed = speed;
    dataObj.timestamp = timestamp;
    dataObj.uri = uri;

    $.ajax({
        url: "/log/position",
        type: "POST",
        contentType: "application/json;charset=utf-8",
        dataType: "json",
        data: JSON.stringify(dataObj),
        success: function (result) {
        },
        error: function () {
        }
    });
}

/**
 * 文档加载完毕启动位置定位
 */
$(document).ready(function () {
    initLocationProcedure();
    setInterval(initLocationProcedure, 60000);
    webLog(-1);
})