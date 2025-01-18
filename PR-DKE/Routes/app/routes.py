from flask import render_template, flash, url_for, redirect, jsonify
from flask_login import current_user, login_user, logout_user, login_required

from app import app, db
from app.decorators import admin_required
from app.forms import LoginForm, StationForm, RegistrationForm, EditStationForm, DeleteStationForm, \
    EditSectionForm, EditRouteForm, WarningForm
from app.models import User, Station, Section, UserRole, Route, Warning

#This method shows the homepage after admin-login. It just displays a greeting.
@app.route('/')
@app.route('/index')
@login_required
@admin_required
def index():
    return render_template("index.html", title='Home Page')


#This method shows the homepage after employee-login. It just displays a greeting.
@app.route('/index_ma')
@login_required
def index_ma():
    return render_template("index_ma.html", title='Home Page')


#This method shows a table to the admin of all stations that are saved
@app.route('/station/overview')
@admin_required
@login_required
def station_overview():
    stations = Station.query.all()
    return render_template("station_overview.html", title='Stations', stations=stations)


#This method shows a table to the employee of all stations that are saved
@app.route('/station/overview/ma')
@login_required
def station_ma_overview():
    stations = Station.query.all()
    return render_template("station_ma_overview.html", title='Stations', stations=stations)


#This method shows a table to the admin of all sections plus their warnings
@app.route('/section/overview')
@admin_required
@login_required
def section_overview():
    sections = Section.query.all()
    warning = Warning.query.all()
    return render_template("section_overview.html", title='Stations', sections=sections, warning=warning)


#This method shows a table to the employee of all sections plus their warnings
@app.route('/section/overview/ma')
@login_required
def section_ma_overview():
    sections = Section.query.all()
    warning = Warning.query.all()

    return render_template("section_ma_overview.html", title='Stations', sections=sections, warning=warning)


#This method shows a table to the admin of all routes plus their warnings
@app.route('/route/overview')
@admin_required
@login_required
def route_overview():
    routes = Route.query.all()
    warning = Warning.query.all()

    return render_template("route_overview.html", title='Stations', routes=routes, warning=warning)


#This method shows a table to the employee of all sections plus their warnings
@app.route('/route/overview/ma')
@login_required
def route_ma_overview():
    routes = Route.query.all()
    warning = Warning.query.all()

    return render_template("route_ma_overview.html", title='Stations', routes=routes, warning=warning)


#this method registers a new user with the inputs
# username, email, password and role
@login_required
@admin_required
@app.route('/register', methods=['GET', 'POST'])
def register():
    form = RegistrationForm()
    if form.validate_on_submit():
        user = User(username=form.username.data, email=form.email.data, role=form.role.data)
        user.set_password(form.password.data)
        db.session.add(user)
        db.session.commit()
        flash('Congratulations, you are now a registered user!')
        return redirect(url_for('login'))
    return render_template('register.html', title='Register', form=form)


#this method logs the current user in and
#based on their role redirects them to their template
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
        #checks which role the user has, for redirection
        if user.role == UserRole.admin:
            next_page = url_for('index')
        else:
            next_page = url_for('index_ma')
        return redirect(next_page)
    return render_template('login.html', title='Sign In', form=form)


#this method logs out the current user
@app.route('/logout')
def logout():
    logout_user()
    return redirect(url_for('index'))


#this method creates a new station object in the databse,
#with the input values name, road, place, postal
@app.route('/station', methods=['GET', 'POST'])
@admin_required
@login_required
def station():
    form = StationForm()
    if form.validate_on_submit():
        stat = Station(name=form.designation.data, road=form.road.data, place=form.place.data, postal=form.postal.data)
        db.session.add(stat)
        db.session.commit()
        flash('Station is saved!')
        return redirect(url_for('station_overview'))
    return render_template('station.html', title='Station', form=form)


#this method edits an already existing station,
#the station is filtered by their name, which is an input value
@app.route('/station/edit/<stationName>', methods=['GET', 'POST'])
@admin_required
@login_required
def station_edit2(stationName):
    form = EditStationForm()
    #the form values are filled for the html
    stat = Station.query.filter_by(name=stationName).first()
    form.designation.data = stationName
    form.road.data = stat.road
    form.place.data = stat.place
    form.postal.data = stat.postal
    if form.validate_on_submit():
        stat = Station.query.filter_by(name=stationName).first()
        form = EditStationForm()
        stat.name = form.designation.data
        stat.road = form.road.data
        stat.place = form.place.data
        stat.postal = form.postal.data
        db.session.commit()
        flash('Station is edited!')
        return redirect(url_for('station_overview'))
    return render_template('edit_station.html', title='Station', form=form, name=stationName)


#this method deletes the station based on their name,
# which is an input value
@app.route('/station/delete/<stationName>', methods=['GET', 'POST'])
@admin_required
@login_required
def station_delete2(stationName):
    form = DeleteStationForm()
    form.id.choices = ['nothing selected']
    stations = Station.query.all()
    for s in stations:
        form.id.append(s.name)
    stat = Station.query.filter_by(name=stationName).delete()
    db.session.commit()
    flash('Station is deleted!')
    return redirect(url_for('station_overview'))

