$(function () {
    $('.skin-minimal input').iCheck({
        checkboxClass: 'icheckbox-blue',
        radioClass: 'iradio-blue',
        increaseArea: '20%'
    });

    $("#form-inventory-edit").validate({
        rules: {
            name: {
                required: true
            },
            type: {
                required: true
            },
            location: {
                required: true
            },
            repository: {
                required: true
            },
            category: {
                required: true
            },
            inventory: {
                required: true
            }
        },
        onkeyup: false,
        focusCleanup: true,
        success: "valid",
        submitHandler: function (form) {
            $(form).ajaxSubmit({
                type: 'PUT',
                url: "/admin/order/product/" + $("#pid").val(),
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