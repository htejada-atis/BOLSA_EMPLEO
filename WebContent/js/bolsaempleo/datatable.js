/**
 * Datatable
 * @copyright ATISoluciones 2021
 */

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

function DataTable(id, config) {
    this.id = id;
    this.node = $(id);
    this.thead = $('tr', this.node).first();
    this.tbody = $('tbody', this.node).last();
    this.tfoot = $('tfoot', this.node).last();
    this.config = config;
    this.params = {
        'a': 'datatable',
        'page': 0,
        'pageSize': getProp(config, 'pageSize', 10),
        'orderBy': null,
        'orderDirection': 'asc'
    };
    this.lastResponse = null;
    this.checked = {};

    var self = this;

    this.refresh = function() {
    	self.loading(true);
    	
    	$.ajax({
	        type: "GET",
	        url: self.config.ajax.url,
	        contentType: "application/json",
	        dataType: "json",
	        data: self.params,
	        success: this.parseResponse.bind(self),
	        error: this.errorResponse.bind(self)
	    });	      
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
    
    	var error = $.parseJSON(err.responseText);
    	var errorText = getProp(error, 'error', 'Sin definir');
        
        $('tr', self.tbody).hide();
        $(self.tbody).append('<p class="loading">Error: ' + errorText + '</p>');
    };
    
    this.addRow = function(row, index) {
    	var tr = $('<tr id="' + this.id + '_row_' + index + '"></tr>');
    	
    	self.config.columns.forEach(function(columnDef) {
    		var td = $('<td></td>');
    		$(td).append(self.renderCol(row, columnDef));
    		$(tr).append(td);
        });
    	
    	$(self.tbody).append(tr); 
    };
    
    this.renderCol = function(row, columnDef) {
    	var data = columnDef.data;
    	var value = getProp(row, data);
    	
    	if (columnDef.hasOwnProperty('render')) {
    		return columnDef.render(row);
    	}
    	
    	if (columnDef.hasOwnProperty('buttons')) {
    		var buttons = $('<span class="btns"></span>');
    		
    		columnDef.buttons.forEach(function(buttonDef) {
                var label = isFunction(buttonDef.label) ? buttonDef.label(row) : buttonDef.label;
    			var btn = $('<button class="btn" type="button">'+label+'</button>');
    			$(btn).on("click", function() { buttonDef.onClick(row, self); });
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
    	var selected = self.getCheckedItems();  
        var labelTotal = 'Total ' + self.lastResponse.recordsTotal;
        var labelSelected = selected.length > 0 ? ' (Seleccionados ' + selected.length + ')' : ''
        var textoTotal = '<span class="total">' + labelTotal + labelSelected +'</span>';
        
        var btnFirst = $('<button class="btn" type="button">&lt;&lt;</button>');
        $(btnFirst).on("click", function() { self.paginateFirst(); });

        var btnBack = $('<button class="btn" type="button">&lt;</button>');
        $(btnBack).on("click", function() { self.paginationPrevious(); });

        var btnNext = $('<button class="btn" type="button">&gt;</button>');
        $(btnNext).on("click", function() { self.paginationNext(); });

        var btnLast = $('<button class="btn" type="button">&gt;&gt;</button>');
        $(btnLast).on("click", function() { self.paginationLast(); });

        var pagination = $('<span class="pagination"></span>');
        $(pagination).append(btnFirst);
        $(pagination).append(btnBack);
        $(pagination).append(' P&aacute;gina ' + (self.params.page + 1) + ' / ' + (self.lastResponse.pagesTotal + 1) + ' ');
        $(pagination).append(btnNext);
        $(pagination).append(btnLast);

        $('th', self.tfoot).empty();        
    	$('th', self.tfoot).append($(pagination));
        $('th', self.tfoot).append($(textoTotal));
    };
    
    this.prepareTable = function() {
        // si es selectable check en la cabecera para marcar/desmacar todos
    	if (self.config.selectable) {
    		var check = $('<input type="checkbox"/>')
    		$(check).on('change', function() { self.checkUncheckAll(this.checked); });
    		$('th', self.thead).first().append(check);
    	}

        self.config.columns.forEach(function(columnDef, index) {
            var orderable = getProp(columnDef, 'orderable', true);
            var selectable = getProp(columnDef, 'selectable');
            var buttons = getProp(columnDef, 'buttons');

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
				var value = getProp(row, firstColumnDef.data);
				
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
