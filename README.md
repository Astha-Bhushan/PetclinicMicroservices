# PetclinicMicroservices

Petclinic is an application used by the clinic for Pets and Veterinary services. It tracks the 
clinical visits for Pets for various veterinary services. It has functionalities related to Pet 
Owners, Pets, Pet Types, Veterinaries and Specialities. You are required to create the 
following microservices:
• owner-service
• vet-service
• visit-service
owner-service has the following requirements:
1. An Owner has more than one Pets
2. Can create a new owner and add new pets to owner and remove pets from owner
3. Can search owner based on its id
4. Can list all owners with their pets
5. Delete Owner
6. Update Owner
vet-service has the following requirements:
1. Search a vet based on speciality
2. List all vets
3. List all vet for a speciality
4. Add a vet
5. Remove a vet
6. Update a vet
7. Add Speciality
8. Remove Speciality
9. List all specialities
visit-service has the following requirements:
1. This service maintains a record of clinical visits to various specialities for pets
2. Create a new visit for a pet
3. List all Visits for a pet using pet id;
4. List all visits for a given set of pet id(s)
