<%@ page import="Model.FileInfo" %>
<%@ page import="java.util.*" %>
<%@ page import="Model.OptInfo" %>
<%--
  Created by IntelliJ IDEA.
  User: wzh
  Date: 18-3-29
  Time: 下午12:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Opt</title>
</head>
<script type="text/javascript" src="jquery/jquery-3.2.1.min.js"></script>
<script src="bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
<script src="bootstrap-3.3.7-dist/js/jquery-3.2.1.min.js"></script>
<link rel="stylesheet" href="bootstrap-3.3.7-dist/css/bootstrap.min.css">
<link rel="stylesheet"
      href="bootstrap-3.3.7-dist/css/bootstrap-theme.min.css">
<link rel="stylesheet" href="css/Opt.css">

<%
    String resMessage = (String)session.getAttribute("message");
    String realMessage = "";
    if(resMessage!=null && !resMessage.isEmpty()) {
        realMessage = resMessage;
    }
%>
<body onload="showMessage()">

<input id="resMessage" type="hidden" value="<%=realMessage%>">
<div class="container-fluid p1">
    <table align="center">
        <tr align="center">
            <td><img src="image/sign.png"></td>
        </tr>
        <tr align="center">
            <td><label class="sign">HDFS纠删码</label></td>
        </tr>
    </table>

</div>
<div class="de" id="de">
    <div class="p2">
        <div class="titin">
            <ul class="nav nav-tabs nav-justified">
                <li role="presentation" class="active bt1"><a href="#">Delete</a></li>
                <li role="presentation" class="bt2"><a href="#">Download</a></li>
                <li role="presentation" class="bt3"><a href="#">Upload</a></li>
            </ul>
        </div>
        <div class="p3">
            <div class="part1">
                <table class="table table-striped">
                    <tr align="center">
                        <td>filename</td>
                        <td>fileSize</td>
                        <td>fileType</td>
                        <td>filePath</td>
                        <td>uploadTime</td>
                    </tr>
                    <% ArrayList<FileInfo> fileList = (ArrayList<FileInfo>) session.getAttribute("fileList"); %>
                    <% for (int i = 0; i < fileList.size(); i++) {
                    %>
                    <tr align="center">
                        <td><%=fileList.get(i).getFileName()%>
                        </td>
                        <td><%=fileList.get(i).getFileSize()%>
                        </td>
                        <td><%=fileList.get(i).getFileType()%>
                        </td>
                        <td><%=fileList.get(i).getFilePath()%>
                        </td>
                        <td><%=fileList.get(i).getUploadTime()%>
                        </td>
                    </tr>
                    <%
                        }
                    %>
                </table>
            </div>
            <div class="part2">
                <form id="deleteloadFile" action="/deleteFile" method="post">
                    <code>Delete File</code><br>
                    <div class="filenameinp">
                        <table align="center" class="ta0">
                            <tr align="center">
                                <td><label>Input file name</label></td>
                                <td><input type="hidden" name="count"
                                           value="<%=(String)session.getAttribute("count")%>">
                                    <input type="text" class="form-control" name="fileName"/></td>
                                <td><input id="delete" class="btn btn-warning" type="submit" value="Delete File"/></td>
                                <td>ex:<code>xxx</code></td>
                            </tr>
                        </table>
                    </div>

                </form>
            </div>
            <div class="part3">
                <code>result</code><br>
                <label style="margin-left: 100px;">
                    <%= realMessage%>
                    </label>
            </div>
        </div>
    </div>
    <div class="part4">
        <div id="infoPanel1">
            <form id="refreshOptList1" action="/refreshOptList" method="post">
                <input id="refresh1" type="submit" class="btn btn-warning" value="Refresh">
            </form>
            <div class="opt">
                <ul id="infoList1">
                    <% ArrayList<OptInfo> optList = (ArrayList<OptInfo>) session.getAttribute("optList"); %>
                    <% for (int i = 0; i < optList.size(); i++) {
                        OptInfo tempOpt = optList.get(i);
                    %>
                    <li><%=tempOpt.getFileName()%>|<%=tempOpt.getUserName()%>|<%=tempOpt.getAlgName()%>
                        |<%=tempOpt.getOptType()%>|<%=tempOpt.getOptTime()%>|<%=tempOpt.getOptLastTime()%>
                        |<%=tempOpt.getOptUsedBytes()%>|
                    </li>
                    <%
                        }
                    %>
                </ul>
            </div>
        </div>
    </div>
