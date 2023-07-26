package dev.omega.arcane.lexer;

import java.util.Collections;
import java.util.List;

public record LexedMolang(List<MolangTokenInstance> tokens) {

    public LexedMolang(List<MolangTokenInstance> tokens) {
        this.tokens = Collections.unmodifiableList(tokens);
    }

    /**
     * Returns an unmodifiable {@link List} of the {@link MolangTokenInstance} backing this object.
     */
    @Override
    public List<MolangTokenInstance> tokens() {
        return tokens;
    }

    public boolean matches(MolangTokenType... types) {
        if(types.length != tokens.size()) {
            return false;
        }

        for (int i = 0; i < types.length; i++) {
            if(tokens.get(i).type() != types[i]) {
                return false;
            }
        }

        return true;
    }
}
