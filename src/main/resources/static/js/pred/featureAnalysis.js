var analysisStatus = 0;
// 页面进入初始化
$(document).ready(function () {
    console.log("初始化");
    $.ajax({
        type: "get",
        url: "/file/getFeatureAnalysisStatus",
        contentType: false,
        processData: false,
        async: false,
        success: function (data) {

            console.log(data);
            switch (data) {
                case 0:
                    document.getElementById("showPicture").style.display = "none";
                    document.getElementById("startAnalysis").disabled = "true";
                    analysisStatus = 0;
                    break;
                case 1:
                    showPicture("block", "none", "true");
                    analysisStatus = 1;
                    document.getElementById("startAnalysis").value = "分析中...";

                    break;
                case 2:
                    intialPicture();
                    showPicture("none", "block", "");
                    analysisStatus = 2;
                    document.getElementById("startAnalysis").value = "开始分析";

                    break;
                default:
                    break;
            }
        },
        error: function () {
            alert("失败");
        }
    });
});

function showPicture(loading, picture, able) {
    document.getElementById("loading1").style.display = loading;
    document.getElementById("loading2").style.display = loading;
    document.getElementById("loading3").style.display = loading;
    document.getElementById("loading4").style.display = loading;
    document.getElementById("loadAndWeather").style.display = picture;
    document.getElementById("box").style.display = picture;
    document.getElementById("heat").style.display = picture;
    document.getElementById("featureScore").style.display = picture;
    document.getElementById("startAnalysis").disabled = able;
    document.getElementById("ifUpload").disabled = able;
    document.getElementById("btnCsv").disabled = able;
    document.getElementById("historyFileList").disabled = able;
    document.getElementById("csvFile").disabled = able;

}

// 文件上传
function fileUploadAuto() {
    document.getElementById("startAnalysis").disabled = "true";
    var show = "";
    var optional = document.getElementsByName("dataFrom");
    for (var i = 0; i < optional.length; i++) {
        if (optional[i].checked) {
            show = optional[i].id;
        }
    }
    switch (show) {
        case "historicalData":
            document.getElementById("ifUpload").style.display = "none";
            document.getElementById("fromHistory").style.display = "block";
            //设置li拥有的文件
            $.ajax({
                type: "get",
                url: '/file/getAllFile',
                contentType: false,
                processData: false,
                async: false,
                success: function (data) {
                    $("#historyFileList").find("li").remove();
                    if (data != null) {
                        if (analysisStatus != 1) {
                            for (var i = 0; i < data.length; i++) {
                                $("#historyFileList")
                                    .append("<li style='display: inline-block;padding-left: 60px;list-style:none'>"
                                        + "<input name='historyList' class='form-check-input' type='radio' id='" + data[i] + "' onclick='showAnalysisBtn()'>"
                                        + data[i] + "</li>");
                            }
                        } else {
                            for (var i = 0; i < data.length; i++) {
                                $("#historyFileList")
                                    .append("<li style='display: inline-block;padding-left: 60px;list-style:none'>"
                                        + "<input name='historyList' class='form-check-input' type='radio' id='" + data[i] + "' onclick='showAnalysisBtn()' disabled>"
                                        + data[i] + "</li>");
                            }
                        }
                    }
                },
                error: function () {
                    alert("获取失败")
                }
            });
            break;
        case "fileData":
        default:
            document.getElementById("ifUpload").style.display = "block";
            document.getElementById("fromHistory").style.display = "none";
            break;
    }
}

function showAnalysisBtn() {
    document.getElementById("startAnalysis").disabled = "";
}

//文件选择后上传问题
function uploadCsv() {
    console.log("btn click");
    var fileObj = document.getElementById("csvFile").files[0];
    console.log(fileObj);
    if (fileObj == undefined) {
        console.log("in undefined");
        document.getElementById("fileFormat").value = "*未选择任何文件";
    } else {
        var formData = new FormData();
        formData.append("file", fileObj);
        $.ajax({
            type: 'post',
            url: '/file/upload',
            contentType: false,
            processData: false,
            data: formData,
            async: false,
            success: function (data) {
                switch (data) {
                    case "success":
                        document.getElementById("fileFormat").value = "上传成功!";
                        document.getElementById("startAnalysis").disabled = "";
                        break;
                    default:
                        document.getElementById("fileFormat").value = data;
                        document.getElementById("startAnalysis").disabled = "true";
                        break;
                }
                console.log(data);
            },
            error: function () {
                alert("失败");
            }
        });
    }
}

