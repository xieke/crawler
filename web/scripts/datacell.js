var show_col	= false;
var charMode	= true;
var act_bgc	= "#BEC5DE";
var act_fc	= "black";
var cur_bgc	= "#ccffcc";
var cur_fc	= "black";

//把table初始化为datacell
//initDataCell( firesttable, entityType, queryAction, initParamFunction, rtXpath, submitAction );
function initDataCell( datacell, entityType, queryAction, initParamFunction, rtXpath, pageCount, submitAction ,noCount)
{
try{
	//属性
	datacell.cur_row		= null;
	datacell.cur_cell		= null;
	datacell._styleRow		= null;
	datacell._tbody			= null;
	datacell.editors		= new Object();
	datacell.oncellrefresh	= null;
	datacell.readOnly		= false;
	
	datacell.noCount        = noCount;
	datacell.currPage       = 1;
	datacell.entityType		= entityType;
	datacell.queryAction	= queryAction;
	datacell.rtXpath		= rtXpath;
	datacell.pageCount		= pageCount;
	datacell.submitAction	= submitAction;
	datacell.initParamFunction	= initParamFunction;
	datacell.initParam		= "";//_datacell_initAttachParam( initParamFunction );
	datacell.dataset		= new Dataset( entityType );
	
	datacell.pilot			= null;
	//datacell._drag		= _createDrag();
	datacell.editorC = document.createElement( "DIV" );
	document.body.appendChild( datacell.editorC );
	datacell.editorC.datacell = datacell;
	datacell.editorC.onkeydown = _editorC_onkeydown
	datacell.currPageRecords=datacell.tBodies[0].rows;
	//方法
	datacell.locked           = _datacell_locked;
　　datacell.unlocked         = _datacell_unlocked;
　	datacell.addrow			= _datacell_addrow;
	datacell.deleterow		= _datacell_deleterow;
	datacell.refresh		= _datacell_refresh;
	datacell.sort			= _datacell_sort;
    datacell.serverSort     =_datacell_serverSort;
	datacell.setEditor		= _datacell_setEditor;
	datacell.getEditor		= _datacell_getEditor;
	datacell.initAttachParam		= _datacell_initAttachParam;
	datacell.loadData		= _datacell_loadData;
	datacell.reload			= _datacell_reload;
	
	datacell.selectNextRow		= _datacell_selectNextRow;
	datacell.selectPreRow		= _datacell_selectPreRow;
	datacell.selectFirstRow		= _datacell_selectFirstRow;
	datacell.selectLastRow		= _datacell_selectLastRow;
	
	datacell.getNoCount         =_datacell_getNoCount;
	datacell.firstPage			= _datacell_firstPage;
	datacell.lastPage			= _datacell_lastPage;
	datacell.prePage			= _datacell_prePage;
	datacell.nextPage			= _datacell_nextPage;
	datacell.getPageIndex		= _datacell_getPageIndex;
	datacell.getPageCount		= _datacell_getPageCount;
	datacell.submit				= _datacell_submit;
	datacell.reset				= _datacell_reset;
	datacell.processSortIcon    =_datacell_processSortIcon;
	datacell.addOnrefreshListener	= _datacell_addOnrefreshListener;
	datacell.fireOnrefreshEvent		= _datacell_fireOnrefreshEvent;
	datacell.addcheckbox            =_datacell_addcheckbox;
	
	//事件
	datacell.onclick		= _datacell_onclick;
	datacell.onkeydown		= _datacell_onkeydown;
	datacell.onselectstart	= function(){return false;};
	datacell.onLostFocus	= _datacell_onLostFocus;
	
	//查找tbody的第一行，作为添加数据行的样式行
	datacell._tbody = datacell.tBodies[0];
	datacell._styleRow = datacell._tbody.rows[0].cloneNode( true );
	//datacell._tbody.deleteRow(0);
		
	//row的属性
	datacell._styleRow.entity = null;
	datacell._styleRow.table = datacell;
	//row的方法
	datacell._styleRow.refresh = _row_refresh;
	
	//初始化cell的编辑框
	initCellEditor();
	function initCellEditor(){
		var _row = datacell._tbody.rows[0];
		var cell, _editor;
		for( var i = 0; i < _row.cells.length; i++ )
		{
			cell = _row.cells[i];
			_editor = cell.getAttribute( "editor" );
			try{
				if( _editor )
				{
					//如果是checkBox编辑框
					if( _editor.indexOf( "checkBoxEditor" ) == 0 )
					{
						datacell.setEditor( cell.getAttribute( "name" ), _editor );
						datacell.addcheckbox(datacell,cell);
					}
					switch( _editor )
					{
						case "calendarEditor" :

						{　 if(cell.format)
							{
						    
                            if(cell.format=="yyyyMMddHHmmss"||cell.format=="HH:mm:ss,yyyy-MM-dd"||cell.format=="yyyy/MM/dd HH:mm:ss"||cell.format=="yyyy年MM月dd日HH时mm分ss秒")

								  
									datacell.setEditor( cell.getAttribute( "name" ), new CalendarEditor( "yyyyMMddHHmmss" ));  
									 
								else
									datacell.setEditor( cell.getAttribute( "name" ), new CalendarEditor( "yyyyMMdd" ));

						     }
						     else
							{
							datacell.setEditor( cell.getAttribute( "name" ), new CalendarEditor( "yyyyMMdd" ));
							}

							break;
						}
						case "textEditor" :
						{
							datacell.setEditor( cell.getAttribute( "name" ), new TextEditor( datacell ) );
							break;
						}
						default :
						{
							if( eval( _editor ) )
							{
								datacell.setEditor( cell.getAttribute( "name" ), eval( _editor ) );
							}
						}
					}
				}
			}catch( e2 )
			{
			}
		}
	}
	
	//注册事件，当点击页面时取消编辑状态
	PopBlockManager.register( datacell, "onLostFocus" );
	return datacell;
}catch( e )
{
	alert( " catch Exception in method : initDataCell " );
}
}
function _datacell_reset()
{if( !processCellBlur( this.cur_cell ) )
		return;
 if(!confirm("是否确定要重置？"))return;
		this.dataset.clear();
		this.dataset.reset();
		this.refresh();
		
}

function _datacell_onLostFocus()
{
	processCellBlur( this.cur_cell );
	//_datacell_unselectRow( this.cur_row );
}

//首页
function _datacell_firstPage()
{
	if( !processCellBlur( this.cur_cell ) )
		return;
	_datacell_unselectRow( this.cur_row );
	
　　//检查是否修改数据，如修改，提示是否继续操作
	if(isData_Modified(this))
	 {
     if(!confirm("数据已经修改，但并末提交，是否要继续？"))
		 {
         return;
	     }
	 }　


	if( this.getPageIndex() > 0 )
	{   
		this.locked();
		this.dataset.loadFirstPageData();	
		this.refresh();
		this.unlocked();
	}
	
}
//尾页
function _datacell_lastPage()
{
	if( !processCellBlur( this.cur_cell ) )
		return;
	_datacell_unselectRow( this.cur_row );
	
　　if(isData_Modified(this))
	 {
     if(!confirm("数据已经修改，但并末提交，是否要继续？"))
		 {
         return;
	     }
	 }　


	if( this.getPageIndex() < (this.getPageCount()-1) )
	{	this.locked();
		this.dataset.loadLastPageData();
		this.refresh();
		this.unlocked();
	}	
}
//向前翻页
function _datacell_prePage()
{
	if( !processCellBlur( this.cur_cell ) )
		return;		
	_datacell_unselectRow( this.cur_row );
	
	　　if(isData_Modified(this))
	 {
     if(!confirm("数据已经修改，但并末提交，是否要继续？"))
		 {
         return;
	     }
	 }　


	if( this.getPageIndex() > 0 )
	{		
		this.locked();
		this.dataset.loadPrePageData();
		this.refresh();
		this.unlocked();
	}
}
//向后翻页
function _datacell_nextPage()
{
	if( !processCellBlur( this.cur_cell ) )
		return;
	_datacell_unselectRow( this.cur_row );	

　　if(isData_Modified(this))
	 {
     if(!confirm("数据已经修改，但并末提交，是否要继续？"))
		 {
         return;
	     }
	 }　


	if(this.noCount=="true")
	{   this.locked();
	
		this.dataset.loadNextPageData();
		this.refresh();
		this.unlocked();
	}
		else

	if( this.getPageIndex() < (this.getPageCount()-1) )
	{	this.locked();
		this.dataset.loadNextPageData();
		this.refresh();
		this.unlocked();
	}
	
}

