var AppMain;
AppMain = angular.module('AppMain', ['ngRoute'], function ($routeProvider) {
    var htmlRoot = '/static/html/';
    $routeProvider.
        when('/', {controller: MainCtrl, templateUrl: htmlRoot + 'main.html'}).
        when('/register', {controller: RegisterCtrl, templateUrl: htmlRoot + 'register.html'}).
        when('/example', {controller: ExampleCtrl, templateUrl: htmlRoot + 'example.html'}).
        when('/mission', {controller: MissionCtrl, templateUrl: htmlRoot + 'mission.html'}).
        when('/mine/personalInformation', {
            controller: PersonalInformationCtrl,
            templateUrl: htmlRoot + 'personalInformation.html'
        }).
        when('/mine/missionAccept', {controller: MineMissionAcceptCtrl, templateUrl: htmlRoot + 'missionAccept.html'}).
        when('/mine/missionPublish', {
            controller: MineMissionPublishCtrl,
            templateUrl: htmlRoot + 'missionPublish.html'
        }).
        when('/missionDetail/:missionId', {
            controller: MissionDetailCtrl,
            templateUrl: htmlRoot + 'missionDetail.html'
        }).
        when('/missionCreate/:missionId', {
            controller: MissionCreateCtrl,
            templateUrl: htmlRoot + 'missionCreate.html'
        }).
        when('/manager/setting', {controller: ManagerSettingCtrl, templateUrl: htmlRoot + 'managerSetting.html'});
});

function IndexCtrl($scope, $http, $location) {
    $scope.user;
    $scope.userShow = false;
    $scope.alertClass = 'alert';
    $scope.alertText = '';
    $scope.doInit = function () {
        commonLoginValidate($scope, $http, function indexInit(userShow, user) {
            $scope.userShow = userShow;
            $scope.user = user;
        })
    };
    $scope.logOut = function () {
        $http({
            method: 'GET',
            url: '/user/logOut'
        }).success(function (result) {
            console.log(result);
            if (0 == result["status"]) {
                $scope.userShow = false;
                commonEmit($scope, "success", "注销成功", "operateStatus");
                $location.path('/');
            } else {
                commonEmit($scope, "danger", "注销失败", "operateStatus");
            }
        }).error(function () {
            commonEmit($scope, "danger", "网络连接错误，请稍后重试", "operateStatus");
        });
    };
    $scope.$on("userShowChange",
        function (event, msg) {
            console.log("parent", msg);
            if (null != msg) {
                $scope.userShow = true;
                $scope.user = msg;
            } else {
                $scope.userShow = false;
                $scope.user = null;
                commonEmit($scope, "danger", "请登录后操作", "operateStatus");
            }
        });
    $scope.$on("userChange",
        function (event, msg) {
            console.log("userChange parent", msg);
            if (null != msg) {
                $scope.email = msg;
            }
        });
    $scope.$on("operateStatus",
        function (event, msg) {
            console.log("parent---operateStatus", msg);
            $scope.showOperateDiv(msg.status, msg.text);
        });
    $scope.$on("operateProgressStatus",
        function (event, msg) {
            console.log("parent---operateProgressStatus", msg);
            $("#pleaseWaitDialog").modal(msg.text);
        });
    $scope.showOperateDiv = function (alertStatus, alertText) {
        $scope.alertClass = "alert alert-" + alertStatus;
        $scope.alertText = alertText;
        $("#operateModalDiv").modal();
    }
}

