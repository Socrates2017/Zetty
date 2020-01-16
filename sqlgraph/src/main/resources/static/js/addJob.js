var app = new Vue({
    el: '#detail-app',
    data: {
        msgType: "no",
        alertMsg: "",
        activeTestImageIndex: 0,
        confirm: {
            message: {
                title: '',
                content: ''
            },
            yesFunc: null
        },
        testImages: [],
        job: {
            name: "",
            execMonth: null,
            execDay: null,
            execHour: null,
            execMinute: null,
            execSecond: 1,
            ddRobot: null,
            sql: [],
            dbId: null,
            project_id: null
        },
        projects: [],
        dbs: []
    },
    created: function () {
        this.getAllDb()
        this.getAllProject()
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
        getAllProject: function () {
            this.postUtil({
                path: '/data/getAllProject',
                success: function (data) {
                    var projects = data;
                    app.projects = []
                    for (var index = 0, len = projects.length; index < len; index++) {
                        app.projects.push(projects[index]);
                    }
                },
                error: function (error) {
                    app.showErrorMsg("读取project失败：" + error)
                }
            });
        },
        addSql: function (job) {
            job.sql.push("")
        },
        deleteSql: function (job, index) {
            job.sql.splice(index, 1)
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
                } else {
                    defaultArgs.error(value.data.message)
                }
                defaultArgs.end()
            }).catch(function (error) {
                defaultArgs.error(error);
                defaultArgs.end()
            })
        },
        testJob: function (event) {
            this.btnLoaderShow(event);
            this.postUtil({
                path: '/data/testJob',
                data: JSON.stringify(app.job),
                success: function (data) {
                    if (app.testImages.length === 4) {
                        app.testImages.pop();
                    }
                    app.testImages.reverse();
                    app.testImages.push(data.image);
                    app.testImages.reverse();
                },
                error: function (error) {
                    app.showErrorMsg('生成图片失败:' + error)
                },
                end: function () {
                    app.btnLoaderHide(event)
                }
            });
        },
        addJob: function (event) {
            if (!this.checkData()) {
                return
            }
            this.openConfirm({
                title: '确认添加',
                content: '添加成功后需要手动启动',
                yesFunc: function () {
                    app.btnLoaderShow(event);
                    app.postUtil({
                        path: '/data/addJob',
                        data: JSON.stringify(app.job),
                        success: function () {
                            app.showSuccessMsg("添加成功")
                        },
                        error: function (error) {
                            app.showErrorMsg("添加失败：" + error)
                        },
                        end: function () {
                            app.btnLoaderHide(event)
                        }
                    })
                }
            });
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
        },
        checkData: function () {
            if (app.job.name == null || app.job.name === '') {
                this.showWarningMsg("job name 不能空");
                return false
            }
            if (app.job.sql.length === 0) {
                this.showWarningMsg("请添加至少一条sql语句");
                return false
            }
            if (!(app.job.execMonth != null && app.job.execMonth !== '' ||
                app.job.execDay != null && app.job.execDay !== '' ||
                app.job.execHour != null && app.job.execHour !== '' ||
                app.job.execMinute != null && app.job.execMinute !== '' ||
                app.job.execSecond != null && app.job.execSecond !== '')) {
                this.showWarningMsg("请设置执行时间");
                return false
            }
            if (app.job.ddRobot == null || app.job.ddRobot === '') {
                this.showWarningMsg("请添加钉钉机器人");
                return false
            }
            return true
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