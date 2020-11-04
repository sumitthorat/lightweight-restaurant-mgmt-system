from flask import Flask
from flask_restful import Api, Resource, reqparse, abort, fields, marshal_with
from flask_sqlalchemy import SQLAlchemy
from flask import jsonify


app = Flask(__name__)
api = Api(app)
app.config['SQLALCHEMY_DATABASE_URI'] = "sqlite:///database.db"
db = SQLAlchemy(app)

class MenuTab(db.Model):
    __tablename__ = 'menutab'
    #id = db.Column(db.Integer, primary_key=True)
    item_name = db.Column(db.String(100), primary_key=True)
    price = db.Column(db.Integer, nullable=False)
    category = db.Column(db.String, db.ForeignKey('categories.category'))

    def __repr__(self):
        return f"Menu(items = {item_name}, price = {price})"

class TableTab(db.Model):
    __tablename__ = 'tabletab'
    tableid = db.Column(db.Integer, primary_key=True)
    encodstr = db.Column(db.String(100), unique=True, nullable=False)
    orders_pen = db.relationship('OrdersPending', backref='ordpen')
    #orders_comp = db.relationship('OrdersComplete', backref='ordcomp')

class OrdersPending(db.Model):
    ordid = db.Column(db.Integer, primary_key=True)
    content = db.Column(db.String(200), nullable=False)
    amount = db.Column(db.Float, nullable=False)
    tableid = db.Column(db.Integer, db.ForeignKey('tabletab.tableid'))

class OrdersComplete(db.Model):
    ordid = db.Column(db.Integer, primary_key=True)
    content = db.Column(db.String(200), nullable=False)
    amount = db.Column(db.Float, nullable=False)
    tableid = db.Column(db.Integer)

class Users(db.Model):
    username = db.Column(db.String(40), primary_key=True)
    password_hash = db.Column(db.String, nullable=False)
    role = db.Column(db.String(20), nullable=False)

class Categories(db.Model):
    category = db.Column(db.String(20), primary_key=True)
    menu_add = db.relationship('MenuTab', backref='item')


#db.create_all()

# Define parser objects and field types for all tables in database
menu_put_item = reqparse.RequestParser()
menu_put_item.add_argument("item_name", type=str,required=True, help="Item name not sent")
menu_put_item.add_argument("price", type=int,required=True, help="Price of item is required")
menu_put_item.add_argument("category", type=str, required=True, help="Category not given")

menu_update_item = reqparse.RequestParser()
menu_update_item.add_argument("item_name", type=str)
menu_update_item.add_argument("price", type=int)
menu_update_item.add_argument("category", type=str)

table_put_item = reqparse.RequestParser()
table_put_item.add_argument("encodstr", type=str, required=True, help="Table encoded string not ")

ordpen_put_item = reqparse.RequestParser()
ordpen_put_item.add_argument("content", type=str, required=True, help="No food items entered")
ordpen_put_item.add_argument("amount", type=float, required=True, help="Amount not entered")
ordpen_put_item.add_argument("tableid", type=int, required=True, help="table id not entered")

ordpen_update_item = reqparse.RequestParser()
ordpen_update_item.add_argument("content", type=str)
ordpen_update_item.add_argument("amount", type=float)
ordpen_update_item.add_argument("tableid", type=int)

ordcomp_put_item = reqparse.RequestParser()
ordcomp_put_item.add_argument("content", type=str, required=True, help="No food items entered")
ordcomp_put_item.add_argument("amount", type=float, required=True, help="Amount not entered")
ordcomp_put_item.add_argument("tableid", type=int, required=True, help="table id not entered")

user_put_item = reqparse.RequestParser()
user_put_item.add_argument("username", type=str, required=True, help="username required")
user_put_item.add_argument("password_hash", type=str, required=True, help="password required")
user_put_item.add_argument("role", type=str, required=True, help="role of user required")

