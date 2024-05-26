function NotificationUtil(){}

var reWhitespace   = /^\s+$/;

//Types: danger, info, warning, success
NotificationUtil.show = function(container, type, title, message, timeout) {
	
	if (typeof(timeout)==='undefined') timeout = 5;
	
	var icon = '';
	
	if (type == 'danger') {icon = 'fa-ban'}
	if (type == 'info') {icon = 'fa-info'}
	if (type == 'warning') {icon = 'fa-warning'}
	if (type == 'success') {icon = 'fa-check'}
	
	var divContent = '<div id="alertMessage" class="alertCustom alert alert-'+type+' alert-dismissable">'
				   + '    <i class="fa '+icon+'"></i>'
				   + '    <button type="button" class="close" data-dismiss="alert" aria-hidden="true">&times;</button>'
				   + '    <b>'+title+'</b> '+message
				   + '</div>';
		
	container.append(divContent);
	
	if (timeout > 0) {
		window.setTimeout(function() {
		    $("#alertMessage").fadeTo(500, 0).slideUp(500, function(){
		        $(this).remove(); 
		    });
		}, timeout * 1000);
	}
		
}

function ConfirmationUtil(){}

ConfirmationUtil.show = function(container, title, message, confirmFunction, cancelLabel, confirmLabel) {

	cancelLabel = typeof cancelLabel !== 'undefined' ? cancelLabel : 'Cancelar';
	confirmLabel = typeof confirmLabel !== 'undefined' ? confirmLabel : 'Confirmar';
	
	var divContent = '<div id="confirmationMessage" class="alert alert-warning" style="margin-top:15px">'
				   + '	<p><i class="fa fa-warning"></i> <b>'+title+'</b></p>'
				   + '	<p>'+message+'</p>'
				   + '	<p>'
				   + '		<button class="btn btn-danger btn-sm" onclick="javascript: $(\'#confirmationMessage\').remove();">'+cancelLabel+'</button> <button id="confirm-button" class="btn btn-success btn-sm">'+confirmLabel+'</button>'
				   + '	</p>'
				   + '</div>';
	
	container.append(divContent);
	$("#confirm-button").click(function() {
		$("#confirmationMessage").remove();
		confirmFunction();
	});
}

function LoadingUtil(){}

LoadingUtil.show = function(container) {
	
	var divContent = '<div id="tab-overlay" class="overlay"></div>'
				   + '<div id="tab-loading-img" class="loading-img"></div>';
	
	container.append(divContent);	
}

LoadingUtil.hide = function(container) {	
	container.empty();	
}

function checkOnlynumbers(event) {
	var key = event.keyCode ? event.keyCode : event.which ? event.which : event.charCode;
	if((key < 48 || key > 57) && (key != 8 && key != 9 && key != 13)) {
		return false;
	}
	return true;
}

function checkCurrencyValue(event) {
	var key = event.keyCode ? event.keyCode : event.which ? event.which : event.charCode;
	if((key < 48 || key > 57) && (key != 8 && key != 9 && key != 13 && key != 44 && key != 46)) {
		return false;
	}
	return true;
}

function formatCurrencyField (field) {
	field.value = formatCurrencyValue(field.value);
}

function formatCurrencyValue( value ) {
	value = value.toString();

	if( isWhitespace( value ) ) {
		value = "0";
	}

	value = value.replace(/["."]/g, "");

	var integerPart;
	var decimalPart;

	var commaIndex = value.indexOf(",");
	if( commaIndex == -1 ) {
		integerPart = value;
		decimalPart = "0000";
	} else {
		integerPart = value.substring(0, commaIndex);
		decimalPart = value.substring(commaIndex + 1) + "0000";
		decimalPart = decimalPart.replace(/[","]/g, "");
	}

	integerPart = removeLeftZeros(integerPart);

	var cont = 0;
	value = "";
	for(var i = integerPart.length - 1; i >= 0; i--) {
		value = integerPart.charAt(i) + value;
		cont++;
		if(cont % 3 == 0 && i != 0) {
			value = "." + value;
		}
	}

	value = value + "," + decimalPart.substring(0, 4);

	return value;
}

function removeLeftZeros( stringNumber ) {
	for( var i = 0; i < stringNumber.length; i++ ) {
		if( stringNumber.charAt(i) != '0' ) {
			return stringNumber.substring( i );
		}
	}
	return "0";
}

function isWhitespace(param)
{
  return (isEmpty(param) || reWhitespace.test(param));
}

function isEmpty(param)
{
  return ((param == null) || (param.length == 0));
}
