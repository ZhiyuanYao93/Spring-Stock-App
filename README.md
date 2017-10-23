#Simple Stock Price App

A simple web application that can check user selected stock prices. 

**What it does?** 

Each user has his/her own account, which needs to be created with a username and password.
After logging in, user can add a stock by typing in its symbol. The symbol will be validated. If no such symbol exists, user needs to re-enter. If symbol passes validation, user will see the new stock displayed in user's homepage. 



**Where is the data from?** 

The source of data is from the follwoing URL (a sample):
http://finance.yahoo.com/d/quotes.csv?s=AAPL+GOOG+MSFT&f=nabs

The above link gives a CSV file in which stock name, ask price, bid price and symbol are included. 

**Improvements needed for this project (updated 10.23.2017):**
1. More security functions. For example, user authentication, and email validation. 

2. Decrease the requests made to CSV file source. 

3. Database connection pool, for small enterprise application. 
  
4. Currently, some of the stock symbols bring back N/A prices. This needs more investigation. Change of data source may needed. 

5. A more pleasant and responsive UI.