user_login_item = reqparse.RequestParser()
user_login_item.add_argument("username", type=str, required=True, help="username required")
user_login_item.add_argument("password_hash", type=str, required=True, help="password required")

user_update_item = reqparse.RequestParser()
user_update_item.add_argument("username", type=str, required=True)
user_update_item.add_argument("password_hash", type=str)
user_update_item.add_argument("role", type=str)


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
# ############################################################################################################
# class Catadd(Resource):
#     def put(self, categoryadd):
#         fetch_category = Categories.query.filter_by(category = categoryadd).first()
#         if fetch_category:
#             return jsonify({"status":-1, "message":"Category already exists"})
#         add_category = Categories(category = categoryadd)
#         db.session.add(add_category)
#         db.session.commit()
#         response = jsonify({"status":1, "message":"Category added successfully"})
#         #print(response)
#         return response
# ############################################################################################################
# #THIS CLASS WILL BE USED FOR CRUD OPERATIONS USING SENT ITEM_ID AND DATA FOR MENU TABLE
# class Menu(Resource):
#     @marshal_with(resource_menu_fields)
#     def put(self):
#         args = menu_put_item.parse_args()
#         result = MenuTab.query.filter_by(item_name = args['item_name']).first()
#         if result:
#             # abort(409, message="Item name taken",status=-1)
#             response = jsonify({"status":-1, "message":"Item name already exists"})
#             return response

#         fetch_category = Categories.query.filter_by(category=args['category']).first()
#         if not fetch_category:
#             add_category = Categories(category=args['category'])
#             db.session.add(add_category)
#             db.session.commit()

#         fetch_category = Categories.query.filter_by(category=args['category']).first()
#         menu_item = MenuTab(item_name=args['item_name'], price=args['price'], item = fetch_category)
#         db.session.add(menu_item)
#         db.session.commit()
#         # abort(409, message="Item added successfully",status=1)
#         return jsonify({"status":1, "message":"Item added successfully"})
#         #return "OK"

#     @marshal_with(resource_menu_fields)
#     def get(self):
#         args = menu_update_item.parse_args()
#         result = MenuTab.query.filter_by(item_name = args['item_name']).first()
#         if not result:
#             abort(404, message="Could not find any food item with that item_id", status=-1)
#             #return jsonify({"status":-1, "message":"No item with that name"})
#         return result

#     @marshal_with(resource_menu_fields)
#     def patch(self):
#         args = menu_update_item.parse_args()
#         result = MenuTab.query.filter_by(item_name = args['item_name']).first()
#         if not result:
#             abort(404, message="Could not find any food item with that item_id", status=-1)
#             #return jsonify({"status":-1, "message":"No item with that name"})
        
#         if args['price']:
#             result.price = args['price']
#         if args['category']:
#             fetch_category = Categories.query.filter_by(category=args['category']).first()
#             if not fetch_category:
#                 add_category = Categories(category=args['category'])
#                 db.session.add(add_category)
#                 db.session.commit()
#             result.category = args['category']
#         db.session.commit()
#         abort(409, message="Item updated successfully", status=1)
#         #return jsonify({"status":1, "message":"Item updated successfully"}) 

#     def delete(self):
#         args = menu_update_item.parse_args()
#         result = MenuTab.query.filter_by(item_name = args['item_name']).first()
#         if not result:
#             abort(404, message="Could not find any food item with that item_id")
#             #return jsonify({"status":-1, "message":"No item with that name"})
#         db.session.delete(result)
#         db.session.commit()
#         return jsonify({"status":1, "message":"Item deleted successfully"})
# ############################################################################################################
# #THIS CLASS WILL BE USED FOR Create and Delete OPERATIONS USING SENT Table_ID AND DATA FOR TableTab TABLE
# class Table(Resource):
#     @marshal_with(resource_table_fields)
#     def get(self,table_id):
#         result = TableTab.query.filter_by(tableid=table_id).first()
#         if not result:
#             abort(404, message="Could not find any table with that table_id", status=-1)
#         return result

