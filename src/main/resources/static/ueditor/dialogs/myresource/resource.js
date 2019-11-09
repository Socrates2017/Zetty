/**
 * User: Jinqn
 * Date: 14-04-08
 * Time: 下午16:34
 * 上传文件对话框逻辑代码,包括tab: 远程文件/上传文件/在线文件/搜索文件
 */

(function () {

    var resourceInfo;
    var tree;
    var ctx = "/brm";
    var URL_MIDDELOPEN =  ctx + "/view/middelopen?attid=";
    var URL_THUMBOPEN =  ctx + "/view/thumbopen?attid=";
    var URL_VIEWFILE =  ctx + "/view/viewfile?attid=";
    var FILE_CATEGORY = 'Image,File,Video';
    
    window.onload = function () {
    	
    	resourceInfo = resourceInfo || new ResourceInfo();
    	resourceInfo.initData();
    	
        initAlign();
        initButtons(); 
        initTree();
    };

    /* 初始化onok事件 */
    function initButtons() {

        dialog.onok = function () {
            var remote = false, list = [], id;
            list = resourceInfo.getInsertList();
            remote = true;
            if(list) {
            	var imgList = []
            		,vodList = []
            		,fileList = [];
            	for(var i=0,j=list.length;i<j;i++){
            		var id = list[i].id;
            		var alt = list[i].alt;
            		var align = list[i].floatStyle;
            		
                    if('Image'==list[i].type){
                    	var url = URL_MIDDELOPEN  + id;
                    	imgList.push({src:url,alt:alt,floatStyle:align});
                    }
                    else if('Video'==list[i].type){
                    	var url = URL_VIEWFILE  + id;
                    	vodList.push({url:url,alt:alt,floatStyle:align});
                    }
                    else if('File'==list[i].type){
                    	var url = URL_VIEWFILE  + id;
                    	url += "&alt=" + alt;
                    	fileList.push({title:alt,url:url});
                    }
            	}
                if(imgList.length>0){
                	editor.execCommand('insertimage', imgList);                	
                }
                if(vodList.length>0){
                	editor.execCommand('insertvideo', vodList);                	
                }
                if(fileList.length>0){
                	editor.execCommand('insertfile', fileList);                	
                }//remote && editor.fireEvent("catchRemoteImage");
            }
        };
    }
    

    /* 初始化类别树 */
    function initTree(){
    	var url = ctx + "/config/level-paramenter!treeXml.do?&entityName=config.ResourceType&propertyName=&rootId=all&cache=1.5&keyword=";
    	
    	$('#application-tree').html('');
        tree=new dhtmlXTreeObject("application-tree","100%","100%",0);
    	tree.loadXML(url,afterCallTree);
    	tree.setOnClickHandler(onNodeSelect);
     }
    
    function afterCallTree(){
    	//查询类别数量
    	var param={};
   		param.category = FILE_CATEGORY;//'Image,File,Video'
   		queryTypeQuantity(param);
    }
    
    //查询对应类别的数量
    function queryTypeQuantity(param){
    	for(var prop in param){
    		$('#'+prop).val(param[prop]);
    	}
    	new $.ajax({
    		url:ctx+'/resource/res-use-file!queryTypeQuantity.do',
    		type : 'POST',
    		data : param,
    		complete : function(xhr){
    			var jsons = xhr.responseText;
    			var arrData = eval('(' + jsons + ')');
    			for(var i=0,k=arrData.length;i<k;i++){
    				var data = arrData[i];
    				var treeId = data[0];
    				var quan = data[1];
    				
    				var text = tree.getItemText(treeId);
    				if(text){
    					text = text.replace(/\(\d+\)/,"");
    					if(quan>0){
    						tree.setItemText(treeId,text+'('+quan+')');
    					}else{
    						tree.setItemText(treeId,text);
    					}
    				}
    			}
    			
    			//查询对应的资源信息
    			queryResinfo(param);
    		}
    	});	
    }
    

  //所有资源ID集合
  var resIds = [];
  function queryResinfo(param){
  	var itemActionHandler = onNodeSelect;
  	var image1 = "tombs.gif";//图标

  	//先清除原节点
  	while(resIds.length>0){
  		tree.deleteItem(resIds.pop());
  	}
  	
  	new $.ajax({
  		url:ctx + '/resource/res-use-file!queryResinfo.do',
  		type : 'POST',
  		data : param,
  		complete : function(xhr){
  			var jsons = xhr.responseText;
  			var arrData = eval('(' + jsons + ')');
  			for(var i=0,k=arrData.length;i<k;i++){
  				var data = arrData[i];
  				var parentId = data[0];//当前类别id
  				var itemId = data[1];//资源id
  				var quan = data[3];//数量
  				var itemText = data[2]+"("+quan+")";//资源名称
  				
  				resIds.push(itemId);
  				var text = tree.getItemText(itemId);
  				if(text){
  					tree.setItemText(itemId,itemText);
  				}
  				else{
  					//tree.deleteItem(itemId);
  					tree.insertNewChild(parentId,itemId,itemText,itemActionHandler,image1);
  					//,image2,image3,optionStr,children)				
  				}
  			}			
  			//
  			//findTreeNode(param.refreshTree);
  		}
  	});	
  }    

  //点击节点
  function onNodeSelect(id){
		//focusTreeNode(id);	
		//var url = getFileListUrl()+curLocationId;
		//Ext.get('application-info-iframe').dom.src=url;
	  resourceInfo.getResourceData(id);
	}
  
    /* 初始化对其方式的点击事件 */
    function initAlign(){
        /* 点击align图标 */
        domUtils.on($G("alignIcon"), 'click', function(e){
            var target = e.target || e.srcElement;
            if(target.className && target.className.indexOf('-align') != -1) {
                setAlign(target.getAttribute('data-align'));
            }
        });
    }

    /* 设置对齐方式 */
    function setAlign(align){
        align = align || 'none';
        var aligns = $G("alignIcon").children;
        for(i = 0; i < aligns.length; i++){
            if(aligns[i].getAttribute('data-align') == align) {
                domUtils.addClass(aligns[i], 'focus');
                $G("align").value = aligns[i].getAttribute('data-align');
            } else {
                domUtils.removeClasses(aligns[i], 'focus');
            }
        }
    }
    /* 获取对齐方式 */
    function getAlign(){
        var align = $G("align").value || 'none';
        return align == 'none' ? '':align;
    }

    /*资源文件 */
    function ResourceInfo() {
        this.init();
    }
    ResourceInfo.prototype = {
        init: function () {
            this.initEvents();
        },
        initEvents: function(){
            var _this = this;
            /* 选中文件 */
            domUtils.on($G('searchList'), 'click', function(e){
                var target = e.target || e.srcElement,
                    li = target.parentNode.parentNode;

                if (li.tagName.toLowerCase() == 'li') {
                    if (domUtils.hasClass(li, 'selected')) {
                        domUtils.removeClasses(li, 'selected');
                    } else {
                        domUtils.addClass(li, 'selected');
                    }
                }
            });
        },
        /* 初始化第一次的数据 */
        initData: function () {

            /* 拉取数据需要使用的值 */
            //this.state = 0;
            //this.listSize = editor.getOpt('imageManagerListSize');
            //this.listIndex = 0;
            //this.listEnd = false;

            /* 第一次拉取数据 */
            this.getResourceData('');
        },
        encodeToGb2312:function (str){
            return str;
        },
        /* 改变文件大小 */
        scale: function (img, w, h) {
            var ow = img.width,
                oh = img.height;

            if (ow >= oh) {
                img.width = w * ow / oh;
                img.height = h;
                img.style.marginLeft = '-' + parseInt((img.width - w) / 2) + 'px';
            } else {
                img.width = w;
                img.height = h * oh / ow;
                img.style.marginTop = '-' + parseInt((img.height - h) / 2) + 'px';
            }
        },
        getResourceData: function(tid){
            var _this = this,
            url = ctx + "/resource/res-use-file!medialist.do?"+new Date;

            $G('searchListUl').innerHTML = lang.searchLoading;
            
            var param={typeId:tid};
            param.category = FILE_CATEGORY;//'Image,File,Video'
            
            new $.ajax({
            	url : url,
        		type : 'POST',
        		data : param,
        		complete : function(xhr){
        			var json = xhr.responseJSON;
        			var list = [];
                    if(json) {
                        for(var i = 0; i < json.length; i++) {
                        	var attid = json[i].oid;
                            if(attid) {
                            	var cag = json[i].category;
                            	var src = URL_THUMBOPEN  + attid;
                                list.push({
                                	id: attid,
                                    title: json[i].title,
                                    src: src,
                                    category: cag,
                                    url: URL_VIEWFILE + attid
                                });
                            }
                        }
                    }
                    _this.setList(list);
        		},
                error:function(){
                    $G('searchListUl').innerHTML = lang.searchRetry;
                }
            });
            
        },
        /* 添加文件到列表界面上 */
        setList: function (list) {
            var i, item, p, img, link, _this = this,
                listUl = $G('searchListUl');

            listUl.innerHTML = '';
            if(list.length) {
                for (i = 0; i < list.length; i++) {
                    item = document.createElement('li');
                    p = document.createElement('p');
                    img = document.createElement('img');
                    link = document.createElement('a');

                    img.onload = function () {
                        _this.scale(this, 113, 113);
                    };
                    img.width = 113;
                    img.setAttribute('id', list[i].id);
                    img.setAttribute('src', list[i].src);
                    //img.setAttribute('src', list[i].url);
                    img.setAttribute('title', list[i].title);

                    link.href = list[i].url;
                    link.target = '_blank';
                    link.title = list[i].title;
                    link.innerHTML = list[i].title;

                    p.appendChild(img);
                    item.appendChild(p);
                    item.appendChild(link);
                    
                    item.type=list[i].category;
                    
                    listUl.appendChild(item);
                }
            } else {
                listUl.innerHTML = lang.searchRetry;
            }
        },
        getInsertList: function () {
            var child,
                src,
                align = getAlign(),
                list = [],
                items = $G('searchListUl').children;
            for(var i = 0; i < items.length; i++) {
                child = items[i].firstChild && items[i].firstChild.firstChild;
                if(child.tagName && child.tagName.toLowerCase() == 'img' 
                	&& domUtils.hasClass(items[i], 'selected')) {
                	id = child.id;
                    src = child.src;
                	title = child.title;
                    list.push({
                    	id: id,
                        src: src,
                        _src: src,
                        //alt: src.substr(src.lastIndexOf('/') + 1),
                        alt: title,
                        type: items[i].type,
                        floatStyle: align
                    });
                }
            }
            return list;
        }
    };

})();
