import os


class Parser:
    """Parser for assembler."""
    src_list = 0

    def __init__(self, file_pathname):
        self.pathname = file_pathname

    @staticmethod
    def code_cleanup(self, list_data):
        """handle useless characters in source code"""

        command_line_list = []
        for line in list_data:
            newline = line[:-1].replace(" ", "")

            if newline == "":
                continue
            if newline.startswith("//"):
                continue
            if newline.startswith("\n"):
                continue
            if newline.find("//") != -1:
                newline = newline[:newline.find("//")]

            command_line_list.append(newline)

        return command_line_list

    @staticmethod
    def handle_labels(self, command_line_list):
        pre_defined_label_dict = {
            "SP": "0",
            "LCL": "1",
            "ARG": "2",
            "THIS": "3",
            "THAT": "4",
            "R0": "0",
            "R1": "1",
            "R2": "2",
            "R3": "3",
            "R4": "4",
            "R5": "5",
            "R6": "6",
            "R7": "7",
            "R8": "8",
            "R9": "9",
            "R10": "10",
            "R11": "11",
            "R12": "12",
            "R13": "13",
            "R14": "14",
            "R15": "15",
            "SCREEN": "16384",
            "KBD": "24576"
        }


        code_label_dict = {}
        new_commandline = []
        line_index = 0
        for line in command_line_list:
            if line.find("(") == -1:
                line_index += 1
            else:
                code_label_dict[str(line[1:-1])] = str(line_index)
                continue

            new_commandline.append(line)

        symbol_label_dict = {}
        symbol_index = 16
        for line in command_line_list:
            if line.find("(") == -1 and line.find("@") != -1:
                line = line[1:]
                if line not in pre_defined_label_dict and line not in code_label_dict:
                    if not line[0].isdigit():
                        if str(line) not in symbol_label_dict:
                            symbol_label_dict[str(line)] = str(symbol_index)
                            symbol_index += 1

        print(symbol_label_dict)

        command_line_without_label_list = []
        for line in new_commandline:
            if line.startswith("@"):
                pre_defined_label = line[1:]
                if pre_defined_label in pre_defined_label_dict:
                    line = line.replace(pre_defined_label, pre_defined_label_dict[pre_defined_label])
                if pre_defined_label in code_label_dict:
                    line = line.replace(pre_defined_label, code_label_dict[pre_defined_label])
                if pre_defined_label in symbol_label_dict:
                    line = line.replace(pre_defined_label, symbol_label_dict[pre_defined_label])

            command_line_without_label_list.append(line)

        return command_line_without_label_list

    @staticmethod
    def handle_code_symbols(self, command_line_list):
        code_lines = self.handle_labels(self, command_line_list)

        # exit(0)
        for line in code_lines:
            print(line)

        # exit(0)
        return code_lines

    def code_pre_processing(self):
        with open(self.pathname) as f:
            list_data = list(f)

        after_cleanup = self.code_cleanup(self, list_data)
        return self.handle_code_symbols(self, after_cleanup)

    @staticmethod
    def get_command_type(single_cmd):
        if single_cmd[0] == "@":
            return "COMMAND_A"
        else:
            return "COMMAND_C"

    @staticmethod
    def handle_a_command(command_line):
        """handle A command
        :param command_line: a single command to translate
        :return: binary command
        """
        prefix = "0"

        print(command_line[1:])
        binary_addr = bin(int(command_line[1:]))[2:]

        binary_len = len(binary_addr)
        if binary_len < 15:
            binary_addr = (15 - binary_len) * "0" + binary_addr

        # construct A command with 0 and 15-bit address
        binary_str = prefix + binary_addr
        return binary_str

    @staticmethod
    def comp_str(self, command_line):
        if command_line.find("=") != -1:
            command = command_line[command_line.find("=") + 1:]

        if command_line.find(";") != -1:
            command = command_line[:command_line.find(";")]

        dest_command_dict = {
            "0": "0101010",
            "1": "0111111",
            "-1": "0111010",
            "D": "0001100",
            "A": "0110000",
            "!D": "0001101",
            "!A": "0110001",
            "-D": "0001111",
            "-A": "0110011",
            "D+1": "0011111",
            "A+1": "0110111",
            "D-1": "0001110",
            "A-1": "0110010",
            "D+A": "0000010",
            "D-A": "0010011",
            "A-D": "0000111",
            "D&A": "0000000",
            "D|A": "0010101",

            "M": "1110000",
            "!M": "1110001",
            "-M": "1110011",
            "M+1": "1110111",
            "M-1": "1110010",
            "D+M": "1000010",
            "D-M": "1010011",
            "M-D": "1000111",
            "D&M": "1000000",
            "D|M": "1010101",
        }

        return dest_command_dict[command]

    @staticmethod
    def dest_str(self, command_line):
        if command_line.find("=") == -1:
            return "000"
        else:
            command = command_line[:command_line.find("=")]

            dest_command_dict = {
                "M": "001",
                "D": "010",
                "MD": "011",
                "A": "100",
                "AM": "101",
                "AD": "110",
                "ADM": "111",
            }

            return dest_command_dict[command]

    @staticmethod
    def jump_str(self, command_line):
        if command_line.find(";") == -1:
            return "000"
        else:
            command = command_line[command_line.find(";") + 1:]

            jump_command_dict = {
                "JGT": "001",
                "JEQ": "010",
                "JGE": "011",
                "JLT": "100",
                "JNE": "101",
                "JLE": "110",
                "JMP": "111",
            }

            return jump_command_dict[command]

    def handle_c_command(self, command_line):
        """handle C command"""
        prefix = "111"

        comp_str = self.comp_str(self, command_line)
        dest_str = self.dest_str(self, command_line)
        jump_str = self.jump_str(self, command_line)

        binary_command = prefix + comp_str + dest_str + jump_str

        return binary_command

    def asm_line_translate(self, command_line):
        if self.get_command_type(command_line) == "COMMAND_A":
            return self.handle_a_command(command_line)

        if self.get_command_type(command_line) == "COMMAND_C":
            return self.handle_c_command(command_line)

    def asm_file_translate(self):
        command_lines = self.code_pre_processing()

        binary_list = []
        for line in command_lines:
            binary_list.append(self.asm_line_translate(line))
        return binary_list


