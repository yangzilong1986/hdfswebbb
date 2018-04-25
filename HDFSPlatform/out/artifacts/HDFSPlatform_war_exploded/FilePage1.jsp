<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="Model.FileInfo" %>
<%@ page import="java.util.*" %>
<%@ page import="Model.OptInfo" %>
<html>
<head>
    <title>Main</title>
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
<script>
    $(document).ready(function () {
        $("#b").hide();
        $("#c").hide();
        $("#up_").click(function () {
            $("#a").hide();
            $("#c").hide();
            $("#b").show();
        });
        $("#do_").click(function () {
            $("#c").show();
            $("#a").hide();
            $("#b").hide();
        });
        $("#ma_").click(function () {
            $("#a").show();
            $("#b").hide();
            $("#c").hide();
        });
    });

    function formSubmit(n) {
        document.getElementById(n).submit()
    }
</script>
<script type="text/javascript">
    var impr_fileName = "";
    function loadBlock(de_blockName,fileName) {
        document.getElementById('ta_block').innerHTML = "";
        impr_fileName = fileName;
        var recoverFlage = "";
        var count = document.getElementById("count").value;
        var xmlhttp;
        if (de_blockName != "" && de_blockName != "recover" ) {
            var a = confirm("Are you sure?");
            if (a == false) {
                de_blockName = "";
            }
        }
        if (de_blockName == "recover") {
            recoverFlage = "recover";
            de_blockName = "";
        }
        if (window.XMLHttpRequest) {// code for IE7+, Firefox, Chrome, Opera, Safari
            xmlhttp = new XMLHttpRequest();
        }
        else {// code for IE6, IE5
            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
        }
        xmlhttp.onreadystatechange = function () {
            if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {

                var result = eval("(" + xmlhttp.responseText + ")");

                var table = document.getElementById("ta_block");
                var newtr;
                var newtd0;
                var newtd1;
                var newtd2;
                for (var i = 0; i < result.blockInfo.length; i++) {
                    newtr = table.insertRow();
                    newtd0 = newtr.insertCell();
                    newtd1 = newtr.insertCell();
                    newtd2 = newtr.insertCell();
                    newtd0.innerHTML = i + 1;
                    newtd1.innerHTML = "<span class='glyphicon glyphicon-hdd'></span>&nbsp;&nbsp;&nbsp;" + result.blockInfo[i].blockname;
                    if (result.blockInfo[i].check == 1) {
                        newtd2.innerHTML = "<button class='delete_block btn btn-default' id='delete_block' onclick='loadBlock(&quot;" + result.blockInfo[i].blockname + "&quot;,&quot;" + fileName + "&quot;)'>Delete</button>";
                    }
                    else {
                        newtd2.innerHTML = "<button class='delete_block btn btn-default' disabled='disabled' id='delete_block'>Delete</button>";
                    }
                }
            }
        }
        xmlhttp.open("GET", "/showBlock?count=" + count + "&fileName=" + fileName + "&de_blockName=" + de_blockName + "&recoverFlage=" + recoverFlage, false);
        // xmlhttp.open("GET","/showBlock?count="+count+"&fileName="+fileName,true);
        xmlhttp.send();
    }
    function getFilename(){
        var fileName=impr_fileName;
        loadBlock("recover",fileName);
    }
