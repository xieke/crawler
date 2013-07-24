//此脚本依赖:
//rtreedata.js;string.js中的trim()函数

/*rtree最终生成的html代码为
<span>
<div nowrap><IMG/>用于显示展开（关闭）的图片
	<IMG/>用于显示节点图片
	<span>root</span>
<div children style="{margin-left:20}">

</div>
</div>
<span>
*/

//----- RTree ---------------
function RTree( id, rootName, rootIcon, width, height, attachParamFunction )
{	
	var treeModel = new Object();
	var treeView = new RTreeView( id, treeModel, rootName, rootIcon, width, height);
	//bind( treeView, treeModel );
	
	//方法
	treeModel.setNodeExpandAction	= _rtreemodel_setNodeExpandAction;
	treeModel.getNodeExpandAction	= _rtreemodel_getNodeExpandAction;
	treeModel.getExpandRetXpaths	= _rtreemodel_getExpandRetXpaths;
	treeModel.getExpandParam		= _rtreemodel_getExpandParam;
	treeModel.loadNodeChild			= _rtreemodel_loadNodeChild;
	treeModel.initAttachParam		= _rtreemodel_initAttachParam;
	treeModel.getTreeNodeName		= _rtreemodel_getTreeNodeName;
	treeModel.getTreeNodeIcon		= _rtreemodel_getTreeNodeIcon;
	treeModel.setEntityInfo			= _rtreemodel_setEntityInfo;
	treeModel.getEntityInfo			= _rtreemodel_getEntityInfo;
	treeModel.showNodeMenu			= _rtreemodel_showNodeMenu;
	treeModel.addMenuItem			= _rtreemodel_addMenuItem;
	treeModel.onmenuclick			= _rtreemodel_onmenuclick;
	treeModel.getMovemenu			= _rtreemodel_getMovemenu;
	treeModel.isNodeMovable			= _rtreemodel_isNodeMovable;
	treeModel.moveNode				= _rtreemodel_moveNode;				//把一个节点移动到另一节点下面
	treeModel.setMoveAction			= _rtreemodel_setMoveAction;
	treeModel.getMoveAction			= _rtreemodel_getMoveAction;		//取得结点移动时调用的业务逻辑
	treeModel.getMoveParam			= _rtreemodel_getMoveParam;			//取得结点移动时调用业务逻辑的参数
	treeModel.clearup				= _rtreemodel_clearup;
	
	//属性
	treeModel.treeView			= treeView;
	treeModel.attachParam		= "";						//附加参数
	treeModel.nodeExpandActions	= new Object();				//节点展开时加载子节点的动作
	treeModel.entityInfos		= new Object();				//节点显示规则、动作等相关信息
	treeModel.initParam			= treeModel.initAttachParam( attachParamFunction );
	treeModel.menu				= new EOSTreeMenu( treeModel );
		EOSTreeMenu.register( treeModel.menu );				//注册menu
	treeModel.move_node			= null;						//纪录被拖动节点
	treeModel.moveto_node		= null;						//纪录拖动目标节点	
	treeModel.move_menu			= new RTreeMoveMenu();		//拖动时的弹出菜单
	
	return treeView;
}

//树的展开、关闭、叶子的图标
var EOSPageComponentRoot = "/pageComponent/resources/";
RTree.COLLAPS_ICON = EOSPageComponentRoot + "images/t_plus.gif";
RTree.EXPAND_ICON = EOSPageComponentRoot + "images/t_minus.gif";
RTree.LEAF_ICON = EOSPageComponentRoot + "images/t_dot.gif";

function _rtreemodel_loadNodeChild( rtreeNode )
{
	function getEntities( xmlString )
	{
		var entities = new Array();
		try{
			var retDom = new ActiveXObject("MSXML2.DOMDocument");
			retDom.async=false;
			retDom.loadXML( trim(xmlString) );
			
			var listNode, childNodes;
			
			for( var i = 0; i < xpathArray.length; i++ )
			{
				listNode = retDom.selectSingleNode( "root/data/" + xpathArray[i] );
				childNodes = listNode.childNodes;
				for( var j = 0; j < childNodes.length; j++ )
				{
					entities.push( EntityFactory.createRTreeEntity( childNodes[j] ) );
				}
			}
		}catch( e )
		{
			//return new Array();
		}
		return entities;
	}
try{
	var action = this.getNodeExpandAction( rtreeNode );
	var param = this.getExpandParam( rtreeNode );
	var xpathArray = this.getExpandRetXpaths( rtreeNode );
	
	if( !(action && xpathArray) ) return;
	
	retXmlStr = callBizAction( action, param );
	var entities = getEntities( retXmlStr );
	
	var childNode, entity, name, icon;
	for( var i = 0; i < entities.length; i++ )
	{
		entity = entities[i];
		name = this.getTreeNodeName( entity );
		icon = this.getTreeNodeIcon( entity.name );
		childNode = new RTreeNode( name, icon, entity );
		//如果没有expendAction，节点图标变为不可展开
		if( !(this.getNodeExpandAction( childNode )) )
		{
			childNode.expandIcon.src = RTree.LEAF_ICON;
			childNode.isleaf = true;
		}
		rtreeNode.childrenContainer.appendChild( childNode );
		childNode.refresh();
	}
	
}catch( e )
{
	alert( "catch exception on _rtreemodel_loadNodeChild" );
}	
}

