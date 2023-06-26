class CompilationEngine:
    """
    compiles a jack source file from a jack tokenizer into xml form in output_file
    """

    TERMINAL_TOKEN_TYPES = ["STRING_CONST", "INT_CONST", "IDENTIFIER", "SYMBOL"]
    TERMINAL_KEYWORDS = ["boolean", "class", "void", "int"]
    CLASS_VAR_DEC_TOKENS = ["static", "field"]
    SUBROUTINE_TOKENS = ["function", "method", "constructor"]
    STATEMENT_TOKENS = ["do", "let", "while", "return", "if"]
    STARTING_TOKENS = {
        "var_dec": ["var"],
        "parameter_list": ["("],
        "subroutine_body": ["{"],
        "expression_list": ["("],
        "expression": ["=", "[", "("],
    }
    TERMINATING_TOKENS = {
        "class": ["}"],
        "class_var_dec": [";"],
        "subroutine": ["}"],
        "parameter_list": [")"],
        "expression_list": [")"],
        "statements": ["}"],
        "do": [";"],
        "let": [";"],
        "while": ["}"],
        "if": ["}"],
        "var_dec": [";"],
        "return": [";"],
        "expression": [";", ")", "]", ","],
    }
    OPERATORS = ["+", "-", "*", "/", "&amp;", "|", "&lt;", "&gt;", "="]
    UNARY_OPERATORS = ["-", "~"]

    def __init__(self, tokenizer, output_file):
        self.tokenizer = tokenizer
        self.output_file = output_file

    def compile_class(self):
        self.tokenizer.setup()
        self._write_current_outer_tag(body="class")

        while self.tokenizer.hasMoreTokens():
            print(self.tokenizer.advance())
            if self._terminal_token_type() or self._terminal_keyword():
                self._write_current_terminal_token()
            elif self.tokenizer.current_token in self.CLASS_VAR_DEC_TOKENS:
                self.compile_class_var_dec()
            elif self.tokenizer.current_token in self.SUBROUTINE_TOKENS:
                self.compile_subroutine()

        self._write_current_outer_tag(body="/class")

        return True

    def compile_class_var_dec(self):
        """
        example: field int x;
        """
        self._write_current_outer_tag(body="classVarDec")
        self._write_current_terminal_token()

        while self._not_terminal_token_for('class_var_dec'):
            self.tokenizer.advance()
            self._write_current_terminal_token()

        self._write_current_outer_tag(body="/classVarDec")

    def compile_subroutine(self):
        """
        example: method/constructor/function void dispose() { ...
        """
        self._write_current_outer_tag(body="subroutineDec")
        self._write_current_terminal_token()

        while self._not_terminal_token_for('subroutine'):
            self.tokenizer.advance()

            if self._starting_token_for('parameter_list'):
                self.compile_parameter_list()
            elif self._starting_token_for('subroutine_body'):
                self.compile_subroutine_body()
            else:
                self._write_current_terminal_token()

        self._write_current_outer_tag(body="/subroutineDec")

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

    def _write_current_outer_tag(self, body):
        self.output_file.write("<{}>\n".format(body))

    def _write_current_terminal_token(self):
        if self.tokenizer.current_token_type == "STRING_CONST":
            tag_name = "stringConstant"
        elif self.tokenizer.current_token_type == "INT_CONST":
            tag_name = "integerConstant"
        else:
            tag_name = self.tokenizer.current_token_type.lower()

        if self.tokenizer.current_token_type == "STRING_CONST":
            value = self.tokenizer.current_token.replace("\"", "")
        else:
            value = self.tokenizer.current_token

        self.output_file.write(
            "<{}> {} </{}>\n".format(
                tag_name,
                value,
                tag_name
            )
        )

    def _terminal_token_type(self):
        # print("token type:", self.tokenizer.current_token_type())
        return self.tokenizer.current_token_type in self.TERMINAL_TOKEN_TYPES

    def _terminal_keyword(self):
        return self.tokenizer.current_token in self.TERMINAL_KEYWORDS
    
    def _not_terminal_token_for(self, keyword_token, position='current'):
        if position == 'current':
            return not self.tokenizer.current_token in self.TERMINATING_TOKENS[keyword_token]
        elif position == 'next':
            return not self.tokenizer.next_token in self.TERMINATING_TOKENS[keyword_token]
