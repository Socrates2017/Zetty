var isCreator = false;

/**
 * 获取内容
 * @param article
 */
function getContent(url) {
    $('#content').attr('src', url);
    changeFrameHeight();
    var html = '<a href="' + url + '" target="_blank" ><span class="glyphicon glyphicon-share"></span></a>'
    $('#new-window-content').html(html);
}

/**
 * iframe高度自适应
 */
function changeFrameHeight() {
    var ifm = document.getElementById("content");
    ifm.height = document.documentElement.clientHeight - 56;
    $("#left").height(document.documentElement.clientHeight);
}

window.onresize = function () {
    changeFrameHeight();
}


/**
 * 隐藏章节目录
 * @param id
 */
function hideIndex(id) {
    $("#index_" + id).html("");
    var showHtml = '<a href="javascript:void(0);" onclick="getIndex(' + id + ')"><span class="glyphicon glyphicon-chevron-right"></span></a>'
    $("#show-" + id).html(showHtml);
}

/**
 * 删除章节
 * @returns {boolean}
 */
function deleteIndex() {
    var deleteIndexParentId = $("#deleteIndexParentId").val();
    var deleteIndexId = $("#deleteIndexId").val();
    var deleteIndexParentTitle = $("#deleteIndexParentTitle").val().trim();

    var bookId = $("#bookId").val();
    if ($.isBlank(bookId)) {
        $.MsgBox.Confirm("提示", "错误：bookId 为空！", function () {
        });
        return false;
    }

    if ($.isBlank(deleteIndexId)) {

        return false;
    }
    if ($.isBlank(deleteIndexParentTitle)) {
        return false;
    }

    var deleteDom = $("#name-" + deleteIndexId)
    var title = deleteDom.text();

    if (title != deleteIndexParentTitle) {
        $.MsgBox.Confirm("提示", "输入标题不正确！", function () {
        });
        return;
    }

    var dataObj = {};
    dataObj.id = deleteIndexId;
    dataObj.bookId = bookId;
    showReflesh();
    $.ajax({
        url: "/bookIndex/delete",
        type: "POST",
        contentType: "application/json;charset=utf-8",
        dataType: "json",
        data: JSON.stringify(dataObj),
        success: function (result) {
            removeReflesh();
            if (result.code == 10000) {
                $.MsgBox.Confirm("提示", "删除成功！", function () {
                    getIndex(deleteIndexParentId);
                });

            } else {
                $.MsgBox.Confirm("提示", result.message + ": " + result.data, function () {
                });
            }
            $('#deleteIndexModal').modal('hide')
        },
        error: function () {
            removeReflesh();
            $('#deleteIndexModal').modal('hide')
            $.MsgBox.Confirm("提示", "系统异常，稍后重试", function () {
            });
        }
    });
}

/**
 * 删除书籍
 * @returns {boolean}
 */
function deleteBook() {

    var bookId = $("#bookId").val();
    if ($.isBlank(bookId)) {
        $.MsgBox.Confirm("提示", "错误：bookId 为空！", function () {
        });
        return;
    }

    var deleteBookName = $("#deleteBookName").val().trim();
    if ($.isBlank(deleteBookName)) {
        $.MsgBox.Confirm("提示", "请输入要删除的书名！", function () {
        });
        return false;
    }

    var deleteDom = $("#bookName")
    var bookName = deleteDom.text();

    if (bookName != bookName) {
        $.MsgBox.Confirm("提示", "输入书名不正确！", function () {
        });
        return false;
    }

    var dataObj = {};
    dataObj.bookId = bookId;
    showReflesh();
    $.ajax({
        url: "/book/delete",
        type: "POST",
        contentType: "application/json;charset=utf-8",
        dataType: "json",
        data: JSON.stringify(dataObj),
        success: function (result) {
            removeReflesh();
            if (result.code == 10000) {
                $.MsgBox.Confirm("提示", "删除成功！", function () {
                    location.href = "/"
                });

            } else {
                $.MsgBox.Confirm("提示", result.message + ": " + result.data, function () {
                });
            }
            $('#deleteIndexModal').modal('hide')
        },
        error: function () {
            removeReflesh();
            $('#deleteIndexModal').modal('hide')
            $.MsgBox.Confirm("提示", "系统异常，稍后重试", function () {
            });
        }
    });
}


/**
 * 新增章节
 * @returns {boolean}
 */
