class VMWriter():

    ARITHMETIC_LOGICAL_OPERATORS = {
        '+': 'add',
        '-': 'sub',
        '=': 'eq',
        '>': 'gt',
        '<': 'lt',
        '&': 'and',
        '|': 'or'
    }

    UNARY_OPERATORS = {
        '-': "neg",
        '~': 'not'
    }

    def __init__(self, output_file):
        """prepares a new vm file for writing"""
        self.output_file = open(output_file, 'w+')

    def write_push(self, segment, index):
        """
        write a vm push command
        segments: CONST, ARG, LOCAL, STATIC, THIS, THAT, POINTER, TEMP 
        and the index is int
        """
        self.output_file.write("push {0} {1}\n".format(segment, index))

    def write_pop(self, segment, index):
        """
        write a vm pop command
        segments: CONST, ARG, LOCAL, STATIC, THIS, THAT, POINTER, TEMP,
        and the index is int
        """
        self.output_file.write("pop {0} {1}\n".format(segment, index))
    
    def write_arithmetic(self, command):
        """
        write a vm arithmetic-logic command, such as ADD, SUB, EQ, GT, LT, AND, OR
        """
        self.output_file.write(
            "{0}\n".format(self.ARITHMETIC_LOGICAL_OPERATORS[command])
        )

    def write_unary(self, command):
        """
        writes avm unary command, such as NEG, NOT
        """
        self.output_file.write("{}\n".format(self.UNARY_OPERATORS[command]))

    def write_label(self, label):
        """writes a vm label command, label is a string"""
        self.output_file.write("label {}\n".format(label))

    def write_goto(self, label):
        """writes a vm goto command, label is a string"""
        self.output_file.write("goto {}\n".format(label))

    def write_ifgoto(self, label):
        """writes a vm ifgoto command, label is a string"""
        self.output_file.write('if-goto {}\n'.format(label))

    def write_call(self, name, num_args):
        """
        writes a vm function call command
        name: string, name of the subroutine
        num_args: int, number of argument to subroutine
        """
        self.output_file.write("call {} {}\n".format(name, num_args))

    def write_function(self, name, num_locals):
        """writes a vm function command"""
        self.output_file.write("function {} {}\n".format(name, num_locals))

    def write_return(self):
        """
        writes a return command
        """
        self.output_file.write("return\n")