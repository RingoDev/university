from datetime import datetime

from flask import render_template, flash, redirect, url_for, request
from flask_login import current_user, login_user, logout_user, login_required
from werkzeug.urls import url_parse

from app import app, db
from app.decorators import required_role
from app.forms import LoginForm, UserCreateForm
from app.models import User, EngineWagon, PassengerWagon, TrackWidth, Train, Maintenance


@app.route('/')
@app.route('/index')
@login_required
def index():
    return render_template('index.html', title='Home', user=user)


@app.route('/login', methods=['GET', 'POST'])
def login():
    if current_user.is_authenticated:
        return redirect(url_for('index'))
    form = LoginForm()
    if form.validate_on_submit():
        user = User.query.filter_by(username=form.username.data).first()
        if user is None or not user.check_password(form.password.data):
            flash('Invalid username or password')
            return redirect(url_for('login'))
        login_user(user, remember=form.remember_me.data)
        next_page = request.args.get('next')
        if not next_page or url_parse(next_page).netloc != '':
            next_page = url_for('index')
        return redirect(next_page)
    return render_template('login.html', title='Log in', form=form)


@app.route('/logout')
def logout():
    logout_user()
    return redirect(url_for('index'))


@app.route('/user_create', methods=['GET', 'POST'])
@login_required
@required_role
def user_create():
    form = UserCreateForm()
    if form.validate_on_submit():
        user = User(username=form.username.data, email=form.email.data)
        user.set_password(form.password.data)
        db.session.add(user)
        db.session.commit()
        flash('Benutzer erfolgreich angelegt!')
        return redirect(url_for('login'))
    return render_template('user_create.html', title='Benutzeranlage', form=form)


@app.route('/user/<username>')
@login_required
def user(username):
    user = User.query.filter_by(username=username).first_or_404()
    return render_template('user.html', user=user)


@login_required
def user(user_id):
    user = User.query.filter_by(user_id=user_id).first_or_404()
    return user


@app.route('/engine_wagon/create', methods=['GET', 'POST'])
@login_required
@required_role
def engine_wagon_create():
    if not request.form:
        return render_template('engine_wagon/engine_wagon_create.html',
                               user=user,
                               success=None,
                               title='Triebkraftwagen anlegen')
    else:
        form = request.form.to_dict()
        track_width = TrackWidth.SCHMALSPUR if form['Spurweite'] == "S" else TrackWidth.METERSPUR
        engine_wagon = EngineWagon(name=form['Bezeichnung'],
                                   track_width=track_width,
                                   max_traction=form['Max. Zugkraft'])
        db.session.add(engine_wagon)
        db.session.commit()
        return render_template('engine_wagon/engine_wagon_create.html',
                               user=user,
                               success=True,
                               engine_wagon=engine_wagon,
                               title='Triebkraftwagen anlegen')


@app.route('/engine_wagon/edit/<engine_wagon_id>', methods=['GET', 'POST'])
@login_required
@required_role
def engine_wagon_edit(engine_wagon_id):
    if not request.form:
        engine_wagon = EngineWagon.query.filter(EngineWagon.id == engine_wagon_id).first()
        return render_template('engine_wagon/engine_wagon_edit.html',
                               success=None,
                               engine_wagon=engine_wagon,
                               title='Triebkraftwagen bearbeiten')
    else:
        form = request.form.to_dict()
        name = form['Bezeichnung']
        track_width = TrackWidth.SCHMALSPUR if form['Spurweite'] == "S" else TrackWidth.METERSPUR
        max_traction = form['Max. Zugkraft']

        engine_wagon = EngineWagon.query.filter(EngineWagon.id == engine_wagon_id).first()

        engine_wagon.set_name(name)
        engine_wagon.set_track_width(track_width)
        engine_wagon.set_max_traction(max_traction)
        db.session.commit()
        return render_template('engine_wagon/engine_wagon_edit.html',
                               success=True,
                               engine_wagon=engine_wagon,
                               title='Triebkraftwagen bearbeiten')


@app.route('/engine_wagon/overview', methods=['GET', 'POST'])
@login_required
def engine_wagon_overview():
    data = EngineWagon.query.all()
    return render_template('engine_wagon/engine_wagon_overview.html',
                           success=None,
                           data=data,
                           title='Triebkraftwagenübersicht')


@app.route('/engine_wagon/delete/<engine_wagon_id>', methods=['GET', 'POST'])
@login_required
@required_role
def engine_wagon_delete(engine_wagon_id):
    #checks if engine_wagon is connected to train
    if Train.query.filter_by(engine_wagon_id=int(engine_wagon_id)).first() is not None:
        return render_template('engine_wagon/engine_wagon_overview.html', success=False)

    EngineWagon.query.filter(EngineWagon.id == engine_wagon_id).delete()
    db.session.commit()
    data = EngineWagon.query.all()
    return render_template('engine_wagon/engine_wagon_overview.html', success=True, data=data)


