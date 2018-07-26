/**
 * 进行订单生产调度
 */
function order_schedule(title, url, w, h) {
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

    layer.confirm('确认要进行生产调度？', function () {
        url = url + '/' + selectedIds;
        layer_show(title, url, w, h);
    });
}
