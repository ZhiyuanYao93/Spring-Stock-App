#Simple Stock Price App

A simple web application that allows each user to track his/her own selected stocks.  

This application is extremely easy to use. 

If you are bored and have time to spend, please check out the code and give me critics and siggestions. 

Thank you！ 

**What it does?** 

Each user has his/her own account, which needs to be created with a username and password.
After logging in, user can add a stock by typing in its symbol. The symbol will be validated. If no such symbol exists, user needs to re-enter. If symbol passes validation, user will see the new stock displayed in user's homepage. 

The stock price is freshly fetched from data source.

I do not gurantee the degree of delay, because I solely rely on the returned data. 

Please note that the stock name will be checked and updated each time new data is fetched back. 

I assume that a company's stock symbol would not change for a long time but the company's own business name could change. 



**Where is the data from?** 

The source of data is from the follwoing URL (a sample):
http://finance.yahoo.com/d/quotes.csv?s=AAPL+GOOG+MSFT&f=nabs

The above link gives a CSV file in which stock name, ask price, bid price and symbol are included. 

**Improvements needed for this project (updated 10.25.2017):**
1. More security functions. For example, user authentication, and email validation. 

2. Decrease the requests made to CSV file source. 

3. Database connection pool, for small enterprise application. 
  
4. Currently, some of the stock symbols bring back N/A prices. This needs more investigation. Change of data source may needed. 

5. A more pleasant and responsive UI.

6. More comprehensive exception handling.

7. Unit tests!!!!!!

**_(Added 10.25.2017)_**

1. Basic function is done on Oct. 27th 2017.

2. Still needs admin operations and more user prompt (More communicative).