//提交变更的数据
function _datacell_submit()
{
	var isChanged=false;
	if( !processCellBlur( this.cur_cell ) )
		return;
	_datacell_unselectRow( this.cur_row );
	
	//校验输入数据是否符合指定格式
	var row, status, fieldName;
	for( var i = 0; i < this.tBodies[0].rows.length; i++ )
	{
		row = this.tBodies[0].rows[i];
		status = row.entity.status;

		if(this.noCount!="true")
       		if(status==Entity.STATUS_NEW) this.noCount="false";//是否有新增记录，如有，则设置noCount,需要重新计算总记录数
		
		if( (status == Entity.STATUS_NEW) || (status == Entity.STATUS_MODIFIED) )
		{   isChanged=true;
			for( var j = 0; j < row.cells.length; j++ )
			{
				fieldName = row.cells[j].getAttribute( "name" );
				var formatOK = VerifyValueFormat( row.cells[j], row.entity.getProperty( fieldName ) );
				if( !formatOK )
				{
					event.cancelBubble = true;
					event.returnValue = false;
					processCellActive( row.cells[j] );
					return;
				}
			}
		}
	}
	
　　if(this.dataset.removedEntities.length!=0)
	{
	   if(this.noCount=="1")this.noCount="false";//是否有删除记录，如有，则设置noCount,需要重新计算总记录数
	    isChanged=true;
	}
	if(isChanged==false) 
	  {
		alert("数据没有修改，不需要提交");
		return;
	}
	var dataset = this.dataset;
	dataset.submitAction	= this.submitAction;
	
	//如果提交成功，重新载入第一页数据
	this.locked();
	var submitSucess = dataset.submit( this.initParam );
	this.unlocked();
	if( submitSucess )
	{   this.locked();
		this.reload();
		this.unlocked();
	}
}

function _datacell_getPageIndex()
{
	return this.dataset.pageIndex;
}
//反回总页数
function _datacell_getPageCount()
{  
	//总记录数能被每页记录数整除
　　if(this.dataset.recordCount%this.dataset.rowNumb==0)
	return this.dataset.recordCount/this.dataset.rowNumb　
    //如不能整除，则补零;
	return (parseInt(this.dataset.recordCount)+parseInt(this.dataset.rowNumb-(this.dataset.recordCount%this.dataset.rowNumb)))/this.dataset.rowNumb;
}

/*
 * 方法说明：设置字段的编辑器，现有的编辑器类型有textEditor和selectEditor。如果字段没有设置编辑器，默认为textEditor
**/
function _datacell_setEditor( fieldName, editor )
{
	var editors = this.editors;
	eval( "editors." + entity_parse_invalid(fieldName) + " = editor" );
	
	//对checkBox编辑框，刷新时显示
	//editor为String:checkBoxEditor(uncheckedValue:checkedValue)
	if( typeof( editor ) == "string" )
	{
		var index1 = "checkBoxEditor(".length;
		var index2 = editor.length - 1;
		var str2 = editor.substring( index1, index2 );
		var values = str2.split( ":" );
		var uncheckedValue = values[0];
		var checkedValue = values[1];
		
		var fName = "chechBoxRefresh";
		var paramList = [uncheckedValue, checkedValue];
		
		this.addOnrefreshListener( fieldName, fName, paramList );
		return;
	}
	//--------------
	
	editor.holder = this;
	//this.appendChild( editor );
	this.editorC.appendChild( editor );
	//document.body.appendChild( editor );
	
	//对业务字典，注册刷新事件监听器，控制刷新时的显示
	
	var paramList = [ editor ];
	
	if( editor.type == "SelectEditor" )
	{    var fName = "dictRefresh";
		this.addOnrefreshListener( fieldName, fName, paramList );
	}
	//对日期格式，注册刷新事件监听器，控制刷新时的显示
	if( editor.type == "calendarEditor" )
	{   var fName = "calendarRefresh";
	  
		this.addOnrefreshListener( fieldName, fName, paramList );
	}
	
	return editor;
}

function dictRefresh( cell, editor )
{
	var dataset = editor.options.table.dataset;
	
	var map	= dataset.findEntity( editor.valueField, cell.value );
	if(map!=null)
	  cell.innerHTML = map.getProperty( editor.nameField );
}

function calendarRefresh( cell )
{
	if(cell.value==null||cell.value=="")return;	
	cell.innerHTML = formatSpecial(cell.value,cell.format);

}

function chechBoxRefresh( cell, uncheckedValue, checkedValue )
{
	if( !cell.checkBox )
	{
		cell.checkBox = new CheckBoxEditor( cell, uncheckedValue, checkedValue );
	}
	cell.innerHTML = "";
	cell.style.textAlign = "center";
	cell.appendChild( cell.checkBox );
	
    var row = cell.parentNode;
	var entity = row.entity;
	var datacell = row.table;
	if(cell.modify=="false")
		 if(entity.status!=Entity.STATUS_NEW)
	    cell.checkBox.disabled="true";

	cell.checkBox.refresh();
}

/*
 * 方法说明：获取字段的编辑器。如果字段没有设置编辑器，默认为textEditor
**/
function _datacell_getEditor( cell )
{
	var fieldName = cell.getAttribute( "name" );
	var editors = this.editors;
	var editor = eval(  "editors." + entity_parse_invalid(fieldName) );

	return editor;	
}

function _row_refresh()
{
try{
	var entity = this.entity;
	var fieldName;
	
	for( var j = 0; j < this.cells.length; j++ )
	{
		cell = this.cells[j];
		_cell_refresh( cell, entity );
	}
}catch( e )
{
	alert( " catch Exception in method : _row_refresh " );
}
}

function _cell_setDefault( cell, entity )
{
	var datacell = cell.parentNode.table;
	var fieldName = cell.getAttribute("name");
	var defaultValue=cell.getAttribute("defaultValue");

if(defaultValue!=null)
	 entity.setProperty( fieldName,defaultValue );
	//alert(cell.value);
	//alert(cell.editor);
	
}


function _cell_refresh( cell, entity )
{
	var datacell = cell.parentNode.table;
	var fieldName = cell.getAttribute("name");
	cell.value = entity.getProperty( fieldName );
	//alert(cell.value);
	//alert(cell.editor);

	cell.innerHTML = conversion(cell.value);

	//触发刷新事件

	datacell.fireOnrefreshEvent( cell );
	
	var onrefresh = datacell.oncellrefresh;
	if( onrefresh && onrefresh != "" )
	{
		fireUserEvent( onrefresh, [cell, fieldName, entity] );
	}
	//alert( fieldName + ":::" + entity + ":::" + cell.value);
}

function _datacell_initAttachParam()
{   
	if( this.initParamFunction && isUserEventDefined(this.initParamFunction) )
		fireUserEvent(this.initParamFunction, [this]);
	
}

function _datacell_loadData()
{
	with( this )
	{  
		initAttachParam();
		
		dataset.loadData( queryAction, initParam, [rtXpath], 0, pageCount,noCount );
		noCount=dataset.noCount;
	}
}

function _datacell_reload()
{
	
try{
	with( this )
	{
		//initAttachParam();
	    dataset.clear();
		dataset.pageIndex = 0;
		//dataset.recordCount = -1;
		dataset.loadData( queryAction, initParam, [rtXpath], 0, pageCount,noCount );
		noCount=dataset.noCount;
		refresh();
	}
}catch( e )
{
	alert( " catch Exception in method : _datacell_reload " );
}
}

