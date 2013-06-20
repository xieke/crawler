
function Entity( name )
{
	if( !name )
		name = "entity";
		
	var entity = new Array();
	
	entity.setProperty = _entity_setProperty;
	entity.getProperty = _entity_getProperty;
	entity.toString = _entity_toString;
	entity.clone = _entity_clone;
		
	entity.status = Entity.STATUS_INIT;
	
	entity.name = name;
		
	return entity;
}

Entity.STATUS_NEW = 0;
Entity.STATUS_INIT = 1;
Entity.STATUS_MODIFIED = 2;
Entity.STATUS_DELT = 3;

function entity_parse_invalid(name)
{
	var str="_";
	for(i=0;i<name.length;i++)
	{
		var cc=name.charAt(i);
		switch(cc)
		{
			case "-" :
			{ cc="_0_"
				break;
				}
				case "+" :
			{ cc="_0_"
				break;
				}
				case "/" :
			{ cc="_0_"
				break;
				}
				case "\\" :
			{ cc="_0_"
				break;
				}
				case "(" :
			{ cc="_0_"
				break;
				}
				case ")" :
			{ cc="_0_"
				break;
				}
				case "*" :
			{ cc="_0_"
				break;
				}
				case "," :
			{ cc="_0_"
				break;
				}
				case "." :
			{ cc="_0_"
				break;
				}
		}
str=str+cc;
}

	return str;
	}

function _entity_getProperty( fieldname )
{
	var index = eval( "this." + entity_parse_invalid(fieldname) +"_index" );
	var value;
	
	if( typeof(index)!="undefined" && index!=null )
	{
		value = this[index].value;
	}
	
	return value ? value : "";
}

