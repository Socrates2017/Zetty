var app = new Vue({
    el: '#userManager-app',
    data: {
        msgType: "no",
        alertMsg: "",
        confirm: {
            message: {
                title: '',
                content: ''
            },
            yesFunc: null
        },
        managerUserId: '',
        users: [],
        levelOptions: [{
            text: '超级管理员',
            value: 0
        }, {
            text: '管理员',
            value: 1
        }, {
            text: '用户',
            value: 2
        }],
        level: '',
        levelText: ''
    },
    created: function () {
        this.getUserLevel();
        this.getAllUsers()
    },
    methods: {
        getAllUsers: function () {
            this.postUtil({
                path: '/user/all',
                success: function (data) {
                    data.forEach(function (user) {
                        user['saved'] = true;
                        app.users.push(user)
                    })
                },
                error: function (error) {
                    app.showErrorMsg("获取用户列表失败 " + error)
                }
            })
        },
        getUserLevel: function () {
            this.postUtil({
                path: '/user/get/level',
                success: function (data) {
                    app.level = parseInt(data);
                    app.levelOptions.forEach(function (option) {
                        if (option.value === parseInt(app.level)) {
                            app.levelText = option.text;
                        }
                    })
                },
                error: function (error) {
                    app.showErrorMsg("获取用户列表失败 " + error)
                }
            })
        },
        addUser: function (event, name, level, index) {
            this.openConfirm({
                title: "确定添加",
                content: "此操作会添加一个用户",
                yesFunc: function () {
                    app.btnLoaderShow(event);
                    app.postUtil({
                        path: '/user/add',
                        data: {
                            name: name,
                            level: level
                        },
                        success: function (data) {
                            Vue.set(app.users[index], 'id', parseInt(data));
                            Vue.set(app.users[index], 'saved', true);
                            app.showSuccessMsg()
                        },
                        error: function (error) {
                            app.showErrorMsg(error)
                        },
                        end: function () {
                            app.btnLoaderHide(event)
                        }
                    })
                }
            })
        },
        deleteUser: function (event, userId, index) {
            this.openConfirm({
                title: "确定删除",
                content: "此操作会删除此用户",
                yesFunc: function () {
                    app.btnLoaderShow(event);
                    app.postUtil({
                        path: '/user/delete',
                        data: {
                            userId: userId
                        },
                        success: function () {
                            Vue.set(app.users[index], 'name', 'deleted');
                            app.showSuccessMsg()
                        },
                        error: function (error) {
                            app.showErrorMsg(error)
                        },
                        end: function () {
                            app.btnLoaderHide(event)
                        }
                    })
                }
            })
        },
        updateUserLevel: function (event, userId, level) {
            this.openConfirm({
                title: "确定更新",
                content: "此操作会更新用户级别",
                yesFunc: function () {
                    app.btnLoaderShow(event);
                    app.postUtil({
                        path: '/user/update/level',
                        data: {
                            userId: userId,
                            level: level
                        },
                        success: function () {
                            app.showSuccessMsg();
                        },
                        error: function (error) {
                            app.showErrorMsg(error)
                        },
                        end: function () {
                            app.btnLoaderHide(event)
                        }
                    })
                }
            })
        },
        addUserNotPost: function () {
            app.users.push({
                name: null,
                level: 2,
                saved: false
            })
        },
        deleteUserNotSaved: function (index) {
            Vue.set(app.users[index], 'name', 'deleted')
        },
        showErrorMsg: function (content) {
            app.msgType = 'error';
            app.alertMsg = content
        },
        showWarningMsg: function (content) {
            app.msgType = 'warning';
            app.alertMsg = content
        },
        showSuccessMsg: function (content) {
            app.msgType = 'success';
            app.alertMsg = content;
            setTimeout(function () {
                app.msgType = '';
            }, 3000)
        },
        postUtil: function (args) {
            var defaultArgs = {
                path: "",
                data: "",
                success: function () {
                },
                error: function () {
                },
                end: function () {
                },
            };
            Object.keys(args).forEach(function (key) {
                defaultArgs[key] = args[key]
            });
            axios.post(defaultArgs.path, defaultArgs.data).then(function (value) {
                if (value.data.code === 1) {
                    defaultArgs.success(value.data.data);
                }
                else {
                    defaultArgs.error(value.data.message)
                }
                defaultArgs.end()
            }).catch(function (error) {
                defaultArgs.error(error);
                defaultArgs.end()
            })
        },
        btnLoaderShow: function (event) {
            let el = $(event.target);
            el.attr('disabled', true);
            el.parent().children('div.loader').removeClass('d-none')
        },
        btnLoaderHide: function (event) {
            let el = $(event.target);
            el.attr('disabled', false);
            el.parent().children('div.loader').addClass('d-none')
        },
        openConfirm: function (args) {
            app.confirm.message.title = args.title;
            app.confirm.message.content = args.content;
            app.confirm.yesFunc = args.yesFunc;
            $("#confirmModal").modal('show');
        }
    }
});

