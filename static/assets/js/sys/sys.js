var token = JSON.parse(sessionStorage.getItem('token'));
var table;
var roleId;
$('#table').DataTable().destroy();

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
    pagingType: "full",  //分页样式：simple,simple_numbers,full,full_numbers

    ajax: function (data, callback, settings) {
        //封装请求参数
        var param = {};
        param.pageSize = data.length;//页面显示记录条数，在页面显示每页显示多少项的时候
        param.pageIndex = (data.start / data.length) + 1;//当前页码
        //console.log(param);
        //ajax请求数据
        $.ajax({
            type: "get",
            url: "http://" + host + "/sys/admin/role/allRoleByPage",
            cache: true,  //禁用缓存
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
            },
            error: function (result) {
                toastr.error("没有权限")
            }
        });
    },
    //列表表头字段
    columns: [
        {"data": "id"},
        {"data": "name"},
        {"data": "description"},
        {
            "data": "createtime",
            "render": function (data, type, row, meta) {
                return timeStamp2Date(data);
            }
        },
        {
            "data": "updatetime",
            "render": function (data, type, row, meta) {
                return timeStamp2Date(data);
            }
        },
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
}).api();//此处需调用api()方法,否则返回的是JQuery对象而不是DataTables的API对象


$("#searchUser").click(function () {

    var username = "";
    username = $('input[name="searchText"]').val();
    if (username == "") {
        toastr.error("请输入用户名！");
    } else {

        /*$('#table').DataTable().destroy();
        $('#table').html("");*/
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
                        callback(returnData);
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
                            "<button class='btn btn-danger' onclick='del(" + data.id + ")'><i class='fa fa-pencil-square-o'></i>删除</button>";
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
            url: "http://" + host + "/sys/admin/role/" + id,
            type: "DELETE",
            data: {"id": id},
            headers: {"token": token},
            success: function (data) {
                if (data.code == 200) {
                    table.ajax.reload();
                    toastr.info(data.message);
                } else {
                    toastr.error(data.message);
                }
            },
            error: function (data) {
                toastr.error(data.message);
            }
        });
    }
}


/*添加*/

function addInit() {
    $.ajax({
        type: "get",
        url: "http://" + host + "/sys/admin/permissions/all",
        headers: {"token": token},
        success: function (data) {
            var setting = {
                check: {
                    enable: true,
                    chkStyle: "checkbox",
                    chkboxType: {
                        "Y": "ps",
                        "N": "ps"
                    }

                },
                data: {
                    simpleData: {
                        enable: true
                    }
                }
            };

            var zNodes = [];
            $.each(data.data, function (index, item) {
                var node = {};
                node.id = item.id;
                node.pId = item.parentId;
                node.name = item.name;
                node.open = true;
                zNodes.push(node);
            });

            $(document).ready(function () {
                $.fn.zTree.init($("#add-tree"), setting, zNodes);
            });
        }

    });
}

/* 添加*/
$("#addRole").click(function () {
    $('.modal-backdrop').remove();  // 去掉灰色遮罩
    var treeObj = $.fn.zTree.getZTreeObj("add-tree");
    var nodes = treeObj.getCheckedNodes(true);
    var permissionIds = "";
    var i;
    for (i = 0; i < nodes.length - 1; i++) {
        permissionIds += nodes[i].id.toString() + ",";
    }
    permissionIds += nodes[i].id.toString();
    $.ajax({
        type: "post",
        url: "http://" + host + "/sys/admin/role/add",
        data: {
            "roleId": roleId,
            "name": $('input[name="add-name"]').val(),
            "description": $('input[name="add-desc"]').val(),
            "selectedPermissionId": permissionIds
        },
        headers: {"token": token},
        success: function (data) {
            $("#addModal").modal('hide');
            $('.modal-backdrop').remove();  // 去掉灰色遮罩
            toastr.info(data.message);
            table.ajax.reload();
        },
        error: function (data) {
            toastr.info(data.message);
        }
    });

});


var roleId;

// 编辑查询数据回显
function edit(id) {
    roleId = id;
    $.ajax({
        type: "get",
        url: "http://" + host + "/sys/admin/role/getSingleRole/" + id,
        headers: {"token": token},
        success: function (data) {

            if (data.code == "200") {
                $("#editModal").modal('show');
                $("#edit-id").val(data.object.sysRole.id);
                $('input[name="edit-name"]').val(data.object.sysRole.name);
                $('input[name="edit-desc"]').val(data.object.sysRole.description);
                roleId = data.object.sysRole.id;
                // ztree 数据回显
                initZtree();

            }
        },

        error: function (data) {

        }
    })

}


// 提交编辑
$("#editRole").click(function () {

    var treeObj = $.fn.zTree.getZTreeObj("edit-tree");
    var nodes = treeObj.getCheckedNodes(true);
    var permissionIds = "";
    var i;
    for (i = 0; i < nodes.length - 1; i++) {
        permissionIds += nodes[i].id.toString() + ",";
    }
    permissionIds += nodes[i].id.toString();
    $.ajax({
        type: "post",
        url: "http://" + host + "/sys/admin/role/update",
        data: {
            "roleId": roleId,
            "name": $('input[name="edit-name"]').val(),
            "description": $('input[name="edit-desc"]').val(),
            "selectedPermissionId": permissionIds
        },
        headers: {"token": token},
        success: function (data) {
            $("#editModal").modal('hide');
            $('.modal-backdrop').remove();  // 去掉灰色遮罩
            toastr.info(data.message);
            table.ajax.reload();

        },
        error: function (data) {
            toastr.info(data.message);
        }
    });

});


var allPermission;
var ownPermission;

function initZtree() {
    $.ajax({
        type: "get",
        url: "http://" + host + "/sys/admin/permissions/all",
        headers: {"token": token},
        success: function (data) {
            allPermission = data;
            getRolePermission(roleId);
        }

    });

}

function getRolePermission(roleId) {
    $.ajax({
        type: "get",
        url: "http://" + host + "/sys/admin/permissions/" + roleId,
        headers: {"token": token},
        success: function (data) {
            ownPermission = data;
            renderzTree();
        }

    });
}


function renderzTree() {

    var setting = {
        check: {
            enable: true,
            chkStyle: "checkbox",
            chkboxType: {
                "Y": "ps",
                "N": "ps"
            }

        },
        data: {
            simpleData: {
                enable: true
            }
        }
    };

    var zNodes = [];
    $.each(allPermission.data, function (index, item) {
        var node = {};
        $.each(ownPermission.data, function (index, own) {
            if (item.id == own.id) {
                node.checked = true;
            }
        });
        node.id = item.id;
        node.pId = item.parentId;
        node.name = item.name;
        node.open = true;
        zNodes.push(node);
    });

    $(document).ready(function () {
        $.fn.zTree.init($("#edit-tree"), setting, zNodes);
    });


}

function timeStamp2Date(timestamp) {

    var datetime = new Date();
    datetime.setTime(timestamp);
    var year = datetime.getFullYear();
    var month = datetime.getMonth() + 1;
    var date = datetime.getDate();
    var hour = datetime.getHours();
    var minute = datetime.getMinutes();
    var second = datetime.getSeconds();
    var msecond = datetime.getMilliseconds();
    return year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second;

}

// 刷新
$("#refresh-role").click(function () {
    $("#table").DataTable().ajax.reload();
    toastr.info("刷新成功");
});

