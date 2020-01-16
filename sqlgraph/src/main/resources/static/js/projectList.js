var app = new Vue({
        el: "#main-app",
        data: {
            screenLoader: false,

            username: "",
            userlevel: 2,
            msgType: "no",
            alertMsg: "",
            confirm: {
                message: {
                    title: '',
                    content: ''
                },
                yesFunc: null
            },
            projectConfirm: {
                projectIndex: 0,
                show: false,
                yesFunc: null,
                noFunc: function () {
                    app.projectConfirm.show = false;
                },
                loading: false
            },
            page: 0,
            projects: [],
            userProjects: [],
            userProject: {
                user_id: null,
                project_id: null
            },
            addProjectData: {
                id: null,
                name: null,
                description: null
            },
            usersNoPermission: [],
            projectIdIndex: {},
            userId: null,
            apiToken: null,

            schedulerStatus: -1
        },
        created: function () {
            this.postUtil({
                path: '/api/check',
                success: function () {
                    app.schedulerStatus = 0
                },
                error: function () {
                    app.schedulerStatus = 1
                }
            });

            this.getApiData();
            this.getAllProject();
            this.getUsername();


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
            getAllProject: function () {
                this.postUtil({
                    path: '/data/getAllProject',
                    success: function (data) {
                        var projects = data;
                        app.projects = []
                        app.projectIdIndex = {}
                        for (var index = 0, len = projects.length; index < len; index++) {
                            projects[index]['loaderShow'] = false;
                            app.projects.push(projects[index]);
                            app.projectIdIndex[projects[index]['id']] = index;
                        }
                        app.page++;
                    },
                    error: function (error) {
                        app.showErrorMsg("读取project失败：" + error)
                    }
                });
            },
            getUsersNoPermission: function (projectId) {
                this.postUtil({
                    path: '/data/getUsersNoPermission/' + projectId,
                    success: function (data) {
                        app.usersNoPermission = []
                        data.forEach(function (user) {
                            app.usersNoPermission.push(user)
                        })
                    },
                    error: function (error) {
                        app.showErrorMsg("获取用户列表失败 " + error)
                    }
                })
            },
            getUsername: function () {
                this.postUtil({
                    path: '/user/info',
                    success: function (data) {
                        app.username = data.name
                        app.userlevel = data.level
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
            projectLoaderShow: function (projectIndex) {
                Vue.set(app.projects[projectIndex], 'loaderShow', true);
            },
            projectLoaderHide: function (projectIndex) {
                Vue.set(app.projects[projectIndex], 'loaderShow', false);
            },

            jumpUrl: function (url) {
                window.location.href = url
            },
            setVueObject: function (vueObject, object) {
                Object.keys(object).forEach(function (key) {
                    Vue.set(vueObject, key, object[key])
                })
            },
            updateProject: function (project_id) {
                this.postUtil({
                    path: '/data/getUserProject',
                    data: {
                        project_id: project_id
                    },
                    success: function (data) {
                        app.userProjects = []
                        app.setVueObject(app.userProjects, data)
                    },
                    error: function (error) {
                        app.userProjects = []
                        app.showErrorMsg('获取project失败')
                    }
                })
                app.userProject.project_id = project_id;
                this.getUsersNoPermission(project_id);
                $('#updateProject').modal('show');
            },
            updateProjectDetail: function (id, name, description) {
                $('#updateProjectDetail').modal('show');
                app.addProjectData.id = id;
                app.addProjectData.name = name;
                app.addProjectData.description = description;
            },
            updateProjectDetailData: function () {

                if (app.addProjectData == null || app.addProjectData === '') {
                    this.showWarningMsg("user name 不能空");
                    return;
                }
                app.postUtil({
                    path: '/data/updateProject',
                    data: JSON.stringify(app.addProjectData),
                    success: function () {
                        app.getAllProject();
                        $('#updateProjectDetail').modal('hide');
                        app.showSuccessMsg("添加成功")
                    },
                    error: function (error) {
                        app.showErrorMsg("修改失败：" + error)
                    },
                    end: function () {
                        app.addProjectData.id = null;
                        app.addProjectData.name = null;
                        app.addProjectData.description = null;
                    }
                });

            },
            deleteProject: function (id) {
                this.openConfirm({
                    title: '确认删除项目',
                    content: "此操作会完全删除项目",
                    yesFunc: function () {
                        //app.jobLoaderShow(id);
                        app.postUtil({
                            path: '/data/deleteProject/' + id,
                            success: function () {
                                app.getAllProject();
                                app.showSuccessMsg("删除成功")
                            },
                            error: function (error) {
                                app.showErrorMsg("删除失败：" + error)
                            }

                        });
                    }
                })
            },


            addProject: function () {
                app.addProjectData.id = null;
                app.addProjectData.name = null;
                app.addProjectData.description = null;
                $('#addProject').modal('show');
            },
            addProjectDataF: function () {
                if (app.addProjectData == null || app.addProjectData === '') {
                    this.showWarningMsg("user name 不能空");
                    return;
                }
                app.postUtil({
                    path: '/data/addProjectData',
                    data: JSON.stringify(app.addProjectData),
                    success: function () {
                        app.getAllProject();
                        $('#addProject').modal('hide');
                        app.showSuccessMsg("添加成功")

                    },
                    error: function (error) {
                        app.showErrorMsg("添加失败：" + error)
                    }
                });

            },
            addUserProject: function (event) {
                if (app.userProject == null || app.userProject === '') {
                    this.showWarningMsg("user name 不能空");
                    return;
                }
                app.postUtil({
                    path: '/data/addUserProject',
                    data: JSON.stringify(app.userProject),
                    success: function () {
                        app.updateProject(app.userProject.project_id);
                        app.showSuccessMsg("添加成功")
                    },
                    error: function (error) {
                        app.showErrorMsg("添加失败：" + error)
                    }
                });
            },
            deleteUserProject: function (user_id) {
                event.stopPropagation();
                if (app.userProject == null || app.userProject === '') {
                    this.showWarningMsg("user name 不能空");
                    return;
                }
                app.postUtil({
                    path: '/data/deleteUserProject/' + user_id,
                    data: JSON.stringify(app.userProject),
                    success: function () {
                        app.updateProject(app.userProject.project_id);
                        app.showSuccessMsg("删除成功")
                    },
                    error: function (error) {
                        app.showErrorMsg("添加失败：" + error)
                    }
                });

            }

        }
    })
;

Vue.component('project-loader', {
    template: "<div class=\"project-loader-mask\">\n" +
        "    <div class=\"project-loader-container\">\n" +
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