@app.route('/passenger_wagon/create', methods=['GET', 'POST'])
@login_required
@required_role
def passenger_wagon_create():
    if not request.form:
        return render_template('passenger_wagon/passenger_wagon_create.html',
                               user=user,
                               success=None,
                               title='Personenwagen anlegen')
    else:
        form = request.form.to_dict()
        track_width = TrackWidth.SCHMALSPUR if form['Spurweite'] == "S" else TrackWidth.METERSPUR
        passenger_wagon = PassengerWagon(name=form['Bezeichnung'],
                                         track_width=track_width,
                                         seats=form['Sitzanzahl'],
                                         max_capacity=form['Max. Gewicht'])
        db.session.add(passenger_wagon)
        db.session.commit()
        return render_template('passenger_wagon/passenger_wagon_create.html',
                               user=user,
                               success=True,
                               passenger_wagon=passenger_wagon,
                               title='Personenwagen anlegen')


@app.route('/passenger_wagon/edit/<passenger_wagon_id>', methods=['GET', 'POST'])
@login_required
@required_role
def passenger_wagon_edit(passenger_wagon_id):
    if not request.form:
        passenger_wagon = PassengerWagon.query.filter(PassengerWagon.id == passenger_wagon_id).first()
        return render_template('passenger_wagon/passenger_wagon_edit.html',
                               success=None,
                               passenger_wagon=passenger_wagon,
                               title='Personenwagen bearbeiten')
    else:
        form = request.form.to_dict()
        name = form['Bezeichnung']
        track_width = TrackWidth.SCHMALSPUR if form['Spurweite'] == "S" else TrackWidth.METERSPUR
        seats = form['Sitzanzahl']
        max_capacity = form['Max. Gewicht']

        passenger_wagon = PassengerWagon.query.filter(PassengerWagon.id == passenger_wagon_id).first()

        passenger_wagon.set_name(name)
        passenger_wagon.set_track_width(track_width)
        passenger_wagon.set_seats(seats)
        passenger_wagon.set_max_capacity(max_capacity)
        db.session.commit()
        return render_template('passenger_wagon/passenger_wagon_edit.html',
                               success=True,
                               passenger_wagon=passenger_wagon,
                               title='Personenwagen bearbeiten')


@app.route('/passenger_wagon/overview', methods=['GET', 'POST'])
@login_required
def passenger_wagon_overview():
    data = PassengerWagon.query.all()
    return render_template('passenger_wagon/passenger_wagon_overview.html',
                           success=None,
                           data=data,
                           title='Personenwagenübersicht')


@app.route('/passenger_wagon/delete/<passenger_wagon_id>', methods=['GET', 'POST'])
@login_required
@required_role
def passenger_wagon_delete(passenger_wagon_id):
    PassengerWagon.query.filter(PassengerWagon.id == passenger_wagon_id).delete()
    db.session.commit()
    data = PassengerWagon.query.all()
    return render_template('passenger_wagon/passenger_wagon_overview.html',
                           success=True,
                           data=data)


@app.route('/train/create', methods=['GET', 'POST'])
@login_required
@required_role
def train_create():
    if not request.form:
        return render_template('train/train_create.html',
                               user=user,
                               engine_wagons=EngineWagon.query.all(),
                               passenger_waggons=PassengerWagon.query.all(),
                               success=None,
                               title='Zug anlegen'
                               )
    else:
        form = request.form.to_dict()
        p_waggons = []
        for id in request.form.getlist(('Personenwaggons')):
            p_waggons.append(PassengerWagon.query.filter_by(id=int(id)).first())

        train = Train()
        train.number = int(form['Zugnummer'])
        train.engine_wagon_id = form['Triebkraftwagen']
        train.passenger_waggons = p_waggons

        db.session.add(train)

        engine_w: TrackWidth = EngineWagon.query.filter_by(id=train.engine_wagon_id).first()
        passenger_w: TrackWidth = PassengerWagon.query.filter_by(id=train.passenger_waggons[0].id).first()

        # checks if track_width from engine_wagon and first passenger_wagon is not equals
        if passenger_w.track_width != engine_w.track_width:
            return render_template('train/train_create.html',
                                   user=user,
                                   success=False,
                                   train=train,
                                   title='Zug anlegen')

        db.session.commit()
        # checks if track_width from engine_wagon and first passenger_wagon is equals
        if passenger_w.track_width == engine_w.track_width:
            return render_template('train/train_create.html', user=user, train=train, success=True)


