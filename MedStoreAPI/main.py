from flask import Flask  , request , jsonify
from db.createTableOpertaion import createTables ,  addColumnDynamic
from db.addOperation import createUser , createProduct , createOrder
from db.readOperation import getAllUsers , getSpecificUser , get_product , getAllOrders , delete_all_users , delete_specific_user , delete_all_products , delete_specific_product , getSpecificProduct , getSpecificOrder , delete_specific_order
from auth import user_auth 
from db.updateOpertaion import updateUserAllFields , updateProductAllFields , updateOrderAllFields
import sqlite3


app = Flask(__name__)

@app.route('/',methods=['GET'])
def home():
    return "Hello, World!"


def get_request_data():
    if request.is_json:
        return request.json
    else:
        return request.form.to_dict()


@app.route('/signUp',methods=['POST'])
def signUp():
    data = get_request_data()
    print(f"Received data: {data}")
    name1 = data.get('name')
    password = data.get('password')
    email = data.get('email')
    phone = data.get('phoneNumber')
    address = data.get('address')
    pinCode = data.get('pinCode')

    if not name1:
        return jsonify({"error": "Name is required"}), 403

    result = createUser(name= name1 , password=password , phone_number=phone , email=email , pinCode=pinCode , address=address)
    
    if result['status'] == 'success':
        return jsonify({"message": "User created successfully", "user_id": result['user_id'] , "name" : result['name']}), 201
    else:
        return jsonify({"error": result['message']}), 400


@app.route('/ipProduct',methods=['POST'])
def ipProduct():
    try:
        data = get_request_data()
        pName = data.get('Product Name')
        pED = data.get('Expiry Date')
        pPrice = data.get('Price')
        pQuantity = data.get('Stock')
        pCategory = data.get('Category')

        result = createProduct(product_name=pName, expiry_date=pED, price=pPrice, stock=pQuantity, category=pCategory)

        if result['status'] == 'success':
            return jsonify({"message": "User created successfully", "product_id": result['product_id']}), 201
        else:
            return jsonify({"error": result['message']}), 400

      
    except Exception as e:
        return jsonify({"status" : 400 , "message" : str(e)})

 
@app.route('/login',methods=['POST'])
def login():
    try:
        data = get_request_data()
        email = data.get('email')
        password = data.get('password')
        logindata = user_auth(email=email, password=password)
    
        if logindata:
            return jsonify({"status" : 200 , "message" : logindata[1]})
        else:
            return jsonify({"status" : 400 , "message" : "Invalid User"})
        
    except Exception as e:
        return jsonify({"status" : 400 , "message" : str(e)})
    

@app.route('/getAllUsers',methods=['GET'])
def getAllUser():
    return getAllUsers()
   

@app.route('/get_all_products',methods=['GET'])
def get_all_product():
    return get_product()


@app.route('/getSpecificUser',methods=['POST'])
def getSpecificUseMain1():
    try:
        data = get_request_data()
        userID = data.get('userID')
        getUserInfo = getSpecificUser(userID= userID)
        return getUserInfo
    except Exception as e:
        return jsonify({"status" : 400, "message" : str(e)})
    

@app.route('/getSpecificOrder',methods=['POST'])
def getSpecificUseMain2():
    try:
        data = get_request_data()
        userID = data.get('orderID')
        getUserInfo = getSpecificOrder(orderID= userID)
        return getUserInfo
    except Exception as e:
        return jsonify({"status" : 400, "message" : str(e)}) 
    

@app.route('/getSpecificProduct',methods=['POST'])
def getSpecificUseMain3():
    try:
        data = get_request_data()
        productID = data.get('productID')
        getUserInfo = getSpecificProduct(productID= productID)
        return getUserInfo
    except Exception as e:
        return jsonify({"status" : 400, "message" : str(e)})       
    
    
