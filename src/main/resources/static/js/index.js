/**
 * 全文搜索
 */
function search() {
    var searchKey = $("#searchKey").val();
    var url = "/article/search?q=" + searchKey;
    window.open(url);
    return false;
}

/**
 * 正则表达式验证是否是邮箱
 */
var fChkMail = function (szMail) {
    var szReg = /^(\w-*\.*)+@(\w-?)+(\.\w{2,})+$/;
    var bChk = szReg.test(szMail);
    return bChk;
}

/**
 *  检查邮箱是否被注册
 */
$("#register-email").blur(function () {
    var email = $("#register-email").val();
    if (fChkMail(email)) {
        var dataObj = {};
        dataObj.email = email;
        $.ajax({
            url: "/user/checkEmail",
            type: "POST",
            cache: false,
            contentType: "application/json;charset=utf-8",
            datatype: "json",
            data: JSON.stringify(dataObj),
            success: function (result) {
                if (result.code == 10000) {
                    $("#div-email").addClass("has-success has-feedback");
                    $("#register-email").after("<span class='glyphicon glyphicon-ok form-control-feedback' aria-hidden='true'></span>");
                    /*
                     * 如果第一次重复了 换了一个账号之后成功了
                     * 要把第一次加的class和span取消掉
                     */
                    $("#div-email").removeClass("has-error");
                    $("#div-email span").remove(".glyphicon-remove");
                } else {
                    $("#div-email").addClass("has-error has-feedback");
                    $("#register-email").after("<span class='glyphicon glyphicon-remove form-control-feedback' aria-hidden='true'></span>");
                    $("#div-email").removeClass("has-success");
                    $("#div-email span").remove(".glyphicon-ok");
                }
            }
        });
    } else {
        $("#div-email").addClass("has-error has-feedback");
        $("#register-email").after("<span class='glyphicon glyphicon-remove form-control-feedback' aria-hidden='true'></span>");
        $("#div-email").removeClass("has-success");
        $("#div-email span").remove(".glyphicon-ok");
    }
});

/**
 *  检查昵称是否为空
 */
$("#register-nickName").blur(function () {
    var nickName = $("#register-nickName").val();
    if (nickName != undefined && nickName != "") {
        $("#div-nickName").addClass("has-success has-feedback");
        $("#register-nickName").after("<span class='glyphicon glyphicon-ok form-control-feedback' aria-hidden='true'></span>");
        /*
         * 如果第一次重复了 换了一个账号之后成功了
         * 要把第一次加的class和span取消掉
         */
        $("#div-nickName").removeClass("has-error");
        $("#div-nickName span").remove(".glyphicon-remove");
    } else {
        $("#div-nickName").addClass("has-error has-feedback");
        $("#register-nickName").after("<span class='glyphicon glyphicon-remove form-control-feedback' aria-hidden='true'></span>");
        $("#div-nickName").removeClass("has-success");
        $("#div-nickName span").remove(".glyphicon-ok");
    }
});

/**
 * 检查密码长度
 */
$("#password1").blur(function () {
    var password1 = $("#password1").val();
    if (password1 != undefined && password1.length > 5) {
        $("#div-password1").addClass("has-success has-feedback");
        $("#password1").after("<span class='glyphicon glyphicon-ok form-control-feedback' aria-hidden='true'></span>");
        /*
         * 如果第一次重复了 换了一个账号之后成功了
         * 要把第一次加的class和span取消掉
         */
        $("#div-password1").removeClass("has-error");
        $("#div-password1 span").remove(".glyphicon-remove");
    } else {
        $("#div-password1").addClass("has-error has-feedback");
        $("#password1").after("<span class='glyphicon glyphicon-remove form-control-feedback' aria-hidden='true'></span>");
        $("#div-password1").removeClass("has-success");
        $("#div-password1 span").remove(".glyphicon-ok");
    }
});

/**
 *  检查密码是否一致
 */
$("#password2").blur(function () {
    var password1 = $("#password1").val();
    var password2 = $("#password2").val();
    if (password2 != undefined && password1.length > 5 && password1 == password2) {
        $("#div-password2").addClass("has-success has-feedback");
        $("#password2").after("<span class='glyphicon glyphicon-ok form-control-feedback' aria-hidden='true'></span>");
        /*
         * 如果第一次重复了 换了一个账号之后成功了
         * 要把第一次加的class和span取消掉
         */
        $("#div-password2").removeClass("has-error");
        $("#div-password2 span").remove(".glyphicon-remove");
    } else {
        $("#div-password2").addClass("has-error has-feedback");
        $("#password2").after("<span class='glyphicon glyphicon-remove form-control-feedback' aria-hidden='true'></span>");
        $("#div-password2").removeClass("has-success");
        $("#div-password2 span").remove(".glyphicon-ok");
    }
});

