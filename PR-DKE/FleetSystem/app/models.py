import enum

from flask_login import UserMixin
from werkzeug.security import generate_password_hash, check_password_hash

from app import db, login


@login.user_loader
def load_user(id):
    return User.query.get(int(id))


class UserRole(enum.Enum):
    admin = 'admin'
    employee = 'employee'


class User(UserMixin, db.Model):
    __tablename__ = 'user'
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(64), index=True, unique=True)
    email = db.Column(db.String(120), index=True, unique=True)
    password_hash = db.Column(db.String(128))
    role = db.Column(db.Enum(UserRole), server_default="employee", nullable=False)

    def __repr__(self):
        return '<User {}>'.format(self.username)

    def set_password(self, password):
        self.password_hash = generate_password_hash(password)

    def check_password(self, password):
        return check_password_hash(self.password_hash, password)

    def is_admin(self):
        if self.role == UserRole.admin:
            return True
        else:
            return False

    @login.user_loader
    def load_user(id):
        return User.query.get(int(id))


class Maintenance(db.Model):
    __tablename__ = 'maintenance'
    id = db.Column(db.Integer, primary_key=True)
    type = db.Column(db.String(64), nullable=False)
    start_date = db.Column(db.DATETIME, nullable=False)
    end_date = db.Column(db.DATETIME, nullable=False)
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'), nullable=False)
    train_id = db.Column(db.Integer, db.ForeignKey('train.id'))
    train = db.relationship("Train", back_populates="maintenances")

    def __repr__(self):
        return ' {} {} '.format(self.id, self.type)

    def set_type(self, type):
        self.type = type

    def set_start_date(self, start_date):
        self.start_date = start_date

    def set_end_date(self, end_date):
        self.end_date = end_date

    def set_user(self, user_id):
        self.user_id = user_id


class TrackWidth(enum.Enum):
    METERSPUR = 'METERSPUR'
    SCHMALSPUR = 'SCHMALSPUR'

    def __str__(self):
        return self.name  # value string

    def __html__(self):
        return self.value  # label string


class EngineWagon(db.Model):
    __tablename__ = 'engine_wagon'
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(32), unique=True)
    track_width = db.Column(db.Enum(TrackWidth), server_default="METERSPUR", nullable=False)
    max_traction = db.Column(db.Integer, nullable=False)

    def __repr__(self):
        return ' {} {} {} {} '.format(self.id, self.name, self.track_width, self.max_traction)

    def set_name(self, name):
        self.name = name

    def set_track_width(self, track_width):
        self.track_width = track_width

    def set_max_traction(self, max_traction):
        self.max_traction = max_traction

    def has_track_width_meterspur(self):
        return self.track_width == TrackWidth.METERSPUR


class PassengerWagon(db.Model):
    __tablename__ = 'passenger_wagon'
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(32), unique=True)
    track_width = db.Column(db.Enum(TrackWidth), server_default="METERSPUR", nullable=False)
    seats = db.Column(db.Integer, nullable=False)
    max_capacity = db.Column(db.Integer, nullable=False)
    train_id = db.Column(db.Integer, db.ForeignKey('train.id', ondelete="SET NULL"))
    train = db.relationship("Train", back_populates="passenger_waggons")

    def __repr__(self):
        return ' {} {} {} '.format(self.id, self.name, self.track_width)

    def set_name(self, name):
        self.name = name

    def set_track_width(self, track_width):
        self.track_width = track_width

    def set_seats(self, seats):
        self.seats = seats

    def set_max_capacity(self, max_capacity):
        self.max_capacity = max_capacity

    def has_track_width_meterspur(self):
        return self.track_width == TrackWidth.METERSPUR


class Train(db.Model):
    __tablename__ = 'train'
    id = db.Column(db.Integer, primary_key=True)
    number = db.Column(db.Integer, nullable=False, unique=True)
    engine_wagon_id = db.Column(db.Integer, db.ForeignKey(EngineWagon.id), nullable=False)
    passenger_waggons = db.relationship("PassengerWagon", back_populates="train")
    maintenances = db.relationship("Maintenance", back_populates="train")

    def __repr__(self):
        return ' <{} {} {} {}> '.format(self.id, self.number, self.engine_wagon_id, self.passenger_waggons)

    def set_number(self, number):
        self.number = number

    def set_engine_wagon_id(self, engine_wagon_id):
        self.engine_wagon_id = engine_wagon_id
