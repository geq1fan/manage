/**
 * 需求预测
 * @param title
 * @param url
 * @param w
 * @param h
 */
function schedule_inventory_predict(title, url, w, h) {
    layer_show(title, url, w, h);
}

/**
 * 库存生产调度
 * @param title
 * @param url
 * @param w
 * @param h
 */
function schedule_inventory(title, url, w, h) {
    layer_show(title, url, w, h);
}

function inventory_schedule(title, url, w, h) {
    //复选框选择id集合
    var selectedIds = [];
    $(".text-c :checkbox").each(function () {
        var id = $(this).val();
        var isSelected = this.checked;
        if (isSelected) {
            selectedIds.push(id);
        } else {
            selectedIds.removeObject(id);
        }
    });

    if (selectedIds == "") {
        errorMessage("请先选择一条记录!");
        return false;
    }

    layer.confirm('确认要进行生产调度？', function () {
        url = url + '/' + selectedIds;
        layer_show(title, url, w, h);
    });
}