function addIndex() {
    var parentId = $("#parentId").val();
    var url = $("#url").val();
    var childTitle = $("#childTitle").val();
    var bookId = $("#bookId").val();
    var indexOrder = $("#add-index-order").val();

    if ($.isBlank(indexOrder)) {
        indexOrder = 0;
    }

    if ($.isBlank(bookId)) {
        $.MsgBox.Confirm("提示", "错误：bookId 为空！", function () {
        });
        return false;
    }

    if ($.isBlank(parentId)) {
        $.MsgBox.Confirm("提示", "错误：parentId 为空！", function () {
        });
        return false;
    }
    if ($.isBlank(url)) {
        $.MsgBox.Confirm("提示", "错误：url 为空！", function () {
        });
        return false;
    }
    if ($.isBlank(childTitle)) {
        $.MsgBox.Confirm("提示", "错误：name 为空！", function () {
        });
        return false;
    }

    var dataObj = {};
    dataObj.parentId = parentId;
    dataObj.url = url;
    dataObj.childTitle = childTitle;
    dataObj.bookId = bookId;
    dataObj.indexOrder = indexOrder;
    showReflesh();
    $.ajax({
        url: "/bookIndex/add",
        type: "POST",
        contentType: "application/json;charset=utf-8",
        dataType: "json",
        data: JSON.stringify(dataObj),
        success: function (result) {
            removeReflesh();
            if (result.code == 10000) {
                $.MsgBox.Confirm("提示", "新增成功！", function () {
                    getIndex(parentId);
                });

            } else {
                $.MsgBox.Confirm("提示", result.message, function () {
                });
            }
            $('#addIndexModal').modal('hide')
        },
        error: function () {
            removeReflesh();
            $('#addIndexModal').modal('hide')
            $.MsgBox.Confirm("提示", "系统异常，稍后重试", function () {
            });
        }
    });
}

/**
 * 更新
 * @returns {boolean}
 */
function updateIndex() {
    var pid = $("#update-pid").val();
    var id = $("#updateIndexId").val();
    var url = $("#updateIndexModalUrl").val();
    var name = $("#updateIndexModalTitle").val();
    var indexOrder = $("#update-index-order").val();

    if ($.isBlank(indexOrder)) {
        indexOrder = 0;
    }

    var bookId = $("#bookId").val();
    if ($.isBlank(bookId)) {
        $.MsgBox.Confirm("提示", "错误：bookId 为空！", function () {
        });
        return false;
    }

    if ($.isBlank(id)) {
        return false;
    }
    if ($.isBlank(url)) {
        return false;
    }
    if ($.isBlank(name)) {
        return false;
    }

    var dataObj = {};
    dataObj.id = id;
    dataObj.url = url;
    dataObj.name = name;
    dataObj.bookId = bookId;
    dataObj.indexOrder = indexOrder;
    showReflesh();
    $.ajax({
        url: "/bookIndex/update",
        type: "POST",
        contentType: "application/json;charset=utf-8",
        dataType: "json",
        data: JSON.stringify(dataObj),
        success: function (result) {
            removeReflesh();
            if (result.code == 10000) {
                $.MsgBox.Confirm("提示", "修改成功！", function () {
                    //$("#name-" + parentId).html(name)
                });
                getIndex(pid);
            } else {
                $.MsgBox.Confirm("提示", result.message, function () {
                });
            }
            $('#updateIndexModal').modal('hide')
        },
        error: function () {
            removeReflesh();
            $('#updateIndexModal').modal('hide')
            $.MsgBox.Confirm("提示", "系统异常，稍后重试", function () {
            });
        }
    });
}

/**
 * 更新
 * @returns {boolean}
 */
