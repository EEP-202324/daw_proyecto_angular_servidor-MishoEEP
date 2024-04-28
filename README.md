The application is about Education fair, showin the best schools of Madrid.

1. Made schools component. Showed the list using mock-schools.ts.
2. List can be interactivly changed using Angular's class binding, to change the names of the selected school (method onSelect also implemented).
3. Addind the separate component for the detail of the schools (school-detail).
4. Making a (injectable) school service to fetch/save data.
5. Adding a an Observable so it can fetch data asynchronously (using HttpClient.get())
6. Adding a MessageComponent to display app messages at the bottom of the screen. Injecting messageService into schoolService.