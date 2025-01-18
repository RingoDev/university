from datetime import datetime

from flask import render_template, flash, redirect, url_for, request
from flask_login import current_user, login_user, logout_user, login_required
from sqlalchemy.sql.functions import user
from werkzeug.urls import url_parse

from app import app, db
from app.decorators import required_role
from app.forms import LoginForm
from app.forms import RegistrationForm
from app.models import User, Promotion, Ticket, TicketStatus, Reservation
from app.requests import get_all_routes, get_all_trips, get_warnings, get_trip_by_id, get_train_by_id


# ************************************Home Page************************************
# This method shows the home page after logging in. It includes the search for trips and the buying process.
@app.route('/')
@app.route('/index', methods=['GET', 'POST'])
@login_required
def index():
    if not request.form:
        # get request - open form
        trips = get_all_trips()
        now = datetime.now()
        return render_template('index.html', title='Home', user=user, start_stations=get_start_stations(trips),
                               end_stations=get_end_stations(trips), start=None, end=None, search_type=None, now=now)
    else:
        # post request - press search
        form = request.form.to_dict()
        # check if all fields of search are filled
        if form['Von'] == "Startbahnhof auswählen" or form['Nach'] == "Endbahnhof auswählen":
            trips = get_all_trips()
            now = datetime.now()
            return render_template('index.html', title='Home', user=user, no_result=False, success=False,
                                   start_stations=get_start_stations(trips),
                                   end_stations=get_end_stations(trips), start=None, end=None, search_type=None,
                                   now=now)

        # get entered user data from search form
        search_datetime = datetime.combine(datetime.strptime(form['Datum'], "%Y-%m-%d").date(),
                                           datetime.strptime(form['Zeit'], "%H:%M").time())
        start_id = int(form['Von'])
        end_id = int(form['Nach'])
        search_type = str(form['search_type'])

        # search for result regarding the selected search_type (Ab, An)
        result = []
        trips = get_all_trips()
        if search_type == "Ab":
            result = search_trips_after(trips, start_id, end_id, search_datetime)
        elif search_type == "An":
            result = search_trips_before(trips, start_id, end_id, search_datetime)

        # check if result is empty
        if len(result) == 0:
            return render_template('index.html', title='Home', user=user, no_result=True, success=True,
                                   start_stations=get_start_stations(trips),
                                   end_stations=get_end_stations(trips), start=start_id, end=end_id,
                                   search_type=search_type, now=search_datetime)

        # check if warnings or promotions exist for the trips in the result
        warnings = get_warnings_dict_for_route_id(result)
        promotions = Promotion.query.all()
        trips_price = get_trips_price(result, promotions)

        return render_template('index.html', user=user, no_result=False, success=True, result=result, warnings=warnings,
                               trips_price=trips_price, start_stations=get_start_stations(trips),
                               end_stations=get_end_stations(trips), start=start_id, end=end_id,
                               search_type=search_type, now=search_datetime)


# This method creates a map with key: trip id and value: trip price. It sums up all prices and includes the promotions.
def get_trips_price(result, promotions):
    trips_price = {}
    for trip in result:
        trips_price[trip["id"]] = trip["price"]
        for promotion in promotions:
            # only apply promotion with matching route id and has correct daterange
            if promotion_valid_for_trip(trip, promotion):
                # only use if cheaper than already used promotion
                promotion_price = trip["price"] * (1 - promotion.percent / 100)
                if promotion_price < trips_price[trip["id"]]:
                    trips_price[trip["id"]] = round(promotion_price, 2)
    return trips_price


# This method checks if a promotion is matching a trip in given parameter.
def promotion_valid_for_trip(trip, promotion):
    # check if route id is matching
    if promotion.route_id == trip["routeId"]:
        # check if daterange is overlapping
        trip_departure_string = trip["stops"][0]["departure"]
        trip_departure = datetime.strptime(trip_departure_string, "%Y-%m-%dT%H:%M").date()
        if promotion.start_date <= trip_departure <= promotion.end_date:
            return True
    return False


def get_start_stations(trips):
    start_stations = [trip["stops"][0]["station"] for trip in trips]
    result = list({station['id']: station for station in start_stations}.values())
    return result


def get_end_stations(trips):
    end_stations = [trip["stops"][-1]["station"] for trip in trips]
    result = list({station['id']: station for station in end_stations}.values())
    return result


