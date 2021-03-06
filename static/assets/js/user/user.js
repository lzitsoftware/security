var token = JSON.parse(sessionStorage.getItem('token'));
var table;
// 系统角色初始化
$.ajax({
    type: "get",
    url: "http://" + host + "/sys/admin/role/allRole",
    headers: {"token": token},
    success: function (result) {
        var roles = '';
        $.each(result.data, function (index, item) {
            roles += "<option value='" + item.id + "'>" + item.name + "</option>"

        });
        $("#edit-role").append(roles);
        $("#add-role").append(roles);
    },
    error: function (data) {
        toastr.error("标签加载异常" + data)
    }
});

$('#table').DataTable().destroy();
console.info("hhhhhh");
//提示信息
var lang = {
    "sProcessing": "处理中...",
    "sLengthMenu": "每页 _MENU_ 项",
    "sZeroRecords": "没有匹配结果",
    "sInfo": "当前显示第 _START_ 至 _END_ 项，共 _TOTAL_ 项。",
    "sInfoEmpty": "当前显示第 0 至 0 项，共 0 项",
    "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
    "sInfoPostFix": "",
    "sSearch": "搜索:",
    "sUrl": "",
    "sEmptyTable": "表中数据为空",
    "sLoadingRecords": "载入中...",
    "sInfoThousands": ",",
    "oPaginate": {
        "sFirst": "首页",
        "sPrevious": "上页",
        "sNext": "下页",
        "sLast": "末页",
        "sJump": "跳转"
    },
    "oAria": {
        "sSortAscending": ": 以升序排列此列",
        "sSortDescending": ": 以降序排列此列"
    }
};
//初始化表格
var table = $("#table").dataTable({
    language: lang,  //提示信息
    autoWidth: false,  //禁用自动调整列宽
    stripeClasses: ["odd", "even"],  //为奇偶行加上样式，兼容不支持CSS伪类的场合
    processing: true,  //隐藏加载提示,自行处理
    serverSide: true,  //启用服务器端分页
    searching: false,  //禁用原生搜索
    orderMulti: false,  //启用多列排序
    order: [],  //取消默认排序查询,否则复选框一列会出现小箭头
    renderer: "bootstrap",  //渲染样式：Bootstrap和jquery-ui
    pagingType: "simple_numbers",  //分页样式：simple,simple_numbers,full,full_numbers

    ajax: function (data, callback, settings) {
        //封装请求参数
        var param = {};
        param.pageSize = data.length;//页面显示记录条数，在页面显示每页显示多少项的时候
        param.pageIndex = (data.start / data.length) + 1;//当前页码
        //console.log(param);
        //ajax请求数据
        $.ajax({
            type: "post",
            url: "http://" + host + "/sys/admin/user/allUser",
            cache: false,  //禁用缓存
            data: param,  //传入组装的参数
            headers: {"token": token},
            success: function (result) {
                //封装返回数据
                var returnData = {};
                returnData.draw = data.draw;//这里直接自行返回了draw计数器,应该由后台返回
                returnData.recordsTotal = result.recordsTotal;//返回数据全部记录
                returnData.recordsFiltered = result.recordsFiltered;//后台不实现过滤功能，每次查询均视作全部结果
                returnData.data = result.data;//返回的数据列表
                //console.log(returnData);
                //调用DataTables提供的callback方法，代表数据已封装完成并传回DataTables进行渲染
                //此时的数据需确保正确无误，异常判断应在执行此回调前自行处理完毕
                callback(returnData);
            }
        });
    },
    //列表表头字段
    columns: [
        {"data": "id"},
        {"data": "username"},
        {"data": "phone"},
        {"data": "email"},
        {   // 字段通过id渲染
            "data": "sex",
            "render": function (data, type, row, meta) {
                if (data == "1") {
                    return "男";
                } else {
                    return "女";
                }
            }
        },
        {"data": "status"},
        {"data": null}
    ],
    columnDefs: [
        {
            "targets": -1,//编辑
            "data": "id",
            "orderable": false,
            "render": function (data, type, row, meta) {
                return "<button  class='btn btn-warning' onclick='edit(" + data.id + ")' ><i class='fa fa-pencil-square-o'></i>编辑</button>" +
                    "<button class='btn btn-danger' onclick='del(" + data.id + ")'><i class='fa fa-trash-o'></i>删除</button>";
            }

        }
    ]
});
table.api();