function _datacell_refresh( dataset )
{
try{
	var datacell = this;
	var _dataset = dataset ? dataset : this.dataset;
	this.dataset = _dataset;
	var tbody = this._tbody;
	var _row = null;
	var cell = null;
	var entity = null;
	if( !processCellBlur( this.cur_cell ) )
		return;
	//清除所有行
	for( var j = tbody.rows.length -1; j >= 0; j-- )
	{
		tbody.deleteRow( j );
	}
	//显示数据
	
	for( var i = 0; i < _dataset.entities.length; i++ )
	{
		entity = _dataset.entities[i];
		_row = this._styleRow.cloneNode( true );
		_row.entity = entity;
		tbody.appendChild( _row );
		if( (_row.rowIndex%2) == 0 )
			_row.className = "RC_DATACELL_NEXTROW";
		_row.refresh();
	}
	
	this.cur_cell=null;
	//把第一行的第一个节点设置为编辑状态
	/*var firstRow = datacell._tbody.rows[0];
	if( firstRow && firstRow.cells[0] )
	{
		processCellActive( firstRow.cells[0] );
		_datacell_selectRow( firstRow );
	}*/
	
	//设置导航条按钮的状态
	
	if(this.noCount!="true")
	{ 
		this.pilot.refreshPageButton( this.getPageIndex(), this.getPageCount() );
	    this.pilot.refreshCurrPage( this.getPageIndex(), this.getPageCount() );
	  }
    	else
    	{
		this.pilot.refreshPageButtonwithNoCount( this.getPageIndex(),this.dataset.rowNumb,this.currPageRecords.length );
		this.pilot.refreshCurrPagewithNoCount(this.getPageIndex());
		}

	this.pilot.gotoPage.value="";
	
}catch( e )
{
	alert( " catch Exception in method : _datacell_refresh " );
}
}


function _datacell_unselectRow( the_row )
{
try{
	if( !the_row ) return;
	var table = the_row.table;
	
	//清除行的颜色
	if( (the_row.rowIndex%2) == 0 )
		the_row.className = "RC_DATACELL_NEXTROW";
	else
		the_row.className = "";
	table.cur_row = null;
}catch( e )
{
	alert( " catch Exception in method : _datacell_unselectRow " );
}
}

function _datacell_selectRow( the_row )
{
try{
	if( !the_row ) return;
	var table = the_row.table;
	
	if( (table.cur_row != null) && (the_row != table.cur_row) )
	{
		_datacell_unselectRow( table.cur_row );
	}
	//设置行的颜色
	the_row.className = "RC_DATACELL_ACTIVEROW";	
	table.cur_row = the_row;

//	table.pilot.refreshRowButton(table.cur_row.rowIndex,table.rows.length);
	
	
}catch( e )
{
	alert( " catch Exception in method : _datacell_selectRow " );
}
}

function _datacell_addrow()
{
//try{
	if( !processCellBlur( this.cur_cell ) )
		return;
	var new_row = this._styleRow.cloneNode( true );
	
	var new_cell = new_row.childNodes[0];
	if( !isCellEditorable( new_cell ) )
	{
		new_cell = getNextEditorableCell( new_cell );
	}
	if( !new_cell )
	{
		alert( "没有可编辑字段！" );
		delete new_row;
		return;
	}
	this.tBodies[0].appendChild( new_row );
	
	//dataset增加一entity
	new_row.entity = this.dataset.addEntity();
	new_cell.innerHTML = "";
	for( var i = 0; i < new_row.cells.length; i++ )
	{	 _cell_setDefault( new_row.cells[i], new_row.entity );
	 _cell_refresh( new_row.cells[i], new_row.entity );}
	
	/*var new_cell = new_row.cells[0];
	if( !isCellEditorable( new_cell ) )
		new_cell = getNextEditorableCell( new_cell );
	*/
	//给new_cell值，以便显示行的高度
	
	
	processCellActive( new_cell );
	_datacell_selectRow( new_row );
	
	//停止事件传播，让光标停留在new_cell上面
	event.cancelBubble = true;
	return false;
//}catch( e )
//{
//	alert( " catch Exception in method : _datacell_addrow " );
//}
		
}

function _datacell_deleterow( the_row )
{
try{
	var the_row = the_row ? the_row : this.cur_row;
	
	if( !the_row ) return;
	
	//当要删除的行是正在编辑的行时，处理单元格blur，并不校验
	if( this.cur_row == the_row && this.cur_cell )	
	{
		this.cur_cell.removeAttribute( "eos_displayname" );
		processCellBlur( this.cur_cell );
	}else
	{
		if( !processCellBlur( this.cur_cell ) && (this.cur_row != the_row) )
			return;
	}	
	
	var entity = the_row.entity;	
	var next_row = the_row.nextSibling ? the_row.nextSibling : the_row.previousSibling;
	
	the_row.removeNode(true);
	
	//dataset删除当前entity
	this.dataset.removeEntity( entity );
	
	if( next_row )
	{
		_datacell_selectRow( next_row );
	}
}catch( e )
{
	alert( " catch Exception in method : _datacell_deleterow " );
}
}

//交换表的列
function _datacell_changCol( colIndex1, colindex2 )
{
try{
	var table = this;
	for( var i = 0; i < this.rows.length; i++ )
	{
		table.rows[i].cells[colIndex1].swapNode( table.rows[i].cells[colIndex2] );
	}
}catch( e )
{
	alert( " catch Exception in method : _datacell_changCol " );
}
}

//交换表的行
function _datacell_changRoe( rowIndex1, rowlindex2 )
{
	var table = this;
	table.rows[rowIndex1].swapNode( table.rows[rowIndex2] );
}
//服务器端排序
function _datacell_serverSort(th)
{
	var sortMode;
	if( typeof(th.sortMode) == "undefined" )
	{
		th.sortMode = true;
	}

	if(th.sortMode)
	    { sortMode="ASC" ;
		   }
	else
	{sortMode="DESC"}
	th.sortMode=!th.sortMode

var table = this;

if( !processCellBlur( table.cur_cell ) )
		return;
var rowcount=table._tbody.rows.length;

if(rowcount=0)
	return
	else
	{
var fieldName= table._tbody.rows[0].cells[th.cellIndex].name;
//table.initAttachParam();
//构造orderby节点
var initParam=table.initParam+"<orderby><col1 "+"order='"+sortMode+"'>"+fieldName+"</col1></orderby>";
table.locked();
table.dataset.clear();
table.dataset.loadData( table.queryAction, initParam, [table.rtXpath], 0, table.pageCount,table.noCount );
table.unlocked();
table.noCount=table.getNoCount();
table.refresh();
	}

}

//按照某列排序
function _datacell_sort( th )
{
	var table = this;
	var colIndex = th.cellIndex;
	
	if( typeof(th.sortMode) == "undefined" )
	{
		th.sortMode = true;
	}
	
	if( !processCellBlur( table.cur_cell ) )
		return;
	
	//排序算法
	function SortArray( mode )
	{
		return function( arr1, arr2 )
		{
			var a = arr1[0];
			var b = arr2[0];			
			
			if( a == null && b == null )
				return 0;
			if( a == null )
				return mode ? 1 : -1;
			if( b == null )
				return mode ? -1 : 1;
				
			a = a.toString();
			b = b.toString();
			
			return mode?(a>b?1:(a<b?-1:0)):(a<b?1:(a>b?-1:0));
		}
	}
	
	var rowArray = new Array();
	var value;
	for( var i = 0; i < table.tBodies[0].rows.length; i++ )
	{
		value = table.tBodies[0].rows[i].cells[colIndex].value;
		if( value )
			rowArray.push( [value.toLowerCase(), table.tBodies[0].rows[i]] );
		else
			rowArray.push( [null, table.tBodies[0].rows[i]] );
	}
	
	rowArray.sort( SortArray( th.sortMode ) );
	th.sortMode = !th.sortMode;
	
	for( var j = 0; j < rowArray.length; j++ )
	{
		table.tBodies[0].appendChild( rowArray[j][1] );
		rowArray[j][1].refresh();
	}
	
	var _row;
	for( var m = 0; m < table.tBodies[0].rows.length; m++ )
	{
		_row = table.tBodies[0].rows[m];
		if( (m%2) == 1 )
			_row.className = "RC_DATACELL_NEXTROW";
		else
			_row.className = "";
	}
}

