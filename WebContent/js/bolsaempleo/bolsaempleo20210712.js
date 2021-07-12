/**
 * Utils y datatable.
 * https://codebeautify.org/jsviewer
 *
 * @copyright ATISoluciones 2021
 */

 function alertDialog(title, message, onOk) {
	$('<div class="atisDialog"></div>')
		.appendTo('body')
		.html(message)
		.dialog({
			modal: true,
			title: title,
			zIndex: 10000,
			autoOpen: true,
			width: 'auto',
			resizable: false,
			buttons: {
				Ok: function() {
					if (onOk) {
						if (onOk(this)) {
							$(this).dialog("close");
						}
					} else {
						$(this).dialog("close");
					}
				}
			},
			close: function(event, ui) {
				$(this).remove();
			}
	});
}

function confirmDialog(title, message, buttons) {
	$('<div></div>').appendTo('body')
	    	.html(message)
	    	.dialog({
		      modal: true,
		      title: title,
		      zIndex: 10000,
		      autoOpen: true,
		      width: 'auto',
		      resizable: false,
		      buttons: buttons,
		      close: function(event, ui) {
		        $(this).remove();
		      }
		});
}

function escapeHtml(str) {
    //return str.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/"/g, "&quot;").replace(/'/g, "&#039;");
    return str.replace(/</g, "&lt;").replace(/>/g, "&gt;");
}

 function formatearFecha(fecha) {
	var sin_hora = fecha.split(" ")[0];
	var sin_guiones = sin_hora.split("-");
	return sin_guiones[2] + "/" + sin_guiones[1] + "/" +sin_guiones[0];
}

function getErrorResponse(response) {
	try {
		var contentType = response.getResponseHeader('content-type').split(';')[0];		
		if (contentType == 'application/json') {					
			if (!response.responseText) {
				return getProp(response, 'status', '999') + ": respuesta vacia";
			}
			
			var error = json2Object(response.responseText);
			return getProp(error, 'descripcion', 'Sin definir');
		} else {
			return getProp(response, 'status', '999') + ": " + getProp(response, 'statusText', 'Sin definir');
		}     
	} catch (err) { 
		console.error("Error procesando error", response, err);
	}
}

function getProp( object, keys, defaultVal ){
	keys = Array.isArray( keys )? keys : keys.split('.');
	object = object[keys[0]];
	if( object && keys.length > 1 ){
		return getProp( object, keys.slice(1) );
	}
	return object === undefined? defaultVal : object;
}

function setProp( object, keys, val ){
	keys = Array.isArray( keys )? keys : keys.split('.');
	if( keys.length > 1 ){
		object[keys[0]] = object[keys[0]] || {};
		return setProp( object[keys[0]], keys.slice(1), val );
	}
	object[keys[0]] = val;
}

function isFunction(functionToCheck) {
	return functionToCheck && {}.toString.call(functionToCheck) === '[object Function]';
}


function isUndefined(myVar) {
	return typeof myVar === 'undefined'; 
}

function object2Json(object) { 
	return JSON.stringify(object);
}

function json2Object(json) {
	return JSON.parse(json);
}

function redondearFloat(number, decimalPlaces) {
	// https://medium.com/swlh/how-to-round-to-a-certain-number-of-decimal-places-in-javascript-ed74c471c1b8

	decimalPlaces = decimalPlaces || 2;
	return Number(Math.round(number + "e" + decimalPlaces) + "e-" + decimalPlaces);
}

function removeValueArray(array, val) {
	array = Array.isArray(array) ? array : [array];
	const index = array.indexOf(val);
	if (index > -1) {
		array.splice(index, 1);
	}	
}

function sendAjax(url, params, success, error) {
	$.ajax({
		type: 'GET',
		url: url,
		contentType: "application/json",
		dataType: "json",
		data: params,
		success: success,
		error: error
	});
}

function sendForm(url, params) {
	var form = document.createElement("form");		
	form.method = "POST";
	form.action = url;
	document.body.appendChild(form);
		
	for(key in params) {
		var element = document.createElement("input");
		element.type = "hidden";
		element.name = key;
		element.value = params[key];
		form.appendChild(element);
	}
			
	form.submit();
}

function smoothScrollFromOneToAnotherAnchor(origin, destination) {
	window.scrollTo(0,$(id).offset().top);
}