function updateIndexPid(pid, id, soucePid) {
    pid = parseInt(pid)
    id = parseInt(id)
    var bookId = $("#bookId").val();
    if ($.isBlank(bookId)) {
        $.MsgBox.Confirm("提示", "错误：bookId 为空！", function () {
        });
        return false;
    }

    if (isNaN(pid)) {
        $.MsgBox.Confirm("提示", "错误：pid 为空，请拖放到标题上！", function () {
        });
        return false;
    }
    if (isNaN(id)) {
        $.MsgBox.Confirm("提示", "错误：id 为空，请按住标题左边的空白处开始拖动", function () {
        });
        return false;
    }


    var dataObj = {};
    dataObj.id = id;
    dataObj.pid = pid;
    dataObj.bookId = bookId;

    showReflesh();
    $.ajax({
        url: "/bookIndex/updatePid",
        type: "POST",
        contentType: "application/json;charset=utf-8",
        dataType: "json",
        data: JSON.stringify(dataObj),
        success: function (result) {
            removeReflesh();
            if (result.code == 10000) {
                $.MsgBox.Confirm("提示", "修改成功！", function () {
                    //$("#name-" + parentId).html(name)

                });
                getIndex(pid);
                getIndex(soucePid);

            } else {
                $.MsgBox.Confirm("提示", result.message, function () {
                });
            }
            $('#updateIndexModal').modal('hide')
        },
        error: function () {
            removeReflesh();
            $('#updateIndexModal').modal('hide')
            $.MsgBox.Confirm("提示", "系统异常，稍后重试", function () {
            });
        }
    });
}

/**
 * 更新
 * @returns {boolean}
 */
function updateBook() {
    var updateBookName = $("#updateBookName").val();
    var updateBookDescription = $("#updateBookDescription").val();

    var bookId = $("#bookId").val();
    if ($.isBlank(bookId)) {
        $.MsgBox.Confirm("提示", "错误：bookId 为空！", function () {
        });
        return false;
    }

    if ($.isBlank(updateBookName)) {
        if ($.isBlank(bookId)) {
            $.MsgBox.Confirm("提示", "错误：书名不能为空！", function () {
            });
            return false;
        }
    }
    if ($.isBlank(updateBookDescription)) {
        if ($.isBlank(bookId)) {
            $.MsgBox.Confirm("提示", "错误：摘要不能为空！", function () {
            });
            return false;
        }
    }

    var dataObj = {};
    dataObj.bookId = bookId;
    dataObj.name = updateBookName;
    dataObj.description = updateBookDescription;
    showReflesh();
    $.ajax({
        url: "/book/update",
        type: "POST",
        contentType: "application/json;charset=utf-8",
        dataType: "json",
        data: JSON.stringify(dataObj),
        success: function (result) {
            removeReflesh();
            if (result.code == 10000) {
                $.MsgBox.Confirm("提示", "修改成功！", function () {
                    getBookInfo();
                });

            } else {
                $.MsgBox.Confirm("提示", result.message, function () {
                });
            }
            $('#updateBookModal').modal('hide')
        },
        error: function () {
            removeReflesh();
            $('#updateBookModal').modal('hide')
            $.MsgBox.Confirm("提示", "系统异常，稍后重试", function () {
            });
        }
    });
}

/**
 * 弹出新增目录的模态框
 * @param id
 */
function showAddIndexModal(id) {
    $("#parentId").val(id);
    $('#addIndexModal').modal('show')
}

/**
 * 弹出修改目录模态框
 * @param id
 * @param name
 * @param url
 */
function showUpdateIndexModal(pid, id, name, url, order) {
    $("#update-pid").val(pid);
    $("#updateIndexId").val(id);
    $("#updateIndexModalTitle").val(name);
    $("#updateIndexModalUrl").val(url);
    $("#update-index-order").val(order);
    $('#updateIndexModal').modal('show')
}

/**
 * 弹出删除目录的模态框
 * @param parentId
 * @param id
 */
function showDeleteIndexModal(parentId, id) {
    $("#deleteIndexParentId").val(parentId)
    $("#deleteIndexId").val(id)
    $('#deleteIndexModal').modal('show')
}

/**
 * 弹出删除书本模态框
 */
function showDeleteBookModal() {
    $('#deleteBookModal').modal('show')
}

/**
 * 弹出更新书本模态框
 */
function showUpdateBookModal() {
    $("#updateBookName").val($("#bookName").text());
    $("#updateBookDescription").val($("#bookDescription").text());
    $('#updateBookModal').modal('show')
}

/**
 *
 * @param ev
 */
function drag(ev) {
    var sourceId = ev.target.id;
    var sourcePid = ev.target.parentNode.id;

    ev.dataTransfer.setData("Text", sourceId + "#" + sourcePid);
}

function allowDrop(ev) {
    ev.preventDefault();
}

