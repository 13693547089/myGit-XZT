//加载功能树
function initTree() {
	$.jstree.destroy();
	$.ajax({
				type : "Get",
				url : "/Department/GetTreeData/0?_t=" + new Date().getTime(), // 获取数据的ajax请求地址
				success : function(data) {
					$('#dptTree').jstree({ // 创建JsTtree
						'core' : {
							'data' : data, // 绑定JsTree数据
							"multiple" : false // 是否多选
						},
						"plugins" : [ "wholerow" ]
					// 配置信息
					})
					$("#dptTree").on("after_open.jstree",
							function(event, data) {

							})
					$("#dptTree").on("ready.jstree", function(e, data) { // 树创建完成事件
						// data.instance.open_all(); //展开所有节点
					});
					$("#dptTree").on(
									'changed.jstree',
									function(e, data) { // 选中节点改变事件
										// 获取所有选中项目及子项目
										function getChildNodes(treeNode, result) {
											var childrenNodes = data.instance
													.get_children_dom(treeNode);
											if (childrenNodes) {
												for ( var i = 0; i < childrenNodes.length; i++) {
													var row = childrenNodes[i];
													if ($.inArray(row.id,
															result) == -1) {
														result.push(row.id);
													}
													result = getChildNodes(
															row.id, result);
												}
											}
											return result;
										}
										var result = [];
										// 获取选中的节点
										var node = data.instance
												.get_node(data.selected[0]);
										result.push(node.id);
										// 遍历选中节点的子节点
										var childNodes = data.instance
												.get_children_dom(node);
										for ( var i = 0; i < childNodes.length; i++) {
											var row = childNodes[i];
											if ($.inArray(row.Id, result) == -1) {
												result.push(row.id);
											}
											getChildNodes(row, result);
										}
										if (node && firstLoaded != 1) {
											dptIds = result; // 保存选中的节点ID
											dptParentId = node.parent; // 保存选中的节点父ID
											app.loaddata(1);
										}
										;
									});

					$("#dptTree")
							.on(
									'loaded.jstree',
									function(e, data) {
										// 当tree加载完毕时，获取树的根节点对象；
										// 调用select_node方法，选择根节点。
										firstLoaded = 1;
										var inst = data.instance;
										var obj = inst
												.get_node(e.target.firstChild.firstChild.lastChild);
										inst.select_node(obj);
										firstLoaded = 2;
									});
				}
			});
}