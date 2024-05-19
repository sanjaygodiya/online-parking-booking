var tzoffset = (new Date()).getTimezoneOffset() * 60000;
var mindate = (new Date(Date.now() - tzoffset)).toISOString().slice(0, 16);

var maxdate = new Date(new Date().setHours(47,59));
maxdate = (new Date(maxdate - tzoffset)).toISOString().slice(0, 16);

document.getElementsByName("parkingstartdatetime")[0].min = mindate;
document.getElementsByName("parkingenddatetime")[0].min = mindate;

document.getElementsByName("parkingstartdatetime")[0].max = maxdate;
document.getElementsByName("parkingenddatetime")[0].max = maxdate;

var twowheelerparkingrate = 0;
var fourwheelerparkingrate = 0;
var twowheelertotalcount = 0;
var fourwheelertotalcount = 0;
var bookedslots = 0;
var availableslots = 0;

document.getElementById("bookSubmit").disabled = true;

$(document).ready(function(){$('#parkingenddatetime').change(
        function() {
			var parkingstartdatetime=new Date(document.getElementsByName("parkingstartdatetime")[0].value);
			var parkingenddatetime=new Date(document.getElementsByName("parkingenddatetime")[0].value);
			var timediff =new Date(parkingenddatetime - parkingstartdatetime) / 1000;
  			timediff /= (60 * 60);
            timediff = Math.abs(Math.round(timediff));
            $('.totalparkinghours').html(timediff);
            document.getElementsByName("totalparkinghours")[0].value=timediff;
            var vehicletype=document.getElementsByName("vehicletype")[0].value;
            var rate=0;
            var billamount=0;
            if(vehicletype=="2Wheeler")
            	{
            		rate=twowheelerparkingrate;
            	}
            else
            	{
            		rate=fourwheelerparkingrate;
            	}
            billamount = rate * timediff;
            $('.billamount').html(billamount);
            document.getElementsByName("billamount")[0].value=billamount;
        });
       });

$(document).ready(function(){$('#parkingstartdatetime').change(
        function() {
			var parkingstartdatetime=new Date(document.getElementsByName("parkingstartdatetime")[0].value);
			var parkingenddatetime=new Date(document.getElementsByName("parkingenddatetime")[0].value);
			var timediff =new Date(parkingenddatetime - parkingstartdatetime) / 1000;
  			timediff /= (60 * 60);
            timediff = Math.abs(Math.round(timediff));
            $('.totalparkinghours').html(timediff);
            document.getElementsByName("totalparkinghours")[0].value=timediff;
            var vehicletype=document.getElementsByName("vehicletype")[0].value;
            var rate=0;
            var billamount=0;
            if(vehicletype=="2Wheeler")
            	{
            		rate=twowheelerparkingrate;
            	}
            else
            	{
            		rate=fourwheelerparkingrate;
            	}
            billamount = rate * timediff;
            $('.billamount').html(billamount);
            document.getElementsByName("billamount")[0].value=billamount;
        });
       });
       
$(document).ready(function(){$('#vehicletype').change(
        function() {      	
        	document.getElementsByName("vehicleregistrationnumber")[0].value='';
        	document.getElementsByName("parkingstartdatetime")[0].value='';
        	document.getElementsByName("parkingenddatetime")[0].value='';
        	document.getElementsByName("totalparkinghours")[0].value='';
        	document.getElementsByName("billamount")[0].value='';            	
        	$('.totalparkinghours').html('');
        	$('.billamount').html('');
        	$('.availableslots').html('');
        	document.getElementById("bookSubmit").disabled = true;
        });
       });
       
$(document).ready(function(){$('#parkingcity').change(
        function() {
            $.getJSON("/parkinglist", {
                city : $(this).val(),
                ajax : 'true'
            }, function(data) {
                var html = '<option disabled="disabled" selected="selected" value=0>Select Your Parking</option>';
                for ( var i = 0; i < data.length; i++) {
                    html += '<option value="' + data[i].parkingid + '">'
                            + data[i].parkingname + '</option>';
                }
                $('#parkingid').html(html);
                document.getElementsByName("vehicletype")[0].value='';
            	document.getElementsByName("vehicleregistrationnumber")[0].value='';
            	document.getElementsByName("parkingstartdatetime")[0].value='';
            	document.getElementsByName("parkingenddatetime")[0].value='';
            	document.getElementsByName("totalparkinghours")[0].value='';
            	document.getElementsByName("billamount")[0].value='';            	
            	$('.totalparkinghours').html('');
            	$('.billamount').html('');
            	$('.availableslots').html('');
            	document.getElementById("bookSubmit").disabled = true;
            });
        });
       });
       
