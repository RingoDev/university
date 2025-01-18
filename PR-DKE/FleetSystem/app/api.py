import json
from typing import List

from app import app
from app.models import Train, Maintenance, EngineWagon

# creates a dictionary from a train object
from app.models import PassengerWagon


def train_to_dict(train: Train):
    engine_w: EngineWagon = EngineWagon.query.filter_by(id=train.engine_wagon_id).first()
    train_dict = {
        "id": train.id,
        "track_width": str(engine_w.track_width),
        "booked_maintenance": [],
        "seats": 0
    }

    # add up seats of passenger waggons
    for wagon in train.passenger_waggons:
        train_dict["seats"] = train_dict["seats"] + wagon.seats

    # add maintenances to dict
    for maintenance in train.maintenances:
        train_dict["booked_maintenance"].append(
            {"start": maintenance.start_date.strftime("%Y-%m-%dT%H:%M"),
             "end": maintenance.end_date.strftime("%Y-%m-%dT%H:%M")})
    return train_dict


@app.route('/api/train', methods=['GET'])
def get_trains():
    trains = Train.query.all()
    trains_list = []
    for train in trains:
        trains_list.append(train_to_dict(train))
    return json.dumps(trains_list)


@app.route('/api/train/<train_id>', methods=['GET'])
def get_train(train_id):
    train = Train.query.filter_by(id=train_id).first()
    return json.dumps(train_to_dict(train))
