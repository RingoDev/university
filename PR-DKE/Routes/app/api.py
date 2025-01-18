import time
import datetime

from flask import jsonify, abort

from app import app
from app.models import Station, Section, Route, Warning


#this method returns all stations
#as an json object
@app.route('/api/stations', methods=['GET'])
def get_stations():
    stations = Station.query.all()
    stationArray = []

    for s in stations:
        sObj = {}
        sObj['id'] = s.id
        sObj['name'] = s.name
        sObj['road'] = s.road
        sObj['place'] = s.place
        sObj['postal'] = s.postal
        stationArray.append(sObj)

    return jsonify({'stations': stationArray})

#this method returns the information of a station
#based on their id, as a json object
@app.route('/api/station/<station_id>', methods=['GET'])
def get_station(station_id):
    station = Station.query.filter_by(id=station_id).all()
    stationArray = []

    for s in station:
        sObj = {}
        sObj['id'] = s.id
        sObj['name'] = s.name
        sObj['road'] = s.road
        sObj['place'] = s.place
        sObj['postal'] = s.postal
        stationArray.append(sObj)

    return jsonify({'station': stationArray})

#this method returns all sections
#as an json object
@app.route('/api/sections', methods=['GET'])
def get_sections():
    sections = Section.query.all()
    sectionArray = []

    for s in sections:
        sObj = {}
        sObj['id'] = s.id
        sObj['name'] = s.name
        sObj['startStation'] = s.startStation
        sObj['endStation'] = s.endStation
        sObj['distance'] = s.distance
        sObj['maxSpeed'] = s.maxSpeed
        sObj['width'] = s.width
        sObj['usageFee'] = s.usageFee

        sectionArray.append(sObj)

    return jsonify({'sections': sectionArray})

#this method returns the information of a section
#based on their id, as a json object
@app.route('/api/section/<section_id>', methods=['GET'])
def get_section(section_id):
    section = Section.query.filter_by(id=section_id).first()
    sectionArray = []

    sObj = {}
    sObj['id'] = section.id
    sObj['name'] = section.name
    sObj['startStation'] = section.startStation
    sObj['endStation'] = section.endStation
    sObj['distance'] = section.distance
    sObj['maxSpeed'] = section.maxSpeed
    sObj['width'] = section.width
    sObj['usageFee'] = section.usageFee

    sectionArray.append(sObj)

    return jsonify({'sections': sectionArray})


#this method returns a route json object
# based on the method route_to_route_dict
#if the route does not exist, an error occurs
@app.route('/api/route/<route_id>', methods=['GET'])
def get_route(route_id):
    route = Route.query.filter_by(id=int(route_id)).first()
    if route is None:
        abort(404)
    result = jsonify(route_to_route_dict(route))
    return result


#this method shows all routes as an json object
#the routes are defined by the metho route_to_route_dict
@app.route('/api/route', methods=['GET'])
def get_routes():
    routes = Route.query.all()
    route_array = []
    for route in routes:
        route_array.append(route_to_route_dict(route))
    return jsonify(route_array)

#this method creates an object,
#where all the sections are connected via
#stations, plus the cost, width and duration
def route_to_route_dict(route: Route):
    route_dict = {
        "id": route.id,
        "stops": [],
    }

    costs = 0
    total_traveling_duration = datetime.timedelta()
    sections = route.routeSection.all()
    # set route width to width of first section
    route_dict["width"] = sections[0].width

    # add start stations of all section
    for section in sections:
        station = Station.query.filter_by(name=section.startStation).first()
        my_time = datetime.datetime(year=2020, month=1, day=1, hour=0, minute=0)

        route_dict["stops"].append({
            "station": {
                "id": station.id,
                "name": station.name
            },
            "time": (my_time + total_traveling_duration).time().strftime("%H:%M")
        })

        # increasing total travel duration and costs
        total_traveling_duration = total_traveling_duration + datetime.timedelta(
            milliseconds=(section.distance / section.maxSpeed) * 60 * 60 * 1000)
        costs = costs + section.usageFee

    # --- add end station of section ---
    end_station = Station.query.filter_by(name=sections[-1].endStation).first()
    route_dict["stops"].append({
        "station": {
            "id": end_station.id,
            "name": end_station.name
        },
        "time": (datetime.datetime(year=2020, month=1, day=1, hour=0,
                                   minute=0) + total_traveling_duration).time().strftime("%H:%M")
    })

    # rounding to 2 digits
    route_dict["costs"] = round(costs, 2)
    return route_dict


#this method lists all the warnings as
#a json object
@app.route('/api/warning', methods=['GET'])
def get_warnings():
    warnings = Warning.query.all()
    warningArray = []

    for w in warnings:
        sObj = {}
        sObj['id'] = w.id
        sObj['description'] = w.description
        section = Section.query.filter_by(name=w.section).first()
        sObj['section'] = section.id
        warningArray.append(sObj)

    return jsonify(warningArray)


#this method shows all warnings as json object
# for one section, based on their id
@app.route('/api/warning/<section_id>', methods=['GET'])
def get_warning(section_id):
    warnings = Warning.query.all()
    warningArray = []
    section = Section.query.filter_by(id=section_id).first()
    for w in warnings:
        sObj = {}
        if w.section == section.name:
            sObj = {}
            sObj['id'] = w.id
            sObj['description'] = w.description
            sObj['section'] = section.id
            warningArray.append(sObj)

    return jsonify(warningArray)