function _rtreemodel_setNodeExpandAction( entityName, bizAction, childEntityXpaths )
{
	var entityInfo = this.getEntityInfo( entityName );
	
	entityInfo.expandAction = bizAction;
	entityInfo.childEntityXpaths = childEntityXpaths;
}

/**
 * 取得节点展开时的动作
 */
function _rtreemodel_getNodeExpandAction( rtreeNode )
{
	var entityName = rtreeNode.entity.name;
	var entityInfo = this.getEntityInfo( entityName );
	
	return entityInfo.expandAction;
}

/**
 * 取得节点展开时，子节点数据的xpath，返回值为xpath数组
 */
function _rtreemodel_getExpandRetXpaths( rtreeNode )
{
	var entityName = rtreeNode.entity.name;
	var entityInfo = this.getEntityInfo( entityName );
	
	return  entityInfo.childEntityXpaths;
}

/**
 * 计算节点展开时的参数
 */
function _rtreemodel_getExpandParam( rtreeNode )
{
	var initParam = this.initParam;
	return "<root><data>" + initParam + rtreeNode.entity.toString() + "</data></root>";
}

//初始化附加参数
function _rtreemodel_initAttachParam( attachParamFunction )
{
	if( attachParamFunction && isUserEventDefined(attachParamFunction) )
	{
		return fireUserEvent( attachParamFunction, []);
	}else
	{
		return "";
	}
}

/**
 * 根据表达式，取得节点的显示值
 */
function _rtreemodel_getTreeNodeName( entity )
{	
try{		
	var entityName = entity.name;
	var entityInfo = this.getEntityInfo( entityName );
	
	var rule = entityInfo.expression;
	if( rule && rule != "" )
	{
		var exp = /\$\[(\w*)\]/;
		var display = rule;
		var field = "";
		for( var rs = display.match( exp ); rs; rs = display.match( exp ) )
		{
			field = rs[1];
			display = display.replace(exp, entity.getProperty(field));
		}
		return display;
	}else
	{
		return entityName;
	}
}catch( e )
{
	alert( "exception : _treenode_getDisplay" );
}
}

/**
 * 取得节点的图标
 */
function _rtreemodel_getTreeNodeIcon( entityName )
{	
	var entityInfo = this.getEntityInfo( entityName );
	
	return entityInfo.iconSrc;
}

/**
 * 得到节点的相关信息
 */
function _rtreemodel_getEntityInfo( entityName )
{
	entityName = entityName.toLowerCase();
	var entityInfos = this.entityInfos;
	
	var _entityInfo;
	eval( "_entityInfo = entityInfos." + entityName );
	if( !_entityInfo )
	{
		_entityInfo = new EntityInfo();
		eval( "entityInfos." + entityName + "=_entityInfo;" );
	}
	
	return _entityInfo;
}
/**
 * 设置节点的显示规则
 * 规则表达式exp中，遇到$[fieldname]的地方，用entity的field替代，exp中的其他字符不作修改
 */
function _rtreemodel_setEntityInfo(entityName, exp, iconSrc, onclick, onrefresh, ondblclick)
{
	entityName = entityName.toLowerCase();
	var entityInfo = this.getEntityInfo( entityName );
	entityInfo.expression	= exp;			//显示表达式
	entityInfo.iconSrc		= iconSrc;		//图标路径
	entityInfo.onrefresh	= onrefresh;	//刷新节点时调用的动作
	entityInfo.onclick		= onclick;		//onclick动作
	entityInfo.ondblclick	= ondblclick;	//ondblclick动作
}

function _rtreemodel_setMoveAction( moveEntity, toEntity, bizAction )
{
	var moveActions = this.getEntityInfo( moveEntity ).moveActions;
	var index;
	eval( "index = moveActions." + toEntity + ";" );
	
	if( !index )
	{
		index = moveActions.length;
		eval( "moveActions." + toEntity + " = index;" );
	}
	moveActions[index] = bizAction;
}

/**
 * 判断节点是否可以被拖动
 */
function _rtreemodel_isNodeMovable( move_node )
{
	var entityName = move_node.entity.name;
	
	var entityInfo = this.getEntityInfo( entityName );
	var moveActions = entityInfo.moveActions;
	
	if( moveActions.length > 0 )
		return true;
	else
		return false;
}

/**
 * 把一个节点移动到另一节点下面
 */