function MainCtrl($scope, $http) {
    $scope.email;
    $scope.password;
    $scope.recommends = [];
    $scope.recentlyMission = [];
    $scope.doInit = function () {
        $scope.fetchRecommends();
        $scope.fetchRecentlyMission();
    };
    $scope.fetchRecommends = function () {
        $http({
            method: 'GET',
            url: '/index/fetchRecommends'
        }).success(function (result) {
            console.log(result);
            $scope.recommends = result;
        });
    };
    $scope.fetchRecentlyMission = function () {
        $http({
            method: 'GET',
            url: '/index/recentlyMission'
        }).success(function (result) {
            console.log(result);
            if (0 == result["status"]) {
                $scope.recentlyMission = result["data"];
            }
        });
    };
    $scope.loginForm = function () {
        $http({
            method: 'POST',
            url: '/user/login',
            params: {
                email: $scope.email,
                password: $scope.password
            }
        }).success(function (result) {
            console.log(result);
            if (0 == result["status"]) {
                $scope.fetchRecentlyMission();
                $scope.$emit("userShowChange", result["user"]);
                commonEmit($scope, "success", "登录成功", "operateStatus");
            } else {
                commonEmit($scope, "danger", "登录邮箱或密码错误，登录失败", "operateStatus");
            }
        }).error(function () {
            commonEmit($scope, "danger", "网络连接错误，请检查网络连接后重试", "operateStatus");
        });
    }
}
function RegisterCtrl($scope, $http, $location) {
    $scope.userName;
    $scope.email;
    $scope.password;
    $scope.passwordConfirm;
    $scope.registerForm = function () {
        if ($scope.password != $scope.passwordConfirm) {//todo
            commonEmit($scope, "danger", "两次输入密码不一样", "operateStatus");
            return;
        }
        $http.post('/user/register', {
            userName: $scope.userName,
            email: $scope.email,
            password: $scope.password
        }).success(function (result) {
            console.log(result);
            if (0 == result["status"]) {
                commonEmit($scope, "success", "欢迎加入川大威客，用户已自动登录", "operateStatus");
                $scope.$emit("userShowChange", result["data"]);
                $location.path('/');
            } else if (3 == result["status"]) {
                commonEmit($scope, "danger", "该邮箱已注册，请直接登录", "operateStatus");
                $scope.$emit("userChange", result["data"]);
                $location.path('/');
            } else {
                commonEmit($scope, "danger", "出错了，请稍后重试", "operateStatus");
            }
        });
    }

}
function ExampleCtrl($scope, $http, $timeout) {
    $scope.picUrl = '';
    $scope.zipUrl = '';
    $scope.fileUrl = '';
    $scope.doInit = function () {
    };
    $scope.clickPic = function () {
        $('#file').click();
    };
    $scope.uploadPic = function (files) {
        uploadImages($http, files, '/manager/file/uploadImageTest', function (result) {
            console.log(result);
            if (0 == result["status"]) {
                commonEmit($scope, "success", "图片上传成功", "operateStatus");
                $scope.picUrl = result["data"][0];
            } else {
                commonEmit($scope, "danger", "图片上传失败", "operateStatus");
            }
        });
    };
    $scope.clickZip = function () {
        $('#zipFile').click();
    };
    $scope.uploadZip = function (files) {
        uploadImages($http, files, '/manager/file/uploadZipFile', function (result) {
            console.log(result);
            if (0 == result["status"]) {
                commonEmit($scope, "success", "zip文件上传成功", "operateStatus");
                $scope.zipUrl = result["data"][0];
            } else {
                commonEmit($scope, "danger", "zip文件上传失败", "operateStatus");
            }
        });
    };
    $scope.deleteFileByUrl = function () {//todo
        $http({
            method: 'GET',
            url: '/manager/file/deleteFile',
            params: {
                fileUrl: $scope.fileUrl
            }
        }).success(function (result) {
            console.log(result);
        }).error(function () {
        });
    };
    $scope.showDialog = function () {
        $("#pleaseWaitDialog").modal();
        $timeout(function () {
            console.log('3 seconds delay');
            $("#pleaseWaitDialog").modal('hide');
            commonEmit($scope, "success", "操作完成", "operateStatus");
        }, 3000);
    }
}
function MissionCtrl($scope, $http, $location) {
    $scope.pageAmount = 1;
    $scope.currentTab = 0;
    $scope.currentPage = 1;
    $scope.missionWinningMode = '中标模式';
    $scope.missionMode = '任务模式';
    $scope.missionStatus = '任务状态';
    $scope.allMission = [];
    $scope.panelClass = [];
    $scope.doInit = function () {
        commonLoginValidate($scope, $http, function redirectToIndex(userShow) {
            if (!userShow) {
                commonEmit($scope, "danger", "请登录后操作", "operateStatus");
                $location.path('/');
            }
        });
        $scope.fetchAllMission();
    };
    $scope.fetchAllMission = function () {
        $http({
            method: 'GET',
            url: '/mission/allMission',
            params: {
                missionWinningMode: $scope.missionWinningMode,
                missionMode: $scope.missionMode,
                missionStatus: $scope.missionStatus,
                currentPage: $scope.currentPage
            }
        }).success(function (result) {
            console.log(result);
            if (0 == result["status"]) {
                $scope.allMission = result["data"];
                $scope.pageAmount = result["pageAmount"];
                $scope.initPanelClass();
                $scope.initPagination();
            } else {
                commonEmit($scope, "danger", "任务列表加载失败", "operateStatus");
            }
        }).error(function () {
            commonEmit($scope, "danger", "网络连接错误，请稍后重试", "operateStatus");
        });
    };
    $scope.initPanelClass = function () {
        $scope.panelClass = [];
        angular.forEach($scope.allMission, function (mission) {
            if ('进行中' == mission.missionStatus) {
                $scope.panelClass.push({panelClass: "panel panel-default"});
            } else if ('待审核' == mission.missionStatus) {
                $scope.panelClass.push({panelClass: "panel panel-primary"});
            } else if ('已结束' == mission.missionStatus) {
                $scope.panelClass.push({panelClass: "panel panel-danger"});
            }
        })
    };
    $scope.activeIfTabEquals = function (tab) {
        return tab === $scope.currentTab ? 'active' : '';
    };
    $scope.setTab = function (tab, query, queryString) {
        $scope.currentTab = tab;
        $scope.currentPage = 1;
        switch (query) {
            case 0:
                $scope.missionWinningMode = '中标模式';
                $scope.missionMode = '任务模式';
                $scope.missionStatus = '任务状态';
                break;
            case 1:
                $scope.missionWinningMode = queryString;
                break;
            case 2:
                $scope.missionMode = queryString;
                break;
            case 3:
                $scope.missionStatus = queryString;
                break;
            default :
                $scope.missionWinningMode = '中标模式';
                $scope.missionMode = '任务模式';
                $scope.missionStatus = '任务状态';
                break;
        }
        $scope.fetchAllMission();
    };
    $scope.initPagination = function () {
        var options = {
            size: "normal",
            bootstrapMajorVersion: 3,
            currentPage: $scope.currentPage,
            numberOfPages: 5,
            totalPages: $scope.pageAmount,
            useBootstrapTooltip: true,
            tooltipTitles: function (type, page, current) {
                switch (type) {
                    case "first":
                        return "首页 <i class='icon-fast-backward icon-white'></i>";
                    case "prev":
                        return "上一页 <i class='icon-backward icon-white'></i>";
                    case "next":
                        return "下一页 <i class='icon-forward icon-white'></i>";
                    case "last":
                        return "末页 <i class='icon-fast-forward icon-white'></i>";
                    case "page":
                        return page === current ? "当前第" + page + "页 <i class='icon-file icon-white'></i>" : "跳转到第 " + page + "页 <i class='icon-file icon-white'></i>";
                }
            },
            bootstrapTooltipOptions: {
                html: true,
                placement: 'top'
            },
            itemContainerClass: function (type, page, current) {
                return (page === current) ? "active" : "pointer-cursor";
            },
            itemTexts: function (type, page) {
                switch (type) {
                    case "first":
                        return "首页";
                    case "prev":
                        return "上一页";
                    case "next":
                        return "下一页";
                    case "last":
                        return "末页";
                    case "page":
                        return page;
                }
            },
            onPageClicked: function (e, originalEvent, type, page) {
                $scope.currentPage = page;
                $scope.fetchAllMission();
            }
        };
        $('#pagination').bootstrapPaginator(options);
    }
}
function PersonalInformationCtrl($scope, $http, $location) {
    $scope.avatarPic;
    $scope.showAvatar = true;
    $scope.startX;
    $scope.startY;
    $scope.avatarCutWidth;
    $scope.avatarCutHeight;
    $scope.boundx;
    $scope.boundy;
    $scope.avatarWidth = 140;
    $scope.avatarHeight = 140;
    $scope.jcrop_api;
    $scope.user;
    $scope.doInit = function () {
        commonLoginValidate($scope, $http, function redirectToIndex(userShow, user) {
            if (!userShow) {
                commonEmit($scope, "danger", "请登录后操作", "operateStatus");
                $location.path('/');
            }
            $scope.user = user;
        });
    };
    $scope.initJcrop = function () {
        $('#avatarImg').Jcrop({
            onChange: $scope.showPreview,
            onSelect: $scope.showPreview,
            aspectRatio: 1
        }, function () {
            var bounds = this.getBounds();
            $scope.boundx = bounds[0];
            $scope.boundy = bounds[1];
            $scope.jcrop_api = this;
        });
    };
    $scope.showPreview = function (coords) {
        $scope.startX = coords.x;
        $scope.startY = coords.y;
        $scope.avatarCutWidth = coords.w;
        $scope.avatarCutHeight = coords.h;
        var rx = $scope.avatarWidth / coords.w;
        var ry = $scope.avatarHeight / coords.h;
        $('#avatarPreview').css({
            width: Math.round(rx * $scope.boundx) + 'px',
            height: Math.round(ry * $scope.boundy) + 'px',
            marginLeft: '-' + Math.round(rx * coords.x) + 'px',
            marginTop: '-' + Math.round(ry * coords.y) + 'px'
        });
    };
    function modifyCoverImagePath(src) {
        $scope.initJcrop();
        var imgWidth;
        var imgHeight;
        $("<img/>").attr("src", src).load(function () {
            imgWidth = this.width;
            imgHeight = this.height;
            if (imgWidth < $scope.avatarWidth || imgHeight < $scope.avatarHeight) {
                alert("所选的图片太小,不能制作头像");
            }
            else {
                $scope.jcrop_api.setImage(src);
                $('#avatarImg').attr('src', src);
                $('#avatarPreview').attr('src', src);
            }
        });
    }

    $scope.updateUserForm = function () {
        $("#pleaseWaitDialog").modal();
        $http.post('/user/updatePersonalInformation', {
            id: $scope.user.id,
            userName: $scope.user.userName,
            email: $scope.user.email,
            password: $scope.user.password,
            school: $scope.user.school,
            college: $scope.user.college,
            specialty: $scope.user.specialty,
            grade: $scope.user.grade,
            qq: $scope.user.qq,
            telephone: $scope.user.telephone,
            avatar: $scope.user.avatar
        }).success(function (result) {
            $("#pleaseWaitDialog").modal('hide');
            console.log(result);
            if (0 == result["status"]) {
                commonEmit($scope, "success", "个人资料更新成功", "operateStatus");
            } else {
                commonEmit($scope, "danger", "个人资料更新失败", "operateStatus");
            }
        }).error(function () {
            $("#pleaseWaitDialog").modal('hide');
            commonEmit($scope, "danger", "网络连接错误，请稍后重试", "operateStatus");
        });
    };
    $scope.clickPic = function () {
        $('#avatar').click();
    };
    $scope.uploadAvatar = function (files) {
        $("#pleaseWaitDialog").modal();
        uploadImages($http, files, '/user/uploadAvatarPic', function (result) {
            console.log(result);
            $("#pleaseWaitDialog").modal('hide');
            if (0 == result["status"]) {
                $scope.showAvatar = false;
                $scope.avatarPic = result["data"][0];
                modifyCoverImagePath($scope.avatarPic);
            } else {
                commonEmit($scope, "danger", "图片上传失败", "operateStatus");
            }
        });
    };
    $scope.updateAvatar = function () {
        $("#pleaseWaitDialog").modal();
        $http({
            method: 'GET',
            url: '/user/updateAvatar',
            params: {
                id: $scope.user.id,
                avatarPic: $scope.avatarPic,
                startX: $scope.startX,
                startY: $scope.startY,
                avatarCutWidth: $scope.avatarCutWidth,
                avatarCutHeight: $scope.avatarCutHeight
            }
        }).success(function (result) {
            $("#pleaseWaitDialog").modal('hide');
            console.log(result);
            if (0 == result["status"]) {
                $scope.user.avatar = result["data"];
                $scope.showAvatar = true;
            } else {
                commonEmit($scope, "danger", "头像更新失败", "operateStatus");
            }
        }).error(function () {
            $("#pleaseWaitDialog").modal('hide');
            commonEmit($scope, "danger", "网络连接错误，请稍后重试", "operateStatus");
        });
    }
}
function MineMissionAcceptCtrl($scope, $http, $location) {
    $scope.pageAmount = 1;
    $scope.currentTab = 0;
    $scope.currentPage = 1;
    $scope.missionWinningMode = '中标模式';
    $scope.missionMode = '任务模式';
    $scope.missionStatus = '任务状态';
    $scope.missionAccept = [];
    $scope.panelClass = [];
    $scope.doInit = function () {
        commonLoginValidate($scope, $http, function redirectToIndex(userShow) {
            if (!userShow) {
                commonEmit($scope, "danger", "请登录后操作", "operateStatus");
                $location.path('/');
            }
        });
        $scope.fetchMissionByUserId();
    };
    $scope.fetchMissionByUserId = function () {
        $http({
            method: 'GET',
            url: '/mission/getAcceptMission',
            params: {
                missionWinningMode: $scope.missionWinningMode,
                missionMode: $scope.missionMode,
                missionStatus: $scope.missionStatus,
                currentPage: $scope.currentPage
            }
        }).success(function (result) {
            console.log(result);
            if (0 == result["status"]) {
                $scope.missionAccept = result["data"];
                $scope.pageAmount = result["pageAmount"];
                $scope.initPanelClass();
                $scope.initPagination();
            } else {
                commonEmit($scope, "danger", "任务列表加载失败", "operateStatus");
            }
        }).error(function () {
            commonEmit($scope, "danger", "网络连接错误，请稍后重试", "operateStatus");
        });
    };
    $scope.initPanelClass = function () {
        $scope.panelClass = [];
        angular.forEach($scope.missionAccept, function (mission) {
            if ('进行中' === mission.missionStatus) {
                $scope.panelClass.push({panelClass: "panel panel-default"});
            } else if ('待审核' === mission.missionStatus) {
                $scope.panelClass.push({panelClass: "panel panel-primary"});
            } else if ('已结束' === mission.missionStatus) {
                $scope.panelClass.push({panelClass: "panel panel-danger"});
            }
        })
    };
    $scope.activeIfTabEquals = function (tab) {
        return tab === $scope.currentTab ? 'active' : '';
    };
    $scope.setTab = function (tab, query, queryString) {
        $scope.currentTab = tab;
        $scope.currentPage = 1;
        switch (query) {
            case 0:
                $scope.missionWinningMode = '中标模式';
                $scope.missionMode = '任务模式';
                $scope.missionStatus = '任务状态';
                break;
            case 1:
                $scope.missionWinningMode = queryString;
                break;
            case 2:
                $scope.missionMode = queryString;
                break;
            case 3:
                $scope.missionStatus = queryString;
                break;
            default :
                $scope.missionWinningMode = '中标模式';
                $scope.missionMode = '任务模式';
                $scope.missionStatus = '任务状态';
                break;
        }
        $scope.fetchMissionByUserId();
    };
    $scope.initPagination = function () {
        var options = {
            size: "normal",
            bootstrapMajorVersion: 3,
            currentPage: $scope.currentPage,
            numberOfPages: 5,
            totalPages: $scope.pageAmount,
            useBootstrapTooltip: true,
            tooltipTitles: function (type, page, current) {
                switch (type) {
                    case "first":
                        return "首页 <i class='icon-fast-backward icon-white'></i>";
                    case "prev":
                        return "上一页 <i class='icon-backward icon-white'></i>";
                    case "next":
                        return "下一页 <i class='icon-forward icon-white'></i>";
                    case "last":
                        return "末页 <i class='icon-fast-forward icon-white'></i>";
                    case "page":
                        return page === current ? "当前第" + page + "页 <i class='icon-file icon-white'></i>" : "跳转到第 " + page + "页 <i class='icon-file icon-white'></i>";
                }
            },
            bootstrapTooltipOptions: {
                html: true,
                placement: 'top'
            },
            itemContainerClass: function (type, page, current) {
                return (page === current) ? "active" : "pointer-cursor";
            },
            itemTexts: function (type, page) {
                switch (type) {
                    case "first":
                        return "首页";
                    case "prev":
                        return "上一页";
                    case "next":
                        return "下一页";
                    case "last":
                        return "末页";
                    case "page":
                        return page;
                }
            },
            onPageClicked: function (e, originalEvent, type, page) {
                $scope.currentPage = page;
                $scope.fetchMissionByUserId();
            }
        };
        $('#pagination').bootstrapPaginator(options);
    };
    $scope.showEmpty = function () {
        return $scope.currentTab == 0 && $scope.missionAccept.length == 0;
    }
}

