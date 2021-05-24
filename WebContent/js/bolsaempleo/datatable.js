/**
 * Datatable
 * @copyright ATISoluciones 2021
 */

function DataTable(id, config) {
    this.id = id;
    this.node = $(id);
    this.thead = $('tr', this.node).first();
    this.tbody = $('tbody', this.node).last();
    this.tfoot = $('tfoot', this.node).last();
    this.config = config;
    this.params = {
        'a': Atis.getProp(config, 'action', 'datatable'),
        'page': 0,
        'pageSize': Atis.getProp(config, 'pageSize', 10),
        'orderBy': Atis.getProp(config, 'defaultOrderBy', null),
        'orderDirection': Atis.getProp(config, 'defaultOrderDirection', 'asc'),
        'filter': ''
    };
    this.pageSizeOptions = Atis.getProp(config, 'pageSizeOptions', [5,10,20,100]);
    this.filterParams = {}
    this.title = Atis.getProp(config, 'title', undefined);
    this.lastResponse = null;
    this.checked = {};

    var self = this;

    this.refresh = function() {
    	self.loading(true);
    	$.ajax({
            async: Atis.getProp(self.config.ajax, 'async', true),
	        type: Atis.getProp(self.config.ajax, 'method', 'GET'),
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

    this.setTitle = function(tit) {
        self.title = tit;
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
    	var beforeRender = Atis.getProp(config, 'beforeRender');
        beforeRender && beforeRender(self);
    	
        // limpiamos
        $('tr', self.tbody).empty();
    	
    	if (response.data.length > 0) {
	    	response.data.forEach(function(row, index) {
	    		self.addRow(row, index);	    	    		    
	    	});
    	} else {
    		// sin resultados
    		$(self.tbody).append('<tr><td colSpan="'+ self.config.columns.length + '">Sin resultados</tr>');	
        }   

        // eventos after render
        var afterRender = Atis.getProp(config, 'afterRender');
        afterRender && afterRender(self);
    }

    this.errorResponse = function(err) {
    	self.loading(false);
    	
    	$('tr', self.tbody).hide();
        $(self.tbody).append('<p class="loading">Error: ' + Atis.getErrorResponse(err) + '</p>');
    };
    
    this.addRow = function(row, index) {
    	var tr = $('<tr id="' + this.id + '_row_' + index + '"></tr>');

        if (self.config.clickable) {
            $(tr).addClass('clickable');
        }

        if (self.config.clickable) {
            $(tr).on('click', self.config.clickable.onClick.bind($(tr), row, self));
        }

        row.selected = false;
        
        if (Array.isArray(self.config.selected)) {            
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
    	var value = Atis.getProp(row, data);

    	if (columnDef.hasOwnProperty('render')) {
    		return columnDef.render(row);
    	}

        if (columnDef.hasOwnProperty('overflow')) {
            var overflow;

            if (columnDef.overflow == 'auto') {
                overflow = $('<div></div>');
                overflow.addClass('overflow-auto');
            }

            return overflow.append(typeof value !== 'undefined' ? '' + value : '');
        }
    	
    	if (columnDef.hasOwnProperty('buttons')) {
    		var buttons = $('<span class="btns"></span>');
    		
    		columnDef.buttons.forEach(function(buttonDef) {
                var label = isFunction(buttonDef.label) ? buttonDef.label(row) : buttonDef.label;
                var title = isFunction(buttonDef.title) ? buttonDef.title(row) : buttonDef.title;
    			var btn = $('<button class="btn"' + (title ? 'title="' + title + '"' : '') + ' type="button">'+ (label ? label : '')+'</button>');
    			
    			if (buttonDef.hasOwnProperty('class')) {
    				$(btn).addClass(buttonDef.class);
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
    			$(buttons).append(btn);
    		});

    		return buttons;
    	}
    	
    	if (columnDef.hasOwnProperty('selectable') && columnDef.selectable) {
    		var check = $('<input type="checkbox"/>');

            if (row.selected) {
                if (columnDef.selectable.hasOwnProperty('disabled') && columnDef.selectable.disabled) {
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
                    checked ? selected.push(value) : Atis.removeValueArray(selected, value);
                }
                
    			self.checked[value] = checked;
                checked ? $(this).parent().parent().addClass("selected") : $(this).parent().parent().removeClass("selected");
    			self.renderFooter();

                if (columnDef.selectable.hasOwnProperty('onChange')) {
                    columnDef.selectable.onChange(row, this);
                }
    		});

            $(check).on('click', function(event) {
                event.stopPropagation();
            });

            if (columnDef.selectable.exclude && Atis.getProp(row, columnDef.selectable.exclude)) {
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
        $(pagination).append(' P&aacute;gina ' + (self.params.page + 1) + ' / ' + self.lastResponse.pagesTotal + ' ');
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
	            	var btn = $('<button class="btn"' + (action.title ? 'title="' + action.title + '"' : '') + ' type="button">' + action.label + '</button>');
	            	
	            	if (self.config.selected) {
	            		selected = self.config.selected;
	            	}

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
        // si es selectable check en la cabecera para marcar/desmacar todos
    	if (self.config.selectable && self.config.selectable.all != false) {
    		var check = $('<input type="checkbox"/>')
    		$(check).on('change', function() { self.checkUncheckAll(this.checked); });
    		$('th', self.thead).first().append(check);
    	}
    	
    	if(self.config.params) {
    		for(var param in self.config.params) {
    			this.setParam(param, self.config.params[param]);
    		}
    	}

        self.config.columns.forEach(function(columnDef, index) {
            var order = Atis.getProp(columnDef, 'order', {'active': true});
            var selectable = Atis.getProp(columnDef, 'selectable');
            var buttons = Atis.getProp(columnDef, 'buttons');

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

        var filterable = Atis.getProp(self.config, 'filterable', false);

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
    
    this.checkUncheckAll = function(check) {
    	if (self.lastResponse.data) {
			self.lastResponse.data.forEach(function (row) {
				var firstColumnDef = self.config.columns[0];
				var value = Atis.getProp(row, firstColumnDef.data);
				
				$("input[type='checkbox']", self.tbody).prop('checked', check);
				self.checked[value] = check;
                
                check ? $("tr", self.tbody).addClass("selected") : $("tr", self.tbody).removeClass("selected");
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
        
        if (value == "" || value == 0) {
            delete self.filterParams[indexColumnDef];
        }

        self.params.filter = JSON.stringify(self.filterParams);
        self.refresh();
    }
    
    this.prepareTable();
    this.refresh();
}

window.Atis = $.extend(window.Atis ? window.Atis : {}, {"DataTable": DataTable});