#this method searches a user, based
#on their username
@app.route('/user/<username>')
@admin_required
@login_required
def user(username):
    user = User.query.filter_by(username=username).first_or_404()
    return render_template('user.html', user=user)


#this method creates a new section object in the database,
# with the inputs name, start and end station, distance
# maxSpeed, width, usageFee
@app.route('/section', methods=['GET', 'POST'])
@admin_required
@login_required
def section():
    form = EditSectionForm()
    form.startStation.choices = ['nothing selected']
    form.endStation.choices = ['nothing selected']
    stations = Station.query.all()
    for s in stations:
        form.startStation.choices.append(s.name)
        form.endStation.choices.append(s.name)
    if form.validate_on_submit():
        #checks if start and endstation are the same
        if (form.startStation.data == form.endStation.data):
            flash('Start Station can not be Endstation', 'error')
        else:
            #if stations are not the same, a section will be added
            section = Section(name=form.name.data, startStation=form.startStation.data, endStation=form.endStation.data,
                              distance=form.distance.data, maxSpeed=form.maxSpeed.data, width=form.width.data,
                              usageFee=form.usageFee.data)
            db.session.add(section)
            db.session.commit()
            flash('Section is saved!')
            return redirect(url_for('section_overview'))
    return render_template('section.html', title='Section', form=form)


#this method edits an section,
#this section is filtered by their name
@app.route('/section/edit/<sectionName>', methods=['GET', 'POST'])
@admin_required
@login_required
def section_edit(sectionName):
    form = EditSectionForm()
    form.startStation.choices = ['nothing selected']
    form.endStation.choices = ['nothing selected']
    stations = Station.query.all()
    for s in stations:
        form.startStation.choices.append(s.name)
        form.endStation.choices.append(s.name)
    #the input values will be filled with the existing data
    section = Section.query.filter_by(name=sectionName).first()
    form.name.data = sectionName
    form.startStation.data = section.startStation
    form.endStation.data = section.endStation
    form.distance.data = section.distance
    form.maxSpeed.data = section.maxSpeed
    form.width.data = section.width
    form.usageFee.data = section.usageFee
    if form.validate_on_submit():
        #checks if start and endstation are the same

        if (form.startStation.data == form.endStation.data):
            flash('Start Station can not be Endstation', 'error')
        else:
            #if the stations are not the same, the section will be edited
            section = Section.query.filter_by(name=sectionName).first()
            section.name = form.name.data
            section.startStation = form.startStation.data
            section.endStation = form.endStation.data
            section.distance = form.distance.data
            section.maxSpeed = form.maxSpeed.data
            section.width = form.width.data

            section.usageFee = form.usageFee.data
            db.session.commit()
            flash('Section is edited!')
            return redirect(url_for('section_overview'))
    return render_template('edit_section.html', title='Section', form=form, name=sectionName)


#this method deletes a section based,
#on its name
@app.route('/section/delete/<sectionName>', methods=['GET', 'POST'])
@admin_required
@login_required
def section_delete(sectionName):
    sec = Section.query.filter_by(name=sectionName).delete()
    db.session.commit()
    flash('Section is deleted!')
    return redirect(url_for('section_overview'))


#this method creates a route in the database,
#with the input start and end station and all section in between
@app.route('/route', methods=['GET', 'POST'])
@admin_required
@login_required
def route():
    form = EditRouteForm()
    form.startStation.choices = ['nothing selected']
    form.endStation.choices = ['nothing selected']
    form.sections.choices = []
    sections = Section.query.all()
    for c in sections:
        form.sections.choices.append(c.name)

    stations = Station.query.all()
    for c in stations:
        form.startStation.choices.append(c.name)
        form.endStation.choices.append(c.name)

    if form.validate_on_submit():
        #checks if the stations are not the same
        if (form.startStation.data == form.endStation.data):
            flash('Start Station can not be Endstation', 'error')
        else:
            stations = []
            width = []
            route = Route(startStation=form.startStation.data, endStation=form.endStation.data);
            for s in form.sections.data:
                sectionObj = Section.query.filter_by(name=s).first()
                stations.append(sectionObj.startStation)
                stations.append(sectionObj.endStation)
                width.append(sectionObj.width)
                route.routeSection.append(sectionObj)

            #the start and end stations are removed from the stations array
            stations.remove(form.startStation.data)
            stations.remove(form.endStation.data)

            #a list with the amount of stations that are found
            #this always has to be 2 because, one start station must
            #be connected to another ones end station
            listStations = {i: stations.count(i) for i in stations}

            #remove duplicates
            stations2 = list(dict.fromkeys(stations))
            width2 = list(dict.fromkeys(width))

            is_connected = True
            for s in stations2:
                x = listStations.get(s)

                if (x != 2):
                    #if the amount of stations is not 2 the sections are not connected
                    is_connected = False

            if len(width2) != 1:
                #if there is more than 1 different width sections, they can not be connected
                flash('Width is not correlating')
            elif (is_connected == True):

                db.session.add(route)
                db.session.commit()
                flash('Route is saved!')
                return redirect(url_for('route_overview'))
            else:
                flash('Sections are not connected!')
    return render_template('route.html', title='Route', form=form)