@app.route('/updateUserDetails',methods=['PATCH'])
def updateUserNameMain():
    try:
        data = get_request_data()
        userID = data.get('user_id')
        
        allFields = request.form.items()

        updateUser = {}
        
        for key,value in allFields:
            if key != 'user_id':
                updateUser[key] = value

        updateUserAllFields(userID=userID, **updateUser)

        return jsonify({"status" : 200 ,"message": "User Name updated successfully"})      

    except Exception as e:
        return jsonify({"status" : 400 , "message" : str(e)})


@app.route('/updateProductDetails',methods=['PATCH'])
def updateProductDetails():
    try:
        data = get_request_data()
        productID = data.get('product_id')
        
        allFields = request.form.items()

        updateProduct = {}
        
        for key,value in allFields:
            if key != 'product_id':
                updateProduct[key] = value

        updateProductAllFields(productID= productID, **updateProduct)

        return jsonify({"status" : 200 ,"message": "Product Details updated successfully"})      

    except Exception as e:
        return jsonify({"status" : 400 , "message" : str(e)})


@app.route("/updateOrderDetails", methods=['PATCH'])
def updateOrderDetails():
    try:
        data = get_request_data()
        orderID = data.get('orderID')

        allFields = request.form.items()

        updateOrder = {}
        
        for key,value in allFields:
            if key != 'orderID':
                updateOrder[key] = value

        updateOrderAllFields(orderID=orderID, **updateOrder)
        

        return jsonify({"status" : 200 ,"message": "Order Details updated successfully"})      

    except Exception as e:
        return jsonify({"status" : 400 , "message" : str(e)})

        

@app.route('/getOrders',methods=['POST'])
def createOrders():
    try:

        data = get_request_data()
        userID = data.get('userID')
        productID = data.get('productID')
        quantity1 = data.get('quantity')
        order_date = data.get('orderDate')
        
        r = createOrder(user_id=userID, product_id=productID, quantity=quantity1, order_date= order_date)

        if r['status'] == 'success' :
            return jsonify({"status" : 200 , "Order ID" : r['order_id']})
        else:
            return jsonify({"status" : 405 , "message" : r['message'] , })
        
    except Exception as e:
        return jsonify({"status" : 400, "message" : str(e) })
    
    
@app.route('/getAllOrders',methods=['GET'])
def get_all_orders():
    return getAllOrders()


@app.route('/deleteAllUsers',methods=['DELETE'])
def deleteAll():
    try:
        delete_all_users()
        return jsonify({"status" : 200 , "message" : "All Users deleted successfully"})
    except Exception as e:
        return jsonify({"status" : 400, "message" : str(e)})
    

@app.route('/deleteSpecificUser',methods=['DELETE'])
def deleteUser():
    try:
        data = get_request_data()
        userID = data.get('userID')
        delete_specific_user(userID=userID)
        return jsonify({"status" : 200 , "message" : "User deleted successfully"})
    except Exception as e:
        return jsonify({"status" : 400, "message" : str(e)})


@app.route('/deleteAllProducts',methods=['DELETE'])
def deleteAllProduct():
    try:
        delete_all_products()
        return jsonify({"status" : 200 , "message" : "All Products deleted successfully"})
    except Exception as e:
        return jsonify({"status" : 400, "message" : str(e)})
    

@app.route('/deleteSpecificProduct',methods=['DELETE'])
def deleteProduct():
    try:
        data = get_request_data()
        productID = data.get('productID')
        delete_specific_product(product_id=productID)
        return jsonify({"status" : 200 , "message" : "Product deleted successfully"})
    except Exception as e:
        return jsonify({"status" : 400, "message" : str(e)})

@app.route('/deleteSpecificOrder',methods=['DELETE'])
def deleteOrder():
    try:
        data = get_request_data()
        productID = data.get('orderID')
        delete_specific_order(order_id=productID)
        return jsonify({"status" : 200 , "message" : "Order deleted successfully"})
    except Exception as e:
        return jsonify({"status" : 400, "message" : str(e)})    


# Starting the server
if __name__ == "__main__":
    createTables()
    # addColumn()
    addColumnDynamic("isApproved", "BOOLEAN")
    app.run(debug=True)

    