@app.route('/train/edit/<train_id>', methods=['GET', 'POST'])
@login_required
@required_role
def train_edit(train_id):
    if not request.form:
        train = Train.query.filter(Train.id == train_id).first()
        return render_template('train/train_edit.html',
                               success=None,
                               engine_wagons=EngineWagon.query.all(),
                               passenger_waggons=PassengerWagon.query.all(),
                               train=train,
                               title='Zug bearbeiten')
    else:
        form = request.form.to_dict()
        number = form['Zugnummer']
        engine_wagon_id = form['Triebkraftwagen']
        p_waggons = []
        for id in request.form.getlist(('Personenwaggons')):
            p_waggons.append(PassengerWagon.query.filter_by(id=int(id)).first())

        train = Train.query.filter(Train.id == train_id).first()

        train.set_number(number)
        train.set_engine_wagon_id(engine_wagon_id)
        train.passenger_waggons = p_waggons

        engine_w: TrackWidth = EngineWagon.query.filter_by(id=train.engine_wagon_id).first()
        passenger_w: TrackWidth = PassengerWagon.query.filter_by(id=train.passenger_waggons[0].id).first()

        # checks if track_width from engine_wagon and first passenger_wagon is not equals
        if passenger_w.track_width != engine_w.track_width:
            return render_template('train/train_create.html',
                                   user=user,
                                   success=False,
                                   title='Zug bearbeiten')

        db.session.commit()
        # checks if track_width from engine_wagon and first passenger_wagon is equals
        if passenger_w.track_width == engine_w.track_width:
            return render_template('train/train_edit.html',
                                   success=True,
                                   train=train,
                                   title='Zug bearbeiten')


@app.route('/train/overview', methods=['GET', 'POST'])
@login_required
def train_overview():
    trains = Train.query.all()
    return render_template('train/train_overview.html',
                           success=None,
                           trains=trains,
                           title='Zugübersicht')


@app.route('/train/delete/<train_id>', methods=['GET', 'POST'])
@login_required
@required_role
def train_delete(train_id):
    Train.query.filter(Train.id == train_id).delete()
    db.session.commit()
    data = Train.query.all()
    return render_template('train/train_overview.html',
                           success=True,
                           data=data)


@app.route('/maintenance/create', methods=['GET', 'POST'])
@login_required
@required_role
def maintenance_create():
    if not request.form:
        return render_template('maintenance/maintenance_create.html',
                               users=User.query.all(),
                               trains=Train.query.all(),
                               success=None,
                               title='Wartung anlegen')
    else:
        form = request.form.to_dict()
        start_date = datetime.combine(datetime.strptime(form['Beginn'], "%Y-%m-%d").date(),
                                      datetime.strptime(form['Beginnzeit'], "%H:%M").time())
        end_date = datetime.combine(datetime.strptime(form['Ende'], "%Y-%m-%d").date(),
                                    datetime.strptime(form['Endzeit'], "%H:%M").time())
        maintenance = Maintenance(type=form['Wartungsart'],
                                  start_date=start_date,
                                  end_date=end_date,
                                  user_id=form['Mitarbeiter'],
                                  train_id=form['Zug'])

        db.session.add(maintenance)
        #checks if startdate before enddate
        if start_date >= end_date:
            return render_template('maintenance/maintenance_create.html',
                                   success=False,
                                   title='Wartung anlegen')

        db.session.commit()

        if start_date < end_date:
            return render_template('maintenance/maintenance_create.html',
                                   maintenance=maintenance,
                                   title='Wartung anlegen',
                                   success=True)


@app.route('/maintenance/employee', methods=['GET', 'POST'])
@login_required
def maintenance_employee():
    data = []
    maintenance = Maintenance.query.all()
    #Only the maintenance of the logged-in user
    for m in maintenance:
        if m.user_id == current_user.id:
            data.append(m)

    return render_template('maintenance/maintenance_employee.html',
                           success=None,
                           data=data,
                           title='Wartungsübersicht')


@app.route('/maintenance/overview', methods=['GET', 'POST'])
@login_required
def maintenance_overview():
    data = Maintenance.query.all()
    return render_template('/maintenance/maintenance_overview.html',
                           success=None,
                           title='Wartungsübersicht',
                           data=data)


@app.route('/maintenance/delete/<maintenance_id>', methods=['GET', 'POST'])
@login_required
@required_role
def maintenance_delete(maintenance_id):
    Maintenance.query.filter(Maintenance.id == maintenance_id).delete()
    db.session.commit()
    data = Maintenance.query.all()
    return render_template('maintenance/maintenance_overview.html',
                           success=True,
                           data=data)
