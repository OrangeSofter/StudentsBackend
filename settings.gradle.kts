rootProject.name = "ru.orange.studback.studentsbackend"

include("common")
findProject("common")?.name = "common"

include("dataservices:students")
findProject(":dataservices:students")?.name = "students"

include("dataservices:student_groups")
findProject(":dataservices:student_groups")?.name = "student_groups"

include("dataservices:connections")
findProject(":dataservices:connections")?.name = "connections"

include("dataservices:courses")
findProject(":dataservices:courses")?.name = "courses"

include("dataservices:attending")
findProject(":dataservices:attending")?.name = "attending"

include("domain_services:reports")
findProject("domain_services:reports")?.name = "reports"
