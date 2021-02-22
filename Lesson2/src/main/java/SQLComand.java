public enum SQLComand {
    SQLDelete("delete from 'users' where NickID = ?"),
    SQLInsert("insert into 'users'('NickID','Password') values (?,?)"),
    SQLUpdatePassword("update 'users' set Password = ? where NickID = ? "),
    SQLSelectAll(" select * from 'users'"),
    SQLSelectNickId(" select * from 'users' where NickID = ?  ");
    private String comand;

    SQLComand(String comand) {
        this.comand = comand;
    }

    public String getComand() {
        return comand;
    }
}
