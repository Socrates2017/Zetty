/**
 * 获取文章列表
 */
function tableList(tag, dom, pageNum, pageSide) {
    showReflesh();
    try {
        var userId = $("#user-id").val();
        var url;
        if (tag == "未公开文章") {
            url = "/article/private" + "?pageNum=" + pageNum + "&pageSide=" + pageSide + "&userId=" + userId;
        } else {
            url = "/article/page" + "?pageNum=" + pageNum + "&pageSide=" + pageSide + "&tag=" + tag + "&userId=" + userId;
        }

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
        var userId = $("#user-id").val();
        var url = "/tag/all?userId=" + userId;
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
        var userId = $("#user-id").val();
        var url = "/book/infoList?userId=" + userId;
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
                        html += '<a href="/book/' + book.id + '" target="_blank" style="font-size: 20px">《' + book.name + '》</a>&emsp;<span style="font-size: 20px">' + book.creator + '</span><p style="font-size: 16px;background: #F8F8FF">' +
                            book.description + "</p>";
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
 * 检测当前用户是否为书籍作者
 * @param callback 回调函数，获取首层目录
 */
function getUserInfo() {
    var userId = $("#user-id").val();
    if ($.isBlank(userId)) {
        return;
    }

    $.ajax({
        url: "/user/info/" + userId,
        type: "GET",
        datatype: "json",
        async: true,
        success: function (result) {
            var code = result.code;
            if (code == 10000) {
                var map = result.data;
                $("#user-name").text(map.userName);
                document.title = map.userName;
                if (map.isUserSelf) {
                    $("#article-private").show();
                }

            }
        },
        error: function () {
        }
    });
}


/**
 * 文档加载完毕即执行
 */
$(function () {
    var uri = window.location.pathname;
    var userId = uri.substr(13);
    $("#user-id").val(userId);
    getUserInfo();
    allTags();
    toSearch(null);
    bookList();
});

