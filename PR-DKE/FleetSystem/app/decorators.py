from flask import abort, redirect, url_for
from flask_login import current_user
from functools import wraps


def required_role(function):
    @wraps(function)
    def decorated_function(*args, **kwargs):
        if not current_user.is_admin():
            abort(403)
        return function(*args, **kwargs)
    return decorated_function
