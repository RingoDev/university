from flask import render_template, redirect, url_for, abort
from flask_login import login_required
from sqlalchemy import exc

from app import app, db
from app.decorators import admin_required
from app.forms import EmployeeCreateForm
from app.models.models import User, Role


@app.route('/employee')
@login_required
@admin_required
def employees_list():
    employees = User.query.filter_by(role=Role.employee)
    return render_template("employee/list.html", employees=employees)


@app.route('/employee/<employee_id>', methods=['GET'])
@login_required
@admin_required
def employee_show(employee_id):
    # get employee with id from db
    employee = get_employee(employee_id)
    if employee is None:
        abort(404)
    return render_template("employee/show.html", employee=employee)


@app.route('/employee/create', methods=['GET', 'POST'])
@login_required
@admin_required
def employee_create():
    form = EmployeeCreateForm()
    if form.validate_on_submit():
        employee = User(username=form.username.data,
                        first_name=form.first_name.data,
                        last_name=form.last_name.data,
                        role=Role.employee,
                        password=form.password.data)
        try:
            # could throw errors due to unique username
            db.session.add(employee)
            db.session.commit()
        except exc.SQLAlchemyError:
            # unnecessary rollback, but better safe than sorry
            db.session.rollback()
            abort(500)
        return redirect(url_for('employee_show', employee_id=employee.id))
    return render_template("employee/create.html", form=form)


@app.route('/employee/edit/<employee_id>', methods=['GET', 'POST'])
@login_required
@admin_required
def employee_edit(employee_id):
    employee = get_employee(employee_id)
    if employee is None:
        abort(404)

    # prefill form with existing employee data
    form = EmployeeCreateForm(username=employee.username,
                              first_name=employee.first_name,
                              last_name=employee.last_name)
    form.submit.label.text = "Speichern"
    # allow password field to be empty
    form.password.validators = []

    if form.validate_on_submit():
        employee.first_name = form.first_name.data
        employee.last_name = form.last_name.data
        employee.username = form.username.data
        if form.password.data is not None and form.password.data != "":
            employee.set_password(form.password.data)
        db.session.commit()
        return redirect(url_for("employee_show", employee_id=employee.id))
    return render_template("employee/edit.html", employee=employee, form=form)


@app.route('/employee/delete/<employee_id>', methods=['GET'])
@login_required
@admin_required
def employee_delete(employee_id):
    User.query.filter_by(id=employee_id, role=Role.employee).delete()
    db.session.commit()
    return redirect(url_for("employees_list"))


def get_employee(employee_id) -> User:
    return User.query.filter_by(id=employee_id, role=Role.employee).first()
