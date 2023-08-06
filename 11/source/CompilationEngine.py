from SymbolTable import SymbolTable
from VMWriter import VMWriter
from Operator import Operator

class CompilationEngine:
    """
    compiles a jack source file from a jack tokenizer into xml form in output_file
    """

    TERMINAL_TOKEN_TYPES = ["STRING_CONST", "INT_CONST", "IDENTIFIER", "SYMBOL"]
    TERMINAL_KEYWORDS = ["boolean", "class", "void", "int"]
    CLASS_VAR_DEC_TOKENS = ["static", "field"]
    SUBROUTINE_TOKENS = ["function", "method", "constructor"]
    STATEMENT_TOKENS = ["do", "let", "while", "return", "if"]

    SYMBOL_KINDS = {
        'parameter_list': 'argument',
        'var_dec': 'local'
    }
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

    def __init__(self, tokenizer, output_file_name):
        self.tokenizer = tokenizer
        self.class_symbol_table = SymbolTable()
        self.subroutine_symbol_table = SymbolTable()
        self.vm_writer = VMWriter(output_file_name)
        self.class_name = None

    def compile_class(self):
        self.tokenizer.setup()

        # skip useless token until class key word
        while not self.tokenizer.class_token_reached():
            self.tokenizer.advance()

        print("compile_class")

        # and then get the name of class
        self.class_name = self.tokenizer.next_token_instance.text

        while self.tokenizer.hasMoreTokens():
            self.tokenizer.advance()

            if self.tokenizer.current_token in self.CLASS_VAR_DEC_TOKENS:
                # using class level variable to generate symbol table
                self.compile_class_var_dec()
            elif self.tokenizer.current_token in self.SUBROUTINE_TOKENS:
                # handle subroutine
                self.compile_subroutine()

        print(self.class_symbol_table.dumps())

        return True

    def compile_class_var_dec(self):
        """
        example: field int x;
        """

        symbol_kind = self.tokenizer.keyword()

        # getting symbol type after getting symbol kind, the kind is field and the type is int
        self.tokenizer.advance()
        symbol_type = self.tokenizer.keyword()

        while self._not_terminal_token_for("class_var_dec"):
            self.tokenizer.advance()

            if self.tokenizer.identifier():
                symbol_name = self.tokenizer.identifier()
                # get class level symbol table
                self.class_symbol_table.define(
                    name=symbol_name,
                    symbol_type=symbol_type,
                    kind=symbol_kind
                )

    def compile_subroutine(self):
        """
        example: method/constructor/function void dispose() { ...
        """

        # starts a new subroutine symbol table
        self.subroutine_symbol_table.reset()

        # gets the name of subroutine
        self.tokenizer.advance()
        self.tokenizer.advance()
        subroutine_name = self.tokenizer.current_token

        # compile parameter list
        self.tokenizer.advance()
        self.compile_parameter_list()

        print("compile_subroutine")

        # compile subroutine body
        self.tokenizer.advance()
        self.compile_subroutine_body(subroutine_name)

        print(self.subroutine_symbol_table.dumps())

    def compile_parameter_list(self):
        """
        example: dispose(int a, int b)
        """

        while self._not_terminal_token_for(keyword_token="parameter_list"):
            self.tokenizer.advance()

            # get argument symbol table
            if self.tokenizer.next_token_instance.is_identifier():
                symbol_kind = self.SYMBOL_KINDS['parameter_list']
                symbol_type = self.tokenizer.current_token.text
                symbol_name = self.tokenizer.next_token_instance.text
                self.subroutine_symbol_table.define(
                    kind = symbol_kind, 
                    name = symbol_name,
                    symbol_type=symbol_type,
                )

    def compile_subroutine_body(self, subroutine_name):
        """
        example: do Output.printInt(1 + (2 * 3));
        """

        self.tokenizer.advance()

        # get the num of locals
        num_locals = 0
        while self._starting_token_for("var_dec"):
            num_locals += self.compile_var_dec()
            self.tokenizer.advance()

        print("num of locals: ", num_locals)

        # write function command
        self.vm_writer.write_function(
            name="{0}.{1}".format(self.class_name, subroutine_name),
            num_locals=num_locals,
        )

        while self._not_terminal_token_for("subroutine"):
            self.compile_statements()

    def compile_var_dec(self):
        """
        example: var int b;
        """

        # skip the var keyword
        self.tokenizer.advance()
        # get symbol type
        symbol_type = self.tokenizer.current_token
        # count the num of vars
        vars_count = 0

        while self._not_terminal_token_for("var_dec"):
            self.tokenizer.advance()

            if self.tokenizer.identifier():
                vars_count += 1
                symbol_kind = self.SYMBOL_KINDS['var_dec']
                symbol_name = self.tokenizer.identifier()
                self.subroutine_symbol_table.define(
                    name=symbol_name,
                    kind=symbol_kind,
                    symbol_type=symbol_type,
                )

        return vars_count

    def compile_statements(self):
        """
        call correct statement
        """

        statement_methods = {
            "if": self.compile_if,
            "do": self.compile_do,
            "let": self.compile_let,
            "while": self.compile_while,
            "return": self.compile_return,
        }

        while self._not_terminal_token_for("subroutine"):

            if self.tokenizer.current_token_instance.is_statement_token():
                statement_type = self.tokenizer.current_token_instance.text

                print("statement_type: ", statement_type)
                statement_methods[statement_type]()

            self.tokenizer.advance()

    def compile_if(self):
        """
        example: if (True) { ... } else { ... }
        """
        self._write_current_outer_tag(body="ifStatement")
        # write keyword if
        self._write_current_terminal_token()

        # advance to expression start
        self.tokenizer.advance()

        # compile expression in ()
        self.compile_expression()

        def not_terminate_func():
            return self._not_terminal_token_for("if")

        def condition_func():
            return self._statement_token()

        def do_something_special_func():
            return self.compile_statements()

        self.compile_statement_body(
            not_terminate_func, condition_func, do_something_special_func
        )

        # compile else
        if self.tokenizer.next_token == "else":
            # write closing {
            self._write_current_terminal_token()
            # past closing {
            self.tokenizer.advance()
            # write else
            self._write_current_terminal_token()
            # same as above
            self.compile_statement_body(
                not_terminate_func, condition_func, do_something_special_func
            )

        # write terminal token
        self._write_current_terminal_token()
        self._write_current_outer_tag(body="/ifStatement")

    def compile_do(self):
        """
        example: do Output.printInt(1 + (2 * 3));
        """

        # after do keyword
        self.tokenizer.advance()

        # get caller name
        caller_name = self.tokenizer.current_token_instance.text

        # look up the caller name in symbol table
        symbol = self._find_symbol_in_symbol_tables(symbol_name=caller_name)

        # skip .
        self.tokenizer.advance()

        # get subroutine name
        self.tokenizer.advance()
        subroutine_name = self.tokenizer.current_token_instance.text

        # if symbol is no none, means it's a user defined method
        if symbol:
            print("it's a user defined method\n")
        else:
            symbol_type = caller_name

        subroutine_call_name = symbol_type + "." + subroutine_name

        print(subroutine_call_name)

        # start to handle expression list
        self.tokenizer.advance()
        num_args = self.compile_expression_list()

        # if it's a method call
        if symbol:
            """object itself is also as a argument"""
            num_args += 1

        # write a function call
        self.vm_writer.write_call(name=subroutine_call_name, num_args=num_args)

        # pop the return value of previous function call that we don't need
        self.vm_writer.write_pop(segment='temp', index='0')

    def compile_let(self):
        """
        # 'let' varName ('[' expression ']')? '=' expression ';'
        example: let direction = 0;
        """
        self._write_current_outer_tag(body="letStatement")
        # write let keyword
        self._write_current_terminal_token()

        while self._not_terminal_token_for('let'):
            self.tokenizer.advance()

            if self._starting_token_for('expression'):
                self.compile_expression()
            else:
                self._write_current_terminal_token()

        self._write_current_outer_tag(body="/letStatement")

    def compile_while(self):
        """
        example: while (x > 0) { ... }
        # 'while' '(' expression ')' '{' statements '}'
        """
        self._write_current_outer_tag(body="whileStatement")
        # write keyword while
        self._write_current_terminal_token()

        # advance to expression start (
        self.tokenizer.advance()

        # compile expression in ()
        self.compile_expression()

        while self._not_terminal_token_for('while'):
            self.tokenizer.advance()

            if self._statement_token():
                self.compile_statements()
            else:
                self._write_current_terminal_token()
        # write terminal token
        self._write_current_terminal_token()

        self._write_current_outer_tag(body="/whileStatement")

    def compile_return(self):
        """
        example: return x; or return;
        """

        # if we need to return an experession
        if self._not_terminal_token_for(keyword_token='return', position='next'):
            self.compile_expression()
        else: 
            # return void, push a useless constant value
            self.vm_writer.write_push(segment='constant', index='0')
            self.tokenizer.advance()

        self.vm_writer.write_return()

    def compile_expression(self):
        """
        # term (op term)*
        such as 1 + (2 * 3)
        """

        ops = []

        while self._not_terminal_token_for('expression'):
            if self._subroutine_call():
                self.compile_subroutine_call()
                self.tokenizer.advance()
            elif self.tokenizer.current_token_instance.text.isdigit():
                self.vm_writer.write_push(
                    segment="constant",
                    index=self.tokenizer.current_token_instance.text)
            elif self.tokenizer.current_token_instance.is_operator():
                ops.insert(0, Operator(token=self.tokenizer.current_token_instance.text, category='bi'))
            elif self._starting_token_for('expression'):
                # skif the start token (
                self.tokenizer.advance()
                self.compile_expression()
            elif self.tokenizer.null():
                self.vm_writer.write_push(segment='constant', index=0)

            self.tokenizer.advance()

        for operator in ops:
            self.compile_op(operator)

    def compile_op(self, op):
        """example: +/-/* etc. """

        if op.unary():
            self.vm_writer.write_unary(command=op.token)
        elif op.multiplication():
            self.vm_writer.write_call(name='Math.multiply', num_args=2)
        elif op.division():
            self.vm_writer.write_call(name='Math.divide', num_args=2)
        else:
            self.vm_writer.write_arithmetic(command=op.token)

    def compile_subroutine_call(self):
        """ exp: Screen.setColor(true) """

        subroutine_name = ""

        while not self._starting_token_for('expression_list'):
            subroutine_name += self.tokenizer.current_token.text
            self.tokenizer.advance()

        # get the number of args
        num_args = self.compile_expression_list()

        # call subroutine after pushing argument
        self.vm_writer.write_call(name=subroutine_name, num_args=num_args)

    def compile_expression_list(self):
        # (expression (',' expression)* )?

        args_num = 0

        if self._empty_expression_list():
            return args_num

        # skip initial token (
        self.tokenizer.advance()

        while self._not_terminal_token_for('expression_list'):
            args_num += 1
            self.compile_expression()

            # current token could be , or ) to end expression list
            if self._another_expression_coming():
                self.tokenizer.advance()

        return args_num

    def compile_statement_body(self, not_terminate_func, condition_func, do_something_special_func):
        while not_terminate_func():
            self.tokenizer.advance()

            if condition_func():
                do_something_special_func()
            else:
                self._write_current_terminal_token()


    def _write_current_terminal_token(self):
        if self.tokenizer.current_token_type == "STRING_CONST":
            tag_name = "stringConstant"
        elif self.tokenizer.current_token_type == "INT_CONST":
            tag_name = "integerConstant"
        else:
            tag_name = self.tokenizer.current_token_type.lower()

        if self.tokenizer.current_token_type == "STRING_CONST":
            value = self.tokenizer.current_token.replace('"', "")
        else:
            value = self.tokenizer.current_token

    def _terminal_token_type(self):
        # print("token type:", self.tokenizer.current_token_type())
        return self.tokenizer.current_token_type in self.TERMINAL_TOKEN_TYPES

    def _terminal_keyword(self):
        return self.tokenizer.current_token in self.TERMINAL_KEYWORDS

    def _not_terminal_token_for(self, keyword_token, position="current"):
        if position == "current":
            return (
                not self.tokenizer.current_token
                in self.TERMINATING_TOKENS[keyword_token]
            )
        elif position == "next":
            return (
                not self.tokenizer.next_token in self.TERMINATING_TOKENS[keyword_token]
            )

    def _starting_token_for(self, keyword_token, position="current"):
        if position == "current":
            return self.tokenizer.current_token in self.STARTING_TOKENS[keyword_token]
        elif position == "next":
            return self.tokenizer.next_token in self.STARTING_TOKENS[keyword_token]

    def _statement_token(self):
        return self.tokenizer.current_token in self.STATEMENT_TOKENS
    
    def _next_token_is_negative_unary_operator(self):
        return self.tokenizer.next_token == "-"

    def _operator_token(self, position='current'):
        if position == 'current':
            return self.tokenizer.current_token in self.OPERATORS
        elif position == 'next':
            return self.tokenizer.next_token in self.OPERATORS

    def _not_terminal_condition_for_term(self):
        return self._not_terminal_token_for('expression')

    def _next_token_is_operation_not_in_expression(self):
        return self._operator_token(position='next') and not self._starting_token_for('expression')
    
    def _another_expression_coming(self):
        return self.tokenizer.current_token == ","
    
    def _find_symbol_in_symbol_tables(self, symbol_name):
        if self.subroutine_symbol_table.find_symbol_by_name(symbol_name):
            return self.subroutine_symbol_table.find_symbol_by_name(symbol_name)
        elif self.class_symbol_table.find_symbol_by_name(symbol_name):
            return self.class_symbol_table.find_symbol_by_name(symbol_name)

    def _empty_expression_list(self):
        # if the current token is start of a expression list and the next token is end of a expression,
        # means the expression list is empty
        return self._start_of_expression_list() and self._next_ends_expression_list()

    def _start_of_expression_list(self):
        return self.tokenizer.current_token_instance.text in self.STARTING_TOKENS['expression_list']

    def _next_ends_expression_list(self):
        return self.tokenizer.next_token_instance.text in self.TERMINATING_TOKENS['expression_list']
    
    def _subroutine_call(self):
        return self.tokenizer.identifier() and self.tokenizer.next_token.is_subroutine_call_delimiter()

    def _part_of_expression_list(self):
        return self.tokenizer.part_of_expression_list()