/**
 * 检查注册验证码是否正确
 */
$("#register-authCode").blur(function () {
    var authCode = $("#register-authCode").val();
    if (authCode != undefined && authCode != "") {
        var dataObj = {};
        dataObj.authCode = authCode;
        $.ajax({
            url: "/checkAuthCode",
            type: "POST",
            cache: false,
            contentType: "application/json;charset=utf-8",
            datatype: "json",
            data: JSON.stringify(dataObj),
            success: function (result) {
                if (result.code == 10000) {
                    $("#div-authCode").addClass("has-success has-feedback");
                    $("#register-authCode").after("<span class='glyphicon glyphicon-ok form-control-feedback' aria-hidden='true'></span>");
                    /*
                     * 如果第一次重复了 换了一个账号之后成功了
                     * 要把第一次加的class和span取消掉
                     */
                    $("#div-authCode").removeClass("has-error");
                    $("#div-authCode span").remove(".glyphicon-remove");
                } else {
                    $("#div-authCode").addClass("has-error has-feedback");
                    $("#register-authCode").after("<span class='glyphicon glyphicon-remove form-control-feedback' aria-hidden='true'></span>");
                    $("#div-authCode").removeClass("has-success");
                    $("#div-authCode span").remove(".glyphicon-ok");
                }
            }
        });
    } else {
        $("#div-authCode").addClass("has-error has-feedback");
        $("#register-authCode").after("<span class='glyphicon glyphicon-remove form-control-feedback' aria-hidden='true'></span>");
        $("#div-authCode").removeClass("has-success");
        $("#div-authCode span").remove(".glyphicon-ok");
    }
});

/**
 *输入框失去焦点时触发登录验证码检查方法
 */
$("#login-authCode").blur(function () {
    checkloginauthCode();
});

/**
 * 检查登录验证码是否正确
 * @returns
 */
function checkloginauthCode() {
    var authCode = $("#login-authCode").val();
    if (authCode != undefined && authCode != "") {
        var dataObj = {};
        dataObj.authCode = authCode;
        $.ajax({
            url: "/checkAuthCode",
            type: "POST",
            cache: false,
            contentType: "application/json;charset=utf-8",
            datatype: "json",
            data: JSON.stringify(dataObj),
            success: function (result) {
                if (result.code == 10000) {
                    $("#div-authCode-login").addClass("has-success has-feedback");
                    $("#login-authCode").after("<span class='glyphicon glyphicon-ok form-control-feedback' aria-hidden='true'></span>");
                    /*
                     * 如果第一次重复了 换了一个账号之后成功了
                     * 要把第一次加的class和span取消掉
                     */
                    $("#div-authCode-login").removeClass("has-error");
                    $("#div-authCode-login span").remove(".glyphicon-remove");
                } else if (result.code == 50101) {
                    $("#div-authCode-login").addClass("has-error has-feedback");
                    $("#login-authCode").after("<span class='glyphicon glyphicon-remove form-control-feedback' aria-hidden='true'></span>");
                    $("#div-authCode-login").removeClass("has-success");
                    $("#div-authCode-login span").remove(".glyphicon-ok");
                } else {
                    console.error("")
                }
            }
        });
    } else {
        $("#div-authCode-login").addClass("has-error has-feedback");
        $("#login-authCode").after("<span class='glyphicon glyphicon-remove form-control-feedback' aria-hidden='true'></span>");
        $("#div-authCode-login").removeClass("has-success");
        $("#div-authCode-login span").remove(".glyphicon-ok");
    }
}

/**
 * 提交注册表单
 * @returns
 */
