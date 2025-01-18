from datetime import datetime, timedelta
from typing import List

from flask import render_template, redirect, url_for, abort
from flask_login import login_required

from app import app, db
from app.decorators import admin_required
from app.fetch import get_trains, get_train_by_id
from app.forms import TripBulkCreateForm, TripCreateForm, TripEditForm
from app.models.models import Trip, Role, User
from app.routes.routes import get_route_by_id


def sort_by_departure(trip):
    return int(trip.departure.strftime("%Y%m%d%H%M%S"))


@app.route('/trip')
@login_required
def trips_list():
    trips = Trip.query.all()
    trips.sort(key=sort_by_departure)
    return render_template('trip/list.html', trips=trips)


@app.route('/trip/<trip_id>', methods=['GET', 'POST'])
@login_required
def trip_show(trip_id):
    # get trip with id from db
    trip = Trip.query.filter_by(id=trip_id).first()
    return render_template('trip/show.html', trip=trip)


@app.route('/trip/create/<route_id>', methods=['GET', 'POST'])
@login_required
@admin_required
def trip_create(route_id):
    route_id = int(route_id)
    form = TripCreateForm()
    route = get_route_by_id(route_id)
    if form.validate_on_submit() and route is not None:
        trip = Trip(route_id, form.departure.data, form.price.data)
        db.session.add(trip)
        db.session.commit()
        return redirect(url_for("trip_show", trip_id=trip.id))
    return render_template('trip/create.html', form=form,
                           route=route)


@app.route('/trip/bulk_create/<route_id>', methods=['GET', 'POST'])
@login_required
@admin_required
def trip_bulk_create(route_id):
    form = TripBulkCreateForm()
    route = get_route_by_id(route_id)
    employees = User.query.filter_by(role=Role.employee).all()

    if form.validate_on_submit():
        weekdays = ["Mo", "Di", "Mi", "Do", "Fr", "Sa", "So"]
        weekdays_boolean = [x in form.weekdays.data for x in weekdays]
        # list of days from start_date to end_date
        date_range = [form.start_date.data + timedelta(days=n) for n in
                      range(int((form.end_date.data - form.start_date.data).days))]
        for single_date in date_range:
            if weekdays_boolean[single_date.weekday()]:
                trip = Trip(route_id=route_id,
                            departure=datetime.combine(single_date, form.departure.data),
                            price=form.price.data)
                db.session.add(trip)
        db.session.commit()
        return redirect(url_for("trips_list"))
    return render_template('trip/bulk_create.html',
                           title='Fahrt erstellen',
                           route=route,
                           employees=employees, form=form)


@app.route('/trip/edit/<trip_id>', methods=['GET', 'POST'])
@login_required
@admin_required
def trip_edit(trip_id: str):
    trip: Trip = Trip.query.filter_by(id=int(trip_id)).first()
    if trip is None:
        abort(404)

    employees: List[User] = User.query.filter_by(role=Role.employee).all()

    # employees that are already booked for the trip
    booked_employees = [emp for emp in employees if any(t.id == int(trip.id) for t in emp.trips)]

    # employees that are not booked for the trip and
    # are not booked for a different trip in this timeframe
    available_employees = [employee for employee in employees if
                           not employee.booked_trips_overlapping_trip(trip)]

    # the train that is currently booked for this trip or None
    booked_train = get_train_by_id(trip.train_id)

    # the trains that could be booked for this trip
    bookable_trains = [train for train in get_trains() if
                       train.is_bookable_for_trip(Trip.query.filter_by(train_id=train.id), trip)]

    trains = [booked_train] + bookable_trains if booked_train is not None else bookable_trains

    # set default values for form
    form = TripEditForm(price=trip.price,
                        employees=[str(employee.id) for employee in booked_employees],
                        train=booked_train.id if booked_train is not None else str(-1))

    # set select fields possible values
    form.employees.choices = [(str(employee.id), f"{employee.first_name} {employee.last_name}")
                              for employee in booked_employees + available_employees]
    form.train.choices = [(str(-1), "-")] + [(str(train.id), f"{train.id} - {train.track_width}") for train in
                                             trains]

    if form.validate_on_submit():
        # set employees that were selected for the trip
        trip.employees = User.query.filter(
            User.id.in_(form.employees.data)) \
            .filter_by(role=Role.employee).all()
        trip.price = form.price.data
        if form.train.data is None or form.train.data == "" or form.train.data == "-1":
            trip.train_id = None
        else:
            trip.train_id = int(form.train.data)
        db.session.commit()
        return redirect(url_for("trip_show", trip_id=trip.id))
    return render_template('trip/edit.html', trip=trip, form=form)


@app.route('/trip/delete/<trip_id>', methods=['GET'])
@login_required
@admin_required
def trip_delete(trip_id):
    Trip.query.filter_by(id=trip_id).delete()
    db.session.commit()
    return redirect(url_for("trips_list"))