</script>
<script>
    var a=${error};
    if(a!=""&&a!=null){
        alert(a);
    }
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
        margin-left: 250px;
    }

    .btn_search {
        margin-left: 20px;
    }

    .username {
        margin-left: 40px;
    }

    .green {
        color: #449f2d;
    }

    .de_bl {
        color: #000;
    }

    .file_info {
        width: 1000px;
        margin: auto;
        margin-top: 10px;
    }

    .table_inside {
        margin: auto;
        margin-top: 10px;
        width: 1000px;
        height: 280px;
        overflow-y: scroll;
        position: absolute;
    }

    .tinyinterface {
        margin: auto;
        width: 1000px;
        height: 260px;
        margin-top: 10px;
        position: absolute;
    }

    .ta_file {
        width: 975px;
        height: 280px;
        text-align: center;
        margin: auto;
        border: 1px solid #eeeeee;
        border-width: 0 0 1px 0;
    }

    .ta_file td {
        height: 50px;
    }

    .slave_info_tit {
        font-size: 150%;
        margin-left: 30px;
    }

    .part {
        width: 1000px;
        margin: auto;
        margin-top: 330px;
    }

    .slave_info {
        width: 30%;
        float: left;
    }

    .ta_slave {
        margin-top: 10px;
        text-align: center;
        border: 1px solid #eeeeee;
        border-width: 0 0 1px 0;
    }

    .ta_slave td {
        height: 50px;
    }

    .jumbotron {
        width: 100%;
        height: 200px;
    }

    .operation {
        width: 70%;
        float: right;
        margin-top: 50px;
    }

    .big {
        width: 95%;
        height: 250px;
        margin-top: -25px;
        margin-left: 30px;
        border: 4px solid #eeeeee;
        border-radius: 5px;
    }

    .warning {
        font-size: 120%;
    }

    .operation_Main {
        position: absolute;
        width: 700px;
    }

    .operation_Upload {
        position: absolute;
        width: 700px;
    }

    .operation_Download {
        position: absolute;
        width: 700px;
    }

    .operation_tit {
        margin-left: 20px;
    }

    .show_opt {
        width: 100%;
        margin-top: 260px;
    }

    .input_filename {
        width: 400px;
        text-align: center;
    }

    .parameter {
        width: 250px;
    }

    .ta_upload {
        height: 100%;
        margin-left: 100px;
    }

    .btn_up {
        margin-left: 150px;
        margin-top: -30px;
    }

    .ta_inside_opt {
        margin-top: 30px;
        width: 99%;
        height: 315px;
        overflow-y: scroll;
    }

    .ta_opt {
        width: 95%;
        margin-left: 30px;
        text-align: center;
    }

    .ta_opt td {
        height: 45px;
    }

    .opt_tit_no {
        width: 99%;
    }

    .beautiful {
        width: 100%;
        height: 200px;
        background-image: url("image/zz.png");
        background-size: cover;
    }

    .tiny_interface_btn {
        height: 30px;
        background: transparent;
        border: 1px solid #bdbdbd;
        border-radius: 4px;
    }

    .delete_block {
        background: transparent;
    }

    .ta_block {
        width: 100%;
        height: 170px;
        text-align: center;
    }

    .ta_block td {
        height: 45px;
        width: 33%;
        border: 1px solid #e0e0e0;
        border-width: 0 0 1px 0;
    }

    .block_interface_introduction {
        width: 74%;
        margin: auto;
        margin-top: 5px;
        font-size: 115%;
    }

    .ta_btn {
        float: right;
    }

    .btn_close {
        margin-top: -15px;
    }

    .Operation_sign {
        background: #b4b4b4;
    }

    .ta_Operation_sign {
        margin-top: -40px;
        margin-left: 525px;
        position: absolute;
    }

    .ta_block_isinside {
        overflow-y: scroll;
        height: 190px;
    }
</style>
<% String count = (String) session.getAttribute("count");
    String[][] a = (String[][]) session.getAttribute("a");
    if (a == null) {
        a = new String[2][3];
        a[0][0] = "none";
        a[0][1] = "none";
        a[0][2] = "none";
        a[1][0] = "none";
        a[1][1] = "none";
        a[1][2] = "none";
    }%>
<script>
    function ca1() {
        $("#table_inside").animate({left: '-=2000px'}, 800);
        setTimeout("sshow()", 400);
    }

    function ca2() {
        $("#table_inside").animate({left: '+=2000px'}, 1000);
        setTimeout("hhide()", 100);
    }

    function sshow() {
        $("#tinyinterface").show("slow");
    }

    function hhide() {
        $("#tinyinterface").hide("slow");
    }

    $(document).ready(function () {
        $("#tinyinterface").hide();
    });
</script>
<body>
<nav class="navbar navbar-default">
    <div class="title">
        <a class="navbar-brand" href="#">
            <img alt="Ensure Code" src="image/sign.png" class="sign">
        </a>
        <a class="navbar-brand" href="#">
            <label>Erasure <span>Code</span></label>
        </a>
        <ul class="nav navbar-nav">
            <li class="btn_tit">
                <a href="#" id="ma_">
                    <label>Main</label>
                </a>
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
                   aria-expanded="false"><%=count%> <span class="caret"></span></a>
                <ul class="dropdown-menu">
                    <li><a href="LoginPage1.jsp">Logout</a></li>
                </ul>
            </li>
        </ul>
    </div>