function MineMissionPublishCtrl($scope, $http, $location) {
    $scope.pageAmount = 1;
    $scope.currentTab = 0;
    $scope.currentPage = 1;
    $scope.missionWinningMode = '中标模式';
    $scope.missionMode = '任务模式';
    $scope.missionStatus = '任务状态';
    $scope.missionPublish = [];
    $scope.panelClass = [];
    $scope.doInit = function () {
        commonLoginValidate($scope, $http, function redirectToIndex(userShow) {
            if (!userShow) {
                commonEmit($scope, "danger", "请登录后操作", "operateStatus");
                $location.path('/');
            }
        });
        $scope.fetchMissionByMissionPublisherId();
    };
    $scope.fetchMissionByMissionPublisherId = function () {
        $http({
            method: 'GET',
            url: '/mission/mine/missionPublish',
            params: {
                missionWinningMode: $scope.missionWinningMode,
                missionMode: $scope.missionMode,
                missionStatus: $scope.missionStatus,
                currentPage: $scope.currentPage
            }
        }).success(function (result) {
            console.log(result);
            if (0 == result["status"]) {
                $scope.missionPublish = result["data"];
                $scope.pageAmount = result["pageAmount"];
                $scope.initPanelClass();
                $scope.initPagination();
            } else {
                commonEmit($scope, "danger", "任务列表加载失败", "operateStatus");
            }
        }).error(function () {
            commonEmit($scope, "danger", "网络连接错误，请稍后重试", "operateStatus");
        });
    };
    $scope.initPanelClass = function () {
        $scope.panelClass = [];
        angular.forEach($scope.missionPublish, function (mission) {
            if ('进行中' === mission.missionStatus) {
                $scope.panelClass.push({panelClass: "panel panel-default"});
            } else if ('待审核' === mission.missionStatus) {
                $scope.panelClass.push({panelClass: "panel panel-primary"});
            } else if ('已结束' === mission.missionStatus) {
                $scope.panelClass.push({panelClass: "panel panel-danger"});
            }
        })
    };
    $scope.activeIfTabEquals = function (tab) {
        return tab === $scope.currentTab ? 'active' : '';
    };
    $scope.setTab = function (tab, query, queryString) {
        $scope.currentTab = tab;
        $scope.currentPage = 1;
        switch (query) {
            case 0:
                $scope.missionWinningMode = '中标模式';
                $scope.missionMode = '任务模式';
                $scope.missionStatus = '任务状态';
                break;
            case 1:
                $scope.missionWinningMode = queryString;
                break;
            case 2:
                $scope.missionMode = queryString;
                break;
            case 3:
                $scope.missionStatus = queryString;
                break;
            default :
                $scope.missionWinningMode = '中标模式';
                $scope.missionMode = '任务模式';
                $scope.missionStatus = '任务状态';
                break;
        }
        $scope.fetchMissionByMissionPublisherId();
    };
    $scope.initPagination = function () {
        var options = {
            size: "normal",
            bootstrapMajorVersion: 3,
            currentPage: $scope.currentPage,
            numberOfPages: 5,
            totalPages: $scope.pageAmount,
            useBootstrapTooltip: true,
            tooltipTitles: function (type, page, current) {
                switch (type) {
                    case "first":
                        return "首页 <i class='icon-fast-backward icon-white'></i>";
                    case "prev":
                        return "上一页 <i class='icon-backward icon-white'></i>";
                    case "next":
                        return "下一页 <i class='icon-forward icon-white'></i>";
                    case "last":
                        return "末页 <i class='icon-fast-forward icon-white'></i>";
                    case "page":
                        return page === current ? "当前第" + page + "页 <i class='icon-file icon-white'></i>" : "跳转到第 " + page + "页 <i class='icon-file icon-white'></i>";
                }
            },
            bootstrapTooltipOptions: {
                html: true,
                placement: 'top'
            },
            itemContainerClass: function (type, page, current) {
                return (page === current) ? "active" : "pointer-cursor";
            },
            itemTexts: function (type, page) {
                switch (type) {
                    case "first":
                        return "首页";
                    case "prev":
                        return "上一页";
                    case "next":
                        return "下一页";
                    case "last":
                        return "末页";
                    case "page":
                        return page;
                }
            },
            onPageClicked: function (e, originalEvent, type, page) {
                $scope.currentPage = page;
                $scope.fetchMissionByMissionPublisherId();
            }
        };
        $('#pagination').bootstrapPaginator(options);
    };
    $scope.showEmpty = function () {
        return $scope.currentTab == 0 && $scope.missionPublish.length == 0;
    }
}

