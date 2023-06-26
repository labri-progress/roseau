package com.github.maracas.roseau.changes;

import com.github.maracas.roseau.model.ConstructorDeclaration;

public class ConstructorBreakingChange extends BreakingChangeElement {
    public ConstructorDeclaration constructor;

    public ConstructorBreakingChange(ConstructorDeclaration constructor) {
        this.constructor = constructor;
    }

    public ConstructorDeclaration getElement() {
        return constructor;
    }

    public void setConstructorBreakingChange(ConstructorDeclaration constructor) {
        this.constructor = constructor;
    }
}

