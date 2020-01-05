Date.prototype.Format = function (fmt) { //author: meizz 
            var o = {
                "M+": this.getMonth() + 1, //月份 
                "d+": this.getDate(), //日 
                "h+": this.getHours(), //小时 
                "m+": this.getMinutes(), //分 
                "s+": this.getSeconds(), //秒 
                "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
                "S": this.getMilliseconds() //毫秒 
            };
            if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
            for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
            return fmt;
}
var height = $(window).height()-52;

/*
 * 负荷
 */
var ElectricTableInit = function () {
	  var oTableInit = new Object();
	  //初始化Table
	  oTableInit.Init = function () {
		  $('#dt-table').bootstrapTable('destroy').bootstrapTable({
		            url: '/elec/dayElectric/list',      //请求后台的URL（*）
		            method: 'get',                      //请求方式（*）
		            dataField: 'rows',
		            striped: true,                      //是否显示行间隔色
		            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
		            sortable: false,                     //是否启用排序
		            sortOrder: "asc",                   //排序方式
		            queryParams: oTableInit.queryParams,//传递参数（*）
		            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
		            showColumns: true,                  //是否显示所有的列
		            clickToSelect: true,                //是否启用点击选中行
		            height: height,                     //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
		            uniqueId: "id",                     //每一行的唯一标识，一般为主键列
		            detailView: false,                  //是否显示父子表
		            columns: [
		            [{
		               	field: 'id',
		                title: '序号',
		                rowspan: 2,
		                sortable: true,
		                align: 'center',
		                valign: 'middle',
						formatter: formatID
		            }, {
		                field: 'name',
		                title: '监测点',
		                rowspan: 2,
		     		    align: 'center',
		                valign: 'middle',
						formatter: operateFormatter
		            }, {
		                field: 'date',
		                title: '日期',
		                rowspan: 2,
		     		    align: 'center',
		                valign: 'middle'  
		            }, {
		                title: '负荷(KW)',
		                colspan: 5,
		                align: 'center'
		            }],
		            [{
		                  field: 'maxP',
		                  title: '最大负荷',
		                  sortable: true,
		                  align: 'center'
		             }, {
		                  field: 'minP',
		                  title: '最小负荷',
		                  sortable: true,
		                  align: 'center'
		             },{
		                  field: 'meanP',
		                  title: '平均负荷',
		                  sortable: true,
		                  align: 'center'
		             },{
		                  field: 'maxMinDiffP',
		                  title: '峰谷差率',
		                  sortable: true,
		                  align: 'center'
		             },{
		                  field: 'avgP',
		                  title: '负荷率',
		                  sortable: true,
		                  align: 'center'
		             }
		            ]]
	      });
	  };
	    
	  //得到查询的参数
	  oTableInit.queryParams = function (params) {
		    var temp = {
		        type:'electric',
		        deviceId: deviceId,
		        date:$('#date').val(),
		        measPointType:'P'
		    };
		    return temp;
	  };
	    
	  return oTableInit;
};

/*
 * 电量
 */
var PowerTableInit = function () {
	  var oTableInit = new Object();
	  //初始化Table
	  oTableInit.Init = function () {
		  $('#dt-table').bootstrapTable('destroy').bootstrapTable({
		            url: '/elec/dayElectric/list',      //请求后台的URL（*）
		            method: 'get',                      //请求方式（*）
		            dataField: 'rows',
		            striped: true,                      //是否显示行间隔色
		            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
		            sortable: false,                     //是否启用排序
		            sortOrder: "asc",                   //排序方式
		            queryParams: oTableInit.queryParams,//传递参数（*）
		            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
		            showColumns: true,                  //是否显示所有的列
		            clickToSelect: true,                //是否启用点击选中行
		            height: height,                     //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
		            uniqueId: "id",                     //每一行的唯一标识，一般为主键列
		            detailView: false,                   //是否显示父子表
		            columns: [
                      [{
                          field: 'id',
                          title: '序号',
                          rowspan: 2,
                          sortable: true,
                          align: 'center',
                          valign: 'middle',
          				formatter: formatID
                      }, {
                          field: 'name',
                          title: '监测点',
                          rowspan: 2,
               		    align: 'center',
                          valign: 'middle',
          				formatter: operateFormatter 	
                      }, {
                          field: 'date',
                          title: '日期',
                          rowspan: 2,
               		    align: 'center',
                          valign: 'middle'  
                      }, {
                          title: '有功电量(kwh)',
                          colspan: 4,
                          align: 'center'
                      }, {
                     		field: "Eq",
                          title: '无功电量(kvar)',
                          rowspan: 2,
                          align: 'center'
                      }],
                      [{
                            field: 'Ep',
                            title: '总',
                            sortable: true,
                            align: 'center'
                       }, {
                            field: 'outPeakValue',
                            title: '峰',
                            sortable: true,
                            align: 'center'
                       },{
                            field: 'outValeValue',
                            title: '谷',
                            sortable: true,
                            align: 'center'
                       },{
                            field: 'outPikeValue',
                            title: '尖峰',
                            sortable: true,
                            align: 'center'
                       }
                      ]]
	      });
	  };
	    
	  //得到查询的参数
	  oTableInit.queryParams = function (params) {
		    var temp = {
		        type:'electric',
		        deviceId: deviceId,
		        date:$('#date').val(),
		        measPointType:'E'
		    };
		    return temp;
	  };
	    
	  return oTableInit;
};

