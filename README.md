
![logo-github](https://github.com/abistarun/resseract-lite/assets/7782937/4f29391e-0a77-4ac4-8290-29ed1e4953b8)

## Resseract Lite: Effortlessly Explore Your Data

Bring your data to life with Resseract Lite, a powerful yet simple data visualization tool that makes it easy for anyone to uncover hidden insights. Resseract empowers you to craft stunning visualizations in minutes, ranging from bar charts and pie charts to geo maps and more, all without writing a single line of code. 
Dive into your data interactively, slicing and dicing it to uncover hidden patterns and trends that might have otherwise gone unnoticed. We support diverse data sources, seamlessly integrating with CSV, Excel, and other common file formats to effortlessly align with your existing workflows. You can build custom dashboards, enabling you to track progress, present findings to stakeholders, and foster collaboration within your team. With a intuitive interface, Resseract welcomes users of all levels, from data novices to seasoned analysts.

## Key Features

* **Create stunning visualizations in minutes:** Build bar charts, pie charts, line charts, geo maps, and more with a few clicks—no coding required.
* **Explore your data interactively:** Slice and dice your data to filter, zoom, and discover patterns you might have missed otherwise.
* **Intelligence built in:** Resseract has the ability to understand your data to identify types and present insights on what might be important
* **Support for diverse data sources:** Load data from CSV, Excel, and other common file types to seamlessly integrate with your existing workflows. For list of supported file types [please refer this](https://github.com/abistarun/resseract-lite/wiki/Data-Upload#supported-data-sources)
* **Save and share your findings:** Create custom dashboards to track key metrics, present results to stakeholders, and collaborate with your team.
* **Intuitive interface:** Designed for users of all levels, from data novices to experienced analysts.

<img width="1680" alt="Dashboard" src="https://github.com/abistarun/resseract-lite/assets/7782937/426e0fec-689b-4771-bdce-4c16698e0c99">

**Let's resseract your data together!**

***

## Getting Started : Installing Resseract Lite
1. Get the latest release of Resseract Lite from [our releases page](https://github.com/abistarun/resseract-lite/releases)
2. Unzip the folder
3. Go to the unzipped folder and Run the executable as per your system
   * Windows : Double click on `run-resseract.bat` or open cmd and run `run-resseract.bat`
   * Linux/Mac : Double click on `run-resseract.sh` or open terminal and run `sh run-resseract.sh`
4. Once the application starts you can access resseract lite at [http://localhost:8242/canvas](http://localhost:8242/canvas)

**If you face any issue, [file a ticket here](https://github.com/abistarun/resseract-lite/issues/new) and we will get back to you at the earliest**

***

## Getting Started : Create your first dashboard

* Step 1: Upload Data
  * Download a sample CSV file from [here](https://raw.githubusercontent.com/IBM/employee-attrition-aif360/master/data/emp_attrition.csv)
  * Launch Resseract Lite using above steps
  * In the menu on the left, select `Upload Data` under Data
  * Under source select `CSV` and click Next
  * Upload the downloaded file and click Next
  * Change the Data Key to `Employee Attrition` and click Next
  * Data should be uploaded successfully 
* Step 2: Add a bar chart
  * In the menu on the left, select `Bar Chart` under Charts
  * Under `Data Key`, select `Employee Attrition`
  * Under Group By Axes, select `Department`
  * Under Value Column, select `EmployeeCount`
  * Click on Add, see that the bar chart is added
* Step 3: Save the dashboard
  * In the menu on the left, select `Save Dashboard` under Manage
  * Type the dashboard name as `Employee Attrition Dashboard` and click `Save`
  * Your dashboard is now saved

https://github.com/abistarun/resseract-lite/assets/7782937/c384e7c1-dafb-4254-b669-7b1cc8bbe798

***

## Table of Content

* [Getting Started](https://github.com/abistarun/resseract-lite/wiki/Getting-Started)
  * [Installing Resseract Lite](https://github.com/abistarun/resseract-lite/wiki/Getting-Started#installing-resseract-lite)
  * [Create your first dashboard](https://github.com/abistarun/resseract-lite/wiki/Getting-Started#create-your-first-dashboard)
* [Gallery](https://github.com/abistarun/resseract-lite/wiki/Gallery)
* [Data Upload](https://github.com/abistarun/resseract-lite/wiki/Data-Upload)
  * [Supported Data Sources](https://github.com/abistarun/resseract-lite/wiki/Data-Upload#supported-data-sources)
    * [CSV (Comma Seperated Values) Files](https://github.com/abistarun/resseract-lite/wiki/Data-Upload#csv-comma-seperated-values-files)
  * [Adding Custom Columns](https://github.com/abistarun/resseract-lite/wiki/Data-Upload#adding-custom-columns)
  * [Delete Data](https://github.com/abistarun/resseract-lite/wiki/Data-Upload#delete-data)
* [Widgets](https://github.com/abistarun/resseract-lite/wiki/Widgets)
  * [Bar Chart](https://github.com/abistarun/resseract-lite/wiki/Bar-Chart)
  * [Line Chart](https://github.com/abistarun/resseract-lite/wiki/Line-Chart)
  * [Pie Chart](https://github.com/abistarun/resseract-lite/wiki/Pie-Chart)
  * [Geo Map Chart](https://github.com/abistarun/resseract-lite/wiki/Geo-Map-Chart)
  * [Data Table](https://github.com/abistarun/resseract-lite/wiki/Data-Table)
  * [Dial Chart](https://github.com/abistarun/resseract-lite/wiki/Dial-Chart)
  * [Data Value Widget](https://github.com/abistarun/resseract-lite/wiki/Data-Value-Widget)
  * [Slice Widget](https://github.com/abistarun/resseract-lite/wiki/Slice-Widget)
* [Dashboard](https://github.com/abistarun/resseract-lite/wiki/Dashboard)
* [Expression Language](https://github.com/abistarun/resseract-lite/wiki/Expression-Language)
* [FAQs](https://github.com/abistarun/resseract-lite/wiki/FAQs)
* [Report a Issue / Request for new feature](https://github.com/abistarun/resseract-lite/issues/new)

***

<div align="center">

[!["LinkedIn"](https://img.shields.io/badge/LinkedIn-0077B5.svg?style=for-the-badge&logo=YouTube&logoColor=white)](https://www.linkedin.com/company/resseract/)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
[!["Youtube"](https://img.shields.io/badge/YouTube-FF0000.svg?style=for-the-badge&logo=YouTube&logoColor=white)](https://www.youtube.com/channel/UCcW0rKD6jrDUbxi43fvDTdQ)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
[!["Instagram"](https://img.shields.io/badge/Instagram-E4405F?style=for-the-badge&logo=instagram&logoColor=white)](https://www.instagram.com/resseract)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
[!["Buy Me A Coffee"](https://img.shields.io/badge/Buy%20Me%20A%20Coffee-FFDD00.svg?style=for-the-badge&logo=Buy-Me-A-Coffee&logoColor=black)](https://www.buymeacoffee.com/abistarun)
</rtl>