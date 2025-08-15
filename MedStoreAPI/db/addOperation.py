import sqlite3
import uuid
from datetime import date

def createUser( name , password , phone_number , email , pinCode , addr ):
    
    conn = sqlite3.connect("my_medicalshop.db")
    cursor = conn.cursor()

    user_id = str(uuid.uuid4())

    date_of_accountcreation = date.today()

    try:
        cursor.execute("""
        INSERT INTO Users (user_id,password,level,date_of_account_creation,isApproved,block,name,email,phone_number,pinCode,address)
        VALUES (?,?,?,?,?,?,?,?,?,?,?)
        """, (user_id,password,1,date_of_accountcreation,0,0,name,email,phone_number,pinCode,address))
    
        conn.commit()
    
        return {"status": "success", "user_id": user_id , "name" : name}
    
    except sqlite3.Error as e:
        print(f"An error occurred: {e}")
        return {"status": "error", "message": str(e)}
    
    finally:
        conn.close()
        

def createProduct(product_name , expiry_date , price , stock , category) :
    
    conn = sqlite3.connect("my_medicalshop.db")
    cursor = conn.cursor()

    product_id = str(uuid.uuid4())

    try:
        cursor.execute("""
        INSERT INTO Product_table (product_id,product_name,expiry_date,price,stock,category)
        VALUES (?,?,?,?,?,?)
        """, (product_id,product_name,expiry_date,price,stock,category))
    
        conn.commit()
    
        return {"status": "success", "product_id": product_id}
    
    except sqlite3.Error as e:
        print(f"An error occurred: {e}")
        return {"status": "error", "message": str(e)}
    
    finally:
        conn.close()

def createOrder(user_id,product_id,quantity,order_date):
    
    conn = sqlite3.connect("my_medicalshop.db")
    cursor = conn.cursor()
    
    order_id = str(uuid.uuid4())

    cursor.execute('SELECT * FROM Product_table WHERE product_id = ?', (product_id,))
    d = cursor.fetchone()

    if not d:
        return {"status": "error", "message":"Product not found"}

    category = d[6]
    
    product_expiry_date = d[3]

    tp = d[4]
        
    t = int(quantity)

    total_price = tp*t

    try:
        cursor.execute("""
        INSERT INTO Order_table (order_id,user_id,product_id,quantity,category,product_expiry_date,order_date,total_price,isApproved)
        VALUES (?,?,?,?,?,?,?,?,?)
        """, (order_id,user_id,product_id,quantity,category,product_expiry_date,order_date,total_price,0))
    
        conn.commit()
    
        return {"status": "success", "order_id": order_id}
    
    except sqlite3.Error as e:
        print(f"An error occurred: {e}")
        return {"status": "error", "message": str(e)}
    
    finally:
        conn.close()

