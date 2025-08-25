import sqlite3

def user_auth(email, password):
    conn = sqlite3.connect('my_medicalshop.db')
    cursor = con.cursor()

    cursor.execute('SELECT * FROM Users WHERE email = ? AND password = ?', (email, password))
    user = cursor.fetchone() # fetch whole row details

    conn.close()

    return user


def order_auth(product_id):

    conn = sqlite3.connect('my_medicalshop.db')
    cursor = conn.cursor()

    cursor.execute('SELECT * FROM Product_table WHERE product_id =?', (product_id,))
    order = cursor.fetchone() # fetch whole row details

    conn.close()

    return order
    
   
