import sqlite3
import json


def getAllUsers():
    conn = sqlite3.connect('my_medicalshop.db')
    cursor = conn.cursor()

    cursor.execute('SELECT * FROM Users')
    users = cursor.fetchall()
    conn.commit()
    conn.close()

    userJson = []

    for user in users:
        tempUser = {
            "id": user[0],
            "user_id": user[1],
            "password": user[2],
            "level": user[3],
            "date_of_account_creation": user[4],
            "isApproved": user[5],
            "block": user[6],
            "name": user[7],
            "email": user[8],
            "phone_number": user[9],
            "pinCode": user[10],
            "address": user[11]
        }
        userJson.append(tempUser)

    return(json.dumps(userJson))


def get_product():
    conn = sqlite3.connect('my_medicalshop.db')
    cursor = conn.cursor()

    cursor.execute("SELECT * FROM Product_table")
    products = cursor.fetchall()
    conn.commit()
    conn.close()

    productJson = []

    for product in products:
        tempProduct = {
            "id": product[0],
            "product_id": product[1],
            "product_name": product[2],
            "expiry_date": product[3],
            "price": product[4],
            "stock": product[5],
            "category": product[6]
        }
        productJson.append(tempProduct)

    return(json.dumps(productJson))

def getSpecificProduct(productID):

    conn = sqlite3.connect('my_medicalshop.db')
    cursor = conn.cursor()

    cursor.execute("SELECT * FROM Product_table WHERE product_id =?", (productID,))
    products = cursor.fetchall()
    conn.close()

    userJson = []

    for product in products:
        tempUser = {
            "id": product[0],
            "product_id": product[1],
            "product_name": product[2],
            "price": product[4],
            "stock": product[5],
            "category": product[6],
            "expiry_date": product[3]
        }
        userJson.append(tempUser)

    return(json.dumps(tempUser))


def getSpecificOrder(orderID):

    conn = sqlite3.connect('my_medicalshop.db')
    cursor = conn.cursor()

    cursor.execute("SELECT * FROM Order_table WHERE order_id =?", (orderID,))
    products = cursor.fetchall()
    conn.close()

    userJson = []

    for product in products:
        tempUser = {
            "id": product[0],
            "user_id": product[2],
            "product_id": product[3],
            "quantity": product[4],
            "category": product[5],
            "product_expiry_date": product[6],
            "order_date": product[7],
            "total_price": product[8]
        }
        userJson.append(tempUser)

    return(json.dumps(tempUser))


def getSpecificUser(userID):

    conn = sqlite3.connect('my_medicalshop.db')
    cursor = conn.cursor()

    cursor.execute("SELECT * FROM Users WHERE user_id =?", (userID,))
    users = cursor.fetchall()
    conn.close()

    userJson = []

    for user in users:
        tempUser = {
            "id": user[0],
            "user_id": user[1],
            "password": user[2],
            "level": user[3],
            "date_of_account_creation": user[4],
            "isApproved": user[5],
            "block": user[6],
            "name": user[7],
            "email": user[8],
            "phone_number": user[9],
            "pinCode": user[10],
            "address": user[11]
        }
        userJson.append(tempUser)

    return(json.dumps(tempUser))


def getAllOrders():

    conn = sqlite3.connect('my_medicalshop.db')
    cursor = conn.cursor()

    cursor.execute('SELECT * FROM Order_table')
    users = cursor.fetchall()
    conn.commit()
    conn.close()

    userJson = []

    for user in users:
        tempUser = {
            "id": user[0],
            "order_id": user[1],
            "user_id": user[2],
            "product_id": user[3],
            "quantity": user[4],
            "category" : user[5],
            "product_expiry_date" : user[6],
            "order_date": user[7],
            "total_price": user[8],
            "isApproved": user[9]
        }
        userJson.append(tempUser)

    return(json.dumps(userJson))


def delete_all_users():

    conn = sqlite3.connect('my_medicalshop.db')
    cursor = conn.cursor()

    cursor.execute("DELETE FROM Users")

    conn.commit()
    conn.close()


def delete_specific_user(user_id): 

    conn = sqlite3.connect('my_medicalshop.db')
    cursor = conn.cursor()

    cursor.execute("DELETE FROM Users WHERE user_id =?",(user_id,)) 

    conn.commit()
    conn.close()  


def delete_all_products():

    conn = sqlite3.connect('my_medicalshop.db')
    cursor = conn.cursor()

    cursor.execute("DELETE FROM Product_table")

    conn.commit()
    conn.close()


def delete_specific_product(product_id): 

    conn = sqlite3.connect('my_medicalshop.db')
    cursor = conn.cursor()

    cursor.execute("DELETE FROM Product_table WHERE product_id =?",(product_id,)) 

    conn.commit()
    conn.close()  

def delete_specific_order(order_id): 

    conn = sqlite3.connect('my_medicalshop.db')
    cursor = conn.cursor()

    cursor.execute("DELETE FROM Order_table WHERE order_id =?",(order_id,)) 

    conn.commit()
    conn.close()  



