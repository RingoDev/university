from flask import Flask
from config import Config
from flask_sqlalchemy import SQLAlchemy
from flask_migrate import Migrate
from flask_login import LoginManager

app = Flask(__name__, static_folder="styles", static_url_path="/styles")
app.config.from_object(Config)
db = SQLAlchemy(app)
migrate = Migrate(app, db, render_as_batch=True)
login = LoginManager(app)
login.login_view = 'login'

from app import routes, models



# # create admin user if non exists in db
# adminUser = models.User.query.filter_by(username="admin").first()
# if adminUser is None:
#     print("adding admin user with password 12345")
#     user = models.User(username="admin", role="admin")
#     user.set_password("12345")
#     db.session.add(user)
#     db.session.commit()