function register() {
    var email_class = $("#div-email span").attr("class");
    if (email_class.indexOf("glyphicon-remove") > 0) {
        alert("邮箱已注册");
        return false;
    }
    var nickName_class = $("#div-nickName span").attr("class");
    if (nickName_class.indexOf("glyphicon-remove") > 0) {
        alert("昵称不能为空");
        return false;
    }
    var password2_class = $("#div-password2 span").attr("class");
    if (password2_class.indexOf("glyphicon-remove") > 0) {
        alert("两次输入的密码不一致");
        return false;
    }
    var authCode_class = $("#div-authCode span").attr("class");
    if (authCode_class.indexOf("glyphicon-remove") > 0) {
        alert("验证码不正确");
        return false;
    }
    var email = $("#register-email").val();
    var nickName = $("#register-nickName").val();
    var password = $("#password1").val();
    var authCode = $("#register-authCode").val();
    var dataObj = {};
    dataObj.email = email;
    dataObj.name = nickName;
    dataObj.password = password;
    dataObj.authCode = authCode;
    showReflesh();//显示缓冲图标
    $.ajax({
        url: "/user/register",
        type: "post",
        contentType: "application/json;charset=utf-8",
        datatype: "json",
        data: JSON.stringify(dataObj),
        success: function (result) {
            removeReflesh();//移除缓冲图标
            if (result.code == 10000) {
                $.MsgBox.Confirm("提示", "已发送一封激活邮件到您的邮箱，请打开邮箱确认！", function () {
                    $('#register').modal('hide');
                });
            } else {
                $.MsgBox.Confirm("提示", result.message, function () {
                    //$("#img-authCode-register").click();
                    console.log("注册异常");
                });
            }
        }
    });
}

/**
 * 提交登录表单
 * @returns
 */
function login() {
    var authCode_class = $("#div-authCode-login span").attr("class");
    if (authCode_class.indexOf("glyphicon-remove") > 0) {
        //alert("验证码不正确");
        return false;
    }
    var email = $("#login-email").val();
    var password = $("#login-password").val();
    var authCode = $("#login-authCode").val();
    var dataObj = {};
    dataObj.email = email;
    dataObj.password = password;
    dataObj.authCode = authCode;
    showReflesh();//显示缓冲图标
    $.ajax({
        url: "/user/login",
        type: "post",
        contentType: "application/json;charset=utf-8",
        datatype: "json",
        data: JSON.stringify(dataObj),
        success: function (result) {
            removeReflesh();//移除缓冲图标
            if (result.code == 10000) {
                $('#login').modal('hide');
                refreshPcrimg("checkLogin");
            } else {
                $.MsgBox.Confirm("提示", result.message, function () {
                    console.log("登录失败")
                });
            }
        }
    });
}

/**
 * 监听 enter事件，按下enter触发登录事件

 document.onkeydown = function(e) {
	var ev = document.all ? window.event : e;
	if (ev.keyCode == 13) {
		login();
	}
}*/

/**
 * 退出登录
 * @returns
 */
function logout() {
    var dataObj = {};
    $.ajax({
        url: "/user/logout",
        type: "post",
        contentType: "application/json;charset=utf-8",
        datatype: "json",
        data: JSON.stringify(dataObj),
        success: function (result) {
            if (result.code == 10000) {
                location.reload(true);//刷新当前页面
            } else {
                $.MsgBox.Confirm("提示", result.message, function () {
                    console.log("退出失败")
                });
            }
        }
    });
}

/**
 * 刷新验证码
 * @param callback
 */
function refreshPcrimg(callback) {
    //var url = '/pcrimg?d=' + Math.random();
    var url = '/pcrimg';
    $.get(url, function (result) {
        console.log("刷新验证码！")
        $("#img-authCode-login").attr("src", url);
        $("#img-authCode-register").attr("src", url);

        if ($.isBlank(callback)) {
        } else {
            window[callback].call(this);
        }

    });
}

/**
 * 检测用户是否已经登录
 * @returns
 */
function checkLogin() {

    $.ajax({
        url: "/user/checkLogin",
        type: "GET",
        datatype: "json",
        async: true,
        success: function (result) {
            var code = result.code;

            if ((code == 40101) || (code == 40102)) {
                /*客户端或服务端缺失session，调用生成session的接口：同验证码生成接口*/
                console.log("session缺失");
                refreshPcrimg();
            } else if (code == 10000) {
                //获取cookie中的用户id
                var user = result.data;
                var ulHtml = '';
                ulHtml += '<li class="dropdown"><a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"> <img id="sm-name" src="';
                if ($.isBlank(user.headImg)) {
                    ulHtml += '/static/img/default_headImg.jpg"';
                } else {
                    ulHtml += user.headImg;
                }
                ulHtml += 'class="center-block img-circle" alt="图片无效"></a><ul class="dropdown-menu"><li><a href="';
                ulHtml += '/user/center/' + user.id + '" target="_blank">';
                ulHtml += user.name + '<span class="glyphicon glyphicon-cog"></span></a> <a onclick="logout()"> 退出登录 <span class="glyphicon glyphicon-off"></span></a></li></ul></li>';
                /**ulHtml += '<li> <a class="btn btn-default" href="/download/zrzhen2.0.0.apk" download="zrzhen_Android_2.0.0.apk" id="contact_weixin">下载APP</a></li>';*/

                $('#userCenterUl').html(ulHtml);

            } else if (code == 40103) {
                console.log("未登录")
            } else {
                console.log("检查登录异常")
            }
        }
    });
}