function drop(ev) {
    ev.preventDefault();
    var li = ev.target.parentNode.parentNode;
    var pid = li.id;

    if (pid.substr(0, 3) == "li-") {
        pid = pid.substr(3);
    } else if (pid.substr(0, 6) == "index_") {
        pid = pid.substr(6);
    }
    var data = ev.dataTransfer.getData("Text").split("#");
    var souceId = data[0].substr(3);
    var soucePid = data[1].substr(3);
    updateIndexPid(pid, souceId, soucePid);
}

/**
 * 获取节点目录并局部刷新
 * @param id
 */
function getIndex(id) {

    var bookId = $("#bookId").val();
    if ($.isBlank(bookId)) {
        $.MsgBox.Confirm("提示", "错误：bookId 为空！", function () {
        });
        return;
    }

    $.ajax({
        url: "/bookIndex/children?book=" + bookId + "&parent=" + id,
        type: "GET",
        datatype: "json",
        async: true,
        success: function (result) {
            var code = result.code;

            if ((code == 10000) || (code == 40102)) {
                var data = result.data;
                var html = '<ul class="" id="ul-' + id + '" >';
                for (var i in data) {
                    var bookIndex = data[i];

                    html += '<li id="li-' + bookIndex.id + '" class="" draggable="true" ondragstart="drag(event)" ondrop="drop(event)" ondragover="allowDrop(event)" style="margin-top:20px;"><div style="display: inline" id="show-' + bookIndex.id
                        + '"><a href="javascript:void(0);" onclick="getIndex(' + bookIndex.id +
                        ')"><span class="glyphicon glyphicon-chevron-right"></span></a></div>&emsp;<a href="javascript:void(0);" onclick="getContent(\'' + bookIndex.url + '\')"><span id="name-' + bookIndex.id + '">' + bookIndex.name +
                        '</span></a>';
                    if (isCreator) {
                        html += '&emsp;&emsp;<div style="display: inline" id="opear-' + bookIndex.id + '"><a href="javascript:void(0);" onclick="showUpdateIndexModal(' + id + "," + bookIndex.id + ",\'" + bookIndex.name + "\',\'" + bookIndex.url + '\',' + bookIndex.indexOrder
                            + ')"><span class="glyphicon glyphicon-pencil"></span></a><a href="javascript:void(0);" onclick="showAddIndexModal(' + bookIndex.id
                            + ')"><span class="glyphicon glyphicon-plus-sign"></span></a><a href="javascript:void(0);" onclick="showDeleteIndexModal(' + id + "," + bookIndex.id +
                            ')"><span class="glyphicon glyphicon-minus-sign"></span></a></div></li> ';
                    }
                    html += '<div id="index_' + bookIndex.id + '" style="padding-left:20px;" ></div>';
                }
                html += '</ul>'
                $("#index_" + id).html(html);

                var showHtml = '<a href="javascript:void(0);" onclick="hideIndex(' + id + ')"><span class="glyphicon glyphicon-chevron-down"></span></a>'
                $("#show-" + id).html(showHtml);

            } else {
                console.log(result.message)
            }
        }
    });
}

/**
 * 检测当前用户是否为书籍作者
 * @returns
 */
function checkUser() {

    var bookId = $("#bookId").val();
    if ($.isBlank(bookId)) {
        $.MsgBox.Confirm("提示", "错误：bookId 为空！", function () {
        });
        return;
    }

    $.ajax({
        url: "/book/checkUser?bookId=" + bookId,
        type: "GET",
        datatype: "json",
        async: true,
        success: function (result) {
            var code = result.code;
            if (code == 10000) {
                $('.user-show').show();
                isCreator = true;
            } else {
                $('.user-show').hide();
            }
        }
    });
}

function getBookInfo() {

    var bookId = $("#bookId").val();
    if ($.isBlank(bookId)) {
        $.MsgBox.Confirm("提示", "错误：bookId 为空！", function () {
        });
        return;
    }

    $.ajax({
        url: "/book/info/" + bookId,
        type: "GET",
        datatype: "json",
        async: true,
        success: function (result) {
            var code = result.code;
            if (code == 10000) {
                var data = result.data;
                $("#bookName").html(data.name);
                $("#bookCreator").html(data.creator);
                $("#bookDescription").html(data.description);
            } else {

            }
        }
    });
}

$(function () {
    $("#left").height(document.documentElement.clientHeight);
    var uri = window.location.pathname;
    var bookId = uri.substr(6);
    $("#bookId").val(bookId);
    getBookInfo();
    checkUser();
    getIndex(0);
});