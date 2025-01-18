import requests

dynamic = True


# ************************************Dummy Data************************************
def get_all_routes():
    if dynamic:
        return get_all_routes_dynamic()
    return [{
        "id": 777777,
        "stops": [
            {
                "time": 0,
                "station": {
                    "id": 999999,
                    "name": "Linz Hauptbahnhof"
                }
            },
            {
                "time": 30000,
                "station": {
                    "id": 999998,
                    "name": "Linz Frankstraße"
                }
            },
            {
                "time": 60000,
                "station": {
                    "id": 999997,
                    "name": "Katsdorf"
                }
            },
            {
                "time": 90000,
                "station": {
                    "id": 999996,
                    "name": "Summerau"
                }
            }
        ]
    },
        {
            "id": 888888,
            "stops": [
                {
                    "time": 60000,
                    "station": {
                        "id": 999997,
                        "name": "Katsdorf"
                    }
                },
                {
                    "time": 30000,
                    "station": {
                        "id": 999998,
                        "name": "Linz Frankstraße"
                    }
                },
                {
                    "time": 0,
                    "station": {
                        "id": 999999,
                        "name": "Linz Hauptbahnhof"
                    }
                }
            ]
        }
    ]


def get_all_trips():
    if dynamic:
        return get_all_trips_dynamic()
    return [{
        "id": 111111,
        "routeId": 777777,
        "trainId": 50,
        "price": 15.5,
        "stops": [
            {
                "departure": "2021-12-03T18:14",
                "station": {
                    "id": 999999,
                    "name": "Linz Hauptbahnhof"
                }
            },
            {
                "departure": "2021-12-03T18:29",
                "station": {
                    "id": 999998,
                    "name": "Linz Frankstraße"
                }
            },
            {
                "departure": "2021-12-03T18:46",
                "station": {
                    "id": 999997,
                    "name": "Katsdorf"
                }
            },
            {
                "departure": "2021-12-03T19:49",
                "station": {
                    "id": 999996,
                    "name": "Summerau"
                }
            }
        ]
    },
        {
            "id": 222222,
            "routeId": 888888,
            "trainId": 37,
            "price": 35.70,
            "stops": [
                {
                    "departure": "2022-01-29T09:35",
                    "station": {
                        "id": 999999,
                        "name": "Linz Hauptbahnhof"
                    }
                },
                {
                    "departure": "2022-01-29T09:55",
                    "station": {
                        "id": 999996,
                        "name": "Amstetten"
                    }
                },
                {
                    "departure": "2022-01-29T10:11",
                    "station": {
                        "id": 999997,
                        "name": "St. Pölten Hauptbahnhof"
                    }
                },
                {
                    "departure": "2022-01-29T10:55",
                    "station": {
                        "id": 999998,
                        "name": "Wien Hauptbahnhof"
                    }
                },
            ]
        },
        {
            "id": 111111,
            "routeId": 777777,
            "trainId": 50,
            "price": 100,
            "stops": [
                {
                    "departure": "2022-01-01T18:14",
                    "station": {
                        "id": 999999,
                        "name": "Linz Hauptbahnhof"
                    }
                },
                {
                    "departure": "2022-01-01T18:29",
                    "station": {
                        "id": 999998,
                        "name": "Linz Frankstraße"
                    }
                },
                {
                    "departure": "2022-01-01T18:46",
                    "station": {
                        "id": 999997,
                        "name": "Katsdorf"
                    }
                },
                {
                    "departure": "2022-01-01T19:49",
                    "station": {
                        "id": 999996,
                        "name": "Ried"
                    }
                }
            ]
        }
    ]


def get_all_trains():
    if dynamic:
        return get_all_trains_dynamic()
    return [{
        "id": 99998,
        "name": "REX 2598",
        "trackWidth": 1,
        "seats": 2,
        "maintenances": [
            {
                "from": 1635931300000,
                "to": 1635931400000
            },
            {
                "from": 1635932500000,
                "to": 1635932600000
            }
        ]
    },
        {
            "id": 37,
            "name": "ICE 1234",
            "trackWidth": 1,
            "seats": 1,
            "maintenances": [
                {
                    "from": 1635931500000,
                    "to": 1635931600000
                },
                {
                    "from": 1635933000000,
                    "to": 1635933100000
                }
            ]
        }

    ]


def get_trip_by_id(trip_id):
    if dynamic:
        return get_trip_by_id_dynamic(trip_id)
    trips = get_all_trips()
    for trip in trips:
        if str(trip["id"]) == str(trip_id):
            return trip


def get_train_by_id(train_id):
    if dynamic:
        return get_train_by_id_dynamic(train_id)
    trains = get_all_trains()
    for train in trains:
        if str(train["id"]) == str(train_id):
            return train


def get_warnings(route_id):
    if dynamic:
        return get_warnings_by_route_id_dynamic(route_id)
    if int(route_id) % 2 == 1:
        return [
            {
                "id": 13,
                "description": "Es kommt zu Verzögerungen aufgrund von Bauarbeiten"
            }
        ]
    else:
        return []


# ************************************APIs to other systems************************************

def get_all_trips_dynamic():
    response = requests.get(url="http://127.0.0.1:5002/api/trip")
    return response.json() if response.status_code == 200 else []


def get_trip_by_id_dynamic(trip_id):
    response = requests.get(url="http://127.0.0.1:5002/api/trip/" + str(trip_id))
    return response.json() if response.status_code == 200 else None


def get_all_routes_dynamic():
    response = requests.get(url="http://127.0.0.1:5001/api/route")
    return response.json() if response.status_code == 200 else []


def get_route_by_id_dynamic(route_id):
    response = requests.get(url="http://127.0.0.1:5001/api/route/" + str(route_id))
    return response.json() if response.status_code == 200 else None


def get_all_trains_dynamic():
    response = requests.get(url="http://127.0.0.1:5000/api/train")
    return response.json() if response.status_code == 200 else []


def get_train_by_id_dynamic(train_id):
    response = requests.get(url="http://127.0.0.1:5000/api/train/" + str(train_id))
    return response.json() if response.status_code == 200 else None


def get_all_warnings_dynamic():
    response = requests.get(url="http://127.0.0.1:5001/api/warning")
    return response.json() if response.status_code == 200 else []


def get_warnings_by_route_id_dynamic(route_id):
    response = requests.get(url="http://127.0.0.1:5001/api/warning/" + str(route_id))
    return response.json() if response.status_code == 200 else None
