/*
 * 编辑框的属性和要实现的接口
 
 function Editor( holder )
 {
	var editor = document.createElement("DIV");
	
	//属性
	editor.holder		= holder;
	editor.text			= null;
	
	//方法
	editor.getValue			= function() {};
	editor.setPosition		= function() {};
	editor.setInitValue		= function() {};
	editor.shown			= function() {};
	editor.hidden			= function() {};
	
	//事件
	//把截获的键盘事件传递给编辑器的所有者处理
	editor.onkeydown		= function(){
								this.holder.onkeydown();
							}
	
	return editor;
 }
**/

//静态工厂类
EOSEditor = new Editor();
function Editor()
{	
	var	EDITOR	= new Object;
	
	//属性
	EDITOR.textEditor		= null;
	EDITOR.selectEditor		= null;
	
	EDITOR.createTextEditor = function( holder ) {
								if( !this.textEditor )
									this.textEditor = new TextEditor( holder );
								return this.textEditor;								
							};
	return EDITOR;
}

//Editor的公用方法
function _editor_shown()
{
	this.style.display	= "";
	this.firstChild.focus();
	this.firstChild.select();
}

function _editor_hidden()
{
	this.style.display	= "none";	
}

function _editor_setInitValue( value )
{
	this.text.value = value;
	
}

//屏蔽onclick事件
function _editor_onclick()
{
	event.cancelBubble = true;
}


//-------------------
/*
 * 文本编辑框
**/
function TextEditor( holder )
{	
	var editor = document.createElement("DIV");
	//editor风格
	//editor.className = "RC_DATACELL_TEXTEDITOR";
	/*editor.style.textAlign 	= "center";
	editor.style.cursor 	= "hand";
	editor.style.border 	= "1 solid black";*/
	editor.style.position 	= "absolute";
	editor.style.display 	= "none";
	editor.style.zIndex 	= "999";
	
	
	var text = document.createElement("<input type=text>");		
	editor.appendChild( text );	
	
	//方法
	editor.getValue			= function() { return this.text.value; };
	editor.getShowValue		= function() { return this.text.value; };
	
	editor.setInitValue		= _editor_setInitValue;
	editor.shown			= _editor_shown;
	editor.hidden			= _editor_hidden;
	editor.setPosition		= _texteditor_setPosition;
	
	//事件
	editor.onclick			= _editor_onclick;
		
	editor.onselectstart	= function(){ event.cancelBubble = true; return true;};
	
	//属性
	editor.type			= "TextEditor";
	editor.value		= null;
	editor.holder		= holder;
	editor.text			= text;
	return editor;
}


function _texteditor_setPosition(top, left, width, height)
{
	this.style.posTop = top;
	this.style.posLeft = left;
	this.text.width	= width;
	this.text.height= height;
}


//-----------------------------------------
/*
 * CheckBox编辑框
*/
function CheckBoxEditor( cell, uncheckedValue, checkedValue )
{
	var checkBox = document.createElement( "input" );
	checkBox.type = "checkbox";
	
		
	//属性
	checkBox.cell	= cell;
	checkBox.checkedValue	= checkedValue;
	checkBox.uncheckedValue	= uncheckedValue;
	
	//方法
	checkBox.refresh	= _checkBoxEditor_refresh;
	//editor接口
	checkBox.getValue	= _checkBoxEditor_getValue;
	checkBox.hidden	= function() {};
	
	//事件
	
	return checkBox;
}

function _checkBoxEditor_refresh()
{
	if( this.cell.value == this.checkedValue )
		this.checked = true;
}

function _checkBoxEditor_getValue()
{
	return this.checked ? this.checkedValue : this.uncheckedValue;
}

