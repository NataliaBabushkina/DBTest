import java.sql.DriverManager

fun main(){
    val c = DriverManager.getConnection("jdbc:mysql://localhost:3306/test2020?serverTimezone=UTC", "Natalia",
        "yfnf555if")
    val s=c.createStatement()
    val sql = "SELECT sid, surname, n, p, MIN(mark) as mmark FROM \n" +
            "\n" +
            "(SELECT Students.id as sid, surname, substring(first_name, 1, 1) as n, substring(patronymic, 1, 1) as p, 2*(YEAR(NOW())-admission)-IF(MONTH(NOW())=1, 2,\n" +
            " if(month(now())>=2 and month(now())<=6, 1, 0)) as sess FROM Students) as studsess\n" +
            "INNER JOIN\n" +
            "(SELECT student_id, discipline_id, IF(assessment<56, 2, IF(assessment<71, 3, IF(assessment<86, 4, 5))) as Mark, semester, Disciplines.reporting_form as fr from Progress\n" +
            "INNER JOIN Disciplines ON discipline_id=Disciplines.id ) AS marks\n" +
            "ON sid = marks.student_id\n" +
            "WHERE ( marks.semester=studsess.sess and !(marks.fr LIKE \"Зачет\" and marks.Mark>2))\n" +
            "GROUP BY sid"
    val rs=s.executeQuery(sql)
    while(rs.next()){
        print(rs.getString("sid")+" ")
        print(rs.getString("surname")+" ")
        print(rs.getString("n")+" "+rs.getString("p")+" ")
        print(Studentship(rs.getInt("mmark")))
        print("\n")
    }


}
fun Studentship(mark: Int): Int{
    if (mark==5) return 2600
    if (mark==4) return 1700
    return 0
}