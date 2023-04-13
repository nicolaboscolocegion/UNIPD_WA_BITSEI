#### Spreadsheet here --> https://docs.google.com/spreadsheets/d/10E8tmawCBEwZ606LSldohYFDFf6RoxPeJKttjdBuI-U/edit?usp=sharing ####

### POINT 1. Project Overview ###
# Scope:
	Provide a high-level overview of the project, including its goals, 	objectives, and target audience. Describe the purpose of the software application and how it will solve the problem it's intended to address.
# Definition:
	In this subgroup we're interested in developing a section for our wep app in which the user can find and generate statistics and insights of interest. These data can be gathered in various formats and represented by plots. Eventually, these can also be exported and saved on the device, or forwarded to an email address and/or to a telegram account.
	In addition, we also want to implement a notification system to keep the user updated about the status of his invoicings. The user can choose if he wants to have these notifications also forwarded to his email address and/or to his telegram account.

### POINT 2. Requirements Analysis ###
# Scope:
	List and describe the requirements for the software application. This should include functional requirements, such as what the application should do, and non-functional requirements, such as performance, scalability, and security.
		A greedy idea about how a requirement can be described is the following:
			- Title
			- Description
			- Acceptance criteria
		e.g. 
			- Title: User Authentication and Authorization
			- Description: Users of the application must be able to authenticate and authorize themselves before accessing the application.Authentication should be based on username and password.
			- Acceptance criteria:
				> Users must be able to log in to the application using their registered username and password.
				> Users must be able to reset and change their password if they forget it.
