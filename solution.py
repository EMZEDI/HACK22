from structure import Station, Train, csv

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


schedule = [
    (200, 0),
    (400, 10),
    (400, 15),
    (400, 20),
    (400, 30),
    (400, 40),
    (400, 50),
    (400, 60),
    (400, 70),
    (400, 80),
    (400, 90),
    (400, 105),
    (400, 130),
    (200, 150),
    (200, 160),
    (200, 180),
]

trains = [Train(c, t) for c, t in schedule]

av = Train.run_schedule(trains)

write_csv(trains)

print(av)
print(Train.total_passengers_collected)
