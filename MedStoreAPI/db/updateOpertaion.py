import sqlite3

def updateUserAllFields(userID , **ketword):

    conn = sqlite3.connect('my_medicalshop.db')
    cursor = conn.cursor()

    for key, value in ketword.items():
        if key == "name":
            cursor.execute("UPDATE Users SET name = ? WHERE user_id =?", (value, userID,))

        elif key == "email":
            cursor.execute("UPDATE Users SET email =? WHERE user_id =?", (value, userID,))

        elif key == "password":
            cursor.execute("UPDATE Users SET password =? WHERE user_id =?", (value, userID,))

        elif key == "phoneNumber":
            cursor.execute("UPDATE Users SET phone_number =? WHERE user_id =?", (value, userID,))

        elif key == "pinCode":
            cursor.execute("UPDATE Users SET pinCode =? WHERE user_id =?", (value, userID,))

        elif key == "address":
            cursor.execute("UPDATE Users SET address =? WHERE user_id =?", (value, userID,))

        elif key == "isApproved":
            cursor.execute("UPDATE Users SET isApproved =? WHERE user_id =?", (value, userID,))
            
        else:
            return {"status": "error", "message": "Not updatable"}
        
    conn.commit()
    conn.close()
        
    
def updateProductAllFields(productID , **ketword):

    conn = sqlite3.connect('my_medicalshop.db')
    cursor = conn.cursor()

    for key, value in ketword.items():
        if key == "product_name":
            cursor.execute("UPDATE Product_table SET product_name = ? WHERE product_id =?", (value, productID,))

        elif key == "expiry_date":
            cursor.execute("UPDATE Product_table SET expiry_date =? WHERE product_id =?", (value, productID,))

        elif key == "price":
            cursor.execute("UPDATE Product_table SET price =? WHERE product_id =?", (value, productID,))

        elif key == "stock":
            cursor.execute("UPDATE Product_table SET stock =? WHERE product_id =?", (value, productID,))

        elif key == "category":
            cursor.execute("UPDATE Product_table SET category =? WHERE product_id =?", (value, productID,))

        else:
            return {"status": "error", "message": "Not updatable"}
        
    conn.commit()
    conn.close()
        
def updateOrderAllFields(orderID , **ketword):

    conn = sqlite3.connect('my_medicalshop.db')
    cursor = conn.cursor()

    for key, value in ketword.items():
        if key == "Quantity":
            cursor.execute("UPDATE Order_table SET quantity = ? WHERE order_id =?", (value, orderID,))

        elif key == "PED":
            cursor.execute("UPDATE Order_table SET product_expiry_date =? WHERE order_id =?", (value, orderID,))

        elif key == "isApproved":
            cursor.execute("UPDATE Order_table SET isApproved =? WHERE order_id =?", (value, orderID,))    

        else:
            return {"status": "error", "message": "Not updatable"}


    conn.commit()
    conn.close()
        