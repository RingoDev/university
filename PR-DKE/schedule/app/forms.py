from flask_wtf import FlaskForm
from wtforms import PasswordField, BooleanField, SubmitField, StringField
from wtforms.fields.choices import SelectMultipleField, SelectField
from wtforms.fields.datetime import DateField, TimeField, DateTimeLocalField
from wtforms.fields.numeric import DecimalField
from wtforms.validators import DataRequired


class LoginForm(FlaskForm):
    username = StringField('Benutzername', validators=[DataRequired()])
    password = PasswordField('Passwort', validators=[DataRequired()])
    remember_me = BooleanField('Remember Me')
    submit = SubmitField('Sign In')


class TripBulkCreateForm(FlaskForm):
    start_date = DateField('Startdatum', validators=[DataRequired()])
    end_date = DateField('Enddatum', validators=[DataRequired()])
    departure = TimeField('Abfahrt', validators=[DataRequired()])
    price = DecimalField('Preis', validators=[DataRequired()])
    weekdays = SelectMultipleField("Wochentage",
                                   choices=['Mo', 'Di', 'Mi', 'Do', 'Fr', 'Sa', 'So'],
                                   validators=[DataRequired()])
    submit = SubmitField('Submit')


class TripCreateForm(FlaskForm):
    departure = DateTimeLocalField('Abfahrt', format='%Y-%m-%dT%H:%M', validators=[DataRequired()])
    price = DecimalField('Preis', validators=[DataRequired()])
    submit = SubmitField('Submit')


class TripEditForm(FlaskForm):
    price = DecimalField('Preis', validators=[DataRequired()])
    train = SelectField('Zug')
    employees = SelectMultipleField('Mitarbeiter')
    submit = SubmitField('Speichern')


class EmployeeCreateForm(FlaskForm):
    username = StringField('Benutzername', validators=[DataRequired()])
    first_name = StringField('Vorname', validators=[DataRequired()])
    last_name = StringField('Nachname', validators=[DataRequired()])
    password = PasswordField('Passwort', validators=[DataRequired()])
    submit = SubmitField('Erstellen')
