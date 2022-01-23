from structure import Train, csv

# function to optimize
def optimize():
    # loop to check the best time for a train to leave to pick up as many ppl as possible
    availableTimes = [minute for minute in range(0, 178)]  # this is in minutes
    unavailableTimes = []
    trainTypes = [200, 400, 400, 400, 400, 400, 400, 400, 400, 400, 400, 400, 400, 200, 200]
    trainSchedule = []

    # for all 16 trains

    for type in trainTypes:
        maxEncountered = 0
        bestTime = 0
        for time in availableTimes:
            train = Train(type, time)
            if time in unavailableTimes:
                continue
            else:
                ppl_encountered = train.encounter()

                if ppl_encountered > maxEncountered:
                    maxEncountered = ppl_encountered
                    bestTime = time

        # append this best time to a list of unavailable times
        unavailableTimes.append(bestTime)
        unavailableTimes.append(bestTime - 1)
        unavailableTimes.append(bestTime - 2)
        unavailableTimes.append(bestTime + 1)
        unavailableTimes.append(bestTime + 2)


        finalTrain = Train(type, bestTime)
        finalTrain.run_train_omptimize()
        trainSchedule.append((type, bestTime))

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

    schedule = [(400, 0), (400, 17), (400, 20), (400, 29), (200, 40), (400, 43), (400, 49), (400, 60), (400, 67), (400, 70), (200, 87), (400, 90), (400, 110), (400, 130), (200, 150), (200, 180)]
    trains = [Train(c, t) for (c, t) in schedule]
    av = Train.run_schedule(trains)

    # Writing the CSV of the schedule
    write_csv(trains)

    # For checking
    print(av)
    print(Train.total_passengers_collected)
    return schedule

makeSchedule()
