var region_names = [];
var counts = [];
var region_coordinates = [];

var color_low = '#00ff00';
var color_medium = '#ffff00';
var color_high = '#ff0000';

var population_low = 1500;
var population_medium = 7500;

var polygons = [];
function parseGeoJSON() {
	$.each(data, function(i, item) {
		if(i === "features") {
			$.each(item, function(j, jtem) {
				var name = jtem["properties"]["RegName"];
				var count = jtem["properties"]["Broi_Lica"];
				var coordinate = jtem["geometry"]["coordinates"][0][0];
				region_names.push(name);
				counts.push(count);
				region_coordinates.push(coordinate);
				L.geoJSON(jtem).addTo(mymap);
			});
		}
	});
}

function drawGeofences(mymap) {
	var utm = "+proj=utm +zone=34N";
    var wgs84 = "+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs";
	
	parseGeoJSON();
	for (var i = 0; i < region_names.length; i++) {
		var parsed_coords = [];
		$.each(region_coordinates[i], function(j, item) {
			parsed_coords.push(proj4(utm,wgs84,region_coordinates[i][j]).reverse());
			
		});
		
		var color;
		if (counts[i] <= population_low)
			color = color_low;
		else if (counts[i] <= population_medium)
			color = color_medium;
		else 
			color = color_high;
		
		var polygon = L.polygon(parsed_coords, {
			fillColor: color,
			opacity: 0.1,
			color: color			
		});
		polygons.push(polygon);
		polygon.bindTooltip("Name: " + region_names[i] + "\nPopulation: " + counts[i], {permanent: false, direction:"center"});
	} 
	
	toggleRegions(mymap);
}

function toggleRegions(mymap) {
	polygons.forEach(polygon => $("#regions").is(":checked") ? polygon.addTo(mymap) : mymap.removeLayer(polygon));
}