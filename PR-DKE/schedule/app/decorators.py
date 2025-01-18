from flask import abort
from flask_login import current_user
from functools import wraps


# decorator that should be used inside of decorator "login_required" which checks if the logged in user is an admin user
def admin_required(func):
    @wraps(func)
    def decorated_function(*args, **kwargs):
        if not current_user.is_admin():
            abort(403)
        return func(*args, **kwargs)

    return decorated_function
