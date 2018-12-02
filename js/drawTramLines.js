var tramLineCoordinates = [];

var tramColor = '#ff0000';

var tramLines = [];
function parseGeoJSONTramLines() {
	var utm = "+proj=utm +zone=34N";
    var wgs84 = "+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs";
	
	$.each(tramsData, function(i, item) {
		if(i === "features") {
			$.each(item, function(j, jtem) {
				$.each(jtem["geometry"]["coordinates"], function(k, ktem) {
					jtem["geometry"]["coordinates"][k] = proj4(utm,wgs84,ktem).reverse();
					
				});
				tramLineCoordinates.push(jtem["geometry"]["coordinates"]);
			});
		}
	});
}
/*
function parseBusLinesJSON() {
	$.each(trolleybussesLines["trams"], function(i, item) {
		var busLineObject = {};
		busLineObject.name = item["name"];
		busLineObject.stops = [];
	    $.each(item["stops"], function(j, jtem) {
			var stop = {};
			stop.code = jtem["code"];
			stop.output = jtem["output"];
			stop.input = jtem["input"];
			stop.exchange = jtem["exchange"];
			stop.natovarenost = jtem["natovarenost"];
			
			if(stations.find(station => station.code === stop.code) !== undefined) {
				stations.find(station => station.code === stop.code).exchange += stop.exchange;
			}
			
			busLineObject.stops.push(stop);
		});
		busLinesObjects.push(busLineObject);
	});
}
*/
function drawTramLines(mymap) {
	parseGeoJSONTramLines();
	for(var i = 0; i < tramLineCoordinates.length; i++) {	
		var line = L.polyline(tramLineCoordinates[i], {
			fillColor: tramColor,
			opacity: 0.5,
			color: tramColor,
			weight: 2
		});
		tramLines.push(line);
		line.bindTooltip("Name: " + station_names[i], {permanent: false, direction:"center"})
	}
	toggleTrams(mymap);
} 

function toggleTrams(mymap) {
	tramLines.forEach(tram => $("#trams").is(":checked") ? tram.addTo(mymap) : mymap.removeLayer(tram));
}
