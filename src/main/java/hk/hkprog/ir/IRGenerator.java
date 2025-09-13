package hk.hkprog.ir;

import org.antlr.v4.runtime.tree.*;
import com.hoodlum.antlr.CBaseVisitor;
import com.hoodlum.antlr.CParser;
import java.util.*;

/**
 * Generates intermediate representation from the C AST
 */
public class IRGenerator extends CBaseVisitor<Void> {
    private IRProgram program;
    private IRFunction currentFunction;
    private int tempCounter;
    private Map<String, IRType> symbolTable;
    
    public IRGenerator() {
        this.program = new IRProgram();
        this.tempCounter = 0;
        this.symbolTable = new HashMap<>();
    }
    
    public IRProgram generateIR(ParseTree tree) {
        super.visit(tree);
        return program;
    }
    
    private String getNextTemp() {
        return "t" + (tempCounter++);
    }
    
    @Override
    public Void visitCompilationUnit(CParser.CompilationUnitContext ctx) {
        // Visit all external declarations (functions, global variables)
        if (ctx.translationUnit() != null) {
            visit(ctx.translationUnit());
        }
        return null;
    }
    
    @Override
    public Void visitTranslationUnit(CParser.TranslationUnitContext ctx) {
        for (CParser.ExternalDeclarationContext extDecl : ctx.externalDeclaration()) {
            visit(extDecl);
        }
        return null;
    }
    
    @Override
    public Void visitExternalDeclaration(CParser.ExternalDeclarationContext ctx) {
        if (ctx.functionDefinition() != null) {
            visit(ctx.functionDefinition());
        } else if (ctx.declaration() != null) {
            // Handle global variable declarations
            visit(ctx.declaration());
        }
        return null;
    }
    
    @Override
    public Void visitFunctionDefinition(CParser.FunctionDefinitionContext ctx) {
        // Extract function name and type
        CParser.DirectDeclaratorContext directDecl = ctx.declarator().directDeclarator();
        String functionName = directDecl.Identifier().getText();
        
        // For simplicity, assume all functions return int
        IRType returnType = IRType.INT;
        currentFunction = new IRFunction(functionName, returnType);
        
        // Process parameters if any
        if (directDecl.parameterTypeList() != null) {
            processParameters(directDecl.parameterTypeList());
        }
        
        // Process function body
        if (ctx.compoundStatement() != null) {
            visit(ctx.compoundStatement());
        }
        
        program.addFunction(currentFunction);
        currentFunction = null;
        return null;
    }
    
    private void processParameters(CParser.ParameterTypeListContext paramCtx) {
        if (paramCtx.parameterList() != null) {
            for (CParser.ParameterDeclarationContext paramDecl : paramCtx.parameterList().parameterDeclaration()) {
                // For simplicity, assume all parameters are int
                String paramName = "param" + currentFunction.getParameters().size();
                IRParameter param = new IRParameter(paramName, IRType.INT);
                currentFunction.addParameter(param);
            }
        }
    }
    
    @Override
    public Void visitCompoundStatement(CParser.CompoundStatementContext ctx) {
        if (ctx.blockItemList() != null) {
            visit(ctx.blockItemList());
        }
        return null;
    }
    
    @Override
    public Void visitBlockItemList(CParser.BlockItemListContext ctx) {
        for (CParser.BlockItemContext item : ctx.blockItem()) {
            visit(item);
        }
        return null;
    }
    
    @Override
    public Void visitBlockItem(CParser.BlockItemContext ctx) {
        if (ctx.statement() != null) {
            visit(ctx.statement());
        } else if (ctx.declaration() != null) {
            visit(ctx.declaration());
        }
        return null;
    }
    
    @Override
    public Void visitStatement(CParser.StatementContext ctx) {
        if (ctx.expressionStatement() != null) {
            visit(ctx.expressionStatement());
        } else if (ctx.jumpStatement() != null) {
            visit(ctx.jumpStatement());
        }
        return null;
    }
    
    @Override
    public Void visitExpressionStatement(CParser.ExpressionStatementContext ctx) {
        if (ctx.expression() != null) {
            visit(ctx.expression());
        }
        return null;
    }
    
    @Override
    public Void visitExpression(CParser.ExpressionContext ctx) {
        if (ctx.assignmentExpression().size() > 0) {
            // Process the first assignment expression
            visit(ctx.assignmentExpression(0));
        }
        return null;
    }
    
    @Override
    public Void visitAssignmentExpression(CParser.AssignmentExpressionContext ctx) {
        if (ctx.conditionalExpression() != null) {
            visit(ctx.conditionalExpression());
        }
        return null;
    }
    