def get_warnings_dict_for_route_id(trips):
    result = {trip["routeId"]: get_warnings(trip["routeId"]) for trip in trips}
    return result


# This method searches all trips which depart after the given datetime
def search_trips_after(trips, start, end, search_datetime):
    result = []
    for trip in trips:
        date = datetime.strptime(trip['stops'][0]['departure'], "%Y-%m-%dT%H:%M")
        # check date & time
        if date >= search_datetime:
            # check start & end station
            if trip['stops'][0]['station']['id'] == start and trip['stops'][-1]['station']['id'] == end:
                result.append(trip)
    return result


# This method searches all trips which arrive before the given datetime
def search_trips_before(trips, start, end, search_datetime):
    result = []
    for trip in trips:
        date = datetime.strptime(trip['stops'][-1]['departure'], "%Y-%m-%dT%H:%M")
        # check date & time
        if date <= search_datetime:
            # check start & end station
            if trip['stops'][0]['station']['id'] == start and trip['stops'][-1]['station']['id'] == end:
                result.append(trip)
    return result


# This method simulates the buying process of a ticket and adds the new ticket to database
@app.route('/index/buy/<trip_id>')
@login_required
def index_buy(trip_id):
    # set success to True in order to display a success message after buying
    success = request.args.get('success', default=False, type=bool)
    trip = get_trip_by_id(trip_id)
    if success:
        ticket = Ticket(state='active', trip_id=trip_id, user_id=current_user.id)
        # add new ticket in database
        db.session.add(ticket)
        db.session.commit()
    return render_template('index_buy.html', user=user, trip=trip, success=success)


# ************************************Ticket Overview************************************

# This method shows the overview of all tickets, the logged in user bought
@app.route('/tickets')
@login_required
def tickets():
    # set success to True in order to display a success message after cancelling a ticket
    success = request.args.get('success', default=False, type=bool)
    result = []
    _tickets = Ticket.query.all()
    reserved = {}
    today = datetime.now()

    # get all tickets from the logged in user
    for ticket in _tickets:
        trip = get_trip_by_id(ticket.trip_id)
        if trip is None:
            continue
        trip_date = datetime.strptime(trip["stops"][0]["departure"], "%Y-%m-%dT%H:%M")

        if ticket.user_id == current_user.id:
            result.append(ticket)

        # check if ticket already has a reservation
        if Reservation.query.filter_by(ticket_id=ticket.id).first() is None:
            reserved[ticket.id] = False
        else:
            reserved[ticket.id] = True

        # check if ticket is already used
        if ticket.state != TicketStatus.used and trip_date < today:
            ticket.set_state(TicketStatus.used)

    return render_template('tickets.html', user=user, result=result, reserved=reserved, success=success)


# This method cancels a ticket and also deletes a reservation if it exists to the ticket

@app.route('/tickets/delete/<ticket_id>')
@login_required
def ticket_delete(ticket_id):
    ticket = Ticket.query.filter(Ticket.id == ticket_id).first()
    ticket.set_state(TicketStatus.cancelled)
    Reservation.query.filter_by(ticket_id=ticket.id).delete()
    db.session.commit()
    return redirect(url_for('tickets') + '?success=1')


# This method checks if train is free and reservates a seat for the ticket with given ticket id
@app.route('/tickets/reservate/<ticket_id>', methods=['GET', 'POST'])
@login_required
def tickets_reservate(ticket_id):
    ticket = Ticket.query.filter(Ticket.id == ticket_id).first()
    trip = get_trip_by_id(ticket.trip_id)
    if request.method == "GET":
        return render_template('tickets_reservate.html', ticket_id=ticket_id, trip=trip, success=None)
    else:
        train = get_train_by_id(trip["trainId"])
        all_reservations = Reservation.query.filter_by(trip_id=trip["id"]).all()
        seats_booked = len(all_reservations)

        # check if train is already overbooked
        if int(seats_booked) >= int(train["seats"]):
            return render_template('tickets_reservate.html', user=user, trip=trip, success=False)

        # create reservation and add to database
        reservation = Reservation(ticket_id=ticket_id, trip_id=trip["id"])
        db.session.add(reservation)
        db.session.commit()

        return render_template('tickets_reservate.html', ticket_id=ticket_id, trip=trip, success=True)


