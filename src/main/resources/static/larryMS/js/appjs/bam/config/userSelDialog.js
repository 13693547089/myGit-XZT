var table;
var prefix = "/bam/cf";

var checkedRow = [];
layui.use('table', function(){
	  table = layui.table;
	  var $ = layui.$;
	  
	  // 加载数据
	  loadUserData();
	  
	  //监听表格复选框选择
	  table.on('checkbox(demo)', function(obj){
		  	var type = obj.type;
			var checked = obj.checked;
			var data = obj.data;
			if (type == 'all') {
				var cacheData = table.cache.userTableId;
				if (checked) {
					var tempData = checkedRow;
					for ( var k = 0; k < cacheData.length; k++) {
						var elem = cacheData[k];
						if (tempData.indexOf(elem) == -1)
							checkedRow.push(elem);
					}
				} else {
					var tempData = [];
					for ( var k = 0; k < checkedRow.length; k++) {
						var elem = checkedRow[k];
						if (cacheData.indexOf(elem) == -1)
							tempData.push(elem);
					}
					checkedRow = tempData;
				}
			} else if (type = 'one') {
				if (checked) {
					checkedRow.push(data);
				} else {
					var tempData = [];
					for ( var i = 0; i < checkedRow.length; i++) {
						var elem = checkedRow[i];
						if (elem.mateId != data.mateId)
							tempData.push(elem);
					}
					checkedRow = tempData;
				}
			}
	  });
	  
	  /**
	   * 查询
	   */
	  $('#searchBtn').click(function(){
		  //initTableData();
		  	var query = $("#userInfo").val();
			// 执行重载
			table.reload('userTableId', {
				page : {
					curr : 1
				// 重新从第 1 页开始
				},
				where : {
					queryParams : query
				}
			});
	  });

	  /**
	   * 获取用户数据
	   * @returns
	   */
	  function loadUserData(){
			table.render({
				 elem:"#userTable",
				 url : '/sys/user/list',
				 page:true,
				 height:'auto',
				 limit:15,
				 limits:[15,30,50,100],
				 cols:[[{
					 	checkbox:true,
						width : 50
					 },{
						field : 'name',
						title : '姓名',
						width : 110
					}, {
						field : 'username',
						title : '用户名',
						width : 110
					},  {
						field : 'userType',
						title : '用户类型',
						width : 100,
						templet : function(d) {
							if (d.userType == 'user') {
								return '<span>内部用户</span>';
							} else {
								return '<span>外部供应商</span>';
							}
						}
					}, {
						field : 'sfnames',
						title : '全路径'
					}
				 ]],
				 id:'userTableId'
			 });
		}
});

function getCheckedData(){
	return checkedRow;
}






