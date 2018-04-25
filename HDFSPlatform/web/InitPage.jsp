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
    <title>Title</title>
</head>
<body>
    <div id="header"></div>
    <div id="main">
        <form id="parameter" action="/initSystem">
            <input type="hidden" name="count" value="<%=(String)session.getAttribute("count")%>">
            <label>Choose one Algorithm</label>
            <select id="algorithm" name="algorithm">
                <option value="0">Copy</option>
                <option value="1">EVENODD</option>
                <option value="2">RDP</option>
            </select>
            <label>Parameter K</label>
            <input id="paramK" type="text" name="paramK"/>
            <label>Parameter N</label>
            <input id="paramN" type="text" name="paramN"/>
            <input id="start" type="submit" value="Start System"/>
        </form>
    </div>
</body>
</html>
