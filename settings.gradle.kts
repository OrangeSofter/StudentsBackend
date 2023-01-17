rootProject.name = "ru.orange.studback.studentsbackend"

include("common")
findProject("common")?.name = "common"

include("dataservices:students")
findProject(":dataservices:students")?.name = "students"
