var station_names = [];
var station_coordinates = [];

var color = '#0000ff';

function parseGeoJSONStops() {
	var utm = "+proj=utm +zone=34N";
    var wgs84 = "+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs";
	$.each(stopsData, function(i, item) {
		if(i === "features") {
			$.each(item, function(j, jtem) {
				var name = jtem["properties"]["ИМЕ_СПИР00"];
				var coordinate = jtem["geometry"]["coordinates"].slice(0, 2);
				coordinate = proj4(utm,wgs84,coordinate);
				station_names.push(name);
				station_coordinates.push(coordinate.reverse());
			});
		}
	});
}

function drawStations(mymap) {
		
	parseGeoJSONStops();
	for(var i = 0; i < station_names.length; i++) {	
		var circle = L.circle(station_coordinates[i], {
			fillColor: color,
			opacity: 1,
			color: color,
			radius: Math.floor(Math.random() * 10) + 5
			
			}).addTo(mymap);
			
			circle.bindTooltip("Name: " + station_names[i], {permanent: false, direction:"center"})
	}
} 

