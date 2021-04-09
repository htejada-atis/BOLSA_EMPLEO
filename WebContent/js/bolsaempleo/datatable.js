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
        'orderBy': null,
        'orderDirection': 'asc'
    };
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
    }

    this.errorResponse = function(err) {
    	self.loading(false);
    	
    	$('tr', self.tbody).hide();
        $(self.tbody).append('<p class="loading">Error: ' + Atis.getErrorResponse(err) + '</p>');
    };
    
    this.addRow = function(row, index) {
    	var tr = $('<tr id="' + this.id + '_row_' + index + '"></tr>');

        if (self.config.clickable || self.config.selectable) {
            $(tr).addClass("clickable");
        }

        if (self.config.clickable) {
            $(tr).on("click", self.config.clickable.onClick.bind($(tr), row, self));
        }
        
        if (self.config.selected && self.config.selected == row.codNum) {
        	$(tr).addClass("selected");
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
    	
    	if (columnDef.hasOwnProperty('buttons')) {
    		var buttons = $('<span class="btns"></span>');
    		
    		columnDef.buttons.forEach(function(buttonDef) {
                var label = isFunction(buttonDef.label) ? buttonDef.label(row) : buttonDef.label;
    			var btn = $('<button class="btn" type="button">'+label+'</button>');
    			
    			if (buttonDef.hasOwnProperty('class')) {
    				$(btn).addClass(buttonDef.class);
    			}
    			
    			$(btn).on("click", buttonDef.onClick.bind($(btn), row, self));
    			$(buttons).append(btn);
    		});

    		return buttons;
    	}
    	
    	if (columnDef.hasOwnProperty('selectable') && columnDef.selectable) {
    		var check = $('<input type="checkbox"/>')
    		
    		$(check).on('change', function() {
    			self.checked[value] = this.checked;
    			self.renderFooter();    			
    		});
    		
    		return check;
    	}

    	return typeof value !== 'undefined' ? '' + value : '';    	
    };
    
    this.renderFooter = function() {
    	if (!self.lastResponse) { return ''; }    	
    	
        // texto total
        var selected = self.getCheckedItems();  
        var labelTotal = 'Total ' + self.lastResponse.recordsTotal;
        var labelSelected = selected.length > 0 ? ' (Seleccionados ' + selected.length + ')' : ''
        var textoTotal = '<span class="total">' + labelTotal + labelSelected +'</span>';
        
        // pagination
        var btnFirst = $('<button class="btn first" type="button">&lt;&lt;</button>');
        $(btnFirst).on("click", function() { self.paginateFirst(); });
        var btnBack = $('<button class="btn back" type="button">&lt;</button>');
        $(btnBack).on("click", function() { self.paginationPrevious(); });
        var btnNext = $('<button class="btn next" type="button">&gt;</button>');
        $(btnNext).on("click", function() { self.paginationNext(); });
        var btnLast = $('<button class="btn last" type="button">&gt;&gt;</button>');
        $(btnLast).on("click", function() { self.paginationLast(); });

        var pagination = $('<span class="pagination"></span>');
        $(pagination).append(btnFirst);
        $(pagination).append(btnBack);
        $(pagination).append(' P&aacute;gina ' + (self.params.page + 1) + ' / ' + (self.lastResponse.pagesTotal + 1) + ' ');
        $(pagination).append(btnNext);
        $(pagination).append(btnLast);

        // acciones
        var actions = $('<span class="actions"></span>');
        if (self.config.actions) {
            for (var i = 0; i < self.config.actions.length; i++) {
            	var action = self.config.actions[i];
            	if (action.showWhenSelected == null || action.showWhenSelected == (self.config.selected > 0)) {
	            	var btn = $('<button class="btn" type="button">' + action.label + '</button>');
	            	
	            	if (self.config.selected) {
	            		selected = self.config.selected;
	            	}
	            	
	            	$(btn).on("click", action.onClick.bind(self, selected));
	            	$(actions).append(btn);
	            }
            }
        }

        $('th', self.tfoot).empty();
    	$('th', self.tfoot).append($(pagination));
        $('th', self.tfoot).append($(actions));
        $('th', self.tfoot).append($(textoTotal));
    };
    
    this.prepareTable = function() {
        // si es selectable check en la cabecera para marcar/desmacar todos
    	if (self.config.selectable) {
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
            var orderable = Atis.getProp(columnDef, 'orderable', true);
            var selectable = Atis.getProp(columnDef, 'selectable');
            var buttons = Atis.getProp(columnDef, 'buttons');

            columnDef.node = $('th', self.thead).get(index);
            
            if  (selectable || buttons) {
                return;
            }
            
            if (orderable) {
                $(columnDef.node).css('cursor', 'pointer');
                $(columnDef.node).on('click', function() { self.orderBy(columnDef, index); });
            }
        })
    };
    
    this.checkUncheckAll = function(check) {
    	if (self.lastResponse.data) {
			self.lastResponse.data.forEach(function (row) {
				var firstColumnDef = self.config.columns[0];
				var value = Atis.getProp(row, firstColumnDef.data);
				
				$("input[type='checkbox']", self.tbody).prop('checked', check);
				self.checked[value] = check;
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
        if (self.params.page < self.lastResponse.pagesTotal) {
            self.params.page++;
            self.refresh();
        }
    };

    this.paginationLast = function() {
        if (self.params.page < self.lastResponse.pagesTotal) {
            self.params.page = self.lastResponse.pagesTotal;
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
    
    this.prepareTable();
    this.refresh();
}