//-----------------------------------------
/*
 * 下拉编辑框
*/
function SelectEditor( options, valueField, nameField )
{	
try{
	var editor = document.createElement("DIV");
	//editor风格
	/*editor.style.textAlign 	= "center";
	editor.style.cursor 	= "hand";
	editor.style.border 	= "1 solid black";*/
	editor.style.position 	= "absolute";
	editor.style.display 	= "none";
	editor.style.zIndex 	= "999";
	
	var text = document.createElement("<input type=text>");
	text.readOnly = true;
	editor.appendChild( text );	
	var button = document.createElement("<input type=button width='10'>");
	editor.appendChild( button );
	
	editor.appendChild( options );
	
	//initSelectText( text, button, options );
	
	//属性
	editor.type			= "SelectEditor";
	editor.valueField	= valueField;
	editor.nameField	= nameField;
	//editor.holder		= null;
	editor.text			= text;
	editor.button		= button;
	editor.options		= options;
	editor.entity		= null;
	
	//方法
	editor.shown			= _editor_shown;
	editor.setInitValue		= _selecteditor_setInitValue;
	editor.hidden			= _selecteditor_hidden;
	editor.setPosition		= _selecteditor_setPosition;
	editor.refresh			= _selecteditor_refresh;
	editor.getOptionsPosition	= function() {
									var position = new Object();
									position.top = this.text.offsetTop + this.text.offsetHeight;
									position.left = this.text.offsetLeft;
									return position;
								};
								
	editor.getValue			= function() { 
								if( this.entity )
									return this.entity.getProperty( this.valueField );
								else
									return "";
							};
	editor.getShowValue		= function() { 
								if( this.entity )
									return this.entity.getProperty( this.nameField );
								else
									return "";
							};
	//事件
	//editor.onclick			= _editor_onclick;
    editor.onmousedown=_editor_onclick;
	editor.onkeydown			= _selecteditor_onkeydown;
	editor.onselectstart	= function(){ event.cancelBubble = true; return true;};
	
	options.bind( editor );
	return editor;
}catch( e )
{
	alert( "exception : SelectEditor" );
}
}


function _selecteditor_setInitValue( value )
{
	this.entity = this.options.table.dataset.findEntity( this.valueField, value );
	this.refresh();
}

function _selecteditor_refresh( entity )
{
	if( entity )
		this.entity = entity;
	
	if( !this.entity ) return;
	
	this.text.value = this.entity.getProperty( this.nameField );
	this.text.select();
}

function _selecteditor_hidden()
{	
	//隐藏下拉框列表
	var options = this.options;
	//options.removeNode( true );
	options.hide();
	this.style.display	= "none";
	this.text.value = "";
	this.entity	= null;
}

function _selecteditor_setPosition(top, left, width, height)
{
	this.style.top = top;
	this.style.left = left;
	
	this.button.style.width = 14;
	this.button.height	= height;
	this.text.width	= width - 14;
	this.text.height= height;
}

function _selecteditor_onkeydown()
{
	var text = this.text;
	
	var options = this.options;
	
	switch( event.keyCode )
	{
		case 13:					//回车键，把列表所选的行输入text框
		{
			//下拉框没显示，不做处理
			if( options.style.display == "none" )
			{
				return;
			}
			if( options.getSelectRow() )
			{
				this.entity = options.getSelectRow().entity;
				this.refresh();				
			}
			options.hide();
			event.cancelBubble = true;
			break;
		}
		case 191:		//按'/'+Ctrl键，显示列表框
		{
			if( event.ctrlKey )
			{
				this.button.onclick();
				event.cancelBubble = true;
				return false;
			}
			break;
		}
		case 40 :	//当按"下"键时，下移一行
		{
			//如果显示列表框，列表框下移一行
			if( options.style.display != "none" )
			{
				options.table.selectNextRow();
				event.cancelBubble = true;
			}
			break;
		}
		case 38 :	//当按"上"键时，上移一行
		{
			//如果显示列表框，列表框下移一行
			if( options.style.display != "none" )
			{
				options.table.selectPreRoe();
				event.cancelBubble = true;
			}
			break;		
		}
	}
}
//------------------------------------------
//列表选择框
function Options( table, width, height,onclick )
{
	var options = document.createElement( "DIV " );
	
	var optionItem;
	var cell;
	var entity;
	
	
	options.className = "RC_OPTIONTABLE";
		/*options.style.border = "2px solid gray";
		options.style.background = "white";
		options.style.color = "white";
		options.style.cursor = "hand";
		options.style.fontsize = 10;
		options.style.background = "white";*/
		options.style.position = "absolute";
		options.style.display = "none";
		options.style.overflow = "scroll";
		options.style.zIndex = 999;
		
		
	options.appendChild( table );
	document.body.insertBefore( options );
	
	//属性
	options.onUserClick = onclick;
	options.table	= table;
	options.holder	= null;
	options.width	= width;
	options.height	= height;
	//方法
	options.bind	= _options_bind;
	options.show	= _options_show;
	options.hide	= _options_hide;
	options.refresh	= _options_refresh;
	options.filter	= _options_filter;
	//options.onclick	= _options_onclick;
	options.onmousedown	= _options_onclick;
	options.getSelectRow	= _options_getSelectRow;
	options.selectFirstRow	= _options_selectFirstRow;
	//事件
	
	//注册事件，当点击页面时隐藏列表选择框
	PopBlockManager.register( options, "hide" );
	
	options.refresh();
	return options;
}

