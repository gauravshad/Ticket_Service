# Walmart_Labs_Ticket_Service

## Build Instructions
1. I am using maven to build this project.
2. When inside this repository, use `mvn clean package` to build the project. This command will run the test cases and build the jar in the target folder.
3. Change current directory to target `cd target/`
4. Use this command to run the ticket service `java -jar ticketservice-1.0-SNAPSHOT.jar 44 32` . 44 and 32 are the command line arguments where first argument stands for capacity of the venue (sample command uses 44) and second stands for hold duration (sample command uses 32). You can use any integral values for those arguments. This command will start the service where you can easily naviagte through the command line.

## Assumptions
1. To find the best seats, I am assuming that the best seats are the ones with the lowest seat numbers.
2. Hold functionality will hold the seats but does not hold the seat numbers. Final seat numbers will only be allotted at the time of reservation.
3. I am mostly using Integers in implementation. So, values like capcaity can only hold a maximum value of `INTEGER.MAX_VALUE`.
4. There was nothing mentioned about how the reservation confirmation code will be generated. So, I came up with my own scheme. The confirmation code will have the following format `x-y-z` where x is the reservation sequence, y is the number of seats reserved and z is the name of the customer parsed through its Email id. For example, `2-6-martin` means 2 is the reservation sequence, 6 is the number of tickets reserved and martin is the customer.
