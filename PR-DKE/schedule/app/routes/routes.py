from flask import render_template
from flask_login import login_required

from app import app
from app.fetch import get_routes, get_route_by_id
from app.models.models import Trip


@app.route('/route', methods=['GET'])
@login_required
def routes_list():
    return render_template('route/list.html', routes=get_routes())


@app.route('/route/<route_id>', methods=['GET'])
@login_required
def route_show(route_id):
    trips = Trip.query.filter_by(route_id=route_id).all()
    return render_template('route/show.html', route=get_route_by_id(route_id), trips=trips)
