function schedule_execute() {
    //复选框选择id集合
    var selectedIds = [];
    $(".text-c :checkbox").each(function () {
        var id = $(this).val();
        var isSelected = this.checked;
        if (isSelected && id !== '') {
            selectedIds.push(id);
        } else {
            selectedIds.removeObject(id);
        }
    });

    if (selectedIds === "") {
        errorMessage("请先选择一条记录!");
        return false;
    }

    layer.confirm('确认要执行吗？', function () {
        //此处请求后台程序，下方是成功后的前台处理……
        $.ajax({
            type: "GET",
            dataType: "json",
            url: "/admin/schedule/execute/" + selectedIds,
            data: {
                "timestamp": new Date().getTime()
            },
            statusCode: {
                200: function (data) {
                    succeedMessage(data.responseText);
                    window.location.reload();
                },
                404: function (data) {
                    errorMessage(data.responseText);
                },
                500: function () {
                    errorMessage('系统错误!');
                }
            }
        });
    });
}

function schedule_feedback() {
    //复选框选择id集合
    var selectedIds = [];
    $(".text-c :checkbox").each(function () {
        var id = $(this).val();
        var isSelected = this.checked;
        if (isSelected && id !== '') {
            selectedIds.push(id);
        } else {
            selectedIds.removeObject(id);
        }
    });

    if (selectedIds === "") {
        errorMessage("请先选择一条记录!");
        return false;
    }

    layer.confirm('确认要反馈吗？', function () {
        //此处请求后台程序，下方是成功后的前台处理……
        $.ajax({
            type: "GET",
            dataType: "json",
            url: "/admin/schedule/feedback/" + selectedIds,
            data: {
                "timestamp": new Date().getTime()
            },
            statusCode: {
                200: function (data) {
                    succeedMessage(data.responseText);
                    window.location.reload();
                },
                404: function (data) {
                    errorMessage(data.responseText);
                },
                500: function () {
                    errorMessage('系统错误!');
                }
            }
        });
    });
}