function _rtreemodel_moveNode()
{
	var move_node = this.move_node;
	var moveto_node = this.moveto_node;
	
	this.move_node = null;
	this.moveto_node = null;
	this.move_menu.hide();
	
	if( !move_node || !moveto_node ) return;
	
	var parent_node = move_node.getParent();
	
	if( move_node == moveto_node ) return;	
	if( moveto_node == parent_node )
	{
		alert( "无法移动节点: 目标节点和被移动节点的父节点相同。" );
		return;
	}
	if( moveto_node.isChildOf( move_node ) )
	{
		alert( "无法移动节点: 目标节点是被移动节点的子节点。" );
		return;
	}
	
	var beforemoveFunction = this.treeView.id + "_onbeforemove";
	var movable = fireUserEvent( beforemoveFunction, [move_node, moveto_node]);	
	if( isUserEventDefined(beforemoveFunction) && !movable )
		return;
	
	var moveAction = this.getMoveAction( move_node, moveto_node );
	var moveParam = this.getMoveParam( move_node, moveto_node );
	if( moveAction )
	{
		callBizAction( moveAction, moveParam );
		
		var doRoloadMovetoNode = !moveto_node.isChildOf(move_node.getParent()) && (moveto_node!=move_node.getParent());
		
		if( !move_node.getParent().isChildOf(moveto_node) )
			move_node.getParent().reloadChild();
		if( doRoloadMovetoNode )
			moveto_node.reloadChild();
	}
}

/**
 * 取得结点移动时调用的业务逻辑
 */
function _rtreemodel_getMoveAction( move_node, moveto_node )
{
	var entityName = move_node.entity.name;
	var toEntityName = moveto_node.entity.name;
	var entityInfo = this.getEntityInfo( entityName );
	var moveActions = entityInfo.moveActions;
	var index;
	eval( "index = moveActions." + toEntityName + ";" );
	if( typeof(index) != "undefined"  )
		return moveActions[index];
	else
		return null;
}

/**
 * 取得结点移动时调用业务逻辑的参数
 */
function _rtreemodel_getMoveParam( move_node, moveto_node )
{
	var from_node = move_node.getParent();
	return "<root><data>" + this.initParam + "<from>" + from_node.entity + "</from><to>" + moveto_node.entity +"</to>" + move_node.entity + "</data></root>"; 
}


/**
 * 清除model
 */
function _rtreemodel_clearup()
{
	var treeModel = this;
	//方法
	treeModel.setNodeExpandAction	= null;
	treeModel.getNodeExpandAction	= null;
	treeModel.getExpandRetXpaths	= null;
	treeModel.getExpandParam		= null;
	treeModel.loadNodeChild			= null;
	treeModel.initAttachParam		= null;
	treeModel.getTreeNodeName		= null;
	treeModel.getTreeNodeIcon		= null;
	treeModel.setEntityInfo			= null;
	treeModel.getEntityInfo			= null;
	treeModel.showNodeMenu			= null;
	treeModel.addMenuItem			= null;
	treeModel.onmenuclick			= null;
	treeModel.getMovemenu			= null;
	treeModel.isNodeMovable			= null;
	treeModel.moveNode				= null;
	treeModel.setMoveAction			= null;
	treeModel.getMoveAction			= null;
	treeModel.getMoveParam			= null;
	treeModel.clearup				= null;
	
	//属性
	treeModel.treeView			= null;
	treeModel.attachParam		= null;
	treeModel.nodeExpandActions	= null;
	treeModel.entityInfos		= null;
	treeModel.initParam			= null;
	treeModel.menu				= null;
	treeModel.move_node			= null;
	treeModel.moveto_node		= null;
	treeModel.move_menu			= null;
}

/**
 * 存储节点相关信息
 */
function EntityInfo()
{
	//属性
	this.expandAction = null;		//展开动作
	this.moveActions = new Array();	//移动动作
	this.childEntityXpaths = [];	//子节点数据xpath
	this.expression	= "";			//显示表达式
	this.iconSrc	= "";			//图标路径
	this.onrefresh	= "";			//刷新节点时调用的动作
	this.onclick	= "";			//onclick动作
	this.ondblclick	= "";			//ondblclick动作
	this.menuItems	= new Array()	//节点右键菜单项
	
	return this;
}

/**
 *
 */
function _rtreemodel_addMenuItem( entityName, name, onclick )
{
	var entityInfo = this.getEntityInfo( entityName );
	
	entityInfo.menuItems.push( new EOSTreeMenuItem( name, onclick ) );
}
/**
 * 显示节点右键菜单
 */
function _rtreemodel_showNodeMenu( rtreeNode )
{
	var entityName = rtreeNode.entity.name;
	var entityInfo = this.getEntityInfo( entityName );
	
	var menuItems = entityInfo.menuItems;
	if( menuItems.length > 0 )
	{
		for( var i = 0; i < menuItems.length; i++ )
		{
			this.menu.insertItem( menuItems[i] );
		}
		this.menu.show();
	}
}

function _rtreemodel_onmenuclick( functionName )
{
	fireUserEvent( functionName, [this.treeView.cur_node] );
}

