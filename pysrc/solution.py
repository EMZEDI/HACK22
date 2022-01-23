from structure import Train, csv

'''FUNDAMENTAL ASSUMPTION BEHIND ALGORITHM: To minimize the average wait time, trains should be sent at the time when
they will enounter the most number of people. Encounter is defined as people who arrived at the station in the two most
recent arrival time intervals'''

# Function to optimize the train schedule
def optimize():

    # List of all possible train departure times in minutes
    availableTimes = [minute for minute in range(181)]

    # List of times unavailable for train departure: will be appended as each train departure time is set
    unavailableTimes = []

    # List of train types that the algorithm will iterate through IF WE HAVE TIME: calcualate the average waiting
    # time for all permutations of this list. For now, list order chosen by trial and error Only 15 trains in this
    # list, as the last train must leave at 10:00 to make sure everyone is picked up
    trainTypes = [200, 400, 400, 400, 400, 400, 200, 400, 400, 400, 400, 400, 400, 400, 200]

    # List of tuples, of parameters used to construct a train object. Tuple is (capacity, current_time)
    trainSchedule = []

    # For all 15 trains
    for t in trainTypes:

        # Loop through all available times, skip those that are not available
        maxEncountered = 0
        bestTime = 0

        for time in availableTimes:
            train = Train(t, time)
            if time in unavailableTimes:
                continue
            # If time is available, check how many people a train would encounter if it left at that time
            else:
                encountered = train.encounter()

                if encountered > maxEncountered:
                    maxEncountered = encountered
                    bestTime = time

        # Append this best time to a list of unavailable times, as well as +/- 2 of that time to avoid conflicts in
        # the station
        unavailableTimes.append(bestTime)
        unavailableTimes.append(bestTime - 1)
        unavailableTimes.append(bestTime - 2)
        unavailableTimes.append(bestTime + 1)
        unavailableTimes.append(bestTime + 2)

        finalTrain = Train(t, bestTime)
        finalTrain.run_train_optimize()
        trainSchedule.append((t, bestTime))

    # Train that leaves at 10:00 (time value of 180) is FIXED
    trainSchedule.append((200, 180))
    trainSchedule.sort(key=lambda x: x[1])
    
    # Returning the list of constructor parameters for all 16 trains
    return trainSchedule


# Function to write the CSV information for the given schedule
def write_csv(trains: list) -> None:
    with open("output.csv", "w", newline="") as output:
        writer = csv.writer(output, delimiter=",")

        writer.writerow([
            "TrainNum",
            "TrainType",
            "A_ArrivalTime",
            "A_AvailCap",
            "A_Boarding",
            "B_ArrivalTime",
            "B_AvailCap",
            "B_Boarding",
            "C_ArrivalTime",
            "C_AvailCap",
            "C_Boarding",
            "U_Arrival",
            "U_AvailCap",
            "U_Offloading"
        ])

        for i, train in enumerate(trains):
            writer.writerow([i + 1] + train.details)


# Function that outputs the csv file
def makeSchedule():
    schedule = optimize()
    trainsList = [Train(c, t) for (c, t) in schedule]
    av = Train.run_schedule(trainsList)

    # Writing the CSV of the schedule
    write_csv(trainsList)

    # For checking
    # print(av)
    # print(Train.total_passengers_collected)
    return av


# PRELIMINATRY CODE TO RUN EVERY PERMUTATION OF THE TRAIN LIST, AND CALCULATE THE AVERAGE WAIT TIME FOR THAT

"""
# The following is a brute force way to find the best permutation of trains, given that they depart
# 11 mins apart (except the last, departing at 10 AM)

import itertools

L4_indexes = itertools.combinations(range(16), r=4)
perms = [[200 if i in comb else 400 for i in range(16)] for comb in L4_indexes]


# Assume the first permutation is the best
best = [Train(c, 180 if i == 15 else i * 11) for i, c in enumerate(perms[0])]
min_wait = Train.run_schedule(best)


for i, perm in enumerate(perms):

    Train.total_passengers_collected = 0
    Train.total_waiting_time = 0
    Train.stations = Station.initialise_stations()

    trains = [Train(c, 180 if i == 15 else i * 11) for i, c in enumerate(perm)]

    av = Train.run_schedule(trains)

    if av < min_wait:
        min_wait = av
        best = trains
"""

x = makeSchedule()
print("Please refer to the output.csv file for the complete train schedule. The average waiting time is", str(x) +
      " Minutes for each person! Unbelievable!")