/**
 * 文章提交按钮
 *
 * @returns
 */
function addArticle() {
    var title = $("#title").val();
    var content = $("#content").val();
    var status = $("input[name=article-status]:checked").val();

    if (title == "") {
        $.MsgBox.Confirm("提示", "标题不能为空！", function () {
        });
        return false;
    }
    if ($("#content").val() == "") {
        $.MsgBox.Confirm("提示", "内容不能为空！", function () {
        });
        return false;
    }
    var v = content.replace(/\r/g, '');
    if (v != '') {
        v = '<p>' + v.replace(/\n*$/g, '').replace(/\n/g, '</p><p>') + '</p>';
        var dataObj = {};
        dataObj.title = title;
        dataObj.content = v;
        dataObj.status = status;
        showReflesh();
        $.ajax({
            url: "/article/add",
            type: "POST",
            contentType: "application/json;charset=utf-8",
            dataType: "json",
            data: JSON.stringify(dataObj),
            success: function (result) {
                removeReflesh();//移除缓冲图标
                if (result.code == 10000) {
                    $.MsgBox.Confirm("提示", "发布成功！", function () {
                    });
                    toSearch(null)
                } else {
                    $.MsgBox.Confirm("提示", result.message, function () {
                    });
                }
            },
            error: function () {
                removeReflesh();
                $.MsgBox.Confirm("提示", "系统异常，稍后重试", function () {
                });
            }
        });
    }
}


/**
 * lucene中查询分页
 * @param pageIndex
 * @param tag
 * @returns
 */
function questionListFromLucene(pageIndex, tag) {
    $.ajax({
        url: "/getQuestionListFromLucene?pageIndex=" + pageIndex + "&tag=" + tag,
        type: "GET",//查询数据，用get方式
        contentType: "application/json;charset=utf-8",
        datatype: "json",
        async: true,
        success: function (result) {
            $('#pagetable').empty();
            $('#pageNav').empty();
            var pagehtml = "";
            var pageNav = result.respData.pageNav;

            var questions = result.respData.questions;
            for (var i = 0; i < questions.length; i++) {
                pagehtml += '<a href="' + '/question/' + questions[i].id + '" target="_blank" style="font-weight:bold;">' + questions[i].title + '</a>';
                pagehtml += '<div style="float:right;font-size:12px;color:#888888;">' + questions[i].createdTimeStr + '</div>';
                pagehtml += '<hr>';
            }

            $('#pagetable').append(pagehtml);
            $('#pageNav').append(pageNav);
        }
    });
}

/**
 * 获取文章列表
 */
function tableList(tag, dom, pageNum, pageSide) {
    showReflesh();
    try {
        var url = "/article/page" + "?pageNum=" + pageNum + "&pageSide=" + pageSide + "&tag=" + tag;
        $.ajax({
            url: url,
            type: "GET",
            datatype: "json",
            success: function (result) {
                try {
                    showDataDiv(result, dom)
                } catch (err) {
                } finally {
                    removeReflesh();
                }
            },
            error: function () {
                removeReflesh();
                $.MsgBox.Confirm("提示", "系统繁忙，请重试", function () {
                });
            }
        });
    } catch (err) {
    }
}

/**
 * 展示文章列表
 * @param result
 * @param dom
 */
function showDataDiv(result, dom) {
    var data = result.data.data;
    var start = result.data.pageNum;
    var row = result.data.pageSide;

    var html = '';

    for (var i = 0; i < data.length; i++) {
        var article = data[i]
        var id = article.id;
        var title = article.title;
        var ctime = new Date(article.ctime).Format("yyyy-MM-dd");
        var user = article.username;
        var index = (start - 1) * row + parseInt(i) + 1;

        html += '<a href="' + '/article/detail/' + id + '" target="_blank" style="font-weight:bold;">' + title + '</a>';
        html += '<div style="float:right;font-size:12px;color:#888888;">' + ctime + '</div>';
        html += '<hr>';
    }
    $("#" + dom).html(html);
}


