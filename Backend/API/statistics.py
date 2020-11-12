# Return Sale of a particular item
@app.route('/ItemSale', methods=['GET'])
def item_sale():
    args = request.get_json()
    name = args['item_name']
    prev_days = args['days']

    total_qty = 0
    #todays_datetime = datetime(datetime.today().year, datetime.today().month, datetime.today().day)
    if(prev_days != 0):
        filter_after = datetime.today() - timedelta(days= prev_days)
    else :
        filter_after = datetime(datetime.today().year, datetime.today().month, datetime.today().day)

    #todays_orders = OrdersMaster.query.filter(OrdersMaster.time_end >= todays_datetime).all()
    todays_orders = OrdersMaster.query.filter(OrdersMaster.time_end >= filter_after).all()

    for order in todays_orders:
        id = order.orderid
        #print(id , " iddddd")
        completed = OrdersComplete.query.filter_by(orderid = id).all()
        for x in completed:
            if(x.item_name == name):
                #print(x.orderid, " ID")
                #print(x.quantity)
                total_qty += x.quantity
        
    return jsonify({"item_name": name, "quantity_sold" : total_qty})


# Return Most Sold Items For the Day
@app.route('/MostSoldItems', methods=['GET'])
def most_sold():

    today_orderid_list = []
    filter_after = datetime(datetime.today().year, datetime.today().month, datetime.today().day)
    todays_orders = OrdersMaster.query.filter(OrdersMaster.time_end >= filter_after).all()
    for order in todays_orders:
        today_orderid_list.append(order.orderid)

    sold_items = OrdersComplete.query.all()
    output = []
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
	return jsonify({"status" : -1, "message" : "No Item Sold For Today"})

    mx = max(items.values())
    max_sold = [k for k, v in items.items() if v == mx]

    for x in max_sold :
        temp = {}
        temp['item_name'] = x
        temp['quantity'] = items[x]
        output.append(temp)
    #return jsonify({"item_name" : max_sold, "quantity_sold" : items[max_sold] })
    return jsonify(output)        


# Total Sale For the Day
@app.route('/CurrentDaySale', methods=['GET'])
def total_sale():
    day_sale = 0
    filter_after = datetime(datetime.today().year, datetime.today().month, datetime.today().day)
    todays_orders = OrdersMaster.query.filter(OrdersMaster.time_end >= filter_after).all()
    for order in todays_orders:
        if(order.status == "complete"):
            day_sale += order.amount


    return jsonify({"current_day_sale" : day_sale})

# Average Order Completion Time
@app.route('/AverageOrderCompletionTime', methods=['GET'])
def average_time():
    total_time = 0.0
    count = 0
    filter_after = datetime(datetime.today().year, datetime.today().month, datetime.today().day)
    todays_orders = OrdersMaster.query.filter(OrdersMaster.time_end >= filter_after).all()
    for order in todays_orders:
        count += 1
        start = order.time_start
        end = order.time_end
        delta = (end - start).total_seconds()
        total_time += delta
    avg = total_time / ( 60 * count)
    return jsonify({"Average_Time (Minutes)" : avg})