function MissionCreateCtrl($scope, $http, $location, $routeParams, $filter) {
    $scope.missionUserRelationships = [];
    $scope.userAcceptMission = [];
    $scope.user = {};
    $scope.missionDetail = {};
    $scope.mission = {};
    $scope.missionPublisher = {};
    $scope.missionUserRelationship = {};
    $scope.mission.missionTitle;
    $scope.mission.missionDescription;
    $scope.mission.missionEndDate = $filter('date')(new Date(), 'yyyy-MM-dd');
    $scope.mission.missionWinningMode = '单人中标';
    $scope.mission.missionMode = '训练模式';
    $scope.mission.missionReward = 0;
    $scope.mission.missionData;
    $scope.mission.missionDataName;
    $scope.mission.id = $routeParams["missionId"];
    $scope.doInit = function () {
        commonLoginValidate($scope, $http, function redirectToIndex(userShow, user) {
            if (!userShow) {
                commonEmit($scope, "danger", "请登录后操作", "operateStatus");
                $location.path('/');
            }
            $scope.user = user;
        });
        $(".form_datetime").datetimepicker({
            format: "yyyy-mm-dd",
            autoclose: true,
            todayBtn: true,
            pickerPosition: "bottom-left",
            minView: 1,
            startDate: $scope.mission.missionEndDate
        });
        $scope.initMission();
        $scope.initMissionUserRelationship();
    };
    $scope.initMission = function () {
        if (-1 != $scope.mission.id) {
            $http({
                method: 'GET',
                url: '/mission/getMissionById',
                params: {
                    missionId: $scope.mission.id
                }
            }).success(function (result) {
                console.log(result);
                if (0 == result["status"]) {
                    $scope.missionDetail = result["data"];
                    $scope.mission = $scope.missionDetail["missionModel"];
                    $scope.missionPublisher = $scope.missionDetail["missionPublisher"];
                    $scope.missionUserRelationship = $scope.missionDetail["missionUserRelationshipModel"];
                } else {
                    commonEmit($scope, "danger", "任务详细信息获取失败", "operateStatus");
                }
            }).error(function () {
                commonEmit($scope, "danger", "网络连接错误，请稍后重试", "operateStatus");
            });
        }
    };
    $scope.initMissionUserRelationship = function () {
        if (-1 != $scope.mission.id) {
            $http({
                method: 'GET',
                url: '/mission/getMissionUserRelationshipByMissionId',
                params: {
                    missionId: $scope.mission.id
                }
            }).success(function (result) {
                console.log(result);
                if (0 == result["status"]) {
                    $scope.missionUserRelationships = result["data"];
                    $scope.userAcceptMission = result["userModelList"];
                } else {
                    commonEmit($scope, "danger", "任务作品获取失败", "operateStatus");
                }
            }).error(function () {
                commonEmit($scope, "danger", "网络连接错误，请稍后重试", "operateStatus");
            });
        }
    };
    $scope.publishMission = function () {
        $("#pleaseWaitDialog").modal();
        $http.post('/mission/publishMission', {
            missionTitle: $scope.mission.missionTitle,
            missionDescription: $scope.mission.missionDescription,
            missionBeginDate: $filter('date')(new Date(), 'yyyy-MM-dd HH:mm:ss'),
            missionEndDate: $filter('date')($scope.mission.missionEndDate, 'yyyy-MM-dd HH:mm:ss'),
            missionWinningMode: $scope.mission.missionWinningMode,
            missionMode: $scope.mission.missionMode,
            missionReward: $scope.mission.missionReward,
            missionData: $scope.mission.missionData,
            missionDataName: $scope.mission.missionDataName
        }).success(function (result) {
            console.log(result);
            $("#pleaseWaitDialog").modal('hide');
            if (0 == result["status"]) {
                commonEmit($scope, "success", "发布任务成功", "operateStatus");
                $location.path('/mine/missionPublish');
            } else {
                commonEmit($scope, "danger", "发布任务失败", "operateStatus");
            }
        }).error(function () {
            $("#pleaseWaitDialog").modal('hide');
            commonEmit($scope, "danger", "网络连接错误，请稍后重试", "operateStatus");
        });
    };
    $scope.updateMission = function () {
        $("#pleaseWaitDialog").modal();
        $http.post('/mission/updateMission', {
            id: $scope.mission.id,
            missionTitle: $scope.mission.missionTitle,
            missionDescription: $scope.mission.missionDescription,
            missionBeginDate: $filter('date')(new Date(), 'yyyy-MM-dd HH:mm:ss'),
            missionEndDate: $filter('date')($scope.mission.missionEndDate, 'yyyy-MM-dd HH:mm:ss'),
            missionWinningMode: $scope.mission.missionWinningMode,
            missionMode: $scope.mission.missionMode,
            missionReward: $scope.mission.missionReward,
            missionPublisherId: $scope.mission.missionPublisherId,
            missionStatus: $scope.mission.missionStatus,
            missionData: $scope.mission.missionData,
            missionDataName: $scope.mission.missionDataName
        }).success(function (result) {
            console.log(result);
            $("#pleaseWaitDialog").modal('hide');
            if (0 == result["status"]) {
                commonEmit($scope, "success", "更新任务成功", "operateStatus");
            } else {
                commonEmit($scope, "danger", "更新任务失败", "operateStatus");
            }
        }).error(function () {
            $("#pleaseWaitDialog").modal('hide');
            commonEmit($scope, "danger", "网络连接错误，请稍后重试", "operateStatus");
        });
    };
    $scope.clickMissionData = function () {
        $('#missionData').click();
    };
    $scope.uploadMissionData = function (files) {
        $("#pleaseWaitDialog").modal();
        $scope.mission.missionDataName = files[0].name;
        uploadImages($http, files, '/manager/file/uploadZipFile', function (result) {
            console.log(result);
            $("#pleaseWaitDialog").modal('hide');
            if (0 == result["status"]) {
                commonEmit($scope, "success", "zip文件上传成功", "operateStatus");
                $scope.mission.missionData = result["data"][0];
            } else {
                commonEmit($scope, "danger", "zip文件上传失败", "operateStatus");
            }
        });
    };
    $scope.showUpdateMissionBtn = function () {
        return $scope.mission.id != -1 && $scope.mission.missionStatus == '进行中';
    };
    $scope.showBidBtn = function (status) {
        return $scope.mission.missionStatus == '待审核' && status != '中标';
    };
    $scope.showBidStatus = function (status) {
        return status == '中标';
    };
    $scope.disableUpdate = function () {
        return $scope.mission.missionStatus == '待审核' || $scope.mission.missionStatus == '已结束';
    };
    $scope.getStatusLabelClass = function (status) {
        return status == $scope.mission.missionStatus ? 'label label-success' : 'label label-default';
    };
    $scope.getMissionSubmitLabelClass = function (status) {
        return status == '中标' ? 'panel panel-primary' : 'panel panel-default';
    };
    $scope.bidRelationship = function (relationship) {
        $http({
            method: 'GET',
            url: '/mission/bidRelationship',
            params: {
                id: relationship.id,
                missionId: $scope.mission.id,
                missionWinningMode: $scope.mission.missionWinningMode
            }
        }).success(function (result) {
            console.log(result);
            if (0 == result["status"]) {
                commonEmit($scope, "success", "任务作品中标选中成功", "operateStatus");
                $scope.initMission();
                $scope.initMissionUserRelationship();
            } else {
                commonEmit($scope, "danger", "任务作品中标选中失败", "operateStatus");
            }
        }).error(function () {
            commonEmit($scope, "danger", "网络连接错误，请稍后重试", "operateStatus");
        });
    }
}

