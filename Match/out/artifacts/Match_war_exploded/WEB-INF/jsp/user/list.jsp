<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/4/27
  Time: 20:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <base href="${pageContext.request.contextPath}/resource/"/>
    <title>用户管理</title>

    <!-- Bootstrap core CSS -->
    <link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">

    <script src="js/jquery-1.8.3.min.js"></script>
    <script src="bootstrap/bootstrap.min.js"></script>

    <!-- dialog 弹出框 -->
    <link rel="stylesheet" href="//apps.bdimg.com/libs/jqueryui/1.10.4/css/jquery-ui.min.css"/>
    <script type="text/javascript" src="widget/dialog/jquery-ui-1.9.2.custom.min.js"></script>

    <!-- ztree 树形结构 -->
    <link rel="stylesheet" href="widget/zTree/zTreeStyle/zTreeStyle.css"/>
    <script type="text/javascript" src="widget/zTree/jquery.ztree.all.min.js"></script>

    <!-- 图片上传插件 -->
    <link rel="stylesheet" href="widget/webuploader/webuploader.css"/>
    <script type="text/javascript" src="widget/webuploader/webuploader.min.js"></script>

    <script>
        //打开添加职工的弹出框
        function open_dialog(){
            //清空数据
            $("#id").val("");
            $("#username").val("");
            $("#password").val("");
            $(":radio[name='esex'][value='1']").attr("checked", true);
            $("#age").val("");
            $("#time").val("");
            $("#email").val("");
            $("#phone").val("");
            $("#area").val("");
            $("#isadmin").val("");

            //处理头像
            $("#header_img").attr("src", "img/icons/header.jpg");
            $("#header_hidden").val("");

            //打开添加的弹出框
            $("#dialog_div").dialog({
                width:600,
                height:500,
                title:"添加",
                modal:true
            });
        }

        //弹出修改的对话框
        function show_update_Dialog(id) {
            $.get("${pageContext.request.contextPath}/user/queryOne",{id:id},function (data) {

                //回填修改数据
                $("#id").val(data.id);
                $("#username").val(data.username);
                $("#password").val(data.password);
                $(":radio[name='sex'][value='"+ data.sex + "']").attr("checked", true);
                $("#age").val(data.age);
                var time = $.datepicker.formatDate("yy-mm-dd",new Date(data.time));
                $("#time").val(time);
                $("#email").val(data.email);
                $("#phone").val(data.phone);
                $("#area").val(data.area);
                $(":radio[name='isadmin'][value='"+ data.isadmin + "']").attr("checked", true);

                //处理头像
                $("#header_img").attr("src", "${pageContext.request.contextPath}/user/img?fileName=" + data.pic);
                $("#header_hidden").val(data.pic);

                //打开修改的弹出框
                $("#dialog_div").dialog({
                    width:600,
                    height:500,
                    title:"修改",
                    modal:true
                });
            },"json")
        }


        /**
         * 给用户选择角色
         */
        function select_role(uid) {
            //设置用户id
            $("#uid").val(uid);

            //ajax获取所有角色信息
            $.ajax({
                url:"${pageContext.request.contextPath}/role/queryRole",
                type:"GET",
                data:{uid:uid},
                success:function(data){
                    //简单的树形结构
                    var setting = {
                        data: {
                            key:{
                                name: "rname"
                            }
                        },
                        view:{
                            showIcon:false
                        },
                        check:{
                            enable:true
                        }
                    };
                    var zNodes = data;
                    //初始化
                    $.fn.zTree.init($("#ztree_role_div"), setting, zNodes);


                    //弹出选择角色的弹出框
                    $("#role_dialog_div").dialog({
                        width: 300,
                        height: 400,
                        title: "选择角色",
                        modal: true
                    });
                },
                dataType:"json"
            })
        }

        //提交选择的角色
        function submitRoles(){
            //获取当前选择了哪些角色
            var ztreeObj = $.fn.zTree.getZTreeObj("ztree_role_div");
            //获得该树选择节点集合
            var nodes = ztreeObj.getCheckedNodes(true);

            //node -> <input value="rid"/>
            var html = "";
            for(var i = 0; i < nodes.length; i++){
                html += "<input name='rid' type='hidden' value='" + nodes[i].id + "'/>";
            }

            $("#roleid_div").html(html);

            //提交表单
            $("#formid").submit();
        }
    </script>
</head>
<body>
<div class="row">
    <div class="col-md-12">
        <h1>用户管理</h1>
    </div>
</div>

<div class="row" style="margin-bottom: 20px">
    <div class="col-md-1 col-md-offset-8">
        <button type="button" onclick="open_dialog();" class="btn btn-success">新增</button>
    </div>
    <%--<div class="col-md-1">
        <button type="button" class="btn btn-danger">删除</button>
    </div>--%>
</div>

