function getidvalue(){
	return document.getElementById('casid').value;
}
function submitRequest(casid) {
			//var casid = document.getElementById('casid');
			//alert("value "+casid);
			if($('#casid').val() == ''){
		      $( "#dialog" ).dialog({
				      resizable: false,
				      height: "auto",
				      width: 200,
				      modal: true,
				      buttons: {
				        Close: function() {
				          $( this ).dialog( "close" );
				        }
				      }
				    });
		   }else{
	 $( function() {
 	    $( "#dialog-confirm-submit" ).dialog({
 	      resizable: false,
 	      height: "auto",
 	      width: 400,
 	      modal: true,
 	      buttons: {
 	        "Yes": function() {
 	        	var modal = $('<div>').dialog({ modal: true });
 	        	modal.dialog('widget').hide();
 	        	$("#dialog-confirm-submit").dialog('close');
 	        	$('#spinner').show();
 	       		var datas = {
 	       					"casid" : casid
 	       					}
 	       		$.ajax({
 	       			type: "POST",
 	       			contentType : 'application/json; charset=utf-8',
 	       			dataType : 'json',
 	       			url: "/DeletefromCAS/SubmitRequest",
 	       			data: JSON.stringify(datas),
 	       			success :function(data) {
 	       				$('#spinner').hide();
 	       				modal.dialog('close');
 	       			    $( function() {
 	       			               $( "#dialog-confirm" ).dialog({
 	       			                 resizable: false,
 	       			                 height: "auto",
 	       			                 width: 400,
 	       			                 modal: true,
 	       			                 buttons: {
 	       			                   "Select a new project": function() {
 	       			                     	$(".button").prop('disabled', true).css('opacity',0.5);
 	       									location.reload(true);
 	       			                   }
 	       			                 }
 	       			               });
 	       			             } );
 	       				},
 	       				error: function (e) {
 	       			}
 	   	     });
 	        },
 	        	Cancel : function() {
 	        		$( this ).dialog( "close" );
 	        		}
 	      		}
 	    	});
 	  } );
 	  }
}