package com.github.maracas.roseau.changes;
import com.github.maracas.roseau.model.FieldDeclaration;


public class FieldBreakingChange extends BreakingChangeElement {
    public FieldDeclaration field;

    public FieldBreakingChange(FieldDeclaration field) {
        this.field = field;
    }

    public FieldDeclaration getElement() {
        return field;
    }

    public void setFieldBreakingChange(FieldDeclaration field) {
        this.field = field;
    }
}