/*示数表格*/
var IndicateTableInit = function () {
	  var oTableInit = new Object();
	  //初始化Table
	  oTableInit.Init = function () {

        $('#dt-table').bootstrapTable('destroy').bootstrapTable({
        	url: '/elec/dayElectric/list',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            dataField: 'rows',
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            sortable: false,                     //是否启用排序
            sortOrder: "asc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            showColumns: true,                  //是否显示所有的列
            clickToSelect: true,                //是否启用点击选中行
            height: height,                     //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "id",                     //每一行的唯一标识，一般为主键列
            detailView: false,                   //是否显示父子表
            columns: [
            [{
				field: 'id',
                title: '序号',
                rowspan: 2,
                sortable: true,
                align: 'center',
                valign: 'middle',
				formatter: formatID
            }, {
                field: 'name',
                title: '监测点',
                rowspan: 2,
     		    align: 'center',
                valign: 'middle',
				formatter: operateFormatter     	
            }, {
                field: 'date',
                title: '日期',
                rowspan: 2,
     		    align: 'center',
                valign: 'middle'  
            }, {
                title: '示数(KVh)',
                colspan: 5,
                align: 'center'
            }, {
                title: '最大需量(KVh)',
                colspan: 2,
                align: 'center'
            }],
            [{
                  field: 'maxEp',
                  title: '总',
                  sortable: true,
                  align: 'center'
             }, {
                  field: '',
                  title: '峰',
                  sortable: true,
                  align: 'center'
             },{
                  field: '',
                  title: '平',
                  sortable: true,
                  align: 'center'
             },{
                  field: '',
                  title: '谷',
                  sortable: true,
                  align: 'center'
             },{
                  field: '',
                  title: '尖峰',
                  sortable: true,
                  align: 'center'
             },{
                  field: 'maxP',
                  title: '最大需量',
                  sortable: true,
                  align: 'center'
             },{
                  field: 'maxDate',
                  title: '发生时间',
                  sortable: true,
                  align: 'center'
             }
            ]]
        });
	  };
	  //得到查询的参数
	  oTableInit.queryParams = function (params) {
		    var temp = {
		        type:'indicate',
		        deviceId: deviceId,
		        date:$('#date').val()
		    };
		    return temp;
	  };
	  return oTableInit;
};




/*电压表格*/
var VoltageTableInit = function () {
	  var oTableInit = new Object();
	  //初始化Table
	  oTableInit.Init = function () {

        $('#dt-table').bootstrapTable('destroy').bootstrapTable({
        	url: '/elec/dayElectric/list',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            dataField: 'rows',
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            sortable: false,                     //是否启用排序
            sortOrder: "asc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            showColumns: true,                  //是否显示所有的列
            clickToSelect: true,                //是否启用点击选中行
            height: height,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "id",                     //每一行的唯一标识，一般为主键列
            detailView: false,                   //是否显示父子表
            columns: [
            [{
            	field: 'id',
                title: '序号',
                rowspan: 2,
                sortable: true,
                align: 'center',
                valign: 'middle',
				formatter: formatID
            }, {
                field: 'name',
                title: '监测点',
                rowspan: 2,
     		    align: 'center',
                valign: 'middle',
				formatter: operateFormatter    	
            }, {
                field: 'date',
                title: '日期',
                rowspan: 2,
     		    align: 'center',
                valign: 'middle'  
            }, {
                title: '最高电压',
                colspan: 3,
                align: 'center'
            }, {
                title: '最低电压',
                colspan: 3,
                align: 'center'
            }],
            [{
                  field: 'maxVa',
                  title: 'A相电压',
                  sortable: true,
                  align: 'center'
             }, {
                  field: 'maxVb',
                  title: 'B相电压',
                  sortable: true,
                  align: 'center'
             },{
                  field: 'maxVc',
                  title: 'C相电压',
                  sortable: true,
                  align: 'center'
             },{
                  field: 'minVa',
                  title: 'A相电压',
                  sortable: true,
                  align: 'center'
             },{
                  field: 'minVb',
                  title: 'B相电压',
                  sortable: true,
                  align: 'center'
             },{
                  field: 'minVb',
                  title: 'C相电压',
                  sortable: true,
                  align: 'center'
             }
            ]]
        });
	 };
	 //得到查询的参数
	  oTableInit.queryParams = function (params) {
		    var temp = {
		        type:'voltage',
		        deviceId: deviceId,
		        date:$('#date').val(),
		        measPointType:'U'
		    };
		    return temp;
	  };
	  return oTableInit;
};


