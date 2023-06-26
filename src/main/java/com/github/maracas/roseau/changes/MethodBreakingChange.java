package com.github.maracas.roseau.changes;
import com.github.maracas.roseau.model.MethodDeclaration;

public class MethodBreakingChange extends BreakingChangeElement {
    public MethodDeclaration method;

    public MethodBreakingChange(MethodDeclaration method) {
        this.method = method;
    }


    public MethodDeclaration getElement() {
        return method;
    }

    public void setMethodBreakingChange(MethodDeclaration method) {
        this.method = method;
    }
}