function _options_selectFirstRow()
{
	this.table.selectFirstRow();
}

function _options_getSelectRow()
{
	return this.table.cur_row;
}

function _options_refresh( dataset )
{
	var table = this.table;
	
	table.refresh( dataset );
}

function _options_filter( filterField, key )
{
	if( !filterField || filterField == "" )
		return;
		
	var _dataset = this.table.dataset.filter( filterField, key );
	
	this.refresh( _dataset );
}

function _options_bind( holder )
{
	this.holder	= holder;
	var button	= holder.button;
	
	button.onclick = function() {
						event.cancelBubble = true;
						var holder	= this.parentNode;
						var options	= holder.options;
						var text	= holder.text;
						
						if( options.style.display == "" )
						{
							options.hide();
						}else
						{
							//var top = text.offsetTop + text.offsetHeight;
							//var left = text.offsetLeft;
							
							options.refresh( options.dataset );
							//options.show( top, left );
							options.show();
						}
						text.focus();
					}
}

function _options_hide()
{
	this.style.display = "none";
	this.table.cur_row = null;
}

function _options_show()
{
	var holder = this.holder;
	
	var top = holder.getOptionsPosition().top;
	var left = holder.getOptionsPosition().left;
	
	this.style.display = "";
	
	this.style.top	= top;
	this.style.left	= left;
	this.style.width	= this.width;
	this.style.height	= this.height;
}

//当选择列表项后，隐藏列表框，并返回所选entity给editor
function _options_onclick()
{
try{
	event.cancelBubble = true;
	//找出当前选中的单元格
	var the_obj = event.srcElement;
	if( the_obj.tagName.toLowerCase() == "table" ) return;
	
	//如果点击列名，按此列排序
	if( the_obj.tagName.toLowerCase() == "th" )
	{
		return;
	}
	
	var the_cell = _get_Element( the_obj, "td" );
	if( the_cell == null ) return;	
	var the_row = the_cell.parentNode;
	var holder	= this.holder;
	holder.entity = the_row.entity;
	fireUserEvent(this.onUserClick,[the_row.entity]);
	holder.refresh();
	this.hide();
}catch( e )
{
	alert( " catch Exception in method : _options_onclick " );
}
}

//---------------------------
//选择编辑框
function initSelectText( text, bt, options, nameField, filterField )
{
	var _text = document.createElement( "span" );
	text.insertAdjacentElement( "beforeBegin", _text );
	_text.appendChild( text );
	_text.appendChild( bt );
	_text.appendChild( options );
	
	//属性
	_text.text			= text;
	_text.button		= bt;
	_text.options		= options;
	_text.nameField		= nameField;
	_text.filterField	= filterField;
	_text.entity		= null;
	
	options.bind( _text );
		
	//方法
	_text.refresh	= _selecttext_refresh;
	_text.getOptionsPosition	= function() {
									var position = new Object();
									position.top = this.text.offsetTop + this.text.offsetHeight;
									position.left = this.text.offsetLeft;
									
									var target = this.text;
									while( target = target.offsetParent )
									{
										position.top += target.offsetTop;
										position.left += target.offsetLeft;
									}
									return position;
								};
								
	
	//事件
	_text.onkeyup = _selecttext_onkeyup;
	_text.onkeydown = _selecttext_onkeydown;
	
	return _text;
}

function _selecttext_refresh( entity )
{
	if( entity )
		this.entity = entity;
		
	if( !this.entity ) return;
	this.text.value = this.entity.getProperty( this.nameField );
	this.text.select();
}

function _selecttext_onkeydown()
{
	var text = this.text;
	if( event.srcElement != text ) return;
	//纪录原值
	this.preValue = text.value;
	
	var options = this.options;
	
	//alert( event.keyCode );
	switch( event.keyCode )
	{
		case 13:					//回车键，把列表所选的行输入text框
		{
			//下拉框没显示，不做处理
			if( options.style.display == "none" )
			{
				return;
			}
			if( options.getSelectRow() )
			{
				this.entity = options.getSelectRow().entity;
				fireUserEvent(options.onUserClick,[this.entity]);
				this.refresh();
				
				options.hide();
			}
			event.cancelBubble = true;
			break;
		}
		case 191:		//按'/'+Ctrl键，显示列表框
		{
			if( event.ctrlKey )
			{
				this.button.onclick();
				event.cancelBubble = true;
				return false;
			}
			break;
		}
		case 40 :	//当按"下"键时，下移一行
		{
			//如果显示列表框，列表框下移一行
			if( options.style.display != "none" )
			{
				options.table.selectNextRow();
				event.cancelBubble = true;
			}
			break;
		}
		case 38 :	//当按"上"键时，上移一行
		{
			//如果显示列表框，列表框下移一行
			if( options.style.display != "none" )
			{
				options.table.selectPreRoe();
				event.cancelBubble = true;
			}
			break;		
		}
	}
}