/**
 * 分页查询
 * @param e 点击的分页元素
 */
function toSearch(e) {
    showReflesh();
    try {
        var pageNum = 1;//所选页序,默认为1
        var eo = $(e);//js对象转jquery对象
        var pageNumS = eo.html();
        if (null != e) {
            pageNum = parseInt(pageNumS);
        }

        var activeObj = $(".active");//活跃页序对象
        var active = 1;
        if (undefined != activeObj) {
            active = parseInt(activeObj.html()); //活跃页序
            activeObj.removeClass("active");//清除活跃页序
        }

        if (null == e) {
            var foot = '<ul class="pagination"><li><a href="#" onclick="toSearch(this);return false;">«</a></li>' +
                '<li><a class ="active" href="#" onclick="toSearch(this);return false;">' + pageNum + '</a></li>' +
                '<li><a href="#" onclick="toSearch(this);return false;">' + (pageNum + 1) + '</a></li>' +
                '<li><a href="#" onclick="toSearch(this);return false;">' + (pageNum + 2) + '</a></li>' +
                '<li><a href="#" onclick="toSearch(this);return false;">' + (pageNum + 3) + '</a></li>' +
                '<li><a href="#" onclick="toSearch(this);return false;">' + (pageNum + 4) + '</a></li>' +
                '<li><a href="#" onclick="toSearch(this);return false;">»</a></li></ul>';
            $("#foot").html(foot);
        } else if ("«" == pageNumS) {
            pageNum = active - 1;
            if (pageNum > 5) {
                var foot = '<ul class="pagination"><li><a href="#" onclick="toSearch(this);return false;">«</a></li>' +
                    '<li><a href="#" onclick="toSearch(this);return false;">' + (pageNum - 4) + '</a></li>' +
                    '<li><a href="#" onclick="toSearch(this);return false;">' + (pageNum - 3) + '</a></li>' +
                    '<li><a href="#" onclick="toSearch(this);return false;">' + (pageNum - 2) + '</a></li>' +
                    '<li><a href="#" onclick="toSearch(this);return false;">' + (pageNum - 1) + '</a></li>' +
                    '<li><a class ="active" href="#" onclick="toSearch(this);return false;">' + pageNum + '</a></li>' +
                    '<li><a href="#" onclick="toSearch(this);return false;">»</a></li></ul>';
                $("#foot").html(foot);
            } else {
                var foot = '<ul class="pagination"><li><a href="#" onclick="toSearch(this);return false;">«</a></li>' +
                    '<li><a';
                if (2 > pageNum) {
                    foot += ' class ="active"';
                    pageNum = 1;
                }
                foot += ' href="#" onclick="toSearch(this);return false;">' + 1 + '</a></li>' +
                    '<li><a';
                if (2 == pageNum) {
                    foot += ' class ="active"';
                }
                foot += ' href="#" onclick="toSearch(this);return false;">' + 2 + '</a></li>' +
                    '<li><a';
                if (3 == pageNum) {
                    foot += ' class ="active"';
                }
                foot += ' href="#" onclick="toSearch(this);return false;">' + 3 + '</a></li>' +
                    '<li><a';
                if (4 == pageNum) {
                    foot += ' class ="active"';
                }
                foot += ' href="#" onclick="toSearch(this);return false;">' + 4 + '</a></li>' +
                    '<li><a';
                if (5 == pageNum) {
                    foot += ' class ="active"';
                }
                foot += ' href="#" onclick="toSearch(this);return false;">' + 5 + '</a></li>' +
                    '<li><a href="#" onclick="toSearch(this);return false;">»</a></li></ul>';
                $("#foot").html(foot);
            }
        } else if ("»" == pageNumS) {
            pageNum = active + 1;
            var foot = '<ul class="pagination"><li><a href="#" onclick="toSearch(this);return false;">«</a></li>' +
                '<li><a class ="active" href="#" onclick="toSearch(this);return false;">' + pageNum + '</a></li>' +
                '<li><a href="#" onclick="toSearch(this);return false;">' + (pageNum + 1) + '</a></li>' +
                '<li><a href="#" onclick="toSearch(this);return false;">' + (pageNum + 2) + '</a></li>' +
                '<li><a href="#" onclick="toSearch(this);return false;">' + (pageNum + 3) + '</a></li>' +
                '<li><a href="#" onclick="toSearch(this);return false;">' + (pageNum + 4) + '</a></li>' +
                '<li><a href="#" onclick="toSearch(this);return false;">»</a></li></ul>';
            $("#foot").html(foot);
        } else {
            eo.addClass("active");//赋予所选项为活跃页序
        }

        var dom = "tableList"
        var tag = $(".activeTag").html();

        if ($.isBlank(tag) || "全部" == tag) {
            tag = '';
        }

        tableList(tag, dom, pageNum, 20);

    } catch (err) {

    } finally {
        removeReflesh();
    }
}

