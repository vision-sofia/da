var bus_line = {};

var bus_line_coordinates = [];

var color = '#0000ff';

function parseGeoJSONBusLines() {
	var utm = "+proj=utm +zone=34N";
    var wgs84 = "+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs";
	
	$.each(busLineData, function(i, item) {
		if(i === "features") {
			$.each(item, function(j, jtem) {
				$.each(jtem["geometry"]["coordinates"], function(k, ktem) {
					jtem["geometry"]["coordinates"][k] = proj4(utm,wgs84,ktem).reverse();
					
				});
				console.log(jtem["geometry"]["coordinates"]);
				bus_line_coordinates.push(jtem["geometry"]["coordinates"]);
			});
		}
	});
}

function drawBusLines(mymap) {
		
	parseGeoJSONBusLines();
	for(var i = 0; i < bus_line_coordinates.length; i++) {	
		var line = L.polyline(bus_line_coordinates[i], {
			fillColor: 'red',
			opacity: 0.5,
			color: 'red',
			weight: 2
			
			
			}).addTo(mymap);
			
			line.bindTooltip("Name: " + station_names[i], {permanent: false, direction:"center"})
	}
} 