# This method deletes a reservation from the ticket with given ticket id
@app.route('/ticket/delete/reservation/<ticket_id>')
@login_required
def ticket_delete_reservation(ticket_id):
    ticket = Ticket.query.filter(Ticket.id == ticket_id).first()
    Reservation.query.filter_by(ticket_id=ticket.id).delete()
    db.session.commit()
    return redirect(url_for('tickets'))


# ************************************Admin View************************************

# This method checks if train is free and reservates a seat for the ticket with given ticket id
@app.route('/promotion/create', methods=['GET', 'POST'])
@login_required
@required_role
def promotion_create():
    if not request.form:
        # get request - open form
        return render_template('promotion_create.html', user=user, success=None, routes=get_all_routes())
    else:
        # post request - press create
        form = request.form.to_dict()

        # get entered user data from search form
        start_date = datetime.strptime(form['Beginn'], "%Y-%m-%d").date()
        end_date = datetime.strptime(form['Ende'], "%Y-%m-%d").date()

        # create promotion and add to database
        promotion = Promotion(start_date=start_date, end_date=end_date, percent=form['Prozent'], route_id=form['Route'])
        db.session.add(promotion)
        db.session.commit()
        return render_template('promotion_create.html', user=user, success=True)


# This method edits a promotion with the given promotion id
@app.route('/promotion/edit/<promotion_id>', methods=['GET', 'POST'])
@login_required
@required_role
def promotion_edit(promotion_id):
    if not request.form:
        # get request - open form
        promotion = Promotion.query.filter(Promotion.id == promotion_id).first()
        return render_template('promotion_edit.html', success=None, promotion=promotion, routes=get_all_routes())
    else:
        # post request - press save
        form = request.form.to_dict()
        percent = form['Prozent']
        start_date = datetime.strptime(form['Beginn'], "%Y-%m-%d").date()
        end_date = datetime.strptime(form['Ende'], "%Y-%m-%d").date()
        route_id = form['Route']

        # get promotion from database
        promotion = Promotion.query.filter(Promotion.id == promotion_id).first()

        # set new values
        promotion.set_percent(percent)
        promotion.set_start(start_date)
        promotion.set_end(end_date)
        promotion.set_route(route_id)
        db.session.commit()
        return render_template('promotion_edit.html', success=True, promotion=promotion, routes=get_all_routes())


@app.route('/promotion/overview', methods=['GET', 'POST'])
@login_required
@required_role
def promotion_overview():
    data = Promotion.query.all()
    return render_template('promotion_overview.html', success=None, data=data)


# This method deletes a promotion with the given promotion id
@app.route('/promotion/delete/<promotion_id>', methods=['GET', 'POST'])
@login_required
@required_role
def promotion_delete(promotion_id):
    Promotion.query.filter(Promotion.id == promotion_id).delete()
    db.session.commit()
    data = Promotion.query.all()
    return render_template('promotion_overview.html', success=True, data=data)


# ************************************Onboarding************************************

@app.route('/login', methods=['GET', 'POST'])
def login():
    if current_user.is_authenticated:
        if current_user.is_admin():
            return redirect(url_for('adminView'))
        return redirect(url_for('index'))
    form = LoginForm()
    if form.validate_on_submit():
        _user = User.query.filter_by(username=form.username.data).first()
        if _user is None or not _user.check_password(form.password.data):
            flash('Invalid username or password')
            return redirect(url_for('login'))
        login_user(_user, remember=form.remember_me.data)
        next_page = request.args.get('next')
        if not next_page or url_parse(next_page).netloc != '':
            next_page = url_for('index')
        return redirect(next_page)
    return render_template('login.html', title='Sign In', form=form)


@app.route('/logout')
def logout():
    logout_user()
    return redirect(url_for('index'))


@app.route('/register', methods=['GET', 'POST'])
def register():
    if current_user.is_authenticated:
        return redirect(url_for('index'))
    form = RegistrationForm()
    if form.validate_on_submit():
        _user = User(username=form.username.data)
        _user.set_password(form.password.data)
        db.session.add(_user)
        db.session.commit()
        flash('Congratulations, you are now a registered user!')
        return redirect(url_for('login'))
    return render_template('register.html', title='Register', form=form)