function _rtreemodel_getMovemenu( rtreeNode )
{
	if( rtreeNode.icon )
		this.move_menu.innerHTML = rtreeNode.icon.outerHTML + rtreeNode.cell.innerHTML;
	else
		this.move_menu.innerHTML = rtreeNode.cell.innerHTML;
    return this.move_menu;
}
//----- End RTree ---------------

//----- RTreeView ---------------
function RTreeView( id, model, rootName, rootIcon, width, height )
{
	//document.write( "<SPAN id = '" + id + "'></SPAN>" );
	var treeview = id;
	treeview.style.overflow = "auto";
	treeview.style.width = width;
	treeview.style.height = height;
	treeview.className = "RC_TREE";
	treeview.setAttribute( "richclientType", "RTREE" );
	
	//方法
	treeview.findTreeNode	= _rtreeview_findTreeNode;
	treeview.clearup		= _rtreeview_clearup;
	
	//属性
	treeview.model		= model;
	treeview.cur_node	= null;	//当前选中节点
	treeview.rootNode	= new RTreeNode( rootName, rootIcon, new RTreeEntity("root") );	
		treeview.rootNode.isroot = true;
		treeview.rootNode.level = 0;
	
	treeview.appendChild( treeview.rootNode );
	
	//事件
	treeview.onselectstart	= function(){ return false; };
	treeview.onmouseup		= _rtreeview_onmouseup;
	treeview.onmousemove	= _rtreeview_onmousemove;
	treeview.onkeydown		= _rtreeview_onkeydown;
	
	return treeview;	
}

/**
 * 查找节点
 */
function _rtreeview_findTreeNode( entityName, fieldName, value )
{

	function findTreeNodeIn( treeNode, entityName, fieldName, value )
	{		
		var _entityName = treeNode.entity.name;
		
		if( _entityName == entityName )
		{
			var _value = treeNode.entity.getProperty( fieldName );
			if( _value && (_value==value) )
				return treeNode;
		}
		
		var children = treeNode.getChildren();
		var _treeNode;
		for( var i = 0; i < children.length; i++ )
		{
			_treeNode =  findTreeNodeIn( children[i], entityName, fieldName, value );
			if( _treeNode )
				return _treeNode;
		}
		
		return null;
	}
	var root = this.rootNode;
			
	return findTreeNodeIn( root, entityName, fieldName, value );
}


/**
 * 清除树
 */
function _rtreeview_clearup()
{
	this.rootNode.clearup();
	this.model.clearup();
}

/**
 * 当鼠标弹起时，清除被拖动标记
 */
function _rtreeview_onmouseup()
{	
	this.model.moveNode();
}

/**
 * 当鼠标拖动时，如果有被拖动结点，移动被拖动结点
 */ 
doc_onmousemove = document.onmousemove;
doc_onmouseup	= document.onmouseup;
function _rtreeview_onmousemove()
{
	var model = this.model;
	
	if( !model.move_node ) return;
	
	var move_menu = model.getMovemenu( model.move_node );
	move_menu.show();
	
	//保存document的事件处理方法，修改onmousemove和
	//onmouseup方法，并在onmouseup后恢复原来的事件处理方法
	document.onmousemove = function()
	{
		move_menu.show();
		move_menu.style.posTop = event.y + document.body.scrollTop;
		move_menu.style.posLeft = event.x + document.body.scrollLeft;
	}
	document.onmouseup = function()
	{
		model.moveNode();
		document.onmousemove = doc_onmousemove;
		document.onmouseup = doc_onmouseup;
	}
	
}

/**
 * 键盘操作，选择节点
 */
