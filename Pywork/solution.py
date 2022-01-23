from structure import Train, csv
# function to optimize
def optimizer():
    # loop to check the best time for a train to leave to pick up as many ppl as possible
    availableTimes = [minute for minute in range(0, 178)]  # this is in minutes
    unavailableTimes = []
    trainTypes = [200, 400, 400, 400, 400, 400, 200, 400, 400, 400, 400, 400, 400, 400, 200]
    trainSchedule = []

    # for all 16 trains

    for type1 in trainTypes:
        maxEncountered = 0
        bestTime = 0
        for time in availableTimes:
            train = Train(type1, time)
            if time in unavailableTimes:
                continue
            else:
                ppl_encountered = train.encounter()

                if ppl_encountered > maxEncountered:
                    maxEncountered = ppl_encountered
                    bestTime = time

        # append this best time to a list of unavailable times, as well as +- 2
        unavailableTimes.append(bestTime)
        unavailableTimes.append(bestTime - 1)
        unavailableTimes.append(bestTime - 2)
        unavailableTimes.append(bestTime + 1)
        unavailableTimes.append(bestTime + 2)


        finalTrain = Train(type1, bestTime)
        finalTrain.run_train_optimize()
        trainSchedule.append((type1, bestTime))

        # run the train and remove the passengers it can take

    #train that leaves at 180 is FIXED
    trainSchedule.append((200, 180))
    trainSchedule.sort(key=lambda x: x[1])
    print(trainSchedule)


    return trainSchedule


# Function to write the CSV information for the given schedule
def write_csv(trains: list) -> None:
    with open("output.csv", "w") as output:
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


# function that outputs the csv file
def makeSchedule():

    schedule = optimizer()
    trainsList = [Train(c, t) for (c, t) in schedule]
    av = Train.run_schedule(trainsList)

    # Writing the CSV of the schedule
    write_csv(trainsList)

    # For checking
    print(av)
    print(Train.total_passengers_collected)
    return av
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
x=makeSchedule()
print(x)