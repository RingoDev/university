
from flask import abort, redirect, url_for
from flask_login import current_user
from functools import wraps

#checks if the current user is an admin
def admin_required(func):
    @wraps(func)
    def decorated_function(*args, **kwargs):
        if not current_user.is_admin():
            abort(403)
        return func(*args, **kwargs)

    return decorated_function