/**
 * 点击标签
 * @param e
 */
function changeTag(e) {

    $(".tagList").each(function () {
        $(this).removeClass("activeTag");
    });
    $(e).addClass("activeTag")

    toSearch(null);
}

/**
 *获取标签列表
 */
function allTags() {
    try {
        var url = "/tag/all";
        $.ajax({
            url: url,
            type: "GET",
            datatype: "json",
            success: function (result) {
                if (result.code == 10000) {
                    var data = result.data;
                    var html = '<button class="button tagList" id="id全部" onclick="changeTag(this);"style="height: 30px; font-size: 14px;">全部</button>'

                    for (var i in data) {
                        var tag = data[i]

                        html += '<button class="button tagList" id="id' + tag + '" onclick="changeTag(this);"style="height: 30px; font-size: 14px;">' +
                            tag + '</button>'
                    }

                    $("#tagList").html(html);

                } else {
                    $.MsgBox.Confirm("提示", result.message, function () {
                    });
                }
            },
            error: function () {
                $.MsgBox.Confirm("提示", "系统繁忙，请重试", function () {
                });
            }
        });
    } catch (err) {
    } finally {
    }
}

/**
 * 获取网书列表
 */
function bookList() {
    try {
        var url = "/book/infoList";
        $.ajax({
            url: url,
            type: "GET",
            datatype: "json",
            success: function (result) {
                if (result.code == 10000) {
                    var data = result.data;
                    var html = ''
                    for (var i in data) {
                        var book = data[i]
                        html += '<a href="/book/' + book.id + '" target="_blank" style="font-size: 20px">《' + book.name
                            + '》</a>&emsp;<a href="/user/center/' + book.creatorId
                            + '" target="_blank" style="font-size: 20px">'+book.creator + '</a><p style="font-size: 16px;background: #F8F8FF">'
                            + book.description + "</p>";
                    }

                    $("#bookList").html(html);

                } else {
                    $.MsgBox.Confirm("提示", result.message, function () {
                    });
                }
            },
            error: function () {
                $.MsgBox.Confirm("提示", "系统繁忙，请重试", function () {
                });
            }
        });
    } catch (err) {
    } finally {
    }
}

/**
 * 弹出新增网书表单
 */
function showAddBookModal() {
    $.ajax({
        url: "/user/checkLogin",
        type: "GET",
        datatype: "json",
        async: true,
        success: function (result) {
            var code = result.code;
            if (code == 10000) {
                $('#addBookModal').modal('show')
            } else {
                $.MsgBox.Confirm("提示", "请先登录！", function () {
                });
            }
        }
    });


}

/**
 * 新增网书
 * @returns {boolean}
 */
function addBook() {
    var name = $("#book-name").val();
    var description = $("#book-description").val();

    if ($.isBlank(name)) {
        $.MsgBox.Confirm("提示", "错误：name 为空！", function () {
        });
        return false;
    }

    if ($.isBlank(description)) {
        $.MsgBox.Confirm("提示", "错误：description 为空！", function () {
        });
        return false;
    }

    var dataObj = {};
    dataObj.name = name;
    dataObj.description = description;
    showReflesh();
    $.ajax({
        url: "book/add",
        type: "POST",
        contentType: "application/json;charset=utf-8",
        dataType: "json",
        data: JSON.stringify(dataObj),
        success: function (result) {
            removeReflesh();
            if (result.code == 10000) {
                $.MsgBox.Confirm("提示", "新增成功！", function () {

                });
                bookList();
            } else {
                $.MsgBox.Confirm("提示", result.message, function () {
                });
            }
            $('#addBookModal').modal('hide')
        },
        error: function () {
            removeReflesh();
            $('#addBookModal').modal('hide')
            $.MsgBox.Confirm("提示", "系统异常，稍后重试", function () {
            });
        }
    });
}

/**
 * 文档加载完毕即执行
 */
$(function () {
    //设置提问框高度自动增加
    var text = document.getElementById("content");
    autoTextarea(text);

    refreshPcrimg("checkLogin");
    allTags();
    toSearch(null);
    bookList();
});