$("#searchUser").click(function () {


    var username = "";
    username = $('input[name="searchText"]').val();
    if (username == "") {
        toastr.error("请输入用户名！");
    } else {
        $('#table').DataTable().destroy();
        //提示信息
        var lang = {
            "sProcessing": "处理中...",
            "sLengthMenu": "每页 _MENU_ 项",
            "sZeroRecords": "没有匹配结果",
            "sEmptyTable": "Overridden message here",
            "sInfo": "当前显示第 _START_ 至 _END_ 项，共 _TOTAL_ 项。",
            "sInfoEmpty": "当前显示第 0 至 0 项，共 0 项",
            "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
            "sInfoPostFix": "",
            "sSearch": "搜索:",
            "sUrl": "",
            "sEmptyTable": "表中数据为空",
            "sLoadingRecords": "载入中...",
            "sInfoThousands": ",",
            "oPaginate": {
                "sFirst": "首页",
                "sPrevious": "上页",
                "sNext": "下页",
                "sLast": "末页",
                "sJump": "跳转"
            },
            "oAria": {
                "sSortAscending": ": 以升序排列此列",
                "sSortDescending": ": 以降序排列此列"
            }
        };

        table = $("#table").dataTable({
            language: lang,  //提示信息
            autoWidth: false,  //禁用自动调整列宽
            stripeClasses: ["odd", "even"],  //为奇偶行加上样式，兼容不支持CSS伪类的场合
            processing: true,  //隐藏加载提示,自行处理
            serverSide: true,  //启用服务器端分页
            searching: false,  //禁用原生搜索
            orderMulti: false,  //启用多列排序
            order: [],  //取消默认排序查询,否则复选框一列会出现小箭头
            renderer: "bootstrap",  //渲染样式：Bootstrap和jquery-ui
            pagingType: "full",  //分页样式：simple,simple_numbers,full,full_numbers

            ajax: function (data, callback, settings) {
                //封装请求参数
                var param = {};
                param.pageSize = 10;//页面显示记录条数，在页面显示每页显示多少项的时候
                param.pageIndex = 1;//当前页码
                //console.log(param);
                //ajax请求数据
                $.ajax({
                    type: "GET",
                    url: "http://" + host + "/sys/admin/user/search/" + username,
                    cache: false,  //禁用缓存
                    data: param,  //传入组装的参数
                    headers: {"token": token},
                    success: function (result) {
                        //封装返回数据
                        var returnData = {};
                        returnData.draw = data.draw;//这里直接自行返回了draw计数器,应该由后台返回
                        returnData.recordsTotal = result.recordsTotal;//返回数据全部记录
                        returnData.recordsFiltered = result.recordsFiltered;//后台不实现过滤功能，每次查询均视作全部结果
                        returnData.data = result.data;//返回的数据列表
                        //console.log(returnData);
                        //调用DataTables提供的callback方法，代表数据已封装完成并传回DataTables进行渲染
                        //此时的数据需确保正确无误，异常判断应在执行此回调前自行处理完毕
                        if (result.data[0] == null) {
                            toastr.warning("无查询结果！");
                        }else {
                            callback(returnData);
                        }

                    },
                    error: function (data) {
                        toastr.error("" + data.message);
                    }
                });
            },
            //列表表头字段
            columns: [
                {"data": "id"},
                {"data": "username"},
                {"data": "phone"},
                {"data": "email"},
                {   // 字段通过id渲染
                    "data": "sex",
                    "render": function (data, type, row, meta) {
                        if (data == "1") {
                            return "男";
                        } else {
                            return "女";
                        }
                    }
                },
                {"data": "status"},
                {"data": null}
            ],
            columnDefs: [
                {
                    "targets": -1,//编辑
                    "data": "id",
                    "orderable": false,
                    "render": function (data, type, row, meta) {
                        return "<button  class='btn btn-warning' onclick='edit(" + data.id + ")' ><i class='fa fa-pencil-square-o'></i>编辑</button>" +
                            "<button class='btn btn-danger ' onclick='del(" + data.id + ")'><i class='fa fa-trash-o'></i>删除</button>";
                    }

                }
            ]
        }).api();//此处需调用api()方法,否则返回的是JQuery对象而不是DataTables的API对象
    }



});

/**
 * 删除数据
 * @param name
 */
function del(id) {

    var r = confirm("您确认删除吗?");
    if (r == true) {
        $.ajax({
            url: "http://" + host + "/sys/admin/user/deleteUser/" + id,
            type: "DELETE",
            data: {"id": id},
            headers: {"token": token},
            success: function (data) {
                table.ajax.reload();
                toastr.info(data.message+"");
            },
            error: function (result) {
                toastr.warning(data.responseJSON.message);
            }
        });
    }
}