</div>
<div class="do" id="do">
    <div class="p2">
        <div class="titin">
            <ul class="nav nav-tabs nav-justified">
                <li role="presentation" class="bt1"><a href="#">Delete</a></li>
                <li role="presentation" class="active bt2"><a href="#">Download</a></li>
                <li role="presentation" class="bt3"><a href="#">Upload</a></li>
            </ul>
        </div>
        <div class="p3">
            <div class="part1">
                <table class="table table-striped">
                    <tr align="center">
                        <td>filename</td>
                        <td>fileSize</td>
                        <td>fileType</td>
                        <td>filePath</td>
                        <td>uploadTime</td>
                    </tr>
                    <% fileList = (ArrayList<FileInfo>) session.getAttribute("fileList"); %>
                    <% for (int i = 0; i < fileList.size(); i++) {
                    %>
                    <tr align="center">
                        <td><%=fileList.get(i).getFileName()%>
                        </td>
                        <td><%=fileList.get(i).getFileSize()%>
                        </td>
                        <td><%=fileList.get(i).getFileType()%>
                        </td>
                        <td><%=fileList.get(i).getFilePath()%>
                        </td>
                        <td><%=fileList.get(i).getUploadTime()%>
                        </td>
                    </tr>
                    <%
                        }
                    %>
                </table>
            </div>
            <div class="part2">
                <form id="downloadFile" action="/downloadFile" method="post">
                    <code>Download File</code><br>
                    <div class="filenameinp">
                        <table align="center" class="ta0">
                            <tr align="center">
                                <td><label>Input file name</label></td>
                                <td><input type="hidden" name="count"
                                           value="<%=(String)session.getAttribute("count")%>">
                                    <input type="text" class="form-control" name="fileName"/></td>
                                <td><input id="download" class="btn btn-warning" type="submit" value="Download File"/>
                                </td>
                                <td>ex:<code>xxx</code></td>
                            </tr>
                        </table>
                    </div>
                </form>
            </div>
            <div class="part3">
                <code>result</code><br>
                <label  class="result">
                    <%= realMessage%>
                </label>
            </div>
        </div>
    </div>
    <div class="part4">
        <div id="infoPanel2">
            <form id="refreshOptList2" action="/refreshOptList" method="post">
                <input id="refresh2" type="submit" class="btn btn-warning" value="Refresh">
            </form>
            <div class="opt">
                <ul id="infoList2">
                    <% optList = (ArrayList<OptInfo>) session.getAttribute("optList"); %>
                    <% for (int i = 0; i < optList.size(); i++) {
                        OptInfo tempOpt = optList.get(i);
                    %>
                    <li><%=tempOpt.getFileName()%>|<%=tempOpt.getUserName()%>|<%=tempOpt.getAlgName()%>
                        |<%=tempOpt.getOptType()%>|<%=tempOpt.getOptTime()%>|<%=tempOpt.getOptLastTime()%>
                        |<%=tempOpt.getOptUsedBytes()%>|
                    </li>
                    <%
                        }
                    %>
                </ul>
            </div>
        </div>
    </div>
</div>
<div class="up" id="up">
    <div class="p2">
        <div class="titin">
            <ul class="nav nav-tabs nav-justified">
                <li role="presentation" class="bt1"><a href="#">Delete</a></li>
                <li role="presentation" class="bt2"><a href="#">Download</a></li>
                <li role="presentation" class="active bt3"><a href="#">Upload</a></li>
            </ul>
        </div>
        <div class="p3">
            <div class="part1">
                <table class="table table-striped">
                    <tr align="center">
                        <td>filename</td>
                        <td>fileSize</td>
                        <td>fileType</td>
                        <td>filePath</td>
                        <td>uploadTime</td>
                    </tr>
                    <% fileList = (ArrayList<FileInfo>) session.getAttribute("fileList"); %>
                    <% for (int i = 0; i < fileList.size(); i++) {
                    %>
                    <tr align="center">
                        <td><%=fileList.get(i).getFileName()%>
                        </td>
                        <td><%=fileList.get(i).getFileSize()%>
                        </td>
                        <td><%=fileList.get(i).getFileType()%>
                        </td>
                        <td><%=fileList.get(i).getFilePath()%>
                        </td>
                        <td><%=fileList.get(i).getUploadTime()%>
                        </td>
                    </tr>
                    <%
                        }
                    %>
                </table>
            </div>
            <div class="part2">
                <code>Upload File</code><br>
                <div class="filenameinp">
                    <form id="fileForm" action="/uploadFile" method="post" enctype="multipart/form-data">
                        <input type="hidden" name="count" value="<%=(String)session.getAttribute("count")%>">
                        <table align="center" id="ta1">
                            <tr align="center">
                                <td width="33%">
                                    <div class="selectt">
                                        <label>Choose one Algorithm</label>
                                        <select id="algorithm" name="algorithm">
                                            <option value="0">Replication</option>
                                            <option value="1">EVENODD</option>
                                            <option value="2">LREVENODD</option>
                                            <option value="3">RDP</option>
                                            <option value="4">LRRDP</option>
                                        </select>
                                    </div>
                                </td>
                                <td width="15%">
                                    <label>Parameter K:</label>
                                </td>
                                <td width="25%">
                                    <div>
                                        <input id="paramK" type="text" class="form-control" name="paramK"/>
                                    </div>
                                </td>
                                <td width="12%">
                                </td>
                            </tr>
                        </table>
                        <table align="center" id="ta2">
                            <tr align="center">
                                <td width="15%">
                                    <label style="">Parameter N:</label>
                                </td>
                                <td width="25%">
                                    <input id="paramN" type="text" class="form-control" name="paramN"/>
                                </td>
                                <td width="15%">
                                    <label>Choose File</label>
                                </td>
                                <td width="28%">
                                    <input type="file" name="file">
                                </td>
                                <td width="17%">
                                    <input id="upload" type="submit" value="Upload File" class="btn btn-warning"/>
                                </td>
                            </tr>
                        </table>
                    </form>
                </div>
            </div>
            <div class="part3">
                <code>result</code><br>
                <label  class="result">
                    <%= realMessage%>
                </label>
            </div>
        </div>
    </div>
    <div class="part4">
        <div id="infoPanel">
            <form id="refreshOptList" action="/refreshOptList" method="post">
                <input id="refresh" type="submit" class="btn btn-warning" value="Refresh">
            </form>
            <div class="opt">
                <ul id="infoList">
                    <% optList = (ArrayList<OptInfo>) session.getAttribute("optList"); %>
                    <% for (int i = 0; i < optList.size(); i++) {
                        OptInfo tempOpt = optList.get(i);
                    %>
                    <li><%=tempOpt.getFileName()%>|<%=tempOpt.getUserName()%>|<%=tempOpt.getAlgName()%>
                        |<%=tempOpt.getOptType()%>|<%=tempOpt.getOptTime()%>|<%=tempOpt.getOptLastTime()%>
                        |<%=tempOpt.getOptUsedBytes()%>|
                    </li>
                    <%
                        }
                    %>
                </ul>
            </div>
        </div>
    </div>