function _datacell_onkeydown()
{
try{
	//event.cancelBubble = true;
	switch( event.keyCode )
	{
		case 40 :	//当按"下"键时，下移一行
		{
			event.cancelBubble = true;
			temp = this.cur_cell;
			movetodowncell(temp);
			break;
		}
		case 38 :	//当按"上"键时，上移一行
		{
			event.cancelBubble = true;
			temp = this.cur_cell;
			movetoupcell(temp);
			break;
		}
		case 9:
		{
			event.cancelBubble = true;
			if( event.shiftKey )	//按'shift+tab'键，回退一格
			{
				temp = this.cur_cell;
				movetoprecell(temp);
				event.cancelBubble = true;
				return false;
			}else					//按'tab'键，前进一格
			{
				temp = this.cur_cell;
				movetonextcell(temp);
				event.cancelBubble = true;
				return false;
			}
			break;
		}
		case 13:					//按'回车'键，前进一格
		{
			temp = this.cur_cell;
			//setTimeout("movetonextcell(temp)", 0);
			movetonextcell(temp);
			event.cancelBubble = true;
			break;
		}
	}
}catch( e )
{
	alert( " catch Exception in method : _datacell_onkeydown " );
}
	
}

function _editorC_onkeydown()
{
try{
	//event.cancelBubble = true;
	var datacell = this.datacell;
	switch( event.keyCode )
	{
		case 40 :	//当按"下"键时，下移一行
		{
			event.cancelBubble = true;
			temp = datacell.cur_cell;
			movetodowncell(temp);
			break;
		}
		case 38 :	//当按"上"键时，上移一行
		{
			event.cancelBubble = true;
			temp = datacell.cur_cell;
			movetoupcell(temp);
			break;
		}
		case 9:
		{
			event.cancelBubble = true;
			if( event.shiftKey )	//按'shift+tab'键，回退一格
			{
				temp = datacell.cur_cell;
				movetoprecell(temp);
				event.cancelBubble = true;
				return false;
			}else					//按'tab'键，前进一格
			{
				temp = datacell.cur_cell;
				movetonextcell(temp);
				event.cancelBubble = true;
				return false;
			}
			break;
		}
		case 13:					//按'回车'键，前进一格
		{
			temp = datacell.cur_cell;
			//setTimeout("movetonextcell(temp)", 0);
			movetonextcell(temp);
			event.cancelBubble = true;
			break;
		}
	}
}catch( e )
{
	alert( " catch Exception in method : _editorC_onkeydown " );
}
	
}

/*
 *当用户点击单元格时，弹出编辑框，让用户对单元格的内容作修改
**/
function _datacell_onclick()
{
try{
	event.cancelBubble = true;
	//找出当前选中的单元格
	var the_obj = event.srcElement;
	
	if( the_obj.tagName.toLowerCase() == "table"|| the_obj.tagName.toLowerCase() == "tr" ) return;

	//如果点击列名，按此列排序
	if( the_obj.tagName.toLowerCase() == "th"||the_obj.parentNode.tagName.toLowerCase()=="th" )
	{   
     if(the_obj.tagName.toLowerCase()=="img") the_obj=the_obj.parentNode;
	  //如果noCount是true模式，则为服务器排序；
	  //如果单页，客户端排序;如果多页，服务器端排序
	  //如是当前前无记录，或当前editor为chechbox，则不排序
	  if(the_obj.tagName.toLowerCase()=="input") the_obj=the_obj.parentNode;
	  if(this.rows.length<=1) return;
	     else
		if( (typeof(this.rows[1].cells[the_obj.cellIndex].editor)=="string") && (this.rows[1].cells[the_obj.cellIndex].editor.indexOf( "checkBoxEditor" ) == 0) )
		{
		return;	
		}	 
		
	  if(this.noCount!="true")
		{
	  if(this.getPageCount()<=1)
		this.sort( the_obj );
		else
	    this.serverSort(the_obj);
		}
		else
        this.serverSort(the_obj);
		
		//切换升降序的图标；
		this.processSortIcon(the_obj);
		return;
	}
	
	var the_cell = _get_Element( the_obj, "td", this );
	if( the_cell == null ) return;	
	var the_row = the_cell.parentNode;
	
	//如果选中某单元格，先处理原选中单元格，再把选中单元格置为编辑状态
	if( this.cur_cell == the_cell )	//如果为同一单元格，不用处理
		return;
	if( this.cur_cell != null &&  this.cur_cell != the_row )
	{
		if( !processCellBlur( this.cur_cell ) )
			return;
	}
	this.cur_cell = the_cell;
	processCellActive( the_cell );
	_datacell_selectRow( the_cell.parentNode );
}catch( e )
{
	alert( " catch Exception in method : _datacell_onclick " );
}
}
//切换升降序图标
function _datacell_processSortIcon(obj)
{
	
	_datacell_cleanSortIcon(obj);
　　
	if(!obj.icon)
	{
	//第一次点击，新建图标；
    var icon = document.createElement("image");
	icon.style.display 	= "";
	icon.style.width="9px";
	icon.style.height ="8px";
	icon.src= PCRoot + "/images/datacell_asc.gif";
	obj.appendChild( icon );	
	obj.icon=icon;
     }
	 else
	{
		 if (obj.sortMode!=true)
		 {
			 obj.icon.src=PCRoot+"/images/datacell_asc.gif";
			 obj.icon.style.display="";
		 }
		 else
		{
			 obj.icon.src=PCRoot+"/images/datacell_desc.gif";
			 obj.icon.style.display="";
		 }

	 }

}
//隐藏所有升降序图标
function _datacell_cleanSortIcon(obj)
{
   var row=obj.parentNode;
   for(i=0;i<row.cells.length;i++)
	{ var col=row.cells[i];
      if(col.icon)
		{col.icon.style.display="none"}

     }

}


//当cell失去焦点，先对数据进行格式校验，若校验通过，则修改dataset中对应entity的值
function processCellBlur( cell )
{
//try{
	if( !cell ) return true;
	
	var row = cell.parentNode;

	var entity = row.entity;
	var datacell = row.table;
	if( datacell.readOnly )
		return true;
	
	if(cell.modify=="false") 
		 if(entity.status!=Entity.STATUS_NEW) return true ;
	
	//从编辑框中取得修改过的值

	var the_editor = datacell.getEditor( cell );
	if( the_editor )
	{
		if( (typeof(the_editor)=="string") && (the_editor.indexOf( "checkBoxEditor" ) == 0) )
		{
			the_editor = cell.checkBox;
		}
		var newValue = the_editor.getValue();
		
		var valuechanged = ( cell.value != newValue );
		if( valuechanged )
		{
			//校验输入数据是否符合指定格式
			var formatOK = VerifyValueFormat( cell, newValue );
			if( !formatOK )
			{
				event.cancelBubble = true;
				event.returnValue = false;
				the_editor.shown();
				return false;
			}
			//把修改过的值填回到dataset
			var dataset = datacell.dataset;
			var fieldname = cell.getAttribute("name");
			dataset.setValue( entity, fieldname, newValue );
					
			_cell_refresh( cell, entity );
		}
		the_editor.hidden();
	}
	cell.editor = null;
	//cell.style.border = "";
	datacell.cur_cell = null;
	
	return true;
//}catch( e )
//{
//	alert( "Catch Exception in processCellBlur " );
//}
}

