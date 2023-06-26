package com.github.maracas.roseau.changes;

import com.github.maracas.roseau.model.TypeDeclaration;

public class TypeBreakingChange extends BreakingChangeElement {
    public TypeDeclaration type;

    public TypeBreakingChange(TypeDeclaration type) {
        this.type = type;
    }

    public TypeDeclaration getElement() {return type;}

    public void setTypeBreakingChange(TypeDeclaration type) {
        this.type = type;
    }
}
