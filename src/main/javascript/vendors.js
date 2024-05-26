function VendorInit(){}

VendorInit.initFancybox = function () {
			
	jQuery(".fancybox-button").fancybox({
        groupAttr: 'data-rel',
        prevEffect: 'none',
        nextEffect: 'none',
        type:'image',
        closeBtn: true,
        helpers: {
            title: {
                type: 'inside'
                }
            }
        });
	
}
