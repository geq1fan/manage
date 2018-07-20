//后台传来的option的xAxis和yAxis写成了xaxis和yaxis
//导致echarts无法解析option
//因此需要转换一下
//并且修改formatter属性
function change(json) {
    var press = function (params) {
        var relVal = '';
        for (l = params.length - 1; l >= 0; l--) {
            relVal += params[l].seriesName + ' : ' + params[l].value[1] + '×10^5Pa' + '<br/>';
        }
        return relVal;
    }
    var temp = function (params) {
        var relVal = '';
        for (l = params.length - 1; l >= 0; l--) {
            relVal += params[l].seriesName + ' : ' + params[l].value[1] + '℃' + '<br/>';
        }
        return relVal;
    }
    var height = function (params) {
        var relVal = '';
        for (l = params.length - 1; l >= 0; l--) {
            relVal += params[l].seriesName + ' : ' + params[l].value[1] + 'cm' + '<br/>';
        }
        return relVal;
    }

    var newJson = {};
    var jsonKeys = Object.keys(json);
    for (var i = 0; i < jsonKeys.length; i++) {
        if (jsonKeys[i] === 'yaxis') {
            newJson['yAxis'] = json[jsonKeys[i]];
        }
        else if (jsonKeys[i] === 'xaxis') {
            newJson['xAxis'] = json[jsonKeys[i]];
        } else {
            newJson[jsonKeys[i]] = json[jsonKeys[i]];
        }
        // if (jsonKeys[i] === 'tooltip') {
        //     if (type === 'press') {
        //         newJson['tooltip']['formatter'] = press;
        //     } else if (type === 'temp') {
        //         newJson['tooltip']['formatter'] = temp;
        //     } else if (type === 'height') {
        //         newJson['tooltip']['formatter'] = height;
        //     }
        // }
    }
    return newJson;
}

//图表
var psLineChar = echarts.init(document.getElementById('psLine'));
//全局参数
var productlineName = $("#productlineName").val();
var type = $("#type").val();

//查询
function loadDrugs() {
    psLineChar.clear();
    psLineChar.showLoading({text: '正在努力的读取数据中...'});

    $.getJSON(productlineName + '/' + type, function (data) {
        if (data.status === "success") {
            psLineChar.setOption(change(data.option), true);
            psLineChar.hideLoading();
        } else {
            alert('提示', data.msg);
        }
    });
}

//载入图表
loadDrugs();