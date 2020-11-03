from flask import Flask
from flask_restful import Api, Resource, reqparse, abort, fields, marshal_with
from flask_sqlalchemy import SQLAlchemy
from flask import jsonify


app = Flask(__name__)
api = Api(app)
app.config['SQLALCHEMY_DATABASE_URI'] = "sqlite:///database.db"
db = SQLAlchemy(app)

class MenuTab(db.Model):
    id = db.Column(db.Integer, primary_key=True)
    item_name = db.Column(db.String(100), nullable=False)
    price = db.Column(db.Integer, nullable=False)

    def __repr__(self):
        return f"Menu(items = {item_name}, price = {price})"

class TableTab(db.Model):
    tableid = db.Column(db.Integer, primary_key=True)
    encodstr = db.Column(db.String(100), unique=True, nullable=False)
    #orders_pen = db.relationship('OrdersPending', backref='ordpen')
    #orders_comp = db.relationship('OrdersComplete', backref='ordcomp')

class OrdersPending(db.Model):
    ordid = db.Column(db.Integer, primary_key=True)
    content = db.Column(db.String(200), nullable=False, unique=True)
    amount = db.Column(db.Float, nullable=False)
    tableid = db.Column(db.Integer)

class OrdersComplete(db.Model):
    ordid = db.Column(db.Integer, primary_key=True)
    content = db.Column(db.String(200), nullable=False)
    amount = db.Column(db.Float, nullable=False)
    tableid = db.Column(db.Integer)

class Users(db.Model):
    username = db.Column(db.String(40), primary_key=True)
    password_hash = db.Column(db.String, nullable=False)
    role = db.Column(db.String(20), nullable=False)


#db.create_all()



############################################################################################################
#THIS SECTION WILL DEFINE PARSER OBJECTS AND FIELD TYPES FOR ALL THE TABLES IN THE DATABASE
menu_put_item = reqparse.RequestParser()
menu_put_item.add_argument("item_name", type=str,required=True, help="Item name not sent")
menu_put_item.add_argument("price", type=int,required=True, help="Price of item is required")

menu_update_item = reqparse.RequestParser()
menu_update_item.add_argument("item_name", type=str)
menu_update_item.add_argument("price", type=int)


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
    'id' : fields.Integer,
    'item_name' : fields.String,
    'price' : fields.Integer
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

############################################################################################################
#THIS CLASS WILL BE USED FOR CRUD OPERATIONS USING SENT ITEM_ID AND DATA FOR MENU TABLE
class Menu(Resource):
    @marshal_with(resource_menu_fields)
    def put(self, item_id):
        args = menu_put_item.parse_args()
        result = MenuTab.query.filter_by(id=item_id).first()
        if result:
            abort(409, message="Item id taken")
        menu_item = MenuTab(id=item_id, item_name=args['item_name'], price=args['price'])
        db.session.add(menu_item)
        db.session.commit()
        return menu_item, 201

    @marshal_with(resource_menu_fields)
    def get(self,item_id):
        result = MenuTab.query.filter_by(id=item_id).first()
        if not result:
            abort(404, message="Could not find any food item with that item_id")
        return result

    @marshal_with(resource_menu_fields)
    def patch(self, item_id):
        args = menu_update_item.parse_args()
        result = MenuTab.query.filter_by(id=item_id).first()
        if not result:
            abort(404, message="Could not find any food item with that item_id")
        
        if args['price']:
            result.price = args['price']
        if args['item_name']:
            result.item_name = args['item_name']
        db.session.commit()
        return result    

    def delete(self,item_id):
        result = MenuTab.query.filter_by(id=item_id).first()
        if not result:
            abort(404, message="Could not find any food item with that item_id")
        db.session.delete(result)
        db.session.commit()
        return '', 204
############################################################################################################
#THIS CLASS WILL BE USED FOR Create and Delete OPERATIONS USING SENT Table_ID AND DATA FOR TableTab TABLE
class Table(Resource):
    @marshal_with(resource_table_fields)
    def get(self,table_id):
        result = TableTab.query.filter_by(tableid=table_id).first()
        if not result:
            abort(404, message="Could not find any table with that table_id")
        return result

    @marshal_with(resource_table_fields)
    def put(self, table_id):
        args = table_put_item.parse_args()
        result = TableTab.query.filter_by(tableid=table_id).first()
        if result:
            abort(409, message="Table id taken")
        tabledata = TableTab(tableid=table_id, encodstr = args['encodstr'])
        db.session.add(tabledata)
        db.session.commit()
        return tabledata, 201    

    def delete(self,table_id):
        result = TableTab.query.filter_by(tableid=table_id).first()
        if not result:
            abort(404, message="Could not find any table with that table_id")
        db.session.delete(result)
        db.session.commit()
        return '', 204
