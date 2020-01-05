function initDeviceType() {
	$.ajax({
		type : 'get',
		url : '/deviceType/list',
		async : false,
		success : function(data) {
			var select = $("#deviceTypeId");
			for (var i = 0; i < data.length; i++) {
				var v = data[i];
				select.append("<option value='" + v.id + "'>" + v.name + "</option>")
			}
		}
	});
}
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