var stations = [];

var station_names = [];
var station_coordinates = [];

var stationsColor = '#0000ff';

var stops = [];
var people_max = 7200;
var delimeter = 240;
var station_layer = [];
var indexes = [];
var checked = true;
 
function parseGeoJSONStops() {
	var utm = "+proj=utm +zone=34N";
    var wgs84 = "+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs";
	$.each(stopsData, function(i, item) {
		if(i === "features") {
			$.each(item, function(j, jtem) {
				var name = jtem["properties"]["ИМЕ_СПИР00"];
				var busses = jtem["properties"]["ЛИНИЯ_BUS"];
				var trolleybusses = jtem["properties"]["ЛИНИЯ_TB"];
				var trams = jtem["properties"]["ЛИНИЯ_TM"];
				var code = jtem["properties"]["КОД_СПИРКА"];
				var coordinate = jtem["geometry"]["coordinates"].slice(0, 2);
				var station = { name: name, code: code, coordinate: proj4(utm,wgs84,coordinate).reverse(), exchange: 0, busses: busses, trolleybusses: trolleybusses, trams: trams };
				stations.push(station);
			});
		}
	});
}
parseGeoJSONStops();
function drawStations(mymap) {
    for(var i = 0; i < stations.length; i++) {
		if( mymap.getZoom() < 15) {
			if (stations[i].exchange > 4000) {
				display(mymap, i, stations[i].exchange );
			}
		} else if(mymap.getZoom() >= 15 && mymap.getZoom() < 18) {
			if (stations[i].exchange > 2000)
				display(mymap, i, stations[i].exchange );
		} else if(mymap.getZoom() >= 18) {
			display(mymap, i, stations[i].exchange );
		}
    }
	
	toggleStops(mymap);
}
 
function display(mymap, i, exchange) {
    var circle = L.circle(stations[i].coordinate, {
		fillColor: stationsColor,
		opacity: 1,
		color: stationsColor,
		radius: (exchange / delimeter) + 5
	}).addTo(mymap);
	console.log(stations[i].busses);
	station_layer.push(circle);
	circle.bindTooltip("Name: " + stations[i].name + " Exchange: " + stations[i].exchange + (stations[i].busses == "" ? "" : (" Busses: " + stations[i].busses)), {permanent: false, direction:"center"});
   
}

function toggleStops(mymap) {
	if(checked && $("#stops").is(":checked")) return;
	station_layer.forEach(stop => $("#stops").is(":checked") ? stop.addTo(mymap) : mymap.removeLayer(stop));
}

function clearAll(mymap) {
    for (i = 0; i < station_layer.length; i++){
        mymap.removeLayer(station_layer[i]);
	}
    station_layer = [];
    station_names = [];
    station_coordinates = [];
}
