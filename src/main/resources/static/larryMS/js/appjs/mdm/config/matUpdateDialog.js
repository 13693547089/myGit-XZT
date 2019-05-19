layui.use([ 'form' ], function() {
	var form = layui.form;
	var $ = layui.$;
})

function getData() {
	var board = $('#board').val();
	var boardName = $('#board').find("option:selected").text();
	if(boardName.indexOf('请选择')!=-1){
		boardName = '';
	}
	var itemCode = $('#itemType').val();
	var itemName = $('#itemType').find("option:selected").text();
	if(itemName.indexOf('请选择')!=-1){
		itemName = '';
	}
	var obj = {
		"itemCode" : itemCode,
		"itemName" : itemName,
		"board" : board,
		"boardName" : boardName
	};
	return obj;
}