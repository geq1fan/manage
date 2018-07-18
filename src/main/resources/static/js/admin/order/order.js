/**
 * 删除历史记录
 * @param obj 当前元素对象
 * @param url 删除url
 */
function order_del(obj, url) {
    layer.confirm('确认要删除吗？', function (index) {
        //此处请求后台程序，下方是成功后的前台处理……
        $.ajax({
            type: "DELETE",
            dataType: "json",
            url: url,
            data: {
                "timestamp": new Date().getTime()
            },
            statusCode: {
                200: function (data) {
                    $(obj).parents("tr").remove();
                    var total = $("#total").text();
                    $("#total").text(parseInt(total) - 1);
                    succeedMessage(data.responseText);
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

/**
 * 添加商品信息
 * @param title
 * @param url
 * @param w
 * @param h
 */
function order_product_add(title, url, w, h) {
    layer_show(title, url, w, h);
}

/**
 * 编辑商品信息
 * @param title
 * @param url
 * @param w
 * @param h
 */
function order_product_edit(title, url, w, h) {
    layer_show(title, url, w, h);
}

/**
 * 添加订单
 */
function order_add(title, url, w, h) {

    //复选框选择id集合
    var selectedId;
    $(".text-c :radio").each(function (index, ele) {
        var id = $(this).val();
        var isSelected = this.checked;
        if (isSelected) {
            selectedId = id;
        }
    });

    if (selectedId === undefined) {
        errorMessage("请先选择一项商品!");
        return false;
    }
    layer.confirm('确定创建订单？', function (index) {
        url = url + '/' + selectedId;
        layer_show(title, url, w, h);
    });
}

/**
 * 审核订单
 * @param title
 * @param url
 * @param w
 * @param h
 */
function order_check(title, url, w, h) {
    layer_show(title, url, w, h);
}