function startAnalysis() {
    console.log("startAnalysis");
    var show = "";
    var optional = document.getElementsByName("dataFrom");
    for (var i = 0; i < optional.length; i++) {
        if (optional[i].checked) {
            show = optional[i].id;
        }
    }
    //按分类型进行数据分析
    var formData = new FormData();
    formData.append("location", "ningbo");
    switch (show) {
        case "historicalData":
            console.log("historicalData");
            var show = "";
            var optional = document.getElementsByName("historyList");
            for (var i = 0; i < optional.length; i++) {
                if (optional[i].checked) {
                    show = optional[i].id;
                }
            }
            formData.append("analysisFile", show);
            break;
        case "fileData":
        default:
            console.log("fileData");
            // formData.append("analysisFile","/Users/liangjinsi/Documents/graduate/pydjc/src/main/resources/static/csvData/admin/csvData/20200107194206.csv");
            $.ajax({
                type: "get",
                url: "/file/getNewFile",
                contentType: false,
                processData: false,
                async: false,
                success: function (data) {
                    console.log(data);
                    if (data != "") {
                        formData.append("analysisFile", data);
                    } else {
                        alert("未找到文件");

                    }
                },
                error: function () {
                    alert("出现错误");

                }

            });
            break;
    }

    document.getElementById("showPicture").style.display = "block";
    showPicture("block", "none", "true");
    document.getElementById("startAnalysis").value = "分析中...";

    //访问端口
    $.ajax({
        type: 'post',
        url: '/file/startAnalysis',
        contentType: false,
        processData: false,
        async: true,
        data: formData,
        success: function (data) {
            console.log(data);
            switch (data) {
                case 0:
                    analysisStatus = 2;
                    document.getElementById("showPicture").display = "none";
                    intialPicture();
                    showPicture("none", "block", "");
                    document.getElementById("showPicture").display = "block";
                    console.log("come to here");
                    document.getElementById("startAnalysis").value = "开始分析";
                    break;
                default:
                    switch (analysisStatus) {
                        case 0:
                            showPicture("none", "none", "");
                            document.getElementById("startAnalysis").value = "开始分析";
                            break;
                        case 1:
                            showPicture("block", "none", "true");
                            document.getElementById("startAnalysis").value = "分析中...";
                            break;
                        default:
                            showPicture("none", "block", "");
                            document.getElementById("startAnalysis").value = "开始分析";
                            break;
                    }
                    break;
            }
        },
        error: function () {
            alert("失败");
            switch (analysisStatus) {
                case 0:
                    showPicture("none", "none", "");
                    document.getElementById("startAnalysis").value = "开始分析";
                    break;
                case 1:
                    showPicture("block", "none", "true");
                    document.getElementById("startAnalysis").value = "分析中...";
                    break;
                default:
                    showPicture("none", "block", "");
                    document.getElementById("startAnalysis").value = "开始分析";
                    break;
            }
        }
    });
}