function _rtreeview_onkeydown()
{
	//递归得到父节点的下一兄弟节点
	function getNextSibling( rtreeNode )
	{
		if( rtreeNode )
		{
			sibling = rtreeNode.nextSibling;
			if( sibling )
				return sibling;
			else
				return getNextSibling( rtreeNode.getParent() );
		}else
			return null;
	}
	//递归得到上一节点的最末子节点
	function getPreSibling( rtreeNode )
	{
		if( !rtreeNode ) return null;
		
		var children = rtreeNode.getChildren();
		var length = children.length;
		if( rtreeNode.isExpanded() && (length>0) )
		{
			return getPreSibling( children[length-1] );
		}else
			return rtreeNode;
	}
	event.cancelBubble = true;
	var treeView = this;
	var cur_node = treeView.cur_node;
	if( !cur_node ) return;
	
	switch( event.keyCode )
	{
		/*当按"下"键时，如果节点有子节点且展开，移到第一个子节点；
			如果有下一个兄弟节点，移动到下一兄弟节点；
			否则，移动到父节点的下一兄弟节点（递归）
		*/
		case 40 :
		{
			if( cur_node.getChildren()[0] && cur_node.isExpanded() )
			{
				cur_node.getChildren()[0].selected();
			}else
			{
				var nextNode = getNextSibling( cur_node );
				if( nextNode )
					nextNode.selected();
			}
			treeView.cur_node.scrollIntoView();
			break;
		}
		//当按"上"键时，移到上一兄弟节点的最末子节点；若没有，移到父节点
		case 38 :	
		{
			if( cur_node.previousSibling )
			{
				var nextNode = getPreSibling( cur_node.previousSibling );
				if( nextNode )
					nextNode.selected();
			}else if( cur_node.getParent() )
			{
				cur_node.getParent().selected();
			}
			treeView.cur_node.scrollIntoView();
			break;
		}
		case 37:	//"左"键， 关闭此节点；若节点已关闭，选择节点的父节点
		{
			if( cur_node.isExpanded() )
			{
				cur_node.collapseNode();
			}else
			{
				if( cur_node.getParent() )
					cur_node.getParent().selected();
			}
			treeView.cur_node.scrollIntoView();
			break;
		}
		case 39:	//"右"键， 展开此节点；若节点已展开，选择节点的第一个子节点
		{
			if( !cur_node.isExpanded() )
			{
				cur_node.expandNode();
			}else
			{
				if( cur_node.getChildren()[0] )
					cur_node.getChildren()[0].selected();
			}
			treeView.cur_node.scrollIntoView();
			break;
		}	
	}//end switch
	//treeView.cur_node.scrollIntoView();
	return false;
}
/**
 * RTreeNode代表树节点
 * 参数说明：
 * name: 节点显示名
 * iconsrc: 节点图标
 * entity: 节点对应的数据
 */
function RTreeNode( name, iconsrc, entity )
{
	var node = document.createElement( "DIV" );
		node.noWrap = true;
	var expandIcon = document.createElement( "IMG" );
		expandIcon.src = RTree.COLLAPS_ICON;
		expandIcon.ondrag = function(){ event.cancelBubble=true; return false};
		node.appendChild( expandIcon );
	var icon;
		if( iconsrc )
		{
			icon = document.createElement( "IMG" );
			icon.src = iconsrc;
			icon.ondrag = function(){ event.cancelBubble=true; return false};
			node.appendChild( icon );
		}
	var cell = document.createElement( "SPAN" );
		cell.innerHTML = name;
		cell.className = "RC_TREE_CELL";
		node.appendChild( cell );
	var childrenContainer = document.createElement( "DIV" );
		childrenContainer.style.marginLeft = 20;
		childrenContainer.style.display = "none";
		node.appendChild( childrenContainer );
		
	node.setAttribute( "richclientType", "RTREENODE" );
		
	//属性
	node.expandIcon = expandIcon;
	node.icon = icon;
	node.cell = cell;
	node.childLoaded = false;
	node.entity = entity;		//节点对应的数据
	node.isroot = false;
	node.isleaf = false;
	node.level = false;	
	node.childrenContainer = childrenContainer;
	
	//方法
	node.addChildNode		= _rtreeNode_addChildNodes;
	node.expandNode			= _rtreeNode_expandNode;
	node.collapseNode		= _rtreeNode_collapseNode;
	node.clearChildren		= _rtreeNode_clearChildren;
	node.selected			= _rtreeNode_selected;
	node.refresh			= _rtreeNode_refresh;
	node.isChildOf			= _rtreeNode_isChildOf;
	node.isExpanded			= _rtreeNode_isExpanded;
	node.getParent			= _rtreeNode_getParent;			//得到父节点
	node.getChildren		= _rtreeNode_getChildren;		//得到子节点
	node.getTree			= _rtreeNode_getTree;			//得到节点所在的树
	node.reloadChild		= _rtreeNode_reloadChild;		//重新加载子节点
	node.clearup			= _rtreeNode_clearup;
	
	//事件
	node.onclick		= _rtreenode_onclick;
	node.ondblclick		= _rtreenode_ondblclick;
	node.oncontextmenu	= _rtreenode_oncontextmenu;
	node.onmousedown	= _rtreenode_onmousedown;
	node.onmouseover	= _rtreenode_onmouseover;
	//node.onmouseup		= _rtreenode_onmouseup;
	
	return node;
}

function _rtreeNode_selected()
{
	var rtreeView = findRTree( this );
	//清除原选中节点颜色，设置当前选中节点颜色
	if( rtreeView.cur_node )
	{
		rtreeView.cur_node.cell.className = "RC_TREE_CELL";
	}
	this.cell.className = "RC_TREE_ACTIVENODE";
	rtreeView.cur_node = this;
}

function _rtreeNode_addChildNodes( rtreeNode )
{
	this.childrenContainer.appendChild( rtreeNode );
}

