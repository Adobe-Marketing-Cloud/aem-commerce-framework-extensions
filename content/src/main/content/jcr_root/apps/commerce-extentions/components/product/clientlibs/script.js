/*
 ADOBE CONFIDENTIAL
 __________________

 Copyright 2012 Adobe Systems Incorporated
 All Rights Reserved.

 NOTICE:  All information contained herein is, and remains
 the property of Adobe Systems Incorporated and its suppliers,
 if any.  The intellectual and technical concepts contained
 herein are proprietary to Adobe Systems Incorporated and its
 suppliers and are protected by trade secret or copyright law.
 Dissemination of this information or reproduction of this material
 is strictly forbidden unless prior written permission is obtained
 from Adobe Systems Incorporated.
 */

jQuery(function ($) {

    var allVariants = $("article.product");
    var allLis = allVariants.find(".product-chooser li");

    allVariants.each(function() {
        var thisVariant = $(this);
        var thisForm    = thisVariant.children(".product-form");
        var product     = thisVariant.parent();
        var isMobile    = product.is(".product_mobile");

        function initVariantSwitch() {
            var variantSwitch = thisVariant.children(".product-chooser");
            variantSwitch.find("li").each(function () {
                var li = $(this);
                var variant = product.children("article[data-sku='"+li.attr("data-sku")+"']");

                if (variant.attr("data-sku") == thisVariant.attr("data-sku")) {
                    li.addClass("selected");
                }

                li.click(function () {
                    var oldQty   = thisForm.find(".product-quantity").find("select");
                    var oldSize  = thisForm.find(".product-size").find(isMobile ? "select" : "input:checked");
                    var newForm  = variant.children(".product-form");
                    var newLi = variant.find(".product-chooser li[data-sku='"+li.attr("data-sku")+"']");

                    newForm.find(".product-quantity select").val(oldQty.val());
                    if (isMobile) {
                        newForm.find(".product-size select").val(oldSize.val()).trigger("change");
                    } else {
                        var newSize = newForm.find(".product-size input[value='"+oldSize.val()+"']");
                        if (newSize.length > 0) {
                            newSize.click();
                        } else {
                            // if new variant doesn't have size that was selected in old variant,
                            // just select the first size:
                            var firstSize = newForm.find(".product-size input");
                            if (firstSize.length > 0) {
                                $(firstSize[0]).click();
                            }
                        }
                    }
                    allVariants.addClass("isHidden");
                    variant.removeClass("isHidden");
                    allLis.removeClass("selected");
                    newLi.addClass("selected");
                });
                li.hover(function () {
                    li.addClass("hover");
                }, function () {
                    li.removeClass("hover");
                });
            });
        }

        function initSizeChooser() {
            var sizeChooser = thisVariant.find(".product-size");

            // desktop:
            sizeChooser.find("li").each(function () {
                var li = $(this);
                var input = li.find("input");
                var affectedInput = sizeChooser.find("input[name='"+input.attr("name")+"']");
                var affectedLi = affectedInput.closest("li");

                input.add(li);
                li.click(function () {
                    var val = input.val();
                    affectedInput.val([val]);
                    affectedLi.removeClass("selected");
                    li.addClass("selected");
                    thisVariant.find(".product-price").html(input.attr("data-price"));
                    thisVariant.find(".product-item").html(input.attr("data-sku"));
                    thisVariant.find(".product-title").html(input.attr("data-title"));
                    thisVariant.find(".product-description").html(input.attr("data-description"));
                    thisVariant.find(".product-submit input[name='product-path']").val(input.attr("data-path"));
                    return false;
                });
                li.hover(function () {
                    li.addClass("hover");
                }, function () {
                    li.removeClass("hover");
                });

                li.keyup(function(e) {
                    var code = e.which;
                    if( code==32 || code==13 ) {
                        li.click();
                    }
                });

                if (input.is(":checked")) {
                    li.addClass("selected");
                }
            });

            // mobile:
            sizeChooser.find("select").each(function() {
                $(this).change(function () {
                    var selectedOption = $(this).find("option:selected");
                    $(".product-price").html(selectedOption.attr("data-price"));
                    $(".product-submit input[name='product-path']").val(selectedOption.attr("data-path"));
                });
            });
        }

        initVariantSwitch();
        initSizeChooser();
    });
});
