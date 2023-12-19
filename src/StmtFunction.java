

import java.util.List;

public class StmtFunction extends Statement {
    final Token name;
    final List<Token> params;
    final Statement body;

    StmtFunction(Token name, List<Token> params, Statement body) {
        this.name = name;
        this.params = params;
        this.body = body;
    }
}
