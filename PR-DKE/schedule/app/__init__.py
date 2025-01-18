from flask import Flask
from flask_login import LoginManager
from flask_migrate import Migrate
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy import event
from sqlalchemy.engine import Engine

from config import Config


@event.listens_for(Engine, "connect")
def set_sqlite_pragma(dbapi_connection, _):
    cursor = dbapi_connection.cursor()
    cursor.execute("PRAGMA foreign_keys=ON")
    cursor.close()


app = Flask(__name__, static_folder="static", static_url_path="/static")
app.config.from_object(Config)

db = SQLAlchemy(app)

# render as batch needed for sqlLite database
migrate = Migrate(app, db, render_as_batch=True)
login = LoginManager(app)
login.login_view = 'login'

from app.models import models
from app.routes import routes, trips, employee, general, api, work_schedule
from app import errors

# # create admin user if non exists in db
# adminUser = models.User.query.filter_by(username="admin").first()
# if adminUser is None:
#     print("adding admin user with password 12345")
#     user = models.User(username="admin", role=models.Role.admin, first_name="admin", last_name="admin", password="12345")
#     db.session.add(user)
#     db.session.commit()