function smoothScrollToAnchor(id) {
	window.scrollTo(0,$(id).offset().top);
}


/* Datatable */

function DataTable(id, config) {
    this.id = id;
    this.idFinal = id.substring(1);
    this.node = $(id);
    this.thead = $('tr', this.node).first();
    this.tbody = $('tbody', this.node).last();
    this.tfoot = $('tfoot', this.node).last();    
    this.config = config;
    this.params = {
        'a': getProp(config, 'action', 'datatable'),
        'page': 0,
        'pageSize': getProp(config, 'pageSize', 10),
        'orderBy': getProp(config, 'defaultOrderBy', null),
        'orderDirection': getProp(config, 'defaultOrderDirection', 'asc'),
        'filter': ''
    };
    this.pageSizeOptions = getProp(config, 'pageSizeOptions', [5,10,20,100]);
    this.filterParams = {}
    this.title = getProp(config, 'title', undefined);
    this.lastResponse = null;
    this.checked = {};

    var self = this;

    this.refresh = function() {
    	self.loading(true);
    	$.ajax({
            async: getProp(self.config.ajax, 'async', true),
	        type: getProp(self.config.ajax, 'method', 'GET'),
	        url: self.config.ajax.url,
	        contentType: "application/json",
	        dataType: "json",
	        data: self.params,
	        success: self.parseResponse.bind(self),
            error: self.errorResponse.bind(self)
	    });
    };
    
    this.setParam = function(key, value) {
    	self.params[key] = value;
    };

    this.getParam = function(key) {
    	return self.params[key];
    };

    this.setFilter = function(key, value) {
        self.filterParams[key] = value;
    };

    this.setTitle = function(tit) {
        self.title = tit;
    };
    
    this.getRow = function(codNum) {
    	if (self.lastResponse && self.lastResponse.data) {
            for(var i=0; i<self.lastResponse.data.length; i++) {
            	if (self.lastResponse.data[i].codNum == codNum) {
                    return self.lastResponse.data[i];
                }
            }
        }
        return null;
    };
        
    this.loading = function(on) {
    	if (on) {
    		$('tr', self.tbody).hide();
    		$(self.tbody).append('<p class="loading"><img src="/img/intranet/ajax-loader.gif"/> Loading...</p>');
    	} else {
    		$('p.loading', self.tbody).remove();
    		$('tr', self.tbody).show();    		
    	}
    };
    
    this.parseResponse = function(response) {
    	self.lastResponse = response;

        self.loading(false);
    	self.checkUncheckAll(false);
    	self.renderFooter();
        self.renderHeader();
    	
    	// evento before render
    	var beforeRender = getProp(config, 'beforeRender');
        beforeRender && beforeRender(self);
    	
        // limpiamos
        $(self.tbody).empty();
    	
    	if (response.data.length > 0) {
	    	response.data.forEach(function(row, index) {
	    		self.addRow(row, index);	    	    		    
	    	});
    	} else {
    		// sin resultados
    		$(self.tbody).append('<tr id="' + this.idFinal + '_row_0"><td colSpan="'+ self.config.columns.length + '">Sin resultados</tr>');	
        }   

        // eventos after render
        var afterRender = getProp(config, 'afterRender');
        afterRender && afterRender(self);
    }

    this.errorResponse = function(err) {
    	self.loading(false);
    	
    	$('tr', self.tbody).hide();
        $(self.tbody).append('<p class="loading">Error: ' + getErrorResponse(err) + '</p>');
    };
    
    this.addRow = function(row, index) {
    	var tr = $('<tr id="' + this.idFinal + '_row_' + index + '"></tr>');

        if (self.config.clickable) {
            $(tr).addClass('clickable');
        }

        if (self.config.clickable) {
            $(tr).on('click', function () {
                if (window.getSelection && window.getSelection().toString() == '') {
                self.config.clickable.onClick(row, self);
                }
            });
        }

        row.selected = false;
        
        if (self.config.selectedAll) {
        	if (!Array.isArray(self.config.selected)) {
        		self.config.selected = [];
        	}        	      
        	self.config.selected.push(row.codNum);
        	row.selected = true;
        } else if (Array.isArray(self.config.selected)) {
        	self.config.selected.forEach(function (element) {
                if (element === row.codNum) {
                    row.selected = true;
                }
            });
        }

        if (row.selected) {
            $(tr).addClass('selected');
        }
    	
    	self.config.columns.forEach(function(columnDef) {
    		var td = $('<td></td>');
    		
    		if (columnDef.hasOwnProperty('class')) {
	    		$(td).addClass(columnDef.class);
	    	}
    		
    		$(td).append(self.renderCol(row, columnDef));
    		$(tr).append(td);
        });
    	
    	$(self.tbody).append(tr);
    };
    
    this.renderCol = function(row, columnDef) {
    	var data = columnDef.data;
    	var value = getProp(row, data);

    	if (columnDef.hasOwnProperty('overflow')) {
            var overflow = $('<div></div>');

            if (columnDef.overflow == 'auto') {
                overflow.addClass('overflow-auto');
            }

            return overflow.append(
                columnDef.hasOwnProperty('render') ? columnDef.render(row) : typeof value !== 'undefined' ? '' + value : ''
            );
        }

        if (columnDef.hasOwnProperty('render')) {
    		return columnDef.render(row);
    	}
    	
    	if (columnDef.hasOwnProperty('renderBoolean')) {
    		var condition = row[columnDef.data]; 
    		var title = condition ? Atis.getProp(columnDef.renderBoolean, 'true', '') : Atis.getProp(columnDef.renderBoolean, 'false', '');     		    
    		return '<div class="circle-' + (condition ? 'true' : 'false') + '" title="' + title + '"></div>';    		
    	}
    	
    	if (columnDef.hasOwnProperty('buttons')) {
    		var buttons = $('<span class="btns"></span>');
    		
    		columnDef.buttons.forEach(function(buttonDef) {
                var label = isFunction(buttonDef.label) ? buttonDef.label(row) : buttonDef.label;
                var title = isFunction(buttonDef.title) ? buttonDef.title(row) : buttonDef.title;
                var visible = isFunction(buttonDef.visible) ? buttonDef.visible(row) : !isUndefined(buttonDef.visible) ? buttonDef.visible : true;
                
                if (visible) {
                	var btn = $('<button class="btn"' + (title ? 'title="' + title + '"' : '') + ' type="button">'+ (label ? label : '')+'</button>');
    			
	    			if (buttonDef.hasOwnProperty('class')) {
	    				$(btn).addClass(isFunction(buttonDef.class) ? buttonDef.class(row) : buttonDef.class);
	    			}
	
	                if (buttonDef.hasOwnProperty('icon')) {
	                    var position = buttonDef.icon.position ? buttonDef.icon.position : 'left';
	                    var icon = $('<i class="' + buttonDef.icon.class + '"></i>');
	                    if (position == 'left') {
	                        btn.prepend(icon);
	                    } else {
	                        btn.append(icon);
	                    }
	                }
	    			
	    			$(btn).on('click', buttonDef.onClick.bind($(btn), row, self));
	                $(btn).on('click', event => event.stopPropagation());
	    			$(buttons).append(btn);
                } else {
                    if (buttonDef.hasOwnProperty('renderNotVisible')) {
                        $(buttons).append(buttonDef.renderNotVisible(row));
                    }
                }
    		});

    		return buttons;
    	}
    	
    	if (columnDef.hasOwnProperty('selectable') && columnDef.selectable) {
    		var check = $('<input type="checkbox"/>');

            if (row.selected) {
                if (columnDef.selectable.hasOwnProperty('disabled') && columnDef.selectable.disabled == row.codNum) {
                    $(check).attr("disabled", true);
                }
                typeof value !== 'undefined' ? self.checked[value] = true : '';
                check.prop('checked', true);
                self.renderFooter();
            }
    		
    		$(check).on('change', function() {
                var checked  = this.checked;
                var selected = self.config.selected;

                if (selected) {
                    checked ? selected.push(value) : removeValueArray(selected, value);
                }
                
                typeof value !== 'undefined' ? self.checked[value] = checked : '';
                checked ? $(this).parent().parent().addClass("selected") : $(this).parent().parent().removeClass("selected");
    			self.renderFooter();

                if (columnDef.selectable.hasOwnProperty('onChange')) {
                    columnDef.selectable.onChange(row, this);
                }
    		});

            $(check).on('click', function(event) {
                event.stopPropagation();
            });

            if (columnDef.selectable.exclude && getProp(row, columnDef.selectable.exclude)) {
                $(check).prop('disabled', true);
            }
    		
    		return check;
    	}

    	return typeof value !== 'undefined' ? '' + value : '';
    };
    
    this.renderFooter = function() {
    	if (!self.lastResponse) { return ''; }
    	
        // texto total
        var selected = self.getCheckedItems();
        var labelTotal = self.lastResponse.recordsTotal ? 'Total ' + self.lastResponse.recordsTotal : 'Total 0';
        var labelSelected = selected.length > 0 ? ' (Seleccionados ' + selected.length + ')' : '';
        var textoTotal = '<span class="total">' + labelTotal + labelSelected +'</span>';
        
        // pagination
        var btnFirst = $('<button class="btn first" title="Ir a la primera p&aacute;gina" type="button">&lt;&lt;</button>');
        var btnBack = $('<button class="btn back" title="Ir a la p&aacute;gina anterior" type="button">&lt;</button>');
        var btnNext = $('<button class="btn next" title="Ir a la p&aacute;gina siguiente" type="button">&gt;</button>');
        var btnLast = $('<button class="btn last" title="Ir a la &uacute;ltima p&aacute;gina" type="button">&gt;&gt;</button>');

        if (self.lastResponse.pagesTotal > 1) {
            $(btnFirst).on("click", function() { self.paginateFirst(); });
            $(btnBack).on("click", function() { self.paginationPrevious(); });
            $(btnNext).on("click", function() { self.paginationNext(); });
            $(btnLast).on("click", function() { self.paginationLast(); });
        } else {
            $(btnFirst).prop("disabled",true);
            $(btnBack).prop("disabled",true);
            $(btnNext).prop("disabled",true);
            $(btnLast).prop("disabled",true);
        }
        
        var pagination = $('<span class="pagination"></span>');
        $(pagination).append(btnFirst);
        $(pagination).append(btnBack);
        $(pagination).append(' P&aacute;gina ' + (self.lastResponse.pagesTotal > 0 ? self.params.page + 1 : 0) + ' / ' + self.lastResponse.pagesTotal + ' ');
        $(pagination).append(btnNext);
        $(pagination).append(btnLast);

        // page size select
        var selectSize = $('<select></select>');
        selectSize.prop('title', 'Cambiar tama\u00f1o de p\u00e1gina');

        self.pageSizeOptions.forEach(function(option) {
            var option = $('<option ' + (self.params.pageSize == option ? 'selected' : '') + '>' + option + '</option>');
            selectSize.append(option);
        });

        selectSize.on('change', function() {
            self.params.pageSize = this.value;
            self.params.page = 0;
            self.refresh();
        });

        var pageSize = $('<span class="page-size"></span>');
        $(pageSize).append(selectSize);

        // acciones
        var actions = $('<span class="actions"></span>');
        if (self.config.actions) {
            for (var i = 0; i < self.config.actions.length; i++) {
            	var action = self.config.actions[i];     
            	
            	if (action.showWhenSelected == null || action.showWhenSelected == (self.config.selected > 0)) {
            		if (self.config.selected) {
	            		selected = self.config.selected;
	            	}
	            	
            		var label = Atis.isFunction(action.label) ? action.label.bind(self, selected)() : action.label;
                	var title = action.title ? (Atis.isFunction(action.title) ? action.title.bind(self, selected)() : action.title) : '';
	            	var btn = $('<button class="btn" title="' + title + '" type="button">' + label + '</button>');
	            	
                    if (action.hasOwnProperty('class')) {
                        $(btn).addClass(action.class);
                    }                    

                    if (action.hasOwnProperty('icon')) {
                        var position = action.icon.position ? action.icon.position : 'left';
                        var icon = $('<i class="' + action.icon.class + '"></i>');
                        if (position == 'left') {
                            btn.prepend(icon);
                        } else {
                            btn.append(icon);
                        }
                    }
	            	
	            	$(btn).on("click", action.onClick.bind(self, selected));
	            	$(actions).append(btn);
	            }
            }
        }

        $('th', self.tfoot).empty();
    	$('th', self.tfoot).append($(pagination));
        $('th', self.tfoot).append($(pageSize));
        $('th', self.tfoot).append($(actions));
        $('th', self.tfoot).append($(textoTotal));
    };

    this.renderHeader = function(row) {
        if (self.title) {
            var header = $('<caption>' + self.title + '</caption>');

            if (self.config.dropdown) {
                var dropdown = $('<div class="dropdown"></div>');
                var arrow = $('<img class="show" src="/img/iconos/down.png" />');

                dropdown.on('click', function() {
                    $(self.node).children('tbody').toggle('fast');
                    $('th span:lt(3)', self.tfoot).toggle('fast');
                    $(this).find('img').toggleClass('show');
                });

                dropdown.append(arrow);
                header.append(dropdown);
            }
            
            $(self.node).find('caption').remove();
            $(self.node).prepend(header);
        }
    };
    
    this.prepareTable = function() {
    	$(this.thead).addClass('header');
    
        // si es selectable check en la cabecera para marcar/desmacar todos
    	if (self.config.selectable && self.config.selectable.all != false) {
    		var check = $('<input type="checkbox"/>')
    		$(check).on('change', function() { self.checkUncheckAll(this.checked, true); });
    		$('th', self.thead).first().append(check);
    	}
    	
    	if(self.config.params) {
    		for(var param in self.config.params) {
    			this.setParam(param, self.config.params[param]);
    		}
    	}

        self.config.columns.forEach(function(columnDef, index) {
            var order = getProp(columnDef, 'order', {'active': true});
            var selectable = getProp(columnDef, 'selectable');
            var buttons = getProp(columnDef, 'buttons');

            columnDef.node = $('th', self.thead).get(index);
            
            if  (selectable || buttons) {
                return;
            }
            
            if (order.active) {
                if(self.params.orderBy != null && self.params.orderBy == index) {
                    $(columnDef.node).prepend('<img src="/img/iconos/' + (self.params.orderDirection === 'asc' ? 'down.png' : 'up.png') + '" class="order"/>');
                }

                $(columnDef.node).prop('title', 'Ordenar por ' + $(columnDef.node).text());
                $(columnDef.node).css('cursor', 'pointer');
                $(columnDef.node).on('click', function() { self.orderBy(columnDef, index); });
            }
        });

        var filterable = getProp(self.config, 'filterable', false);

        if (filterable && !$('tbody', this.node).find('.filterable').length) {
            var tr = $('<tr class="filterable"></tr>');
            self.config.columns.forEach(function(columnDef, index) {
                var th = $('<th></th>');

                if (columnDef.filter) {
                    var filterElement;
                    var type = columnDef.filter.type ? columnDef.filter.type : 'text';
                    var name = '" name="' + columnDef.data + '"';
                    switch(type) {
                        case 'selectBoolean':
                            filterElement = $('<select ' + name + '></select>');
                            filterElement.append($('<option value="0">-----</option>'));

                            if (columnDef.filter.true) {
                                filterElement.append($('<option value="true" title="' + columnDef.filter.true + '" ' + (columnDef.filter.optionDefault == "true" ? ' selected' : '') + '>' + columnDef.filter.true + '</option>'));
                            } else {
                                filterElement.append($('<option>' + true + '</option>'));
                            }

                            if (columnDef.filter.false) {
                                filterElement.append($('<option value="false" title="' + columnDef.filter.true + '"' + (columnDef.filter.optionDefault == "false" ? ' selected' : '') + '>' + columnDef.filter.false + '</option>'));
                            } else {
                                filterElement.append($('<option>' + false + '</option>'));
                            }
                            
                            if (columnDef.filter.optionDefault) {
                                self.filterParams[index] = $(filterElement).val();
                                self.params.filter = JSON.stringify(self.filterParams);
                            }
                            
                            break;
                        case 'select':
                            var options = columnDef.filter.options;
                            filterElement = $('<select ' + name + '></select>');
                            filterElement.append($('<option value="0">----------</option>'));

                            if (Array.isArray(options)) {
                                options.forEach(function (option, index) {
                                    filterElement.append($('<option>' + option + '</option>'));
                                });
                            } else {
                                for (option in options) {
                                    if (columnDef.filter.optionDefault && columnDef.filter.optionDefault == option) {
                                        filterElement.append($('<option value="' + option + '" selected>' + options[option] + '</option>'));
                                    } else {
                                        filterElement.append($('<option value="' + option + '">' + options[option] + '</option>'));
                                    }
                                }
                            }

                            if (columnDef.filter.optionDefault) {
                                self.filterParams[index] = $(filterElement).val();
                                self.params.filter = JSON.stringify(self.filterParams);
                            }
                            
                            break;
                        default:
                            filterElement = $('<input type="' + type + name + ' autocomplete="off" />');
                            $(filterElement).bind("enterKey", e => self.filterBy(index, this.value));
                            break;
                    }

                    $(filterElement).on('change', function() {
                        self.filterBy(index, this.value);
                    });

                    th.append(filterElement);
                }

                tr.append(th);
            });

            $('tbody', this.node).first().children().eq(1).remove();
            $('tbody', this.node).first().append(tr);
            $('tbody', this.node).find('input[type="date"]').each(function() {
               	this.type = 'text';
                $(this).datepicker();
            });
        }

        if (self.config.selected) {
            if (!Array.isArray(self.config.selected)) {
                self.config.selected = [self.config.selected];
            }

            self.config.selected.forEach(function (id) {
                self.checked[id] = true;
            });
        }
    };
    
    this.checkUncheckAll = function(check, changeSelected) {
    	if (self.lastResponse.data) {
    	
    		if (changeSelected && self.config.selected) {
    			self.config.selected = [];
    		}
    	
			self.lastResponse.data.forEach(function (row) {
				var firstColumnDef = self.config.columns[0];
				var value = getProp(row, firstColumnDef.data);
				
				$("input[type='checkbox']", self.tbody).prop('checked', check);
				self.checked[value] = check;
                
                check ? $("tr", self.tbody).addClass("selected") : $("tr", self.tbody).removeClass("selected");
                
                if (changeSelected && self.config.selected && check) {
                	self.config.selected.push(row.codNum);
                }                              
			});
		}
		
		self.renderFooter();
    };
    
    this.getCheckedItems = function() {
    	var checked = [];
	    Object.entries(self.checked).forEach(function([key, value]) {
	    	if (value) {
	    		checked.push(parseInt(key, 10))
	    	}	    	
	    });  	    
	    return checked  	
    };

    this.paginationNext = function() {
        if ((self.params.page + 1) < self.lastResponse.pagesTotal) {
            self.params.page++;
            self.refresh();
        }
    };

    this.paginationLast = function() {
        if ((self.params.page + 1) < self.lastResponse.pagesTotal) {
            self.params.page = self.lastResponse.pagesTotal - 1;
            self.refresh();
        }
    };

    this.paginationPrevious = function() {
        if (self.params.page > 0) {
            self.params.page--;
            self.refresh();
        }
    };

    this.paginateFirst = function() {
        if (self.params.page > 0) {
            self.params.page = 0;
            self.refresh();
        }
    };

    this.orderBy = function(columnDef, indexColumnDef) {
        self.config.columns.forEach(function(columnDef) {
            $('img.order', columnDef.node).remove();
        });
        
        if (self.params.orderBy === indexColumnDef) {
            self.params.orderDirection = self.params.orderDirection === 'asc' ? 'desc' : 'asc';
        } else {
            self.params.orderDirection = 'asc';
        }
        
        self.params.orderBy = indexColumnDef;
        $(columnDef.node).prepend('<img src="/img/iconos/' + (this.params.orderDirection === 'asc' ? 'down.png' : 'up.png') + '" class="order"/>');
        
        self.refresh();
    }

    this.filterBy = function(indexColumnDef, value) {
        self.filterParams[indexColumnDef] = value;
        self.params.page = 0;
        
        if (self.config.selectedAll) {
        	self.config.selected = [];
        }
                
        if (value == "" || value == 0) {
            delete self.filterParams[indexColumnDef];
        }

        self.params.filter = JSON.stringify(self.filterParams);
        self.refresh();
    }
    
    this.prepareTable();
    this.refresh();
}


window.Atis = {
	"getProp": getProp,
	"setProp": setProp,
	"alertDialog": alertDialog,
	"confirmDialog": confirmDialog,
	"getErrorResponse": getErrorResponse,
	"isUndefined": isUndefined,
	"isFunction": isFunction,
	"formatearFecha": formatearFecha,	
	"sendForm": sendForm,
	"object2Json": object2Json,
	"json2Object": json2Object,
	"sendAjax": sendAjax,
	"removeValueArray": removeValueArray,
	"escapeHtml": escapeHtml,
	"redondearFloat": redondearFloat,
	"smoothScrollToAnchor": smoothScrollToAnchor,
	"smoothScrollFromOneToAnotherAnchor": smoothScrollFromOneToAnotherAnchor,
    "DataTable": DataTable
};