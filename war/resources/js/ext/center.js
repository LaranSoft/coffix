jQuery.fn.center = function(absolute, horizontal, vertical, zIndex) {
        
        horizontal = horizontal != null ? horizontal : jQuery.fn.center.options.horizontal;
        vertical = vertical != null ? vertical : jQuery.fn.center.options.vertical;
        zIndex = zIndex || jQuery.fn.center.options.zIndex;
        
        return this.each(function () {
                var t = jQuery(this);

                var firstCss = {
                        position: absolute ? 'absolute' : 'fixed', 
                        zIndex: zIndex
                };
                
                if(horizontal) firstCss.left = '50%';
                if(vertical) firstCss.top = '50%';
                
                t.css(firstCss);
                
                var secondCss = {};
                if(horizontal) secondCss.marginLeft = '-' + (t.outerWidth() / 2) + 'px';
                if(vertical) secondCss.marginTop = '-' + (t.outerHeight() / 2) + 'px';
                
                t.css(secondCss);

                if (absolute) {
                        var thirdCss = {};
                        
                        if(horizontal) thirdCss.marginTop = parseInt(t.css('marginTop'), 10) + jQuery(window).scrollTop();
                        if(vertical) thirdCss.marginLeft = parseInt(t.css('marginLeft'), 10) + jQuery(window).scrollLeft();
                        t.css(thirdCss);
                }
        });
};

jQuery.fn.center.options = {
        absolute: null,
        horizontal: true,
        vertical: true,
        zIndex: '99'
};