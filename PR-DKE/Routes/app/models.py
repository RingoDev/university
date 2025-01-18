import enum
from datetime import datetime
from app import db, login
from werkzeug.security import generate_password_hash, check_password_hash
from flask_login import UserMixin

#this method loads the user based on id
@login.user_loader
def load_user(id):
    return User.query.get(int(id))

#this enum shows the possible values for the user
#admin or employee
class UserRole(enum.Enum):
    admin = 'admin'
    employee = 'employee'


#this class creates the table user,
#it also integrates the enum role
class User(UserMixin, db.Model):
    id = db.Column(db.Integer, primary_key=True)
    username = db.Column(db.String(64), index=True, unique=True)
    email = db.Column(db.String(120), index=True, unique=True)
    password_hash = db.Column(db.String(128))
    posts = db.relationship('Post', backref='author', lazy='dynamic')
    role = db.Column(db.Enum(UserRole), server_default="employee", nullable=False)

    def __repr__(self):
        return '<User {}>'.format(self.username)

    def set_password(self, password):
        self.password_hash = generate_password_hash(password)

    def check_password(self, password):
        return check_password_hash(self.password_hash, password)

    def is_admin(self):
        return self.role == UserRole.admin

#this class creates the table post
class Post(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    body = db.Column(db.String(140))
    timestamp = db.Column(db.DateTime, index=True, default=datetime.utcnow)
    user_id = db.Column(db.Integer, db.ForeignKey('user.id'))

    def __repr__(self):
        return '<Post {}>'.format(self.body)


#this class generates the table station
class Station(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(64), index=True, unique=True)
    road = db.Column(db.String(120))
    place = db.Column(db.String(120))
    postal = db.Column(db.String(120))


#this class generates the table section
class Section(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(64), index=True, unique=True)
    startStation = db.Column(db.Integer, db.ForeignKey('station.id'))
    endStation = db.Column(db.Integer, db.ForeignKey('station.id'))
    distance = db.Column(db.Float)
    maxSpeed = db.Column(db.Integer)
    width = db.Column(db.String(64))
    usageFee = db.Column(db.Float)


#this class is the connection-class between
#routes and section
routeSection = db.Table('routeSection',
                        db.Column('id', db.Integer, primary_key=True),
                        db.Column('section_id', db.Integer, db.ForeignKey('section.id')),
                        db.Column('route_id', db.Integer, db.ForeignKey('routes.id'))
                        )

#this class creates the table routes,
#it also displays the relationship to routeSection
class Route(db.Model):
    __tablename__ = 'routes'
    id = db.Column(db.Integer, primary_key=True)
    startStation = db.Column(db.Integer, db.ForeignKey('station.id'))
    endStation = db.Column(db.Integer, db.ForeignKey('station.id'))
    routeSection = db.relationship('Section', secondary=routeSection, backref = db.backref('routes', lazy='dynamic'),
                                   lazy='dynamic')


#this class creates warnings
class Warning(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    description = db.Column(db.String)
    section = db.Column(db.Integer, db.ForeignKey('section.id'))







