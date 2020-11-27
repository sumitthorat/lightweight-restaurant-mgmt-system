""" @brief API Endpoint Functions And Database Classes """

##
# @mainpage Lightweight Restraunt Management System
#
# @section description_main Description
# Lightweight Restaurant Management System is a software system which aims
#to digitise the order management process at restaurants whilst being very light,
#inexpensive and user friendly. It does so by enabling the restaurant staff to
#make use of an Android app to view orders, edit menu, manage tables and view
#statistics. IT allows placing orders using a webapp which can be accessed via a
#table specific QR code.
##
# @file main.py
#
# @brief File Related to API endpoints and database
#
# @section description_function Description
# Classes used for database creation
# - MenuTab (for storing menu)
# - TableTab (for storing restraunt table data)
# - OrdersMaster (master table for order tracking)
# - OrdersPending (table for storing pending orders)
# - OrdersComplete (table for storing completed orders)
# - Categories (Storing item names with category of that item)
# - Users (Table for storing user details)
#
# @section libraries_sensors Libraries/Modules
# - flask library
# - flask_restful library
# - flask_sqlalchemy library
# - json standard library
# - datetime standard library 
# - pyqrcode library
# - base64 library
# - socketio library
# - png library
# - os standard library
#
# @section notes_sensors Notes
# - Comments are Doxygen compatible.

#Imports
from flask import Flask, request
from flask_restful import Api, Resource, reqparse, abort, fields, marshal_with
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy import func, desc
from flask import jsonify
from datetime import datetime, timedelta
import json
import socketio
import base64
import png
import os
import pyqrcode
from flask_cors import CORS, cross_origin

app = Flask(__name__)
app.config['CORS_HEADERS'] = 'Content-Type'
CORS(app, resources={r"/NewOrder": {"origins": "http://localhost:8000"}})


sio = socketio.Server(logger=True, async_mode=None)
app.wsgi_app = socketio.WSGIApp(sio, app.wsgi_app)

api = Api(app)
app.config['SQLALCHEMY_DATABASE_URI'] = "sqlite:///database.db"
db = SQLAlchemy(app)

class MenuTab(db.Model):
	"""
	The database model class for menu storage.
	
	Defines MenuTab table attributes and the appropriate keys.
	"""
    __tablename__ = 'menutab'
    item_name = db.Column(db.String(100), primary_key=True)
    price = db.Column(db.Integer, nullable=False)
    category = db.Column(db.String, db.ForeignKey('categories.category'))

    def __repr__(self):
        return f"Menu(items = {item_name}, price = {price})"

class TableTab(db.Model):
	"""
	The database model class for restraunt table storage.
	
	Defines TableTab table attributes and the appropriate keys.
	"""
    __tablename__ = 'tabletab'
    tableid = db.Column(db.Integer, primary_key=True)
    qrcode_str = db.Column(db.String, unique=True, nullable=False)
    

class OrdersMaster(db.Model):
	"""
	The database model class for defining Order table.
	
	Defines OrdersMaster table attributes and the appropriate keys.
	"""
    orderid = db.Column(db.Integer, primary_key=True)
    status = db.Column(db.String, nullable=False)
    amount = db.Column(db.Float, nullable=False)
    tableid = db.Column(db.Integer, nullable=False)
    time_start = db.Column(db.DateTime,nullable=False)
    time_end = db.Column(db.DateTime)


class OrdersPending(db.Model):
	"""
	The database model class for defining table for pending orders.
	
	Defines OrdersPending table attributes and the appropriate keys.
	"""
    __tablename__ = 'orders_pending'
    orderid = db.Column(db.Integer, db.ForeignKey('orders_master.orderid'), primary_key=True)
    item_name = db.Column(db.String, primary_key=True)
    quantity = db.Column(db.Integer)
    
class OrdersComplete(db.Model):
	"""
	The database model class for defining table for completed orders.
	
	Defines OrdersComplete table attributes and the appropriate keys.
	"""
    orderid = db.Column(db.Integer, db.ForeignKey('orders_master.orderid'), primary_key=True)
    item_name = db.Column(db.String, primary_key=True)
    quantity = db.Column(db.Integer)
    

class Users(db.Model):
	"""
	The database model class for storing user details.
	
	Defines Users table attributes and the appropriate keys.
	"""
    username = db.Column(db.String(40), primary_key=True)
    password_hash = db.Column(db.String, nullable=False)
    role = db.Column(db.String(20), nullable=False)

