from django.shortcuts import render
from pysrc.structure import Train, Station, csv
from railvision.settings import BASE_DIR
import os, math

# Create your views here.

def landing_view(request):

    trains = []

    with open(os.path.join(BASE_DIR, "static/optimized/output.csv"), "r") as csv_file:
            csv_reader = csv.reader(csv_file, delimiter=",")
            for i, row in enumerate(csv_reader):
                if not i:
                    continue

                trains.append((i, row[1], row[2], row[5], row[8], row[11]))
    
    with open(os.path.join(BASE_DIR, "static/optimized/min_wait.txt"), "r") as min_wait_file:
        min_wait = float(min_wait_file.read())
    

    context = {
        'trains': trains,
        'min_wait': f'{math.floor(min_wait)} minutes and {math.floor(60 * (min_wait - math.floor(min_wait)))} seconds'
        }

    return render(request, 'pages/landing.html', context)