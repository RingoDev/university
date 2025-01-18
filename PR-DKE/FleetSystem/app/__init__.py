import logging
import os
from logging.handlers import RotatingFileHandler

from flask import Flask
from sqlalchemy import event
from sqlalchemy.engine import Engine

from config import Config
from flask_sqlalchemy import SQLAlchemy
from flask_migrate import Migrate
from flask_login import LoginManager


# to allow foreign key cascades in sqllite db
@event.listens_for(Engine, "connect")
def set_sqlite_pragma(dbapi_connection, connection_record):
    cursor = dbapi_connection.cursor()
    cursor.execute("PRAGMA foreign_keys=ON")
    cursor.close()


app = Flask(__name__)
app.config.from_object(Config)
db = SQLAlchemy(app)
login = LoginManager(app)
login.login_view = 'login'

from app import models

# render as batch needed for sqlLite database
migrate = Migrate(app, db, render_as_batch=True)

from app import api, routes, errors

# create admin user if non exists in db

# adminUser = models.User.query.filter_by(username="admin").first()
# if adminUser is None:
#     print("adding admin user with password 12345")
#     user = models.User(username="admin", role="admin")
#     user.set_password("12345")
#     db.session.add(user)
#     db.session.commit()

if not app.debug:
    if not os.path.exists('logs'):
        os.mkdir('logs')
    file_handler = RotatingFileHandler('logs/fleetSystem.log', maxBytes=10240,
                                       backupCount=10)
    file_handler.setFormatter(logging.Formatter(
        '%(asctime)s %(levelname)s: %(message)s [in %(pathname)s:%(lineno)d]'))
    file_handler.setLevel(logging.INFO)
    app.logger.addHandler(file_handler)

    app.logger.setLevel(logging.INFO)
    app.logger.info('Fleetsystem startup')
