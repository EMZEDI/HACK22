import csv, datetime

class Station:

    def __init__(self, passengers: list, time_to_next: int) -> None:
        # A list of integers. The index represents the time at which
        # that number of people arrive at the station andt he integer
        # itself represents the number of people remaining 
        self.passengers = passengers
        self.time_to_next = time_to_next

class Train:

    a = []
    b = []
    c = []

    with open("./input.csv", "r") as csv_file:
        csv_reader = csv.reader(csv_file, delimiter=",")

        for i, row in enumerate(csv_reader):
            if not i:
                continue

            [a, b, c][["A", "B", "C"].index(row[0])].append(int(row[1]))  


    stations = [Station(l, t) for l, t in [(a, 11), (b, 12), (c, 14)]]
    total_waiting_time = 0
    total_passengers_collected = 0

    # capacity varies on L4 or L8 and current_time is initialised to the
    # departure time
    def __init__(self, capacity: int, current_time: int) -> None:
        self.capacity = capacity
        self.remaining = capacity
        self.current_time = current_time
        self.details = ["L4" if capacity == 200 else "L8"]

    def run_train(self):
        for station in Train.stations:
            self.details.append((datetime.datetime(2000, 1, 1, 7, 0) 
            + datetime.timedelta(minutes=self.current_time)).strftime("%-I:%M"))

            self.details.append(self.remaining)

            passengers_taken = 0

            for i in range(len(station.passengers)):
                if i * 10 > self.current_time:
                    break

                if self.remaining - station.passengers[i] < 0:
                    station.passengers[i] -= self.remaining
                    passengers_taken += self.remaining
                    Train.total_waiting_time += self.remaining * (self.current_time - (i * 10))
                    Train.total_passengers_collected += self.remaining
                    self.remaining = 0
                    break
                else:
                    self.remaining -= station.passengers[i]
                    passengers_taken += station.passengers[i]
                    Train.total_waiting_time += station.passengers[i] * (self.current_time - (i * 10))
                    Train.total_passengers_collected += station.passengers[i]
                    station.passengers[i] = 0
            
            self.details.append(passengers_taken)
            self.current_time += station.time_to_next

        self.details.append((datetime.datetime(2000, 1, 1, 7, 0) 
            + datetime.timedelta(minutes=self.current_time)).strftime("%-I:%M"))
        self.details.append(self.remaining)
        self.details.append(self.capacity - self.remaining)
    
    @staticmethod
    def run_schedule(trains: list) -> float:
        for train in trains:
            train.run_train()

        return Train.total_waiting_time / Train.total_passengers_collected



