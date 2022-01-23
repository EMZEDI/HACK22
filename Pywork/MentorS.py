import datetime as dt
import csv
from structure import Train

# loop to check the best time for a train to leave to pick up as many ppl as possible

availableTimes = [minute for minute in range(0, 178)]  # this is in minutes
unavailableTimes = []
trainTypes = [400,400,400,400,400,400,400,400,400,400,400,400,200,200,200]
trainSchedule = []

#for all 16 trains

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

    #append this best time to a list of unavailable times
    unavailableTimes.append(bestTime)
    unavailableTimes.append(bestTime+1)
    unavailableTimes.append(bestTime+2)

    finalTrain = Train(type,bestTime)
    finalTrain.run_train_omptimize()
    trainSchedule.append((type,bestTime))


    #run the train and remove the passengers it can take
trainSchedule.append((200,180))
trainSchedule.sort(key=lambda x:x[1])


if __name__ == '__main__':
    print(bestTime)
    print(trainSchedule)