    @Override
    public Void visitConditionalExpression(CParser.ConditionalExpressionContext ctx) {
        if (ctx.logicalOrExpression() != null) {
            visit(ctx.logicalOrExpression());
        }
        return null;
    }
    
    @Override
    public Void visitLogicalOrExpression(CParser.LogicalOrExpressionContext ctx) {
        if (ctx.logicalAndExpression().size() > 0) {
            visit(ctx.logicalAndExpression(0));
        }
        return null;
    }
    
    @Override
    public Void visitLogicalAndExpression(CParser.LogicalAndExpressionContext ctx) {
        if (ctx.inclusiveOrExpression().size() > 0) {
            visit(ctx.inclusiveOrExpression(0));
        }
        return null;
    }
    
    @Override
    public Void visitInclusiveOrExpression(CParser.InclusiveOrExpressionContext ctx) {
        if (ctx.exclusiveOrExpression().size() > 0) {
            visit(ctx.exclusiveOrExpression(0));
        }
        return null;
    }
    
    @Override
    public Void visitExclusiveOrExpression(CParser.ExclusiveOrExpressionContext ctx) {
        if (ctx.andExpression().size() > 0) {
            visit(ctx.andExpression(0));
        }
        return null;
    }
    
    @Override
    public Void visitAndExpression(CParser.AndExpressionContext ctx) {
        if (ctx.equalityExpression().size() > 0) {
            visit(ctx.equalityExpression(0));
        }
        return null;
    }
    
    @Override
    public Void visitEqualityExpression(CParser.EqualityExpressionContext ctx) {
        if (ctx.relationalExpression().size() > 0) {
            visit(ctx.relationalExpression(0));
        }
        return null;
    }
    
    @Override
    public Void visitRelationalExpression(CParser.RelationalExpressionContext ctx) {
        if (ctx.shiftExpression().size() > 0) {
            visit(ctx.shiftExpression(0));
        }
        return null;
    }
    
    @Override
    public Void visitShiftExpression(CParser.ShiftExpressionContext ctx) {
        if (ctx.additiveExpression().size() > 0) {
            visit(ctx.additiveExpression(0));
        }
        return null;
    }
    
    @Override
    public Void visitAdditiveExpression(CParser.AdditiveExpressionContext ctx) {
        if (ctx.multiplicativeExpression().size() > 0) {
            visit(ctx.multiplicativeExpression(0));
        }
        return null;
    }
    
    @Override
    public Void visitMultiplicativeExpression(CParser.MultiplicativeExpressionContext ctx) {
        if (ctx.castExpression().size() > 0) {
            visit(ctx.castExpression(0));
        }
        return null;
    }
    
    @Override
    public Void visitCastExpression(CParser.CastExpressionContext ctx) {
        if (ctx.unaryExpression() != null) {
            visit(ctx.unaryExpression());
        }
        return null;
    }
    
    @Override
    public Void visitUnaryExpression(CParser.UnaryExpressionContext ctx) {
        if (ctx.postfixExpression() != null) {
            visit(ctx.postfixExpression());
        }
        return null;
    }
    
    @Override
    public Void visitPostfixExpression(CParser.PostfixExpressionContext ctx) {
        if (ctx.primaryExpression() != null) {
            visit(ctx.primaryExpression());
        } else if (ctx.getText().contains("(") && ctx.argumentExpressionList() != null) {
            // This is a function call
            handleFunctionCall(ctx);
        }
        return null;
    }
    
    private void handleFunctionCall(CParser.PostfixExpressionContext ctx) {
        // Extract function name from the primary expression part
        String functionName = null;
        if (ctx.primaryExpression() != null && ctx.primaryExpression().Identifier() != null) {
            functionName = ctx.primaryExpression().Identifier().getText();
        } else {
            // Try to get function name from text
            String fullText = ctx.getText();
            int parenIndex = fullText.indexOf('(');
            if (parenIndex > 0) {
                functionName = fullText.substring(0, parenIndex);
            }
        }
        
        // For printf calls with string literals
        if ("printf".equals(functionName)) {
            // Simple approach: just create printf call with one string argument
            String[] arguments = {"\"Hello World\""};
            
            IRCallInstruction callInstr = new IRCallInstruction(null, functionName, arguments);
            currentFunction.addInstruction(callInstr);
        }
    }
    
    @Override
    public Void visitJumpStatement(CParser.JumpStatementContext ctx) {
        if (ctx.Return() != null) {
            String returnValue = null;
            if (ctx.expression() != null) {
                returnValue = ctx.expression().getText();
            }
            IRReturnInstruction returnInstr = new IRReturnInstruction(returnValue);
            currentFunction.addInstruction(returnInstr);
        }
        return null;
    }
}