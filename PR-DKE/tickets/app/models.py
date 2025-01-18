import enum

from flask import url_for, render_template
from werkzeug.utils import redirect

from app import db
from app import login
from werkzeug.security import generate_password_hash, check_password_hash
from flask_login import UserMixin

from app.requests import get_trip_by_id


class UserRoleEnum(enum.Enum):
    admin = 'admin'
    client = 'client'

class User(UserMixin, db.Model):
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(64), index=True, unique=True)
    password_hash = db.Column(db.String(128))
    role = db.Column(db.Enum(UserRoleEnum), nullable=False, server_default='client')

    def __repr__(self):
        return '<User {}>'.format(self.username)

    def set_password(self, password):
        self.password_hash = generate_password_hash(password)

    def check_password(self, password):
        return check_password_hash(self.password_hash, password)

    def is_admin(self):
        if self.role == UserRoleEnum.admin:
            return True
        else:
            return False

    @login.user_loader
    def load_user(id):
        return User.query.get(int(id))

class TicketStatus(enum.Enum):
    active = 'active'
    used = 'used'
    cancelled = 'cancelled'

class Ticket(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    state = db.Column(db.Enum(TicketStatus), nullable=False, server_default='active')
    trip_id = db.Column(db.Integer)
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'))

    def __repr__(self):
        return '<Ticket {}>'.format(self.body)

    def set_state(self, state):
        self.state = state

    def get_trip(self):
        return get_trip_by_id(self.trip_id)

class Promotion(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    percent = db.Column(db.Integer)
    start_date = db.Column(db.Date)
    end_date = db.Column(db.Date)
    route_id = db.Column(db.Integer)

    def __repr__(self):
        return '<Promotion {} {} {} {} {} >'.format(self.id,self.percent,self.start_date,self.end_date,self.route_id)

    def set_start(self, start_date):
        self.start_date = start_date

    def set_end(self, end_date):
        self.end_date = end_date

    def set_route(self, route_id):
        self.route_id = route_id

    def set_percent(self, percent):
        self.percent = percent

class Reservation(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    ticket_id = db.Column(db.Integer)
    trip_id = db.Column(db.Integer)



