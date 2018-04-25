<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<link rel="icon" href="image/sign.png" type="image/x-icon"/>

<link rel="stylesheet" href="bootstrap-3.3.7-dist/css/bootstrap.css">
<link rel="stylesheet" href="bootstrap-3.3.7-dist/css/bootstrap-theme.css">
<script src="bootstrap-3.3.7-dist/js/bootstrap.js"></script>

<script type="text/javascript"
        src="jquery/jquery-3.2.1.min.js"></script>
<script type="text/javascript"
        src="bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
<script type="text/javascript"
        src="bootstrap-3.3.7-dist/js/jquery-3.2.1.min.js"></script>
<link rel="stylesheet"
      href="bootstrap-3.3.7-dist/css/bootstrap.min.css">
<link rel="stylesheet"
      href="bootstrap-3.3.7-dist/css/bootstrap-theme.min.css">
<script type="text/javascript">
    function over() {
        $("#introduction").show();
    }

    function out() {
        $("#introduction").hide();
    }

    $(document).ready(function () {
        $("#introduction").hide();
    });
</script>
<style type="text/css">
    .title {
        margin: auto;
        width: 1000px;
    }

    .sign {
        width: 30px;
        height: 35px;
        margin-top: -7px;
    }

    .btn_tit {
        width: 80px;
        margin-left: 20px;
        text-align: center;
    }

    .search {
        margin-left: 140px;
    }

    .btn_search {
        margin-left: 20px;
    }

    .username {
        margin-left: 17px;
    }

    .beautiful {
        width: 100%;
        height: 200px;
        background-image: url("image/zz.png");
        background-size: cover;
    }

    .login {
        width: 700px;
        height: 350px;
        margin: auto;
        margin-top: 10px;
        border: 2px solid #1dc219;
        border-radius: 5px;
    }

    .login_1 {
        width: 680px;
        height: 330px;
        margin: auto;
        margin-top: 7px;
        border: 2px solid #bdbdbd;
        border-radius: 5px;
    }

    .green {
        color: #449f2d;
    }

    .ta_login {
        width: 60%;
        margin-top: 30px;
    }

    .login_txt {
        margin: auto;
        width: 350px;
    }

    .remember_me {
        margin-left: 20px;
    }

    .btn_login {
        margin-left: 20px;
        width: 60px;
        height: 30px;
        background: transparent;
        border: 1px solid #bdbdbd;
        border-radius: 4px;
    }

    .introduction {
        margin-top: -100px;
        margin-left: 630px;
    }

    .introduction_ {
        margin-top: -50px;
        margin-left: 1000px;
        width: 400px;
        height: 170px;
        border: 1px solid #bdbdbd;
        border-radius: 4px;
        background-color: #ffffff;
    }

    .inside_introduction_ {
        width: 380px;
        height: 180px;
        margin: auto;
    }

    .introduction__ {
        font-size: 120%;
    }
</style>
<body>
<nav class="navbar navbar-default">
    <div class="title">
        <a class="navbar-brand">
            <img alt="Ensure Code" src="image/sign.png" class="sign">
        </a>
        <a class="navbar-brand">
            <label>Erasure <span>Code</span></label>
        </a>
        <ul class="nav navbar-nav">
            <li class="btn_tit">
                <a id="ma_">
                    <label>Main</label>
                </a>
            </li>
            <li>
                <a href="#" class="dropdwn-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                   aria-expanded="false">Operation <span class="caret"></span></a>
                <ul class="dropdown-menu">
                    <li><a id="up_">Upload</a></li>
                    <li><a id="do_">Download</a></li>
                </ul>
            </li>
        </ul>
        <form class="navbar-form navbar-left">
            <div class="form-group">
                <input type="text" class="form-control search" placeholder="Search">
            </div>
            <button type="submit" class="btn btn-default btn_search">Submit</button>
        </form>
        <ul class="nav navbar-nav username">
            <li>
                <a href="#" class="dropdwn-toggle" data-toggle="dropdown" role="button" aria-haspopup="true"
                   aria-expanded="false">Login <span class="caret"></span></a>
                <ul class="dropdown-menu">
                    <li><a href="#">Logout</a></li>
                </ul>
            </li>
        </ul>
    </div>
</nav>
<div class="beautiful">
</div>
<div class="login">
    <div class="login_1">
        <table align="center">
            <tr>
                <td>
                    <h3 class="tit">Erasure <span class="green">Coding</span>
                        <small>performance testing platform</small>
                    </h3>
                </td>
            </tr>
        </table>
        <form action="/login" method="post">
            <table align="center" class="ta_login">
                <tr>
                    <td>
                        <span class="glyphicon glyphicon-user" aria-hidden="true"></span>
                    </td>
                    <td>
                        <input type="text" class="form-control login_txt" placeholder="Count" name="count">
                    </td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td>
                        <span class="glyphicon glyphicon-lock" aria-hidden="true"></span>
                    </td>
                    <td>
                        <input type="password" class="form-control login_txt" placeholder="Key" name="key">
                    </td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>
                        <div class="checkbox">
                            <label class="remember_me">
                                <input type="checkbox"> Remember me
                            </label>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td>&nbsp;</td>
                    <td>
                        <button type="submit" class="btn_login">
                            <span class="green">Login</span>
                        </button>
                    </td>
                </tr>
            </table>
        </form>
    </div>
    <div class="introduction">
        <span class="label label-success" onmouseover="over();" onmouseout="out();">Introduction</span>
    </div>
</div>
<div class="introduction_" id="introduction">
    <div class="inside_introduction_">
        <h3>Introduction</h3>
        <p class="introduction__">
            This is a <span class="green">Erasure Coding</span> performance testing platform
            based on HDFS. you also can upload, download, delete
            and other operations on the file.
        </p>
    </div>
</div>
</body>
</html>
