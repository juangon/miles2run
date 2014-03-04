<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="author" content="Shekhar Gulati">

    <title>Update Profile</title>

    <link href="css/bootstrap.css" rel="stylesheet">
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

        </div>
        <!--/.nav-collapse -->
    </div>
</div>

<div id="main" class="container-fluid">

    <form class="form-profile" role="form" action="" method="POST">
        <h2 class="form-profile-heading">Update your Profile</h2>
        <input type="text" name="fullName" class="form-control" placeholder="Full Name" value="${requestScope.fullName}" required autofocus>
        <input type="email" name="email" class="form-control" placeholder="Email address" value="${requestScope.email}" required>
        <input type="text" name="city" class="form-control" placeholder="City" value="${requestScope.city}" required>
        <input type="text" name="country" class="form-control" placeholder="Country" value="${requestScope.country}" required>
        <input type="text" name="bio" class="form-control" placeholder="Bio" value="${requestScope.bio}" required>
        <input type="number" name="distance" class="form-control" placeholder="Distance you want to run(in metre)" min="1000" step="1000" value="${requestScope.distance}" required>
        <input type="hidden" name="socialNetworkId" id="socialNetworkId" value="${requestScope.socialNetworkId}">
        <button class="btn btn-lg btn-primary btn-block" type="submit">Update Profile</button>
    </form>

</div>


<script src="js/jquery.js"></script>
<script src="js/bootstrap.js"></script>
</body>
</html>