var trolleyBusLineCoordinates = [];

var trolleyBusColor = '#00ff00';

var trolleyBusLines = [];

var trolleyBusLinesObjects = [];
function parseGeoJSONTrolleyBusLines() {
	var utm = "+proj=utm +zone=34N";
    var wgs84 = "+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs";
	
	$.each(trolleybussesData, function(i, item) {
		if(i === "features") {
			$.each(item, function(j, jtem) {
				$.each(jtem["geometry"]["coordinates"], function(k, ktem) {
					jtem["geometry"]["coordinates"][k] = proj4(utm,wgs84,ktem).reverse();
					
				});
				trolleyBusLineCoordinates.push(jtem["geometry"]["coordinates"]);
			});
		}
	});
}

function parseTrolleyBusLinesJSON() {
	console.log(trolleybussesLines);
	$.each(trolleybussesLines["trolleybusses"], function(i, item) {
		var trolleyBusLinesObject = {};
		trolleyBusLinesObject.name = item["name"];
		trolleyBusLinesObject.stops = [];
	    $.each(item["stops"], function(j, jtem) {
			var stop = {};
			stop.code = jtem["code"];
			stop.output = jtem["output"];
			stop.input = jtem["input"];
			stop.exchange = jtem["exchange"];
			stop.natovarenost = jtem["natovarenost"];
			
			if(stations.find(station => station.code === stop.code) !== undefined) {
			    console.log("Adding to: " + stations.find(station => station.code === stop.code).name + " -> " + stop.exchange);
				stations.find(station => station.code === stop.code).exchange += stop.exchange;
			}
			
			trolleyBusLinesObject.stops.push(stop);
		});
		trolleyBusLinesObjects.push(trolleyBusLinesObject);
	});
}

function drawTrolleyBusLines(mymap) {
	parseGeoJSONTrolleyBusLines();
	parseTrolleyBusLinesJSON()
	for(var i = 0; i < trolleyBusLineCoordinates.length; i++) {	
		var line = L.polyline(trolleyBusLineCoordinates[i], {
			fillColor: trolleyBusColor,
			opacity: 0.5,
			color: trolleyBusColor,
			weight: 2
		});
		trolleyBusLines.push(line);
		line.bindTooltip("Name: " + station_names[i], {permanent: false, direction:"center"})
	}
	toggleTrolleyBusses(mymap);
} 

function toggleTrolleyBusses(mymap) {
	trolleyBusLines.forEach(trolleyBus => $("#trolleyBusses").is(":checked") ? trolleyBus.addTo(mymap) : mymap.removeLayer(trolleyBus));
}
