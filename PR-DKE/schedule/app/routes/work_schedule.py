from flask import render_template
from flask_login import login_required, current_user

from app import app


@app.route('/work_schedule')
@login_required
def show_work_schedule():
    return render_template("work_schedule/show_work_schedule.html", trips=current_user.trips)
