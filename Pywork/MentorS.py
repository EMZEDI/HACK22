import pandas as pd
import numpy as np
import datetime as dt
import csv
from structure import Train

# loop to check the best time for a train to leave to pick up as many ppl as possible
capacity_l8 = 400
available_times = [minute for minute in range(0, 178)]  # this is in minutes
current_time = 0
max_encounter = 0
best_time = 0

for time in available_times:
    # create the obj for each loop
    train = Train(400, time)
    ppl_encountered = train.encounter()

    if ppl_encountered > max_encounter:
        max_encounter = ppl_encountered
        best_time = time

if __name__ == '__main__':
    print(best_time)
