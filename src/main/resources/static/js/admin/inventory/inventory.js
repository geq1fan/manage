/**
 * 删除历史记录
 * @param obj 当前元素对象
 * @param url 删除url
 */
function inventory_del(obj, url) {
    layer.confirm('确认要删除吗？',function(index){
        //此处请求后台程序，下方是成功后的前台处理……
        $.ajax({
            type:"DELETE",
            dataType:"json",
            url: url,
            data:{
                "timestamp":new Date().getTime()
            },
            statusCode: {
                200 : function(data){
                    $(obj).parents("tr").remove();
                    var total = $("#total").text();
                    $("#total").text(parseInt(total)-1);
                    succeedMessage(data.responseText);
                },
                404 : function(data){
                    errorMessage(data.responseText);
                },
                500 : function(){
                    errorMessage('系统错误!');
                }
            }
        });
    });
}

/**
 * 添加产品信息
 * @param title
 * @param url
 * @param w
 * @param h
 */
function inventory_product_add(title,url,w,h) {
    layer_show(title,url,w,h);
}

/**
 * 添加产品信息
 * @param title
 * @param url
 * @param w
 * @param h
 */
function inventory_product_edit(title,url,w,h) {
    layer_show(title,url,w,h);
}