function processCellActive( cell )
{
try{
	function bindingEditor( editor, cell )
	{
		the_editor.outerHTML;
		var top = cell.offsetTop;
		var left = cell.offsetLeft;
		//取得editor框的定位
		var target = cell;		
		while (target = target.offsetParent)
		{
			if(target.tagName=="BODY") continue;
			top += target.offsetTop-target.scrollTop; 
			left += target.offsetLeft-target.scrollLeft;
		}
		
		//????编辑框的位置和大小如何确定
		editor.setPosition( top, left, cell.clientWidth, cell.clientHeight);

   
	
		editor.setInitValue( cell.value );
		//editor.style.fontSize = cell.style.fontSize;
		//alert( cell.style.fontSize );
		
		cell.editor = editor;
	}
	if( !cell ) return;
	
	var row = cell.parentNode;
	var entity = row.entity;
	var datacell = row.table;
	
	if( datacell.readOnly )
		return;
	
	//显示编辑框，来修改值
	//datacell.setEditor( cell.getAttribute( "name" ), new TextEditor( datacell ) );
 
  

	if(cell.modify!="false")

	{
	  		  
	var   the_editor = datacell.getEditor( cell );	

	if( the_editor )
	{
		if( typeof( the_editor ) == "string" )	//是checkBox编辑框
		{
			cell.checkBox.focus();
		}else
		{
			bindingEditor( the_editor, cell );	
			the_editor.shown();
		}
	}
	}
	else
	{ 
		if(entity.status==Entity.STATUS_NEW)
		{
		var   the_editor = datacell.getEditor( cell );	

	if( the_editor )
	{
		if( typeof( the_editor ) == "string" )	//是checkBox编辑框
		{
			cell.checkBox.focus();
		}else
		{
			bindingEditor( the_editor, cell );	
			the_editor.shown();
		}
	}
		}
	}

	//cell.style.border = "1 solid pink";
	datacell.cur_cell = cell;
}catch( e )
{
	alert( " catch Exception in method : processCellActive " );
}
}


function isCellEditorable( cell )
{
	var row = cell.parentNode;
	var datacell = row.table;
	
	var the_editor = datacell.getEditor( cell );
	if( the_editor )
	{
		return true;
	}
	
	return false;
}

function getNextEditorableCell( cur_cell )
{	
	var cur_row = cur_cell.parentNode;
	var next_row = cur_row.nextSibling;
	var next_cell = cur_cell.nextSibling;
	
	if(	!next_cell )	//如果当前单元格为本行最后一格
	{
		if( next_row )	//如果当前单元格不为最后一行的最后一格，转到下一行第一格
		{
			next_cell = next_row.cells[0];
		}else			//如果当前单元格为最后一行的最后一格，不移动
		{
			return null;
		}
	}
	if( isCellEditorable( next_cell ) )
		return next_cell;
	else
		return getNextEditorableCell( next_cell );		
}

function movetonextcell( cur_cell )
{
try{
	if( !cur_cell ) return;
	
	
	var next_cell = getNextEditorableCell( cur_cell );
	if( !next_cell )
		return;
			
	if( !processCellBlur( cur_cell ) )
		return;
		
	var cur_row = cur_cell.parentNode;
	var next_row = next_cell.parentNode;
	//清除源选中行的颜色，设置新选中行的颜色
	if( next_row != cur_row )
		_datacell_selectRow( next_row );
	
	processCellActive(next_cell);
	
}catch( e )
{
	alert( " catch Exception in method : movetonextcell " );
}

}

function movetoprecell( cur_cell )
{
try{
	if( !cur_cell ) return;
	
	function getPreEditorableCell( cur_cell )
	{	
		var cur_row = cur_cell.parentNode;
		var pre_row = cur_row.previousSibling;
		var pre_cell = cur_cell.previousSibling;
		
		if(	!pre_cell )	//如果当前单元格为本行第一格
		{
			if( pre_row )	//如果当前单元格不为第一行的第一格，转到上一行最后一格
			{
				pre_cell = pre_row.cells[pre_row.cells.length-1];
			}else
			{
				return null;
			}
		}
		if( isCellEditorable( pre_cell ) )
			return pre_cell;
		else
			return getPreEditorableCell( pre_cell );		
	}
	
		
	var pre_cell = getPreEditorableCell( cur_cell );
	if( !pre_cell )
		return;
			
	if( !processCellBlur( cur_cell ) )
		return;
		
	var cur_row = cur_cell.parentNode;
	var pre_row = pre_cell.parentNode;
	//清除源选中行的颜色，设置新选中行的颜色
	if( pre_row != cur_row )
		_datacell_selectRow( pre_row );
		
	processCellActive( pre_cell );

}catch( e )
{
	alert( " catch Exception in method : movetoprecell " );
}
}

function movetoupcell( cur_cell )
{
try{
	if( !cur_cell ) return;
	
	var row = cur_cell.parentNode;
	var table = row.table;
	
	var up_row = row.previousSibling;
	if( !up_row ) return;		//当前在第一行
	
	var cell_index = cur_cell.cellIndex;
	var up_cell = up_row.cells[cell_index];
	
	if( !processCellBlur( cur_cell ) )
		return;
	processCellActive( up_cell );
	_datacell_selectRow( up_row );
}catch( e )
{
	alert( " catch Exception in method : movetoupcell " );
}
}


function movetodowncell( cur_cell )
{
try{
	if( !cur_cell ) return;
	
	var row = cur_cell.parentNode;
	var table = row.table;
	
	var down_row = row.nextSibling;
	if( !down_row ) return;		//当前在最后一行
	
	var cell_index = cur_cell.cellIndex;
	var down_cell = down_row.cells[cell_index];
	
	if( !processCellBlur( cur_cell ) )
		return;
	processCellActive(down_cell);
	_datacell_selectRow( down_row );
}catch( e )
{
	alert( " catch Exception in method : movetodowncell " );
}
}


function _datacell_selectNextRow()
{	
	if( !this.cur_row )//选择第一行
	{
		_datacell_selectRow( this.tBodies[0].rows[0] );
		return;
	}else if( this.cur_row.nextSibling )
	{
		_datacell_selectRow( this.cur_row.nextSibling );
		return;
	}
	_datacell_selectRow( this.cur_row );
}

function _datacell_selectPreRow()
{
	if( !this.cur_row )//选择第一行
	{
		_datacell_selectRow( this.tBodies[0].rows[0] );
		return;
	}else if( this.cur_row.previousSibling )
	{
		_datacell_selectRow( this.cur_row.previousSibling );
		return;
	}
	_datacell_selectRow( this.cur_row );
}

function _datacell_selectFirstRow()
{
	_datacell_selectRow( this.tBodies[0].rows[0] );
}

function _datacell_selectLastRow()
{
	var rows = this.tBodies[0].rows;
	_datacell_selectRow( rows[ rows.length-1 ] );
}

/*
function _createDrag()
{
	var drag = document.createElement( "DIV" );
	drag.style.position		= "absolute";
	drag.style.display		= "none";
	drag.style.zIndex		= "999";
	
	document.body.insertBefore( drag );
}*/

function _datacell_addOnrefreshListener( fieldName, fName, paramList )
{
	if( !this.listeners )
		this.listeners = new Object();
		
	var todo		= new Object();
	todo.fName		= fName;
	todo.paramList	= paramList;
	eval( "this.listeners." + entity_parse_invalid(fieldName) + " = todo;" );
}

function _datacell_fireOnrefreshEvent( cell )
{
	var fieldName = cell.getAttribute( "name" );
	if( !this.listeners ) return;
	
	var todo;
	eval( "todo = this.listeners." + entity_parse_invalid(fieldName) );

	if( !todo )	return;
	
	var fName = todo.fName;
	
	var paramList = [cell].concat( todo.paramList );
	fireUserEvent( fName, paramList );
}