class Categories(db.Model):
    """
	The database model class for storing item categories.
	
	Defines Users table attributes and the appropriate keys.
	"""
	category = db.Column(db.String(20), primary_key=True)
    menu_add = db.relationship('MenuTab', backref='item')



#db.create_all()


# Define parser objects and field types for all tables in database
#Global Parser Objects
## Parser object to parse menu items
menu_put_item = reqparse.RequestParser()
menu_put_item.add_argument("item_name", type=str,required=True, help="Item name not sent")
menu_put_item.add_argument("price", type=int,required=True, help="Price of item is required")
menu_put_item.add_argument("category", type=str, required=True, help="Category not given")

## Parser object to parse menu items for updation
menu_update_item = reqparse.RequestParser()
menu_update_item.add_argument("item_name", type=str)
menu_update_item.add_argument("price", type=int)
menu_update_item.add_argument("category", type=str)

## Parser object to parse table data
table_put_item = reqparse.RequestParser()
table_put_item.add_argument("tableid", type=int, required=True, help="Table id required")

## Parser object to parse pending order data
ordpen_put_item = reqparse.RequestParser()
ordpen_put_item.add_argument("content", type=str, required=True, help="No food items entered")
ordpen_put_item.add_argument("amount", type=float, required=True, help="Amount not entered")
ordpen_put_item.add_argument("tableid", type=int, required=True, help="table id not entered")

## Parser object to parse pending order data for update
ordpen_update_item = reqparse.RequestParser()
ordpen_update_item.add_argument("content", type=str)
ordpen_update_item.add_argument("amount", type=float)
ordpen_update_item.add_argument("tableid", type=int)

ordcomp_put_item = reqparse.RequestParser()
ordcomp_put_item.add_argument("content", type=str, required=True, help="No food items entered")
ordcomp_put_item.add_argument("amount", type=float, required=True, help="Amount not entered")
ordcomp_put_item.add_argument("tableid", type=int, required=True, help="table id not entered")

## Parser object to parse user details data
user_put_item = reqparse.RequestParser()
user_put_item.add_argument("username", type=str, required=True, help="username required")
user_put_item.add_argument("password_hash", type=str, required=True, help="password required")
user_put_item.add_argument("role", type=str, required=True, help="role of user required")

## Parser object to parse login data
user_login_item = reqparse.RequestParser()
user_login_item.add_argument("username", type=str, required=True, help="username required")
user_login_item.add_argument("password_hash", type=str, required=True, help="password required")

## Parser object to parse user update details
user_update_item = reqparse.RequestParser()
user_update_item.add_argument("username", type=str, required=True)
user_update_item.add_argument("password_hash", type=str)
user_update_item.add_argument("role", type=str)

order_complete = reqparse.RequestParser()
order_complete.add_argument("orderid", type=int, required=True)
#order_complete.add_argument("amount", type=float, required=True)

resource_menu_fields = {
    'item_name' : fields.String,
    'price' : fields.Integer,
    'category' : fields.String
}

resource_table_fields = {
    'tableid' : fields.Integer,
    'encodstr' : fields.String
}

resource_order_fields = {
    'ordid' : fields.Integer,
    'content' : fields.String,
    'amount'  : fields.Float,
    'tableid' : fields.Integer
}

# Delete A Table
@app.route('/DeleteTable', methods=['PUT'])
def delete_table():
	"""
	This function is used to delete table from database
	
	@return A json structure having status and message of operation.
	"""
	args = request.get_json()
	table_id = args['tableid']
	
	pending_list = OrdersMaster.query.filter_by(status = "pending").all()
	
	for order in pending_list:
		if(order.tableid == table_id):
			return jsonify({"status" : -1, "message" : "Cannot delete with currently pending order(s)"})
	
	delete_tab = TableTab.query.filter_by(tableid = table_id).first()
	db.session.delete(delete_tab)
	db.session.commit()
	return jsonify({"status" : 1, "message" : "Table Deleted Successfully"})

# Get all tables info
@app.route('/GetTables', methods=['GET'])
def get_tables():
	"""
	This function is used to get tables list from database
	
	@return A json structure having tableid and encoded string.
	"""
    all_tables = TableTab.query.all()

    output = []
    for table in all_tables:
        item = {}
        item['tableid'] = table.tableid
        item['qrcode_str'] = table.qrcode_str
        output.append(item)

    return jsonify(output)