</nav>
<div class="beautiful">
</div>
<div class="file_info">
    <div class="table_inside" id="table_inside">
        <label class="slave_info_tit">File <span class="green">Information</span></label>
        <table class="table table-striped ta_file">
            <tr>
                <td>FileName</td>
                <td>FileSize</td>
                <td>FileType</td>
                <td>UploadTime</td>
                <td>DeleteFile</td>
                <td>ShowBlock</td>
            </tr>
            <% ArrayList<FileInfo> fileList = (ArrayList<FileInfo>) session.getAttribute("fileList"); %>
            <% for (int i = 0, j = 50; i < fileList.size(); i++, j++) {
            %>
            <tr>
                <td><%=fileList.get(i).getFileName()%>
                </td>
                <td><%=fileList.get(i).getFileSize()%>
                </td>
                <td><%=fileList.get(i).getFileType()%>
                </td>
                <td><%=fileList.get(i).getUploadTime()%>
                </td>
                <td>
                    <form action="/deleteFile" method="post" id="<%=i%>">
                        <input type="hidden" name="count" value="<%=(String)session.getAttribute("count")%>">
                        <input type="hidden" name="fileName" value="<%=fileList.get(i).getFileName()%>">
                        <a href="#" onclick="formSubmit(<%=i%>)"><span class="de_bl"><u>Delete</u></span></a>
                    </form>
                </td>
                <td>
                    <input id="count" type="hidden" name="count" value="<%=(String)session.getAttribute("count")%>">
                    <input id="fileName" class="fileName" type="hidden" name="fileName"
                           value="<%=fileList.get(i).getFileName()%>">
                    <a href="#" id="showblock" onclick="loadBlock('','<%=fileList.get(i).getFileName()%>');ca1();"><span
                            class="green"><u>Block</u></span></a>
                </td>
            </tr>
            <%
                }
            %>
            <tr>
                <td>xxx</td>
                <td>xxx</td>
                <td>xxx</td>
                <td>xxxx.xxxxx-xx.xxx-xx</td>
                <td><a href="#"><span class="de_bl"><u>Delete</u></span></a></td>
                <td><a href="#"><span class="green"><u>Block</u></span></a></td>
            </tr>
            <tr>
                <td>xxx</td>
                <td>xxx</td>
                <td>xxx</td>
                <td>xxxx.xxxxx-xx.xxx-xx</td>
                <td><a href="#"><span class="de_bl"><u>Delete</u></span></a></td>
                <td><a href="#"><span class="green"><u>Block</u></span></a></td>
            </tr>
            <tr>
                <td>xxx</td>
                <td>xxx</td>
                <td>xxx</td>
                <td>xxxx.xxxxx-xx.xxx-xx</td>
                <td><a href="#"><span class="de_bl"><u>Delete</u></span></a></td>
                <td><a href="#"><span class="green"><u>Block</u></span></a></td>
            </tr>
            <tr>
                <td>xxx</td>
                <td>xxx</td>
                <td>xxx</td>
                <td>xxxx.xxxxx-xx.xxx-xx</td>
                <td><a href="#"><span class="de_bl"><u>Delete</u></span></a></td>
                <td><a href="#"><span class="green"><u>Block</u></span></a></td>
            </tr>
            <tr>
                <td>xxx</td>
                <td>xxx</td>
                <td>xxx</td>
                <td>xxxx.xxxxx-xx.xxx-xx</td>
                <td><a href="#"><span class="de_bl"><u>Delete</u></span></a></td>
                <td><a href="#"><span class="green"><u>Block</u></span></a></td>
            </tr>
        </table>
    </div>
    <div class="tinyinterface" id="tinyinterface">
        <label class="slave_info_tit">Exhibition of <span class="green">Blocks</span></label><br>
        <div class="ta_block_isinside">
            <table id="ta_block" class="ta_block">
                <tr>
                    <td>#</td>
                    <td>blockName</td>
                    <td>delete</td>
                </tr>
            </table>
        </div>
        <p class="block_interface_introduction">
            This interface shows all blocks of the file in the system.
            You can <span class="green">delete</span> some blocks or <span class="green">recover</span> them.<br>
        </p>
        <div class="modal-footer">
            <button type="submit" class="tiny_interface_btn" onclick="getFilename();">
                <span class="green">Recover</span>
            </button>&nbsp;&nbsp;&nbsp;&nbsp;
            <button type="submit" class="tiny_interface_btn" id="back" onclick="ca2();">Back</button>
        </div>
    </div>