function MissionDetailCtrl($scope, $http, $location, $routeParams) {
    $scope.user = {};
    $scope.missionDetail = {};
    $scope.mission = {};
    $scope.missionPublisher = {};
    $scope.missionUserRelationship = {};
    $scope.mission.id = $routeParams["missionId"];
    $scope.doInit = function () {
        commonLoginValidate($scope, $http, function redirectToIndex(userShow, user) {
            if (!userShow) {
                commonEmit($scope, "danger", "请登录后操作", "operateStatus");
                $location.path('/');
            }
            $scope.user = user;
        });
        $scope.initMission();
    };
    $scope.initMission = function () {
        if (-1 != $scope.mission.id) {
            $http({
                method: 'GET',
                url: '/mission/getMissionById',
                params: {
                    missionId: $scope.mission.id
                }
            }).success(function (result) {
                console.log(result);
                if (0 == result["status"]) {
                    $scope.missionDetail = result["data"];
                    $scope.mission = $scope.missionDetail["missionModel"];
                    $scope.missionPublisher = $scope.missionDetail["missionPublisher"];
                    $scope.missionUserRelationship = $scope.missionDetail["missionUserRelationshipModel"];
                } else {
                    commonEmit($scope, "danger", "任务详细信息获取失败", "operateStatus");
                }
            }).error(function () {
                commonEmit($scope, "danger", "网络连接错误，请稍后重试", "operateStatus");
            });
        }
    };
    $scope.acceptMission = function () {
        $http({
            method: 'GET',
            url: '/mission/acceptMission',
            params: {
                missionId: $scope.mission.id
            }
        }).success(function (result) {
            console.log(result);
            if (0 == result["status"]) {
                commonEmit($scope, "success", "接受任务成功", "operateStatus");
                $scope.initMission();
            } else {
                commonEmit($scope, "danger", "接受任务失败", "operateStatus");
            }
        }).error(function () {
            commonEmit($scope, "danger", "网络连接错误，请稍后重试", "operateStatus");
        });
    };
    $scope.clickMissionSubmitData = function () {
        $('#missionSubmitData').click();
    };
    $scope.uploadMissionSubmitData = function (files) {
        $("#pleaseWaitDialog").modal();
        $scope.missionUserRelationship.missionSubmitDataName = files[0].name;
        console.log("uploadMissionSubmitData---" + $scope.missionUserRelationship.missionSubmitDataName);
        uploadImages($http, files, '/manager/file/uploadZipFile', function (result) {
            console.log(result);
            $("#pleaseWaitDialog").modal('hide');
            if (0 == result["status"]) {
                commonEmit($scope, "success", "zip文件上传成功", "operateStatus");
                $scope.missionUserRelationship.missionSubmitData = result["data"][0];
            } else {
                commonEmit($scope, "danger", "zip文件上传失败", "operateStatus");
            }
        });
    };
    $scope.submitMission = function () {
        $http({
            method: 'GET',
            url: '/mission/submitMission',
            params: {
                id: $scope.missionUserRelationship.id,
                missionSubmitData: $scope.missionUserRelationship.missionSubmitData,
                missionSubmitDataName: $scope.missionUserRelationship.missionSubmitDataName
            }
        }).success(function (result) {
            console.log(result);
            if (0 == result["status"]) {
                commonEmit($scope, "success", "任务作品提交成功", "operateStatus");
                $scope.initMission();
            } else {
                commonEmit($scope, "danger", "任务作品提交失败", "operateStatus");
            }
        }).error(function () {
            commonEmit($scope, "danger", "网络连接错误，请稍后重试", "operateStatus");
        });
    };
    $scope.showAcceptMissionBtn = function () {
        return $scope.missionPublisher.id != $scope.user.id && ($scope.missionUserRelationship == null || $scope.missionUserRelationship.userId != $scope.user.id) && $scope.mission.missionStatus == '进行中';
    };
    $scope.showSubmitMissionBtn = function () {
        return $scope.missionUserRelationship != null && $scope.missionUserRelationship.userId == $scope.user.id && $scope.mission.missionStatus == '进行中' && $scope.missionUserRelationship.missionCompletionStatus != '已提交';
    };
    $scope.getStatusLabelClass = function (status) {
        return status == $scope.mission.missionStatus ? 'label label-success' : 'label label-default';
    };
}

