	//用于 ext 的 tip 提示 输入框@
		function loadFrm(element1,element2,itemtype)
		{

			//alert("捡来了");
			var url2 = "/depot/GeneralHandleSvt?reqType=utility.UtilActionHandler.showSelectCause&type="+itemtype;
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
		}



		function loadFrm2(element1,element2,itemtype)
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
				valueField:'id',
				displayField:'value',
				typeAhead:true,
				mode: 'local',
				triggerAction: 'all',
				autoShow:'x-hide-display',
	  		selectOnFocus:true,
    		applyTo:element1
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
    		applyTo:element1
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


		