//----------- 导航条 -------------------
function initPilot( pilot, datacell_id )
{
	var datacell_id = datacell_id;
	//属性
	
	pilot.firstPage_button	= eval( datacell_id + "_firstPage" );
	pilot.prePage_button	= eval( datacell_id + "_prePage" );
//	pilot.preRow_button		= eval( datacell_id + "_preRow" );
//	pilot.nextRow_button	= eval( datacell_id + "_nextRow" );
    pilot.nextPage_button	= eval( datacell_id + "_nextPage" );
	pilot.lastPage_button	= eval( datacell_id + "_lastPage" );
	pilot.addrow_button		= eval( datacell_id + "_addrow" );
	pilot.deleterow_button	= eval( datacell_id + "_deleterow" );
	pilot.submit_button		= eval( datacell_id + "_submit" );
	pilot.reset_button		= eval( datacell_id + "_reset" );
	
	pilot.gotoPage          = eval(datacell_id+"_gotoPage");
	pilot.currPage          = eval(datacell_id+"_currPage");
    pilot.gotoPage.datacell =  datacell_id;
	pilot.gotoPage.className="PG_DATACELL_GOTO";
    pilot.currPage.className="PG_DATACELL_CURRPAGE";
    pilot.gotoPage.parentNode.className="PG_DATACELL_CURRPAGE";
    
	//方法	
	pilot.refreshPageButtonwithNoCount=_pilot_refreshPageButton_noCount;//刷新翻页相关button的状态
	pilot.refreshCurrPagewithNoCount=_refresh_currPage_noCount;
	pilot.refreshCurrPage      =_refresh_currPage;//刷新导行条上当前第几页
	pilot.refreshPageButton		= _pilot_refreshPageButton;	//刷新翻页相关button的状态
	pilot.refreshRowButton		= _pilot_refreshRowButton;	//刷新换行相关button的状态
	pilot.disableButton	= _pilot_disableButton;
	pilot.enableButton	= _pilot_enableButton;
	
	//事件	
	pilot.gotoPage.onkeypress=_pilot_gotoPage_keypress; 
	pilot.firstPage_button.onmouseover	= _pilot_button_onmouseover;
	pilot.firstPage_button.onmouseout	= _pilot_button_onmouseout;	
	pilot.firstPage_button.onclick		= function(){ eval(datacell_id+".firstPage()"); };
	pilot.prePage_button.onmouseover	= _pilot_button_onmouseover;
	pilot.prePage_button.onmouseout		= _pilot_button_onmouseout;	
	pilot.prePage_button.onclick		= function(){ eval(datacell_id+".prePage()"); };
	//pilot.preRow_button.onmouseover		= _pilot_button_onmouseover;
	//pilot.preRow_button.onmouseout		= _pilot_button_onmouseout;	
	//pilot.preRow_button.onclick			= function(){ eval(datacell_id+".selectPreRow()"); };
	//pilot.nextRow_button.onmouseover	= _pilot_button_onmouseover;
	//pilot.nextRow_button.onmouseout		= _pilot_button_onmouseout;	
	//pilot.nextRow_button.onclick		= function(){ eval(datacell_id+".selectNextRow()"); };
	pilot.nextPage_button.onmouseover	= _pilot_button_onmouseover;
	pilot.nextPage_button.onmouseout	= _pilot_button_onmouseout;	
	pilot.nextPage_button.onclick		= function(){ eval(datacell_id+".nextPage()"); };
	pilot.lastPage_button.onmouseover	= _pilot_button_onmouseover;
	pilot.lastPage_button.onmouseout	= _pilot_button_onmouseout;	
	pilot.lastPage_button.onclick		= function(){ eval(datacell_id+".lastPage()"); };
	pilot.addrow_button.onmouseover		= _pilot_button_onmouseover;
	pilot.addrow_button.onmouseout		= _pilot_button_onmouseout;	
	pilot.addrow_button.onclick			= function(){ eval(datacell_id+".addrow()"); };
	pilot.deleterow_button.onmouseover	= _pilot_button_onmouseover;
	pilot.deleterow_button.onmouseout	= _pilot_button_onmouseout;	
	pilot.deleterow_button.onclick		= function(){ eval(datacell_id+".deleterow()"); };
	pilot.submit_button.onmouseover		= _pilot_button_onmouseover;
	pilot.submit_button.onmouseout		= _pilot_button_onmouseout;
	pilot.submit_button.onclick			= function(){ eval(datacell_id+".submit()"); };
	pilot.reset_button.onmouseover		= _pilot_button_onmouseover;
	pilot.reset_button.onmouseout		= _pilot_button_onmouseout;
	pilot.reset_button.onclick			= function(){ eval(datacell_id+".reset()"); };

	return pilot;
}

/* 根据显示的页面确定翻页按钮状态
 * 参数
 * pageIndex：当前页
 * pageCount：纪录总页数
 **/
function _pilot_refreshPageButton( pageIndex, pageCount )
{
	
	//如果只有一页
	if( pageCount <= 1 )
	{
		with( this )
		{
			disableButton( firstPage_button );
			disableButton( prePage_button );
			disableButton( nextPage_button );
			disableButton( lastPage_button );
		}
	}else if( pageIndex <=0 )	//第一页
	{
		with( this )
		{
			disableButton( firstPage_button );
			disableButton( prePage_button );
			enableButton( nextPage_button );
			enableButton( lastPage_button );
		}
		return;
	}else if( pageIndex == (pageCount-1) )//最后一页
	{
		with( this )
		{
			enableButton( firstPage_button );
			enableButton( prePage_button );
			disableButton( nextPage_button );
			disableButton( lastPage_button );
		}
		return;
	}else//中间页
	{
		with( this )
		{
			enableButton( firstPage_button );
			enableButton( prePage_button );
			enableButton( nextPage_button );
			enableButton( lastPage_button );
		}
		return;
	}
}

/* 根据选中行确定换行按钮状态
 * 参数
 * rowIndex：当前行
 * rowCount：页面总行数
 **/
function _pilot_refreshRowButton( rowIndex, rowCount )
{
	//如果只有一行
	if( rowCount <= 1 )
	{
		with( this )
		{
			//disableButton( preRow_button );
			//disableButton( nextRow_button );
		}
	}else if( rowIndex <=1 )	//第一行
	{
		with( this )
		{
			//disableButton( preRow_button );
			//enableButton( nextRow_button );
		}
		return;
	}else if( rowIndex == (rowCount-1) )//最后一行
	{
		with( this )
		{
			//enableButton( preRow_button );
			//disableButton( nextRow_button );
		}
		return;
	}else//中间行
	{
		with( this )
		{
			//enableButton( preRow_button );
			//enableButton( nextRow_button );
		}
		return;
	}
}

function _pilot_disableButton( button )
{
	button_switchImg( button, 2 );
	//button_switchImg( button, 0 );
	button.disabled = true;
}

function _pilot_enableButton( button )
{
	button_switchImg( button, 0 );
	button.disabled = false;
}

//当鼠标移上按钮时，切换图片
function _pilot_button_onmouseover()
{
	event.cancelBubble = true;
	var button = event.srcElement;
	button_switchImg( button, 1 );
}

//当鼠标移出按钮时，切换图片
function _pilot_button_onmouseout()
{
	event.cancelBubble = true;
	var button = event.srcElement;
	button_switchImg( button, 0 );
}

/*
 * 参数
 * button：切换图片的按钮<img>
 * state：按钮状态，0-正常；1-选中；2-disable
 **/