# Add new table
@app.route('/AddTable', methods=['PUT'])
def add_new_table():
	"""
	This function is used to add new table info to database
	
	@return A json structure having status and message of operation.
	"""
    args = table_put_item.parse_args()
    result = TableTab.query.filter_by(tableid=args['tableid']).first()
    if result:
        response = jsonify({"status": -1, "message": "Table id already exists"})
        return response

    s = "localhost:5000/order/?table=" + str(args['tableid'])
    qr = pyqrcode.create(s)
    qr.png("qr_table.png", scale = 6)

    image = open('qr_table.png', 'rb')
    image_read = image.read()
    image_encoded = base64.standard_b64encode(image_read)
    print(image_encoded)
    print()
    image_encoded = str(image_encoded)
    image.close()
    os.remove("qr_table.png")
    '''
    image_decode = base64.decodestring(image_encoded)
    image_result = open('qr_table.png', 'wb')
    image_result.write(image_decode)
    image_result.close()
    '''
    table_new = TableTab(tableid=args['tableid'], qrcode_str=image_encoded)
    db.session.add(table_new)
    db.session.commit()
    return jsonify({"status":1, "message":"Table added successfully"})

# Add new user
@app.route('/AddNewUser', methods=['PUT'])
def add_new_user():
	"""
	This function is used to add user details database
	
	@return A json structure having status and message of operation.
	"""
	args = user_put_item.parse_args()
	result = Users.query.filter_by(username=args['username']).first()
	if result:
		return jsonify({"status":-1, "message":"Username already taken"})

	user_item = Users(username=args['username'], password_hash=args['password_hash'], role=args['role'])
	db.session.add(user_item)
	db.session.commit()
	return jsonify({"status":1, "message":"User added successfully"})

# Add new menu item
@app.route('/AddItemToMenu', methods=['PUT'])
def add_item_to_menu():
	"""
	This function is used to a new menu item to database
	
	@return A json structure having status and message of operation.
	"""
	args = menu_put_item.parse_args()
	result = MenuTab.query.filter_by(item_name = args['item_name']).first()
	if result:
		response = jsonify({"status":-1, "message":"Item name already exists"})
		return response

	fetch_category = Categories.query.filter_by(category=args['category']).first()
	if not fetch_category:
		add_category = Categories(category=args['category'])
		db.session.add(add_category)
		db.session.commit()

	fetch_category = Categories.query.filter_by(category=args['category']).first()
	menu_item = MenuTab(item_name=args['item_name'], price=args['price'], item = fetch_category)
	db.session.add(menu_item)
	db.session.commit()
	return jsonify({"status":1, "message":"Item added successfully"})


# Attempt user login
@app.route('/AttemptLogin',methods=['PUT'])
def attempt_login():
	"""
	This function is used to verify user login.
	
	@return A json structure having status and message of operation.
	"""
    args = user_login_item.parse_args()
    result = Users.query.filter_by(username=args['username']).first()
    if not result:
        return jsonify({"status" : -1, "message" : "Username does not exist"})
    if(result.password_hash != args['password_hash']):
        return jsonify({"status" : -1, "message" : "Password does not match"})
    else:
        return jsonify({"status" : 1, "message" : "Login Successful"})


# Returns all menu categories
@app.route('/GetCategories', methods=['GET'])
def all_categories():
	"""
	This function is used to get item categories from database.
	
	@return A json structure having status and message of operation with categories list.
	"""
    full_categories = Categories.query.all()
    res = []
    output = {}
    for category in full_categories:
        category_data = {}
        res.append(category.category)


    output['categories'] = res
    output['message'] = 'Successful'
    output['status'] = 1

    if len(res) <= 0:
        output['message'] = 'No categories available'
        output['status'] = -1
    
    return jsonify(output)


# Returns the entire menu
@app.route('/GetFullMenu',methods=['GET'])
def get_full_menu():
	"""
	This function is used to retrieve entire menu from database
	
	@return A json structure having menu items and their categories.
	"""
    fullmenu = MenuTab.query.all()
    categories_list = []
    # output will a list of categories, each category will have a list of items
    for item in fullmenu:
        item_data = {}
        item_data['item_name'] = item.item_name
        item_data['price'] = item.price

        found = False
        for category_item in categories_list:
            if category_item['category'] == item.category:
                category_item['menu_items'].append(item_data)
                found = True
                break
        
        if not found:
            categories_list.append({'category' : item.category, 'menu_items' : [item_data]})
            
            
                
    return jsonify({'categories' : categories_list})

    