#     @marshal_with(resource_table_fields)
#     def put(self, table_id):
#         args = table_put_item.parse_args()
#         result = TableTab.query.filter_by(tableid=table_id).first()
#         if result:
#             abort(409, message="Table id taken", status=-1)
#         tabledata = TableTab(tableid=table_id, encodstr = args['encodstr'])
#         db.session.add(tabledata)
#         db.session.commit()
#         abort(409, message="Table added", status=1)
#         #return tabledata, 201    

#     def delete(self,table_id):
#         result = TableTab.query.filter_by(tableid=table_id).first()
#         if not result:
#             abort(404, message="Could not find any table with that table_id")
#         db.session.delete(result)
#         db.session.commit()
#         abort(409, message="Table deleted", status=1)
#         #return '', 204
# ############################################################################################################
# ############################################################################################################
# #THIS CLASS WILL BE USED FOR CRUD OPERATIONS USING SENT Table_ID AND DATA FOR OrdersPending TABLE
# class OrdPen(Resource):
#     @marshal_with(resource_order_fields)
#     def get(self,order_id):
#         result = OrdersPending.query.filter_by(ordid=order_id).first()
#         if not result:
#             abort(404, message="Could not find any pending order with that order_id")
#             #return jsonify({"status":-1, "message":"No Order with that order_id"})
#         return result
    
#     @marshal_with(resource_order_fields)
#     def put(self, order_id):
#         args = ordpen_put_item.parse_args()
#         result = OrdersPending.query.filter_by(ordid=order_id).first()
#         if result:
#             abort(409, message="Order id taken", status = 1)
#             #return jsonify({"status":-1, "message":"Order id taken"})

#         fetch_table = TableTab.query.filter_by(tableid = args['tableid']).first()
#         if not fetch_table:
#             abort(404, message="No table exists for given table id", status = 1)
#             #return jsonify({"status":-1, "message":"No table exists for given table id"})

#         order_item = OrdersPending(ordid=order_id, content=args['content'], amount=args['amount'], ordpen = fetch_table)
#         db.session.add(order_item)
#         db.session.commit()
#         #return order_item, 201
#         abort(404, message="Pending Order added successfully", status = 1)
#         #return jsonify({"status":1, "message":"Pending Order added successfully"})

#     @marshal_with(resource_order_fields)
#     def patch(self, order_id):
#         args = ordpen_update_item.parse_args()
#         result = OrdersPending.query.filter_by(ordid=order_id).first()
#         if not result:
#             abort(404, message="Could not find any order to update with that order id")
#             #return jsonify({"status":-1, "message":"No pending order with that order id"})
        
#         if args['amount']:
#             result.amount = args['amount']
#         if args['content']:
#             result.content = args['content']
#         if args['tableid']:
#             fetch_table = TableTab.query.filter_by(tableid = args['tableid']).first()
#             if not fetch_table:
#                 abort(404, message="No table exists for given table id")
#                 #return jsonify({"status":-1, "message":"No table exists for given table id"})
#             result.tableid = args['tableid']
#         db.session.commit()
#         #return result
#         abort(404,message="Pending order updated successfully", status=1)
#         #return jsonify({"status":1, "message":"Pending order updated successfully"})
    
#     def delete(self,order_id):
#         result = OrdersPending.query.filter_by(ordid=order_id).first()
#         if not result:
#             #abort(404, message="No pending orders with that order id")
#             return jsonify({"status":-1, "message":"No pending order with that order id"})
#         db.session.delete(result)
#         db.session.commit()
#         #return '', 204
#         abort(404,message="Pending order deleted successfully", status=1)      #return jsonify({"status":1, "message":"Pending order deleted successfully"})


# ############################################################################################################
# #THIS CLASS WILL BE USED FOR CRUD OPERATIONS USING SENT Table_ID AND DATA FOR OrdersComplete TABLE
# class OrdComp(Resource):
#     @marshal_with(resource_order_fields)
#     def get(self,order_id):
#         result = OrdersComplete.query.filter_by(ordid=order_id).first()
#         if not result:
#             abort(404, message="Could not find any Completed order with that order_id", status=-1)
#             #return jsonify({"status":-1, "message":"No completed order with that order id"})
#         return result

