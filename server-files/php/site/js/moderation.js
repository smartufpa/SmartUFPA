$(".btn-edit").click(function(){
	var section = $(this).closest("section");
 	var inputs = section.find(".editable-input");
 	inputs.removeAttr("readonly");
 	inputs.addClass("editing");
 	$(this).siblings(".btn-save").css("display","inline-block");
 });

 $(".btn-save").click(function(){
		var section = $(this).closest("section");
	 	var inputs = section.find(".editable-input");
	 	inputs.attr("readonly","true");
	 	inputs.removeClass("editing");
	 	$(this).css("display","none");
	 });