import json
from datetime import datetime
from typing import List

import requests

use_rest_APIs = True


# ================== Data-Object Classes ================== #


class Maintenance(object):
    def __init__(self, start, end):
        self.start = datetime.strptime(start, "%Y-%m-%dT%H:%M")
        self.end = datetime.strptime(end, "%Y-%m-%dT%H:%M")

    def __repr__(self):
        return f"<Maintenance: {self.start=}, {self.end=}>"


# checks if any of the trips in the list overlaps with a given daterange
def booked_trips_overlapping_trip(trips: list, trip_start: datetime, trip_end: datetime):
    for trip in trips:
        if trip_start < trip.get_arrival() and trip_end > trip.departure:
            return True
    return False


class Train(object):
    def __init__(self, id_: int, track_width: str, maintenances: List[Maintenance]):
        self.id = id_
        self.track_width = track_width
        self.maintenances = maintenances

    def __repr__(self):
        return f"<Train: {self.id=}, {self.track_width=}, {self.maintenances=}>"

    def booked_maintenance_overlapping_trip(self, trip_start: datetime, trip_end: datetime):
        for maintenance in self.maintenances:
            if trip_start < maintenance.end and trip_end > maintenance.start:
                return True
        return False

    def is_bookable_for_trip(self, trips, trip):
        return self.track_width == get_route_by_id(trip.route_id).track_width \
               and not self.booked_maintenance_overlapping_trip(trip.departure, trip.get_arrival()) \
               and not booked_trips_overlapping_trip(trips, trip.departure, trip.get_arrival())


class Station(object):
    def __init__(self, id_: int, name: str):
        self.id = id_
        self.name = name

    def __repr__(self):
        return f"<Station: {self.id=}, {self.name=}>"


class Stop(object):
    def __init__(self, time_str: str, station: Station):
        self.time = datetime.strptime(time_str, "%H:%M").time()
        self.station = station

    def __repr__(self):
        return f"<Stop: {self.time=}, {self.station=}>"


class Route(object):
    def __init__(self, id_: int, track_width: str, costs: float, stops: List[Stop]):
        self.id = id_
        self.track_width = track_width
        self.costs = costs
        self.stops = stops

    def __repr__(self):
        return f"<Route: {self.id=}, {self.track_width=}, {self.costs=}, {self.stops=}>"


# ================== Decoders from Dicts to Objects ================== #


def route_decoder(route_dict):
    return Route(route_dict['id'],
                 route_dict['width'],
                 route_dict['costs'],
                 [Stop(x["time"], Station(x['station']['id'], x['station']['name'])) for x in route_dict["stops"]]
                 )


def train_decoder(train_dict):
    return Train(train_dict['id'],
                 train_dict['track_width'],
                 [Maintenance(x["start"], x["end"]) for x in train_dict["booked_maintenance"]]
                 )


# ================== Fetching Methods ================== #


def get_route_by_id(route_id: int) -> 'Route':
    if use_rest_APIs:
        response = requests.get(f"http://127.0.0.1:5001/api/route/{route_id}")
        return route_decoder(response.json()) if response.status_code == 200 else None
    filtered_routes = [route for route in get_dummy_routes() if int(route_id) == route.id]
    return None if len(filtered_routes) == 0 else filtered_routes[0]


def get_routes() -> List[Route]:
    if use_rest_APIs:
        response = requests.get("http://127.0.0.1:5001/api/route")
        return [route_decoder(x) for x in response.json()] if response.status_code == 200 else []
    return get_dummy_routes()


def get_train_by_id(train_id: int) -> 'Train':
    if use_rest_APIs:
        response = requests.get(f"http://127.0.0.1:5000/api/train/{train_id}")
        return train_decoder(response.json()) if response.status_code == 200 else None
    filtered_trains = [train for train in get_dummy_trains() if train.id == train_id]
    return None if len(filtered_trains) == 0 else filtered_trains[0]


def get_trains() -> List[Train]:
    if use_rest_APIs:
        response = requests.get("http://127.0.0.1:5000/api/train")
        return [train_decoder(x) for x in response.json()] if response.status_code == 200 else []
    return get_dummy_trains()


# ================== Dummy Data ================== #

def get_dummy_trains() -> List[Train]:
    train_dicts = [{
        "id": 99998,
        "track_width": "METERSPUR",
        "seats": 0,
        "booked_maintenance":
            [{"start": "2021-12-05T00:00", "end": "2021-12-06T00:00"}]
    }, {
        "id": 99999,
        "track_width": "SCHMALSPUR",
        "booked_maintenance":
            [{"start": "2021-12-07T00:00", "end": "2021-12-07T06:00"}]
    }]

    return [train_decoder(json.loads(json.dumps(x))) for x in train_dicts]


def get_dummy_routes() -> List[Route]:
    route1 = {
        "width": "METERSPUR",
        "id": 777777,
        "costs": 20.35,
        "stops": [
            {
                "time": "00:00",
                "station": {
                    "id": 999999,
                    "name": "Linz Hauptbahnhof"
                }
            },
            {
                "time": "00:15",
                "station": {
                    "id": 999998,
                    "name": "Linz Frankstraße"
                }
            },
            {
                "time": "00:32",
                "station": {
                    "id": 999997,
                    "name": "Katsdorf"
                }
            },
            {
                "time": "01:35",
                "station": {
                    "id": 999996,
                    "name": "Summerau"
                }
            }
        ]
    }

    route2 = {
        "width": "SCHMALSPUR",
        "id": 888888,
        "costs": 25,
        "stops": [
            {
                "time": "00:00",
                "station": {
                    "id": 999997,
                    "name": "Katsdorf"
                }
            },
            {
                "time": "00:15",
                "station": {
                    "id": 999998,
                    "name": "Linz Frankstraße"
                }
            },
            {
                "time": "00:28",
                "station": {
                    "id": 999999,
                    "name": "Linz Hauptbahnhof"
                }
            }
        ]
    }
    route3 = {
        "width": "METERSPUR",
        "id": 888889,
        "costs": 10.2,
        "stops": [
            {
                "time": "00:00",
                "station": {
                    "id": 1498731,
                    "name": "Amstetten"
                }
            },
            {
                "time": "00:12",
                "station": {
                    "id": 509112,
                    "name": "Stadt Haag"
                }
            },
            {
                "time": "00:22",
                "station": {
                    "id": 578903,
                    "name": "St. Valentin"
                }
            }
        ]
    }
    route4 = {
        "width": "METERSPUR",
        "id": 888890,
        "costs": 27.5,
        "stops": [
            {
                "time": "00:00",
                "station": {
                    "id": 789902,
                    "name": "Passau"
                }
            },
            {
                "time": "00:15",
                "station": {
                    "id": 987423,
                    "name": "Gerlingen"
                }
            },
            {
                "time": "00:28",
                "station": {
                    "id": 984323,
                    "name": "Linz Mühlkreisbahnhof"
                }
            }
        ]
    }
    route5 = {
        "width": "METERSPUR",
        "id": 888891,
        "costs": 999.99,
        "stops": [
            {
                "time": "00:00",
                "station": {
                    "id": 984323,
                    "name": "Hintertupfing"
                }
            },
            {
                "time": "05:12",
                "station": {
                    "id": 892344,
                    "name": "Hogwarts"
                }
            },
            {
                "time": "12:45",
                "station": {
                    "id": 777322,
                    "name": "Entenhausen"
                }
            }
        ]
    }
    route_dicts = [
        route1,
        route2,
        route3,
        route4,
        route5
    ]
    return [route_decoder(json.loads(json.dumps(x))) for x in route_dicts]