Vue.component('loading-8', {
    template: "<div class=\"loader loader--style8\">\n" +
    "  <svg version=\"1.1\" id=\"Layer_1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" x=\"0px\" y=\"0px\"\n" +
    "     width=\"24px\" height=\"30px\" viewBox=\"0 0 24 30\" style=\"enable-background:new 0 0 50 50;\" xml:space=\"preserve\">\n" +
    "    <rect x=\"0\" y=\"10\" width=\"4\" height=\"10\" fill=\"#333\" opacity=\"0.2\">\n" +
    "      <animate attributeName=\"opacity\" attributeType=\"XML\" values=\"0.2; 1; .2\" begin=\"0s\" dur=\"0.6s\" repeatCount=\"indefinite\" />\n" +
    "      <animate attributeName=\"height\" attributeType=\"XML\" values=\"10; 20; 10\" begin=\"0s\" dur=\"0.6s\" repeatCount=\"indefinite\" />\n" +
    "      <animate attributeName=\"y\" attributeType=\"XML\" values=\"10; 5; 10\" begin=\"0s\" dur=\"0.6s\" repeatCount=\"indefinite\" />\n" +
    "    </rect>\n" +
    "    <rect x=\"8\" y=\"10\" width=\"4\" height=\"10\" fill=\"#333\"  opacity=\"0.2\">\n" +
    "      <animate attributeName=\"opacity\" attributeType=\"XML\" values=\"0.2; 1; .2\" begin=\"0.15s\" dur=\"0.6s\" repeatCount=\"indefinite\" />\n" +
    "      <animate attributeName=\"height\" attributeType=\"XML\" values=\"10; 20; 10\" begin=\"0.15s\" dur=\"0.6s\" repeatCount=\"indefinite\" />\n" +
    "      <animate attributeName=\"y\" attributeType=\"XML\" values=\"10; 5; 10\" begin=\"0.15s\" dur=\"0.6s\" repeatCount=\"indefinite\" />\n" +
    "    </rect>\n" +
    "    <rect x=\"16\" y=\"10\" width=\"4\" height=\"10\" fill=\"#333\"  opacity=\"0.2\">\n" +
    "      <animate attributeName=\"opacity\" attributeType=\"XML\" values=\"0.2; 1; .2\" begin=\"0.3s\" dur=\"0.6s\" repeatCount=\"indefinite\" />\n" +
    "      <animate attributeName=\"height\" attributeType=\"XML\" values=\"10; 20; 10\" begin=\"0.3s\" dur=\"0.6s\" repeatCount=\"indefinite\" />\n" +
    "      <animate attributeName=\"y\" attributeType=\"XML\" values=\"10; 5; 10\" begin=\"0.3s\" dur=\"0.6s\" repeatCount=\"indefinite\" />\n" +
    "    </rect>\n" +
    "  </svg>\n" +
    "</div>"
});

$('button.click-load').on('click', function () {
    $(this).attr('disabled', 'true');

});