$(document).ready(function(){$('#parkingid').change(
        function() {
            $.getJSON("/parkingdetails", {
                id : $(this).val(),
                ajax : 'true'
            }, function(data) {         	
            	twowheelerparkingrate = data.twowheelerparkingrate;
            	fourwheelerparkingrate = data.fourwheelerparkingrate;
            	twowheelertotalcount = data.twowheelertotalcount;
            	fourwheelertotalcount = data.fourwheelertotalcount;
            	document.getElementsByName("vehicletype")[0].value='';
            	document.getElementsByName("vehicleregistrationnumber")[0].value='';
            	document.getElementsByName("parkingstartdatetime")[0].value='';
            	document.getElementsByName("parkingenddatetime")[0].value='';
            	document.getElementsByName("totalparkinghours")[0].value='';
            	document.getElementsByName("billamount")[0].value='';            	
            	$('.totalparkinghours').html('');
            	$('.billamount').html('');
            	$('.availableslots').html('');
            	document.getElementById("bookSubmit").disabled = true;
            });
        });
       });
       
 $(document).ready(function(){$('#parkingenddatetime').change(
        function() {
            $.getJSON("/bookedslots", {
                id : document.getElementsByName("parkingid")[0].value,
            	vehicletype : document.getElementsByName("vehicletype")[0].value,
            	parkingstartdate : document.getElementsByName("parkingstartdatetime")[0].value,
            	parkingenddate : document.getElementsByName("parkingenddatetime")[0].value,
                ajax : 'true'
            }, function(data) {         	
            	bookedslots = data;   
            	var vehicletype=document.getElementsByName("vehicletype")[0].value;    
            	if(vehicletype=="2Wheeler")
            	{
            		availableslots=twowheelertotalcount-bookedslots;
            	}
            	else
            	{
            		availableslots=fourwheelertotalcount-bookedslots;
            	}
            	if(availableslots>0)
            	{
            		document.getElementById("bookSubmit").disabled = false;
            	}   	
            	$('.availableslots').html(availableslots);
            });
        });
       });
       
 $(document).ready(function(){$('#parkingstartdatetime').change(
        function() {
	        if(document.getElementsByName("parkingenddatetime")[0].value!=''){
	            $.getJSON("/bookedslots", {
	                id : document.getElementsByName("parkingid")[0].value,
	            	vehicletype : document.getElementsByName("vehicletype")[0].value,
	            	parkingstartdate : document.getElementsByName("parkingstartdatetime")[0].value,
	            	parkingenddate : document.getElementsByName("parkingenddatetime")[0].value,
	                ajax : 'true'
	            }, function(data) {         	
	            	bookedslots = data;   
	            	var vehicletype=document.getElementsByName("vehicletype")[0].value;    
	            	if(vehicletype=="2Wheeler")
	            	{
	            		availableslots=twowheelertotalcount-bookedslots;
	            	}
	            	else
	            	{
	            		availableslots=fourwheelertotalcount-bookedslots;
	            	}
	            	if(availableslots>0)
	            	{
	            		document.getElementById("bookSubmit").disabled = false;
	            	}   	
	            	$('.availableslots').html(availableslots);
	            });
	        }
        });
       });

window.onload = function() {
	$.getJSON("/parkinglist", {
		city : document.getElementsByName("parkingcity")[0].value,
		ajax : 'true'
	}, function(data) {
		var html = '<option disabled="disabled" selected="selected" value=0>Select Your Parking</option>';
		for ( var i = 0; i < data.length; i++) {
			html += '<option value="' + data[i].parkingid + '">'
					+ data[i].parkingname + '</option>';
		}
		$('#parkingid').html(html);
	})	
};