# Returns a list of all 'tables' in the restaurant
@app.route('/GetAllTablesInfo', methods=['GET'])
def all_tables():
	"""
	This function is used to get restraunt tables list from database.
	
	@return A json structure having tables info data.
	"""
    tablelist = TableTab.query.all()
    output = []
    for table in tablelist:
        table_data = {}
        table_data['tableid'] = table.tableid
        table_data['encodstr'] = table.encodstr
        output.append(table_data)
    
    return jsonify(output)


# Return pending orders
@app.route('/GetPendingOrders', methods=['GET'])
def get_pending_orders():
	"""
	This function is used to retrieve pending orders from database.
	
	@return A json structure having order id with a list of items data.
	"""
    pending_list = OrdersPending.query.all()

    output = []
    for pending_order in pending_list:
        get_tableid = OrdersMaster.query.filter_by(orderid = pending_order.orderid).first()
        table = get_tableid.tableid
        item_data = {"item_name" : pending_order.item_name, "item_qty" : pending_order.quantity}
        
        found = False
        for order in output:
            if order['orderid'] == pending_order.orderid:
                order['items'].append(item_data)
                found = True

        if not found:
            output.append({"orderid" : pending_order.orderid, "items" : [item_data], "tableid" : table})
    
    return jsonify(output)

# Create New Order
@app.route('/NewOrder', methods=['PUT'])
@cross_origin(origin='localhost',headers=['Content- Type','Authorization'])
def new_order():
	"""
	This function is used to create new order in database.
	
	@return A json structure having status and message of operation.
	"""
    check_empty = OrdersMaster.query.first()
    if not check_empty:
        order_id = 0
    else :
        order_id = db.session.query(func.max(OrdersMaster.orderid)).scalar()
        order_id += 1

    orders_json_req = request.get_json()
    tableid = orders_json_req['tableid']
    items = orders_json_req['items']

    add_master = OrdersMaster(orderid = order_id, status = "pending", time_start = datetime.now(), tableid = tableid)
    db.session.add(add_master)
    db.session.commit()

    for item in items:
        add_pend = OrdersPending(orderid = order_id, item_name = item['item_name'], quantity = item['item_qty'])
        db.session.add(add_pend)
        db.session.commit()

    sio.emit('new order', {'orderid' : order_id, 'items' : items, 'tableid' : tableid})

    return jsonify({"status":1, "message":"Order added successfully"})

#Complete an Order
@app.route('/OrderComplete', methods=['PUT'])
def complete_order():
	"""
	This function is used to complete a pending order from database.
	
	@return A json structure having status and message of operation.
	"""
    args = order_complete.parse_args()

    multi_rows = OrdersPending.query.filter_by(orderid = args['orderid']).all()
    total_bill = 0.0

    for row in multi_rows :
        oid = int(row.orderid)
        name = row.item_name
        qty = int(row.quantity)

        fetch_price = MenuTab.query.filter_by(item_name = name).first()
        total_bill += qty * float(fetch_price.price)

        add_comp = OrdersComplete(orderid = oid, item_name = name, quantity = qty)
        db.session.add(add_comp)
        db.session.commit()
        #db.session.delete(row)
        #db.session.commit()

    db.session.query(OrdersPending).filter(OrdersPending.orderid == args['orderid']).delete()
    db.session.commit()

    total_bill = float(total_bill)
    result = OrdersMaster.query.filter_by(orderid = args['orderid']).first()
    result.time_end = datetime.now()
    result.amount = total_bill
    result.status = "complete"
    db.session.commit()
    return jsonify({"status":1, "message":"Order completed successfully"})



# Return Sale of a particular item
@app.route('/ItemSale', methods=['PUT'])
def item_sale():
	"""
	This function is used to retrieve sale of particular item from database.
	
	@return A json structure having item name and quantity sold of that item.
	"""
    args = request.get_json()
    name = args['item_name']
    prev_days = args['days']

    total_qty = 0
    if(prev_days != 0):
        filter_after = datetime.today() - timedelta(days= prev_days)
    else :
        filter_after = datetime(datetime.today().year, datetime.today().month, datetime.today().day)

    todays_orders = OrdersMaster.query.filter(OrdersMaster.time_end >= filter_after).all()
    
    if not todays_orders :
	    return jsonify({"item_name" : name, "quantity_sold" : total_qty})

    for order in todays_orders:
        id = order.orderid
        completed = OrdersComplete.query.filter_by(orderid = id).all()
        for x in completed:
            if(x.item_name == name):
                total_qty += x.quantity
        
    return jsonify({"item_name": name, "quantity_sold" : total_qty})

