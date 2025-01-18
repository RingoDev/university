from flask_wtf import FlaskForm
from wtforms import StringField, PasswordField, BooleanField, SubmitField, SelectField, RadioField, FloatField, \
    IntegerField, SelectMultipleField
from wtforms.validators import ValidationError, DataRequired, Email, EqualTo

from app.models import User


class LoginForm(FlaskForm):
    username = StringField('Username', validators=[DataRequired()])
    password = PasswordField('Password', validators=[DataRequired()])
    remember_me = BooleanField('Remember Me')
    submit = SubmitField('Sign In')


class StationForm(FlaskForm):
    designation = StringField('Designation', validators=[DataRequired()])
    road = StringField('Road', validators=[DataRequired()])
    place = StringField('Place', validators=[DataRequired()])
    postal = StringField('Postal', validators=[DataRequired()])
    submit = SubmitField('Anlegen')


class RegistrationForm(FlaskForm):
    username = StringField('Username', validators=[DataRequired()])
    email = StringField('Email', validators=[DataRequired(), Email()])
    password = PasswordField('Password', validators=[DataRequired()])
    password2 = PasswordField(
        'Repeat Password', validators=[DataRequired(), EqualTo('password')])
    role = SelectField(u'Role', choices=[('admin', 'admin'), ('employee', 'employee')])
    submit = SubmitField('Register')

    def validate_username(self, username):
        user = User.query.filter_by(username=username.data).first()
        if user is not None:
            raise ValidationError('Please use a different username.')

    def validate_email(self, email):
        user = User.query.filter_by(email=email.data).first()
        if user is not None:
            raise ValidationError('Please use a different email address.')


class EditStationForm(FlaskForm):
    designation = StringField('Designation')
    road = StringField('Road')
    place = StringField('Place')
    postal = StringField('Postal')
    submit = SubmitField('Edit')


class DeleteStationForm(FlaskForm):
    id = SelectField(u'Designation')
    submit = SubmitField('Delete')


class EditSectionForm(FlaskForm):
    name = StringField('name', validators=[DataRequired()])
    startStation = SelectField(u'startStation')
    endStation = SelectField(u'endStation')
    distance = FloatField('distance')
    maxSpeed = IntegerField('maxSpeed')
    width = RadioField('width', choices=[('SCHMALSPUR', 'Schmalspur'), ('METERSPUR', 'Meterspur')])
    usageFee = FloatField('usageFee')
    submit = SubmitField('Edit')


class EditRouteForm(FlaskForm):
    startStation = SelectField(u'Start-Station')
    endStation = SelectField(u'End-Station')
    sections = SelectMultipleField(u'Sections')
    submit = SubmitField('Anlegen')


class WarningForm(FlaskForm):
    description = StringField('Description', validators=[DataRequired()])
    section = SelectField(u'Section')
    submit = SubmitField('Anlegen')