</div>
<div class="part">
    <div class="slave_info">
        <label class="slave_info_tit">Slave <span class="green">Information</span></label>
        <table class="table ta_slave">
            <tr>
                <td>Slave Name</td>
                <td>Slave IP</td>
            </tr>
            <tr>
                <td><%=a[0][0]%>
                </td>
                <td><%=a[0][1]%>
                </td>
            </tr>
            <tr>
                <td>Capacity</td>
                <td>
                    <div class="progress">
                        <div class="progress-bar progress-bar-success progress-bar-striped" role="progressbar"
                             aria-valuenow="2" aria-valuemin="0" aria-valuemax="100"
                             style="min-width: 0px; width: <%=a[0][2]%>%;">
                            <%=a[0][2]%>%
                        </div>
                    </div>
                </td>
            </tr>
            <tr>
                <td>Slave Name</td>
                <td>Slave IP</td>
            </tr>
            <tr>
                <td><%=a[1][0]%>
                </td>
                <td><%=a[1][1]%>
                </td>
            </tr>
            <tr>
                <td>Capacity</td>
                <td>
                    <div class="progress">
                        <div class="progress-bar progress-bar-success progress-bar-striped" role="progressbar"
                             aria-valuenow="2" aria-valuemin="0" aria-valuemax="100"
                             style="min-width: 0px; width: <%=a[1][2]%>%;">
                            <%=a[1][2]%>%
                        </div>
                    </div>
                </td>
            </tr>
        </table>
        <div class="jumbotron">
            <span class="label label-success">Result</span>
        </div>
    </div>
    <div class="operation">
        <table class="ta_Operation_sign">
            <tr>
                <td align="right"><a href="#" id="up_"><span class="label label-success operation_tit Operation_sign">Upload</span></a>
                </td>
                <td align="right"><a href="#" id="do_"><span class="label label-success operation_tit Operation_sign">Download</span></a>
                </td>
            </tr>
        </table>
        <div class="operation_Main" id="a">
            <span class="label label-success operation_tit">Operation</span>

            <div class="big">
                <table align="center">
                    <tr>
                        <td>
                            <br><br><br>
                            <span class="warning">
				                Please select an <span class="green">operation</span>
			                </span>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
        <div class="operation_Upload" id="b">
            <span class="label label-success Operation_tit">Upload</span>
            <div class="big">
                <form id="fileForm" action="/uploadFile" method="post" enctype="multipart/form-data">
                    <input type="hidden" name="count"
                           value="<%=(String)session.getAttribute("count")%>">
                    <table class="ta_upload">
                        <tr>
                            <td height="25%">
                                <span>Choose one Algorithm</span>
                                <select id="algorithm" name="algorithm" class="btn btn-default">
                                    <option value="0">Replication</option>
                                    <option value="1">EVENODD</option>
                                    <option value="2">LREVENODD</option>
                                    <option value="3">RDP</option>
                                    <option value="4">LRRDP</option>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td height="50%">
                                <table>
                                    <tr>
                                        <td width="50%">parameter K:&nbsp;&nbsp;</td>
                                        <td><input id="paramK" type="text" class="form-control parameter" name="paramK">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td width="50%">parameter N:&nbsp;&nbsp;</td>
                                        <td><input id="paramN" type="text" class="form-control parameter" name="paramN">
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td height="25%">
                                <table>
                                    <tr>
                                        <td>
                                            <input type="file" name="file">
                                        </td>
                                        <td>
                                            <input type="submit" class="btn btn-default btn_up" value="Upload">
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </form>
            </div>
        </div>
        <div class="operation_Download" id="c">
            <span class="label label-success Operation_tit">Download</span>
            <div class="big">
                <br><br>
                <form id="downloadFile" action="/downloadFile" method="post">
                    <table align="center">
                        <tr>
                            <td>
                                <input type="text" class="form-control input_filename" placeholder="File Name"
                                       name="fileName">
                                <input type="hidden" name="count"
                                       value="<%=(String)session.getAttribute("count")%>">
                            </td>
                        </tr>
                    </table>
                    <br>
                    <table align="center">
                        <tr>
                            <td align="center">
                                <input type="submit" class="btn btn-default" value="Download">
                            </td>
                            <td>&nbsp;&nbsp;</td>
                            <td>&nbsp;&nbsp;</td>
                            <td>
                                <span>ex:<span class="green">xxx</span></span>
                            </td>
                        </tr>
                    </table>
                </form>
            </div>
        </div>
        <div class="show_opt">
            <table class="opt_tit_no">
                <tr>
                    <td>
                        <label class="slave_info_tit">Operation <span class="green">Log</span></label>
                    </td>
                    <td align="right">
                        <input type="submit" class="btn btn-default" value="Refresh">
                    </td>
                </tr>
            </table>
            <div class="ta_inside_opt">
                <table class="ta_opt table table-bordered">
                    <tr>
                        <td>FileName</td>
                        <td>Count</td>
                        <td>Algorithm</td>
                        <td>Operation</td>
                        <td>Time</td>
                        <td>LastTime</td>
                        <td>UsedBytes</td>
                    </tr>
                    <% ArrayList<OptInfo> optList = (ArrayList<OptInfo>) session.getAttribute("optList"); %>
                    <% for (int i = 0; i < optList.size(); i++) {
                        OptInfo tempOpt = optList.get(i);
                    %>
                    <tr>
                        <td><%=tempOpt.getFileName()%>
                        </td>
                        <td><%=tempOpt.getUserName()%>
                        </td>
                        <td><%=tempOpt.getAlgName()%>
                        </td>
                        <td><%=tempOpt.getOptType()%>
                        </td>
                        <td><%=tempOpt.getOptTime()%>
                        </td>
                        <td><%=tempOpt.getOptLastTime()%>
                        </td>
                        <td><%=tempOpt.getOptUsedBytes()%>
                        </td>
                    </tr>
                    <%
                        }
                    %>
                    <tr>
                        <td>xxx</td>
                        <td>xxx</td>
                        <td>xxx</td>
                        <td>xxx</td>
                        <td>xxxx.xxxx.xxx</td>
                        <td>xxx</td>
                        <td>xxx</td>
                    </tr>
                    <tr>
                        <td>xxx</td>
                        <td>xxx</td>
                        <td>xxx</td>
                        <td>xxx</td>
                        <td>xxxx.xxxx.xxx</td>
                        <td>xxx</td>
                        <td>xxx</td>
                    </tr>
                    <tr>
                        <td>xxx</td>
                        <td>xxx</td>
                        <td>xxx</td>
                        <td>xxx</td>
                        <td>xxxx.xxxx.xxx</td>
                        <td>xxx</td>
                        <td>xxx</td>
                    </tr>
                    <tr>
                        <td>xxx</td>
                        <td>xxx</td>
                        <td>xxx</td>
                        <td>xxx</td>
                        <td>xxxx.xxxx.xxx</td>
                        <td>xxx</td>
                        <td>xxx</td>
                    </tr>
                    <tr>
                        <td>xxx</td>
                        <td>xxx</td>
                        <td>xxx</td>
                        <td>xxx</td>
                        <td>xxxx.xxxx.xxx</td>
                        <td>xxx</td>
                        <td>xxx</td>
                    </tr>
                    <tr>
                        <td>xxx</td>
                        <td>xxx</td>
                        <td>xxx</td>
                        <td>xxx</td>
                        <td>xxxx.xxxx.xxx</td>
                        <td>xxx</td>
                        <td>xxx</td>
                    </tr>
                    <tr>
                        <td>xxx</td>
                        <td>xxx</td>
                        <td>xxx</td>
                        <td>xxx</td>
                        <td>xxxx.xxxx.xxx</td>
                        <td>xxx</td>
                        <td>xxx</td>
                    </tr>
                    <tr>
                        <td>xxx</td>
                        <td>xxx</td>
                        <td>xxx</td>
                        <td>xxx</td>
                        <td>xxxx.xxxx.xxx</td>
                        <td>xxx</td>
                        <td>xxx</td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>