$("#addUser").click(function () {

    $('.modal-backdrop').remove();  // 去掉灰色遮罩
    /*toastr.options = {
        closeButton: false,                                            // 是否显示关闭按钮，（提示框右上角关闭按钮）
        debug: false,                                                    // 是否使用deBug模式
        progressBar: true,                                            // 是否显示进度条，（设置关闭的超时时间进度条）
        positionClass: "toast-top-center",              // 设置提示款显示的位置
        onclick: null,                                                     // 点击消息框自定义事件 
        showDuration: "300",                                      // 显示动画的时间
        hideDuration: "1000",                                     //  消失的动画时间
        timeOut: "2000",                                             //  自动关闭超时时间 
        extendedTimeOut: "1000",                             //  加长展示时间
        showEasing: "swing",                                     //  显示时的动画缓冲方式
        hideEasing: "linear",                                       //   消失时的动画缓冲方式
        showMethod: "fadeIn",                                   //   显示时的动画方式
        hideMethod: "fadeOut"                                   //   消失时的动画方式
    };*/

    //toastr.info("添加中......");

    $.ajax({
        type: "post",
        url: "http://" + host + "/sys/admin/user/addUser",
        data: {
            "username": $('input[name="add-username"]').val(),
            "password": $('input[name="add-nickname"]').val(),
            "nickname": $('input[name="add-password"]').val(),
            "phone": $('input[name="add-phone"]').val(),
            "email": $('input[name="add-email"]').val(),
            "sex": $("#add-sex").val(),
            "birthday": $('input[name="add-birthday"]').val(),
            "role": $("#edit-role").val()
        },
        headers: {"token": token},
        success: function (data) {
            $("#myModal").modal('hide');
            $('.modal-backdrop').remove();  // 去掉灰色遮罩
            toastr.info(data.message + "");
            table.ajax.reload();
        },
        error: function (data) {
            alert(data.message);
            toastr.error(data.message + "");
        }
    });
});


// 编辑查询数据回显
function edit(id) {
    $.ajax({
        type: "get",
        url: "http://" + host + "/sys/admin/user/getSingleUser/" + id,
        headers: {"token": token},
        success: function (data) {
            console.info(data);
            if (data.code == "200") {
                $("#editModal").modal('show');
                $("#edit-id").val(data.object.id);
                $('input[name="edit-username"]').val(data.object.username);
                $('input[name="edit-nickname"]').val(data.object.nickname);
                $('input[name="edit-phone"]').val(data.object.phone);
                $('input[name="edit-email"]').val(data.object.email);
                if ($.trim(data.object.sex) == $.trim(1)) {
                    // 给select 赋默认值
                    $("#edit-sex").val("1")
                } else if ($.trim(data.object.sex) == $.trim(2)) {
                    $("#edit-sex").val("2")
                }
                $("#edit-role").val(data.object.roles[0].id + "");
            }

        },

        error: function (data) {

        }
    })

}

// 提交编辑
$("#editUser").click(function () {

    var id = $("#edit-id").val();
    /*var editForm = new FormData;
    editForm.append();
    editForm.append("title",$('input[name="title"]').val());
    editForm.append("coverUrl",$('input[name="coverUrl"]').val());
    editForm.append("likeSize",$('input[name="likeSize"]').val());
    editForm.append("unLikeSize",$('input[name="unLikeSize"]').val());*/
    $.ajax({
        type: "post",
        url: "http://" + host + "/sys/admin/user/updateUser/" + id,
        data: {
            "username": $('input[name="edit-username"]').val(),
            "nickname": $('input[name="edit-nickname"]').val(),
            "phone": $('input[name="edit-phone"]').val(),
            "email": $('input[name="edit-email"]').val(),
            "sex": $("#edit-sex").val(),
            "role": $("#edit-role").val()
        },
        headers: {"token": token},
        success: function (data) {
            $("#editModal").modal('hide');
            $('.modal-backdrop').remove();  // 去掉灰色遮罩
            toastr.info(data.message);
            table.ajax.reload();

        },
        error: function (data) {
            toastr.warning(data.responseJSON.message);
        }
    });

});


// 刷新
$("#refresh-user").click(function () {
    $("#table").DataTable().ajax.reload();
    toastr.info("刷新成功");
});