# Definition: 
	# Functional Requirements 1 (#FR1)
	- Title: Invoice Filtering
	- Description: Users must be able filter the invoices by various criteria such as relevance, date, amount of money, customer, and product.
	- Acceptance Criteria:
		> Users must be able to select among some predefined tasks such as relevance or product/s
		> Users must be able to select among some half-customisable tasks such as from start-amount of money to end-amount of money 
		> Users must be able to save these plots into the local device 
		> Users must be able to see a summary of the filtered invoices, including the total number of invoices, the total amount invoiced, and the average invoice value.
		 

	# Functional Requirements 2 (#FR2)
	- Title: Generation of Invoice Metrics
	- Description: Users of the application must be able to see metrics related to invoices, such as the number of invoices sent, received, and paid, as well as the average time it takes for invoices to be paid.
	The Users can also fix a window of time to filter these data. 
	- Acceptance Criteria:
		> Users must be able to access a section "Invoice Metrics" in which they find a dashboard containing general metrics related to invoices
		> Users must be able to select a window of time (start-date / end-date) to filter these metrics
		> Users must be able to pick the metrics they want to see 
		> Users must be able to filter the data by some aspects of interest, like customer_id, invoice_product...
		> Users must be able to customise the main dashboard which appears on the section open

	# Functional Requirements 3 (#FR3)
	- Title: Plot of the Invoice Metrics and Export
	- Description: When the users select some metrics to be shown (#RF1), they must be able to generate some plots using these statistics. The plots to be generated can be picked by the Users.
	They must be able also to export these plots in various formats (pdf, png, xml...) and save them into the local device. 
	- Acceptance Criteria:
		> Users must be able to take some metrics and use these to generate some plots of interests.
		> Users must be able to export these plots into the format they choose
		> Users must be able to save these plots into the local device 
		> Users must be able to choose the name they want for the exported file
		
	# Functional Requirements 4 (#FR4)
	- Title: Forwarding of the Exported Plots via Email and/or Telegram
	- Description: After the Users have generated some metrics (#RF1), used these as basis for some plots which has then been exported (#RF2), they must be able also to forward these directly to their email address and/or to their telegram account (priority to email)
	- Acceptance Criteria:
		> Users must be able to decide if they want the exported plots to be sended to their email address and/or to their telegram account
		> Users must be able to decide the email address and the telegram account they want to use for these operations.
		> Users must be able to filter the data by some aspects of interest, like customer_id, invoice_product...
		> Users must be able to customise the main dashboard which appears on the section open
	
	# Functional Requirements 5 (#FR5)
	- Title: Customer Insights
	- Description: Users must be able to see insights on customer behavior, such as which customers are paying on time, which customers are consistently late, and which customers have the highest invoice values.
	- Acceptance Criteria:
		> Users must be able to see details associated to what above in a clear format
		> Users must be able to see plots associated to metrics described above 
		> Users must be able to see a dashboard with all what above 
		> Users must be able to see dashboard specific to every aspect of the ones above 

	# Functional Requirements 6 (#FR6)
	- Title: Invoice Aging and History
	- Description: Users must be able to see the aging of the invoices, broken down by filters such as the number of days outstanding and the amount of money outstanding, in order to be able to track the progress of payments.
	For the payed invoices, the Users must be able to see the payment history, including the date of payment and the amount paid.
	- Acceptance Criteria:
		> Users must be able to see details associated to paid invoices
		> Users must be able to see details associated to pending invoices.

	# Functional Requirements 7 (#FR7)
	- Title: Product insights
	- Description: Users must be able to see insights on product performance, such as which products are selling the most, which products are generating the most revenue, and which products have the highest profit margins.
	- Acceptance Criteria:
		> To be compiled based on what above

	# Functional Requirements 8 (#FR8)
	- Title: Sales Trends
	- Description: Users must be able to see sales trends over time, broken down by product, customer, and region, in order to be able to identify growth opportunities and potential risks.
	- Acceptance Criteria:
		> To be compiled based on what above

	# Functional Requirements 9 (#FR9)
	- Title: Comparative Analysis
	- Description: Users must be able to compare the performance of different products, customers, and regions to identify trends and patterns.
	- Acceptance Criteria:
		> To be compiled based on what above
	
	# Functional Requirements 10 (#FR10)
	- Title: Forecasting and Projections
	- Description: Users must be able to see forecasting and projection capabilities, such as predicting future revenue, expenses, and cash flow based on historical data.
	- Acceptance Criteria:
		> To be compiled based on what above

	# Functional Requirements 11 (#FR11)
	- Title: Customizable Reports
	- Description: Users must be able to create and save my own customized reports with the metrics and visualizations that are most important to their business.
	- Acceptance Criteria:
		> To be compiled based on what above

	# Functional Requirements 12 (#FR12)
	- Title: Data Visualization
	- Description: Users must be able to see the data presented in interactive charts, graphs, and other visualizations that allow to explore the data in different ways.
	- Acceptance Criteria:
		> To be compiled based on what above
	
	# Functional Requirements 13 (#FR13)
	- Title: Exportable Reports
	- Description: Users must be able to export the reports and visualizations in different formats, such as PDF or CSV.
	- Acceptance Criteria:
		> To be compiled based on what above
	
	# Functional Requirements 14 (#FR14)
	- Title: Customizable Visualizations
	- Description: In addition to being able to customize reports (#FR11), the Users must be able to customize the visualizations themselves, such as changing the color scheme, font size, and layout.

	- Acceptance Criteria:
		> To be compiled based on what above
	
	# Functional Requirements 15 (#FR15)
	- Title: User Permissions
	- Description: The Users must be able to set user permissions for the statistics and insights section, so that I can control who has access to certain data and reports.
	- Acceptance Criteria:
		> To be compiled based on what above

	# Functional Requirements 16 (#FR16)
	- Title: Integration with other Data Sources
	- Description: The Users must be able to able to integrate data from other sources, such as social media, marketing campaigns, and website analytics, to get a more complete picture of their business performances.
	- Acceptance Criteria:
		> To be compiled based on what above

	# Functional Requirements 17 (#FR17)
	- Title: Predictive Analytics
	- Description: In addition to forecasting, the Users must be able to use predictive analytics to identify potential risks and opportunities based on patterns in the data.
	- Acceptance Criteria:
		> To be compiled based on what above

	# Functional Requirements 18 (#FR18)
	- Title: Automated Alerts
	- Description: The Users must be able to set up automated alerts for certain metrics, such as when a customer's payment history changes, so that they can proactively address any issues.
	- Acceptance Criteria:
		> To be compiled based on what above

### POINT 3. System Architecture ###
# Scope
	Describe the overall architecture of the system, including the hardware and software components. This should include the following:
		- A high-level system diagram that shows the major components of the system and how they interact.
		- A description of the roles and responsibilities of each component.
		- A description of how the components will communicate with each other.
		- A list of the software and hardware technologies that will be used.
# Definition: 
	TODO	

### POINT 4. Data Model ###
# Scope
	Provide a description of the data model for the application, including the following:
		- A list of the data entities and their attributes.
		- A description of the relationships between the data entities.
		- A description of the data access methods.
# Definition:
	TODO

### POINT 5. Application Design ###
# Scope:
	Describe the design of the application, including the following:
		- A list of the major features and functions of the application.
		- A description of the user interface, including wireframes or mockups.
		- A description of the workflows and business processes that the application will support.
		- A description of the software design patterns and frameworks that will be used.
# Definition:
	TODO

### POINT 6. Security Design ###
# Scope:
	Provide a description of the security design for the application, including the following:
		- A description of the authentication and authorization mechanisms.
		- A description of the encryption and decryption methods.
		- A description of the access control methods.
# Definition:
	TODO

### POINT 7. Performance Design ###
# Scope:
	Provide a description of the performance design for the application, including the following:
		- A description of the expected load on the system.
		- A description of the expected response time for different user actions.
		- A description of the performance testing and tuning methods.
# Definition:
	TODO

### POINT 8. Deployment Design ###
# Scope:
	Describe how the application will be deployed, including the following:
		- A description of the deployment architecture.
		- A description of the deployment process.
		- A description of the testing and quality assurance process.
# Definition:
	TODO