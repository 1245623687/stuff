$(document).ready(function () {
	$('#go_button').on ('click', function (event) {

		//prevents the url from adding '#' at the end
		event.preventDefault ();

		//create red and green hello and world words and put them in the span 
		//inside the inner-div-hello-world
		var red = '<i>hello </i>',
			green = '<i>world</i>',
			$hw = $('#inner-div-hello-world span');
		
		$(red)
			.css ('color', '#FF0000')
			.css ('font-style', 'normal')
			.appendTo ($hw);
		
		$(green)
			.css ('color', '#00FF00')
			.css ('font-style', 'normal')
			.appendTo ($hw);
			
			
		//grab the hex color string input field
		$hex_field = $('#hex_color_string_input_field');
		
		//get the string from the hex color input field
		var hex_color_string = $hex_field.val();
		//console.log(hex_color_string);
		
		//create an html string from that string
		var open_p = '<p>';  var close_p = '</p>'		
		var html_hex_string = open_p.concat(hex_color_string).concat(close_p);
		console.log(html_hex_string);
		
		//for testing for now display the hex string below hello world
		$(html_hex_string).appendTo($hw);
		
	});
});