import sqlite3

def createTables():
    
    conn = sqlite3.connect("my_medicalshop.db")
    cursor = conn.cursor()

    cursor.execute('''    
        CREATE TABLE IF NOT EXISTS Users(
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            user_id VARCHAR(255),
            password VARCHAR(255),
            level INT,
            date_of_account_creation DATE,
            isApproved BOOLEAN,
            block BOOLEAN,
            name VARCHAR(255),
            email VARCHAR(255),
            phone_number VARCHAR(255),
            pinCode VARCHAR(255),
            address VARCHAR(255)      
            );
        ''')
    
    cursor.execute('''    
        CREATE TABLE IF NOT EXISTS Product_table(
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            product_id VARCHAR(255), 
            product_name VARCHAR(255),
            expiry_date DATE,
            price DECIMAL(10,2),
            stock INT,
            category VARCHAR(255)      
            );
        ''')
    
    cursor.execute('''
        CREATE TABLE IF NOT EXISTS Order_table(
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            order_id VARCHAR(255) UNIQUE,
            user_id VARCHAR(255),
            product_id VARCHAR(255),
            quantity INT,
            category VARCHAR(255),
            product_expiry_date DATE,
            order_date DATE,
            total_price DECIMAL(10,2)
            );
        ''')
    
    cursor.execute('''
        CREATE TABLE IF NOT EXISTS Sell_History(
                   
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            sell_id VARCHAR(255),
            product_id VARCHAR(255),
            quantity INT,
            remaining_stock INT,
            date_of_sell DATE,
            total_amount FLOAT,
            price FLOAT,
            product_name VARCHAR(255),
            user_name VARCHAR(255),
            user_id VARCHAR(255)      
            );

    ''')

    # cursor.execute('''ALTER TABLE Order_table ALTER COLUMN isApproved BOOLEAN''')

    conn.commit() # commit is used when we use database
    conn.close()  # After finishing the task on database we use it


# def addColumn():
#     conn = sqlite3.connect("my_medicalshop.db")
#     cursor = conn.cursor()

#     # Add a new column (if it doesn't already exist)
#     try:
#         cursor.execute('''
#             ALTER TABLE Order_table ADD COLUMN isApproved BOOLEAN;
#         ''')
#         print("New column added successfully!")
#     except sqlite3.OperationalError as e:
#         if "duplicate" in str(e).lower():
#             print("Column already exists.")
#         else:
#             print(f"An error occurred: {e}")

#     conn.commit()
#     conn.close()

def addColumnDynamic(column_name, column_type):
    conn = sqlite3.connect("my_medicalshop.db")
    cursor = conn.cursor()

    try:
        # Dynamically build the query
        query = f"ALTER TABLE Order_table ADD COLUMN {column_name} {column_type};"
        print(f"Executing Query: {query}")  # Debugging output
        cursor.execute(query)
        print(f"Column '{column_name}' added successfully!")
    except sqlite3.OperationalError as e:
        print(f"SQLite OperationalError: {e}")  # Track error type
    except Exception as e:
        print(f"General error: {e}")  # For unexpected bugs
    finally:
        conn.commit()
        conn.close()