function _rtreeNode_expandNode()
{
try{
	//对叶子节点不做处理
	if( this.isleaf )	return;
	
	var rtreeNode = this;
	var model = findRTree( rtreeNode ).model;
	
	this.expandIcon.src = RTree.EXPAND_ICON;
	//如果子节点数据未加载，加载数据并展开
	if( !rtreeNode.childLoaded )
	{
		model.loadNodeChild( rtreeNode );
		rtreeNode.childLoaded = true;
		//没有子节点，为叶子节点
		if( rtreeNode.childrenContainer.children.length == 0 )
		{
			rtreeNode.isleaf = true;
			rtreeNode.expandIcon.src = RTree.LEAF_ICON;
		}
	}
	if( !rtreeNode.isleaf )
		rtreeNode.childrenContainer.style.display = "";
}catch( e )
{
	alert( "catch exception on _rtreeNode_expandNode" );
}
}

function _rtreeNode_collapseNode()
{
	this.expandIcon.src = RTree.COLLAPS_ICON;
	this.childrenContainer.style.display = "none";
}

function _rtreeNode_clearChildren()
{
	//this.childrenContainer.innerHTML = "";
	var children = this.childrenContainer.childNodes;
	for( var i = children.length -1; i >= 0; i-- )
	{
		children[i].removeNode( true );
	}
	this.expandIcon.src = RTree.COLLAPS_ICON;
	this.childrenContainer.style.display = "none";
	this.isleaf = false;
	this.childLoaded = false;
}

/**
 * 刷新节点，重新计算节点的显示
 */
function _rtreeNode_refresh( entity )
{
	var rtreeView = findRTree( this );
	var model = rtreeView.model;
	if( entity )
		this.entity = entity;
	
	//设置level
	if( this.isroot )
		this.level = 0;
	else
		this.level = this.getParent().level+1;
		
	this.cell.innerHTML = model.getTreeNodeName( this.entity );
	//this.icon.src = model.getTreeNodeIcon( this.entity.name );
	var iconsrc = model.getTreeNodeIcon( this.entity.name );	
	if( iconsrc )
	{
		if( !this.icon )
		{
			this.icon = document.createElement( "IMG" );
			this.icon.ondrag = function(){ event.cancelBubble=true; return false};
			this.appendChild( this.icon );
		}
		this.icon.src = iconsrc;
	}
	
	var entityInfor = model.getEntityInfo( this.entity.name );
	var onrefreshFunction = entityInfor.onrefresh;
	if( !onrefreshFunction )	return;	
	fireUserEvent( onrefreshFunction, [this, this.cell]);
}

/**
 * 得到父节点
 */
function _rtreeNode_getParent()
{
	if( this.isroot )
		return null;
	else
		return this.parentNode.parentNode;
}

/**
 * 得到子节点
 */
function _rtreeNode_getChildren()
{
	return this.childrenContainer.childNodes;
}

/**
 * 得到节点所在树
 */
function _rtreeNode_getTree()
{
	return findRTree( this );
}

/**
 * 判断节点是否是某节点的子节点
 */
function _rtreeNode_isChildOf( parentNode )
{
	var _parent = this;
	while( _parent = _parent.getParent() )
	{
		if( _parent == parentNode )
			return true;
	}
	return false;
}

/**
 * 判断节点是否处于展开状态
 */
function _rtreeNode_isExpanded()
{
	if( this.childrenContainer.style.display == "none" )
		return false;
	else
		return true;
}

/**
 * 重新加载子节点
 */
function _rtreeNode_reloadChild()
{
	if( this.childLoaded )
	{
		this.clearChildren();
		this.expandNode();
	}
}

/**
 * 清除节点
 */
function _rtreeNode_clearup()
{
	var node = this;
	var children = node.getChildren();
	for( var i = children.length -1; i >= 0; i-- )
	{
		children[i].clearup( true );
	}
	
	node.expandIcon = null;
	node.icon = null;
	node.cell = null;
	node.entity = null;
	node.childrenContainer = null;
	
	//方法
	node.addChildNode		= null;
	node.expandNode			= null;
	node.collapseNode		= null;
	node.clearChildren		= null;
	node.selected			= null;
	node.refresh			= null;
	node.isChildOf			= null;
	node.isExpanded			= null;
	node.getParent			= null;	
	node.getChildren		= null;
	node.getTree			= null;
	node.reloadChild		= null;
	node.clearup			= null;
	
	//事件
	node.onclick		= null;
	node.ondblclick		= null;
	node.oncontextmenu	= null;
	node.onmousedown	= null;
	node.onmouseover	= null;
	node.onmouseup		= null;
	
	node.removeNode( true );
}

function _rtreenode_onclick()
{
	function expandOrCollapseNode()
	{
		//如果是叶子节点，不做处理
		if( rtreeNode.isleaf )
			return;
		
		if( rtreeNode.childrenContainer.style.display == "none" )
		{
			rtreeNode.expandNode();
		}else
		{
			rtreeNode.collapseNode();
		}
	}
try{
	event.cancelBubble = true;
	var rtreeView = findRTree( this );
	var model = rtreeView.model;
	var rtreeNode = this;
	var src = event.srcElement;
	
	if( src == this.cell || src == this.expandIcon || src == this.icon )	//选中节点
	{
		this.selected();
	}
	if( src == this.cell )			//触发用户自定义动作
	{
		var functionName = model.getEntityInfo( this.entity.name ).onclick;
		if( functionName )
		{
			fireUserEvent(functionName, [this]);
		}
	}
	if( src == this.expandIcon )	//点击展开关闭结点
	{
		expandOrCollapseNode();
	}
	
}catch( e )
{
	alert( "catch exception on _rtreenode_onclick" );
}
}


