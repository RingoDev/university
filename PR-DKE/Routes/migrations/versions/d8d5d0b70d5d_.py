"""empty message

Revision ID: d8d5d0b70d5d
Revises: 
Create Date: 2022-01-29 20:14:05.531978

"""
from alembic import op
import sqlalchemy as sa


# revision identifiers, used by Alembic.
revision = 'd8d5d0b70d5d'
down_revision = None
branch_labels = None
depends_on = None


def upgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    op.create_table('station',
    sa.Column('id', sa.Integer(), nullable=False),
    sa.Column('name', sa.String(length=64), nullable=True),
    sa.Column('road', sa.String(length=120), nullable=True),
    sa.Column('place', sa.String(length=120), nullable=True),
    sa.Column('postal', sa.String(length=120), nullable=True),
    sa.PrimaryKeyConstraint('id')
    )
    op.create_index(op.f('ix_station_name'), 'station', ['name'], unique=True)
    op.create_table('user',
    sa.Column('id', sa.Integer(), nullable=False),
    sa.Column('username', sa.String(length=64), nullable=True),
    sa.Column('email', sa.String(length=120), nullable=True),
    sa.Column('password_hash', sa.String(length=128), nullable=True),
    sa.Column('role', sa.Enum('admin', 'employee', name='userrole'), server_default='employee', nullable=False),
    sa.PrimaryKeyConstraint('id')
    )
    op.create_index(op.f('ix_user_email'), 'user', ['email'], unique=True)
    op.create_index(op.f('ix_user_username'), 'user', ['username'], unique=True)
    op.create_table('post',
    sa.Column('id', sa.Integer(), nullable=False),
    sa.Column('body', sa.String(length=140), nullable=True),
    sa.Column('timestamp', sa.DateTime(), nullable=True),
    sa.Column('user_id', sa.Integer(), nullable=True),
    sa.ForeignKeyConstraint(['user_id'], ['user.id'], ),
    sa.PrimaryKeyConstraint('id')
    )
    op.create_index(op.f('ix_post_timestamp'), 'post', ['timestamp'], unique=False)
    op.create_table('routes',
    sa.Column('id', sa.Integer(), nullable=False),
    sa.Column('startStation', sa.Integer(), nullable=True),
    sa.Column('endStation', sa.Integer(), nullable=True),
    sa.ForeignKeyConstraint(['endStation'], ['station.id'], ),
    sa.ForeignKeyConstraint(['startStation'], ['station.id'], ),
    sa.PrimaryKeyConstraint('id')
    )
    op.create_table('section',
    sa.Column('id', sa.Integer(), nullable=False),
    sa.Column('name', sa.String(length=64), nullable=True),
    sa.Column('startStation', sa.Integer(), nullable=True),
    sa.Column('endStation', sa.Integer(), nullable=True),
    sa.Column('distance', sa.Float(), nullable=True),
    sa.Column('maxSpeed', sa.Integer(), nullable=True),
    sa.Column('width', sa.String(length=64), nullable=True),
    sa.Column('usageFee', sa.Float(), nullable=True),
    sa.ForeignKeyConstraint(['endStation'], ['station.id'], ),
    sa.ForeignKeyConstraint(['startStation'], ['station.id'], ),
    sa.PrimaryKeyConstraint('id')
    )
    op.create_index(op.f('ix_section_name'), 'section', ['name'], unique=True)
    op.create_table('routeSection',
    sa.Column('id', sa.Integer(), nullable=False),
    sa.Column('section_id', sa.Integer(), nullable=True),
    sa.Column('route_id', sa.Integer(), nullable=True),
    sa.ForeignKeyConstraint(['route_id'], ['routes.id'], ),
    sa.ForeignKeyConstraint(['section_id'], ['section.id'], ),
    sa.PrimaryKeyConstraint('id')
    )
    op.create_table('warning',
    sa.Column('id', sa.Integer(), nullable=False),
    sa.Column('description', sa.String(), nullable=True),
    sa.Column('section', sa.Integer(), nullable=True),
    sa.ForeignKeyConstraint(['section'], ['section.id'], ),
    sa.PrimaryKeyConstraint('id')
    )
    # ### end Alembic commands ###


def downgrade():
    # ### commands auto generated by Alembic - please adjust! ###
    op.drop_table('warning')
    op.drop_table('routeSection')
    op.drop_index(op.f('ix_section_name'), table_name='section')
    op.drop_table('section')
    op.drop_table('routes')
    op.drop_index(op.f('ix_post_timestamp'), table_name='post')
    op.drop_table('post')
    op.drop_index(op.f('ix_user_username'), table_name='user')
    op.drop_index(op.f('ix_user_email'), table_name='user')
    op.drop_table('user')
    op.drop_index(op.f('ix_station_name'), table_name='station')
    op.drop_table('station')
    # ### end Alembic commands ###
