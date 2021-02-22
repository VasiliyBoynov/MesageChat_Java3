package Server;

public enum SQLComand {
    SQLDelete("delete from 'users' where NickID = ?"),
    SQLInsert("insert into 'users'('NickID','Password','Connection') values (?,?,1<0)"),
    SQLUpdatePassword("update 'users' set Password = ? where NickID = ? "),
    SQLUpdateNick("update 'users' set NickID = ? where NickID = ? "),
    SQLUpdateConnection("update 'users' set Connection = ? where NickID = ? "),
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
