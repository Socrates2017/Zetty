var isCreator = false;

/**
 * 访问url并写到frame中
 * @param url
 */
function getContent(url) {
    $('#content').attr('src', url);
    changeFrameHeight();
    var html = '<a href="' + url + '" target="_blank" ><span class="glyphicon glyphicon-share"></span></a>'
    $('#new-window-content').html(html);
}

/**
 * iframe高度自适应
 * 左边目录栏高度自适应
 */
function changeFrameHeight() {
    var ifm = document.getElementById("content");
    ifm.height = document.documentElement.clientHeight - 30;
    $("#left-scroll").height(document.documentElement.clientHeight - 30);
}

/**
 * 窗口高度改变时触发
 */
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
        $.MsgBox.Confirm("提示", "错误：要删除的目录Id为空！", function () {
        });
        return false;
    }
    if ($.isBlank(deleteIndexParentTitle)) {
        $.MsgBox.Confirm("提示", "请输入要删除的章节目录名字！", function () {
        });
        return false;
    }

    var deleteDom = $("#name-" + deleteIndexId)
    var title = deleteDom.text();

    if (title != deleteIndexParentTitle) {
        $.MsgBox.Confirm("提示", "输入的章节目录名字不一致！", function () {
        });
        return false;
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
        return false;
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
    var isLeaf = $("input[name=add-isLeaf]:checked").val();


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
    if ($.isBlank(isLeaf)) {
        $.MsgBox.Confirm("提示", "请选择是否为目录章节！", function () {
        });
        return false;
    }

    var dataObj = {};
    dataObj.parentId = parentId;
    dataObj.url = url;
    dataObj.childTitle = childTitle;
    dataObj.bookId = bookId;
    dataObj.indexOrder = indexOrder;
    dataObj.isLeaf = isLeaf;
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
    var isLeaf = $("input[name=update-isLeaf]:checked").val();

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
        $.MsgBox.Confirm("提示", "错误：id 为空！", function () {
        });
        return false;
    }
    if ($.isBlank(url)) {
        $.MsgBox.Confirm("提示", "错误：url 为空！", function () {
        });
        return false;
    }
    if ($.isBlank(name)) {
        $.MsgBox.Confirm("提示", "错误：name 为空！", function () {
        });
        return false;
    }
    if ($.isBlank(isLeaf)) {
        $.MsgBox.Confirm("提示", "错误：isLeaf 为空！", function () {
        });
        return false;
    }

    var dataObj = {};
    dataObj.id = id;
    dataObj.url = url;
    dataObj.name = name;
    dataObj.bookId = bookId;
    dataObj.indexOrder = indexOrder;
    dataObj.isLeaf = isLeaf;
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
                });
                getBookInfo();
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
function showUpdateIndexModal(pid, id, name, url, order, isLeaf) {
    $("#update-pid").val(pid);
    $("#updateIndexId").val(id);
    $("#updateIndexModalTitle").val(name);
    $("#updateIndexModalUrl").val(url);
    $("#update-index-order").val(order);

    if (isLeaf == 0) {
        $("input[name='update-isLeaf'][value=0]").prop("checked", true);
    } else {
        $("input[name='update-isLeaf'][value=1]").prop("checked", true);
    }
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
 * 开始拖动的源事件
 * @param ev
 */
function drag(ev) {
    var sourceId = ev.target.id;
    var sourcePid = ev.target.parentNode.id;

    ev.dataTransfer.setData("Text", sourceId + "#" + sourcePid);
}

/**
 * 拖动完毕后的源事件
 * @param event
 */
function dragEnd(event) {

}

/**
 * 当被鼠标拖动的对象进入其容器范围内时触发此事件
 * @param ev
 */
function dragEnter(ev) {
    ev.preventDefault();
    ev.target.style.border = "3px dotted red";
}

/**
 *当某被拖动的对象在另一对象容器范围内拖动时触发此事件
 * @param event
 */
function allowDrop(event) {
    event.preventDefault();
}

/**
 * 当被鼠标拖动的对象离开其容器范围内时触发此事件
 * @param event
 */
function dragLeave(event) {
    event.preventDefault();
    event.target.style.border = "";
}

/**
 * 在一个拖动过程中，释放鼠标键时触发此事件
 * @param ev
 */
function drop(ev) {
    ev.preventDefault();
    ev.target.style.border = "";

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
 * 缩小右边栏
 */
function indentRight() {
    var classes = document.getElementById("left").classList;
    var leftNum = 3;

    for (var j = 0, len = classes.length; j < len; j++) {
        var className = classes[j];
        if (className.substr(0, 7) == "col-sm-") {
            leftNum = parseInt(className.substr(7))
            break;
        }
    }

    var numRight = 12 - leftNum;
    var leftClassOld = 'col-sm-' + leftNum + ' col-md-' + leftNum + ' col-lg-' + leftNum;
    var rightClassOld = 'col-sm-' + numRight + ' col-md-' + numRight + ' col-lg-' + numRight;

    var newNum = leftNum + 1;
    var newNumRight = 12 - newNum;
    var leftClassNew = 'col-sm-' + newNum + ' col-md-' + newNum + ' col-lg-' + newNum;
    var rightClassNew = 'col-sm-' + newNumRight + ' col-md-' + newNumRight + ' col-lg-' + newNumRight;

    $("#left").removeClass(leftClassOld);
    $("#left").addClass(leftClassNew)
    $("#right").removeClass(rightClassOld);
    $("#right").addClass(rightClassNew)

}

/**
 * 缩小左边栏
 */
function indentLeft() {
    var classes = document.getElementById("left").classList;
    var leftNum = 3;
    for (var j = 0, len = classes.length; j < len; j++) {
        var className = classes[j];
        if (className.substr(0, 7) == "col-sm-") {
            leftNum = parseInt(className.substr(7))
            break;
        }
    }

    var numRight = 12 - leftNum;
    var leftClassOld = 'col-sm-' + leftNum + ' col-md-' + leftNum + ' col-lg-' + leftNum;
    var rightClassOld = 'col-sm-' + numRight + ' col-md-' + numRight + ' col-lg-' + numRight;

    var newNum = leftNum - 1;
    var newNumRight = 12 - newNum;
    var leftClassNew = 'col-sm-' + newNum + ' col-md-' + newNum + ' col-lg-' + newNum;
    var rightClassNew = 'col-sm-' + newNumRight + ' col-md-' + newNumRight + ' col-lg-' + newNumRight;

    $("#left").removeClass(leftClassOld);
    $("#left").addClass(leftClassNew)
    $("#right").removeClass(rightClassOld);
    $("#right").addClass(rightClassNew)

}

/**
 * 隐藏左边栏
 */
function hideLeft() {
    var classes = document.getElementById("left").classList;
    var leftNum = 3;
    for (var j = 0, len = classes.length; j < len; j++) {
        var className = classes[j];
        if (className.substr(0, 7) == "col-sm-") {
            leftNum = parseInt(className.substr(7))
            break;
        }
    }
    var numRight = 12 - leftNum;
    var rightClassOld = 'col-sm-' + numRight + ' col-md-' + numRight + ' col-lg-' + numRight;

    $("#left").hide()
    $("#right").removeClass(rightClassOld);
    $("#right").addClass('col-sm-12 col-md-12 col-lg-12')

    $("#indentRight").hide()
}


/**
 * 显示左边栏
 */
function showLeft() {
    var classes = document.getElementById("left").classList;
    var leftNumOld = 3;
    for (var j = 0, len = classes.length; j < len; j++) {
        var className = classes[j];
        if (className.substr(0, 7) == "col-sm-") {
            leftNumOld = parseInt(className.substr(7))
            break;
        }
    }

    var newNumRight = 12 - leftNumOld;
    var rightClassNew = 'col-sm-' + newNumRight + ' col-md-' + newNumRight + ' col-lg-' + newNumRight;

    $("#left").show()
    $("#right").removeClass('col-sm-12 col-md-12 col-lg-12');
    $("#right").addClass(rightClassNew)

    $("#indentRight").show()

}

/**
 * 控制隐藏或显示左边栏
 */
function hideOrShowLeft() {
    var ishide = $("#indentRight").is(':hidden');
    if (ishide) {
        showLeft()
    } else {
        hideLeft()
    }
}

/**
 * 获取章节目录并局部刷新
 *
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

            if (code == 10000) {
                var data = result.data;
                var html = '<ul class="" id="ul-' + id + '" >';
                for (var i in data) {
                    var bookIndex = data[i];
                    var isLeaf = bookIndex.isLeaf;

                    html += '<li id="li-' + bookIndex.id + '" class=""  style="margin-top:20px;" draggable="true" ondragstart="drag(event)" ';

                    if (isLeaf == 0) {
                        html += 'ondrop="drop(event)" ondragenter="dragEnter(event)" ondragleave="dragLeave(event)" ondragover="allowDrop(event)">';
                        html += '<div style="display: inline" id="show-' + bookIndex.id + '"><a href="javascript:void(0);" onclick="getIndex(' + bookIndex.id + ')"><span class="glyphicon glyphicon-chevron-right"></span></a></div>';
                    } else {
                        html += '>';
                    }

                    html += '&emsp;<a href="javascript:void(0);" onclick="getContent(\'' + bookIndex.url + '\')"><span id="name-' + bookIndex.id + '">' + bookIndex.name + '</span></a>';
                    if (isCreator) {
                        html += '&emsp;&emsp;<div style="display: inline" id="opear-' + bookIndex.id + '"><a href="javascript:void(0);" onclick="showUpdateIndexModal(' + id + "," + bookIndex.id + ",\'" + bookIndex.name + "\',\'" + bookIndex.url + '\',' + bookIndex.indexOrder + ',' + isLeaf
                            + ')"><span class="glyphicon glyphicon-pencil"></span></a>';

                        if (isLeaf == 0) {
                            html += '<a href="javascript:void(0);" onclick="showAddIndexModal(' + bookIndex.id + ')"><span class="glyphicon glyphicon-plus-sign"></span></a>';
                        }

                        html += '<a href="javascript:void(0);" onclick="showDeleteIndexModal(' + id + "," + bookIndex.id + ')"><span class="glyphicon glyphicon-minus-sign"></span></a></div></li> ';
                    }
                    html += '<div id="index_' + bookIndex.id + '" style="padding-left:20px;" ></div>';
                }
                html += '</ul>'
                $("#index_" + id).html(html);

                var showHtml = '<a href="javascript:void(0);" onclick="hideIndex(' + id + ')"><span class="glyphicon glyphicon-chevron-down"></span></a>';
                $("#show-" + id).html(showHtml);

            } else {
                console.log(result.message)
            }
        }
    });
}

/**
 * 检测当前用户是否为书籍作者
 * @param callback 回调函数，获取首层目录
 */
function checkUser(callback) {

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
            callback(0);
        },
        error: function () {
            callback(0);
        }
    });
}

/**
 * 获取书籍信息
 */
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
                document.title=data.name;
            } else {
                $.MsgBox.Confirm("提示", result.message, function () {
                });
            }
        },
        error: function () {
            $.MsgBox.Confirm("提示", "系统异常，请稍后重试", function () {
            });
        }
    });
}

$(function () {
    $("#left-scroll").height(document.documentElement.clientHeight - 30);
    var uri = window.location.pathname;
    var bookId = uri.substr(6);
    $("#bookId").val(bookId);
    getBookInfo();
    checkUser(getIndex);
});