function _entity_clone()
{
	var _entity = new Entity( this.name );
	var field = null;
	for( var i = 0; i < this.length; i++ )
	{
		field = this[i];
		_entity.setProperty( field.fieldname, field.value );
	}
	
	return _entity;
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

function _entity_setProperty( fieldname, value )
{
	var index = eval( "this." + entity_parse_invalid(fieldname) + "_index");
	
	if( typeof( index ) == "undefined" )		//如果fieldname不存在，添加field
	{
		index = this.length;
		
		var field = new Object();
		field.fieldname = fieldname;
		field.value = value;
		
		eval( "this." + entity_parse_invalid(fieldname) + "_index = " + index );
		this[index] = field;
	}
	
	this[index].value = value;	
}

function EntityList( entityArray )
{
	var entitylist;
	
	entitylist = entityArray ? entityArray : new Array();
	
	entitylist.addEntity = _entitylist_addEntity;
	entitylist.removeEntity = _entitylist_removeEntity;
	entitylist.toString = _entitylist_toString;
	entitylist.init = _entitylist_init;
	
	return entitylist;
}

/*根据xml构造entitylist，xml的结构为：
<list>
	<entity>
		<field1>value1</field1>
		<field2>value2</field2>
		<field3>value3</field3>
		...
	</entity>
	<entity>
		<field1>value1</field1>
		<field2>value2</field2>
		<field3>value3</field3>
		...
	</entity>
	...
</list>
 参数：listNode XMLDOMNode
**/
function _entitylist_init( listNode )
{
	if( !listNode ) return;
	var list = listNode.childNodes;
	
	if( list.item(0) )
	{
		this.entityType = list.item(0).tagName;
	}
	
	for( var i = 0; i < list.length; i++ )
	{
		_entity = list.item(i);
		entity = this.addEntity( new Entity( _entity.tagName ) );
		for( var j = 0; j < _entity.childNodes.length; j++ )
		{
			_field = _entity.childNodes.item(j);
			entity.setProperty( _field.tagName, _field.text );
		}
	}
}

function _entitylist_toString()
{	
	var str = "<list>";
	for( var i = 0; i < this.length; i++ )
	{
		str += this[i].toString();
	}
	
	str += "</list>";
	
	return str;
}

function _entitylist_addEntity( entity )
{
	if( !entity ) return;
	
	var index = this.length;		
	this[index] = entity;
	
	return entity;
}

function _entitylist_removeEntity( entity )
{
	for( var i = 0; i < this.length; i++ )
	{
		if( this[i] == entity )
		{
			this.splice( i, 1 );
		}
	}
}

//-----------------------------

/*
 * Dataset构造函数
 * 参数：
	entityType	Enitity类型，新增entity时按此类型构造entity
	action		业务逻辑名
	param		传递给业务逻辑的参数
	retXpaths	从业务逻辑返回数据提取entitylist的xpath数组
**/
function Dataset( entityType)
{
	var dataset = new Object();
	//属性
	dataset.pageIndex	= 0;
	dataset.recordCount	= -1;
	dataset.rowNumb		= 10;
	dataset.entityType	= entityType;
	dataset.action		= null;
	dataset.submitAction		= null;
	dataset.param		= null;
	dataset.xpaths	= null;
	dataset.srcXml=null;
	dataset.noCount=false;
	dataset.entities	= new EntityList();
	dataset.removedEntities	= new EntityList();	
	
	//方法
	dataset.gotoPage            = _dataset_gotoPage;  
	dataset.loadData			= _dataset_loadData;
	dataset.loadFirstPageData	= _dataset_loadFirstPageData;
	dataset.loadLastPageData	= _dataset_loadLastPageData;
	dataset.loadPrePageData		= _dataset_loadPrePageData;
	dataset.loadNextPageData	= _dataset_loadNextPageData;
	dataset.reloadData			= _dataset_reloadData;
	//dataset.loadData2			= _dataset_loadData2;
	dataset.addEntity			= _dataset_addEntity;
	dataset.removeEntity		= _dataset_removeEntity;
	dataset.setValue			= _dataset_setvalue;
	dataset.getModifiedEntities	= _dataset_getModifiedEntities;
	dataset.getInsertEntities	= _dataset_getInsertEntities;
	dataset.getRemovedEntities	= _dataset_getRemovedEntities;
	dataset.filter				= _dataset_filter;
	dataset.submit				= _dataset_submit;
	dataset.reset              = _dataset_reset;
	dataset.findEntity			= _dataset_findEntity;
	dataset.toString			= _dataset_toString;
	dataset.clear				= _dataset_clear;
	
	return dataset;
}

function _dataset_reset()
{

//try{
 var retDom = new ActiveXObject("MSXML2.DOMDocument");
		retDom.async=false;
		retDom.loadXML( trim(this.srcXml) );
		
		//返回的结果中可以有多个entitylist节点
		var listNode;
		var entitylistArray = new Array();
		for( var i = 0; i < this.xpaths.length; i++ )
		{
			listNode = retDom.selectSingleNode( "root/data/" + this.xpaths[i] );
			var a = new EntityList();
			a.init( listNode );
			this.entities = new EntityList( this.entities.concat( a ) );
			//this.entities = new EntityList( this.entities.concat( new EntityList().init( listNode ) );
		}
		//返回分页的总页数
		
		
		
	//}catch( e )
	//{
//		alert( " catch exception on donging _dataset_reset()" );
	//}

}

function _dataset_reloadData()
{	
	//清除原有数据
	this.entities = null;
	this.removedEntities = null;
	delete this.entities;
	delete this.removedEntities;
	
	this.entities	= new EntityList();
	this.removedEntities	= new EntityList();
	this.loadData( this.action, this.param, this.xpaths, this.pageIndex, this.rowNumb,this.noCount);
	
	return this;
}

function _dataset_submit( initParam )
{	
	var bizParam = "<root><data>";
	
	if( initParam )
		bizParam += initParam;
	
	bizParam += "<updateEntity>";
	bizParam += this.getModifiedEntities().toString();
	bizParam += "</updateEntity>";
	
	bizParam += "<insertEntity>";
	bizParam += this.getInsertEntities().toString();
	bizParam += "</insertEntity>";
	
	bizParam += "<removeEntity>";
	bizParam += this.getRemovedEntities().toString();
	bizParam += "</removeEntity>";		
	bizParam += "</data></root>";
	
	try
	{ 
		var submitSuccess = callBizAction( this.submitAction, bizParam );
		
		//提交数据失败，保持当前数据
		if( !submitSuccess )
		{
			return false;
		}
		
		//改变entity的状态
		for( var i = 0; i < this.entities.length; i++ )
			this.entities[i].status = Entity.STATUS_INIT;
		//清除removedEntities
		this.removedEntities	= new EntityList();	
	}catch( e )
	{
		alert( "_dataset_submitChange() :: callBizAction failed!!" );
		return false;
	}
	return true;
}

function _dataset_loadData( bizAction, bizParam, xpaths, pageIndex, rowNumb,noCount )
{  
	function setSplitPageParm( pageIndex, rowNumb )
	{
		var i = bizParam.indexOf( "</data></root>" );
		var r = bizParam.substring(0, i);
		
		r = r + "<PageCond><begin>" + pageIndex*rowNumb + "</begin><length>" + rowNumb + "</length>";
		
		if(noCount=="true")
			      r=r+"<count>noCount</count>";
		  
		   if(noCount=="false")
		        {
			      r=r+"<count>-1</count>";
				  
					 }
           
                if(noCount==1)
                      r=r+"<count>1</count>";
                 


		bizParam = r +"</PageCond>"+ bizParam.substring(i);
	
	}
	
	pageIndex ? this.pageIndex = pageIndex : this.pageIndex = 0;
	rowNumb ? this.rowNumb = rowNumb : this.rowNumb = 10;
	
	this.action	=  bizAction;
	this.param	=  bizParam;
	this.xpaths	=  xpaths;	
	this.noCount=  noCount;
	
	try{
		var retXmlStr = "";
		if( !bizParam )
			bizParam = "<root><data></data></root>";
		else
			bizParam = "<root><data>" + bizParam + "</data></root>";
		setSplitPageParm( this.pageIndex, this.rowNumb );
           if(noCount=="false")
		   this.noCount=1;
		  
		 
		try
		{
			
			retXmlStr = callBizAction( bizAction, bizParam );
			
			if( !retXmlStr )
				return this;
		}catch( e )
		{
			debug( "loadChildTreeNodesData() :: callBizAction failed!!" );
			retXmlStr = "<root><data></data></root>";
		}
		this.srcXml=retXmlStr;
		var retDom = new ActiveXObject("MSXML2.DOMDocument");
		retDom.async=false;
		retDom.loadXML( trim(retXmlStr) );
		
		//返回的结果中可以有多个entitylist节点
		var listNode;
		var entitylistArray = new Array();
		for( var i = 0; i < xpaths.length; i++ )
		{
			listNode = retDom.selectSingleNode( "root/data/" + xpaths[i] );
			var a = new EntityList();
			a.init( listNode );
			this.entities = new EntityList( this.entities.concat( a ) );
			//this.entities = new EntityList( this.entities.concat( new EntityList().init( listNode ) );
		}
		//返回分页的总页数
		var countNode = retDom.selectSingleNode( "root/data/PageCond/count" );
		//如果noCount是false，则读入总页数
		if(noCount=="false")
		   if( countNode )
			this.recordCount = countNode.text;
		
		
		return this;
	}catch( e )
	{
		alert( " catch exception on donging _dataset_loadData()" );
	}
}

function _dataset_clear()
{
	this.entities = null;
	this.removedEntities = null;
	delete this.entities;
	delete this.removedEntities;
	
	this.entities	= new EntityList();
	this.removedEntities	= new EntityList();
}

//取得首页数据
function _dataset_loadFirstPageData()
{
	if( this.pageIndex <= 0 )
		return this;
	//清除原有数据
	this.clear();
	
	this.pageIndex = 0;	
	this.loadData( this.action, this.param, this.xpaths, this.pageIndex, this.rowNumb,this.noCount);
	
	return this;
}

//取得尾页数据
function _dataset_loadLastPageData()
{
try{
	if( (this.recordCount > 0) && ((this.pageIndex+1)*this.rowNumb >= this.recordCount) )
		return this;
	
	//清除原有数据
	this.clear();
	this.pageIndex = Math.ceil(this.recordCount/this.rowNumb)-1;
	this.loadData( this.action, this.param, this.xpaths, this.pageIndex, this.rowNumb,this.noCount);
	
	return this;
}catch( e )
{
	alert( "Catch Exception in _dataset_loadNextPageData " );
}
}

function _dataset_loadPrePageData()
{
	if( this.pageIndex <= 0 )
		return this;
	//清除原有数据
	this.clear();
	
	this.pageIndex -= 1;	
	this.loadData( this.action, this.param, this.xpaths, this.pageIndex, this.rowNumb,this.noCount);
	
	return this;
}

function _dataset_loadNextPageData()
{
try{
	if( (this.recordCount > 0) && ((this.pageIndex+1)*this.rowNumb >= this.recordCount) )
		return this;
	
	//清除原有数据
	this.clear();
	
	this.pageIndex += 1;	
	this.loadData( this.action, this.param, this.xpaths, this.pageIndex, this.rowNumb,this.noCount);
	
	return this;
}catch( e )
{
	alert( "Catch Exception in _dataset_loadNextPageData " );
}
}

function _dataset_gotoPage(pageIndex)
{

try{
//	if( (this.recordCount > 0) && ((this.pageIndex+1)*this.rowNumb >= this.recordCount) )
	//	return this;
	
	//清除原有数据
	this.clear();
	
	this.pageIndex=pageIndex-1;	
	this.loadData( this.action, this.param, this.xpaths, this.pageIndex, this.rowNumb,this.noCount);
	
	return this;
}catch( e )
{
	alert( "Catch Exception in _dataset_loadNextPageData " );
}

}

function _dataset_loadData2( datasetid )
{
try{
	if( !datasetid ) return;
	
	var dataset = this;
	var entity = null;
	var _entity = null;
	var _field = null;
	
	var list = datasetid.XMLDocument.selectSingleNode("list");
	
	for( var i = 0; i < list.childNodes.length; i++ )
	{
		_entity = list.childNodes[i];
		entity = dataset.addEntity( new Entity(list.childNodes[i].tagName) );
		
		for( var j = 0; j < _entity.childNodes.length; j++ )
		{
			_field = _entity.childNodes[j];
			entity.setProperty( _field.tagName, _field.text );	
		}	
		entity.status = Entity.STATUS_INIT;
	}
}catch( e )
{
	alert( " catch exception on donging _dataset_loadData2()" );
}
}

//如果参数为空，增加一新entity
function _dataset_addEntity( entity )
{
	var index = this.entities.length;
	var the_entity = null;
		
	the_entity = entity ? entity : new Entity( this.entityType );
	the_entity.status = Entity.STATUS_NEW;
		
	this.entities.addEntity( the_entity );
	this.cur_index = index;
	
	return the_entity;
}

function _dataset_setvalue( entity, fieldname, value )
{
	//var entity = this.entities[this.cur_index];
	entity.setProperty( fieldname, value );
	
	//如果当前entity为init状态，修改状态为modified
	//对entity为new和modified的状态不做修改
	if( entity.status == Entity.STATUS_INIT )
		entity.status = Entity.STATUS_MODIFIED;
}

function _dataset_removeEntity( entity )
{
	//如果删除的entity不是新增加的entity
	if( entity.status != Entity.STATUS_NEW )
		this.removedEntities.addEntity(entity);
		
	this.entities.removeEntity( entity );	
}

function _dataset_getModifiedEntities()
{
	var mlist = new EntityList();
	
	var entity = null;
	for( var i = 0; i < this.entities.length; i++ )
	{
		entity = this.entities[i];
		if( entity.status == Entity.STATUS_MODIFIED )
		{
			mlist.addEntity( entity );
		}
	}
	
	return mlist;
}

function _dataset_getInsertEntities()
{
	var nlist = new EntityList();
	
	var entity = null;
	for( var i = 0; i < this.entities.length; i++ )
	{
		entity = this.entities[i];
		if( entity.status == Entity.STATUS_NEW )
		{
			nlist.addEntity( entity );
		}
	}
	
	return nlist;
}

function _dataset_getRemovedEntities()
{
	return this.removedEntities;
}

function _dataset_findEntity( fieldName, value )
{	
	var entity = null;
	var _value;
	for( var i = 0; i < this.entities.length; i++ )
	{
		entity = this.entities[i];
		_value = entity.getProperty( fieldName );
		
		if( _value == value )
			return entity;
	}
	
	return null;
}

function _dataset_filter( fieldName, key )
{	
	var _dataset = new Dataset( this.dataType );
	
	var entity = null;
	var _value;
	for( var i = 0; i < this.entities.length; i++ )
	{
		entity = this.entities[i];
		_value = entity.getProperty( fieldName );
		
		if( _value.indexOf( key ) == 0 )
			_dataset.addEntity( entity.clone() );
	}
	
	return _dataset;
}

function _dataset_toString()
{
	return this.entities.toString();
}

function createDatasetFromSelect( select )
{
	var dataset = new Dataset();	
	if( !select ) return dataset;
	
	var options = select.options;
	var value, text, entity;
	for( var i = 0; i < options.length; i++ )
	{
		value	= options[i].value;
		text	= options[i].text;
		entity	= new Entity();
		entity.setProperty( "value", value );
		entity.setProperty( "text", text );
		
		dataset.addEntity( entity );		
	}
	return dataset;
}
//---------------------------------
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
//alert( "isUserEventDefined(" + function_name + " ) :: " + isUserEventDefined(function_name));
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

var jsdebug = false;
function debug( msg )
{
	if( jsdebug )
		alert( msg );
}
