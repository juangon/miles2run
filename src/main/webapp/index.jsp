<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en" ng-app="milestogo">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="author" content="Shekhar Gulati">

    <title>MilesToGo</title>

    <link href="css/bootstrap.css" rel="stylesheet">
    <link href="css/toastr.css" rel="stylesheet">
    <link href="css/main.css" rel="stylesheet">
    <link href="//netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>

<body>

<div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="#">MilesToGo</a>
        </div>
        <div class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li class="active"><a href="#">Home</a></li>
                <li><a href="#about">About</a></li>
            </ul>

            <c:if test="${not empty sessionScope.profile}">
                <ul class="nav navbar-collapse pull-right">
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown"><img src="${sessionScope.profile.profilePic}"> ${sessionScope.profile.fullName} <b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <li><a href="<c:url value="/logout"></c:url>">Logout</a></li>
                        </ul>
                    </li>
                </ul>
            </c:if>
        </div>

        <!--/.nav-collapse -->
    </div>
</div>

<div id="main" class="container-fluid">
    <c:if test="${empty sessionScope.profile}">
        <div id="activity" class="row">
            <h2>Latest MilesToGo Activity</h2>
            <h6>See what's happening in MilesToGo around the world</h6>
            <br><br>

            <div class="col-xs-4">
                <div class="activity-developers">
                    <h1>500 Developers</h1>
                </div>

            </div>
            <div class="col-xs-4">
                <div class="activity-countries">
                    <h1>50 Countries</h1>
                </div>

            </div>
            <div class="col-xs-4">
                <div class="activity-meters">
                    <h1>500,000 Meters Run</h1>
                </div>
            </div>
        </div>
        <br><br>

        <div id="join" class="row">
            <div class="col-xs-4">
                <h2>Join the community</h2>
            </div>
            <div class="col-xs-4">
                <a href="signin/twitter" class="btn btn-lg btn-info"><i class="fa fa-twitter"></i> Sign in with Twitter</a>
            </div>
            <div class="col-xs-4">
                <a href="signin/facebook" class="btn btn-lg btn-info"><i class="fa fa-facebook"></i> Sign in with
                    Facebook</a>
            </div>

        </div>
    </c:if>
    <c:if test="${not empty sessionScope.profile}">
        <div class="row">
            <div class="col-xs-2">
                <ul class="nav nav-pills nav-stacked">
                    <li><a href="#">Timeline</a></li>
                    <li><a href="#status/new"><i class="icon-plus-sign icon-white"></i> Post Status</a></li>
                    <li><a href="#status/progress">Progress</a></li>
                </ul>
            </div>
            <div class="col-xs-offset-2 col-xs-8" ng-view>

            </div>
        </div>
    </c:if>

</div>


<script src="js/jquery.js"></script>
<script src="js/bootstrap.js"></script>
<script src="js/angular.js"></script>
<script src="js/angular-resource.js"></script>
<script src="js/toastr.js"></script>
<script src="js/app.js"></script>
</body>
</html>