/**
 * 删除历史记录
 * @param obj 当前元素对象
 * @param url 删除url
 */
function history_del(obj, url) {
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
 * 批量删除历史记录
 */
function history_batch_del() {

    //复选框选择id集合
    var selectedIds=[];
    $(".text-c :checkbox").each(function (index, ele) {
        var id = $(this).val();
        var isSelected = this.checked;
        if (isSelected) {
            selectedIds.push(id);
        } else {
            selectedIds.removeObject(id);
        }
    });

    if(selectedIds == ""){
        errorMessage("请先选择一条记录!");
        return false;
    }

    layer.confirm('确认要删除吗？',function(index){
        //此处请求后台程序，下方是成功后的前台处理……
        $.ajax({
            type:"DELETE",
            dataType:"json",
            url: "/admin/productline/batch/"+selectedIds,
            data:{
                "timestamp":new Date().getTime()
            },
            statusCode: {
                200 : function(data){
                    succeedMessage(data.responseText);
                    window.location.reload();
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
 * 查看历史记录详情
 * @param title
 * @param url
 * @param w
 * @param h
 */
function history_view(title,url,w,h) {
    layer_show(title,url,w,h);
}