function button_switchImg( button, state )
{
	var img = button;
	var name = img.name;
	switch( name )
	{
		case "firstPage" : 
		{
			switch( state )
			{
				case 0 : 
				{
					img.src = PCRoot + "/images/datacell_firstRow.gif";
					break;
				}
				case 1 : 
				{
					img.src = PCRoot + "/images/datacell_firstRow2.gif";
					break;
				}
				case 2 :
				{
					img.src = PCRoot + "/images/datacell_firstRow3.gif";
					break;
				}
			}
			break;
		}
		case "prePage" : 
		{
			switch( state )
			{
				case 0 :
				{
					img.src = PCRoot + "/images/datacell_preRow.gif";
					break;
				}
				case 1 :
				{
					img.src = PCRoot + "/images/datacell_preRow2.gif";
					break;
				}
				case 2 :
				{
					img.src = PCRoot + "/images/datacell_preRow3.gif";
					break;
				}
			}			
			break;
		}
		case "preRow" : 
		{
			switch( state )
			{
				case 0 :
				{
					img.src = PCRoot + "/images/datacell_preRow.gif";
					break;
				}
				case 1 :
				{
					img.src = PCRoot + "/images/datacell_preRow2.gif";
					break;
				}
				case 2 :
				{
					img.src = PCRoot + "/images/datacell_preRow3.gif";
					break;
				}
			}			
			break;
		}
		case "nextRow" : 
		{
			switch( state )
			{
				case 0 :
				{
					img.src = PCRoot + "/images/datacell_nextRow.gif";
					break;
				}
				case 1 :
				{
					img.src = PCRoot + "/images/datacell_nextRow2.gif";
					break;
				}
				case 2 :
				{
					img.src = PCRoot + "/images/datacell_nextRow3.gif";
					break;
				}
			}
			break;
		}
		case "nextPage" : 
		{
			switch( state )
			{
				case 0 :
				{
					img.src = PCRoot + "/images/datacell_nextRow.gif";
					break;
				}
				case 1 :
				{
					img.src = PCRoot + "/images/datacell_nextRow2.gif";
					break;
				}
				case 2 :
				{
					img.src = PCRoot + "/images/datacell_nextRow3.gif";
					break;
				}
			}
			break;
		}
		case "lastPage" : 
		{
			switch( state )
			{
				case 0 :
				{
					img.src = PCRoot + "/images/datacell_lastRow.gif";
					break;
				}
				case 1 :
				{
					img.src = PCRoot + "/images/datacell_lastRow2.gif";
					break;
				}
				case 2 :
				{
					img.src = PCRoot + "/images/datacell_lastRow3.gif";
					break;
				}
			}
			break;
		}
		case "addrow" : 
		{
			switch( state )
			{
				case 0 :
				{
					img.src = PCRoot + "/images/datacell_addRow.gif";
					break;
				}
				case 1 :
				{
					img.src = PCRoot + "/images/datacell_addRow2.gif";
					break;
				}
				case 2 :
				{
					img.src = PCRoot + "/images/datacell_addRow3.gif";
					break;
				}
			}
			break;
		}
		case "deleterow" : 
		{
			switch( state )
			{
				case 0 :
				{
					img.src = PCRoot + "/images/datacell_deleteRow.gif";
					break;
				}
				case 1 :
				{
					img.src = PCRoot + "/images/datacell_deleteRow2.gif";
					break;
				}
				case 2 :
				{
					img.src = PCRoot + "/images/datacell_deleteRow3.gif";
					break;
				}
			}
			break;
		}
		case "submit" : 
		{
			switch( state )
			{
				case 0 :
				{
					img.src = PCRoot + "/images/datacell_confirm.gif";
					break;
				}
				case 1 :
				{
					img.src = PCRoot + "/images/datacell_confirm2.gif";
					break;
				}
				case 2 :
				{
					img.src = PCRoot + "/images/datacell_confirm3.gif";
					break;
				}
			}
			break;
		}
		case "reset" : 
		{
			switch( state )
			{
				case 0 :
				{
					img.src = PCRoot + "/images/datacell_reset.gif";
					break;
				}
				case 1 :
				{
					img.src = PCRoot + "/images/datacell_reset2.gif";
					break;
				}
				case 2 :
				{
					img.src = PCRoot + "/images/datacell_reset3.gif";
					break;
				}
			}
			break;
		}
	}//end switch name
}


//---------------------------------------
//----- DataGrid -------------------
function DataGrid( table, dataset )
{
	var datagrid = table;
	
	//属性
	datagrid._tbody			= null;
	datagrid._styleRow		= null;
	datagrid.cur_row		= null;
	datagrid.dataset		= dataset;
	
	//方法
	datagrid.refresh		= _datagrid_refresh;
	datagrid.selectFirstRow	= _datagrid_selectFirstRow;
	datagrid.selectLastRow	= _datagrid_selectLastRow;
	datagrid.selectNextRow	= _datagrid_selectNextRow;
	datagrid.selectPreRoe	= _datagrid_selectPreRow;
	
	//事件
	datagrid.onmouseover	= _datagrid_onmouseover;
	
	//查找tbody的第一行，作为添加数据行的样式行
	if( datagrid.tBodies && datagrid.tBodies.length > 0 )
	{
		datagrid._tbody = datagrid.tBodies[0];
	}else
	{
		datagrid._tbody = datagrid.appendChild( "TBODY" );
	}
	if( datagrid._tbody.rows && datagrid._tbody.rows.length > 0 )
	{
		datagrid._styleRow = datagrid._tbody.rows[0].cloneNode( true );
		datagrid._tbody.deleteRow(0);
	}else
	{
		datagrid._styleRow = document.createElement( "TR" );
	}
	//row的属性
	datagrid._styleRow.entity = null;
	datagrid._styleRow.table = table;
	
	return datagrid;
}


function _datagrid_refresh( dataset )
{
try{
	var datagrid = this;
	var _dataset = dataset ? dataset : this.dataset;	
	var tbody = this._tbody;
	
	//清除所有行
	for( var j = tbody.rows.length -1; j >= 0; j-- )
	{
		tbody.deleteRow( j );
	}
	//显示数据
	var entity = null;
	var _row = null;
	var cell = null;
	var fieldName;
	for( var i = 0; i < _dataset.entities.length; i++ )
	{
		entity = _dataset.entities[i];
		_row = this._styleRow.cloneNode( true );
		_row.entity = entity;
		tbody.appendChild( _row );
		for( var j = 0; j < _row.cells.length; j++ )
		{
			cell = _row.cells[j];
			fieldName = cell.getAttribute("name");
			cell.value = entity.getProperty( fieldName );
			cell.innerText = cell.value;
		}
	}
	
}catch( e )
{
	alert( " catch Exception in method : _datagrid_refresh " );
}
}

function _datagrid_selectNextRow()
{	
	if( !this.cur_row )//选择第一行
	{
		_datagrid_selectRow( this.tBodies[0].rows[0] );
		return;
	}else if( this.cur_row.nextSibling )
	{
		_datagrid_selectRow( this.cur_row.nextSibling );
		return;
	}
	_datagrid_selectRow( this.cur_row );
}

function _datagrid_selectPreRow()
{
	if( !this.cur_row )//选择第一行
	{
		_datagrid_selectRow( this.tBodies[0].rows[0] );
		return;
	}else if( this.cur_row.previousSibling )
	{
		_datagrid_selectRow( this.cur_row.previousSibling );
		return;
	}
	_datagrid_selectRow( this.cur_row );
}

function _datagrid_selectFirstRow()
{
	_datagrid_selectRow( this.tBodies[0].rows[0] );
}

function _datagrid_selectLastRow()
{
	var rows = this.tBodies[0].rows;
	_datagrid_selectRow( rows[ rows.length-1 ] );
}

function _datagrid_onmouseover()
{
	var _obj = event.srcElement;
	
	var _cell = _get_Element( _obj, "td", this );
	if( !_cell ) return;
	var _row = _cell.parentNode;
	
	if( _row )
		_datagrid_selectRow( _row );
}

function _datagrid_unselectRow( the_row )
{
try{
	if( !the_row ) return;
	var table = the_row.table;
	
	//清除行的颜色
	the_row.className = "";
	table.cur_row = null;
}catch( e )
{
	alert( " catch Exception in method : _datacell_unselectRow " );
}
}

function _datagrid_selectRow( the_row )
{
try{
	if( !the_row ) return;
	var table = the_row.table;
	
	if( (table.cur_row != null) && (the_row != table.cur_row) )
	{
		_datagrid_unselectRow( table.cur_row );
	}
	//设置行的颜色
	the_row.className = "RC_DATAGIRD_ACTIVEROW";
	table.cur_row = the_row;
	
}catch( e )
{
	alert( " catch Exception in method : _datagrid_selectRow " );
}
}


//---------- Utils ------------
function _get_Element( the_ele, the_tag, owner){
	the_tag = the_tag.toLowerCase();
	if(the_ele.tagName.toLowerCase()==the_tag)return the_ele;
	while(the_ele=the_ele.offsetParent || the_ele.id == this.id ){
		if(the_ele.tagName.toLowerCase()==the_tag)return the_ele;
		if( the_ele == owner ) return null;
	}
	return(null);
}

/*
对单元格的数据进行校验
使用了陈春提供的dataformcheck.js中的checkInput、f_alert函数进行校验
*/
function VerifyValueFormat( cell, nValue )
{
try{
	var _cell = cell.cloneNode(true);
	_cell.value = nValue;
	/* 非自定义属性的元素不校验 */
	if (_cell.eos_displayname + "" == "undefined") return true;
	
	try{
		/* 非空校验 */
		if (_cell.eos_isnull=="false" && isnull(_cell.value))
		{
			f_alert(_cell,"不能为空");
			return false;
		}
		/* 数据类型校验 */
		if (checkInput(_cell)==false)
		{
			//_cell.value = oValue;
			return false;
		}
	}catch( e )
	{
		return false
	}
	return true;
}catch( e )
{
	alert( " catch Exception in method : VerifyValueFormat " );
}
	
}
//设置当前页号
function _refresh_currPage(pageIndex, pageCount)
{
  this.currPage.innerHTML="当前"+(pageIndex+1)+"/"+pageCount+"页"

}
//设置当前页号
function _refresh_currPage_noCount(pageIndex)
{
  this.currPage.innerHTML="当前第"+(pageIndex+1)+"页"

}

