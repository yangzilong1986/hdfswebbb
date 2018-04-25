<%--
  Created by IntelliJ IDEA.
  User: luke
  Date: 18-2-27
  Time: 下午7:21
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>LOGIN</title>
</head>
<script type="text/javascript" src="jquery/jquery-3.2.1.min.js"></script>
<script src="bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
<script src="bootstrap-3.3.7-dist/js/jquery-3.2.1.min.js"></script>
<link rel="stylesheet" href="bootstrap-3.3.7-dist/css/bootstrap.min.css">
<link rel="stylesheet"
      href="bootstrap-3.3.7-dist/css/bootstrap-theme.min.css">
<link rel="stylesheet" href="css/mine1.css">
<script type="text/javascript">
    $(document).ready(function () {
        $("#1").hide();
        $("#show").click(function () {
            $("#1").show(300);
        });
        $("#x").click(function () {
            $("#1").hide(300);
        });
    });
</script>
<body>
<div class="sign">
    <table align="center">
        <tr>
            <td><img src="image/sign.png"></td>
        </tr>
        <tr>
            <td>
                <br>
                <div class="p1">
                    <label id="la0">HDFS纠删码</label>
                </div>
            </td>
        </tr>
        <tr>
            <td>
                <div class="h1"></div>
            </td>
        </tr>
        <tr>
            <td>
                <div class="h2"></div>
            </td>
        </tr>
        <tr>
            <td>
                <p>This is a HDFS based erasure test table, <br>
                    you can upload, download, delete and other operations <br>
                    on the file.</p>
            </td>
        </tr>
        <tr>
            <td>
                <div class="h3"></div>
            </td>
        </tr>
        <tr>
            <td>
                <div class="h4">
                    <button type="button" class="btn btn-default" id="show">LOGIN</button>
                </div>

            </td>
        </tr>
    </table>
</div>

<div class="login" id="1">
    <nav class="navbar navbar-default tit">
        <label id="la1">Login</label>
        <button type="button" class="btn btn-default" id="x">X</button>
    </nav>
    <form class="navbar-form navbar-left" id="login" role="search" action="/login">
        <table>
            <tr>

                <td><label>Count</label></td>
            </tr>
            <tr>
                <td>
                    <input type="text" name="count" class="form-control" id="count" placeholder="count"/></td>
            </tr>
            <tr>
                <td>
            <tr>
                <td><label>Key</label></td>
            </tr>
            <td>
                <input type="password" name="key" class="form-control" id="key" placeholder="key"></td>
            </tr>
            <tr>
                <td><input type="submit" class="btn btn-default" id="tn_lo" value="Login"></td>
            </tr>
        </table>
    </form>
</div>
</body>
</html>
