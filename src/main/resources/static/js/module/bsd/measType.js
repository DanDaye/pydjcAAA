/*function initMonitorType() {
	$.ajax({
		type : 'get',
		url : '/monitorType/list',
		async : false,
		success : function(data) {
			var select = $("#deviceTypeId");
			for (var i = 0; i < data.length; i++) {
				var v = data[i];
				select.append("<option value='" + v.id + "'>" + v.name + "</option>")
			}
		}
	});
}*/
function initVandor() {
	$.ajax({
		type : 'get',
		url : '/Vandor/list',
		async : false,
		success : function(data) {
			var select = $("#vandorId");
			for (var i = 0; i < data.length; i++) {
				var v = data[i];
				select.append("<option value='" + v.id + "'>" + v.shortName + "</option>")
			}
		}
	});
}

function getCheckedMeasTypeIds() {
	var ids = [];
	$("#isStart input[type='checkbox']").each(function() {
		if ($(this).prop("checked")) {
			ids.push($(this).val(1));
		}else{
			ids.push($(this).val(0));
		}
	});

	return ids;
}

function initRoleDatas(userId) {
	$.ajax({
		type : 'get',
		url : '/roles?userId=' + userId,
		success : function(data) {
			var length = data.length;
			for (var i = 0; i < length; i++) {
				$("input[type='checkbox']").each(function() {
					var v = $(this).val();
					if (v == data[i]['id']) {
						$(this).attr("checked", true);
					}
				});
			}
		}
	});
}