<div class="row">
    <div class="col-md-12">
        <table class="table table-bordered table-hover">
            <tr>

                <td>用户名</td>
                <td>头像</td>
                <td>密码</td>
                <td>性别</td>
                <td>年龄</td>
                <td>邮箱</td>
                <td>电话</td>
                <td>身份</td>
                <td>单位</td>
                <td>编辑</td>
            </tr>
            <c:forEach items="${users}" var="user">
                <tr>
                    <td>${user.username}</td>
                    <td><img src="img/pic1.jpg" style="height: 70px; width: 100px"></td>
                    <td>${user.password}</td>
                    <td>${user.sex == 1 ? '男' : '女'}</td>
                    <td>${user.age}</td>
                    <td>${user.email}</td>
                    <td>${user.phone}</td>
                    <td><c:if test="${user.isadmin == 0}">
                        管理员
                        </c:if>
                        <c:if test="${user.isadmin == 1}">
                         普通用户
                        </c:if>
                    </td>
                    <td>${user.area}</td>
                    <td>
                        <!-- 修改 -->
                        <a href="javascript:show_update_Dialog(${user.id});" title="Edit"><img
                                src="img/icons/pencil.png" alt="Edit"/></a>

                        <!-- 删除 -->
                        <a href="${pageContext.request.contextPath}/user/deleteById/${user.id}" title="Delete">
                            <img
                                    src="img/icons/cross.png" alt="Delete"/></a>

                        <!-- 用户编辑角色 -->
                        <a href="javascript:select_role(${user.id});" title="Edit Meta">
                            <img
                                    src="img/icons/hammer_screwdriver.png"
                                    alt="Edit Meta"/></a>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>
</div>

<div class="row">
    <div class="col-md-4 col-md-offset-8">
        <%@include file="../index/page.jsp"%>
    </div>
</div>

<!-- 需要弹出的框 -->
<div id="dialog_div" style="display:none;">
    <div class="content-box-content">
        <div class="tab-content default-tab" id="tab2">
            <!-- 添加用户的表单 -->
            <form action="${pageContext.request.contextPath}/user/insert" method="post">
                <fieldset>
                    <!-- Set class to "column-left" or "column-right" on fieldsets to divide the form into columns -->
                    <p>
                        <input type="hidden" id="id" name="id" value="" />
                    </p>

                    <p>
                        <label>头像</label>
                        <img id="header_img" style="width: 100px; height: 70px;" src="img/icons/header.jpg"/>
                    <div id="select_pic">上传头像</div><p id="p_info"></p>
                    <!-- 上传头像的文件名称 -->
                    <input id="header_hidden" name="header" type="hidden"/>
                    </p>

                    <p>
                        <label>用户名</label><br>
                        <input
                                class="text-input small-input" type="text" id="username"
                                name="username"/>
                    </p>
                    <p>
                        <label>密码</label><br>
                        <input
                                class="text-input small-input" type="text" id="password"
                                name="password"/>
                    </p>
                    <p>
                        <label>性别</label>
                        <input type="radio" name="sex" value="1" checked/>
                        男
                        <input type="radio" name="sex" value="0"/>
                        女
                    </p>
                    <p>
                        <label>年龄</label><br>
                        <input class="text-input small-input" type="text" id="age"
                               name="age"/>
                    </p>
                    <p>
                        <label>身份</label>
                        <input type="radio" name="isadmin" value="0" checked/>
                        管理员
                        <input type="radio" name="isadmin" value="1"/>
                        普通用户
                    </p>
                    <p>
                        <label>注册时间</label><br>
                        <input class="text-input small-input" type="date" id="time"
                               name="time"/>
                    </p>

                    <p>
                        <label>邮箱</label><br>
                        <input
                                class="text-input small-input" type="text" id="email"
                                name="email"/>
                    </p>
                    <p>
                        <label>联系方式</label><br>
                        <input
                                class="text-input small-input" type="text" id="phone"
                                name="phone"/>
                    </p>
                    <p>
                        <label>单位</label><br>
                        <input
                                class="text-input small-input" type="text" id="area"
                                name="area"/>
                    </p>
                    <p>
                        <input class="mybutton" type="submit" value="submit"/>
                    </p>
                </fieldset>
                <div class="clear"></div>
                <!-- End .clear -->
            </form>
        </div>
        <!-- End #tab2 -->
    </div>
    <!-- End .content-box-content -->
</div>

<!-- 树形结构div -->
<div id="ztree_dialog_div" style="display:none;">
    <div id="ztree_div" class="ztree">

    </div>
</div>

<!-- 选择角色的弹出框 -->
<div id="role_dialog_div" style="display:none;">
    <div id="ztree_role_div" class="ztree">

    </div>

    <!-- 设置角色的表单 -->
    <form id="formid" action="${pageContext.request.contextPath}/user/selectRole" method="post">
        <!-- 需要设置用户id -->
        <input name="uid" id="uid" type="hidden" value=""/>
        <!-- 需要设置的角色的id集合 -->
        <div id="roleid_div"></div>

        <button type="button" onclick="submitRoles();" class="mybutton">设置角色</button>
    </form>

</div>

</div>
</div>
</body>
</html>