function _rtreenode_ondblclick()
{
	event.cancelBubble = true;
	
	var rtreeView = findRTree( this );
	var model = rtreeView.model;
	var rtreeNode = this;
	var src = event.srcElement;
	
	if( src == this.cell )			//触发用户自定义动作
	{
		var functionName = model.getEntityInfo( this.entity.name ).ondblclick;
		if( functionName )
		{
			fireUserEvent(functionName, [this]);
		}
	}
}

/**
 * 点击右键弹出菜单
 */
function _rtreenode_oncontextmenu()
{
	event.cancelBubble = true;
	
	var rtreeView = findRTree( this );
	var model = rtreeView.model;
	var menu = model.menu;	
	var rtreeNode = this;	
	
	rtreeNode.selected();
	
	if( event.srcElement == this.cell )
		model.showNodeMenu( rtreeNode );	
	
	return false;
}

/**
 * 当鼠标右键按下时，准备被拖动
 */
function _rtreenode_onmousedown()
{
	if( event.button != 1 ) return;
	event.cancelBubble = true;
	
	var cell = event.srcElement;
	if( cell != this.cell ) return;
	
	var treeView = findRTree( this );
	var model = treeView.model;
	
	if( model.isNodeMovable( this ) )
		model.move_node = this;
}

/**
 * 在托拽过程中，当移动到某结点时
 */
function _rtreenode_onmouseover()
{
	var treeView = findRTree( this );
	var model = treeView.model;	
	if( model.move_node != null )
	{
		event.cancelBubble = true;
		
		model.moveto_node = this;
		this.selected();
	}
}

/**
 * 在托拽过程中，当托拽到某结点时
 */
function _rtreenode_onmouseup()
{
	var treeView = findRTree( this );
	var model = treeView.model;	
	
	model.moveNode();
}

function findRTree( tag )
{
	var parent = tag;
	var richclientType;
	while( parent = parent.parentNode  )
	{
		richclientType = parent.getAttribute( "richclientType" );
		if( richclientType == "RTREE" )
			return parent;
	}
	return null;
}

/**
 * 树的右键弹出菜单
 */
function EOSTreeMenu( model )
{
	topwindow=_get_top_window();
	var menu = topwindow.document.createElement( "div" );
	//menu.className = "RC_TREE_MENU";
	
	menu.style.cursor="default";
	menu.style.font="normal 10pt Arial, Helvetica, sans-serif";
	menu.style.textAlign="left";
	menu.style.width="120px";
	menu.style.backgroundColor="#E8E8EA";
	menu.style.border="1 solid buttonface";
	menu.style.border="2 outset buttonhighlight";
	menu.style.cursor=" hand";
	menu.style.color="#000000";
	menu.style.position = "absolute";
	menu.style.display = "none";
	topwindow.document.body.appendChild( menu );
	
	
	//方法
	menu.clearItems		= _treemenu_clearItems;
	menu.insertItem		= _treemenu_insertItem;	
	menu.hide			= function(){ 
								this.clearItems(); 
								this.style.display = "none"; 
							};
	menu.show			= function(){
								this.style.display = "";
								this.style.zindex = 999;
								this.style.posTop = screenTop-topwindow.screenTop+event.clientY+topwindow.document.body.scrollTop;
								this.style.posLeft = screenLeft-topwindow.screenLeft+event.clientX+topwindow.document.body.scrollLeft;
								this.setActive();
							};
	
	//属性
	menu.model		= model;
	
	//事件
	menu.oncontextmenu	= function(){ return false; };
	menu.onselectstart	= function(){ return false; };
	menu.onblur		= function(){ EOSTreeMenu.hideAll(); };	//当失去焦点时隐藏所有菜单
	
	return menu;
}

/**
 * 注册所有菜单
 */
EOSTreeMenu.register = function( menu )
{
	if( !this.menus ) this.menus = new Array();
	
	this.menus.push( menu );
}

/**
 * 隐藏所有菜单
 */
EOSTreeMenu.hideAll = function()
{
	if( !this.menus ) return;
	
	for( var i = 0; i < this.menus.length; i++ )
		this.menus[i].hide();
}

/**
 * 树的右键弹出菜单项
 */
function EOSTreeMenuItem( name, onclick )
{
	var topwindow=_get_top_window();
	var menuItem = topwindow.document.createElement( "div" );
	menuItem.className = "RC_TREE_MENUITEM";
	menuItem.innerHTML = name;
	
	//属性
	menuItem.onclickFunction	= onclick;
	
	//事件
	menuItem.onclick		= _treemenuitem_onclick;
	menuItem.onmouseover	= _treemenuitem_onmouseover;
	menuItem.onmouseout		= _treemenuitem_onmouseout;
	return menuItem;
}
/**
 * 清除菜单所有菜单项
 */
