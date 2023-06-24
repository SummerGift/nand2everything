class CompilationEngine():
    """
    compiles a jack source file from a jack tokenizer into xml form in output_file
    """

    TERMINAL_TOKEN_TYPES = [ "STRING_CONST", "INT_CONST", "IDENTIFIER", "SYMBOL"]
    TERMINAL_KEYWORDS = [ "boolean", "class", "void", "int" ]
    CLASS_VAR_DEC_TOKENS = [ "static", "field" ]
    SUBROUTINE_TOKENS = [ "function", "method", "constructor" ]
    STATEMENT_TOKENS = [ 'do', 'let', 'while', 'return', 'if' ]
    STARTING_TOKENS = {
        'var_dec': ['var'],
        'parameter_list': ['('],
        'subroutine_body': ['{'],
        'expression_list': ['('],
        'expression': ['=', '[', '(']
    }
    TERMINATING_TOKENS = {
        'class': ['}'],
        'class_var_dec': [';'],
        'subroutine': ['}'],
        'parameter_list': [')'],
        'expression_list': [')'],
        'statements': ['}'],
        'do': [';'],
        'let': [';'],
        'while': ['}'],
        'if': ['}'],
        'var_dec': [';'],
        'return': [';'],
        'expression': [';', ')', ']', ',']
    }
    OPERATORS = [
        '+',
        '-',
        '*',
        '/',
        '&amp;',
        '|',
        '&lt;',
        '&gt;',
        '='
    ]
    UNARY_OPERATORS = [ '-', '~' ]

    def __init__(self, tokenizer, output_file):
        self.tokenizer = tokenizer
        self.output_file = output_file

    def compile_class(self):
        return True

    def compile_class_var_dec(self):
        return True

    def compile_subroutine(self):
        return True

    def compile_parameter_list(self):
        return True

    def compile_var_dec(self):
        return True

    def compile_statements(self):
        return True

    def compile_do(self):
        return True

    def compile_let(self):
        return True

    def compile_while(self):
        return True

    def compile_return(self):
        return True

    def compile_if(self):
        return True

    def compile_expression(self):
        return True

    def compile_term(self):
        return True

    def compile_expression_list(self):
        return True


