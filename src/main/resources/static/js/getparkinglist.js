$(document).ready(function(){$('#parkingcity').change(
        function() {
            $.getJSON("/parkinglist", {
                city : $(this).val(),
                ajax : 'true'
            }, function(data) {
                var html = '<option disabled="disabled" selected="selected" value=0>Select Parking</option>';
                for ( var i = 0; i < data.length; i++) {
                    html += '<option value="' + data[i].parkingid + '">'
                            + data[i].parkingname + '</option>';
                }
                $('#parkingid').html(html);
                document.getElementsByName("vehicletype")[0].value='';
            	document.getElementsByName("date")[0].value='';
            	document.getElementById('bookings').innerHTML = '';
    	});
	});
});



function getBookings(){
	$.getJSON("/bookings", {
            id : document.getElementsByName("parkingid")[0].value,
            vehicletype : document.getElementsByName("vehicletype")[0].value,
            date : document.getElementsByName("date")[0].value,
            ajax : 'true'
        }, function(data){
        	var html = '<table class="tb-th-td tb"><tr><th class="tb-th-td">Booking Id</th><th class="tb-th-td">Phone</th><th class="tb-th-td">Vehicle Type</th><th class="tb-th-td">Vehicle Number</th>';
        	html += '<th class="tb-th-td">Booking Date</th><th class="tb-th-td">Parking Start DateTime</th><th class="tb-th-td">Parking End DateTime</th><th class="tb-th-td">Total Hours</th><th class="tb-th-td">Bill Amount</th>';
        	for ( var i = 0; i < data.length; i++) {
        		html += '<tr><td class="tb-th-td">';
                html += data[i].bookingid+'</td><td class="tb-th-td">'+data[i].userphone+'</td><td class="tb-th-td">'+data[i].vehicletype+'</td><td class="tb-th-td">';
                html += data[i].vehicleregistrationnumber+'</td><td class="tb-th-td">'+data[i].bookingdate+'</td><td class="tb-th-td">'+data[i].parkingstartdatetime+'</td><td class="tb-th-td">'; 
                html += data[i].parkingenddatetime+'</td><td class="tb-th-td">'+data[i].totalparkinghours+'</td><td class="tb-th-td">'+data[i].billamount;
                html += '</td></tr>';
            }
            html += '</table>';
            document.getElementById('bookings').innerHTML = html;
	});
};