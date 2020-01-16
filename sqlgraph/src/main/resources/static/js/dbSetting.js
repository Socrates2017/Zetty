var app = new Vue({
    el: '#db-app',
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
        dbs: []
    },
    created: function () {
        this.getAllDb()
    },
    methods: {
        getAllDb: function () {
            this.postUtil({
                path: '/data/getAllDbs',
                success: function (data) {
                    data.forEach(function (db) {
                        app.dbs.push(db)
                    })
                },
                error: function (error) {
                    app.showErrorMsg("获取数据库列表失败 " + error)
                }
            })
        },
        updateAllDb: function (event) {
            this.openConfirm({
                title: "确认修改",
                content: "由于没做数据检查，请再三确认测试通过，否则影响定时任务执行！！！",
                yesFunc: function () {
                    app.btnLoaderShow(event);
                    app.postUtil({
                        path: '/data/updateDbs',
                        data: {
                            dbs: JSON.stringify(app.dbs)
                        },
                        success: function () {
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
            });
        },
        testDb: function (event, index) {
            this.btnLoaderShow(event);
            this.postUtil({
                path: '/data/testDb',
                data: {
                    db: JSON.stringify(app.dbs[index])
                },
                success: function () {
                    app.showSuccessMsg()
                },
                error: function (error) {
                    app.showErrorMsg(error)
                },
                end: function () {
                    app.btnLoaderHide(event)
                }
            })
        },
        addDb: function () {
            app.dbs.push({
                host: null,
                port: null,
                user: null,
                password: null,
                database: null
            })
        },
        deleteDb: function (index) {
            Vue.set(app.dbs[index], 'host', 'deleted')
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
        setVueObject: function (vueObject, object) {
            Object.keys(object).forEach(function (key) {
                Vue.set(vueObject, key, object[key])
            })
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