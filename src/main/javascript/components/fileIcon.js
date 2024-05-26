angular.module('franchisorLogistics')
.component('fileIcon', {
	bindings: {
		contentType: '<'
	},
	controller: function() {
		this.$onInit = function() {
			this.className = undefined;
			
			if (this.contentType) {
				this.className = 'fa-file-o';
				if (this.contentType.indexOf("pdf") >= 0) 			{this.className = 'fa-file-pdf-o';}
		    	if (this.contentType.indexOf("image") >= 0) 		{this.className = 'fa-file-image-o';}
		    	if (this.contentType.indexOf("presentation") >= 0) 	{this.className = 'fa-file-powerpoint-o';}
		    	if (this.contentType.indexOf("excel") >= 0) 		{this.className = 'fa-file-excel-o';}
			}
		};		
	},
	template: '<i class="fa {{$ctrl.className}}"></i>'
})