############################################################################################################
############################################################################################################
#THIS CLASS WILL BE USED FOR CRUD OPERATIONS USING SENT Table_ID AND DATA FOR OrdersPending TABLE
class OrdPen(Resource):
    @marshal_with(resource_order_fields)
    def get(self,order_id):
        result = OrdersPending.query.filter_by(ordid=order_id).first()
        if not result:
            abort(404, message="Could not find any pending order with that order_id")
        return result
    
    @marshal_with(resource_order_fields)
    def put(self, order_id):
        args = ordpen_put_item.parse_args()
        result = OrdersPending.query.filter_by(ordid=order_id).first()
        if result:
            abort(409, message="Order id taken")
        order_item = OrdersPending(ordid=order_id, content=args['content'], amount=args['amount'], tableid=args['tableid'])
        db.session.add(order_item)
        db.session.commit()
        return order_item, 201

    @marshal_with(resource_order_fields)
    def patch(self, order_id):
        args = ordpen_update_item.parse_args()
        result = OrdersPending.query.filter_by(ordid=order_id).first()
        if not result:
            abort(404, message="Could not find any order to update with that order id")
        
        if args['amount']:
            result.amount = args['amount']
        if args['content']:
            result.content = args['content']
        if args['tableid']:
            result.tableid = args['tableid']    
        db.session.commit()
        return result
    
    def delete(self,order_id):
        result = OrdersPending.query.filter_by(ordid=order_id).first()
        if not result:
            abort(404, message="No pending orders with that order id")
        db.session.delete(result)
        db.session.commit()
        return '', 204


############################################################################################################
#THIS CLASS WILL BE USED FOR CRUD OPERATIONS USING SENT Table_ID AND DATA FOR OrdersComplete TABLE
class OrdComp(Resource):
    @marshal_with(resource_order_fields)
    def get(self,order_id):
        result = OrdersComplete.query.filter_by(ordid=order_id).first()
        if not result:
            abort(404, message="Could not find any Completed order with that order_id")
        return result

    @marshal_with(resource_order_fields)
    def put(self, order_id):
        args = ordcomp_put_item.parse_args()
        result = OrdersComplete.query.filter_by(ordid=order_id).first()
        if result:
            abort(409, message="Order already completed with this Order id")
        order_item = OrdersComplete(ordid=order_id, content=args['content'], amount=args['amount'], tableid=args['tableid'])
        db.session.add(order_item)
        db.session.commit()
        return order_item, 201

    def delete(self,order_id):
        result = OrdersComplete.query.filter_by(ordid=order_id).first()
        if not result:
            abort(404, message="No Completed orders with that order id")
        db.session.delete(result)
        db.session.commit()
        return '', 204    
############################################################################################################
class UserFunctions(Resource):
    
    def put(self):
        args = user_put_item.parse_args()
        result = Users.query.filter_by(username=args['username']).first()
        if result:
            return jsonify({"status" : -1, "message" : "Username already taken"})
            
        user_item = Users(username=args['username'], password_hash=args['password_hash'], role=args['role'])
        db.session.add(user_item)
        db.session.commit()
        return jsonify({"status" : 1, "message" : "User successfully added"})

    def delete(self):
        args = user_update_item.parse_args()
        result = Users.query.filter_by(username=args['username']).first()
        if not result:
            return jsonify({"status" : -1, "message" : "Username does not exist"})

        db.session.delete(result)
        db.session.commit()
        return jsonify({"status" : 1, "message" : "User successfully deleted"})



############################################################################################################
api.add_resource(Menu, "/menu/<int:item_id>")
api.add_resource(Table, "/table/<int:table_id>")
api.add_resource(OrdPen, "/ordpen/<int:order_id>")
api.add_resource(OrdComp, "/ordcomp/<int:order_id>")
api.add_resource(UserFunctions, "/user")
#########################################################################################################
#TO VERIFY USER LOGIN INFO
@app.route('/login',methods=['PUT'])
def login():
    args = user_login_item.parse_args()
    result = Users.query.filter_by(username=args['username']).first()
    if not result:
        return jsonify({"status" : -1, "message" : "Username does not exist"})
    if(result.password_hash != args['password_hash']):
        return jsonify({"status" : -1, "message" : "Password does not match"})
    else:
        return jsonify({"status" : 0, "message" : "Login Successful"})

#########################################################################################################
#TO GET FULL MENU
@app.route('/menu',methods=['GET'])
def all_prod():
    fullmenu = MenuTab.query.all()
    output = []
    for item in fullmenu:
        item_data = {}
        item_data['id'] = item.id
        item_data['item_name'] = item.item_name
        item_data['price'] = item.price
        output.append(item_data)
    
    return jsonify(output)
############################################################################################################
#To get list of all tables
@app.route('/table', methods=['GET'])
def all_tables():
    tablelist = TableTab.query.all()
    output = []
    for table in tablelist:
        table_data = {}
        table_data['tableid'] = table.tableid
        table_data['encodstr'] = table.encodstr
        output.append(table_data)
    
    return jsonify(output)
############################################################################################################
#To Get all Pending Orders
@app.route('/ordpen', methods=['GET'])
def all_pending():
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

############################################################################################################
#To Get all Completed Orders
@app.route('/ordcomp', methods=['GET'])
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