function ManagerSettingCtrl($scope, $http, $location) {
    $scope.submitBtnGroup = [false, false, false, false, false, false];
    $scope.addRecommendPic = 'http://scuwitkey-scuwitkeydomain.stor.sinaapp.com/Penguins.jpg';//todo 修改添加图片
    $scope.defaultRecommendPic = 'http://scuwitkey-scuwitkeydomain.stor.sinaapp.com/Penguins.jpg';//todo 修改默认图片
    $scope.currentIndex;
    $scope.recommends = [];
    $scope.doInit = function () {
        commonLoginValidate($scope, $http, function redirectToIndex(userShow) {
            if (!userShow) {
                commonEmit($scope, "danger", "请登录后操作", "operateStatus");
                $location.path('/');
            }
        });
        $scope.fetchRecommends();
        $scope.fetchDefaultImage();
    };
    $scope.fetchDefaultImage = function () {
        $http({
            method: 'GET',
            url: '/manager/fetchDefaultImage'
        }).success(function (result) {
            console.log(result);
            $scope.addRecommendPic = result["addRecommendPic"];
            $scope.defaultRecommendPic = result["defaultRecommendPic"];
        });
    };
    $scope.fetchRecommends = function () {
        $http({
            method: 'GET',
            url: '/index/fetchRecommends'
        }).success(function (result) {
            console.log(result);
            $scope.recommends = result;
        });
    };
    $scope.clickPic = function (index) {
        $scope.currentIndex = index;
        $('#file' + index).click();
    };
    $scope.recommendForm = function (index) {
        console.log("recommendForm --- " + index);
        $("#pleaseWaitDialog").modal();
        $http.post('/manager/publishRecommend', {
            id: index,
            imgUrl: $scope.recommends[index].imgUrl,
            title: $scope.recommends[index].title,
            description: $scope.recommends[index].description
        }).success(function (result) {
            console.log(result);
            $("#pleaseWaitDialog").modal('hide');
            if (0 == result["status"]) {
                commonEmit($scope, "success", "发布新闻成功", "operateStatus");
                $scope.hideSubmitBtn(index);
            } else {
                commonEmit($scope, "danger", "发布新闻失败", "operateStatus");
            }
        }).error(function () {
            $("#pleaseWaitDialog").modal('hide');
            commonEmit($scope, "danger", "网络连接错误，请稍后重试", "operateStatus");
        });
    };
    $scope.uploadPic = function (files) {
        $("#pleaseWaitDialog").modal();
        uploadImages($http, files, '/manager/file/uploadImage', function (result) {
            console.log(result);
            $("#pleaseWaitDialog").modal('hide');
            if (0 == result["status"]) {
                commonEmit($scope, "success", "新闻图片更新成功", "operateStatus");
                $scope.recommends[$scope.currentIndex].imgUrl = result["data"][0];
                $scope.showSubmitBtn($scope.currentIndex);
            } else {
                commonEmit($scope, "danger", "新闻图片更新失败", "operateStatus");
            }
        });
    };
    $scope.deleteRecommend = function (index) {
        console.log("deleteRecommend --- " + index);
        $http({
            method: 'GET',
            url: '/manager/deleteRecommend',
            params: {
                id: index
            }
        }).success(function (result) {
            console.log(result);
            if (0 == result["status"]) {
                commonEmit($scope, "success", "删除新闻成功", "operateStatus");
                $scope.oldRecommeds = $scope.recommends;
                $scope.recommends = [];
                angular.forEach($scope.oldRecommeds, function (recommend) {
                    if (recommend != $scope.oldRecommeds[index]) {
                        $scope.recommends.push(recommend);
                    }
                });
            } else {
                commonEmit($scope, "danger", "删除新闻失败", "operateStatus");
            }
        }).error(function () {
            commonEmit($scope, "danger", "网络连接错误，请稍后重试", "operateStatus");
        });
    };
    $scope.addRecommend = function () {
        var index = $scope.recommends.length;
        $scope.recommends.push({id: index, imgUrl: $scope.defaultRecommendPic, title: '', description: ''});
        $scope.showSubmitBtn(index);
    };
    $scope.showSubmitBtn = function (index) {
        $scope.submitBtnGroup[index] = true;
    };
    $scope.hideSubmitBtn = function (index) {
        $scope.submitBtnGroup[index] = false;
    }
}