/*电流表格*/

var CurrentTableInit = function () {
	  var oTableInit = new Object();
	  //初始化Table
	  oTableInit.Init = function () {
        $('#dt-table').bootstrapTable('destroy').bootstrapTable({
        	url: '/elec/dayElectric/list',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            dataField: 'rows',
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            sortable: false,                     //是否启用排序
            sortOrder: "asc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            showColumns: true,                  //是否显示所有的列
            clickToSelect: true,                //是否启用点击选中行
            height: height,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "id",                     //每一行的唯一标识，一般为主键列
            detailView: false,                   //是否显示父子表
            columns: [
            [{
             	field: 'id',
                title: '序号',
                rowspan: 2,
                sortable: true,
                align: 'center',
                valign: 'middle',
				formatter: formatID
            }, {
                field: 'name',
                title: '监测点',
                rowspan: 2,
     		    align: 'center',
                valign: 'middle',
				formatter: operateFormatter    	
            }, {
                field: 'date',
                title: '日期',
                rowspan: 2,
     		    align: 'center',
                valign: 'middle'  
            }, {
                title: '最高电流(A)',
                colspan: 3,
                align: 'center'
            }, {
                title: '最低电流(A)',
                colspan: 3,
                align: 'center'
            }],
            [{
                  field: 'maxIa',
                  title: 'A相电流',
                  sortable: true,
                  align: 'center'
             }, {
                  field: 'maxIb',
                  title: 'B相电流',
                  sortable: true,
                  align: 'center'
             },{
                  field: 'maxIc',
                  title: 'C相电流',
                  sortable: true,
                  align: 'center'
             },{
                  field: 'minIa',
                  title: 'A相电流',
                  sortable: true,
                  align: 'center'
             },{
                  field: 'minIb',
                  title: 'B相电流',
                  sortable: true,
                  align: 'center'
             },{
                  field: 'minIc',
                  title: 'C相电流',
                  sortable: true,
                  align: 'center'
             }
            ]]
        });
	  };
	  //得到查询的参数
	  oTableInit.queryParams = function (params) {
		    var temp = {
		        type:'current',
		        deviceId: deviceId,
		        date:$('#date').val(),
		        measPointType:'I'
		    };
		    return temp;
	  };
	  return oTableInit;
};


/*功率因素表格*/
var FactorTableInit = function () {
	  var oTableInit = new Object();
	  //初始化Table
	  oTableInit.Init = function () {
		 
        $('#dt-table').bootstrapTable('destroy').bootstrapTable({
        	url: '/elec/dayElectric/list',         //请求后台的URL（*）
            method: 'get',                      //请求方式（*）
            dataField: 'rows',
            striped: true,                      //是否显示行间隔色
            cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            sortable: false,                     //是否启用排序
            sortOrder: "asc",                   //排序方式
            queryParams: oTableInit.queryParams,//传递参数（*）
            sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
            showColumns: true,                  //是否显示所有的列
            clickToSelect: true,                //是否启用点击选中行
            height: height,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
            uniqueId: "id",                     //每一行的唯一标识，一般为主键列
            detailView: false,                   //是否显示父子表
            columns: [
            [{
             	field: 'no',
                title: '序号',
                rowspan: 2,
                sortable: true,
                align: 'center',
                valign: 'middle',
				formatter: formatID
            }, {
                field: 'name',
                title: '监测点',
                rowspan: 2,
     		    align: 'center',
                valign: 'middle',
				formatter: operateFormatter
            }, {
                field: 'date',
                title: '日期',
                rowspan: 2,
     		    align: 'center',
                valign: 'middle'  
            }, {
                title: '平均功率因数',
                colspan: 2,
                align: 'center'
            }],
            [{
                  field: 'standardCos',
                  title: '标准',
                  sortable: true,
                  align: 'center'
             }, {
                  field: 'meanCos',
                  title: '功率因数',
                  sortable: true,
                  align: 'center'
             }
            ]]
        });
	 };
	 //得到查询的参数
	  oTableInit.queryParams = function (params) {
		    var temp = {
		        type:'factor',
		        deviceId: deviceId,
		        date:$('#date').val(),
		        measPointType:'Cos'
		    };
		    return temp;
	  };
	  return oTableInit;
};


function formatID(value ,row,index) {
	return index+1;
}
