var busLineCoordinates = [];
var drawedCoordinates = [];

var busColor = '#0000ff';

var busLines = [];

var busLinesObjects = [];
function parseGeoJSONBusLines() {
	var utm = "+proj=utm +zone=34N";
    var wgs84 = "+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs";
	
	$.each(busLineData, function(i, item) {
		if(i === "features") {
			$.each(item, function(j, jtem) {
				$.each(jtem["geometry"]["coordinates"], function(k, ktem) {
					jtem["geometry"]["coordinates"][k] = proj4(utm,wgs84,ktem).reverse();
				});
				busLineCoordinates.push(jtem["geometry"]["coordinates"]);
			});
		}
	});
}

function parseBusLinesJSON() {
	$.each(busLinesFirst["busses"], function(i, item) {
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

function drawBusLines(mymap) {
	parseGeoJSONBusLines();
	parseBusLinesJSON();
	$.each(busLinesObjects, function(i, item) {
		for(var index = 0; index < item.stops.length - 1; index++) {
			var stationOne = stations.filter(station => { return station.code == item.stops[index].code })[0];
			var stationTwo = stations.filter(station => { return station.code == item.stops[index + 1].code })[0];
			
			if( stationOne === undefined || stationTwo === undefined) {
				console.log("undefined station");
				continue;
			}
			
			var filteredCoordinates = busLineCoordinates.find(busLineCoordinate => (JSON.stringify(busLineCoordinate[0]) === JSON.stringify(stationOne.coordinate)
																	   && JSON.stringify(busLineCoordinate[busLineCoordinate.length-1]) === JSON.stringify(stationTwo.coordinate)));
			if(filteredCoordinates !== null && filteredCoordinates !== undefined){
				console.log(filteredCoordinates);
				
				var polyline = { natovarenost: item.stops[index].natovarenost, coordinates: filteredCoordinates  }
				
				var line = L.polyline(polyline.coordinates, {
					fillColor: busColor,
					opacity: 0.5,
					color: busColor,
					weight: 2
				});
				busLines.push(line);
				line.bindTooltip("Natovarenost: " + polyline.natovarenost, {permanent: false, direction:"center"});
			}
		} 
		
	});	
	toggleBusses(mymap);
} 

function drawSimpleBusLines(mymap) {
	parseGeoJSONBusLines();
	parseBusLinesJSON();
	for(var i = 0; i < busLineCoordinates.length; i++) {	
		var line = L.polyline(busLineCoordinates[i], {
			fillColor: busColor,
			opacity: 0.5,
			color: busColor,
			weight: 2
		});
		busLines.push(line);
		line.bindTooltip("Name: " + station_names[i], {permanent: false, direction:"center"});
	}
		
	toggleBusses(mymap);
}

function toggleBusses(mymap) {
	if(busLines.length === 0) drawBusLines(mymap);
	busLines.forEach(function(bus) {
		$("#busses").is(":checked") ? bus.addTo(mymap) : mymap.removeLayer(bus);
	});
}
