	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<%@ page contentType="text/html;charset=utf-8" %>
	//用于 ext 的 tip 提示 输入框@	
		function loadFrm(element1,element2,itemtype)
		{

			//alert("捡来了");
			var url2 = "<c:url value='/GeneralHandleSvt?reqType=utility.UtilActionHandler.showSelectCause&type='/>"+itemtype;
			//,ajax.LibraryAH.select&backdoor=qJdBlE";
			var st = new Ext.data.JsonStore({
			    url:url2,
			    root: 'item',
			    fields: ['id','value']
			});
			
			//alert('loaded');
			var e1 = document.getElementById(element1);
			var e2 = document.getElementById(element2);
			var f = new Ext.form.ComboBox({
				store:st,
				valueField:'id', 
				displayField:'value',				
				typeAhead:true,
				mode:'local',				
				triggerAction:'all',
				autoShow:'x-hide-display',
	  		selectOnFocus:true,
    		applyTo:element1,
            
            
    		onBlur:function(r,i,s){if(e1.value=="")e2.value="";//用于当输入框无值或删除输入框值候，清空隐藏对象的值
			this.store.each(function(D) {					   //用于输入字符不用点击选项或按钮，自动匹配相应项值
                if(D.data['value']==e1.value)e2.value=D.data['id'];
            });
			
			},   			
    			
    		onSelect:function (r,i,s){e1.value=r.get('value');e2.value=r.get('id');this.collapse(); }
			});	
			
			//增加侦听事件,当得到焦点时触发
			f.on('focus',function(f){
				//判断下拉中的总的数量
				
				if(st.getTotalCount()<=0){
					//alert("数据加载中...");
					//e1.value="数据加载中...";	
					//alert('focus');
					st.load();
					//e1.value="";	
				}    				
			});				
			//f.on('select',myFun);
			return f;
		}

		function refresh(){
			
			alert(f);
			f.reset(); 
			//st.proxy= new Ext.data.HttpProxy({url: 'ConjunctSelectProcess.jsp?id=' + combo.getValue()}); 
			st.load(); 

		}

		function loadFrm2(element1,element2,itemtype)
		{
			var url2 = "<c:url value='/GeneralHandleSvt?reqType=utility.UtilActionHandler.showSelectCause&type='/>"+itemtype;
			//,ajax.LibraryAH.select&backdoor=qJdBlE";
			var st = new Ext.data.JsonStore({
			    url:url2,
			    root: 'item',
			    fields: ['id','value']
			});
			
			var f = new Ext.form.ComboBox({
				store:st,
				valueField:'id',
				displayField:'value',
				typeAhead:true,
				mode: 'local',
				triggerAction: 'all',
				autoShow:'x-hide-display',
	  		selectOnFocus:true,
    		applyTo:element1,
    		onBlur:function(r,i,s){if(e1.value=="")e2.value="";//用于当输入框无值或删除输入框值候，清空隐藏对象的值
			this.store.each(function(D) {					   //用于输入字符不用点击选项或按钮，自动匹配相应项值
                if(D.data['value']==e1.value)e2.value=D.data['id'];
            });
			
			}
			});
			//f.reset();
			//增加侦听事件,当得到焦点时触发
			f.on('focus',function(f){

				//判断下拉中的总的数量
				if(st.getTotalCount()<=0){
					st.load();
				}    				
			});	
			//当改变值时触发
			f.on('change',function(f,value){
				document.getElementById(element2).value=value;
    		this.collapse();
			});	
		}
		
		function loadFrmNoCache(element1,element2,itemtype)
		{
			var url2 = "<c:url value='/GeneralHandleSvt?reqType=utility.UtilActionHandler.showSelectCause&cache=no&type='/>"+itemtype;
			//,ajax.LibraryAH.select&backdoor=qJdBlE";
			var st = new Ext.data.JsonStore({
			    url:url2,
			    root: 'item',
			    fields: ['id','value']
			});
			
			var f = new Ext.form.ComboBox({
				store:st,
				valueField:'id',
				displayField:'value',
				typeAhead:true,
				mode: 'local',
				triggerAction: 'all',
				autoShow:'x-hide-display',
	  		selectOnFocus:true,
    		applyTo:element1,
    		onBlur:function(r,i,s){if(e1.value=="")e2.value="";//用于当输入框无值或删除输入框值候，清空隐藏对象的值
			this.store.each(function(D) {					   //用于输入字符不用点击选项或按钮，自动匹配相应项值
                if(D.data['value']==e1.value)e2.value=D.data['id'];
            });
			
			}
			});
			//f.reset();
			//增加侦听事件,当得到焦点时触发
			f.on('focus',function(f){

				//判断下拉中的总的数量
				if(st.getTotalCount()<=0){
					st.load();
				}    				
			});	
			//当改变值时触发
			f.on('change',function(f,value){
				document.getElementById(element2).value=value;
    		this.collapse();
			});	
		}

		function loadFrm3(element1,element2,itemtype,itemvalue)
		{
			var url2 = "/depot/GeneralHandleSvt?reqType=utility.UtilActionHandler.showSelectCause&type="+itemtype;
			//,ajax.LibraryAH.select&backdoor=qJdBlE";
			var st = new Ext.data.JsonStore({
			    url:url2,
			    root: 'item',
			    fields: ['id','value']
			});
			
			var f = new Ext.form.ComboBox({
				store:st,
				value:itemvalue,
				valueField:'id',
				displayField:'value',
				typeAhead:true,
				mode: 'local',
				triggerAction: 'all',
				autoShow:'x-hide-display',
	  		selectOnFocus:true,
    		applyTo:element1,
    		onBlur:function(r,i,s){if(e1.value=="")e2.value="";//用于当输入框无值或删除输入框值候，清空隐藏对象的值
			this.store.each(function(D) {					   //用于输入字符不用点击选项或按钮，自动匹配相应项值
                if(D.data['value']==e1.value)e2.value=D.data['id'];
            });
			
			}
			});
			//增加侦听事件,当得到焦点时触发
			f.on('focus',function(f){

				//判断下拉中的总的数量
				if(st.getTotalCount()<=0){
					st.load();
				}    				
			});	
			//当改变值时触发
			f.on('change',function(f,value){
				document.getElementById(element2).value=value;
    		this.collapse();
			});	
		}		


		
