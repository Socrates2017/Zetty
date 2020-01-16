var app = new Vue({
    el: "#main-app",
    data: {
        screenLoader: false,

        username: "",
        msgType: "no",
        alertMsg: "",
        confirm: {
            message: {
                title: '',
                content: ''
            },
            yesFunc: null
        },
        jobConfirm: {
            jobIndex: 0,
            show: false,
            yesFunc: null,
            noFunc: function () {
                app.jobConfirm.show = false;
            },
            loading: false
        },
        page: 0,
        jobs: [],
        activeJobIndex: 0,
        jobIdIndex: {},
        userId: null,
        apiToken: null,

        schedulerStatus: -1
    },
    created: function () {
        // this.postUtil({
        //     path: '/api/check',
        //     success: function () {
        //         app.schedulerStatus = 0
        //     },
        //     error: function () {
        //         app.schedulerStatus = 1
        //     }
        // });

        this.getAllJobs(0);
        //this.getUsername();
        //this.getApiData();

        $("#main-app > div.screen-mask").addClass("screen-mask-hide");
        setTimeout(function () {
            $("#main-app > div.screen-mask").css("display", "none")
        }, 1000)
    },
    methods: {
        restartScheduler: function () {
            this.openConfirm({
                title: '确认重启',
                content: "此操作会重启scheduler，当且仅当scheduler挂了才使用，请明确！！！",
                yesFunc: function () {
                    app.screenLoader = true;
                    app.postUtil({
                        path: '/api/restart/scheduler',
                        success: function () {
                            app.showSuccessMsg()
                        },
                        error: function (error) {
                            app.showErrorMsg(error)
                        },
                        end: function () {
                            app.screenLoader = false;
                        }
                    })
                }
            })
        },
        getAllJobs: function (page) {
            $("#all").addClass("active");
            $("#running").removeClass("active");
            $("#stopped").removeClass("active");
            $("#offline").removeClass("active");
            this.postUtil({
                path: '/data/getAllJobs',
                data: {
                    page: page
                },
                success: function (data) {
                    var jobs = data;
                    app.jobs = []
                    app.jobIdIndex = {}
                    for (var index = 0, len = jobs.length; index < len; index++) {
                        jobs[index]['loaderShow'] = false;
                        app.jobs.push(jobs[index]);
                        app.jobIdIndex[jobs[index]['id']] = index;
                    }
                    app.page++;
                },
                error: function (error) {
                    app.showErrorMsg("读取job失败：" + error)
                }
            });
        },
        getJobsByStatus: function (page, status) {

            if (status == 1) {
                $("#all").removeClass("active");
                $("#running").addClass("active");
                $("#stopped").removeClass("active");
                $("#offline").removeClass("active");
            } else if (status == 2) {
                $("#all").removeClass("active");
                $("#running").removeClass("active");
                $("#stopped").addClass("active");
                $("#offline").removeClass("active");
            } else if (status == 0) {
                $("#all").removeClass("active");
                $("#running").removeClass("active");
                $("#stopped").removeClass("active");
                $("#offline").addClass("active");
            }

            this.postUtil({
                path: '/data/getJobsByStatus/' + status,
                data: {
                    page: page
                },
                success: function (data) {
                    var jobs = data;
                    app.jobs = []
                    app.jobIdIndex = {}
                    for (var index = 0, len = jobs.length; index < len; index++) {
                        jobs[index]['loaderShow'] = false;
                        app.jobs.push(jobs[index]);
                        app.jobIdIndex[jobs[index]['id']] = index;
                    }
                    app.page++;
                },
                error: function (error) {
                    app.showErrorMsg("读取job失败：" + error)
                }
            });
        },
        getUsername: function () {
            this.postUtil({
                path: '/user/get/name',
                success: function (data) {
                    app.username = data
                },
                error: function (error) {
                    app.showErrorMsg(error)
                }
            })
        },
        getApiData: function () {
            this.postUtil({
                path: '/data/api/get',
                success: function (data) {
                    app.userId = data.userId;
                    app.apiToken = data.apiToken;
                },
                error: function (error) {
                    app.showErrorMsg(error)
                }
            })
        },
        showApiData: function () {
            this.openConfirm({
                title: "api数据",
                content: "userId: " + app.userId + "\ttoken: " + app.apiToken
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
                if (value.data.code === 10000) {
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
        openConfirm: function (args) {
            app.confirm.message.title = args.title;
            app.confirm.message.content = args.content;
            app.confirm.yesFunc = args.yesFunc;
            $("#confirmModal").modal('show');
        },
        showErrorMsg: function (content) {
            app.msgType = 'error';
            app.alertMsg = content;

            setTimeout(function () {
                app.msgType = 'no';
                app.alertMsg = "";
            }, 5000)
        },
        showWarningMsg: function (content) {
            app.msgType = 'warning';
            app.alertMsg = content;

            setTimeout(function () {
                app.msgType = 'no';
                app.alertMsg = "";
            }, 5000)
        },
        showSuccessMsg: function (content) {
            app.msgType = 'success';
            app.alertMsg = content;

            setTimeout(function () {
                app.msgType = 'no';
                app.alertMsg = "";
            }, 5000)
        },
        jobLoaderShow: function (jobIndex) {
            Vue.set(app.jobs[jobIndex], 'loaderShow', true);
        },
        jobLoaderHide: function (jobIndex) {
            Vue.set(app.jobs[jobIndex], 'loaderShow', false);
        },
        runJob: function (index) {
            this.openConfirm({
                title: '确认运行',
                content: "此操作会添加job到任务序列",
                yesFunc: function () {
                    app.jobLoaderShow(index);
                    app.postUtil({
                        path: '/scheduler/addJob',
                        data: {
                            jobId: app.jobs[index].id
                        },
                        success: function (data) {
                            Vue.set(app.jobs[index], 'status', data.status);
                            app.showSuccessMsg()
                        },
                        error: function (error) {
                            app.showErrorMsg(error)
                        },
                        end: function () {
                            app.jobLoaderHide(index)
                        }
                    })
                }
            })
        },
        execJob: function (index) {
            this.openConfirm({
                title: '确认执行',
                content: "此操作会立即执行一次任务",
                yesFunc: function () {
                    app.jobLoaderShow(index);
                    app.postUtil({
                        path: '/scheduler/execJob',
                        data: {
                            jobId: app.jobs[index].id
                        },
                        success: function (data) {
                            app.showSuccessMsg()
                        },
                        error: function (error) {
                            app.showErrorMsg(error)
                        },
                        end: function () {
                            app.jobLoaderHide(index)
                        }
                    })
                }
            })
        },
        pauseJob: function (index) {
            this.openConfirm({
                title: '确认暂停',
                content: "此操作会暂时执行计划任务",
                yesFunc: function () {
                    app.jobLoaderShow(index);
                    app.postUtil({
                        path: '/scheduler/pauseJob',
                        data: {
                            jobId: app.jobs[index].id
                        },
                        success: function (data) {
                            Vue.set(app.jobs[index], 'status', data.status);
                            app.showSuccessMsg()
                        },
                        error: function (error) {
                            app.showErrorMsg(error)
                        },
                        end: function () {
                            app.jobLoaderHide(index)
                        }
                    })
                }
            })
        },
        removeJob: function (index) {
            this.openConfirm({
                title: '确认移除',
                content: "此操作会移除计划任务",
                yesFunc: function () {
                    app.jobLoaderShow(index);
                    app.postUtil({
                        path: '/scheduler/removeJob',
                        data: {
                            jobId: app.jobs[index].id
                        },
                        success: function (data) {
                            Vue.set(app.jobs[index], 'status', data.status);
                            app.showSuccessMsg()
                        },
                        error: function (error) {
                            app.showErrorMsg(error)
                        },
                        end: function () {
                            app.jobLoaderHide(index)
                        }
                    })
                }
            })
        },
        resumeJob: function (index) {
            this.openConfirm({
                title: '确认重启',
                content: "此操作会重启计划任务",
                yesFunc: function () {
                    app.jobLoaderShow(index);
                    app.postUtil({
                        path: '/scheduler/resumeJob',
                        data: {
                            jobId: app.jobs[index].id
                        },
                        success: function (data) {
                            Vue.set(app.jobs[index], 'status', data.status);
                            app.showSuccessMsg()
                        },
                        error: function (error) {
                            app.showErrorMsg(error)
                        },
                        end: function () {
                            app.jobLoaderHide(index)
                        }
                    })
                }
            })
        },
        jumpUrl: function (url) {
            window.location.href = url
        }
    }
});

Vue.component('job-loader', {
    template: "<div class=\"job-loader-mask\">\n" +
        "    <div class=\"job-loader-container\">\n" +
        "        <div class=\"loader self-center\">\n" +
        "            <svg version=\"1.1\" id=\"loader-1\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" x=\"0px\" y=\"0px\"\n" +
        "               width=\"40px\" height=\"40px\" viewBox=\"0 0 40 40\" enable-background=\"new 0 0 40 40\" xml:space=\"preserve\">\n" +
        "              <path opacity=\"0.2\" fill=\"#000\" d=\"M20.201,5.169c-8.254,0-14.946,6.692-14.946,14.946c0,8.255,6.692,14.946,14.946,14.946\n" +
        "                s14.946-6.691,14.946-14.946C35.146,11.861,28.455,5.169,20.201,5.169z M20.201,31.749c-6.425,0-11.634-5.208-11.634-11.634\n" +
        "                c0-6.425,5.209-11.634,11.634-11.634c6.425,0,11.633,5.209,11.633,11.634C31.834,26.541,26.626,31.749,20.201,31.749z\"/>\n" +
        "              <path fill=\"#000\" d=\"M26.013,10.047l1.654-2.866c-2.198-1.272-4.743-2.012-7.466-2.012h0v3.312h0\n" +
        "                C22.32,8.481,24.301,9.057,26.013,10.047z\">\n" +
        "                <animateTransform attributeType=\"xml\"\n" +
        "                  attributeName=\"transform\"\n" +
        "                  type=\"rotate\"\n" +
        "                  from=\"0 20 20\"\n" +
        "                  to=\"360 20 20\"\n" +
        "                  dur=\"0.5s\"\n" +
        "                  repeatCount=\"indefinite\"/>\n" +
        "                </path>\n" +
        "              </svg>\n" +
        "        </div>\n" +
        "    </div>\n" +
        "</div>"
});