def main():
    # if len(sys.argv) is not 2:
    #     print("Useage: python assembler xxx.asm.")
    #     return

    # get arg from command line
    # asm_file = sys.argv[1]
    # asm_file = "/Users/mac/work/nand2everything/material/projects/06/add/Add.asm"
    asm_file = r"G:\working_on\nand2everything\material\projects\06\add\Add.asm"
    # asm_file = "/Users/mac/work/nand2everything/material/projects/06/max/MaxL.asm"
    # asm_file = "/Users/mac/work/nand2everything/material/projects/06/max/Max.asm"
    # asm_file = r"G:\working_on\nand2everything\material\projects\06\max\Max.asm"
    # asm_file = "/Users/mac/work/nand2everything/material/projects/06/pong/PongL.asm"
    # asm_file = r"G:\working_on\nand2everything\material\projects\06\pong\Pong.asm"
    # asm_file = "/Users/mac/work/nand2everything/material/projects/06/rect/RectL.asm"
    # asm_file = r"G:\working_on\nand2everything\material\projects\06\rect\Rect.asm"

    parser = Parser(asm_file)
    binary_code = parser.asm_file_translate()

    file_dir = os.path.dirname(asm_file)
    filename = os.path.basename(asm_file)
    new_filename = filename + "_summer.hack"
    output_file = os.path.join(file_dir, new_filename)

    with open(output_file, 'w') as f:
        for line in binary_code:
            f.write(line + "\n")

    return True


if __name__ == "__main__":
    main()
