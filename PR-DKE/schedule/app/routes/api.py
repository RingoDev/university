from datetime import timedelta

from flask import jsonify, make_response

from app import app
from app.models.models import Trip


# returns all trips which have a train id defined -> needed for reservation of tickets in ticket system
@app.route('/api/trip', methods=['GET'])
def get_trips():
    result = [enrich_trip(trip) for trip in Trip.query.all() if trip.train_id is not None]
    return jsonify(result)


@app.route('/api/trip/<trip_id>', methods=['GET'])
def get_trip(trip_id):
    trip = Trip.query.filter_by(id=trip_id).first()
    if trip is not None and trip.train_id is not None:
        return jsonify(enrich_trip(trip))
    else:
        return make_response("Trip doesn't exist", 404)


# reformat a trip object into the dict that will be returned by this systems API
def enrich_trip(trip: Trip):
    return {
        "id": trip.id,
        "routeId": trip.route_id,
        "trainId": trip.train_id,
        "price": float(trip.price),
        "stops": enrich_stops(trip)
    }


# reformat the stops of a trip into the dict that will be returned by this systems API
def enrich_stops(trip: Trip):
    result = []
    for stop in trip.get_route().stops:
        stop_departure = trip.departure + timedelta(hours=stop.time.hour, minutes=stop.time.minute)
        result.append({"departure": stop_departure.strftime("%Y-%m-%dT%H:%M"),
                       "station": {
                           "name": stop.station.name,
                           "id": stop.station.id
                       }})
    return result
