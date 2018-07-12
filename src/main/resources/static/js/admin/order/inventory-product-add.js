$(function () {
    $('.skin-minimal input').iCheck({
        checkboxClass: 'icheckbox-blue',
        radioClass: 'iradio-blue',
        increaseArea: '20%'
    });

    $("#form-inventory-product-add").validate({
        rules: {
            type: {
                required: true,
                remote: {
                    url: "/admin/inventory/product/isExist",
                    type: "get",
                    data: {
                        type: function () {
                            return $("#type").val();
                        }
                    }
                }
            },
            name: {
                required: true
            },
            price: {
                required: true
            },
            inventory: {
                required: true
            }
        },
        messages: {
            type: {
                remote: "该产品规格已经存在！"
            }
        },
        onkeyup: false,
        focusCleanup: true,
        success: "valid",
        submitHandler: function (form) {
            $(form).ajaxSubmit({
                type: 'post',
                url: "/admin/inventory/product",
                dataType: "json",
                success: function (data) {
                    if (data.status == "success") {
                        succeedMessage(data.message);
                        var index = parent.layer.getFrameIndex(window.name);
                        parent.location.reload();
                        parent.layer.close(index);
                    } else {
                        errorMessage(data.message);
                    }
                }
            });
            return false; // 非常重要，如果是false，则表明是不跳转，在本页上处理，也就是ajax，如果是非false，则传统的form跳转。
        }
    });
});