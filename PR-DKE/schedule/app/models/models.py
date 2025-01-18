import enum
from datetime import datetime, timedelta

from flask_login import UserMixin
from werkzeug.security import generate_password_hash, check_password_hash

from app import db
from app import login
from app.fetch import get_route_by_id, get_train_by_id


@login.user_loader
def load_user(id_):
    return User.query.get(int(id_))


class Role(enum.Enum):
    admin = "admin"
    employee = "employee"


employee_trip = db.Table(
    'employee_trip',
    db.Column('employee_id', db.ForeignKey('user.id')),
    db.Column('trip_id', db.ForeignKey('trip.id'))
)


class User(UserMixin, db.Model):
    __tablename__ = 'user'
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(64), index=True, unique=True)
    first_name = db.Column(db.String(64))
    last_name = db.Column(db.String(64))
    password_hash = db.Column(db.String(128))
    role = db.Column(db.Enum(Role), server_default="employee", nullable=False)
    trips = db.relationship(
        'Trip',
        secondary=employee_trip,
        cascade="all,delete",
        back_populates="employees"
    )

    def __init__(self, username: str, first_name: str, last_name: str, role: Role, password: str):
        self.username = username
        self.first_name = first_name
        self.last_name = last_name
        self.role = role
        self.password_hash = generate_password_hash(password)

    def set_password(self, password):
        self.password_hash = generate_password_hash(password)

    def check_password(self, password):
        return check_password_hash(self.password_hash, password)

    def is_admin(self):
        return self.role == Role.admin

    def __repr__(self):
        return '<User {} {} {} {} {}>'.format(self.username, self.first_name, self.last_name, self.role,
                                              self.password_hash)

    def booked_trips_overlapping_trip(self, trip: "Trip") -> bool:
        for booked_trip in self.trips:
            if trip.departure < booked_trip.get_arrival() and trip.get_arrival() > booked_trip.departure:
                # overlapping timeframes
                return True
        return False


class Trip(db.Model):
    __tablename__ = 'trip'
    id = db.Column(db.Integer, primary_key=True)
    route_id = db.Column(db.Integer, nullable=False)
    train_id = db.Column(db.Integer)
    departure = db.Column(db.DateTime, nullable=False, default=datetime.utcfromtimestamp(0))
    price = db.Column(db.Numeric, nullable=False)
    employees = db.relationship(
        'User',
        secondary=employee_trip,
        cascade="all,delete",
        back_populates="trips"
    )

    def __init__(self, route_id: int, departure: datetime, price: float):
        self.route_id = route_id
        self.departure = departure
        self.price = price

    def get_arrival(self):
        route = get_route_by_id(self.route_id)
        duration = route.stops[-1].time
        return self.departure + timedelta(hours=duration.hour, minutes=duration.minute)

    def get_price_as_euro_string(self):
        return "{:,.2f}€".format(self.price)

    def get_price(self):
        return round(float(self.price), 2)

    def get_datetime_as_string(self):
        return self.departure.strftime("%d.%m.%Y-%H:%M")

    def get_date_as_string(self):
        return self.departure.strftime("%d.%m.%Y")

    def get_time_as_string(self):
        return self.departure.strftime("%H:%M")

    def get_route(self):
        return get_route_by_id(self.route_id)

    def get_train(self):
        return get_train_by_id(self.train_id) if self.train_id is not None else None

    def __repr__(self):
        return f'<Trip {self.id} {self.route_id} {self.departure} {self.price} >'


class PricePresets(db.Model):
    __tablename__ = 'price_presets'
    route_id = db.Column(db.Integer, primary_key=True)
    intervals = db.relationship('PriceInterval',
                                cascade="all, delete",
                                passive_deletes=True)


class PriceInterval(db.Model):
    __tablename__ = 'price_interval'
    id = db.Column(db.Integer, primary_key=True)
    preset_id = db.Column(db.Integer, db.ForeignKey('price_presets.route_id', ondelete="CASCADE"))
    price = db.Column(db.Numeric, nullable=False)
    start = db.Column(db.Time, nullable=False)
    end = db.Column(db.Time, nullable=False)

    def get_price(self):
        return round(float(self.price), 2)

    def get_start_as_string(self):
        return self.start.strftime("%H:%M")

    def get_end_as_string(self):
        return self.end.strftime("%H:%M")
