<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
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
    <link rel="stylesheet" href="plugins/layui/css/layui.css" media="all">

    <!-- Bootstrap core CSS -->
    <link rel="stylesheet" href="bootstrap/bootstrap.min.css" >
    <link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">

    <script type="text/javascript"
            src="js/jquery-1.8.3.min.js"></script>
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

    <style>
        #select_pic div:nth-child(2){width:100%!important;height:100%!important;}
    </style>

    <script>
        //打开添加职工的弹出框
        function open_dialog(){
            //清空数据
            $("#id").val("");
            $("#username").val("");
            $("#password").val("");
            $("#name").val("");
            $(":radio[name='esex'][value='1']").attr("checked", true);
            $("#age").val("");
            $("#time").val("");
            $("#email").val("");
            $("#phone").val("");
            $("#area").val("");

            //处理角色
            $("#btn").val("请选择");
            $("#rid").val("");

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
            $.get("${pageContext.request.contextPath}/user/queryOneCase",{id:id},function (data) {
                alert(data)
                //回填修改数据
                $("#id").val(data.id);
                $("#username").val(data.username);
                $("#password").val(data.password);
                $("#name").val(data.name);
                $(":radio[name='sex'][value='"+ data.sex + "']").attr("checked", true);
                $("#age").val(data.age);
                var time = $.datepicker.formatDate("yy-mm-dd",new Date(data.time));
                $("#time").val(time);
                $("#email").val(data.email);
                $("#phone").val(data.phone);
                $("#area").val(data.area);

                //处理角色
                $("#rid").val(data.rid);

                //处理头像
                $("#header_img").attr("src", "${pageContext.request.contextPath}/file/img/" + data.pic);
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
         * 图片上传
         * 初始化webuploader
         */
        $(function(){
            // 初始化Web Uploader
            var uploader = WebUploader.create({
                // 选完文件后，是否自动上传。
                auto: true,
                // swf文件路径
                swf: 'widget/webuploader/Uploader.swf',
                // 文件接收服务端。
                server: '${pageContext.request.contextPath}/file/imgUpload',
                // 选择文件的按钮。可选。
                // 内部根据当前运行是创建，可能是input元素，也可能是flash.
                pick: '#select_pic'
            });

            //webuploader的事件绑定
            //uploader.on("事件的名称", function);

            //图片添加进队列的事件回调
            uploader.on( 'fileQueued', function( file ) {
                alert(file)
                //需要显示缩略图的img标签
                var $img = $("#header_img");

                // 创建缩略图
                // 如果为非图片文件，可以不用调用此方法。
                // thumbnailWidth x thumbnailHeight 为 100 x 100
                uploader.makeThumb( file, function( error, src ) {
                    if ( error ) {
                        $img.replaceWith('<span>不能预览</span>');
                        return;
                    }

                    $img.attr( 'src', src );
                }, 100, 100 );
            });

            //上传成功！
            uploader.on("uploadSuccess", function(file, response){
                alert(response.filename)
                var $img=$("#header");
                $img.attr("src","${pageContext.request.contextPath}/file/img/"+response.filename);
                $("#p_info").html("<font color='green'>上传成功</font>");
                $("#header_hidden").val(response.filename);
            });

            //上传失败！
            uploader.on("uploadError", function(file){
                $("#p_info").html("<font color='red'>上传失败</font>");
            });
        })

        /**
         * 给用户选择角色
         */
        function open_tree_dialog(){
            //ajax
            $.ajax({
                url:"${pageContext.request.contextPath}/role/queryAllAjax",
                type:"GET",
                success:function(data){
                    //alert(data)
                    //开启简单json格式的模式
                    var setting = {
                        data: {
                            key:{
                                name: "rname"
                            },
                            simpleData: {
                                pIdKey: "id"
                            }
                        },
                        view:{
                            showIcon:false  //是否显示节点的图标
                        },
                        callback:{
                            onClick:function(event, treeid, treeNode){
                                //改变button的内容
                                $("#btn").val(treeNode.rname);
                                //修改隐藏域的id
                                $("#rid").val(treeNode.id);

                                //关闭树形结构dialog
                                $("#ztree_dialog_div").dialog("close");
                            }
                        }
                    };

                    //ztree需要的json
                    var zNodes = data;

                    //初始化ztree
                    var ztreeObject = $.fn.zTree.init($("#ztree_div"), setting, zNodes);
                    ztreeObject.expandAll(true);

                    //获得当前选中的部门
                    var id = $("#btn").val();
                    //获取属性id为指定值的部门节点对象
                    var treeNode = ztreeObject.getNodeByParam("id", id);
                    //选中这个部门
                    ztreeObject.selectNode(treeNode, true);

                    //弹出ztreediv
                    $("#ztree_dialog_div").dialog({
                        width: 300,
                        height: 400,
                        title: "选择",
                        modal: true
                    });
                },
                dataType:"json"
            });
        }

    </script>
</head>
<body>
<blockquote class="layui-elem-quote layui-text">
    用户管理
</blockquote>

<div class="row" style="margin-bottom: 20px">
    <div class="col-md-1 col-md-offset-8">
        <button type="button" onclick="open_dialog();" class="layui-btn">新增用户</button>
    </div>
</div>

<div class="row">
    <div class="col-md-12">
        <table class="table table-bordered table-hover">
            <tr>

                <td>用户名</td>
                <td>头像</td>
                <td>姓名</td>
                <td>密码</td>
                <td>性别</td>
                <td>年龄</td>
                <td>邮箱</td>
                <td>电话</td>
                <td>角色</td>
                <td>单位</td>
                <td>编辑</td>
            </tr>
            <c:forEach items="${users}" var="user">
                <tr>
                    <td>${user.username}</td>
                    <td><img style="width: 90px; height: 100px;"
                             src="${pageContext.request.contextPath}/file/img/${user.pic}" />
                    </td>
                    <td>${user.name}</td>
                    <td>${user.password}</td>
                    <td>${user.sex == 1 ? '男' : '女'}</td>
                    <td>${user.age}</td>
                    <td>${user.email}</td>
                    <td>${user.phone}</td>
                    <td>${user.role.rname}</td>
                    <td>${user.area}</td>
                    <td>
                        <!-- 修改 -->
                        <security:authorize url="/user/insert">
                        <a href="javascript:show_update_Dialog(${user.id});" title="Edit">
                            <i class="layui-icon">&#xe642;</i>修改</a>
                        </security:authorize>
                        <!-- 删除 -->
                        <security:authorize url="/user/deleteById/*">
                        <a href="${pageContext.request.contextPath}/user/deleteById/${user.id}" title="Delete" style="color: #FF5722">
                            <i class="layui-icon">&#x1006;</i>删除</a>
                        </security:authorize>

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
                        <img id="header_img" style="width: 90px; height: 100px;"
                             src="images/icons/header.jpg"/>
                    <div id="select_pic">上传头像</div><p id="p_info"></p>
                    <!-- 上传头像的文件名称 -->
                    <input id="header_hidden" name="pic" type="hidden"/>
                    </p>

                    <p>
                        <label>用户名</label><br>
                        <input
                                class="text-input small-input" type="text" id="username"
                                name="username"/>
                    </p>
                    <p>
                        <label>姓名</label><br>
                        <input
                                class="text-input small-input" type="text" id="name"
                                name="name"/>
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
                        <label>角色</label>
                        <input id="btn" type="button" value="请选择" onclick="open_tree_dialog();"/>
                        <input id="rid" name="rid" type="hidden" value=""/>
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
                        <input class="layui-btn" type="submit" value="提交"/>
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


</div>
</div>
</body>
</html>
