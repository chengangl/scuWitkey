<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html ng-app="AppMain" ng-cloak class="ng-cloak">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>川大威客联盟</title>
    <link rel="stylesheet" type="text/css" href="/static/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="/static/css/bootstrap-datetimepicker.min.css">
    <link rel="stylesheet" type="text/css" href="/static/css/jquery.Jcrop.min.css">
    <link rel="icon" href="/static/images/favicon.ico" type="image/x-icon" />
    <link rel="shortcut icon" href="/static/images/favicon.ico" type="image/x-icon" />
</head>
<body ng-controller="IndexCtrl" ng-init="doInit()">
<div class="container">
    <div class="navbar navbar-default" role="navigation">
        <div class="container">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="#/">scu威客</a>
            </div>
            <div class="navbar-collapse collapse">
                <ul class="nav navbar-nav">
                    <%--<li><a href="#/example">成功案例</a></li>--%>
                    <li><a href="#/mission">任务</a></li>
                    <li class="dropdown">
                        <a href="" class="dropdown-toggle" data-toggle="dropdown">我的<b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <li role="presentation" class="dropdown-header">任务</li>
                            <li><a href="#/mine/missionAccept">我接受的任务</a></li>
                            <li><a href="#/mine/missionPublish">我发起的任务</a></li>
                            <li role="presentation" class="divider"></li>
                            <li role="presentation" class="dropdown-header">基本设置</li>
                            <li><a href="#/mine/personalInformation">个人资料</a></li>
                            <li role="presentation" class="divider" ng-show="user.userName == 'manager'"></li>
                            <li role="presentation" class="dropdown-header" ng-show="user.userName == 'manager'">系统设定
                            </li>
                            <li ng-show="user.userName == 'manager'"><a href="#/manager/setting">推荐新闻</a></li>
                        </ul>
                    </li>
                </ul>
                <ul class="nav navbar-nav navbar-right" ng-hide="userShow">
                    <li><a href="#/register">注册</a></li>
                </ul>
                <ul class="nav navbar-nav navbar-right" ng-show="userShow">
                    <li><a href="#/mine/personalInformation">{{user.email}}</a></li>
                    <li><a href="" ng-click="logOut()">退出</a></li>
                </ul>
            </div>
        </div>
    </div>
    <div id="operateModalDiv" class="modal fade">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title">提示</h4>
                </div>
                <div class="modal-body">
                    <div ng-class="alertClass">{{alertText}}</div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" data-dismiss="modal">知道了</button>
                </div>
            </div>
        </div>
    </div>
    <div class="modal fade" id="pleaseWaitDialog" data-backdrop="static" data-keyboard="false">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header"><h1 id="progressText">请等待...</h1></div>
                <div class="modal-body">
                    <div class="progress progress-striped active">
                        <div class="progress-bar" role="progressbar" aria-valuenow="100" aria-valuemin="0"
                             aria-valuemax="100" style="width: 100%">

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div ng-view ng-cloak></div>
</div>
<div id="footer">
    <div class="container well">
        <p>&copy; 2013 scuWitkey, Inc. &middot; Privacy &middot; Terms</p>
    </div>
</div>


<script type="text/javascript" src="/static/js/jquery.js"></script>
<script type="text/javascript" src="/static/js/bootstrap.min.js"></script>
<script type="text/javascript" src="/static/js/bootstrap-datetimepicker.min.js"></script>
<script type="text/javascript" src="/static/js/bootstrap-paginator.min.js"></script>
<script type="text/javascript" src="/static/js/angular.min.js"></script>
<script type="text/javascript" src="/static/js/angular-route.min.js"></script>
<script type="text/javascript" src="/static/js/jquery.Jcrop.js"></script>
<script type="text/javascript" src="/static/js/app.js"></script>
</body>
</html>