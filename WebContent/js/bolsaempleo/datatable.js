/**
 * Datatable
 * @copyright ATISoluciones 2021
 */

function getProp( object, keys, defaultVal ){
  keys = Array.isArray( keys )? keys : keys.split('.');
  object = object[keys[0]];
  if( object && keys.length>1 ){
    return getProp( object, keys.slice(1) );
  }
  return object === undefined? defaultVal : object;
}

function setProp( object, keys, val ){
  keys = Array.isArray( keys )? keys : keys.split('.');
  if( keys.length>1 ){
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
        'page': 1
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
	        error: function(response) {
	            console.log(response);
	        }
	    });   
    };
        
    this.loading = function(on) {
    	if (on) {
    		$(self.tbody).append('<p class="loading">Loading...</p>');
    	} else {
    		$('p.loading', self.tbody).remove();    		
    	}    	
    };
    
    this.parseResponse = function(response) {
    	self.lastResponse = response;
    	self.loading(false);
    	self.checkUncheckAll(false);
    	self.renderFooter();
    	
    	if (response.data.length > 0) {
	    	response.data.forEach(function(row, index) {    		
	    		self.addRow(row, index);	    	    		    
	    	});
    	} else {
    		// sin resultados
    		$(self.tbody).append('<tr><td colSpan="'+ self.config.columns.length + '">Sin resultados</tr>');	
        }    	
    }
    
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
    			var btn = $('<button class="btn" type="button">'+buttonDef.label+'</button>');
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
    	if (!this.lastResponse) { return ''; }    	
    	var selected = self.getCheckedItems();  
        var labelTotal = 'Total ' + this.lastResponse.recordsTotal;
        var labelSelected = selected.length > 0 ? ' (Seleccionados ' + selected.length + ')' : ''
        var textoTotal = '<span class="total">' + labelTotal + labelSelected +'</span>';
        var pagination = '<span class="pagination">P&aacute;gina ' + this.params.page + ' / ' + this.lastResponse.recordsTotal + '</span>';

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
    
    this.prepareTable();
    this.refresh();
}