function _treemenu_clearItems()
{
	var children = this.children;
	while (children.length>0)
	{
		this.removeChild( children[0] );
	}
}

/**
 * 添加菜单项
 */
function _treemenu_insertItem( menuItem )
{
	this.appendChild( menuItem );
}

function _treemenuitem_onclick()
{
	var menu = this.parentNode;		
	menu.model.onmenuclick( this.onclickFunction );
	this.style.backgroundColor = "";
	this.style.color = "black";
	menu.hide();
}

/**
 * 当鼠标移入时，改变菜单项颜色
 */
function _treemenuitem_onmouseover()
{
	this.style.backgroundColor = "highlight";
	this.style.color = "white";
}

/**
 * 当鼠标移出时，改变菜单项颜色
 */
function _treemenuitem_onmouseout()
{
	this.style.backgroundColor = "";
	this.style.color = "black";
}

function RTreeMoveMenu()
{
	
	var move_menu = document.createElement( "div" );
	move_menu.className = "RC_TREE_DRAGNODE";
	move_menu.onselectstart = function(){event.cancelBubble=true;return false;};
	//div.style.display = "block";
	/*drag.style.border = "2px solid gray";
	drag.style.background = "blue";
	drag.style.color = "white";
	drag.style.cursor = "hand";*/
	move_menu.style.position = "absolute";
	move_menu.style.display = "none";
	document.body.appendChild(move_menu);
    
    //方法
    move_menu.show		= _rtreemovemenu_show;
    move_menu.hide		= _rtreemovemenu_hide;
    return move_menu;
}

function _rtreemovemenu_show()
{
	this.style.display = "";
}

function _rtreemovemenu_hide()
{
	this.style.display = "none";
}
//----- End RTreeView ---------------

//----- RTreeEntity ---------------

function RTreeEntity( name )
{
	var entity = new Array();
	
	//属性
	entity.name = name;
	//方法
	entity.setProperty	= _entity_setProperty;
	entity.getProperty	= _entity_getProperty;
	entity.toString		= _entity_toString;
	
	return entity;
}

/**
 * 根据XML Node节点创建Entity
 * Node结构为<entity><field1/><field2/><field3/></entity>
 */
function _entity_createRTreeEntity( node )
{
	if( !node ) return;
	try{	
		var entity = new RTreeEntity( node.tagName );
		var _fieldNode;
		for( var i = 0; i < node.childNodes.length; i++ )
		{
			_fieldNode = node.childNodes.item(i);
			entity.setProperty( _fieldNode.tagName, _fieldNode.text );
		}
		return entity;
	}catch( e )
	{
		return new RTreeEntity( node.tagName );
	}
}

var EntityFactory = new Object();
EntityFactory.createRTreeEntity = _entity_createRTreeEntity;

function _entity_getProperty( fieldname )
{
	var index = eval( "this." + fieldname +"_index" );
	
	if( typeof(index)!="undefined" && index!=null )
	{
		return this[index].value;
	}
	
	return null;
}

function _entity_setProperty( fieldname, value )
{
	var index = eval( "this." + fieldname + "_index");
	if( typeof( index ) == "undefined" )		//如果fieldname不存在，添加field
	{
		index = this.length;
		
		var field = new Object();
		field.fieldname = fieldname;
		field.value = value;
		
		eval( "this." + fieldname + "_index = " + index );
		this[index] = field;
	}
	
	this[index].value = value;	
}

function _entity_toString()
{
	var field = null;
	var str = "<" + this.name + ">";
	for( var i = 0; i < this.length; i++ )
	{
		field = this[i];
		str += "<" + field.fieldname + ">";
		str += conversion(field.value);
		str += "</" + field.fieldname + ">";
	}
	
	str += "</" + this.name + ">";
	
	return str;
}

//--------------------- Utile Functions ----------------------
function fireUserEvent(function_name, param)
{
	var result;
	var paramstr="";
	for(i=0; i<param.length; i++){
		if (i==0)
		 	paramstr="param["+i+"]";
		 else
		 	paramstr=paramstr+",param["+i+"]";
	}

	if (isUserEventDefined(function_name))
	{
		eval("result="+function_name+"("+paramstr+");");
	}
	return result;
}

function isUserEventDefined( function_name )
{
	if (function_name=="") return false;
	
	var result;
	eval("result=(typeof("+function_name+")!=\"undefined\");");
	return result;
}
function _get_top_window()
{
 if (top.document.body.isTextEdit)
  return top;
 var pWin = parent;
 var pValid = window;
 if (pWin.document.body.isTextEdit)
  pValid = pWin;
 while (top != pWin) {
  pWin = pWin.parent;
  if (pWin.document.body.isTextEdit)
   pValid = pWin;
 }
  return pValid;
}