//当用户在text框输入时，显示下拉列表，并根据用户的输入，对下拉列表的内容进行过滤
function _selecttext_onkeyup()
{
	var text = this.text;
	var options = this.options;
	
	if( event.srcElement != text ) return;
	
	var keyCode = event.keyCode;
	//如果按下的是方向键、Tab键回车键、Shift键，不做处理
	if( keyCode == 9 || keyCode == 16 || keyCode == 37 || keyCode == 38 || keyCode == 39 || keyCode == 40 || keyCode == 13 )
		return;
	//如果text框的值没有发生变化，不做处理
	if( text.value == this.preValue )
		return;
		
	event.cancelBubble = true;
	
	if( options.style.display == "none" )
	{
		this.button.onclick();
	}
	
	var value = text.value;
	options.filter( this.filterField, value );
	
	//选择第一行
	options.selectFirstRow();
}

//------------
if (!Array.prototype.push){
    Array.prototype.push = function(elem)
    {
        this[this.length] = elem;
    }
}
//----- 弹出（浮动）块控制器，注册所有弹出（浮动）块，在特定时候隐藏这些块
var PopBlockManager = 
{	
    _registry: null,

    initialise: function(){
        if (this._registry == null){
            this._registry = [];

            //注册事件，当点击页面时隐藏所有弹出快
            EventManager.Add(document, "mousedown", this.hideAll );
        }
    },
    /**
     * 注册弹出（浮动）块
     *
     * @param  popBlock         弹出（浮动）块.
     *
     * @return null.
     */
    register: function( popBlock, functionName ){
        this.initialise();
        this._registry.push( {popBlock: popBlock, functionName: functionName} );
    },

    /**
     * Cleans up all the registered event handlers.
     */
    hideAll: function(){
		
        for (var i = 0; i < PopBlockManager._registry.length; i++){
            with (PopBlockManager._registry[i]){
                eval( "popBlock." + functionName + "()" );
            }
        }
    }
};

//-----------------------------------------
/*
 * 日历编辑框
*/
function CalendarEditor( format  )
{	
try{
	var editor = document.createElement("DIV");
	//editor风格
	//editor.className = "RC_DATACELL_TEXTEDITOR";
	/*editor.style.textAlign 	= "center";
	editor.style.cursor 	= "hand";
	editor.style.border 	= "1 solid black";*/
	editor.style.position 	= "absolute";
	editor.style.display 	= "none";
	editor.style.zIndex 	= "999";
	editor.format=format;
	editor.type = "calendarEditor"
	var text = document.createElement("<input type=text>");
	text.readOnly = true;
	editor.appendChild( text );	
	
	var button = document.createElement("<input type=button width='10'>");
	editor.appendChild( button );
	
	button.onclick = function(){ ;calendar(text,this.parentNode.format); event.cancelBubble = true; };
	button.onmousedown = function(){ event.cancelBubble = true; };
	//属性
	editor.value		= null;
	editor.holder		= null;
	editor.text			= text;
	editor.button		= button;
	
	//方法
	editor.getValue			= function() { return this.text.value; };
	editor.getShowValue		= function() { return this.text.value; };
	editor.setInitValue		= _editor_setInitValue;
	editor.shown			= _editor_shown;
	editor.hidden			= _calendareditor_hidden;
	editor.setPosition		= _selecteditor_setPosition;
	//editor.calendar = new CalendarWebControl( format, false );
	
	//事件
	editor.onclick			= _editor_onclick;
	editor.onselectstart	= function(){ event.cancelBubble = true; return true;};
	
	//editor.calendar.bind( text, button );
	
	return editor;
}catch( e )
{
	alert( "exception : CalendarEditor" );
}
}

function _calendareditor_hidden()
{	
    hiddenCalendar();
	this.style.display = "none";
}