#     @marshal_with(resource_order_fields)
#     def put(self, order_id):
#         args = ordcomp_put_item.parse_args()
#         result = OrdersComplete.query.filter_by(ordid=order_id).first()

#         if result:
#             abort(409, message="Order already completed with this Order id", status=-1)
#             #return jsonify({"status":-1, "message":"Order already completed with this Order id"})

#         order_item = OrdersComplete(ordid=order_id, content=args['content'], amount=args['amount'], tableid=args['tableid'])
#         db.session.add(order_item)
#         db.session.commit()
#         #return order_item, 201
#         abort(409, message="Completed Order added successfully", status=-1)
#         #return jsonify({"status":1, "message":"Completed Order added successfully"})

#     def delete(self,order_id):
#         result = OrdersComplete.query.filter_by(ordid=order_id).first()
#         if not result:
#             #abort(404, message="No Completed orders with that order id")
#             return jsonify({"status":-1, "message":"No completed order with that order id"})
#         db.session.delete(result)
#         db.session.commit()
#         #return '', 204
#         abort(409, message="Completed Order deleted successfully", status=1)
#         #return jsonify({"status":1, "message":"Completed order deleted successfully"})    
# ############################################################################################################
# class UserFunctions(Resource):
    
#     def put(self):
#         args = user_put_item.parse_args()
#         result = Users.query.filter_by(username=args['username']).first()
#         if result:
#             #abort(409, message="Username already taken")
#             return jsonify({"status":-1, "message":"Username already taken"})
#         user_item = Users(username=args['username'], password_hash=args['password_hash'], role=args['role'])
#         db.session.add(user_item)
#         db.session.commit()
#         #return '', 204
#         return jsonify({"status":1, "message":"User added successfully"})

#     def delete(self):
#         args = user_update_item.parse_args()
#         result = Users.query.filter_by(username=args['username']).first()
#         if not result:
#             #abort(404, message="No such user exists")
#             return jsonify({"status":-1, "message":"No such user exists"})
#         db.session.delete(result)
#         db.session.commit()
#         #return '', 204
#         return jsonify({"status":1, "message":"User deleted successfully"})


# api.add_resource(Menu, "/menufunc")
# api.add_resource(Catadd, "/catadd/<string:categoryadd>")
# api.add_resource(Table, "/table/<int:table_id>")
# api.add_resource(OrdPen, "/ordpen/<int:order_id>")
# api.add_resource(OrdComp, "/ordcomp/<int:order_id>")
# api.add_resource(UserFunctions, "/user")

# Add new user
@app.route('/AddNewUser', methods=['PUT'])
def add_new_user():
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
    full_categories = Categories.query.all()
    output = []
    for category in full_categories:
        category_data = {}
        category_data['category'] = category.category
        output.append(category_data)

    return jsonify(output)


# Returns the entire menu
@app.route('/GetFullMenu',methods=['GET'])
def get_full_menu():
    fullmenu = MenuTab.query.all()
    output = []
    for item in fullmenu:
        item_data = {}
        item_data['item_name'] = item.item_name
        item_data['price'] = item.price
        item_data['category'] = item.category
        output.append(item_data)
    
    return jsonify(output)


# Returns a list of all 'tables' in the restaurant
@app.route('/GetAllTablesInfo', methods=['GET'])
def all_tables():
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
    pendinglist = OrdersPending.query.all()
    output = []
    for order in pendinglist:
        order_data = {}
        order_data['ordid'] = order.ordid
        order_data['content'] = order.content
        order_data['amount'] = order.amount
        order_data['tableid'] = order.tableid
        output.append(order_data)
    return jsonify(output)


# Returns all completed orders
@app.route('/GetCompletedOrders', methods=['GET'])
def all_completed():
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