function commonEmit($scope, status, text, emitObject) {
    var msg = {};
    msg.status = status;
    msg.text = text;
    $scope.$emit(emitObject, msg);
}
function commonLoginValidate($scope, $http, callback) {
    $scope.userShow = false;
    $scope.user;
    $http({
        method: 'GET',
        url: '/user/loginValidate'
    }).success(function (result) {
        console.log(result);
        $scope.userShow = result["userShow"];
        $scope.user = result["user"];
        if (null == $scope.user || undefined == $scope.user) {
            callback(false, null);
            return;
        }
        callback($scope.userShow, $scope.user);
    }).error(function () {
        callback(false, null);
    });
}
function uploadImages($http, files, url, callback) {
    var form = new FormData();
    for (var i = 0; i < files.length; i++) {
        form.append('files', files[i]);
    }
    $http({
        method: 'POST',
        url: url,
        headers: {
            'Content-Type': undefined
        },
        transformRequest: angular.identity,
        data: form
    }).success(callback).error(function () {
        $("#pleaseWaitDialog").modal('hide');
    });
}

AppMain.directive('picFileOnChange', function () {
    return {
        restrict: 'A',
        link: function (scope, elm, attr) {
            if (attr.type !== 'file') {
                return;
            }
            elm.bind('change', function (e) {
                var files = e.target.files;
                if (!files || files.length <= 0) {
                    return;
                }

                for (var i = 0; i < files.length; i++) {
                    var f = files[i];
                    var filename = f.name;
                    var type = filename.split('.').pop().toLowerCase();
                    if (type !== 'png' && type !== 'jpg') {
                        alert("只支持png、jpg");
                        return;
                    }
                }
                scope[attr.ngCallback](files);
            });
        }
    };
});

AppMain.directive('zipFileOnChange', function () {
    return {
        restrict: 'A',
        link: function (scope, elm, attr) {
            if (attr.type !== 'file') {
                return;
            }
            elm.bind('change', function (e) {
                var files = e.target.files;
                if (!files || files.length <= 0) {
                    return;
                }

                for (var i = 0; i < files.length; i++) {
                    var f = files[i];
                    var filename = f.name;
                    var type = filename.split('.').pop().toLowerCase();
                    if (type !== 'zip' && type !== 'rar') {
                        alert("只支持zip、rar");
                        return;
                    }
                }
                scope[attr.ngCallback](files);
            });
        }
    };
});