import copy
import csv
import datetime

'''--- OBJECT ORIENTED APPORACH---'''


# Train Station Class
class Station:

    def __init__(self, passengers: list, time_to_next: int) -> None:
        # A list of integer represents the number of people remaining from each time interval
        self.passengers = passengers

        # Minutes to reach next station after arrival (3 min dwell + travel time)
        self.time_to_next = time_to_next

    @classmethod
    def initialise_stations(cls):
        # Initial work to read input data
        a = []
        b = []
        c = []

        with open("./input.csv", "r") as csv_file:
            csv_reader = csv.reader(csv_file, delimiter=",")

            for i, row in enumerate(csv_reader):
                if not i:
                    continue

                [a, b, c][["A", "B", "C"].index(row[0])].append(int(row[1]))

        return [cls(l, t) for l, t in [(a, 11), (b, 12), (c, 14)]]


class Train:
    stations = Station.initialise_stations()  # A list of stations
    optimize_copy = copy.deepcopy(stations)  # A cloned list that can be manipulated without affecting the original
    total_waiting_time = 0  # The total time spent waiting for all passengers
    total_passengers_collected = 0  # The total amount of people that took the train

    def __init__(self, capacity: int, current_time: int) -> None:
        self.capacity = capacity  # The capacity of the train
        self.remaining = capacity
        # Seats remaining on the train
        self.current_time = current_time  # The current time, represented as minutes after 7 AM
        self.details = ["L4" if capacity == 200 else "L8"]  # CSV details for the train

    # Runs a train based on its scheduled start time and capacity
    def run_train(self) -> None:

        # Iterating through the stations
        for station in Train.stations:

            # Appending station's arrival time
            self.details.append((datetime.datetime(2000, 1, 1, 7, 0)
                                 + datetime.timedelta(minutes=self.current_time)).strftime("%-I:%M"))

            # Appending available capacity
            self.details.append(self.remaining)

            # Number of passengers taken at this station
            passengers_taken = 0

            # Iterating in terms of first come first serve
            for i in range(len(station.passengers)):

                # Passengers arrive in 10 minutes intervals after 7 AM
                # Break if the arrival time is in the future or if there's no space
                if i * 10 > self.current_time or self.remaining == 0:
                    break

                # If there's no people, check the next time interval
                if not station.passengers[i]:
                    continue

                # If not enough capacity, take as many as you can and break
                if self.remaining - station.passengers[i] < 0:

                    # Taking as many as we can
                    station.passengers[i] -= self.remaining

                    # Adjusting waiting time and passengers collected
                    Train.total_waiting_time += self.remaining * (self.current_time - (i * 10))
                    passengers_taken += self.remaining

                    # No more space left
                    self.remaining = 0
                    break
                else:

                    # Taking all people since we have space
                    self.remaining -= station.passengers[i]

                    # Adjusting waiting time and passengers collected
                    Train.total_waiting_time += station.passengers[i] * (self.current_time - (i * 10))
                    passengers_taken += station.passengers[i]

                    # No more people left for this arrival time
                    station.passengers[i] = 0

            self.details.append(passengers_taken)  # Appending passengers taken at this station
            Train.total_passengers_collected += passengers_taken  # Adding passengers taken at this station
            self.current_time += station.time_to_next  # Updating the time (for the next station)

        # Appending Union Station's arrival time, available capacity, and offloaded amount
        self.details.append((datetime.datetime(2000, 1, 1, 7, 0)
                             + datetime.timedelta(minutes=self.current_time)).strftime("%-I:%M"))
        self.details.append(self.remaining)
        self.details.append(self.capacity - self.remaining)

    # Runs the trains according to schedule, returning the average wait time
    @staticmethod
    def run_schedule(trains: list) -> float:
        for train in trains:
            train.run_train()

        return Train.total_waiting_time / Train.total_passengers_collected

    # 'encounter' function: if a train were to leave at a certain time, calculates how many people it will
    # encounter. An encounter is defined as those who arrived in the two most recent time windows.
    def encounter(self):

        # mirror the run train function but only on self - calculates the number of encounters
        passengers_encountered: int = 0
        # iterate through stations in the cloned list
        for station in Train.optimize_copy:
            # edge case before 7:10 am
            if self.current_time // 10 == 0:
                passengers_encountered += station.passengers[0]
            # edge case when time is after 10 am
            elif self.current_time > 180:
                self.current_time = 180
            # otherwise, normal case
            else:
                # count the passengers in the two most recent arrival times
                for i in range((self.current_time // 10) - 1, (self.current_time // 10) + 1):
                    passengers_encountered += station.passengers[i]
            # advance time to the next stations
            self.current_time += station.time_to_next

        return passengers_encountered

    # optimize function: runs the train at the best time, and removes all the passengers it can pick up from the
    # list of passengers in the station list
    def run_train_optimize(self) -> None:

        # starts off having taken no passengers
        passengers_taken: int = 0
        # iterate through all stations in the cloned list
        for station in Train.optimize_copy:

            # if there is no space left in the train, break, it cannot carry any more people.
            if self.remaining == 0:
                break
            # check for the time being after 10am - edge case
            if self.current_time > 180:
                self.current_time = 180

            # check for when the time is below 7:10 am
            if self.current_time // 10 == 0:
                passengers_taken += station.passengers[0]
                station.passengers[0] = 0
                self.remaining -= passengers_taken
            # otherwise normal case: check two most recent arrival times and remove as many people from them as
            # the train can carry without filling
            else:
                for i in range((self.current_time // 10) - 1, (self.current_time // 10) + 1):
                    # if there is no space for everyone

                    if self.remaining - station.passengers[i] < 0:
                        # Taking as many as we can
                        station.passengers[i] -= self.remaining
                        passengers_taken += self.remaining
                        self.remaining = 0
                    # otherwise there is space
                    else:
                        passengers_taken += station.passengers[i]
                        self.remaining -= station.passengers[i]
                        station.passengers[i] = 0

            self.current_time += station.time_to_next