//
function _pilot_gotoPage_keypress()
{
　event.cancelBubble = true;
 if((event.keyCode>=48&&event.keyCode<=57)||event.keyCode==13)
   	{
　　　　//是否是回车
     if(event.keyCode==13)
		 {　
        //回车处理
		

		 var datacell=eval(this.datacell);//得到datacell对象
　if(isData_Modified(datacell))
	 {
     if(!confirm("数据已经修改，但并末提交，是否要继续？"))
		 {
         return;
	     }
	 }　

　　　　//是否是noCount模式
		 if(datacell.noCount!="true")
			 {
		     if(this.value==0||this.value>datacell.getPageCount())
				 {
			　　	  alert("输入页数超出范围！");
		　　	 　　 return;
		　　　	  }
		
　

　　　　　　　//用户输入正确，载入数据　
			　   if( !processCellBlur( datacell.cur_cell ) )
		        　　 return;
	          　  _datacell_unselectRow( datacell.cur_row );	

                 datacell.locked();
                 datacell.dataset.gotoPage(this.value);
                 datacell.refresh();
				 datacell.unlocked();
			 }
			else
		 {
  　　　　　　 if( !processCellBlur( datacell.cur_cell ) )
		        　　 return;
	          　  _datacell_unselectRow( datacell.cur_row );	
			     datacell.locked();
                 datacell.dataset.gotoPage(this.value);
				 datacell.refresh();
				 datacell.unlocked();
		 }
			
	 }
	}
	 else
		 
	{
		alert("请输入数字！")
	}
 }

 /* 根据显示的页面确定翻页按钮状态--在noCount为true的模式下
 * 参数
 * pageIndex：当前页
 * pageRecord: 每页记录数
 * currPageRecord:当前页总记录数
 **/
function _pilot_refreshPageButton_noCount( pageIndex ,pageRecord,currPageRecord)
{
		
	
 if( pageIndex <=0&&pageRecord==currPageRecord )	//第一页；并且当前页记录数等于每页记录数
	{
		with( this )
		{
			disableButton( firstPage_button );
			disableButton( prePage_button );
			enableButton( nextPage_button );
			disableButton( lastPage_button );
		}
		return;
	}
	else
	{
		if(pageRecord==currPageRecord)　　　　　　　　//当前页记录数等于每页记录数
		{with( this )
		{
			enableButton( firstPage_button );
			enableButton( prePage_button );
			enableButton( nextPage_button );
			disableButton( lastPage_button );
		}
		return;}
		else
				{with( this )
		{
			enableButton( firstPage_button );
			enableButton( prePage_button );
			disableButton( nextPage_button );
			disableButton( lastPage_button );
		}
		return;}
	}
	


}
//检索noCount的模式
function _datacell_getNoCount()
{
	return this.dataset.noCount;
}
//检索数据是否有修改
/*
* 参数：
*　　obj:datacell对象
*/
function isData_Modified(obj)
{
 
 
	var isChanged=false;

	if( !processCellBlur( obj.cur_cell ) )
		return;
	_datacell_unselectRow( obj.cur_row );
	
	
	var row, status, fieldName;
	for( var i = 0; i < obj.tBodies[0].rows.length; i++ )
	{
		row = obj.tBodies[0].rows[i];
		status = row.entity.status;

	
		if( (status == Entity.STATUS_NEW) || (status == Entity.STATUS_MODIFIED) )
		{   
			isChanged=true;
		    break;	
		}
	}
	
　　if(obj.dataset.removedEntities.length!=0)
	{
	   	    isChanged=true;
	}

	return isChanged;
}

function _datacell_locked()
{
      
	   if(document.getElementById("datacell_lock")==null)
	 {
	
		//建立pilot的锁定层
		var div = document.createElement("div");
		div.id="pilot_lock";
		var table=document.createElement("table");
        table.id="pilot_lock_table";
		var td=table.insertRow(0);
		td.insertCell();
		div.appendChild(table);
        document.body.appendChild(div)  ;


	//建立datacell的锁定层
		var div = document.createElement("div");
		div.id="datacell_lock";
		var table=document.createElement("table");
        table.id="datacell_lock_table";
		var td=table.insertRow(0);
		td.insertCell();
		div.appendChild(table);
        document.body.appendChild(div)  ;
		
		pilot_lock_table.style.cursor="wait";
		pilot_lock.style.position="absolute";
		pilot_lock.style.zIndex="1";
		
		
		datacell_lock.style.position="absolute";
		datacell_lock.style.zIndex="1";
		datacell_lock.style.cursor="wait"
  	 }

		//document.write("<div id='pilot_lock'><table id='pilot_lock_table' ><td></td></table></div>")
		
		//锁定
		
		var target = this.pilot;
	
		var top = target.offsetTop;
		var left = target.offsetLeft;
		while (target = target.offsetParent)
		{
			top += target.offsetTop; 
			left += target.offsetLeft;
		}
	    pilot_lock.style.cursor="wait";
		pilot_lock.style.color="red";
		pilot_lock.style.top=top;
		pilot_lock.style.left=left;
		pilot_lock_table.style.width=this.pilot.offsetWidth;
		pilot_lock_table.style.height=this.pilot.offsetHeight;
		pilot_lock.style.display="";
		
		
		//锁定datacell
		var target = this;	
		var top = target.offsetTop;
		var left = target.offsetLeft;
		while (target = target.offsetParent)
		{
			top += target.offsetTop; 
			left += target.offsetLeft;
		}
		
        datacell_lock_table.style.cursor="wait";
	    datacell_lock.style.top=top;
		datacell_lock.style.left=left;
		datacell_lock_table.style.width=this.offsetWidth;
		datacell_lock_table.style.height=this.offsetHeight;
		datacell_lock.style.display=""
		datacell_lock.click();
        

}

function _datacell_unlocked()
{
 pilot_lock.style.display="none";
 datacell_lock.style.display="none";
}

//在表头上增加checkbox，控制当前列所有的checkbox;
	function _datacell_addcheckbox(table,cell)
	{

	var cellindex=cell.cellIndex;
	var thcell=table.rows[0].cells[cellindex];

	var checkbox;
    if(cell.modify!="false")
	 checkbox=document.createElement("<input type='checkbox' >");
	
	else
     checkbox=document.createElement("<input type='checkbox' disabled='true' >");
	

	checkbox.table=table;
	checkbox.cellindex=cellindex;
	checkbox.onclick=function ()
	{
	var datacell=this.table;
	if( !processCellBlur( datacell.cur_cell ) )
	return;


	for(var i=1;i<datacell.rows.length;i++)
	{
	var cell=datacell.rows[i].cells[this.cellindex];

	var the_editor = datacell.getEditor( cell );
	if( the_editor )
	{
	if( (typeof(the_editor)=="string") && (the_editor.indexOf( "checkBoxEditor" ) == 0) )
	{
	the_editor = cell.checkBox;
	}

	the_editor.checked=this.checked;


	var newValue = the_editor.getValue();

	var valuechanged = ( cell.value != newValue );

	if( valuechanged )
	{
	//校验输入数据是否符合指定格式
	var formatOK = VerifyValueFormat( cell, newValue );
	if( !formatOK )
	{
	event.cancelBubble = true;
	event.returnValue = false;
	the_editor.shown();
	return false;
	}
	//把修改过的值填回到dataset
	var dataset = datacell.dataset;
	var fieldname = cell.getAttribute("name");
	var entity =datacell.rows[i].entity;
	dataset.setValue( entity, fieldname, newValue );
	_cell_refresh( cell, entity );
	}
	}
	}
	}

	thcell.appendChild(checkbox);

	}



var PCRoot = "/pageComponent/resources";