#this method edits a route element,
#which is filtered by their id
@app.route('/route/edit/<routeID>', methods=['GET', 'POST'])
@admin_required
@login_required
def route_edit(routeID):
    form = EditRouteForm()

    form.startStation.choices = ['nothing selected']
    form.endStation.choices = ['nothing selected']
    form.sections.choices = []
    sections = Section.query.all()
    for c in sections:
        form.sections.choices.append(c.name)

    stations = Station.query.all()
    for c in stations:
        form.startStation.choices.append(c.name)
        form.endStation.choices.append(c.name)
    #the form data are filled
    route = Route.query.filter_by(id=routeID).first()
    form.startStation.data = route.startStation
    form.endStation.data = route.endStation
    array = []
    for section in route.routeSection:
        array.append(section.name)
    form.sections.data = array

    if form.validate_on_submit():
        # checks if the stations are not the same
        if (form.startStation.data == form.endStation.data):
            flash('Start Station can not be Endstation', 'error')
        else:
            form = EditRouteForm()

            form.startStation.choices = ['nothing selected']
            form.endStation.choices = ['nothing selected']
            form.sections.choices = []
            sections = Section.query.all()
            for c in sections:
                form.sections.choices.append(c.name)

            stations = Station.query.all()
            for c in stations:
                form.startStation.choices.append(c.name)
                form.endStation.choices.append(c.name)

            stations = []
            width = []
            for s in form.sections.data:
                sectionObj = Section.query.filter_by(name=s).first()
                stations.append(sectionObj.startStation)
                stations.append(sectionObj.endStation)
                width.append(sectionObj.width)

            # the start and end stations are removed from the stations array
            stations.remove(form.startStation.data)
            stations.remove(form.endStation.data)

            #a list with the amount of stations that are found
            #this always has to be 2 because, one start station must
            #be connected to another ones end station
            listStations = {i: stations.count(i) for i in stations}
            # remove duplicates
            stations2 = list(dict.fromkeys(stations))
            width2 = list(dict.fromkeys(width))

            is_connected = True
            for s in stations2:
                x = listStations.get(s)

                if (x != 2):
                    # if the amount of stations is not 2 the sections are not connected
                    is_connected = False

            if len(width2) != 1:
                # if there is more than 1 different width sections, they can not be connected
                flash('Width is not correlating')
            elif (is_connected == True):

                route = Route.query.filter_by(id=routeID).first()
                route.startStation = form.startStation.data
                route.endStation = form.endStation.data
                for s in form.sections.data:
                    sectionObj = Section.query.filter_by(name=s).first()
                    route.routeSection.append(sectionObj)
                db.session.commit()
                flash('Route is edited!')
                return redirect(url_for('route_overview'))
            else:
                flash('Sections are not connected!')
    return render_template('edit_route.html', title='Route', form=form)


#this method deletes a route object based on their id
@app.route('/route/delete/<routeID>', methods=['GET', 'POST'])
@admin_required
@login_required
def route_delete(routeID):
    route = Route.query.filter_by(id=routeID).delete()
    db.session.commit()
    flash('Route is deleted!')
    return redirect(url_for('route_overview'))


#this method creates a warning for a section,
#the inputs are a description and a section
@app.route('/warning', methods=['GET', 'POST'])
@admin_required
@login_required
def warning():
    form = WarningForm()

    form.section.choices = []
    sections = Section.query.all()
    for s in sections:
        form.section.choices.append(s.name)

    if form.validate_on_submit():
        warning = Warning(description=form.description.data, section=form.section.data);
        db.session.add(warning)
        db.session.commit()
        flash('Warning is saved!')
        return redirect(url_for('section_overview'))
    return render_template('warning.html', title='Warning', form=form)


#this method shows the detail view for the admin
#of warnings based on a route object,
#which is filtered by their id
@app.route('/warning/detail/<routeID>', methods=['GET', 'POST'])
@admin_required
@login_required
def warning_detail(routeID):
    route = Route.query.filter_by(id=routeID).first()

    warning = Warning.query.all()
    return render_template('warning_detail.html', title='Warning', route=route, warning=warning)


#this method shows the detail view for the employee
#of warnings based on a route object,
#which is filtered by their id
@app.route('/warning/detail/ma/<routeID>', methods=['GET', 'POST'])
@login_required
def warning_ma_detail(routeID):
    route = Route.query.filter_by(id=routeID).first()
    warning = Warning.query.all()
    return render_template('warning_ma_detail.html', title='Warning', route=route, warning=warning)
