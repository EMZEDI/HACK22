# HACK22 REPO 👀
![0*2_18iBop2Y2oGkb3](https://user-images.githubusercontent.com/77243080/150665531-50a30b7e-d9c5-42d8-9d96-45e086c16ccb.png)
# Welcome to our Repository for the McHacks 9 RailVision Challenge!<br>
In this repository you can find:<br>
- ☺️ The source code/directories for our RailVision train challenge solution which uses a type of <b>greedy algorithm coded in Python</b> to efficiently compute the best schedule which can be made for a train transportation system given the rush hours to reduce the average passenger waiting time.
- ☺️ The source code/directories for the visualization animation of the same problem given the input and output which uses <b>Java</b>.
- ☺️ The source code/directories for our website made up using <b>Django, HTML and CSS + using a .tech domain and google cloud! </b>
- To see the project overview and result go to our website: Before then see the output animations in static/videos folder!
- For detailed "How to run" you can find the information below: http://mchackstransit.herokuapp.com/
---
# To run the files:
1- To run the <b>python</b> file associated with the algorithm and see the csv file as an output, Make sure you have python3 installed
and hop in the pysrc directory.<b> If you are on Windows, you have to change the '-'s to '#s' in the structure.py file lines 58 and 109.</b> run <b>solution.py</b>using <code>python3 HACK22/solution.py</code> in the Pywork directory using terminal(You can see the average wait time as the output). 
Afterwards, you have to refer to the <b>output.csv</b> file (placed in the same directory) to see the most optimal schedule.

2- To check the <b>visualization</b> part of the project make sure you have the <b>csv</b> file outputted from the python app. In Eclipse IDE you have to import the Jwork directory into your workspace, and make sure to uncheck the copy project to workspace. You have to run the 
<b>MainFrame</b> file which is inside the <b>/Jwork/src/application</b> folder (You have to have java virtual machine associated with the Jwork directory) and run the application to see the animation!

3- To see the whole overview of the website built you can go to the main direcotry terminal and download django using <code> python3 -m pip install Django </code><br>.
Then run: <code> python3 manage.py makemigrations </code> <br> <code> python3 manage.py migrate </code><br> <code> python3 manage.py runserver </code><br> you will be given a link to the website but its better to use the global link to avoid all this process. 