# Return Most Sold Items For the Day
@app.route('/MostSoldItem', methods=['GET'])
def most_sold():
	"""
	This function is used to retrieve most sold item for the day.
	
	@return A json structure having item name and quantity sale.
	"""

    today_orderid_list = []
    filter_after = datetime(datetime.today().year, datetime.today().month, datetime.today().day)
    todays_orders = OrdersMaster.query.filter(OrdersMaster.time_end >= filter_after).all()
    for order in todays_orders:
        today_orderid_list.append(order.orderid)

    sold_items = OrdersComplete.query.all()
    items = {}

    for sold in sold_items:
        if sold.orderid in today_orderid_list:
            item_name = sold.item_name
            item_qty = sold.quantity

            if item_name in items:
                items[item_name] += item_qty
            else :
                items[item_name] = item_qty
        
    if not items :
	    return jsonify({"item_name" : "No Item sold today", "quantity_sold" : 0})

    mx = max(items.values())
    max_sold = [k for k, v in items.items() if v == mx]
    max_sold.sort()
    
    return jsonify({"item_name" : max_sold[0], "quantity_sold" : items[max_sold[0]] })


# Total Sale For the Day
@app.route('/CurrentDaySale', methods=['GET'])
def total_sale():
	"""
	This function is used to retrieve total sale of current day.
	
	@return A json structure having amount field.
	"""
    day_sale = 0
    filter_after = datetime(datetime.today().year, datetime.today().month, datetime.today().day)
    todays_orders = OrdersMaster.query.filter(OrdersMaster.time_end >= filter_after).all()
    for order in todays_orders:
        if(order.status == "complete"):
            day_sale += order.amount


    return jsonify({"current_day_sale" : day_sale})

# Average Order Completion Time
@app.route('/AvgOrderTime', methods=['GET'])
def average_time():
	"""
	This function is used to retrieve average time for order completion.
	
	@return A json structure having numeber of orders completed and average time taken.
	"""
    total_time = 0.0
    count = 0
    filter_after = datetime(datetime.today().year, datetime.today().month, datetime.today().day)
    todays_orders = OrdersMaster.query.filter(OrdersMaster.time_end >= filter_after).all()
    if not todays_orders :
	    return jsonify({"orders_completed" : 0, "avg_time" : 0.00})
    for order in todays_orders:
        if(order.status == "complete"):
            count += 1
            start = order.time_start
            end = order.time_end
            delta = (end - start).total_seconds()
            total_time += delta
    if(count == 0) :
	    return jsonify({"orders_completed" : 0, "avg_time" : 0.00})
    avg = total_time / ( 60 * count)
    avg = "{:.2f}".format(avg)
    return jsonify({"orders_completed" : count, "avg_time" : avg})




# Returns all completed orders
@app.route('/GetCompletedOrders', methods=['GET'])
def all_completed():
	"""
	This function is used to retrieve list of complete orders.
	
	@return A json structure having completed orders data.
	"""
    completelist = OrdersComplete.query.all()
    output = []
    for order in completelist:
        order_data = {}
        order_data['ordid'] = order.ordid
        order_data['content'] = order.content
        order_data['amount'] = order.amount
        order_data['tableid'] = order.tableid
        output.append(order_data)
    return jsonify(output)

############################################################################################################


if __name__ == "__main__":
    app.run(debug=True)
''' 
CREATE TABLE "orders_master" (
	"orderid"	INTEGER NOT NULL AUTOINCREMENT,
	"status"	VARCHAR NOT NULL,
	"time_start"	DATETIME NOT NULL,
	"time_end"	DATETIME,
	"amount"	INTEGER,
	PRIMARY KEY("orderid")
);

CREATE TABLE "orders_pending" (
	"orderid"	INTEGER NOT NULL,
	"item_name"	VARCHAR NOT NULL,
	"quantity"	INTEGER NOT NULL,
	PRIMARY KEY("orderid","item_name"),
	FOREIGN KEY("item_name") REFERENCES "menutab"("item_name")
);
'''
