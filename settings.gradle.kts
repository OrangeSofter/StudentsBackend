rootProject.name = "ru.orange.studback.studentsbackend"
include("dataservices:students")
findProject(":dataservices:students")?.name = "students"