</div>
<div class="home" id="home">
    <div class="p2">
        <div class="titin">
            <ul class="nav nav-tabs nav-justified">
                <li role="presentation" class="bt1"><a href="#">Delete</a></li>
                <li role="presentation" class="bt2"><a href="#">Download</a></li>
                <li role="presentation" class="bt3"><a href="#">Upload</a></li>
            </ul>
        </div>
        <div class="p3">
            <div class="part1">
                <table class="table table-striped">
                    <tr align="center">
                        <td>filename</td>
                        <td>fileSize</td>
                        <td>fileType</td>
                        <td>filePath</td>
                        <td>uploadTime</td>
                    </tr>
                    <% fileList = (ArrayList<FileInfo>) session.getAttribute("fileList"); %>
                    <% for (int i = 0; i < fileList.size(); i++) {
                    %>
                    <tr align="center">
                        <td><%=fileList.get(i).getFileName()%>
                        </td>
                        <td><%=fileList.get(i).getFileSize()%>
                        </td>
                        <td><%=fileList.get(i).getFileType()%>
                        </td>
                        <td><%=fileList.get(i).getFilePath()%>
                        </td>
                        <td><%=fileList.get(i).getUploadTime()%>
                        </td>
                    </tr>
                    <%
                        }
                    %>
                </table>
            </div>
            <div class="part2">
                    <code>Operation</code><br>
                <span class="prompt">Please select an operation</span>
            </div>
            <div class="part3">
                <code>result</code><br>
                <label class="result">
                    <%= realMessage%>
                </label>
            </div>
        </div>
    </div>
    <div class="part4">
        <div id="infoPanel3">
            <form id="refreshOptList3" action="/refreshOptList" method="post">
                <input id="refresh3" type="submit" class="btn btn-warning" value="Refresh">
            </form>
            <div class="opt">
                <ul id="infoList3">
                    <% optList = (ArrayList<OptInfo>) session.getAttribute("optList"); %>
                    <% for (int i = 0; i < optList.size(); i++) {
                        OptInfo tempOpt = optList.get(i);
                    %>
                    <li><%=tempOpt.getFileName()%>|<%=tempOpt.getUserName()%>|<%=tempOpt.getAlgName()%>
                        |<%=tempOpt.getOptType()%>|<%=tempOpt.getOptTime()%>|<%=tempOpt.getOptLastTime()%>
                        |<%=tempOpt.getOptPartTime()%>|<%=tempOpt.getOptUsedBytes()%>|
                    </li>
                    <%
                        }
                    %>
                </ul>
            </div>
        </div>
    </div>
</div>
<script>
    function showMessage() {
        var resMessage = document.getElementById("resMessage").value;
        if(resMessage){
            alert(resMessage);
        }
    }
    $(document).ready(function () {
        $("#de").hide();
        $("#do").hide();
        $("#up").hide();
        $(".bt3").click(function () {
            $("#de").hide();
            $("#do").hide();
            $("#up").show();
            $("#home").hide();
        });
        $(".bt1").click(function () {
            $("#de").show();
            $("#do").hide();
            $("#up").hide();
            $("#home").hide();
        });
        $(".bt2").click(function () {
            $("#de").hide();
            $("#do").show();
            $("#up").hide();
            $("#home").hide();
        });
    });
</script>
</body>
</html>