function intialPicture() {
    console.log("initialPicture");
    var formData = new FormData();
    formData.append("file","/Box.json");
    //绘制箱型图
    $.ajax({
        type: "post",
        url: "/file/getJson",
        contentType: false,
        processData: false,
        async: false,
        data:formData,
        success: function (value) {
            console.log("initialBox");
            var jsonData = JSON.parse(value);
            var myChart = echarts.init(document.getElementById('box'));

            // Generate data.
            var varchar = [jsonData.weekday, jsonData.weekend, jsonData.monday, jsonData.tuesday, jsonData.wednesday, jsonData.thursday, jsonData.firday];
            var data = echarts.dataTool.prepareBoxplotData(varchar, {boundIQR: 'none'});

            //console.log(data)

            option = {
                title: [
                    {
                        text: 'upper: Q3 + 1.5 * IRQ \nlower: Q1 - 1.5 * IRQ',
                        borderColor: '#999',
                        borderWidth: 1,
                        textStyle: {
                            fontSize: 14
                        },
                        left: '10%',
                        top: '90%'
                    }
                ],
                tooltip: {
                    trigger: 'item', //触发类型,数据项图形触发，主要在散点图，饼图等无类目轴的图表中使用。
                    axisPointer: { //指示器类型。
                        type: 'shadow'
                    }
                },
                grid: { //直角坐标系网格。
                    //show: true,//default: false
                    left: '10%',
                    right: '10%',
                    bottom: '15%',
                    //borderWidth: 1,
                    //borderColor: '#000',
                },
                xAxis: { //X轴
                    type: 'category', //'category' 类目轴，适用于离散的类目数据，为该类型时必须通过 data 设置类目数据。
                    //data: data.axisData,
                    data: ['工作日', '节假日', '周一', '周二', '周三', '周四', '周五'],
                    boundaryGap: true, //类目轴中 boundaryGap 可以配置为 true 和 false。默认为 true，这时候刻度只是作为分隔线，标签和数据点都会在两个刻度之间的带(band)中间。
                    nameGap: 30, //坐标轴名称与轴线之间的距离。
                    splitArea: { //坐标轴在 grid 区域中的分隔区域，默认不显示。
                        //show: true, //是否显示分隔区域
                        //interval: 'auto', //坐标轴分隔区域的显示间隔，在类目轴中有效
                    },
                    axisLabel: { //坐标轴刻度标签的相关设置。
                        //formatter: 'expr {value}',  // 使用字符串模板，模板变量为刻度默认标签 {value}
                        show: true, //是否显示刻度标签。
                        //interval: 'auto', //坐标轴刻度标签的显示间隔，在类目轴中有效。
                        color: 'black',

                    },
                    splitLine: { //坐标轴在 grid 区域中的分隔线。
                        show: true, //是否显示分隔线。默认数值轴显示，类目轴不显示。
                        lineStyle: { //分隔线样式
                            type: 'dashed', //分隔线线的类型。
                        },
                    },
                    axisLine: { //坐标轴轴线相关设置。
                        show: true, //是否显示坐标轴轴线。
                        //onZero:false,//X 轴或者 Y 轴的轴线是否在另一个轴的 0 刻度上，只有在另一个轴为数值轴且包含 0 刻度时有效。
                        //symbol:'arrow', //轴线两边的箭头, 默认不显示箭头，即 'none'
                        lineStyle: { //轴线样式
                            width: 2,
                            color: 'green',
                            //opacity: 1, //图形透明度。支持从 0 到 1 的数字，为 0 时不绘制该图形。
                        },
                    },
                    axisTick: { //坐标轴刻度相关设置。
                        show: true, //是否显示坐标轴刻度。
                        //alignWithLabel: true,//类目轴中在 boundaryGap 为 true 的时候有效，可以保证刻度线和标签对齐,default: false

                    }
                },
                yAxis: { //y轴
                    type: 'value',
                    splitArea: { //坐标轴在 grid 区域中的分隔区域，默认不显示。
                        //show: true
                    },
                    axisLabel: { //坐标轴刻度标签的相关设置。
                        //formatter: 'expr {value}',  // 使用字符串模板，模板变量为刻度默认标签 {value}
                        show: true, //是否显示刻度标签。
                        //interval: 'auto', //坐标轴刻度标签的显示间隔，在类目轴中有效。
                        color: 'black',
                    },
                    splitLine: {
                        show: true,
                        lineStyle: {
                            type: 'dashed'
                        },
                    },
                    axisLine: {
                        show: true, //是否显示坐标轴轴线。
                        //onZero:false,//X 轴或者 Y 轴的轴线是否在另一个轴的 0 刻度上，只有在另一个轴为数值轴且包含 0 刻度时有效。
                        //symbol:'arrow', //轴线两边的箭头
                        lineStyle: {
                            width: 2,
                            color: 'green',
                        },
                    },
                },
                series: [
                    {
                        name: 'boxplot',//箱形图
                        type: 'boxplot',
                        //legendHoverLink: true, //是否启用图例 hover 时的联动高亮。
                        //hoverAnimation: false, //是否开启 hover 在 box 上的动画效果。
                        itemStyle: { //盒须图样式。
                            //color: '#fff', //boxplot图形的颜色。 默认从全局调色盘 option.color 获取颜色
                            borderColor: 'blue', //boxplot图形的描边颜色。支持的颜色格式同 color，不支持回调函数。
                        },
                        data: data.boxData,
                        tooltip: { //注意：series.tooltip 仅在 tooltip.trigger 为 'item' 时有效。
                            formatter: function (param) {
                                return [
                                    '类目名 ' + param.name + ': ',
                                    'upper: ' + param.data[5],
                                    'Q3: ' + param.data[4],
                                    'median: ' + param.data[3],
                                    'Q1: ' + param.data[2],
                                    'lower: ' + param.data[1]
                                ].join('<br/>')
                            }
                        }
                    },
                    {
                        name: '异常值',//异常值
                        type: 'scatter',//分散
                        data: data.outliers
                    }
                ]
            };

            // 使用刚指定的配置项和数据显示图表。
            myChart.hideLoading();
            myChart.setOption(option);
        },
        error: function () {
            alert("加载图片失败");
        }
    });

    // 基于准备好的dom，初始化echarts实例
    formData = new FormData();
    formData.append("file","/featureScores.json");
    //绘制箱型图
    $.ajax({
        type: "post",
        url: "/file/getJson",
        contentType: false,
        processData: false,
        async: false,
        data:formData,
        success: function (value) {
            var myChart = echarts.init(document.getElementById('featureScore'));
            var jsonData = JSON.parse(value);
            // 指定图表的配置项和数据
            var option = {
                tooltip: {},
                legend: {
                    data: ['特征']
                },
                xAxis: {
                    data: jsonData.sorted_names
                },
                yAxis: {},
                series: [{
                    name: '特征',
                    type: 'bar',
                    data: jsonData.sorted_scores,
                    itemStyle: {
                        normal: {
                            color: 'grey'
                        },
                        emphasis: {//悬浮的渐变色在这里
                            color: 'red'
                        }
                    }
                }]
            };
            myChart.hideLoading();
            myChart.setOption(option);
        },error:function () {
            alert("图片加载失败");
        }
    });
    formData = new FormData();
    formData.append("file","/LoadAndWeather.json");
    $.ajax({
        type: "post",
        url: "/file/getJson",
        contentType: false,
        processData: false,
        async: false,
        data:formData,
        success:function (value) {
            var myChart = echarts.init(document.getElementById('loadAndWeather'));
            var jsonData = JSON.parse(value);
            option = {
                // x轴
                xAxis: {
                    data: jsonData.date // x轴坐标名称
                },
                // y轴
                yAxis: {},
                // 提示框，鼠标悬浮交互时的信息提示
                tooltip: {
                    show: true, // 是否显示
                    trigger: 'axis', // 触发类型，默认数据触发，见下图，可选为：'item' | 'axis'
                },
                legend: {
                    data: ['电力负荷', '最高温度', '最低温度', '平均温度']
                },
                dataZoom: [{
                    type: "slider",         //详细配置可见echarts官网
                    show: true,
                    realtime: true,
                    start: 0,
                    end: 100
                }],
                // 指定图标的类型
                series: [
                    // 第一条折线图
                    {
                        name: '电力负荷', // 系列名称
                        type: 'line', // 类型：线
                        data: jsonData.load, // 数据
                        markPoint: {
                            // 标注图形数据
                            data: [{
                                type: 'max', // 类型
                                symbol: 'pin', // 标志图形类型，默认自动选择（8种类型循环使用，不显示标志图形可设为'none'），默认循环选择类型有：'circle' | 'rectangle' | 'triangle' | 'diamond' |'emptyCircle' | 'emptyRectangle' | 'emptyTriangle' | 'emptyDiamond' 另外，还支持五种更特别的标志图形'heart'（心形）、'droplet'（水滴）、'pin'（标注）、'arrow'（箭头）和'star'（五角星），这并不出现在常规的8类图形中，但无论是在系列级还是数据级上你都可以指定使用，同时，'star' + n（n>=3)可变化出N角星，如指定为'star6'则可以显示6角星
                                name: '最大值'
                            },
                                {
                                    type: 'min', // 类型
                                    symbol: 'pin',
                                    name: '最小值'
                                },
                            ],
                            // 特殊标注文字
                            label: {
                                normal: {
                                    show: true,
                                    //position: 'top', // 文字位置
                                    // 显示的文字
                                    formatter: '{b}：{c}',
                                }
                            },
                            // 触发操作
                            tooltip: {
                                show: true, // 是否显示
                                formatter: '{b}：{c}', // 内容格式器 a（系列名称），b（类目值），c（数值）, d（无）
                                trigger: 'item', // 触发类型，默认数据触发，见下图，可选为：'item' | 'axis'
                            },
                        },
                        markLine: {
                            data: [{
                                type: 'average',
                                name: '平均值',
                                itemStyle: {
                                    normal: {
                                        color: 'orange'
                                    }
                                }
                            }]
                        },
                        // 折线图圆点
                        label: {
                            normal: {
                                show: true,
                                //position: 'bottom', // 文字位置
                                // 显示的文字
                                formatter: '{c}',
                                textStyle: {
                                    color: '#000' // 文字颜色
                                }
                            }
                        },
                    },
                    // 第二条折线图
                    {
                        name: '最高温度', // 系列名称
                        type: 'line', // 类型：线
                        data: jsonData.high, // 数据
                        markPoint: {
                            // 标注图形数据
                            data: [{
                                type: 'max', // 类型
                                symbol: 'circle', // 标志图形类型，默认自动选择（8种类型循环使用，不显示标志图形可设为'none'），默认循环选择类型有：'circle' | 'rectangle' | 'triangle' | 'diamond' |'emptyCircle' | 'emptyRectangle' | 'emptyTriangle' | 'emptyDiamond' 另外，还支持五种更特别的标志图形'heart'（心形）、'droplet'（水滴）、'pin'（标注）、'arrow'（箭头）和'star'（五角星），这并不出现在常规的8类图形中，但无论是在系列级还是数据级上你都可以指定使用，同时，'star' + n（n>=3)可变化出N角星，如指定为'star6'则可以显示6角星
                                name: '最大值'
                            },
                                {
                                    type: 'min', // 类型
                                    symbol: 'circle',
                                    name: '最小值'
                                },
                            ],
                            // 特殊标注文字
                            label: {
                                normal: {
                                    show: true,
                                    //position: 'top', // 文字位置
                                    // 显示的文字
                                    formatter: '{b}：{c}',
                                }
                            },
                            // 触发操作
                            tooltip: {
                                show: true, // 是否显示
                                formatter: '{b}：{c}', // 内容格式器 a（系列名称），b（类目值），c（数值）, d（无）
                                trigger: 'item', // 触发类型，默认数据触发，见下图，可选为：'item' | 'axis'
                            },
                        },
                        markLine: {
                            data: [{
                                type: 'average',
                                name: '平均值',
                                itemStyle: {
                                    normal: {
                                        color: 'green'
                                    }
                                }
                            }]
                        },
                        // 折线图圆点
                        label: {
                            normal: {
                                show: true,
                                //position: 'bottom', // 文字位置
                                // 显示的文字
                                formatter: '{c}',
                                textStyle: {
                                    color: '#000' // 文字颜色
                                }
                            }
                        },
                    },
                    // 第三条折线图
                    {
                        name: '最低温度', // 系列名称
                        type: 'line', // 类型：线
                        data: jsonData.low, // 数据
                        markPoint: {
                            // 标注图形数据
                            data: [{
                                type: 'max', // 类型
                                symbol: 'circle', // 标志图形类型，默认自动选择（8种类型循环使用，不显示标志图形可设为'none'），默认循环选择类型有：'circle' | 'rectangle' | 'triangle' | 'diamond' |'emptyCircle' | 'emptyRectangle' | 'emptyTriangle' | 'emptyDiamond' 另外，还支持五种更特别的标志图形'heart'（心形）、'droplet'（水滴）、'pin'（标注）、'arrow'（箭头）和'star'（五角星），这并不出现在常规的8类图形中，但无论是在系列级还是数据级上你都可以指定使用，同时，'star' + n（n>=3)可变化出N角星，如指定为'star6'则可以显示6角星
                                name: '最大值'
                            },
                                {
                                    type: 'min', // 类型
                                    symbol: 'circle',
                                    name: '最小值'
                                },
                            ],
                            // 特殊标注文字
                            label: {
                                normal: {
                                    show: true,
                                    //position: 'top', // 文字位置
                                    // 显示的文字
                                    formatter: '{b}：{c}',
                                }
                            },
                            // 触发操作
                            tooltip: {
                                show: true, // 是否显示
                                formatter: '{b}：{c}', // 内容格式器 a（系列名称），b（类目值），c（数值）, d（无）
                                trigger: 'item', // 触发类型，默认数据触发，见下图，可选为：'item' | 'axis'
                            },
                        },
                        markLine: {
                            data: [{
                                type: 'average',
                                name: '平均值',
                                itemStyle: {
                                    normal: {
                                        color: 'yellow'
                                    }
                                }
                            }]
                        },
                        // 折线图圆点
                        label: {
                            normal: {
                                show: true,
                                //position: 'bottom', // 文字位置
                                // 显示的文字
                                formatter: '{c}',
                                textStyle: {
                                    color: '#000' // 文字颜色
                                }
                            }
                        },
                    },
                    // 第四条折线图
                    {
                        name: '平均温度', // 系列名称
                        type: 'line', // 类型：线
                        data: jsonData.avg, // 数据
                        markPoint: {
                            // 标注图形数据
                            data: [{
                                type: 'max', // 类型
                                symbol: 'circle', // 标志图形类型，默认自动选择（8种类型循环使用，不显示标志图形可设为'none'），默认循环选择类型有：'circle' | 'rectangle' | 'triangle' | 'diamond' |'emptyCircle' | 'emptyRectangle' | 'emptyTriangle' | 'emptyDiamond' 另外，还支持五种更特别的标志图形'heart'（心形）、'droplet'（水滴）、'pin'（标注）、'arrow'（箭头）和'star'（五角星），这并不出现在常规的8类图形中，但无论是在系列级还是数据级上你都可以指定使用，同时，'star' + n（n>=3)可变化出N角星，如指定为'star6'则可以显示6角星
                                name: '最大值'
                            },
                                {
                                    type: 'min', // 类型
                                    symbol: 'circle',
                                    name: '最小值'
                                },
                            ],
                            // 特殊标注文字
                            label: {
                                normal: {
                                    show: true,
                                    //position: 'top', // 文字位置
                                    // 显示的文字
                                    formatter: '{b}：{c}',
                                }
                            },
                            // 触发操作
                            tooltip: {
                                show: true, // 是否显示
                                formatter: '{b}：{c}', // 内容格式器 a（系列名称），b（类目值），c（数值）, d（无）
                                trigger: 'item', // 触发类型，默认数据触发，见下图，可选为：'item' | 'axis'
                            },
                        },
                        markLine: {
                            data: [{
                                type: 'average',
                                name: '平均值',
                                itemStyle: {
                                    normal: {
                                        color: 'blue'
                                    }
                                }
                            }]
                        },
                        // 折线图圆点
                        label: {
                            normal: {
                                show: true,
                                //position: 'bottom', // 文字位置
                                // 显示的文字
                                formatter: '{c}',
                                textStyle: {
                                    color: '#000' // 文字颜色
                                }
                            }
                        },
                    }
                ]
            };

            //可伸缩
            // myChart.setOption()

            // 使用刚指定的配置项和数据显示图表。
            myChart.hideLoading();
            myChart.setOption(option);
        },error:function () {
            alert("图片加载失败");
        }

    });
    // 基于准备好的dom，初始化echarts实例
    formData  = new FormData();
    formData.append("file","/corrScores.json")
    $.ajax({
        type: "post",
        url: "/file/getJson",
        contentType: false,
        processData: false,
        async: false,
        data:formData,
        success: function (value) {
            var myChart = echarts.init(document.getElementById('heat'));
            var jsonData = JSON.parse(value);
            // 指定图表的配置项和数据
            var option = {
                tooltip: {},
                legend: {
                    data: ['特征']
                },
                xAxis: {
                    type: 'category',
                    data: jsonData.corrNames
                },
                yAxis: {},
                series: [{
                    name: '特征',
                    type: 'bar',
                    data: jsonData.corrScores,
                    barWidth: '80%',
                    itemStyle: {
                        normal: {
                            color: 'green'
                        },
                        emphasis: {//悬浮的渐变色在这里
                            color: 'blue'
                        }
                    }
                }]
            };
            myChart.hideLoading();
            myChart.setOption(option);
        },error:function () {
            alert("图片加载失败");
        }
    });
}