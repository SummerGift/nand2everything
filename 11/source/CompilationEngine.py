from SymbolTable import SymbolTable
from VMWriter import VMWriter
from Operator import Operator
from LabelCounter import LabelCounter
import inspect

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
        "conditional": ["if", "else"],
    }
    TERMINATING_TOKENS = {
        "class": ["}"],
        "class_var_dec": [";"],
        "subroutine": ["}"],
        "parameter_list": [")"],
        "expression_list": [')'],
        "statements": ["}"],
        "do": [";"],
        "let": [";"],
        "while": ["}"],
        "if": ["}"],
        "var_dec": [";"],
        "return": [";"],
        'expression': [';', ')', ']', ',']
    }
    OPERATORS = ["+", "-", "*", "/", "&amp;", "|", "&lt;", "&gt;", "="]
    UNARY_OPERATORS = ["-", "~"]

    TOKENS_NEED_LABELS = ['if', 'while']

    def __init__(self, tokenizer, output_file_name):
        self.tokenizer = tokenizer
        self.class_symbol_table = SymbolTable()
        self.subroutine_symbol_table = SymbolTable()
        self.vm_writer = VMWriter(output_file_name)
        self.class_name = None
        self.label_counter = LabelCounter(labels=self.TOKENS_NEED_LABELS)

    def compile_class(self):
        self.tokenizer.setup()

        # skip useless token until class key word
        while not self.tokenizer.class_token_reached():
            self.tokenizer.advance()

        # and then get the name of class
        self.class_name = self.tokenizer.next_token_instance.text

        if self.class_name is not None:
            print("Start to compile class {}".format(self.class_name))
        else:
            print("Error: class_name is not initialized.")

        while self.tokenizer.hasMoreTokens():
            self.tokenizer.advance()

            if self.tokenizer.current_token_instance.text in self.CLASS_VAR_DEC_TOKENS:
                # using class level variable to generate symbol table
                self.compile_class_var_dec()
            elif self.tokenizer.current_token_instance.text in self.SUBROUTINE_TOKENS:
                # handle subroutine
                self.compile_subroutine()

    def compile_class_var_dec(self):
        """
        example: field int x;
        """

        symbol_kind = self.tokenizer.keyword()

        # getting symbol type after getting symbol kind, the kind is field and the type is int
        self.tokenizer.advance()
        symbol_type = self.tokenizer.keyword()

        if symbol_type is None:
            if self.tokenizer.identifier():
                symbol_type = self.tokenizer.identifier()

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

        print("Class level symbol table is parsing:")
        print(self.class_symbol_table.dumps())
        
    def compile_subroutine(self):
        """
        example: method/constructor/function void dispose() { ...
        """

        subroutine_type = self.tokenizer.current_token_instance.text

        # starts a new subroutine symbol table
        self.subroutine_symbol_table.reset()

        self.tokenizer.advance()
        self.tokenizer.advance()
        subroutine_name = self.tokenizer.current_token_instance.text

        print(f"Start to compile {subroutine_name}, subroutine type is {subroutine_type}")

        # compile parameter list
        self.tokenizer.advance()
        arg_num = self.compile_parameter_list()

        # skip to compile subroutine body
        self.tokenizer.advance()
        self.tokenizer.advance()

        print(f"start to compile subroutine variable")

        # handle varible and build a subroutine level symbol table
        num_locals = 0
        while self._starting_token_for("var_dec"):
            num_locals += self.compile_subroutine_var_dec()
            self.tokenizer.advance()

        print(f"compile subroutine variable done, the local vars number is {num_locals}")
        print("Subroutine level symbol table is:", self.subroutine_symbol_table.dumps())

        # write function command
        self.vm_writer.write_function(
            name="{0}.{1}".format(self.class_name, subroutine_name),
            num_locals=num_locals,
        )

        # alloc memory for the obj to be constructed, pop the base address to pointer 0
        if subroutine_type == "constructor":
            arg_num = self.class_symbol_table.var_count("field")
            self.vm_writer.write_push("constant", arg_num)
            self.vm_writer.write_call("Memory.alloc", 1)
            self.vm_writer.write_pop("pointer", 0)
        elif subroutine_type == "method":
            self.vm_writer.write_push("argument", 0)
            self.vm_writer.write_pop("pointer", 0)

        while self._not_terminal_token_for("subroutine"):
            self.compile_statements()

        # need push the base of object and return

    def compile_parameter_list(self):
        """
        example: dispose(int a, int b)
        """

        arg_num = 0

        while self._not_terminal_token_for(keyword_token="parameter_list"):
            self.tokenizer.advance()

            # get argument symbol table
            if self.tokenizer.next_token_instance.is_identifier():
                symbol_kind = self.SYMBOL_KINDS['parameter_list']
                symbol_type = self.tokenizer.current_token_instance.text
                symbol_name = self.tokenizer.next_token_instance.text
                self.subroutine_symbol_table.define(
                    kind = symbol_kind, 
                    name = symbol_name,
                    symbol_type=symbol_type,
                )
                arg_num += 1

        return arg_num

    def compile_statements(self):
        """
        call correct statement
        """

        statement_methods = {
            "do": self.compile_do,
            "let": self.compile_let,
            "if": self.compile_if,
            "while": self.compile_while,
            "return": self.compile_return,
        }

        while self._not_terminal_token_for("subroutine"):
            if self.tokenizer.current_token_instance.is_statement_token():
                statement_type = self.tokenizer.current_token_instance.text
                statement_methods[statement_type]()

            self.tokenizer.advance()

    def compile_subroutine_var_dec(self):
        """
        example: var int b;
        """

        # skip the var keyword
        self.tokenizer.advance()
        # get symbol type
        symbol_type = self.tokenizer.current_token_instance.text
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

    def compile_do(self):
        """
        example: do Output.printInt(1 + (2 * 3));
        """

        # after do keyword
        self.tokenizer.advance()

        # get caller name
        caller_name = self.tokenizer.current_token_instance.text

        # look up the caller name in symbol table.
        # I think the object must be stored in the class level symbol table for now
        # maybe it's a field type  
        symbol = self._find_symbol_in_symbol_tables(symbol_name=caller_name)

        # skip .
        self.tokenizer.advance()

        # get subroutine name
        self.tokenizer.advance()
        subroutine_name = self.tokenizer.current_token_instance.text

        if symbol:
            # since it's a class level symbol, field type, we should access it using this pointer
            segment = 'this'
            index = symbol['index']
            symbol_type = symbol['type']
            self.vm_writer.write_push(segment=segment, index=index)
        else:
            # if symbol is None, means it's a user defined method not a variable
            symbol_type = caller_name

        subroutine_call_name = symbol_type + "." + subroutine_name

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
        example: let value = Memory.peek(8000); 
        # TODO handle array assignment
        """

        print("Start to compile let statement")

        # get the symbol from symbol table to store value
        self.tokenizer.advance()
        symbol_name = self.tokenizer.current_token_instance.text
        symbol = self._find_symbol_in_symbol_tables(symbol_name=symbol_name)

        # advance to '='
        while not self.tokenizer.current_token_instance.text == '=':
            self.tokenizer.advance()

        while self._not_terminal_token_for('let'):
            # compile expression after '='
            self.tokenizer.advance()
            self.compile_expression()

        print("the symbol's kind is", symbol['kind'])

        if symbol['kind'] == "field":
            segment = "this"
        else:
            segment = symbol["kind"]

        # pop the expression value to the symbol's location
        self.vm_writer.write_pop(segment=segment, index=symbol['index'])

    def compile_if(self):
        """
        example: if (True) { ... } else { ... }
        """

        print("start to compile if statement")

        # advance to expression start
        self.tokenizer.advance()
        self.tokenizer.advance()

        # compile expression in ()
        self.compile_expression()

        # write if
        self.vm_writer.write_ifgoto(label="IF_TRUE{}".format(self.label_counter.get('if')))
        # write goto if false (else)
        self.vm_writer.write_goto(label='IF_FALSE{}'.format(self.label_counter.get('if')))
        # write if label
        self.vm_writer.write_label(label='IF_TRUE{}'.format(self.label_counter.get('if')))

        self.compile_conditional_body()

        print("Finished compiling conditional body")
        print("Current token text: ", self.tokenizer.current_token_instance.text)

        # handle else 
        if self._starting_token_for(keyword_token='conditional', position='next'):
            self.tokenizer.advance()

            print("Begin to handle else branch")
            
            # goto if end if this path isn't hit
            self.vm_writer.write_goto(
                label='IF_FALSE{}'.format(self.label_counter.get('if'))
            )

            # if false, hit the else path
            self.vm_writer.write_label(
                label='IF_FALSE{}'.format(self.label_counter.get('if'))
            )

            # compile else body
            self.compile_conditional_body()

            # define IF_END
            self.vm_writer.write_label(
                label='IF_END{}'.format(self.label_counter.get('if'))
            )
        else:
            # no else present, go to the false label directly
            self.vm_writer.write_label(
                label='IF_FALSE{}'.format(self.label_counter.get('if'))
            )

        self.label_counter.increment('if')

    def compile_conditional_body(self):
        while self._not_terminal_token_for('if'):
            self.tokenizer.advance()

            if self._statement_token():
                if self.tokenizer.current_token_instance.is_if():
                    # add ifto labels count
                    self.label_counter.increment('if')
                    # compile nested if
                    self.compile_statements()
                    # subtract for exiting nesting
                    self.label_counter.decrement('if')
                else:
                    self.compile_statements()

    def compile_while(self):
        """
        example: while (x > 0) { ... }
        # 'while' '(' expression ')' '{' statements '}'
        """

        # write while label
        self.vm_writer.write_label(
            label='WHILE_EXP{}'.format(self.label_counter.get('while'))
        )

        # advance to expression start (
        self.tokenizer.advance()
        self.tokenizer.advance()

        # compile expression in ()
        self.compile_expression()

        # test the expression, if false, go to the WHILE_END label
        self.vm_writer.write_unary(command='~')
        self.vm_writer.write_ifgoto(label='WHILE_END{}'.format(self.label_counter.get('while')))
        
        while self._not_terminal_token_for('while'):
            self.tokenizer.advance()

            if self._statement_token():
                self.compile_statements()

        # write goto WHILE_EXP
        self.vm_writer.write_goto(
            label='WHILE_EXP{}'.format(self.label_counter.get('while'))
        )

        # write WHILE_END label
        self.vm_writer.write_label(
            label='WHILE_END{}'.format(self.label_counter.get('while'))
        )

        # increment the label for while
        self.label_counter.increment('while')

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

            # handle subroutine call
            if self._subroutine_call():
                self.compile_subroutine_call()

            # handle const int number
            elif self.tokenizer.current_token_instance.text.isdigit():
                self.vm_writer.write_push(
                    segment="constant",
                    index=self.tokenizer.current_token_instance.text)
            
            # return the pointer of the object just created
            elif self.tokenizer.current_token_instance.text == "this":
                self.vm_writer.write_push(segment='pointer', index='0')

            # push symbol in the symbol table
            elif self.tokenizer.current_token_instance.is_identifier():
                self.push_identifier()
            # need to distinguish the difference between sub and neg
            elif self.tokenizer.current_token_instance.is_operator() and not self.tokenizer.is_likely_unary_operator():
                ops.insert(0, Operator(token=self.tokenizer.current_token_instance.text, category='bi'))
            elif self.tokenizer.current_token_instance.is_unary_operator():
                ops.insert(0, Operator(token=self.tokenizer.current_token_instance.text, category='unary'))
            elif self._starting_token_for('expression'):
                # skif the start token (
                self.tokenizer.advance()
                self.compile_expression()

            elif self.tokenizer.null():
                self.vm_writer.write_push(segment='constant', index=0)

            # handle boolean type
            elif self.tokenizer.current_token_instance.is_boolean():
                self.vm_writer.write_push(segment='constant', index='0')
                if self.tokenizer.current_token_instance.text == 'true':
                    self.vm_writer.write_unary(command='~')

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
            subroutine_name += self.tokenizer.current_token_instance.text
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

    def _terminal_token_type(self):
        # print("token type:", self.tokenizer.current_token_type())
        return self.tokenizer.current_token_type in self.TERMINAL_TOKEN_TYPES

    def _terminal_keyword(self):
        return self.tokenizer.current_token_instance.text in self.TERMINAL_KEYWORDS

    def _not_terminal_token_for(self, keyword_token, position="current"):
        if position == "current":
            if keyword_token == "expression":
                return (not self.tokenizer.current_token_instance.text in [';', ',', ']', ')'])
            else:
                return (not self.tokenizer.current_token_instance.text in self.TERMINATING_TOKENS[keyword_token]) 
        elif position == "next":
            return (not self.tokenizer.next_token_instance.text in self.TERMINATING_TOKENS[keyword_token])

    def _starting_token_for(self, keyword_token, position="current"):
        if position == "current":
            return self.tokenizer.current_token_instance.text in self.STARTING_TOKENS[keyword_token]
        elif position == "next":
            return self.tokenizer.next_token_instance in self.STARTING_TOKENS[keyword_token]

    def _statement_token(self):
        return self.tokenizer.current_token_instance.text in self.STATEMENT_TOKENS
    
    def _next_token_is_negative_unary_operator(self):
        return self.tokenizer.next_token_instance.text == "-"

    def _operator_token(self, position='current'):
        if position == 'current':
            return self.tokenizer.current_token_instance.text in self.OPERATORS
        elif position == 'next':
            return self.tokenizer.next_token_instance.text in self.OPERATORS

    def _not_terminal_condition_for_term(self):
        return self._not_terminal_token_for('expression')

    def _next_token_is_operation_not_in_expression(self):
        return self._operator_token(position='next') and not self._starting_token_for('expression')
    
    def _another_expression_coming(self):
        return self.tokenizer.current_token_instance.text == ","
    
    def _find_symbol_in_symbol_tables(self, symbol_name):
        # search in subroutine symbol first and then search in class symbol table
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
        return self.tokenizer.identifier() and self.tokenizer.next_token_instance.is_subroutine_call_delimiter()

    def _part_of_expression_list(self):
        return self.tokenizer.part_of_expression_list()
    
    def push_identifier(self):
        """
        Handles identifiers in the code
        """
        symbol_name = self.tokenizer.identifier()
        symbol = self._find_symbol_in_symbol_tables(symbol_name=symbol_name)

        print(f"Symbol name: {symbol_name}, Symbol kind: {symbol['kind']}")
        
        if symbol:
            if symbol['kind'] == 'field':
                self.vm_writer.write_push(segment='this', index=symbol['index'])
            else:
                self.vm_writer.write_push(segment=symbol['kind'], index=symbol['index'])
        else:
            raise Exception(f"Identifier {symbol_name} not found in symbol table.")
    